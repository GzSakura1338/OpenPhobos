package org.phobos.api;

import org.objectweb.asm.tree.ClassNode;

public interface ClassTransformerSpec {
   void handleClassNode(ClassNode var1);

   String[] getTextArray();
}
