package org.phobos.core.gui.element;

import java.awt.Color;
import org.phobos.api.DragCapture;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.setting.CurvePoints;
import org.phobos.core.setting.GraphSetting;
import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.math.MathUtils;
import org.phobos.core.gui.ModuleElement;

public class GraphElement extends SettingValueElement<CurvePoints, GraphSetting> {
   public boolean flag;
   public final GraphElement.Point[] pointArray = new GraphElement.Point[4];

   @Override
   public void handleDouble4(double d, double d2) {
      boolean flag = this.isFlag7();
      if (this.getGuiGraphics().testInt(1)) {
         this.flag = flag;
      } else {
         if (this.flag && flag) {
            this.setFlag10(!this.isFlag10());
         }

         this.flag = false;
      }

      if (this.isFlag10()) {
         if (this.getGuiGraphics().testInt(0)) {
            if (this.getGuiManager().getDragCapture() == null) {
               for (GraphElement.Point point : this.pointArray) {
                  if (point.testDouble(d, d2)) {
                     point.setFlag(true);
                     this.getGuiManager().setDragCapture(point);
                     break;
                  }
               }
            }
         } else if (this.isFlag72()) {
            this.run();
         } else {
            this.run2();
         }

         for (int i = 0; i < 4; i++) {
            GraphElement.Point point2 = this.pointArray[i];
            if (point2.isFlag()) {
               float f = this.getFloatValue2() + 2.0F;
               float f2 = this.getFloatValue() + this.getFloatValue4() + 2.0F;
               float f3 = this.getFloatValue2() + this.getFloatValue3() - 4.0F;
               float f4 = this.getFloatValue() + this.getFloatValue4() + this.getFloatValue3() - 4.0F;
               float f5 = MathUtils.computeFloat3((float)d, f, f3);
               float f6 = MathUtils.computeFloat3((float)d2, f2, f4);
               point2.setFloatValue2(f5);
               point2.setFloatValue(f6);
               float f7 = (f3 - f5) / this.getFloatValue3();
               float f8 = (f4 - f6) / this.getFloatValue3();
               this.handleInt(i, i != 0 && i != 3 ? f8 : f7);
            }
         }
      }
   }

   public GraphElement(
      GuiManager guiManager,
      GuiPanel guiPanel,
      float f,
      float f2,
      float f3,
      float f4,
      ModuleElement moduleElement,
      String text,
      GraphSetting graphSetting
   ) {
      super(guiManager, guiPanel, f, f2, f3, f4, moduleElement, text, graphSetting);

      for (int i = 0; i < 4; i++) {
         this.pointArray[i] = this.createValue(guiManager, guiPanel, i);
      }
   }

   public boolean isFlag72() {
      if (this.getGuiManager().getDragCapture() == null) {
         return false;
      }

      for (GraphElement.Point point : this.pointArray) {
         if (this.getGuiManager().getDragCapture().equals(point)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void handleFlag(boolean flag) {
      this.getFloatValue13().setFloatValue(flag ? 60.0F : 0.0F);
      this.getFloatValue13().handleFloat3(10.0F, 0.2F);
   }

   public void handleInt(int i, double d) {
      switch (i) {
         case 0:
            this.getValue().setDoubleValue4(d);
            break;
         case 1:
            this.getValue().setDoubleValue3(d);
         case 2:
         default:
            break;
         case 3:
            this.getValue().setDoubleValue2(d);
            break;
         case 4:
            this.getValue().setDoubleValue(d);
      }
   }

   @Override
   public void handleDouble2(double d, double d2) {
      this.handleFlag(this.isFlag10());
      this.handleDouble12(2.0, 2.0, this.getFloatValue3() - 2.0F, this.getFloatValue4() + 58.0F, ColorUtils.createColor6(Color.WHITE, 0.4F));

      for (GraphElement.Point point : this.pointArray) {
         point.handleDouble2(d, d2);
      }
   }

   public void run2() {
      for (GraphElement.Point point : this.pointArray) {
         point.setFlag(false);
      }
   }

   public GraphElement.Point createValue(GuiManager guiManager, GuiPanel guiPanel, int i) {
      return new GraphElement.Point(guiManager, guiPanel, this.floatValue12, this.floatValue10, 2.0F, 2.0F, null) {
         @Override
         public void handleDouble2(double d, double d2) {
            this.handleDouble12(0.0, 0.0, this.getFloatValue3(), this.getFloatValue4(), Color.WHITE);
         }

         @Override
         public void handleDouble4(double d, double d2) {
         }
      };
   }

   public void run() {
      this.run2();
      this.getGuiManager().setDragCapture(null);
   }

   public double computeInt(int i) {
      return switch (i) {
         case 0 -> this.getValue().getDoubleValue4();
         case 1 -> this.getValue().getDoubleValue3();
         case 2 -> this.getValue().getDoubleValue2();
         case 3 -> this.getValue().getDoubleValue();
         default -> throw new RuntimeException("Invalid index");
      };
   }

   private abstract static class Point extends GuiElement implements DragCapture {
      public boolean flag;

      public Point(GuiManager guiManager, GuiPanel guiPanel, float f, float f2, float f3, float f4, ModuleElement moduleElement) {
         super(guiManager, guiPanel, f, f2, f3, f4, moduleElement);
      }

      public void setFlag(boolean flag) {
         this.flag = flag;
      }

      public boolean isFlag() {
         return this.flag;
      }
   }
}
