package org.phobos.mixin.game.render.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import org.phobos.api.TickTask;
import org.phobos.module.render.AmbienceModule;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OverlayTexture.class)
public abstract class MixinOverlayTexture implements TickTask {
   @Shadow
   @Final
   private NativeImageBackedTexture texture;

   @Override
   public void run() {
      this.pack();
   }

   @Inject(method = "<init>", at = @At("TAIL"))
   private void initHook(CallbackInfo ci) {
      TickTask.handleTickTask(this);
      this.pack();
   }

   private void pack() {
      NativeImage nativeImage = this.texture.getImage();

      for (int i = 0; i < 16; i++) {
         for (int j = 0; j < 16; j++) {
            if (i < 8) {
               assert nativeImage != null;
               if (AmbienceModule.check()) {
                  Color color = AmbienceModule.getColor();
                  nativeImage.setColor(j, i, getColorInt(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));
               } else {
                  nativeImage.setColor(j, i, -1308622593);
               }
            }
         }

         RenderSystem.activeTexture(33985);
         this.texture.bindTexture();
         nativeImage.upload(0, 0, 0, 0, 0, nativeImage.getWidth(), nativeImage.getHeight(), false, true, false, false);
         RenderSystem.activeTexture(33984);
      }
   }

   private static int getColorInt(int red, int green, int blue, int alpha) {
      alpha = 255 - alpha;
      return (alpha << 24) + (blue << 16) + (green << 8) + red;
   }
}
