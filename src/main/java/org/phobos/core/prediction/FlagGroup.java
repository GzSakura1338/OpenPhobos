package org.phobos.core.prediction;

public class FlagGroup {
   public boolean flag;
   public boolean flag2;
   public boolean flag3;
   public boolean flag4;

   public boolean isFlag4() {
      return this.flag4;
   }

   public boolean isFlag2() {
      return this.flag2;
   }

   public void setFlag3(boolean flag) {
      this.flag3 = flag;
   }

   public void setFlag4(boolean flag) {
      this.flag4 = flag;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public void handleDouble(double d) {
      if (d > 0.5) {
         this.setFlag3(true);
      } else {
         if (d < -0.5) {
            this.setFlag2(true);
         }
      }
   }

   public void setFlag2(boolean flag) {
      this.flag2 = flag;
   }

   public void handleDouble2(double d) {
      if (d > 0.5) {
         this.setFlag(true);
      } else {
         if (d < -0.5) {
            this.setFlag4(true);
         }
      }
   }

   public void handleFlagGroup(FlagGroup flagGroup) {
      flagGroup.setFlag3(this.flag3);
      flagGroup.setFlag2(this.flag2);
      flagGroup.setFlag4(this.flag4);
      flagGroup.setFlag(this.flag);
   }

   public boolean isFlag3() {
      return this.flag3;
   }

   public boolean isFlag() {
      return this.flag;
   }
}
