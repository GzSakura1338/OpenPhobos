package org.phobos.event;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class EatFoodEvent {
   public final ItemStack itemStack;
   public final FoodComponent foodComponent;
   public final LivingEntity livingEntity;

   public FoodComponent getFoodComponent() {
      return this.foodComponent;
   }

   public EatFoodEvent(LivingEntity livingEntity, FoodComponent foodComponent, ItemStack itemStack) {
      this.livingEntity = livingEntity;
      this.foodComponent = foodComponent;
      this.itemStack = itemStack;
   }

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   public LivingEntity getLivingEntity() {
      return this.livingEntity;
   }
}
