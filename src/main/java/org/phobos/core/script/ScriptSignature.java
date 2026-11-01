package org.phobos.core.script;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ScriptSignature {
   public final String name;
   public final Script.Var<?>[] varValueArray;
   public final Script.Arg<?>[] argArray;
   public final List<ScriptMember<?>> scriptMemberList = new CopyOnWriteArrayList<>();

   public Script.Var<?>[] getVarValueArray() {
      return this.varValueArray;
   }

   public List<ScriptMember<?>> getScriptMemberList() {
      return this.scriptMemberList;
   }

   public ScriptSignature(String text, Script.Arg<?>[] args, Script.Var<?>[] varValues) {
      this.name = text;
      this.argArray = args;
      this.varValueArray = varValues;
   }

   public String getName() {
      return this.name;
   }

   public Script.Arg<?>[] getArgArray() {
      return this.argArray;
   }
}
