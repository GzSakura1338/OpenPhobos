package org.phobos.core.prediction;

public class FlagGroup2 {
   public boolean flag;
   public boolean flag2;
   public boolean flag3;
   public boolean flag4;

   public void setFlag3(boolean flag) {
      this.flag3 = flag;
   }

   public void setFlag2(boolean flag) {
      this.flag2 = flag;
   }

   public void setFlag4(boolean flag) {
      this.flag4 = flag;
   }

   public boolean isFlag3() {
      return this.flag3;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public boolean isFlag2() {
      return this.flag2;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public void handleFlag(boolean flag, boolean flag2, boolean flag3, boolean flag4) {
      this.setFlag4(flag);
      this.setFlag2(flag2);
      this.setFlag(flag3);
      this.setFlag3(flag4);
   }

   public boolean isFlag4() {
      return this.flag4;
   }
}
