package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.phobos.core.chunk.Hole;
import org.phobos.core.player.PlayerUtils;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.module.movement.AnchorModule;
import org.phobos.module.movement.AnchorModule;

public final class AnchorMoveListener extends ModuleListener.Safe<AnchorModule, MoveEvent2> {
   public AnchorMoveListener(AnchorModule anchorModule) {
      super(anchorModule, MoveEvent2.class);
   }

   public void handleObject(
      MoveEvent2 moveEvent2, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (!this.phobosClient.getSurroundManager().testPlayerEntity(clientPlayerEntity, false)) {
         if (!BlockUtils.testPlayerEntity(clientPlayerEntity, clientWorld)) {
            for (AnchorModule.Disable disable : AnchorModule.Disable.values()) {
               if (this.module.disablers.getValue().get(disable) && disable.getTest().test(this.phobosClient)) {
                  return;
               }
            }

            Vec3d vec3d = clientPlayerEntity.getPos();
            Hole hole = this.phobosClient
               .getHoleChunkScanner()
               .resolveHole3(
                  clientPlayerEntity,
                  holex -> holex.testClientPlayerEntity(clientPlayerEntity, clientWorld, this.phobosClient.getMotionManager().isFlag42())
                     && holex.testWorld(clientWorld)
                     && (this.module.partial.getValue() || holex.isFlag22())
               );
            if (hole != null) {
               if (!(hole.computeEntity(clientPlayerEntity) >= 36.0)) {
                  Vec3d vec3d2 = hole.createVec3d();
                  Vec3d vec3d3 = new Vec3d(vec3d2.x, vec3d.y, vec3d2.z);
                  double d = vec3d.distanceTo(vec3d3);
                  if (!(d > this.module.range.getValue())) {
                     if (hole.isFlag5() || !hole.testEntity2(clientPlayerEntity)) {
                        if (hole.testEntity(clientPlayerEntity)) {
                           moveEvent2.setDoubleValue3(0.0);
                           moveEvent2.setDoubleValue2(0.0);
                        } else {
                           double d2 = Math.toRadians(RotationUtils.resolveVector2d(vec3d, vec3d3).x);
                           double d3 = PlayerUtils.computeClientPlayerEntity4(clientPlayerEntity) * this.module.acceleration.getValue();
                           double d4 = clientPlayerEntity.isOnGround() ? -Math.min(d3, d / this.module.drag.getValue()) : -d3;
                           double d5 = -Math.sin(d2) * d4;
                           double d6 = Math.cos(d2) * d4;
                           moveEvent2.setDoubleValue3(d5);
                           moveEvent2.setDoubleValue2(d6);
                        }
                     }
                  }
               }
            }
         }
      }
   }
}
