package org.phobos.api;

import net.minecraft.client.gl.Framebuffer;

public interface FramebufferHolder {
   void clear();

   Framebuffer getFramebuffer();

   void run();

   void handleFlag2(boolean var1);

   void handleInt(int var1, int var2, boolean var3);

   void handleFlag(boolean var1);
}
