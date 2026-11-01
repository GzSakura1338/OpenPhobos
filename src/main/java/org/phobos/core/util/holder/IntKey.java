package org.phobos.core.util.holder;

public class IntKey {
   public int intValue;

   @Override
   public int hashCode() {
      int i = 59;
      int i2 = 1;
      return i2 * i + this.intValue;
   }

   @Override
   public boolean equals(Object object) {
      if (object == this) {
         return true;
      } else if (object instanceof IntKey intKey) {
         return !intKey.testObject(this) ? false : this.intValue == intKey.intValue;
      } else {
         return false;
      }
   }

   public IntKey(int i) {
      this.intValue = i;
   }

   public void setIntValue(int i) {
      this.intValue = i;
   }

   public boolean testObject(Object object) {
      return object instanceof IntKey;
   }

   public int getIntValue() {
      return this.intValue;
   }
}
