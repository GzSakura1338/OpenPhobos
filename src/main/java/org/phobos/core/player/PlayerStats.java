package org.phobos.core.player;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import org.phobos.api.NumericStats;
import org.phobos.module.client.RotationsModule;
import org.phobos.core.ClientHolder;

public class PlayerStats implements NumericStats {
   public final AbstractClientPlayerEntity abstractClientPlayerEntity;

   @Override
   public boolean check() {
      return this.getAbstractClientPlayerEntity().isInSwimmingPose();
   }

   @Override
   public double value() {
      return this.getAbstractClientPlayerEntity().getZ();
   }

   public PlayerStats(AbstractClientPlayerEntity abstractClientPlayerEntity) {
      this.abstractClientPlayerEntity = abstractClientPlayerEntity;
   }

   @Override
   public double value2() {
      return this.abstractClientPlayerEntity.prevZ;
   }

   @Override
   public boolean check2() {
      return this.getAbstractClientPlayerEntity().isInSneakingPose();
   }

   @Override
   public double value3() {
      return this.getAbstractClientPlayerEntity().getY();
   }

   @Override
   public double value4() {
      return this.abstractClientPlayerEntity.capeX;
   }

   @Override
   public double value5() {
      return this.getAbstractClientPlayerEntity().getX();
   }

   @Override
   public double value6() {
      return this.abstractClientPlayerEntity.prevX;
   }

   @Override
   public float value7() {
      return this.abstractClientPlayerEntity.bodyYaw;
   }

   @Override
   public double value8() {
      return this.abstractClientPlayerEntity.capeZ;
   }

   @Override
   public float value9() {
      return this.abstractClientPlayerEntity.prevBodyYaw;
   }

   @Override
   public boolean check3() {
      return this.getAbstractClientPlayerEntity().isSubmergedInWater();
   }

   public AbstractClientPlayerEntity getAbstractClientPlayerEntity() {
      return this.abstractClientPlayerEntity;
   }

   @Override
   public float value10() {
      return this.abstractClientPlayerEntity.isMainPlayer() && RotationsModule.getRotationsModule().check7()
         ? ClientHolder.getPhobosClient().getRotationManager().getRotationAnimation().getTimedFloatAnimator2().getFloatValue2()
         : this.getAbstractClientPlayerEntity().getYaw();
   }

   @Override
   public float value11() {
      return this.abstractClientPlayerEntity.isMainPlayer() && RotationsModule.getRotationsModule().check7()
         ? ClientHolder.getPhobosClient().getRotationManager().getTimedFloatAnimator().getFloatValue2()
         : this.value9() + this.value7() - this.value9();
   }

   @Override
   public double value12() {
      return this.abstractClientPlayerEntity.prevY;
   }

   @Override
   public float value13() {
      return this.abstractClientPlayerEntity.isMainPlayer() && RotationsModule.getRotationsModule().check7()
         ? ClientHolder.getPhobosClient().getRotationManager().getRotationAnimation().getTimedFloatAnimator().getFloatValue2()
         : this.getAbstractClientPlayerEntity().getPitch();
   }
}
