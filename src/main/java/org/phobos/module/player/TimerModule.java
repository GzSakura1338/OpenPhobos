package org.phobos.module.player;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.common.CommonPingS2CPacket;
import net.minecraft.util.math.MathHelper;
import org.phobos.api.TextProvider;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.BindSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.bind.KeyCombination;
import org.phobos.core.PhobosClient;
import org.phobos.core.entity.LimbAnimatorUtils;
import org.phobos.core.module.Categories;
import org.phobos.core.tick.TimeUtils;
import org.phobos.core.math.MathUtils;
import org.phobos.core.player.PlayerUtils;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.core.tick.TimerManager.TimerPriority;
import org.phobos.event.Listener;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;
import org.phobos.module.movement.StepModule;

public class TimerModule extends Module {
   public final Setting<Long> threshold;
   public int intValue;
   public final Setting<Float> speed;
   public float floatValue;
   public final Setting<Long> lag;
   public final Setting<Boolean> discharge;
   public final Setting<Integer> ticks;
   public long longValue;
   public final Setting<TimerModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", TimerModule.Mode.Normal));
   public final Setting<KeyCombination> use;
   public final Setting<Boolean> step;
   public final TimeGate.Normal normal;
   public boolean flag;

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      if (this.intValue++ < this.ticks.getValue()) {
         int i = clientPlayerEntity.hurtTime;
         int i2 = clientPlayerEntity.lastAttackedTicks;
         int i3 = clientPlayerEntity.itemUseTimeLeft;
         float f = clientPlayerEntity.handSwingProgress;
         float f2 = clientPlayerEntity.lastHandSwingProgress;
         int i4 = clientPlayerEntity.handSwingTicks;
         boolean flag = clientPlayerEntity.handSwinging;
         float f3 = clientPlayerEntity.strideDistance;
         float f4 = clientPlayerEntity.prevStrideDistance;
         float f5 = clientPlayerEntity.getYaw();
         float f6 = clientPlayerEntity.prevYaw;
         float f7 = clientPlayerEntity.renderYaw;
         float f8 = clientPlayerEntity.lastRenderYaw;
         float f9 = clientPlayerEntity.getPitch();
         float f10 = clientPlayerEntity.prevPitch;
         float f11 = clientPlayerEntity.renderPitch;
         float f12 = clientPlayerEntity.lastRenderPitch;
         float f13 = clientPlayerEntity.bodyYaw;
         float f14 = clientPlayerEntity.prevBodyYaw;
         double[] doubles = LimbAnimatorUtils.values(clientPlayerEntity);
         float[] floats = LimbAnimatorUtils.values2(clientPlayerEntity.limbAnimator);
         clientPlayerEntity.tick();
         clientPlayerEntity.hurtTime = i;
         clientPlayerEntity.lastAttackedTicks = i2;
         clientPlayerEntity.itemUseTimeLeft = i3;
         clientPlayerEntity.handSwingProgress = f;
         clientPlayerEntity.lastHandSwingProgress = f2;
         clientPlayerEntity.handSwingTicks = i4;
         clientPlayerEntity.handSwinging = flag;
         clientPlayerEntity.strideDistance = f3;
         clientPlayerEntity.prevStrideDistance = f4;
         clientPlayerEntity.setYaw(f5);
         clientPlayerEntity.prevYaw = f6;
         clientPlayerEntity.renderYaw = f7;
         clientPlayerEntity.lastRenderYaw = f8;
         clientPlayerEntity.setPitch(f9);
         clientPlayerEntity.prevPitch = f10;
         clientPlayerEntity.renderPitch = f11;
         clientPlayerEntity.lastRenderPitch = f12;
         clientPlayerEntity.bodyYaw = f13;
         clientPlayerEntity.prevBodyYaw = f14;
         LimbAnimatorUtils.handleAbstractClientPlayerEntity(clientPlayerEntity, doubles);
         LimbAnimatorUtils.handleLimbAnimator(clientPlayerEntity.limbAnimator, floats);
      } else {
         this.intValue = 0;
      }
   }

   @Override
   public void run4() {
      if (this.step.getValue() && StepModule.getStepModule().check7()) {
         StepModule.getStepModule().run11();
      }

      this.phobosClient.getTimerManager().run2();
   }

   @Override
   public void run3() {
      if (this.step.getValue() && !StepModule.getStepModule().check7()) {
         StepModule.getStepModule().run12();
      }

      this.phobosClient.getTimerManager().run2();
      this.floatValue = 0.0F;
      this.flag = false;
      this.normal.run();
      if (this.mode.testObject3(TimerModule.Mode.Grim)) {
         this.longValue = TimeUtils.value();
      }
   }

   @Override
   public String nothing() {
      return this.mode.testObject3(TimerModule.Mode.Grim) ? String.valueOf(MathUtils.computeFloat5(this.floatValue, 2)) : String.valueOf(this.mode.getValue());
   }

   public TimerModule(PhobosClient phobosClient) {
      super(phobosClient, "Timer", Categories.player, "Speeds up the game");
      this.ticks = this.resolveSetting2(new NumberSetting<>("Ticks", 1, 1, 10));
      this.use = this.resolveSetting2(new BindSetting("Use").resolveSetting4(this.mode, TimerModule.Mode.Grim));
      this.discharge = this.resolveSetting2(new BooleanSetting("Discharge", true).resolveSetting4(this.mode, TimerModule.Mode.Grim));
      this.lag = this.resolveSetting2(
         new LongNumberSetting("Lag", 3000L, 0L, 20000L, true).resolveSetting4(this.mode, TimerModule.Mode.Grim).resolveSetting7("Stops if lagging")
      );
      this.speed = this.resolveSetting2(
         new NumberSetting<>("Speed", 2.0F, 0.001F, 15.0F).resolveSetting3(this.resolveVisibility2(this.mode, TimerModule.Mode.Normal, TimerModule.Mode.Grim))
      );
      this.threshold = this.resolveSetting2(new LongNumberSetting("Threshold", 50L, -1L, 500L, true).resolveSetting4(this.mode, TimerModule.Mode.Intave));
      this.step = this.resolveSetting2(new BooleanSetting("Step"));
      this.normal = new TimeGate.Normal();
      this.handleListener(
         new SafeListener<PlayerTickEvent>(-10) {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               switch ((TimerModule.Mode)TimerModule.this.mode.getValue()) {
                  case Grim:
                     phobosClient.getTimerManager().handleFloat(TimerModule.this.speed.getValue(), TimerPriority.High);
                     break;
                  case Physics:
                     if (!phobosClient.getTickManager().testLong(50L)) {
                        phobosClient.getTimerManager().run2();
                        TimerModule.this.floatValue = 0.0F;
                     }

                     if (TimerModule.this.discharge.getValue() && !PlayerUtils.check5()) {
                        phobosClient.getTimerManager().run2();
                        TimerModule.this.floatValue = 0.0F;
                        return;
                     }

                     if (TimerModule.this.floatValue <= 0.0F
                        || !PlayerUtils.check5()
                        || !phobosClient.getTickManager().testLong(TimerModule.this.lag.getValue())) {
                        phobosClient.getTimerManager().run2();
                        return;
                     }

                     if (phobosClient.getBindManager().testKeyCombination(TimerModule.this.use.getValue())) {
                        phobosClient.getTimerManager().run2();
                        return;
                     }

                     phobosClient.getTimerManager().handleFloat(Math.max(TimerModule.this.speed.getValue(), 1.0F), TimerPriority.High);
                     TimerModule.this.floatValue = MathHelper.clamp(
                        TimerModule.this.floatValue - (0.0025F * TimerModule.this.speed.getValue() - 0.0025F), 0.0F, 1.0F
                     );
                     break;
                  case Normal:
                     if (TimerModule.this.normal.testLong2(TimerModule.this.flag ? 250L + TimerModule.this.threshold.getValue() : 250L)) {
                        TimerModule.this.flag = !TimerModule.this.flag;
                     }

                     if (TimerModule.this.flag) {
                        phobosClient.getTimerManager().handleFloat(1.4F, TimerPriority.High);
                        return;
                     }

                     phobosClient.getTimerManager().handleFloat(0.7F, TimerPriority.High);
                     break;
                  case Intave:
                     TimerModule.this.handleClientPlayerEntity(clientPlayerEntity);
               }
            }
         }
      );
      this.handleListener(
         new Listener<Receive<CommonPingS2CPacket>>() {
            public void handleObject2(Receive<CommonPingS2CPacket> receive) {
               if (TimerModule.this.mode.getValue().equals(TimerModule.Mode.Grim)
                  && phobosClient.getTickManager().testLong(TimerModule.this.lag.getValue())) {
                  if (TimeUtils.testLong2(TimerModule.this.longValue, 25000L)) {
                     TimerModule.this.longValue = TimeUtils.value();
                     TimerModule.this.floatValue = 0.0F;
                  } else {
                     if (!PlayerUtils.check5()) {
                        TimerModule.this.floatValue = MathHelper.clamp(TimerModule.this.floatValue + 0.005F, 0.0F, 1.0F);
                     }

                     receive.setCancelled(true);
                  }
               }
            }
         }
      );
   }

   public enum Mode implements TextProvider {
      Normal("Normal timer"),
      Grim("Grim transaction shift"),
      Intave("Timer balance abuse"),
      Physics("Physics");

      public final String text;

      @Override
      public String getText() {
         return this.text;
      }

      Mode(final String description) {
         this.text = description;
      }
   }
}
