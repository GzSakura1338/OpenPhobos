package org.phobos.module.misc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashSet;
import java.util.Set;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.StringSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.config.ConfigManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.AutoLoginReceiveListener;
import org.phobos.event.AutoLoginSendLoginHelloListener;
import org.phobos.module.Module;

public class AutoLoginModule extends Module {
   public final Setting<Long> timeout;
   public final Setting<String> password;
   public final Setting<String> createPassword;
   public final Setting<String> createLogin;
   public final TimeGate.Normal normal;
   public final Setting<String> trigger;
   public final Setting<Boolean> list;
   public final Setting<AutoLoginModule.Mode> mode = new EnumSetting<>("Mode", AutoLoginModule.Mode.Single).resolveSetting6(this);
   public final AutoLoginModule.LoginMap loginMap;
   public final Setting<Boolean> create;
   public final Setting<String> command;

   @Override
   public JsonElement createJsonPrimitive() {
      if (ConfigManager.flag2) {
         return super.createJsonPrimitive();
      }

      JsonObject jsonObject = super.createJsonPrimitive().getAsJsonObject();
      jsonObject.add("Data", this.loginMap.getJsonElement());
      return jsonObject;
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      super.handleJsonElement(jsonElement);
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      if (jsonObject.has("Data")) {
         JsonElement jsonElement2 = jsonObject.get("Data");
         this.loginMap.handleJsonElement(jsonElement2);
      }
   }

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   public AutoLoginModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoLogin", Categories.misc, "Automatically logs you in using stored database");
      this.trigger = new StringSetting("Trigger", "/login").resolveSetting6(this);
      this.command = new StringSetting("Command", "login %password%").resolveSetting6(this);
      this.timeout = new LongNumberSetting("Timeout", 5000L, 250L, 10000L).resolveSetting6(this);
      this.password = new StringSetting("Password", "123")
         .resolveSetting3(this.resolveVisibility2(this.mode, AutoLoginModule.Mode.Single))
         .resolveSetting6(this);
      this.create = new BooleanSetting("Create").resolveSetting6(this);
      this.createLogin = new StringSetting("CreateLogin", "user").resolveSetting6(this);
      this.createPassword = new StringSetting("CreatePassword", "123").resolveSetting6(this);
      this.list = new BooleanSetting("List").resolveSetting3(this.resolveVisibility2(this.mode, AutoLoginModule.Mode.Multi)).resolveSetting6(this);
      this.normal = new TimeGate.Normal();
      this.loginMap = new AutoLoginModule.LoginMap();
      this.resolveSettingGroup(this.create, this.createLogin, this.createPassword)
         .resolveSettingGroup5(this.resolveVisibility2(this.mode, AutoLoginModule.Mode.Multi));
      this.handleListener(new AutoLoginSendLoginHelloListener(this));
      this.handleListener(new AutoLoginReceiveListener(this));
      this.command.handleConsumer2(text -> {
         if (!text.contains("%password%")) {
            this.handleText6("Command must contain \"%password%\"");
            this.command.setValue(this.command.getObject());
         }
      });
      this.create.handleConsumer2(booleanValue -> {
         if (booleanValue) {
            if (this.createLogin.getValue().isEmpty()) {
               this.handleText6("Can't register an account with no name!");
            } else if (this.createPassword.getValue().isEmpty()) {
               this.handleText6("Can't register an account with no password!");
            } else {
               this.loginMap.handleText(this.createLogin.getValue(), this.createPassword.getValue());
            }

            this.create.setValue(false);
         }
      });
      this.list.handleConsumer2(booleanValue -> {
         if (booleanValue) {
            for (AutoLoginModule.Login login : this.loginMap.loginSet) {
               this.handleText6(login.getText2() + " -> " + login.getText());
            }

            this.list.setValue(false);
         }
      });
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }

   public static class Login {
      public final String text;
      public final String text2;

      public JsonElement getJsonElement() {
         JsonObject jsonObject = new JsonObject();
         jsonObject.addProperty("Username", this.text2);
         jsonObject.addProperty("Password", this.text);
         return jsonObject;
      }

      public String getText2() {
         return this.text2;
      }

      public String getText() {
         return this.text;
      }

      public Login(String text, String text2) {
         this.text2 = text;
         this.text = text2;
      }
   }

   public static class LoginMap {
      public final Set<AutoLoginModule.Login> loginSet = new HashSet<>();

      public JsonElement getJsonElement() {
         JsonArray jsonArray = new JsonArray();

         for (AutoLoginModule.Login login : this.loginSet) {
            jsonArray.add(login.getJsonElement());
         }

         return jsonArray;
      }

      public void handleText(String text, String text2) {
         this.loginSet.add(new AutoLoginModule.Login(text, text2));
      }

      public AutoLoginModule.Login resolveLogin(String text) {
         for (AutoLoginModule.Login login : this.loginSet) {
            if (login.text2.equals(text)) {
               return login;
            }
         }

         return null;
      }

      public void handleJsonElement(JsonElement jsonElement) {
         for (JsonElement jsonElement2 : jsonElement.getAsJsonArray()) {
            JsonObject jsonObject = jsonElement2.getAsJsonObject();
            this.loginSet.add(new AutoLoginModule.Login(jsonObject.get("Username").getAsString(), jsonObject.get("Password").getAsString()));
         }
      }

      public boolean contains(String text) {
         for (AutoLoginModule.Login login : this.loginSet) {
            if (login.text2.equals(text)) {
               return true;
            }
         }

         return false;
      }
   }

   public enum Mode {
      Single,
      Multi;
   }
}
