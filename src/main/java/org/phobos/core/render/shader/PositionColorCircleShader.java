package org.phobos.core.render.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.phobos.api.FloatUniform1;
import org.phobos.api.KeyValue;
import org.phobos.api.ShaderRegistry;
import org.phobos.api.FloatUniform4;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.ShaderProgramHandle;
import org.phobos.core.render.ColorUtils;

public class PositionColorCircleShader implements MinecraftHolder {
   public FloatUniform1 floatUniform1;
   public FloatUniform4 floatUniform4;
   public FloatUniform4 floatUniform42;
   public static final ShaderProgramHandle shaderProgramHandle = ShaderRegistry.getINSTANCE()
      .resolveShaderProgramHandle2(Identifier.of("phoboslite", "position_color_circle"), VertexFormats.POSITION_COLOR);
   public FloatUniform1 floatUniform12;

   public void run() {
      this.floatUniform12 = shaderProgramHandle.resolveFloatUniform1("time");
      this.floatUniform1 = shaderProgramHandle.resolveFloatUniform1("steps");
      this.floatUniform4 = shaderProgramHandle.resolveFloatUniform4("color1");
      this.floatUniform42 = shaderProgramHandle.resolveFloatUniform4("color2");
   }

   public void handleKeyValue(KeyValue<Color, Color> keyValue, float f) {
      this.floatUniform12.handleFloat5(ShaderEffectManager.floatValue);
      this.floatUniform4.handleVector4f(ColorUtils.createVector4f(keyValue.getObject()));
      this.floatUniform42.handleVector4f(ColorUtils.createVector4f(keyValue.getObject2()));
      this.floatUniform1.handleFloat5(f);
   }

   public void run3() {
      RenderSystem.setShader(shaderProgramHandle::getShaderProgram);
   }

   public PositionColorCircleShader() {
      this.run();
   }
}
