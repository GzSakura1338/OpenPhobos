package org.phobos.core.loader;

import org.phobos.core.PhobosClient;

public class ClassLoaderHolder {
   public static DefiningClassLoader definingClassLoader;

   public static DefiningClassLoader getDefiningClassLoader() {
      return definingClassLoader;
   }

   public static void run() {
      definingClassLoader = new DefiningClassLoader(PhobosClient.class.getClassLoader());
   }
}
