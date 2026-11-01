package org.phobos.api;

import org.jetbrains.annotations.ApiStatus.OverrideOnly;

public interface Toggleable {
   static boolean testObject(Object object, boolean flag) {
      return object instanceof Toggleable toggleable ? toggleable.check7() : flag;
   }

   @OverrideOnly
   default void run3() {
   }

   @OverrideOnly
   default void run4() {
   }

   boolean check7();
}
