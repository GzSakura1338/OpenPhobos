package org.phobos.core.script;

import java.util.ArrayList;
import java.util.List;
import org.phobos.core.PhobosClient;

public class ScriptApi {
   public final ScriptLibrary scriptLibrary;
   public final List<ScriptLibrary> scriptLibraryList = new ArrayList<>();
   public final Script script;
   public final ScriptLibrary scriptLibrary2;
   public final PhobosClient phobosClient;

   public ScriptLibrary resolveScriptLibrary(ScriptLibrary scriptLibrary) {
      this.scriptLibraryList.add(scriptLibrary);
      return scriptLibrary;
   }

   public List<ScriptMember<?>> getList() {
      List<ScriptMember<?>> list = new ArrayList<>();

      for (ScriptLibrary scriptLibrary : this.scriptLibraryList) {
         list.addAll(scriptLibrary.getScriptMemberList());
      }

      return list;
   }

   public ScriptApi(PhobosClient phobosClient, Script script) {
      this.phobosClient = phobosClient;
      this.script = script;
      this.scriptLibrary = this.resolveScriptLibrary(new PlayerScriptLibrary(phobosClient, script));
      this.scriptLibrary2 = this.resolveScriptLibrary(new ClientScriptLibrary(phobosClient, script));
   }

   public ScriptApi(PhobosClient phobosClient, Script script, ScriptLibrary scriptLibrary, ScriptLibrary scriptLibrary2) {
      this.phobosClient = phobosClient;
      this.script = script;
      this.scriptLibrary = scriptLibrary;
      this.scriptLibrary2 = scriptLibrary2;
   }
}
