package org.phobos.event;

import net.minecraft.block.Blocks;
import org.phobos.module.movement.IceSpeedModule;

public final class IceSpeedTickListener extends ModuleListener<IceSpeedModule, TickEvent2> {
   public IceSpeedTickListener(IceSpeedModule iceSpeedModule) {
      super(iceSpeedModule, TickEvent2.class);
   }

   public float getFloatValue22() {
      return 0.4F - this.module.speed.getValue();
   }

   public void handleObject2(TickEvent2 tickEvent2) {
      this.module.resolveIAbstractBlock(Blocks.ICE).setSlipperiness(this.getFloatValue22());
      this.module.resolveIAbstractBlock(Blocks.PACKED_ICE).setSlipperiness(this.getFloatValue22());
      this.module.resolveIAbstractBlock(Blocks.FROSTED_ICE).setSlipperiness(this.getFloatValue22());
   }
}
