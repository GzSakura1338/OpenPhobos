package org.phobos.core.openapi;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.Nullable;
import org.phobos.openapi.OpenSetting;
import org.phobos.core.setting.Setting;

public class SettingAdapter<T> implements OpenSetting<T> {
   public Setting<T> setting;

   @Override
   public T getObject() {
      Setting var1 = this.setting;
      return (T)var1.getObject();
   }

   @Nullable
   @Override
   public String describe() {
      Setting var1 = this.setting;
      return var1.getText();
   }

   @Override
   public void run() {
      Setting var1 = this.setting;
      var1.run2();
   }

   @Override
   public JsonElement getJsonElement() {
      Setting var1 = this.setting;
      return var1.createJsonPrimitive();
   }

   @Override
   public T getValue() {
      Setting var1 = this.setting;
      return (T)var1.getValue();
   }

   @Override
   public void handleObject(T object) {
      Setting var2 = this.setting;
      var2.setValue(object);
   }
}
