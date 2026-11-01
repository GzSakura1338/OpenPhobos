package org.phobos.core.hud;

import org.phobos.type.TopBottomEnum;

public class AnchorOffset {
   public double doubleValue;
   public double doubleValue2;
   public TopBottomEnum topBottomEnum;
   public TopBottomEnum topBottomEnum2;

   public void handleTopBottomEnum(TopBottomEnum topBottomEnum, double d) {
      switch (topBottomEnum.axis) {
         case Horizontal:
            if (this.topBottomEnum2 != null && d >= this.doubleValue2) {
               return;
            }

            this.topBottomEnum2 = topBottomEnum;
            this.doubleValue2 = d;
            break;
         case Vertical:
            if (this.topBottomEnum != null && d >= this.doubleValue) {
               return;
            }

            this.topBottomEnum = topBottomEnum;
            this.doubleValue = d;
      }
   }

   public TopBottomEnum[] values() {
      if (this.topBottomEnum2 == null && this.topBottomEnum == null) {
         return new TopBottomEnum[0];
      } else if (this.topBottomEnum2 != null && this.topBottomEnum != null) {
         return new TopBottomEnum[]{this.topBottomEnum2, this.topBottomEnum};
      } else {
         return this.topBottomEnum2 == null ? new TopBottomEnum[]{this.topBottomEnum} : new TopBottomEnum[]{this.topBottomEnum2};
      }
   }
}
