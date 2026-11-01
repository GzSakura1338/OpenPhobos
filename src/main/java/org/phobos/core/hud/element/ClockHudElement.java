package org.phobos.core.hud.element;

import net.minecraft.client.gui.DrawContext;
import org.phobos.api.TimeGate;
import org.phobos.event.Listener;
import org.phobos.event.TickEvent2;
import org.phobos.module.client.HudModule;
import org.phobos.module.client.HudModule;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.hud.ClockTime;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.Setting;
import org.phobos.core.hud.StateFactory;
import org.phobos.core.hud.TextHudElement;
import org.phobos.core.hud.TimeFormat;

public class ClockHudElement extends TextHudElement {
   public final Setting<ClockHudElement.Mode> mode = this.resolveSetting2(
      new EnumSetting<>("Mode", ClockHudElement.Mode.HHMMSS)
   );
   public final ClockTime clockTime;
   public final TimeGate.Normal normal;
   public final Setting<Boolean> booleanValueSetting = this.resolveSetting2(new BooleanSetting("24h"));

   public String describeText(String text) {
      return switch ((HudModule.InfoStyle)HudModule.getHudModule().getStyle().getValue()) {
         case None, Value -> text;
         case Name -> "§r" + text;
      };
   }

   public ClockHudElement() {
      super("Time");
      this.clockTime = StateFactory.resolveClockTime(true);
      this.normal = new TimeGate.Normal();
      this.handleListener(new Listener<TickEvent2>() {
         public void handleObject2(TickEvent2 tickEvent2) {
            if (ClockHudElement.this.normal.testLong(500L)) {
               ClockHudElement.this.clockTime.run();
            }
         }
      });
   }

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      String text = this.mode.getValue().get(this.clockTime, this.booleanValueSetting.getValue());
      String text2 = this.describeText(text);
      this.handleDrawContext9(drawContext, text2);
   }

   private enum Mode {
      YYYYMMDD {
         @Override
         public String get(ClockTime worldTimestamp, boolean _24hr) {
            int year = worldTimestamp.getIntValue2();
            int month = worldTimestamp.getIntValue();
            int day = worldTimestamp.getIntValue3();
            return year + "/" + TimeFormat.describeInt2(month) + "/" + TimeFormat.describeInt2(day);
         }
      },
      MMDDHH {
         @Override
         public String get(ClockTime worldTimestamp, boolean _24hr) {
            int month = worldTimestamp.getIntValue();
            int day = worldTimestamp.getIntValue3();
            int hour = worldTimestamp.getIntValue4();
            String string = TimeFormat.describeInt2(month)
               + "/"
               + TimeFormat.describeInt2(day)
               + ":"
               + TimeFormat.describeInt2(_24hr ? hour : (hour > 12 ? hour % 12 : hour));
            return _24hr ? string : string + (hour >= 12 ? " PM" : " AM");
         }
      },
      MMDDHHMM {
         @Override
         public String get(ClockTime worldTimestamp, boolean _24hr) {
            int month = worldTimestamp.getIntValue();
            int day = worldTimestamp.getIntValue3();
            int hour = worldTimestamp.getIntValue4();
            int minute = worldTimestamp.getIntValue6();
            String string = TimeFormat.describeInt2(month)
               + "/"
               + TimeFormat.describeInt2(day)
               + ":"
               + TimeFormat.describeInt2(_24hr ? hour : (hour > 12 ? hour % 12 : hour))
               + ":"
               + TimeFormat.describeInt2(minute);
            return _24hr ? string : string + (hour >= 12 ? " PM" : " AM");
         }
      },
      DDHHMMSS {
         @Override
         public String get(ClockTime worldTimestamp, boolean _24hr) {
            int day = worldTimestamp.getIntValue3();
            int hour = worldTimestamp.getIntValue4();
            int minute = worldTimestamp.getIntValue6();
            int second = worldTimestamp.getIntValue5();
            String string = TimeFormat.describeInt2(day)
               + ":"
               + TimeFormat.describeInt2(_24hr ? hour : (hour > 12 ? hour % 12 : hour))
               + ":"
               + TimeFormat.describeInt2(minute)
               + ":"
               + TimeFormat.describeInt2(second);
            return _24hr ? string : string + (hour >= 12 ? " PM" : " AM");
         }
      },
      HHMMSS {
         @Override
         public String get(ClockTime worldTimestamp, boolean _24hr) {
            int hour = worldTimestamp.getIntValue4();
            int minute = worldTimestamp.getIntValue6();
            int second = worldTimestamp.getIntValue5();
            String string = TimeFormat.describeInt2(_24hr ? hour : (hour > 12 ? hour % 12 : hour))
               + ":"
               + TimeFormat.describeInt2(minute)
               + ":"
               + TimeFormat.describeInt2(second);
            return _24hr ? string : string + (hour >= 12 ? " PM" : " AM");
         }
      };

      public abstract String get(ClockTime var1, boolean var2);
   }
}
