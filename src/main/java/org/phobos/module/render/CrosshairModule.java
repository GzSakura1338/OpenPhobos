package org.phobos.module.render;

import java.awt.Color;
import java.util.Map;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.RotationAxis;
import org.phobos.api.TextProvider;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.MultiSelectSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.math.MathUtils;
import org.phobos.core.render.ColorUtils;
import org.phobos.event.CrosshairRenderListener;
import org.phobos.event.CrosshairAttackListener;
import org.phobos.event.CrosshairOnMouseButtonListener;
import org.phobos.event.CrosshairPostReceiveEntityDamageListener;
import org.phobos.module.Module;

public class CrosshairModule extends Module {
   public final CrosshairModule.HitCrosshair hitCrosshair;
   public final Setting<CrosshairModule.Style> style = new EnumSetting<>("Style", CrosshairModule.Style.Default).resolveSetting6(this);
   public final Setting<Color> hitColor;
   public final FloatAnimator floatAnimator;
   public final Setting<Float> hitLength;
   public final Setting<Boolean> hit;
   public final Setting<Color> color;
   public final FloatAnimator floatAnimator2;
   public final Setting<Boolean> sync;
   public final Setting<Map<CrosshairModule.Trigger, Boolean>> trigger;
   public final Setting<Boolean> animate;
   public final Setting<Float> length = new NumberSetting<>("Length", 1.0F, 0.0F, 10.0F).resolveSetting7("Length/Size").resolveSetting6(this);
   public final Setting<Float> offset;
   public final Setting<Float> shift = new NumberSetting<>("Shift", 0.0F, 0.0F, 20.0F)
      .resolveSetting3(this.resolveVisibility2(this.style, CrosshairModule.Style.Default, CrosshairModule.Style.Dot))
      .resolveSetting7("Distance from center")
      .resolveSetting6(this);
   public static CrosshairModule crosshairModule;

   @Override
   public String nothing() {
      return String.valueOf(this.style.getValue());
   }

   public CrosshairModule(PhobosClient phobosClient) {
      super(phobosClient, "Crosshair", Categories.render, "Renders custom crosshair");
      this.animate = new BooleanSetting("Animate").resolveSetting7("Moves the crosshair").resolveSetting6(this);
      this.trigger = new MultiSelectSetting<>("Trigger", CrosshairModule.Trigger.class).resolveMultiSelectSetting5(multiSelectSetting -> {
         for (CrosshairModule.Trigger trigger : CrosshairModule.Trigger.values()) {
            multiSelectSetting.resolveMultiSelectSetting(trigger, trigger.name());
         }
      }).resolveSetting6(this);
      this.offset = new NumberSetting<>("Offset", 20.0F, 0.0F, 100.0F).resolveSetting6(this);
      this.color = new ColorSetting("Color", Color.WHITE).getColorSetting().resolveSetting6(this);
      this.hit = new BooleanSetting("Hit").resolveSetting6(this);
      this.sync = new BooleanSetting("Sync").resolveSetting6(this);
      this.hitLength = new NumberSetting<>("HitLength", 2.0F, 0.1F, 16.0F).resolveSetting6(this);
      this.hitColor = new ColorSetting("HitColor", Color.WHITE).resolveSetting6(this);
      this.hitCrosshair = new CrosshairModule.HitCrosshair();
      this.floatAnimator = new FloatAnimator();
      this.floatAnimator2 = new FloatAnimator();
      this.resolveSettingGroup(this.animate, this.trigger, this.offset)
         .resolveSettingGroup5(this.resolveVisibility2(this.style, CrosshairModule.Style.Default, CrosshairModule.Style.Dot));
      this.resolveSettingGroup(this.hit, this.sync, this.hitLength, this.hitColor);
      this.handleListener(new CrosshairAttackListener(this));
      this.handleListener(new CrosshairPostReceiveEntityDamageListener(this));
      this.handleListener(new CrosshairOnMouseButtonListener(this));
      this.handleListener(new CrosshairRenderListener(this));
      crosshairModule = this;
   }

   public static CrosshairModule getCrosshairModule() {
      return crosshairModule;
   }

   public static boolean check() {
      return crosshairModule != null && crosshairModule.check7() && !crosshairModule.style.getValue().equals(CrosshairModule.Style.None);
   }

   public void run() {
      this.floatAnimator2.setFloatValue2(1.0F);
      this.floatAnimator2.setFloatValue(0.0F);
   }

   public static final class HitCrosshair {
      public void handleDrawContext(DrawContext drawContext, Color color, float f, float f2) {
         drawContext.getMatrices().push();
         drawContext.getMatrices().translate(MinecraftHolder.mc.getWindow().getScaledWidth() / 2.0F, MinecraftHolder.mc.getWindow().getScaledHeight() / 2.0F, 0.0F);
         drawContext.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(45.0F));
         double d = 0.0;
         double d2 = 0.0;
         Color color2 = ColorUtils.createColor6(color, MathUtils.computeFloat3(color.getAlpha() / 255.0F, 0.0F, MathUtils.computeFloat3(f, 0.0F, 1.0F)));
         RenderUtils.handleMatrixStack23(drawContext.getMatrices(), color2, d, d2, d, d2 + f2);
         RenderUtils.handleMatrixStack23(drawContext.getMatrices(), color2, d, d2 + 0.45, d, d2 - f2);
         RenderUtils.handleMatrixStack23(drawContext.getMatrices(), color2, d - 0.45, d2, d + f2, d2);
         RenderUtils.handleMatrixStack23(drawContext.getMatrices(), color2, d, d2, d - f2, d2);
         drawContext.getMatrices().pop();
      }
   }

   public enum Style {
      Default,
      Dot,
      None;
   }

   public enum Trigger implements TextProvider {
      Attack("Triggers when attacking an entity"),
      Click("Triggers when clicking");

      public final String text;

      @Override
      public String getText() {
         return this.text;
      }

      Trigger(final String description) {
         this.text = description;
      }
   }
}
