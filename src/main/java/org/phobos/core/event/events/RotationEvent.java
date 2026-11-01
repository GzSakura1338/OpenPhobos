package org.phobos.core.event.events;

import org.phobos.core.rotation.RotationManager;

public class RotationEvent extends CancellableEvent {
   public final RotationManager.Rotation rotation;

   public RotationManager.Rotation getRotation() {
      return this.rotation;
   }

   public RotationEvent(RotationManager.Rotation rotation) {
      this.rotation = rotation;
   }
}
