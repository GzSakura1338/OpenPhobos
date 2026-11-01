package org.phobos.core.hud.element;

import net.minecraft.client.gui.DrawContext;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.config.BuildInfo;
import org.phobos.core.chat.ChatTextUtils;
import org.phobos.core.hud.HudElement;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.StringSetting;

public class WatermarkHudElement extends HudElement {
   public final String text;
   public final String text2;
   public final Setting<Boolean> format;
   public final Setting<String> watermarkType = this.resolveSetting2(new StringSetting("WatermarkType", "3arthh4ck"));

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      String text = this.watermarkType.getValue() + " §7-§r " + (this.format.getValue() ? "§f" : "") + this.text2 + this.text;
      this.handleDrawContext4(drawContext, text, 0.0F, 0.0F);
      this.handleText3(text);
   }

   public WatermarkHudElement() {
      super("Watermark", 0.0F, 0.0F, 0.0F, 9.0F);
      this.format = this.resolveSetting2(new BooleanSetting("Format", false));
      BuildInfo buildInfo = BuildInfo.getINSTANCE();
      this.text = buildInfo.getType().equals(BuildInfo.Type.Beta) ? "-" + ChatTextUtils.describeText5(buildInfo.describe(), 14) : "";
      this.text2 = buildInfo.describe3();
   }
}
