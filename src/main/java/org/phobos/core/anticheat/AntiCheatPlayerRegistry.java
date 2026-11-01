package org.phobos.core.anticheat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import org.phobos.api.MinecraftHolder;
import org.phobos.module.client.AntiCheatModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AntiCheatPlayerRegistry implements MinecraftHolder {
   public Map<UUID, AntiCheatPlayer> uUIDMap;
   public Map<UUID, Boolean> uUIDMap2 = new HashMap<>();
   public final AntiCheatModule antiCheatModule;
   public static AntiCheatPlayerRegistry antiCheatPlayerRegistry;
   public static final Logger LOGGER = LoggerFactory.getLogger(AntiCheatPlayerRegistry.class);

   public void run6() {
      if (mc.world != null && mc.player != null) {
         this.uUIDMap2.forEach((uUIDx, booleanValue) -> this.uUIDMap2.replace(uUIDx, false));

         try {
            for (AbstractClientPlayerEntity abstractClientPlayerEntity : this.getList()) {
               UUID uUID = abstractClientPlayerEntity.getUuid();
               if (!this.antiCheatModule.getFriends().getValue() && this.antiCheatModule.getPhobosClient().getFriendList().testUUID(uUID)) {
                  this.uUIDMap2.remove(uUID);
               } else {
                  if (!this.uUIDMap2.containsKey(uUID)) {
                     AntiCheatPlayer antiCheatPlayer;
                     if (mc.player.equals(abstractClientPlayerEntity)) {
                        antiCheatPlayer = new AntiCheatScheduler(mc.player, this.antiCheatModule);
                     } else {
                        antiCheatPlayer = AntiCheatPlayer.createAntiCheatPlayer(abstractClientPlayerEntity, this.antiCheatModule);
                     }

                     this.uUIDMap2.put(uUID, true);
                     this.uUIDMap.put(uUID, antiCheatPlayer);
                  }

                  this.uUIDMap2.replace(uUID, true);

                  try {
                     AntiCheatPlayer antiCheatPlayer3 = this.uUIDMap.get(uUID);
                     if (this.antiCheatModule.getPhobosClient().getTimerManager().getFloatValue22() != 1.0F) {
                        antiCheatPlayer3.antiCheatCheckRegistry.intValue2 = 30;
                     }

                     antiCheatPlayer3.handleAbstractClientPlayerEntity(abstractClientPlayerEntity);
                  } catch (Exception exception) {
                     this.uUIDMap2.remove(uUID);
                  }
               }
            }
         } catch (ConcurrentModificationException concurrentModificationException) {
            LOGGER.error(concurrentModificationException.getLocalizedMessage());
         }
      }
   }

   public Map<UUID, AntiCheatPlayer> getUUIDMap() {
      return this.uUIDMap;
   }

   public List<AbstractClientPlayerEntity> getList() {
      if (mc.world == null) {
         return Collections.emptyList();
      }

      List<AbstractClientPlayerEntity> list = new ArrayList<>();
      mc.world.getEntities().forEach(entity -> {
         if (entity instanceof AbstractClientPlayerEntity abstractClientPlayerEntity) {
            list.add(abstractClientPlayerEntity);
         }
      });
      return list;
   }

   public static AntiCheatPlayerRegistry getAntiCheatPlayerRegistry() {
      return antiCheatPlayerRegistry;
   }

   public List<AntiCheatPlayer> getList2() {
      return this.uUIDMap.values().stream().filter(antiCheatPlayer -> this.uUIDMap2.containsKey(antiCheatPlayer.abstractClientPlayerEntity.getUuid())).toList();
   }

   public AntiCheatPlayerRegistry(AntiCheatModule antiCheatModule) {
      this.uUIDMap = new HashMap<>();
      this.antiCheatModule = antiCheatModule;
      antiCheatPlayerRegistry = this;
   }
}
