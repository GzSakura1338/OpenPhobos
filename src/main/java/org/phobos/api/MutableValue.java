package org.phobos.api;

public interface MutableValue<V> {
   V getValue();

   void setValue(V var1);
}
