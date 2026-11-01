package org.phobos.core.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.function.Function;
import java.util.function.Supplier;
import org.phobos.api.Named;

public class ConfigProperty<T> implements Named {
   public final Function<String, T> textFunction;
   public final String name;
   public final Function<T, String> function;
   public T value;
   public static final String text = "v0.1";
   public final Supplier<T> supplier;

   public void run2() {
      this.setValue(this.supplier.get());
   }

   public Function<String, T> getTextFunction() {
      return this.textFunction;
   }

   public void setValue(T object) {
      this.value = object;
   }

   public ConfigProperty(String text, Function<T, String> function, Function<String, T> function2, Supplier<T> supplier) {
      this.name = text;
      this.function = function;
      this.textFunction = function2;
      this.supplier = supplier;
   }

   public Function<T, String> getFunction() {
      return this.function;
   }

   public T getValue() {
      return this.value;
   }

   public Supplier<T> getSupplier() {
      return this.supplier;
   }

   public void handleJsonObject(JsonObject jsonObject) {
      JsonElement jsonElement = jsonObject.get(this.name);
      if (jsonElement != null) {
         try {
            String text = jsonElement.getAsString();
            T object = this.textFunction.apply(text);
            this.setValue(object);
         } catch (Throwable var5) {
         }
      }
   }

   @Override
   public String getName() {
      return this.name;
   }

   public void handleJsonObject2(JsonObject jsonObject) {
      if (this.value != null) {
         try {
            String text = this.function.apply(this.value);
            jsonObject.addProperty(this.name, text);
         } catch (Throwable var3) {
         }
      }
   }
}
