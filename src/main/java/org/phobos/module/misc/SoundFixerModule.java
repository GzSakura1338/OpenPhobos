package org.phobos.module.misc;

import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.module.Module;

public class SoundFixerModule extends Module {
   public static SoundFixerModule soundFixerModule;

   public SoundFixerModule(PhobosClient phobosClient) {
      super(phobosClient, "SoundFixer", Categories.misc, "Fixes weird sound behaviour");
      soundFixerModule = this;
   }

   public static boolean check3() {
      return soundFixerModule != null && soundFixerModule.check7();
   }
}
