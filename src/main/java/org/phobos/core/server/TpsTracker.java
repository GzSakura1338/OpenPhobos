package org.phobos.core.server;

import java.util.ArrayDeque;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.event.SafeListener;
import org.phobos.core.Feature;

public class TpsTracker extends Feature {
   public final ArrayDeque<Float> floatValueArrayDeque = new ArrayDeque<>(20);
   public float floatValue;
   public long longValue;
   public float floatValue2;

   public float value() {
      return this.floatValue2 / 20.0F;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public TpsTracker() {
      this.handleListener(
         new SafeListener<Receive<WorldTimeUpdateS2CPacket>>() {
            public void handleObject(
               Receive<WorldTimeUpdateS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (TpsTracker.this.longValue != 0L) {
                  if (TpsTracker.this.floatValueArrayDeque.size() > 20) {
                     TpsTracker.this.floatValueArrayDeque.poll();
                  }

                  TpsTracker.this.floatValue = Math.max(
                     0.0F, Math.min(20.0F, 20.0F * (1000.0F / (float)(System.currentTimeMillis() - TpsTracker.this.longValue)))
                  );
                  TpsTracker.this.floatValueArrayDeque.add(TpsTracker.this.floatValue);
                  float f = 0.0F;

                  for (Float floatValue : TpsTracker.this.floatValueArrayDeque) {
                     f += Math.max(0.0F, Math.min(20.0F, floatValue));
                  }

                  if (!TpsTracker.this.floatValueArrayDeque.isEmpty()) {
                     f /= TpsTracker.this.floatValueArrayDeque.size();
                  }

                  TpsTracker.this.floatValue2 = f;
               }

               TpsTracker.this.longValue = System.currentTimeMillis();
            }
         }
      );
   }

   public float getFloatValue2() {
      return this.floatValue2;
   }
}
