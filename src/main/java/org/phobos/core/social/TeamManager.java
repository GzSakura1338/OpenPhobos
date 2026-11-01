package org.phobos.core.social;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.phobos.event.AddEntityEvent;
import org.phobos.event.RemoveEntityEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.core.combat.AttackerVictimPair;
import org.phobos.core.Feature;
import org.phobos.core.FeatureRegistry;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.PhobosClient;
import org.phobos.core.tick.TimeUtils;

public class TeamManager extends Feature {
   public final Set<PlayerEntity> playerEntitySet;
   public final List<TeamManager.PlayerTeam> playerTeamList = new ArrayList<>();

   @Nullable
   public TeamManager.PlayerTeam resolvePlayerTeam(PlayerEntity playerEntity) {
      return this.playerTeamList.stream().filter(playerTeam -> playerTeam.playerEntityList.contains(playerEntity)).findFirst().orElse(null);
   }

   public TeamManager(PhobosClient phobosClient, FeatureRegistry featureRegistry) {
      this.playerEntitySet = new HashSet<>();
      this.handleListener(
         new SafeListener<AttackerVictimPair>() {
            public void handleObject(
               AttackerVictimPair attackerVictimPair,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               PlayerEntity playerEntity2 = attackerVictimPair.getPlayerEntity2();
               if (!playerEntity2.equals(clientPlayerEntity) && attackerVictimPair.getPlayerEntity().equals(clientPlayerEntity)) {
                  for (PlayerEntity playerEntity : clientWorld.getPlayers()) {
                     if (!(playerEntity2.distanceTo(playerEntity) > 7.0)
                        && !playerEntity.equals(clientPlayerEntity)
                        && clientPlayerEntity.distanceTo(playerEntity2) > playerEntity2.distanceTo(playerEntity)) {
                        if (TeamManager.this.playerEntitySet.contains(playerEntity)) {
                           TeamManager.PlayerTeam playerTeam = TeamManager.this.resolvePlayerTeam(playerEntity);
                           if (playerTeam != null) {
                              playerTeam.playerEntityList.add(playerEntity2);
                           } else {
                              TeamManager.PlayerTeam playerTeam2 = new TeamManager.PlayerTeam(playerEntity2);
                              playerTeam2.playerEntityList.add(playerEntity);
                              TeamManager.this.playerTeamList.add(playerTeam2);
                           }
                        } else {
                           TeamManager.this.playerEntitySet.add(playerEntity2);
                        }
                        break;
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<RemoveEntityEvent>() {
            public void handleObject(
               RemoveEntityEvent removeEntityEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (removeEntityEvent.getEntity() instanceof PlayerEntity playerEntity) {
                  for (TeamManager.PlayerTeam playerTeam : TeamManager.this.playerTeamList) {
                     playerTeam.getPlayerEntityMap().put(playerEntity, System.currentTimeMillis());
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<AddEntityEvent>() {
            public void handleObject(
               AddEntityEvent addEntityEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (addEntityEvent.getEntity() instanceof PlayerEntity playerEntity) {
                  for (TeamManager.PlayerTeam playerTeam : TeamManager.this.playerTeamList) {
                     playerTeam.getPlayerEntityMap().remove(playerEntity);
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               for (TeamManager.PlayerTeam playerTeam : TeamManager.this.playerTeamList) {
                  if (!playerTeam.getPlayerEntityMap().isEmpty()) {
                     List<PlayerEntity> list = new ArrayList<>();
                     playerTeam.getPlayerEntityMap().entrySet().removeIf(entry -> {
                        if (!TimeUtils.testLong2(entry.getValue(), 60000L)) {
                           return false;
                        }

                        list.add(entry.getKey());
                        return true;
                     });

                     for (PlayerEntity playerEntity : list) {
                        playerTeam.getPlayerEntityMap().remove(playerEntity);
                        playerTeam.getPlayerEntityList().remove(playerEntity);
                     }
                  }
               }

               TeamManager.this.playerTeamList.removeIf(TeamManager.PlayerTeam::isEmpty);
            }
         }
      );
      ModuleListenerUtils.handleListenerHolder4(this, this.playerTeamList::clear);
   }

   public static class PlayerTeam {
      public final Map<PlayerEntity, Long> playerEntityMap;
      public final List<PlayerEntity> playerEntityList = new ArrayList<>();

      public List<PlayerEntity> getPlayerEntityList() {
         return this.playerEntityList;
      }

      public boolean isEmpty() {
         return this.playerEntityList.isEmpty();
      }

      public PlayerTeam(PlayerEntity playerEntity) {
         this.playerEntityMap = new HashMap<>();
         this.playerEntityList.add(playerEntity);
      }

      public Map<PlayerEntity, Long> getPlayerEntityMap() {
         return this.playerEntityMap;
      }
   }
}
