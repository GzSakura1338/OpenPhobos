package org.phobos.event;

import org.joml.Vector3f;
import org.phobos.core.event.events.CancellableEvent;
import org.phobos.module.render.AmbienceModule;

public class RenderSkyEvent extends CancellableEvent {
   public AmbienceModule.SkyMode skyMode;
   public Vector3f vector3f;
   public boolean flag;

   public void setSkyMode(AmbienceModule.SkyMode skyMode) {
      this.skyMode = skyMode;
   }

   public void setVector3f(Vector3f vector3f) {
      this.vector3f = vector3f;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public Vector3f getVector3f() {
      return this.vector3f;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public AmbienceModule.SkyMode getSkyMode() {
      return this.skyMode;
   }
}
