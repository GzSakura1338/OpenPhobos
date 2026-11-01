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
import org.phobos.api.TextureUniform;
import org.phobos.api.FloatUniform4;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.ShaderProgramHandle;

public class BlurShader implements MinecraftHolder {
   public Framebuffer framebuffer;
   public FloatUniform1 floatUniform1;
   public FloatUniform2 floatUniform2;
   public FloatUniform2 floatUniform22;
   public FloatUniform4 floatUniform4;
   public FloatUniform1 floatUniform12;
   public static final ShaderProgramHandle shaderProgramHandle = ShaderRegistry.getINSTANCE().resolveShaderProgramHandle2(Identifier.of("phoboslite", "blur"), VertexFormats.POSITION);
   public TextureUniform textureUniform;
   public FloatUniform2 floatUniform23;
   public FloatUniform1 floatUniform13;

   public BlurShader() {
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

      this.floatUniform22.handleFloat(framebuffer.textureWidth, framebuffer.textureHeight);
      this.textureUniform.handleInt3(this.framebuffer.getColorAttachment());
      RenderSystem.setShader(shaderProgramHandle::getShaderProgram);
   }

   public void handleFloat(float f, float f2, float f3, float f4, float f5, Color color, float f6, float f7) {
      if (this.framebuffer == null) {
         this.framebuffer = new SimpleFramebuffer(mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), false, MinecraftClient.IS_SYSTEM_MAC);
      }

      float f8 = (float)mc.getWindow().getScaleFactor();
      this.floatUniform12.handleFloat5(f5 * f8);
      this.floatUniform23.handleFloat(f * f8, -f2 * f8 + mc.getWindow().getScaledHeight() * f8 - f4 * f8);
      this.floatUniform2.handleFloat(f3 * f8, f4 * f8);
      this.floatUniform1.handleFloat5(f7);
      this.floatUniform13.handleFloat5(f6);
      this.floatUniform4.handleFloat3(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
      this.textureUniform.handleInt3(this.framebuffer.getColorAttachment());
   }

   public void run() {
      this.floatUniform22 = shaderProgramHandle.resolveFloatUniform2("InputResolution");
      this.floatUniform1 = shaderProgramHandle.resolveFloatUniform1("Brightness");
      this.floatUniform13 = shaderProgramHandle.resolveFloatUniform1("Quality");
      this.floatUniform4 = shaderProgramHandle.resolveFloatUniform4("color1");
      this.floatUniform2 = shaderProgramHandle.resolveFloatUniform2("uSize");
      this.floatUniform23 = shaderProgramHandle.resolveFloatUniform2("uLocation");
      this.floatUniform12 = shaderProgramHandle.resolveFloatUniform1("radius");
      this.textureUniform = shaderProgramHandle.resolveTextureUniform("InputSampler");
      WindowCallback.windowCallbackEvent.register((WindowCallback)(minecraftClient, window) -> {
         if (this.framebuffer != null) {
            this.framebuffer.resize(window.getFramebufferWidth(), window.getFramebufferHeight(), MinecraftClient.IS_SYSTEM_MAC);
         }
      });
   }
}
