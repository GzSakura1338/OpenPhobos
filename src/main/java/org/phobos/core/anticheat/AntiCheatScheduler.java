package org.phobos.core.anticheat;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import org.phobos.module.client.AntiCheatModule;

public class AntiCheatScheduler extends AntiCheatPlayer {
   public static AntiCheatScheduler antiCheatScheduler;
   public ClientPlayerEntity clientPlayerEntity;
   public ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(8);

   @Override
   public void handleAbstractClientPlayerEntity(AbstractClientPlayerEntity abstractClientPlayerEntity) {
      if (abstractClientPlayerEntity instanceof ClientPlayerEntity clientPlayerEntity) {
         this.clientPlayerEntity = clientPlayerEntity;
      }

      super.handleAbstractClientPlayerEntity(abstractClientPlayerEntity);
   }

   public AntiCheatScheduler(ClientPlayerEntity clientPlayerEntity, AntiCheatModule antiCheatModule) {
      super(clientPlayerEntity, true, antiCheatModule);
      this.clientPlayerEntity = clientPlayerEntity;
      antiCheatScheduler = this;
   }

   public static AntiCheatScheduler getAntiCheatScheduler() {
      return antiCheatScheduler;
   }

   @Override
   public boolean alwaysTrue() {
      return true;
   }
}
