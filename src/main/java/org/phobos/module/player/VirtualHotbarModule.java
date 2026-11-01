package org.phobos.module.player;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.inventory.VirtualHotbarData;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.module.Module;

public class VirtualHotbarModule extends Module {
   public static VirtualHotbarModule virtualHotbarModule;
   public VirtualHotbarData virtualHotbarData;
   public final Setting<Boolean> setup = new BooleanSetting("Setup").resolveSetting6(this);
   public Map<Integer, Integer> integerMap;
   public Map<Integer, Integer> integerMap2 = null;

   public int computeInt(int i) {
      if (i == -2) {
         return 45;
      } else {
         return i > -1 && i < 9 ? 36 + i : i;
      }
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      super.handleJsonElement(jsonObject);
      JsonElement jsonElement2 = jsonObject.get("layout");
      if (jsonElement2 != null) {
         this.virtualHotbarData = new VirtualHotbarData();
         this.virtualHotbarData.handleJsonElement(jsonElement2);
      }
   }

   public void run() {
      if (this.check7() && mc.player != null) {
         VirtualHotbarData virtualHotbarData = this.virtualHotbarData;
         if (virtualHotbarData != null) {
            Map<Integer, Integer> map = new HashMap<>(14, 0.75F);
            Map<Integer, Integer> map2 = new HashMap<>(14, 0.75F);
            List<Integer> list = new ArrayList<>();

            for (int i2 = 0; i2 < 9; i2++) {
               Item item = virtualHotbarData.resolveItem(this.computeInt(i2));
               if (item == Items.AIR) {
                  list.add(i2);
               } else {
                  boolean flag = true;

                  for (int i = 0; i < 9; i++) {
                     if (!map.containsValue(i) && item == mc.player.getInventory().getStack(i).getItem()) {
                        map.put(i2, i);
                        map2.put(i, i2);
                        flag = false;
                        break;
                     }
                  }

                  if (flag) {
                     list.add(i2);
                  }
               }
            }

            for (int i4 : list) {
               for (int i3 = 0; i3 < 9; i3++) {
                  if (!map.containsValue(i3)) {
                     map.put(i4, i3);
                     map2.put(i3, i4);
                     break;
                  }
               }
            }

            this.integerMap2 = map2;
            this.integerMap = map;
         }
      }
   }

   public boolean testInt(int i) {
      if (this.check7() && mc.player != null) {
         Map<Integer, Integer> map = this.integerMap;
         Map<Integer, Integer> map2 = this.integerMap2;
         if (map != null && map2 != null) {
            if (i > 0) {
               i = 1;
            }

            if (i < 0) {
               i = -1;
            }

            int i2 = mc.player.getInventory().selectedSlot;
            int i3 = map2.getOrDefault(i2, i2);
            i3 -= i;
            if (i3 < 0) {
               i3 = 8;
            } else if (i3 > 8) {
               i3 = 0;
            }

            mc.player.getInventory().selectedSlot = map.getOrDefault(i3, i3);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public int computeInt2(int i) {
      return this.computeInt3(i, this.integerMap2);
   }

   public VirtualHotbarModule(PhobosClient phobosClient) {
      super(phobosClient, "VirtualHotbar", Categories.player, "Simulates an additional hotbar");
      this.integerMap = null;
      this.setup.handleConsumer2(booleanValue -> {
         if (this.setup.getValue()) {
            if (mc.player != null) {
               this.virtualHotbarData = VirtualHotbarData.resolveVirtualHotbarData(mc.player);
            }

            this.setup.setValue(false);
         }
      });
      virtualHotbarModule = this;
   }

   public static VirtualHotbarModule getVirtualHotbarModule() {
      return virtualHotbarModule;
   }

   public int computeInt3(int i, Map<Integer, Integer> map) {
      return this.check7() && map != null ? map.getOrDefault(i, i) : i;
   }

   @Override
   public JsonElement createJsonPrimitive() {
      JsonObject jsonObject = super.createJsonPrimitive().getAsJsonObject();
      if (this.virtualHotbarData != null) {
         jsonObject.add("layout", this.virtualHotbarData.createJsonPrimitive());
      }

      return jsonObject;
   }

   public int computeInt4(int i) {
      return this.computeInt3(i, this.integerMap);
   }
}
