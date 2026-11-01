package org.phobos.core.server;

import java.util.Set;
import net.minecraft.network.packet.c2s.handshake.ConnectionIntent;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import org.jetbrains.annotations.Unmodifiable;
import org.phobos.api.TimeGate;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.core.event.events.PacketEvent.Send;
import org.phobos.event.Listener;
import org.phobos.core.Feature;

public class ServerDetector extends Feature {
   public static final String text = "ovh.crystalpvp.cc";
   public boolean flag;
   public static final @Unmodifiable Set<String> textSet = Set.of("grim.crystalpvp.cc", "anarchy.crystalpvp.cc");
   public static final String text2 = "crystalpvp.cc";
   public static final String text3 = "anarchy.crystalpvp.cc";
   public static final @Unmodifiable Set<String> textSet2 = Set.of(
      "crystalpvp.cc", "ovh.crystalpvp.cc", "grim.crystalpvp.cc", "us.crystalpvp.cc", "anarchy.crystalpvp.cc"
   );
   public static final String text4 = "grim.crystalpvp.cc";
   public boolean flag2;
   public static final String text5 = "us.crystalpvp.cc";
   public final TimeGate.Normal normal = new TimeGate.Normal();

   public boolean isFlag2() {
      return this.flag2;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public long value() {
      return this.normal.value();
   }

   public ServerDetector() {
      this.handleListener(new Listener<Send<HandshakeC2SPacket>>(Integer.MAX_VALUE) {
         public void handleObject2(Send<HandshakeC2SPacket> send) {
            if (((HandshakeC2SPacket)send.getPacket()).intendedState().equals(ConnectionIntent.LOGIN)) {
               ServerDetector.this.flag2 = ServerDetector.textSet2.contains(((HandshakeC2SPacket)send.getPacket()).address());
               ServerDetector.this.flag = ServerDetector.textSet.contains(((HandshakeC2SPacket)send.getPacket()).address());
            }
         }
      });
      this.handleListener(new Listener<Receive<?>>() {
         public void handleObject2(Receive<?> receive) {
            ServerDetector.this.normal.run();
         }
      });
   }
}
