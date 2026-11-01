package org.phobos.core.render.texture;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraft.client.texture.NativeImageBackedTexture;

public class AnimatedTexture {
   public long longValue;
   public int intValue;
   public boolean flag;
   public long longValue2;
   public int intValue2;
   public NativeImageBackedTexture[] nativeImageBackedTextureArray;
   public final String name;

   public AnimatedTexture(String text) {
      this.name = text;
   }

   public void setLongValue2(long l) {
      this.longValue2 = l;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public NativeImageBackedTexture[] getNativeImageBackedTextureArray() {
      return this.nativeImageBackedTextureArray;
   }

   public long getLongValue() {
      return this.longValue;
   }

   public void setNativeImageBackedTextureArray(NativeImageBackedTexture[] nativeImageBackedTextures) {
      this.nativeImageBackedTextureArray = nativeImageBackedTextures;
   }

   public boolean testText(String text) {
      return this.name.equals(text);
   }

   public void setLongValue(long l) {
      this.longValue = l;
   }

   public void clear() {
      this.flag = false;
      if (this.nativeImageBackedTextureArray != null) {
         for (int i = 0; i < this.nativeImageBackedTextureArray.length; i++) {
            NativeImageBackedTexture nativeImageBackedTexture = this.nativeImageBackedTextureArray[i];
            if (nativeImageBackedTexture != null) {
               nativeImageBackedTexture.close();
            }

            this.nativeImageBackedTextureArray[i] = null;
         }

         this.nativeImageBackedTextureArray = null;
      }
   }

   public void run() {
      if (this.longValue++ > this.longValue2) {
         if (++this.intValue2 >= this.intValue) {
            this.intValue2 = 0;
         }

         this.longValue = 0L;
      }
   }

   public String describe() {
      String text3 = System.getProperty("user.home");
      Path path = Path.of(text3, "earthhack", "textures");
      if (!path.toFile().exists()) {
         return "Textures folder not found";
      }

      Path path2 = path.resolve(this.name + ".json");
      if (!path2.toFile().exists()) {
         return "File not found";
      }

      try {
         String text2 = Files.readString(path2);
         JsonElement jsonElement2 = JsonParser.parseString(text2);
         if (!jsonElement2.isJsonObject()) {
            return "Invalid format";
         }

         JsonObject jsonObject = jsonElement2.getAsJsonObject();
         this.setLongValue2(jsonObject.get("duration").getAsLong());
         JsonArray jsonArray = jsonObject.get("textures").getAsJsonArray();
         int i = 0;
         this.intValue = jsonArray.size();
         this.nativeImageBackedTextureArray = new NativeImageBackedTexture[this.intValue];

         for (JsonElement jsonElement : jsonArray) {
            String text = jsonElement.getAsString();
            NativeImageBackedTexture nativeImageBackedTexture = SkyboxTextureUtils.resolveNativeImageBackedTexture(path.resolve(text));
            if (nativeImageBackedTexture == null) {
               return "Texture %s was not found".formatted(text);
            }

            this.nativeImageBackedTextureArray[i] = nativeImageBackedTexture;
            i++;
         }

         this.flag = true;
         return null;
      } catch (Throwable throwable) {
         return "Failed to load: " + throwable.getMessage();
      }
   }

   public int getIntValue2() {
      return this.intValue2;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public void setIntValue2(int i) {
      this.intValue2 = i;
   }

   public NativeImageBackedTexture getNativeImageBackedTexture() {
      return this.nativeImageBackedTextureArray[this.intValue2];
   }

   public boolean check() {
      return this.nativeImageBackedTextureArray != null && this.flag;
   }

   public long getLongValue2() {
      return this.longValue2;
   }

   public void setIntValue(int i) {
      this.intValue = i;
   }

   public String getName() {
      return this.name;
   }
}
