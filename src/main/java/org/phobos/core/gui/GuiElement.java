package org.phobos.core.gui;

import java.awt.Color;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.nanovg.NanoVG;
import org.phobos.api.MouseMoveListener;
import org.phobos.api.ScrollHandler;
import org.phobos.api.GuiGraphics;
import org.phobos.api.MinecraftHolder;
import org.phobos.module.client.FontModule;
import org.phobos.module.client.UIModule;
import org.phobos.core.ClientHolder;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.PhobosSounds;
import org.phobos.core.render.RenderUtils;

public abstract class GuiElement implements MinecraftHolder, MouseMoveListener, ScrollHandler {
   public float floatValue5;
   public final GuiPanel guiPanel;
   public float floatValue6;
   public float floatValue7;
   public boolean flag6;
   public float floatValue8;
   public final FloatAnimator floatAnimator8;
   public final GuiGraphics guiGraphics;
   public float floatValue9;
   public boolean flag7;
   public float floatValue10;
   public float floatValue11;
   public boolean flag8 = true;
   public final GuiElement guiElement;
   public final GuiManager guiManager;
   public Predicate<GuiElement> guiElementPredicate;
   public float floatValue12;
   public boolean flag9 = false;

   public void handleDouble7(double d, double d2, boolean flag) {
      this.setFlag7(this.testDouble(d, d2) && flag);
      this.floatAnimator8.handleFloat3(10.0F, 0.04F);
      this.flag6 = false;
      this.run8();
   }

   public void run8() {
      this.setFloatValue3(this.getFloatValue5() * UIModule.getUIModule().width.getValue());
      this.setFloatValue4(this.getFloatValue9() * UIModule.getUIModule().height.getValue());
   }

   public void handleChar2(char c, int i) {
   }

   public float getFloatValue12() {
      return this.floatValue12;
   }

   public void handleInt2(int i, int i2, int i3) {
   }

   public void handleDouble8(double d, double d2, int i) {
   }

   public float getFloatValue2() {
      return this.floatValue12 + this.floatValue7;
   }

   public void run10() {
      this.setFlag9(true);
   }

   public float getFloatValue() {
      return this.floatValue10 + this.floatValue8;
   }

   public Color resolveColor(Color color) {
      return ColorUtils.createColor6(color, MathHelper.clamp(color.getAlpha() / 255.0F, 0.0F, this.floatAnimator8.getFloatValue2()));
   }

   public boolean check2() {
      return this.floatAnimator8.getFloatValue2() <= 0.3F;
   }

   public void handleDouble3(double d, double d2, int i) {
   }

   public void run11() {
      if (!this.flag6) {
         this.floatAnimator8.setFloatValue2(0.0F);
         this.flag6 = true;
      }
   }

   public void close() {
      this.getFloatAnimator8().setFloatValue(0.0F);
   }

   public float value3() {
      return this.flag9 ? 0.0F : this.getFloatValue2();
   }

   public float value4() {
      return this.flag9 ? 0.0F : this.getFloatValue();
   }

   public float getFloatValue10() {
      return this.floatValue10;
   }

   public void run5() {
      this.floatAnimator8.setFloatValue2(0.0F);
      this.floatAnimator8.setFloatValue(1.0F);
      this.run8();
   }

   public GuiElement(GuiManager guiManager, GuiPanel guiPanel, float f, float f2, float f3, float f4, ModuleElement moduleElement) {
      this.floatAnimator8 = new FloatAnimator();
      this.guiGraphics = guiManager.getGuiGraphics();
      this.guiPanel = guiPanel;
      this.guiElement = moduleElement;
      this.floatValue5 = f3;
      this.floatValue9 = f4;
      this.guiManager = guiManager;
      this.floatValue12 = f;
      this.floatValue10 = f2;
      this.floatValue6 = f3;
      this.floatValue11 = f4;
      if (moduleElement != null) {
         moduleElement.handleGuiElement(this);
      }
   }

   public void run12() {
      this.setFlag8(this.getGuiElementPredicate() == null || this.getGuiElementPredicate().test(this));
   }

   public void handleText(String text, float f, float f2, Color color, float f3, boolean flag, boolean... flags) {
      f2 += UIModule.getUIModule().offset.getValue();
      color = Objects.equals(color, Color.WHITE) ? this.getGuiManager().getUiTheme().getColor10() : color;
      this.guiGraphics.handleText(text, this.value3() + f, this.value4() + f2, this.resolveColor(color), f3, flag);
      if (FontModule.getFontModule().check7()) {
         if (flags.length == 0) {
            if (this.isFlag7()) {
               ClientHolder.getPhobosClient()
                  .getRenderManager()
                  .handleText3(text, this.value3() + f, this.value4() + f2, this.resolveColor(color), f3, true, 10.0F);
            }
         } else {
            for (boolean flag2 : flags) {
               if (flag2) {
                  ClientHolder.getPhobosClient()
                     .getRenderManager()
                     .handleText3(text, this.value3() + f, this.value4() + f2, this.resolveColor(color), f3, true, 10.0F);
                  break;
               }
            }
         }
      }
   }

   public void handleText2(String text, float f, float f2, Color color, boolean flag, boolean... flags) {
      color = Objects.equals(color, Color.WHITE) ? this.getGuiManager().getUiTheme().getColor10() : color;
      this.handleText4(text, f, f2, this.resolveColor(color), 1.0F, flag, flags);
   }

   public double computeText4(String text, float f) {
      return this.guiGraphics.computeText5(text) * f;
   }

   public void handleDouble9(double d, double d2, double d3, double d4, Color color, GuiManager.UIShader uIShader) {
      color = this.resolveColor(color);
      switch (uIShader) {
         case Blur:
            RenderUtils.blurShader.handleFloat((float)d, (float)d2, (float)d3, (float)d4, 1.0F, color, 10.0F, 1.0F - color.getAlpha() / 255.0F);
            RenderUtils.blurShader.run3();
            break;
         case Colorful:
            RenderUtils.menuButtonShader.handleColor(color);
            RenderUtils.menuButtonShader.run3();
            break;
         case Fluid:
            RenderUtils.colorfulThemeShader.handleFloat(color.getAlpha() / 255.0F);
            RenderUtils.colorfulThemeShader.run3();
            break;
         case None:
            RenderUtils.plasmaThemeShader.handleColor(color);
            RenderUtils.plasmaThemeShader.run3();
      }
   }

   public boolean testDouble(double d, double d2) {
      return d >= this.floatValue12 && d2 >= this.floatValue10 && d <= this.floatValue12 + this.floatValue6 && d2 <= this.floatValue10 + this.floatValue11;
   }

   public void run4() {
      ClientHolder.getPhobosClient().getRenderManager().run4();
   }

   public void handleText3(String text, Consumer<GuiPanel> consumer) {
      GuiPanel guiPanel = new GuiPanel(this.guiManager, text) {
         @Override
         public boolean testDouble3(double d, double d2) {
            if (this.getGuiGraphics().testInt(0)
               && !this.testDouble2(d, d2, this.getFloatValue2(), this.getFloatValue(), this.getFloatValue3(), this.getFloatValue6())) {
               GuiElement.this.guiManager.handleGuiPanel(this);
               return false;
            } else {
               super.handleDouble4(d, d2);
               return true;
            }
         }
      };
      this.guiManager.handleGuiPanel2(guiPanel);
      consumer.accept(guiPanel);
   }

   public void handleText4(String text, float f, float f2, Color color, float f3, boolean flag, boolean... flags) {
      f2 += UIModule.getUIModule().offset.getValue();
      color = Objects.equals(color, Color.WHITE) ? this.getGuiManager().getUiTheme().getColor10() : color;
      this.guiGraphics.handleText(text, this.value3() + f, this.value4() + f2, this.resolveColor(color), f3, flag);
      if (FontModule.getFontModule().check7() && UIModule.getUIModule().glow.getValue()) {
         if (flags.length == 0) {
            if (this.isFlag7()) {
               ClientHolder.getPhobosClient()
                  .getRenderManager()
                  .handleText3(text, this.value3() + f, this.value4() + f2, this.resolveColor(color), f3, true, 10.0F);
            }
         } else {
            for (boolean flag2 : flags) {
               if (flag2) {
                  ClientHolder.getPhobosClient()
                     .getRenderManager()
                     .handleText3(text, this.value3() + f, this.value4() + f2, this.resolveColor(color), f3, true, 10.0F);
                  break;
               }
            }
         }
      }
   }

   public MatrixStack getMatrixStack() {
      return this.guiGraphics.getDrawContext().getMatrices();
   }

   public void handleText5(String text, float f, float f2, Color color, boolean flag, boolean... flags) {
      color = Objects.equals(color, Color.WHITE) ? this.getGuiManager().getUiTheme().getColor10() : color;
      this.handleText(text, f, f2, this.resolveColor(color), 1.0F, flag, flags);
   }

   public double computeText5(String text) {
      return this.guiGraphics.computeText5(text);
   }

   public double computeGuiElement(GuiElement guiElement) {
      return guiElement.getFloatValue3();
   }

   public void handleDouble10(double d, double d2, double d3, double d4, Color color) {
      this.guiGraphics.handleDouble10(d, d2, d3, d4, this.resolveColor(color));
   }

   public boolean testDouble2(double d, double d2, double d3, double d4, double d5, double d6) {
      return d >= d3 && d <= d3 + d5 && d2 >= d4 && d2 <= d4 + d6;
   }

   public double computeText(String text) {
      return this.guiGraphics.computeText(text);
   }

   public boolean testDouble3(double d, double d2) {
      return false;
   }

   public void run13() {
      this.setFlag9(false);
   }

   public void handleDouble11(double d, double d2, double d3, double d4, Color color, GuiManager.UIShader uIShader) {
      color = this.resolveColor(color);
      Color color2 = color;
      switch (uIShader) {
         case Blur:
         case Colorful:
         case Fluid:
         case None:
            RenderUtils.handleMatrixStack12(
               this.getMatrixStack(),
               color,
               this.value3() + d,
               this.value4() + d2,
               this.value3() + d + d3,
               this.value4() + d2 + d4,
               0.0F,
               () -> {
                  switch (uIShader) {
                     case Blur:
                        RenderUtils.blurShader
                           .handleFloat(
                              (float)(this.value3() + d),
                              (float)(this.value4() + d2),
                              (float)d3,
                              (float)d4,
                              1.0F,
                              color2,
                              10.0F,
                              1.0F - color2.getAlpha() / 255.0F
                           );
                        RenderUtils.blurShader.run3();
                        break;
                     case Colorful:
                        RenderUtils.menuButtonShader.handleColor(color2);
                        RenderUtils.menuButtonShader.run3();
                        break;
                     case Fluid:
                        RenderUtils.colorfulThemeShader.handleFloat(color2.getAlpha() / 255.0F);
                        RenderUtils.colorfulThemeShader.run3();
                        break;
                     case None:
                        RenderUtils.plasmaThemeShader.handleColor(color2);
                        RenderUtils.plasmaThemeShader.run3();
                  }
               }
            );
            break;
         case Plasma:
            this.handleDouble12(d, d2, d3, d4, color);
      }
   }

   public void start() {
      ClientHolder.getPhobosClient().getRenderManager().start();
   }

   public void handleDouble12(double d, double d2, double d3, double d4, Color color) {
      this.guiGraphics.handleDouble12(this.value3() + d, this.value4() + d2, this.value3() + d3, this.value4() + d4, this.resolveColor(color));
   }

   public float getFloatValue5() {
      return this.floatValue5;
   }

   public float getFloatValue3() {
      return this.floatValue6;
   }

   public GuiPanel getGuiPanel() {
      return this.guiPanel;
   }

   public int value5() {
      return this.guiPanel == null
         ? -1
         : Objects.hash(this.guiPanel.value5(), this.floatValue12, this.floatValue10, this.floatValue6, this.floatValue11);
   }

   public void run3() {
      int i = this.guiManager.getIntValue();
      if (i < 0) {
         throw new IllegalStateException("unclip called with scissor depth 0");
      }

      try {
         long l = this.value();
         NanoVG.nvgRestore(l);
      } catch (Throwable var4) {
      }

      this.guiManager.run3();
   }

   public void run14() {
      if (UIModule.getUIModule().sounds.getValue()) {
         PhobosSounds phobosSounds = ClientHolder.getPhobosClient().getPhobosSounds();
         phobosSounds.handleSoundEvent(phobosSounds.soundEvent8, UIModule.getUIModule().soundsVolume.getValue());
      }
   }

   public GuiGraphics getGuiGraphics() {
      return this.guiGraphics;
   }

   public void handleStyle(CursorUtils.Style style) {
      this.guiGraphics.setFlag(true);
      CursorUtils.handleStyle(style);
   }

   public ClipStack getClipStack() {
      return this.guiManager.getClipStack();
   }

   public boolean isFlag5() {
      return this.guiGraphics.isFlag5();
   }

   public GuiElement getGuiElement() {
      return this.guiElement;
   }

   public void handleDouble(double d, double d2, double d3, double d4) {
      int i = this.guiManager.getIntValue();
      if (i > 25) {
         throw new IllegalStateException("clip called with scissor depth over 25: " + i);
      }

      this.getGuiManager().getDrawStats().run4();

      try {
         long l = this.value();
         NanoVG.nvgSave(l);
         NanoVG.nvgIntersectScissor(l, (float)(this.value3() + d), (float)(this.value4() + d2), (float)d3, (float)d4);
      } catch (Throwable var12) {
      }

      this.guiManager.handleDouble(this.value3() + d, this.value4() + d2, d3, d4);
   }

   public float getFloatValue9() {
      return this.floatValue9;
   }

   public GuiManager getGuiManager() {
      return this.guiManager;
   }

   public float getFloatValue7() {
      return this.floatValue7;
   }

   public long value() {
      return ClientHolder.getPhobosClient().getRenderManager().value3();
   }

   public float getFloatValue4() {
      return this.floatValue11;
   }

   public CursorUtils.Style getStyle() {
      return CursorUtils.Style.Default;
   }

   public void run15() {
      if (UIModule.getUIModule().sounds.getValue()) {
         PhobosSounds phobosSounds = ClientHolder.getPhobosClient().getPhobosSounds();
         phobosSounds.handleSoundEvent(phobosSounds.soundEvent12, UIModule.getUIModule().soundsVolume.getValue());
      }
   }

   public HoverRegion getHoverRegion() {
      return HoverRegion.createHoverRegion(this.floatValue12, this.floatValue10, this.floatValue6, this.floatValue11);
   }

   public void setFlag7(boolean flag) {
      this.flag7 = flag;
   }

   public boolean isFlag8() {
      return this.flag8;
   }

   public boolean isFlag7() {
      return this.flag7;
   }

   public void setFlag8(boolean flag) {
      this.flag8 = flag;
   }

   public void setFloatValue8(float f) {
      this.floatValue8 = f;
   }

   public boolean isFlag6() {
      return this.flag6;
   }

   public void setFloatValue3(float f) {
      this.floatValue6 = f;
   }

   public FloatAnimator getFloatAnimator8() {
      return this.floatAnimator8;
   }

   public Predicate<GuiElement> getGuiElementPredicate() {
      return this.guiElementPredicate;
   }

   public void setFloatValue2(float f) {
      this.floatValue12 = f;
   }

   public void setFloatValue5(float f) {
      this.floatValue5 = f;
   }

   public void setFloatValue7(float f) {
      this.floatValue7 = f;
   }

   public void setFloatValue9(float f) {
      this.floatValue9 = f;
   }

   public boolean isFlag9() {
      return this.flag9;
   }

   public void setGuiElementPredicate(Predicate<GuiElement> predicate) {
      this.guiElementPredicate = predicate;
   }

   public void setFloatValue4(float f) {
      this.floatValue11 = f;
   }

   public float getFloatValue8() {
      return this.floatValue8;
   }

   public void setFloatValue(float f) {
      this.floatValue10 = f;
   }

   public void setFlag9(boolean flag) {
      this.flag9 = flag;
   }
}
