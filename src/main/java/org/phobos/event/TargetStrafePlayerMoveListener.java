package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.phobos.core.event.events.PlayerMoveEvent;
import org.phobos.core.player.PlayerUtils;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.module.combat.AuraModule;
import org.phobos.module.movement.FlightModule;
import org.phobos.module.player.ScaffoldModule;
import org.phobos.module.movement.SpeedModule;
import org.phobos.module.movement.TargetStrafeModule;

public final class TargetStrafePlayerMoveListener extends ModuleListener.Safe<TargetStrafeModule, PlayerMoveEvent> {
   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      for (int i = (int)clientPlayerEntity.getY(); i >= (int)clientPlayerEntity.getY() - 5; i--) {
         if (BlockUtils.testBlockPos7(new BlockPos(clientPlayerEntity.getBlockX(), i, clientPlayerEntity.getBlockZ()), clientWorld)) {
            return true;
         }
      }

      return false;
   }

   public TargetStrafePlayerMoveListener(TargetStrafeModule targetStrafeModule) {
      super(targetStrafeModule, PlayerMoveEvent.class);
   }

   public void handleObject(
      PlayerMoveEvent playerMoveEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (!ScaffoldModule.getScaffoldModule().check7() && AuraModule.getAuraModule().check7()) {
         TargetStrafeModule.flag3 = true;
         if ((this.module.speed.getValue() || !SpeedModule.getSpeedModule().check7())
            && (this.module.fly.getValue() || !FlightModule.getFlightModule().check7())
            && (this.module.manual.getValue() || SpeedModule.getSpeedModule().check7() || FlightModule.getFlightModule().check7())) {
            if (AuraModule.getEntity() == null) {
               TargetStrafeModule.entity = null;
            } else {
               if (!clientPlayerEntity.horizontalCollision && this.testClientPlayerEntity(clientPlayerEntity, clientWorld)) {
                  this.module.flag2 = false;
               } else {
                  if (!this.module.flag2) {
                     if (this.module.strafe.getValue()) {
                        double[] doubles = PlayerUtils.values2(clientPlayerEntity, PlayerUtils.computeClientPlayerEntity4(clientPlayerEntity));
                        PlayerActionUtils.handleClientPlayerEntity24(clientPlayerEntity, doubles[0]);
                        PlayerActionUtils.handleClientPlayerEntity26(clientPlayerEntity, doubles[1]);
                     }

                     this.module.flag = !this.module.flag;
                  }

                  this.module.flag2 = true;
               }

               TargetStrafeModule.entity = AuraModule.getEntity();
               if (TargetStrafeModule.entity != null) {
                  float f = RotationUtils.values2(clientPlayerEntity, clientPlayerEntity.getPos())[0] + 135 * (this.module.flag ? -1 : 1);
                  double d = this.module.range.getValue() + Math.random() / 100.0;
                  double d2 = MathHelper.sin((float)Math.toRadians(f)) * d + TargetStrafeModule.entity.getX();
                  double d3 = MathHelper.cos((float)Math.toRadians(f)) * d + TargetStrafeModule.entity.getZ();
                  Vec3d vec3d = new Vec3d(d2, TargetStrafeModule.entity.getY(), d3);
                  TargetStrafeModule.floatValue = RotationUtils.values2(clientPlayerEntity, vec3d)[0];
               }
            }
         } else {
            TargetStrafeModule.entity = null;
         }
      } else {
         TargetStrafeModule.flag3 = false;
      }
   }
}
