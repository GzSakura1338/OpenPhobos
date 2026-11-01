package org.phobos.module.player;

import org.phobos.api.TextProvider;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.module.Module;

public class AntiLevitationModule extends Module {
   public static AntiLevitationModule antiLevitationModule;
   public final Setting<AntiLevitationModule.Mode> mode = new EnumSetting<>("Mode", AntiLevitationModule.Mode.Disable).resolveSetting6(this);

   public static boolean check() {
      return antiLevitationModule.check7() && antiLevitationModule.mode.getValue().equals(AntiLevitationModule.Mode.Disable);
   }

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   public static boolean check2() {
      return antiLevitationModule.check7() && antiLevitationModule.mode.getValue().equals(AntiLevitationModule.Mode.Control);
   }

   public static int value() {
      boolean flag = mc.options.jumpKey.isPressed();
      boolean flag2 = mc.options.sneakKey.isPressed();
      return flag && flag2 ? 0 : (flag ? 1 : (flag2 ? -1 : 0));
   }

   public AntiLevitationModule(PhobosClient phobosClient) {
      super(phobosClient, "AntiLevitation", Categories.player, "Controls levitation effect behaviour");
      antiLevitationModule = this;
   }

   public enum Mode implements TextProvider {
      Disable("Disables the effect"),
      Control("Allows for precise control");

      public final String text;

      @Override
      public String getText() {
         return this.text;
      }

      Mode(final String description) {
         this.text = description;
      }
   }
}
