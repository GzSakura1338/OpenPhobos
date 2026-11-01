package org.phobos.event;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import org.phobos.core.event.events.CancellableEvent;

public class ApplyEatOrDrinkTransformationEvent extends CancellableEvent {
   public final ItemStack itemStack;
   public final float floatValue;
   public final Arm arm;
   public final MatrixStack matrixStack;

   public float getFloatValue2() {
      return this.floatValue;
   }

   public Arm getArm() {
      return this.arm;
   }

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   public ApplyEatOrDrinkTransformationEvent(MatrixStack matrixStack, float f, Arm arm, ItemStack itemStack) {
      this.matrixStack = matrixStack;
      this.floatValue = f;
      this.arm = arm;
      this.itemStack = itemStack;
   }

   public MatrixStack getMatrixStack() {
      return this.matrixStack;
   }
}
