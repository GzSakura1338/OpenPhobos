package org.phobos.core.config;

import org.phobos.type.PUBLICPRIVATEEnum;

public class CloudConfigRef {
   public final String text;
   public final String name;
   public final PUBLICPRIVATEEnum pUBLICPRIVATEEnum;

   public CloudConfigRef(String text, String text2, PUBLICPRIVATEEnum pUBLICPRIVATEEnum) {
      this.name = text;
      this.text = text2;
      this.pUBLICPRIVATEEnum = pUBLICPRIVATEEnum;
   }

   public String getName() {
      return this.name;
   }

   public PUBLICPRIVATEEnum getPUBLICPRIVATEEnum() {
      return this.pUBLICPRIVATEEnum;
   }

   public String getText() {
      return this.text;
   }
}
