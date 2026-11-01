package org.phobos.core.script;

import java.util.ArrayList;
import java.util.List;
import org.phobos.core.PhobosClient;

public abstract class ScriptLibrary {
   public final List<ScriptMember<?>> scriptMemberList = new ArrayList<>();
   public final String name;
   public final Script script;
   public final PhobosClient phobosClient;

   public String getName() {
      return this.name;
   }

   public ScriptLibrary(String text, PhobosClient phobosClient, Script script) {
      this.name = text;
      this.phobosClient = phobosClient;
      this.script = script;
   }

   public List<ScriptMember<?>> getScriptMemberList() {
      return this.scriptMemberList;
   }

   public void handleScriptMember(ScriptMember<?> scriptMember) {
      this.scriptMemberList.add(scriptMember);
   }

   public Script getScript() {
      return this.script;
   }

   public void handleText(String text, Class<?> classValue, Script.Arg<?>... args) {
      this.scriptMemberList.add(new ScriptMember<>(null, this.name + "." + text, classValue, args));
   }

   public PhobosClient getPhobosClient() {
      return this.phobosClient;
   }
}
