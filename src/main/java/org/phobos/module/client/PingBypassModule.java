package org.phobos.module.client;

import org.phobos.core.server.PingBypassStatus;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.module.Module;

public class PingBypassModule extends Module {
   public static final PingBypassStatus pingBypassStatus = new PingBypassStatus();
   public static PingBypassModule pingBypassModule;

   public PingBypassModule(PhobosClient phobosClient) {
      super(phobosClient, "PingBypass", Categories.client);
   }

   public static PingBypassModule getPingBypassModule() {
      return pingBypassModule;
   }
}
