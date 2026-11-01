package org.phobos.core.movement;

import java.util.function.Function;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import org.phobos.core.tick.TimerManager.TimerPriority;
import org.phobos.event.GetStepHeightEvent;
import org.phobos.event.Listener;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.core.ClientHolder;
import org.phobos.core.Feature;
import org.phobos.core.event.events.PlayerMoveEvent;

public class MotionManager extends Feature {
   public boolean flag;
   public Function<Double, double[]> doubleValueFunction;
   public boolean flag2;
   public boolean flag3;
   public boolean flag4;
   public float floatValue = 0.6F;
   public boolean flag5;

   public void setDoubleValueFunction(Function<Double, double[]> function) {
      this.doubleValueFunction = function;
   }

   public void handleDouble(double[] doubles, ClientPlayerEntity clientPlayerEntity) {
      for (double d : doubles) {
         if (this.flag3) {
            PlayerEntity playerEntity = ClientHolder.getPhobosClient().getMovementSimulator().resolvePlayerEntity(clientPlayerEntity);
            clientPlayerEntity.networkHandler
               .sendPacket(
                  new Full(
                     clientPlayerEntity.prevX, clientPlayerEntity.prevY + d, clientPlayerEntity.prevZ, playerEntity.getYaw(), playerEntity.getPitch(), false
                  )
               );
         } else {
            clientPlayerEntity.networkHandler
               .sendPacket(new PositionAndOnGround(clientPlayerEntity.prevX, clientPlayerEntity.prevY + d, clientPlayerEntity.prevZ, false));
         }
      }
   }

   public double[] values() {
      return new double[]{0.42, 0.75, 1.0};
   }

   public double[] values2(double d) {
      double[] doubles;
      if (d >= 2.0) {
         doubles = new double[]{0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43};
      } else {
         doubles = new double[d > 1.0 ? 6 : 2];
         doubles[0] = 0.42;
         doubles[1] = d < 1.0 && d > 0.8 ? 0.753 : 0.75;
         if (d > 1.0) {
            doubles[2] = 1.0;
            doubles[3] = 1.16;
            doubles[4] = 1.23;
            doubles[5] = 1.2;
         }
      }

      return doubles;
   }

   public boolean isFlag42() {
      return this.flag4;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public Function<Double, double[]> getDoubleValueFunction() {
      return this.doubleValueFunction;
   }

   public boolean isFlag5() {
      return this.flag5;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public void setFlag3(boolean flag) {
      this.flag3 = flag;
   }

   public void setFloatValue(float f) {
      this.floatValue = f;
   }

   public float getFloatValue6() {
      return this.floatValue;
   }

   public boolean isFlag3() {
      return this.flag3;
   }

   public boolean isFlag2() {
      return this.flag2;
   }

   public void setFlag5(boolean flag) {
      this.flag5 = flag;
   }

   public MotionManager() {
      this.flag = false;
      this.flag3 = false;
      this.flag4 = false;
      this.flag2 = false;
      this.flag5 = false;
      this.handleListener(
         new SafeListener<PlayerMoveEvent>() {
            public void handleObject(
               PlayerMoveEvent playerMoveEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (MotionManager.this.flag && MotionManager.this.flag4 && MotionManager.this.doubleValueFunction != null) {
                  double d = clientPlayerEntity.getY() - clientPlayerEntity.prevY;
                  if (!(d <= 0.5) && !(d > MotionManager.this.floatValue)) {
                     if (MotionManager.this.flag2) {
                        ClientHolder.getPhobosClient().getTimerManager().handleFloat(d > 1.0 ? 0.15F : 0.35F, TimerPriority.Max);
                        MotionManager.this.flag5 = true;
                     }

                     MotionManager.this.handleDouble(MotionManager.this.doubleValueFunction.apply(d), clientPlayerEntity);
                  }
               }
            }
         }
      );
      this.handleListener(new Listener<GetStepHeightEvent>() {
         public void handleObject2(GetStepHeightEvent getStepHeightEvent) {
            if (MotionManager.this.flag4) {
               getStepHeightEvent.setFloatValue(MotionManager.this.floatValue);
            }
         }
      });
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (MotionManager.this.flag5) {
                  if (clientPlayerEntity.isTouchingWater()
                     || clientPlayerEntity.isInLava()
                     || clientPlayerEntity.isFallFlying()
                     || clientPlayerEntity.isOnGround()) {
                     ClientHolder.getPhobosClient().getTimerManager().run2();
                     MotionManager.this.flag5 = false;
                  }
               }
            }
         }
      );
   }

   public void setFlag4(boolean flag) {
      this.flag4 = flag;
   }

   public void setFlag2(boolean flag) {
      this.flag2 = flag;
   }
}
