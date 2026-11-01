package org.phobos.core.loader;

import org.phobos.api.ClassTransformerSpec;

public abstract class ClassTransformer implements ClassTransformerSpec {
   public final String[] textArray;

   @Override
   public String[] getTextArray() {
      return this.textArray;
   }

   public ClassTransformer(String... texts) {
      this.textArray = texts;
   }
}
