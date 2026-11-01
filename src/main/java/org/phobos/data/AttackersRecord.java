package org.phobos.data;

import java.util.List;
import net.minecraft.entity.player.PlayerEntity;

public record AttackersRecord(List<PlayerEntity> attackers, PlayerEntity victim) {

   public boolean check() {
      return this.attackers.isEmpty();
   }

   public boolean testPlayerEntity(PlayerEntity playerEntity) {
      return this.attackers.contains(playerEntity);
   }
}
