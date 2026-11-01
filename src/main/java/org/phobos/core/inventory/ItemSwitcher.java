package org.phobos.core.inventory;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.PickFromInventoryC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.MinecraftHolder;
import org.phobos.mixin.game.world.IClientPlayerInteractionManager;
import org.phobos.module.client.AntiCheatModule;
import org.phobos.module.player.SilentHotbarModule;
import org.phobos.core.gui.screen.ClickGuiScreen;
import org.phobos.core.ClientHolder;
import org.phobos.core.math.MathUtils;

public class ItemSwitcher implements MinecraftHolder {
   public boolean flag;
   public final List<ItemSwitcher.Switch> switchValueList = new ArrayList<>();
   public ItemSwitcher.HotbarSwitch hotbarSwitch;
   public final ClientPlayerInteractionManager clientPlayerInteractionManager;
   public static final int intValue = 16;
   public static final int intValue2 = 0;
   public static final int intValue3 = 32;
   public static final int intValue4 = 4;
   public static final int intValue5 = 64;
   public static final int intValue6 = 2;
   public boolean flag2 = false;
   public final InventoryManager inventoryManager;
   public final ClientPlayerEntity clientPlayerEntity;
   public static final int intValue7 = 1;
   public boolean flag3;
   public static final int intValue8 = 8;

   @Nullable
   public ItemSwitcher.SwitchResult resolveSwitchResult(@NotNull Hand hand, @Nullable Slot slot, int i) {
      if (slot == null) {
         return null;
      }

      Slot slot2 = hand == Hand.MAIN_HAND && (i & 32) == 0 ? this.getSlot3() : this.getSlot2();
      if (slot2.equals(slot)) {
         return ItemSwitcher.SwitchResult.resolveSwitchResult(slot);
      }

      if ((i & 4) != 0) {
         if (slot2.getStack().isEmpty()) {
            this.handleSlot3(slot, slot2);
            return ItemSwitcher.SwitchResult.resolveSwitchResult(slot2);
         } else {
            this.handleSlot4(slot, slot2);
            return ItemSwitcher.SwitchResult.resolveSwitchResult(slot2);
         }
      } else {
         boolean flag = (i & 8) != 0;
         boolean flag2 = (i & 16) != 0;
         boolean flag3 = (i & 1) != 0;
         boolean flag4 = (i & 64) != 0;
         ItemSwitcher.Switch switchValue = !AntiCheatModule.check()
            ? (
               (i & 2) != 0 && testSlot7(slot) && testSlot7(slot2)
                  ? new ItemSwitcher.HotbarSwitch(slot2, slot, flag3)
                  : (
                     flag
                        ? new ItemSwitcher.InventorySwap(slot, slot2, flag3, flag4)
                        : (flag2 ? new ItemSwitcher.PickSwap(slot, slot2, flag3) : new ItemSwitcher.SwapSwitch(slot, slot2, (i & 1) != 0, flag4))
                  )
            )
            : (AntiCheatModule.check2() ? new ItemSwitcher.InventorySwap(slot, slot2, flag3, flag4) : new ItemSwitcher.PickSwap(slot, slot2, flag3));
         switchValue.testItemSwitcher3(this);
         if (this.flag2) {
            this.switchValueList.add(switchValue);
         } else if (switchValue instanceof ItemSwitcher.HotbarSwitch hotbarSwitch) {
            if (this.hotbarSwitch == null) {
               this.hotbarSwitch = hotbarSwitch;
            }
         } else {
            this.switchValueList.add(switchValue);
         }

         return ItemSwitcher.SwitchResult.createSwitchResult(slot2, switchValue, i);
      }
   }

   @SafeVarargs
   @Nullable
   public final <T> T resolveObject(Function<Slot, @Nullable T>... functions) {
      for (Function<Slot, T> function : functions) {
         if (!this.flag) {
            T object = function.apply(this.clientPlayerEntity.playerScreenHandler.getSlot(45));
            if (object != null) {
               return object;
            }
         }

         T object2 = function.apply(this.getSlot3());
         if (object2 != null) {
            return object2;
         }

         for (int i = 36; i < 45; i++) {
            object2 = function.apply(this.clientPlayerEntity.playerScreenHandler.getSlot(i));
            if (object2 != null) {
               return object2;
            }
         }

         if (!this.flag3) {
            for (int i2 = 9; i2 < 36; i2++) {
               object2 = function.apply(this.clientPlayerEntity.playerScreenHandler.getSlot(i2));
               if (object2 != null) {
                  return object2;
               }
            }

            for (int i3 = 1; i3 < 5; i3++) {
               object2 = function.apply(this.clientPlayerEntity.playerScreenHandler.getSlot(i3));
               if (object2 != null) {
                  return object2;
               }
            }

            for (int i4 = 5; i4 < 9; i4++) {
               object2 = function.apply(this.clientPlayerEntity.playerScreenHandler.getSlot(i4));
               if (object2 != null) {
                  return object2;
               }
            }
         }
      }

      return null;
   }

   @Nullable
   public <T> ItemSwitcher.SwitchResult resolveSwitchResult2(Hand hand, Function<Slot, Slot> function, int i) {
      Slot slot = this.resolveObject(function);
      return this.resolveSwitchResult(hand, slot, i);
   }

   public void handleBlock(Block block, Consumer<Block> consumer) {
      Block block2 = this.resolveBlock(block);
      if (block2 != null) {
         consumer.accept(block2);
      }
   }

   public void handleRunnable(Runnable runnable) {
   }

   public void handleSlot(Slot slot, ScheduledClick scheduledClick) {
      this.handleInt5(slot.id, scheduledClick.getIntValue(), scheduledClick.getSlotActionType());
   }

   public boolean testSlot(Slot slot) {
      for (int i = 5; i < 9; i++) {
         if (this.clientPlayerEntity.playerScreenHandler.getSlot(i).equals(slot)) {
            return true;
         }
      }

      return false;
   }

   public void run() {
      if (AntiCheatModule.getAntiCheatModule().getInventory().getValue().isGrim()) {
         if (ClientHolder.getPhobosClient().getMovementSimulator().isFlag2()) {
            this.clientPlayerEntity.networkHandler.sendPacket(new ClientCommandC2SPacket(this.clientPlayerEntity, Mode.STOP_SPRINTING));
            this.clientPlayerEntity.setSprinting(false);
         }

         if (ClientHolder.getPhobosClient().getMovementSimulator().isFlag()) {
            this.clientPlayerEntity.networkHandler.sendPacket(new ClientCommandC2SPacket(this.clientPlayerEntity, Mode.RELEASE_SHIFT_KEY));
            this.clientPlayerEntity.setSneaking(false);
         }
      }
   }

   public boolean testSlot2(Slot slot, Slot slot2) {
      return this.testItemStack(slot.getStack(), slot2.getStack());
   }

   public void run2() {
   }

   public boolean testItemStack(ItemStack itemStack, ItemStack itemStack2) {
      return ItemStack.areItemsAndComponentsEqual(itemStack, itemStack2);
   }

   public void handleRunnable2(Runnable runnable) {
      this.run();
      runnable.run();
      this.run3();
      this.run2();
   }

   public boolean testItem(Item item) {
      for (int i = 36; i < 45; i++) {
         if (this.clientPlayerEntity.playerScreenHandler.getSlot(i).getStack().isOf(item)) {
            return true;
         }
      }

      return false;
   }

   public void handleRunnable3(Runnable runnable) {
      this.flag3 = true;
      runnable.run();
      this.flag3 = false;
   }

   public Slot resolveSlot(int i) {
      for (int i2 = 36; i2 < 45; i2++) {
         Slot slot = this.clientPlayerEntity.playerScreenHandler.getSlot(i2);
         if (slot.getIndex() == i) {
            return slot;
         }
      }

      throw new IllegalArgumentException("Index out of bounds: " + i);
   }

   @Nullable
   public ItemSwitcher.SwitchResult resolveSwitchResult3(Hand hand, Item item, int i) {
      if (!(this.clientPlayerEntity.currentScreenHandler instanceof PlayerScreenHandler)) {
         return null;
      } else if (((Slot)this.clientPlayerEntity.playerScreenHandler.slots.get(45)).getStack().isOf(item) && hand == Hand.OFF_HAND) {
         return ItemSwitcher.SwitchResult.resolveSwitchResult(this.clientPlayerEntity.playerScreenHandler.getSlot(45));
      } else if (!this.getSlot3().getStack().isOf(item) || hand != Hand.MAIN_HAND && hand != null) {
         Slot slot = this.resolveObject(slotx -> slotx.getStack().isOf(item) ? slotx : null);
         return this.resolveSwitchResult(hand, slot, i);
      } else {
         return ItemSwitcher.SwitchResult.resolveSwitchResult(this.getSlot3());
      }
   }

   public void handleInt(int i, Consumer<Slot> consumer) {
      for (int i2 = 36; i2 < 45; i2++) {
         Slot slot = this.clientPlayerEntity.playerScreenHandler.getSlot(i2);
         if (slot.getIndex() == i) {
            consumer.accept(slot);
            return;
         }
      }
   }

   public boolean check() {
      for (int i = 36; i < 45; i++) {
         if (this.clientPlayerEntity.playerScreenHandler.getSlot(i).getStack().isEmpty()) {
            return true;
         }
      }

      return false;
   }

   public boolean testSlot3(Slot slot) {
      return slot.getIndex() == this.getSlot3().getIndex();
   }

   public Slot resolveSlot2(Item... items) {
      return this.<Slot>resolveObject(
         Arrays.stream(items).<Function<Slot, Slot>>map(item -> slot -> slot.getStack().isOf(item) && slot.getStack().getCount() != 0 ? slot : null).toArray(Function[]::new)
      );
   }

   public Item resolveItem(Stream<Item> stream) {
      return this.<Item>resolveObject(
         stream.<Function<Slot, Item>>map(item -> slot -> slot.getStack().isOf(item) && slot.getStack().getCount() != 0 ? item : null).toArray(Function[]::new)
      );
   }

   public boolean testSlot4(Slot slot) {
      for (int i = 36; i < 45; i++) {
         if (this.clientPlayerEntity.playerScreenHandler.getSlot(i).equals(slot)) {
            return true;
         }
      }

      return false;
   }

   public void handleSlot2(Slot slot, boolean flag) {
      this.clientPlayerInteractionManager
         .clickSlot(this.clientPlayerEntity.playerScreenHandler.syncId, slot.id, flag ? 1 : 0, SlotActionType.THROW, this.clientPlayerEntity);
      this.run3();
   }

   public boolean testItem2(Item... items) {
      return this.resolveItem2(items) != null;
   }

   public Block resolveBlock(Block... blocks) {
      return this.<Block>resolveObject(
         Arrays.stream(blocks)
            .<Function<Slot, Block>>map(block -> slot -> slot.getStack().isOf(block.asItem()) && slot.getStack().getCount() != 0 ? block : null)
            .toArray(Function[]::new)
      );
   }

   public boolean testFlag(boolean flag, boolean flag2) {
      for (int i = 9; i < 36; i++) {
         if (this.clientPlayerEntity.playerScreenHandler.getSlot(i).getStack().isEmpty()) {
            return true;
         }
      }

      if (flag) {
         for (int i2 = 36; i2 < 45; i2++) {
            if (this.clientPlayerEntity.playerScreenHandler.getSlot(i2).getStack().isEmpty()) {
               return true;
            }
         }
      }

      if (flag2) {
         for (int i3 = 1; i3 < 5; i3++) {
            if (this.clientPlayerEntity.playerScreenHandler.getSlot(i3).getStack().isEmpty()) {
               return true;
            }
         }
      }

      return false;
   }

   public Item resolveItem2(Item... items) {
      return this.<Item>resolveObject(
         Arrays.stream(items).<Function<Slot, Item>>map(item -> slot -> slot.getStack().isOf(item) && slot.getStack().getCount() != 0 ? item : null).toArray(Function[]::new)
      );
   }

   public boolean testSlot5(Slot slot) {
      for (int i = 1; i < 5; i++) {
         if (this.clientPlayerEntity.playerScreenHandler.getSlot(i).equals(slot)) {
            return true;
         }
      }

      return false;
   }

   public Slot resolveSlot3(Predicate<Slot> predicate) {
      return this.resolveObject(slot -> slot.getStack().isEmpty() && predicate.test(slot) ? slot : null);
   }

   public boolean testSlot6(Slot slot, boolean flag) {
      return slot.getIndex() == this.resolveSlot4(flag).getIndex();
   }

   public int computePredicate(Predicate<ItemStack> predicate) {
      int i = 0;

      for (Slot slot : this.clientPlayerEntity.playerScreenHandler.slots) {
         if (predicate.test(slot.getStack())) {
            i += slot.getStack().getCount();
         }
      }

      return i;
   }

   public static boolean testSlot7(Slot slot) {
      return slot.getIndex() >= 0 && slot.getIndex() < 9;
   }

   public void handleInt2(int i, int i2) {
      if (this.clientPlayerEntity.currentScreenHandler instanceof PlayerScreenHandler) {
         this.clientPlayerInteractionManager
            .clickSlot(this.clientPlayerEntity.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, this.clientPlayerEntity);
         this.clientPlayerInteractionManager
            .clickSlot(this.clientPlayerEntity.currentScreenHandler.syncId, i2, 0, SlotActionType.PICKUP, this.clientPlayerEntity);
         this.clientPlayerInteractionManager
            .clickSlot(this.clientPlayerEntity.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, this.clientPlayerEntity);
         this.run3();
      }
   }

   public Block resolveBlock2(Stream<Block> stream) {
      return this.<Block>resolveObject(
         stream.<Function<Slot, Block>>map(block -> slot -> slot.getStack().isOf(block.asItem()) && slot.getStack().getCount() != 0 ? block : null).toArray(Function[]::new)
      );
   }

   public Slot resolveSlot4(boolean flag) {
      int i = computeInt(flag ? SilentHotbarModule.value() : this.clientPlayerEntity.getInventory().selectedSlot);
      if (i < 36 || i >= 45) {
         i = 36;
      }

      return this.clientPlayerEntity.playerScreenHandler.getSlot(i);
   }

   public void handleSlot3(Slot slot, Slot slot2) {
      if (this.clientPlayerEntity.currentScreenHandler instanceof PlayerScreenHandler) {
         this.clientPlayerInteractionManager
            .clickSlot(this.clientPlayerEntity.currentScreenHandler.syncId, slot.id, 0, SlotActionType.PICKUP, this.clientPlayerEntity);
         this.clientPlayerInteractionManager
            .clickSlot(this.clientPlayerEntity.currentScreenHandler.syncId, slot2.id, 0, SlotActionType.PICKUP, this.clientPlayerEntity);
         this.run3();
      }
   }

   public static Hand resolveHand(Slot slot) {
      return slot.getIndex() == 45 ? Hand.OFF_HAND : Hand.MAIN_HAND;
   }

   public Slot getSlot() {
      return this.resolveObject(slot -> {
         if (!testSlot7(slot)) {
            return null;
         } else {
            return slot.getStack().isOf(Items.TOTEM_OF_UNDYING) ? slot : null;
         }
      });
   }

   public static int computeInt(int i) {
      if (i == 40) {
         return 45;
      } else if (i >= 0 && i < PlayerInventory.getHotbarSize()) {
         return 36 + i;
      } else {
         return i >= 0 && i <= 45 ? i : -1;
      }
   }

   public static boolean testItem3(Item... items) {
      MutableBoolean mutableBoolean = new MutableBoolean(false);
      ClientHolder.getPhobosClient().getInventoryManager().handleConsumer2(itemSwitcher -> mutableBoolean.setValue(itemSwitcher.testItem2(items)));
      return mutableBoolean.getValue();
   }

   public void handleInt3(int i, int i2) {
      if (this.clientPlayerEntity.currentScreenHandler instanceof PlayerScreenHandler) {
         this.clientPlayerInteractionManager
            .clickSlot(this.clientPlayerEntity.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, this.clientPlayerEntity);
         this.clientPlayerInteractionManager
            .clickSlot(this.clientPlayerEntity.currentScreenHandler.syncId, i2, 0, SlotActionType.PICKUP, this.clientPlayerEntity);
         this.run3();
      }
   }

   public void handleSlot4(Slot slot, Slot slot2) {
      if (this.clientPlayerEntity.currentScreenHandler instanceof PlayerScreenHandler) {
         this.clientPlayerInteractionManager
            .clickSlot(this.clientPlayerEntity.currentScreenHandler.syncId, slot.id, 0, SlotActionType.PICKUP, this.clientPlayerEntity);
         this.clientPlayerInteractionManager
            .clickSlot(this.clientPlayerEntity.currentScreenHandler.syncId, slot2.id, 0, SlotActionType.PICKUP, this.clientPlayerEntity);
         this.clientPlayerInteractionManager
            .clickSlot(this.clientPlayerEntity.currentScreenHandler.syncId, slot.id, 0, SlotActionType.PICKUP, this.clientPlayerEntity);
         this.run3();
      }
   }

   public Slot getSlot2() {
      return this.clientPlayerEntity.playerScreenHandler.getSlot(45);
   }

   public void handleInt4(int i, int i2, SlotActionType slotActionType, ScreenHandler screenHandler) {
      this.clientPlayerInteractionManager.clickSlot(screenHandler.syncId, i, i2, slotActionType, this.clientPlayerEntity);
   }

   public void run4() {
      if (this.inventoryManager.getAtomicBoolean().get()) {
         for (int i = this.switchValueList.size() - 1; i >= 0; i--) {
            ItemSwitcher.Switch switchValue = this.switchValueList.get(i);
            if (!switchValue.flag3 || !switchValue.testItemSwitcher(this)) {
               break;
            }
         }

         if (this.hotbarSwitch != null && this.hotbarSwitch.flag3) {
            this.hotbarSwitch.testItemSwitcher(this);
         }
      }
   }

   public static boolean testInt(int i) {
      return i >= 0 && i < 9;
   }

   public void handleInt5(int i, int i2, SlotActionType slotActionType, boolean... flags) {
      PlayerScreenHandler playerScreenHandler = this.clientPlayerEntity.playerScreenHandler;
      DefaultedList<Slot> defaultedList = playerScreenHandler.slots;
      int i4 = defaultedList.size();
      ArrayList<ItemStack> arrayList = Lists.newArrayListWithCapacity(i4);

      for (Slot slot : defaultedList) {
         arrayList.add(slot.getStack().copy());
      }

      if (flags.length == 0 || !flags[0]) {
         playerScreenHandler.onSlotClick(i, i2, slotActionType, this.clientPlayerEntity);
      }

      Int2ObjectOpenHashMap<ItemStack> int2ObjectOpenHashMap = new Int2ObjectOpenHashMap();

      for (int i3 = 0; i3 < i4; i3++) {
         ItemStack itemStack2 = arrayList.get(i3);
         ItemStack itemStack;
         if (!ItemStack.areEqual(itemStack2, itemStack = ((Slot)defaultedList.get(i3)).getStack())) {
            int2ObjectOpenHashMap.put(i3, itemStack.copy());
         }
      }

      this.clientPlayerEntity
         .networkHandler
         .sendPacket(
            new ClickSlotC2SPacket(
               this.clientPlayerEntity.playerScreenHandler.syncId,
               playerScreenHandler.getRevision(),
               i,
               i2,
               slotActionType,
               playerScreenHandler.getCursorStack().copy(),
               int2ObjectOpenHashMap
            )
         );
   }

   public Slot getSlot3() {
      return this.resolveSlot4(false);
   }

   public ItemSwitcher(
      InventoryManager inventoryManager, ClientPlayerEntity clientPlayerEntity, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      this.flag = false;
      this.inventoryManager = inventoryManager;
      this.clientPlayerEntity = clientPlayerEntity;
      this.clientPlayerInteractionManager = clientPlayerInteractionManager;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public void run3() {
      if (AntiCheatModule.getAntiCheatModule().getInventory().getValue().isStrict()
         && this.clientPlayerEntity.currentScreenHandler == this.clientPlayerEntity.playerScreenHandler) {
         this.clientPlayerEntity.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(this.clientPlayerEntity.currentScreenHandler.syncId));
      }
   }

   public static float computeFloat(float f) {
      return f + MathUtils.computeFloat8(-6.0F, 6.0F);
   }

   public void setFlag2(boolean flag) {
      this.flag2 = flag;
   }

   public static class HotbarSwitch extends ItemSwitcher.Switch {
      public HotbarSwitch(Slot slot, Slot slot2, boolean flag) {
         super(slot, slot2, flag);
      }

      @Override
      public boolean testItemSwitcher2(ItemSwitcher itemSwitcher, Slot slot, Slot slot2) {
         if (ItemSwitcher.testSlot7(slot2) && MinecraftClient.getInstance().interactionManager != null) {
            itemSwitcher.clientPlayerEntity.getInventory().selectedSlot = slot2.getIndex();
            ((IClientPlayerInteractionManager)(Object)itemSwitcher.clientPlayerInteractionManager).invokeSyncSelectedSlot();
            return true;
         } else {
            return false;
         }
      }
   }

   public static class InventorySwap extends ItemSwitcher.Switch {
      public boolean flag;
      public final boolean flag2;

      @Override
      public boolean testItemSwitcher(ItemSwitcher itemSwitcher) {
         if (!this.flag) {
            return super.testItemSwitcher(itemSwitcher);
         }

         itemSwitcher.run();
         itemSwitcher.handleInt5(this.slot.id, this.slot2.getIndex(), SlotActionType.SWAP, this.flag2);
         itemSwitcher.run2();
         return true;
      }

      @Override
      public boolean testItemSwitcher2(ItemSwitcher itemSwitcher, Slot slot, Slot slot2) {
         if (ItemSwitcher.testSlot7(slot2)) {
            if (ItemSwitcher.testSlot7(slot)) {
               Slot slot3 = itemSwitcher.resolveObject(slotx -> !ItemSwitcher.testSlot7(slotx) && !slotx.equals(itemSwitcher.getSlot2()) ? slotx : null);
               if (slot3 == null) {
                  return false;
               }

               itemSwitcher.run();
               itemSwitcher.handleInt5(slot3.id, slot.getIndex(), SlotActionType.SWAP, this.flag2);
               itemSwitcher.handleInt5(slot3.id, slot2.getIndex(), SlotActionType.SWAP, this.flag2);
               itemSwitcher.handleInt5(slot3.id, slot.getIndex(), SlotActionType.SWAP, this.flag2);
               itemSwitcher.run2();
            } else {
               itemSwitcher.run();
               itemSwitcher.handleInt5(slot.id, slot2.getIndex(), SlotActionType.SWAP, this.flag2);
               this.flag = true;
               itemSwitcher.run2();
            }
         }

         return false;
      }

      public InventorySwap(Slot slot, Slot slot2, boolean flag, boolean flag2) {
         super(slot, slot2, flag);
         this.flag2 = flag2;
      }
   }

   public static class PickSwap extends ItemSwitcher.Switch {
      public int intValue = -1;

      public PickSwap(Slot slot, Slot slot2, boolean flag) {
         super(slot, slot2, flag);
      }

      @Override
      public boolean testItemSwitcher(ItemSwitcher itemSwitcher) {
         if (this.intValue >= 0) {
            itemSwitcher.clientPlayerEntity.networkHandler.sendPacket(new PickFromInventoryC2SPacket(this.intValue));
            this.intValue = -1;
            return true;
         } else {
            return false;
         }
      }

      @Override
      public boolean testItemSwitcher2(ItemSwitcher itemSwitcher, Slot slot, Slot slot2) {
         int i = slot.getIndex();
         if (i >= 0 && !itemSwitcher.getSlot2().equals(slot) && !itemSwitcher.getSlot2().equals(slot2)) {
            this.intValue = i;
            itemSwitcher.clientPlayerEntity.networkHandler.sendPacket(new PickFromInventoryC2SPacket(i));
            return true;
         } else {
            return false;
         }
      }
   }

   public static class SwapSwitch extends ItemSwitcher.Switch {
      public final boolean flag;

      public SwapSwitch(Slot slot, Slot slot2, boolean flag, boolean flag2) {
         super(slot, slot2, flag);
         this.flag = flag2;
      }

      @Override
      public boolean testItemSwitcher(ItemSwitcher itemSwitcher) {
         return this.testItemSwitcher2(itemSwitcher, this.slot, this.slot2);
      }

      @Override
      public boolean testItemSwitcher2(ItemSwitcher itemSwitcher, Slot slot, Slot slot2) {
         if (!(itemSwitcher.clientPlayerEntity.currentScreenHandler instanceof PlayerScreenHandler) && !(MinecraftHolder.mc.currentScreen instanceof ClickGuiScreen)
            || !ItemSwitcher.testSlot7(slot2) && slot2.getIndex() != 40) {
            return false;
         }

         itemSwitcher.run();
         itemSwitcher.handleInt5(slot.id, slot2.getIndex(), SlotActionType.SWAP, this.flag);
         itemSwitcher.run3();
         itemSwitcher.run2();
         return true;
      }
   }

   public abstract static class Switch {
      public final Slot slot;
      public final boolean flag3;
      public final Slot slot2;

      public boolean testItemSwitcher(ItemSwitcher itemSwitcher) {
         return this.testItemSwitcher2(itemSwitcher, this.slot2, this.slot);
      }

      public Switch(Slot slot, Slot slot2, boolean flag) {
         this.slot = slot;
         this.slot2 = slot2;
         this.flag3 = flag;
      }

      public abstract boolean testItemSwitcher2(ItemSwitcher var1, Slot var2, Slot var3);

      public boolean testItemSwitcher3(ItemSwitcher itemSwitcher) {
         return this.testItemSwitcher2(itemSwitcher, this.slot, this.slot2);
      }
   }

   public record SwitchResult(Slot slot, Hand hand, @Nullable Block block, @Nullable ItemSwitcher.Switch action) {

      public static ItemSwitcher.SwitchResult createSwitchResult(Slot slot, @Nullable ItemSwitcher.Switch switchValue, int i) {
         return new ItemSwitcher.SwitchResult(
            slot,
            (i & 32) != 0 ? Hand.OFF_HAND : ItemSwitcher.resolveHand(slot),
            slot.getStack().getItem() instanceof BlockItem blockItem ? blockItem.getBlock() : null,
            switchValue
         );
      }

      public static ItemSwitcher.SwitchResult createSwitchResult2(Slot slot, @Nullable ItemSwitcher.Switch switchValue) {
         return new ItemSwitcher.SwitchResult(
            slot, ItemSwitcher.resolveHand(slot), slot.getStack().getItem() instanceof BlockItem blockItem ? blockItem.getBlock() : null, switchValue
         );
      }

      public static ItemSwitcher.SwitchResult resolveSwitchResult(Slot slot) {
         return createSwitchResult2(slot, null);
      }

      public Hand getHand() {
         return this.hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
      }
   }
}
