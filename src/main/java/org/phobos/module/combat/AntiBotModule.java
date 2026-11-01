package org.phobos.module.combat;

import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRemoveS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket.Action;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket.Entry;
import org.phobos.api.DelayedTask;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import oshi.util.tuples.Pair;
import org.phobos.module.Module;

public class AntiBotModule extends Module {
   public final Setting<Boolean> health;
   public final Setting<AntiBotModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", AntiBotModule.Mode.Custom));
   public final Set<UUID> uUIDSet;
   public final Setting<Boolean> duplicate = this.resolveSetting2(new BooleanSetting("Duplicate"));
   public final Setting<Boolean> angle;
   public final Set<UUID> uUIDSet2;
   public final Setting<Boolean> gamemode = this.resolveSetting2(new BooleanSetting("Gamemode"));
   public final Setting<Boolean> iD;
   public static AntiBotModule antiBotModule;
   public final List<UUID> uUIDList;
   public final Set<UUID> uUIDSet3;
   public final Setting<Boolean> name2;
   public final Map<UUID, Long> uUIDMap;
   public final Map<UUID, Pair<Integer, Long>> uUIDMap2;
   public final Set<UUID> uUIDSet4;

   public void handleList(List<Entry> list) {
      for (Entry entry : list) {
         GameProfile gameProfile = entry.profile();
         if (gameProfile != null && entry.latency() >= 2 && !this.testGameProfile2(gameProfile)) {
            this.uUIDMap2.put(gameProfile.getId(), new Pair(entry.latency(), System.currentTimeMillis()));
         }
      }
   }

   public boolean testPlayerEntity(PlayerEntity playerEntity) {
      boolean flag = true;

      for (int i = 0; i <= 3; i++) {
         ItemStack itemStack = playerEntity.getInventory().getArmorStack(i);
         if (!itemStack.hasEnchantments() || !(itemStack.getItem() instanceof ArmorItem)) {
            flag = false;
            break;
         }
      }

      return flag;
   }

   public boolean testPlayerEntity2(PlayerEntity playerEntity) {
      String text = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_";
      String text2 = playerEntity.getNameForScoreboard();
      if (text2.length() >= 3 && text2.length() <= 16) {
         for (int i = 0; i < text2.length(); i++) {
            if (text.contains(String.valueOf(text2.charAt(i)))) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   public void handleList2(List<Entry> list) {
      for (Entry entry : list) {
         if (this.uUIDMap2.containsKey(entry.profileId())) {
            int i = (Integer)this.uUIDMap2.get(entry.profileId()).getA();
            int i2 = i - entry.latency();
            long l = System.currentTimeMillis() - (Long)this.uUIDMap2.get(entry.profileId()).getB();
            if (i2 == i && l <= 15L) {
               this.uUIDList.add(entry.profileId());
            }

            this.uUIDMap2.remove(entry.profileId());
         }
      }
   }

   public boolean testGameProfile(GameProfile gameProfile) {
      return mc.getNetworkHandler() != null
         && mc.getNetworkHandler()
               .getPlayerList()
               .stream()
               .filter(
                  playerListEntry -> playerListEntry.getProfile().getName().equalsIgnoreCase(gameProfile.getName())
                     && playerListEntry.getProfile().getId() != gameProfile.getId()
               )
               .count()
            == 1L;
   }

   public void handlePlayerListS2CPacket(PlayerListS2CPacket playerListS2CPacket) {
      switch ((AntiBotModule.Mode)this.mode.getValue()) {
         case Intave:
            for (Entry entry : playerListS2CPacket.getPlayerAdditionEntries()) {
               GameProfile gameProfile = entry.profile();
               if (gameProfile != null
                  && entry.latency() >= 2
                  && (gameProfile.getProperties() == null || !gameProfile.getProperties().isEmpty())
                  && !this.testGameProfile2(gameProfile)) {
                  if (this.testGameProfile(gameProfile)) {
                     this.uUIDSet4.add(entry.profileId());
                  } else {
                     this.uUIDSet3.add(entry.profileId());
                  }
               }
            }
            break;
         case Matrix:
            if (playerListS2CPacket.getActions().isEmpty()) {
               return;
            }

            Action action = (Action)playerListS2CPacket.getActions().stream().findFirst().orElse(null);
            if (action == null) {
               return;
            }

            if (action.equals(Action.ADD_PLAYER)) {
               this.handleList2(playerListS2CPacket.getEntries());
            }

            if (action.equals(Action.UPDATE_LATENCY)) {
               this.handleList(playerListS2CPacket.getEntries());
            }
            break;
         case Radon:
            Action action2 = (Action)playerListS2CPacket.getActions().stream().findFirst().orElse(null);
            if (action2 == null) {
               return;
            }

            if (!action2.equals(Action.ADD_PLAYER)) {
               return;
            }

            for (Entry entry2 : playerListS2CPacket.getEntries()) {
               if (entry2.gameMode() == null) {
                  this.uUIDSet.add(entry2.profileId());
               }
            }
      }
   }

   public void run2() {
      this.uUIDList.clear();
      this.uUIDMap2.clear();
      this.uUIDSet4.clear();
      this.uUIDSet3.clear();
   }

   public void handlePlayerRemoveS2CPacket(PlayerRemoveS2CPacket playerRemoveS2CPacket) {
      switch ((AntiBotModule.Mode)this.mode.getValue()) {
         case Intave:
            for (UUID uUID2 : playerRemoveS2CPacket.profileIds()) {
               this.uUIDSet3.remove(uUID2);
               this.uUIDSet4.remove(uUID2);
            }
            break;
         case Matrix:
            for (UUID uUID : playerRemoveS2CPacket.profileIds()) {
               this.uUIDMap2.remove(uUID);
               this.uUIDList.remove(uUID);
            }
            break;
         case Radon:
            for (UUID uUID3 : playerRemoveS2CPacket.profileIds()) {
               this.uUIDSet.remove(uUID3);
            }
      }
   }

   public boolean testEntity(Entity entity) {
      if (entity instanceof PlayerEntity playerEntity && mc.getNetworkHandler() != null) {
         return switch ((AntiBotModule.Mode)this.mode.getValue()) {
            case Sentinel -> {
               boolean flag = this.gamemode.getValue()
                  && mc.getNetworkHandler().getPlayerListEntry(playerEntity.getUuid()) != null
                  && mc.getNetworkHandler().getPlayerListEntry(playerEntity.getUuid()).getGameMode() == null;
               boolean flag2 = this.iD.getValue() && (playerEntity.getId() < 0 || playerEntity.getId() >= 1.0E9);
               boolean flag3 = this.duplicate.getValue() && this.testGameProfile(playerEntity.getGameProfile());
               boolean flag4 = this.name2.getValue() && this.testPlayerEntity2(playerEntity);
               boolean flag5 = this.angle.getValue() && Math.abs(playerEntity.getPitch()) > 90.0F;
               boolean flag6 = this.health.getValue() && playerEntity.getHealth() > 20.0F;
               yield flag || flag2 || flag3 || flag4 || flag5 || flag6;
            }
            case Intave -> this.uUIDSet4.contains(playerEntity.getUuid());
            case Matrix -> this.uUIDList.contains(playerEntity.getUuid());
            case Radon -> this.uUIDSet.contains(playerEntity.getUuid());
            case Custom -> !this.uUIDMap.containsKey(playerEntity.getUuid());
            case Horizon -> this.uUIDSet2.contains(playerEntity.getUuid());
         };
      } else {
         return false;
      }
   }

   public boolean testGameProfile2(GameProfile gameProfile) {
      return mc.getNetworkHandler() != null
         && mc.getNetworkHandler()
               .getPlayerList()
               .stream()
               .filter(
                  playerListEntry -> playerListEntry.getProfile().getName().equals(gameProfile.getName())
                     && playerListEntry.getProfile().getId().equals(gameProfile.getId())
               )
               .count()
            == 1L;
   }

   @Override
   public void run4() {
      this.run2();
   }

   public AntiBotModule(PhobosClient phobosClient) {
      super(phobosClient, "AntiBot", Categories.combat, "Filters out bots");
      this.angle = this.resolveSetting2(new BooleanSetting("Angle"));
      this.iD = this.resolveSetting2(new BooleanSetting("ID"));
      this.name2 = this.resolveSetting2(new BooleanSetting("Name"));
      this.health = this.resolveSetting2(new BooleanSetting("Health"));
      this.uUIDMap2 = new ConcurrentHashMap<>();
      this.uUIDList = new ArrayList<>();
      this.uUIDSet3 = ConcurrentHashMap.newKeySet();
      this.uUIDSet4 = ConcurrentHashMap.newKeySet();
      this.uUIDSet = ConcurrentHashMap.newKeySet();
      this.uUIDMap = new ConcurrentHashMap<>();
      this.uUIDSet2 = ConcurrentHashMap.newKeySet();
      this.resolveSettingGroup2("Filter", this.duplicate, this.gamemode, this.angle, this.iD, this.name2, this.health)
         .resolveSettingGroup5(this.resolveVisibility2(this.mode, AntiBotModule.Mode.Custom));
      this.handleListener(
         new SafeListener<Receive<?>>() {
            public void handleObject(
               Receive<?> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               Packet<?> packet = receive.getPacket();
               if (packet instanceof PlayerListS2CPacket playerListS2CPacket) {
                  AntiBotModule.this.handlePlayerListS2CPacket(playerListS2CPacket);
               }

               if (packet instanceof PlayerRemoveS2CPacket playerRemoveS2CPacket) {
                  AntiBotModule.this.handlePlayerRemoveS2CPacket(playerRemoveS2CPacket);
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
               AntiBotModule.this.uUIDMap.entrySet().removeIf(entry -> entry.getValue() + 500L < System.currentTimeMillis());

               for (PlayerEntity playerEntity : clientWorld.getPlayers()) {
                  switch ((AntiBotModule.Mode)AntiBotModule.this.mode.getValue()) {
                     case Intave:
                        if (!AntiBotModule.this.testPlayerEntity(playerEntity)) {
                           List<ItemStack> list = playerEntity.getInventory().armor;
                           DelayedTask.handleLong(
                              100L,
                              () -> {
                                 if ((AntiBotModule.this.testPlayerEntity(playerEntity) || AntiBotModule.this.testPlayerEntity3(playerEntity, list))
                                    && playerEntity.getGameProfile().getProperties().isEmpty()) {
                                    AntiBotModule.this.uUIDSet4.add(playerEntity.getUuid());
                                 }

                                 AntiBotModule.this.uUIDSet3.add(playerEntity.getUuid());
                              }
                           );
                        }
                        break;
                     case Custom:
                        if (playerEntity.isOnGround() || playerEntity.hurtTime != 0) {
                           AntiBotModule.this.uUIDMap.put(playerEntity.getUuid(), System.currentTimeMillis());
                        }
                  }
               }
            }
         }
      );
      antiBotModule = this;
   }

   public boolean testPlayerEntity3(PlayerEntity playerEntity, List<ItemStack> list) {
      return playerEntity.getInventory().armor != list;
   }

   public static boolean testEntity2(Entity entity) {
      return antiBotModule.check7() && antiBotModule.testEntity(entity);
   }

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   private enum Mode {
      Custom,
      Matrix,
      Intave,
      Horizon,
      Radon,
      Sentinel;
   }
}
