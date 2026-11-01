package org.phobos.core.setting;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.MixinClassNames;
import org.phobos.api.NumberRange;
import org.phobos.api.NumberRange;
import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.math.MathUtils;
import org.phobos.core.gui.ModuleElement;
import org.phobos.core.gui.element.MutableDoubleElement;
import org.phobos.core.gui.element.MutableFloatElement;
import org.phobos.core.gui.element.MutableIntegerElement;

public class MutableNumberSetting<N extends Number> extends Setting<NumberRange.Mutable<N>> {
   public final N number;
   public final N number2;
   public final N number3;
   @Nullable
   public final String text;

   public MutableNumberSetting(String text, NumberRange.Mutable<N> mutable, N number, N number2, String... texts) {
      this(text, mutable, number, number2, MathUtils.resolveNumber2(number), texts);
   }

   public N getNumber() {
      return this.number;
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      this.getValue().handleJsonElement(jsonElement);
   }

   public N getNumber4() {
      N number = this.getNumber22();
      NumberRange.Mutable<N> mutable = this.getValue();
      if (number instanceof Integer integer) {
         return MixinClassNames.CCS.resolveObject2(
            MathUtils.computeInt5(MixinClassNames.CCS.resolveObject2(mutable.getNumber2(), integer), MixinClassNames.CCS.resolveObject2(mutable.getNumber3(), integer)),
            this.getNumber22()
         );
      } else if (number instanceof Float floatValue) {
         return MixinClassNames.CCS.resolveObject2(
            MathUtils.computeFloat8(
               MixinClassNames.CCS.resolveObject2(mutable.getNumber2(), floatValue), MixinClassNames.CCS.resolveObject2(mutable.getNumber3(), floatValue)
            ),
            this.getNumber22()
         );
      } else if (number instanceof Double doubleValue) {
         return MixinClassNames.CCS.resolveObject2(
            MathUtils.computeDouble9(
               MixinClassNames.CCS.resolveObject2(mutable.getNumber2(), doubleValue), MixinClassNames.CCS.resolveObject2(mutable.getNumber3(), doubleValue)
            ),
            this.getNumber22()
         );
      } else if (number instanceof Long longValue) {
         return MixinClassNames.CCS.resolveObject2(
            MathUtils.computeLong5(
               MixinClassNames.CCS.resolveObject2(mutable.getNumber2(), longValue), MixinClassNames.CCS.resolveObject2(mutable.getNumber3(), longValue)
            ),
            this.getNumber22()
         );
      } else {
         throw new UnsupportedOperationException("Invalid type");
      }
   }

   @Override
   public GuiElement createEnumElement(GuiManager guiManager, GuiPanel guiPanel, String text, ModuleElement moduleElement) {
      N number = this.getNumber22();
      if (number instanceof Integer) {
         MutableNumberSetting<Integer> mutableNumberSetting = (MutableNumberSetting<Integer>)(Object)this;
         return new MutableIntegerElement(
            guiManager,
            guiPanel,
            0.0F,
            0.0F,
            0.0F,
            0.0F,
            moduleElement,
            text,
            mutableNumberSetting,
            mutableNumberSetting.getNumber2(),
            mutableNumberSetting.getNumber3()
         );
      } else if (number instanceof Float) {
         MutableNumberSetting<Float> mutableNumberSetting2 = (MutableNumberSetting<Float>)(Object)this;
         return new MutableFloatElement(
            guiManager,
            guiPanel,
            0.0F,
            0.0F,
            0.0F,
            0.0F,
            moduleElement,
            text,
            mutableNumberSetting2,
            mutableNumberSetting2.getNumber2(),
            mutableNumberSetting2.getNumber3()
         );
      } else if (number instanceof Double) {
         MutableNumberSetting<Double> mutableNumberSetting3 = (MutableNumberSetting<Double>)(Object)this;
         return new MutableDoubleElement(
            guiManager,
            guiPanel,
            0.0F,
            0.0F,
            0.0F,
            0.0F,
            moduleElement,
            text,
            mutableNumberSetting3,
            mutableNumberSetting3.getNumber2(),
            mutableNumberSetting3.getNumber3()
         );
      } else {
         throw new UnsupportedOperationException("Invalid type: {name = %s, parent = %s}".formatted(this.getName(), this.getSettingContainer()));
      }
   }

   public MutableNumberSetting(String text, NumberRange.Mutable<N> mutable, N number, N number2, N number3, String... texts) {
      super(text, "", mutable);
      this.number2 = number;
      this.number3 = number2;
      this.number = number3;
      this.text = texts.length == 1 ? texts[0] : null;
   }

   @Nullable
   public String getText2() {
      return this.text;
   }

   public N getNumber3() {
      return this.number3;
   }

   public MutableNumberSetting(String text, N number, N number2, N number3, String... texts) {
      this(text, NumberRange.createMutable(number, number), number2, number3, texts);
   }

   @Override
   public JsonElement createJsonPrimitive() {
      return this.getValue().createJsonPrimitive();
   }

   public N getNumber2() {
      return this.number2;
   }

   public N getNumber22() {
      return this.number2;
   }
}
