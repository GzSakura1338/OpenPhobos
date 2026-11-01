package org.phobos.module.misc;

import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.SpooferSendCustomPayloadListener;
import org.phobos.module.Module;

public class SpooferModule extends Module {
   public SpooferModule(PhobosClient phobosClient) {
      super(phobosClient, "Spoofer", Categories.misc, "Spoofs your client brand");
      this.handleListener(new SpooferSendCustomPayloadListener(this));
   }
}
