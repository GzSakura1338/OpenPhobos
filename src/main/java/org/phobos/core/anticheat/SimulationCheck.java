package org.phobos.core.anticheat;

import net.minecraft.text.Text;
import org.phobos.module.client.NotificationsModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SimulationCheck extends AntiCheatCheck {
   public static final Logger LOGGER2 = LoggerFactory.getLogger(SimulationCheck.class);
   public int intValue3 = 0;

   @Override
   public void handleText(String text) {
      if (this.antiCheatPlayer.antiCheatCheckRegistry.intValue2 <= 0 && this.intValue3 <= 0) {
         if (!this.isFlag4()) {
            if (this.antiCheatPlayer.getAntiCheatModule().getSelf().getValue() || !this.antiCheatPlayer.equals(AntiCheatScheduler.getAntiCheatScheduler())) {
               this.intValue2++;
               this.antiCheatPlayer.intValue2++;
               this.antiCheatPlayer.intValue3++;
               this.antiCheatPlayer.antiCheatCheckRegistry.handleConsumer(antiCheatCheck -> antiCheatCheck.handleAntiCheatCheck2(this));
               if (this.intValue2 >= this.value()) {
                  NotificationsModule.getNotificationsModule()
                     .handleText5(
                        this.antiCheatPlayer
                           .abstractClientPlayerEntity
                           .getName()
                           .copy()
                           .append(
                              Text.literal(" §7failed§r Simulation" + String.format(" §7(§fVL:%s§7)§r", this.antiCheatPlayer.getIntValue2()) + " | §7" + text)
                           )
                     );
               }
            }
         }
      }
   }

   @Override
   public void run7() {
      if (this.antiCheatPlayer.antiCheatCheckRegistry.intValue2 <= 0 && this.intValue3 <= 0) {
         if (!this.isFlag4()) {
            if (this.antiCheatPlayer.getAntiCheatModule().getSelf().getValue() || !this.antiCheatPlayer.equals(AntiCheatScheduler.getAntiCheatScheduler())) {
               this.intValue2++;
               this.antiCheatPlayer.intValue2++;
               this.antiCheatPlayer.intValue3++;
               this.antiCheatPlayer.antiCheatCheckRegistry.handleConsumer(antiCheatCheck -> antiCheatCheck.handleAntiCheatCheck2(this));
               if (this.intValue2 >= this.value()) {
                  NotificationsModule.getNotificationsModule()
                     .handleText5(
                        this.antiCheatPlayer
                           .abstractClientPlayerEntity
                           .getName()
                           .copy()
                           .append(Text.literal(" §7failed§r Simulation" + String.format(" §7(§fVL:%s§7)§r", this.antiCheatPlayer.getIntValue2())))
                     );
               }
            }
         }
      }
   }

   @Override
   public void run6() {
      if (this.intValue3 > 0) {
         this.intValue3--;
      }

      super.run6();
   }

   @Override
   public void run9() {
      this.intValue3 = 4;
      super.run9();
   }

   public SimulationCheck(String text, AntiCheatPlayer antiCheatPlayer) {
      super(text, antiCheatPlayer);
   }
}
