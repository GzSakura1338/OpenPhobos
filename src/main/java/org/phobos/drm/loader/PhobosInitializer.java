package org.phobos.drm.loader;

import lombok.SneakyThrows;
import net.fabricmc.api.ModInitializer;
import org.phobos.core.PhobosClient;
import org.phobos.core.ClientHolder;

public class PhobosInitializer implements ModInitializer {
   @Override
   public void onInitialize() {
      run();
   }

   @SneakyThrows public static void run() {
      PhobosClient var0;
      label33: {
         Throwable var1;
         label25: {
            try {
               var0 = new PhobosClient();
            } catch (Throwable var4) {
               var1 = var4;
               break label25;
            }

            try {
               ClientHolder.setPhobosClient(var0);
               break label33;
            } catch (Throwable var3) {
               var1 = var3;
            }
         }

         boolean var2 = true;
         if (!var2) {
            throw var1;
         }

         var1.printStackTrace();
         return;
      }

      var0.run();
   }
}
