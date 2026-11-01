package org.phobos.core.media.mac;

public class AppleScriptMusic extends AppleScriptRunner {
   public static final AppleScriptCommand id = new AppleScriptCommand("id");
   public static final AppleScriptCommand appleScriptCommand = new AppleScriptCommand("previous", "track");
   public static final AppleScriptCommand duration = new AppleScriptCommand("duration");
   public static final AppleScriptCommand appleScriptCommand2 = new AppleScriptCommand("next", "track");
   public static final AppleScriptCommand playpause = new AppleScriptCommand("playpause");
   public static final AppleScriptCommand appleScriptCommand3 = new AppleScriptCommand("player", "position");
   public static final AppleScriptCommand name = new AppleScriptCommand("name");
   public static final AppleScriptCommand appleScriptCommand4 = new AppleScriptCommand("player", "state");
   public static final AppleScriptCommand appleScriptCommand5 = new AppleScriptCommand("current", "track");
   public static final AppleScriptCommand artist = new AppleScriptCommand("artist");

   public int value() throws Exception {
      return Integer.parseInt(this.describeAppleScriptCommand(duration, appleScriptCommand5));
   }

   public int value2() throws Exception {
      return (int)(Double.parseDouble(this.describeAppleScriptCommand2(appleScriptCommand3)) * 1000.0);
   }

   public AppleScriptMusic() {
      super("Spotify");
   }

   public boolean check() throws Exception {
      return this.describeAppleScriptCommand2(appleScriptCommand4).equals("playing");
   }

   public String describe() throws Exception {
      return this.describeAppleScriptCommand(artist, appleScriptCommand5);
   }

   public String describe2() throws Exception {
      return this.describeAppleScriptCommand(name, appleScriptCommand5);
   }

   public void run() throws Exception {
      this.describeWHFofauYLzMy2FRcxWpMvM3TQ7SVAO4F(appleScriptCommand2);
   }

   public void run2() throws Exception {
      this.describeWHFofauYLzMy2FRcxWpMvM3TQ7SVAO4F(appleScriptCommand);
   }

   public void run3() throws Exception {
      this.describeWHFofauYLzMy2FRcxWpMvM3TQ7SVAO4F(playpause);
   }

   public String describe3() throws Exception {
      return this.describeAppleScriptCommand(id, appleScriptCommand5).substring(14);
   }
}
