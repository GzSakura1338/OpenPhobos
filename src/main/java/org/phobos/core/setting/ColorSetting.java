package org.phobos.core.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.awt.Color;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.gui.element.ColorElement;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.gui.ModuleElement;

public class ColorSetting extends Setting<Color> {
   public Color color;
   public boolean flag;
   public boolean flag2;
   public float floatValue;
   public boolean flag3;
   public float floatValue2;
   public float floatValue3 = 1.0F;

   public boolean testText(String text) {
      try {
         this.setValue(ColorUtils.createColor7(Integer.parseInt(text)));
         return true;
      } catch (Throwable throwable) {
         return false;
      }
   }

   @Override
   public JsonElement createJsonPrimitive() {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("component", ColorUtils.computeColor(this.getValue()));
      JsonObject jsonObject2 = new JsonObject();
      jsonObject2.addProperty("rainbow", this.isFlag());
      jsonObject2.addProperty("static", this.isFlag3());
      jsonObject2.addProperty("sync", this.isFlag2());
      jsonObject2.addProperty("saturation", this.floatValue3);
      jsonObject2.addProperty("brightness", this.floatValue);
      jsonObject2.addProperty("speed", this.floatValue2);
      jsonObject.add("dynamic", jsonObject2);
      return jsonObject;
   }

   @Override
   public boolean testString(@NotNull String... texts) {
      try {
         if (texts.length < 2) {
            throw new IllegalStateException("Invalid arguments");
         }

         switch (texts[0]) {
            case "red":
               this.handleInt2(Integer.parseInt(texts[1]));
               return true;
            case "green":
               this.handleInt3(Integer.parseInt(texts[1]));
               return true;
            case "blue":
               this.handleInt4(Integer.parseInt(texts[1]));
               return true;
            case "alpha":
               this.handleInt(Integer.parseInt(texts[1]));
               return true;
            default:
               throw new IllegalStateException("Invalid argument");
         }
      } catch (Throwable throwable) {
         return false;
      }
   }

   public void setValue(Color color) {
      super.setValue(color);
      this.color = color;
   }

   public ColorSetting(String text, Color color) {
      super(text, "", color);
      this.floatValue = 1.0F;
      this.floatValue2 = 1.0F;
      this.setColor(color);
   }

   public ColorSetting(String text, @Nullable String text2, Color color) {
      super(text, text2, color);
      this.floatValue = 1.0F;
      this.floatValue2 = 1.0F;
      this.setColor(color);
   }

   public void handleColor(Color color) {
      Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue(), this.getValue().getAlpha());
      this.handleColor2(color2);
   }

   public int value() {
      return this.getValue().getRed();
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      if (jsonElement instanceof JsonPrimitive jsonPrimitive && jsonPrimitive.isString()) {
         String text = jsonPrimitive.getAsString();
         this.handleText(text);
      } else {
         JsonObject jsonObject = jsonElement.getAsJsonObject();
         int i = jsonObject.get("component").getAsInt();
         this.setValue(ColorUtils.createColor7(i));
         JsonObject jsonObject2 = jsonObject.get("dynamic").getAsJsonObject();
         this.setFlag(jsonObject2.get("rainbow").getAsBoolean());
         this.setFlag3(jsonObject2.get("static").getAsBoolean());
         this.setFlag2(jsonObject2.get("sync").getAsBoolean());
         this.setFloatValue3(jsonObject2.get("saturation").getAsFloat());
         this.setFloatValue(jsonObject2.get("brightness").getAsFloat());
         this.setFloatValue2(jsonObject2.get("speed").getAsFloat());
      }
   }

   public int value2() {
      return this.getValue().getGreen();
   }

   public ColorSetting getColorSetting() {
      this.setFlag2(true);
      return this;
   }

   public void handleInt(int i) {
      this.setValue(new Color(this.value(), this.value2(), this.value4(), i));
   }

   public void handleInt2(int i) {
      this.setValue(new Color(i, this.value2(), this.value4(), this.value3()));
   }

   public int value3() {
      return this.getValue().getAlpha();
   }

   public void handleInt3(int i) {
      this.setValue(new Color(this.value(), i, this.value4(), this.value3()));
   }

   public int value4() {
      return this.getValue().getBlue();
   }

   public ColorSetting resolveColorSetting(Consumer<ColorSetting> consumer) {
      consumer.accept(this);
      return this;
   }

   public void handleText(String text) {
      String[] texts = text.split("&");
      int i = Integer.parseInt(texts[0]);
      int i2 = Integer.parseInt(texts[1]);
      int i3 = Integer.parseInt(texts[2]);
      int i4 = Integer.parseInt(texts[3]);
      Color color = new Color(i, i2, i3, i4);
      this.setValue(color);
      this.setFlag(Boolean.parseBoolean(texts[4]));
      this.setFlag3(Boolean.parseBoolean(texts[5]));
      this.setFlag2(Boolean.parseBoolean(texts[6]));
      if (texts.length == 10) {
         this.setFloatValue3(Float.parseFloat(texts[7]));
         this.setFloatValue(Float.parseFloat(texts[8]));
         this.setFloatValue2(Float.parseFloat(texts[9]));
      }
   }

   public void handleInt4(int i) {
      this.setValue(new Color(this.value(), this.value2(), i, this.value3()));
   }

   public boolean isFlag() {
      return this.flag;
   }

   public void setFlag2(boolean flag) {
      this.flag2 = flag;
   }

   public boolean isFlag3() {
      return this.flag3;
   }

   public float getFloatValue22() {
      return this.floatValue2;
   }

   public void setColor(Color color) {
      this.color = color;
   }

   public void setFloatValue(float f) {
      this.floatValue = f;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public void setFloatValue2(float f) {
      this.floatValue2 = f;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   @Override
   public GuiElement createEnumElement(GuiManager guiManager, GuiPanel guiPanel, String text, ModuleElement moduleElement) {
      return new ColorElement(guiManager, guiPanel, 0.0F, 0.0F, 0.0F, 0.0F, moduleElement, text, this);
   }

   public void setFloatValue3(float f) {
      this.floatValue3 = f;
   }

   public void handleColor2(Color color) {
      super.setValue(color);
   }

   public Color getColor() {
      return this.color;
   }

   @Override
   public String toString() {
      return "(%s,%s,%s,%s)".formatted(this.value(), this.value2(), this.value4(), this.value3());
   }

   public float getFloatValue3() {
      return this.floatValue3;
   }

   public boolean isFlag2() {
      return this.flag2;
   }

   public void setFlag3(boolean flag) {
      this.flag3 = flag;
   }
}
