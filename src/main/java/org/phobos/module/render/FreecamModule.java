package org.phobos.module.render;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Vec3d;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.render.RenderContext;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.player.PlayerUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.event.Listener;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent;
import org.phobos.module.Module;

public class FreecamModule extends Module {
   public final Setting<Boolean> rotate;
   public static FreecamModule freecamModule;
   public final FloatAnimator.Double doubleValue;
   public final FloatAnimator.Double doubleValue2;
   public final FloatAnimator.Double doubleValue3;
   public final Setting<Double> horizontal = this.resolveSetting2(new NumberSetting<>("Horizontal", 3.0, 0.1, 10.0).resolveSetting7("Move speed"));
   public float floatValue;
   public final Setting<Double> vertical = this.resolveSetting2(new NumberSetting<>("Vertical", 3.0, 0.1, 10.0).resolveSetting7("Move speed"));
   public float floatValue2;

   public FreecamModule(PhobosClient phobosClient) {
      super(phobosClient, "Freecam", Categories.render, "Free camera");
      this.rotate = this.resolveSetting2(new BooleanSetting("Rotate"));
      this.doubleValue3 = new FloatAnimator.Double();
      this.doubleValue2 = new FloatAnimator.Double();
      this.doubleValue = new FloatAnimator.Double();
      this.floatValue2 = Float.NaN;
      this.floatValue = Float.NaN;
      this.handleListener(new Listener<RenderContext>() {
         public void handleObject2(RenderContext renderContext) {
            FreecamModule.this.doubleValue3.handleDouble(10.0, 0.05);
            FreecamModule.this.doubleValue2.handleDouble(10.0, 0.05);
            FreecamModule.this.doubleValue.handleDouble(10.0, 0.05);
         }
      });
      this.handleListener(
         new SafeListener<TickEvent>(Integer.MIN_VALUE) {
            public void handleObject(
               TickEvent tickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               Vec3d vec3d = PlayerUtils.createVec3d(clientPlayerEntity);
               double d = FreecamModule.this.horizontal.getValue();
               double d2 = FreecamModule.this.vertical.getValue();
               Vec3d vec3d2 = new Vec3d(d * vec3d.x, PlayerUtils.computeFlag(mc.options.jumpKey.isPressed(), mc.options.sneakKey.isPressed()) * d2, d * vec3d.z);
               Vec3d vec3d3 = new Vec3d(
                  FreecamModule.this.doubleValue3.getDoubleValue2(),
                  FreecamModule.this.doubleValue2.getDoubleValue2(),
                  FreecamModule.this.doubleValue.getDoubleValue2()
               );
               Vec3d vec3d4 = vec3d3.add(vec3d2);
               FreecamModule.this.doubleValue3.setDoubleValue2(vec3d4.getX());
               FreecamModule.this.doubleValue2.setDoubleValue2(vec3d4.getY());
               FreecamModule.this.doubleValue.setDoubleValue2(vec3d4.getZ());
               tickEvent.getKeyboardInput().jumping = false;
               tickEvent.getKeyboardInput().sneaking = false;
               tickEvent.getKeyboardInput().movementForward = 0.0F;
               tickEvent.getKeyboardInput().movementSideways = 0.0F;
            }
         }
      );
      this.handleListener(
         new SafeListener<PlayerTickEvent>(-68) {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!RotationManager.isFlag()) {
                  if (FreecamModule.this.rotate.getValue()
                     && mc.crosshairTarget != null
                     && mc.crosshairTarget.getType() == Type.BLOCK
                     && mc.crosshairTarget instanceof BlockHitResult blockHitResult) {
                     Vec3d vec3d = blockHitResult.getPos();
                     if (vec3d != null) {
                        float[] floats = RotationUtils.values2(clientPlayerEntity, vec3d);
                        phobosClient.getRotationManager().testFloat(floats[0], floats[1]);
                     }
                  } else {
                     if (!Float.isNaN(FreecamModule.this.floatValue2) && !Float.isNaN(FreecamModule.this.floatValue)) {
                        phobosClient.getRotationManager().testFloat(FreecamModule.this.floatValue2, FreecamModule.this.floatValue);
                     }
                  }
               }
            }
         }
      );
      ModuleListenerUtils.handleListenerHolder4(this, this::run11);
      freecamModule = this;
   }

   public float getFloatValue2() {
      return this.floatValue2;
   }

   public static FreecamModule getFreecamModule() {
      return freecamModule;
   }

   public Setting<Double> getVertical() {
      return this.vertical;
   }

   public FloatAnimator.Double getDoubleValue() {
      return this.doubleValue;
   }

   public Vec3d createVec3d() {
      return new Vec3d(this.doubleValue3.getDoubleValue(), this.doubleValue2.getDoubleValue(), this.doubleValue.getDoubleValue());
   }

   @Override
   public void run3() {
      if (mc.player != null) {
         mc.chunkCullingEnabled = false;
         double d = mc.player.getX();
         double d2 = mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose());
         double d3 = mc.player.getZ();
         this.floatValue2 = mc.player.getYaw();
         this.floatValue = mc.player.getPitch();
         this.doubleValue3.handleFloat((float)d);
         this.doubleValue2.handleFloat((float)d2);
         this.doubleValue.handleFloat((float)d3);
      }
   }

   public FloatAnimator.Double getDoubleValue2() {
      return this.doubleValue2;
   }

   public Setting<Double> getHorizontal() {
      return this.horizontal;
   }

   @Override
   public void run4() {
      mc.chunkCullingEnabled = true;
      if (mc.player != null && !Float.isNaN(this.floatValue2) && !Float.isNaN(this.floatValue)) {
         mc.player.setYaw(this.floatValue2);
         mc.player.setPitch(this.floatValue);
      }

      this.floatValue2 = Float.NaN;
      this.floatValue = Float.NaN;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public FloatAnimator.Double getDoubleValue3() {
      return this.doubleValue3;
   }

   public Setting<Boolean> getRotate() {
      return this.rotate;
   }
}
