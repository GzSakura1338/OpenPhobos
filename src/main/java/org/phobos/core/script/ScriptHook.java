package org.phobos.core.script;

import java.util.function.Supplier;
import org.phobos.api.ScriptObject;

public abstract class ScriptHook {
   public Supplier<Boolean> booleanValueSupplier = () -> true;
   public final String name;

   public Supplier<Boolean> getBooleanValueSupplier() {
      return this.booleanValueSupplier;
   }

   @Override
   public String toString() {
      return this.name;
   }

   public ScriptHook(String text, Supplier<Boolean> supplier) {
      this.name = text;
      this.booleanValueSupplier = supplier;
   }

   public abstract void handleScriptObject(ScriptObject var1, Object... var2);

   public void setBooleanValueSupplier(Supplier<Boolean> supplier) {
      this.booleanValueSupplier = supplier;
   }

   public String getName() {
      return this.name;
   }

   public void handleScriptObject2(ScriptObject scriptObject, Object... objects) {
      this.handleScriptObject(scriptObject, objects);
   }

   public ScriptHook(String text) {
      this.name = text;
   }
}
