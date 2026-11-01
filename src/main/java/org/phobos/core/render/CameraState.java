package org.phobos.core.render;

import java.util.function.BiConsumer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.phobos.core.entity.EntityInterpolation;

public class CameraState {
   @Nullable
   public Vec3d vec3d;
   public boolean flag = false;
   @Nullable
   public Camera camera;
   @Nullable
   public Entity entity;
   @Nullable
   public ClientPlayerEntity clientPlayerEntity;

   public float computeDouble(double d) {
      float f = 0.0018F + 0.003F * (float)d;
      if (d <= 8.0) {
         f = 0.0245F;
      }

      return f;
   }

   public double computeFloat(float f) {
      if (this.entity != null && this.vec3d != null && this.clientPlayerEntity != null) {
         Vec3d vec3d = EntityInterpolation.createVec3d5(this.entity, f);
         double d = this.vec3d.getX();
         double d2 = this.vec3d.getY();
         double d3 = this.vec3d.getZ();
         double d4 = this.clientPlayerEntity.getX() - vec3d.getX() - d;
         double d5 = this.clientPlayerEntity.getY() - vec3d.getY() - d2;
         double d6 = this.clientPlayerEntity.getZ() - vec3d.getZ() - d3;
         return Math.sqrt(d4 * d4 + d5 * d5 + d6 * d6);
      } else {
         return 0.0;
      }
   }

   public void handleFloat(float f, float f2, BiConsumer<MatrixStack, Vec3d> biConsumer) {
      if (this.camera != null && this.vec3d != null) {
         double d = this.computeFloat(f);
         if (!(d > 4096.0)) {
            float f3 = this.computeDouble(d) * f2;
            MatrixStack matrixStack = new MatrixStack();
            matrixStack.push();
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(this.camera.getPitch()));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(this.camera.getYaw() + 180.0F));
            matrixStack.translate(
               this.vec3d.getX() - this.camera.getPos().getX(),
               this.vec3d.getY() - this.camera.getPos().getY(),
               this.vec3d.getZ() - this.camera.getPos().getZ()
            );
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-this.camera.getYaw()));
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(this.camera.getPitch()));
            matrixStack.scale(-f3, -f3, -1.0F);
            biConsumer.accept(matrixStack, this.vec3d);
            matrixStack.pop();
            if (this.flag) {
               this.flag = false;
               GL11.glDepthFunc(515);
            }
         }
      }
   }

   public boolean isFlag() {
      return this.flag;
   }

   public void setCamera(@Nullable Camera camera) {
      this.camera = camera;
   }

   public void setVec3d(@Nullable Vec3d vec3d) {
      this.vec3d = vec3d;
   }

   public void run() {
      GL11.glDepthFunc(519);
      this.flag = true;
   }

   @Nullable
   public ClientPlayerEntity getClientPlayerEntity() {
      return this.clientPlayerEntity;
   }

   public void setEntity(@Nullable Entity entity) {
      this.entity = entity;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   @Nullable
   public Camera getCamera() {
      return this.camera;
   }

   public void setClientPlayerEntity(@Nullable ClientPlayerEntity clientPlayerEntity) {
      this.clientPlayerEntity = clientPlayerEntity;
   }

   @Nullable
   public Entity getEntity() {
      return this.entity;
   }

   @Nullable
   public Vec3d getVec3d() {
      return this.vec3d;
   }
}
