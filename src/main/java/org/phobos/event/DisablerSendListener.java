package org.phobos.event;

import java.util.function.Consumer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdatePlayerAbilitiesC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.OnGroundOnly;
import org.earthhack.ducks.IClientPlayerNetworkHandler;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.mixin.net.interfaces.ICommonPongC2SPacket;
import org.phobos.mixin.net.interfaces.IPlayerMoveC2SPacket;
import org.phobos.module.movement.DisablerModule;
import org.phobos.module.movement.DisablerModule;
import org.phobos.module.player.ScaffoldModule;

public final class DisablerSendListener extends ModuleListener.Safe<DisablerModule, PacketEvent.Send<?>> {
   public void handleObject(
      PacketEvent.Send<?> send,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      Packet<?> packet = send.getPacket();
      switch ((DisablerModule.Mode)this.module.mode.getValue()) {
         case Sentinel:
            if (this.module.intValue == 1) {
               this.module.intValue++;
            }

            if (this.module.intValue >= 3) {
               if (packet instanceof PlayerInputC2SPacket) {
                  ((IClientPlayerNetworkHandler)(Object)clientPlayerEntity.networkHandler).handlePacket(new PlayerInputC2SPacket(0.0F, 0.0F, false, false));
                  send.setCancelled(true);
               }

               if ((this.module.intValue >= 328 || this.module.intValue <= 5) && packet instanceof PlayerMoveC2SPacket) {
                  send.setCancelled(true);
               } else if (this.module.intValue % 55 == 0) {
                  ((IClientPlayerNetworkHandler)(Object)clientPlayerEntity.networkHandler).handlePacket(new OnGroundOnly(true));
               }
            }

            if (packet instanceof CommonPongC2SPacket commonPongC2SPacket) {
               int i = commonPongC2SPacket.getParameter();
               if (i >= 0 || this.module.intValue < 3 && this.module.intValue != 0) {
                  if (i != 0) {
                     send.setCancelled(true);
                     if (this.module.intValue >= 330 || this.module.intValue == 2) {
                        ((IClientPlayerNetworkHandler)(Object)clientPlayerEntity.networkHandler).handlePacket(new CommonPongC2SPacket(i + 3));
                        this.module.intValue = 3;
                     }
                  }
               } else {
                  this.module.intValue++;
                  ((IClientPlayerNetworkHandler)(Object)clientPlayerEntity.networkHandler).handlePacket(new CommonPongC2SPacket(i + 1));
               }
            }
            break;
         case Verus:
            if (packet instanceof CommonPongC2SPacket && this.module.flag10) {
               ((ICommonPongC2SPacket)(Object)send.getPacket()).setParameter(this.module.flag4 ? -1 : 1);
               this.module.flag4 = !this.module.flag4;
            }
            break;
         case Grim:
            if (clientPlayerEntity.getAbilities().flying || !clientPlayerEntity.isCreative() && !clientPlayerEntity.getAbilities().allowFlying) {
               return;
            }

            Consumer<ClientPlayerEntity> consumer = clientPlayerEntityx -> this.module
               .handlePacket(new UpdatePlayerAbilitiesC2SPacket(clientPlayerEntityx.getAbilities()));
            send.setCancelled(true);
            clientPlayerEntity.getAbilities().flying = true;
            consumer.accept(clientPlayerEntity);
            this.module.handlePacket(packet);
            clientPlayerEntity.getAbilities().flying = false;
            consumer.accept(clientPlayerEntity);
            break;
         case Watchdog:
            if (!this.module.scaffold.getValue()) {
               return;
            }

            if (packet instanceof PlayerMoveC2SPacket playerMoveC2SPacket) {
               if (!this.module.flag8) {
                  return;
               }

               if (!ScaffoldModule.getScaffoldModule().check7()) {
                  return;
               }

               if (ScaffoldModule.getScaffoldModule().check3()) {
                  this.module.flag8 = false;
                  this.module.flag = false;
                  return;
               }

               if (clientPlayerEntity.isOnGround()) {
                  IPlayerMoveC2SPacket iPlayerMoveC2SPacket = (IPlayerMoveC2SPacket)(Object)playerMoveC2SPacket;
                  iPlayerMoveC2SPacket.setY(iPlayerMoveC2SPacket.getY() + clientPlayerEntity.age % 2 == 0.0 ? 0.03 : 1.0E-5);
               }
            }
            break;
         case Swing:
            if (packet instanceof HandSwingC2SPacket) {
               send.setCancelled(true);
            }
      }
   }

   public DisablerSendListener(DisablerModule disablerModule) {
      super(disablerModule, PacketEvent.Send.class);
   }
}
