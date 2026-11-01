package org.phobos.mixin.game.render.sodium;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.module.render.NoRenderModule;
import org.phobos.module.render.XRayModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockOcclusionCache", remap = false)
public abstract class MixinBlockOcclusionCache implements EventSubscriber {
   @Inject(method = "shouldDrawSide", at = @At("RETURN"), cancellable = true)
   private void shouldRenderHook(BlockState state, BlockView view, BlockPos pos, Direction facing, CallbackInfoReturnable<Boolean> cir) {
      if (NoRenderModule.testBlockState(state)) {
         cir.setReturnValue(false);
      } else {
         XRayModule.handleBlock(state.getBlock(), cir);
      }
   }
}
