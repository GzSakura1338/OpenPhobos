package org.phobos.core.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.JsonSerializable;
import org.phobos.type.KeyboardMouseEnum;
import org.phobos.type.NORMALCOMBINEDEnum;
import org.phobos.core.ClientHolder;
import org.phobos.core.util.NullSafe;

public class Macro implements JsonSerializable, Runnable {
   public int intValue = 0;
   public String[] textArray;
   public String name;
   public BindKey bindKey;
   public NORMALCOMBINEDEnum nORMALCOMBINEDEnum;

   @NotNull
   public static Macro createMacro(String text, BindKey bindKey, Macro... macros) {
      List<String> list = new ArrayList<>();

      for (Macro macro : macros) {
         list.addAll(Arrays.asList(macro.textArray));
      }

      return new Macro(text, list.toArray(new String[0]), bindKey, NORMALCOMBINEDEnum.COMBINED);
   }

   @Override
   public void run() {
      for (String text : this.textArray) {
         this.handleText(text);
      }
   }

   public NORMALCOMBINEDEnum getNORMALCOMBINEDEnum() {
      return this.nORMALCOMBINEDEnum;
   }

   @Override
   public JsonElement createJsonPrimitive() {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("Key", this.bindKey.getIntValue());
      jsonObject.addProperty("Type", this.bindKey.getKeyboardMouseEnum().toString());
      JsonArray jsonArray = new JsonArray();

      for (String text : this.textArray) {
         jsonArray.add(text);
      }

      jsonObject.add("Commands", jsonArray);
      jsonObject.addProperty("Type", this.nORMALCOMBINEDEnum.toString());
      return jsonObject;
   }

   public String getName() {
      return this.name;
   }

   public void handleText(String text) {
      ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
      if (clientPlayerEntity != null) {
         clientPlayerEntity.networkHandler.sendChatMessage(text);
      }
   }

   public Macro(String text, String[] texts, BindKey bindKey) {
      this(text, texts, bindKey, NORMALCOMBINEDEnum.NORMAL);
   }

   public Macro() {
   }

   public void setName(String text) {
      this.name = text;
   }

   public Macro(String text, String[] texts, BindKey bindKey, NORMALCOMBINEDEnum nORMALCOMBINEDEnum) {
      this.name = text;
      this.textArray = texts;
      this.bindKey = bindKey;
      this.nORMALCOMBINEDEnum = nORMALCOMBINEDEnum;
   }

   public BindKey getBindKey() {
      return this.bindKey;
   }

   @Nullable
   public static Macro createMacro2(String text, BindKey bindKey, Macro... macros) {
      List<String> list = new ArrayList<>();
      int i = 0;

      for (Macro macro : macros) {
         if (macro.getNORMALCOMBINEDEnum().equals(NORMALCOMBINEDEnum.NORMAL)) {
            list.addAll(Arrays.asList(macro.textArray));
            i++;
         }
      }

      return i == 0 ? null : new Macro(text, list.toArray(new String[0]), bindKey, NORMALCOMBINEDEnum.SWITCH);
   }

   public int getIntValue() {
      return this.intValue;
   }

   public String[] getTextArray() {
      return this.textArray;
   }

   public void setIntValue(int i) {
      this.intValue = i;
   }

   public void setTextArray(String[] texts) {
      this.textArray = texts;
   }

   public void setNORMALCOMBINEDEnum(NORMALCOMBINEDEnum nORMALCOMBINEDEnum) {
      this.nORMALCOMBINEDEnum = nORMALCOMBINEDEnum;
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      NullSafe.handleObject(
         jsonObject.get("Type"),
         jsonElementx -> {
            KeyboardMouseEnum keyboardMouseEnum = KeyboardMouseEnum.fromString(jsonElementx.getAsString());
            NullSafe.handleObject(
               jsonObject.get("Key"),
               jsonElementxx -> {
                  int i = jsonElementxx.getAsInt();
                  NullSafe.handleObject(
                     ClientHolder.getPhobosClient().getBindManager().resolveBindKey(keyboardMouseEnum, i),
                     bindKey -> this.bindKey = bindKey
                  );
               }
            );
         }
      );
      JsonArray jsonArray = jsonObject.get("Commands").getAsJsonArray();
      List<String> list = new ArrayList<>();

      for (JsonElement jsonElement2 : jsonArray) {
         list.add(jsonElement2.getAsString());
      }

      this.setTextArray(list.toArray(new String[0]));
      this.setNORMALCOMBINEDEnum(NORMALCOMBINEDEnum.fromString(jsonObject.get("Type").getAsString()));
   }
}
