package org.phobos.core.hud;

import java.time.Instant;
import java.time.LocalDateTime;
import org.phobos.type.MorningAfternoonEnum;

public class ClockTime {
   public int intValue;
   public int intValue2;
   public int intValue3;
   public int intValue4;
   public int intValue5;
   public Instant instant;
   public MorningAfternoonEnum morningAfternoonEnum;
   public int intValue6;

   public int getIntValue2() {
      return this.intValue2;
   }

   public int getIntValue5() {
      return this.intValue5;
   }

   public int getIntValue3() {
      return this.intValue3;
   }

   public void setIntValue(int i) {
      this.intValue = i;
   }

   public void setIntValue4(int i) {
      this.intValue4 = i;
   }

   public void setMorningAfternoonEnum(MorningAfternoonEnum morningAfternoonEnum) {
      this.morningAfternoonEnum = morningAfternoonEnum;
   }

   public void setIntValue3(int i) {
      this.intValue3 = i;
   }

   public void setIntValue5(int i) {
      this.intValue5 = i;
   }

   public void run() {
      Instant instant = Instant.now();
      LocalDateTime localDateTime = LocalDateTime.now();
      int i = localDateTime.getYear();
      int i2 = localDateTime.getMonthValue();
      int i3 = localDateTime.getDayOfMonth();
      int i4 = localDateTime.getHour();
      int i5 = localDateTime.getMinute();
      int i6 = localDateTime.getSecond();
      this.setInstant(instant);
      this.setMorningAfternoonEnum(MorningAfternoonEnum.of(i4));
      this.setIntValue2(i);
      this.setIntValue(i2);
      this.setIntValue3(i3);
      this.setIntValue4(i4);
      this.setIntValue6(i5);
      this.setIntValue5(i6);
   }

   public int getIntValue6() {
      return this.intValue6;
   }

   public void setIntValue2(int i) {
      this.intValue2 = i;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public int getIntValue4() {
      return this.intValue4;
   }

   public Instant getInstant() {
      return this.instant;
   }

   public void setIntValue6(int i) {
      this.intValue6 = i;
   }

   public MorningAfternoonEnum getMorningAfternoonEnum() {
      return this.morningAfternoonEnum;
   }

   public void setInstant(Instant instant) {
      this.instant = instant;
   }
}
