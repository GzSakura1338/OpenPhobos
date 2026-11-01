package org.phobos.core.prediction;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.earthhack.ducks.IAbstractClientPlayerEntity;
import org.jetbrains.annotations.Range;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.event.TrackedPositionUpdateEvent;
import org.phobos.module.combat.AntiBotModule;
import org.phobos.module.misc.FastLatencyModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.Feature;
import org.phobos.core.FeatureRegistry;
import org.phobos.core.movement.LowHopMovementModel;
import org.phobos.core.math.MathUtils;
import org.phobos.core.fakeplayer.MovingFakePlayer;
import org.phobos.core.player.PlayerActionUtils;

public class PredictionManager extends Feature {
   public final FeatureRegistry featureRegistry;
   public static final double doubleValue = MathHelper.square(30.0);
   public final PredictionEngine predictionEngine = new PredictionEngine(PredictionConditions.createPredictionConditions(), new LowHopMovementModel());
   public static final Logger LOGGER = LoggerFactory.getLogger(PredictionManager.class);

   public PlayerEntity resolvePlayerEntity2(@Range(from = 0L, to = 16L) int i, ClientPlayerEntity clientPlayerEntity) {
      if (!this.featureRegistry.getEngineModule().check2() || i == 0) {
         return clientPlayerEntity;
      }

      if (mc.world == null) {
         return clientPlayerEntity;
      }

      MovingFakePlayer movingFakePlayer = new MovingFakePlayer(mc.world);
      movingFakePlayer.copyFrom(clientPlayerEntity);
      movingFakePlayer.setFlag4(true);
      int i3 = MathUtils.computeInt3(i, 0, 16);
      Vec3d vec3d = new Vec3d(clientPlayerEntity.sidewaysSpeed, clientPlayerEntity.upwardSpeed, clientPlayerEntity.forwardSpeed);

      for (int i2 = 0; i2 < i3; i2++) {
         movingFakePlayer.travel(vec3d);
      }

      return movingFakePlayer;
   }

   public PlayerEntity resolvePlayerEntity3(@Range(from = 0L, to = 6L) int i, PlayerEntity playerEntity) {
      if (!this.featureRegistry.getEngineModule().check2()) {
         return playerEntity;
      } else {
         return (PlayerEntity)(playerEntity instanceof AbstractClientPlayerEntity abstractClientPlayerEntity
               && abstractClientPlayerEntity instanceof IAbstractClientPlayerEntity iAbstractClientPlayerEntity
               && !(playerEntity instanceof ClientPlayerEntity)
            ? iAbstractClientPlayerEntity.getPredictions()[Math.max(0, Math.min(i - 1, iAbstractClientPlayerEntity.getPredictions().length - 1))]
            : playerEntity);
      }
   }

   public PlayerEntity resolvePlayerEntity(PlayerEntity playerEntity) {
      if (!this.featureRegistry.getEngineModule().check2()) {
         return playerEntity;
      } else {
         return (PlayerEntity)(playerEntity instanceof AbstractClientPlayerEntity abstractClientPlayerEntity
               && abstractClientPlayerEntity instanceof IAbstractClientPlayerEntity iAbstractClientPlayerEntity
               && !(playerEntity instanceof ClientPlayerEntity)
            ? iAbstractClientPlayerEntity.getPredictions()[Math.max(
               0, Math.min(MathUtils.computeInt3(this.getIntValue() / 50, 0, 4) - 1, iAbstractClientPlayerEntity.getPredictions().length - 1)
            )]
            : playerEntity);
      }
   }

   public PredictionManager(FeatureRegistry featureRegistry) {
      this.featureRegistry = featureRegistry;
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               for (AbstractClientPlayerEntity abstractClientPlayerEntity : clientWorld.getPlayers()) {
                  if (!AntiBotModule.testEntity2(abstractClientPlayerEntity)) {
                     PredictionManager.this.predictionEngine.handleAbstractClientPlayerEntity(abstractClientPlayerEntity);
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<TrackedPositionUpdateEvent>() {
            public void handleObject(
               TrackedPositionUpdateEvent trackedPositionUpdateEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (featureRegistry.getEngineModule().check2()) {
                  if (!AntiBotModule.testEntity2(trackedPositionUpdateEvent.getLivingEntity())) {
                     PredictionManager.this.predictionEngine.getTrackedEntityState().setTrackedPositionUpdateEvent(trackedPositionUpdateEvent);
                     if (!PredictionManager.this.predictionEngine.testClientPlayerEntity(clientPlayerEntity)) {
                        PredictionManager.this.predictionEngine.run2();
                     } else {
                        PredictionManager.this.predictionEngine.handleClientWorld(clientWorld);
                        PredictionManager.this.predictionEngine.run();
                     }
                  }
               }
            }
         }
      );
   }

   public boolean testPlayerEntity(PlayerEntity playerEntity, ClientWorld clientWorld, BlockPos blockPos) {
      return this.testPlayerEntity2(playerEntity, clientWorld, blockPos, 1) || this.testPlayerEntity2(playerEntity, clientWorld, blockPos, 2);
   }

   public int getIntValue() {
      ClientPlayerEntity clientPlayerEntity = mc.player;
      if (clientPlayerEntity == null) {
         return 0;
      }

      if (FastLatencyModule.getFastLatencyModule().check7()) {
         return FastLatencyModule.getFastLatencyModule().getIntValue();
      }

      PlayerListEntry playerListEntry = clientPlayerEntity.networkHandler.getPlayerListEntry(clientPlayerEntity.getUuid());
      return playerListEntry == null ? 0 : playerListEntry.getLatency();
   }

   public PlayerEntity resolvePlayerEntity4(ClientPlayerEntity clientPlayerEntity) {
      if (!this.featureRegistry.getEngineModule().check2()) {
         return clientPlayerEntity;
      }

      if (mc.world == null) {
         return clientPlayerEntity;
      }

      MovingFakePlayer movingFakePlayer = new MovingFakePlayer(mc.world);
      movingFakePlayer.copyFrom(clientPlayerEntity);
      int i2 = MathUtils.computeInt3(MathUtils.computeInt3(this.getIntValue() / 50, 0, 4), 0, 16);
      Vec3d vec3d = new Vec3d(clientPlayerEntity.sidewaysSpeed, clientPlayerEntity.upwardSpeed, clientPlayerEntity.forwardSpeed);

      for (int i = 0; i < i2; i++) {
         movingFakePlayer.travel(vec3d);
      }

      return movingFakePlayer;
   }

   public boolean testPlayerEntity2(PlayerEntity playerEntity, ClientWorld clientWorld, BlockPos blockPos, int i) {
      PlayerEntity playerEntity2 = this.resolvePlayerEntity3(i, playerEntity);
      return playerEntity2 == null
         ? false
         : PlayerActionUtils.testPlayerEntity(playerEntity, vec3d -> Math.abs(vec3d.squaredDistanceTo(playerEntity2.getPos())) > 3.4899284E-4)
            && BlockUtils.testBox(playerEntity2.getBoundingBox().expand(0.03409882741414, 0.0, 0.03409882741414), clientWorld, blockPos);
   }
}
