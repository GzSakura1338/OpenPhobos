package org.phobos.core.hud;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.OverrideOnly;
import org.phobos.type.CenterRightEnum;
import org.phobos.type.TopCenterEnum;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.Setting;

public abstract class ListHudElement<T> extends HudElement {
   public final List<ListHudElement.DataType<T>> dataTypeList;
   public final Setting<Float> spacing;
   public final Supplier<Boolean> booleanValueSupplier = this.getSupplier();

   @OverrideOnly
   public float computeDataType(ListHudElement.DataType<T> dataType) {
      return dataType.flag ? 0.3F : 0.1F;
   }

   public ListHudElement(String text) {
      super(text, 0.0F, 0.0F, 10.0F, 10.0F);
      this.spacing = this.resolveSetting2(new NumberSetting<>("Spacing", 12.0F, 10.0F, 15.0F).resolveNumberSetting("Sync", 12.0F));
      this.dataTypeList = this.createCopyOnWriteArrayList();
   }

   public void handleDrawContext(DrawContext drawContext) {
      this.dataTypeList.removeIf(dataType -> dataType.flag && dataType.getFloatAnimator3().getFloatValue2() < 0.01F);
      int i = this.topCenterEnum.equals(TopCenterEnum.Bottom) ? -10 : 0;
      float f10 = i;
      float f11 = 0.0F;
      this.setFlag8(false);
      float f12 = this.spacing.getValue();

      for (ListHudElement.DataType<T> dataType : this.dataTypeList) {
         ListHudElement.ReservedString reservedString = dataType.getDataTypeBiFunction().apply(dataType, dataType.getObject());
         ListHudElement.IconRenderer<T> iconRenderer = dataType.iconRenderer;
         float f4 = this.computeText2(reservedString.getText2());
         float f5 = dataType.flag ? 0.0F : f4;
         dataType.floatAnimator2.setFloatValue(f5);
         dataType.floatAnimator2.handleFloat3(5.0F, dataType.flag ? 0.2F : 0.1F);
         float f6 = dataType.floatAnimator2.getFloatValue2();
         f11 = Math.max(f11, f6 + (iconRenderer != null ? 12 : 0));
         dataType.floatAnimator.setFloatValue(this.computeDataType3(dataType));
         dataType.floatAnimator3.setFloatValue(dataType.floatAnimator.getFloatValue2() < 0.5F && dataType.flag ? 0.0F : 1.0F);
         dataType.floatAnimator3.handleFloat3(10.0F, 0.1F);
         dataType.floatAnimator.handleFloat3(10.0F, this.computeDataType(dataType));
         if (dataType.getFloatAnimator().getFloatValue2() < 0.1F) {
            f10 += this.computeFloat(dataType.floatAnimator3.getFloatValue2() * f12);
         } else {
            float f7 = this.check2() ? this.getFloatValue2() - f6 : this.getFloatValue2() - f4 + f6;
            float f8 = this.getFloatValue() + f10;
            if (this.background.getValue()) {
               float f = iconRenderer != null ? (this.check2() ? -12 : 12) : 0.0F;
               float f2 = this.check2() ? this.getFloatValue2() - f6 + f : this.getFloatValue2();
               float f3 = this.check2() ? this.getFloatValue2() : this.getFloatValue2() + f6 + f;
               this.handleFloat(f2, f8, f3 - 1.0F, f8 + 9.0F);
            }

            if (iconRenderer != null) {
               f7 += this.check2() ? -12 : 12;
               double d = f7 + (this.check2() ? f4 + 3.0F : -11.0F);
               double d2 = f8 - 0.5;
               this.phobosClient
                  .getRenderManager()
                  .handleConsumer(drawContextx -> iconRenderer.handleDataType(dataType, drawContextx.getMatrices(), d, d2, 10.0));
            }

            label103: {
               float f9 = dataType.floatAnimator.getFloatValue2();
               String text = reservedString.getText();
               if (dataType.function != null) {
                  Color color = dataType.function.apply(dataType.object);
                  if (color != null) {
                     this.handleDrawContext8(drawContext, text, f7, f8, ColorUtils.createColor6(color, f9));
                     break label103;
                  }
               }

               this.handleDrawContext7(drawContext, text, f7, f8, f9);
            }

            f10 += this.computeFloat(dataType.floatAnimator3.getFloatValue2() * f12);
         }
      }

      this.setFlag8(true);
      this.setFloatValue4(this.dataTypeList.isEmpty() ? 0.0F : f10 - i + (!this.topCenterEnum.equals(TopCenterEnum.Bottom) ? -2 : 2));
      int i2 = this.centerRightEnum.equals(CenterRightEnum.Right) ? -1 : 1;
      float f13 = f11 * i2;
      switch (i2) {
         case -1:
            this.setFloatValue3(Math.min(-0.01F, f13));
            break;
         case 1:
            this.setFloatValue3(Math.max(0.01F, f13));
      }
   }

   public boolean testPredicate(Predicate<ListHudElement.DataType<T>> predicate) {
      return this.dataTypeList.stream().anyMatch(predicate);
   }

   public float computeFloat(float f) {
      return !this.topCenterEnum.equals(TopCenterEnum.Bottom) ? f : -f;
   }

   @Override
   public boolean alwaysTrue() {
      return true;
   }

   public void handleDataType(ListHudElement.DataType<T> dataType) {
      if (this.booleanValueSupplier.get()) {
         this.dataTypeList.addFirst(dataType);
      } else {
         this.dataTypeList.add(dataType);
      }
   }

   public int size() {
      return this.dataTypeList.size();
   }

   public List<ListHudElement.DataType<T>> createCopyOnWriteArrayList() {
      return new CopyOnWriteArrayList<>();
   }

   public void handleDataType2(ListHudElement.DataType<T> dataType) {
      dataType.setFlag(true);
   }

   public int computeDataType2(ListHudElement.DataType<T> dataType) {
      return this.dataTypeList.indexOf(dataType);
   }

   public void handlePredicate(Predicate<ListHudElement.DataType<T>> predicate) {
      this.dataTypeList.stream().filter(predicate).forEach(this::handleDataType2);
   }

   public float computeDataType3(ListHudElement.DataType<T> dataType) {
      return dataType.flag ? 0.0F : 1.0F;
   }

   @Override
   public float[] values() {
      return values2(this.getFloatValue2(), this.getFloatValue(), this.getFloatValue3(), this.getFloatValue4());
   }

   public void handleFlag(boolean flag) {
      if (flag) {
         this.dataTypeList.clear();
      } else {
         for (ListHudElement.DataType<T> dataType : this.dataTypeList) {
            this.handleDataType2(dataType);
         }
      }
   }

   public Supplier<Boolean> getSupplier() {
      return this.resolveSetting2(new BooleanSetting("Reverse"));
   }

   public void run() {
      this.handleDataType2(this.dataTypeList.getFirst());
   }

   public void run2() {
      this.handleDataType2(this.dataTypeList.getLast());
   }

   public static class DataType<T> {
      public final BiFunction<ListHudElement.DataType<T>, T, ListHudElement.ReservedString> dataTypeBiFunction;
      @Nullable
      public ListHudElement.IconRenderer<T> iconRenderer;
      public final FloatAnimator floatAnimator = new FloatAnimator(1.0F, 1.0F);
      @Nullable
      public T object;
      public Object object2;
      public boolean flag;
      public final long longValue;
      public final FloatAnimator floatAnimator2 = new FloatAnimator();
      public final Function<T, Color> function;
      public final FloatAnimator floatAnimator3 = new FloatAnimator(0.0F, 1.0F);

      public void setObject(@Nullable T object) {
         this.object = object;
      }

      public ListHudElement.DataType<T> resolveDataType(
         Function<ListHudElement.DataType<T>, ListHudElement.IconRenderer<T>> function
      ) {
         this.iconRenderer = function.apply(this);
         return this;
      }

      public FloatAnimator getFloatAnimator3() {
         return this.floatAnimator3;
      }

      public Function<T, Color> getFunction() {
         return this.function;
      }

      public static <T> ListHudElement.DataType<T> createDataType(@NotNull T object, Function<T, ListHudElement.ReservedString> function) {
         return new ListHudElement.DataType<>(object, (dataType, objectx) -> function.apply((T)objectx), System.currentTimeMillis(), null);
      }

      public static <T> ListHudElement.DataType<T> createDataType2(
         @NotNull T object, BiFunction<ListHudElement.DataType<T>, T, ListHudElement.ReservedString> biFunction
      ) {
         return new ListHudElement.DataType<>(object, biFunction, System.currentTimeMillis(), null);
      }

      @Nullable
      public ListHudElement.IconRenderer<T> getIconRenderer() {
         return this.iconRenderer;
      }

      public static <T> ListHudElement.DataType<T> createDataType3(@Nullable T object, ListHudElement.ReservedString reservedString) {
         return new ListHudElement.DataType<>(object, (dataType, objectx) -> reservedString, System.currentTimeMillis(), null);
      }

      public void setObject2(Object object) {
         this.object2 = object;
      }

      public DataType(
         @Nullable T object,
         BiFunction<ListHudElement.DataType<T>, T, ListHudElement.ReservedString> biFunction,
         long l,
         Function<T, Color> function
      ) {
         this.object = object;
         this.dataTypeBiFunction = biFunction;
         this.longValue = l;
         this.function = function;
      }

      @Nullable
      public T getObject() {
         return this.object;
      }

      public FloatAnimator getFloatAnimator() {
         return this.floatAnimator;
      }

      public static <T> ListHudElement.DataType<T> createDataType4(
         @NotNull T object, Function<T, ListHudElement.ReservedString> function, Function<T, Color> function2
      ) {
         return new ListHudElement.DataType<>(object, (dataType, objectx) -> function.apply((T)objectx), System.currentTimeMillis(), function2);
      }

      public void setIconRenderer(@Nullable ListHudElement.IconRenderer<T> iconRenderer) {
         this.iconRenderer = iconRenderer;
      }

      public boolean isFlag() {
         return this.flag;
      }

      public ListHudElement.DataType<T> resolveDataType2(Supplier<ListHudElement.IconRenderer<T>> supplier) {
         this.iconRenderer = supplier.get();
         return this;
      }

      public static <T> ListHudElement.DataType<T> resolveDataType3(ListHudElement.ReservedString reservedString) {
         return createDataType3(null, reservedString);
      }

      public void setFlag(boolean flag) {
         this.flag = flag;
      }

      public FloatAnimator getFloatAnimator2() {
         return this.floatAnimator2;
      }

      public ListHudElement.DataType<T> resolveDataType4(Consumer<ListHudElement.DataType<T>> consumer) {
         consumer.accept(this);
         return this;
      }

      public long getLongValue() {
         return this.longValue;
      }

      public Object getObject2() {
         return this.object2;
      }

      public BiFunction<ListHudElement.DataType<T>, T, ListHudElement.ReservedString> getDataTypeBiFunction() {
         return this.dataTypeBiFunction;
      }
   }

   public interface IconRenderer<T> {
      void handleDataType(ListHudElement.DataType<T> var1, MatrixStack var2, double var3, double var5, double var7);
   }

   public static class ReservedString {
      public final String text;
      public final String text2;

      public static ListHudElement.ReservedString createReservedString(String text) {
         return new ListHudElement.ReservedString(text, text);
      }

      public static ListHudElement.ReservedString createReservedString2(String text, String text2) {
         return new ListHudElement.ReservedString(text, text2);
      }

      public String getText() {
         return this.text;
      }

      public ReservedString(String text, String text2) {
         this.text = text;
         this.text2 = text2;
      }

      public String getText2() {
         return this.text2;
      }
   }
}
