package org.phobos.module.misc;

import org.phobos.core.movement.MountBypassHandler;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.module.Module;

public class MountBypassModule extends Module {
   public MountBypassModule(PhobosClient phobosClient) {
      super(phobosClient, "MountBypass", Categories.misc);
      this.handleListenerHolder(new MountBypassHandler(() -> true));
   }
}
