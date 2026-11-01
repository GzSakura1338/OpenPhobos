package org.phobos.core.anticheat.check;

import net.minecraft.entity.MovementType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.ClientHolder;
import org.phobos.core.fakeplayer.MovingFakePlayer;
import org.phobos.core.task.Scheduler;
import org.phobos.core.anticheat.SimulationCheck;

public class TickACheck extends SimulationCheck {
   public static final Logger LOGGER = LoggerFactory.getLogger(TickACheck.class);

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getMovement().getValue() || !this.antiCheatPlayer.getAntiCheatModule().getSimulation().getValue();
   }

   @Override
   public int value() {
      return 2;
   }

   @Override
   public void run3() {
      if (!this.isFlag4()) {
         if (!this.antiCheatPlayer.abstractClientPlayerEntity.isFallFlying() && !this.antiCheatPlayer.abstractClientPlayerEntity.hasVehicle()) {
            MovingFakePlayer movingFakePlayer = new MovingFakePlayer(this.antiCheatPlayer.abstractClientPlayerEntity.clientWorld);
            movingFakePlayer.copyFrom(this.antiCheatPlayer.abstractClientPlayerEntity);
            movingFakePlayer.setPosition(this.antiCheatPlayer.getVec3d3());
            movingFakePlayer.setVelocity(this.antiCheatPlayer.getVec3d2());
            movingFakePlayer.move(MovementType.SELF, movingFakePlayer.getVelocity());
            Scheduler scheduler = ClientHolder.getPhobosClient().getScheduler();
            scheduler.handleSupplier(() -> {
               double d = movingFakePlayer.getPos().distanceTo(this.antiCheatPlayer.vec3d3);
               double d2 = 0.855726 * (!this.antiCheatPlayer.alwaysTrue() ? 2 : 1);
               return d > d2 * this.antiCheatPlayer.doubleValue;
            }, booleanValue -> {
               if (booleanValue) {
                  this.run7();
               }
            }, 1);
         }
      }
   }

   public TickACheck(AntiCheatPlayer antiCheatPlayer) {
      super("TickA", antiCheatPlayer);
   }
}
