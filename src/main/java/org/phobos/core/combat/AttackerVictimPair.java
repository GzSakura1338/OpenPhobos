package org.phobos.core.combat;

import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

public class AttackerVictimPair {
   @NotNull
   public final PlayerEntity playerEntity;
   @NotNull
   public final PlayerEntity playerEntity2;

   @NotNull
   public PlayerEntity getPlayerEntity2() {
      return this.playerEntity2;
   }

   @NotNull
   public PlayerEntity getPlayerEntity() {
      return this.playerEntity;
   }

   public AttackerVictimPair(@NotNull PlayerEntity playerEntity, @NotNull PlayerEntity playerEntity2) {
      if (playerEntity == null) {
         throw new NullPointerException("entity is marked non-null but is null");
      }

      if (playerEntity2 == null) {
         throw new NullPointerException("attacker is marked non-null but is null");
      }

      this.playerEntity = playerEntity;
      this.playerEntity2 = playerEntity2;
   }
}
