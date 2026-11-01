package org.phobos.core.config;

import org.phobos.api.ConfigInfo;
import org.phobos.type.PUBLICPRIVATEEnum;

public class CloudConfigInfo implements ConfigInfo {
   public final String text;
   public final BuildMetadata buildMetadata;
   public final boolean flag;
   public final String text2;
   public final String name;
   public final boolean flag2;
   public final PUBLICPRIVATEEnum pUBLICPRIVATEEnum;

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public String getText() {
      return this.text2;
   }

   @Override
   public BuildMetadata getBuildMetadata() {
      return this.buildMetadata;
   }

   @Override
   public String getText2() {
      return this.text;
   }

   @Override
   public boolean isFlag2() {
      return this.flag2;
   }

   @Override
   public boolean isFlag() {
      return this.flag;
   }

   @Override
   public PUBLICPRIVATEEnum getPUBLICPRIVATEEnum() {
      return this.pUBLICPRIVATEEnum;
   }

   public CloudConfigInfo(String text, String text2, String text3, boolean flag, boolean flag2, PUBLICPRIVATEEnum pUBLICPRIVATEEnum, BuildMetadata buildMetadata) {
      this.name = text;
      this.text = text2;
      this.text2 = text3;
      this.flag2 = flag;
      this.flag = flag2;
      this.pUBLICPRIVATEEnum = pUBLICPRIVATEEnum;
      this.buildMetadata = buildMetadata;
   }
}
