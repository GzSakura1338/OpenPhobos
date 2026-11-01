package org.phobos.core.render;

import com.google.common.collect.Lists;
import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import java.awt.Color;
import java.util.List;
import net.minecraft.client.font.EmptyGlyphRenderer;
import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.Glyph;
import net.minecraft.client.font.GlyphRenderer;
import net.minecraft.client.font.GlyphRenderer.Rectangle;
import net.minecraft.client.font.TextRenderer.TextLayerType;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.CharacterVisitor;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.TextVisitFactory;
import net.minecraft.util.Language;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.phobos.api.MinecraftHolder;
import org.phobos.mixin.game.render.interfaces.ITessellator;
import org.phobos.mixin.game.render.interfaces.ITextRenderer;

public class Text3dRenderer implements MinecraftHolder {
   public static final Vector3f vector3f = new Vector3f(0.0F, 0.0F, 0.03F);

   public int computeText(
      Text text,
      float f,
      float f2,
      int i,
      boolean flag,
      Matrix4f matrix4f,
      VertexConsumerProvider vertexConsumerProvider,
      TextLayerType textLayerType,
      int i2,
      int i3
   ) {
      return this.computeOrderedText3(text.asOrderedText(), f, f2, i, flag, matrix4f, vertexConsumerProvider, textLayerType, i2, i3);
   }

   public static int computeInt(int i) {
      return (i & -67108864) == 0 ? i | 0xFF000000 : i;
   }

   public float computeText2(
      String text,
      float f,
      float f2,
      int i,
      boolean flag,
      Matrix4f matrix4f,
      VertexConsumerProvider vertexConsumerProvider,
      TextLayerType textLayerType,
      int i2,
      int i3
   ) {
      Text3dRenderer.Drawer drawer = new Text3dRenderer.Drawer(vertexConsumerProvider, f, f2, i, flag, matrix4f, textLayerType, i3);
      TextVisitFactory.visitFormatted(text, Style.EMPTY, drawer);
      return drawer.computeInt(i2, f);
   }

   public String describeText(String text) {
      try {
         Bidi bidi = new Bidi(new ArabicShaping(8).shape(text), 127);
         bidi.setReorderingMode(0);
         return bidi.writeReordered(2);
      } catch (ArabicShapingException arabicShapingException) {
         return text;
      }
   }

   public void handleMatrixStack(MatrixStack matrixStack, String text, float f, float f2, int i) {
      this.handleMatrixStack3(matrixStack, text, f, f2, i, false);
   }

   public void handleGlyphRenderer(
      GlyphRenderer glyphRenderer,
      boolean flag,
      boolean flag2,
      float f,
      float f2,
      float f3,
      Matrix4f matrix4f,
      VertexConsumer vertexConsumer,
      float f4,
      float f5,
      float f6,
      float f7,
      int i
   ) {
      glyphRenderer.draw(flag2, f2, f3, matrix4f, vertexConsumer, f4, f5, f6, f7, i);
      if (flag) {
         glyphRenderer.draw(flag2, f2 + f, f3, matrix4f, vertexConsumer, f4, f5, f6, f7, i);
      }
   }

   public void handleText(
      String text,
      float f,
      float f2,
      int i,
      boolean flag,
      Matrix4f matrix4f,
      VertexConsumerProvider vertexConsumerProvider,
      TextLayerType textLayerType,
      int i2,
      int i3,
      boolean flag2
   ) {
      this.handleText2(text, f, f2, i, flag, matrix4f, vertexConsumerProvider, textLayerType, i2, i3, flag2);
   }

   public void handleMatrixStack2(MatrixStack matrixStack, String text, float f, float f2, int i) {
      if (text != null) {
         Immediate immediate = VertexConsumerProvider.immediate(((ITessellator)(Object)Tessellator.getInstance()).getAllocator());
         this.handleText3(text, f, f2, i, true, matrixStack.peek().getPositionMatrix(), immediate, TextLayerType.SEE_THROUGH, 0, 15728880);
         this.handleText3(text, f, f2, i, false, matrixStack.peek().getPositionMatrix(), immediate, TextLayerType.POLYGON_OFFSET, 0, 15728880);
         immediate.draw();
      }
   }

   public void handleText2(
      String text,
      float f,
      float f2,
      int i,
      boolean flag,
      Matrix4f matrix4f,
      VertexConsumerProvider vertexConsumerProvider,
      TextLayerType textLayerType,
      int i2,
      int i3,
      boolean flag2
   ) {
      if (flag2) {
         text = this.describeText(text);
      }

      i = computeInt(i);
      Matrix4f matrix4f2 = new Matrix4f(matrix4f);
      this.computeText2(text, f, f2, i, flag, matrix4f2, vertexConsumerProvider, textLayerType, i2, i3);
   }

   public void handleMatrixStack3(MatrixStack matrixStack, String text, float f, float f2, int i, boolean flag) {
      if (text != null) {
         Immediate immediate = VertexConsumerProvider.immediate(((ITessellator)(Object)Tessellator.getInstance()).getAllocator());
         if (flag) {
            this.handleMatrixStack2(matrixStack, text, f, f2, i);
         } else {
            this.handleText3(text, f, f2, i, false, matrixStack.peek().getPositionMatrix(), immediate, TextLayerType.NORMAL, 0, 15728880);
            immediate.draw();
         }
      }
   }

   public void handleOrderedText(
      OrderedText orderedText, float f, float f2, int i, int i2, Matrix4f matrix4f, VertexConsumerProvider vertexConsumerProvider, int i3
   ) {
      int i8 = computeInt(i2);
      Text3dRenderer.Drawer drawer = new Text3dRenderer.Drawer(vertexConsumerProvider, 0.0F, 0.0F, i8, false, matrix4f, TextLayerType.NORMAL, i3);

      for (int i7 = -1; i7 <= 1; i7++) {
         for (int i6 = -1; i6 <= 1; i6++) {
            if (i7 != 0 || i6 != 0) {
               float[] floats = new float[]{f};
               int i4 = i7;
               int i5 = i6;
               orderedText.accept((i4x, style, i5x) -> {
                  boolean flag = style.isBold();
                  FontStorage fontStorage = ((ITextRenderer)(Object)mc.textRenderer).invokeGetFontStorage(style.getFont());
                  Glyph glyph = fontStorage.getGlyph(i5x, ((ITextRenderer)(Object)mc.textRenderer).getValidateAdvance());
                  drawer.floatValue3 = floats[0] + i4 * glyph.getShadowOffset();
                  drawer.floatValue6 = f2 + i5 * glyph.getShadowOffset();
                  floats[0] += glyph.getAdvance(flag);
                  return drawer.accept(i4x, style.withColor(i8), i5x);
               });
            }
         }
      }

      Text3dRenderer.Drawer drawer2 = new Text3dRenderer.Drawer(vertexConsumerProvider, f, f2, computeInt(i), false, matrix4f, TextLayerType.POLYGON_OFFSET, i3);
      orderedText.accept(drawer2);
      drawer2.computeInt(0, f);
   }

   public float computeOrderedText(
      OrderedText orderedText,
      float f,
      float f2,
      int i,
      boolean flag,
      Matrix4f matrix4f,
      VertexConsumerProvider vertexConsumerProvider,
      TextLayerType textLayerType,
      int i2,
      int i3
   ) {
      Text3dRenderer.Drawer drawer = new Text3dRenderer.Drawer(vertexConsumerProvider, f, f2, i, flag, matrix4f, textLayerType, i3);
      orderedText.accept(drawer);
      return drawer.computeInt(i2, f);
   }

   public boolean check() {
      return Language.getInstance().isRightToLeft();
   }

   public void handleText3(
      String text,
      float f,
      float f2,
      int i,
      boolean flag,
      Matrix4f matrix4f,
      VertexConsumerProvider vertexConsumerProvider,
      TextLayerType textLayerType,
      int i2,
      int i3
   ) {
      this.handleText(text, f, f2, i, flag, matrix4f, vertexConsumerProvider, textLayerType, i2, i3, this.check());
   }

   public int computeOrderedText2(
      OrderedText orderedText,
      float f,
      float f2,
      int i,
      boolean flag,
      Matrix4f matrix4f,
      VertexConsumerProvider vertexConsumerProvider,
      TextLayerType textLayerType,
      int i2,
      int i3
   ) {
      i = computeInt(i);
      Matrix4f matrix4f2 = new Matrix4f(matrix4f);
      if (flag) {
         this.computeOrderedText(orderedText, f, f2, i, true, matrix4f, vertexConsumerProvider, textLayerType, i2, i3);
         matrix4f2.translate(vector3f);
      }

      f = this.computeOrderedText(orderedText, f, f2, i, false, matrix4f2, vertexConsumerProvider, textLayerType, i2, i3);
      return (int)f + (flag ? 1 : 0);
   }

   public int computeInt2(int i) {
      Color color = new Color(i);
      return new Color(color.getRed() / 255.0F * 0.25F, color.getGreen() / 255.0F * 0.25F, color.getBlue() / 255.0F * 0.25F, color.getAlpha() / 255.0F)
         .hashCode();
   }

   public int computeOrderedText3(
      OrderedText orderedText,
      float f,
      float f2,
      int i,
      boolean flag,
      Matrix4f matrix4f,
      VertexConsumerProvider vertexConsumerProvider,
      TextLayerType textLayerType,
      int i2,
      int i3
   ) {
      return this.computeOrderedText2(orderedText, f, f2, i, flag, matrix4f, vertexConsumerProvider, textLayerType, i2, i3);
   }

   private static class Drawer implements CharacterVisitor {
      public final VertexConsumerProvider vertexConsumerProvider;
      public final float floatValue;
      public final float floatValue2;
      public float floatValue3;
      public final float floatValue4;
      public final float floatValue5;
      @Nullable
      public List<Rectangle> rectangleList;
      public final Matrix4f matrix4f;
      public float floatValue6;
      public final TextLayerType textLayerType;
      public final float floatValue7;
      public final int intValue;
      public final boolean flag;

      public float computeInt(int i, float f) {
         if (i != 0) {
            float f2 = (i >> 24 & 0xFF) / 255.0F;
            float f3 = (i >> 16 & 0xFF) / 255.0F;
            float f4 = (i >> 8 & 0xFF) / 255.0F;
            float f5 = (i & 0xFF) / 255.0F;
            this.handleRectangle(new Rectangle(f - 1.0F, this.floatValue6 + 9.0F, this.floatValue3 + 1.0F, this.floatValue6 - 1.0F, 0.01F, f3, f4, f5, f2));
         }

         if (this.rectangleList != null) {
            GlyphRenderer glyphRenderer = ((ITextRenderer)(Object)MinecraftHolder.mc.textRenderer).invokeGetFontStorage(Style.DEFAULT_FONT_ID).getRectangleRenderer();
            VertexConsumer vertexConsumer = this.vertexConsumerProvider.getBuffer(glyphRenderer.getLayer(this.textLayerType));

            for (Rectangle rectangle : this.rectangleList) {
               glyphRenderer.drawRectangle(rectangle, this.matrix4f, vertexConsumer, this.intValue);
            }
         }

         return this.floatValue3;
      }

      public boolean accept(int i, Style style, int i2) {
         FontStorage fontStorage = ((ITextRenderer)(Object)MinecraftHolder.mc.textRenderer).invokeGetFontStorage(style.getFont());
         Glyph glyph = fontStorage.getGlyph(i2, ((ITextRenderer)(Object)MinecraftHolder.mc.textRenderer).getValidateAdvance());
         GlyphRenderer glyphRenderer = style.isObfuscated() && i2 != 32 ? fontStorage.getObfuscatedGlyphRenderer(glyph) : fontStorage.getGlyphRenderer(i2);
         boolean flag = style.isBold();
         float f6 = this.floatValue5;
         TextColor textColor = style.getColor();
         float f;
         float f2;
         float f3;
         if (textColor != null) {
            int i3 = textColor.getRgb();
            f = (i3 >> 16 & 0xFF) / 255.0F * this.floatValue7;
            f2 = (i3 >> 8 & 0xFF) / 255.0F * this.floatValue7;
            f3 = (i3 & 0xFF) / 255.0F * this.floatValue7;
         } else {
            f = this.floatValue2;
            f2 = this.floatValue4;
            f3 = this.floatValue;
         }

         if (!(glyphRenderer instanceof EmptyGlyphRenderer)) {
            float f5 = flag ? glyph.getBoldOffset() : 0.0F;
            float f4 = this.flag ? glyph.getShadowOffset() : 0.0F;
            VertexConsumer vertexConsumer = this.vertexConsumerProvider.getBuffer(glyphRenderer.getLayer(this.textLayerType));
            ((ITextRenderer)(Object)MinecraftHolder.mc.textRenderer)
               .invokeDrawGlyph(
                  glyphRenderer,
                  flag,
                  style.isItalic(),
                  f5,
                  this.floatValue3 + f4,
                  this.floatValue6 + f4,
                  this.matrix4f,
                  vertexConsumer,
                  f,
                  f2,
                  f3,
                  f6,
                  this.intValue
               );
         }

         float f11 = glyph.getAdvance(flag);
         float f10 = this.flag ? 1.0F : 0.0F;
         if (style.isStrikethrough()) {
            this.handleRectangle(
               new Rectangle(
                  this.floatValue3 + f10 - 1.0F,
                  this.floatValue6 + f10 + 4.5F,
                  this.floatValue3 + f10 + f11,
                  this.floatValue6 + f10 + 4.5F - 1.0F,
                  0.01F,
                  f,
                  f2,
                  f3,
                  f6
               )
            );
         }

         if (style.isUnderlined()) {
            this.handleRectangle(
               new Rectangle(
                  this.floatValue3 + f10 - 1.0F,
                  this.floatValue6 + f10 + 9.0F,
                  this.floatValue3 + f10 + f11,
                  this.floatValue6 + f10 + 9.0F - 1.0F,
                  0.01F,
                  f,
                  f2,
                  f3,
                  f6
               )
            );
         }

         this.floatValue3 += f11;
         return true;
      }

      public Drawer(
         VertexConsumerProvider vertexConsumerProvider, float f, float f2, int i, boolean flag, Matrix4f matrix4f, TextLayerType textLayerType, int i2
      ) {
         this.vertexConsumerProvider = vertexConsumerProvider;
         this.floatValue3 = f;
         this.floatValue6 = f2;
         this.flag = flag;
         this.floatValue7 = flag ? 0.25F : 1.0F;
         this.floatValue2 = (i >> 16 & 0xFF) / 255.0F * this.floatValue7;
         this.floatValue4 = (i >> 8 & 0xFF) / 255.0F * this.floatValue7;
         this.floatValue = (i & 0xFF) / 255.0F * this.floatValue7;
         this.floatValue5 = (i >> 24 & 0xFF) / 255.0F;
         this.matrix4f = matrix4f;
         this.textLayerType = textLayerType;
         this.intValue = i2;
      }

      public void handleRectangle(Rectangle rectangle) {
         if (this.rectangleList == null) {
            this.rectangleList = Lists.newArrayList();
         }

         this.rectangleList.add(rectangle);
      }
   }
}
