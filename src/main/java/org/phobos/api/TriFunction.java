package org.phobos.api;

public interface TriFunction<Q, R, S, T> {
   T resolveObject(Q var1, R var2, S var3);

   static <Q, R, S> TriFunction<Q, R, S, Boolean> getTriFunction() {
      return (object, object2, object3) -> true;
   }
}
