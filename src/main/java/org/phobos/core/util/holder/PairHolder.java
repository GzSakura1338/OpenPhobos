package org.phobos.core.util.holder;

import org.jetbrains.annotations.Nullable;
import org.phobos.api.KeyValue;

public class PairHolder<K, V> implements KeyValue<K, V> {
   @Nullable
   public K object;
   @Nullable
   public V object2;

   public PairHolder<K, V> resolvePairHolder(@Nullable V object) {
      this.object2 = object;
      return this;
   }

   public PairHolder(@Nullable K object, @Nullable V object2) {
      this.object = object;
      this.object2 = object2;
   }

   public PairHolder() {
   }

   @Nullable
   @Override
   public V getObject2() {
      return this.object2;
   }

   public PairHolder<K, V> resolvePairHolder2(@Nullable K object) {
      this.object = object;
      return this;
   }

   @Nullable
   @Override
   public K getObject() {
      return this.object;
   }
}
