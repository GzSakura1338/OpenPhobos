package org.phobos.mixin.game.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.earthhack.ducks.ISlot;
import org.earthhack.mixin.EventSubscriber;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.render.Highlights;
import org.phobos.core.kit.GhostItems;
import org.phobos.core.kit.GhostItems;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.inventory.InventoryUtils;
import org.phobos.event.OnMouseClickEvent;
import org.phobos.module.misc.TooltipsModule;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public abstract class MixinHandledScreen<T extends ScreenHandler> implements EventSubscriber {
   @Shadow
   protected int x;
   @Shadow
   protected int backgroundWidth;
   @Shadow
   @Nullable
   protected Slot focusedSlot;
   @Shadow
   protected int y;
   @Unique
   private final Color color = new Color(1.0F, 1.0F, 1.0F, 0.2F);
   @Shadow
   @Final
   protected T handler;

   @Shadow
   public abstract T getScreenHandler();

   @Redirect(
      method = "render",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawSlot(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/screen/slot/Slot;)V"
      )
   )
   private void renderHook(HandledScreen<T> instance, DrawContext context, Slot slot) {
      instance.drawSlot(context, slot);
      Color color = Highlights.resolveColor(slot);
      if (color != null) {
         int x = slot.x;
         int y = slot.y;
         int width = 16;
         int height = 16;
         context.fill(x, y, x + width, y + height, color.hashCode());
      }

      if (slot.getStack().isEmpty()) {
         GhostItems.GhostItem ghostItem = GhostItems.resolveGhostItem(slot);
         if (ghostItem != null) {
            Item item = ghostItem.getItem();
            int x = slot.x;
            int y = slot.y;
            ItemStack stack = new ItemStack(item);
            RenderSystem.enableBlend();
            RenderUtils.handleDrawContext(context, stack, x, y, ColorUtils.createColor6(Color.WHITE, ghostItem.getFloatAnimator().getFloatValue2()));
            RenderSystem.disableBlend();
         }
      }
   }

   @Inject(method = "onMouseClick(Lnet/minecraft/screen/slot/Slot;IILnet/minecraft/screen/slot/SlotActionType;)V", at = @At("HEAD"))
   public void onMouseClickHook(Slot slot, int slotId, int button, SlotActionType actionType, CallbackInfo ci) {
      this.invoke(new OnMouseClickEvent(slot, slotId, button, actionType), ci);
   }

   @Inject(
      method = "drawSlot",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/DrawContext;drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
         shift = Shift.AFTER
      )
   )
   private void drawSlotHook1(DrawContext context, Slot slot, CallbackInfo ci) {
      if (TooltipsModule.check()) {
         ItemStack stack = slot.getStack();
         if (InventoryUtils.testItemStack3(stack)) {
            int x = slot.x;
            int y = slot.y;
            int seed = slot.x + slot.y * this.backgroundWidth;
            ItemStack[] stacks = new ItemStack[27];
            InventoryUtils.handleItemStack(stack, stacks);
            ItemStack result = this.analyze(stacks);
            if (result != null) {
               result.setCount(1);
               float scale = 0.69F;
               context.getMatrices().push();
               context.getMatrices().scale(scale, scale, 1.0F);
               context.getMatrices().translate(x / scale + 7.0F, y / scale, 15.0F);
               context.drawItem(result, 0, 0, seed);
               context.getMatrices().pop();
            }
         }
      }
   }

   @Inject(method = "render", at = @At("HEAD"))
   private void renderHook(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      Highlights.run();
   }

   @Inject(method = "drawSlot", at = @At("HEAD"))
   private void drawSlotHook2(DrawContext context, Slot slot, CallbackInfo ci) {
      ISlot access = (ISlot)(Object)slot;
      float renderX = this.x + slot.x + 8.0F;
      float renderY = this.y + slot.y + 8.0F;
      access.setLastRenderX(renderX);
      access.setLastRenderY(renderY);
      access.setLoaded(true);
   }

   @Unique
   @Nullable
   private ItemStack analyze(ItemStack... stacks) {
      Item item = null;
      ItemStack itemStack = null;

      for (ItemStack stack : stacks) {
         if (!stack.isEmpty()) {
            if (item == null) {
               item = stack.getItem();
               itemStack = stack;
            } else {
               Item stackItem = stack.getItem();
               if (!(
                     item instanceof ArmorItem armor1
                        && stackItem instanceof ArmorItem armor2
                        && ((ArmorMaterial)armor1.getMaterial().value()).equals(armor2.getMaterial().value())
                  )
                  && !stack.isOf(item)) {
                  return null;
               }
            }
         }
      }

      return itemStack;
   }

   @Inject(method = "isPointOverSlot", at = @At("HEAD"), cancellable = true)
   private void isPointOverSlotHook(Slot slot, double pointX, double pointY, CallbackInfoReturnable<Boolean> cir) {
   }
}
