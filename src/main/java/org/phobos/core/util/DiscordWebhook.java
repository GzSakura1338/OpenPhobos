package org.phobos.core.util;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import javax.net.ssl.HttpsURLConnection;

public class DiscordWebhook {
   public String text;
   public String text2;
   public final List<DiscordWebhook.EmbedObject> embedObjectList = new ArrayList<>();
   public boolean flag;
   public String text3;
   public final String text4;

   public void setText3(String text) {
      this.text3 = text;
   }

   public DiscordWebhook(String text) {
      this.text4 = text;
   }

   public void setText(String text) {
      this.text = text;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public void handleEmbedObject(DiscordWebhook.EmbedObject embedObject) {
      this.embedObjectList.add(embedObject);
   }

   public void setText2(String text) {
      this.text2 = text;
   }

   public void run() throws IOException {
      if (this.text == null && this.embedObjectList.isEmpty()) {
         throw new IllegalArgumentException("Set content or add at least one EmbedObject");
      }

      DiscordWebhook.JSONObject jSONObject7 = new DiscordWebhook.JSONObject();
      jSONObject7.handleText("content", this.text);
      jSONObject7.handleText("username", this.text3);
      jSONObject7.handleText("avatar_url", this.text2);
      jSONObject7.handleText("tts", this.flag);
      if (!this.embedObjectList.isEmpty()) {
         List<DiscordWebhook.JSONObject> list3 = new ArrayList<>();

         for (DiscordWebhook.EmbedObject embedObject : this.embedObjectList) {
            DiscordWebhook.JSONObject jSONObject6 = new DiscordWebhook.JSONObject();
            jSONObject6.handleText("title", embedObject.getText());
            jSONObject6.handleText("description", embedObject.getText3());
            jSONObject6.handleText("url", embedObject.getText2());
            if (embedObject.getColor() != null) {
               Color color = embedObject.getColor();
               int i = color.getRed();
               i = (i << 8) + color.getGreen();
               i = (i << 8) + color.getBlue();
               jSONObject6.handleText("color", i);
            }

            DiscordWebhook.EmbedObject.Footer footer = embedObject.getFooter();
            DiscordWebhook.EmbedObject.Image image = embedObject.getImage();
            DiscordWebhook.EmbedObject.Thumbnail thumbnail = embedObject.getThumbnail();
            DiscordWebhook.EmbedObject.Author author = embedObject.getAuthor();
            List<DiscordWebhook.EmbedObject.Field> list = embedObject.getFieldList();
            if (footer != null) {
               DiscordWebhook.JSONObject jSONObject = new DiscordWebhook.JSONObject();
               jSONObject.handleText("text", footer.text());
               jSONObject.handleText("icon_url", footer.iconUrl());
               jSONObject6.handleText("footer", jSONObject);
            }

            if (image != null) {
               DiscordWebhook.JSONObject jSONObject2 = new DiscordWebhook.JSONObject();
               jSONObject2.handleText("url", image.url());
               jSONObject6.handleText("image", jSONObject2);
            }

            if (thumbnail != null) {
               DiscordWebhook.JSONObject jSONObject3 = new DiscordWebhook.JSONObject();
               jSONObject3.handleText("url", thumbnail.url());
               jSONObject6.handleText("thumbnail", jSONObject3);
            }

            if (author != null) {
               DiscordWebhook.JSONObject jSONObject4 = new DiscordWebhook.JSONObject();
               jSONObject4.handleText("name", author.name());
               jSONObject4.handleText("url", author.url());
               jSONObject4.handleText("icon_url", author.iconUrl());
               jSONObject6.handleText("author", jSONObject4);
            }

            List<DiscordWebhook.JSONObject> list2 = new ArrayList<>();

            for (DiscordWebhook.EmbedObject.Field field : list) {
               DiscordWebhook.JSONObject jSONObject5 = new DiscordWebhook.JSONObject();
               jSONObject5.handleText("name", field.name());
               jSONObject5.handleText("value", field.value());
               jSONObject5.handleText("inline", field.inline());
               list2.add(jSONObject5);
            }

            jSONObject6.handleText("fields", list2.toArray());
            list3.add(jSONObject6);
         }

         jSONObject7.handleText("embeds", list3.toArray());
      }

      URL uRL = new URL(this.text4);
      HttpsURLConnection httpsURLConnection = (HttpsURLConnection)uRL.openConnection();
      httpsURLConnection.addRequestProperty("Content-Type", "application/json");
      httpsURLConnection.addRequestProperty("User-Agent", "Java-DiscordWebhook");
      httpsURLConnection.setDoOutput(true);
      httpsURLConnection.setRequestMethod("POST");
      OutputStream outputStream = httpsURLConnection.getOutputStream();
      outputStream.write(jSONObject7.toString().getBytes());
      outputStream.flush();
      outputStream.close();
      httpsURLConnection.getInputStream().close();
      httpsURLConnection.disconnect();
   }

   public static class EmbedObject {
      public String text;
      public DiscordWebhook.EmbedObject.Author author;
      public DiscordWebhook.EmbedObject.Image image;
      public final List<DiscordWebhook.EmbedObject.Field> fieldList = new ArrayList<>();
      public String text2;
      public DiscordWebhook.EmbedObject.Footer footer;
      public DiscordWebhook.EmbedObject.Thumbnail thumbnail;
      public String text3;
      public Color color;

      public DiscordWebhook.EmbedObject resolveEmbedObject(String text, String text2) {
         this.footer = new DiscordWebhook.EmbedObject.Footer(text, text2);
         return this;
      }

      public String getText3() {
         return this.text3;
      }

      public String getText() {
         return this.text;
      }

      public DiscordWebhook.EmbedObject resolveEmbedObject2(String text) {
         this.thumbnail = new DiscordWebhook.EmbedObject.Thumbnail(text);
         return this;
      }

      public DiscordWebhook.EmbedObject resolveEmbedObject3(String text) {
         this.text = text;
         return this;
      }

      public DiscordWebhook.EmbedObject resolveEmbedObject4(String text) {
         this.text3 = text;
         return this;
      }

      public DiscordWebhook.EmbedObject resolveEmbedObject5(String text, String text2, String text3) {
         this.author = new DiscordWebhook.EmbedObject.Author(text, text2, text3);
         return this;
      }

      public DiscordWebhook.EmbedObject.Thumbnail getThumbnail() {
         return this.thumbnail;
      }

      public DiscordWebhook.EmbedObject resolveEmbedObject6(String text, String text2, boolean flag) {
         this.fieldList.add(new DiscordWebhook.EmbedObject.Field(text, text2, flag));
         return this;
      }

      public Color getColor() {
         return this.color;
      }

      public DiscordWebhook.EmbedObject.Image getImage() {
         return this.image;
      }

      public List<DiscordWebhook.EmbedObject.Field> getFieldList() {
         return this.fieldList;
      }

      public DiscordWebhook.EmbedObject resolveEmbedObject7(String text) {
         this.text2 = text;
         return this;
      }

      public String getText2() {
         return this.text2;
      }

      public DiscordWebhook.EmbedObject resolveEmbedObject8(String text) {
         this.image = new DiscordWebhook.EmbedObject.Image(text);
         return this;
      }

      public DiscordWebhook.EmbedObject resolveEmbedObject9(Color color) {
         this.color = color;
         return this;
      }

      public DiscordWebhook.EmbedObject.Author getAuthor() {
         return this.author;
      }

      public DiscordWebhook.EmbedObject.Footer getFooter() {
         return this.footer;
      }

      public record Author(String name, String url, String iconUrl) {
      }

      public record Field(String name, String value, boolean inline) {
      }

      public record Footer(String text, String iconUrl) {
      }

      public record Image(String url) {
      }

      public record Thumbnail(String url) {
      }
   }

   private static class JSONObject {
      public final HashMap<String, Object> textHashMap = new HashMap<>();

      public void handleText(String text, Object object) {
         if (object != null) {
            this.textHashMap.put(text, object);
         }
      }

      public JSONObject() {
      }

      @Override
      public String toString() {
         StringBuilder stringBuilder = new StringBuilder();
         Set<Entry<String, Object>> set = this.textHashMap.entrySet();
         stringBuilder.append("{");
         int i3 = 0;

         for (Entry<String, Object> entry : set) {
            Object object = entry.getValue();
            stringBuilder.append(this.describeText(entry.getKey())).append(":");
            if (object instanceof String) {
               stringBuilder.append(this.describeText(String.valueOf(object)));
            } else if (object instanceof Integer) {
               stringBuilder.append(Integer.valueOf(String.valueOf(object)));
            } else if (object instanceof Boolean) {
               stringBuilder.append(object);
            } else if (object instanceof DiscordWebhook.JSONObject) {
               stringBuilder.append(object.toString());
            } else if (object.getClass().isArray()) {
               stringBuilder.append("[");
               int i2 = Array.getLength(object);

               for (int i = 0; i < i2; i++) {
                  stringBuilder.append(Array.get(object, i).toString()).append(i != i2 - 1 ? "," : "");
               }

               stringBuilder.append("]");
            }

            i3++;
            stringBuilder.append(i3 == set.size() ? "}" : ",");
         }

         return stringBuilder.toString();
      }

      public String describeText(String text) {
         return "\"" + text + "\"";
      }
   }
}
