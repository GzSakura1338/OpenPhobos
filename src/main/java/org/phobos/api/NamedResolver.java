package org.phobos.api;

public interface NamedResolver {
   <T> T resolveObject(Holder<T> var1, String var2);
}
