package org.phobos.core.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;
import org.phobos.api.TimeGate.Normal;
import org.phobos.core.ClientGuard;
import org.phobos.core.PhobosClient;
import org.phobos.core.user.UserSession;

public class CloudApiClient {
   public final Normal normal;
   public final Set<String> textSet = ConcurrentHashMap.newKeySet();
   private static Object[] phobos$g1806fb1e8;
   private static Object[] phobos$g1806fb360;

   public String describe() {
      String var1 = ClientGuard.text;
      boolean var2 = var1.equals("127.0.0.1");
      if (!var2) {
         ClientGuard.handleText("Swapping out server IP");
         ClientGuard.stop();
      }

      String var3 = ClientGuard.text;
      return "http://" + var3 + ":7565/api/v1/";
   }

   public Set<String> getTextSet() {
      return this.textSet;
   }

   public void run() {
      if (this.normal.testLong2(60000L)) {
         PhobosClient.resolveCompletableFuture2(() -> this.handleSet(this.textSet));
      }
   }

   public boolean check() {
      return !this.textSet.isEmpty();
   }

   public CloudApiClient() {
      this.normal = new Normal();
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
         return "[]";
      }
   }

   public void handleSet(Set<String> set) {
      String var2 = this.describe();
      Object[] var3 = new Object[1];
      String var4 = UserSession.describe3();
      var3[0] = var4;
      String var5 = "usertable?token=%s".formatted(var3);
      String var13 = var2 + var5;
      String var14 = this.describeText(var13, "GET", (String)null);
      JsonElement var15 = JsonParser.parseString(var14);
      boolean var16 = var15.isJsonArray();
      if (var16) {
         JsonArray var17 = var15.getAsJsonArray();
         Iterator var18 = var17.iterator();

         while (true) {
            boolean var19 = var18.hasNext();
            if (!var19) {
               break;
            }

            Object var20 = var18.next();
            boolean var21 = var20 instanceof JsonElement;
            if (var20 != null && !var21) {
               throw new ClassCastException("CHECKCAST failed");
            }

            String var22 = ((JsonElement)var20).getAsString();
            set.add(var22);
         }
      }
   }
}
