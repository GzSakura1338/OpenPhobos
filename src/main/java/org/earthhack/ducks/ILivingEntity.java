package org.earthhack.ducks;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.phobos.core.render.PlayerRenderState;
import org.phobos.core.animation.AnimatorGroup;
import org.phobos.core.animation.FloatAnimator;

public interface ILivingEntity {
   void setPrevBodyPitch(float var1);

   float getPrevBodyPitch();

   void setPrevRightLegPivotZ(float var1);

   float getPrevRightLegPivotZ();

   void setPrevLeftLegPivotZ(float var1);

   float getPrevLeftLegPivotZ();

   void setPrevRightLegPivotY(float var1);

   float getPrevRightLegPivotY();

   void setPrevLeftLegPivotY(float var1);

   float getPrevLeftLegPivotY();

   void setPrevHeadPivotY(float var1);

   float getPrevHeadPivotY();

   void setPrevBodyPivotY(float var1);

   float getPrevBodyPivotY();

   void setPrevLeftArmPivotY(float var1);

   float getPrevLeftArmPivotY();

   void setPrevRightArmPivotY(float var1);

   float getPrevRightArmPivotY();

   float getPrevRightArmPitch();

   float getPrevLeftArmPitch();

   FloatAnimator getBodyPitchAnimation();

   FloatAnimator getRightLegPivotZAnimation();

   FloatAnimator getLeftLegPivotZAnimation();

   FloatAnimator getRightLegPivotYAnimation();

   FloatAnimator getLeftLegPivotYAnimation();

   FloatAnimator getHeadPivotYAnimation();

   FloatAnimator getBodyPivotYAnimation();

   FloatAnimator getLeftArmPivotYAnimation();

   FloatAnimator getRightArmPivotYAnimation();

   FloatAnimator getRightArmPitchAnimation();

   FloatAnimator getLeftArmPitchAnimation();

   FloatAnimator getGlobalAnimation();

   AnimatorGroup getSneakAnimations();

   boolean is$sneaking();

   void set$sneaking(boolean var1);

   FloatAnimator getFloatAnimator();

   void setLastLerp(long var1);

   ItemStack getItemStack2();

   void setLastMainhandStack(ItemStack var1);

   ItemStack getItemStack();

   void setLastOffhandStack(ItemStack var1);

   int getLastMainhandCount();

   void setLastMainhandCount(int var1);

   int getLastOffhandCount();

   void setLastOffhandCount(int var1);

   PlayerRenderState getGhostModel();

   void setGhostModel(PlayerRenderState var1);

   float getLastAttackCooldown();

   static ILivingEntity cast(LivingEntity entity) {
      return (ILivingEntity)(Object)entity;
   }
}
