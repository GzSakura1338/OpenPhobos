package org.phobos.core.fakeplayer;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import org.phobos.core.prediction.FlagHolder;

public class TrackedFakePlayer extends ControlledFakePlayer {
   public final FlagHolder flagHolder = new FlagHolder();
   public long longValue;

   public TrackedFakePlayer(AbstractClientPlayerEntity abstractClientPlayerEntity) {
      super(abstractClientPlayerEntity);
   }

   public FlagHolder getFlagHolder() {
      return this.flagHolder;
   }

   public void setLongValue(long l) {
      this.longValue = l;
   }

   public long getLongValue() {
      return this.longValue;
   }
}
