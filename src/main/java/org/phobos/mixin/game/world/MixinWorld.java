package org.phobos.mixin.game.world;

import net.minecraft.world.World;
import org.phobos.module.misc.WeatherModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class MixinWorld {
   @Inject(method = "getRainGradient", at = @At("TAIL"), cancellable = true)
   private void getRainGradientHook(float tickDelta, CallbackInfoReturnable<Float> cir) {
      cir.setReturnValue(WeatherModule.computeFloat2((Float)cir.getReturnValue()));
   }
}
