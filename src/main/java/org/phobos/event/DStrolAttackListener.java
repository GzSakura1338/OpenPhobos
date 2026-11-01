package org.phobos.event;

import org.phobos.core.tick.TimerManager;
import org.phobos.module.combat.AuraModule;
import org.phobos.module.combat.DStrolModule;
import org.phobos.module.combat.DStrolModule;

public final class DStrolAttackListener extends ModuleListener<DStrolModule, AttackEvent2> {
   public void handleObject2(AttackEvent2 attackEvent2) {
      if (this.module.mode.getValue().equals(DStrolModule.Mode.Single)) {
         if (AuraModule.getEntity() == attackEvent2.getEntity()) {
            this.phobosClient.getTimerManager().handleFloat(this.module.factor.getValue(), TimerManager.TimerPriority.High);
            this.phobosClient.getScheduler().handleRunnable2(() -> this.phobosClient.getTimerManager().run2(), this.module.ticks.getValue() + 1);
         }
      }
   }

   public DStrolAttackListener(DStrolModule dStrolModule) {
      super(dStrolModule, AttackEvent2.class);
   }
}
