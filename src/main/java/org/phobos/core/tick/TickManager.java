package org.phobos.core.tick;

import java.util.Arrays;
import java.util.Objects;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.MixinClassNames;
import org.phobos.api.TimeGate;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.module.player.BlinkModule;
import org.phobos.core.event.events.CancellableEvent;
import org.phobos.core.Feature;
import org.phobos.core.PhobosClient;

public class TickManager extends Feature {
   public final PhobosClient phobosClient;
   public volatile boolean flag;
   public final MutableBoolean mutableBoolean;
   public final TimeGate.Threaded threaded = new TimeGate.Threaded();
   public int intValue;
   public int intValue2;
   public boolean flag2;
   public TickManager.Tick tick;
   public Runnable[] runnableArray;

   public boolean isFlag2() {
      return this.flag2;
   }

   public void setRunnableArray(Runnable[] runnables) {
      this.runnableArray = runnables;
   }

   public boolean check() {
      return this.mutableBoolean.getValue();
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      int i = this.intValue2 - this.intValue;
      this.phobosClient.getEventBus().handleRunnable(() -> {
         for (int i2 = 0; i2 < i; i2++) {
            clientPlayerEntity.tick();
         }
      });
      this.mutableBoolean.setFalse();
   }

   public TickManager.Tick getTick() {
      return this.tick;
   }

   public MutableBoolean getMutableBoolean() {
      return this.mutableBoolean;
   }

   public void setFlag2(boolean flag) {
      this.flag2 = flag;
   }

   public void handleClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity) {
      Runnable runnable = switch (this.tick) {
         case Player -> clientPlayerEntity::tick;
         case Game -> mc::tick;
      };

      for (int i = 0; i < this.intValue2; i++) {
         runnable.run();
      }

      Arrays.stream(this.runnableArray).forEach(Runnable::run);
   }

   public void setIntValue(int i) {
      this.intValue = i;
   }

   public TimeGate.Threaded getThreaded() {
      return this.threaded;
   }

   public void handleInt(int i, int i2, boolean flag, Runnable[] runnables) {
      this.mutableBoolean.setTrue();
      this.setIntValue(i);
      this.setIntValue2(i2);
      this.setFlag2(flag);
      this.setRunnableArray(runnables);
   }

   public boolean testLong(long l) {
      return this.threaded.testLong(l);
   }

   public void handleClientPlayerEntity3(ClientPlayerEntity clientPlayerEntity, TickManager.TickData tickData, TickManager.Tick tick) {
      if (this.check()) {
         this.handleClientPlayerEntity5(clientPlayerEntity, null, this.tick, true);
      }

      this.tick = tick;
      this.handleInt(tickData.getIntValue2(), tickData.getIntValue(), tickData.isFlag(), tickData.getRunnableArray());
      this.phobosClient.getScheduler().handleRunnable2(() -> this.setIntValue(0), this.intValue);
   }

   public Runnable[] getRunnableArray() {
      return this.runnableArray;
   }

   public void setIntValue2(int i) {
      this.intValue2 = i;
   }

   public void setTick(TickManager.Tick tick) {
      this.tick = tick;
   }

   public PhobosClient getPhobosClient() {
      return this.phobosClient;
   }

   public int getIntValue2() {
      return this.intValue2;
   }

   public void handleClientPlayerEntity4(ClientPlayerEntity clientPlayerEntity, CancellableEvent cancellableEvent) {
      if (this.intValue == 0 && MixinClassNames.ECA.testFlag3(this.mutableBoolean.isTrue(), this.mutableBoolean::setFalse)) {
         this.flag = true;
         if (this.flag2) {
            this.handleClientPlayerEntity2(clientPlayerEntity);
         } else {
            this.phobosClient.getEventBus().handleRunnable(() -> this.handleClientPlayerEntity2(clientPlayerEntity));
         }

         this.flag = false;
      } else {
         if (this.intValue-- > 0) {
            cancellableEvent.setCancelled(true);
         }
      }
   }

   public int getIntValue() {
      return this.intValue;
   }

   public TickManager(PhobosClient phobosClient) {
      this.mutableBoolean = new MutableBoolean();
      this.flag = false;
      this.intValue2 = 0;
      this.intValue = 0;
      this.phobosClient = phobosClient;
      this.handleListener(
         new SafeListener<Receive<PlayerPositionLookS2CPacket>>() {
            public void handleObject(
               Receive<PlayerPositionLookS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               TickManager.this.threaded.run();
            }
         }
      );
      this.handleListener(
         new SafeListener<PlayerTickEvent>() {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (clientPlayerEntity.getVehicle() == null && !BlinkModule.getBlinkModule().check7()) {
                  if (!TickManager.this.flag) {
                     TickManager.this.handleClientPlayerEntity5(clientPlayerEntity, playerTickEvent, TickManager.Tick.Player, false);
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (clientPlayerEntity.getVehicle() == null && !BlinkModule.getBlinkModule().check7()) {
                  if (!TickManager.this.flag) {
                     TickManager.this.handleClientPlayerEntity5(clientPlayerEntity, tickEvent2, TickManager.Tick.Game, false);
                  }
               }
            }
         }
      );
   }

   public void handleClientPlayerEntity5(ClientPlayerEntity clientPlayerEntity, @Nullable CancellableEvent cancellableEvent, TickManager.Tick tick, boolean flag) {
      if (Objects.equals(this.tick, tick)) {
         if (flag) {
            this.handleClientPlayerEntity(clientPlayerEntity);
         } else if (cancellableEvent != null) {
            this.handleClientPlayerEntity4(clientPlayerEntity, cancellableEvent);
         }
      }
   }

   public enum Tick {
      Player,
      Game;
   }

   public static class TickData {
      public Runnable[] runnableArray;
      public boolean flag = false;
      public final int intValue;
      public final int intValue2;

      public static TickManager.TickData createTickData(int i) {
         return new TickManager.TickData(i, i);
      }

      public static TickManager.TickData resolveTickData(int i, int i2) {
         return createTickData2(i, i + i2);
      }

      public TickData(int i, int i2) {
         this.runnableArray = new Runnable[0];
         this.intValue2 = i;
         this.intValue = i2;
      }

      public static TickManager.TickData resolveTickData2(int i) {
         return createTickData2(i, 0);
      }

      public static TickManager.TickData createTickData2(int i, int i2) {
         return new TickManager.TickData(i, i2);
      }

      public TickManager.TickData resolveTickData3(boolean flag) {
         this.flag = flag;
         return this;
      }

      public void setRunnableArray(Runnable[] runnables) {
         this.runnableArray = runnables;
      }

      public int getIntValue2() {
         return this.intValue2;
      }

      public Runnable[] getRunnableArray() {
         return this.runnableArray;
      }

      public void setFlag(boolean flag) {
         this.flag = flag;
      }

      public int getIntValue() {
         return this.intValue;
      }

      public boolean isFlag() {
         return this.flag;
      }

      public TickManager.TickData resolveTickData4(Runnable... runnables) {
         this.runnableArray = runnables;
         return this;
      }

      public TickManager.TickData getTickData() {
         this.flag = true;
         return this;
      }
   }
}
