package org.phobos.mixin.game.entity;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface ILivingEntity {
   @Accessor("serverY")
   double getLerpY();

   @Accessor("lastLeaningPitch")
   float getLastLeaningPitch();

   @Accessor("serverX")
   double getLerpX();

   @Accessor("leaningPitch")
   void setLeaningPitch(float var1);

   @Accessor("leaningPitch")
   float getLeaningPitch();

   @Accessor("lastLeaningPitch")
   void setLastLeaningPitch(float var1);

   @Accessor("serverZ")
   double getLerpZ();
}
