package org.phobos.event;

import net.minecraft.sound.SoundEvent;
import org.phobos.core.event.events.CancellableEvent;

public class PlaySoundEvent extends CancellableEvent {
   public final double doubleValue;
   public final double doubleValue2;
   public final SoundEvent soundEvent;
   public final double doubleValue3;

   public PlaySoundEvent(double d, double d2, double d3, SoundEvent soundEvent) {
      this.doubleValue2 = d;
      this.doubleValue3 = d2;
      this.doubleValue = d3;
      this.soundEvent = soundEvent;
   }

   public double getDoubleValue() {
      return this.doubleValue;
   }

   public double getDoubleValue2() {
      return this.doubleValue2;
   }

   public double getDoubleValue3() {
      return this.doubleValue3;
   }

   public SoundEvent getSoundEvent() {
      return this.soundEvent;
   }
}
