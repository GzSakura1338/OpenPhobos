package org.phobos.api;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface KeyValue<K, V> {
   K getObject();

   V getObject2();

   default <T, S> KeyValue<T, S> resolveKeyValue(Class<T> classValue, Class<S> classValue2) {
      return resolveKeyValue7(this, classValue, classValue2);
   }

   static <T> KeyValue<T, T> resolveKeyValue2(Constant<T> constant) {
      T object = constant.get();
      return createValue(object, object);
   }

   default KeyValue<V, K> getKeyValue2() {
      return createValue(this.getObject2(), this.getObject());
   }

   default <T, S> KeyValue<T, S> resolveKeyValue3(Function<K, T> function, Function<V, S> function2) {
      return createValue(function.apply(this.getObject()), function2.apply(this.getObject2()));
   }

   static <T> KeyValue<T, T> resolveKeyValue4(T object) {
      return createValue(object, object);
   }

   static <K, V> KeyValue<K, V> createValue(K object, V object2) {
      return new KeyValue<K, V>() {
         @Override
         public K getObject() {
            return object;
         }

         @Override
         public V getObject2() {
            return object2;
         }
      };
   }

   default <T, S> KeyValue<T, S> resolveKeyValue5(BiFunction<K, V, KeyValue<T, S>> biFunction) {
      return biFunction.apply(this.getObject(), this.getObject2());
   }

   default KeyValue<K, K> getKeyValue3() {
      K object = this.getObject();
      return createValue(object, object);
   }

   static <K, V> KeyValue<K, V> resolveKeyValue6(KeyValue<?, ?> keyValue, Class<K> classValue, Class<V> classValue2) throws ClassCastException {
      return createValue((K)keyValue.getObject(), (V)keyValue.getObject2());
   }

   default KeyValue<V, V> getKeyValue4() {
      V object = this.getObject2();
      return createValue(object, object);
   }

   static <K, V> KeyValue<K, V> resolveKeyValue7(KeyValue<?, ?> keyValue, Class<K> classValue, Class<V> classValue2) throws ClassCastException {
      return (KeyValue<K, V>)keyValue;
   }

   default KeyValue<K, V> resolveKeyValue8(BiConsumer<? super K, ? super V> biConsumer) {
      biConsumer.accept(this.getObject(), this.getObject2());
      return this;
   }

   default <T, S> KeyValue<T, S> resolveKeyValue9(BiConsumer<? super K, ? super V> biConsumer, BiFunction<K, V, KeyValue<T, S>> biFunction) {
      return this.resolveKeyValue8(biConsumer).resolveKeyValue5(biFunction);
   }
}
