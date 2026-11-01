package org.phobos.core.media.linux;

import java.util.HashMap;
import java.util.Map;

public class MprisSpotifyClient {
   public static final DbusArgument dest = new DbusArgument("dest", "org.mpris.MediaPlayer2.spotify");
   public static final DbusInterface dbusInterface = new DbusInterface("org.mpris.MediaPlayer2.Player.Next");
   public final DbusSend dbusSend = new DbusSend(new DbusArgument[]{dest}, "/org/mpris/MediaPlayer2");
   public final Map<String, Object> textMap = new HashMap<>();
   public static final DbusInterface dbusInterface2 = new DbusInterface("org.mpris.MediaPlayer2.Player.Previous");
   public static final DbusInterface dbusInterface3 = new DbusInterface("org.mpris.MediaPlayer2.Player.PlayPause");

   public String describe() throws Exception {
      this.run3();
      return String.join(", ", (String[])this.textMap.get("xesam:artist"));
   }

   public Integer getInteger() throws Exception {
      this.run3();
      return (int)((Long)this.textMap.get("mpris:length") / 1000L) + 1;
   }

   public String describe2() throws Exception {
      this.run3();
      return this.textMap.get("xesam:title").toString();
   }

   public boolean check() throws Exception {
      return this.dbusSend.resolveDbusVariant2("org.mpris.MediaPlayer2.Player", "PlaybackStatus").getValue().equals("Playing");
   }

   public void run() throws Exception {
      this.dbusSend.resolveDbusVariant(dbusInterface3);
   }

   public void run2() throws Exception {
      this.dbusSend.resolveDbusVariant(dbusInterface);
   }

   public void run3() throws Exception {
      this.textMap.clear();
      DbusVariant dbusVariant2 = this.dbusSend.resolveDbusVariant2("org.mpris.MediaPlayer2.Player", "Metadata");

      for (DbusVariant dbusVariant : (DbusVariant[])dbusVariant2.getValue()) {
         this.textMap.put(dbusVariant.getText(), dbusVariant.getValue());
      }
   }

   public void run4() throws Exception {
      this.dbusSend.resolveDbusVariant(dbusInterface2);
   }

   public String describe3() throws Exception {
      this.run3();
      return ((String)this.textMap.get("mpris:trackid")).split("/")[4];
   }

   public Integer getInteger2() throws Exception {
      return (int)(this.dbusSend.resolveDbusVariant2("org.mpris.MediaPlayer2.Player", "Position").<Long>getValue() / 1000L);
   }
}
