package org.phobos.event;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.phobos.api.CloakAccess;
import org.phobos.api.CosmeticRenderer;
import org.phobos.api.Animatable;
import org.phobos.api.PlayerAnimator;
import org.phobos.core.render.PlayerModelContext;
import org.phobos.core.math.Vector4;
import org.phobos.core.math.Vector3;
import org.phobos.core.render.CosmeticRendererImpl;
import org.phobos.core.ClientHolder;
import org.phobos.core.player.SkinUtils;
import org.phobos.module.client.CapesModule;
import org.phobos.module.client.RotationsModule;
import org.phobos.type.BlocksSingleEnum;
import org.phobos.type.NoneWavesEnum;
import org.phobos.type.VanillaBasicEnum;

public class InitEvent2 extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
   public final ModelPart[] modelPartArray = CapesModule.values(64, 64, i -> 0, i -> i);
   public static final CosmeticRendererImpl cosmeticRendererImpl = new CosmeticRendererImpl();
   public static final int intValue = 16;

   public double computeVector3(Vector3 vector3, Vector3 vector32) {
      Vector3 vector33 = vector32.resolveVector34(vector3);
      return Math.toDegrees(Math.atan2(vector33.floatValue3, vector33.floatValue)) + 180.0;
   }

   public static void handleVertexConsumer(
      VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix4f matrix4f2, float f, float f2, float f3, float f4, float f5, float f6, int i, int i2
   ) {
      if (f < f4) {
         f = f4;
         f4 = f;
      }

      if (f2 < f5) {
         f2 = f5;
         f5 = f2;
         matrix4f = matrix4f2;
         matrix4f2 = matrix4f;
      }

      float f7 = 0.5F;
      float f8 = f7 / 16.0F;
      float f9 = 0.03125F + f8 * (i + 1);
      float f10 = 0.03125F + f8 * i;
      Vector3 vector3 = resolveVector3(
         matrix4f2, matrix4f2, matrix4f, new Vector3(f, f2, f3), new Vector3(f4, f2, f3), new Vector3(f, f5, f6), i2 == 15728880
      );
      SkinUtils.handleVertexConsumer2(
         vertexConsumer,
         matrix4f2,
         f,
         f2,
         f3,
         0.1875F,
         f9,
         OverlayTexture.DEFAULT_UV,
         i2,
         vector3.floatValue3,
         vector3.floatValue,
         vector3.floatValue2
      );
      SkinUtils.handleVertexConsumer2(
         vertexConsumer,
         matrix4f2,
         f4,
         f2,
         f3,
         0.34375F,
         f9,
         OverlayTexture.DEFAULT_UV,
         i2,
         vector3.floatValue3,
         vector3.floatValue,
         vector3.floatValue2
      );
      SkinUtils.handleVertexConsumer2(
         vertexConsumer,
         matrix4f,
         f4,
         f5,
         f6,
         0.34375F,
         f10,
         OverlayTexture.DEFAULT_UV,
         i2,
         vector3.floatValue3,
         vector3.floatValue,
         vector3.floatValue2
      );
      SkinUtils.handleVertexConsumer2(
         vertexConsumer,
         matrix4f,
         f,
         f5,
         f6,
         0.1875F,
         f10,
         OverlayTexture.DEFAULT_UV,
         i2,
         vector3.floatValue3,
         vector3.floatValue,
         vector3.floatValue2
      );
   }

   public void handleAbstractClientPlayerEntity(
      AbstractClientPlayerEntity abstractClientPlayerEntity,
      MatrixStack matrixStack,
      VertexConsumerProvider vertexConsumerProvider,
      CosmeticRenderer cosmeticRenderer,
      PlayerModelContext playerModelContext,
      float f,
      int i
   ) {
      VertexConsumer vertexConsumer = cosmeticRenderer.resolveVertexConsumer(abstractClientPlayerEntity, vertexConsumerProvider, playerModelContext);
      if (vertexConsumer != null) {
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         Matrix4f matrix4f = null;

         for (int i2 = 0; i2 < 16; i2++) {
            this.handleMatrixStack2(matrixStack, playerModelContext, f, i2);
            if (matrix4f == null) {
               matrix4f = matrixStack.peek().getPositionMatrix();
            }

            if (i2 == 0) {
               handleVertexConsumer3(vertexConsumer, matrixStack.peek().getPositionMatrix(), matrix4f, 0.3125F, 0.0F, 0.0F, -0.3125F, 0.0F, -0.0625F, i);
            }

            if (i2 == 15) {
               handleVertexConsumer4(
                  vertexConsumer,
                  matrixStack.peek().getPositionMatrix(),
                  matrixStack.peek().getPositionMatrix(),
                  0.3125F,
                  (i2 + 1) * 0.0625F,
                  0.0F,
                  -0.3125F,
                  (i2 + 1) * 0.0625F,
                  -0.0625F,
                  i
               );
            }

            handleVertexConsumer5(
               vertexConsumer, matrixStack.peek().getPositionMatrix(), matrix4f, -0.3125F, (i2 + 1) * 0.0625F, 0.0F, -0.3125F, i2 * 0.0625F, -0.0625F, i2, i
            );
            handleVertexConsumer6(
               vertexConsumer, matrixStack.peek().getPositionMatrix(), matrix4f, 0.3125F, (i2 + 1) * 0.0625F, 0.0F, 0.3125F, i2 * 0.0625F, -0.0625F, i2, i
            );
            handleVertexConsumer2(
               vertexConsumer, matrixStack.peek().getPositionMatrix(), matrix4f, 0.3125F, (i2 + 1) * 0.0625F, -0.0625F, -0.3125F, i2 * 0.0625F, -0.0625F, i2, i
            );
            handleVertexConsumer(
               vertexConsumer, matrix4f, matrixStack.peek().getPositionMatrix(), 0.3125F, (i2 + 1) * 0.0625F, 0.0F, -0.3125F, i2 * 0.0625F, 0.0F, i2, i
            );
            matrix4f = new Matrix4f(matrixStack.peek().getPositionMatrix());
            matrixStack.pop();
         }
      }
   }

   public void handleMatrixStack(MatrixStack matrixStack, PlayerModelContext playerModelContext, float f, int i) {
      Animatable animatable = playerModelContext.getPlayerAnimator().getSimulation();
      matrixStack.push();
      matrixStack.translate(0.0, 0.0, 0.125);
      float f2 = animatable.getList().get(i).computeFloat(f) - animatable.getList().getFirst().computeFloat(f);
      if (f2 > 0.0F) {
         f2 = 0.0F;
      }

      float f3 = animatable.getList().getFirst().computeFloat3(f) - i - animatable.getList().get(i).computeFloat3(f);
      float f4 = animatable.getList().getFirst().computeFloat2(f) - animatable.getList().get(i).computeFloat2(f);
      float f5 = this.computeFloat2(f, i, animatable);
      float f6 = this.computeInt(i, playerModelContext.check4());
      matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(6.0F + f6));
      matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(0.0F));
      matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
      matrixStack.translate(-f4 / 16.0F, f3 / 16.0F, f2 / 16.0F);
      matrixStack.translate(0.0, 0.03, -0.03);
      matrixStack.translate(0.0F, i * 1.0F / 16.0F, i * 0 / 16.0F);
      matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-f5));
      matrixStack.translate(0.0F, -i * 1.0F / 16.0F, -i * 0 / 16.0F);
      matrixStack.translate(0.0, -0.03, 0.03);
   }

   public static void handleVertexConsumer2(
      VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix4f matrix4f2, float f, float f2, float f3, float f4, float f5, float f6, int i, int i2
   ) {
      if (f < f4) {
         f = f4;
         f4 = f;
      }

      if (f2 < f5) {
         f2 = f5;
         f5 = f2;
         matrix4f = matrix4f2;
         matrix4f2 = matrix4f;
      }

      float f7 = 0.5F;
      float f8 = f7 / 16.0F;
      float f9 = 0.03125F + f8 * (i + 1);
      float f10 = 0.03125F + f8 * i;
      Vector3 vector3 = resolveVector3(
         matrix4f2, matrix4f2, matrix4f, new Vector3(f, f5, f3), new Vector3(f4, f5, f3), new Vector3(f, f2, f6), i2 == 15728880
      );
      SkinUtils.handleVertexConsumer2(
         vertexConsumer,
         matrix4f2,
         f,
         f5,
         f3,
         0.171875F,
         f10,
         OverlayTexture.DEFAULT_UV,
         i2,
         vector3.floatValue3,
         vector3.floatValue,
         vector3.floatValue2
      );
      SkinUtils.handleVertexConsumer2(
         vertexConsumer,
         matrix4f2,
         f4,
         f5,
         f3,
         0.015625F,
         f10,
         OverlayTexture.DEFAULT_UV,
         i2,
         vector3.floatValue3,
         vector3.floatValue,
         vector3.floatValue2
      );
      SkinUtils.handleVertexConsumer2(
         vertexConsumer,
         matrix4f,
         f4,
         f2,
         f6,
         0.015625F,
         f9,
         OverlayTexture.DEFAULT_UV,
         i2,
         vector3.floatValue3,
         vector3.floatValue,
         vector3.floatValue2
      );
      SkinUtils.handleVertexConsumer2(
         vertexConsumer,
         matrix4f,
         f,
         f2,
         f6,
         0.171875F,
         f9,
         OverlayTexture.DEFAULT_UV,
         i2,
         vector3.floatValue3,
         vector3.floatValue,
         vector3.floatValue2
      );
   }

   public static float computeFloat(float f) {
      return MathHelper.sin((float)(f * Math.PI / 2.0));
   }

   public static void handleVertexConsumer3(
      VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix4f matrix4f2, float f, float f2, float f3, float f4, float f5, float f6, int i
   ) {
      if (f < f4) {
         f = f4;
         f4 = f;
      }

      if (f2 < f5) {
         f2 = f5;
         f5 = f2;
      }

      Vector3 vector3 = resolveVector3(
         matrix4f2, matrix4f2, matrix4f, new Vector3(f, f5, f3), new Vector3(f4, f5, f3), new Vector3(f, f2, f6), i == 15728880
      );
      SkinUtils.handleVertexConsumer2(
         vertexConsumer,
         matrix4f2,
         f,
         f5,
         f3,
         0.171875F,
         0.03125F,
         OverlayTexture.DEFAULT_UV,
         i,
         vector3.floatValue3,
         vector3.floatValue,
         vector3.floatValue2
      );
      SkinUtils.handleVertexConsumer2(
         vertexConsumer,
         matrix4f2,
         f4,
         f5,
         f3,
         0.015625F,
         0.03125F,
         OverlayTexture.DEFAULT_UV,
         i,
         vector3.floatValue3,
         vector3.floatValue,
         vector3.floatValue2
      );
      SkinUtils.handleVertexConsumer2(
         vertexConsumer,
         matrix4f,
         f4,
         f2,
         f6,
         0.015625F,
         0.0F,
         OverlayTexture.DEFAULT_UV,
         i,
         vector3.floatValue3,
         vector3.floatValue,
         vector3.floatValue2
      );
      SkinUtils.handleVertexConsumer2(
         vertexConsumer,
         matrix4f,
         f,
         f2,
         f6,
         0.171875F,
         0.0F,
         OverlayTexture.DEFAULT_UV,
         i,
         vector3.floatValue3,
         vector3.floatValue,
         vector3.floatValue2
      );
   }

   public float computeFloat2(float f, int i, Animatable animatable) {
      return i == 15
         ? this.computeFloat2(f, i - 1, animatable)
         : (float)this.computeVector3(animatable.getList().get(i).createVector3(f), animatable.getList().get(i + 1).createVector3(f));
   }

   public void handleMatrixStack2(MatrixStack matrixStack, PlayerModelContext playerModelContext, float f, int i) {
      if (CapesModule.getVanillaBasicEnum() != VanillaBasicEnum.Vanilla) {
         this.handleMatrixStack(matrixStack, playerModelContext, f, i);
      } else {
         this.handleMatrixStack3(matrixStack, playerModelContext.getAbstractClientPlayerEntity(), f, i);
      }
   }

   public static void handleVertexConsumer4(
      VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix4f matrix4f2, float f, float f2, float f3, float f4, float f5, float f6, int i
   ) {
      if (f < f4) {
         f = f4;
         f4 = f;
      }

      if (f2 < f5) {
         f2 = f5;
         f5 = f2;
      }

      Vector3 vector3 = resolveVector3(
         matrix4f2, matrix4f2, matrix4f, new Vector3(f, f5, f6), new Vector3(f4, f5, f6), new Vector3(f, f2, f3), i == 15728880
      );
      SkinUtils.handleVertexConsumer2(
         vertexConsumer,
         matrix4f2,
         f,
         f5,
         f6,
         0.328125F,
         0.0F,
         OverlayTexture.DEFAULT_UV,
         i,
         vector3.floatValue3,
         vector3.floatValue,
         vector3.floatValue2
      );
      SkinUtils.handleVertexConsumer2(
         vertexConsumer,
         matrix4f2,
         f4,
         f5,
         f6,
         0.171875F,
         0.0F,
         OverlayTexture.DEFAULT_UV,
         i,
         vector3.floatValue3,
         vector3.floatValue,
         vector3.floatValue2
      );
      SkinUtils.handleVertexConsumer2(
         vertexConsumer,
         matrix4f,
         f4,
         f2,
         f3,
         0.171875F,
         0.03125F,
         OverlayTexture.DEFAULT_UV,
         i,
         vector3.floatValue3,
         vector3.floatValue,
         vector3.floatValue2
      );
      SkinUtils.handleVertexConsumer2(
         vertexConsumer,
         matrix4f,
         f,
         f2,
         f3,
         0.328125F,
         0.03125F,
         OverlayTexture.DEFAULT_UV,
         i,
         vector3.floatValue3,
         vector3.floatValue,
         vector3.floatValue2
      );
   }

   public static void handleVertexConsumer5(
      VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix4f matrix4f2, float f, float f2, float f3, float f4, float f5, float f6, int i, int i2
   ) {
      if (f < f4) {
         f4 = f;
      }

      if (f2 < f5) {
         f2 = f5;
         f5 = f2;
      }

      float f7 = 0.5F;
      float f8 = f7 / 16.0F;
      float f9 = 0.03125F + f8 * (i + 1);
      float f10 = 0.03125F + f8 * i;
      Vector3 vector3 = resolveVector3(
         matrix4f, matrix4f, matrix4f2, new Vector3(f4, f2, f3), new Vector3(f4, f2, f6), new Vector3(f4, f5, f3), i2 == 15728880
      );
      SkinUtils.handleVertexConsumer2(
         vertexConsumer,
         matrix4f,
         f4,
         f2,
         f3,
         0.0F,
         f9,
         OverlayTexture.DEFAULT_UV,
         i2,
         vector3.floatValue3,
         vector3.floatValue,
         vector3.floatValue2
      );
      SkinUtils.handleVertexConsumer2(
         vertexConsumer,
         matrix4f,
         f4,
         f2,
         f6,
         0.015625F,
         f9,
         OverlayTexture.DEFAULT_UV,
         i2,
         vector3.floatValue3,
         vector3.floatValue,
         vector3.floatValue2
      );
      SkinUtils.handleVertexConsumer2(
         vertexConsumer,
         matrix4f2,
         f4,
         f5,
         f6,
         0.015625F,
         f10,
         OverlayTexture.DEFAULT_UV,
         i2,
         vector3.floatValue3,
         vector3.floatValue,
         vector3.floatValue2
      );
      SkinUtils.handleVertexConsumer2(
         vertexConsumer,
         matrix4f2,
         f4,
         f5,
         f3,
         0.0F,
         f10,
         OverlayTexture.DEFAULT_UV,
         i2,
         vector3.floatValue3,
         vector3.floatValue,
         vector3.floatValue2
      );
   }

   public float computeInt(int i, boolean flag) {
      long l = System.currentTimeMillis() / (flag ? 9 : 3) % 360L;
      float f = (i + 1) / 16.0F;
      return CapesModule.getNoneWavesEnum() == NoneWavesEnum.Waves ? (float)(Math.sin(Math.toRadians(f * 360.0F - (float)l)) * 3.0) : 0.0F;
   }

   public static void handleVertexConsumer6(
      VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix4f matrix4f2, float f, float f2, float f3, float f4, float f5, float f6, int i, int i2
   ) {
      if (f < f4) {
         f4 = f;
      }

      if (f2 < f5) {
         f2 = f5;
         f5 = f2;
      }

      float f7 = 0.5F;
      float f8 = f7 / 16.0F;
      float f9 = 0.03125F + f8 * (i + 1);
      float f10 = 0.03125F + f8 * i;
      Vector3 vector3 = resolveVector3(
         matrix4f, matrix4f, matrix4f2, new Vector3(f4, f2, f6), new Vector3(f4, f2, f3), new Vector3(f4, f5, f6), i2 == 15728880
      );
      SkinUtils.handleVertexConsumer2(
         vertexConsumer,
         matrix4f,
         f4,
         f2,
         f6,
         0.171875F,
         f9,
         OverlayTexture.DEFAULT_UV,
         i2,
         vector3.floatValue3,
         vector3.floatValue,
         vector3.floatValue2
      );
      SkinUtils.handleVertexConsumer2(
         vertexConsumer,
         matrix4f,
         f4,
         f2,
         f3,
         0.1875F,
         f9,
         OverlayTexture.DEFAULT_UV,
         i2,
         vector3.floatValue3,
         vector3.floatValue,
         vector3.floatValue2
      );
      SkinUtils.handleVertexConsumer2(
         vertexConsumer,
         matrix4f2,
         f4,
         f5,
         f3,
         0.1875F,
         f10,
         OverlayTexture.DEFAULT_UV,
         i2,
         vector3.floatValue3,
         vector3.floatValue,
         vector3.floatValue2
      );
      SkinUtils.handleVertexConsumer2(
         vertexConsumer,
         matrix4f2,
         f4,
         f5,
         f6,
         0.171875F,
         f10,
         OverlayTexture.DEFAULT_UV,
         i2,
         vector3.floatValue3,
         vector3.floatValue,
         vector3.floatValue2
      );
   }

   public CosmeticRenderer resolveCosmeticRenderer(PlayerModelContext playerModelContext, VertexConsumerProvider vertexConsumerProvider) {
      if (playerModelContext.getIdentifier() != null && playerModelContext.check2()) {
         cosmeticRendererImpl.vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(playerModelContext.getIdentifier()));
         return cosmeticRendererImpl;
      } else {
         return null;
      }
   }

   public static Vector3 resolveVector3(
      Matrix4f matrix4f,
      Matrix4f matrix4f2,
      Matrix4f matrix4f3,
      Vector3 vector3,
      Vector3 vector32,
      Vector3 vector33,
      boolean flag
   ) {
      Vector3 vector34 = createVector4(
            matrix4f, new Vector4(vector3.floatValue3, vector3.floatValue, vector3.floatValue2, 1.0F)
         )
         .createVector3();
      Vector3 vector35 = createVector4(
            matrix4f2, new Vector4(vector32.floatValue3, vector32.floatValue, vector32.floatValue2, 1.0F)
         )
         .createVector3();
      Vector3 vector36 = createVector4(
            matrix4f3, new Vector4(vector33.floatValue3, vector33.floatValue, vector33.floatValue2, 1.0F)
         )
         .createVector3();
      vector35.resolveVector34(vector34);
      vector36.resolveVector34(vector34);
      vector35.resolveVector32(vector36);
      vector35.getVector3();
      return flag ? vector35.resolveVector36(-1.0F) : vector35;
   }

   public void handleMatrixStack3(MatrixStack matrixStack, AbstractClientPlayerEntity abstractClientPlayerEntity, float f, int i) {
      matrixStack.push();
      matrixStack.translate(0.0, 0.0, 0.125);
      double d = MathHelper.lerp(f, abstractClientPlayerEntity.prevCapeX, abstractClientPlayerEntity.capeX)
         - MathHelper.lerp(f, abstractClientPlayerEntity.prevX, abstractClientPlayerEntity.getX());
      double d2 = MathHelper.lerp(f, abstractClientPlayerEntity.prevCapeY, abstractClientPlayerEntity.capeY)
         - MathHelper.lerp(f, abstractClientPlayerEntity.prevY, abstractClientPlayerEntity.getY());
      double d3 = MathHelper.lerp(f, abstractClientPlayerEntity.prevCapeZ, abstractClientPlayerEntity.capeZ)
         - MathHelper.lerp(f, abstractClientPlayerEntity.prevZ, abstractClientPlayerEntity.getZ());
      float f2 = this.computeAbstractClientPlayerEntity(abstractClientPlayerEntity);
      double d4 = MathHelper.sin(f2 * (float) (Math.PI / 180.0));
      double d5 = -MathHelper.cos(f2 * (float) (Math.PI / 180.0));
      float f3 = (float)d2 * 10.0F;
      float f4 = MathHelper.clamp(f3, -6.0F, 32.0F);
      float f5 = (float)(d * d4 + d3 * d5) * computeFloat(0.0625F * i) * 100.0F;
      float f6 = MathHelper.clamp(f5, 0.0F, 150.0F * computeFloat(0.0625F * i));
      float f7 = MathHelper.clamp((float)(d * d5 - d3 * d4) * 100.0F, -20.0F, 20.0F);
      float f8 = MathHelper.lerp(f, abstractClientPlayerEntity.prevStrideDistance, abstractClientPlayerEntity.strideDistance);
      float f9 = f4
         + MathHelper.sin(MathHelper.lerp(f, abstractClientPlayerEntity.prevHorizontalSpeed, abstractClientPlayerEntity.horizontalSpeed) * 6.0F) * 32.0F * f8;
      float f10 = this.computeInt(i, abstractClientPlayerEntity.isSubmergedInWater());
      matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(6.0F + f6 / 2.0F + f9 + f10));
      matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f7 / 2.0F));
      matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - f7 / 2.0F));
   }

   public static Vector4 createVector4(Matrix4f matrix4f, Vector4 vector4) {
      Vector4f vector4f = matrix4f.transform(
         new Vector4f(vector4.floatValue4, vector4.floatValue3, vector4.floatValue2, vector4.floatValue)
      );
      return new Vector4(vector4f.x, vector4f.y, vector4f.z, vector4f.w);
   }

   public InitEvent2(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
      super(featureRendererContext);
   }

   public float computeAbstractClientPlayerEntity(AbstractClientPlayerEntity abstractClientPlayerEntity) {
      return abstractClientPlayerEntity instanceof ClientPlayerEntity && RotationsModule.getRotationsModule().check7()
         ? ClientHolder.getPhobosClient().getRotationManager().getTimedFloatAnimator().getFloatValue2()
         : abstractClientPlayerEntity.prevBodyYaw + abstractClientPlayerEntity.bodyYaw - abstractClientPlayerEntity.prevBodyYaw;
   }

   public void render(
      MatrixStack matrixStack,
      VertexConsumerProvider vertexConsumerProvider,
      int i,
      AbstractClientPlayerEntity abstractClientPlayerEntity,
      float f,
      float f2,
      float f3,
      float f4,
      float f5,
      float f6
   ) {
      if (CapesModule.check()) {
         PlayerModelContext playerModelContext = new PlayerModelContext(abstractClientPlayerEntity);
         CosmeticRenderer cosmeticRenderer;
         if (!playerModelContext.check3() && (cosmeticRenderer = this.resolveCosmeticRenderer(playerModelContext, vertexConsumerProvider)) != null && !playerModelContext.check5()) {
            PlayerEntityModel<AbstractClientPlayerEntity> playerEntityModel = (PlayerEntityModel<AbstractClientPlayerEntity>)this.getContextModel();
            if (!(playerEntityModel instanceof CloakAccess cloakAccess && !cloakAccess.getCloak().visible)) {
               PlayerAnimator playerAnimator = playerModelContext.getPlayerAnimator();
               playerAnimator.handleInt(16);
               matrixStack.push();
               ((PlayerEntityModel)this.getContextModel()).body.rotate(matrixStack);
               if (playerModelContext.check()) {
                  matrixStack.translate(0.0F, -0.053125F, 0.06875F);
               }

               if (CapesModule.getBlocksSingleEnum() == BlocksSingleEnum.Single && cosmeticRenderer.alwaysTrue()) {
                  this.handleAbstractClientPlayerEntity(abstractClientPlayerEntity, matrixStack, vertexConsumerProvider, cosmeticRenderer, playerModelContext, f3, i);
               } else {
                  ModelPart[] modelParts = this.modelPartArray;

                  for (int i2 = 0; i2 < 16; i2++) {
                     ModelPart modelPart = modelParts[i2];
                     this.handleMatrixStack2(matrixStack, playerModelContext, f3, i2);
                     cosmeticRenderer.handlePlayerModelContext(playerModelContext, i2, modelPart, matrixStack, vertexConsumerProvider, i, OverlayTexture.DEFAULT_UV);
                     matrixStack.pop();
                  }
               }

               matrixStack.pop();
            }
         }
      }
   }
}
