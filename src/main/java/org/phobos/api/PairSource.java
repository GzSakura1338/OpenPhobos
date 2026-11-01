package org.phobos.api;

public interface PairSource<K, V> {
   KeyValue<K, V> getKeyValue();
}
