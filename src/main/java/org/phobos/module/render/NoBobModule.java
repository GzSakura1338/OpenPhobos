package org.phobos.module.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.phobos.module.Module;

public class NoBobModule extends Module {
   public static NoBobModule noBobModule;

   public static boolean testMatrixStack(MatrixStack matrixStack, float f) {
      if (mc.player == null) {
         return false;
      }

      float f2 = -(mc.player.horizontalSpeed + (mc.player.horizontalSpeed - mc.player.prevHorizontalSpeed) * f);
      float f3 = MathHelper.lerp(f, mc.player.prevStrideDistance, mc.player.strideDistance);
      matrixStack.translate(0.0, -Math.abs(f2 * f3 * 3.5E-4), 0.0);
      return true;
   }

   public static boolean isFlag() {
      return noBobModule != null && noBobModule.check7();
   }

   public NoBobModule(PhobosClient phobosClient) {
      super(phobosClient, "NoBob", Categories.render, "Minimizes bobbing effect");
      noBobModule = this;
   }

   public static void handleMatrixStack(MatrixStack matrixStack, float f, CallbackInfo callbackInfo) {
      if (noBobModule != null && noBobModule.check7() && mc.player != null) {
         testMatrixStack(matrixStack, f);
         callbackInfo.cancel();
      }
   }
}
