package org.phobos.core.player;

import org.phobos.core.animation.FloatAnimator;

public class PlayerAnimationState {
   public final FloatAnimator floatAnimator = new FloatAnimator();

   public FloatAnimator getFloatAnimator() {
      return this.floatAnimator;
   }
}
