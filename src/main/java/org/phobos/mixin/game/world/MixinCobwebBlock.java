package org.phobos.mixin.game.world;

import net.minecraft.block.BlockState;
import net.minecraft.block.CobwebBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.phobos.module.movement.AntiWebModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CobwebBlock.class)
public abstract class MixinCobwebBlock {
   @Inject(method = "onEntityCollision", at = @At("HEAD"), cancellable = true)
   private void onEntityCollisionHook(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
      if (AntiWebModule.check()) {
         ci.cancel();
      }
   }
}
