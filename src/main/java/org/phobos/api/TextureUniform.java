package org.phobos.api;

import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.AbstractTexture;

public interface TextureUniform {
   void handleAbstractTexture(AbstractTexture var1);

   void handleInt3(int var1);

   void handleFramebuffer(Framebuffer var1);
}
