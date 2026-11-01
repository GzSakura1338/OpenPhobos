package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.phobos.core.event.events.PlayerMoveEvent;
import org.phobos.core.player.PlayerUtils;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.module.movement.PacketFlyModule;
import org.phobos.module.movement.PacketFlyModule;
import org.phobos.module.movement.PacketFlyModule;

public final class PacketFlyPlayerMoveListener extends ModuleListener.Safe<PacketFlyModule, PlayerMoveEvent> {
   public PacketFlyPlayerMoveListener(PacketFlyModule packetFlyModule) {
      super(packetFlyModule, PlayerMoveEvent.class);
   }

   public void handleObject(
      PlayerMoveEvent playerMoveEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      clientPlayerEntity.setVelocity(Vec3d.ZERO);
      if (!this.module.mode.getValue().equals(PacketFlyModule.Mode.Jitter) && this.module.atomicInteger.get() == 0) {
         if (this.module.testInt(6)) {
            this.module.handleClientPlayerEntity(clientPlayerEntity, Vec3d.ZERO, false);
         }
      } else {
         boolean flag = this.module.testClientPlayerEntity(clientPlayerEntity, clientWorld);
         double d;
         if (mc.options.jumpKey.isPressed() && (flag || !PlayerUtils.check())) {
            if (this.module.antiKick.getValue() && !flag) {
               d = this.module.testInt(this.module.mode.getValue().equals(PacketFlyModule.Mode.Jitter) ? 10 : 20) ? -0.032 : 0.062;
            } else {
               d = 0.062;
            }
         } else if (mc.options.sneakKey.isPressed()) {
            d = -0.062;
         } else {
            d = !flag ? (this.module.testInt(4) ? (this.module.antiKick.getValue() ? -0.04 : 0.0) : 0.0) : 0.0;
         }

         if (this.module.phase.getValue().equals(PacketFlyModule.Phase.Full) && flag && PlayerUtils.check() && d != 0.0) {
            d /= 2.5;
         }

         double[] doubles = PlayerUtils.values2(clientPlayerEntity, this.module.phase.getValue().equals(PacketFlyModule.Phase.Full) && flag ? 0.031 : 0.26);
         this.module.floatValue = this.module.factor.getValue();

         for (int i = 1; i <= (this.module.mode.getValue().equals(PacketFlyModule.Mode.Factor) ? this.module.floatValue : 1.0F); i++) {
            PlayerActionUtils.handleClientPlayerEntity24(clientPlayerEntity, doubles[0] * i);
            PlayerActionUtils.handleClientPlayerEntity29(clientPlayerEntity, d * i);
            PlayerActionUtils.handleClientPlayerEntity26(clientPlayerEntity, doubles[1] * i);
            this.module.handleClientPlayerEntity(clientPlayerEntity, clientPlayerEntity.getVelocity(), true);
         }
      }
   }
}
