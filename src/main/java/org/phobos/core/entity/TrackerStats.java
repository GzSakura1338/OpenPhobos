package org.phobos.core.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class TrackerStats {
   public int intValue;
   public int intValue2;
   public int intValue3;
   public int intValue4;
   public int intValue5;
   public int intValue6 = 0;
   public int intValue7;
   public int intValue8;

   public void setIntValue(int i) {
      this.intValue = i;
   }

   public int getIntValue6() {
      return this.intValue6;
   }

   public void setIntValue3(int i) {
      this.intValue3 = i;
   }

   public Map<String, Integer> getMap() {
      Map<String, Integer> map = new HashMap<>();
      BiConsumer<String, Integer> biConsumer = (text, integer) -> {
         if (map.containsKey(text)) {
            map.put(text, map.get(text) + integer);
         } else {
            map.put(text, integer);
         }
      };

      while (this.intValue6 >= 64) {
         biConsumer.accept("crystals", 1);
         this.intValue8++;
         this.intValue6 -= 64;
      }

      while (this.intValue2 >= 64) {
         biConsumer.accept("ender-chests", 1);
         this.intValue7++;
         this.intValue2 -= 64;
      }

      while (this.intValue3 >= 64) {
         biConsumer.accept("pearls", 1);
         this.intValue5++;
         this.intValue3 -= 64;
      }

      while (this.intValue >= 64) {
         biConsumer.accept("experience", 1);
         this.intValue4++;
         this.intValue -= 64;
      }

      return map;
   }

   public void setIntValue5(int i) {
      this.intValue5 = i;
   }

   public void setIntValue6(int i) {
      this.intValue6 = i;
   }

   public TrackerStats() {
      this.intValue2 = 0;
      this.intValue3 = 0;
      this.intValue = 0;
   }

   public int getIntValue2() {
      return this.intValue2;
   }

   public void setIntValue8(int i) {
      this.intValue8 = i;
   }

   public int getIntValue8() {
      return this.intValue8;
   }

   public boolean testItemStack(ItemStack itemStack, int i) {
      if (itemStack.isOf(Items.END_CRYSTAL)) {
         this.intValue6 += i;
         return true;
      } else if (itemStack.isOf(Blocks.ENDER_CHEST.asItem())) {
         this.intValue2 += i;
         return true;
      } else if (itemStack.isOf(Items.ENDER_PEARL)) {
         this.intValue3 += i;
         return true;
      } else if (itemStack.isOf(Items.EXPERIENCE_BOTTLE)) {
         this.intValue += i;
         return true;
      } else {
         return false;
      }
   }

   public int getIntValue4() {
      return this.intValue4;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public boolean testItemStack2(ItemStack itemStack) {
      return itemStack.isOf(Items.END_CRYSTAL)
         || itemStack.isOf(Blocks.ENDER_CHEST.asItem())
         || itemStack.isOf(Items.ENDER_PEARL)
         || itemStack.isOf(Items.EXPERIENCE_BOTTLE);
   }

   public int getIntValue3() {
      return this.intValue3;
   }

   public int getIntValue5() {
      return this.intValue5;
   }

   public void setIntValue4(int i) {
      this.intValue4 = i;
   }

   public void setIntValue2(int i) {
      this.intValue2 = i;
   }

   public void setIntValue7(int i) {
      this.intValue7 = i;
   }

   public int getIntValue7() {
      return this.intValue7;
   }
}
