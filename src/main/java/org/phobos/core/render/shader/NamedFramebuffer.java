package org.phobos.core.render.shader;

import com.mojang.logging.LogUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.util.Window;
import org.phobos.api.FramebufferHolder;

public final class NamedFramebuffer implements FramebufferHolder {
   public final String name;
   public Framebuffer framebuffer;

   @Override
   public Framebuffer getFramebuffer() {
      return this.framebuffer;
   }

   @Override
   public void handleInt(int i, int i2, boolean flag) {
      if (this.framebuffer != null) {
         this.framebuffer.draw(i, i2, flag);
      }
   }

   public String getName() {
      return this.name;
   }

   @Override
   public void clear() {
      this.handleFlag2(MinecraftClient.IS_SYSTEM_MAC);
   }

   public NamedFramebuffer(String text) {
      this.name = text;
   }

   public void handlePostEffectProcessor(PostEffectProcessor postEffectProcessor) {
      if (postEffectProcessor == null) {
         this.framebuffer = null;
      } else {
         this.framebuffer = postEffectProcessor.getSecondaryTarget(this.name);
         if (this.framebuffer == null) {
            LogUtils.getLogger().warn("No target framebuffer found with name {} in shader {}", this.name, postEffectProcessor.getName());
         }
      }
   }

   @Override
   public void handleFlag(boolean flag) {
      if (this.framebuffer != null) {
         this.framebuffer.beginWrite(flag);
      }
   }

   @Override
   public void handleFlag2(boolean flag) {
      if (this.framebuffer != null) {
         this.framebuffer.clear(flag);
      }
   }

   @Override
   public void run() {
      Window window = MinecraftClient.getInstance().getWindow();
      this.handleInt(window.getFramebufferWidth(), window.getFramebufferHeight(), true);
   }
}
