package org.phobos.api;

public interface ElementSource<S, R> {
   R getObject();

   S get();
}
