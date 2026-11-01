package org.phobos.core.anticheat.check;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.anticheat.SimulationCheck;

public class TimerACheck extends SimulationCheck {
   public long longValue = -300L;

   @Override
   public void run3() {
      this.longValue -= 50L;
   }

   public TimerACheck(AntiCheatPlayer antiCheatPlayer) {
      super("TimerA", antiCheatPlayer);
   }

   @Override
   public int value() {
      return 20;
   }

   @Override
   public void handlePacket2(Packet<?> packet) {
      if (packet instanceof EntityPositionS2CPacket entityPositionS2CPacket) {
         assert mc.world != null;
         if (mc.world.getEntityById(entityPositionS2CPacket.getEntityId()) != this.antiCheatPlayer.abstractClientPlayerEntity) {
            return;
         }

         this.longValue += 50L;
         if (this.longValue >= 100L) {
            this.run7();
            this.longValue -= 50L;
         }
      }
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getMovement().getValue() || !this.antiCheatPlayer.getAntiCheatModule().getSimulation().getValue();
   }
}
