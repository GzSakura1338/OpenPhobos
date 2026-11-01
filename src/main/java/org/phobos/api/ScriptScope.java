package org.phobos.api;

import java.util.Map;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.script.ScriptMember;
import org.phobos.core.script.Script;

public interface ScriptScope {
   default void handleVarValue(Script.Var<?> varValue) {
      this.getIntegerMap2().put(this.getIntValue2(), varValue);
      this.getVarValueMap().put(varValue, this.getIntValue2());
      this.getTextMap().put(varValue.getName(), this.getIntValue2());
      this.setIntValue2(this.getIntValue2() + 1);
   }

   Map<Integer, ScriptMember<?>> getIntegerMap();

   @Nullable
   default ScriptMember<?> resolveScriptMember2(String text) {
      Integer integer = this.getTextMap().get(text);
      return integer == null ? null : this.getIntegerMap().get(integer);
   }

   int getIntValue2();

   Map<Integer, Script.Var<?>> getIntegerMap2();

   Map<ScriptMember<?>, Integer> getScriptMemberMap();

   default void handleScriptMember(ScriptMember<?> scriptMember) {
      this.getIntegerMap().put(this.getIntValue2(), scriptMember);
      this.getScriptMemberMap().put(scriptMember, this.getIntValue2());
      this.getTextMap().put(scriptMember.getName(), this.getIntValue2());
      this.setIntValue2(this.getIntValue2() + 1);
   }

   void setIntValue2(int var1);

   Map<String, Integer> getTextMap();

   Map<Script.Var<?>, Integer> getVarValueMap();
}
