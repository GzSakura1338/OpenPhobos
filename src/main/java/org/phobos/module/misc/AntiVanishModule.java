package org.phobos.module.misc;

import org.phobos.core.module.ModuleGroups;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.AntiVanishTickListener;
import org.phobos.module.Module;

public class AntiVanishModule extends Module {
   public AntiVanishModule(PhobosClient phobosClient) {
      super(phobosClient, "AntiVanish", Categories.misc, "Makes invisible entities visible");
      this.handleListener(new AntiVanishTickListener(this));
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }
}
