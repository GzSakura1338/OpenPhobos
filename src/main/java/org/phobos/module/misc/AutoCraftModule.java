package org.phobos.module.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.CraftRequestC2SPacket;
import net.minecraft.network.packet.s2c.play.CraftFailedResponseS2CPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;
import org.phobos.api.TickGate;
import org.phobos.api.TickGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.ItemSetSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.core.inventory.ItemUtils;
import org.phobos.core.module.Categories;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.module.Module;

public class AutoCraftModule extends Module {
   public final TickGate tickGate;
   public final TickGate tickGate2;
   public final Set<Item> itemSet;
   public static final AutoCraftModule.CraftingGrid craftingGrid = new AutoCraftModule.CraftingGrid(2, 2);
   public final Setting<Boolean> table;
   public static final AutoCraftModule.CraftingGrid craftingGrid2 = new AutoCraftModule.CraftingGrid(3, 3);
   public boolean flag;
   public final Setting<Boolean> procedural;
   public final Map<Item, RecipeEntry<CraftingRecipe>> itemMap;
   public final Setting<Boolean> check;
   public final Setting<Boolean> filter;
   public final Map<Identifier, Long> identifierMap;
   public final Setting<Integer> complete;
   public final Setting<Integer> delay;
   public final Setting<Set<Item>> items = this.resolveSetting2(new ItemSetSetting("Items"));

   public boolean testItemStack(ItemStack[] itemStacks, AutoCraftModule.CraftingGrid craftingGrid) {
      List<ItemStack> list = new ArrayList<>();

      for (ItemStack itemStack : itemStacks) {
         Item item = itemStack.getItem();
         if (this.computePredicate(itemStackx -> itemStackx.isOf(item)) < itemStack.getCount()) {
            RecipeEntry<CraftingRecipe> recipeEntry = this.itemMap.get(item);
            if (recipeEntry != null) {
               if (!this.testCraftingRecipe((CraftingRecipe)recipeEntry.value(), craftingGrid)) {
                  return false;
               }

               list.add(itemStack);
            }
         }
      }

      for (ItemStack itemStack2 : list) {
         this.itemSet.add(itemStack2.getItem());
      }

      return true;
   }

   public boolean testItemStack2(ItemStack[] itemStacks) {
      for (ItemStack itemStack : itemStacks) {
         int i = itemStack.getCount();
         int i2 = this.computePredicate(itemStack2 -> itemStack2.isOf(itemStack.getItem()));
         if (i2 >= i) {
            return true;
         }
      }

      return false;
   }

   public boolean testCraftingRecipe(CraftingRecipe craftingRecipe, AutoCraftModule.CraftingGrid craftingGrid) {
      if (!craftingGrid.testCraftingRecipe(craftingRecipe)) {
         return false;
      }

      for (Ingredient ingredient : craftingRecipe.getIngredients()) {
         ItemStack[] itemStacks = ingredient.getMatchingStacks();
         if (!this.testItemStack2(itemStacks)) {
            return false;
         }
      }

      return true;
   }

   public int computePredicate(Predicate<ItemStack> predicate) {
      MutableInt mutableInt = new MutableInt();
      this.phobosClient.getInventoryManager().handleConsumer2(itemSwitcher -> mutableInt.setValue(itemSwitcher.computePredicate(predicate)));
      return mutableInt.getValue();
   }

   public AutoCraftModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoCraft", Categories.misc);
      this.delay = this.resolveSetting2(new NumberSetting<>("Delay", 5, 0, 30));
      this.complete = this.resolveSetting2(new NumberSetting<>("Complete", 1, 0, 30));
      this.procedural = this.resolveSetting2(new BooleanSetting("Procedural").resolveSetting7("Tries to craft dependencies"));
      this.table = this.resolveSetting2(new BooleanSetting("Table").resolveSetting7("Craft the crafting table if needed"));
      this.check = this.resolveSetting2(new BooleanSetting("Check").resolveSetting7("Checks if you already have the item you want to craft"));
      this.filter = this.resolveSetting2(new BooleanSetting("Filter").resolveSetting7("Runs comparator filtering on tools and armor"));
      this.itemMap = new HashMap<>();
      this.itemSet = ConcurrentHashMap.newKeySet();
      this.identifierMap = new HashMap<>();
      this.tickGate = new TickGate.Normal();
      this.tickGate2 = new TickGate.Normal();
      this.resolveSettingGroup(this.procedural, this.table);
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               AutoCraftModule.this.identifierMap.entrySet().removeIf(entry -> entry.getValue() + 2000L < System.currentTimeMillis());
               if (AutoCraftModule.this.itemMap.isEmpty()) {
                  AutoCraftModule.this.handleWorld(clientWorld);
               } else {
                  Set<Item> set = AutoCraftModule.this.items.getValue();
                  if (mc.currentScreen instanceof CraftingScreen craftingScreen) {
                     CraftingScreenHandler craftingScreenHandler = (CraftingScreenHandler)craftingScreen.getScreenHandler();
                     int i = craftingScreenHandler.syncId;
                     Slot slot = craftingScreenHandler.getSlot(0);
                     ItemStack itemStack = slot.getStack();
                     if (AutoCraftModule.this.tickGate2.testInt3(AutoCraftModule.this.complete.getValue()) && !itemStack.isEmpty()) {
                        Item item = itemStack.getItem();
                        if (set.contains(item) || AutoCraftModule.this.itemSet.contains(item) || Items.CRAFTING_TABLE.equals(item) && AutoCraftModule.this.flag
                           )
                         {
                           clientPlayerInteractionManager.clickSlot(i, slot.id, 0, SlotActionType.QUICK_MOVE, clientPlayerEntity);
                           AutoCraftModule.this.itemSet.remove(item);
                           AutoCraftModule.this.flag = false;
                        }
                     }

                     if (AutoCraftModule.this.tickGate.testInt3(AutoCraftModule.this.delay.getValue())) {
                        if (AutoCraftModule.this.testClientPlayerEntity2(
                           clientPlayerEntity, AutoCraftModule.craftingGrid2, AutoCraftModule.this.itemSet, i, true
                        )) {
                           return;
                        }

                        AutoCraftModule.this.testClientPlayerEntity2(clientPlayerEntity, AutoCraftModule.craftingGrid2, set, i, false);
                     }
                  } else {
                     if (mc.currentScreen instanceof InventoryScreen inventoryScreen) {
                        PlayerScreenHandler playerScreenHandler = (PlayerScreenHandler)inventoryScreen.getScreenHandler();
                        int i2 = playerScreenHandler.syncId;
                        Slot slot2 = playerScreenHandler.getSlot(0);
                        ItemStack itemStack2 = slot2.getStack();
                        if (AutoCraftModule.this.tickGate2.testInt3(AutoCraftModule.this.complete.getValue()) && !itemStack2.isEmpty()) {
                           Item item2 = itemStack2.getItem();
                           if (set.contains(item2)
                              || AutoCraftModule.this.itemSet.contains(item2)
                              || Items.CRAFTING_TABLE.equals(item2) && AutoCraftModule.this.flag) {
                              clientPlayerInteractionManager.clickSlot(i2, slot2.id, 0, SlotActionType.QUICK_MOVE, clientPlayerEntity);
                              AutoCraftModule.this.itemSet.remove(item2);
                              AutoCraftModule.this.flag = false;
                           }
                        }

                        if (AutoCraftModule.this.tickGate.testInt3(AutoCraftModule.this.delay.getValue())) {
                           if (AutoCraftModule.this.testClientPlayerEntity2(
                              clientPlayerEntity, AutoCraftModule.craftingGrid, AutoCraftModule.this.itemSet, i2, true
                           )) {
                              return;
                           }

                           AutoCraftModule.this.testClientPlayerEntity2(clientPlayerEntity, AutoCraftModule.craftingGrid, set, i2, false);
                        }
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Receive<CraftFailedResponseS2CPacket>>() {
            public void handleObject(
               Receive<CraftFailedResponseS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (AutoCraftModule.this.identifierMap.remove(((CraftFailedResponseS2CPacket)receive.getPacket()).getRecipeId()) != null) {
                  receive.setCancelled(true);
               }
            }
         }
      );
      ModuleListenerUtils.handleListenerHolder4(this, this.itemMap::clear);
   }

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, Item item, AutoCraftModule.CraftingGrid craftingGrid, int i) {
      RecipeEntry<CraftingRecipe> recipeEntry = this.itemMap.get(item);
      if (recipeEntry == null) {
         return false;
      }

      CraftingRecipe craftingRecipe = (CraftingRecipe)recipeEntry.value();
      if (!this.testCraftingRecipe(craftingRecipe, craftingGrid)) {
         return false;
      }

      clientPlayerEntity.networkHandler.sendPacket(new CraftRequestC2SPacket(i, recipeEntry, false));
      this.identifierMap.put(recipeEntry.id(), System.currentTimeMillis());
      return true;
   }

   public boolean testCraftingRecipe2(CraftingRecipe craftingRecipe, AutoCraftModule.CraftingGrid craftingGrid) {
      for (Ingredient ingredient : craftingRecipe.getIngredients()) {
         ItemStack[] itemStacks = ingredient.getMatchingStacks();
         if (!this.testItemStack(itemStacks, craftingGrid)) {
            return false;
         }
      }

      return true;
   }

   public void handleWorld(World world) {
      for (RecipeEntry<?> recipeEntry : world.getRecipeManager().values()) {
         if (!recipeEntry.value().isEmpty() && recipeEntry.value() instanceof CraftingRecipe) {
            ItemStack itemStack = recipeEntry.value().getResult(world.getRegistryManager());
            if (!itemStack.isEmpty()) {
               this.itemMap.put(itemStack.getItem(), (RecipeEntry<CraftingRecipe>)recipeEntry);
            }
         }
      }
   }

   public boolean testFunction(Function<ItemSwitcher, Boolean> function) {
      MutableBoolean mutableBoolean = new MutableBoolean();
      this.phobosClient.getInventoryManager().handleConsumer2(itemSwitcher -> mutableBoolean.setValue(function.apply(itemSwitcher)));
      return mutableBoolean.getValue();
   }

   public boolean testItem(Item item) {
      return this.testFunction(itemSwitcher -> itemSwitcher.testItem2(item));
   }

   public boolean testClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity, AutoCraftModule.CraftingGrid craftingGrid, Set<Item> set, int i, boolean flag) {
      for (Item item : set) {
         if (!this.check.getValue() || !this.testItem(item)) {
            if (this.filter.getValue() && ItemUtils.testItem(item)) {
               ItemStack itemStack = new ItemStack(item);
               if (this.testFunction(itemSwitcher -> ItemUtils.testItemSwitcher5(itemSwitcher, item, itemStack))) {
                  continue;
               }
            }

            RecipeEntry<CraftingRecipe> recipeEntry = this.itemMap.get(item);
            if (recipeEntry != null) {
               CraftingRecipe craftingRecipe = (CraftingRecipe)recipeEntry.value();
               if (this.testCraftingRecipe(craftingRecipe, craftingGrid)) {
                  clientPlayerEntity.networkHandler.sendPacket(new CraftRequestC2SPacket(i, recipeEntry, false));
                  this.identifierMap.put(recipeEntry.id(), System.currentTimeMillis());
                  return true;
               }

               if (this.procedural.getValue()) {
                  boolean flag2 = craftingGrid.testCraftingRecipe(craftingRecipe);
                  if (!flag2
                     && this.table.getValue()
                     && !this.testItem(Blocks.CRAFTING_TABLE.asItem())
                     && this.testClientPlayerEntity(clientPlayerEntity, Blocks.CRAFTING_TABLE.asItem(), craftingGrid, i)) {
                     this.flag = true;
                     return true;
                  }

                  if (!flag && flag2 && this.testCraftingRecipe2(craftingRecipe, craftingGrid)) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   private record CraftingGrid(int width, int height) {

      public boolean testCraftingRecipe(CraftingRecipe craftingRecipe) {
         return craftingRecipe.fits(this.width, this.height);
      }
   }
}
