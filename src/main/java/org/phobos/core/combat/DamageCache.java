package org.phobos.core.combat;

import java.util.Collection;
import java.util.function.Function;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityAttributesS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.phobos.api.Raytracer;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.combat.DamageCalculator.CalcType;
import org.phobos.event.SafeListener;
import org.phobos.event.SetStackEvent;
import org.phobos.event.Listener.AnyType;
import org.phobos.core.Feature;
import org.phobos.core.util.NullSafe;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.core.PhobosClient;

public class DamageCache extends Feature implements MinecraftHolder {
   public volatile float floatValue;
   public final DamageCache.MaxDamageCalculator maxDamageCalculator;
   public final Object object = new Object[0];

   public <P extends Packet<?>> void handleClassValue(Class<P> classValue, Function<P, Integer> function) {
      this.handleListener(new AnyType<PacketEvent.PostReceive<P>>(PacketEvent.PostReceive.class, classValue) {
         public void handleObject2(PacketEvent.PostReceive<P> postReceive) {
            MinecraftHolder.mc.submit(() -> NullSafe.handleMinecraftClient(MinecraftHolder.mc, (clientPlayerEntity, clientWorld, clientPlayerInteractionManager) -> {
               if (function.apply(postReceive.getPacket()) == clientPlayerEntity.getId()) {
                  DamageCache.this.handleClientPlayerEntity(clientPlayerEntity, clientWorld);
               }
            }));
         }
      });
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      synchronized (this.object) {
         this.floatValue = this.maxDamageCalculator.computeEntity10(clientPlayerEntity, clientWorld);
      }
   }

   public DamageCache(PhobosClient phobosClient) {
      this.maxDamageCalculator = new DamageCache.MaxDamageCalculator();
      this.floatValue = Float.MAX_VALUE;
      this.handleClassValue(EntityTrackerUpdateS2CPacket.class, EntityTrackerUpdateS2CPacket::id);
      this.handleClassValue(EntityAttributesS2CPacket.class, EntityAttributesS2CPacket::getEntityId);
      this.handleClassValue(EntityStatusEffectS2CPacket.class, EntityStatusEffectS2CPacket::getEntityId);
      this.handleListener(
         new SafeListener<SetStackEvent>() {
            public void handleObject(
               SetStackEvent setStackEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (setStackEvent.getPlayerEntity().equals(clientPlayerEntity)) {
                  phobosClient.getSurroundManager().handleLivingEntity(clientPlayerEntity, clientWorld);
                  DamageCache.this.handleClientPlayerEntity(clientPlayerEntity, clientWorld);
               }
            }
         }
      );
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public DamageCache.MaxDamageCalculator getMaxDamageCalculator() {
      return this.maxDamageCalculator;
   }

   private static final class MaxDamageCalculator extends DamageCalculator {
      public float computeEntity10(Entity entity, World world) {
         return this.computeEntity4(entity, world, 6.0F, entity.getX(), entity.getY(), entity.getZ());
      }

      public MaxDamageCalculator() {
         super(Raytracer.getRaytracer());
      }

      @Override
      public float computeRaytracer(
         Raytracer raytracer, World world, Entity entity, double d, double d2, double d3, CalcType calcType, Collection<BlockPos> collection
      ) {
         return 1.0F;
      }
   }
}
