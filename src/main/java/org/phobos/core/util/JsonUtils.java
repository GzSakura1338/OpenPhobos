package org.phobos.core.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import org.phobos.api.MixinClassNames;

public final class JsonUtils {
   public static JsonElement resolveJsonElement(URLConnection uRLConnection) throws IOException, JsonParseException {
      try (InputStream inputStream = uRLConnection.getInputStream()) {
         InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
         BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
         return JsonParser.parseReader(bufferedReader);
      }
   }

   @SafeVarargs
   public static <N extends Number> JsonElement createJsonPrimitive(N... numbers) {
      if (numbers.length == 0) {
         throw new IllegalArgumentException("Unable to write 0 numbers");
      }

      Function<N[], String> function = numbersx -> {
         StringBuilder stringBuilder = new StringBuilder();

         for (N number : numbersx) {
            stringBuilder.append(describeNumber(number)).append("&");
         }

         String text = stringBuilder.toString();
         return text.substring(0, text.length() - 1);
      };
      return new JsonPrimitive(function.apply(numbers));
   }

   public static Long resolveLongValue(JsonElement jsonElement, long l) {
      return jsonElement != null ? jsonElement.getAsLong() : l;
   }

   public JsonUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static <N extends Number> String describeNumber(N number) {
      if (number instanceof Integer integer) {
         return String.valueOf(integer);
      } else if (number instanceof Float floatValue) {
         return String.valueOf(floatValue);
      } else if (number instanceof Double doubleValue) {
         return String.valueOf(doubleValue);
      } else if (number instanceof Long longValue) {
         return String.valueOf(longValue);
      } else if (number instanceof Short shortValue) {
         return String.valueOf(shortValue);
      } else if (number instanceof Byte byteValue) {
         return String.valueOf(byteValue);
      } else {
         throw new UnsupportedOperationException("Unsupported writing type: %s".formatted(number.getClass().getSimpleName()));
      }
   }

   public static <N extends Number> N resolveNumber(N number, String text) {
      if (number instanceof Integer) {
         return MixinClassNames.CCS.resolveObject2(Integer.valueOf(text), number);
      } else if (number instanceof Float) {
         return MixinClassNames.CCS.resolveObject2(Float.valueOf(text), number);
      } else if (number instanceof Double) {
         return MixinClassNames.CCS.resolveObject2(Double.valueOf(text), number);
      } else if (number instanceof Long) {
         return MixinClassNames.CCS.resolveObject2(Long.valueOf(text), number);
      } else if (number instanceof Short) {
         return MixinClassNames.CCS.resolveObject2(Short.valueOf(text), number);
      } else if (number instanceof Byte) {
         return MixinClassNames.CCS.resolveObject2(Byte.valueOf(text), number);
      } else {
         throw new UnsupportedOperationException("Unsupported writing type: %s".formatted(number.getClass().getSimpleName()));
      }
   }

   public static Boolean resolveBooleanValue(JsonElement jsonElement, boolean flag) {
      return jsonElement != null ? jsonElement.getAsBoolean() : flag;
   }

   public static String describeJsonElement(JsonElement jsonElement, String text) {
      return jsonElement != null ? jsonElement.getAsString() : text;
   }

   public static Integer resolveInteger(JsonElement jsonElement, int i) {
      return jsonElement != null ? jsonElement.getAsInt() : i;
   }

   public static Double resolveDoubleValue(JsonElement jsonElement, double d) {
      return jsonElement != null ? jsonElement.getAsFloat() : d;
   }

   public static Float resolveFloatValue(JsonElement jsonElement, float f) {
      return jsonElement != null ? jsonElement.getAsFloat() : f;
   }

   @SafeVarargs
   public static <N extends Number> void handleJsonElement(JsonElement jsonElement, N number, Consumer<N>... consumers) {
      String text2 = jsonElement.getAsString();
      if (!text2.contains("&")) {
         if (consumers.length != 1) {
            throw new IllegalArgumentException("Parsed singleton, invalid action counter");
         }

         N number2 = resolveNumber(number, text2);
         consumers[0].accept(number2);
      } else {
         String[] texts = text2.split("&");
         List<N> list = new ArrayList<>();

         for (String text : texts) {
            N number3 = resolveNumber(number, text);
            list.add(number3);
         }

         int i2 = list.size();
         if (i2 != consumers.length) {
            throw new IllegalArgumentException("Parsed: %s, Expected: %s".formatted(i2, consumers.length));
         }

         for (int i = 0; i < i2; i++) {
            N number4 = (N)list.get(i);
            Consumer<N> consumer = consumers[i];
            consumer.accept(number4);
         }
      }
   }
}
