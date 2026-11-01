package org.phobos.type;

public enum NoneCameraEnum {
   None(false, false),
   Camera(true, false),
   Position(false, true),
   Full(true, true);

   public final boolean camera;
   public final boolean position;

   NoneCameraEnum(final boolean camera, final boolean position) {
      this.camera = camera;
      this.position = position;
   }

   public boolean isPosition() {
      return this.position;
   }

   public boolean isCamera() {
      return this.camera;
   }
}
