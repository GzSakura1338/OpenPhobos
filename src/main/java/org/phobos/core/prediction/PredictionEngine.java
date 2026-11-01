package org.phobos.core.prediction;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.earthhack.ducks.IAbstractClientPlayerEntity;
import org.phobos.event.TrackedPositionUpdateEvent;
import org.phobos.core.movement.MovementModel;
import org.phobos.core.PhobosLog;
import org.phobos.core.tick.TimeUtils;
import org.phobos.core.fakeplayer.TrackedFakePlayer;

public class PredictionEngine {
   public final PredictionConditions predictionConditions;
   public final PlayerBacktrackData playerBacktrackData;
   public final PredictionPosition predictionPosition;
   public final TrackedEntityState trackedEntityState = new TrackedEntityState();

   public TrackedFakePlayer resolveTrackedFakePlayer(TrackedFakePlayer[] trackedFakePlayers, int i) {
      TrackedFakePlayer trackedFakePlayer = trackedFakePlayers[i];
      if (trackedFakePlayer == null) {
         trackedFakePlayer = trackedFakePlayers[i] = new TrackedFakePlayer(
            this.playerBacktrackData.getAbstractClientPlayerEntity()
         );
      }

      return trackedFakePlayer;
   }

   public void run() {
      this.trackedEntityState.run2();
      this.predictionPosition.run();
      this.playerBacktrackData.run();
   }

   public PredictionEngine(PredictionConditions predictionConditions, PredictionPosition predictionPosition) {
      this.playerBacktrackData = new PlayerBacktrackData();
      this.predictionConditions = predictionConditions;
      this.predictionPosition = predictionPosition;
   }

   public PredictionConditions getPredictionConditions() {
      return this.predictionConditions;
   }

   public void handleClientWorld(ClientWorld clientWorld) {
      Vec3d vec3d = this.playerBacktrackData.getVec3d();
      TrackedFakePlayer[] trackedFakePlayers = this.playerBacktrackData.getTrackedFakePlayerArray();
      this.playerBacktrackData.setTrackedFakePlayer(null);
      boolean flag = Double.compare(vec3d.lengthSquared(), 0.0) == 0 || vec3d.lengthSquared() > 9.0;
      boolean flag2 = TimeUtils.testLong2(this.playerBacktrackData.getLongValue(), 100L);
      PlayerMovementTracker playerMovementTracker = new PlayerMovementTracker(this.playerBacktrackData.getAbstractClientPlayerEntity());
      boolean flag3 = this.predictionConditions.check2();
      if (flag3) {
         playerMovementTracker.handleClientWorld(clientWorld);
         this.trackedEntityState.handlePlayerMovementTracker(playerMovementTracker);
      }

      for (int i = 0; i < trackedFakePlayers.length; i++) {
         TrackedFakePlayer trackedFakePlayer = this.resolveTrackedFakePlayer(trackedFakePlayers, i);
         if (flag) {
            if (!flag2) {
               return;
            }

            trackedFakePlayer.setPosition(
               this.playerBacktrackData.getDoubleValue2(), this.playerBacktrackData.getDoubleValue3(), this.playerBacktrackData.getDoubleValue()
            );
         } else {
            this.playerBacktrackData.handleTrackedFakePlayer(trackedFakePlayer, vec3d);
            TimelineStep.resolveTimelineStep3(
               timelineStep -> timelineStep.resolveTimelineStep(
                     () -> this.trackedEntityState.handleTrackedFakePlayer(trackedFakePlayer),
                     this.trackedEntityState::run,
                     () -> {
                        this.predictionPosition
                           .handleVec3d(vec3d, trackedFakePlayer, this.playerBacktrackData.getTrackedFakePlayer(), playerMovementTracker, flag3);
                        this.playerBacktrackData.handleTrackedFakePlayer3(trackedFakePlayer, this.predictionPosition.getVec3d());
                        this.playerBacktrackData.handleTrackedFakePlayer2(trackedFakePlayer);
                        this.predictionPosition.run();
                     }
                  )
                  .resolveTimelineStep2(() -> this.playerBacktrackData.setTrackedFakePlayer(trackedFakePlayer)),
               throwable -> {
                  PhobosLog.handleText("Prediction/ML", "Caught exception during timeline execution");
                  throwable.printStackTrace();
               }
            );
         }
      }
   }

   public PredictionPosition getPredictionPosition() {
      return this.predictionPosition;
   }

   public void handleAbstractClientPlayerEntity(AbstractClientPlayerEntity abstractClientPlayerEntity) {
      if (!abstractClientPlayerEntity.isMainPlayer() && abstractClientPlayerEntity instanceof IAbstractClientPlayerEntity iAbstractClientPlayerEntity) {
         for (TrackedFakePlayer trackedFakePlayer : iAbstractClientPlayerEntity.getPredictions()) {
            if (trackedFakePlayer == null || !TimeUtils.testLong2(trackedFakePlayer.getLongValue(), 150L)) {
               break;
            }

            trackedFakePlayer.setVelocity(Vec3d.ZERO);
            trackedFakePlayer.setPosition(
               iAbstractClientPlayerEntity.getLerpX(), iAbstractClientPlayerEntity.getLerpY(), iAbstractClientPlayerEntity.getLerpZ()
            );
         }
      }
   }

   public TrackedEntityState getTrackedEntityState() {
      return this.trackedEntityState;
   }

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      TrackedPositionUpdateEvent trackedPositionUpdateEvent = this.trackedEntityState.getTrackedPositionUpdateEvent();
      if (trackedPositionUpdateEvent == null) {
         return false;
      }

      if (trackedPositionUpdateEvent.getLivingEntity() instanceof AbstractClientPlayerEntity abstractClientPlayerEntity
         && trackedPositionUpdateEvent.getLivingEntity() instanceof IAbstractClientPlayerEntity iAbstractClientPlayerEntity
         && !(trackedPositionUpdateEvent.getLivingEntity().squaredDistanceTo(clientPlayerEntity) > PredictionManager.doubleValue)) {
         double d = iAbstractClientPlayerEntity.getLerpX();
         double d2 = iAbstractClientPlayerEntity.getLerpY();
         double d3 = iAbstractClientPlayerEntity.getLerpZ();
         Vec3d vec3d = new Vec3d(
               trackedPositionUpdateEvent.getDoubleValue3() - d,
               trackedPositionUpdateEvent.getDoubleValue2() - d2,
               trackedPositionUpdateEvent.getDoubleValue() - d3
            )
            .multiply(this.predictionConditions.getDoubleSupplier().getAsDouble());
         if (vec3d.getY() > 0.6) {
            iAbstractClientPlayerEntity.getMovementModel().handleText("step", 250L);
         }

         if (abstractClientPlayerEntity.fallDistance <= 0.0F && vec3d.getY() < 0.6) {
            iAbstractClientPlayerEntity.getMovementModel().handleText("fall", 15000L);
         }

         this.trackedEntityState.setFlag2(iAbstractClientPlayerEntity.getMovementModel().testText("step") && this.predictionConditions.check());
         this.trackedEntityState.setFlag(iAbstractClientPlayerEntity.getMovementModel().testText("fall") && this.predictionConditions.check3());
         this.playerBacktrackData.setLongValue(trackedPositionUpdateEvent.getLongValue());
         this.playerBacktrackData.setTrackedFakePlayerArray(iAbstractClientPlayerEntity.getPredictions());
         this.playerBacktrackData.setAbstractClientPlayerEntity(abstractClientPlayerEntity);
         this.playerBacktrackData.setDoubleValue2(trackedPositionUpdateEvent.getDoubleValue3());
         this.playerBacktrackData.setDoubleValue3(trackedPositionUpdateEvent.getDoubleValue2());
         this.playerBacktrackData.setDoubleValue(trackedPositionUpdateEvent.getDoubleValue());
         this.playerBacktrackData.setVec3d(vec3d);
         return true;
      } else {
         return false;
      }
   }

   public PlayerBacktrackData getPlayerBacktrackData() {
      return this.playerBacktrackData;
   }

   public void run2() {
      this.trackedEntityState.setTrackedPositionUpdateEvent(null);
   }

   public PredictionEngine(PredictionConditions predictionConditions, MovementModel movementModel) {
      this.playerBacktrackData = new PlayerBacktrackData();
      this.predictionConditions = predictionConditions;
      this.predictionPosition = new PredictionPosition(movementModel, this.trackedEntityState);
   }
}
