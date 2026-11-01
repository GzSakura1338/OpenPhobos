package org.phobos.core;

import org.phobos.core.event.EventBus;

public class ClientHolder {
   public static boolean flag = true;
   public static boolean flag2;
   public static PhobosClient phobosClient;

   public static PhobosClient getPhobosClient() {
      return phobosClient;
   }

   public static void setPhobosClient(PhobosClient phobosClient) {
      ClientHolder.phobosClient = phobosClient;
   }

   public static EventBus getEventBus() {
      return phobosClient.getEventBus();
   }
}
