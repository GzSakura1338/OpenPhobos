package org.phobos.api;

public interface Named {
   static String describeObject2(Object object, String text) {
      return object instanceof Named named ? named.getName() : text;
   }

   default int computeNamed(Named named) {
      return String.CASE_INSENSITIVE_ORDER.compare(this.getName(), named.getName());
   }

   default String describe3() {
      return this.getName().toLowerCase();
   }

   String getName();
}
