package org.phobos.core.movement;

import java.util.ArrayDeque;
import java.util.Deque;
import org.phobos.core.Feature;
import org.phobos.core.rotation.LookDirection;

public class SilentMovementManager extends Feature {
   public final Object object = new Object[0];
   public final int intValue3;
   public volatile LookDirection lookDirection = new LookDirection();
   public final Deque<LookDirection> lookDirectionDeque2;

   public int getIntValue3() {
      return this.intValue3;
   }

   public LookDirection getLookDirection() {
      return this.lookDirection;
   }

   public void handleLookDirection(LookDirection lookDirection, Deque<LookDirection> deque) {
      synchronized (this.object) {
         if (deque.size() >= this.getIntValue3()) {
            deque.removeLast();
         }

         deque.addFirst(lookDirection);
      }
   }

   public Deque<LookDirection> getLookDirectionDeque2() {
      return this.lookDirectionDeque2;
   }

   public SilentMovementManager(int i) {
      this.lookDirectionDeque2 = new ArrayDeque<>(i);
      this.intValue3 = i;
   }

   public static class SilentMovement {
      public double doubleValue;
      public final double doubleValue2;
      public final double doubleValue3;
      public double doubleValue4;
      public final double doubleValue5;
      public double doubleValue6;

      public double getDoubleValue() {
         return this.doubleValue;
      }

      public double getDoubleValue4() {
         return this.doubleValue4;
      }

      public double getDoubleValue3() {
         return this.doubleValue3;
      }

      public void setDoubleValue6(double d) {
         this.doubleValue6 = d;
      }

      public double getDoubleValue6() {
         return this.doubleValue6;
      }

      public void setDoubleValue4(double d) {
         this.doubleValue4 = d;
      }

      public double getDoubleValue2() {
         return this.doubleValue2;
      }

      public double getDoubleValue5() {
         return this.doubleValue5;
      }

      public SilentMovement(double d, double d2, double d3) {
         this.doubleValue3 = d;
         this.doubleValue5 = d2;
         this.doubleValue2 = d3;
      }

      public void setDoubleValue(double d) {
         this.doubleValue = d;
      }
   }
}
