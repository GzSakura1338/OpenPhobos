package org.phobos.event;

import net.minecraft.particle.ParticleType;
import org.phobos.core.event.events.CancellableEvent;

public class SpawnEvent extends CancellableEvent {
   public final ParticleType<?> particleType;

   public ParticleType<?> getParticleType() {
      return this.particleType;
   }

   public SpawnEvent(ParticleType<?> particleType) {
      this.particleType = particleType;
   }
}
