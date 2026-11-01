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

public class RoundedRectShader implements MinecraftHolder {
   public FloatUniform2 floatUniform2;
   public FloatUniform1 floatUniform1;
   public static final ShaderProgramHandle shaderProgramHandle = ShaderRegistry.getINSTANCE().resolveShaderProgramHandle2(Identifier.of("phoboslite", "hud"), VertexFormats.POSITION);
   public FloatUniform4 floatUniform4;
   public FloatUniform2 floatUniform22;

   public RoundedRectShader() {
      this.run();
   }

   public void handleFloat(float f, float f2, float f3, float f4, float f5, Color color) {
      float f6 = (float)mc.getWindow().getScaleFactor();
      this.floatUniform1.handleFloat5(f5 * f6);
      this.floatUniform22.handleFloat(f * f6, -f2 * f6 + mc.getWindow().getScaledHeight() * f6 - f4 * f6);
      this.floatUniform2.handleFloat(f3 * f6, f4 * f6);
      this.floatUniform4.handleFloat3(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
   }

   public void run3() {
      RenderSystem.setShader(shaderProgramHandle::getShaderProgram);
   }

   public void run() {
      this.floatUniform2 = shaderProgramHandle.resolveFloatUniform2("uSize");
      this.floatUniform22 = shaderProgramHandle.resolveFloatUniform2("uLocation");
      this.floatUniform1 = shaderProgramHandle.resolveFloatUniform1("radius");
      this.floatUniform4 = shaderProgramHandle.resolveFloatUniform4("color");
   }
}
