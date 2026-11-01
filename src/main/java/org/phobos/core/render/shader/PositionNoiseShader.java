package org.phobos.core.render.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.phobos.api.FloatUniform1;
import org.phobos.api.FloatUniform2;
import org.phobos.api.ShaderRegistry;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.ShaderProgramHandle;

public class PositionNoiseShader implements MinecraftHolder {
   public FloatUniform2 floatUniform2;
   public static final ShaderProgramHandle shaderProgramHandle = ShaderRegistry.getINSTANCE()
      .resolveShaderProgramHandle2(Identifier.of("phoboslite", "position_noise"), VertexFormats.POSITION_COLOR);
   public FloatUniform1 floatUniform1;

   public void run3() {
      RenderSystem.setShader(shaderProgramHandle::getShaderProgram);
   }

   public void run() {
      this.floatUniform1 = shaderProgramHandle.resolveFloatUniform1("time");
      this.floatUniform2 = shaderProgramHandle.resolveFloatUniform2("resolution");
   }

   public PositionNoiseShader() {
      this.run();
   }

   public void run2() {
      this.floatUniform1.handleFloat5(ShaderEffectManager.floatValue);
      this.floatUniform2.handleFloat(mc.getWindow().getScaledWidth() * 2.0F, mc.getWindow().getScaledHeight() * 2.0F);
   }
}
