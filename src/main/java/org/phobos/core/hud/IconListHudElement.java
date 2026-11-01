package org.phobos.core.hud;

import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.setting.Setting;

public abstract class IconListHudElement<T> extends ListHudElement<T> {
   public final Setting<Boolean> icons = this.resolveSetting2(new BooleanSetting("Icons"));

   public abstract void handleDataType3(ListHudElement.DataType<T> var1);

   public IconListHudElement(String text) {
      super(text);
      this.icons.handleConsumer2(booleanValue -> {
         for (ListHudElement.DataType<T> dataType : this.dataTypeList) {
            this.handleDataType3(dataType);
         }
      });
   }
}
