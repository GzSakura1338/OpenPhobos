package org.phobos.api;

public interface TickGate {
   default void run() {
      this.setIntValue(this.getIntValue() + 1);
   }

   default boolean testInt(int i) {
      return this.getIntValue() >= i;
   }

   default boolean check() {
      boolean flag = this.getIntValue() <= 0;
      if (flag) {
         return true;
      }

      this.run2();
      return false;
   }

   default boolean check2() {
      return this.getIntValue() <= 0;
   }

   default boolean testInt2(int i) {
      boolean flag = this.testInt(i);
      if (flag) {
         this.run3();
      }

      return flag;
   }

   void setIntValue(int var1);

   default void run2() {
      this.setIntValue(this.getIntValue() - 1);
   }

   default void run3() {
      this.setIntValue(0);
   }

   int getIntValue();

   default boolean testInt3(int i) {
      boolean flag = this.testInt(i);
      if (flag) {
         this.run3();
      } else {
         this.run();
      }

      return flag;
   }

   class Normal implements TickGate {
      public int intValue;

      @Override
      public void setIntValue(int i) {
         this.intValue = i;
      }

      @Override
      public int getIntValue() {
         return this.intValue;
      }
   }

   class Threaded implements TickGate {
      public volatile int intValue;

      @Override
      public void setIntValue(int i) {
         this.intValue = i;
      }

      @Override
      public int getIntValue() {
         return this.intValue;
      }
   }
}
