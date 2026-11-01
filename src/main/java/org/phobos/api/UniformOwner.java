package org.phobos.api;

public interface UniformOwner {
   default void run6() {
      UniformSetup.handleObject2(this);
   }

   FloatUniform1 getFloatUniform1();
}
