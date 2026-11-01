package org.phobos.mixin.game.render;

import java.awt.Dimension;
import java.awt.Toolkit;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFW;
import org.phobos.module.client.RenderModule;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public abstract class MixinWindow {
   @Shadow
   private int height;
   @Shadow
   private int width;
   @Shadow
   @Final
   private long handle;

   @Inject(method = "updateWindowRegion", at = @At("TAIL"))
   private void updateWindowRegionHook(CallbackInfo ci) {
      if (RenderModule.check()) {
         Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
         GLFW.glfwSetWindowAttrib(this.handle, 131077, 0);
         GLFW.glfwSetWindowMonitor(this.handle, 0L, 0, 0, dimension.width, dimension.height, -1);
      }
   }
}
