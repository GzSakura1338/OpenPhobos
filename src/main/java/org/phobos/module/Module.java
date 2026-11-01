package org.phobos.module;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import org.apache.commons.lang3.function.TriConsumer;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.EntryContainer;
import org.phobos.api.SettingHolder;
import org.phobos.api.Toggleable;
import org.phobos.api.Named;
import org.phobos.api.TextProvider;
import org.phobos.api.ModuleHook;
import org.phobos.api.Visibility;
import org.phobos.api.Markable;
import org.phobos.api.SettingContainer;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.JsonSerializable;
import org.phobos.api.Subscribe.Processor;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.BindSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.module.ModuleToggleNotice;
import org.phobos.core.user.UserSession;
import org.phobos.core.bind.BindKey;
import org.phobos.core.module.Category;
import org.phobos.core.setting.SettingGroup;
import org.phobos.core.event.EventBus;
import org.phobos.core.setting.SettingSection;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.Feature;
import org.phobos.core.config.ConfigManager;
import org.phobos.core.setting.SettingSubsection;
import org.phobos.core.bind.KeyCombination;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.util.NullSafe;
import org.phobos.core.setting.SettingSubsection.Builder;
import org.phobos.event.Listener;
import org.phobos.event.TickEvent2;
import org.phobos.type.BINDSMODULESEnum;
import org.phobos.type.KeyboardMouseEnum;
import org.phobos.type.NormalHoldEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.module.client.NotificationsModule;

public class Module
   extends Feature
   implements Named,
   TextProvider,
   SettingHolder,
   ModuleHook,
   MinecraftHolder,
   Toggleable,
   JsonSerializable,
   Markable,
   EntryContainer<SettingGroup>,
   SettingContainer {
   public String text2;
   public boolean flag12;
   public final String name;
   public final List<TriConsumer<ClientPlayerEntity, ClientWorld, ClientPlayerInteractionManager>> triConsumerList;
   public Setting<Boolean> hidden;
   public final List<Setting<?>> settingList;
   public final List<SettingSubsection> settingSubsectionList;
   public static final Logger LOGGER3 = LoggerFactory.getLogger(Module.class);
   public final PhobosClient phobosClient;
   public final MutableObject<Boolean> booleanValueMutableObject;
   public final List<SettingSection> settingSectionList;
   public final FloatAnimator floatAnimator3;
   public final FloatAnimator floatAnimator4;
   public String text3;
   public final int intValue7;
   public final List<Setting<?>> settingList2 = new ArrayList<>();
   public Setting<KeyCombination> bind;
   public final boolean flag13;
   public final Category category;
   public boolean flag14;
   public final List<SettingGroup> settingGroupList2;
   public final String text4;
   public final FloatAnimator floatAnimator5;
   public final FloatAnimator floatAnimator6;
   public final List<TriConsumer<ClientPlayerEntity, ClientWorld, ClientPlayerInteractionManager>> triConsumerList2;
   private static Object[] phobos$g1806c2a60;

   public Stream<Setting<?>> stream() {
      return this.settingList2.stream();
   }

   @Override
   public void run9() {
      this.booleanValueMutableObject.setValue(!this.booleanValueMutableObject.getValue());
      this.run6();
   }

   public void run6() {
      Consumer<TriConsumer> var17 = Module::handleTriConsumer5;
      MutableObject var18 = this.booleanValueMutableObject;
      Object var19 = var18.getValue();
      boolean var20 = var19 instanceof Boolean;
      if (var19 != null && !var20) {
         throw new ClassCastException("CHECKCAST failed");
      }

      boolean var21 = (Boolean)var19;
      int var23;
      if (!var21) {
         List var22 = this.triConsumerList2;
         var22.forEach(var17);
         var23 = 7088896;
      } else {
         List var27 = this.triConsumerList;
         var27.forEach(var17);
         var23 = 7088848;
      }

      switch (var23) {
         case 7088848:
         default:
            this.run3();
            break;
         case 7088896:
            this.run4();
      }

      MutableObject var24 = this.booleanValueMutableObject;
      Object var25 = var24.getValue();
      boolean var26 = var25 instanceof Boolean;
      if (var25 != null && !var26) {
         throw new ClassCastException("CHECKCAST failed");
      }

      label41: {
         int var34;
         EventBus var35;
         label47: {
            boolean var28 = (Boolean)var25;
            if (var28) {
               PhobosClient var29 = this.phobosClient;
               EventBus var30 = var29.getEventBus();
               boolean var31 = var30.testObject(this);
               if (!var31) {
                  PhobosClient var32 = this.phobosClient;
                  EventBus var33 = var32.getEventBus();
                  var34 = 7089056;
                  var35 = var33;
                  break label47;
               }
            }

            PhobosClient var42 = this.phobosClient;
            EventBus var43 = var42.getEventBus();
            boolean var44 = var43.testObject(this);
            if (!var44) {
               break label41;
            }

            PhobosClient var45 = this.phobosClient;
            EventBus var46 = var45.getEventBus();
            var34 = 7089152;
            var35 = var46;
         }

         switch (var34) {
            case 7089056:
            default:
               var35.handleObject(this);
               break;
            case 7089152:
               var35.handleObject4(this);
         }
      }

      PhobosClient var36 = this.phobosClient;
      EventBus var37 = var36.getEventBus();
      MutableObject var39 = this.booleanValueMutableObject;
      Object var40 = var39.getValue();
      boolean var41 = var40 instanceof Boolean;
      if (var40 != null && !var41) {
         throw new ClassCastException("CHECKCAST failed");
      }

      boolean var47 = (Boolean)var40;
      ModuleToggleNotice var38 = new ModuleToggleNotice(this, var47);
      var37.handleObject2(var38);
   }

   public void run10() {
      if (this.check7()) {
         this.run11();
      }

      for (Setting<?> setting : this.settingList2) {
         setting.testObject(setting.getObject());
      }

      this.setText3(null);
   }

   @SafeVarargs
   @Override
   public final <S extends Setting<?>> void handleG77G8vOcsOCkBD4vxs1B9Tu4ZA82vvYs(S... settings) {
      for (Setting<?> setting : settings) {
         this.resolveSetting2(setting);
      }
   }

   public void handleTriConsumer2(TriConsumer<ClientPlayerEntity, ClientWorld, ClientPlayerInteractionManager> triConsumer) {
      this.handleTriConsumer3(triConsumer);
      this.handleTriConsumer4(triConsumer);
   }

   public <S extends Setting<?>> S resolveSetting3(S setting) {
      this.settingList2.remove(setting);
      return setting;
   }

   public Module(PhobosClient phobosClient, String text, Category category, int... ints) {
      this(phobosClient, text, "", category, ints);
   }

   @Override
   public <S extends Setting<?>> S resolveSetting2(S setting) {
      this.settingList2.add(setting);
      setting.setSettingContainer(this);
      return setting;
   }

   public void run11() {
      this.booleanValueMutableObject.setValue(false);
      this.run6();
   }

   public String describe4() {
      return this.text3 != null ? this.text3 : this.name;
   }

   public Module(PhobosClient phobosClient, String text, String text2, Category category, int... ints) {
      this.settingList = new ArrayList<>();
      this.booleanValueMutableObject = new MutableObject<>(false);
      this.floatAnimator3 = new FloatAnimator();
      this.floatAnimator6 = new FloatAnimator();
      this.floatAnimator5 = new FloatAnimator();
      this.floatAnimator4 = new FloatAnimator();
      this.settingGroupList2 = new ArrayList<>();
      this.settingSubsectionList = new ArrayList<>();
      this.settingSectionList = new ArrayList<>();
      this.triConsumerList = new ArrayList<>();
      this.triConsumerList2 = new ArrayList<>();
      this.flag12 = false;
      this.flag14 = false;
      this.hidden = this.resolveSetting2(new BooleanSetting("Hidden", false));
      this.phobosClient = phobosClient;
      this.name = text;
      this.text4 = text2;
      this.category = category;
      this.intValue7 = ints.length > 0 ? ints[0] : -1;
      this.flag13 = UserSession.userSession.getUserProfile().getPermission().isBeta();
      this.bind = this.resolveSetting2(new BindSetting("Bind").resolveSetting7("Toggle key"));
      if (this.intValue7 != -1) {
         BindKey bindKey = phobosClient.getBindManager().resolveBindKey(KeyboardMouseEnum.Keyboard, this.intValue7);
         if (bindKey != null) {
            this.bind.getObject().getBindKeySet().add(bindKey);
         }
      }

      this.handleListener(new Listener<TickEvent2>() {
         public void handleObject2(TickEvent2 tickEvent2) {
            if (Module.this.bind.getValue().getNormalHoldEnum().equals(NormalHoldEnum.Hold)) {
               if (!phobosClient.getBindManager().testKeyCombination(Module.this.bind.getValue())) {
                  Module.this.run11();
               }
            }
         }
      });
      Processor.handleListenerHolder(this);
   }

   public void handleFlag3(boolean flag) {
      for (Setting<?> setting : this.settingList2) {
         setting.run2();
      }

      if (flag) {
         this.setFlag4(false);
         this.run11();
      }

      this.floatAnimator3.handleFloat(0.0F);
      this.floatAnimator6.handleFloat(0.0F);
   }

   public void handleTriConsumer3(TriConsumer<ClientPlayerEntity, ClientWorld, ClientPlayerInteractionManager> triConsumer) {
      this.triConsumerList.add(triConsumer);
   }

   @Override
   public JsonElement createJsonPrimitive() {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("Enabled", this.booleanValueMutableObject.getValue());
      jsonObject.addProperty("Disabled", this.flag12);
      jsonObject.addProperty("DisplayName", this.text3 == null ? this.name : this.text3);

      for (Setting<?> setting : this.settingList2) {
         if (setting.isFlag7()) {
            JsonElement jsonElement = setting.createJsonPrimitive();
            if (jsonElement != null) {
               jsonObject.add(setting.getName(), jsonElement);
            }
         }
      }

      return jsonObject;
   }

   public Module(PhobosClient phobosClient, String text, Category category, String text2, int... ints) {
      this(phobosClient, text, text2, category, ints);
   }

   public void run12() {
      this.booleanValueMutableObject.setValue(true);
      this.run6();
   }

   public void handleTriConsumer4(TriConsumer<ClientPlayerEntity, ClientWorld, ClientPlayerInteractionManager> triConsumer) {
      this.triConsumerList2.add(triConsumer);
   }

   @Override
   public String getText() {
      return this.text4;
   }

   public SettingGroup resolveSettingGroup2(String text, Setting<?>... settings) {
      SettingGroup var3 = new SettingGroup(text, settings);
      List var4 = this.settingGroupList2;
      var4.add(var3);
      return var3;
   }

   @Override
   public String getName() {
      return this.name;
   }

   public Builder createBuilder() {
      return new Builder(this.settingSubsectionList, this.settingGroupList2);
   }

   @Nullable
   public Setting<?> resolveSetting(String text) {
      for (Setting<?> setting : this.settingList2) {
         if (setting.describe3().startsWith(text)) {
            return setting;
         }
      }

      return null;
   }

   @SafeVarargs
   public final <T extends Enum<T>> Visibility resolveVisibility2(Setting<T> setting, T... enumValues) {
      return () -> Arrays.asList(enumValues).contains(setting.getValue());
   }

   @Override
   public boolean check7() {
      return this.booleanValueMutableObject.getValue();
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      if (!ConfigManager.getConfigSectionFilter().check()) {
         boolean flag = Objects.equals(this.category, Categories.render)
            && ConfigManager.getConfigSectionFilter().testBINDSMODULESEnum(BINDSMODULESEnum.VISUALS);
         JsonObject jsonObject = jsonElement.getAsJsonObject();

         for (Setting<?> setting : this.settingList2) {
            if (flag || ConfigManager.getConfigSectionFilter().testSetting(setting)) {
               JsonElement jsonElement2 = jsonObject.get(setting.getName());
               if (jsonElement2 == null) {
                  setting.testObject(setting.getObject());
               } else {
                  try {
                     setting.handleJsonElement(jsonElement2);
                  } catch (Exception var8) {
                  }
               }
            }
         }

         if (flag || ConfigManager.getConfigSectionFilter().testBINDSMODULESEnum(BINDSMODULESEnum.SETTINGS)) {
            NullSafe.handleObject(jsonObject.get("Disabled"), jsonElementx -> this.setFlag4(jsonElementx.getAsBoolean()));
            NullSafe.handleObject(jsonObject.get("DisplayName"), jsonElementx -> this.setText3(jsonElementx.getAsString()));
         }

         if (flag || ConfigManager.getConfigSectionFilter().testBINDSMODULESEnum(BINDSMODULESEnum.MODULES)) {
            boolean flag2 = jsonObject.get("Enabled").getAsBoolean();
            if (flag2) {
               if (!this.check7()) {
                  mc.submit(this::run12);
               }
            } else if (this.check7()) {
               mc.submit(this::run11);
            }
         }
      }
   }

   public String nothing() {
      return null;
   }

   public void handleText7(Text text) {
      NotificationsModule.getNotificationsModule()
         .handleTriConsumer(
            (color, color2, color3) -> NotificationsModule.getNotificationsModule()
               .handleText5(Text.literal("[%s] ".formatted(this.describe4())).withColor(color2.hashCode()).append(text))
         );
   }

   public void handleText8(String text) {
      this.settingGroupList2.removeIf(settingGroup -> {
         if (!settingGroup.getName().equals(text)) {
            return false;
         }

         for (Setting<?> setting : settingGroup.getSettingList()) {
            this.resolveSetting3(setting);
         }

         if (settingGroup.getBooleanValueSetting() != null) {
            this.resolveSetting3(settingGroup.getBooleanValueSetting());
         }

         return true;
      });
   }

   public void handleText10(String text, Object... objects) {
      LOGGER3.info(text, objects);
   }

   public Optional<Setting<?>> resolveOptional(String text) {
      return this.settingList2.stream().filter(setting -> setting.getName().equalsIgnoreCase(text)).findFirst();
   }

   public SettingGroup resolveSettingGroup(Setting<Boolean> setting, Setting<?>... settings) {
      SettingGroup var3 = new SettingGroup(setting, settings);
      List var4 = this.settingGroupList2;
      var4.add(var3);
      return var3;
   }

   public org.phobos.core.setting.SettingSection.Builder createBuilder2() {
      return new org.phobos.core.setting.SettingSection.Builder(this.settingSectionList, this.settingSubsectionList, this.settingGroupList2);
   }

   public void handleText6(String text) {
      NotificationsModule.getNotificationsModule()
         .handleTriConsumer(
            (color, color2, color3) -> NotificationsModule.getNotificationsModule()
               .handleText5(
                  Text.literal("[%s] ".formatted(this.describe4())).withColor(color2.hashCode()).append(Text.literal(text).withColor(color3.hashCode()))
               )
         );
   }

   public PhobosClient getPhobosClient() {
      return this.phobosClient;
   }

   public <T> Optional<Setting<T>> resolveOptional2(String text, @Nullable Class<T> classValue) {
      Optional<Setting<?>> optional = this.resolveOptional(text);
      return classValue != null && (!optional.isPresent() || !classValue.isAssignableFrom(optional.get().getValueClassValue2())) ? Optional.empty() : (Optional<Setting<T>>)(Optional<?>)optional;
   }

   public boolean isFlag13() {
      return this.flag13;
   }

   public FloatAnimator getFloatAnimator5() {
      return this.floatAnimator5;
   }

   public List<SettingSubsection> getSettingSubsectionList() {
      return this.settingSubsectionList;
   }

   public FloatAnimator getFloatAnimator4() {
      return this.floatAnimator4;
   }

   public FloatAnimator getFloatAnimator3() {
      return this.floatAnimator3;
   }

   @Override
   public List<Setting<?>> getSettingList() {
      return this.settingList2;
   }

   public String getText22() {
      return this.text2;
   }

   public MutableObject<Boolean> getBooleanValueMutableObject() {
      return this.booleanValueMutableObject;
   }

   public FloatAnimator getFloatAnimator7() {
      return this.floatAnimator6;
   }

   @Override
   public boolean isFlag4() {
      return this.flag12;
   }

   public List<Setting<?>> getSettingList2() {
      return this.settingList;
   }

   public List<TriConsumer<ClientPlayerEntity, ClientWorld, ClientPlayerInteractionManager>> getTriConsumerList2() {
      return this.triConsumerList2;
   }

   public List<TriConsumer<ClientPlayerEntity, ClientWorld, ClientPlayerInteractionManager>> getTriConsumerList() {
      return this.triConsumerList;
   }

   public int getIntValue7() {
      return this.intValue7;
   }

   public String getText3() {
      return this.text3;
   }

   public List<SettingSection> getSettingSectionList() {
      return this.settingSectionList;
   }

   @Override
   public List<SettingGroup> getSettingGroupList() {
      return this.settingGroupList2;
   }

   public Category getCategory() {
      return this.category;
   }

   @Override
   public void setFlag4(boolean flag) {
      this.flag12 = flag;
   }

   public void setFlag14(boolean flag) {
      this.flag14 = flag;
   }

   public boolean isFlag14() {
      return this.flag14;
   }

   public void setText3(String text) {
      this.text3 = text;
   }

   public static void handleTriConsumer5(TriConsumer triConsumer) {
      triConsumer.accept(mc.player, mc.world, mc.interactionManager);
   }

   public Setting<KeyCombination> getBind() {
      return this.bind;
   }

   public void setText2(String text) {
      this.text2 = text;
   }

   public void setHidden(Setting<Boolean> setting) {
      this.hidden = setting;
   }

   public void setBind(Setting<KeyCombination> setting) {
      this.bind = setting;
   }

   public Setting<Boolean> getHidden() {
      return this.hidden;
   }
}
