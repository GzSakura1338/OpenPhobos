package org.phobos.core.kit;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.Named;

public class Kit implements Named {
   public final ItemHolder itemHolder;
   public final String name;
   public final KitSlotItemMap kitSlotItemMap;
   public final KitSlotItemMap kitSlotItemMap2 = new KitSlotItemMap();

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, Consumer<Slot> consumer) {
      this.handleInt(9, 36, i -> this.handleClientPlayerEntity4(clientPlayerEntity, i, consumer));
      this.handleInt(36, 45, i -> this.handleClientPlayerEntity4(clientPlayerEntity, i, consumer));
   }

   public Map<Integer, Item> getBlockPosMap() {
      Map<Integer, Item> map = new HashMap<>();
      map.putAll(this.kitSlotItemMap.getBlockPosMap());
      map.putAll(this.kitSlotItemMap2.getBlockPosMap());
      return map;
   }

   public void handleSlot(Slot slot, KitSlotItemMap kitSlotItemMap) {
      ItemStack itemStack = slot.getStack();
      if (!itemStack.isEmpty()) {
         if (this.testItem(itemStack.getItem())) {
            kitSlotItemMap.handleSlot(slot, itemStack.getItem());
         }
      }
   }

   public Kit(String text) {
      this.kitSlotItemMap = new KitSlotItemMap();
      this.itemHolder = new ItemHolder();
      this.name = text;
   }

   public void handleClientPlayerEntity2(
      ClientPlayerEntity clientPlayerEntity, Map<Integer, Item> map, int i, Kit.SlotVisitorFactory slotVisitorFactory
   ) {
      this.handleInt(9, 36, i2 -> this.handleClientPlayerEntity3(clientPlayerEntity, map, i2, i, slotVisitorFactory));
      this.handleInt(36, 45, i2 -> this.handleClientPlayerEntity3(clientPlayerEntity, map, i2, i, slotVisitorFactory));
   }

   public static Kit resolveKit(String text, ClientPlayerEntity clientPlayerEntity) {
      Kit kit = new Kit(text);

      for (int i = 9; i < 36; i++) {
         Slot slot = clientPlayerEntity.playerScreenHandler.getSlot(i);
         kit.handleSlot(slot, kit.kitSlotItemMap2);
      }

      for (int i2 = 36; i2 < 45; i2++) {
         Slot slot2 = clientPlayerEntity.playerScreenHandler.getSlot(i2);
         kit.handleSlot(slot2, kit.kitSlotItemMap);
      }

      kit.handleItemStack(clientPlayerEntity.getOffHandStack(), kit.itemHolder);
      return kit;
   }

   public void handleClientPlayerEntity3(
      ClientPlayerEntity clientPlayerEntity, Map<Integer, Item> map, int i, int i2, Kit.SlotVisitorFactory slotVisitorFactory
   ) {
      try {
         Slot slot = clientPlayerEntity.playerScreenHandler.getSlot(i);
         Slot slot2 = clientPlayerEntity.currentScreenHandler.getSlot(i2 + (i - 9));
         Item item = map.get(slot.id);
         slotVisitorFactory.handleSlot(slot, item, slot2);
      } catch (Throwable var9) {
      }
   }

   public RegearPlan resolveRegearPlan(ClientPlayerEntity clientPlayerEntity, List<Slot> list) {
      RegearPlan regearPlan = new RegearPlan();
      List<Slot> list2 = new ArrayList<>();
      Map<Integer, Item> map = this.getBlockPosMap();
      this.handleClientPlayerEntity2(clientPlayerEntity, map, list.size(), (slot, item, slot2x) -> {
         if (item != null) {
            ItemStack itemStackx = slot2x.getStack();
            if (!itemStackx.isEmpty()) {
               if (!itemStackx.isOf(item)) {
                  regearPlan.handleSlot2(slot2x);
               }
            }
         }
      });
      if (this.itemHolder.check()) {
         ItemStack itemStack2 = clientPlayerEntity.getOffHandStack();
         Item item = this.itemHolder.getItem();
         if (itemStack2.isEmpty() || !itemStack2.isOf(item)) {
            for (Slot slot : list) {
               ItemStack itemStack = slot.getStack();
               if (!itemStack.isEmpty() && itemStack.isOf(item)) {
                  regearPlan.setSlotHandle(new SlotHandle(slot));
                  break;
               }
            }
         }
      }

      for (Slot slot2 : list) {
         ItemStack itemStack3 = slot2.getStack();
         if (!itemStack3.isEmpty()) {
            Item item2 = itemStack3.getItem();
            this.handleClientPlayerEntity2(clientPlayerEntity, map, list.size(), (slot2x, item2x, slot3) -> {
               if (item2x != null) {
                  if (!list2.contains(slot3)) {
                     if (item2x.equals(item2)) {
                        ItemStack itemStackx = slot3.getStack();
                        boolean flag = itemStackx.isEmpty();
                        if (itemStackx.getCount() < itemStackx.getMaxCount() || flag) {
                           regearPlan.handleSlot4(slot2, slot3, flag);
                           list2.add(slot3);
                        }
                     }
                  }
               }
            });
         }
      }

      return regearPlan;
   }

   public String describeGson(Gson gson) {
      JsonObject jsonObject = new JsonObject();
      jsonObject.add("hotbar", this.kitSlotItemMap.createJsonPrimitive());
      jsonObject.add("inventory", this.kitSlotItemMap2.createJsonPrimitive());
      if (this.itemHolder.check()) {
         jsonObject.add("offhand", this.itemHolder.createJsonPrimitive());
      }

      return gson.toJson(jsonObject);
   }

   @Override
   public String getName() {
      return this.name;
   }

   public KitSlotItemMap getKitSlotItemMap2() {
      return this.kitSlotItemMap2;
   }

   public ItemHolder getItemHolder() {
      return this.itemHolder;
   }

   public void handleInt(int i, int i2, IntConsumer intConsumer) {
      for (int i3 = i; i3 < i2; i3++) {
         intConsumer.accept(i3);
      }
   }

   public void handleItemStack(ItemStack itemStack, ItemHolder itemHolder) {
      if (!itemStack.isEmpty()) {
         if (this.testItem(itemStack.getItem())) {
            itemHolder.setItem(itemStack.getItem());
         }
      }
   }

   public boolean testItem(Item item) {
      return !(item instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock);
   }

   public static Optional<Kit> resolveOptional(String text, String text2) {
      try {
         JsonObject jsonObject = JsonParser.parseString(text2).getAsJsonObject();
         Kit kit = new Kit(text);
         kit.kitSlotItemMap.handleJsonElement(jsonObject.get("hotbar"));
         kit.kitSlotItemMap2.handleJsonElement(jsonObject.get("inventory"));
         if (jsonObject.has("offhand")) {
            kit.itemHolder.handleJsonElement(jsonObject.get("offhand"));
         }

         return Optional.of(kit);
      } catch (Throwable throwable) {
         return Optional.empty();
      }
   }

   public void handleClientPlayerEntity4(ClientPlayerEntity clientPlayerEntity, int i, Consumer<Slot> consumer) {
      try {
         Slot slot = clientPlayerEntity.playerScreenHandler.getSlot(i);
         consumer.accept(slot);
      } catch (Throwable var5) {
      }
   }

   public boolean isEmpty() {
      return this.kitSlotItemMap.isEmpty() && this.kitSlotItemMap2.isEmpty();
   }

   public KitSlotItemMap getKitSlotItemMap() {
      return this.kitSlotItemMap;
   }

   @FunctionalInterface
   private interface SlotVisitorFactory {
      void handleSlot(Slot var1, @Nullable Item var2, Slot var3);
   }
}
