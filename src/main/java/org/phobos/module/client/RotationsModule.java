package org.phobos.module.client;

import org.phobos.api.TextProvider;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.module.Module;
import org.phobos.module.movement.DisablerModule;

public class RotationsModule extends Module {
   public final Setting<RotationsModule.Cursor> cursor;
   public final Setting<Integer> slices;
   public static RotationsModule rotationsModule;
   public final Setting<Boolean> moveFix = this.resolveSetting2(new BooleanSetting("MoveFix").resolveSetting7("Movement correction"));
   public final Setting<Boolean> sensitivity;
   public final Setting<RotationsModule.Display> display;

   public static boolean check() {
      return rotationsModule.display.testObject2(RotationsModule.Display.Silent);
   }

   public static boolean check2() {
      return rotationsModule.moveFix.getValue() && !DisablerModule.check();
   }

   public RotationsModule(PhobosClient phobosClient) {
      super(phobosClient, "Rotations", Categories.client, "Manages client rotations");
      this.cursor = this.resolveSetting2(new EnumSetting<>("Cursor", RotationsModule.Cursor.Track).resolveSetting7("Controls cursor"));
      this.slices = this.resolveSetting2(new NumberSetting<>("Slices", 6, 0, 10).resolveSetting4(this.cursor, RotationsModule.Cursor.Grim));
      this.sensitivity = this.resolveSetting2(new BooleanSetting("Sensitivity").resolveSetting7("Adjusts sensitivity"));
      this.display = this.resolveSetting2(new EnumSetting<>("Display", RotationsModule.Display.Smooth));
      rotationsModule = this;
   }

   public static boolean check3() {
      return rotationsModule.display.testObject2(RotationsModule.Display.Smooth);
   }

   public static boolean check4() {
      return rotationsModule.cursor.testObject2(RotationsModule.Cursor.Grim);
   }

   public static boolean check5() {
      return rotationsModule.display.testObject2(RotationsModule.Display.Instant);
   }

   public static RotationsModule getRotationsModule() {
      return rotationsModule;
   }

   public static boolean check6() {
      return rotationsModule.cursor.testObject2(RotationsModule.Cursor.Track);
   }

   public enum Cursor implements TextProvider {
      None("No rotations synchronization"),
      Track("Normal rotation synchronization"),
      Grim("Grim on-tick async free cursor");

      public final String text;

      Cursor(final String description) {
         this.text = description;
      }

      @Override
      public String getText() {
         return this.text;
      }
   }

   private enum Display {
      Smooth,
      Instant,
      Silent;
   }
}
