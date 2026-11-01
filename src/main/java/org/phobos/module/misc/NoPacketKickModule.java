package org.phobos.module.misc;

import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.CancelListener;
import org.phobos.event.OnPacketExceptionEvent;
import org.phobos.module.Module;

public class NoPacketKickModule extends Module {
   public NoPacketKickModule(PhobosClient phobosClient) {
      super(phobosClient, "NoPacketKick", Categories.misc, "Prevents kicking on non-critical packet errors");
      this.handleListener(new CancelListener<>(OnPacketExceptionEvent.class));
   }
}
