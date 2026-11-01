package org.earthhack.ducks;

import net.minecraft.entity.EntityPose;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.tick.TimeUtils;

public interface IEntity {
   long getAttackTime();

   void setAttackTime(long var1);

   boolean isClientSide();

   void setClientSide(boolean var1);

   default long value() {
      return TimeUtils.value() - this.getAttackTime();
   }

   void put(String var1, Object var2);

   Object get(String var1);

   @Nullable
   EntityPose getServerPose();

   void setServerPose(EntityPose var1);

   boolean isPainted();

   void setPainted(boolean var1);
}
