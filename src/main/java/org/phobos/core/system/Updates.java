package org.phobos.core.system;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class Updates {
   public static class Change {
      public final ItemStack itemStack;
      public final ItemStack itemStack2;
      public final ItemStack itemStack3;
      public final LivingEntity livingEntity;
      public final ItemStack itemStack4;

      public ItemStack getItemStack() {
         return this.itemStack;
      }

      public ItemStack getItemStack2() {
         return this.itemStack2;
      }

      public ItemStack getItemStack4() {
         return this.itemStack4;
      }

      public Change(LivingEntity livingEntity, ItemStack itemStack, ItemStack itemStack2, ItemStack itemStack3, ItemStack itemStack4) {
         this.livingEntity = livingEntity;
         this.itemStack3 = itemStack;
         this.itemStack2 = itemStack2;
         this.itemStack4 = itemStack3;
         this.itemStack = itemStack4;
      }

      public ItemStack getItemStack3() {
         return this.itemStack3;
      }

      public LivingEntity getLivingEntity() {
         return this.livingEntity;
      }
   }

   public static class Update {
      public final LivingEntity livingEntity;
      public final int intValue;
      public final int intValue2;
      public final int intValue3;
      public final int intValue4;

      public int getIntValue3() {
         return this.intValue3;
      }

      public int getIntValue() {
         return this.intValue;
      }

      public Update(LivingEntity livingEntity, int i, int i2, int i3, int i4) {
         this.livingEntity = livingEntity;
         this.intValue3 = i;
         this.intValue2 = i2;
         this.intValue = i3;
         this.intValue4 = i4;
      }

      public int getIntValue4() {
         return this.intValue4;
      }

      public LivingEntity getLivingEntity() {
         return this.livingEntity;
      }

      public int getIntValue2() {
         return this.intValue2;
      }
   }
}
