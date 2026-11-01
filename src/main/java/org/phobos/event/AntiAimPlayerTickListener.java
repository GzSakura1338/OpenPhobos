package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import org.phobos.api.TimeGate;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.module.misc.AntiAimModule;
import org.phobos.module.misc.AntiAimModule;

public final class AntiAimPlayerTickListener extends ModuleListener.Safe<AntiAimModule, PlayerTickEvent> {
   public final TimeGate.Normal normal = new TimeGate.Normal();
   public boolean flag;
   public float floatValue = 0.0F;

   public AntiAimPlayerTickListener(AntiAimModule antiAimModule) {
      super(antiAimModule, PlayerTickEvent.class, -4);
      this.flag = false;
   }

   public void handleObject(
      PlayerTickEvent playerTickEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      switch ((AntiAimModule.Mode)this.module.mode.getValue()) {
         case Static:
            this.phobosClient.getRotationManager().testFloat(this.module.base.getValue(), this.module.pitch.getValue());
            break;
         case Spin:
            this.floatValue = this.floatValue + this.module.speed.getValue();
            this.phobosClient.getRotationManager().testFloat(this.floatValue, this.module.pitch.getValue());
            break;
         case Jitter:
            if (this.normal.testNumber2(this.module.delay.getValue())) {
               this.flag = !this.flag;
            }

            this.phobosClient
               .getRotationManager()
               .testFloat(
                  clientPlayerEntity.getYaw() + this.module.base.getValue() + (this.flag ? -this.module.angle.getValue() : this.module.angle.getValue()),
                  this.module.pitch.getValue()
               );
            break;
         case Stare:
            PlayerEntity playerEntity = EntityUtils.resolvePlayerEntity2(clientWorld, clientPlayerEntity, this.phobosClient);
            if (playerEntity != null) {
               this.phobosClient.getRotationManager().testFloat(RotationUtils.values(clientPlayerEntity, playerEntity)[0], this.module.pitch.getValue());
            }
      }
   }
}
