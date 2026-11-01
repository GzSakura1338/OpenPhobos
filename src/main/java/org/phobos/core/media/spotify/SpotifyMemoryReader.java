package org.phobos.core.media.spotify;

import org.phobos.api.PlaybackState;
import org.phobos.api.PsapiLibrary;
import org.phobos.core.ClientHolder;
import org.phobos.core.media.UnknownTrack;

public class SpotifyMemoryReader extends ProcessMemoryScanner {
   public static final boolean flag = ClientHolder.flag;
   public final PlaybackState playbackState;
   public final long longValue2;
   public static final String text = "spotify:track:";
   public UnknownTrack unknownTrack = UnknownTrack.INSTANCE;
   public static final long[] longArray = new long[]{1395296L, 1374768L, 1073560L, 1362416L, 1057144L, 1350128L, 1044456L};

   public SpotifyMemoryReader() {
      super("Spotify.exe");
      if (flag) {
         ClientHolder.getPhobosClient().handleText5("Integration", "Spotify process loaded! Searching for addresses...");
      }

      long l = System.currentTimeMillis();
      this.longValue2 = this.value3();
      this.playbackState = this.getPlaybackState2();
      if (flag) {
         ClientHolder.getPhobosClient().handleText5("Integration", "Scanning took " + (System.currentTimeMillis() - l) + "ms");
      }
   }

   public PlaybackState getPlaybackState() {
      return this.playbackState;
   }

   public String describeLong(long l) {
      return this.createText(l + 14L, 22);
   }

   public boolean check2() {
      return this.describe().contains(" - ");
   }

   public UnknownTrack getUnknownTrack() {
      UnknownTrack unknownTrack = UnknownTrack.createUnknownTrack(this.describe());
      return unknownTrack == null ? this.unknownTrack : (this.unknownTrack = unknownTrack);
   }

   public String describe2() {
      return this.describeLong(this.longValue2);
   }

   public long value3() {
      PsapiLibrary.ModuleInfo moduleInfo = this.resolveModuleInfo("chrome_elf.dll");
      if (moduleInfo == null) {
         throw new IllegalStateException("Could not find chrome_elf.dll module");
      }

      long l6 = moduleInfo.value2();
      long l7 = -1L;
      long l8 = Long.MAX_VALUE;
      long l9 = Long.MIN_VALUE;

      for (long l2 : longArray) {
         l8 = Math.min(l8, l2);
         l9 = Math.max(l9, l2);
         long l = l6 + l2;
         if (this.testText(this.describeLong(l))) {
            l7 = l;
            break;
         }
      }

      if (l7 == -1L) {
         if (flag) {
            ClientHolder.getPhobosClient().handleText5("Integration", "Could not find track id with hardcoded offsets. Trying to find it dynamically...");
         }

         long l3 = (l9 - l8) * 3L;
         long l4 = l6 + l8 - l3;
         long l5 = l6 + l9 + l3;
         l7 = this.computeLong4(l4, l5, "spotify:track:", (lx, i) -> this.testText(this.describeLong(lx)));
      }

      if (l7 == -1L) {
         throw new IllegalStateException("Could not find track id in memory");
      }

      if (flag) {
         ClientHolder.getPhobosClient()
            .handleText5(
               "Integration",
               "Found track id address: %s (+%s) [%s%s]%n".formatted(Long.toHexString(l7), Long.toHexString(l7 - l6), "spotify:track:", this.describeLong(l7))
            );
      }

      return l7;
   }

   public PlaybackState getPlaybackState2() {
      long l = this.computeLong4(
         0L,
         268435455L,
         "playlist",
         (lx, i) -> this.testLong2(lx + 408L, "context", "autoplay")
            && this.testLong2(lx + 128L, "your_library", "home")
            && new ProcessPlaybackState(this, lx).alwaysTrue()
      );
      if (l == -1L) {
         l = this.computeLong4(
            0L,
            268435455L,
            "album",
            (lx, i) -> this.testLong2(lx + 408L, "context", "autoplay")
               && this.testLong2(lx + 128L, "your_library", "home")
               && new ProcessPlaybackState(this, lx).alwaysTrue()
         );
      }

      if (l == -1L) {
         if (flag) {
            ClientHolder.getPhobosClient().handleText5("Integration", "Could not find playback address in memory");
         }

         return new MemoryPlaybackState(this);
      } else {
         ProcessPlaybackState processPlaybackState = new ProcessPlaybackState(this, l);
         if (!processPlaybackState.alwaysTrue()) {
            return new MemoryPlaybackState(this);
         }

         if (flag) {
            ClientHolder.getPhobosClient().handleText5("Integration", "Found playback address at: " + Long.toHexString(l));
         }

         return processPlaybackState;
      }
   }

   public long getLongValue2() {
      return this.longValue2;
   }

   public boolean testText(String text) {
      for (char c : text.toCharArray()) {
         boolean flag = c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9';
         if (!flag) {
            return false;
         }
      }

      return true;
   }
}
