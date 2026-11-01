package org.phobos.mixin.game.render.player.feature;

import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FeatureRenderer.class)
public abstract class MixinFeatureRenderer<T extends Entity, M extends EntityModel<T>> {
   @Shadow
   public abstract M getContextModel();
}
