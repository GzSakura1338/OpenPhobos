package org.phobos.core.loader;

import java.util.Arrays;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

public final class AsmUtils {
   public static void handleClassNode(ClassNode classNode, Type type, String... texts) throws RuntimeException {
      String text = null;

      for (FieldNode fieldNode : classNode.fields) {
         if (Type.getType(fieldNode.desc).equals(type) && Arrays.asList(texts).contains(fieldNode.name)) {
            if (text != null) {
               throw new RuntimeException("Found both field " + text + " and " + fieldNode.name + " in class " + classNode.name);
            }

            text = fieldNode.name;
            fieldNode.access |= 64;
         }
      }

      if (text == null) {
         throw new RuntimeException("Failed to find any of " + Arrays.toString(texts) + " in " + classNode.name);
      }
   }

   public static void handleThrowable(Throwable throwable) {
      throwable.printStackTrace();
   }

   public AsmUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
