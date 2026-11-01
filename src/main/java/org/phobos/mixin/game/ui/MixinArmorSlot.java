package org.phobos.mixin.game.ui;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.ArmorSlot;
import org.phobos.module.player.XCarryModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorSlot.class)
public abstract class MixinArmorSlot {
   @Inject(method = "canInsert", at = @At("HEAD"), cancellable = true)
   private void canInsertHook(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
      if (XCarryModule.check()) {
         cir.setReturnValue(true);
      }
   }
}
