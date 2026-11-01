package org.phobos.mixin.game;

import net.minecraft.client.render.RenderTickCounter.Dynamic;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.event.BeginRenderTickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Dynamic.class)
public abstract class MixinDynamic implements EventSubscriber {
   @Shadow
   private float tickDelta;
   @Shadow
   private float lastFrameDuration;

   @Inject(
      method = "beginRenderTick(J)I",
      at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/RenderTickCounter$Dynamic;prevTimeMillis:J", opcode = 181)
   )
   private void beginRenderTickHook(long millis, CallbackInfoReturnable<Integer> cir) {
      BeginRenderTickEvent event = this.invoke(new BeginRenderTickEvent(this.lastFrameDuration, this.tickDelta));
      this.lastFrameDuration = this.lastFrameDuration * event.getFloatValue();
      this.tickDelta = event.getFloatValue2();
   }
}
