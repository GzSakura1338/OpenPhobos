package org.phobos.core.entity;

import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.phobos.api.ColoredTracker;

public abstract class PositionRotationTracker implements ColoredTracker {
   public static final Vec2f vec2f = new Vec2f(1.0F, 1.0F);
   public Vec3d vec3d;
   public float floatValue = Float.NaN;
   public float floatValue2;
   public long longValue;
   public float floatValue3 = Float.NaN;
   public boolean flag = false;
   public Vec3d vec3d2;
   public long longValue2;
   public Vec3d vec3d3;
   public float floatValue4;

   public float getFloatValue() {
      return this.floatValue;
   }

   public PositionRotationTracker(Vec3d vec3d, Vec3d vec3d2, float f, float f2) {
      this.vec3d = vec3d;
      this.vec3d2 = vec3d;
      this.vec3d3 = vec3d2;
      this.floatValue4 = f;
      this.floatValue2 = f2;
      this.longValue2 = System.currentTimeMillis();
   }

   public Vec3d getVec3d() {
      return this.vec3d;
   }

   public void setLongValue2(long l) {
      this.longValue2 = l;
   }

   public float computeFloat(float f) {
      return !Float.isNaN(this.floatValue3) ? this.floatValue3 : f;
   }

   public void setFloatValue2(float f) {
      this.floatValue2 = f;
   }

   public void setFloatValue(float f) {
      this.floatValue = f;
   }

   public void setFloatValue3(float f) {
      this.floatValue3 = f;
   }

   public Vec3d getVec3d2() {
      return this.vec3d2;
   }

   public void setVec3d3(Vec3d vec3d) {
      this.vec3d3 = vec3d;
   }

   public float computeFloat2(float f) {
      return !Float.isNaN(this.floatValue) ? this.floatValue : f;
   }

   public void setVec3d(Vec3d vec3d) {
      this.vec3d = vec3d;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public PositionRotationTracker() {
      this(Vec3d.ZERO);
   }

   public void setVec3d2(Vec3d vec3d) {
      this.vec3d2 = vec3d;
   }

   public void setLongValue(long l) {
      this.longValue = l;
   }

   public PositionRotationTracker(Vec3d vec3d) {
      this(vec3d, Vec3d.ZERO);
   }

   public Vec3d getVec3d3() {
      return this.vec3d3;
   }

   public long getLongValue() {
      return this.longValue;
   }

   public void setFloatValue4(float f) {
      this.floatValue4 = f;
   }

   public PositionRotationTracker(Vec3d vec3d, Vec3d vec3d2, Vec2f vec2f) {
      this(vec3d, vec3d2, vec2f.x, vec2f.y);
   }

   public PositionRotationTracker(Vec3d vec3d, Vec3d vec3d2) {
      this(vec3d, vec3d2, vec2f);
   }

   public float getFloatValue2() {
      return this.floatValue2;
   }

   public float getFloatValue3() {
      return this.floatValue3;
   }

   public long getLongValue2() {
      return this.longValue2;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public float getFloatValue4() {
      return this.floatValue4;
   }
}
