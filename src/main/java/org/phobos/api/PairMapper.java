package org.phobos.api;

public interface PairMapper<T, S, K> {
   static <T, K> PairMapper<T, K, K> resolvePairMapper(K object) {
      return object2 -> KeyValue.resolveKeyValue4(object);
   }

   KeyValue<S, K> resolveKeyValue(T var1);
}
