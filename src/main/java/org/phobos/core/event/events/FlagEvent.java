package org.phobos.core.event.events;

public class FlagEvent extends CancellableEvent {
   public boolean flag;

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public boolean isFlag() {
      return this.flag;
   }
}
