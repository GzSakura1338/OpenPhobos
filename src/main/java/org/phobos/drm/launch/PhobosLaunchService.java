package org.phobos.drm.launch;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.phobos.api.ClassTransformerRegistry;
import org.phobos.core.loader.MixinConfigRegistrar;
import org.phobos.core.system.OsDetector;

public class PhobosLaunchService implements PreLaunchEntrypoint {
   public final ClassTransformerRegistry classTransformerRegistry = MixinConfigRegistrar.getINSTANCE().getClassTransformerRegistry();

   public void onPreLaunch() {
      System.setProperty("java.awt.headless", "false");
      OsDetector.getINSTANCE().run();
      this.classTransformerRegistry.run();
   }
}
