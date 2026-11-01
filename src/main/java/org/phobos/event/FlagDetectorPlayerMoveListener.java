package org.phobos.event;

import org.phobos.core.event.events.PlayerMoveEvent;
import org.phobos.module.misc.FlagDetectorModule;

public final class FlagDetectorPlayerMoveListener extends ModuleListener<FlagDetectorModule, PlayerMoveEvent> {
   public FlagDetectorPlayerMoveListener(FlagDetectorModule flagDetectorModule) {
      super(flagDetectorModule, PlayerMoveEvent.class);
   }

   public void handleObject2(PlayerMoveEvent playerMoveEvent) {
      if (this.module.combat.getValue()) {
         if (this.module.flag && ++this.module.intValue2 >= 3 && this.module.playerEntity != null && this.module.playerEntity.hurtTime <= 0) {
            this.module.flag = false;
            this.module.intValue2 = 0;
            this.module.playerEntity = null;
            this.module.run();
         }
      }
   }
}
