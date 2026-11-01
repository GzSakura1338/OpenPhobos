package org.phobos.api;

import org.phobos.core.loader.PlayerInventorySlotTransformer;
import org.phobos.core.loader.InteractionManagerSlotTransformer;
import org.phobos.core.PhobosLog;

public interface ClassTransformerRegistry {
   void handleClassTransformerSpec(ClassTransformerSpec var1);

   default void run() {
      PhobosLog.handleText("PCT", "Initializing");
      this.handleClassTransformerSpec(new InteractionManagerSlotTransformer());
      this.handleClassTransformerSpec(new PlayerInventorySlotTransformer());
   }
}
