package org.phobos.core.util.holder;

public class TimestampPair {
   public final long longValue = System.currentTimeMillis();
   public final long longValue2;

   public boolean check() {
      return this.longValue + this.longValue2 < System.currentTimeMillis();
   }

   public TimestampPair(long l) {
      this.longValue2 = l;
   }

   public long getLongValue() {
      return this.longValue;
   }

   public long getLongValue2() {
      return this.longValue2;
   }
}
