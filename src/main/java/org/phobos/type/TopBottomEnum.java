package org.phobos.type;

import net.minecraft.util.math.MathHelper;
import org.phobos.api.Bounds;
import org.phobos.core.gui.screen.HudScreen;
import org.phobos.core.hud.HudElement;
import org.phobos.core.util.GridConstants;

public enum TopBottomEnum {
   Top(false, "top", HorizontalVerticalEnum.Vertical) {
      @Override
      public void clip(HudScreen manager, HudElement dragging, Bounds attachment) {
         if (attachment.check5() && dragging.check5()) {
            dragging.setFloatValue(attachment.getFloatValue() + attachment.getFloatValue4() - attachment.value3());
            manager.handleHudElement3(dragging, dragging.getFloatValue());
         } else if (attachment.check5()) {
            dragging.setFloatValue(attachment.getFloatValue() + attachment.getFloatValue4() - attachment.value3() - dragging.getFloatValue4());
            manager.handleHudElement3(dragging, dragging.getFloatValue() + dragging.getFloatValue4());
         } else if (dragging.check5()) {
            dragging.setFloatValue(attachment.getFloatValue() - attachment.value3());
            manager.handleHudElement3(dragging, dragging.getFloatValue());
         } else {
            dragging.setFloatValue(attachment.getFloatValue() - attachment.value3() - dragging.getFloatValue4());
            manager.handleHudElement3(dragging, dragging.getFloatValue() + dragging.getFloatValue4());
         }
      }

      @Override
      public void retain(Bounds parent, HudElement attachment) {
         if (parent.check5() && attachment.check5()) {
            attachment.handleDouble3(parent.getFloatValue() + parent.getFloatValue4() - parent.value3());
         } else if (parent.check5()) {
            attachment.handleDouble3(parent.getFloatValue() + parent.getFloatValue4() - parent.value3() - attachment.getFloatValue4());
         } else if (attachment.check5()) {
            attachment.handleDouble3(parent.getFloatValue() - parent.value3());
         } else {
            attachment.handleDouble3(parent.getFloatValue() - parent.value3() - attachment.getFloatValue4());
         }
      }

      @Override
      public double connects(HudElement dragging, Bounds target) {
         if (dragging.check5() && target.check5()) {
            return this.connect(dragging.getFloatValue(), target.getFloatValue() + target.getFloatValue4(), GridConstants.value());
         } else if (dragging.check5()) {
            return this.connect(dragging.getFloatValue(), target.getFloatValue(), GridConstants.value());
         } else {
            return target.check5()
               ? this.connect(dragging.getFloatValue() + dragging.getFloatValue4(), target.getFloatValue() + target.getFloatValue4(), GridConstants.value())
               : this.connect(dragging.getFloatValue() + dragging.getFloatValue4(), target.getFloatValue(), GridConstants.value());
         }
      }
   },
   Bottom(false, "bottom", HorizontalVerticalEnum.Vertical) {
      @Override
      public double connects(HudElement dragging, Bounds target) {
         if (dragging.check5() && target.check5()) {
            return this.connect(dragging.getFloatValue() + dragging.getFloatValue4(), target.getFloatValue(), GridConstants.value());
         } else if (dragging.check5()) {
            return this.connect(dragging.getFloatValue() + dragging.getFloatValue4(), target.getFloatValue() + target.getFloatValue4(), GridConstants.value());
         } else {
            return target.check5()
               ? this.connect(dragging.getFloatValue(), target.getFloatValue(), GridConstants.value())
               : this.connect(dragging.getFloatValue(), target.getFloatValue() + target.getFloatValue4(), GridConstants.value());
         }
      }

      @Override
      public void clip(HudScreen manager, HudElement dragging, Bounds attachment) {
         if (attachment.check5() && dragging.check5()) {
            dragging.setFloatValue(attachment.getFloatValue() + 2.0F - dragging.getFloatValue4());
            manager.handleHudElement3(dragging, dragging.getFloatValue() + dragging.getFloatValue4());
         } else if (attachment.check5()) {
            dragging.setFloatValue(attachment.getFloatValue() + 2.0F);
            manager.handleHudElement3(dragging, dragging.getFloatValue());
         } else if (dragging.check5()) {
            dragging.setFloatValue(attachment.getFloatValue() + attachment.getFloatValue4() + 2.0F - dragging.getFloatValue4());
            manager.handleHudElement3(dragging, dragging.getFloatValue() + dragging.getFloatValue4());
         } else {
            dragging.setFloatValue(attachment.getFloatValue() + attachment.getFloatValue4() + 2.0F);
            manager.handleHudElement3(dragging, dragging.getFloatValue());
         }
      }

      @Override
      public void retain(Bounds parent, HudElement attachment) {
         if (parent.check5() && attachment.check5()) {
            attachment.handleDouble3(parent.getFloatValue() + parent.value3() - attachment.getFloatValue4());
         } else if (parent.check5()) {
            attachment.handleDouble3(parent.getFloatValue() + parent.value3());
         } else if (attachment.check5()) {
            attachment.handleDouble3(parent.getFloatValue() + parent.getFloatValue4() + parent.value3() - attachment.getFloatValue4());
         } else {
            attachment.handleDouble3(parent.getFloatValue() + parent.getFloatValue4() + parent.value3());
         }
      }
   },
   Left(true, "left", HorizontalVerticalEnum.Horizontal) {
      @Override
      public double connects(HudElement dragging, Bounds target) {
         if (dragging.check6() && target.check6()) {
            return this.connect(dragging.getFloatValue2(), target.getFloatValue2() + target.getFloatValue3(), GridConstants.value2());
         } else if (dragging.check6()) {
            return this.connect(dragging.getFloatValue2(), target.getFloatValue2(), GridConstants.value2());
         } else {
            return target.check6()
               ? this.connect(dragging.getFloatValue2() + dragging.getFloatValue3(), target.getFloatValue2() + target.getFloatValue3(), GridConstants.value2())
               : this.connect(dragging.getFloatValue2() + dragging.getFloatValue3(), target.getFloatValue2(), GridConstants.value2());
         }
      }

      @Override
      public void retain(Bounds parent, HudElement attachment) {
         if (parent.check6() && attachment.check6()) {
            attachment.handleDouble4(parent.getFloatValue2() + parent.getFloatValue3() - parent.value2());
         } else if (parent.check6()) {
            attachment.handleDouble4(parent.getFloatValue2() + parent.getFloatValue3() - parent.value2() - attachment.getFloatValue3());
         } else if (attachment.check6()) {
            attachment.handleDouble4(parent.getFloatValue2() - parent.value2());
         } else {
            attachment.handleDouble4(parent.getFloatValue2() - parent.value2() - attachment.getFloatValue3());
         }
      }

      @Override
      public void clip(HudScreen manager, HudElement dragging, Bounds attachment) {
         if (dragging.check6() && attachment.check6()) {
            dragging.setFloatValue2(attachment.getFloatValue2() + attachment.getFloatValue3() - 4.0F);
            manager.handleHudElement(dragging, dragging.getFloatValue2());
         } else if (dragging.check6()) {
            dragging.setFloatValue2(attachment.getFloatValue2() - 4.0F);
            manager.handleHudElement(dragging, dragging.getFloatValue2());
         } else if (attachment.check6()) {
            dragging.setFloatValue2(attachment.getFloatValue2() + attachment.getFloatValue3() - 4.0F - dragging.getFloatValue3());
            manager.handleHudElement(dragging, dragging.getFloatValue2() + dragging.getFloatValue3());
         } else {
            dragging.setFloatValue2(attachment.getFloatValue2() - 4.0F - dragging.getFloatValue3());
            manager.handleHudElement(dragging, dragging.getFloatValue2() + dragging.getFloatValue3());
         }
      }
   },
   Right(true, "right", HorizontalVerticalEnum.Horizontal) {
      @Override
      public void retain(Bounds parent, HudElement attachment) {
         if (parent.check6() && attachment.check6()) {
            attachment.handleDouble4(parent.getFloatValue2() + parent.value2() - attachment.getFloatValue3());
         } else if (parent.check6()) {
            attachment.handleDouble4(parent.getFloatValue2() + parent.value2());
         } else if (attachment.check6()) {
            attachment.handleDouble4(parent.getFloatValue2() + parent.getFloatValue3() + parent.value2() - attachment.getFloatValue3());
         } else {
            attachment.handleDouble4(parent.getFloatValue2() + parent.getFloatValue3() + parent.value2());
         }
      }

      @Override
      public double connects(HudElement dragging, Bounds target) {
         if (dragging.check6() && target.check6()) {
            return this.connect(dragging.getFloatValue2() + dragging.getFloatValue3(), target.getFloatValue2(), GridConstants.value2());
         } else if (dragging.check6()) {
            return this.connect(dragging.getFloatValue2() + dragging.getFloatValue3(), target.getFloatValue2() + target.getFloatValue3(), GridConstants.value2());
         } else {
            return target.check6()
               ? this.connect(dragging.getFloatValue2(), target.getFloatValue2(), GridConstants.value2())
               : this.connect(dragging.getFloatValue2(), target.getFloatValue2() + target.getFloatValue3(), GridConstants.value2());
         }
      }

      @Override
      public void clip(HudScreen manager, HudElement dragging, Bounds attachment) {
         if (dragging.check6() && attachment.check6()) {
            dragging.setFloatValue2(attachment.getFloatValue2() + 4.0F - dragging.getFloatValue3());
            manager.handleHudElement(dragging, dragging.getFloatValue2() + dragging.getFloatValue3());
         } else if (dragging.check6()) {
            dragging.setFloatValue2(attachment.getFloatValue2() + attachment.getFloatValue3() + 4.0F - dragging.getFloatValue3());
            manager.handleHudElement(dragging, dragging.getFloatValue2() + dragging.getFloatValue3());
         } else if (attachment.check6()) {
            dragging.setFloatValue2(attachment.getFloatValue2() + 4.0F);
            manager.handleHudElement(dragging, dragging.getFloatValue2());
         } else {
            dragging.setFloatValue2(attachment.getFloatValue2() + attachment.getFloatValue3() + 4.0F);
            manager.handleHudElement(dragging, dragging.getFloatValue2());
         }
      }
   },
   InsideRight(true, "insideRight", HorizontalVerticalEnum.Horizontal) {
      @Override
      public void clip(HudScreen manager, HudElement dragging, Bounds attachment) {
         if (dragging.check6() && attachment.check6()) {
            dragging.setFloatValue2(attachment.getFloatValue2());
            manager.handleHudElement(dragging, dragging.getFloatValue2());
         } else if (dragging.check6()) {
            dragging.setFloatValue2(attachment.getFloatValue2() + attachment.getFloatValue3());
            manager.handleHudElement(dragging, dragging.getFloatValue2());
         } else if (attachment.check6()) {
            dragging.setFloatValue2(attachment.getFloatValue2() - dragging.getFloatValue3());
            manager.handleHudElement(dragging, dragging.getFloatValue2() + dragging.getFloatValue3());
         } else {
            dragging.setFloatValue2(attachment.getFloatValue2() + attachment.getFloatValue3() - dragging.getFloatValue3());
            manager.handleHudElement(dragging, dragging.getFloatValue2() + dragging.getFloatValue3());
         }
      }

      @Override
      public void retain(Bounds parent, HudElement attachment) {
         if (parent.check6() && attachment.check6()) {
            attachment.handleDouble4(parent.getFloatValue2());
         } else if (parent.check6()) {
            attachment.handleDouble4(parent.getFloatValue2() - attachment.getFloatValue3());
         } else if (attachment.check6()) {
            attachment.handleDouble4(parent.getFloatValue2() + parent.getFloatValue3());
         } else {
            attachment.handleDouble4(parent.getFloatValue2() + parent.getFloatValue3() - attachment.getFloatValue3());
         }
      }

      @Override
      public double connects(HudElement dragging, Bounds target) {
         if (dragging.check6() && target.check6()) {
            return this.connect(dragging.getFloatValue2(), target.getFloatValue2(), GridConstants.value2());
         } else if (dragging.check6()) {
            return this.connect(dragging.getFloatValue2(), target.getFloatValue2() + target.getFloatValue3(), GridConstants.value2());
         } else {
            return target.check6()
               ? this.connect(dragging.getFloatValue2() + dragging.getFloatValue3(), target.getFloatValue2(), GridConstants.value2())
               : this.connect(dragging.getFloatValue2() + dragging.getFloatValue3(), target.getFloatValue2() + target.getFloatValue3(), GridConstants.value2());
         }
      }
   },
   InsideLeft(true, "insideLeft", HorizontalVerticalEnum.Horizontal) {
      @Override
      public double connects(HudElement dragging, Bounds target) {
         if (dragging.check6() && target.check6()) {
            return this.connect(dragging.getFloatValue2() + dragging.getFloatValue3(), target.getFloatValue2() + target.getFloatValue3(), GridConstants.value2());
         } else if (dragging.check6()) {
            return this.connect(dragging.getFloatValue2() + dragging.getFloatValue3(), target.getFloatValue2(), GridConstants.value2());
         } else {
            return target.check6()
               ? this.connect(dragging.getFloatValue2(), target.getFloatValue2() + target.getFloatValue3(), GridConstants.value2())
               : this.connect(dragging.getFloatValue2(), target.getFloatValue2(), GridConstants.value2());
         }
      }

      @Override
      public void retain(Bounds parent, HudElement attachment) {
         if (parent.check6() && attachment.check6()) {
            attachment.handleDouble4(parent.getFloatValue2() + parent.getFloatValue3() - attachment.getFloatValue3());
         } else if (parent.check6()) {
            attachment.handleDouble4(parent.getFloatValue2() + parent.getFloatValue3());
         } else if (attachment.check6()) {
            attachment.handleDouble4(parent.getFloatValue2() - attachment.getFloatValue3());
         } else {
            attachment.handleDouble4(parent.getFloatValue2());
         }
      }

      @Override
      public void clip(HudScreen manager, HudElement dragging, Bounds attachment) {
         if (dragging.check6() && attachment.check6()) {
            dragging.setFloatValue2(attachment.getFloatValue2() + attachment.getFloatValue3() - dragging.getFloatValue3());
            manager.handleHudElement(dragging, dragging.getFloatValue2() + dragging.getFloatValue3());
         } else if (dragging.check6()) {
            dragging.setFloatValue2(attachment.getFloatValue2() - dragging.getFloatValue3());
            manager.handleHudElement(dragging, dragging.getFloatValue2() + dragging.getFloatValue3());
         } else if (attachment.check6()) {
            dragging.setFloatValue2(attachment.getFloatValue2() + attachment.getFloatValue3());
            manager.handleHudElement(dragging, dragging.getFloatValue2());
         } else {
            dragging.setFloatValue2(attachment.getFloatValue2());
            manager.handleHudElement(dragging, dragging.getFloatValue2());
         }
      }
   },
   InsideTop(true, "insideTop", HorizontalVerticalEnum.Vertical) {
      @Override
      public double connects(HudElement dragging, Bounds target) {
         if (dragging.check5() && target.check5()) {
            return this.connect(dragging.getFloatValue() + dragging.getFloatValue4(), target.getFloatValue() + target.getFloatValue4(), GridConstants.value());
         } else if (dragging.check5()) {
            return this.connect(dragging.getFloatValue() + dragging.getFloatValue4(), target.getFloatValue(), GridConstants.value());
         } else {
            return target.check5()
               ? this.connect(dragging.getFloatValue(), target.getFloatValue() + target.getFloatValue4(), GridConstants.value())
               : this.connect(dragging.getFloatValue(), target.getFloatValue(), GridConstants.value());
         }
      }

      @Override
      public void retain(Bounds parent, HudElement attachment) {
         if (parent.check5() && attachment.check5()) {
            attachment.handleDouble3(parent.getFloatValue() + parent.getFloatValue4() - attachment.getFloatValue4());
         } else if (parent.check5()) {
            attachment.handleDouble3(parent.getFloatValue() + parent.getFloatValue4());
         } else if (attachment.check5()) {
            attachment.handleDouble3(parent.getFloatValue() - attachment.getFloatValue4());
         } else {
            attachment.handleDouble3(parent.getFloatValue());
         }
      }

      @Override
      public void clip(HudScreen manager, HudElement dragging, Bounds attachment) {
         if (dragging.check5() && attachment.check5()) {
            dragging.setFloatValue(attachment.getFloatValue() + attachment.getFloatValue4() - dragging.getFloatValue4());
            manager.handleHudElement3(dragging, dragging.getFloatValue() + dragging.getFloatValue4());
         } else if (dragging.check5()) {
            dragging.setFloatValue(attachment.getFloatValue() - dragging.getFloatValue4());
            manager.handleHudElement3(dragging, dragging.getFloatValue() + dragging.getFloatValue4());
         } else if (attachment.check5()) {
            dragging.setFloatValue(attachment.getFloatValue() + attachment.getFloatValue4());
            manager.handleHudElement3(dragging, dragging.getFloatValue());
         } else {
            dragging.setFloatValue(attachment.getFloatValue());
            manager.handleHudElement3(dragging, dragging.getFloatValue());
         }
      }
   },
   InsideBottom(true, "insideBottom", HorizontalVerticalEnum.Vertical) {
      @Override
      public double connects(HudElement dragging, Bounds target) {
         if (dragging.check5() && target.check5()) {
            return this.connect(dragging.getFloatValue(), target.getFloatValue(), GridConstants.value());
         } else if (dragging.check5()) {
            return this.connect(dragging.getFloatValue(), target.getFloatValue() + target.getFloatValue4(), GridConstants.value());
         } else {
            return target.check5()
               ? this.connect(dragging.getFloatValue() + dragging.getFloatValue4(), target.getFloatValue(), GridConstants.value())
               : this.connect(dragging.getFloatValue() + dragging.getFloatValue4(), target.getFloatValue() + target.getFloatValue4(), GridConstants.value());
         }
      }

      @Override
      public void clip(HudScreen manager, HudElement dragging, Bounds attachment) {
         if (dragging.check5() && attachment.check5()) {
            dragging.setFloatValue(attachment.getFloatValue());
            manager.handleHudElement3(dragging, dragging.getFloatValue());
         } else if (dragging.check5()) {
            dragging.setFloatValue(attachment.getFloatValue() + attachment.getFloatValue4());
            manager.handleHudElement3(dragging, dragging.getFloatValue());
         } else if (attachment.check5()) {
            dragging.setFloatValue(attachment.getFloatValue() - dragging.getFloatValue4());
            manager.handleHudElement3(dragging, dragging.getFloatValue() + dragging.getFloatValue4());
         } else {
            dragging.setFloatValue(attachment.getFloatValue() + attachment.getFloatValue4() - dragging.getFloatValue4());
            manager.handleHudElement3(dragging, dragging.getFloatValue() + dragging.getFloatValue4());
         }
      }

      @Override
      public void retain(Bounds parent, HudElement attachment) {
         if (parent.check5() && attachment.check5()) {
            attachment.handleDouble3(parent.getFloatValue());
         } else if (parent.check5()) {
            attachment.handleDouble3(parent.getFloatValue() + parent.getFloatValue4());
         } else if (attachment.check5()) {
            attachment.handleDouble3(parent.getFloatValue() + parent.getFloatValue4());
         } else {
            attachment.handleDouble3(parent.getFloatValue() + parent.getFloatValue4() - attachment.getFloatValue4());
         }
      }
   },
   YCenter(false, "centerY", HorizontalVerticalEnum.Vertical) {
      @Override
      public void clip(HudScreen manager, HudElement dragging, Bounds attachment) {
         float centerY = MathHelper.lerp(0.5F, attachment.getFloatValue(), attachment.getFloatValue() + attachment.getFloatValue4());
         float half = Math.abs(dragging.getFloatValue4()) / 2.0F;
         float display = Math.abs(attachment.getFloatValue4()) / 2.0F;
         dragging.setFloatValue(centerY - half);
         manager.handleHudElement3(dragging, centerY - display);
         manager.handleHudElement3(dragging, centerY + display);
      }

      @Override
      public double connects(HudElement dragging, Bounds target) {
         return this.connect(
            MathHelper.lerp(0.5F, dragging.getFloatValue(), dragging.getFloatValue() + dragging.getFloatValue4()),
            MathHelper.lerp(0.5F, target.getFloatValue(), target.getFloatValue() + target.getFloatValue4()),
            GridConstants.value()
         );
      }

      @Override
      public void retain(Bounds parent, HudElement attachment) {
         float centerY = MathHelper.lerp(0.5F, parent.getFloatValue(), parent.getFloatValue() + parent.getFloatValue4());
         float height = Math.abs(attachment.getFloatValue4());
         attachment.handleDouble3(centerY - height / 2.0F);
      }
   },
   XCenter(false, "centerX", HorizontalVerticalEnum.Horizontal) {
      @Override
      public void clip(HudScreen manager, HudElement dragging, Bounds attachment) {
         float centerX = MathHelper.lerp(0.5F, attachment.getFloatValue2(), attachment.getFloatValue2() + attachment.getFloatValue3());
         float half = Math.abs(dragging.getFloatValue3()) / 2.0F;
         float display = Math.abs(attachment.getFloatValue3()) / 2.0F;
         dragging.setFloatValue2(!dragging.check6() ? centerX - half : centerX + half);
         manager.handleHudElement(dragging, centerX - display);
         manager.handleHudElement(dragging, centerX + display);
      }

      @Override
      public double connects(HudElement dragging, Bounds target) {
         return this.connect(
            MathHelper.lerp(0.5F, dragging.getFloatValue2(), dragging.getFloatValue2() + dragging.getFloatValue3()),
            MathHelper.lerp(0.5F, target.getFloatValue2(), target.getFloatValue2() + target.getFloatValue3()),
            GridConstants.value2()
         );
      }

      @Override
      public void retain(Bounds parent, HudElement attachment) {
         float centerX = MathHelper.lerp(0.5F, parent.getFloatValue2(), parent.getFloatValue2() + parent.getFloatValue3());
         float half = Math.abs(attachment.getFloatValue3()) / 2.0F;
         if (parent.check6() && attachment.check6()) {
            attachment.handleDouble4(centerX + half);
         } else if ((parent.check6() || attachment.check6()) && !parent.check6()) {
            attachment.handleDouble4(centerX + half);
         } else {
            attachment.handleDouble4(centerX - half);
         }
      }
   };

   public final HorizontalVerticalEnum axis;
   public final boolean side;
   public final String name;

   public void clip(HudScreen manager, HudElement dragging, Bounds attachment) {
   }

   public double connects(HudElement dragging, Bounds target) {
      return Double.MAX_VALUE;
   }

   TopBottomEnum(boolean side, String name, HorizontalVerticalEnum axis) {
      this.side = side;
      this.name = name;
      this.axis = axis;
   }

   public void retain(Bounds parent, HudElement attachment) {
   }

   @Override
   public String toString() {
      return this.name;
   }

   public double connect(double pos1, double pos2, int proximity) {
      double distance = Math.abs(pos1 - pos2);
      return distance < proximity ? distance : Double.MAX_VALUE;
   }

   public static TopBottomEnum fromString(String string) {
      return switch (string) {
         case "top" -> Top;
         case "bottom" -> Bottom;
         case "centerX" -> XCenter;
         case "center", "centerY" -> YCenter;
         case "left" -> Left;
         case "right" -> Right;
         case "insideRight" -> InsideRight;
         case "insideLeft" -> InsideLeft;
         case "insideTop" -> InsideTop;
         case "insideBottom" -> InsideBottom;
         default -> null;
      };
   }
}
