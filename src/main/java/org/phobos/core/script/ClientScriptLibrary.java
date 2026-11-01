package org.phobos.core.script;

import java.util.List;
import org.phobos.core.PhobosClient;

public class ClientScriptLibrary extends ScriptLibrary {
   public void handleObject(Object object) {
      this.phobosClient.handleText5("Output", "[" + this.script.getName() + "] " + object);
   }

   public ClientScriptLibrary(PhobosClient phobosClient, Script script) {
      super("client", phobosClient, script);
      List<ScriptMember<?>> list = ScriptMember.resolveList(this, "client", this.getClass());
      list.forEach(this::handleScriptMember);
   }

   public void handleDouble(double d, double d2) {
      this.phobosClient.getRotationManager().testFloat((float)d, (float)d2);
   }
}
