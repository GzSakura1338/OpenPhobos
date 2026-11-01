package org.phobos.mixin.game.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FireworkRocketEntity.class)
public interface IFireworkRocketEntity {
   @Accessor("shooter")
   LivingEntity getShooter();
}
