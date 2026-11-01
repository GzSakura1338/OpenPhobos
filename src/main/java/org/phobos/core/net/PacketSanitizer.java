package org.phobos.core.net;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.event.Listener;
import org.phobos.event.SafeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.ClientHolder;
import org.phobos.core.Feature;
import org.phobos.core.math.NumberChecks;
import org.phobos.core.event.events.PlayerMoveEvent;

public class PacketSanitizer extends Feature implements MinecraftHolder {
   public static final Logger LOGGER = LoggerFactory.getLogger(PacketSanitizer.class);

   public PacketSanitizer() {
      this.getListenerList().add(new Listener<PlayerMoveEvent>() {
         public void handleObject2(PlayerMoveEvent playerMoveEvent) {
         }
      });
      this.handleListener(
         new SafeListener<Receive<PlayerPositionLookS2CPacket>>(Integer.MAX_VALUE) {
            public void handleObject(
               Receive<PlayerPositionLookS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               PlayerPositionLookS2CPacket playerPositionLookS2CPacket = (PlayerPositionLookS2CPacket)receive.getPacket();
               Runnable runnableArg = () -> ClientHolder.getPhobosClient()
                  .getChatManager()
                  .handleText("Server has attempted illegal position look packet, discarding");
               double[] doubles = new double[]{playerPositionLookS2CPacket.getX(), playerPositionLookS2CPacket.getY(), playerPositionLookS2CPacket.getZ()};
               float[] floats = new float[]{playerPositionLookS2CPacket.getYaw(), playerPositionLookS2CPacket.getPitch()};

               for (double d : doubles) {
                  if (NumberChecks.testDouble(d)) {
                     runnableArg.run();
                     receive.setCancelled(true);
                     return;
                  }
               }

               for (float f : floats) {
                  if (NumberChecks.testFloat(f)) {
                     runnableArg.run();
                     receive.setCancelled(true);
                     return;
                  }
               }

               if (playerPositionLookS2CPacket.getFlags().size() > PositionFlag.values().length) {
                  runnableArg.run();
                  receive.setCancelled(true);
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Receive<ParticleS2CPacket>>(Integer.MAX_VALUE) {
            public void handleObject(
               Receive<ParticleS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               ParticleS2CPacket particleS2CPacket = (ParticleS2CPacket)receive.getPacket();
               Runnable runnableArg = () -> ClientHolder.getPhobosClient()
                  .getChatManager()
                  .handleText("Server has attempted illegal particle packet, discarding");
               double[] doubles = new double[]{particleS2CPacket.getX(), particleS2CPacket.getY(), particleS2CPacket.getZ()};
               float[] floats = new float[]{particleS2CPacket.getOffsetX(), particleS2CPacket.getOffsetY(), particleS2CPacket.getOffsetZ()};

               for (double d : doubles) {
                  if (NumberChecks.testDouble(d)) {
                     runnableArg.run();
                     receive.setCancelled(true);
                     return;
                  }
               }

               for (float f : floats) {
                  if (NumberChecks.testFloat(f)) {
                     runnableArg.run();
                     receive.setCancelled(true);
                     return;
                  }
               }

               if (NumberChecks.testFloat(particleS2CPacket.getSpeed())) {
                  runnableArg.run();
                  receive.setCancelled(true);
               } else if (NumberChecks.testFloat(particleS2CPacket.getCount())) {
                  runnableArg.run();
                  receive.setCancelled(true);
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Receive<ExplosionS2CPacket>>(Integer.MAX_VALUE) {
            public void handleObject(
               Receive<ExplosionS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               ExplosionS2CPacket explosionS2CPacket = (ExplosionS2CPacket)receive.getPacket();
               Runnable runnableArg = () -> ClientHolder.getPhobosClient()
                  .getChatManager()
                  .handleText("Server has attempted illegal explosion packet, discarding");
               double[] doubles = new double[]{explosionS2CPacket.getX(), explosionS2CPacket.getY(), explosionS2CPacket.getZ()};
               float[] floats = new float[]{
                  explosionS2CPacket.getPlayerVelocityX(), explosionS2CPacket.getPlayerVelocityY(), explosionS2CPacket.getPlayerVelocityZ()
               };

               for (double d : doubles) {
                  if (NumberChecks.testDouble(d)) {
                     runnableArg.run();
                     receive.setCancelled(true);
                     return;
                  }
               }

               float[] var14 = floats;
               int var15 = var14.length;

               for (int var16 = 0; var16 < var15; var16++) {
                  double d2 = var14[var16];
                  if (NumberChecks.testDouble(d2)) {
                     runnableArg.run();
                     receive.setCancelled(true);
                     return;
                  }
               }

               if (NumberChecks.testFloat(explosionS2CPacket.getRadius())) {
                  runnableArg.run();
                  receive.setCancelled(true);
               }
            }
         }
      );
   }
}
