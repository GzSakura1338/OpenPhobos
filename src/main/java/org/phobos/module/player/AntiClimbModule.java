package org.phobos.module.player;

import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.module.Module;

public class AntiClimbModule extends Module {
   public static AntiClimbModule antiClimbModule;

   public AntiClimbModule(PhobosClient phobosClient) {
      super(phobosClient, "AntiClimb", Categories.player, "Prevents you from climbing ladders and others");
      antiClimbModule = this;
   }

   public static boolean check() {
      return antiClimbModule.check7();
   }
}
