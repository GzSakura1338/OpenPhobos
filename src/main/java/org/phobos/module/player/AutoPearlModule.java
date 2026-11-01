package org.phobos.module.player;

import java.awt.Color;
import java.util.Objects;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.BindSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.animation.AnimatedVec3d;
import org.phobos.core.render.RenderContext;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.player.InteractionManager;
import org.phobos.core.bind.KeyCombination;
import org.phobos.core.PhobosClient;
import org.phobos.module.TargetModule;
import org.phobos.core.entity.EntityInterpolation;
import org.phobos.core.module.Categories;
import org.phobos.core.combat.TrajectoryUtils;
import org.phobos.core.combat.TrajectoryUtils;
import org.phobos.core.combat.TrajectoryUtils;
import org.phobos.core.combat.TrajectoryUtils;
import org.phobos.core.math.MathUtils;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.tick.TickUtils;
import org.phobos.core.render.Render3d.Lines;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;

public class AutoPearlModule extends TargetModule {
   public final Setting<Double> range;
   public final Setting<Float> renderWidth;
   public final Setting<Boolean> prediction;
   public final Setting<KeyCombination> key;
   public final Setting<InteractionManager.Silent> silent;
   public final Setting<Color> renderColor;
   public final AutoPearlModule.PearlRenderer pearlRenderer;
   public final Setting<Boolean> render;
   public final Setting<Integer> predictionTicks;
   public final Setting<AutoPearlModule.Mode> mode = new EnumSetting<>("Mode", AutoPearlModule.Mode.Offense).resolveSetting6(this);
   public AutoPearlModule.PearlTarget pearlTarget;
   public final Setting<AutoPearlModule.Rotate> rotate;

   @Override
   public void run3() {
      this.run2();
   }

   public float[] values(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      float f3 = 180.0F;
      float f4 = MathUtils.computeFloat8(f3 - 30.0F, f3 + 30.0F);
      float f5 = MathHelper.wrapDegrees(clientPlayerEntity.getYaw() - f4);
      float f6 = -30.0F;
      MutableDouble mutableDouble = new MutableDouble(0.0);
      MutableObject<float[]> mutableObject = new MutableObject(new float[0]);

      for (float f2 = f5 - 30.0F; f2 < f5 + 30.0F; f2++) {
         for (float f = f6 + 30.0F; f > f6 - 30.0F; f--) {
            Vec3d vec3d = TrajectoryUtils.resolveVec3d2(clientPlayerEntity, f, f2, 0.0F, 1.5F);
            TrajectoryUtils.TrajectoryData trajectoryData = TrajectoryUtils.resolveTrajectoryData(
               clientPlayerEntity, clientWorld, null, clientPlayerEntity.getEyePos(), vec3d, TrajectoryUtils.EntityGravityType.Normal, TrajectoryUtils.Drag.Normal
            );
            if (trajectoryData != null) {
               Vec3d vec3d2 = trajectoryData.getVec3d();
               double d = clientPlayerEntity.squaredDistanceTo(vec3d2);
               if (d > mutableDouble.getValue()) {
                  mutableObject.setValue(new float[]{f2, f});
                  mutableDouble.setValue(d);
               }
            }
         }
      }

      return (float[])mutableObject.getValue();
   }

   @NotNull
   public Entity resolveEntity(Entity entity) {
      if (!this.prediction.getValue()) {
         return entity;
      } else {
         return entity instanceof PlayerEntity playerEntity
            ? Objects.requireNonNullElse(
               this.phobosClient.getFeatureRegistry().getPredictionManager().resolvePlayerEntity3(this.predictionTicks.getValue(), playerEntity), entity
            )
            : entity;
      }
   }

   @Override
   public void run4() {
      this.run2();
   }

   @Nullable
   public AutoPearlModule.PearlTarget createPearlTarget(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Entity entity) {
      Entity entity2 = this.resolveEntity(entity);
      float[] floats2 = RotationUtils.values(clientPlayerEntity, entity2);
      float f3 = floats2[0];
      float f4 = floats2[1];
      MutableDouble mutableDouble = new MutableDouble(Double.MAX_VALUE);
      MutableDouble mutableDouble2 = new MutableDouble(Double.MAX_VALUE);
      MutableObject<Vec3d> mutableObject = new MutableObject(null);
      MutableObject<float[]> mutableObject2 = new MutableObject(null);

      for (float f2 = f3 - 15.0F; f2 < f3 + 15.0F; f2 += 4.0F) {
         for (float f = f4 + 30.0F; f > f4 - 30.0F; f -= 4.0F) {
            Vec3d vec3d = TrajectoryUtils.resolveVec3d2(clientPlayerEntity, f, f2, 0.0F, 1.5F);
            TrajectoryUtils.TrajectoryData trajectoryData = TrajectoryUtils.resolveTrajectoryData(
               clientPlayerEntity, clientWorld, null, clientPlayerEntity.getEyePos(), vec3d, TrajectoryUtils.EntityGravityType.Normal, TrajectoryUtils.Drag.Normal
            );
            if (trajectoryData != null) {
               Vec3d vec3d2 = trajectoryData.getVec3d();
               double d = entity2.squaredDistanceTo(vec3d2);
               if (!(d < 3.0) && Math.round(vec3d2.getY()) == BlockPos.ofFloored(vec3d2).getY()) {
                  double d2 = vec3d2.getY();
                  float[] floats = new float[]{f2, f};
                  if (mutableObject2.getValue() == null) {
                     mutableObject.setValue(vec3d2);
                     mutableObject2.setValue(floats);
                     mutableDouble.setValue(d2);
                     mutableDouble2.setValue(d);
                  } else if (mutableDouble2.getValue() > d) {
                     mutableObject.setValue(vec3d2);
                     mutableObject2.setValue(floats);
                     mutableDouble.setValue(d2);
                     mutableDouble2.setValue(d);
                  }
               }
            }
         }
      }

      float[] floats3 = (float[])mutableObject2.getValue();
      return floats3 == null ? null : new AutoPearlModule.PearlTarget(entity2, (Vec3d)mutableObject.getValue(), floats3);
   }

   @Override
   public String nothing() {
      return switch ((AutoPearlModule.Mode)this.mode.getValue()) {
         case Defense -> "Defense";
         case Offense -> {
            if (this.pearlTarget != null && mc.player != null) {
               double d = mc.player.distanceTo(this.pearlTarget.getEntity());
               yield MathUtils.computeDouble(d, 2) + "";
            } else {
               yield "Offense";
            }
         }
      };
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, float[] floats) {
      if (floats.length != 0) {
         float f = floats[0];
         float f2 = floats[1];
         switch ((AutoPearlModule.Rotate)this.rotate.getValue()) {
            case Normal:
               this.phobosClient.getRotationManager().testFloat(f, f2);
               break;
            case Packet:
            case Silent:
               this.phobosClient.getRotationManager().handleClientPlayerEntity3(clientPlayerEntity, f, f2);
         }

         this.phobosClient
            .getInventoryManager()
            .handleConsumer2(
               itemSwitcher -> {
                  Slot slot = itemSwitcher.resolveObject(slotx -> slotx.getStack().isOf(Items.ENDER_PEARL) ? slotx : null);
                  if (slot != null) {
                     itemSwitcher.resolveSwitchResult(Hand.MAIN_HAND, slot, this.silent.getValue().getFlag() | 1);
                     TickUtils.handleClientWorld(
                        clientWorld,
                        integer -> clientPlayerEntity.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, integer, floats[0], floats[1]))
                     );
                     clientPlayerEntity.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
                  }
               }
            );
         if (this.rotate.testObject2(AutoPearlModule.Rotate.Silent)) {
            this.phobosClient.getRotationManager().handleClientPlayerEntity5(clientPlayerEntity);
         }
      }
   }

   public void run2() {
      this.pearlRenderer.handlePearlTarget(null);
      this.pearlTarget = null;
   }

   public AutoPearlModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoPearl", Categories.player, "Throws a following pearl to an existing one");
      this.key = new BindSetting("Key").resolveSetting6(this);
      this.range = new NumberSetting<>("Range", 24.0, 3.0, 30.0);
      this.rotate = new EnumSetting<>("Rotate", AutoPearlModule.Rotate.Normal).resolveSetting6(this);
      this.silent = new EnumSetting<>("Silent", InteractionManager.Silent.Normal).resolveSetting6(this);
      this.prediction = new BooleanSetting("Prediction");
      this.predictionTicks = new NumberSetting<>("PredictionTicks", 1, 1, 5);
      this.render = new BooleanSetting("Render").resolveSetting6(this);
      this.renderColor = new ColorSetting("RenderColor", Color.WHITE).getColorSetting().resolveSetting6(this);
      this.renderWidth = new NumberSetting<>("RenderWidth", 1.0F, 0.1F, 3.0F);
      this.pearlRenderer = new AutoPearlModule.PearlRenderer();
      this.resolveSettingGroup(this.prediction, this.predictionTicks);
      this.resolveSettingGroup(this.render, this.renderColor, this.renderWidth);
      this.handleListener(
         new SafeListener<PlayerTickEvent>() {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (mc.currentScreen == null) {
                  switch ((AutoPearlModule.Mode)AutoPearlModule.this.mode.getValue()) {
                     case Defense:
                        if (clientPlayerEntity.getItemCooldownManager().isCoolingDown(Items.ENDER_PEARL)) {
                           return;
                        }

                        if (!phobosClient.getBindManager().testKeyCombination(AutoPearlModule.this.key.getValue())) {
                           return;
                        }

                        AutoPearlModule.this.handleClientPlayerEntity(
                           clientPlayerEntity, clientWorld, AutoPearlModule.this.values(clientPlayerEntity, clientWorld)
                        );
                        break;
                     case Offense:
                        MutableBoolean mutableBoolean = new MutableBoolean();
                        AutoPearlModule.this.handleClientPlayerEntity2(
                           clientPlayerEntity, clientWorld, AutoPearlModule.this.range.getValue(), livingEntity -> {
                              AutoPearlModule.this.pearlTarget = AutoPearlModule.this.createPearlTarget(clientPlayerEntity, clientWorld, livingEntity);
                              AutoPearlModule.this.pearlRenderer.handlePearlTarget(AutoPearlModule.this.pearlTarget);
                              mutableBoolean.setTrue();
                           }
                        );
                        if (!mutableBoolean.getValue()) {
                           AutoPearlModule.this.pearlTarget = null;
                           AutoPearlModule.this.pearlRenderer.handlePearlTarget(null);
                           return;
                        }

                        if (AutoPearlModule.this.pearlTarget != null
                           && phobosClient.getBindManager().testKeyCombination(AutoPearlModule.this.key.getValue())) {
                           AutoPearlModule.this.handleClientPlayerEntity(clientPlayerEntity, clientWorld, AutoPearlModule.this.pearlTarget.getFloatArray());
                           AutoPearlModule.this.run2();
                        }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<RenderContext>() {
            public void handleObject(
               RenderContext renderContext,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (AutoPearlModule.this.render.getValue()) {
                  AutoPearlModule.this.pearlRenderer.run();
                  Color color = AutoPearlModule.this.renderColor.getValue();
                  float f = AutoPearlModule.this.pearlRenderer.floatAnimator.getFloatValue2();
                  if (AutoPearlModule.this.pearlTarget != null) {
                     if (!(f <= 0.05F)) {
                        Color color2 = ColorUtils.createColor6(color, color.getAlpha() / 255.0F * f);
                        Render3d.handleLines(
                           Lines.createBuilder()
                              .resolveBuilder(
                                 EntityInterpolation.createVec3d3(clientPlayerEntity, 0.25, renderContext.getFloatValue()),
                                 AutoPearlModule.this.pearlRenderer.animatedVec3d.createVec3d(),
                                 color2
                              )
                              .resolveBuilder2(AutoPearlModule.this.renderWidth.getValue())
                              .getLines()
                        );
                     }
                  }
               }
            }
         }
      );
      ModuleListenerUtils.handleListenerHolder4(this, this::run2);
   }

   public enum Mode {
      Defense,
      Offense;
   }

   private static class PearlRenderer {
      public boolean flag;
      public final FloatAnimator floatAnimator;
      public final AnimatedVec3d animatedVec3d = new AnimatedVec3d();

      public PearlRenderer() {
         this.floatAnimator = new FloatAnimator();
         this.flag = true;
      }

      public void handlePearlTarget(@Nullable AutoPearlModule.PearlTarget pearlTarget) {
         if (pearlTarget == null) {
            this.floatAnimator.setFloatValue(0.0F);
            this.flag = true;
         } else {
            this.floatAnimator.setFloatValue(1.0F);
            if (this.flag) {
               this.animatedVec3d.handleVec3d2(pearlTarget.getVec3d());
               this.flag = false;
            } else {
               this.animatedVec3d.handleVec3d(pearlTarget.getVec3d());
            }
         }
      }

      public void run() {
         this.animatedVec3d.handleFloat(10.0F, 0.1F);
         this.floatAnimator.handleFloat3(10.0F, 0.1F);
      }
   }

   private static class PearlTarget {
      public final Entity entity;
      public final float[] floatArray;
      public final Vec3d vec3d;

      public Vec3d getVec3d() {
         return this.vec3d;
      }

      public PearlTarget(Entity entity, Vec3d vec3d, float[] floats) {
         this.entity = entity;
         this.vec3d = vec3d;
         this.floatArray = floats;
      }

      public Entity getEntity() {
         return this.entity;
      }

      public float[] getFloatArray() {
         return this.floatArray;
      }
   }

   public enum Rotate {
      Normal,
      Packet,
      Silent;
   }
}
