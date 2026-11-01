package org.phobos.mixin.game.entity;

import net.minecraft.entity.decoration.EndCrystalEntity;
import org.earthhack.ducks.IEndCrystalEntity;
import org.phobos.core.animation.FloatAnimator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EndCrystalEntity.class)
public abstract class MixinEndCrystalEntity implements IEndCrystalEntity {
   @Unique
   private final FloatAnimator animation = new FloatAnimator(4.0F, 2.0F);
   @Unique
   private boolean rendering;
   @Unique
   private int lastTick;

   @Override
   public FloatAnimator getAnimation() {
      return this.animation;
   }

   @Override
   public boolean isRendering() {
      return this.rendering;
   }

   @Override
   public void setRendering(boolean rendering) {
      this.rendering = rendering;
   }

   @Override
   public int getLastTick() {
      return this.lastTick;
   }

   @Override
   public void setLastTick(int tick) {
      this.lastTick = tick;
   }
}
