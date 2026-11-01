package org.phobos.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import org.phobos.core.event.events.CancellableEvent;

public class UseItemEvent extends CancellableEvent {
   public PlayerEntity playerEntity;
   public Hand hand;

   public void setHand(Hand hand) {
      this.hand = hand;
   }

   public Hand getHand() {
      return this.hand;
   }

   public UseItemEvent(Hand hand, PlayerEntity playerEntity) {
      this.hand = hand;
      this.playerEntity = playerEntity;
   }

   public void setPlayerEntity(PlayerEntity playerEntity) {
      this.playerEntity = playerEntity;
   }

   public PlayerEntity getPlayerEntity() {
      return this.playerEntity;
   }
}
