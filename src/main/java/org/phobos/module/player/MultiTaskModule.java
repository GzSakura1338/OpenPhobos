package org.phobos.module.player;

import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.module.Module;

public class MultiTaskModule extends Module {
   public final Setting<MultiTaskModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", MultiTaskModule.Mode.Rage));
   public static MultiTaskModule multiTaskModule;

   public static boolean check() {
      return multiTaskModule.check7() && multiTaskModule.mode.testObject2(MultiTaskModule.Mode.Legit);
   }

   public MultiTaskModule(PhobosClient phobosClient) {
      super(phobosClient, "MultiTask", Categories.player, "Multi-actions");
      multiTaskModule = this;
   }

   public static boolean check2() {
      return multiTaskModule.check7();
   }

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   public static boolean check3() {
      return multiTaskModule.check7() && multiTaskModule.mode.getValue().attack;
   }

   public static boolean check4() {
      return multiTaskModule.check7() && multiTaskModule.mode.getValue().place;
   }

   private enum Mode {
      Legit(false, false, "Allows you to place on breaking blocks"),
      Rage(true, true, "Allows for any additional interaction while using an item");

      public final String description;
      public final boolean place;
      public final boolean attack;

      Mode(final boolean attack, final boolean place, final String description) {
         this.attack = attack;
         this.place = place;
         this.description = description;
      }

      public String getDescription() {
         return this.description;
      }

      public boolean isAttack() {
         return this.attack;
      }

      public boolean isPlace() {
         return this.place;
      }
   }
}
