package org.phobos.core.anticheat;

import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.phobos.api.MinecraftHolder;
import org.phobos.module.client.NotificationsModule;
import org.phobos.core.entity.EntityListUtils;

public abstract class AntiCheatCheck implements MinecraftHolder {
   public int intValue2;
   public final AntiCheatPlayer antiCheatPlayer;
   public final String name;

   public void run6() {
      if (this.antiCheatPlayer.flag2 && !this.antiCheatPlayer.flag4) {
         this.run10();
      }

      this.run3();
   }

   public void handlePacket(Packet<?> packet) {
      this.handlePacket2(packet);
   }

   public void handleText(String text) {
      if (this.antiCheatPlayer.antiCheatCheckRegistry.intValue2 <= 0) {
         if (!this.isFlag4()) {
            if (this.antiCheatPlayer.getAntiCheatModule().getSelf().getValue() || !this.antiCheatPlayer.equals(AntiCheatScheduler.getAntiCheatScheduler())) {
               this.intValue2++;
               this.antiCheatPlayer.intValue3++;
               this.antiCheatPlayer.antiCheatCheckRegistry.handleConsumer(antiCheatCheck -> antiCheatCheck.handleAntiCheatCheck2(this));
               if (this.intValue2 >= this.value()) {
                  NotificationsModule.getNotificationsModule()
                     .handleText5(
                        this.antiCheatPlayer
                           .abstractClientPlayerEntity
                           .getName()
                           .copy()
                           .append(Text.literal(" §7failed§r " + this.name + String.format(" §7(§fVL:%s§7)§r", this.intValue2) + " | §7" + text))
                     );
               }
            }
         }
      }
   }

   public void run3() {
   }

   public AntiCheatCheck(String text, AntiCheatPlayer antiCheatPlayer) {
      this.name = text;
      this.antiCheatPlayer = antiCheatPlayer;
   }

   public List<Entity> resolveList2(double d) {
      return EntityListUtils.getList()
         .stream()
         .filter(entity -> !entity.equals(this.antiCheatPlayer.abstractClientPlayerEntity))
         .filter(entity -> entity.distanceTo(this.antiCheatPlayer.abstractClientPlayerEntity) < d)
         .toList();
   }

   public void run7() {
      if (this.antiCheatPlayer.antiCheatCheckRegistry.intValue2 <= 0) {
         if (!this.isFlag4()) {
            if (this.antiCheatPlayer.getAntiCheatModule().getSelf().getValue() || !this.antiCheatPlayer.equals(AntiCheatScheduler.getAntiCheatScheduler())) {
               this.intValue2++;
               this.antiCheatPlayer.intValue3++;
               this.antiCheatPlayer.antiCheatCheckRegistry.handleConsumer(antiCheatCheck -> antiCheatCheck.handleAntiCheatCheck2(this));
               if (this.intValue2 >= this.value()) {
                  NotificationsModule.getNotificationsModule()
                     .handleText5(
                        this.antiCheatPlayer
                           .abstractClientPlayerEntity
                           .getName()
                           .copy()
                           .append(Text.literal(" §7failed§r " + this.name + String.format(" §7(§fVL:%s§7)§r", this.intValue2)))
                     );
               }
            }
         }
      }
   }

   public void handlePacket2(Packet<?> packet) {
   }

   public String getName() {
      return this.name;
   }

   public void run8() {
      this.run9();
   }

   public void handleAntiCheatCheck2(AntiCheatCheck antiCheatCheck) {
   }

   public abstract boolean isFlag4();

   public void run4() {
   }

   public void run9() {
   }

   public void handleBlockPos(BlockPos blockPos, BlockState blockState) {
   }

   public void run5() {
   }

   public void run10() {
   }

   public abstract int value();
}
