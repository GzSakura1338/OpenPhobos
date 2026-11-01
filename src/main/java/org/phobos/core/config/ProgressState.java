package org.phobos.core.config;

public class ProgressState {
   public volatile int intValue;
   public volatile boolean flag;
   public volatile boolean flag2 = false;
   public volatile int size;

   public void handleInt(int i) {
      this.flag2 = true;
      this.intValue = 0;
      this.flag = true;
      this.size = i;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public boolean check() {
      if (!this.flag2) {
         return false;
      }

      this.flag2 = false;
      return true;
   }

   public boolean isFlag2() {
      return this.flag2;
   }

   public void setSize(int i) {
      this.size = i;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public void setIntValue(int i) {
      this.intValue = i;
   }

   public float value() {
      return this.size == 0 ? 0.0F : (float)this.intValue / this.size;
   }

   public void run() {
      this.intValue++;
   }

   public void setFlag2(boolean flag) {
      this.flag2 = flag;
   }

   public void stop() {
      this.intValue = this.size;
      this.flag = false;
   }

   public int getSize() {
      return this.size;
   }

   public boolean isFlag() {
      return this.flag;
   }
}
