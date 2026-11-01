package org.phobos.core.gui.element;

import java.awt.Color;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.client.util.math.MatrixStack;
import org.phobos.api.DragCapture;
import org.phobos.api.GuiGraphics;
import org.phobos.api.Easing.InOutEasing;
import org.phobos.module.client.UIModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.animation.AnimatorGroup;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.gui.CursorUtils;
import org.phobos.core.animation.EasedAnimation;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.gui.HoverRegion;
import org.phobos.core.math.MathUtils;
import org.phobos.core.gui.ModuleElement;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.gui.UiTheme;

public class ColorElement extends SettingValueElement<Color, ColorSetting> implements DragCapture {
   public float floatValue;
   public float floatValue2;
   public boolean flag;
   public static final Logger LOGGER = LoggerFactory.getLogger(ColorElement.class);
   public final List<ColorElement.Button> copy;
   public final FloatAnimator floatAnimator;
   public final AnimatorGroup animatorGroup;
   public float floatValue3;
   public final FloatAnimator floatAnimator2 = new FloatAnimator();
   public float floatValue4;
   public final FloatAnimator floatAnimator3;
   public boolean flag2;
   public boolean flag3;
   public final Color color;
   public final FloatAnimator floatAnimator4;
   public final FloatAnimator floatAnimator5;
   public final FloatAnimator floatAnimator6 = new FloatAnimator();
   public final FloatAnimator floatAnimator7;
   public boolean flag4;
   public boolean flag5;

   public ColorElement(
      GuiManager guiManager,
      GuiPanel guiPanel,
      float f,
      float f2,
      float f3,
      float f4,
      ModuleElement moduleElement,
      String text,
      ColorSetting colorSetting
   ) {
      super(guiManager, guiPanel, f, f2, f3, f4, moduleElement, text, colorSetting);
      this.floatAnimator = new FloatAnimator();
      this.floatAnimator5 = new FloatAnimator();
      this.floatAnimator7 = new FloatAnimator();
      this.floatAnimator4 = new FloatAnimator();
      this.floatAnimator3 = new FloatAnimator(10.0F);
      this.animatorGroup = new AnimatorGroup();
      this.color = new Color(120, 120, 120, 42);
      Color color = colorSetting.getValue();
      float[] floats = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
      this.floatValue4 = floats[0];
      this.floatValue3 = floats[1];
      this.floatValue2 = floats[2];
      this.floatValue = color.getAlpha() / 255.0F;
      ColorElement.Button button = new ColorElement.Button(
         "Sync", f, f2, f3, f4, colorSetting.isFlag2(), (buttonx, booleanValue) -> colorSetting.setFlag2(booleanValue)
      );
      ColorElement.Button button2 = new ColorElement.Button(
         "Rainbow",
         f,
         f2,
         f3,
         f4,
         colorSetting.isFlag(),
         !colorSetting.isFlag3(),
         (buttonx, booleanValue) -> colorSetting.setFlag(booleanValue),
         (buttonx, booleanValue) -> colorSetting.setFlag3(!colorSetting.isFlag3())
      );
      ColorElement.Button button3 = new ColorElement.Button("Copy", f, f2, f3, f4, false, (buttonx, booleanValue) -> {
         buttonx.flag4 = false;
         SelectionManager.setClipboard(mc, String.valueOf(ColorUtils.computeColor(colorSetting.getValue())));
      });
      ColorElement.Button button4 = new ColorElement.Button("Paste", f, f2, f3, f4, false, (buttonx, booleanValue) -> {
         buttonx.flag4 = false;
         String textx = SelectionManager.getClipboard(mc);
         if (textx != null && colorSetting.testText(textx)) {
            float[] floatsx = Color.RGBtoHSB(colorSetting.value(), colorSetting.value2(), colorSetting.value4(), null);
            this.floatValue4 = floatsx[0];
            this.floatValue3 = floatsx[1];
            this.floatValue2 = floatsx[2];
            this.floatValue = colorSetting.value3() / 255.0F;
         }
      });
      this.copy = List.of(button, button2, button3, button4);
      this.animatorGroup.handleFloatAnimator(this.floatAnimator2);
      this.animatorGroup.handleFloatAnimator(this.floatAnimator6);
      this.animatorGroup.handleFloatAnimator(this.floatAnimator);
      this.animatorGroup.handleFloatAnimator(this.floatAnimator5);
      this.animatorGroup.handleFloatAnimator(this.floatAnimator7);
      this.animatorGroup.handleFloatAnimator(this.floatAnimator4);
      this.animatorGroup.handleFloatAnimator(this.floatAnimator3);
   }

   @Override
   public void handleDouble2(double d, double d2) {
      this.handleFlag(this.isFlag10());
      MatrixStack matrixStack = this.getGuiGraphics().getDrawContext().getMatrices();
      Color color3 = Color.WHITE;
      Color color4 = Color.BLACK;
      float f45 = 8.0F * UIModule.getUIModule().width.getValue();
      this.handleDouble11(
         0.0,
         0.0,
         this.getFloatValue3(),
         this.getFloatValue4(),
         ColorUtils.createColor5(this.getGuiManager().getUiTheme().getColor8(), this.isFlag7() ? 15 : 0),
         this.getGuiManager().getUiTheme().getUIShader10()
      );
      this.handleDouble12(this.getFloatValue3() - f45 - 3.0F, 3.0, this.getFloatValue3() - 1.5F, this.getFloatValue4() - 3.0F, this.getValue());
      this.handleDouble10(this.getFloatValue3() - f45 - 3.0F, 3.0, 9.2F, 9.0, color4);
      String text = this.getText3();
      this.handleText2(text, 2.0F, 3.0F, color3, true);
      if (!(this.getFloatValue13().getFloatValue2() > 0.05F)) {
         this.animatorGroup.handleFloat3(0.0F);
         this.animatorGroup.handleFloat2(0.0F);
      } else {
         this.handleDouble(0.0, 2.0, this.getFloatValue3(), this.getFloatValue33());
         this.handleDouble10(
            0.0,
            this.getFloatValue4() + 0.4F,
            this.getFloatValue3(),
            this.getFloatValue4() + this.getFloatValue33() - 30.0F,
            this.getGuiManager().getUiTheme().getColor8()
         );
         float f14 = this.getFloatValue2() + 1.0F + this.floatAnimator2.getFloatValue2();
         float f15 = this.getFloatValue() + 79.0F;
         float f16 = f14 + 4.0F;
         float f17 = f15 + 1.0F;
         float f18 = f16 + (this.getFloatValue3() - 20.0F);
         float f19 = f17 + (this.getFloatValue4() - 68.0F);
         int i4 = (int)MathUtils.computeDouble5(d, f16, f18);
         int i5 = (int)MathUtils.computeDouble5(d2, f19, f17);
         RenderUtils.handleMatrixStack20(matrixStack, color3, f16 - 0.5F, f17 - 0.5F + 1.24F, f18 + 0.5F, f19 + 0.5F - 0.92F);
         this.handleFloat(f16, f17, f18, f19);
         float f20 = this.floatValue3 * (f18 - f16);
         float f21 = (1.0F - this.floatValue2) * (f19 - f17);
         if (this.flag2) {
            float f = f18 - f16;
            float f2 = Math.max(i4 - f16, 0.0F);
            this.floatValue3 = f2 / f;
            f20 = f2;
            float f3 = f19 - f17;
            float f4 = i5 - f17;
            this.floatValue2 = Math.max(1.0F - f4 / f3, 0.0F);
            f21 = f4;
            this.handleInt(Color.HSBtoRGB(this.floatValue4, this.floatValue3, this.floatValue2));
         }

         this.floatAnimator6.setFloatValue(f20);
         this.floatAnimator.setFloatValue(f21);
         f20 = this.floatAnimator6.getFloatValue2();
         f21 = this.floatAnimator.getFloatValue2();
         float f22 = f16 + f20 - 0.5F;
         float f23 = f17 + f21 - 0.5F;
         float f24 = f16 + f20 + 0.5F;
         float f25 = f17 + f21 + 0.5F;
         RenderUtils.handleMatrixStack15(matrixStack, color4, f22 - 1.0F, f23 - 1.0F, f22, f25 + 1.0F, 2.0F);
         RenderUtils.handleMatrixStack15(matrixStack, color4, f24, f23 - 1.0F, f24 + 1.0F, f25 + 1.0F, 2.0F);
         RenderUtils.handleMatrixStack15(matrixStack, color4, f22, f23 - 1.0F, f24, f23, 2.0F);
         RenderUtils.handleMatrixStack15(matrixStack, color4, f22, f25, f24, f25 + 1.0F, 2.0F);
         RenderUtils.handleMatrixStack15(matrixStack, color3, f22, f23, f24, f25, 2.0F);
         float f26 = f18 + 2.0F;
         float f27 = f26 + 8.0F;
         int i6 = (int)MathUtils.computeDouble5(d2, f19, f17);
         float f28 = f19 - f17;
         float f29 = (1.0F - this.floatValue4) * f28;
         if (this.flag) {
            float f5 = i6 - f17;
            this.floatValue4 = 1.0F - f5 / f28;
            f29 = f5;
            this.handleInt(Color.HSBtoRGB(this.floatValue4, this.floatValue3, this.floatValue2));
         }

         this.floatAnimator5.setFloatValue(f29);
         RenderUtils.handleMatrixStack20(matrixStack, color3, f26 - 0.5F, f17 - 0.5F + 1.24F, f27 + 0.5F, f19 + 0.5F - 0.96F);
         if (this.getSetting().isFlag()) {
            float f7 = f26 + 10.0F;
            float f8 = f26 + 13.0F;
            int i = (int)MathUtils.computeDouble5(d2, f19, f17);
            float f9 = f19 - f17;
            float f10 = (1.0F - this.getSetting().getFloatValue22()) * f9;
            if (this.flag3) {
               float f6 = i - f17;
               this.getSetting().setFloatValue2(1.0F - f6 / f9);
               f10 = f6;
            }

            this.floatAnimator4.setFloatValue(f10);
            RenderUtils.handleMatrixStack20(matrixStack, this.color, f7 - 0.5F, f17 - 0.5F + 1.24F, f8 + 0.5F, f19 + 0.5F - 0.96F);
            float f11 = f17 + this.floatAnimator4.getFloatValue2() - 0.5F;
            float f12 = f17 + this.floatAnimator4.getFloatValue2() + 0.5F;
            RenderUtils.handleMatrixStack15(matrixStack, color4, f7 - 1.0F, f11 - 1.0F, f7, f12 + 1.0F, 3.0F);
            RenderUtils.handleMatrixStack15(matrixStack, color4, f8, f11 - 1.0F, f8 + 1.0F, f12 + 1.0F, 3.0F);
            RenderUtils.handleMatrixStack15(matrixStack, color4, f7, f11 - 1.0F, f8, f11, 3.0F);
            RenderUtils.handleMatrixStack15(matrixStack, color4, f7, f12, f8, f12 + 1.0F, 3.0F);
            RenderUtils.handleMatrixStack15(matrixStack, color3, f7, f11, f8, f12, 3.0F);
         }

         float f30 = 0.2F;
         float f31 = 5.0F;
         float f32 = f19 - f17;
         float f33 = f32 / 5.0F;
         float f34 = f17;

         for (int i2 = 0; i2 < 5.0F; i2++) {
            boolean flag = i2 == 4.0F;
            Color color = new Color(Color.HSBtoRGB(1.0F - 0.2F * i2, 1.0F, 1.0F));
            Color color2 = new Color(Color.HSBtoRGB(1.0F - 0.2F * (i2 + 1), 1.0F, 1.0F));
            RenderUtils.handleMatrixStack30(matrixStack, f26, f34, f27, f34 + f33, color, color2);
            if (!flag) {
               f34 += f33;
            }
         }

         float f35 = f17 + this.floatAnimator5.getFloatValue2() - 0.5F;
         float f36 = f17 + this.floatAnimator5.getFloatValue2() + 0.5F;
         RenderUtils.handleMatrixStack15(matrixStack, color4, f26 - 1.0F, f35 - 1.0F, f26, f36 + 1.0F, 3.0F);
         RenderUtils.handleMatrixStack15(matrixStack, color4, f27, f35 - 1.0F, f27 + 1.0F, f36 + 1.0F, 3.0F);
         RenderUtils.handleMatrixStack15(matrixStack, color4, f26, f35 - 1.0F, f27, f35, 3.0F);
         RenderUtils.handleMatrixStack15(matrixStack, color4, f26, f36, f27, f36 + 1.0F, 3.0F);
         RenderUtils.handleMatrixStack15(matrixStack, color3, f26, f35, f27, f36, 3.0F);
         float f37 = f19 - 8.0F;
         float f38 = f37 + 6.0F;
         int i7 = Color.HSBtoRGB(this.floatValue4, this.floatValue3, this.floatValue2);
         int i8 = i7 >> 16 & 0xFF;
         int i9 = i7 >> 8 & 0xFF;
         int i10 = i7 & 0xFF;
         float f39 = f18 + this.floatAnimator3.getFloatValue2() - f16;
         float f40 = this.floatValue * f39;
         if (this.flag4) {
            int i3 = (int)MathUtils.computeDouble5(d, f16, f18 + this.floatAnimator3.getFloatValue2());
            float f13 = i3 - f16;
            this.floatValue = MathUtils.computeFloat3(f13 / f39, 0.0F, 1.0F);
            f40 = f13;
            this.handleInt(new Color(i8, i9, i10).getRGB());
         }

         this.floatAnimator7.setFloatValue(f40);
         RenderUtils.handleMatrixStack14(
            matrixStack, color3, f16 - 0.5F, f37 - 0.5F, f18 + 0.5F + this.floatAnimator3.getFloatValue2() - (f16 - 0.5F), f38 + 0.5F - (f37 - 0.5F)
         );
         RenderUtils.handleMatrixStack32(matrixStack, f16, f37, f18 + this.floatAnimator3.getFloatValue2(), f38, Color.BLACK, new Color(i8, i9, i10, 255));
         float f41 = f16 + this.floatAnimator7.getFloatValue2() - 0.5F;
         float f42 = f16 + this.floatAnimator7.getFloatValue2() + 0.5F - this.floatAnimator3.getFloatValue2();
         RenderUtils.handleMatrixStack20(matrixStack, color4, f41 - 1.0F, f37, f42 + 1.0F + this.floatAnimator3.getFloatValue2(), f38);
         RenderUtils.handleMatrixStack20(matrixStack, color3, f41, f37 + 0.6F, f42 + this.floatAnimator3.getFloatValue2(), f38 - 0.6F);
         float f43 = 0.0F;
         float f44 = this.getFloatValue4();

         for (ColorElement.Button button : this.copy) {
            button.setFloatValue3(this.getFloatValue2() + 2.0F);
            button.setFloatValue2(this.getFloatValue() + 85.0F + f43);
            button.setFloatValue4(this.getFloatValue3() - 4.0F);
            button.setFloatValue(this.getFloatValue4());
            button.handleUiTheme(this.getGuiManager().getUiTheme(), this.getGuiGraphics(), d, d2);
            f43 += f44 + 2.0F;
         }

         if (this.getSetting().isFlag2() || this.getSetting().isFlag()) {
            float[] floats = Color.RGBtoHSB(this.getValue().getRed(), this.getValue().getGreen(), this.getValue().getBlue(), null);
            if (this.floatValue4 != floats[0]
               || this.floatValue3 != floats[1]
               || this.floatValue2 != floats[2]
               || this.floatValue != this.getValue().getAlpha() / 255.0F) {
               this.floatValue4 = floats[0];
               this.floatValue3 = floats[1];
               this.floatValue2 = floats[2];
               this.floatValue = this.getValue().getAlpha() / 255.0F;
            }
         }

         this.run3();
      }
   }

   public boolean check() {
      return !this.flag2 && !this.flag && !this.flag4 && !this.flag3;
   }

   public FloatAnimator getFloatAnimator() {
      return this.floatAnimator;
   }

   @Override
   public void handleDouble3(double d, double d2, int i) {
      switch (i) {
         case 0:
            if (!this.check() || !this.isFlag10()) {
               return;
            }

            this.copy.forEach(button -> {
               if (button.testDouble(d, d2)) {
                  button.handleRunnable2(this::run15);
               }
            });
            break;
         case 1:
            if (this.isFlag7()) {
               this.setFlag10(!this.isFlag10());
               return;
            }

            if (!this.check() || !this.isFlag10()) {
               return;
            }

            this.copy.forEach(button -> {
               if (button.testDouble(d, d2)) {
                  button.handleRunnable(this::run14);
               }
            });
      }
   }

   public FloatAnimator getFloatAnimator4() {
      return this.floatAnimator4;
   }

   public FloatAnimator getFloatAnimator7() {
      return this.floatAnimator7;
   }

   @Override
   public CursorUtils.Style getStyle() {
      return this.isFlag10() && this.flag2 ? CursorUtils.Style.Crosshair : CursorUtils.Style.Default;
   }

   @Override
   public HoverRegion getHoverRegion() {
      return HoverRegion.createHoverRegion(0.0F, 0.0F, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
   }

   public AnimatorGroup getAnimatorGroup() {
      return this.animatorGroup;
   }

   public void handleFloat(float f, float f2, float f3, float f4) {
      int i = Color.HSBtoRGB(this.floatValue4, 1.0F, 1.0F);
      Color color = new Color(i);
      RenderUtils.handleMatrixStack18(this.getGuiGraphics().getDrawContext().getMatrices(), f, f2, f3, f4, Color.BLACK, Color.WHITE, Color.BLACK, color);
   }

   public FloatAnimator getFloatAnimator3() {
      return this.floatAnimator3;
   }

   public void run() {
      this.flag2 = false;
      this.flag = false;
      this.flag4 = false;
      this.flag3 = false;
   }

   public FloatAnimator getFloatAnimator5() {
      return this.floatAnimator5;
   }

   public void handleInt(int i) {
      if (this.getSetting().isFlag() && !this.getSetting().isFlag3()) {
         this.setValue(new Color(this.getSetting().value(), this.getSetting().value2(), this.getSetting().value4(), (int)(this.floatValue * 255.0F)));
      } else {
         this.setValue(new Color(i >> 16 & 0xFF, i >> 8 & 0xFF, i & 0xFF, (int)(this.floatValue * 255.0F)));
      }

      this.getSetting().setFloatValue3(this.floatValue3);
      this.getSetting().setFloatValue(this.floatValue2);
   }

   @Override
   public void handleFlag(boolean flag) {
      float f = 0.0F;
      f -= this.getFloatValue4();
      f -= 2.0F;

      for (ColorElement.Button button : this.copy) {
         f += button.getFloatValue() + 2.0F;
      }

      this.getFloatValue13().setFloatValue(flag ? 85.0F + f : 0.0F);
      this.getFloatValue13().handleFloat3(10.0F, 0.1F);
      this.floatAnimator2.setFloatValue(this.getSetting().isFlag() ? -2.34F : 0.0F);
      this.floatAnimator3.setFloatValue(!this.getSetting().isFlag() ? 10.0F : 15.0F);
      this.animatorGroup.handleFloat(10.0F, 0.15F);
   }

   public FloatAnimator getFloatAnimator6() {
      return this.floatAnimator6;
   }

   @Override
   public void handleDouble4(double d, double d2) {
      if (!this.isFlag10()) {
         this.flag5 = false;
         if (this.getGuiManager().getDragCapture() != null && this.getGuiManager().getDragCapture().equals(this)) {
            this.getGuiManager().setDragCapture(null);
         }
      } else {
         if (this.getGuiGraphics().testInt(0)) {
            if (this.getGuiManager().getDragCapture() == null
               && this.testDouble2(d, d2, this.getFloatValue2(), this.getFloatValue(), this.getFloatValue3(), this.value6())
               && this.isFlag10()) {
               this.getGuiManager().setDragCapture(this);
               this.flag5 = true;
            }
         } else if (this.getGuiManager().getDragCapture() != null && this.getGuiManager().getDragCapture().equals(this)) {
            this.flag5 = false;
            this.getGuiManager().setDragCapture(null);
         } else {
            this.flag5 = false;
         }

         if (this.flag5 && this.isFlag10()) {
            float f2 = this.getFloatValue2() + 1.0F + this.floatAnimator2.getFloatValue2();
            float f3 = this.getFloatValue() + 58.0F;
            float f4 = f2 + 4.0F;
            float f5 = f3 + 1.0F;
            float f6 = f4 + (this.getFloatValue3() - 20.0F);
            float f7 = f5 + (this.getFloatValue4() - 68.0F);
            float f8 = f7 + 7.0F;
            float f9 = f6 + 2.0F;
            float f10 = f9 + 10.0F;
            if (!this.getSetting().isFlag2()) {
               float f = 53.0F;
               if (this.testDouble2(d, d2, f4, f5 - 32.0F, this.getFloatValue3() - 20.0F, f) && !this.flag && !this.flag4 && !this.flag3) {
                  this.flag2 = true;
               }

               if (this.testDouble2(d, d2, f9 - 2.0F, f5 - 32.0F, 8.0, f) && !this.flag2 && !this.flag4 && !this.flag3) {
                  this.flag = true;
               }

               if (this.getSetting().isFlag() && this.testDouble2(d, d2, f10 - 2.0F, f5 - 32.0F, 8.0, f) && !this.flag2 && !this.flag4 && !this.flag) {
                  this.flag3 = true;
               }
            }

            if (this.testDouble2(d, d2, f4 - 2.0F, f8, this.getFloatValue3() - 20.0F + (!this.getSetting().isFlag() ? 8.1F : 15.1F), 8.0)
               && !this.flag2
               && !this.flag
               && !this.flag3) {
               this.flag4 = true;
            }
         } else {
            this.run();
         }
      }
   }

   private static class Button {
      public BiConsumer<ColorElement.Button, Boolean> buttonBiConsumer;
      public float floatValue;
      public final String name;
      public float floatValue2;
      public boolean flag;
      public final EasedAnimation easedAnimation = new EasedAnimation(350L);
      public BiConsumer<ColorElement.Button, Boolean> buttonBiConsumer2;
      public float floatValue3;
      public boolean flag2;
      public float floatValue4;
      public boolean flag3;
      public boolean flag4;

      public Button(
         String text,
         float f,
         float f2,
         float f3,
         float f4,
         boolean flag,
         boolean flag2,
         BiConsumer<ColorElement.Button, Boolean> biConsumer,
         BiConsumer<ColorElement.Button, Boolean> biConsumer2
      ) {
         this.name = text;
         this.floatValue3 = f;
         this.floatValue2 = f2;
         this.floatValue4 = f3;
         this.floatValue = f4;
         this.flag4 = flag;
         this.flag3 = flag2;
         this.buttonBiConsumer2 = biConsumer;
         this.buttonBiConsumer = biConsumer2;
      }

      public void handleGuiGraphics(GuiGraphics guiGraphics, UiTheme uiTheme, String text) {
         guiGraphics.handleText(text, (int)(this.getFloatValue3() + 2.0F), (int)(this.getFloatValue2() + 3.0F), uiTheme.getColor10(), 1.0F, true);
      }

      public EasedAnimation getEasedAnimation() {
         return this.easedAnimation;
      }

      public void setFlag2(boolean flag) {
         this.flag2 = flag;
      }

      public float getFloatValue2() {
         return this.floatValue2;
      }

      public float getFloatValue() {
         return this.floatValue;
      }

      public void setButtonBiConsumer(BiConsumer<ColorElement.Button, Boolean> biConsumer) {
         this.buttonBiConsumer = biConsumer;
      }

      public void handleRunnable(Runnable runnable) {
         if (this.buttonBiConsumer != null) {
            this.flag3 = !this.flag3;
            this.buttonBiConsumer.accept(this, this.flag4);
            runnable.run();
         }
      }

      public void handleGuiGraphics2(GuiGraphics guiGraphics, double d, double d2, double d3, double d4, Color color) {
         guiGraphics.handleDouble12(this.getFloatValue3() + d, this.getFloatValue2() + d2, this.getFloatValue3() + d3, this.getFloatValue2() + d4, color);
      }

      public float getFloatValue3() {
         return this.floatValue3;
      }

      public void setFloatValue2(float f) {
         this.floatValue2 = f;
      }

      public BiConsumer<ColorElement.Button, Boolean> getButtonBiConsumer() {
         return this.buttonBiConsumer;
      }

      public void handleRunnable2(Runnable runnable) {
         this.flag4 = !this.flag4;
         this.buttonBiConsumer2.accept(this, this.flag4);
         runnable.run();
      }

      public boolean isFlag3() {
         return this.flag3;
      }

      public float getFloatValue4() {
         return this.floatValue4;
      }

      public String getName() {
         return this.name;
      }

      public Button(String text, float f, float f2, float f3, float f4, boolean flag, BiConsumer<ColorElement.Button, Boolean> biConsumer) {
         this(text, f, f2, f3, f4, flag, false, biConsumer, null);
      }

      public boolean isFlag2() {
         return this.flag2;
      }

      public void setFlag(boolean flag) {
         this.flag = flag;
      }

      public void setFlag4(boolean flag) {
         this.flag4 = flag;
      }

      public void setButtonBiConsumer2(BiConsumer<ColorElement.Button, Boolean> biConsumer) {
         this.buttonBiConsumer2 = biConsumer;
      }

      public boolean isFlag4() {
         return this.flag4;
      }

      public void handleUiTheme(UiTheme uiTheme, GuiGraphics guiGraphics, double d, double d2) {
         float f = this.easedAnimation.computeEasing(InOutEasing.INSTANCE3);
         if (this.easedAnimation.isFlag() != this.flag4) {
            this.easedAnimation.handleFlag2(this.flag4);
         }

         this.handleGuiGraphics2(
            guiGraphics,
            this.getFloatValue4() * f,
            0.0,
            this.getFloatValue4(),
            this.getFloatValue(),
            ColorUtils.createColor5(uiTheme.getColor12(), this.testDouble(d, d2) ? 15 : 0)
         );
         if (f > 0.05) {
            this.handleGuiGraphics2(guiGraphics, 0.0, 0.0, this.getFloatValue4() * f, this.getFloatValue(), uiTheme.getColor4());
         }

         this.handleGuiGraphics(guiGraphics, uiTheme, this.getName() + (this.flag3 ? "*" : ""));
      }

      public void setFlag3(boolean flag) {
         this.flag3 = flag;
      }

      public void setFloatValue(float f) {
         this.floatValue = f;
      }

      public boolean isFlag() {
         return this.flag;
      }

      public boolean testDouble(double d, double d2) {
         return d >= this.floatValue3 && d <= this.floatValue3 + this.floatValue4 && d2 >= this.floatValue2 && d2 <= this.floatValue2 + this.floatValue;
      }

      public BiConsumer<ColorElement.Button, Boolean> getButtonBiConsumer2() {
         return this.buttonBiConsumer2;
      }

      public void setFloatValue3(float f) {
         this.floatValue3 = f;
      }

      public void setFloatValue4(float f) {
         this.floatValue4 = f;
      }
   }
}
