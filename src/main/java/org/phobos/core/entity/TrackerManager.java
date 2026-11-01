package org.phobos.core.entity;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.phobos.api.MinecraftHolder;
import org.phobos.event.Listener;
import org.phobos.event.TickEvent2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.Feature;
import org.phobos.core.render.RenderContext;
import org.phobos.core.render.RenderUtils;

public class TrackerManager extends Feature {
   public final Set<PositionRotationTracker> positionRotationTrackerSet = ConcurrentHashMap.newKeySet();
   public static final Logger LOGGER = LoggerFactory.getLogger(TrackerManager.class);

   public TrackerManager() {
      this.handleListener(new Listener<TickEvent2>() {
         public void handleObject2(TickEvent2 tickEvent2) {
            if (!TrackerManager.this.positionRotationTrackerSet.isEmpty()) {
               for (PositionRotationTracker positionRotationTracker : TrackerManager.this.positionRotationTrackerSet) {
                  positionRotationTracker.run();
               }
            }
         }
      });
      this.handleListener(
         new Listener<RenderContext>() {
            public void handleObject2(RenderContext renderContext) {
               if (MinecraftHolder.mc.getCameraEntity() != null && !TrackerManager.this.positionRotationTrackerSet.isEmpty()) {
                  TrackerManager.this.positionRotationTrackerSet.removeIf(positionRotationTrackerx -> positionRotationTrackerx.getLongValue2() + positionRotationTrackerx.getLongValue() < System.currentTimeMillis());
                  Camera camera = MinecraftHolder.mc.gameRenderer.getCamera();
                  float f6 = renderContext.getFloatValue();
                  Vec3d vec3d4 = camera.getPos();
                  Vec3d vec3d5 = EntityInterpolation.createVec3d5(MinecraftHolder.mc.getCameraEntity(), f6);

                  for (PositionRotationTracker positionRotationTracker : TrackerManager.this.positionRotationTrackerSet) {
                     float f = positionRotationTracker.computeFloat2(camera.getYaw());
                     float f2 = positionRotationTracker.computeFloat(camera.getPitch());
                     if (positionRotationTracker.isFlag()) {
                        GL11.glDepthFunc(519);
                     }

                     Vec3d vec3d = positionRotationTracker.getVec3d();
                     Vec3d vec3d2 = positionRotationTracker.getVec3d2();
                     Vec3d vec3d3 = new Vec3d(
                        vec3d2.x - MathHelper.lerp(f6, vec3d.x, vec3d2.x),
                        vec3d2.y - MathHelper.lerp(f6, vec3d.y, vec3d2.y),
                        vec3d2.z - MathHelper.lerp(f6, vec3d.z, vec3d2.z)
                     );
                     double d = vec3d2.x - vec3d3.getX();
                     double d2 = vec3d2.y - vec3d3.getY();
                     double d3 = vec3d2.z - vec3d3.getZ();
                     double d4 = vec3d4.getX() - vec3d2.x;
                     double d5 = vec3d4.getY() - vec3d2.y;
                     double d6 = vec3d4.getZ() - vec3d2.z;
                     double d7 = Math.sqrt(d4 * d4 + d5 * d5 + d6 * d6);
                     if (!(d7 > 4096.0)) {
                        float f3 = 0.0018F + 0.003F * (float)d7;
                        if (d7 <= 8.0) {
                           f3 = 0.0245F;
                        }

                        boolean flag = positionRotationTracker.alwaysTrue();
                        MatrixStack matrixStack = new MatrixStack();
                        matrixStack.push();
                        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
                        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
                        matrixStack.translate(d - vec3d4.getX(), d2 - vec3d4.getY(), d3 - vec3d4.getZ());
                        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f));
                        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(f2));
                        matrixStack.scale(-f3, -f3, -1.0F);
                        float f4 = positionRotationTracker.getFloatValue4();
                        float f5 = positionRotationTracker.getFloatValue2();
                        if (flag) {
                           RenderUtils.handleMatrixStack20(matrixStack, positionRotationTracker.getColor(), 0.0, 0.0, f4, f5);
                           if (positionRotationTracker.isFlag()) {
                              GL11.glDepthFunc(515);
                           }

                           matrixStack.pop();
                        } else {
                           RenderUtils.handleMatrixStack29(matrixStack, positionRotationTracker.getColor(), 0.0, 0.0, f4, f5, 360);
                           if (positionRotationTracker.isFlag()) {
                              GL11.glDepthFunc(515);
                           }

                           matrixStack.pop();
                        }
                     }
                  }
               }
            }
         }
      );
   }

   public void handlePositionRotationTracker(PositionRotationTracker positionRotationTracker) {
      this.positionRotationTrackerSet.remove(positionRotationTracker);
   }

   public void handlePositionRotationTracker2(PositionRotationTracker positionRotationTracker, long l) {
      positionRotationTracker.setLongValue(l);
      this.positionRotationTrackerSet.add(positionRotationTracker);
   }
}
