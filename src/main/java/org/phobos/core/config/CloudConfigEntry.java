package org.phobos.core.config;

import org.phobos.api.ConfigInfo;
import org.phobos.type.PUBLICPRIVATEEnum;
import org.phobos.core.PhobosClient;

public class CloudConfigEntry implements ConfigInfo {
   public final PUBLICPRIVATEEnum pUBLICPRIVATEEnum;
   public final String text;
   public final boolean flag;
   public final String text2 = null;
   public final PhobosClient phobosClient;
   public final String name;
   public final BuildMetadata buildMetadata;

   public PhobosClient getPhobosClient() {
      return this.phobosClient;
   }

   @Override
   public boolean isFlag2() {
      return false;
   }

   @Override
   public BuildMetadata getBuildMetadata() {
      return this.buildMetadata;
   }

   @Override
   public PUBLICPRIVATEEnum getPUBLICPRIVATEEnum() {
      return this.pUBLICPRIVATEEnum;
   }

   @Override
   public String getText() {
      return this.text;
   }

   @Override
   public boolean isFlag() {
      return false;
   }

   @Override
   public String getText2() {
      return this.text2;
   }

   public CloudConfigEntry(PhobosClient phobosClient, String text, String text2, BuildMetadata buildMetadata) {
      this.flag = false;
      this.pUBLICPRIVATEEnum = PUBLICPRIVATEEnum.PUBLIC;
      this.phobosClient = phobosClient;
      this.name = text;
      this.text = text2;
      this.buildMetadata = buildMetadata;
   }

   @Override
   public String getName() {
      return this.name;
   }
}
