package org.phobos.core.combat;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import org.phobos.core.event.events.PacketEvent.PostReceive;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.event.PostReceiveListener.Safe;
import org.phobos.module.client.CalculationsModule;
import org.phobos.core.fakeplayer.ControlledFakePlayer;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.Feature;
import org.phobos.core.rotation.LookDirection;
import org.phobos.core.PhobosClient;
import org.phobos.core.tick.TimeUtils;

public class ExplosionTracker extends Feature {
   public static final long longValue = 500L;
   public final Object object = new Object[0];
   public volatile ExplosionTracker.Frame frame;
   public volatile ExplosionTracker.Frame frame2;
   public volatile float floatValue;
   public final Deque<ExplosionTracker.Explosion> explosionDeque = new ArrayDeque<>();
   public volatile ExplosionTracker.Frame frame3 = ExplosionTracker.Frame.INSTANCE;

   public ExplosionTracker.Frame getFrame() {
      return this.frame;
   }

   public void handlePlayerEntity(PlayerEntity playerEntity, String text) {
      if (CalculationsModule.check3()) {
         CalculationsModule.getCalculationsModule().handleText6("[%s] %s".formatted(playerEntity.getNameForScoreboard(), text));
      }
   }

   public float computeLong(long l) {
      return TimeUtils.testLong2(this.frame2.longValue, l) ? 0.0F : this.frame2.floatValue;
   }

   public void handleFrame(ExplosionTracker.Frame frame) {
      this.frame = this.frame2;
      this.frame2 = frame;
   }

   public ExplosionTracker.Frame getFrame3() {
      return this.frame3;
   }

   public ExplosionTracker(PhobosClient phobosClient) {
      this.frame = ExplosionTracker.Frame.INSTANCE;
      this.frame2 = ExplosionTracker.Frame.INSTANCE;
      this.floatValue = -1.0F;
      this.handleListener(
         new Safe<PlayerRespawnS2CPacket>(mc) {
            @Override
            public void handlePostReceive(
               PostReceive<PlayerRespawnS2CPacket> postReceive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               ExplosionTracker.this.floatValue = EntityUtils.computeLivingEntity(clientPlayerEntity);
               ExplosionTracker.this.frame3 = ExplosionTracker.Frame.INSTANCE;
               ExplosionTracker.this.frame = ExplosionTracker.Frame.INSTANCE;
               ExplosionTracker.this.frame2 = ExplosionTracker.Frame.INSTANCE;
            }
         }
      );
      this.handleListener(
         new Safe<EntityTrackerUpdateS2CPacket>(mc) {
            @Override
            public void handlePostReceive(
               PostReceive<EntityTrackerUpdateS2CPacket> postReceive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (((EntityTrackerUpdateS2CPacket)postReceive.getPacket()).id() == clientPlayerEntity.getId()) {
                  ExplosionTracker.this.handlePlayerEntity2(clientPlayerEntity);
               }
            }
         }
      );
      this.handleListener(
         new org.phobos.event.PriorityListener.Scheduled.Safe<ExplosionS2CPacket>(mc) {
            @Override
            public void handleReceive2(
               Receive<ExplosionS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               AbstractClientPlayerEntity abstractClientPlayerEntity = ExplosionTracker.this.resolveAbstractClientPlayerEntity(clientPlayerEntity);
               ExplosionTracker.Explosion explosion = new ExplosionTracker.Explosion(new ArrayList<>(), TimeUtils.value());
               ControlledFakePlayer controlledFakePlayer = new ControlledFakePlayer(abstractClientPlayerEntity);
               boolean flag = true;
               DamageCalculator damageCalculator = phobosClient.getDamageCalculator();

               for (LookDirection lookDirection : phobosClient.getMovementSimulator().getLookDirectionDeque2()) {
                  flag = false;
                  if (TimeUtils.testLong2(lookDirection.getLongValue(), 100L)) {
                     break;
                  }

                  lookDirection.handleEntity(controlledFakePlayer);
                  explosion.damages
                     .add(
                        damageCalculator.computeEntity4(
                           controlledFakePlayer,
                           clientWorld,
                           ((ExplosionS2CPacket)receive.getPacket()).getRadius(),
                           ((ExplosionS2CPacket)receive.getPacket()).getX(),
                           ((ExplosionS2CPacket)receive.getPacket()).getY(),
                           ((ExplosionS2CPacket)receive.getPacket()).getZ()
                        )
                     );
               }

               if (flag) {
                  phobosClient.getMovementSimulator().getLookDirection().handleEntity(controlledFakePlayer);
                  explosion.damages
                     .add(
                        damageCalculator.computeEntity4(
                           controlledFakePlayer,
                           clientWorld,
                           ((ExplosionS2CPacket)receive.getPacket()).getRadius(),
                           ((ExplosionS2CPacket)receive.getPacket()).getX(),
                           ((ExplosionS2CPacket)receive.getPacket()).getY(),
                           ((ExplosionS2CPacket)receive.getPacket()).getZ()
                        )
                     );
               }

               if (explosion.damages.stream().anyMatch(floatValue -> floatValue > 0.0F)) {
                  ExplosionTracker.this.explosionDeque.add(explosion);
               }
            }
         }
      );
   }

   public ExplosionTracker.Frame getFrame2() {
      return this.frame2;
   }

   public Deque<ExplosionTracker.Explosion> getExplosionDeque() {
      return this.explosionDeque;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public void handlePlayerEntity2(PlayerEntity playerEntity) {
      synchronized (this.object) {
         if (!EntityUtils.testLivingEntity(playerEntity)) {
            float f2 = EntityUtils.computeLivingEntity(playerEntity);
            if (f2 != this.floatValue && f2 < this.floatValue) {
               float f = this.floatValue - f2;
               this.frame3 = new ExplosionTracker.Frame();
               this.frame3.floatValue += f;
               if (TimeUtils.testLong2(this.frame2.longValue, 750L)) {
                  this.handleFrame(new ExplosionTracker.Frame());
                  this.frame2.floatValue += f;
               } else if (this.explosionDeque.isEmpty()) {
                  if (TimeUtils.testLong2(this.frame2.longValue, 495L)) {
                     this.handleFrame(new ExplosionTracker.Frame());
                  }

                  this.frame2.floatValue += f;
               } else {
                  for (ExplosionTracker.Explosion explosion : this.explosionDeque) {
                     this.handlePlayerEntity3(playerEntity, f, explosion);
                  }
               }

               this.explosionDeque.clear();
            }

            this.floatValue = f2;
         }
      }
   }

   public void handlePlayerEntity3(PlayerEntity playerEntity, float f, ExplosionTracker.Explosion explosion) {
      boolean flag = TimeUtils.testLong2(explosion.time, 55L);
      float f4 = Float.MAX_VALUE;
      float f5 = -1.0F;
      float f6 = Float.MAX_VALUE;
      float f7 = -1.0F;
      boolean flag2 = false;

      for (float f3 : explosion.damages) {
         float f2 = Math.abs(f3 - f);
         if (f2 < f4) {
            f4 = f2;
            f5 = f3;
         }

         f2 = f3 - this.frame2.floatValue;
         if (f2 < 0.0F) {
            flag2 = true;
         }

         f2 = Math.abs(f2);
         if (f2 < f6) {
            f6 = f2;
            f7 = f3;
         }

         f2 = f3 - this.frame.floatValue;
         if (f2 < 0.0F) {
            flag2 = true;
         }
      }

      if (flag) {
         if (!flag2) {
            this.handlePlayerEntity(playerEntity, "Explosion (%sms ago) did not deal any damage".formatted(TimeUtils.computeLong(explosion.time)));
         }
      } else if (!(f5 >= 0.0F) || !(f7 >= 0.0F)) {
         this.handlePlayerEntity(playerEntity, "No damage recorded");
         this.handleFrame(new ExplosionTracker.Frame());
      } else if (f4 < f6) {
         long l = TimeUtils.computeLong(this.frame2.longValue);
         if (l < 400L) {
            this.handlePlayerEntity(playerEntity, "Frame miss-match (%s < 400)".formatted(l));
         }

         if (f4 >= 1.0F) {
            this.handlePlayerEntity(playerEntity, "Out-Frame prediction offset: %s / predicted: %s".formatted(f4, f5));
         }

         this.handleFrame(new ExplosionTracker.Frame());
         this.frame2.floatValue = f;
      } else {
         if (f6 >= 1.0F) {
            this.handlePlayerEntity(playerEntity, "In-Frame prediction offset: %s / predicted: %s".formatted(f6, f7));
         }

         this.frame2.floatValue += f;
      }
   }

   public AbstractClientPlayerEntity resolveAbstractClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      return clientPlayerEntity;
   }

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, float f, long l) {
      return EntityUtils.computeLivingEntity(clientPlayerEntity) + this.computeLong(l) - f <= 0.0F;
   }

   private record Explosion(List<Float> damages, long time) {
   }

   private static class Frame {
      public static final ExplosionTracker.Frame INSTANCE = new ExplosionTracker.Frame();
      public final long longValue;
      public float floatValue;

      public Frame() {
         this(TimeUtils.value());
      }

      public float getFloatValue() {
         return this.floatValue;
      }

      public Frame(long l) {
         this.longValue = l;
      }

      public long getLongValue() {
         return this.longValue;
      }
   }
}
