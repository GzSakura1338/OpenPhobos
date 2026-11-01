package org.phobos.core.loader;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

public class InteractionManagerSlotTransformer extends ClassTransformer {
   @Override
   public void handleClassNode(ClassNode classNode) {
      try {
         AsmUtils.handleClassNode(classNode, Type.INT_TYPE, "field_3721", "carriedIndex", "lastSelectedSlot");
      } catch (Throwable throwable) {
         AsmUtils.handleThrowable(throwable);
      }
   }

   public InteractionManagerSlotTransformer() {
      super("net/minecraft/class_636", "net/minecraft/client/network/ClientPlayerInteractionManager");
   }
}
