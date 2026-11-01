package org.phobos.core.util;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class RandomStringUtils {
   public static final Random random = new Random();

   public static String describeInt(int i) {
      return IntStream.range(0, i).mapToObj(ix -> String.valueOf((char)random.nextInt(97, 123))).collect(Collectors.joining());
   }

   public RandomStringUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
