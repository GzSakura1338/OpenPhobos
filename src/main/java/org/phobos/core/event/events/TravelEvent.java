package org.phobos.core.event.events;

import net.minecraft.util.math.Vec3d;

public abstract class TravelEvent extends CancellableEvent {
   public final Vec3d vec3d;

   public TravelEvent(Vec3d vec3d) {
      this.vec3d = vec3d;
   }

   public Vec3d getVec3d() {
      return this.vec3d;
   }

   public abstract boolean alwaysTrue();

   public static final class Post extends TravelEvent {
      public Post(Vec3d vec3d) {
         super(vec3d);
      }

      @Override
      public boolean alwaysTrue() {
         return true;
      }
   }

   public static final class Pre extends TravelEvent {
      public Pre(Vec3d vec3d) {
         super(vec3d);
      }

      @Override
      public boolean alwaysTrue() {
         return false;
      }
   }
}
