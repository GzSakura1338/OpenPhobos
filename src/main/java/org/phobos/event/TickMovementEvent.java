package org.phobos.event;

public class TickMovementEvent {
   public boolean flag;

   public TickMovementEvent(boolean flag) {
      this.flag = flag;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public boolean isFlag() {
      return this.flag;
   }
}
