package org.phobos.core.render.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.phobos.api.FloatUniform1;
import org.phobos.api.FloatUniform2;
import org.phobos.api.ShaderRegistry;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.ShaderProgramHandle;

public class SkyboxShader implements MinecraftHolder {
   public static final ShaderProgramHandle shaderProgramHandle = ShaderRegistry.getINSTANCE()
      .resolveShaderProgramHandle2(Identifier.of("phobos", "skybox"), VertexFormats.POSITION_TEXTURE_COLOR_NORMAL);
   public FloatUniform1 floatUniform1;
   public FloatUniform2 floatUniform2;

   public void run() {
      this.floatUniform2 = shaderProgramHandle.resolveFloatUniform2("resolution");
      this.floatUniform1 = shaderProgramHandle.resolveFloatUniform1("time");
   }

   public void run2() {
      this.floatUniform2.handleFloat(mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
      this.floatUniform1.handleFloat5(ShaderEffectManager.getFloatValue());
   }

   public void run3() {
      RenderSystem.setShader(shaderProgramHandle::getShaderProgram);
   }

   public SkyboxShader() {
      this.run();
   }
}
