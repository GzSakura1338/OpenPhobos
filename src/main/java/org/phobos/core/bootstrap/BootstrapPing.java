package org.phobos.core.bootstrap;

import org.phobos.api.TimeGate;
import org.phobos.core.ClientHolder;

public class BootstrapPing {
   public static volatile boolean flag = false;
   public static final TimeGate.Normal normal = new TimeGate.Normal();

   public static void run() {
      normal.run();
      Thread thread = new Thread(() -> {
         while (!flag) {
            if (normal.testLong2(3000L)) {
               BootstrapChallenge.handlePacketBus(ClientHolder.getPhobosClient().getPacketBus());
            }
         }
      });
      thread.setDaemon(true);
      thread.start();
   }

   public static void setFlag(boolean flag) {
      BootstrapPing.flag = flag;
   }
}
