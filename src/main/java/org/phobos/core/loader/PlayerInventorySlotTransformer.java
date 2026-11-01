package org.phobos.core.loader;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

public class PlayerInventorySlotTransformer extends ClassTransformer {
   public PlayerInventorySlotTransformer() {
      super("net/minecraft/class_1661", "net/minecraft/entity/player/PlayerInventory");
   }

   @Override
   public void handleClassNode(ClassNode classNode) {
      try {
         AsmUtils.handleClassNode(classNode, Type.INT_TYPE, "field_7545", "selected", "selectedSlot");
      } catch (Throwable throwable) {
         AsmUtils.handleThrowable(throwable);
      }
   }
}
