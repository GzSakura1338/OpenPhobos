package org.phobos.module.combat;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.phobos.api.TickTime;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.tick.TickManager;
import org.phobos.core.tick.TimerManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.math.MathUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.tick.TickManager.TickData;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.module.Module;
import org.phobos.module.player.BlinkModule;
import org.phobos.module.player.ScaffoldModule;

public class TickBaseModule extends Module {
   public final Setting<Double> range;
   public final Setting<TickBaseModule.Distance> mode;
   public final Setting<TickBaseModule.Target> target;
   public final Setting<Integer> ticks;
   public final Setting<Double> distance;
   public final Setting<TickManager.Tick> tick = this.resolveSetting2(new EnumSetting<>("Tick", TickManager.Tick.Player));
   public final Setting<Integer> delta;
   public final Setting<Boolean> fluctuate;
   public final Setting<Integer> cooldown;
   public final TimeGate.Normal normal;
   public final Setting<Integer> pause;

   public TickBaseModule(PhobosClient phobosClient) {
      super(phobosClient, "TickBase", Categories.combat, "Assists you in combat.");
      this.target = this.resolveSetting2(new EnumSetting<>("Target", TickBaseModule.Target.Auto));
      this.mode = this.resolveSetting2(new EnumSetting<>("Mode", TickBaseModule.Distance.Box));
      this.fluctuate = this.resolveSetting2(new BooleanSetting("Fluctuate"));
      this.delta = this.resolveSetting2(new NumberSetting<>("Delta", 1, 1, 3, "t"));
      this.distance = this.resolveSetting2(new NumberSetting<>("Distance", 2.5, 0.0, 16.0).resolveSetting7("Min distance to target"));
      this.range = this.resolveSetting2(new NumberSetting<>("Range", 4.0, 0.0, 16.0).resolveSetting7("Max distance to target"));
      this.ticks = this.resolveSetting2(new NumberSetting<>("Ticks", 4, 1, 20, "t"));
      this.pause = this.resolveSetting2(new NumberSetting<>("Pause", 4, 0, 20, "t").resolveSetting3(() -> !this.fluctuate.getValue()));
      this.cooldown = this.resolveSetting2(new NumberSetting<>("Cooldown", 0, 0, 100, "t"));
      this.normal = new TimeGate.Normal();
      this.resolveSettingGroup2("Main", this.tick, this.target, this.mode);
      this.resolveSettingGroup(this.fluctuate, this.delta);
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (TickBaseModule.this.normal.testLong(TickTime.computeInt(TickBaseModule.this.cooldown.getValue()))
                  && !phobosClient.getTickManager().check()) {
                  if (clientPlayerEntity.getVehicle() == null && !BlinkModule.getBlinkModule().check7() && !ScaffoldModule.getScaffoldModule().check7()) {
                     Entity entity = (Entity)(switch ((TickBaseModule.Target)TickBaseModule.this.target.getValue()) {
                        case Auto -> EntityUtils.resolvePlayerEntity2(clientWorld, clientPlayerEntity, phobosClient);
                        case Sync -> AuraModule.getEntity();
                     });
                     if (entity != null) {
                        double d = Math.sqrt(TickBaseModule.this.mode.getValue().calculate(clientPlayerEntity, entity));
                        if (d > TickBaseModule.this.distance.getValue() && d < TickBaseModule.this.range.getValue()) {
                           phobosClient.getTickManager()
                              .handleClientPlayerEntity3(
                                 clientPlayerEntity,
                                 TickBaseModule.this.fluctuate.getValue()
                                    ? TickData.createTickData(
                                       MathUtils.computeInt5(
                                          TickBaseModule.this.ticks.getValue() - TickBaseModule.this.delta.getValue(),
                                          TickBaseModule.this.ticks.getValue() + TickBaseModule.this.delta.getValue()
                                       )
                                    )
                                    : TickData.createTickData2(TickBaseModule.this.pause.getValue(), TickBaseModule.this.ticks.getValue()),
                                 TickBaseModule.this.tick.getValue()
                              );
                        }

                        TickBaseModule.this.normal.run();
                     }
                  }
               }
            }
         }
      );
   }

   @Override
   public String nothing() {
      return this.target.getValue()
         + ", "
         + (this.phobosClient.getTickManager().check() ? this.phobosClient.getTickManager().getIntValue() : this.pause.getValue());
   }

   public enum Distance {
      Box {
         @Override
         public double calculate(ClientPlayerEntity player, Entity enemy) {
            return enemy.getBoundingBox().squaredMagnitude(player.getClientCameraPosVec(TimerManager.getFloatValue()));
         }
      },
      Position {
         @Override
         public double calculate(ClientPlayerEntity player, Entity enemy) {
            return player.getPos().squaredDistanceTo(enemy.getPos());
         }
      };

      public double calculate(ClientPlayerEntity player, Entity enemy) {
         return 0.0;
      }
   }

   public enum Target {
      Auto,
      Sync;
   }
}
