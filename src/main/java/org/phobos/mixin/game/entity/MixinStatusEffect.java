package org.phobos.mixin.game.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.api.MinecraftHolder;
import org.phobos.event.OnAppliedEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StatusEffect.class)
public abstract class MixinStatusEffect implements MinecraftHolder, EventSubscriber {
   @Inject(method = "onApplied(Lnet/minecraft/entity/LivingEntity;I)V", at = @At("HEAD"))
   private void onAppliedHook(LivingEntity entity, int amplifier, CallbackInfo ci) {
      this.invoke(new OnAppliedEvent(entity, (StatusEffect)(Object)this, amplifier));
   }
}
