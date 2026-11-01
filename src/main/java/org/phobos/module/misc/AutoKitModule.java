package org.phobos.module.misc;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.StringSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.task.TaskScheduler;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.event.AutoKitPostSendClientStatusListener;
import org.phobos.event.AutoKitTickListener;
import org.phobos.event.AutoKitPostReceiveGameJoinListener;
import org.phobos.event.AutoKitPostReceivePlayerPositionLookListener;
import org.phobos.module.Module;

public class AutoKitModule extends Module {
   public boolean flag;
   public final Setting<AutoKitModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", AutoKitModule.Mode.Always));
   public final TimeGate.Normal normal;
   public final Setting<String> kit;
   public final TimeGate.Normal normal2;
   public final Setting<Boolean> kill;
   public final Setting<Long> delay = this.resolveSetting2(
      new LongNumberSetting("Delay", 500L, 0L, 5000L, true).resolveSetting3(this.resolveVisibility2(this.mode, AutoKitModule.Mode.Always))
   );

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   public boolean check() {
      return this.mode.getValue().equals(AutoKitModule.Mode.Auto);
   }

   @Override
   public void run3() {
      this.flag = false;
      if (mc.player != null) {
         this.handleClientPlayerEntity(mc.player, false);
      }
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, boolean flag) {
      switch ((AutoKitModule.Mode)this.mode.getValue()) {
         case Always:
            if (clientPlayerEntity.networkHandler == null) {
               return;
            }

            clientPlayerEntity.networkHandler.sendChatCommand(this.kit.getValue());
            this.flag = false;
            if (flag && this.kill.getValue() && this.normal2.testLong(10000L)) {
               TaskScheduler taskScheduler = new TaskScheduler();
               taskScheduler.handleLong2(
                  1000L,
                  () -> {
                     if (this.normal2.testLong(10000L)
                        && clientPlayerEntity != null
                        && clientPlayerEntity.networkHandler != null
                        && mc.getNetworkHandler() != null) {
                        clientPlayerEntity.networkHandler.sendChatCommand("kill");
                        clientPlayerEntity.networkHandler
                           .sendPacket(new ClientStatusC2SPacket(net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket.Mode.PERFORM_RESPAWN));
                        this.normal2.run();
                     }

                     this.phobosClient.getEventBus().handleObject4(taskScheduler);
                  }
               );
               this.phobosClient.getEventBus().handleObject(taskScheduler);
            }
            break;
         case Auto:
            clientPlayerEntity.networkHandler.sendChatCommand(this.kit.getValue());
      }
   }

   public AutoKitModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoKit", Categories.misc, "Automatically gives you a kit");
      this.kit = this.resolveSetting2(new StringSetting("Kit", "kit 22"));
      this.kill = this.resolveSetting2(new BooleanSetting("Kill").resolveSetting4(this.mode, AutoKitModule.Mode.Auto));
      this.normal2 = new TimeGate.Normal();
      this.normal = new TimeGate.Normal();
      this.flag = false;
      this.handleListener(new AutoKitPostSendClientStatusListener(this));
      this.handleListener(new AutoKitPostReceiveGameJoinListener(this));
      this.handleListener(new AutoKitPostReceivePlayerPositionLookListener(this));
      this.handleListener(new AutoKitTickListener(this));
      ModuleListenerUtils.handleListenerHolder4(this, () -> {
         if (this.mode.getValue().equals(AutoKitModule.Mode.Always)) {
            this.run11();
         }
      });
   }

   public enum Mode {
      Auto,
      Always;
   }
}
