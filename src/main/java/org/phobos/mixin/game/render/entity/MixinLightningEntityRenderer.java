package org.phobos.mixin.game.render.entity;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.entity.LightningEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LightningEntity;
import net.minecraft.util.math.random.Random;
import org.joml.Matrix4f;
import org.phobos.core.util.BufferUtils;
import org.phobos.core.render.RenderStates;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.render.Render3d;
import org.phobos.module.render.AmbienceModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningEntityRenderer.class)
public abstract class MixinLightningEntityRenderer {
   @Inject(
      method = "render(Lnet/minecraft/entity/LightningEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void renderHook(
      LightningEntity lightningEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci
   ) {
      if (AmbienceModule.getAmbienceModule().check7() && AmbienceModule.getAmbienceModule().getLightning().getValue()) {
         float[] fs = new float[8];
         float[] gs = new float[8];
         float h = 0.0F;
         float j = 0.0F;
         Random random = Random.create(lightningEntity.seed);

         for (int k = 7; k >= 0; k--) {
            fs[k] = h;
            gs[k] = j;
            h += random.nextInt(11) - 5;
            j += random.nextInt(11) - 5;
         }

         BufferBuilder bufferBuilder = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
         Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
         Color color = AmbienceModule.getAmbienceModule().getLightningColor().getValue();
         float[] array = ColorUtils.values4(color);

         for (int l = 0; l < 4; l++) {
            Random random2 = Random.create(lightningEntity.seed);

            for (int m = 0; m < 3; m++) {
               int n = 7;
               int o = 0;
               if (m > 0) {
                  n = 7 - m;
               }

               if (m > 0) {
                  o = n - 2;
               }

               float p = fs[n] - h;
               float q = gs[n] - j;

               for (int r = n; r >= o; r--) {
                  float s = p;
                  float t = q;
                  if (m == 0) {
                     p += random2.nextInt(11) - 5;
                     q += random2.nextInt(11) - 5;
                  } else {
                     p += random2.nextInt(31) - 15;
                     q += random2.nextInt(31) - 15;
                  }

                  float u = 0.5F;
                  float v = 0.45F;
                  float w = 0.45F;
                  float x = 0.5F;
                  float y = 0.1F + l * 0.2F;
                  if (m == 0) {
                     y *= r * 0.1F + 1.0F;
                  }

                  float z = 0.1F + l * 0.2F;
                  if (m == 0) {
                     z *= (r - 1.0F) * 0.1F + 1.0F;
                  }

                  draw(matrix4f, bufferBuilder, p, q, r, s, t, array[0], array[1], array[2], array[3], y, z, false, false, true, false);
                  draw(matrix4f, bufferBuilder, p, q, r, s, t, array[0], array[1], array[2], array[3], y, z, true, false, true, true);
                  draw(matrix4f, bufferBuilder, p, q, r, s, t, array[0], array[1], array[2], array[3], y, z, true, true, false, true);
                  draw(matrix4f, bufferBuilder, p, q, r, s, t, array[0], array[1], array[2], array[3], y, z, false, true, false, false);
               }
            }
         }

         Render3d.run7();
         RenderSystem.setShader(GameRenderer::getRenderTypeLightningProgram);
         RenderSystem.enableBlend();
         RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE);
         RenderSystem.setShaderColor(array[0], array[1], array[2], array[3]);
         BufferUtils.handleBufferBuilder(bufferBuilder);
         RenderSystem.disableBlend();
         RenderSystem.defaultBlendFunc();
         RenderStates.runnable2.run();
         Render3d.run8();
         ci.cancel();
      }
   }

   @Unique
   private static void draw(
      Matrix4f matrix,
      VertexConsumer buffer,
      float x1,
      float z1,
      int y,
      float x2,
      float z2,
      float red,
      float green,
      float blue,
      float alpha,
      float offset2,
      float offset1,
      boolean shiftEast1,
      boolean shiftSouth1,
      boolean shiftEast2,
      boolean shiftSouth2
   ) {
      buffer.vertex(matrix, x1 + (shiftEast1 ? offset1 : -offset1), y * 16, z1 + (shiftSouth1 ? offset1 : -offset1)).color(red, green, blue, alpha);
      buffer.vertex(matrix, x2 + (shiftEast1 ? offset2 : -offset2), (y + 1) * 16, z2 + (shiftSouth1 ? offset2 : -offset2)).color(red, green, blue, alpha);
      buffer.vertex(matrix, x2 + (shiftEast2 ? offset2 : -offset2), (y + 1) * 16, z2 + (shiftSouth2 ? offset2 : -offset2)).color(red, green, blue, alpha);
      buffer.vertex(matrix, x1 + (shiftEast2 ? offset1 : -offset1), y * 16, z1 + (shiftSouth2 ? offset1 : -offset1)).color(red, green, blue, alpha);
   }
}
