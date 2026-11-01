package org.phobos.mixin.game.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FishingBobberEntity.class)
public interface IFishingBobberEntity {
   @Accessor("hookedEntity")
   @Nullable
   Entity getHookedEntity();
}
