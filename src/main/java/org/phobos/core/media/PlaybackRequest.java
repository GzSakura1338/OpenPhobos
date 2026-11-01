package org.phobos.core.media;

public class PlaybackRequest {
   public boolean flag;
   public long longValue;

   public boolean isFlag() {
      return this.flag;
   }

   public long getLongValue() {
      return this.longValue;
   }

   public void setLongValue(long l) {
      this.longValue = l;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public PlaybackRequest(long l, boolean flag) {
      this.longValue = l;
      this.flag = flag;
   }

   public static class Builder {
      public long longValue = 10000L;
      public boolean flag = true;

      public PlaybackRequest createPlaybackRequest() {
         return new PlaybackRequest(this.longValue, this.flag);
      }

      public PlaybackRequest.Builder resolveBuilder(boolean flag) {
         this.flag = flag;
         return this;
      }

      public PlaybackRequest.Builder resolveBuilder2(long l) {
         this.longValue = l;
         return this;
      }
   }
}
