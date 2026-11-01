package org.phobos.core.render.shader;

import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.texture.AbstractTexture;
import org.phobos.api.ShaderInfo;

public final class ProgramSamplerVariable extends SamplerVariable {
   @Override
   public void handleInt3(int i) {
      this.set(i);
   }

   @Override
   public void handleFramebuffer(Framebuffer framebuffer) {
      this.set(framebuffer);
   }

   @Override
   public void set(Object object) {
      ShaderInfo[] shaderInfos = this.shaderInfoArray;
      if (shaderInfos.length > 0 && this.object != object) {
         for (ShaderInfo shaderInfo : shaderInfos) {
            ((ShaderProgram)shaderInfo).addSampler(this.name, object);
         }

         this.object = object;
      }
   }

   public ProgramSamplerVariable(String text) {
      super(text);
   }

   @Override
   public void handleAbstractTexture(AbstractTexture abstractTexture) {
      this.set(abstractTexture);
   }
}
