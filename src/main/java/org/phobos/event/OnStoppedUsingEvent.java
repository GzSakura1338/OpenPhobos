package org.phobos.event;

import org.phobos.core.event.events.CancellableEvent;

public class OnStoppedUsingEvent extends CancellableEvent {
   public int intValue;
   public boolean flag;
   public boolean flag2;

   public int getIntValue() {
      return this.intValue;
   }

   public void setFlag2(boolean flag) {
      this.flag2 = flag;
   }

   public OnStoppedUsingEvent(int i, boolean flag, boolean flag2) {
      this.intValue = i;
      this.flag = flag;
      this.flag2 = flag2;
   }

   public boolean isFlag2() {
      return this.flag2;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public void setIntValue(int i) {
      this.intValue = i;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }
}
