package org.phobos.mixin.game.misc;

import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.module.movement.NoPushModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlowableFluid.class)
public abstract class MixinFlowableFluid implements EventSubscriber {
   @Inject(method = "getVelocity", at = @At("HEAD"), cancellable = true)
   private void getVelocityHook(BlockView world, BlockPos pos, FluidState state, CallbackInfoReturnable<Vec3d> cir) {
      if (NoPushModule.check()) {
         cir.setReturnValue(Vec3d.ZERO);
      }
   }
}
