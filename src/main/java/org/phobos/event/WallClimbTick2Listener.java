package org.phobos.event;

import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.phobos.core.player.PlayerUtils;
import org.phobos.module.movement.WallClimbModule;
import org.phobos.module.movement.WallClimbModule;
import org.phobos.module.movement.WallClimbModule;

public final class WallClimbTick2Listener extends ModuleListener.Safe<WallClimbModule, TickEvent> {
   public void handleObject(
      TickEvent tickEvent, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.mode.testObject2(WallClimbModule.Mode.Polar) && !this.module.check()) {
         KeyboardInput var10000 = tickEvent.getKeyboardInput();
         var10000.jumping = var10000.jumping | (clientPlayerEntity.groundCollision && !PlayerUtils.check());
         if (!this.module.back.testObject2(0.0F) && this.module.flag) {
            tickEvent.getKeyboardInput().movementForward = -this.module.back.getValue();
            this.module.flag = false;
         } else {
            MutableBoolean mutableBoolean = new MutableBoolean();
            switch ((WallClimbModule.Boost)this.module.boost.getValue()) {
               case Threshold:
                  if (clientPlayerEntity.fallDistance > this.module.height.getValue() && clientPlayerEntity.fallDistance < this.module.threshold.getValue()) {
                     tickEvent.getKeyboardInput().movementForward = 1.0F;
                     mutableBoolean.setTrue();
                  }
                  break;
               case Collide:
                  if (clientPlayerEntity.fallDistance > this.module.height.getValue() && !PlayerUtils.check()) {
                     tickEvent.getKeyboardInput().movementForward = 1.0F;
                     mutableBoolean.setTrue();
                  }
            }

            if (mutableBoolean.isTrue()) {
               this.module.flag = true;
               if (this.module.packet.getValue()) {
                  clientPlayerEntity.networkHandler.sendPacket(new ClientCommandC2SPacket(clientPlayerEntity, Mode.START_RIDING_JUMP));
                  clientPlayerEntity.networkHandler.sendPacket(new ClientCommandC2SPacket(clientPlayerEntity, Mode.STOP_RIDING_JUMP));
               }
            }
         }
      }
   }

   public WallClimbTick2Listener(WallClimbModule wallClimbModule) {
      super(wallClimbModule, TickEvent.class, -4);
   }
}
