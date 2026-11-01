package org.phobos.core.render;

public class FontState {
   public boolean flag;
   public boolean flag2;
   public static FontState fontState = new FontState();

   public static FontState getFontState() {
      return fontState;
   }

   public boolean isFlag2() {
      return this.flag2;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public void setFlag2(boolean flag) {
      this.flag2 = flag;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }
}
