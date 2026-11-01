package org.phobos.core.rotation;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.api.TimeGate;
import org.phobos.event.SafeListener;
import org.phobos.core.Feature;
import org.phobos.core.player.PlayerUtils;
import org.phobos.core.tick.TickCounter;

public class RotationDelay extends Feature {
   public RotationManager.Rotation rotation;
   public final TimeGate.Threaded threaded = new TimeGate.Threaded();

   public RotationDelay() {
      this.handleListener(
         new SafeListener<TickCounter>() {
            public void handleObject(
               TickCounter tickCounter,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (PlayerUtils.check5()) {
                  RotationDelay.this.threaded.run();
               }

               RotationManager.Rotation rotation = RotationManager.Rotation.createRotation2(clientPlayerEntity);
               if (!rotation.equals(RotationDelay.this.rotation)) {
                  RotationDelay.this.threaded.run();
               }

               RotationDelay.this.rotation = rotation;
            }
         }
      );
   }

   public boolean check() {
      return !mc.isWindowFocused() || this.threaded.testLong(1500L);
   }
}
