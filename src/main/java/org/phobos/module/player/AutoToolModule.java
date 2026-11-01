package org.phobos.module.player;

import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.api.KeyValue;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.inventory.EnchantmentUtils;
import org.phobos.event.AutoToolStartBreakListener;
import org.phobos.module.Module;

public class AutoToolModule extends Module {
   public final Setting<Boolean> inventory = new BooleanSetting("Inventory").resolveSetting7("Gets items from inventory").resolveSetting6(this);
   public final Setting<Boolean> silent = new BooleanSetting("Silent").resolveSetting7("Switches to the item right before breaking").resolveSetting6(this);
   public static AutoToolModule autoToolModule;

   @Override
   public String nothing() {
      return this.silent.getValue() ? "Silent" : "Normal";
   }

   public float computeClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ItemStack itemStack, BlockState blockState, boolean flag) {
      float f = itemStack.getMiningSpeedMultiplier(blockState);
      if (f > 1.0F) {
         int i = EnchantmentUtils.computeItemStack3(itemStack, Enchantments.EFFICIENCY);
         if (i > 0 && !itemStack.isEmpty()) {
            f += i * i + 1;
         }
      }

      if (StatusEffectUtil.hasHaste(clientPlayerEntity)) {
         f *= 1.0F + (StatusEffectUtil.getHasteAmplifier(clientPlayerEntity) + 1) * 0.2F;
      }

      if (clientPlayerEntity.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
         f *= switch (clientPlayerEntity.getStatusEffect(StatusEffects.MINING_FATIGUE).getAmplifier()) {
            case 0 -> 0.3F;
            case 1 -> 0.09F;
            case 2 -> 0.0027F;
            default -> 8.1E-4F;
         };
      }

      f *= (float)clientPlayerEntity.getAttributeValue(EntityAttributes.PLAYER_BLOCK_BREAK_SPEED);
      if (clientPlayerEntity.isSubmergedIn(FluidTags.WATER)) {
         f *= (float)clientPlayerEntity.getAttributeInstance(EntityAttributes.PLAYER_SUBMERGED_MINING_SPEED).getValue();
      }

      if (!flag) {
         f /= 5.0F;
      }

      return Math.max(f, 0.0F);
   }

   public AutoToolModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoTool", Categories.player, "Selects the best mining tool");
      this.handleListener(new AutoToolStartBreakListener(this));
      autoToolModule = this;
   }

   public float computeBlockPos(BlockPos blockPos, BlockState blockState, ClientWorld clientWorld, ClientPlayerEntity clientPlayerEntity, ItemStack itemStack) {
      float f = blockState.getHardness(clientWorld, blockPos);
      if (f == -1.0F) {
         return 0.0F;
      }

      int i = blockState.isToolRequired() && !itemStack.isSuitableFor(blockState) ? 100 : 30;
      return this.computeClientPlayerEntity(clientPlayerEntity, itemStack, blockState, clientPlayerEntity.isOnGround()) / f / i;
   }

   public KeyValue<Slot, Float> resolveKeyValue(BlockPos blockPos, BlockState blockState, ClientWorld clientWorld, ClientPlayerEntity clientPlayerEntity) {
      MutableObject<Slot> mutableObject = new MutableObject(null);
      MutableObject<Float> mutableObject2 = new MutableObject(0.0F);
      this.phobosClient.getInventoryManager().handleConsumer2(itemSwitcher -> itemSwitcher.resolveObject(slot -> {
         if (!this.inventory.getValue() && !itemSwitcher.testSlot4(slot)) {
            return null;
         }

         ItemStack itemStack = slot.getStack();
         float f = this.computeBlockPos(blockPos, blockState, clientWorld, clientPlayerEntity, itemStack);
         if (f > (Float)mutableObject2.getValue()) {
            mutableObject.setValue(slot);
            mutableObject2.setValue(f);
         }

         return null;
      }));
      return KeyValue.createValue((Slot)mutableObject.getValue(), (Float)mutableObject2.getValue());
   }

   public static AutoToolModule getAutoToolModule() {
      return autoToolModule;
   }

   public static boolean check() {
      return autoToolModule.check7() && autoToolModule.silent.getValue();
   }
}
