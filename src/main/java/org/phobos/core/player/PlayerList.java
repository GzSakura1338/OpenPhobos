package org.phobos.core.player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PlayerList implements Iterable<PlayerEntity> {
   public final List<PlayerEntity> playerEntityList = new ArrayList<>();

   public void handlePlayerEntity(PlayerEntity playerEntity) {
      this.playerEntityList.add(playerEntity);
   }

   @NotNull
   @Override
   public Iterator<PlayerEntity> iterator() {
      return this.playerEntityList.iterator();
   }
}
