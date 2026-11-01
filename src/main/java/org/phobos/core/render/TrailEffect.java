package org.phobos.core.render;

import java.util.Random;
import java.util.function.Supplier;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.core.Feature;
import org.phobos.core.PhobosClient;
import org.phobos.core.entity.PositionRotationTracker;

public class TrailEffect extends Feature {
   public final Supplier<PositionRotationTracker> positionRotationTrackerSupplier;
   public final int intValue;
   public final Entity entity;
   public final Random random = new Random();
   public final PhobosClient phobosClient;
   public int intValue2;
   public final long longValue;

   public Entity getEntity() {
      return this.entity;
   }

   public PhobosClient getPhobosClient() {
      return this.phobosClient;
   }

   public Supplier<PositionRotationTracker> getPositionRotationTrackerSupplier() {
      return this.positionRotationTrackerSupplier;
   }

   public int getIntValue2() {
      return this.intValue;
   }

   public int getIntValue22() {
      return this.intValue2;
   }

   public TrailEffect(PhobosClient phobosClient, Supplier<PositionRotationTracker> supplier, Entity entity, int i, long l) {
      this.positionRotationTrackerSupplier = supplier;
      this.phobosClient = phobosClient;
      this.entity = entity;
      this.intValue = i;
      this.longValue = l;
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               for (int ix = 0; ix < 16; ix++) {
                  double d10 = TrailEffect.this.random.nextFloat() * 2.0F - 1.0F;
                  double d11 = TrailEffect.this.random.nextFloat() * 2.0F - 1.0F;
                  double d12 = TrailEffect.this.random.nextFloat() * 2.0F - 1.0F;
                  if (!(d10 * d10 + d11 * d11 + d12 * d12 > 1.0)) {
                     double d7 = entity.offsetX(d10 / 4.0);
                     double d8 = entity.getBodyY(0.5 + d11 / 4.0);
                     double d9 = entity.offsetZ(d12 / 4.0);
                     PositionRotationTracker positionRotationTracker = supplier.get();
                     positionRotationTracker.setVec3d2(new Vec3d(d7, d8, d9));
                     positionRotationTracker.setVec3d3(new Vec3d(d10, d11 + 0.2, d12));
                     double d = (Math.random() * 2.0 - 1.0) * 0.4F;
                     positionRotationTracker.setVec3d3(positionRotationTracker.getVec3d3().add(d, d, d));
                     double d2 = positionRotationTracker.getVec3d3().getX();
                     double d3 = positionRotationTracker.getVec3d3().getY();
                     double d4 = positionRotationTracker.getVec3d3().getZ();
                     double d5 = (Math.random() + Math.random() + 1.0) * 0.15F;
                     double d6 = Math.sqrt(d2 * d2 + d3 * d3 + d4 * d4);
                     d2 = d2 / d6 * d5 * 0.4F;
                     d3 = d3 / d6 * d5 * 0.4F + 0.1F;
                     d4 = d4 / d6 * d5 * 0.4F;
                     positionRotationTracker.setVec3d3(new Vec3d(d2, d3, d4).multiply(0.1F).add(d10 * 0.4, d11 * 0.4, d12 * 0.4));
                     phobosClient.getTrackerManager().handlePositionRotationTracker2(positionRotationTracker, l);
                  }
               }

               TrailEffect.this.intValue2++;
               if (TrailEffect.this.intValue2 >= i) {
                  phobosClient.getEventBus().handleObject4(TrailEffect.this);
               }
            }
         }
      );
   }

   public void setIntValue2(int i) {
      this.intValue2 = i;
   }

   public long getLongValue() {
      return this.longValue;
   }

   public Random getRandom() {
      return this.random;
   }
}
