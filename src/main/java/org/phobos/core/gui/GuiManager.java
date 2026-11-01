package org.phobos.core.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.api.MouseMoveListener;
import org.phobos.api.DragCapture;
import org.phobos.api.GuiGraphics;
import org.phobos.api.MinecraftHolder;
import org.phobos.module.client.UIModule;
import org.phobos.core.animation.FloatAnimator;

public class GuiManager implements MinecraftHolder, MouseMoveListener {
   public final GuiGraphics guiGraphics;
   public final List<GuiPanel> guiPanelList;
   public final FloatAnimator floatAnimator;
   public final ClipStack clipStack;
   public String text;
   public int intValue;
   public double[] doubleArray;
   public final MutableObject<Object> objectMutableObject;
   public final List<GuiPanel> guiPanelList2 = new CopyOnWriteArrayList<>();
   public final UiTheme uiTheme;
   public DragCapture dragCapture;
   public final DrawStats drawStats;

   public int getIntValue() {
      return this.intValue;
   }

   public void setText3(String text) {
      this.text = text;
   }

   public ClipStack getClipStack() {
      return this.clipStack.getClipStack();
   }

   public void setDragCapture(DragCapture dragCapture) {
      this.dragCapture = dragCapture;
   }

   public void run3() {
      this.handleClipBounds(this.clipStack.getClipBounds());
      this.intValue--;
      this.run();
   }

   public void handleObject(Object object) {
      if (this.getObjectMutableObject().getValue() != null && this.getObjectMutableObject().getValue().equals(object)) {
         this.getObjectMutableObject().setValue(null);
      }
   }

   public String getText() {
      return this.text;
   }

   public double[] values() {
      return new double[]{0.0, 0.0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight()};
   }

   public void handleDouble(double d, double d2, double d3, double d4) {
      this.handleClipBounds(this.clipStack.resolveClipBounds(new ClipBounds(d, d2, d3, d4)));
      this.intValue++;
      this.run();
   }

   public List<GuiPanel> getGuiPanelList2() {
      return this.guiPanelList;
   }

   public UiTheme getUiTheme() {
      return this.uiTheme;
   }

   public GuiManager(GuiGraphics guiGraphics) {
      this.guiPanelList = new CopyOnWriteArrayList<>();
      this.drawStats = new DrawStats();
      this.clipStack = new ClipStack();
      this.intValue = 0;
      this.uiTheme = new UiTheme();
      this.objectMutableObject = new MutableObject();
      this.floatAnimator = new FloatAnimator();
      this.guiGraphics = guiGraphics;
      this.dragCapture = null;
   }

   public void run() {
      if (this.clipStack.isEmpty()) {
         this.doubleArray = this.values();
      } else {
         this.doubleArray = this.clipStack.values();
      }
   }

   public void clear() {
      this.guiPanelList2.clear();
   }

   public void handleText(String text, Object object) {
      boolean flag = !Objects.equals(this.text, text);
      this.setText3(text);
      this.getObjectMutableObject().setValue(object);
      if (flag) {
         this.floatAnimator.setFloatValue2(0.0F);
      }
   }

   public void handleClipBounds(ClipBounds clipBounds) {
      if (clipBounds == null) {
         RenderSystem.disableScissor();
      } else {
         double[] doubles = clipBounds.values();
         RenderSystem.enableScissor((int)doubles[0], (int)doubles[1], Math.max(0, (int)doubles[2]), Math.max(0, (int)doubles[3]));
      }
   }

   public boolean testGuiElement(GuiElement guiElement) {
      return this.doubleArray.length == 0
         ? true
         : !(guiElement.getFloatValue2() + guiElement.getFloatValue3() < this.doubleArray[0])
            && !(guiElement.getFloatValue2() > this.doubleArray[2])
            && !(
               guiElement.getFloatValue() + (guiElement instanceof InteractiveElement interactiveElement ? interactiveElement.getFloatValue33() : guiElement.getFloatValue4())
                  < this.doubleArray[1]
            )
            && !(guiElement.getFloatValue() > this.doubleArray[3]);
   }

   public GuiPanel resolveGuiPanel(String text) {
      GuiPanel guiPanel = new GuiPanel(this, text);
      this.guiPanelList2.add(guiPanel);
      return guiPanel;
   }

   public void handleGuiPanel(GuiPanel guiPanel) {
      this.guiPanelList.remove(guiPanel);
      guiPanel.close();
   }

   public List<GuiPanel> getGuiPanelList() {
      return this.guiPanelList2;
   }

   public float value() {
      float f = 0.0F;

      for (GuiPanel guiPanel : this.guiPanelList2) {
         f += guiPanel.getFloatValue3() + 10.0F;
      }

      return f;
   }

   @Override
   public void handleDouble2(double d, double d2) {
      this.clipStack.clear();
      this.drawStats.run3();
      this.doubleArray = this.values();
      float f5 = mc.getWindow().getScaledWidth() / 2.0F - this.value() / 2.0F + 5.0F;
      boolean flag = false;

      for (GuiPanel guiPanel : this.guiPanelList) {
         guiPanel.handleDouble2(d, d2);
         this.drawStats.run5();
         this.drawStats.run();
         if (guiPanel.testDouble3(d, d2)) {
            flag = true;
         }
      }

      float f6 = 0.1F * (UIModule.getUIModule().scroll.getValue() / 100.0F);

      for (GuiPanel guiPanel2 : this.guiPanelList2) {
         if (UIModule.getUIModule().layout.getValue()) {
            guiPanel2.handleFloat2(f5, 30.0F);
         }

         if (!guiPanel2.isFlag5()) {
            CursorUtils.Style style = guiPanel2.getStyle();
            if (style != CursorUtils.Style.Default) {
               HoverRegion hoverRegion = guiPanel2.getHoverRegion();
               if (hoverRegion.testDouble(d, d2)) {
                  guiPanel2.handleStyle(style);
               }
            }
         }

         guiPanel2.getFloatAnimator9().handleFloat3(3.5F, f6);
         guiPanel2.handleDouble7(d, d2, true);
         this.drawStats.run2();
         guiPanel2.handleDouble2(d, d2);
         this.drawStats.run5();
         this.drawStats.run();
         f5 += guiPanel2.getFloatValue3() + 10.0F;
      }

      this.floatAnimator.setFloatValue(UIModule.getUIModule().descriptions.getValue() && this.getObjectMutableObject().getValue() != null ? 1.0F : 0.0F);
      this.floatAnimator.handleFloat3(10.0F, 0.2F);
      if (this.floatAnimator.getFloatValue2() >= 0.05F && this.text != null && !this.text.isEmpty()) {
         float f = mc.getWindow().getScaledWidth() / 2.0F;
         float f2 = (float)(this.guiGraphics.computeText5(this.text) / 1.3F);
         float f3 = f - f2 / 2.0F;
         float f4 = 4.0F - (1.0F - this.floatAnimator.getFloatValue2()) * 16.0F;
         this.guiGraphics.handleText(this.text, f3, f4, Color.WHITE, 1.0F, true);
      }

      if (flag) {
         this.drawStats.handleGuiGraphics(this.getGuiGraphics());
      } else {
         for (int i = this.guiPanelList2.size() - 1; i >= 0; i--) {
            GuiPanel guiPanel3 = this.guiPanelList2.get(i);
            guiPanel3.handleDouble4(d, d2);
            this.drawStats.run2();
         }

         this.drawStats.handleGuiGraphics(this.getGuiGraphics());
      }
   }

   public void handleGuiPanel2(GuiPanel guiPanel) {
      this.guiPanelList.add(guiPanel);
      guiPanel.run5();
   }

   public DragCapture getDragCapture() {
      return this.dragCapture;
   }

   public MutableObject<Object> getObjectMutableObject() {
      return this.objectMutableObject;
   }

   public DrawStats getDrawStats() {
      return this.drawStats;
   }

   public GuiGraphics getGuiGraphics() {
      return this.guiGraphics;
   }

   public enum UIShader {
      None,
      Blur,
      Fluid,
      Colorful,
      Plasma;
   }
}
