package org.phobos.core.util.collection;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import org.phobos.api.Fluent;

public class MapWrapper<K, V> implements Fluent<MapWrapper<K, V>> {
   public Map<K, V> map;

   public MapWrapper<K, V> get() {
      return this;
   }

   public ArrayWrapper<V> createArrayWrapper() {
      return new ArrayWrapper<>((V[])this.map.values().toArray());
   }

   public void forEach(BiConsumer<? super K, ? super V> biConsumer) {
      this.map.forEach(biConsumer);
   }

   public void handleBiPredicate(BiPredicate<? super K, ? super V> biPredicate) {
      this.map.entrySet().removeIf(entry -> biPredicate.test(entry.getKey(), entry.getValue()));
   }

   public MapWrapper(Map<K, V> map) {
      this.map = map;
   }

   public <S, T> Map<S, T> resolveMap(Function<? super K, S> function, Function<? super V, T> function2) {
      Map<S, T> map = new HashMap();
      this.forEach((object, object2) -> map.put(function.apply(object), function2.apply(object2)));
      return map;
   }

   public ArrayWrapper<K> createArrayWrapper2() {
      return new ArrayWrapper<>((K[])this.map.keySet().toArray());
   }

   public void run() {
      this.map = new LinkedHashMap<>(this.map);
   }

   public void run2() {
      this.map = new ConcurrentHashMap<>(this.map);
   }

   public void handleFunction(Function<? super K, K> function, Function<? super V, V> function2) {
      this.forEach((object, object2) -> this.map.put(function.apply(object), function2.apply(object2)));
   }
}
