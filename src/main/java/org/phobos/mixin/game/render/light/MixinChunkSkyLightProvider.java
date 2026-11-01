package org.phobos.mixin.game.render.light;

import net.minecraft.world.chunk.light.ChunkSkyLightProvider;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.module.render.NoRenderModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkSkyLightProvider.class)
public abstract class MixinChunkSkyLightProvider implements EventSubscriber {
   @Inject(method = "method_51531", at = @At("HEAD"), cancellable = true)
   private void updateHook(CallbackInfo ci) {
      this.invoke(new NoRenderModule.Skylight(), ci);
   }
}
