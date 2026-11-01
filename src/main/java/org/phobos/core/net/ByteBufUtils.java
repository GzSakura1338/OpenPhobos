package org.phobos.core.net;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import org.phobos.api.JsonSupport;

public final class ByteBufUtils implements JsonSupport {
   public static void handleByteBuf(ByteBuf byteBuf, String text) {
      byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
      byteBuf.writeInt(bytes.length);
      byteBuf.writeBytes(bytes);
   }

   public static void handleByteBuf2(ByteBuf byteBuf, String[] texts) {
      byteBuf.writeInt(texts.length);

      for (String text : texts) {
         handleByteBuf(byteBuf, text);
      }
   }

   public static void handleByteBuf3(ByteBuf byteBuf, Instant instant) {
      byteBuf.writeLong(instant.toEpochMilli());
   }

   public static Instant resolveInstant(ByteBuf byteBuf) {
      return Instant.ofEpochMilli(byteBuf.readLong());
   }

   public static <T> void handleByteBuf4(ByteBuf byteBuf, T object, Function<T, String> function) {
      handleByteBuf(byteBuf, function.apply(object));
   }

   public static JsonElement resolveJsonElement(ByteBuf byteBuf) {
      return JsonParser.parseString(createText(byteBuf));
   }

   public ByteBufUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static byte[] values(ByteBuf byteBuf) {
      byte[] bytes = new byte[byteBuf.readInt()];
      byteBuf.readBytes(bytes);
      return bytes;
   }

   public static String createText(ByteBuf byteBuf) {
      int i = byteBuf.readInt();
      byte[] bytes = new byte[i];
      byteBuf.readBytes(bytes);
      return new String(bytes, StandardCharsets.UTF_8);
   }

   public static String describeByteBuf(ByteBuf byteBuf) {
      boolean flag = byteBuf.readBoolean();
      return !flag ? null : createText(byteBuf);
   }

   public static <K, V> Map<K, V> resolveMap(ByteBuf byteBuf, Function<String, K> function, Function<String, V> function2) {
      int i2 = byteBuf.readInt();
      Map<K, V> map = new HashMap<>();
      if (i2 == 0) {
         return map;
      }

      for (int i = 0; i < i2; i++) {
         map.put(function.apply(createText(byteBuf)), function2.apply(createText(byteBuf)));
      }

      return map;
   }

   public static String[] values2(ByteBuf byteBuf) {
      int i2 = byteBuf.readInt();
      String[] texts = new String[i2];
      if (i2 == 0) {
         return texts;
      }

      for (int i = 0; i < i2; i++) {
         texts[i] = createText(byteBuf);
      }

      return texts;
   }

   public static <K, V> void handleByteBuf5(ByteBuf byteBuf, Map<K, V> map, Function<K, String> function, Function<V, String> function2) {
      byteBuf.writeInt(map.size());

      for (Map.Entry<K, V> entry : map.entrySet()) {
         handleByteBuf(byteBuf, function.apply(entry.getKey()));
         handleByteBuf(byteBuf, function2.apply(entry.getValue()));
      }
   }

   public static void handleByteBuf6(ByteBuf byteBuf, JsonElement jsonElement) {
      handleByteBuf(byteBuf, gson.toJson(jsonElement));
   }

   public static UUID createUUID(ByteBuf byteBuf) {
      return new UUID(byteBuf.readLong(), byteBuf.readLong());
   }

   public static <T> List<T> resolveList(ByteBuf byteBuf, Function<String, T> function) {
      int i2 = byteBuf.readInt();
      List<T> list = new ArrayList<>(i2);
      if (i2 == 0) {
         return list;
      }

      for (int i = 0; i < i2; i++) {
         list.add(function.apply(createText(byteBuf)));
      }

      return list;
   }

   public static void handleByteBuf7(ByteBuf byteBuf, byte[] bytes) {
      byteBuf.writeInt(bytes.length);
      byteBuf.writeBytes(bytes);
   }

   public static void handleByteBuf8(ByteBuf byteBuf, String text) {
      if (text == null) {
         byteBuf.writeBoolean(false);
      } else {
         byteBuf.writeBoolean(true);
         handleByteBuf(byteBuf, text);
      }
   }

   public static <T> void handleByteBuf9(ByteBuf byteBuf, List<T> list, Function<T, String> function) {
      byteBuf.writeInt(list.size());

      for (T object : list) {
         handleByteBuf(byteBuf, function.apply(object));
      }
   }

   public static <T> T resolveObject(ByteBuf byteBuf, Function<String, T> function) {
      return function.apply(createText(byteBuf));
   }

   public static void handleByteBuf10(ByteBuf byteBuf, UUID uUID) {
      byteBuf.writeLong(uUID.getMostSignificantBits());
      byteBuf.writeLong(uUID.getLeastSignificantBits());
   }
}
