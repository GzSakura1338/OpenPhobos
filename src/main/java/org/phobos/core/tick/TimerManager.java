package org.phobos.core.tick;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.common.CommonPingS2CPacket;
import org.earthhack.ducks.IPlayerMoveC2SPacket;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.core.event.events.PacketEvent.Send;
import org.phobos.event.BeginRenderTickEvent;
import org.phobos.event.Listener;
import org.phobos.core.Feature;

public class TimerManager extends Feature {
   public float floatValue = 1.0F;
   public long longValue;
   public long longValue2;
   public float floatValue2;
   public long longValue3;
   public long longValue4 = 0L;
   public long longValue5 = (long)(System.nanoTime() - 6.0E10);
   public static TimerManager timerManager;
   public boolean flag;
   public long longValue6;
   public TimerManager.TimerPriority timerPriority;
   public boolean flag2;
   public float floatValue3;

   public long getLongValue6() {
      return this.longValue6;
   }

   public long getLongValue4() {
      return this.longValue4;
   }

   public TimerManager() {
      this.longValue = (long)(System.nanoTime() - 6.0E10);
      this.longValue6 = 120000000L;
      this.longValue3 = 1000000000L;
      this.flag2 = false;
      this.longValue2 = System.nanoTime();
      this.flag = false;
      this.handleListener(new Listener<BeginRenderTickEvent>() {
         public void handleObject2(BeginRenderTickEvent beginRenderTickEvent) {
            TimerManager.this.setFloatValue3(beginRenderTickEvent.getFloatValue3());
            beginRenderTickEvent.setFloatValue(TimerManager.this.floatValue);
            TimerManager.this.floatValue2 = TimerManager.this.floatValue2 + TimerManager.this.floatValue3;
            int i = (int)TimerManager.this.floatValue2;
            TimerManager.this.floatValue2 -= i;
         }
      });
      this.handleListener(new Listener<Send<?>>() {
         public void handleObject2(Send<?> send) {
            if (TimerManager.this.flag2 && send.getPacket() instanceof CommonPongC2SPacket) {
               TimerManager.this.longValue5 = TimerManager.this.longValue;
               TimerManager.this.longValue = TimerManager.this.longValue2;
               TimerManager.this.flag2 = false;
            }

            if (TimerManager.this.testPacket(send.getPacket())) {
               TimerManager.this.flag2 = true;
               TimerManager.this.longValue4 = (long)(TimerManager.this.longValue4 + 5.0E7);
               TimerManager.this.flag = TimerManager.this.longValue4 - 5.0E7 > System.nanoTime();
               long l = TimerManager.this.longValue;
               if (System.nanoTime() - l > TimerManager.this.longValue3) {
                  l = System.nanoTime() - TimerManager.this.longValue3;
               }

               TimerManager.this.longValue4 = Math.max(TimerManager.this.longValue4, l - TimerManager.this.longValue6);
            }
         }
      });
      this.handleListener(new Listener<Receive<CommonPingS2CPacket>>() {
         public void handleObject2(Receive<CommonPingS2CPacket> receive) {
            TimerManager.this.longValue2 = System.nanoTime();
         }
      });
      timerManager = this;
   }

   public boolean isFlag2() {
      return this.flag2;
   }

   public static float value() {
      return timerManager.floatValue3;
   }

   public void run2() {
      this.floatValue = 1.0F;
      this.timerPriority = null;
   }

   public void setFloatValue3(float f) {
      this.floatValue3 = f;
   }

   public float getFloatValue22() {
      return this.floatValue;
   }

   public static float getFloatValue() {
      return timerManager.floatValue2;
   }

   public long getLongValue() {
      return this.longValue;
   }

   public long getLongValue2() {
      return this.longValue2;
   }

   public void handleFloat(float f, TimerManager.TimerPriority timerPriority) {
      if (this.timerPriority == null || this.timerPriority.ordinal() > timerPriority.ordinal()) {
         this.floatValue = f;
      }
   }

   public TimerManager.TimerPriority getTimerPriority() {
      return this.timerPriority;
   }

   public boolean testFloat(float f) {
      return this.floatValue == f;
   }

   public long getLongValue3() {
      return this.longValue3;
   }

   public boolean testPlayerMoveC2SPacket(PlayerMoveC2SPacket playerMoveC2SPacket) {
      return ((IPlayerMoveC2SPacket)(Object)playerMoveC2SPacket).isSkipping();
   }

   public boolean testPacket(Packet<?> packet) {
      return packet instanceof PlayerMoveC2SPacket playerMoveC2SPacket && !this.testPlayerMoveC2SPacket(playerMoveC2SPacket);
   }

   public boolean isFlag() {
      return this.floatValue == 1.0F;
   }

   public boolean isFlag3() {
      return this.flag;
   }

   public long getLongValue5() {
      return this.longValue5;
   }

   public void handleTimerPriority(TimerManager.TimerPriority timerPriority) {
      if (this.timerPriority == null || this.timerPriority.ordinal() <= timerPriority.ordinal()) {
         this.floatValue = 1.0F;
         this.timerPriority = null;
      }
   }

   public enum TimerPriority {
      Low,
      Normal,
      High,
      Max;
   }
}
