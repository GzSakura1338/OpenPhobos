package org.phobos.core.render.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.phobos.api.IntUniform1;
import org.phobos.api.FloatUniform1;
import org.phobos.api.FloatUniform2;
import org.phobos.api.FloatUniform3;
import org.phobos.api.ShaderRegistry;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.ShaderProgramHandle;
import org.phobos.core.render.ColorUtils;

public class PositionDissolveShader implements MinecraftHolder {
   public static final ShaderProgramHandle shaderProgramHandle = ShaderRegistry.getINSTANCE()
      .resolveShaderProgramHandle2(Identifier.of("phoboslite", "position_dissolve"), VertexFormats.POSITION);
   public FloatUniform2 floatUniform2;
   public FloatUniform3 floatUniform3;
   public FloatUniform1 floatUniform1;
   public IntUniform1 intUniform1;
   public FloatUniform3 floatUniform32;

   public void handleFloat(float f, Color color, Color color2, boolean flag) {
      this.floatUniform2.handleFloat(mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
      this.floatUniform1.handleFloat5(f);
      this.floatUniform3.handleVector3f(ColorUtils.createVector3f(color));
      this.floatUniform32.handleVector3f(ColorUtils.createVector3f(color2));
      this.intUniform1.handleInt3(flag ? 1 : 0);
   }

   public PositionDissolveShader() {
      this.run();
   }

   public void run3() {
      RenderSystem.setShader(shaderProgramHandle::getShaderProgram);
   }

   public void run() {
      this.floatUniform2 = shaderProgramHandle.resolveFloatUniform2("resolution");
      this.floatUniform1 = shaderProgramHandle.resolveFloatUniform1("time");
      this.floatUniform3 = shaderProgramHandle.resolveFloatUniform3("lead");
      this.floatUniform32 = shaderProgramHandle.resolveFloatUniform3("trail");
      this.intUniform1 = shaderProgramHandle.resolveIntUniform1("mode");
   }
}
