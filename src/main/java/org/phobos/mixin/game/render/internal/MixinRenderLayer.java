package org.phobos.mixin.game.render.internal;

import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.RenderLayer;
import org.earthhack.ducks.IRenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderLayer.class)
public abstract class MixinRenderLayer implements IRenderLayer {
   @Unique
   private Runnable starter;
   @Unique
   private Runnable finisher;

   @Override
   public void setStarter(Runnable starter) {
      this.starter = starter;
   }

   @Override
   public void setFinisher(Runnable finisher) {
      this.finisher = finisher;
   }

   @Inject(method = "draw", at = @At("TAIL"))
   private void drawHook2(BuiltBuffer buffer, CallbackInfo ci) {
      if (this.finisher != null) {
         this.finisher.run();
      }
   }

   @Inject(method = "draw", at = @At("HEAD"))
   private void drawHook1(BuiltBuffer buffer, CallbackInfo ci) {
      if (this.starter != null) {
         this.starter.run();
      }
   }
}
