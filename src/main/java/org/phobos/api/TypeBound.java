package org.phobos.api;

public interface TypeBound<V> {
   Class<? super V> getValueClassValue();

   default boolean testClassValue(Class<?> classValue) {
      return classValue.equals(this.getValueClassValue());
   }

   default boolean testTypeBound(TypeBound<?> typeBound) {
      return typeBound.getValueClassValue().equals(this.getValueClassValue());
   }
}
