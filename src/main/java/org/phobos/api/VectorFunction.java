package org.phobos.api;

import org.phobos.core.math.Vector3;

public interface VectorFunction {
   float computeFloat3(float var1);

   default Vector3 createVector3(float f) {
      return new Vector3(this.computeFloat(f), this.computeFloat3(f), this.computeFloat2(f));
   }

   float computeFloat(float var1);

   float computeFloat2(float var1);
}
