package org.phobos.core.gui;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import net.minecraft.client.gui.DrawContext;

public class ClipStack {
   public final Deque<ClipBounds> clipBoundsDeque = new ArrayDeque<>();

   public void handleDrawContext(DrawContext drawContext) {
      for (ClipBounds clipBounds : this.clipBoundsDeque) {
         drawContext.disableScissor();
      }
   }

   public void handleDrawContext2(DrawContext drawContext) {
      for (ClipBounds clipBounds : this.clipBoundsDeque) {
         int[] ints = clipBounds.values2();
         drawContext.enableScissor(ints[0], ints[1], ints[2], ints[3]);
      }
   }

   public void clear() {
      this.clipBoundsDeque.clear();
   }

   public boolean isEmpty() {
      return this.clipBoundsDeque.isEmpty();
   }

   public double[] values() {
      ClipBounds clipBounds = this.clipBoundsDeque.peekLast();
      return clipBounds == null
         ? new double[0]
         : new double[]{
            clipBounds.getDoubleValue22(),
            clipBounds.getDoubleValue42(),
            clipBounds.getDoubleValue22() + clipBounds.getDoubleValue3(),
            clipBounds.getDoubleValue42() + clipBounds.getDoubleValue()
         };
   }

   public ClipBounds getClipBounds() {
      if (this.clipBoundsDeque.isEmpty()) {
         return null;
      }

      this.clipBoundsDeque.removeLast();
      return this.clipBoundsDeque.peekLast();
   }

   public ClipBounds resolveClipBounds(ClipBounds clipBounds) {
      ClipBounds clipBounds3 = this.clipBoundsDeque.peekLast();
      if (clipBounds3 != null) {
         ClipBounds clipBounds2 = Objects.requireNonNullElse(clipBounds.resolveClipBounds(clipBounds3), ClipBounds.INSTANCE);
         this.clipBoundsDeque.addLast(clipBounds2);
         return clipBounds2;
      } else {
         this.clipBoundsDeque.addLast(clipBounds);
         return clipBounds;
      }
   }

   public ClipStack getClipStack() {
      ClipStack clipStack = new ClipStack();
      clipStack.clipBoundsDeque.addAll(this.clipBoundsDeque);
      return clipStack;
   }
}
