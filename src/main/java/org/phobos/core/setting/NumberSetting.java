package org.phobos.core.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.ElementCreator;
import org.phobos.core.gui.element.DoubleNumberElement;
import org.phobos.core.gui.element.FloatNumberElement;
import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.gui.element.IntegerNumberElement;
import org.phobos.core.gui.ModuleElement;

public class NumberSetting<N extends Number> extends Setting<N> implements ElementCreator {
   public final List<Function<String, N>> functionList;
   public final N number;
   public final String text;
   public boolean flag2 = false;
   public final N number2;
   public final List<Function<N, String>> functionList2;

   public boolean check() {
      return !this.functionList2.isEmpty();
   }

   public <S, T extends Number> T resolveNumber(Class<T> classValue, S object) {
      return (T)((Number)object);
   }

   public NumberSetting<N> resolveNumberSetting(@NotNull String text, @NotNull N number) {
      this.functionList2.add(number2 -> Objects.equals(number2, number) ? text : null);
      this.functionList.add(text2 -> text2.equalsIgnoreCase(text) ? number : null);
      return this;
   }

   @Override
   public boolean testString(@NotNull String... texts) {
      String text = texts[0];
      N number = this.getValue();
      if (number instanceof Integer) {
         return this.testObject(
            MathHelper.clamp(Integer.parseInt(text), this.resolveNumber(Integer.class, this.number), this.resolveNumber(Integer.class, this.number2))
         );
      } else if (number instanceof Float) {
         return this.testObject(
            MathHelper.clamp(Float.parseFloat(text), this.resolveNumber(Float.class, this.number), this.resolveNumber(Float.class, this.number2))
         );
      } else if (number instanceof Double) {
         return this.testObject(
            MathHelper.clamp(Double.parseDouble(text), this.resolveNumber(Double.class, this.number), this.resolveNumber(Double.class, this.number2))
         );
      } else {
         return number instanceof Long
            ? this.testObject(MathHelper.clamp(Long.parseLong(text), this.resolveNumber(Long.class, this.number), this.resolveNumber(Long.class, this.number2)))
            : false;
      }
   }

   public List<Function<String, N>> getFunctionList() {
      return this.functionList;
   }

   public NumberSetting(String text, @Nullable String text2, N number, N number2, N number3) {
      super(text, text2, number);
      this.functionList = new ArrayList<>();
      this.functionList2 = new ArrayList<>();
      this.number = number2;
      this.number2 = number3;
      this.text = "";
   }

   @Override
   public GuiElement createEnumElement(GuiManager guiManager, GuiPanel guiPanel, String text, ModuleElement moduleElement) {
      N number = this.getValue();
      if (number instanceof Integer) {
         NumberSetting<Integer> numberSetting = (NumberSetting<Integer>)(Object)this;
         return new IntegerNumberElement(
            guiManager,
            guiPanel,
            0.0F,
            0.0F,
            0.0F,
            0.0F,
            moduleElement,
            text,
            numberSetting,
            numberSetting.getNumber2(),
            numberSetting.getNumber3()
         );
      } else if (number instanceof Float) {
         NumberSetting<Float> numberSetting2 = (NumberSetting<Float>)(Object)this;
         return new FloatNumberElement(
            guiManager,
            guiPanel,
            0.0F,
            0.0F,
            0.0F,
            0.0F,
            moduleElement,
            text,
            numberSetting2,
            numberSetting2.getNumber2(),
            numberSetting2.getNumber3()
         );
      } else if (number instanceof Double) {
         NumberSetting<Double> numberSetting3 = (NumberSetting<Double>)(Object)this;
         return new DoubleNumberElement(
            guiManager,
            guiPanel,
            0.0F,
            0.0F,
            0.0F,
            0.0F,
            moduleElement,
            text,
            numberSetting3,
            numberSetting3.getNumber2(),
            numberSetting3.getNumber3()
         );
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public boolean check2() {
      return !this.functionList.isEmpty();
   }

   @Override
   public JsonElement createJsonPrimitive() {
      N number = this.getValue();
      if (number instanceof Integer) {
         return new JsonPrimitive(number.intValue());
      } else if (number instanceof Float) {
         return new JsonPrimitive(number.floatValue());
      } else if (number instanceof Double) {
         return new JsonPrimitive(number.doubleValue());
      } else {
         return number instanceof Long ? new JsonPrimitive(number.longValue()) : new JsonPrimitive("Error");
      }
   }

   public String getText2() {
      return this.text;
   }

   public NumberSetting(String text, N number, N number2, N number3) {
      super(text, "", number);
      this.functionList = new ArrayList<>();
      this.functionList2 = new ArrayList<>();
      this.number = number2;
      this.number2 = number3;
      this.text = "";
   }

   public NumberSetting<N> getNumberSetting() {
      this.flag2 = true;
      return this;
   }

   public boolean isFlag2() {
      return this.flag2;
   }

   public String describeNumber(N number) {
      if (this.functionList2.isEmpty()) {
         return null;
      }

      for (Function<N, String> function : this.functionList2) {
         String text = function.apply(number);
         if (text != null) {
            return text;
         }
      }

      return null;
   }

   public N getNumber2() {
      return this.number;
   }

   public boolean testText(String text) {
      if (!this.check2()) {
         return false;
      }

      for (Function<String, N> function : this.functionList) {
         N number = function.apply(text);
         if (number != null) {
            this.setValue(number);
            return true;
         }
      }

      return false;
   }

   public List<Function<N, String>> getFunctionList2() {
      return this.functionList2;
   }

   public NumberSetting(String text, N number, N number2, N number3, String text2) {
      super(text, "", number);
      this.functionList = new ArrayList<>();
      this.functionList2 = new ArrayList<>();
      this.number = number2;
      this.number2 = number3;
      this.text = text2;
   }

   public N getNumber3() {
      return this.number2;
   }

   public NumberSetting(String text, @Nullable String text2, N number, N number2, N number3, String text3) {
      super(text, text2, number);
      this.functionList = new ArrayList<>();
      this.functionList2 = new ArrayList<>();
      this.number = number2;
      this.number2 = number3;
      this.text = text3;
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      N number = this.getValue();
      if (number instanceof Integer) {
         this.testObject(
            MathHelper.clamp(jsonElement.getAsInt(), this.resolveNumber(Integer.class, this.number), this.resolveNumber(Integer.class, this.number2))
         );
      }

      if (number instanceof Float) {
         this.testObject(
            MathHelper.clamp(jsonElement.getAsFloat(), this.resolveNumber(Float.class, this.number), this.resolveNumber(Float.class, this.number2))
         );
      }

      if (number instanceof Double) {
         this.testObject(
            MathHelper.clamp(jsonElement.getAsDouble(), this.resolveNumber(Double.class, this.number), this.resolveNumber(Double.class, this.number2))
         );
      }

      if (number instanceof Long) {
         this.testObject(MathHelper.clamp(jsonElement.getAsLong(), this.resolveNumber(Long.class, this.number), this.resolveNumber(Long.class, this.number2)));
      }
   }
}
