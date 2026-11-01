package org.phobos.module.render;

import java.awt.Color;
import java.util.UUID;
import net.minecraft.util.Identifier;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.animation.TimedFloatAnimator;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.render.ColorUtils;
import org.phobos.event.ArrowsRenderListener;
import org.phobos.module.Module;

public class ArrowsModule extends Module {
   public final Setting<Color> enemies;
   public final Setting<Float> size;
   public final Setting<Double> radius = this.resolveSetting2(new NumberSetting<>("Radius", 1.0, 0.3, 2.0));
   public final Setting<Color> friends;
   public final TimedFloatAnimator timedFloatAnimator;
   public final Setting<Color> color;
   public static final Identifier identifier = Identifier.of("phoboslite", "textures/ui/arrow.png");

   public Color resolveColor(UUID uUID) {
      return ColorUtils.resolveColor2(this.phobosClient, uUID, this.color.getValue(), this.friends.getValue(), this.enemies.getValue());
   }

   public ArrowsModule(PhobosClient phobosClient) {
      super(phobosClient, "Arrows", Categories.render, "Draws arrows to enemies");
      this.size = this.resolveSetting2(new NumberSetting<>("Size", 1.0F, 0.5F, 1.5F));
      this.color = this.resolveSetting2(new ColorSetting("Color", Color.WHITE).getColorSetting());
      this.friends = this.resolveSetting2(new ColorSetting("Friends", Color.CYAN));
      this.enemies = this.resolveSetting2(new ColorSetting("Enemies", Color.RED));
      this.timedFloatAnimator = new TimedFloatAnimator();
      this.handleListener(new ArrowsRenderListener(this));
   }
}
