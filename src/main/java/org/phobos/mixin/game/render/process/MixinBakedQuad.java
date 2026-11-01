package org.phobos.mixin.game.render.process;

import net.minecraft.client.render.model.BakedQuad;
import org.earthhack.ducks.IBakedQuad;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BakedQuad.class)
public abstract class MixinBakedQuad implements IBakedQuad {
   @Shadow
   @Final
   protected int[] vertexData;

   @Override
   public float getZ(int vertex) {
      return Float.intBitsToFloat(this.vertexData[vertex * 8 + 2]);
   }

   @Override
   public float getY(int vertex) {
      return Float.intBitsToFloat(this.vertexData[vertex * 8 + 1]);
   }

   @Override
   public float getX(int vertex) {
      return Float.intBitsToFloat(this.vertexData[vertex * 8]);
   }
}
