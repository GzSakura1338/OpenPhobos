package org.phobos.core.hud.element;

import java.awt.Color;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.client.gui.DrawContext;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.phobos.module.client.HudModule;
import org.phobos.module.Module;
import org.phobos.type.CenterRightEnum;
import org.phobos.type.TopCenterEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.module.Categories;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.render.GradientUtils;
import org.phobos.core.hud.HudAnchor;
import org.phobos.core.hud.HudElement;
import org.phobos.core.module.ModuleManager;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.setting.Setting;
import org.phobos.core.util.StreamUtils;

public class ArrayListHudElement extends HudElement {
   public final Setting<Boolean> vertical = this.resolveSetting2(new BooleanSetting("Vertical").resolveSetting7("Does a vertical roll animation"));
   public final Setting<Boolean> line;
   public final Setting<Boolean> floating = this.resolveSetting2(new BooleanSetting("Floating").resolveSetting7("Allows modules to reposition vertically"));
   public final Setting<Float> spacing = this.resolveSetting2(new NumberSetting<>("Spacing", 12.0F, 10.0F, 15.0F).resolveNumberSetting("Sync", 12.0F));
   public final Setting<Color> main;
   public static final Logger LOGGER = LoggerFactory.getLogger(ArrayListHudElement.class);
   public final Set<Module> moduleSet;
   public final Setting<Float> glow;
   public final Setting<Color> secondary;

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      ModuleManager moduleManager = this.phobosClient.getModuleManager();
      mc.getProfiler().push("Sorting");
      List<Module> list = StreamUtils.resolveStream(
            moduleManager.getList().stream().filter(module -> !module.getCategory().equals(Categories.client) && !module.hidden.getValue()),
            module -> -this.computeText2(this.describeModule2(module))
         )
         .toList();
      mc.getProfiler().swap("Pre");
      int i3 = this.topCenterEnum.equals(TopCenterEnum.Bottom) ? -10 : 0;
      float f20 = i3;
      float f21 = 0.0F;
      float f22 = 0.0F;
      MutableBoolean mutableBoolean = new MutableBoolean();
      if (this.line.getValue()) {
         switch (this.centerRightEnum) {
            case Right:
               float f = this.check2() ? this.getFloatValue2() : this.getFloatValue2() + this.getFloatValue3();
               this.handleDouble(-this.getFloatValue2(), -this.getFloatValue(), f + 3.0F, i2);
               mutableBoolean.setTrue();
               break;
            case Center:
            case Left:
               this.handleDouble(-3.0, -this.getFloatValue(), i, i2);
               mutableBoolean.setTrue();
         }
      }

      mc.getProfiler().swap("Render");
      this.setFlag8(false);
      float f23 = this.spacing.getValue();

      for (Module module : list) {
         if (!module.check7()) {
            this.moduleSet.remove(module);
         }

         String text2 = module.getText3() != null ? module.getText3() : module.getName();
         boolean flag = module.check7();
         float f6 = this.computeText2(this.describeModule2(module));
         float f7 = flag ? f6 : 0.0F;
         module.getFloatAnimator3().setFloatValue(f7);
         module.getFloatAnimator3().handleFloat3(5.0F, flag ? 0.1F : 0.2F);
         module.getFloatAnimator7().setFloatValue(flag ? 1.0F : 0.0F);
         module.getFloatAnimator7().handleFloat3(flag ? 10.0F : 5.0F, flag ? 0.1F : 0.3F);
         float f8 = module.getFloatAnimator3().getFloatValue2();
         f21 = Math.max(f21, f8);
         float f9 = Math.abs(f7 - f8);
         float f10 = this.floating.getValue() ? (flag ? 0.1F : 0.3F) : (flag ? 0.08F : 0.1F);
         module.getFloatAnimator5().setFloatValue(flag ? 1.0F : (f9 < 75.0F ? 0.0F : 1.0F));
         module.getFloatAnimator5().handleFloat3(10.0F, f10);
         float f11 = module.getFloatAnimator5().getFloatValue2();
         float f12 = module.getFloatAnimator7().getFloatValue2();
         float f13 = this.getFloatValue() + f20;
         if (this.floating.getValue()) {
            module.getFloatAnimator4().setFloatValue(f20);
            module.getFloatAnimator4().handleFloat3(10.0F, 0.1F);
         }

         float f14 = this.floating.getValue() ? this.getFloatValue() + module.getFloatAnimator4().getFloatValue2() : f13;
         float f15 = this.check2() ? this.getFloatValue2() - f8 : this.getFloatValue2() - f6 + f8;
         if (f12 <= 0.05F) {
            module.getFloatAnimator4().setFloatValue2(f20);
            f20 += this.computeFloat(Math.min(f11 * f23, f23));
         } else {
            if (this.background.getValue()) {
               float f2 = this.check2() ? this.getFloatValue2() - f8 : this.getFloatValue2();
               float f3 = this.check2() ? this.getFloatValue2() : this.getFloatValue2() + f8;
               this.handleFloat(f2, f14, f3 - 1.0F, f14 + 9.0F);
            }

            String text3 = module.nothing();
            float f16 = this.computeText2(text2);
            this.handleDouble5(f15 - 5.0F, f14 - 1.5F, f6 + 10.0F, 14.0);
            this.handleDrawContext7(drawContext, text2, f15, f14, f12);
            f22 = Math.max(f22, f15 + f16);
            if (text3 != null) {
               float f4 = (module.getFloatAnimator3().getFloatValue() - module.getFloatAnimator3().getFloatValue2()) / f6;
               if (f9 < 1.0F) {
                  this.moduleSet.add(module);
               }

               String text = HudModule.getHudModule().describeText3(text3);
               float f5 = f15 + f16 + (this.check4() ? this.computeText2("a") / 1.5F : this.computeText2(" "));
               this.handleDrawContext7(drawContext, text, f5, f14 + (this.vertical.getValue() && !this.moduleSet.contains(module) ? f4 * 12.0F : 0.0F), f12);
            }

            this.run3();
            f20 += this.computeFloat(Math.min(f11 * f23, f23));
         }
      }

      this.setFlag8(true);
      mc.getProfiler().pop();
      if (mutableBoolean.isTrue()) {
         this.run3();
      }

      if (this.line.getValue()) {
         float f17 = this.getFloatValue2();
         float f18 = this.getFloatValue();
         float f19 = f20;
         this.phobosClient
            .getRenderManager()
            .handleConsumer(
               drawContext2 -> {
                  switch (this.topCenterEnum) {
                     case Top:
                        Color color = GradientUtils.resolveColor2(
                           HudModule.getHudModule().getSpeed().getValue().intValue(), this.getFloatValue(), this.main.getValue(), this.secondary.getValue()
                        );
                        Color color2 = GradientUtils.resolveColor2(
                           HudModule.getHudModule().getSpeed().getValue().intValue(),
                           this.getFloatValue() + f19,
                           this.main.getValue(),
                           this.secondary.getValue()
                        );
                        RenderUtils.handleMatrixStack26(
                           drawContext.getMatrices(),
                           this.check2() ? f17 + 1.0F : f17 - 3.0F,
                           f18 - 1.0F,
                           1.5F,
                           f19,
                           this.glow.getValue(),
                           color,
                           color2,
                           color,
                           color2
                        );
                        break;
                     case Bottom:
                        float f4x = f19 + 10.0F;
                        Color color3 = GradientUtils.resolveColor2(
                           HudModule.getHudModule().getSpeed().getValue().intValue(),
                           this.getFloatValue() + f4x,
                           this.main.getValue(),
                           this.secondary.getValue()
                        );
                        Color color4 = GradientUtils.resolveColor2(
                           HudModule.getHudModule().getSpeed().getValue().intValue(),
                           this.getFloatValue() - f4x,
                           this.main.getValue(),
                           this.secondary.getValue()
                        );
                        RenderUtils.handleMatrixStack26(
                           drawContext.getMatrices(),
                           this.check2() ? f17 + 1.0F : f17 - 3.0F,
                           f18 + f4x + 2.0F,
                           1.5F,
                           -f4x,
                           this.glow.getValue(),
                           color3,
                           color4,
                           color3,
                           color4
                        );
                  }
               }
            );
      }

      this.setFloatValue4(f20 - i3 + (!this.topCenterEnum.equals(TopCenterEnum.Bottom) ? -2 : 2));
      this.setFloatValue3(f21 * (this.centerRightEnum.equals(CenterRightEnum.Right) ? -1 : 1));
   }

   @Override
   public void run7() {
      this.setHudAnchor(HudAnchor.createHudAnchor(() -> mc.getWindow().getScaledWidth() - 2.0F, () -> 0.0F, TopCenterEnum.Top, CenterRightEnum.Right));
      this.run9();
   }

   @Override
   public float[] values() {
      return values2(this.getFloatValue2(), this.getFloatValue(), this.getFloatValue3(), this.getFloatValue4());
   }

   public float computeFloat(float f) {
      return !this.topCenterEnum.equals(TopCenterEnum.Bottom) ? f : -f;
   }

   @Override
   public boolean alwaysTrue() {
      return true;
   }

   public ArrayListHudElement() {
      super("Modules", 0.0F, 0.0F, 0.0F, 40.0F);
      this.line = this.resolveSetting2(new BooleanSetting("Line"));
      this.main = this.resolveSetting2(new ColorSetting("Main", Color.WHITE).getColorSetting());
      this.secondary = this.resolveSetting2(new ColorSetting("Secondary", Color.BLACK));
      this.glow = this.resolveSetting2(new NumberSetting<>("Glow", 1.0F, 0.0F, 1.0F));
      this.moduleSet = new HashSet<>();
      this.resolveSettingGroup(this.line, this.main, this.secondary, this.glow);
   }

   public String describeModule(Module module) {
      return HudModule.getHudModule().describeModule(module);
   }

   public String describeModule2(Module module) {
      return HudModule.getHudModule().describeModule2(module);
   }
}
