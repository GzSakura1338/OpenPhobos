package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.math.MathUtils;
import org.phobos.module.misc.AutoClickerModule;
import org.phobos.module.misc.AutoClickerModule;
import org.phobos.module.misc.AutoClickerModule;

public final class AutoClickerHandleInputEventsListener extends ModuleListener.Safe<AutoClickerModule, HandleInputEventsEvent> {
   public void handleObject(
      HandleInputEventsEvent handleInputEventsEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      switch ((AutoClickerModule.Pattern)this.module.pattern.getValue()) {
         case Constant:
            if (this.module.normal.testNumber2(this.module.delay.getValue())) {
               this.module.run();
            }
            break;
         case Random:
            if (this.module.normal.testLong2(this.module.delay.getValue() + this.module.longValue)) {
               this.module.longValue = MathUtils.computeLong5(-this.module.spread.getValue(), this.module.spread.getValue());
               this.module.run();
            }
            break;
         case Burst:
            AutoClickerModule.ClickPattern clickPattern2 = (AutoClickerModule.ClickPattern)this.module.clickPatternMutableObject.getValue();
            if (clickPattern2 != null) {
               if (!clickPattern2.check()) {
                  clickPattern2.handleInt(this.module.offset.getValue(), this.module::run);
                  return;
               }

               this.module.clickPatternMutableObject.setValue(null);
            }

            if (clickPattern2 == null && this.module.normal.testNumber2(this.module.delay.getValue())) {
               AutoClickerModule.ClickPattern clickPattern = new AutoClickerModule.ClickPattern(
                  Math.max(this.module.size.getValue() + MathUtils.computeInt5(-this.module.random.getValue(), this.module.random.getValue()), 1),
                  this.module.spread.getValue()
               );
               this.module.clickPatternMutableObject.setValue(clickPattern);
            }
      }
   }

   public AutoClickerHandleInputEventsListener(AutoClickerModule autoClickerModule) {
      super(autoClickerModule, HandleInputEventsEvent.class);
   }
}
