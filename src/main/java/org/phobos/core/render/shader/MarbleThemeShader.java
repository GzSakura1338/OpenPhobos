package org.phobos.core.render.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL30;
import org.phobos.api.WindowCallback;
import org.phobos.api.FloatUniform1;
import org.phobos.api.FloatUniform2;
import org.phobos.api.ShaderRegistry;
import org.phobos.api.FloatUniform4;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.ShaderProgramHandle;

public class MarbleThemeShader implements MinecraftHolder {
   public Framebuffer framebuffer;
   public FloatUniform4 floatUniform4;
   public FloatUniform2 floatUniform2;
   public static final ShaderProgramHandle shaderProgramHandle = ShaderRegistry.getINSTANCE()
      .resolveShaderProgramHandle2(Identifier.of("phoboslite", "theme_shader_marble"), VertexFormats.POSITION);
   public FloatUniform1 floatUniform1;

   public MarbleThemeShader() {
      this.run();
   }

   public void run3() {
      Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();
      this.framebuffer.beginWrite(false);
      GL30.glBindFramebuffer(36008, framebuffer.fbo);
      GL30.glBlitFramebuffer(0, 0, framebuffer.textureWidth, framebuffer.textureHeight, 0, 0, framebuffer.textureWidth, framebuffer.textureHeight, 16384, 9729);
      framebuffer.beginWrite(false);
      if (this.framebuffer != null
         && (this.framebuffer.textureWidth != mc.getWindow().getFramebufferWidth() || this.framebuffer.textureHeight != mc.getWindow().getFramebufferHeight())) {
         this.framebuffer.resize(mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight(), MinecraftClient.IS_SYSTEM_MAC);
      }

      this.floatUniform2.handleFloat(framebuffer.textureWidth, framebuffer.textureHeight);
      RenderSystem.setShader(shaderProgramHandle::getShaderProgram);
   }

   public void run() {
      this.floatUniform2 = shaderProgramHandle.resolveFloatUniform2("InputResolution");
      this.floatUniform1 = shaderProgramHandle.resolveFloatUniform1("time");
      this.floatUniform4 = shaderProgramHandle.resolveFloatUniform4("color");
      WindowCallback.windowCallbackEvent.register((WindowCallback)(minecraftClient, window) -> {
         if (this.framebuffer != null) {
            this.framebuffer.resize(window.getFramebufferWidth(), window.getFramebufferHeight(), MinecraftClient.IS_SYSTEM_MAC);
         }
      });
   }

   public void handleColor(Color color) {
      if (this.framebuffer == null) {
         this.framebuffer = new SimpleFramebuffer(mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), false, MinecraftClient.IS_SYSTEM_MAC);
      }

      this.floatUniform1.handleFloat5(ShaderEffectManager.getFloatValue());
      this.floatUniform4.handleFloat3(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
   }
}
