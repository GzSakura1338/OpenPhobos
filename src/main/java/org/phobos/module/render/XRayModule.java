package org.phobos.module.render;

import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.BlockSetSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.phobos.module.Module;

public class XRayModule extends Module {
   public static XRayModule xRayModule;
   public final Setting<Set<Block>> whitelist = this.resolveSetting2(new BlockSetSetting("Whitelist"));

   @Override
   public void run4() {
      mc.chunkCullingEnabled = true;
      mc.worldRenderer.reload();
   }

   public XRayModule(PhobosClient phobosClient) {
      super(phobosClient, "XRay", Categories.render, "Makes everything transparent");
      xRayModule = this;
      this.whitelist.handleConsumer2(set -> {
         if (this.check7()) {
            mc.submit(() -> mc.worldRenderer.reload());
         }
      });
   }

   @Override
   public void run3() {
      mc.chunkCullingEnabled = false;
      mc.worldRenderer.reload();
   }

   public static boolean check3() {
      return xRayModule != null && xRayModule.check7();
   }

   public static boolean testBlock(Block block) {
      if (xRayModule == null || !xRayModule.check7()) {
         return true;
      } else {
         return block instanceof FluidBlock ? true : xRayModule.whitelist.getValue().contains(block);
      }
   }

   public static XRayModule getXRayModule() {
      return xRayModule;
   }

   public static void handleBlock(Block block, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
      if (xRayModule != null && xRayModule.check7()) {
         if (block instanceof FluidBlock) {
            callbackInfoReturnable.setReturnValue(true);
         } else {
            callbackInfoReturnable.setReturnValue(xRayModule.whitelist.getValue().contains(block));
         }
      }
   }
}
