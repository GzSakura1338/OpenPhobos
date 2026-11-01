package org.phobos.mixin.game.entity;

import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PersistentProjectileEntity.class)
public interface IPersistentProjectileEntity {
   @Accessor("inGround")
   boolean collided();

   @Accessor("inGroundTime")
   int getInGroundTime();
}
