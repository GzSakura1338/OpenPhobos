package org.phobos.mixin.game.render.block;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.phobos.module.render.XRayModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidRenderer.class)
public abstract class MixinFluidRenderer {
   @Inject(
      method = "isSideCovered(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;FLnet/minecraft/block/BlockState;)Z",
      at = @At("HEAD"),
      cancellable = true
   )
   private static void isSideCoveredHook(
      BlockView world, BlockPos pos, Direction direction, float maxDeviation, BlockState state, CallbackInfoReturnable<Boolean> cir
   ) {
      if (XRayModule.check3()) {
         cir.setReturnValue(false);
      }
   }
}
