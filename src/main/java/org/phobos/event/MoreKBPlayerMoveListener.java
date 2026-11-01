package org.phobos.event;

import org.phobos.core.event.events.PlayerMoveEvent;
import org.phobos.module.combat.MoreKBModule;
import org.phobos.module.combat.MoreKBModule;

public final class MoreKBPlayerMoveListener extends ModuleListener<MoreKBModule, PlayerMoveEvent> {
   public void handleObject2(PlayerMoveEvent playerMoveEvent) {
      if (this.module.mode.getValue().equals(MoreKBModule.Mode.Legit_Fast) && this.module.flag) {
         mc.options.sprintKey.setPressed(true);
         this.module.flag = false;
      }
   }

   public MoreKBPlayerMoveListener(MoreKBModule moreKBModule) {
      super(moreKBModule, PlayerMoveEvent.class);
   }
}
