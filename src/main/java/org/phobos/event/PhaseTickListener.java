package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.phobos.module.movement.PhaseModule;
import org.phobos.module.movement.PhaseModule;

public final class PhaseTickListener extends ModuleListener.Safe<PhaseModule, TickEvent2> {
   public void handleObject(
      TickEvent2 tickEvent2, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      switch ((PhaseModule.Mode)this.module.mode.getValue()) {
         case Desync:
            if (!clientPlayerEntity.isOnGround()) {
               return;
            } else {
               Box box = clientPlayerEntity.getBoundingBox();
               boolean flag = clientPlayerEntity.horizontalCollision;
               if (flag && !this.module.flag) {
                  this.module.flag = true;
               }

               if (flag || !this.module.flag) {
                  return;
               } else {
                  this.module.flag = false;
                  boolean flag2 = !clientWorld.getBlockCollisions(clientPlayerEntity, box.offset(0.0, 0.0, -0.1)).iterator().hasNext();
                  boolean flag3 = !clientWorld.getBlockCollisions(clientPlayerEntity, box.offset(0.0, 0.0, 0.1)).iterator().hasNext();
                  boolean flag4 = !clientWorld.getBlockCollisions(clientPlayerEntity, box.offset(-0.1, 0.0, 0.0)).iterator().hasNext();
                  boolean flag5 = !clientWorld.getBlockCollisions(clientPlayerEntity, box.offset(0.1, 0.0, 0.0)).iterator().hasNext();
                  if (!flag2 && !flag3 && !flag4 && !flag5) {
                     return;
                  } else {
                     double d = flag4 && flag5 ? 0.0 : (flag4 ? -0.1 : 0.1);
                     double d2 = flag2 && flag3 ? 0.0 : (flag2 ? -0.1 : 0.1);
                     Vec3d vec3d = new Vec3d(d, 0.0, d2);
                     Vec3d vec3d2 = clientPlayerEntity.getPos().add(vec3d.multiply(-1.0E-7));
                     clientPlayerEntity.setPosition(vec3d2);
                     if (!this.module.offset.getValue()) {
                        return;
                     } else {
                        clientPlayerEntity.networkHandler.sendPacket(new PositionAndOnGround(vec3d2.getX(), vec3d2.getY(), vec3d2.getZ(), true));
                     }
                  }
               }
            }
      }
   }

   public PhaseTickListener(PhaseModule phaseModule) {
      super(phaseModule, TickEvent2.class);
   }
}
