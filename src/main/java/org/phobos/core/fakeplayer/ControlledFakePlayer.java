package org.phobos.core.fakeplayer;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.explosion.Explosion;
import org.earthhack.ducks.IDamageProtectionEntity;
import org.earthhack.ducks.ITotemPoppingEntity;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.TimeGate;

public class ControlledFakePlayer extends MovingFakePlayer implements IDamageProtectionEntity, ITotemPoppingEntity {
   public final AbstractClientPlayerEntity abstractClientPlayerEntity;

   public AbstractClientPlayerEntity getAbstractClientPlayerEntity() {
      return this.abstractClientPlayerEntity;
   }

   @Override
   public void setIntValue(int i) {
      ((IDamageProtectionEntity)(Object)this.abstractClientPlayerEntity).setIntValue(i);
   }

   public int getArmor() {
      return this.abstractClientPlayerEntity.getArmor();
   }

   public Iterable<ItemStack> getArmorItems() {
      return this.abstractClientPlayerEntity.getArmorItems();
   }

   public double getAttributeValue(RegistryEntry<EntityAttribute> attribute) {
      if (this.abstractClientPlayerEntity == null) {
         return attribute == EntityAttributes.GENERIC_MAX_HEALTH ? 20.0 : 0.0;
      } else {
         return this.abstractClientPlayerEntity.getAttributeValue(attribute);
      }
   }

   @Override
   public TimeGate lastTotemPop() {
      return ((ITotemPoppingEntity)(Object)this.abstractClientPlayerEntity).lastTotemPop();
   }

   public ControlledFakePlayer(AbstractClientPlayerEntity abstractClientPlayerEntity) {
      super(abstractClientPlayerEntity.clientWorld, abstractClientPlayerEntity.getGameProfile());
      this.abstractClientPlayerEntity = abstractClientPlayerEntity;

      try {
         this.copyFrom(abstractClientPlayerEntity);
      } catch (Throwable var3) {
      }
   }

   @Nullable
   public StatusEffectInstance getStatusEffect(RegistryEntry<StatusEffect> effect) {
      return this.abstractClientPlayerEntity.getStatusEffect(effect);
   }

   public boolean testExplosion(Explosion explosion) {
      return this.abstractClientPlayerEntity.isImmuneToExplosion(explosion);
   }

   @Override
   public int getIntValue() {
      return ((IDamageProtectionEntity)(Object)this.abstractClientPlayerEntity).getIntValue();
   }
}
