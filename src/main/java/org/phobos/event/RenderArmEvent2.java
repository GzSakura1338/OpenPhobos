package org.phobos.event;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import org.phobos.core.event.events.CancellableEvent;

public class RenderArmEvent2 extends CancellableEvent {
   public final MatrixStack matrixStack;
   public final Arm arm;

   public Arm getArm() {
      return this.arm;
   }

   public RenderArmEvent2(MatrixStack matrixStack, Arm arm) {
      this.matrixStack = matrixStack;
      this.arm = arm;
   }

   public MatrixStack getMatrixStack() {
      return this.matrixStack;
   }
}
