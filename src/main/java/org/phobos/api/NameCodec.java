package org.phobos.api;

public interface NameCodec<T> {
   String describeBlock(T var1);

   T resolveObject(String var1);
}
