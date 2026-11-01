package org.phobos.api;

import org.phobos.core.setting.Setting;
import org.phobos.core.tick.TimeUtils;

public interface TimeGate {
   void setLongValue(long var1);

   default boolean testNumber(Number number) {
      return TimeUtils.computeLong(this.getLongValue()) >= number.longValue();
   }

   long getLongValue();

   default boolean testLong(long l) {
      return TimeUtils.computeLong(this.getLongValue()) >= l;
   }

   default boolean testSetting(Setting<NumberRange.Mutable<Long>> setting) {
      boolean flag = TimeUtils.computeLong(this.getLongValue()) >= setting.getValue().getNumber();
      if (flag) {
         setting.getValue().run6();
         this.run();
      }

      return flag;
   }

   default void run() {
      this.setLongValue(TimeUtils.value());
   }

   default boolean testNumber2(Number number) {
      boolean flag = TimeUtils.computeLong(this.getLongValue()) >= number.longValue();
      if (flag) {
         this.run();
      }

      return flag;
   }

   default boolean testSetting2(Setting<NumberRange.Mutable<Long>> setting) {
      return TimeUtils.computeLong(this.getLongValue()) >= setting.getValue().getNumber();
   }

   default long value() {
      return TimeUtils.computeLong(this.getLongValue());
   }

   default boolean testLong2(long l) {
      boolean flag = TimeUtils.computeLong(this.getLongValue()) >= l;
      if (flag) {
         this.run();
      }

      return flag;
   }

   class Normal implements TimeGate {
      public long longValue;

      @Override
      public void setLongValue(long l) {
         this.longValue = l;
      }

      @Override
      public long getLongValue() {
         return this.longValue;
      }
   }

   class Threaded implements TimeGate {
      public volatile long longValue;

      @Override
      public void setLongValue(long l) {
         this.longValue = l;
      }

      @Override
      public long getLongValue() {
         return this.longValue;
      }
   }
}
