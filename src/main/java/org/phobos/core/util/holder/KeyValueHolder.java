package org.phobos.core.util.holder;

import org.phobos.api.PairSource;
import org.phobos.api.KeyValue;

public class KeyValueHolder<K, V> implements KeyValue<K, V>, PairSource<K, V> {
   public final V object;
   public final K object2;

   @Override
   public V getObject2() {
      return this.object;
   }

   @Override
   public KeyValue<K, V> getKeyValue() {
      return this;
   }

   @Override
   public K getObject() {
      return this.object2;
   }

   public KeyValueHolder(K object, V object2) {
      this.object2 = object;
      this.object = object2;
   }
}
