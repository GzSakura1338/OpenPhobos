package org.phobos.core.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.screen.Screen;
import org.phobos.api.Easing;
import org.phobos.api.Named;
import org.phobos.api.TimeGate;
import org.phobos.module.Module;
import org.phobos.module.client.UIModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.gui.element.BooleanElement;
import org.phobos.core.gui.element.ColorElement;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.animation.EasedAnimation;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.hud.HudElement;
import org.phobos.core.bind.KeyCombination;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.gui.element.NumberElement;
import org.phobos.core.setting.Setting;

public class ModuleElement extends InteractiveElement implements Named {
   public final HudElement hudElement;
   public final FloatAnimator floatAnimator;
   public boolean flag;
   public boolean flag2;
   public final List<GuiElement> guiElementList;
   public final String name;
   public EasedAnimation easedAnimation;
   public final Module module;
   public final TimeGate.Normal normal;
   public String text;
   public final boolean flag3;
   public static final Logger LOGGER = LoggerFactory.getLogger(ModuleElement.class);
   public final FloatAnimator floatAnimator2 = new FloatAnimator(2.0F, 2.0F);
   public final Setting<Boolean> booleanValueSetting;
   public final FloatAnimator floatAnimator3 = new FloatAnimator();
   public boolean flag4;
   public String text2;
   public boolean flag5;

   @Override
   public float value6() {
      return this.getFloatValue4() + this.computeFlag(true);
   }

   @Override
   public float getFloatValue33() {
      return this.getFloatValue4() + Math.round(this.computeFlag(false));
   }

   public ModuleElement(GuiManager guiManager, GuiPanel guiPanel, Module module, float f, float f2, float f3, float f4) {
      super(guiManager, guiPanel, f, f2, f3, f4, null);
      this.floatAnimator = new FloatAnimator();
      this.guiElementList = new ArrayList<>();
      this.flag5 = false;
      this.normal = new TimeGate.Normal();
      this.text = "...";
      this.module = module;
      this.name = module.getText3() != null ? module.getText3() : module.getName();
      this.booleanValueSetting = null;
      this.hudElement = null;
      this.easedAnimation = new EasedAnimation(450L, 300L, module.check7());
      this.flag3 = module.getSettingList().isEmpty();
      this.run7();
   }

   @Override
   public void run5() {
      super.run5();
      this.easedAnimation = new EasedAnimation(450L, 300L, false);
   }

   public ModuleElement(
      GuiManager guiManager, GuiPanel guiPanel, HudElement hudElement, float f, float f2, float f3, float f4
   ) {
      super(guiManager, guiPanel, f, f2, f3, f4, null);
      this.floatAnimator = new FloatAnimator();
      this.guiElementList = new ArrayList<>();
      this.flag5 = false;
      this.normal = new TimeGate.Normal();
      this.text = "...";
      this.module = null;
      this.name = hudElement.getName();
      this.booleanValueSetting = null;
      this.hudElement = hudElement;
      this.easedAnimation = new EasedAnimation(450L, 300L, false);
      this.flag3 = hudElement.getSettingList().isEmpty();
      this.run7();
   }

   public ModuleElement(
      GuiManager guiManager,
      GuiPanel guiPanel,
      String text,
      ModuleElement moduleElement,
      Setting<Boolean> setting,
      float f,
      float f2,
      float f3,
      float f4
   ) {
      super(guiManager, guiPanel, f, f2, f3, f4, moduleElement);
      this.floatAnimator = new FloatAnimator();
      this.guiElementList = new ArrayList<>();
      this.flag5 = false;
      this.normal = new TimeGate.Normal();
      this.text = "...";
      this.module = null;
      this.name = text;
      this.booleanValueSetting = setting;
      this.hudElement = null;
      this.easedAnimation = new EasedAnimation(450L, 300L, false);
      this.flag3 = false;
      this.run7();
   }

   @Override
   public void handleDouble3(double d, double d2, int i) {
      if (!this.isFlag4()) {
         switch (i) {
            case 0:
               if (this.isFlag7()) {
                  if (this.module != null) {
                     this.run15();
                     this.module.run9();
                     return;
                  }

                  if (this.booleanValueSetting != null) {
                     this.booleanValueSetting.setValue(!this.booleanValueSetting.getValue());
                     return;
                  }

                  if (this.hudElement != null) {
                     this.hudElement.handleFlag3(!this.hudElement.isFlag3());
                     return;
                  }
               }
               break;
            case 1:
               if (this.isFlag7() && !this.flag3) {
                  this.setFlag10(!this.isFlag10());
                  this.run14();
               }
               break;
            case 2:
               if (!this.isFlag7() || this.module == null && this.hudElement == null) {
                  return;
               }

               if (!Screen.hasControlDown() || !Screen.hasShiftDown()) {
                  return;
               }

               if (this.module != null) {
                  this.module.setFlag4(true);
                  return;
               }

               this.hudElement.setFlag4(true);
         }
      }
   }

   @Override
   public void handleDouble2(double d, double d2) {
      boolean flag = this.isFlag10() || this.flag5;
      this.run();
      this.handleFlag(flag);
      this.floatAnimator3.setFloatValue(this.isFlag7() ? 5.0F : 50.0F);
      this.floatAnimator3.handleFloat3(10.0F, 0.1F);
      boolean flag2 = this.module != null
         ? this.module.check7()
         : (this.booleanValueSetting != null ? this.booleanValueSetting.getValue() : this.hudElement != null && !this.hudElement.isFlag3());
      if (this.easedAnimation.isFlag() != flag2) {
         this.easedAnimation.handleFlag2(flag2);
      }

      float f = this.easedAnimation.computeEasing(Easing.InOutEasing.INSTANCE3);
      this.handleDouble12(
         this.getFloatValue3() * f,
         0.0,
         this.getFloatValue3(),
         this.getFloatValue4(),
         ColorUtils.createColor5(this.getGuiManager().getUiTheme().getColor9(), this.isFlag7() ? 15 : 0)
      );
      if (f > 0.05) {
         this.handleDouble11(
            0.0,
            0.0,
            this.getFloatValue3() * f,
            this.getFloatValue4(),
            ColorUtils.createColor5(
               this.getGuiManager().getUiTheme().getColor13(),
               this.module == null && this.hudElement == null ? (this.booleanValueSetting != null ? 70 : 0) : 150
            ),
            this.getGuiManager().getUiTheme().getUIShader2()
         );
      }

      this.floatAnimator2
         .setFloatValue(UIModule.getUIModule().slide.getValue() && flag ? (float)((this.getFloatValue3() - this.computeText5(this.text2)) / 2.0) : 2.0F);
      this.floatAnimator2.handleFloat3(10.0F, 0.1F);
      this.handleText2(this.text2, this.floatAnimator2.getFloatValue2(), 3.0F, Color.WHITE, true);
      if (this.module != null) {
         this.handleFloat((float)this.computeText5(this.text2), this.module.getBind());
      }

      if (!this.flag3) {
         String text = this.module == null && this.booleanValueSetting == null ? this.describe2() : this.describeFlag(!this.isFlag10() && !this.flag5);
         this.handleText2(text, (int)(this.getFloatValue3() - this.computeText5(text) - 2.0), 3.0F, Color.WHITE, true);
      }

      double d3 = this.getGuiElement() != null && this.getGuiElement() instanceof ModuleElement moduleElement && !moduleElement.isFlag10()
         ? this.computeFlag(false)
         : this.getFloatValue33();
      boolean flag3 = d >= this.getFloatValue2()
         && d <= this.getFloatValue2() + this.getFloatValue3()
         && d2 >= this.getFloatValue()
         && d2 <= this.getFloatValue() + d3 + 1.0;
      if (this.getFloatValue13().getFloatValue2() <= 0.05F) {
         if (!this.flag) {
            this.guiElementList.forEach(guiElementx -> {
               guiElementx.getFloatAnimator8().setFloatValue2(0.0F);
               switch (guiElementx) {
                  case NumberElement<?> numberElement:
                     numberElement.getFloatAnimator4().run2();
                     break;
                  case BooleanElement booleanElement:
                     if (booleanElement.getEasedAnimation().isFlag()) {
                        booleanElement.getEasedAnimation().handleFlag2(false);
                     }
                     break;
                  case ColorElement colorElement:
                     colorElement.getAnimatorGroup().run2();
                     colorElement.setFlag10(false);
                     break;
                  case InteractiveElement interactiveElement:
                     interactiveElement.setFlag10(false);
                     break;
                  default:
               }
            });
            this.flag = true;
         }
      } else {
         this.handleDouble(0.0, 0.0, this.getFloatValue3(), d3 + 1.0);
         this.flag = false;
         float f2 = this.getFloatValue4() + 2.0F;

         for (GuiElement guiElement : this.guiElementList) {
            if (guiElement.isFlag8()) {
               if (guiElement instanceof InteractiveElement interactiveElement) {
                  if (!flag) {
                     interactiveElement.setFlag10(false);
                  }

                  interactiveElement.setFloatValue(this.getFloatValue() + f2);
                  interactiveElement.setFloatValue2(this.getFloatValue2() + 2.0F);
                  if (!this.getGuiManager().testGuiElement(interactiveElement)) {
                     f2 += interactiveElement.getFloatValue33() + 2.0F;
                     interactiveElement.run11();
                  } else {
                     if (!this.isFlag5()) {
                        CursorUtils.Style style = interactiveElement.getStyle();
                        if (style != CursorUtils.Style.Default) {
                           HoverRegion hoverRegion = interactiveElement.getHoverRegion();
                           if (hoverRegion.testDouble(d, d2)) {
                              this.handleStyle(style);
                           }
                        }
                     }

                     interactiveElement.handleDouble7(d, d2, flag3);
                     this.getGuiManager().getDrawStats().run2();
                     interactiveElement.handleDouble2(d, d2);
                     this.getGuiManager().getDrawStats().run();
                     this.getGuiManager().getDrawStats().run5();
                     if (flag) {
                        interactiveElement.handleDouble4(d, d2);
                        this.getGuiManager().getDrawStats().run2();
                     }

                     f2 += interactiveElement.getFloatValue33() + 2.0F;
                  }
               } else {
                  guiElement.setFloatValue(this.getFloatValue() + f2);
                  guiElement.setFloatValue2(this.getFloatValue2() + 2.0F);
                  if (!this.getGuiManager().testGuiElement(guiElement)) {
                     f2 += guiElement.getFloatValue4() + 2.0F;
                     guiElement.run11();
                  } else {
                     if (!this.isFlag5()) {
                        CursorUtils.Style style2 = guiElement.getStyle();
                        if (style2 != CursorUtils.Style.Default) {
                           HoverRegion hoverRegion2 = guiElement.getHoverRegion();
                           if (hoverRegion2.testDouble(d, d2)) {
                              this.handleStyle(style2);
                           }
                        }
                     }

                     guiElement.handleDouble7(d, d2, flag3);
                     this.getGuiManager().getDrawStats().run2();
                     guiElement.handleDouble2(d, d2);
                     this.getGuiManager().getDrawStats().run();
                     this.getGuiManager().getDrawStats().run5();
                     if (flag) {
                        if (!(this.getGuiElement() != null && this.getGuiElement() instanceof ModuleElement moduleElement2)) {
                           guiElement.handleDouble4(d, d2);
                           this.getGuiManager().getDrawStats().run2();
                        } else if (moduleElement2.isFlag10()) {
                           guiElement.handleDouble4(d, d2);
                           this.getGuiManager().getDrawStats().run2();
                        }
                     }

                     f2 += guiElement.getFloatValue4() + 2.0F;
                  }
               }
            }
         }

         this.run3();
      }
   }

   public String describe() {
      if (this.booleanValueSetting != null) {
         return this.booleanValueSetting.getName();
      } else {
         return this.hudElement != null ? this.hudElement.getName() : this.name;
      }
   }

   @Override
   public void handleFlag(boolean flag) {
      this.floatAnimator.setFloatValue(Screen.hasShiftDown() ? 1.0F : -1.0F);
      this.floatAnimator.handleFloat3(10.0F, 0.08F);
      if (flag) {
         float f = 0.0F;

         for (GuiElement guiElement : this.guiElementList) {
            if (guiElement.isFlag8()) {
               if (guiElement instanceof InteractiveElement interactiveElement) {
                  f += interactiveElement.value6() + 2.0F;
               } else {
                  f += guiElement.getFloatValue4() + 2.0F;
               }
            }
         }

         this.getFloatValue13().setFloatValue(f + 0.8F);
         this.getFloatValue13().handleFloat3(10.0F, 0.1F);
      } else {
         this.getFloatValue13().setFloatValue(0.0F);
         this.getFloatValue13().handleFloat3(10.0F, 0.1F);
      }
   }

   public void run() {
      String text = this.getText();
      if (text != null) {
         if (this.isFlag7()) {
            this.getGuiManager().handleText(text, this);
         } else {
            this.getGuiManager().handleObject(this);
         }
      }
   }

   public String describeFlag(boolean flag) {
      return flag ? UIModule.getUIModule().open.getValue() : UIModule.getUIModule().close.getValue();
   }

   public String getText() {
      if (this.module != null) {
         return this.module.getText();
      } else if (this.booleanValueSetting != null) {
         return this.booleanValueSetting.getText();
      } else {
         return this.hudElement != null ? this.hudElement.getText() : null;
      }
   }

   public String describe2() {
      if (this.normal.testLong2(300L) && this.text.length() < (this.isFlag10() ? 4 : 3)) {
         this.text = this.text + ".";
      }

      if (this.text.length() > 3 && this.isFlag10()) {
         this.text = "";
      }

      return this.text;
   }

   public void run7() {
      String text = this.module == null
         ? (
            this.booleanValueSetting != null
               ? this.booleanValueSetting.getName()
               : (this.hudElement != null ? this.hudElement.getName() : this.name)
         )
         : this.module.getName();
      this.text2 = this.getGuiElement() != null && this.getGuiElement() instanceof ModuleElement moduleElement && this.getGuiElement().getGuiElement() != null
         ? text.replaceAll(moduleElement.describe(), "")
         : text;
   }

   @Override
   public CursorUtils.Style getStyle() {
      return (this.module != null || this.hudElement != null) && Screen.hasShiftDown() && Screen.hasControlDown()
         ? CursorUtils.Style.Link
         : CursorUtils.Style.Default;
   }

   public void handleFloat(float f, Setting<KeyCombination> setting) {
      if (UIModule.getUIModule().binds.getValue()) {
         KeyCombination keyCombination = setting.getValue();
         if (!keyCombination.isEmpty()) {
            float f4 = this.floatAnimator.getFloatValue2();
            if (f4 > 0.05F) {
               String text = keyCombination.toString();
               this.handleDouble(0.0, 0.0, this.getFloatValue3() - 12.0F, this.getFloatValue4());
               float f2 = (float)this.computeText4(text, 0.7F);
               float f3 = this.getFloatValue3() - f2 * f4 - 14.0F;
               this.handleText4(text, f3, 5.0F, ColorUtils.createColor6(this.getGuiManager().getUiTheme().getColor10(), f4), 0.7F, true);
               this.run3();
            }
         }
      }
   }

   public boolean isFlag4() {
      return !ModuleGroups.getINSTANCE().isFlag()
         && (this.module != null && this.module.isFlag4() || this.hudElement != null && this.hudElement.isFlag4());
   }

   public void setFlag5(boolean flag) {
      this.flag5 = flag;
   }

   public boolean isFlag52() {
      return this.flag5;
   }

   public float computeFlag(boolean flag) {
      return flag ? this.getFloatValue13().getFloatValue() : this.getFloatValue13().getFloatValue2();
   }

   public void setFlag(boolean flag) {
      this.flag4 = flag;
   }

   public void setFlag2(boolean flag) {
      this.flag2 = flag;
   }

   public boolean isFlag2() {
      return this.flag2;
   }

   public Setting<Boolean> getBooleanValueSetting() {
      return this.booleanValueSetting;
   }

   public void run2() {
      this.guiElementList.forEach(guiElement -> {
         if (guiElement instanceof ModuleElement moduleElement) {
            moduleElement.run2();
         }
      });
   }

   public List<GuiElement> getGuiElementList() {
      return this.guiElementList;
   }

   public ModuleElement getModuleElement() {
      return this.module == null && this.hudElement == null
         ? (this.getGuiElement() instanceof ModuleElement moduleElement ? moduleElement.getModuleElement() : this)
         : this;
   }

   public Module getModule() {
      return this.module;
   }

   public HudElement getHudElement() {
      return this.hudElement;
   }

   @Override
   public String getName() {
      return this.name;
   }

   public boolean isFlag3() {
      return this.flag4;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public void handleGuiElement(GuiElement guiElement) {
      this.guiElementList.add(guiElement);
   }

   public void setFlag3(boolean flag) {
      this.flag = flag;
   }
}
