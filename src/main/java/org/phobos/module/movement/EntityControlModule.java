package org.phobos.module.movement;

import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.module.Module;

public class EntityControlModule extends Module {
   public static EntityControlModule entityControlModule;

   public EntityControlModule(PhobosClient phobosClient) {
      super(phobosClient, "EntityControl", Categories.movement, "Control riding entity without a saddle");
      entityControlModule = this;
   }

   public static boolean check2() {
      return entityControlModule.check7();
   }
}
