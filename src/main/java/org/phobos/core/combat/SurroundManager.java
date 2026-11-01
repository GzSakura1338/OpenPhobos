package org.phobos.core.combat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.mutable.MutableInt;
import org.earthhack.ducks.IDamageProtectionEntity;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.SafeBlocks;
import org.phobos.api.MinecraftHolder;
import org.phobos.data.EntityRecord;
import org.phobos.event.Listener;
import org.phobos.event.OnEntityEquipmentUpdateEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.module.client.CalculationsModule;
import org.phobos.module.client.CalculationsModule;
import org.phobos.module.combat.SurroundModule;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.inventory.EnchantmentUtils;
import org.phobos.core.Feature;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.PhobosClient;
import org.phobos.core.event.events.UnusedEvent;

public class SurroundManager extends Feature implements SafeBlocks {
   public final Map<PlayerEntity, SurroundManager.SurroundCache> playerEntityMap = new ConcurrentHashMap<>();
   public final Map<UUID, SurroundManager.TotemCalculation> uUIDMap = new HashMap<>();
   public static int intValue = -1;

   @Nullable
   public SurroundManager.TotemCalculation resolveTotemCalculation(Entity entity) {
      return this.uUIDMap.get(entity.getUuid());
   }

   public boolean testPlayerEntity(PlayerEntity playerEntity, boolean flag) {
      if (playerEntity != null && mc.player != null && mc.world != null) {
         Set<BlockPos> set = !flag
            ? this.resolveSet(playerEntity)
            : SurroundModule.getSurroundModule().resolveSet2(playerEntity, mc.player, mc.world, Blocks.OBSIDIAN, false);
         if (set != null && !set.isEmpty()) {
            set.removeIf(blockPos -> blockPos.getY() < playerEntity.getY() - 0.5);
            return set.stream()
               .noneMatch(
                  blockPos -> mc.player.clientWorld.getBlockState(blockPos).getOutlineShape(mc.player.clientWorld, blockPos).isEmpty()
                     || !BlockUtils.testBlockPos4(blockPos, mc.player)
               );
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public Set<BlockPos> resolveSet(PlayerEntity playerEntity) {
      return this.resolveSet2(playerEntity, false);
   }

   public int computeItemStack(ItemStack itemStack) {
      int i = EnchantmentUtils.computeItemStack3(itemStack, Enchantments.BLAST_PROTECTION);
      int i2 = EnchantmentUtils.computeItemStack3(itemStack, Enchantments.PROTECTION);
      return i * 2 + i2;
   }

   public void handleLivingEntity(LivingEntity livingEntity, ClientWorld clientWorld) {
      float f = CalculationsModule.getDamage().equals(CalculationsModule.Damage.Enchants)
         ? this.computeIterable(livingEntity.getArmorItems())
         : EnchantmentUtils.computeLivingEntity(livingEntity, clientWorld.getDamageSources().explosion(null, null));
      ((IDamageProtectionEntity)(Object)livingEntity).setIntValue((int)f);
   }

   public SurroundManager(PhobosClient phobosClient) {
      this.handleListener(
         new SafeListener<UnusedEvent>(Integer.MAX_VALUE) {
            public void handleObject(
               UnusedEvent unusedEvent, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               SurroundManager.this.playerEntityMap.clear();

               for (PlayerEntity playerEntity : clientWorld.getPlayers()) {
                  SurroundManager.this.playerEntityMap
                     .put(
                        playerEntity,
                        new SurroundManager.SurroundCache(
                           SurroundModule.getSurroundModule().resolveSet2(playerEntity, clientPlayerEntity, clientWorld, Blocks.OBSIDIAN, false)
                        )
                     );
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<EntityRecord>(20) {
            public void handleObject(
               EntityRecord entityRecord,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               Entity entity = entityRecord.entity();
               if (entity instanceof PlayerEntity) {
                  int i = phobosClient.getEntityIdManager().computeEntity(entity);
                  if (i == -1) {
                     SurroundManager.this.uUIDMap.remove(entity.getUuid());
                  } else {
                     SurroundManager.TotemCalculation totemCalculation = SurroundManager.this.uUIDMap.get(entity.getUuid());
                     if (totemCalculation != null) {
                        if (totemCalculation.current < i) {
                           return;
                        }

                        SurroundManager.this.uUIDMap
                           .put(entity.getUuid(), new SurroundManager.TotemCalculation(i, totemCalculation.previous, System.currentTimeMillis()));
                     } else {
                        SurroundManager.this.uUIDMap
                           .put(entity.getUuid(), new SurroundManager.TotemCalculation(i, SurroundManager.intValue, System.currentTimeMillis()));
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new Listener<OnEntityEquipmentUpdateEvent>() {
            public void handleObject2(OnEntityEquipmentUpdateEvent onEntityEquipmentUpdateEvent) {
               if (CalculationsModule.check5()) {
                  if (onEntityEquipmentUpdateEvent.packet().getEquipmentList().stream().anyMatch(pair -> ((EquipmentSlot)pair.getFirst()).isArmorSlot())
                     && MinecraftHolder.mc.world != null
                     && onEntityEquipmentUpdateEvent.entity() instanceof LivingEntity livingEntity) {
                     SurroundManager.this.handleLivingEntity(livingEntity, MinecraftHolder.mc.world);
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
               if (mc.getNetworkHandler() != null) {
                  SurroundManager.this.uUIDMap
                     .entrySet()
                     .removeIf(
                        entry -> mc.getNetworkHandler().getPlayerListEntry(entry.getKey()) == null
                           && entry.getValue().time + 60000L < System.currentTimeMillis()
                     );
                  SurroundManager.this.handleLivingEntity(clientPlayerEntity, clientWorld);
                  if (!CalculationsModule.check5()) {
                     for (Entity entity : clientWorld.getEntities()) {
                        if (!(entity instanceof ClientPlayerEntity) && entity instanceof LivingEntity livingEntity) {
                           SurroundManager.this.handleLivingEntity(livingEntity, clientWorld);
                        }
                     }
                  }
               }
            }
         }
      );
      ModuleListenerUtils.handleListenerHolder4(this, this.playerEntityMap::clear);
      ModuleListenerUtils.handleListenerHolder4(this, this.uUIDMap::clear);
   }

   public boolean testPlayerEntity2(PlayerEntity playerEntity) {
      return this.testPlayerEntity(playerEntity, false);
   }

   public Set<BlockPos> resolveSet2(PlayerEntity playerEntity, boolean flag) {
      if (playerEntity == null || mc.player == null || mc.world == null) {
         return new HashSet<>(0);
      }

      if (flag) {
         return SurroundModule.getSurroundModule().resolveSet2(playerEntity, mc.player, mc.world, Blocks.OBSIDIAN, false);
      }

      SurroundManager.SurroundCache surroundCache = this.playerEntityMap.get(playerEntity);
      return surroundCache == null ? new HashSet<>(0) : surroundCache.positions;
   }

   public int computeIterable(Iterable<ItemStack> iterable) {
      MutableInt mutableInt = new MutableInt();
      iterable.forEach(itemStack -> mutableInt.add(this.computeItemStack(itemStack)));
      return mutableInt.getValue();
   }

   public record SurroundCache(Set<BlockPos> positions) {
   }

   public record TotemCalculation(int current, int previous, long time) {
   }
}
