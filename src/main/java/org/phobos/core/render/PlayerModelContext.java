package org.phobos.core.render;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.phobos.api.PlayerAnimator;
import org.phobos.core.player.SkinUtils;

public class PlayerModelContext {
   public final AbstractClientPlayerEntity abstractClientPlayerEntity;

   public AbstractClientPlayerEntity getAbstractClientPlayerEntity() {
      return this.abstractClientPlayerEntity;
   }

   public PlayerModelContext(AbstractClientPlayerEntity abstractClientPlayerEntity) {
      this.abstractClientPlayerEntity = abstractClientPlayerEntity;
   }

   public boolean check() {
      return !this.check5() && !this.abstractClientPlayerEntity.getEquippedStack(EquipmentSlot.CHEST).isEmpty();
   }

   public Identifier getIdentifier() {
      return SkinUtils.resolveIdentifier(this.abstractClientPlayerEntity);
   }

   public PlayerAnimator getPlayerAnimator() {
      return (PlayerAnimator)this.abstractClientPlayerEntity;
   }

   public boolean check2() {
      return this.abstractClientPlayerEntity.isPartVisible(PlayerModelPart.CAPE);
   }

   public boolean check3() {
      return this.abstractClientPlayerEntity.isInvisible();
   }

   public boolean check4() {
      return this.abstractClientPlayerEntity.isSubmergedInWater();
   }

   public boolean check5() {
      return this.abstractClientPlayerEntity.getEquippedStack(EquipmentSlot.CHEST).getItem().equals(Items.ELYTRA);
   }
}
