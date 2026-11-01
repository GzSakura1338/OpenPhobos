package org.phobos.core.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.StringSetting;

public class CloudSettings {
   public final Setting<Boolean> capes;
   public final List<Setting<?>> settingList = new ArrayList<>();
   public final Setting<Boolean> skins = this.resolveSetting(new BooleanSetting("Skins", true));
   public final Setting<Boolean> shared;
   public final Setting<Boolean> theme;
   public final Setting<String> kit;
   public final Setting<Boolean> notifications;

   public CloudSettings() {
      this.capes = this.resolveSetting(new BooleanSetting("Capes", true));
      this.notifications = this.resolveSetting(new BooleanSetting("Notifications", true));
      this.shared = this.resolveSetting(new BooleanSetting("Shared", true));
      this.theme = this.resolveSetting(new BooleanSetting("Theme", true));
      this.kit = this.resolveSetting(new StringSetting("Kit", "none"));
   }

   public JsonElement getJsonElement() {
      JsonObject jsonObject = new JsonObject();

      for (Setting<?> setting : this.settingList) {
         JsonElement jsonElement = setting.createJsonPrimitive();
         if (jsonElement != null) {
            jsonObject.add(setting.getName(), jsonElement);
         }
      }

      return jsonObject;
   }

   public void handleJsonElement(JsonElement jsonElement) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();

      for (Setting<?> setting : this.settingList) {
         JsonElement jsonElement2 = jsonObject.get(setting.getName());
         if (jsonElement2 != null) {
            setting.handleJsonElement(jsonElement2);
         }
      }
   }

   public <S extends Setting<?>> S resolveSetting(S setting) {
      this.settingList.add(setting);
      return setting;
   }
}
