package org.phobos.api;

import com.google.gson.JsonElement;
import java.util.Objects;
import org.phobos.core.math.MathUtils;
import org.phobos.core.util.JsonUtils;

public interface NumberRange<N extends Number> extends JsonSerializable {
   static <N extends Number> NumberRange.Immutable<N> createImmutable(N number, N number2) {
      return new NumberRange.Immutable<>(number, number2);
   }

   N getNumber2();

   default N getNumber4() {
      return Objects.equals(this.getNumber2(), this.getNumber3()) ? this.getNumber2() : MathUtils.resolveNumber(this.getNumber2(), this.getNumber3());
   }

   default boolean testNumber(N number) {
      return Objects.equals(this.getNumber2(), number) && Objects.equals(this.getNumber3(), number);
   }

   @Override
   default JsonElement createJsonPrimitive() {
      return JsonUtils.createJsonPrimitive(this.getNumber2(), this.getNumber3());
   }

   static <N extends Number> NumberRange.Mutable<N> createMutable(N number, N number2) {
      return new NumberRange.Mutable<>(number, number2);
   }

   N getNumber3();

   default String describe() {
      return this.getNumber2().toString() + ", " + this.getNumber3().toString();
   }

   @Override
   default void handleJsonElement(JsonElement jsonElement) {
   }

   class Immutable<N extends Number> implements NumberRange<N> {
      public final N number;
      public final N number2;

      @Override
      public N getNumber2() {
         return this.number2;
      }

      @Override
      public String toString() {
         return this.describe();
      }

      public Immutable(N number, N number2) {
         this.number2 = number;
         this.number = number2;
      }

      @Override
      public N getNumber3() {
         return this.number;
      }
   }

   class Mutable<N extends Number> implements NumberRange<N> {
      public N number;
      public N number2;
      public N number3;

      public void run6() {
         this.setNumber(this.getNumber4());
      }

      public void setNumber(N number) {
         this.number = number;
      }

      @Override
      public N getNumber3() {
         return this.number3;
      }

      public N getNumber() {
         return this.number;
      }

      @Override
      public String toString() {
         return this.describe();
      }

      public void setNumber3(N number) {
         this.number3 = number;
      }

      public void handleNumber(N number, N number2) {
         this.setNumber2(number);
         this.setNumber3(number2);
      }

      @Override
      public N getNumber2() {
         return this.number2;
      }

      @Override
      public void handleJsonElement(JsonElement jsonElement) {
         JsonUtils.handleJsonElement(jsonElement, this.number2, this::setNumber2, this::setNumber3);
      }

      public Mutable(N number, N number2) {
         this.number2 = number;
         this.number3 = number2;
         this.setNumber(this.getNumber4());
      }

      public void setNumber2(N number) {
         this.number2 = number;
      }
   }
}
