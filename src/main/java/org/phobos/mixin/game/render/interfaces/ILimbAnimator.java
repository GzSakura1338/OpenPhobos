package org.phobos.mixin.game.render.interfaces;

import net.minecraft.entity.LimbAnimator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LimbAnimator.class)
public interface ILimbAnimator {
   @Accessor("prevSpeed")
   void setPrevSpeed(float var1);

   @Accessor("prevSpeed")
   float getPrevSpeed();

   @Accessor("pos")
   void setPos(float var1);
}
