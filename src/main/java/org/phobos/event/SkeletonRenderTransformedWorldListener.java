package org.phobos.event;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.phobos.core.util.BufferUtils;
import org.phobos.core.entity.EntityInterpolation;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.module.client.RotationsModule;
import org.phobos.module.render.SkeletonModule;

public final class SkeletonRenderTransformedWorldListener extends ModuleListener.Safe<SkeletonModule, RenderTransformedWorldEvent> {
   public void handleAbstractClientPlayerEntity(
      AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, Vec3d vec3d, float f, float f2, float f3, float f4, float f5
   ) {
      float f10 = abstractClientPlayerEntity.getLeaningPitch(f4);
      if (this.testAbstractClientPlayerEntity(abstractClientPlayerEntity)) {
         this.handleLivingEntity(abstractClientPlayerEntity, matrixStack, f2, f3, f4, f5);
         float f6 = abstractClientPlayerEntity.getFallFlyingTicks() + f4;
         float f7 = MathHelper.clamp(f6 * f6 / 100.0F, 0.0F, 1.0F);
         if (!abstractClientPlayerEntity.isUsingRiptide()) {
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(f7 * (-90.0F - f)));
         }

         Vec3d vec3d2 = abstractClientPlayerEntity.lerpVelocity(f4);
         double d3 = vec3d2.horizontalLengthSquared();
         double d4 = vec3d.horizontalLengthSquared();
         if (d3 > 0.0 && d4 > 0.0) {
            double d = (vec3d2.x * vec3d.x + vec3d2.z * vec3d.z) / Math.sqrt(d3 * d4);
            double d2 = vec3d2.x * vec3d.z - vec3d2.z * vec3d.x;
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation((float)(Math.signum(d2) * Math.acos(d))));
         }
      } else if (f10 > 0.0F) {
         this.handleLivingEntity(abstractClientPlayerEntity, matrixStack, f2, f3, f4, f5);
         float f8 = abstractClientPlayerEntity.isTouchingWater() ? -90.0F - f : -90.0F;
         float f9 = MathHelper.lerp(f10, 0.0F, f8);
         matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(f9));
         if (abstractClientPlayerEntity.isInSwimmingPose()) {
            matrixStack.translate(0.0F, -1.06F, 0.3F);
         }
      } else {
         this.handleLivingEntity(abstractClientPlayerEntity, matrixStack, f2, f3, f4, f5);
      }
   }

   public <T extends LivingEntity> void handleLivingEntity(T livingEntity, MatrixStack matrixStack, float f, float f2, float f3, float f4) {
      if (Render3d.testLivingEntity(livingEntity)) {
         f2 += (float)(Math.cos(livingEntity.age * 3.25) * Math.PI * 0.4F);
      }

      if (!livingEntity.isInPose(EntityPose.SLEEPING)) {
         matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - f2));
      }

      if (livingEntity.deathTime > 0) {
         float f5 = (livingEntity.deathTime + f3 - 1.0F) / 20.0F * 1.6F;
         f5 = MathHelper.sqrt(f5);
         if (f5 > 1.0F) {
            f5 = 1.0F;
         }

         matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f5 * 90.0F));
      } else if (livingEntity.isUsingRiptide()) {
         matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F - livingEntity.getPitch()));
         matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((livingEntity.age + f3) * -75.0F));
      } else if (livingEntity.isInPose(EntityPose.SLEEPING)) {
         Direction direction = livingEntity.getSleepingDirection();
         float f6 = direction != null ? Render3d.computeDirection(direction) : f2;
         matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f6));
         matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0F));
         matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(270.0F));
         matrixStack.translate(0.0F, -1.5F, 0.0F);
      } else if (LivingEntityRenderer.shouldFlipUpsideDown(livingEntity)) {
         matrixStack.translate(0.0F, (livingEntity.getHeight() + 0.1F) / f4, 0.0F);
         matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
      }
   }

   public boolean testAbstractClientPlayerEntity(AbstractClientPlayerEntity abstractClientPlayerEntity) {
      return abstractClientPlayerEntity instanceof ClientPlayerEntity && PlayerActionUtils.check() ? false : abstractClientPlayerEntity.isFallFlying();
   }

   public void handleObject(
      RenderTransformedWorldEvent renderTransformedWorldEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      Render3d.handleConsumer(
         matrix4f -> {
            MatrixStack matrixStack = renderTransformedWorldEvent.matrixStack();
            float f9 = renderTransformedWorldEvent.tickDelta();
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(MinecraftClient.isFancyGraphicsOrBetter());

            for (AbstractClientPlayerEntity abstractClientPlayerEntity : clientWorld.getPlayers()) {
               if (abstractClientPlayerEntity.isAlive()
                  && (!mc.options.getPerspective().isFirstPerson() || !(abstractClientPlayerEntity instanceof ClientPlayerEntity))) {
                  Vec3d vec3d = EntityInterpolation.createVec3d(abstractClientPlayerEntity, f9);
                  PlayerEntityRenderer playerEntityRenderer = (PlayerEntityRenderer)mc.getEntityRenderDispatcher().getRenderer(abstractClientPlayerEntity);
                  PlayerEntityModel<PlayerEntity> playerEntityModel = (PlayerEntityModel<PlayerEntity>)(Object)playerEntityRenderer.getModel();
                  float f = abstractClientPlayerEntity instanceof ClientPlayerEntity && RotationsModule.getRotationsModule().check7()
                     ? this.phobosClient.getRotationManager().getTimedFloatAnimator().getFloatValue2()
                     : MathHelper.lerpAngleDegrees(f9, abstractClientPlayerEntity.prevBodyYaw, abstractClientPlayerEntity.bodyYaw);
                  float f2 = abstractClientPlayerEntity instanceof ClientPlayerEntity && RotationsModule.getRotationsModule().check7()
                     ? this.phobosClient.getRotationManager().getRotationAnimation().getTimedFloatAnimator2().getFloatValue2()
                     : MathHelper.lerpAngleDegrees(f9, abstractClientPlayerEntity.prevHeadYaw, abstractClientPlayerEntity.headYaw);
                  float f3 = abstractClientPlayerEntity.limbAnimator.getPos() - abstractClientPlayerEntity.limbAnimator.getSpeed() * (1.0F - f9);
                  float f4 = abstractClientPlayerEntity.limbAnimator.getSpeed(f9);
                  float f5 = abstractClientPlayerEntity.age + f9;
                  float f6 = f2 - f;
                  float f7 = abstractClientPlayerEntity instanceof ClientPlayerEntity && RotationsModule.getRotationsModule().check7()
                     ? this.phobosClient.getRotationManager().getRotationAnimation().getTimedFloatAnimator().getFloatValue2()
                     : abstractClientPlayerEntity.getPitch(f9);
                  playerEntityModel.animateModel(abstractClientPlayerEntity, f3, f4, f9);
                  playerEntityModel.setAngles(abstractClientPlayerEntity, f3, f4, f5, f6, f7);
                  boolean flag = abstractClientPlayerEntity.isSneaking();
                  ModelPart modelPart = playerEntityModel.getHead();
                  ModelPart modelPart2 = playerEntityModel.leftArm;
                  ModelPart modelPart3 = playerEntityModel.rightArm;
                  ModelPart modelPart4 = playerEntityModel.leftLeg;
                  ModelPart modelPart5 = playerEntityModel.rightLeg;
                  Vec3d vec3d2 = mc.gameRenderer.getCamera().getPos();
                  matrixStack.push();
                  matrixStack.translate(vec3d.x - vec3d2.x, vec3d.y - vec3d2.y, vec3d.z - vec3d2.z);
                  float f8 = abstractClientPlayerEntity.getScale();
                  matrixStack.scale(f8, f8, f8);
                  this.handleAbstractClientPlayerEntity(
                     abstractClientPlayerEntity,
                     matrixStack,
                     abstractClientPlayerEntity instanceof ClientPlayerEntity && RotationsModule.getRotationsModule().check7()
                        ? this.phobosClient.getRotationManager().getRotationAnimation().getRotation().getVec3d()
                        : abstractClientPlayerEntity.getRotationVec(f9),
                     f7,
                     f5,
                     f,
                     f9,
                     f8
                  );
                  Tessellator tessellator = Tessellator.getInstance();
                  BufferBuilder bufferBuilder = tessellator.begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
                  Matrix4f matrix4f2 = matrixStack.peek().getPositionMatrix();
                  Color color = ColorUtils.resolveColor2(
                     this.phobosClient,
                     abstractClientPlayerEntity.getUuid(),
                     this.module.colorSetting.getValue(),
                     this.module.colorSetting3.getValue(),
                     this.module.colorSetting2.getValue()
                  );
                  bufferBuilder.vertex(matrix4f2, 0.0F, flag ? 0.6F : 0.7F, flag ? 0.23F : 0.0F)
                     .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
                  bufferBuilder.vertex(matrix4f2, 0.0F, flag ? 1.05F : 1.4F, 0.0F)
                     .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
                  bufferBuilder.vertex(matrix4f2, -0.37F, flag ? 1.05F : 1.35F, 0.0F)
                     .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
                  bufferBuilder.vertex(matrix4f2, 0.37F, flag ? 1.05F : 1.35F, 0.0F)
                     .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
                  bufferBuilder.vertex(matrix4f2, -0.15F, flag ? 0.6F : 0.7F, flag ? 0.23F : 0.0F)
                     .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
                  bufferBuilder.vertex(matrix4f2, 0.15F, flag ? 0.6F : 0.7F, flag ? 0.23F : 0.0F)
                     .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
                  matrixStack.push();
                  matrixStack.translate(0.0F, flag ? 1.05F : 1.4F, 0.0F);
                  this.module.handleMatrixStack(matrixStack, modelPart);
                  matrix4f2 = matrixStack.peek().getPositionMatrix();
                  bufferBuilder.vertex(matrix4f2, 0.0F, 0.0F, 0.0F).color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
                  bufferBuilder.vertex(matrix4f2, 0.0F, 0.25F, 0.0F).color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
                  matrixStack.pop();
                  matrixStack.push();
                  matrixStack.translate(0.15F, flag ? 0.6F : 0.7F, flag ? 0.23F : 0.0F);
                  this.module.handleMatrixStack(matrixStack, modelPart5);
                  matrix4f2 = matrixStack.peek().getPositionMatrix();
                  bufferBuilder.vertex(matrix4f2, 0.0F, 0.0F, 0.0F).color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
                  bufferBuilder.vertex(matrix4f2, 0.0F, -0.6F, 0.0F).color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
                  matrixStack.pop();
                  matrixStack.push();
                  matrixStack.translate(-0.15F, flag ? 0.6F : 0.7F, flag ? 0.23F : 0.0F);
                  this.module.handleMatrixStack(matrixStack, modelPart4);
                  matrix4f2 = matrixStack.peek().getPositionMatrix();
                  bufferBuilder.vertex(matrix4f2, 0.0F, 0.0F, 0.0F).color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
                  bufferBuilder.vertex(matrix4f2, 0.0F, -0.6F, 0.0F).color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
                  matrixStack.pop();
                  matrixStack.push();
                  matrixStack.translate(0.37F, flag ? 1.05F : 1.35F, 0.0F);
                  this.module.handleMatrixStack(matrixStack, modelPart3);
                  matrix4f2 = matrixStack.peek().getPositionMatrix();
                  bufferBuilder.vertex(matrix4f2, 0.0F, 0.0F, 0.0F).color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
                  bufferBuilder.vertex(matrix4f2, 0.0F, -0.55F, 0.0F).color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
                  matrixStack.pop();
                  matrixStack.push();
                  matrixStack.translate(-0.37F, flag ? 1.05F : 1.35F, 0.0F);
                  this.module.handleMatrixStack(matrixStack, modelPart2);
                  matrix4f2 = matrixStack.peek().getPositionMatrix();
                  bufferBuilder.vertex(matrix4f2, 0.0F, 0.0F, 0.0F).color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
                  bufferBuilder.vertex(matrix4f2, 0.0F, -0.55F, 0.0F).color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
                  matrixStack.pop();
                  BufferUtils.handleBufferBuilder(bufferBuilder);
                  matrixStack.pop();
               }
            }

            RenderSystem.disableBlend();
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(true);
         }
      );
   }

   public SkeletonRenderTransformedWorldListener(SkeletonModule skeletonModule) {
      super(skeletonModule, RenderTransformedWorldEvent.class);
   }
}
