package org.phobos.core.movement;

import java.util.function.BiConsumer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.module.client.NotificationsModule;
import org.phobos.core.Feature;
import org.phobos.core.FeatureRegistry;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.fakeplayer.MovingFakePlayer;
import org.phobos.core.PhobosClient;
import org.phobos.core.event.events.UnusedEvent;

public class MovementTracker extends Feature {
   public MovingFakePlayer movingFakePlayer;
   public final FeatureRegistry featureRegistry;

   public MovementTracker(PhobosClient phobosClient, FeatureRegistry featureRegistry) {
      this.featureRegistry = featureRegistry;
      this.handleListener(
         new SafeListener<PlayerTickEvent>(Integer.MIN_VALUE) {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (featureRegistry.getEngineModule2().getMovement().getValue()) {
                  MovementTracker.this.handlePlayerEntity2(clientPlayerEntity, clientWorld);
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<UnusedEvent>(Integer.MIN_VALUE) {
            public void handleObject(
               UnusedEvent unusedEvent, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (featureRegistry.getEngineModule2().getMovement().getValue()) {
                  MovementTracker.this.handlePlayerEntity(
                     clientPlayerEntity,
                     (playerEntity, doubleValue) -> {
                        if (!(Math.abs(doubleValue) < 0.01)) {
                           if (featureRegistry.getEngineModule2().getMovementDebug().getValue()) {
                              NotificationsModule.getNotificationsModule()
                                 .handleTriConsumer(
                                    (color, color2, color3) -> NotificationsModule.getNotificationsModule()
                                       .handleText5(Text.literal("[Movement] Offset: %s".formatted(doubleValue)).withColor(color2.hashCode()))
                                 );
                           }
                        }
                     }
                  );
               }
            }
         }
      );
      ModuleListenerUtils.handleListenerHolder4(this, () -> this.movingFakePlayer = null);
   }

   public void handlePlayerEntity(PlayerEntity playerEntity, BiConsumer<PlayerEntity, Double> biConsumer) {
      if (this.movingFakePlayer != null) {
         Vec3d vec3d = playerEntity.getVelocity();
         Vec3d vec3d2 = this.movingFakePlayer.getVelocity();
         double d = vec3d.lengthSquared();
         double d2 = vec3d2.lengthSquared();
         biConsumer.accept(this.movingFakePlayer, d - d2);
      }
   }

   public void handlePlayerEntity2(PlayerEntity playerEntity, ClientWorld clientWorld) {
      if (this.movingFakePlayer == null) {
         this.movingFakePlayer = new MovingFakePlayer(clientWorld);
      }

      for (int i = 0; i < this.movingFakePlayer.getInventory().size(); i++) {
         this.movingFakePlayer.getInventory().setStack(i, playerEntity.getInventory().getStack(i));
      }

      this.movingFakePlayer.copyFrom(playerEntity);
      this.movingFakePlayer.setYaw(playerEntity.getYaw());
      this.movingFakePlayer.setPitch(playerEntity.getPitch());
      this.movingFakePlayer.setPosition(playerEntity.getPos());
      this.movingFakePlayer.setPose(playerEntity.getPose());
      this.movingFakePlayer.setSwimming(playerEntity.isSwimming());
      this.movingFakePlayer.travel(new Vec3d(playerEntity.sidewaysSpeed, playerEntity.upwardSpeed, playerEntity.forwardSpeed));
   }
}
