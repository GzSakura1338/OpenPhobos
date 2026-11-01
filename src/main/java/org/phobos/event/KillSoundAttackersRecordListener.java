package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.data.AttackersRecord;
import org.phobos.module.misc.KillSoundModule;

public final class KillSoundAttackersRecordListener extends ModuleListener.Safe<KillSoundModule, AttackersRecord> {
   public KillSoundAttackersRecordListener(KillSoundModule killSoundModule) {
      super(killSoundModule, AttackersRecord.class);
   }

   public void handleObject(
      AttackersRecord attackersRecord,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (attackersRecord.testPlayerEntity(clientPlayerEntity)) {
         this.module.mode.getValue().play(this.phobosClient, this.module.volume.getValue());
      }
   }
}
