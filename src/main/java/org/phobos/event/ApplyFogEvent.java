package org.phobos.event;

import net.minecraft.client.render.BackgroundRenderer.FogType;
import org.phobos.core.event.events.CancellableEvent;

public final class ApplyFogEvent extends CancellableEvent {
   public final boolean flag;
   public final float floatValue;
   public final FogType fogType;
   public final float floatValue2;

   public boolean isFlag() {
      return this.flag;
   }

   public float getFloatValue22() {
      return this.floatValue2;
   }

   public FogType getFogType() {
      return this.fogType;
   }

   public ApplyFogEvent(FogType fogType, float f, float f2, boolean flag) {
      this.fogType = fogType;
      this.floatValue2 = f;
      this.floatValue = f2;
      this.flag = flag;
   }

   public float getFloatValue2() {
      return this.floatValue;
   }
}
