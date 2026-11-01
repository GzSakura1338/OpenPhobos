package org.phobos.module.player;

import org.phobos.api.TextProvider;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.AntiCrawlJumpListener;
import org.phobos.event.AntiCrawlPlayerTickListener;
import org.phobos.module.Module;

public class AntiCrawlModule extends Module {
   public final TimeGate.Normal normal;
   public final Setting<AntiCrawlModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", AntiCrawlModule.Mode.Clip));
   public final Setting<Boolean> invert;
   public final Setting<Integer> ticks = this.resolveSetting2(new NumberSetting<>("Ticks", 2, 1, 5).resolveSetting4(this.mode, AntiCrawlModule.Mode.Clip));

   public AntiCrawlModule(PhobosClient phobosClient) {
      super(phobosClient, "AntiCrawl", Categories.player, "Helps you escape swimming spaces");
      this.invert = this.resolveSetting2(
         new BooleanSetting("Invert").resolveSetting7("Looks up when clipping").resolveSetting4(this.mode, AntiCrawlModule.Mode.Clip)
      );
      this.normal = new TimeGate.Normal();
      this.handleListener(new AntiCrawlJumpListener(this));
      this.handleListener(new AntiCrawlPlayerTickListener(this));
   }

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   public enum Mode implements TextProvider {
      Clip("Uses Clip module to pearl you out"),
      Disable("Disables crawling mechanic as a whole");

      public final String text;

      Mode(final String description) {
         this.text = description;
      }

      @Override
      public String getText() {
         return this.text;
      }
   }
}
