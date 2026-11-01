package org.phobos.module.client;

import com.google.gson.internal.LinkedTreeMap;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.PhobosClient;
import org.phobos.core.user.UuidResolver;
import org.phobos.core.task.AsyncUtils;
import org.phobos.core.module.Categories;
import org.phobos.type.MCTiersSubTiersEnum;
import org.phobos.module.Module;

public class GameTiersModule extends Module {
   public final Setting<Boolean> override;
   public final Setting<Boolean> icons;
   public final Setting<Boolean> online = this.resolveSetting2(new BooleanSetting("Online"));
   public final Setting<GameTiersModule.Tier> overrideTier;
   public final Setting<Boolean> overrideR;
   public final Setting<GameTiersModule.Statistic> display;
   public static GameTiersModule gameTiersModule;
   public final Setting<Boolean> retired;
   public final LinkedTreeMap<String, Integer> textLinkedTreeMap;
   public final Setting<MCTiersSubTiersEnum> vendor = this.resolveSetting2(new EnumSetting<>("Vendor", MCTiersSubTiersEnum.MCTiers));

   public Setting<Boolean> getRetired() {
      return this.retired;
   }

   public Setting<Boolean> getIcons() {
      return this.icons;
   }

   public Setting<MCTiersSubTiersEnum> getVendor() {
      return this.vendor;
   }

   public Setting<Boolean> getOverrideR() {
      return this.overrideR;
   }

   public LinkedTreeMap<String, Integer> getTextLinkedTreeMap() {
      return this.textLinkedTreeMap;
   }

   public Setting<Boolean> getOnline() {
      return this.online;
   }

   public GameTiersModule(PhobosClient phobosClient) {
      super(phobosClient, "GameTiers", Categories.client, "Minecraft vanilla tiers");
      this.retired = this.resolveSetting2(new BooleanSetting("Retired"));
      this.display = this.resolveSetting2(new EnumSetting<>("Display", GameTiersModule.Statistic.Tier));
      this.icons = this.resolveSetting2(new BooleanSetting("Icons"));
      this.override = this.resolveSetting2(new BooleanSetting("Override"));
      this.overrideTier = this.resolveSetting2(new EnumSetting<>("OverrideTier", GameTiersModule.Tier.LT5));
      this.overrideR = this.resolveSetting2(new BooleanSetting("OverrideR"));
      this.textLinkedTreeMap = new LinkedTreeMap();
      this.resolveSettingGroup(this.online, this.vendor, this.retired, this.display, this.icons);
      this.resolveSettingGroup(this.override, this.overrideTier, this.overrideR);
      this.textLinkedTreeMap.put("HT1", 15252026);
      this.textLinkedTreeMap.put("LT1", 14005077);
      this.textLinkedTreeMap.put("HT2", 12899303);
      this.textLinkedTreeMap.put("LT2", 10528690);
      this.textLinkedTreeMap.put("HT3", 16293722);
      this.textLinkedTreeMap.put("LT3", 13007682);
      this.textLinkedTreeMap.put("HT4", 8483994);
      this.textLinkedTreeMap.put("LT4", 6642553);
      this.textLinkedTreeMap.put("HT5", 9405096);
      this.textLinkedTreeMap.put("LT5", 6642553);
      this.vendor.handleConsumer2(mCTiersSubTiersEnum -> AsyncUtils.handleRunnable(UuidResolver::run));
      gameTiersModule = this;
      AsyncUtils.handleRunnable(UuidResolver::run);
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }

   public Setting<GameTiersModule.Statistic> getDisplay() {
      return this.display;
   }

   public boolean check() {
      return !this.online.getValue();
   }

   public static boolean check2() {
      return gameTiersModule != null && gameTiersModule.check7() && gameTiersModule.override.getValue();
   }

   public Setting<GameTiersModule.Tier> getOverrideTier() {
      return this.overrideTier;
   }

   public String describe() {
      return (this.overrideR.getValue() ? "R" : "") + this.overrideTier.getValue().toString();
   }

   public Setting<Boolean> getOverride() {
      return this.override;
   }

   public static GameTiersModule getGameTiersModule() {
      return gameTiersModule;
   }

   public String describe2() {
      return this.vendor.getValue().getUrl();
   }

   public enum Statistic {
      Tier(0),
      Rank(1);

      public final int id;

      Statistic(final int id) {
         this.id = id;
      }

      public int getId() {
         return this.id;
      }
   }

   public enum Tier {
      HT1,
      LT1,
      HT2,
      LT2,
      HT3,
      LT3,
      HT4,
      LT4,
      HT5,
      LT5;

      @Override
      public String toString() {
         return this.name();
      }
   }
}
