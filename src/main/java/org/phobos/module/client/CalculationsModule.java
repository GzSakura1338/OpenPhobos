package org.phobos.module.client;

import org.phobos.api.TextProvider;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.module.Module;

public class CalculationsModule extends Module {
   public final Setting<Boolean> terrain;
   public final Setting<Boolean> shared;
   public final Setting<Boolean> simulate;
   public final Setting<Long> simulateTimeout;
   public final Setting<CalculationsModule.Damage> type;
   public final Setting<Boolean> debug = this.resolveSetting2(new BooleanSetting("Debug"));
   public final Setting<CalculationsModule.Update> update = this.resolveSetting2(new EnumSetting<>("Update", CalculationsModule.Update.Async));
   public static CalculationsModule calculationsModule;

   public static boolean check() {
      return calculationsModule.simulate.getValue();
   }

   public CalculationsModule(PhobosClient phobosClient) {
      super(phobosClient, "Calculations", Categories.client, "Manages calculations");
      this.simulate = this.resolveSetting2(new BooleanSetting("Simulate", false).resolveSetting7("Uses localized damage engine"));
      this.shared = this.resolveSetting2(new BooleanSetting("Shared").resolveSetting7("Listens and dispatches health"));
      this.simulateTimeout = this.resolveSetting2(new LongNumberSetting("SimulateTimeout", 30000L, 0L, 300000L, true));
      this.terrain = this.resolveSetting2(new BooleanSetting("Terrain", false).resolveSetting7("Skips terrain blocks when calculating damages"));
      this.type = this.resolveSetting2(new EnumSetting<>("Type", CalculationsModule.Damage.Normal).resolveSetting7("Protection calculation"));
      this.resolveSettingGroup(this.simulate, this.shared, this.simulateTimeout);
      this.simulate.handleConsumer2(booleanValue -> {
         if (!booleanValue) {
            phobosClient.getFeatureRegistry().getDamageTracker().run2();
         }
      });
      calculationsModule = this;
   }

   public static CalculationsModule getCalculationsModule() {
      return calculationsModule;
   }

   public static boolean check2() {
      return calculationsModule.terrain.getValue();
   }

   public static boolean check3() {
      return calculationsModule.debug.getValue();
   }

   public static boolean check4() {
      return calculationsModule.shared.getValue();
   }

   public static CalculationsModule.Damage getDamage() {
      return calculationsModule.type.getValue();
   }

   public static long value() {
      return calculationsModule.simulateTimeout.getValue();
   }

   public static boolean check5() {
      return calculationsModule.update.testObject2(CalculationsModule.Update.Async);
   }

   public enum Damage implements TextProvider {
      Normal("Attributes based calculation"),
      Enchants("Enchantments based calculation"),
      Generic("Assumes best possible armor");

      public final String text;

      Damage(final String description) {
         this.text = description;
      }

      @Override
      public String getText() {
         return this.text;
      }
   }

   public enum Update implements TextProvider {
      Async("Updates protection level when equipment changes"),
      Always("Updates protection level every tick");

      public final String text;

      @Override
      public String getText() {
         return this.text;
      }

      Update(final String description) {
         this.text = description;
      }
   }
}
