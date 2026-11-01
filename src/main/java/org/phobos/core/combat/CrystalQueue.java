package org.phobos.core.combat;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import org.earthhack.ducks.IEndCrystalEntity;
import org.phobos.api.MinecraftHolder;
import org.phobos.module.misc.SoundFixerModule;
import org.phobos.core.math.MathUtils;
import org.phobos.core.PhobosClient;
import org.phobos.core.setting.Setting;

public class CrystalQueue implements MinecraftHolder {
   public final Setting<Integer> integerSetting;
   public final Queue<EndCrystalEntity> endCrystalEntityQueue = new ConcurrentLinkedQueue<>();
   public final PhobosClient phobosClient;
   public final Setting<Boolean> booleanValueSetting;

   public void handleEndCrystalEntity(EndCrystalEntity endCrystalEntity) {
      endCrystalEntity.setShowBottom(false);
      ((IEndCrystalEntity)(Object)endCrystalEntity).setRendering(true);
      mc.submit(
         () -> {
            if (mc.world != null) {
               for (EndCrystalEntity endCrystalEntity2 : mc.world
                  .getEntitiesByClass(EndCrystalEntity.class, endCrystalEntity.getBoundingBox(), endCrystalEntityxx -> true)) {
                  endCrystalEntity.endCrystalAge = endCrystalEntity2.endCrystalAge;
               }
            }

            this.endCrystalEntityQueue.add(endCrystalEntity);
         }
      );
   }

   public CrystalQueue(PhobosClient phobosClient, Setting<Integer> setting, Setting<Boolean> setting2) {
      this.phobosClient = phobosClient;
      this.integerSetting = setting;
      this.booleanValueSetting = setting2;
   }

   public void handleEndCrystalEntity2(EndCrystalEntity endCrystalEntity) {
      for (EndCrystalEntity endCrystalEntity2 : this.endCrystalEntityQueue) {
         if (endCrystalEntity2.getBoundingBox().intersects(endCrystalEntity.getBoundingBox())) {
            endCrystalEntity.endCrystalAge = endCrystalEntity2.endCrystalAge;
         }
      }
   }

   public void clear() {
      for (EndCrystalEntity endCrystalEntity : this.endCrystalEntityQueue) {
         this.phobosClient.getEntityFadeManager().handleEntity2(endCrystalEntity);
      }

      this.endCrystalEntityQueue.clear();
   }

   public void run3() {
      for (EndCrystalEntity endCrystalEntity : this.endCrystalEntityQueue) {
         if (endCrystalEntity instanceof IEndCrystalEntity iEndCrystalEntity && iEndCrystalEntity.isRendering()) {
            this.phobosClient.getEntityFadeManager().handleEntity3(endCrystalEntity);
         }
      }
   }

   public void run() {
      if (this.integerSetting.getValue().equals(0)) {
         this.endCrystalEntityQueue.clear();
      } else {
         for (EndCrystalEntity endCrystalEntity : this.endCrystalEntityQueue) {
            int i = ++endCrystalEntity.age;
            this.handleEndCrystalEntity3(endCrystalEntity);
            endCrystalEntity.endCrystalAge = MathUtils.computeInt5(0, 10000);
            if (mc.player != null && !mc.player.isCreative()) {
               this.phobosClient.getInventoryManager().handleConsumer2(itemSwitcher -> {
                  Slot slot = itemSwitcher.resolveObject(slotx -> slotx.getStack().isOf(Items.END_CRYSTAL) ? slotx : null);
                  if (slot != null) {
                     slot.getStack().setCount(MathUtils.computeInt3(slot.getStack().getCount() - 1, 1, 64));
                  }
               });
            }

            endCrystalEntity.tick();
            if (i >= this.integerSetting.getValue()) {
               this.phobosClient.getEntityFadeManager().handleEntity2(endCrystalEntity);
               this.endCrystalEntityQueue.remove(endCrystalEntity);
               endCrystalEntity.discard();
            }
         }
      }
   }

   public void handleEndCrystalEntity3(EndCrystalEntity endCrystalEntity) {
      if (this.booleanValueSetting.getValue() && mc.world != null && mc.player != null) {
         mc.world
            .playSound(
               endCrystalEntity.getX(),
               endCrystalEntity.getY(),
               endCrystalEntity.getZ(),
               mc.player.age % 15 == 0 ? SoundEvents.ENTITY_PLAYER_ATTACK_STRONG : SoundEvents.ENTITY_PLAYER_ATTACK_WEAK,
               SoundCategory.PLAYERS,
               1.0F,
               (mc.world.random.nextFloat() - mc.world.random.nextFloat()) * 0.2F + 1.0F,
               SoundFixerModule.check3()
            );
         mc.world
            .playSound(
               endCrystalEntity.getX(),
               endCrystalEntity.getY(),
               endCrystalEntity.getZ(),
               (SoundEvent)SoundEvents.ENTITY_GENERIC_EXPLODE.value(),
               SoundCategory.BLOCKS,
               4.0F,
               (1.0F + (mc.world.random.nextFloat() - mc.world.random.nextFloat()) * 0.2F) * 0.7F,
               SoundFixerModule.check3()
            );
      }
   }
}
