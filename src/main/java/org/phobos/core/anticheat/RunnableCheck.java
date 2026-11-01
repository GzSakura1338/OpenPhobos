package org.phobos.core.anticheat;

public abstract class RunnableCheck extends AntiCheatCheck implements Runnable {
   public RunnableCheck(String text, AntiCheatPlayer antiCheatPlayer) {
      super(text, antiCheatPlayer);
   }
}
