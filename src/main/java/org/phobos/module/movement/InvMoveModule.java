package org.phobos.module.movement;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import org.phobos.api.TextProvider;
import org.phobos.core.gui.screen.ScriptScreen;
import org.phobos.core.gui.screen.ConfigScreen;
import org.phobos.core.gui.screen.HudScreen;
import org.phobos.core.gui.screen.ClickGuiScreen;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.player.PlayerUtils;
import org.phobos.core.event.events.PacketEvent.Send;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.type.KeyboardMouseEnum;
import org.phobos.module.Module;

public class InvMoveModule extends Module {
   public boolean flag;
   public final Setting<Boolean> inventory = this.resolveSetting2(new BooleanSetting("Inventory", false));
   public final Setting<InvMoveModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", InvMoveModule.Mode.Vanilla));
   public int intValue;
   public final Setting<Integer> ticks = this.resolveSetting2(
      new NumberSetting<>("Ticks", 2, 0, 5)
         .resolveNumberSetting("Instant", 0)
         .resolveSetting4(this.mode, InvMoveModule.Mode.Polar)
         .resolveSetting7("Release ticks")
   );
   public final Queue<Packet<?>> packetQueue = new LinkedBlockingQueue<>();

   public InvMoveModule(PhobosClient phobosClient) {
      super(phobosClient, "InvMove", Categories.movement, "Allows you to move while in inventory");
      this.flag = false;
      this.intValue = 0;
      this.handleListener(
         new SafeListener<PlayerTickEvent>() {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (mc.currentScreen == null) {
                  if (InvMoveModule.this.flag && InvMoveModule.this.mode.testObject2(InvMoveModule.Mode.Polar) && !InvMoveModule.this.packetQueue.isEmpty()) {
                     if (!InvMoveModule.this.ticks.testObject2(0)) {
                        if (InvMoveModule.this.intValue > InvMoveModule.this.ticks.getValue()) {
                           clientPlayerEntity.networkHandler.sendPacket(InvMoveModule.this.packetQueue.remove());
                           if (InvMoveModule.this.packetQueue.isEmpty()) {
                              InvMoveModule.this.flag = false;
                              InvMoveModule.this.intValue = 0;
                              return;
                           }
                        }

                        InvMoveModule.this.intValue++;
                        mc.options.forwardKey.setPressed(false);
                        mc.options.backKey.setPressed(false);
                        mc.options.rightKey.setPressed(false);
                        mc.options.leftKey.setPressed(false);
                        return;
                     }

                     for (Packet<?> packet : InvMoveModule.this.packetQueue) {
                        clientPlayerEntity.networkHandler.sendPacket(packet);
                     }

                     InvMoveModule.this.flag = false;
                     InvMoveModule.this.intValue = 0;
                     InvMoveModule.this.packetQueue.clear();
                  }
               } else {
                  if (mc.currentScreen instanceof HandledScreen && InvMoveModule.this.inventory.getValue()
                     || mc.currentScreen instanceof ClickGuiScreen
                     || mc.currentScreen instanceof HudScreen
                     || mc.currentScreen instanceof ScriptScreen
                     || mc.currentScreen instanceof ConfigScreen) {
                     if (mc.currentScreen
                        .children()
                        .stream()
                        .anyMatch(element -> element instanceof TextFieldWidget textFieldWidget && textFieldWidget.isActive())) {
                        return;
                     }

                     InvMoveModule.this.run();
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Send<ClickSlotC2SPacket>>() {
            public void handleObject(
               Send<ClickSlotC2SPacket> send,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (PlayerUtils.testClientPlayerEntity(clientPlayerEntity)) {
                  switch ((InvMoveModule.Mode)InvMoveModule.this.mode.getValue()) {
                     case Polar:
                        switch (((ClickSlotC2SPacket)send.getPacket()).getActionType()) {
                           case PICKUP:
                           case PICKUP_ALL:
                              return;
                           default:
                              clientPlayerEntity.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(0));
                              return;
                        }
                     case Grim:
                        InvMoveModule.this.packetQueue.add(send.getPacket());
                        send.setCancelled(true);
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Send<CloseHandledScreenC2SPacket>>() {
            public void handleObject(
               Send<CloseHandledScreenC2SPacket> send,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (PlayerUtils.testClientPlayerEntity(clientPlayerEntity)) {
                  switch ((InvMoveModule.Mode)InvMoveModule.this.mode.getValue()) {
                     case Grim:
                        if (!InvMoveModule.this.packetQueue.isEmpty()) {
                           InvMoveModule.this.packetQueue.add(send.getPacket());
                           send.setCancelled(true);
                           InvMoveModule.this.flag = true;
                        }
                  }
               }
            }
         }
      );
   }

   public void run() {
      mc.options
         .rightKey
         .setPressed(this.getPhobosClient().getBindManager().testKeyboardMouseEnum(KeyboardMouseEnum.Keyboard, 68) || PlayerUtils.flag4 && PlayerUtils.flag3);
      mc.options
         .leftKey
         .setPressed(this.getPhobosClient().getBindManager().testKeyboardMouseEnum(KeyboardMouseEnum.Keyboard, 65) || PlayerUtils.flag4 && PlayerUtils.flag);
      mc.options.jumpKey.setPressed(this.getPhobosClient().getBindManager().testKeyboardMouseEnum(KeyboardMouseEnum.Keyboard, 32));
      mc.options
         .forwardKey
         .setPressed(this.getPhobosClient().getBindManager().testKeyboardMouseEnum(KeyboardMouseEnum.Keyboard, 87) || PlayerUtils.flag4 && PlayerUtils.flag2);
      mc.options
         .backKey
         .setPressed(this.getPhobosClient().getBindManager().testKeyboardMouseEnum(KeyboardMouseEnum.Keyboard, 83) || PlayerUtils.flag4 && PlayerUtils.flag5);
      mc.options.sprintKey.setPressed(this.getPhobosClient().getBindManager().testKeyboardMouseEnum(KeyboardMouseEnum.Keyboard, 341));
   }

   @Override
   public void run4() {
      mc.options.rightKey.setPressed(false);
      mc.options.leftKey.setPressed(false);
      mc.options.jumpKey.setPressed(false);
      mc.options.forwardKey.setPressed(false);
      mc.options.backKey.setPressed(false);
      mc.options.sprintKey.setPressed(false);
   }

   private enum Mode implements TextProvider {
      Vanilla("Vanilla inventory move"),
      Grim("Closes inventory after a click"),
      Polar("Delays all clicks, releases after closing");

      public final String text;

      Mode(final String description) {
         this.text = description;
      }

      @Override
      public String getText() {
         return this.text;
      }
   }
}
