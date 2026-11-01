package org.earthhack.ducks;

public interface IVec3d {
   default void set(double x, double y, double z) {
      this.setX(x);
      this.setY(y);
      this.setZ(z);
   }

   void setX(double var1);

   void setY(double var1);

   void setZ(double var1);
}
