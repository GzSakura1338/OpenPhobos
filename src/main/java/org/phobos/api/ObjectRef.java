package org.phobos.api;

import java.util.function.Consumer;

public interface ObjectRef<T> {
   T getObject();

   T resolveObject(Consumer<T> var1);
}
