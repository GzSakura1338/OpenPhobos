package org.earthhack.ducks;

public interface IClientPlayerEntity {
   void superTick();

   void invokeCrashInternal();

   int getGroundTicks();

   int getIntValue();
}
