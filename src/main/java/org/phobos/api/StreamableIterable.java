package org.phobos.api;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface StreamableIterable<T> extends Iterable<T> {
   default Stream<T> stream() {
      return StreamSupport.stream(this.spliterator(), false);
   }
}
