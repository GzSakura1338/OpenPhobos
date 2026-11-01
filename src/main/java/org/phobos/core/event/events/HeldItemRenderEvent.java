package org.phobos.core.event.events;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class HeldItemRenderEvent extends CancellableEvent {
   public float floatValue;
   public final MatrixStack matrixStack;
   public final ItemStack itemStack;
   public final Hand hand;

   public Hand getHand() {
      return this.hand;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public HeldItemRenderEvent(Hand hand, ItemStack itemStack, float f, MatrixStack matrixStack) {
      this.hand = hand;
      this.itemStack = itemStack;
      this.floatValue = f;
      this.matrixStack = matrixStack;
   }

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   public MatrixStack getMatrixStack() {
      return this.matrixStack;
   }
}
