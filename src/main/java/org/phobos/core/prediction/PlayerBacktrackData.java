package org.phobos.core.prediction;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.tick.TimeUtils;
import org.phobos.core.fakeplayer.TrackedFakePlayer;

public class PlayerBacktrackData {
   public Vec3d vec3d;
   @Nullable
   public TrackedFakePlayer trackedFakePlayer;
   public long longValue;
   public AbstractClientPlayerEntity abstractClientPlayerEntity;
   public double doubleValue;
   public TrackedFakePlayer[] trackedFakePlayerArray;
   public double doubleValue2;
   public double doubleValue3;

   public void run() {
      this.setVec3d(null);
      this.setDoubleValue2(0.0);
      this.setDoubleValue3(0.0);
      this.setDoubleValue(0.0);
      this.setLongValue(0L);
      this.setTrackedFakePlayerArray(null);
      this.setTrackedFakePlayer(null);
      this.setAbstractClientPlayerEntity(null);
   }

   public Vec3d getVec3d() {
      return this.vec3d;
   }

   public void setAbstractClientPlayerEntity(AbstractClientPlayerEntity abstractClientPlayerEntity) {
      this.abstractClientPlayerEntity = abstractClientPlayerEntity;
   }

   public void handleTrackedFakePlayer(TrackedFakePlayer trackedFakePlayer, Vec3d vec3d) {
      trackedFakePlayer.setLongValue(TimeUtils.value());
      if (this.trackedFakePlayer == null) {
         trackedFakePlayer.setPosition(this.doubleValue2, this.doubleValue3, this.doubleValue);
         trackedFakePlayer.setVelocity(vec3d);
         trackedFakePlayer.setOnGround(true);
      } else {
         trackedFakePlayer.copyFrom(this.trackedFakePlayer);
         trackedFakePlayer.setVelocity(this.trackedFakePlayer.getVelocity());
         trackedFakePlayer.setOnGround(this.trackedFakePlayer.isOnGround());
      }
   }

   public void setVec3d(Vec3d vec3d) {
      this.vec3d = vec3d;
   }

   public void setDoubleValue3(double d) {
      this.doubleValue3 = d;
   }

   public AbstractClientPlayerEntity getAbstractClientPlayerEntity() {
      return this.abstractClientPlayerEntity;
   }

   public void handleTrackedFakePlayer2(TrackedFakePlayer trackedFakePlayer) {
      trackedFakePlayer.move(MovementType.SELF, trackedFakePlayer.getVelocity());
   }

   public void setDoubleValue2(double d) {
      this.doubleValue2 = d;
   }

   public double getDoubleValue3() {
      return this.doubleValue3;
   }

   public long getLongValue() {
      return this.longValue;
   }

   public double getDoubleValue2() {
      return this.doubleValue2;
   }

   public void setDoubleValue(double d) {
      this.doubleValue = d;
   }

   @Nullable
   public TrackedFakePlayer getTrackedFakePlayer() {
      return this.trackedFakePlayer;
   }

   public void handleTrackedFakePlayer3(TrackedFakePlayer trackedFakePlayer, Vec3d vec3d) {
      trackedFakePlayer.setVelocity(vec3d);
   }

   public TrackedFakePlayer[] getTrackedFakePlayerArray() {
      return this.trackedFakePlayerArray;
   }

   public void setLongValue(long l) {
      this.longValue = l;
   }

   public void setTrackedFakePlayerArray(TrackedFakePlayer[] trackedFakePlayers) {
      this.trackedFakePlayerArray = trackedFakePlayers;
   }

   public void setTrackedFakePlayer(@Nullable TrackedFakePlayer trackedFakePlayer) {
      this.trackedFakePlayer = trackedFakePlayer;
   }

   public double getDoubleValue() {
      return this.doubleValue;
   }
}
