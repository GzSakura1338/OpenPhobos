package org.phobos.api;

import org.phobos.core.config.BuildMetadata;
import org.phobos.core.ClientHolder;
import org.phobos.type.PUBLICPRIVATEEnum;

public interface ConfigInfo extends Named {
   BuildMetadata getBuildMetadata();

   boolean isFlag2();

   default void run() {
      String text = this.getText();
      if (text != null) {
         ClientHolder.getPhobosClient().getConfigManager().handleText(this.getName(), text);
      }
   }

   boolean isFlag();

   default String describe() {
      return "name:%s\nauthor:%s\ndata:<hidden>\ncloud:%s\npinned:%s\nvisibility:%s\nheader:<hidden>"
         .formatted(this.getName(), this.getText2(), this.isFlag2(), this.isFlag(), this.getPUBLICPRIVATEEnum());
   }

   String getText2();

   String getText();

   default boolean check() {
      return this.getPUBLICPRIVATEEnum() != PUBLICPRIVATEEnum.PUBLIC;
   }

   PUBLICPRIVATEEnum getPUBLICPRIVATEEnum();
}
