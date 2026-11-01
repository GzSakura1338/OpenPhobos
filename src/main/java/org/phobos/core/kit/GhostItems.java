package org.phobos.core.kit;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.item.Item;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.animation.FloatAnimator;

public final class GhostItems {
   public static final Set<GhostItems.GhostItem> ghostItemSet = ConcurrentHashMap.newKeySet();

   public static void handleObject(Object object) {
      for (GhostItems.GhostItem ghostItem : ghostItemSet) {
         if (Objects.equals(object, ghostItem.getObject())) {
            ghostItem.setFlag(true);
         }
      }
   }

   public static void handleObject2(Object object, Slot slot, Item item, float f) {
      ghostItemSet.add(new GhostItems.GhostItem(new FloatAnimator(0.0F, f), object, slot, item));
   }

   public GhostItems() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   @Nullable
   public static GhostItems.GhostItem resolveGhostItem(Slot slot) {
      for (GhostItems.GhostItem ghostItem : ghostItemSet) {
         if (ghostItem.slot.equals(slot)) {
            ghostItem.floatAnimator.setFloatValue(ghostItem.flag ? 0.0F : ghostItem.floatAnimator.getFloatValue());
            ghostItem.floatAnimator.handleFloat3(10.0F, 0.1F);
            if (!(ghostItem.floatAnimator.getFloatValue2() <= 0.05F) || !ghostItem.flag) {
               return ghostItem;
            }

            ghostItemSet.remove(ghostItem);
            break;
         }
      }

      return null;
   }

   public static class GhostItem {
      public final Slot slot;
      public final Object object;
      public final FloatAnimator floatAnimator;
      public final Item item;
      public boolean flag = false;

      public void setFlag(boolean flag) {
         this.flag = flag;
      }

      public Item getItem() {
         return this.item;
      }

      public Slot getSlot() {
         return this.slot;
      }

      public GhostItem(FloatAnimator floatAnimator, Object object, Slot slot, Item item) {
         this.floatAnimator = floatAnimator;
         this.object = object;
         this.slot = slot;
         this.item = item;
      }

      public boolean isFlag() {
         return this.flag;
      }

      public Object getObject() {
         return this.object;
      }

      public FloatAnimator getFloatAnimator() {
         return this.floatAnimator;
      }
   }
}
