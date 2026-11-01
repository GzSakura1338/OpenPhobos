package org.phobos.core.util.collection;

import java.util.Collection;
import java.util.Map;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.util.math.Vec3i;

public class LongKeyMap<T> implements Map<Long, T> {
   public final Map<Long, T> longValueMap;

   public T put(Long longValue, T object) {
      return this.longValueMap.put(longValue, object);
   }

   public T resolveObject(int i, int i2) {
      return this.longValueMap.get(this.computeInt(i, i2));
   }

   @Override
   public void putAll(Map<? extends Long, ? extends T> map) {
      this.longValueMap.putAll(map);
   }

   public long computeInt(int i, int i2) {
      return (long)i << 32 | i2 & 4294967295L;
   }

   public T resolveObject2(Vec3i vec3i, Function<? super Long, ? extends T> function) {
      return this.resolveObject8(vec3i.getX(), vec3i.getZ(), function);
   }

   @Override
   public int size() {
      return this.longValueMap.size();
   }

   @Override
   public boolean isEmpty() {
      return this.longValueMap.isEmpty();
   }

   public T resolveObject3(Vec3i vec3i) {
      return this.resolveObject(vec3i.getX(), vec3i.getZ());
   }

   public T resolveObject4(Vec3i vec3i) {
      return this.resolveObject10(vec3i.getX(), vec3i.getZ());
   }

   public T resolveObject5(int i, int i2, T object) {
      return this.longValueMap.put(this.computeInt(i, i2), object);
   }

   public LongKeyMap(Map<Long, T> map) {
      this.longValueMap = map;
   }

   @Override
   public Set<Long> keySet() {
      return this.longValueMap.keySet();
   }

   public T resolveObject6(int i, int i2, T object) {
      return this.longValueMap.getOrDefault(this.computeInt(i, i2), object);
   }

   @Override
   public boolean containsKey(Object object) {
      return this.longValueMap.containsKey(object);
   }

   public T resolveObject7(Vec3i vec3i, T object) {
      return this.resolveObject6(vec3i.getX(), vec3i.getZ(), object);
   }

   public T resolveObject8(int i, int i2, Function<? super Long, ? extends T> function) {
      return this.longValueMap.computeIfAbsent(this.computeInt(i, i2), function);
   }

   public T resolveObject9(Vec3i vec3i, T object) {
      return this.resolveObject5(vec3i.getX(), vec3i.getZ(), object);
   }

   @Override
   public T get(Object object) {
      return this.longValueMap.get(object);
   }

   @Override
   public T remove(Object object) {
      return this.longValueMap.remove(object);
   }

   @Override
   public boolean containsValue(Object object) {
      return this.longValueMap.containsValue(object);
   }

   @Override
   public void clear() {
      this.longValueMap.clear();
   }

   public T resolveObject10(int i, int i2) {
      return this.longValueMap.remove(this.computeInt(i, i2));
   }

   @Override
   public Set<Map.Entry<Long, T>> entrySet() {
      return this.longValueMap.entrySet();
   }

   public T putIfAbsent(Long longValue, T object) {
      return this.longValueMap.putIfAbsent(longValue, object);
   }

   public T merge(Long longValue, T object, BiFunction<? super T, ? super T, ? extends T> biFunction) {
      return this.longValueMap.merge(longValue, object, biFunction);
   }

   @Override
   public boolean remove(Object object, Object object2) {
      return this.longValueMap.remove(object, object2);
   }

   @Override
   public void replaceAll(BiFunction<? super Long, ? super T, ? extends T> biFunction) {
      this.longValueMap.replaceAll(biFunction);
   }

   public T replace(Long longValue, T object) {
      return this.longValueMap.replace(longValue, object);
   }

   public boolean replace(Long longValue, T object, T object2) {
      return this.longValueMap.replace(longValue, object, object2);
   }

   public T computeIfPresent(Long longValue, BiFunction<? super Long, ? super T, ? extends T> biFunction) {
      return this.longValueMap.computeIfPresent(longValue, biFunction);
   }

   public T compute(Long longValue, BiFunction<? super Long, ? super T, ? extends T> biFunction) {
      return this.longValueMap.compute(longValue, biFunction);
   }

   @Override
   public T getOrDefault(Object object, T object2) {
      return this.longValueMap.getOrDefault(object, object2);
   }

   @Override
   public Collection<T> values() {
      return this.longValueMap.values();
   }

   public T computeIfAbsent(Long longValue, Function<? super Long, ? extends T> function) {
      return this.longValueMap.computeIfAbsent(longValue, function);
   }

   @Override
   public void forEach(BiConsumer<? super Long, ? super T> biConsumer) {
      this.longValueMap.forEach(biConsumer);
   }
}
