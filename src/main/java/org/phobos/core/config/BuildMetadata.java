package org.phobos.core.config;

import com.google.gson.JsonObject;
import java.time.Instant;
import org.phobos.api.Named;
import org.phobos.core.user.UserSession;

public class BuildMetadata {
   public final ConfigPropertyMap configPropertyMap = new ConfigPropertyMap()
      .resolveConfigPropertyMap2("date", Instant::toString, Instant::parse, Instant::now)
      .resolveConfigPropertyMap("version", BuildInfo.getINSTANCE().describe3())
      .resolveConfigPropertyMap("build", BuildInfo.getINSTANCE().getType().name())
      .resolveConfigPropertyMap("product", "3arthh4ck")
      .resolveConfigPropertyMap3("sha1", () -> BuildInfo.getINSTANCE().describe())
      .resolveConfigPropertyMap3("user", () -> UserSession.userSession.getText())
      .resolveConfigPropertyMap("encoding", BuildMetadata.Encoding.APPLICATION.getName())
      .resolveConfigPropertyMap("context", "v0.16")
      .resolveConfigPropertyMap("packer", "v0.1");
   public static final String text = "v0.16";

   public <T> T resolveObject(String text, Class<T> classValue) {
      return this.resolveConfigProperty(text, classValue).getValue();
   }

   public <T> ConfigProperty<T> resolveConfigProperty(String text, Class<T> classValue) {
      return this.configPropertyMap.resolveConfigProperty(text, classValue);
   }

   public ConfigPropertyMap getConfigPropertyMap() {
      return this.configPropertyMap;
   }

   public void handleJsonObject(JsonObject jsonObject) {
      if (!jsonObject.has("header")) {
         JsonObject jsonObject2 = new JsonObject();
         this.configPropertyMap.getCollection().forEach(configProperty -> configProperty.handleJsonObject2(jsonObject2));
         jsonObject.add("header", jsonObject2);
      }
   }

   public void handleJsonObject2(JsonObject jsonObject) {
      if (jsonObject.has("header")) {
         try {
            JsonObject jsonObject2 = jsonObject.get("header").getAsJsonObject();
            this.configPropertyMap.getCollection().forEach(configProperty -> configProperty.handleJsonObject(jsonObject2));
         } catch (Throwable var3) {
         }
      }
   }

   public void run() {
      this.configPropertyMap.getCollection().forEach(ConfigProperty::run2);
   }

   public enum Encoding implements Named {
      APPLICATION("application-json/xlmnk"),
      STRING("string/UTF-8");

      public final String name;

      Encoding(final String name) {
         this.name = name;
      }

      @Override
      public String getName() {
         return this.name;
      }
   }
}
