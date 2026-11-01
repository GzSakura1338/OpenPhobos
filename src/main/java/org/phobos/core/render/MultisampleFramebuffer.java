package org.phobos.core.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import org.lwjgl.opengl.GL30;
import org.phobos.module.client.RenderModule;

public class MultisampleFramebuffer extends Framebuffer {
   public final int intValue;
   public static final int intValue2 = GL30.glGetInteger(36183);
   public static final Map<Integer, MultisampleFramebuffer> integerMap = new HashMap<>();
   public int intValue3;
   public static final int intValue4 = 2;
   public int intValue5;

   public void resize(int width, int height, boolean getError) {
      if (this.textureWidth != width || this.textureHeight != height) {
         super.resize(width, height, getError);
      }
   }

   public MultisampleFramebuffer(int i) {
      super(true);
      if (i < 2 || i > intValue2) {
         i = intValue2;
      }

      if ((i & i - 1) != 0) {
         throw new IllegalArgumentException("The number of samples must be a power of two.");
      }

      this.intValue = i;
      this.setClearColor(1.0F, 1.0F, 1.0F, 0.0F);
   }

   public static void handleRunnable(Runnable runnable) {
      int i = RenderModule.value();
      if (i == -1) {
         runnable.run();
      } else {
         handleInt(i, MinecraftClient.getInstance().getFramebuffer(), runnable);
      }
   }

   public static void handleInt(int i, Framebuffer framebuffer, Runnable runnable) {
      RenderSystem.assertOnRenderThreadOrInit();
      MultisampleFramebuffer multisampleFramebuffer = resolveMultisampleFramebuffer(i);
      multisampleFramebuffer.resize(framebuffer.textureWidth, framebuffer.textureHeight, true);
      GlStateManager._glBindFramebuffer(36008, framebuffer.fbo);
      GlStateManager._glBindFramebuffer(36009, multisampleFramebuffer.fbo);
      GlStateManager._glBlitFrameBuffer(
         0, 0, multisampleFramebuffer.textureWidth, multisampleFramebuffer.textureHeight, 0, 0, multisampleFramebuffer.textureWidth, multisampleFramebuffer.textureHeight, 16384, 9729
      );
      multisampleFramebuffer.beginWrite(true);
      runnable.run();
      multisampleFramebuffer.endWrite();
      GlStateManager._glBindFramebuffer(36008, multisampleFramebuffer.fbo);
      GlStateManager._glBindFramebuffer(36009, framebuffer.fbo);
      GlStateManager._glBlitFrameBuffer(
         0, 0, multisampleFramebuffer.textureWidth, multisampleFramebuffer.textureHeight, 0, 0, multisampleFramebuffer.textureWidth, multisampleFramebuffer.textureHeight, 16384, 9729
      );
      multisampleFramebuffer.clear(true);
      framebuffer.beginWrite(false);
   }

   public void delete() {
      RenderSystem.assertOnRenderThreadOrInit();
      this.endRead();
      this.endWrite();
      if (this.fbo > -1) {
         GlStateManager._glBindFramebuffer(36160, 0);
         GlStateManager._glDeleteFramebuffers(this.fbo);
         this.fbo = -1;
      }

      if (this.intValue3 > -1) {
         GlStateManager._glDeleteRenderbuffers(this.intValue3);
         this.intValue3 = -1;
      }

      if (this.intValue5 > -1) {
         GlStateManager._glDeleteRenderbuffers(this.intValue5);
         this.intValue5 = -1;
      }

      this.colorAttachment = -1;
      this.depthAttachment = -1;
      this.textureWidth = -1;
      this.textureHeight = -1;
   }

   public static MultisampleFramebuffer resolveMultisampleFramebuffer(int i) {
      return integerMap.computeIfAbsent(i, integer -> new MultisampleFramebuffer(i));
   }

   public void initFbo(int width, int height, boolean getError) {
      RenderSystem.assertOnRenderThreadOrInit();
      int i = RenderSystem.maxSupportedTextureSize();
      if (width > 0 && width <= i && height > 0 && height <= i) {
         this.viewportWidth = width;
         this.viewportHeight = height;
         this.textureWidth = width;
         this.textureHeight = height;
         this.fbo = GlStateManager.glGenFramebuffers();
         GlStateManager._glBindFramebuffer(36160, this.fbo);
         this.intValue3 = GlStateManager.glGenRenderbuffers();
         GlStateManager._glBindRenderbuffer(36161, this.intValue3);
         GL30.glRenderbufferStorageMultisample(36161, this.intValue, 32856, width, height);
         GlStateManager._glBindRenderbuffer(36161, 0);
         this.intValue5 = GlStateManager.glGenRenderbuffers();
         GlStateManager._glBindRenderbuffer(36161, this.intValue5);
         GL30.glRenderbufferStorageMultisample(36161, this.intValue, 6402, width, height);
         GlStateManager._glBindRenderbuffer(36161, 0);
         GL30.glFramebufferRenderbuffer(36160, 36064, 36161, this.intValue3);
         GL30.glFramebufferRenderbuffer(36160, 36096, 36161, this.intValue5);
         this.colorAttachment = MinecraftClient.getInstance().getFramebuffer().getColorAttachment();
         this.depthAttachment = MinecraftClient.getInstance().getFramebuffer().getDepthAttachment();
         this.checkFramebufferStatus();
         this.clear(getError);
         this.endRead();
      } else {
         throw new IllegalArgumentException("Window " + width + "x" + height + " size out of bounds (max. size: " + i + ")");
      }
   }
}
