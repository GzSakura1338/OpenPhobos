package org.phobos.core.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public final class ConfigJsonUtils {
   public static void handleJsonObject(JsonObject jsonObject) {
      BuildMetadata buildMetadata = new BuildMetadata();
      buildMetadata.run();
      buildMetadata.handleJsonObject(jsonObject);
   }

   public static BuildMetadata resolveBuildMetadata(String text) {
      BuildMetadata buildMetadata = new BuildMetadata();

      try {
         JsonObject jsonObject = JsonParser.parseString(text).getAsJsonObject();
         buildMetadata.handleJsonObject2(jsonObject);
      } catch (Throwable var3) {
      }

      return buildMetadata;
   }

   public static BuildMetadata resolveBuildMetadata2(JsonObject jsonObject) {
      BuildMetadata buildMetadata = new BuildMetadata();
      buildMetadata.handleJsonObject2(jsonObject);
      return buildMetadata;
   }

   public ConfigJsonUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
