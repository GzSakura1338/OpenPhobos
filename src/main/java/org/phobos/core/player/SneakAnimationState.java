package org.phobos.core.player;

import org.phobos.module.render.AnimationsModule;

public class SneakAnimationState {
   public AnimationsModule.SneakAnimation sneakAnimation = null;

   public void setSneakAnimation(AnimationsModule.SneakAnimation sneakAnimation) {
      this.sneakAnimation = sneakAnimation;
   }

   public AnimationsModule.SneakAnimation getSneakAnimation() {
      return this.sneakAnimation;
   }
}
