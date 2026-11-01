package org.phobos.api;

public interface NamedCollection<T extends Named> extends StreamableIterable<T>, NameLookup<T> {
   default void clear() {
      this.stream().forEach(this::testNamed3);
   }

   default int size() {
      return (int)this.stream().count();
   }

   default boolean testNamed5(T named) {
      return this.stream().anyMatch(named::equals);
   }

   boolean testNamed3(T var1);

   boolean testNamed4(T var1);

   default boolean contains(String text) {
      return this.resolveOptional2(text).isPresent();
   }
}
