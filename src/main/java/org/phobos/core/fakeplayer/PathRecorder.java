package org.phobos.core.fakeplayer;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class PathRecorder {
   public int intValue;
   public final List<PathRecorder.Position> positionList = new ArrayList<>();
   public int intValue2;

   public void stop() {
      this.intValue2 = 0;
      this.intValue = 0;
   }

   public void run() {
      this.positionList.clear();
      this.stop();
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, FakePlayer fakePlayer) {
      fakePlayer.setVelocity(Vec3d.ZERO);
      if (this.intValue++ % 2 == 0) {
         this.positionList.add(new PathRecorder.Position(clientPlayerEntity));
      }
   }

   public void handleFakePlayer(FakePlayer fakePlayer) {
      if (!this.positionList.isEmpty()) {
         if (this.intValue2 >= this.positionList.size()) {
            this.intValue2 = 0;
         }

         if (this.intValue++ % 2 == 0) {
            PathRecorder.Position position = this.positionList.get(this.intValue2++);
            fakePlayer.setYaw(position.getFloatValue());
            fakePlayer.setPitch(position.getFloatValue2());
            fakePlayer.setHeadYaw(position.getFloatValue3());
            fakePlayer.updateTrackedPositionAndAngles(
               position.getDoubleValue3(), position.getDoubleValue(), position.getDoubleValue2(), position.getFloatValue(), position.getFloatValue2(), 3
            );
            fakePlayer.setVelocity(position.getVec3d());
         }
      }
   }

   private static final class Position {
      public double doubleValue;
      public float floatValue;
      public float floatValue2;
      public double doubleValue2;
      public double doubleValue3;
      public Vec3d vec3d;
      public float floatValue3;

      public double getDoubleValue2() {
         return this.doubleValue2;
      }

      public double getDoubleValue3() {
         return this.doubleValue3;
      }

      public void setVec3d(Vec3d vec3d) {
         this.vec3d = vec3d;
      }

      public Position(ClientPlayerEntity clientPlayerEntity) {
         this.doubleValue3 = clientPlayerEntity.getX();
         this.doubleValue = clientPlayerEntity.getY();
         this.doubleValue2 = clientPlayerEntity.getZ();
         this.floatValue = clientPlayerEntity.getYaw();
         this.floatValue2 = clientPlayerEntity.getPitch();
         this.floatValue3 = clientPlayerEntity.getHeadYaw();
         this.vec3d = clientPlayerEntity.getVelocity();
      }

      public float getFloatValue() {
         return this.floatValue;
      }

      public float getFloatValue2() {
         return this.floatValue2;
      }

      public void setFloatValue2(float f) {
         this.floatValue2 = f;
      }

      public void setDoubleValue2(double d) {
         this.doubleValue2 = d;
      }

      public float getFloatValue3() {
         return this.floatValue3;
      }

      public Vec3d getVec3d() {
         return this.vec3d;
      }

      public void setDoubleValue3(double d) {
         this.doubleValue3 = d;
      }

      public double getDoubleValue() {
         return this.doubleValue;
      }

      public void setDoubleValue(double d) {
         this.doubleValue = d;
      }

      public void setFloatValue(float f) {
         this.floatValue = f;
      }

      public void setFloatValue3(float f) {
         this.floatValue3 = f;
      }
   }
}
