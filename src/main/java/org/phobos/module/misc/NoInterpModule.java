package org.phobos.module.misc;

import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.NoInterpTrackedPositionUpdateListener;
import org.phobos.module.Module;

public class NoInterpModule extends Module {
   public NoInterpModule(PhobosClient phobosClient) {
      super(phobosClient, "NoInterp", Categories.misc, "Removes render interpolation");
      this.handleListener(new NoInterpTrackedPositionUpdateListener(this));
   }
}
