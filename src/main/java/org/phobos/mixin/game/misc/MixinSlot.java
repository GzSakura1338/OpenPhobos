package org.phobos.mixin.game.misc;

import net.minecraft.screen.slot.Slot;
import org.earthhack.ducks.ISlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Slot.class)
public class MixinSlot implements ISlot {
   @Unique
   private float lastRenderY;
   @Unique
   private boolean loaded;
   @Unique
   private float lastRenderX;

   @Override
   public float getLastRenderX() {
      return this.lastRenderX;
   }

   @Override
   public void setLoaded(boolean loaded) {
      this.loaded = loaded;
   }

   @Override
   public boolean isLoaded() {
      return this.loaded;
   }

   @Override
   public void setLastRenderY(float lastRenderY) {
      this.lastRenderY = lastRenderY;
   }

   @Override
   public float getLastRenderY() {
      return this.lastRenderY;
   }

   @Override
   public void setLastRenderX(float lastRenderX) {
      this.lastRenderX = lastRenderX;
   }
}
