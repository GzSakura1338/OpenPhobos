package org.phobos.api;

import org.phobos.core.task.CancelToken;

@FunctionalInterface
public interface CancelableFunction<T> {
   T resolveObject(CancelToken var1);
}
