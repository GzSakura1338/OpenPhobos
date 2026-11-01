package org.phobos.mixin.game.input;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.core.event.events.SlotClickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHandler.class)
public abstract class MixinScreenHandler implements EventSubscriber {
   @Inject(method = "internalOnSlotClick", at = @At("HEAD"), cancellable = true)
   private void internalOnSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
      this.invoke(new SlotClickEvent(slotIndex, button, actionType), ci);
   }
}
