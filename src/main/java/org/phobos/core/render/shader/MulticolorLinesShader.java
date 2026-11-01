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
import org.phobos.api.ColoredUniformOwner;
import org.phobos.api.ShaderProgramHandle;
import org.phobos.core.render.ColorUtils;

public class MulticolorLinesShader implements ColoredUniformOwner, MinecraftHolder {
   public FloatUniform1 floatUniform1;
   public FloatUniform2 floatUniform2;
   public FloatUniform4 floatUniform4;
   public FloatUniform4 floatUniform42;
   public static final ShaderProgramHandle shaderProgramHandle = ShaderRegistry.getINSTANCE()
      .resolveShaderProgramHandle2(Identifier.of("phoboslite", "multicolor_lines"), VertexFormats.LINES);

   @Override
   public void handleColor(Color color, Color color2) {
      this.floatUniform2.handleFloat(mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
      this.floatUniform42.handleVector4f(ColorUtils.createVector4f(color));
      this.floatUniform4.handleVector4f(ColorUtils.createVector4f(color2));
      this.run6();
   }

   @Override
   public void run3() {
      RenderSystem.setShader(shaderProgramHandle::getShaderProgram);
   }

   public FloatUniform4 getFloatUniform42() {
      return this.floatUniform4;
   }

   public FloatUniform4 getFloatUniform4() {
      return this.floatUniform42;
   }

   public void run() {
      this.floatUniform2 = shaderProgramHandle.resolveFloatUniform2("resolution");
      this.floatUniform42 = shaderProgramHandle.resolveFloatUniform4("color1");
      this.floatUniform4 = shaderProgramHandle.resolveFloatUniform4("color2");
      this.floatUniform1 = shaderProgramHandle.resolveFloatUniform1("time");
   }

   public MulticolorLinesShader() {
      this.run();
   }

   @Override
   public FloatUniform1 getFloatUniform1() {
      return this.floatUniform1;
   }

   public FloatUniform2 getFloatUniform2() {
      return this.floatUniform2;
   }
}
