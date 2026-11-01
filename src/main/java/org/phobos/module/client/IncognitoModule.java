package org.phobos.module.client;

import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.module.Module;

public class IncognitoModule extends Module {
   public static IncognitoModule incognitoModule;

   public static IncognitoModule getIncognitoModule() {
      return incognitoModule;
   }

   public IncognitoModule(PhobosClient phobosClient) {
      super(phobosClient, "Incognito", Categories.client, "Disables shared features");
      incognitoModule = this;
   }
}
