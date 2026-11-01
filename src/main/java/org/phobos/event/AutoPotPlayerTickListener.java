package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.tick.TickUtils;
import org.phobos.module.combat.AutoPotModule;
import org.phobos.module.combat.AutoPotModule;

public final class AutoPotPlayerTickListener extends ModuleListener.Safe<AutoPotModule, PlayerTickEvent> {
   public AutoPotPlayerTickListener(AutoPotModule autoPotModule) {
      super(autoPotModule, PlayerTickEvent.class);
   }

   public void handleObject(
      PlayerTickEvent playerTickEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      switch ((AutoPotModule.Mode)this.module.modeSetting.getValue()) {
         case Legacy:
            if (EntityUtils.computeLivingEntity(clientPlayerEntity) > this.module.floatValueSetting.getValue()
               || !this.module.normal.testNumber2(this.module.longValueSetting.getValue())) {
               return;
            }

            PlayerEntity playerEntity = this.phobosClient
               .getFeatureRegistry()
               .getPredictionManager()
               .resolvePlayerEntity2(this.module.integerSetting.getValue(), clientPlayerEntity);
            if (playerEntity == null) {
               return;
            }

            float[] floats = RotationUtils.values2(clientPlayerEntity, playerEntity.getPos());
            this.phobosClient.getRotationManager().testFloat(floats[0], floats[1]);
            this.phobosClient
               .getRotationManager()
               .setRunnable(
                  () -> this.phobosClient
                     .getInventoryManager()
                     .handleConsumer2(
                        itemSwitcher -> {
                           Slot slot = itemSwitcher.resolveObject(
                              slotx -> {
                                 if (slotx.getStack().getItem() instanceof SplashPotionItem splashPotionItem) {
                                    PotionContentsComponent potionContentsComponent = (PotionContentsComponent)slotx.getStack()
                                       .getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);
                                    if (!potionContentsComponent.hasEffects()) {
                                       return null;
                                    }

                                    for (StatusEffectInstance statusEffectInstance : potionContentsComponent.getEffects()) {
                                       if (statusEffectInstance.getEffectType().equals(StatusEffects.INSTANT_HEALTH)) {
                                          return slotx;
                                       }
                                    }
                                 }

                                 return null;
                              }
                           );
                           if (slot != null) {
                              itemSwitcher.resolveSwitchResult(Hand.MAIN_HAND, slot, 1);
                              TickUtils.handleClientWorld(
                                 clientWorld,
                                 integer -> clientPlayerEntity.networkHandler
                                    .sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, integer, floats[0], floats[1]))
                              );
                           }
                        }
                     )
               );
            break;
         case Modern:
            if (!this.module.booleanValueSetting2.getValue()) {
               return;
            }

            PlayerEntity playerEntity2 = EntityUtils.resolvePlayerEntity2(clientWorld, clientPlayerEntity, this.phobosClient);
            if (playerEntity2 == null
               || this.module.testClientPlayerEntity5(clientPlayerEntity, playerEntity2)
               || !this.module.testClientPlayerEntity3(clientPlayerEntity, playerEntity2)) {
               this.module.registryEntrySet2.clear();
               return;
            }

            for (RegistryEntry<StatusEffect> registryEntry : AutoPotModule.registryEntrySet) {
               if (clientPlayerEntity.hasStatusEffect(registryEntry)) {
                  this.module.registryEntrySet2.remove(registryEntry);
               } else {
                  this.module.registryEntrySet2.add(registryEntry);
               }
            }

            if (!this.module.normal.testNumber(this.module.longValueSetting2.getValue())) {
               return;
            }

            for (RegistryEntry<StatusEffect> registryEntry2 : this.module.registryEntrySet2) {
               if (this.module.testClientPlayerEntity4(clientPlayerEntity, clientWorld, registryEntry2)) {
                  this.module.normal.run();
                  return;
               }
            }
      }
   }
}
