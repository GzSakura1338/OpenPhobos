package org.phobos.module.movement;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.api.TextProvider;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.event.events.PlayerMoveEvent;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.Listener;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class StepModule extends Module {
   public final Setting<StepModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", StepModule.Mode.Vanilla));
   public final Setting<Boolean> disable;
   public final Setting<Float> height = this.resolveSetting2(
      new NumberSetting<>("Height", 2.0F, 0.6F, 5.0F).resolveSetting3(this.resolveVisibility2(this.mode, StepModule.Mode.Vanilla, StepModule.Mode.Normal))
   );
   public final Setting<Boolean> slow = this.resolveSetting2(new BooleanSetting("Slow"));
   public boolean flag;
   public static StepModule stepModule;

   public StepModule(PhobosClient phobosClient) {
      super(phobosClient, "Step", Categories.movement, "Makes you step up blocks");
      this.disable = this.resolveSetting2(new BooleanSetting("Disable").resolveSetting7("Disables when you're safe"));
      this.handleListener(new Listener<PlayerMoveEvent>() {
         public void handleObject2(PlayerMoveEvent playerMoveEvent) {
            phobosClient.getMotionManager().setFlag4(true);
            phobosClient.getMotionManager().setFlag2(StepModule.this.slow.getValue());
            switch ((StepModule.Mode)StepModule.this.mode.getValue()) {
               case Vanilla:
                  phobosClient.getMotionManager().setFlag(false);
                  phobosClient.getMotionManager().setFloatValue(StepModule.this.height.getValue());
                  break;
               case Plain:
                  phobosClient.getMotionManager().setFlag(true);
                  phobosClient.getMotionManager().setFloatValue(StepModule.this.height.getValue());
                  phobosClient.getMotionManager().setDoubleValueFunction(doubleValue -> phobosClient.getMotionManager().values2(doubleValue));
                  break;
               case Normal:
                  phobosClient.getMotionManager().setFlag(true);
                  phobosClient.getMotionManager().setFloatValue(1.0F);
                  phobosClient.getMotionManager().setDoubleValueFunction(doubleValue -> phobosClient.getMotionManager().values());
                  break;
               case Watchdog:
                  phobosClient.getMotionManager().setFlag(true);
                  phobosClient.getMotionManager().setFlag3(true);
                  phobosClient.getMotionManager().setFloatValue(1.0F);
                  phobosClient.getMotionManager().setDoubleValueFunction(doubleValue -> phobosClient.getMotionManager().values());
            }
         }
      });
      this.handleListener(
         new SafeListener<PlayerTickEvent>() {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (StepModule.this.disable.getValue()) {
                  boolean flag = phobosClient.getSurroundManager().testPlayerEntity2(clientPlayerEntity);
                  if (flag && !StepModule.this.flag) {
                     StepModule.this.run11();
                  }

                  StepModule.this.flag = flag;
               }
            }
         }
      );
      stepModule = this;
   }

   public static StepModule getStepModule() {
      return stepModule;
   }

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   @Override
   public void run4() {
      this.phobosClient.getMotionManager().setFlag4(false);
      this.phobosClient.getMotionManager().setFlag2(false);
   }

   private enum Mode implements TextProvider {
      Vanilla("Vanilla step"),
      Normal("NCP step"),
      Watchdog("Watchdog 1 block step"),
      Plain("Strict NCP step");

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
