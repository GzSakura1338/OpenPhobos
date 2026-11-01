package org.phobos.api;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.script.ScriptHook;
import org.phobos.core.script.ScriptTypeRef;
import org.phobos.core.script.ScriptMember;
import org.phobos.core.script.Script;

public interface ScriptObject {
   @Nullable
   ScriptMember<?> resolveScriptMember(String var1);

   void setCompleted(boolean var1);

   boolean isCompleted();

   List<ScriptHook> getScriptHookList();

   List<ScriptMember<?>> getScriptMemberList();

   List<ScriptTypeRef> getScriptTypeRefList();

   List<Script.Var<?>> getVarValueList();

   default void handleScriptHook2(ScriptHook scriptHook, Object... objects) {
      try {
         scriptHook.handleScriptObject2(this, objects);
      } catch (Exception exception) {
         exception.printStackTrace();
      }
   }
}
