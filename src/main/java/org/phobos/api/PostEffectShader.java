package org.phobos.api;

import java.awt.Color;
import net.minecraft.client.gl.PostEffectProcessor;
import org.joml.Matrix4f;

public interface PostEffectShader extends UniformLookup {
   void run();

   default void handleText7(String text, Color color) {
      this.handleText(text, color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
   }

   void handleText5(String var1, float var2, float var3);

   void handleFloat(float var1);

   FramebufferHolder resolveFramebufferHolder(String var1);

   SamplerUniform resolveTextureUniform(String var1);

   PostEffectProcessor getPostEffectProcessor();

   void handleText3(String var1, int var2);

   void handleText6(String var1, float var2, float var3, float var4);

   void handleText2(String var1, float var2);

   default void handleText8(String text, Color color) {
      this.handleText6(text, color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F);
   }

   void handleText(String var1, float var2, float var3, float var4, float var5);

   void handleText4(String var1, Matrix4f var2);
}
