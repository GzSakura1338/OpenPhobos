package org.phobos.core.config;

import org.phobos.type.PUBLICPRIVATEEnum;

public class ConfigMeta {
   public final String text;
   public final String name;
   public final boolean flag;
   public final boolean flag2;
   public final PUBLICPRIVATEEnum pUBLICPRIVATEEnum;
   public int intValue;

   @Override
   public String toString() {
      return "{" + String.join(", ", this.name, this.text, this.pUBLICPRIVATEEnum.toString(), String.valueOf(this.flag), String.valueOf(this.intValue)) + "}";
   }

   public boolean isFlag2() {
      return this.flag2;
   }

   public ConfigMeta(String text, String text2, PUBLICPRIVATEEnum pUBLICPRIVATEEnum, boolean flag, boolean flag2, int i) {
      this.name = text;
      this.text = text2;
      this.pUBLICPRIVATEEnum = pUBLICPRIVATEEnum;
      this.flag = flag;
      this.flag2 = flag2;
      this.intValue = i;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public boolean check() {
      return this.pUBLICPRIVATEEnum != PUBLICPRIVATEEnum.PUBLIC;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public String getText() {
      return this.text;
   }

   public PUBLICPRIVATEEnum getPUBLICPRIVATEEnum() {
      return this.pUBLICPRIVATEEnum;
   }

   public void setIntValue(int i) {
      this.intValue = i;
   }

   public void run() {
      this.intValue++;
   }

   public String getName() {
      return this.name;
   }
}
