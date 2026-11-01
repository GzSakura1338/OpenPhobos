package org.earthhack.ducks;

import org.phobos.core.animation.FloatAnimator;

public interface IEndCrystalEntity {
   int getLastTick();

   void setLastTick(int var1);

   boolean isRendering();

   void setRendering(boolean var1);

   FloatAnimator getAnimation();
}
