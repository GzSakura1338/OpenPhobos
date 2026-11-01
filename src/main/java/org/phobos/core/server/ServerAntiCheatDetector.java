package org.phobos.core.server;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.s2c.common.CommonPingS2CPacket;
import net.minecraft.text.Text;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.core.event.events.PacketEvent.Send;
import org.phobos.event.DisconnectEvent2;
import org.phobos.event.Listener;
import org.phobos.event.SafeListener;
import org.phobos.module.client.NotificationsModule;
import org.phobos.core.ClientGuard;
import org.phobos.core.Feature;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.tick.TickUtils;
import org.phobos.core.user.UserSession;

public class ServerAntiCheatDetector extends Feature {
   public String text;
   public final Map<Integer, Integer> integerMap = new HashMap<>();
   public final AtomicInteger atomicInteger;
   public boolean flag;
   public final MutableBoolean mutableBoolean;
   public final IntList intList;
   public int intValue = 0;

   public ServerAntiCheatDetector() {
      this.flag = true;
      this.intList = new IntArrayList();
      this.mutableBoolean = new MutableBoolean();
      this.text = "Unknown";
      this.atomicInteger = new AtomicInteger();
      this.handleListener(new Listener<Send<HandshakeC2SPacket>>() {
         public void handleObject2(Send<HandshakeC2SPacket> send) {
            ServerAntiCheatDetector.this.run2();
            ServerAntiCheatDetector.this.intList.clear();
         }
      });
      this.handleListener(
         new SafeListener<Receive<CommonPingS2CPacket>>() {
            public void handleObject(
               Receive<CommonPingS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               int i = receive.getPacket().getParameter();
               if (ServerAntiCheatDetector.this.mutableBoolean.getValue()) {
                  ServerAntiCheatDetector.this.intList.add(i);
                  if (ServerAntiCheatDetector.this.intList.size() >= 10) {
                     NotificationsModule.getNotificationsModule()
                        .handleTriConsumer(
                           (color, color2, color3) -> {
                              StringBuilder stringBuilder = new StringBuilder();

                              for (int ix : ServerAntiCheatDetector.this.intList) {
                                 stringBuilder.append(ix).append(", ");
                              }

                              String text = stringBuilder.toString();
                              Text text2 = Text.literal("Transactions: ")
                                 .withColor(color2.hashCode())
                                 .append(Text.literal(text.substring(0, text.length() - 2)).withColor(color3.hashCode()));
                              NotificationsModule.getNotificationsModule().handleText5(text2);
                           }
                        );
                     ServerAntiCheatDetector.this.mutableBoolean.setFalse();
                  }
               }

               if (ServerAntiCheatDetector.this.integerMap.size() > 10) {
                  if (ServerAntiCheatDetector.this.flag) {
                     ServerAntiCheatDetector.this.run();
                  }

                  ServerAntiCheatDetector.this.flag = false;
               } else {
                  ServerAntiCheatDetector.this.integerMap.put(ServerAntiCheatDetector.this.intValue, i);
                  ServerAntiCheatDetector.this.intValue++;
               }
            }
         }
      );
      this.handleListener(new Listener<Endpoint>() {
         public void handleObject2(Endpoint endpoint) {
            ServerAntiCheatDetector.this.mutableBoolean.setTrue();
         }
      });
      this.handleListener(
         new SafeListener<DisconnectEvent2>() {
            public void handleObject(
               DisconnectEvent2 disconnectEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               ServerAntiCheatDetector.this.text = "Unknown";
               ServerAntiCheatDetector.this.integerMap.clear();
               ServerAntiCheatDetector.this.intValue = 0;
               ServerAntiCheatDetector.this.flag = true;
            }
         }
      );
      this.handleListener(
         new SafeListener<Send<?>>(Integer.MIN_VALUE) {
            public void handleObject(
               Send<?> send, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!send.isCancelled()) {
                  switch (send.getPacket()) {
                     case PlayerInteractBlockC2SPacket playerInteractBlockC2SPacket:
                        ServerAntiCheatDetector.this.atomicInteger.set(playerInteractBlockC2SPacket.getSequence());
                        break;
                     case PlayerInteractItemC2SPacket playerInteractItemC2SPacket:
                        ServerAntiCheatDetector.this.atomicInteger.set(playerInteractItemC2SPacket.getSequence());
                        break;
                     case PlayerActionC2SPacket playerActionC2SPacket:
                        if (playerActionC2SPacket.getSequence() == 0) {
                           return;
                        }

                        ServerAntiCheatDetector.this.atomicInteger.set(playerActionC2SPacket.getSequence());
                        return;
                     default:
                  }
               }
            }
         }
      );
      ModuleListenerUtils.handleListenerHolder4(this, () -> this.atomicInteger.set(0));
   }

   public void run() {
      Map var1 = this.integerMap;
      boolean var2 = var1.isEmpty();
      if (!var2) {
         boolean var3 = this.testInt(-1, -2, -3);
         String var13;
         if (!var3) {
            boolean var4 = this.testInt(32768, 32769, 32770);
            if (!var4) {
               boolean var5 = this.testInt(34769, 34770, 34771);
               label46:
               if (!var5) {
                  boolean var6 = this.testInt(34769, 34770, 39769);
                  if (!var6) {
                     boolean var7 = this.testInt(41769, 41770, 41771);
                     if (!var7) {
                        boolean var8 = this.testInt(62536, 62535, 62534);
                        if (!var8) {
                           int var9 = this.computeInt(0);
                           int var10 = this.computeInt(1);
                           int var11 = this.computeInt(3);
                           String var12;
                           if (var9 + -1 != var10 && var9 + 1 != var10 && var9 < var10 && var10 + -1 == var11) {
                              var12 = "Polar";
                           } else {
                              boolean var14 = this.testInt2(0, 57249);
                              if (!var14 || var10 + -1 != var11) {
                                 return;
                              }

                              var12 = "Errata";
                           }

                           this.setText(var12);
                           return;
                        }

                        var13 = "Karhu";
                        break label46;
                     }
                  }

                  var13 = "Vulcan";
               } else {
                  var13 = "Verus";
               }
            } else {
               var13 = "Intave";
            }
         } else {
            var13 = "Grim";
         }

         this.setText(var13);
      }
   }

   public void run2() {
      String var1 = UserSession.describe2();
      boolean var2 = "5Dwkhj1yKNCvEdxQJT7g2PWBNFnRktwj".equals(var1);
      if (!var2) {
         ClientGuard.stop();
      }
   }

   public void handleConsumer(Consumer<Integer> consumer) {
      consumer.accept(this.atomicInteger.get() + 1);
      TickUtils.run();
   }

   public boolean testInt(int i, int i2, int i3) {
      Map var4 = this.integerMap;
      Integer var5 = 0;
      Object var6 = var4.get(var5);
      boolean var7 = var6 instanceof Integer;
      if (var6 != null && !var7) {
         throw new ClassCastException("CHECKCAST failed");
      }

      int var8 = (Integer)var6;
      if (var8 == i) {
         Map var9 = this.integerMap;
         Integer var10 = 1;
         Object var11 = var9.get(var10);
         boolean var12 = var11 instanceof Integer;
         if (var11 != null && !var12) {
            throw new ClassCastException("CHECKCAST failed");
         }

         int var14 = (Integer)var11;
         if (var14 == i2) {
            Map var15 = this.integerMap;
            Integer var16 = 2;
            Object var17 = var15.get(var16);
            boolean var18 = var17 instanceof Integer;
            if (var17 != null && !var18) {
               throw new ClassCastException("CHECKCAST failed");
            }

            int var19 = (Integer)var17;
            if (var19 == i3) {
               int var13 = 1;
               return (((int)((int)(var13 & 4294967295L) & 0xFF & 0xFF)) != 0);
            }
         }
      }

      int var13 = 0;
      return (((int)((int)(var13 & 4294967295L) & 0xFF & 0xFF)) != 0);
   }

   public String getText() {
      return this.text;
   }

   public boolean testInt2(int i, int i2) {
      return this.integerMap.get(i) == i2;
   }

   public AtomicInteger getAtomicInteger() {
      return this.atomicInteger;
   }

   public int computeInt(int i) {
      return this.integerMap.get(i);
   }

   public void setText(String text) {
      this.text = text;
   }
}
