package org.phobos.core.render.shader;

import java.util.function.IntSupplier;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.client.texture.AbstractTexture;
import org.phobos.api.SamplerUniform;
import org.phobos.api.ShaderInfo;

public final class PostEffectSamplerVariable extends SamplerVariable implements SamplerUniform {
   @Override
   public void handleFramebuffer(Framebuffer framebuffer) {
      this.set(framebuffer::getColorAttachment);
   }

   @Override
   public void set(IntSupplier intSupplier) {
      ShaderInfo[] shaderInfos = this.shaderInfoArray;
      if (shaderInfos.length > 0 && this.object != intSupplier) {
         for (ShaderInfo shaderInfo : shaderInfos) {
            ((JsonEffectShaderProgram)shaderInfo).bindSampler(this.name, intSupplier);
         }

         this.object = intSupplier;
      }
   }

   @Override
   public void handleAbstractTexture(AbstractTexture abstractTexture) {
      this.set(abstractTexture::getGlId);
   }

   @Override
   public void set(Object object) {
      this.set((IntSupplier)object);
   }

   public PostEffectSamplerVariable(String text) {
      super(text);
   }

   @Override
   public void handleInt3(int i) {
      this.set(() -> i);
   }
}
