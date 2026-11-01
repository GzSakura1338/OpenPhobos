package org.phobos.core.util;

import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Stream;

public final class StreamUtils {
   public StreamUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static <T, U extends Comparable<? super U>> Stream<T> resolveStream(Stream<T> stream, Function<? super T, ? extends U> function) {
      return stream.<StreamUtils.CachedEntry<T, U>>map(object -> new StreamUtils.CachedEntry<T, U>(object, function.apply(object)))
         .sorted(Comparator.comparing(StreamUtils.CachedEntry::key))
         .map(StreamUtils.CachedEntry::value);
   }

   private record CachedEntry<T, U extends Comparable<? super U>>(T value, U key) {
   }
}
