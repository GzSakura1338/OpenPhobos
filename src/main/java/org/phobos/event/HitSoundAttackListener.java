package org.phobos.event;

import org.phobos.module.misc.HitSoundModule;

public final class HitSoundAttackListener extends ModuleListener<HitSoundModule, AttackEvent2> {
   public HitSoundAttackListener(HitSoundModule hitSoundModule) {
      super(hitSoundModule, AttackEvent2.class);
   }

   public void handleObject2(AttackEvent2 attackEvent2) {
      if (!this.module.sync.getValue()) {
         if (this.module.testEntity2(attackEvent2.getEntity())) {
            this.module.mode.getValue().play(this.phobosClient, this.module.volume.getValue());
         }
      }
   }
}
