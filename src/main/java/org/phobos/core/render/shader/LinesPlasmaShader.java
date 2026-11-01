package org.phobos.core.render.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.phobos.api.FloatUniform1;
import org.phobos.api.FloatUniform2;
import org.phobos.api.KeyValue;
import org.phobos.api.ShaderRegistry;
import org.phobos.api.FloatUniform4;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.ShaderProgramHandle;
import org.phobos.core.render.ColorUtils;

public class LinesPlasmaShader implements MinecraftHolder {
   public FloatUniform1 floatUniform1;
   public static final ShaderProgramHandle shaderProgramHandle = ShaderRegistry.getINSTANCE()
      .resolveShaderProgramHandle2(Identifier.of("phoboslite", "rendertype_lines_plasma"), VertexFormats.LINES);
   public FloatUniform4 floatUniform4;
   public FloatUniform2 floatUniform2;
   public FloatUniform4 floatUniform42;

   public void run3() {
      RenderSystem.setShader(shaderProgramHandle::getShaderProgram);
   }

   public void handleKeyValue(KeyValue<Color, Color> keyValue) {
      this.floatUniform1.handleFloat5(ShaderEffectManager.floatValue * 0.1F);
      this.floatUniform4.handleVector4f(ColorUtils.createVector4f(keyValue.getObject()));
      this.floatUniform42.handleVector4f(ColorUtils.createVector4f(keyValue.getObject2()));
      this.floatUniform2.handleFloat(mc.getWindow().getScaledWidth() * 2.0F, mc.getWindow().getScaledHeight() * 2.0F);
   }

   public LinesPlasmaShader() {
      this.run();
   }

   public void run() {
      this.floatUniform1 = shaderProgramHandle.resolveFloatUniform1("time");
      this.floatUniform4 = shaderProgramHandle.resolveFloatUniform4("color1");
      this.floatUniform42 = shaderProgramHandle.resolveFloatUniform4("color2");
      this.floatUniform2 = shaderProgramHandle.resolveFloatUniform2("resolution");
   }
}
