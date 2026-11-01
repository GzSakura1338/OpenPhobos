package org.phobos.module.movement;

import java.util.function.Consumer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.phobos.api.TextProvider;
import org.phobos.api.TimeGate.Normal;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.event.events.PlayerMoveEvent;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.render.texture.SkyboxTextureUtils;
import org.phobos.core.tick.TickUtils;
import org.phobos.core.inventory.InventoryUtils;
import org.phobos.event.Listener;
import org.phobos.event.SafeListener;
import org.phobos.event.TickMovementEvent2;
import org.phobos.module.Module;

public class NoSlowModule extends Module {
   public Setting<Boolean> booleanValueSetting;
   public Setting<Boolean> booleanValueSetting2;
   public static NoSlowModule noSlowModule;
   public Normal normal;
   public Setting<NoSlowModule.Mode> modeSetting;
   public int intValue;

   public boolean check() {
      if (mc.player == null) {
         return false;
      }

      if (this.booleanValueSetting2.getValue()) {
         return mc.player.getMainHandStack().getItem() instanceof SwordItem;
      }

      return switch ((NoSlowModule.Mode)this.modeSetting.getValue()) {
         case Skip -> !this.check2();
         case Watchdog -> mc.player.getItemUseTime() > 1 && mc.player.getItemUseTime() % 2 != 0;
         case Normal -> false;
         default -> true;
      };
   }

   public static NoSlowModule getNoSlowModule() {
      return noSlowModule;
   }

   public Setting<Boolean> getBooleanValueSetting2() {
      return this.booleanValueSetting2;
   }

   public boolean check2() {
      return mc.player == null ? false : this.testItemStack(mc.player.getMainHandStack()) && this.testItemStack(mc.player.getOffHandStack());
   }

   @Override
   public String nothing() {
      return String.valueOf(this.modeSetting.getValue()).replaceAll("_", " ");
   }

   public NoSlowModule(PhobosClient phobosClient) {
      super(phobosClient, "NoSlow", Categories.movement, "Disables consumables slow down");
      this.nativeInit(phobosClient);
   }

   public void nativeInit(final PhobosClient var1) {
      NoSlowModule.Mode var3 = NoSlowModule.Mode.Normal;
      EnumSetting var2 = new EnumSetting<>("Mode", var3);
      Setting var4 = this.resolveSetting2(var2);
      this.modeSetting = var4;
      BooleanSetting var5 = new BooleanSetting("Crawl");
      Setting var6 = this.resolveSetting2(var5);
      this.booleanValueSetting = var6;
      BooleanSetting var7 = new BooleanSetting("Sword");
      Setting var8 = this.resolveSetting2(var7);
      this.booleanValueSetting2 = var8;
      Normal var9 = new Normal();
      this.normal = var9;
      this.intValue = 0;
      var var10 = new Listener<TickMovementEvent2>() {
         public void handleObject2(TickMovementEvent2 tickMovementEvent2) {
            if (NoSlowModule.this.check()) {
               tickMovementEvent2.setCancelled(true);
            }
         }
      };
      this.handleListener(var10);
      var var11 = new SafeListener<PlayerMoveEvent>() {
         public void handleObject(
            PlayerMoveEvent playerMoveEvent,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            switch ((NoSlowModule.Mode)NoSlowModule.this.modeSetting.getValue()) {
               case Skip:
                  if (!clientPlayerEntity.isUsingItem() || NoSlowModule.this.check2()) {
                     return;
                  }

                  TickUtils.handleClientWorld(
                     clientWorld,
                     integer -> clientPlayerEntity.networkHandler
                        .sendPacket(
                           new PlayerInteractItemC2SPacket(
                              clientPlayerEntity.getActiveHand().equals(Hand.MAIN_HAND) ? Hand.OFF_HAND : Hand.MAIN_HAND,
                              integer,
                              var1.getRotationManager().computeFloat5(clientPlayerEntity.getYaw()),
                              var1.getRotationManager().computeFloat6(clientPlayerEntity.getPitch())
                           )
                        )
                  );
                  break;
               case Grim:
                  if (!clientPlayerEntity.isUsingItem()) {
                     return;
                  }

                  int i = PlayerActionUtils.value();
                  clientPlayerEntity.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(i % 8 + 1));
                  clientPlayerEntity.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(i));
                  break;
               case Grim_Switch:
                  if (clientPlayerEntity.isUsingItem()) {
                     Hand hand = clientPlayerEntity.getActiveHand();
                     Consumer<Packet<?>> consumer = SkyboxTextureUtils::handlePacket;
                     switch (hand) {
                        case MAIN_HAND:
                           consumer.accept(new PlayerInteractItemC2SPacket(Hand.OFF_HAND, 0, clientPlayerEntity.getYaw(), clientPlayerEntity.getPitch()));
                           break;
                        case OFF_HAND:
                           int i2 = PlayerActionUtils.value();
                           consumer.accept(new UpdateSelectedSlotC2SPacket(i2 % 8 + 1));
                           consumer.accept(new UpdateSelectedSlotC2SPacket(i2 % 7 + 2));
                           consumer.accept(new UpdateSelectedSlotC2SPacket(i2));
                     }
                  }
                  break;
               case Grim_Exempt:
                  NoSlowModule.this.intValue = clientPlayerEntity.isOnGround() ? 0 : NoSlowModule.this.intValue + 1;
                  if (clientPlayerEntity.isUsingItem()) {
                     playerMoveEvent.setDoubleValue(playerMoveEvent.getDoubleValue3() + 1.0E-14);
                  }
                  break;
               case None:
                  if (clientPlayerEntity.getItemUseTime() <= 2 || clientPlayerEntity.getItemUseTimeLeft() == 0) {
                     clientPlayerEntity.networkHandler
                        .sendPacket(new PlayerActionC2SPacket(Action.RELEASE_USE_ITEM, clientPlayerEntity.getBlockPos(), Direction.UP));
                  }
                  break;
               case Polar:
                  if (clientPlayerEntity.isUsingItem() && NoSlowModule.this.normal.testLong2(130L)) {
                     BlockPos.stream(clientPlayerEntity.getBoundingBox().offset(0.0, -1.0, 0.0))
                        .filter(blockPos -> !clientWorld.getBlockState(blockPos).isAir())
                        .forEach(
                           blockPos -> {
                              clientPlayerEntity.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, blockPos, Direction.UP));
                              clientPlayerEntity.networkHandler
                                 .sendPacket(new ClientCommandC2SPacket(clientPlayerEntity, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));
                              var1.getDelayedTaskManager()
                                 .handleRunnable(
                                    () -> clientPlayerEntity.networkHandler
                                       .sendPacket(new ClientCommandC2SPacket(clientPlayerEntity, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY)),
                                    1L
                                 );
                           }
                        );
                  }
            }
         }
      };
      this.handleListener(var11);
      noSlowModule = this;
   }

   public Setting<Boolean> getBooleanValueSetting() {
      return this.booleanValueSetting;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public Normal getNormal() {
      return this.normal;
   }

   public Setting<NoSlowModule.Mode> getModeSetting() {
      return this.modeSetting;
   }

   public boolean testItemStack(ItemStack itemStack) {
      return InventoryUtils.testItem(itemStack.getItem());
   }

   private enum Mode implements TextProvider {
      Normal("Vanilla NoSlow"),
      Grim("Spoofs use hand"),
      Grim_Switch("Switches hotbar slots"),
      Grim_Exempt("Spoofs use hand while shuffling hotbar"),
      Watchdog("Watchdog NoSlow"),
      Intave("Intave NoSlow"),
      Polar("Polar NoSlow"),
      Skip("Abuses lag compensation to gain 50% of walk speed"),
      None("No item no-slow");

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
