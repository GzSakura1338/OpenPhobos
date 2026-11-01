package org.phobos.core.render.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.phobos.api.FloatUniform1;
import org.phobos.api.FloatUniform2;
import org.phobos.api.ShaderRegistry;
import org.phobos.api.FloatUniform4;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.ShaderProgramHandle;

public class GradientRoundedRectShader implements MinecraftHolder {
   public FloatUniform4 floatUniform4;
   public FloatUniform1 floatUniform1;
   public FloatUniform2 floatUniform2;
   public static final ShaderProgramHandle shaderProgramHandle = ShaderRegistry.getINSTANCE().resolveShaderProgramHandle2(Identifier.of("phoboslite", "hud"), VertexFormats.POSITION);
   public FloatUniform1 floatUniform12;
   public FloatUniform4 floatUniform42;
   public FloatUniform4 floatUniform43;
   public FloatUniform1 floatUniform13;
   public FloatUniform2 floatUniform22;
   public FloatUniform1 floatUniform14;
   public FloatUniform1 floatUniform15;
   public FloatUniform4 floatUniform44;

   public GradientRoundedRectShader() {
      this.run();
   }

   public void run3() {
      RenderSystem.setShader(shaderProgramHandle::getShaderProgram);
   }

   public void handleFloat(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, Color color, Color color2, Color color3, Color color4) {
      this.handleFloat2(f, f2, f3, f4, f5, f6, f7, f8, 0.0F, color, color2, color3, color4);
   }

   public void handleFloat2(
      float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, Color color, Color color2, Color color3, Color color4
   ) {
      float f10 = (float)mc.getWindow().getScaleFactor();
      this.floatUniform15.handleFloat5(f5 * f10);
      this.floatUniform22.handleFloat(f * f10, -f2 * f10 + mc.getWindow().getScaledHeight() * f10 - f4 * f10);
      this.floatUniform2.handleFloat(f3 * f10, f4 * f10);
      this.floatUniform43.handleFloat3(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, f6);
      this.floatUniform44.handleFloat3(color2.getRed() / 255.0F, color2.getGreen() / 255.0F, color2.getBlue() / 255.0F, f6);
      this.floatUniform42.handleFloat3(color3.getRed() / 255.0F, color3.getGreen() / 255.0F, color3.getBlue() / 255.0F, f6);
      this.floatUniform4.handleFloat3(color4.getRed() / 255.0F, color4.getGreen() / 255.0F, color4.getBlue() / 255.0F, f6);
      this.floatUniform1.handleFloat5(1.0F);
      this.floatUniform14.handleFloat5(f9);
      this.floatUniform13.handleFloat5(f8);
      this.floatUniform12.handleFloat5(f7);
   }

   public void run() {
      this.floatUniform2 = shaderProgramHandle.resolveFloatUniform2("uSize");
      this.floatUniform22 = shaderProgramHandle.resolveFloatUniform2("uLocation");
      this.floatUniform15 = shaderProgramHandle.resolveFloatUniform1("radius");
      this.floatUniform1 = shaderProgramHandle.resolveFloatUniform1("blend");
      this.floatUniform12 = shaderProgramHandle.resolveFloatUniform1("alpha");
      this.floatUniform43 = shaderProgramHandle.resolveFloatUniform4("color1");
      this.floatUniform44 = shaderProgramHandle.resolveFloatUniform4("color2");
      this.floatUniform42 = shaderProgramHandle.resolveFloatUniform4("color3");
      this.floatUniform4 = shaderProgramHandle.resolveFloatUniform4("color4");
      this.floatUniform14 = shaderProgramHandle.resolveFloatUniform1("outline");
      this.floatUniform13 = shaderProgramHandle.resolveFloatUniform1("glow");
   }
}
