package org.phobos.core.config;

import org.phobos.type.PUBLICPRIVATEEnum;

public class CloudProxyRef {
   public final PUBLICPRIVATEEnum pUBLICPRIVATEEnum;
   public final String name;
   public final String text;

   public String getText() {
      return this.text;
   }

   public PUBLICPRIVATEEnum getPUBLICPRIVATEEnum() {
      return this.pUBLICPRIVATEEnum;
   }

   public CloudProxyRef(String text, String text2, PUBLICPRIVATEEnum pUBLICPRIVATEEnum) {
      this.name = text;
      this.text = text2;
      this.pUBLICPRIVATEEnum = pUBLICPRIVATEEnum;
   }

   public String getName() {
      return this.name;
   }
}
