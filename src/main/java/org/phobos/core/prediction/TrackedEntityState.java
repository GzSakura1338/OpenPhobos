package org.phobos.core.prediction;

import org.jetbrains.annotations.Nullable;
import org.phobos.event.TrackedPositionUpdateEvent;
import org.phobos.core.fakeplayer.TrackedFakePlayer;

public class TrackedEntityState {
   public boolean flag;
   @Nullable
   public TrackedFakePlayer trackedFakePlayer;
   public TrackedPositionUpdateEvent trackedPositionUpdateEvent;
   public boolean flag2;

   public boolean isFlag2() {
      return this.flag2;
   }

   public void run() {
      if (this.trackedFakePlayer != null) {
         this.trackedFakePlayer.setFlag3(false);
         this.trackedFakePlayer = null;
      }
   }

   public void setTrackedFakePlayer(@Nullable TrackedFakePlayer trackedFakePlayer) {
      this.trackedFakePlayer = trackedFakePlayer;
   }

   public void handleTrackedFakePlayer(TrackedFakePlayer trackedFakePlayer) {
      this.trackedFakePlayer = trackedFakePlayer;
      trackedFakePlayer.setFlag3(this.flag2);
   }

   public boolean isFlag() {
      return this.flag;
   }

   @Nullable
   public TrackedFakePlayer getTrackedFakePlayer() {
      return this.trackedFakePlayer;
   }

   public TrackedPositionUpdateEvent getTrackedPositionUpdateEvent() {
      return this.trackedPositionUpdateEvent;
   }

   public void handlePlayerMovementTracker(PlayerMovementTracker playerMovementTracker) {
      if (playerMovementTracker.getFlagPair().isCompleted()) {
         this.flag2 = this.flag2 | playerMovementTracker.getFlagPair().getFlagGroup2().isFlag3();
      }
   }

   public void setTrackedPositionUpdateEvent(TrackedPositionUpdateEvent trackedPositionUpdateEvent) {
      this.trackedPositionUpdateEvent = trackedPositionUpdateEvent;
   }

   public void run2() {
      this.setTrackedPositionUpdateEvent(null);
      this.setFlag2(false);
      this.setFlag(false);
      this.setTrackedFakePlayer(this.trackedFakePlayer);
   }

   public void setFlag2(boolean flag) {
      this.flag2 = flag;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }
}
