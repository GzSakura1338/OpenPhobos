package org.phobos.module.player;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.event.EntityStatusEvent;
import org.phobos.event.Listener;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.module.Module;

public class AutoBlinkModule extends Module {
   public final Setting<Long> timeout;
   public final Setting<Boolean> triggerBlock;
   public final Setting<Boolean> releaseTime;
   public final TimeGate.Normal normal;
   public final Setting<Boolean> releaseBlock;
   public static AutoBlinkModule autoBlinkModule;
   public final Setting<Boolean> triggerHole = this.resolveSetting2(new BooleanSetting("TriggerHole"));
   public boolean flag;
   public boolean flag2;
   public final Setting<Boolean> releaseHole;

   public static AutoBlinkModule getAutoBlinkModule() {
      return autoBlinkModule;
   }

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      BlockPos blockPos = clientPlayerEntity.getBlockPos();
      return BlockUtils.testBlockPos7(blockPos, clientWorld);
   }

   @Override
   public void run3() {
      this.normal.run();
      this.flag = false;
      this.flag2 = false;
   }

   @Override
   public String nothing() {
      return BlinkModule.getBlinkModule().check7() ? "Active" : "Ready";
   }

   public void run() {
      if (BlinkModule.getBlinkModule().check7()) {
         BlinkModule.getBlinkModule().run11();
      }
   }

   @Override
   public void run4() {
      if (BlinkModule.getBlinkModule().check7()) {
         BlinkModule.getBlinkModule().run11();
      }
   }

   public AutoBlinkModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoBlink", Categories.player, "Turns blink on and off for you");
      this.triggerBlock = this.resolveSetting2(new BooleanSetting("TriggerBlock"));
      this.releaseHole = this.resolveSetting2(new BooleanSetting("ReleaseHole"));
      this.releaseBlock = this.resolveSetting2(new BooleanSetting("ReleaseBlock"));
      this.releaseTime = this.resolveSetting2(new BooleanSetting("ReleaseTime"));
      this.timeout = this.resolveSetting2(new LongNumberSetting("Timeout", 500L, 50L, 10000L, true));
      this.normal = new TimeGate.Normal();
      this.resolveSettingGroup2("Trigger", this.triggerHole, this.triggerBlock);
      this.createBuilder()
         .resolveBuilder4("Release")
         .resolveBuilder5(this.resolveSettingGroup(this.releaseTime, this.timeout))
         .resolveBuilder(this.releaseHole, this.releaseBlock)
         .getSettingSubsection();
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               boolean flag = phobosClient.getSurroundManager().testPlayerEntity2(clientPlayerEntity);
               boolean flag2 = AutoBlinkModule.this.testClientPlayerEntity(clientPlayerEntity, clientWorld);
               if (!BlinkModule.getBlinkModule().check7()) {
                  if (EntityUtils.resolvePlayerEntity(clientWorld, clientPlayerEntity, phobosClient, 16.0) != null) {
                     if (AutoBlinkModule.this.triggerHole.getValue() && !flag && AutoBlinkModule.this.flag) {
                        BlinkModule.getBlinkModule().run12();
                        AutoBlinkModule.this.normal.run();
                     }

                     if (AutoBlinkModule.this.triggerBlock.getValue() && !flag2 && AutoBlinkModule.this.flag2) {
                        BlinkModule.getBlinkModule().run12();
                        AutoBlinkModule.this.normal.run();
                     }
                  }
               } else {
                  if (AutoBlinkModule.this.releaseHole.getValue() && flag) {
                     BlinkModule.getBlinkModule().run11();
                  }

                  if (AutoBlinkModule.this.releaseBlock.getValue() && flag2) {
                     BlinkModule.getBlinkModule().run11();
                  }

                  if (AutoBlinkModule.this.releaseTime.getValue() && AutoBlinkModule.this.normal.testNumber(AutoBlinkModule.this.timeout.getValue())) {
                     BlinkModule.getBlinkModule().run11();
                  }
               }

               AutoBlinkModule.this.flag = flag;
               AutoBlinkModule.this.flag2 = flag2;
            }
         }
      );
      this.handleListener(new Listener<EntityStatusEvent>() {
         public void handleObject2(EntityStatusEvent entityStatusEvent) {
            if (entityStatusEvent.entity() instanceof ClientPlayerEntity && BlinkModule.getBlinkModule().check7()) {
               BlinkModule.getBlinkModule().run11();
            }
         }
      });
      autoBlinkModule = this;
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }
}
