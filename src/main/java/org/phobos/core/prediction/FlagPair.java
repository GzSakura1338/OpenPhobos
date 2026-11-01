package org.phobos.core.prediction;

public class FlagPair {
   public final FlagGroup flagGroup;
   public boolean completed;
   public final FlagGroup2 flagGroup2 = new FlagGroup2();

   public FlagPair() {
      this.flagGroup = new FlagGroup();
      this.completed = false;
   }

   public FlagGroup2 getFlagGroup2() {
      return this.flagGroup2;
   }

   public FlagGroup getFlagGroup() {
      return this.flagGroup;
   }

   public boolean isCompleted() {
      return this.completed;
   }

   public void run() {
      this.completed = true;
   }

   public void setCompleted(boolean flag) {
      this.completed = flag;
   }
}
