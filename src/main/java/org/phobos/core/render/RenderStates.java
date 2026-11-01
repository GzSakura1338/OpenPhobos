package org.phobos.core.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;

public class RenderStates {
   public static Runnable runnable = () -> {};
   public static Runnable runnable2 = () -> RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   public static Runnable runnable3 = () -> RenderSystem.setShaderGlintAlpha((Double)MinecraftClient.getInstance().options.getGlintStrength().getValue());

   public static boolean testRunnable(Runnable runnable) {
      runnable.run();
      return true;
   }
}
