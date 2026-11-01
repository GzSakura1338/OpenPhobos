package org.phobos.core.script;

import org.phobos.core.PhobosClient;

public class MemoryScriptLibrary extends ScriptLibrary {
   public MemoryScriptLibrary(PhobosClient phobosClient, Script script) {
      super("memory", phobosClient, script);
   }
}
