package org.phobos.event;

import net.minecraft.text.Text;
import org.phobos.module.movement.DisablerModule;
import org.phobos.module.movement.DisablerModule;
import org.phobos.module.client.NotificationsModule;

public final class DisablerJumpPostListener extends ModuleListener<DisablerModule, JumpEvent2.Post> {
   public void handleObject2(JumpEvent2.Post post) {
      if (this.module.mode.getValue().equals(DisablerModule.Mode.Watchdog) && this.module.motion.getValue()) {
         if (this.module.flag9) {
            NotificationsModule.getNotificationsModule()
               .handleText9(Text.literal("Executing disabler task, this may take a while..."), "WatchdogMotionDisabler");
            this.module.flag9 = false;
            this.module.flag11 = true;
         }
      }
   }

   public DisablerJumpPostListener(DisablerModule disablerModule) {
      super(disablerModule, JumpEvent2.Post.class);
   }
}
