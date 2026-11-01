package org.phobos.mixin.game.input;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.core.inventory.InventoryManager;
import org.phobos.core.PhobosLog;
import org.phobos.event.SetStackEvent;
import org.phobos.module.player.SilentHotbarModule;
import org.phobos.module.player.VirtualHotbarModule;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public abstract class MixinPlayerInventory implements EventSubscriber {
   @Shadow
   @Final
   public PlayerEntity player;
   @Shadow
   @Final
   private List<DefaultedList<ItemStack>> combinedInventory;

   @Redirect(method = "swapSlotWithHotbar", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerInventory;selectedSlot:I", opcode = 181))
   private void swapSlotWithHotbarHook(PlayerInventory instance, int value) {
      InventoryManager.handleLock(InventoryManager.reentrantLock, () -> instance.selectedSlot = value);
   }

   @ModifyExpressionValue(
      method = {"dropSelectedItem", "getBlockBreakingSpeed", "getMainHandStack"},
      at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerInventory;selectedSlot:I")
   )
   private int overrideSlot(int original) {
      return ((PlayerInventory)(Object)this).player == MinecraftClient.getInstance().player ? SilentHotbarModule.getSilentHotbarManager().value() : original;
   }

   @Redirect(method = "addPickBlock", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerInventory;selectedSlot:I", opcode = 181))
   private void addPickBlockHook(PlayerInventory instance, int value) {
      InventoryManager.handleLock(InventoryManager.reentrantLock, () -> instance.selectedSlot = value);
   }

   @Inject(method = "scrollInHotbar", at = @At("HEAD"), cancellable = true)
   private void scrollInHotbar(double scrollAmount, CallbackInfo ci) {
      int i = (int)Math.signum(scrollAmount);
      if (VirtualHotbarModule.getVirtualHotbarModule().testInt(i)) {
         ci.cancel();
      }
   }

   @Inject(method = "setStack", at = @At("RETURN"))
   private void setStackHook(int slot, ItemStack stack, CallbackInfo ci) {
      this.invoke(new SetStackEvent(this.player, slot, stack));
   }

   @Overwrite
   @SuppressWarnings("overwrite")
   public void removeOne(ItemStack stack) {
      for (DefaultedList<ItemStack> itemStacks : this.combinedInventory) {
         for (int i = 0; i < itemStacks.size(); i++) {
            try {
               if (itemStacks.get(i) == stack) {
                  itemStacks.set(i, ItemStack.EMPTY);
                  break;
               }
            } catch (ArrayIndexOutOfBoundsException exception) {
               PhobosLog.handleText("Minecraft", "Caught generic minecraft exception while iterating over inventory stack");
            }
         }
      }
   }
}
