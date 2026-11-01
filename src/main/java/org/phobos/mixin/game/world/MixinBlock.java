package org.phobos.mixin.game.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.module.render.NoRenderModule;
import org.phobos.module.render.NoRenderModule;
import org.phobos.module.render.XRayModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class MixinBlock implements EventSubscriber {
   @Inject(method = "shouldDrawSide", at = @At("HEAD"), cancellable = true)
   private static void shouldDrawSideHook(
      BlockState state, BlockView world, BlockPos pos, Direction side, BlockPos otherPos, CallbackInfoReturnable<Boolean> cir
   ) {
      if (NoRenderModule.testBlockState(state)) {
         cir.setReturnValue(false);
      } else {
         XRayModule.handleBlock(state.getBlock(), cir);
      }
   }

   @Inject(method = "spawnBreakParticles", at = @At("HEAD"), cancellable = true)
   private void spawnBreakParticlesHook(World world, PlayerEntity player, BlockPos pos, BlockState state, CallbackInfo ci) {
      this.invoke(new NoRenderModule.BlockBreak(), ci);
   }
}
