package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.phobos.module.movement.HighJumpModule;
import org.phobos.module.movement.HighJumpModule;
import org.phobos.module.movement.HighJumpModule;

public final class HighJumpPlayerTickListener extends ModuleListener.Safe<HighJumpModule, PlayerTickEvent> {
   public void handleObject(
      PlayerTickEvent playerTickEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      switch ((HighJumpModule.Mode)this.module.mode.getValue()) {
         case Polar:
            switch (this.module.stage) {
               case None:
                  if (this.module.intValue2++ > this.module.ticks.getValue()) {
                     this.module.handleClientPlayerEntity(clientPlayerEntity);
                     this.module.stage = HighJumpModule.Stage.Lag;
                     this.module.intValue2 = 0;
                  }
                  break;
               case Lag:
                  this.module.handleClientPlayerEntity(clientPlayerEntity);
                  if (++this.module.intValue > this.module.lag.getValue()) {
                     this.module.stage = HighJumpModule.Stage.Boost;
                  }
                  break;
               case Boost:
                  this.phobosClient.getTimerManager().run2();
                  if (clientPlayerEntity.fallDistance <= 0.0F || !this.module.automatic.getValue()) {
                     return;
                  } else {
                     Vec3d vec3d = clientPlayerEntity.getPos();
                     Vec3d vec3d2 = vec3d.add(0.0, -5.0, 0.0);
                     BlockHitResult blockHitResult = clientWorld.raycast(
                        new RaycastContext(vec3d, vec3d2, ShapeType.OUTLINE, FluidHandling.NONE, clientPlayerEntity)
                     );
                     if (blockHitResult != null && vec3d.distanceTo(blockHitResult.getPos()) <= 3.0) {
                        this.module.stage = HighJumpModule.Stage.None;
                     }
                  }
            }
      }
   }

   public HighJumpPlayerTickListener(HighJumpModule highJumpModule) {
      super(highJumpModule, PlayerTickEvent.class);
   }
}
