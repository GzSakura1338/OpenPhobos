package org.phobos.mixin.game.misc;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracked;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.event.SetEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DataTracker.class)
public abstract class MixinDataTracker<T> implements EventSubscriber {
   @Shadow
   @Final
   private DataTracked trackedEntity;

   @Shadow
   public abstract <S> S get(TrackedData<S> var1);

   @Unique
   private float eval(float current, float newValue) {
      return current > newValue ? -(newValue - current) : newValue - current;
   }

   @Inject(
      method = "set(Lnet/minecraft/entity/data/TrackedData;Ljava/lang/Object;Z)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/data/DataTracker$Entry;set(Ljava/lang/Object;)V")
   )
   private void setHook(TrackedData<T> key, T value, boolean force, CallbackInfo ci) {
      if (LivingEntity.HEALTH.equals(key) && this.trackedEntity instanceof LivingEntity entity && value instanceof Float floating) {
         this.invoke(new SetEvent(entity, floating, this.eval((Float)entity.getDataTracker().get(LivingEntity.HEALTH), floating)));
      }
   }
}
