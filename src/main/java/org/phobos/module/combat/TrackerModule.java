package org.phobos.module.combat;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.ItemPickupAnimationS2CPacket;
import net.minecraft.text.Text;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.entity.TrackerStats;
import org.phobos.core.entity.EntityDataTracker;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.event.events.PacketEvent.PostReceive;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class TrackerModule extends Module {
   public final Map<PlayerEntity, TrackerStats> playerEntityMap;
   public final Setting<Boolean> friends;
   public final Setting<Boolean> drops = this.resolveSetting2(new BooleanSetting("Drops").resolveSetting7("Notifies you of any items picked up"));

   public TrackerModule(PhobosClient phobosClient) {
      super(phobosClient, "Tracker", Categories.combat, "Tracks how many items target uses");
      this.friends = this.resolveSetting2(new BooleanSetting("Friends"));
      this.playerEntityMap = new HashMap<>();
      this.handleListener(
         new SafeListener<Receive<ItemPickupAnimationS2CPacket>>() {
            public void handleObject(
               Receive<ItemPickupAnimationS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (TrackerModule.this.drops.getValue()) {
                  if (clientWorld.getEntityById(((ItemPickupAnimationS2CPacket)receive.getPacket()).getEntityId()) instanceof ItemEntity itemEntity) {
                     Entity entity2 = clientWorld.getEntityById(((ItemPickupAnimationS2CPacket)receive.getPacket()).getCollectorEntityId());
                     if (entity2 instanceof PlayerEntity playerEntity) {
                        if (TrackerModule.this.friends.getValue() || !phobosClient.getFriendList().testUUID(entity2.getUuid())) {
                           TrackerStats trackerStats = TrackerModule.this.playerEntityMap.get(playerEntity);
                           if (trackerStats != null) {
                              if (trackerStats.testItemStack2(itemEntity.getStack())) {
                                 TrackerModule.this.handleText7(
                                    Text.literal(
                                       "%s has picked up %s of %s"
                                          .formatted(
                                             playerEntity.getNameForScoreboard(),
                                             ((ItemPickupAnimationS2CPacket)receive.getPacket()).getStackAmount(),
                                             itemEntity.getStack().getItem().getName().getString()
                                          )
                                    )
                                 );
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<PostReceive<EntitySpawnS2CPacket>>() {
            public void handleObject(
               PostReceive<EntitySpawnS2CPacket> postReceive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (clientWorld.getEntityById(((EntitySpawnS2CPacket)postReceive.getPacket()).getEntityId()) instanceof ExperienceBottleEntity experienceBottleEntity
                  )
                {
                  if (experienceBottleEntity.getOwner() instanceof PlayerEntity playerEntity) {
                     if (TrackerModule.this.friends.getValue() || !phobosClient.getFriendList().testUUID(playerEntity.getUuid())) {
                        TrackerStats trackerStats = TrackerModule.this.playerEntityMap.get(playerEntity);
                        if (trackerStats != null) {
                           trackerStats.setIntValue(trackerStats.getIntValue() + 1);
                        }
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Receive<EntityEquipmentUpdateS2CPacket>>() {
            public void handleObject(
               Receive<EntityEquipmentUpdateS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (clientWorld.getEntityById(((EntityEquipmentUpdateS2CPacket)receive.getPacket()).getEntityId()) instanceof PlayerEntity playerEntity) {
                  if (TrackerModule.this.friends.getValue() || !phobosClient.getFriendList().testUUID(playerEntity.getUuid())) {
                     EntityUtils.handleEntityEquipmentUpdateS2CPacket(
                        (EntityEquipmentUpdateS2CPacket)receive.getPacket(),
                        clientWorld,
                        (itemStack, itemStack2, itemStack3, itemStack4) -> {
                           TrackerStats trackerStats = TrackerModule.this.playerEntityMap.get(playerEntity);
                           if (trackerStats != null) {
                              MutableBoolean mutableBoolean = new MutableBoolean(false);
                              if (itemStack.isOf(itemStack2.getItem()) && itemStack2.getCount() < itemStack.getCount()) {
                                 mutableBoolean.setValue(
                                    mutableBoolean.getValue() || trackerStats.testItemStack(itemStack2, itemStack.getCount() - itemStack2.getCount())
                                 );
                              }

                              if (itemStack3.isOf(itemStack4.getItem()) && itemStack4.getCount() < itemStack3.getCount()) {
                                 mutableBoolean.setValue(
                                    mutableBoolean.getValue() || trackerStats.testItemStack(itemStack4, itemStack3.getCount() - itemStack4.getCount())
                                 );
                              }

                              if (mutableBoolean.getValue()) {
                                 Map<String, Integer> map = trackerStats.getMap();
                                 if (!map.isEmpty()) {
                                    for (String text : map.keySet()) {
                                       Integer integer = map.get(text);
                                       TrackerModule.this.handleText7(
                                          Text.literal("%s has used %s stacks of %s".formatted(playerEntity.getNameForScoreboard(), integer, text))
                                       );
                                    }
                                 }
                              }
                           }
                        }
                     );
                  }
               }
            }
         }
      );
      EntityDataTracker.resolveEntityDataTracker(PlayerEntity.class, this.playerEntityMap, playerEntity -> new TrackerStats(), this);
   }
}
