package org.phobos.mixin.game.entity;

import net.minecraft.entity.LightningEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LightningEntity.class)
public interface ILightningEntity {
   @Accessor("remainingActions")
   void setRemainingActions(int var1);

   @Accessor("ambientTick")
   void setAmbientTick(int var1);
}
