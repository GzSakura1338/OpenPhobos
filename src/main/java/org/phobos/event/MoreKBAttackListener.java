package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import org.phobos.module.combat.MoreKBModule;
import org.phobos.module.combat.MoreKBModule;

public final class MoreKBAttackListener extends ModuleListener.Safe<MoreKBModule, AttackEvent2> {
   public MoreKBAttackListener(MoreKBModule moreKBModule) {
      super(moreKBModule, AttackEvent2.class);
   }

   public void handleClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity) {
      clientPlayerEntity.networkHandler.sendPacket(new ClientCommandC2SPacket(clientPlayerEntity, Mode.STOP_SPRINTING));
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      clientPlayerEntity.networkHandler.sendPacket(new ClientCommandC2SPacket(clientPlayerEntity, Mode.START_SPRINTING));
   }

   public void handleObject(
      AttackEvent2 attackEvent2, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.chance.getValue().equals(100.0) || !(Math.random() * 100.0 > this.module.chance.getValue())) {
         switch ((MoreKBModule.Mode)this.module.mode.getValue()) {
            case Legit_Fast:
               mc.options.sprintKey.setPressed(false);
               this.module.flag = true;
               break;
            case Packet:
               if (clientPlayerEntity.isSprinting()) {
                  this.handleClientPlayerEntity2(clientPlayerEntity);
               }

               this.handleClientPlayerEntity(clientPlayerEntity);
               this.handleClientPlayerEntity2(clientPlayerEntity);
               this.handleClientPlayerEntity(clientPlayerEntity);
         }
      }
   }
}
