package org.phobos.core.hud.element;

import net.minecraft.client.gui.DrawContext;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.api.TimeGate;
import org.phobos.core.PhobosClient.InitEvent;
import org.phobos.event.Listener;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.hud.HudElement;
import org.phobos.core.math.MathUtils;
import org.phobos.core.media.MediaTrack;
import org.phobos.core.media.PlaybackListenerImpl;

public class SpotifyHudElement extends HudElement {
   public boolean flag;
   public final FloatAnimator floatAnimator;
   public final FloatAnimator floatAnimator2;
   public final TimeGate.Normal normal;
   public final MutableObject<String> textMutableObject = new MutableObject(null);

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      this.floatAnimator2.handleFloat3(1.0F, 0.02F);
      this.floatAnimator.handleFloat3(1.0F, 0.02F);
      if (this.phobosClient.getMediaPlayerApi().check2()) {
         String text = this.phobosClient.getMediaPlayerApi().getMediaTrack().describe();
         if (this.flag && this.normal.testLong(500L)) {
            this.textMutableObject.setValue(text);
            this.floatAnimator2.setFloatValue(0.0F);
            this.floatAnimator.setFloatValue(1.0F);
            this.flag = false;
         }

         if (this.textMutableObject.getValue() == null || !this.phobosClient.getMediaPlayerApi().isFlag2()) {
            this.textMutableObject.setValue(text);
            this.floatAnimator2.setFloatValue(-20.0F);
            this.floatAnimator.setFloatValue(0.0F);
         }

         if (this.phobosClient.getMediaPlayerApi().isFlag2()
            && this.floatAnimator2.getFloatValue() == -20.0F
            && this.floatAnimator2.getFloatValue2() < -15.0F
            && this.normal.testLong(1000L)) {
            this.floatAnimator2.setFloatValue(0.0F);
            this.floatAnimator.setFloatValue(1.0F);
         }

         String text2 = (String)this.textMutableObject.getValue();
         this.handleDrawContext7(
            drawContext, text2, 0.0F, this.floatAnimator2.getFloatValue2(), MathUtils.computeFloat3(this.floatAnimator.getFloatValue2(), 0.05F, 1.0F)
         );
         this.handleText3(text2);
      }
   }

   public SpotifyHudElement() {
      super("Spotify", 0.0F, 0.0F, 0.0F, 10.0F);
      this.normal = new TimeGate.Normal();
      this.floatAnimator2 = new FloatAnimator();
      this.floatAnimator = new FloatAnimator();
      this.flag = false;
      this.handleListener(new Listener<InitEvent>() {
         public void handleObject2(InitEvent initEvent) {
            initEvent.getPhobosClient().getMediaPlayerApi().handlePlaybackListener2(new PlaybackListenerImpl() {
               @Override
               public void handleMediaTrack(MediaTrack mediaTrack) {
                  SpotifyHudElement.this.floatAnimator2.setFloatValue(-20.0F);
                  SpotifyHudElement.this.floatAnimator.setFloatValue(0.0F);
                  SpotifyHudElement.this.flag = true;
                  SpotifyHudElement.this.normal.run();
               }
            });
         }
      });
   }
}
