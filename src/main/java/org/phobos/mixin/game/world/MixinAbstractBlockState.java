package org.phobos.mixin.game.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.phobos.module.render.NoRenderModule;
import org.phobos.module.render.XRayModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlockState.class)
public abstract class MixinAbstractBlockState {
   @Inject(method = "getLuminance", at = @At("HEAD"), cancellable = true)
   private void getLuminanceHook(CallbackInfoReturnable<Integer> cir) {
      if (XRayModule.check3()) {
         cir.setReturnValue(15);
      }
   }

   @Inject(method = "isTransparent", at = @At("HEAD"), cancellable = true)
   private void isTransparentHook(BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
      if (!XRayModule.testBlock(this.getBlock()) || NoRenderModule.testBlock(this.getBlock())) {
         cir.setReturnValue(true);
      }
   }

   @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
   private void getRenderTypeHook(CallbackInfoReturnable<BlockRenderType> cir) {
      if (!XRayModule.testBlock(this.getBlock()) || NoRenderModule.testBlock(this.getBlock())) {
         cir.setReturnValue(BlockRenderType.INVISIBLE);
      }
   }

   @Shadow
   public abstract Block getBlock();
}
