package org.phobos.core.render;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.MatrixStack.Entry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.joml.Math;
import org.joml.Matrix4f;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.render.shader.BlurShader;
import org.phobos.core.util.BufferUtils;
import org.phobos.core.render.shader.ColorfulThemeShader;
import org.phobos.core.render.shader.FractThemeShader;
import org.phobos.core.render.shader.GlowShader;
import org.phobos.core.render.shader.GradientRoundedRectShader;
import org.phobos.core.render.shader.GridThemeShader;
import org.phobos.core.render.shader.LoadingShader;
import org.phobos.core.render.shader.MarbleThemeShader;
import org.phobos.core.render.shader.MenuButtonShader;
import org.phobos.core.render.shader.PlasmaThemeShader;
import org.phobos.core.render.shader.PostProcessShader;
import org.phobos.core.render.shader.RoundedRectShader;

public final class RenderUtils {
   public static LoadingShader loadingShader;
   public static PostProcessShader postProcessShader;
   public static FractThemeShader fractThemeShader;
   public static MenuButtonShader menuButtonShader;
   public static final float[][] floatArray = new float[][]{new float[3], new float[3], new float[3], new float[3]};
   public static RoundedRectShader roundedRectShader;
   public static MarbleThemeShader marbleThemeShader;
   public static PlasmaThemeShader plasmaThemeShader;
   public static GradientRoundedRectShader gradientRoundedRectShader;
   public static ColorfulThemeShader colorfulThemeShader;
   public static BlurShader blurShader;
   public static GlowShader glowShader;
   public static GridThemeShader gridThemeShader;

   public static void handleMatrix4f(Matrix4f matrix4f, double d, double d2, double d3, double d4, float f, float f2, float f3, float f4) {
      BufferBuilder bufferBuilder = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
      bufferBuilder.vertex(matrix4f, (float)d, (float)d4, 0.0F).texture(f, f4);
      bufferBuilder.vertex(matrix4f, (float)d2, (float)d4, 0.0F).texture(f2, f4);
      bufferBuilder.vertex(matrix4f, (float)d2, (float)d3, 0.0F).texture(f2, f3);
      bufferBuilder.vertex(matrix4f, (float)d, (float)d3, 0.0F).texture(f, f3);
      RenderSystem.setShader(GameRenderer::getPositionTexProgram);
      BufferUtils.handleBufferBuilder(bufferBuilder);
   }

   public static void handleMatrixStack(MatrixStack matrixStack, double d, double d2, double d3, double d4, Sprite sprite) {
      handleMatrixStack8(matrixStack, sprite, d, d2, d3, d4);
   }

   public static void run() {
      RenderSystem.disableBlend();
      RenderSystem.enableCull();
   }

   public static void handleMatrixStack2(MatrixStack matrixStack, double d, double d2, double d3) {
      handleMatrixStack3(matrixStack, d, d2, d3, d3);
   }

   public static void handleMatrixStack3(MatrixStack matrixStack, double d, double d2, double d3, double d4) {
      handleMatrixStack10(matrixStack, d, d2, d3, d4, 0.0F, 0.0F, d3, d4, d3, d4);
   }

   public static void run2() {
      gradientRoundedRectShader = new GradientRoundedRectShader();
      roundedRectShader = new RoundedRectShader();
      blurShader = new BlurShader();
      glowShader = new GlowShader();
      postProcessShader = new PostProcessShader();
      menuButtonShader = new MenuButtonShader();
      loadingShader = new LoadingShader();
      fractThemeShader = new FractThemeShader();
      colorfulThemeShader = new ColorfulThemeShader();
      plasmaThemeShader = new PlasmaThemeShader();
      gridThemeShader = new GridThemeShader();
      marbleThemeShader = new MarbleThemeShader();
   }

   public static void handleMatrixStack4(MatrixStack matrixStack, Color color, double d, double d2, double d3) {
      handleMatrixStack11(matrixStack, color, d, d2, d3, d3);
   }

   public static void handleMatrixStack5(MatrixStack matrixStack, Identifier identifier, double d, double d2, double d3) {
      float f = 8.0F;
      float f2 = 8.0F;
      RenderSystem.setShaderTexture(0, identifier);
      handleMatrixStack10(matrixStack, d, d2, d3, d3, 8.0F, f, 8.0, f2, 64.0, 64.0);
      handleMatrixStack9(matrixStack, identifier, d, d2, d3);
   }

   public static void handleMatrixStack6(
      MatrixStack matrixStack, Color color, double d, double d2, double d3, double d4, float f, float f2, double d5, double d6, double d7, double d8
   ) {
      handleMatrix4f2(
         matrixStack.peek().getPositionMatrix(),
         color,
         d,
         d + d3,
         d2,
         d2 + d4,
         (f + 0.0F) / (float)d7,
         (f + (float)d5) / (float)d7,
         (f2 + 0.0F) / (float)d8,
         (f2 + (float)d6) / (float)d8
      );
   }

   public static void handleMatrixStack7(MatrixStack matrixStack, double d, double d2, double d3, Identifier identifier) {
      BufferBuilder bufferBuilder = Tessellator.getInstance().begin(DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_TEXTURE);
      Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();

      for (int i = 0; i < 360; i += (int)Math.min(1.0, 360 - i)) {
         double d4 = Math.toRadians(i);
         double d5 = Math.sin(d4) * d3;
         double d6 = Math.cos(d4) * d3;
         bufferBuilder.vertex(matrix4f, (float)(d + d5), (float)(d2 + d6), 0.0F).texture(0.0F, 0.0F);
      }

      RenderSystem.setShaderTexture(0, identifier);
      RenderSystem.setShader(GameRenderer::getPositionTexProgram);
      BufferUtils.handleBufferBuilder(bufferBuilder);
   }

   public static void handleMatrixStack8(MatrixStack matrixStack, Sprite sprite, double d, double d2, double d3, double d4) {
      if (d3 != 0.0 && d4 != 0.0) {
         RenderSystem.setShaderTexture(0, sprite.getAtlasId());
         handleMatrix4f2(
            matrixStack.peek().getPositionMatrix(), Color.WHITE, d, d + d3, d2, d2 + d4, sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV()
         );
      }
   }

   public static void handleMatrixStack9(MatrixStack matrixStack, Identifier identifier, double d, double d2, double d3) {
      float f = 8.0F;
      float f2 = 8.0F;
      RenderSystem.enableBlend();
      RenderSystem.setShaderTexture(0, identifier);
      handleMatrixStack10(matrixStack, d, d2, d3, d3, 40.0F, f, 8.0, f2, 64.0, 64.0);
      RenderSystem.disableBlend();
   }

   public static void run3() {
      RenderSystem.disableCull();
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void handleMatrix4f2(Matrix4f matrix4f, Color color, double d, double d2, double d3, double d4, float f, float f2, float f3, float f4) {
      BufferBuilder bufferBuilder = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
      bufferBuilder.vertex(matrix4f, (float)d, (float)d4, 0.0F)
         .texture(f, f4)
         .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
      bufferBuilder.vertex(matrix4f, (float)d2, (float)d4, 0.0F)
         .texture(f2, f4)
         .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
      bufferBuilder.vertex(matrix4f, (float)d2, (float)d3, 0.0F)
         .texture(f2, f3)
         .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
      bufferBuilder.vertex(matrix4f, (float)d, (float)d3, 0.0F)
         .texture(f, f3)
         .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
      RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
      BufferUtils.handleBufferBuilder(bufferBuilder);
   }

   public static void handleMatrixStack10(
      MatrixStack matrixStack, double d, double d2, double d3, double d4, float f, float f2, double d5, double d6, double d7, double d8
   ) {
      handleMatrix4f(
         matrixStack.peek().getPositionMatrix(),
         d,
         d + d3,
         d2,
         d2 + d4,
         (f + 0.0F) / (float)d7,
         (f + (float)d5) / (float)d7,
         (f2 + 0.0F) / (float)d8,
         (f2 + (float)d6) / (float)d8
      );
   }

   public static void handleMatrixStack11(MatrixStack matrixStack, Color color, double d, double d2, double d3, double d4) {
      handleMatrixStack6(matrixStack, color, d, d2, d3, d4, 0.0F, 0.0F, 512.0, 512.0, 512.0, 512.0);
   }

   public static void handleMatrixStack12(MatrixStack matrixStack, Color color, double d, double d2, double d3, double d4, float f, Runnable runnable) {
      if (d < d3) {
         double d5 = d;
         d = d3;
         d3 = d5;
      }

      if (d2 < d4) {
         double d6 = d2;
         d2 = d4;
         d4 = d6;
      }

      Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
      float f2 = color.getRed() / 255.0F;
      float f3 = color.getGreen() / 255.0F;
      float f4 = color.getBlue() / 255.0F;
      float f5 = color.getAlpha() / 255.0F;
      BufferBuilder bufferBuilder = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
      bufferBuilder.vertex(matrix4f, (float)d, (float)d4, f).color(f2, f3, f4, f5);
      bufferBuilder.vertex(matrix4f, (float)d3, (float)d4, f).color(f2, f3, f4, f5);
      bufferBuilder.vertex(matrix4f, (float)d3, (float)d2, f).color(f2, f3, f4, f5);
      bufferBuilder.vertex(matrix4f, (float)d, (float)d2, f).color(f2, f3, f4, f5);
      run3();
      runnable.run();
      BufferUtils.handleBufferBuilder(bufferBuilder);
      run();
   }

   public static void handleMatrixStack13(MatrixStack matrixStack, Color color, double d, double d2, double d3, double d4, double d5) {
      handleMatrixStack20(matrixStack, color, d, d2, d + d3, d2 + d5);
      handleMatrixStack20(matrixStack, color, d, d2 + d4 - d5, d + d3, d2 + d4);
      handleMatrixStack20(matrixStack, color, d, d2 + d5, d + d5, d2 + d4 - d5);
      handleMatrixStack20(matrixStack, color, d + d3 - d5, d2 + d5, d + d3, d2 + d4 - d5);
   }

   public static void handleMatrixStack14(MatrixStack matrixStack, Color color, double d, double d2, double d3, double d4) {
      handleMatrixStack20(matrixStack, color, d, d2, d + d3, d2 + 1.0);
      handleMatrixStack20(matrixStack, color, d, d2 + d4 - 1.0, d + d3, d2 + d4);
      handleMatrixStack20(matrixStack, color, d, d2 + 1.0, d + 1.0, d2 + d4 - 1.0);
      handleMatrixStack20(matrixStack, color, d + d3 - 1.0, d2 + 1.0, d + d3, d2 + d4 - 1.0);
   }

   public static void handleFloat(float f, float f2, float f3, float f4, float f5, float f6, float f7, Color color, Color color2, Color color3, Color color4) {
      gradientRoundedRectShader.handleFloat2(f, f2, f3, f4, f5, 1.0F, 1.0F, f6, f7, color, color2, color3, color4);
      gradientRoundedRectShader.run3();
   }

   public static void handleMatrixStack15(MatrixStack matrixStack, Color color, double d, double d2, double d3, double d4, float f) {
      handleMatrixStack12(matrixStack, color, d, d2, d3, d4, f, () -> RenderSystem.setShader(GameRenderer::getPositionColorProgram));
   }

   public static void handleMatrixStack16(MatrixStack matrixStack, Color color, double d, double d2, double d3, float f) {
      handleMatrixStack20(matrixStack, color, d, d2, d + f, d3);
   }

   public static void handleMatrixStack17(MatrixStack matrixStack, float f, float f2, Color color, float f3) {
      run3();
      MinecraftClient minecraftClient = MinecraftClient.getInstance();
      BufferBuilder bufferBuilder = resolveBufferBuilder(
         matrixStack.peek().getPositionMatrix(),
         Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION),
         0.0F,
         0.0F,
         minecraftClient.getWindow().getScaledWidth() + 20,
         minecraftClient.getWindow().getScaledHeight() + 20,
         0.0F
      );
      glowShader.handleFloat(f, f2, color, f3);
      glowShader.run3();
      BufferUtils.handleBufferBuilder(bufferBuilder);
      run();
   }

   public static void handleMatrixStack18(MatrixStack matrixStack, float f, float f2, float f3, float f4, Color color, Color color2, Color color3, Color color4) {
      handleMatrixStack21(matrixStack, f, f2, f3, f4, color, color2, color3, color4, 0.0F);
   }

   public static void handleMatrixStack19(MatrixStack matrixStack, Color color, double d, double d2, double d3, float f) {
      handleMatrixStack20(matrixStack, color, d, d2, d3, d2 + f);
   }

   public static void handleMatrixStack20(MatrixStack matrixStack, Color color, double d, double d2, double d3, double d4) {
      handleMatrixStack15(matrixStack, color, d, d2, d3, d4, 0.0F);
   }

   public static void handleMatrixStack21(
      MatrixStack matrixStack, float f, float f2, float f3, float f4, Color color, Color color2, Color color3, Color color4, float f5
   ) {
      Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
      run3();
      RenderSystem.setShader(GameRenderer::getPositionColorProgram);
      BufferBuilder bufferBuilder = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
      bufferBuilder.vertex(matrix4f, f3, f2, 0.0F).color(color4.getRGB());
      bufferBuilder.vertex(matrix4f, f, f2, 0.0F).color(color2.getRGB());
      bufferBuilder.vertex(matrix4f, f, f4, 0.0F).color(color.getRGB());
      bufferBuilder.vertex(matrix4f, f3, f4, 0.0F).color(color3.getRGB());
      BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
      run();
   }

   public static void handleMatrixStack22(MatrixStack matrixStack, float f, float f2, float f3, float f4, Color color, float f5) {
      run3();
      BufferBuilder bufferBuilder = resolveBufferBuilder(
         matrixStack.peek().getPositionMatrix(),
         Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION),
         f - 10.0F,
         f2 - 10.0F,
         f3 + 20.0F,
         f4 + 20.0F,
         0.0F
      );
      blurShader.handleFloat(f, f2, f3, f4, 1.0F, color, f5, 1.0F - color.getAlpha() / 255.0F);
      blurShader.run3();
      BufferUtils.handleBufferBuilder(bufferBuilder);
      run();
   }

   public static void handleFloat2(float f, float f2, float f3, float f4, float f5, Color color) {
      roundedRectShader.handleFloat(f, f2, f3, f4, f5, color);
      roundedRectShader.run3();
   }

   public static Runnable resolveRunnable(float f, float f2, float f3, float f4, Color color, float f5) {
      return () -> {
         blurShader.handleFloat(f, f2, f3, f4, 1.0F, color, f5, 1.0F - color.getAlpha() / 255.0F);
         blurShader.run3();
      };
   }

   public static void handleFloat3(float f, float f2, float f3, float f4, float f5, float f6, Color color) {
      gradientRoundedRectShader.handleFloat(f, f2, f3, f4, f5, 1.0F, 1.0F, f6, color, color, color, color);
      gradientRoundedRectShader.run3();
   }

   public static void handleMatrixStack23(MatrixStack matrixStack, Color color, double d, double d2, double d3, double d4) {
      float[] floats = ColorUtils.values2(ColorUtils.values(color.getRGB()));
      Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
      BufferBuilder bufferBuilder = Tessellator.getInstance().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
      bufferBuilder.vertex(matrix4f, (float)d, (float)d2, 0.0F).color(floats[0], floats[1], floats[2], floats[3]);
      bufferBuilder.vertex(matrix4f, (float)d3, (float)d4, 0.0F).color(floats[0], floats[1], floats[2], floats[3]);
      run3();
      RenderSystem.setShader(GameRenderer::getPositionColorProgram);
      BufferUtils.handleBufferBuilder(bufferBuilder);
      run();
   }

   public static void handleMatrixStack24(MatrixStack matrixStack, Color color, double d, double d2, double d3, double d4, int i, float f, Runnable runnable) {
      i = MathHelper.clamp(i, 16, 360);
      Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
      float f3 = color.getRed() / 255.0F;
      float f4 = color.getGreen() / 255.0F;
      float f5 = color.getBlue() / 255.0F;
      float f6 = color.getAlpha() / 255.0F;
      BufferBuilder bufferBuilder = Tessellator.getInstance().begin(DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
      bufferBuilder.vertex(matrix4f, (float)d, (float)d2, f).color(f3, f4, f5, f6);
      float f7 = 360.0F / i;

      for (float f2 = 0.0F; f2 <= 360.0F; f2 += f7) {
         double d5 = Math.toRadians(f2);
         double d6 = Math.sin(d5) * d3;
         double d7 = Math.cos(d5) * d4;
         bufferBuilder.vertex(matrix4f, (float)(d + d6), (float)(d2 + d7), f).color(f3, f4, f5, f6);
      }

      run3();
      runnable.run();
      BufferUtils.handleBufferBuilder(bufferBuilder);
      run();
   }

   public static void handleMatrixStack25(MatrixStack matrixStack, float f, float f2, float f3, float f4, Color color, Color color2, float f5) {
      Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
      run3();
      RenderSystem.blendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA, SrcFactor.ONE, DstFactor.ZERO);
      RenderSystem.setShader(GameRenderer::getPositionColorProgram);
      BufferBuilder bufferBuilder = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
      bufferBuilder.vertex(matrix4f, f, f2, f5).color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
      bufferBuilder.vertex(matrix4f, f, f4, f5)
         .color(color2.getRed() / 255.0F, color2.getGreen() / 255.0F, color2.getBlue() / 255.0F, color2.getAlpha() / 255.0F);
      bufferBuilder.vertex(matrix4f, f3, f4, f5)
         .color(color2.getRed() / 255.0F, color2.getGreen() / 255.0F, color2.getBlue() / 255.0F, color2.getAlpha() / 255.0F);
      bufferBuilder.vertex(matrix4f, f3, f2, f5).color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
      BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
      run();
   }

   public static void handleMatrixStack26(
      MatrixStack matrixStack, float f, float f2, float f3, float f4, float f5, Color color, Color color2, Color color3, Color color4
   ) {
      handleMatrixStack34(matrixStack, f, f2, f3, f4, f5, 0.0F, 1.0F, color, color2, color3, color4);
   }

   public static void handleMatrix4f3(
      Matrix4f matrix4f, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13
   ) {
      BufferBuilder bufferBuilder = Tessellator.getInstance().begin(DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
      handleFloat4(f7 - f12, f8 - f12, f12, 0);
      handleFloat4(f7 - f10, f6 + f10, f10, 1);
      handleFloat4(f5 + f9, f6 + f9, f9, 2);
      handleFloat4(f5 + f11, f8 - f11, f11, 3);

      for (int i = 0; i < 4; i++) {
         float[] floats = floatArray[i];
         float f18 = floats[2];
         float f17 = i * 90.0F;

         while (f17 <= (i + 1) * 90.0F) {
            float f14 = Math.toRadians(f17);
            float f15 = Math.sin(f14) * f18;
            float f16 = Math.cos(f14) * f18;
            bufferBuilder.vertex(matrix4f, floats[0] + f15, floats[1] + f16, 0.0F).color(f, f2, f3, f4);
            f17 += 90.0F / f13;
         }
      }

      BufferUtils.handleBufferBuilder(bufferBuilder);
   }

   public static void handleMatrixStack27(
      MatrixStack matrixStack,
      float f,
      float f2,
      float f3,
      float f4,
      float f5,
      float f6,
      float f7,
      float f8,
      Color color,
      Color color2,
      Color color3,
      Color color4
   ) {
      run3();
      BufferBuilder bufferBuilder = resolveBufferBuilder(
         matrixStack.peek().getPositionMatrix(),
         Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION),
         f - 10.0F,
         f2 - 10.0F,
         f3 + 20.0F,
         f4 + 20.0F,
         f5
      );
      handleFloat(f, f2, f3, f4, f8, f6, f7, color, color2, color3, color4);
      BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
      run();
   }

   public static void handleMatrixStack28(MatrixStack matrixStack, Color color, double d, double d2, double d3, double d4, int i, float f) {
      handleMatrixStack24(matrixStack, color, d, d2, d3, d4, i, f, () -> RenderSystem.setShader(GameRenderer::getPositionColorProgram));
   }

   public static void handleMatrixStack29(MatrixStack matrixStack, Color color, double d, double d2, double d3, double d4, int i) {
      handleMatrixStack28(matrixStack, color, d, d2, d3, d4, i, 0.0F);
   }

   public static void handleMatrixStack30(MatrixStack matrixStack, float f, float f2, float f3, float f4, Color color, Color color2) {
      handleMatrixStack25(matrixStack, f, f2, f3, f4, color, color2, 0.0F);
   }

   public static void handleMatrixStack31(MatrixStack matrixStack, float f, float f2, float f3, float f4, Color color, Color color2, float f5) {
      Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
      run3();
      RenderSystem.blendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA, SrcFactor.ONE, DstFactor.ZERO);
      RenderSystem.setShader(GameRenderer::getPositionColorProgram);
      BufferBuilder bufferBuilder = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
      bufferBuilder.vertex(matrix4f, f, f2, f5).color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
      bufferBuilder.vertex(matrix4f, f, f4, f5).color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
      bufferBuilder.vertex(matrix4f, f3, f4, f5)
         .color(color2.getRed() / 255.0F, color2.getGreen() / 255.0F, color2.getBlue() / 255.0F, color2.getAlpha() / 255.0F);
      bufferBuilder.vertex(matrix4f, f3, f2, f5)
         .color(color2.getRed() / 255.0F, color2.getGreen() / 255.0F, color2.getBlue() / 255.0F, color2.getAlpha() / 255.0F);
      BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
      run();
   }

   public static void handleMatrixStack32(MatrixStack matrixStack, float f, float f2, float f3, float f4, Color color, Color color2) {
      handleMatrixStack31(matrixStack, f, f2, f3, f4, color, color2, 0.0F);
   }

   public static void handleMatrixStack33(
      MatrixStack matrixStack, Color color, double d, double d2, double d3, double d4, float f, float f2, float f3, float f4, float f5, Runnable runnable
   ) {
      Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
      float f6 = color.getRed() / 255.0F;
      float f7 = color.getGreen() / 255.0F;
      float f8 = color.getBlue() / 255.0F;
      float f9 = color.getAlpha() / 255.0F;
      run3();
      runnable.run();
      handleMatrix4f3(matrix4f, f6, f7, f8, f9, (float)d, (float)d2, (float)d3, (float)d4, f, f2, f3, f4, f5);
      run();
   }

   public static void handleMatrixStack34(
      MatrixStack matrixStack, float f, float f2, float f3, float f4, float f5, float f6, float f7, Color color, Color color2, Color color3, Color color4
   ) {
      handleMatrixStack27(matrixStack, f, f2, f3, f4, 0.0F, f5, f6, f7, color, color2, color3, color4);
   }

   public static BufferBuilder resolveBufferBuilder(Matrix4f matrix4f, BufferBuilder bufferBuilder, float f, float f2, float f3, float f4, float f5) {
      bufferBuilder.vertex(matrix4f, f, f2, f5);
      bufferBuilder.vertex(matrix4f, f, f2 + f4, f5);
      bufferBuilder.vertex(matrix4f, f + f3, f2 + f4, f5);
      bufferBuilder.vertex(matrix4f, f + f3, f2, f5);
      return bufferBuilder;
   }

   public static void handleMatrixStack35(MatrixStack matrixStack, Color color, float f, float f2, float f3, float f4, Runnable runnable) {
      float f5 = color.getRed() / 255.0F;
      float f6 = color.getGreen() / 255.0F;
      float f7 = color.getBlue() / 255.0F;
      float f8 = color.getAlpha() / 255.0F;
      Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
      BufferBuilder bufferBuilder = Tessellator.getInstance().begin(DrawMode.LINES, VertexFormats.POSITION_COLOR);

      for (int i = 0; i <= 360; i++) {
         bufferBuilder.vertex(matrix4f, (float)(f + Math.sin(i * 3.141526 / 180.0) * f3), (float)(f2 + Math.cos(i * 3.141526 / 180.0) * f3), 0.0F)
            .color(f5, f6, f7, f8);
      }

      run3();
      runnable.run();
      BufferUtils.handleBufferBuilder(bufferBuilder);
      run();
   }

   public static void handleMatrixStack36(MatrixStack matrixStack, float f, float f2, float f3, float f4, float f5, float f6, float f7, Color color) {
      handleMatrixStack34(matrixStack, f, f2, f3, f4, f5, f6, f7, color, color, color, color);
   }

   public static void handleMatrixStack37(MatrixStack matrixStack, Color color, float f, float f2, float f3, float f4) {
      handleMatrixStack35(matrixStack, color, f, f2, f3, f4, () -> RenderSystem.setShader(GameRenderer::getPositionColorProgram));
   }

   public static void handleMatrixStack38(
      MatrixStack matrixStack, Color color, double d, double d2, double d3, double d4, float f, float f2, float f3, float f4, float f5
   ) {
      handleMatrixStack33(matrixStack, color, d, d2, d3, d4, f, f2, f3, f4, f5, () -> RenderSystem.setShader(GameRenderer::getPositionColorProgram));
   }

   public static void handleDrawContext(DrawContext drawContext, ItemStack itemStack, int i, int i2, Color color) {
      MatrixStack matrixStack = drawContext.getMatrices();
      if (!itemStack.isEmpty()) {
         BakedModel bakedModel = MinecraftHolder.mc.getItemRenderer().getModel(itemStack, MinecraftHolder.mc.world, MinecraftHolder.mc.player, 0);
         matrixStack.push();
         matrixStack.translate(i + 8, i2 + 8, 150.0F);

         try {
            matrixStack.scale(16.0F, -16.0F, 16.0F);
            boolean flag = !bakedModel.isSideLit();
            if (flag) {
               DiffuseLighting.disableGuiDepthLighting();
            }

            handleItemStack(itemStack, matrixStack, drawContext.getVertexConsumers(), 15728880, color, bakedModel);
            drawContext.draw();
            if (flag) {
               DiffuseLighting.enableGuiDepthLighting();
            }
         } catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.create(throwable, "Phobos item rendering");
            CrashReportSection crashReportSection = crashReport.addElement("Item being rendered");
            crashReportSection.add("Item Type", () -> String.valueOf(itemStack.getItem()));
            crashReportSection.add("Item Components", () -> String.valueOf(itemStack.getComponents()));
            crashReportSection.add("Item Foil", () -> String.valueOf(itemStack.hasGlint()));
            throw new CrashException(crashReport);
         }

         matrixStack.pop();
      }
   }

   public static void handleBakedModel(BakedModel bakedModel, ItemStack itemStack, int i, Color color, MatrixStack matrixStack, VertexConsumer vertexConsumer) {
      Random random = Random.create();
      long l = 42L;

      for (Direction direction : Direction.values()) {
         random.setSeed(42L);
         handleMatrixStack39(matrixStack, vertexConsumer, bakedModel.getQuads(null, direction, random), itemStack, i, color);
      }

      random.setSeed(42L);
      handleMatrixStack39(matrixStack, vertexConsumer, bakedModel.getQuads(null, null, random), itemStack, i, color);
   }

   public RenderUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static void handleFloat4(float f, float f2, float f3, int i) {
      floatArray[i][0] = f;
      floatArray[i][1] = f2;
      floatArray[i][2] = f3;
   }

   public static void handleItemStack(
      ItemStack itemStack, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, Color color, BakedModel bakedModel
   ) {
      if (!itemStack.isEmpty()) {
         matrixStack.push();
         if (itemStack.isOf(Items.TRIDENT)) {
            bakedModel = MinecraftHolder.mc.getItemRenderer().getModels().getModelManager().getModel(ItemRenderer.TRIDENT);
         } else if (itemStack.isOf(Items.SPYGLASS)) {
            bakedModel = MinecraftHolder.mc.getItemRenderer().getModels().getModelManager().getModel(ItemRenderer.SPYGLASS);
         }

         bakedModel.getTransformation().getTransformation(ModelTransformationMode.GUI).apply(false, matrixStack);
         matrixStack.translate(-0.5F, -0.5F, -0.5F);
         if (bakedModel.isBuiltin()) {
            MinecraftHolder.mc
               .getItemRenderer()
               .builtinModelItemRenderer
               .render(itemStack, ModelTransformationMode.GUI, matrixStack, vertexConsumerProvider, i, color.hashCode());
         } else {
            RenderLayer renderLayer = color.getAlpha() != 255 ? BlockRenderLayers.resolveRenderLayer3(itemStack, true) : RenderLayers.getItemLayer(itemStack, true);
            VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumerProvider, renderLayer, true, false);
            handleBakedModel(bakedModel, itemStack, i, color, matrixStack, vertexConsumer);
         }

         matrixStack.pop();
      }
   }

   public static void handleMatrixStack39(MatrixStack matrixStack, VertexConsumer vertexConsumer, List<BakedQuad> list, ItemStack itemStack, int i, Color color) {
      boolean flag = !itemStack.isEmpty();
      Entry entry = matrixStack.peek();

      for (BakedQuad bakedQuad : list) {
         float f = color.getAlpha() / 255.0F;
         float f2 = color.getRed() / 255.0F;
         float f3 = color.getGreen() / 255.0F;
         float f4 = color.getBlue() / 255.0F;
         vertexConsumer.quad(entry, bakedQuad, f2, f3, f4, f, i, OverlayTexture.DEFAULT_UV);
      }
   }
}
