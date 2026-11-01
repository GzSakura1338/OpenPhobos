package org.phobos.mixin.game.render.process;

import java.util.List;
import net.minecraft.client.render.model.json.ItemModelGenerator;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.render.model.json.ItemModelGenerator.Frame;
import net.minecraft.client.render.model.json.ItemModelGenerator.Side;
import net.minecraft.client.texture.SpriteContents;
import org.phobos.core.render.texture.SpriteUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemModelGenerator.class)
public abstract class MixinItemModelGenerator {
   @Overwrite
   @SuppressWarnings("overwrite")
   private void buildCube(List<Frame> cubes, Side side, int x, int y) {
      if (!SpriteUtils.testList(cubes, side, x, y)) {
         Frame frame = null;

         for (Frame frame2 : cubes) {
            if (frame2.getSide() == side) {
               int i = side.isVertical() ? y : x;
               if (frame2.getLevel() == i) {
                  frame = frame2;
                  break;
               }
            }
         }

         int j = side.isVertical() ? y : x;
         int k = side.isVertical() ? x : y;
         if (frame == null) {
            cubes.add(new Frame(side, k, j));
         } else {
            frame.expand(k);
         }
      }
   }

   @Inject(method = "addSubComponents", at = @At("RETURN"), cancellable = true)
   private void addSubComponentsHook(SpriteContents sprite, String key, int layer, CallbackInfoReturnable<List<ModelElement>> cir) {
      SpriteUtils.handleCallbackInfoReturnable(cir);
   }
}
