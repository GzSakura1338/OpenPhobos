package org.phobos.core.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.phobos.api.ConfigInfo;
import org.phobos.type.ConfigScriptEnum;
import org.phobos.type.PUBLICPRIVATEEnum;
import org.phobos.core.ClientGuard;
import org.phobos.core.gui.screen.ConfigEntryElement;
import org.phobos.core.net.packet.ConfigRequestPacket;
import org.phobos.core.gui.screen.ConfigScreen;
import org.phobos.core.Feature;
import org.phobos.core.util.NullSafe;
import org.phobos.core.PhobosClient;
import org.phobos.core.script.Script;
import org.phobos.core.user.UserSession;

public class CloudConfigApi extends Feature {
   public final Set<String> textSet = new HashSet<>();
   private static Object[] phobos$g1806d38f8;

   public void handleText(String text, Consumer<ConfigMeta> consumer) {
      PhobosClient.resolveCompletableFuture2(
         () -> {
            String text4 = this.describeText(this.describe() + "getAll?storage=" + ConfigScriptEnum.Config + "&token=" + UserSession.describe3(), "GET", null);
            JsonObject jsonObject2 = JsonParser.parseString(text4).getAsJsonObject();

            for (String text3 : jsonObject2.keySet()) {
               if (text3.equals(text)) {
                  JsonObject jsonObject = jsonObject2.get(text3).getAsJsonObject();
                  String text2 = NullSafe.resolveObject(jsonObject.get("Author"), JsonElement::getAsString, "Unknown");
                  PUBLICPRIVATEEnum pUBLICPRIVATEEnum = NullSafe.resolveObject(
                     jsonObject.get("Visibility"), jsonElement -> PUBLICPRIVATEEnum.fromString(jsonElement.getAsString()), PUBLICPRIVATEEnum.PRIVATE
                  );
                  boolean flag = NullSafe.resolveObject(jsonObject.get("Pinned"), JsonElement::getAsBoolean, false);
                  int i = NullSafe.resolveObject(jsonObject.get("Downloads"), JsonElement::getAsInt, 0);
                  consumer.accept(new ConfigMeta(text3, text2, pUBLICPRIVATEEnum, flag, true, i));
                  break;
               }
            }
         }
      );
   }

   public CloudConfigApi(PhobosClient phobosClient) {
      phobosClient.getPacketBus()
         .handleClassValue(
            ConfigRequestPacket.class,
            configRequestPacket -> {
               switch (configRequestPacket.getConfigScriptEnum()) {
                  case Script:
                     String text = configRequestPacket.getName();
                     if (this.textSet.remove(text)) {
                        return;
                     } else {
                        ConfigScreen.configScreen.resolveConfigEntryElement2(text);
                        this.handleText(
                           text,
                           configMeta -> {
                              ConfigEntryElement configEntryElement = ConfigScreen.configScreen.resolveConfigEntryElement4(configMeta);
                              this.handleText8(
                                 text,
                                 configRequestPacket.getConfigScriptEnum(),
                                 text2 -> {
                                    ConfigInfo configInfo = new CloudConfigInfo(
                                       text,
                                       configMeta.getText(),
                                       text2,
                                       true,
                                       configMeta.isFlag(),
                                       configMeta.getPUBLICPRIVATEEnum(),
                                       ConfigJsonUtils.resolveBuildMetadata(text2)
                                    );
                                    configEntryElement.setConfigInfo(configInfo);
                                    configEntryElement.run7();
                                    ConfigScreen.configScreen.handleConfigEntryElement(configEntryElement);
                                 }
                              );
                           }
                        );
                     }
                  case Proxy:
                  case Config:
               }
            }
         );
   }

   public CompletableFuture<Void> resolveCompletableFuture(String text, ConfigScriptEnum configScriptEnum, Consumer<String> consumer, Runnable... runnables) {
      return PhobosClient.resolveCompletableFuture2(
            () -> {
               String text2 = this.describeText(
                  this.describe() + "getFile?name=" + text + "&storage=" + configScriptEnum.toString() + "&token=" + UserSession.describe3(), "GET", null
               );
               consumer.accept(text2.replaceAll("///g", "\n"));
            }
         )
         .thenRun(() -> this.handleRunnable(runnables));
   }

   @NotNull
   public String describeText(String text, String text2, String text3) {
      try {
         URL uRL = new URL(text);
         HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
         httpURLConnection.setRequestMethod(text2);
         httpURLConnection.setConnectTimeout(15000);
         httpURLConnection.setReadTimeout(15000);
         httpURLConnection.setDoOutput(true);
         if (text3 != null) {
            httpURLConnection.setRequestProperty("Content-Type", "application/json");

            try (OutputStream outputStream = httpURLConnection.getOutputStream()) {
               outputStream.write(text3.getBytes(StandardCharsets.UTF_8));
            }
         }

         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
         StringBuilder stringBuilder = new StringBuilder();

         String text4;
         while ((text4 = bufferedReader.readLine()) != null) {
            stringBuilder.append(text4);
         }

         bufferedReader.close();
         return stringBuilder.toString();
      } catch (Throwable throwable) {
         return "{}";
      }
   }

   public void handleText2(String text, ConfigScriptEnum configScriptEnum, boolean flag, Runnable... runnables) {
      PhobosClient.resolveCompletableFuture2(() -> {
         JsonObject jsonObject = new JsonObject();
         jsonObject.addProperty("Name", text);
         jsonObject.addProperty("Storage", configScriptEnum.toString());
         jsonObject.addProperty("State", flag);
         jsonObject.addProperty("Token", UserSession.describe3());
         this.describeText(this.describe() + "pin", "POST", jsonObject.toString());
      }).thenRun(() -> this.handleRunnable(runnables));
   }

   public void handleText3(String text, ConfigScriptEnum configScriptEnum, PUBLICPRIVATEEnum pUBLICPRIVATEEnum, String text2, Runnable... runnables) {
      PhobosClient.resolveCompletableFuture2(() -> {
         JsonObject jsonObject = new JsonObject();
         jsonObject.addProperty("Name", text);
         jsonObject.addProperty("Storage", configScriptEnum.toString());
         jsonObject.addProperty("Visibility", pUBLICPRIVATEEnum.toString());
         jsonObject.addProperty("Data", text2);
         jsonObject.addProperty("Token", UserSession.describe3());
         this.describeText(this.describe() + "save", "POST", jsonObject.toString());
      }).thenRun(() -> this.handleRunnable(runnables));
   }

   public void handleText4(String text, ConfigScriptEnum configScriptEnum, PUBLICPRIVATEEnum pUBLICPRIVATEEnum, Runnable... runnables) {
      PhobosClient.resolveCompletableFuture2(() -> {
         JsonObject jsonObject = new JsonObject();
         jsonObject.addProperty("Name", text);
         jsonObject.addProperty("Storage", configScriptEnum.toString());
         jsonObject.addProperty("Visibility", pUBLICPRIVATEEnum.toString());
         jsonObject.addProperty("Token", UserSession.describe3());
         this.describeText(this.describe() + "updateVisibility", "POST", jsonObject.toString());
      }).thenRun(() -> this.handleRunnable(runnables));
   }

   public String describe() {
      String var1 = ClientGuard.text;
      boolean var2 = var1.equals("127.0.0.1");
      if (!var2) {
         ClientGuard.handleText("Swapping out server IP");
         ClientGuard.stop();
      }

      String var3 = ClientGuard.text;
      return "http://" + var3 + ":7565/api/v1/client/";
   }

   public void handleText5(String text, String text2, Runnable... runnables) {
      PhobosClient.resolveCompletableFuture2(() -> {
         JsonObject jsonObject = new JsonObject();
         jsonObject.addProperty("Name", text);
         jsonObject.addProperty("Data", text2);
         jsonObject.addProperty("Token", UserSession.describe3());
         this.describeText(this.describe() + "replace", "POST", jsonObject.toString());
      }).thenRun(() -> this.handleRunnable(runnables));
   }

   public void handleText6(String text, ConfigScriptEnum configScriptEnum, Runnable... runnables) {
      PhobosClient.resolveCompletableFuture2(() -> {
         JsonObject jsonObject = new JsonObject();
         jsonObject.addProperty("Name", text);
         jsonObject.addProperty("Storage", configScriptEnum.toString());
         jsonObject.addProperty("Token", UserSession.describe3());
         this.describeText(this.describe() + "delete", "DELETE", jsonObject.toString());
      }).thenRun(() -> this.handleRunnable(runnables));
   }

   public void handleConsumer(Consumer<List<CloudConfigRef>> consumer, Runnable... runnables) {
      PhobosClient.resolveCompletableFuture2(() -> {
         String text3 = this.describeText(this.describe() + "getAll?storage=" + ConfigScriptEnum.Script + "&token=" + UserSession.describe3(), "GET", null);
         JsonObject jsonObject2 = JsonParser.parseString(text3).getAsJsonObject();
         List<CloudConfigRef> list = new ArrayList<>();

         for (String text2 : jsonObject2.keySet()) {
            JsonObject jsonObject = jsonObject2.get(text2).getAsJsonObject();
            String text = jsonObject.get("Author").getAsString();
            PUBLICPRIVATEEnum pUBLICPRIVATEEnum = PUBLICPRIVATEEnum.fromString(jsonObject.get("Visibility").getAsString());
            list.add(new CloudConfigRef(text2, text, pUBLICPRIVATEEnum));
         }

         consumer.accept(list);
      }).thenRun(() -> this.handleRunnable(runnables));
   }

   public void handleText7(String text, ConfigScriptEnum configScriptEnum) {
      PhobosClient.resolveCompletableFuture2(() -> {
         JsonObject jsonObject = new JsonObject();
         jsonObject.addProperty("Name", text);
         jsonObject.addProperty("Storage", configScriptEnum.toString());
         jsonObject.addProperty("Token", UserSession.describe3());
         this.describeText(this.describe() + "download", "POST", jsonObject.toString());
      });
   }

   public void handleText8(String text, ConfigScriptEnum configScriptEnum, Consumer<String> consumer) {
      String text2 = this.describeText(
         this.describe() + "getFile?name=" + text + "&storage=" + configScriptEnum.toString() + "&token=" + UserSession.describe3(), "GET", null
      );
      if (!text2.equals("{}")) {
         consumer.accept(text2.replaceAll("///g", "\n"));
      }
   }

   public void handleRunnable(Runnable... runnables) {
      for (Runnable runnable : runnables) {
         runnable.run();
      }
   }

   public void handleText9(String text) {
      this.textSet.add(text);
   }

   public void handleConsumer2(Consumer<List<ConfigMeta>> consumer, Runnable... runnables) {
      PhobosClient.resolveCompletableFuture2(
            () -> {
               String text3 = this.describeText(
                  this.describe() + "getAll?storage=" + ConfigScriptEnum.Config + "&token=" + UserSession.describe3(), "GET", null
               );
               JsonObject jsonObject2 = JsonParser.parseString(text3).getAsJsonObject();
               List<ConfigMeta> list = new ArrayList<>();

               for (String text2 : jsonObject2.keySet()) {
                  JsonObject jsonObject = jsonObject2.get(text2).getAsJsonObject();
                  String text = NullSafe.resolveObject(jsonObject.get("Author"), JsonElement::getAsString, "Unknown");
                  PUBLICPRIVATEEnum pUBLICPRIVATEEnum = NullSafe.resolveObject(
                     jsonObject.get("Visibility"), jsonElement -> PUBLICPRIVATEEnum.fromString(jsonElement.getAsString()), PUBLICPRIVATEEnum.PRIVATE
                  );
                  boolean flag = NullSafe.resolveObject(jsonObject.get("Pinned"), JsonElement::getAsBoolean, false);
                  int i = NullSafe.resolveObject(jsonObject.get("Downloads"), JsonElement::getAsInt, 0);
                  list.add(new ConfigMeta(text2, text, pUBLICPRIVATEEnum, flag, true, i));
               }

               consumer.accept(list);
            }
         )
         .thenRun(() -> this.handleRunnable(runnables));
   }

   public void handleConsumer3(Consumer<List<CloudProxyRef>> consumer, Runnable... runnables) {
      PhobosClient.resolveCompletableFuture2(() -> {
         String text3 = this.describeText(this.describe() + "getAll?storage=" + ConfigScriptEnum.Proxy + "&token=" + UserSession.describe3(), "GET", null);
         JsonObject jsonObject2 = JsonParser.parseString(text3).getAsJsonObject();
         List<CloudProxyRef> list = new ArrayList<>();

         for (String text2 : jsonObject2.keySet()) {
            JsonObject jsonObject = jsonObject2.get(text2).getAsJsonObject();
            String text = jsonObject.get("Author").getAsString();
            PUBLICPRIVATEEnum pUBLICPRIVATEEnum = PUBLICPRIVATEEnum.fromString(jsonObject.get("Visibility").getAsString());
            list.add(new CloudProxyRef(text2, text, pUBLICPRIVATEEnum));
         }

         consumer.accept(list);
      }).thenRun(() -> this.handleRunnable(runnables));
   }
}
