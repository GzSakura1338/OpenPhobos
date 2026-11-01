package org.phobos.mixin.game.render.entity.model;

import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityModel.class)
public abstract class MixinEntityModel<T extends Entity> {
   @Shadow
   public boolean riding;
   @Shadow
   public float handSwingProgress;
}
