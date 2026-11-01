package org.phobos.mixin.game.render.process;

import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteContents;
import org.phobos.core.render.texture.SpriteUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Sprite.class)
public abstract class MixinSprite {
   @Shadow
   public abstract SpriteContents getContents();

   @Inject(method = "getAnimationFrameDelta", at = @At("RETURN"), cancellable = true)
   private void getAnimationFrameDeltaHook(CallbackInfoReturnable<Float> cir) {
      SpriteUtils.handleSpriteContents(this.getContents(), 4.0F / this.getFrameDeltaFactor(), cir);
   }

   @Shadow
   protected abstract float getFrameDeltaFactor();
}
