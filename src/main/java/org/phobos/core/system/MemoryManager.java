package org.phobos.core.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.api.TickTask;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.data.AttackersRecord;
import org.phobos.data.EntityRecord;
import org.phobos.event.EndTickEvent;
import org.phobos.event.Listener;
import org.phobos.event.SafeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.combat.AttackerVictimPair;
import org.phobos.core.fakeplayer.FakePlayer;
import org.phobos.core.Feature;
import org.phobos.core.PhobosClient;
import org.phobos.core.PhobosLog;
import org.phobos.core.tick.TickCounter;

public class MemoryManager extends Feature implements EventSubscriber {
   public final Map<UUID, Long> uUIDMap = new HashMap<>();
   public static final Logger LOGGER = LoggerFactory.getLogger(MemoryManager.class);

   public MemoryManager(PhobosClient phobosClient) {
      this.handleListener(
         new SafeListener<EndTickEvent>() {
            public void handleObject(
               EndTickEvent endTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               clientWorld.getEntities().iterator().forEachRemaining(entity -> {
                  if (entity != null) {
                     if (entity instanceof LivingEntity livingEntity && livingEntity.isDead()) {
                        MemoryManager.this.invoke(new EntityRecord(entity));
                     }
                  }
               });
            }
         }
      );
      this.handleListener(new Listener<TickCounter>() {
         public void handleObject2(TickCounter tickCounter) {
            if (Runtime.getRuntime().freeMemory() < 250L) {
               PhobosLog.handleText("Runtime", "Free JVM Memory is under 250, starting internal garbage collector");
               MinecraftHolder.mc.submit(MemoryCleaner::run);
            }
         }
      });
      this.handleListener(
         new SafeListener<EntityRecord>() {
            public void handleObject(
               EntityRecord entityRecord,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               MemoryManager.this.uUIDMap.entrySet().removeIf(entry -> entry.getValue() + 2000L < System.currentTimeMillis());
               if (entityRecord.entity() instanceof PlayerEntity playerEntity) {
                  UUID var10 = playerEntity.getUuid();
                  if (!MemoryManager.this.uUIDMap.containsKey(var10)) {
                     List<PlayerEntity> list = new ArrayList<>();

                     for (PlayerEntity playerEntity2 : clientWorld.getPlayers()) {
                        if (!playerEntity2.isDead()
                           && playerEntity2.isAlive()
                           && !playerEntity2.isSpectator()
                           && !playerEntity2.isCreative()
                           && !playerEntity2.equals(playerEntity)
                           && !(playerEntity2.squaredDistanceTo(playerEntity) > 100.0)
                           && !(playerEntity2 instanceof FakePlayer)) {
                           list.add(playerEntity2);
                        }
                     }

                     MemoryManager.this.uUIDMap.put(var10, System.currentTimeMillis());
                     MemoryManager.this.invoke(new AttackersRecord(list, playerEntity));
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Receive<EntityDamageS2CPacket>>() {
            public void handleObject(
               Receive<EntityDamageS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               DamageSource damageSource = ((EntityDamageS2CPacket)receive.getPacket()).createDamageSource(clientWorld);
               if (clientWorld.getEntityById(((EntityDamageS2CPacket)receive.getPacket()).entityId()) instanceof PlayerEntity playerEntity) {
                  if (damageSource.getAttacker() instanceof PlayerEntity playerEntity2) {
                     MemoryManager.this.invoke(new AttackerVictimPair(playerEntity, playerEntity2));
                  }
               }
            }
         }
      );
      this.handleListener(new Listener<EndTickEvent>() {
         public void handleObject2(EndTickEvent endTickEvent) {
            TickTask.run2();
         }
      });
   }
}
