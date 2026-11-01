package org.phobos.core.player;

public class ScaffoldFlags {
   public boolean flag;
   public boolean flag2;

   public boolean isFlag2() {
      return this.flag2;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public void setFlag2(boolean flag) {
      this.flag2 = flag;
   }

   public ScaffoldFlags(boolean flag, boolean flag2) {
      this.flag = flag;
      this.flag2 = flag2;
   }
}
