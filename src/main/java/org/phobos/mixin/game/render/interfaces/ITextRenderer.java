package org.phobos.mixin.game.render.interfaces;

import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.GlyphRenderer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TextRenderer.class)
public interface ITextRenderer {
   @Accessor("validateAdvance")
   boolean getValidateAdvance();

   @Invoker("getFontStorage")
   FontStorage invokeGetFontStorage(Identifier var1);

   @Invoker("drawGlyph")
   void invokeDrawGlyph(
      GlyphRenderer var1,
      boolean var2,
      boolean var3,
      float var4,
      float var5,
      float var6,
      Matrix4f var7,
      VertexConsumer var8,
      float var9,
      float var10,
      float var11,
      float var12,
      int var13
   );
}
