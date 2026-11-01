package org.phobos.event;

import net.minecraft.client.render.Camera;

public class PreRenderEvent {
   public final Camera camera;

   public Camera getCamera() {
      return this.camera;
   }

   public PreRenderEvent(Camera camera) {
      this.camera = camera;
   }
}
