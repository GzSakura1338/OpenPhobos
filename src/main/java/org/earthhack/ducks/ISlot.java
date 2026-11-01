package org.earthhack.ducks;

import net.minecraft.screen.slot.Slot;

public interface ISlot {
   float getLastRenderX();

   float getLastRenderY();

   void setLastRenderX(float var1);

   void setLastRenderY(float var1);

   boolean isLoaded();

   void setLoaded(boolean var1);

   static ISlot cast(Slot slot) {
      return (ISlot)(Object)slot;
   }
}
