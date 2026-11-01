package org.phobos.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.session.Session;
import net.minecraft.client.session.Session.AccountType;
import org.phobos.core.util.holder.NamedUuid;
import org.phobos.core.util.holder.StringPair;
import org.phobos.core.util.JsonUtils;
import org.phobos.core.PhobosException;
import org.phobos.mixin.IMinecraftClient;

public enum EmptyEnum {
   ;
   public static final URL MC_TOKEN_URL = createURL("https://api.minecraftservices.com/authentication/login_with_xbox");
   public static final String CLIENT_ID = "00000000402b5328";
   public static final URL AUTH_TOKEN_URL = createURL("https://login.live.com/oauth20_token.srf");
   public static final Pattern PPFT_REGEX = Pattern.compile("sFTTag: ?'.*value=\"(.*)\"/>");
   public static final URL XSTS_TOKEN_URL = createURL("https://xsts.auth.xboxlive.com/xsts/authorize");
   public static final String REDIRECT_URI_ENCODED = "https%3A%2F%2Flogin.live.com%2Foauth20_desktop.srf";
   public static final URL PROFILE_URL = createURL("https://api.minecraftservices.com/minecraft/profile");
   public static final String SCOPE_ENCODED = "service%3A%3Auser.auth.xboxlive.com%3A%3AMBI_SSL";
   public static final URL LOGIN_URL = createURL(
      "https://login.live.com/oauth20_authorize.srf?client_id=00000000402b5328&response_type=code&scope=service%3A%3Auser.auth.xboxlive.com%3A%3AMBI_SSL&redirect_uri=https%3A%2F%2Flogin.live.com%2Foauth20_desktop.srf"
   );
   public static final URL XBL_TOKEN_URL = createURL("https://user.auth.xboxlive.com/user/authenticate");
   public static final Pattern AUTHCODE_REGEX = Pattern.compile("[?|&]code=([\\w.-]+)");
   public static final Pattern URLPOST_REGEX = Pattern.compile("urlPost: ?'(.+?(?='))");
   public static final String SCOPE_UNENCODED = "service::user.auth.xboxlive.com::MBI_SSL";

   public static StringPair getXBLToken(String msftAccessToken) throws PhobosException {
      JsonObject properties = new JsonObject();
      properties.addProperty("AuthMethod", "RPS");
      properties.addProperty("SiteName", "user.auth.xboxlive.com");
      properties.addProperty("RpsTicket", msftAccessToken);
      JsonObject postData = new JsonObject();
      postData.addProperty("RelyingParty", "http://auth.xboxlive.com");
      postData.addProperty("TokenType", "JWT");
      postData.add("Properties", properties);
      String request = postData.toString();

      try {
         URLConnection connection = XBL_TOKEN_URL.openConnection();
         connection.setRequestProperty("Content-Type", "application/json");
         connection.setRequestProperty("Accept", "application/json");
         connection.setDoOutput(true);

         try (OutputStream out = connection.getOutputStream()) {
            out.write(request.getBytes(StandardCharsets.US_ASCII));
         }

         JsonObject json = JsonUtils.resolveJsonElement(connection).getAsJsonObject();
         String token = json.get("Token").getAsString();
         String uhs = json.get("DisplayClaims").getAsJsonObject().get("xui").getAsJsonArray().get(0).getAsJsonObject().get("uhs").getAsString();
         return new StringPair(token, uhs);
      } catch (IOException e) {
         throw new PhobosException("Connection failed: " + e, e);
      }
   }

   public static String getXSTSToken(String xblToken) throws PhobosException {
      JsonArray tokens = new JsonArray();
      tokens.add(xblToken);
      JsonObject properties = new JsonObject();
      properties.addProperty("SandboxId", "RETAIL");
      properties.add("UserTokens", tokens);
      JsonObject postData = new JsonObject();
      postData.addProperty("RelyingParty", "rp://api.minecraftservices.com/");
      postData.addProperty("TokenType", "JWT");
      postData.add("Properties", properties);
      String request = postData.toString();

      try {
         URLConnection connection = XSTS_TOKEN_URL.openConnection();
         connection.setRequestProperty("Content-Type", "application/json");
         connection.setRequestProperty("Accept", "application/json");
         connection.setDoOutput(true);

         try (OutputStream out = connection.getOutputStream()) {
            out.write(request.getBytes(StandardCharsets.US_ASCII));
         }

         JsonObject json = JsonUtils.resolveJsonElement(connection).getAsJsonObject();
         return json.get("Token").getAsString();
      } catch (IOException e) {
         throw new PhobosException("Connection failed: " + e, e);
      }
   }

   public static String getMinecraftAccessToken(String uhs, String xstsToken) throws PhobosException {
      JsonObject postData = new JsonObject();
      postData.addProperty("identityToken", "XBL3.0 x=" + uhs + ";" + xstsToken);
      String request = postData.toString();

      try {
         URLConnection connection = MC_TOKEN_URL.openConnection();
         connection.setRequestProperty("Content-Type", "application/json");
         connection.setRequestProperty("Accept", "application/json");
         connection.setDoOutput(true);

         try (OutputStream out = connection.getOutputStream()) {
            out.write(request.getBytes(StandardCharsets.US_ASCII));
         }

         JsonObject json = JsonUtils.resolveJsonElement(connection).getAsJsonObject();
         return json.get("access_token").getAsString();
      } catch (IOException e) {
         throw new PhobosException("Connection failed: " + e, e);
      }
   }

   public static String getMicrosoftAccessToken(String authCode) throws PhobosException {
      Map<String, String> postData = new HashMap<>();
      postData.put("client_id", "00000000402b5328");
      postData.put("code", authCode);
      postData.put("grant_type", "authorization_code");
      postData.put("redirect_uri", "https://login.live.com/oauth20_desktop.srf");
      postData.put("scope", "service::user.auth.xboxlive.com::MBI_SSL");
      byte[] encodedDataBytes = urlEncodeMap(postData).getBytes(StandardCharsets.UTF_8);

      try {
         HttpURLConnection connection = (HttpURLConnection)AUTH_TOKEN_URL.openConnection();
         connection.setRequestMethod("POST");
         connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
         connection.setDoOutput(true);

         try (OutputStream out = connection.getOutputStream()) {
            out.write(encodedDataBytes);
         }

         JsonObject json = JsonUtils.resolveJsonElement(connection).getAsJsonObject();
         return json.get("access_token").getAsString();
      } catch (IOException e) {
         throw new PhobosException("Connection failed: " + e, e);
      }
   }

   public static UUID uuidFromJson(String jsonUUID) {
      String withDashes = jsonUUID.replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5");
      return UUID.fromString(withDashes);
   }

   public static NamedUuid getAccount(String email, String password) throws PhobosException {
      String authCode = getAuthorizationCode(email, password);
      String msftAccessToken = getMicrosoftAccessToken(authCode);
      StringPair xblToken = getXBLToken(msftAccessToken);
      String xstsToken = getXSTSToken(xblToken.getText());
      String mcAccessToken = getMinecraftAccessToken(xblToken.getText2(), xstsToken);
      return getMinecraftProfile(mcAccessToken);
   }

   public static NamedUuid getMinecraftProfile(String mcAccessToken) throws PhobosException {
      try {
         URLConnection connection = PROFILE_URL.openConnection();
         connection.setRequestProperty("Authorization", "Bearer " + mcAccessToken);
         System.out.println("Getting UUID and name...");
         JsonObject json = JsonUtils.resolveJsonElement(connection).getAsJsonObject();
         if (json.has("error")) {
            throw new PhobosException("Error message from api.minecraftservices.com:\n" + json.get("error"));
         }

         UUID uuid = uuidFromJson(json.get("id").getAsString());
         String name = json.get("name").getAsString();
         return new NamedUuid(uuid, name, mcAccessToken);
      } catch (IOException e) {
         throw new PhobosException("Connection failed: " + e, e);
      }
   }

   public static String downloadData(URLConnection connection) throws IOException {
      try (InputStream input = connection.getInputStream()) {
         InputStreamReader reader = new InputStreamReader(input);
         BufferedReader bufferedReader = new BufferedReader(reader);
         return bufferedReader.lines().collect(Collectors.joining("\n"));
      }
   }

   public static void login(String email, String password) throws PhobosException {
      NamedUuid mcProfile = getAccount(email, password);
      Session session = new Session(mcProfile.getName(), mcProfile.getUUID(), mcProfile.getText(), Optional.empty(), Optional.empty(), AccountType.MSA);
      ((IMinecraftClient)(Object)MinecraftClient.getInstance()).setSession(session);
   }

   public static String urlEncodeMap(Map<String, String> map) {
      StringBuilder sb = new StringBuilder();

      for (Map.Entry<String, String> entry : map.entrySet()) {
         if (!sb.isEmpty()) {
            sb.append("&");
         }

         sb.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
         sb.append("=");
         sb.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
      }

      return sb.toString();
   }

   public static String microsoftLogin(String email, String password, String cookie, String ppft, String urlPost) throws PhobosException {
      Map<String, String> postData = new HashMap<>();
      postData.put("login", email);
      postData.put("loginfmt", email);
      postData.put("passwd", password);
      postData.put("PPFT", ppft);
      byte[] encodedDataBytes = urlEncodeMap(postData).getBytes(StandardCharsets.UTF_8);

      try {
         URL url = URI.create(urlPost).toURL();
         HttpURLConnection connection = (HttpURLConnection)url.openConnection();
         connection.setRequestMethod("POST");
         connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
         connection.setRequestProperty("Content-Length", encodedDataBytes.length + "");
         connection.setRequestProperty("Cookie", cookie);
         connection.setDoInput(true);
         connection.setDoOutput(true);

         try (OutputStream out = connection.getOutputStream()) {
            out.write(encodedDataBytes);
         }

         int responseCode = connection.getResponseCode();
         if (responseCode >= 500 && responseCode <= 599) {
            throw new PhobosException("Servers are down (code " + responseCode + ").");
         } else if (responseCode != 200) {
            throw new PhobosException("Got code " + responseCode + " from urlPost.");
         } else {
            String decodedUrl = URLDecoder.decode(connection.getURL().toString(), StandardCharsets.UTF_8);
            Matcher matcher = AUTHCODE_REGEX.matcher(decodedUrl);
            if (!matcher.find()) {
               throw new PhobosException("Didn't get authCode. (Wrong email/password?)");
            } else {
               return matcher.group(1);
            }
         }
      } catch (IOException e) {
         throw new PhobosException("Connection failed: " + e);
      }
   }

   public static URL createURL(String url) {
      try {
         return URI.create(url).toURL();
      } catch (MalformedURLException e) {
         throw new IllegalArgumentException(e);
      }
   }

   public static String getAuthorizationCode(String email, String password) throws PhobosException {
      StringBuilder cookie;
      String loginWebpage;
      try {
         URLConnection connection = LOGIN_URL.openConnection();
         cookie = new StringBuilder();
         List<String> cookies = connection.getHeaderFields().get("Set-Cookie");
         if (cookies == null) {
            cookies = Collections.emptyList();
         }

         for (String c : cookies) {
            String cookieTrimmed = c.substring(0, c.indexOf(";") + 1);
            cookie.append(cookieTrimmed);
         }

         loginWebpage = downloadData(connection);
      } catch (IOException e) {
         throw new PhobosException("Connection failed: " + e);
      }

      Matcher matcher = PPFT_REGEX.matcher(loginWebpage);
      if (!matcher.find()) {
         throw new PhobosException("sFTTag / PPFT regex failed.");
      }

      String ppft = matcher.group(1);
      matcher = URLPOST_REGEX.matcher(loginWebpage);
      if (!matcher.find()) {
         throw new PhobosException("urlPost regex failed.");
      }

      String urlPost = matcher.group(1);
      return microsoftLogin(email, password, cookie.toString(), ppft, urlPost);
   }
}
