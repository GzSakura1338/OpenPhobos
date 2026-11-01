package org.phobos.core.entity;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.data.DataTracker.Entry;
import net.minecraft.entity.data.DataTracker.SerializedEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.util.math.Box;
import org.earthhack.ducks.IEntity;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.event.SafeListener;
import org.phobos.module.client.AntiCheatModule;
import org.phobos.core.Feature;

public class EntityPoseManager extends Feature {
   public Box resolveBox(Entity entity, EntityPose entityPose) {
      return entity.getDimensions(entityPose).getBoxAt(entity.getPos());
   }

   public EntityPoseManager() {
      this.nativeInit();
   }

   public void nativeInit() {
      var var1 = new SafeListener<Receive<EntityTrackerUpdateS2CPacket>>() {
         public void handleObject(
            Receive<EntityTrackerUpdateS2CPacket> receive,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            Entity entity = clientWorld.getEntityById(receive.getPacket().id());
            if (entity instanceof PlayerEntity playerEntity) {
               for (SerializedEntry<?> serializedEntry2 : receive.getPacket().trackedValues()) {
                  Entry<?> entry = entity.getDataTracker().entries[serializedEntry2.id()];
                  if (entry.getData().equals(Entity.POSE)) {
                     SerializedEntry<EntityPose> serializedEntry = (SerializedEntry<EntityPose>)serializedEntry2;
                     EntityPose entityPose = serializedEntry.value();
                     IEntity iEntity = (IEntity)(Object)playerEntity;
                     iEntity.setServerPose(entityPose);
                  }
               }
            }
         }
      };
      this.handleListener(var1);
   }

   public EntityPose resolveEntityPose(Entity entity) {
      if (!AntiCheatModule.getAntiCheatModule().getHitboxSync().getValue()) {
         return entity.getPose();
      }

      IEntity iEntity = (IEntity)(Object)entity;
      EntityPose entityPose = iEntity.getServerPose();
      return entityPose == null ? entity.getPose() : entityPose;
   }
}
