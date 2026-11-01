package org.phobos.module.render;

import java.awt.Color;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.render.ColorUtils;
import org.phobos.event.ChorusHighlightPlaySoundListener;
import org.phobos.module.Module;

public class ChorusHighlightModule extends Module {
   public final Setting<Color> box = new ColorSetting("Box", ColorUtils.createColor6(Color.WHITE, 0.3F)).getColorSetting().resolveSetting6(this);
   public final Setting<Float> width;
   public final Setting<Long> fade;
   public final Setting<Color> line = new ColorSetting("Line", Color.WHITE).getColorSetting().resolveSetting6(this);

   public ChorusHighlightModule(PhobosClient phobosClient) {
      super(phobosClient, "ChorusHighlight", Categories.render, "Highlights chorus teleports");
      this.width = new NumberSetting<>("Width", 1.0F, 0.1F, 3.0F).resolveSetting6(this);
      this.fade = new LongNumberSetting("Fade", 5000L, 250L, 15000L).resolveSetting6(this);
      this.resolveSettingGroup2("Render", this.box, this.line, this.width, this.fade);
      this.handleListener(new ChorusHighlightPlaySoundListener(this));
   }
}
