package org.phobos.module.render;

import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.module.Module;

public class FullbrightModule extends Module {
   public static FullbrightModule fullbrightModule;
   public static final Logger LOGGER = LoggerFactory.getLogger(FullbrightModule.class);

   public FullbrightModule(PhobosClient phobosClient) {
      super(phobosClient, "Fullbright", Categories.render, "Removes shadows");
      fullbrightModule = this;
   }

   public static boolean check() {
      return fullbrightModule != null && fullbrightModule.check7();
   }
}
