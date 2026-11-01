package org.phobos.core.bootstrap;

import java.util.concurrent.ThreadLocalRandom;
import org.phobos.rt.ArrayAccess;
import org.phobos.core.net.packet.ByteTripletPacket240;
import org.phobos.core.net.PacketBus;

public final class BootstrapChallenge {
   public BootstrapChallenge() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static void handlePacketBus(PacketBus packetBus) {
      ThreadLocalRandom var1 = ThreadLocalRandom.current();
      int var2 = var1.nextInt(256);
      byte[] var3 = new byte[64];
      ArrayAccess.st(var3, 0, var2 & 0xFF);
      long var4 = 1L;

      while (true) {
         long var6 = var4;
         long var8 = var6;
         long var10 = var8;
         long var12 = var10;
         if ((int)((int)var4 & 4294967295L) >= 64) {
            ByteTripletPacket240 var81 = new ByteTripletPacket240((byte)89, (byte)100, (byte)-6, var3, "v_2");
            packetBus.handleNetworkPacket2(var81);
            return;
         }

         long var14 = var12;
         long var16 = var14;
         long var18 = var16;
         long var20 = var18;
         long var22 = var20;
         long var24 = var22;
         long var26 = var24;
         long var28 = var26;
         long var30 = var28;
         long var32 = var30;
         long var34 = var32;
         long var36 = var34;
         long var38 = var36;
         int var40 = ArrayAccess.ld(var3, (int)((int)((int)var26 & 4294967295L) + -1 & 4294967295L));
         long var41 = var38;
         long var43 = var41;
         long var45 = var43;
         long var47 = var45;
         long var49 = var47;
         long var51 = var49;
         long var53 = var51;
         long var55 = var53;
         long var57 = var55;
         long var59 = var57;
         long var61 = var59;
         long var63 = var61;
         long var65 = var63;
         long var67 = var65;
         long var69 = var67;
         long var71 = var69;
         long var73 = var71;
         long var75 = var73;
         long var77 = var75;
         long var79 = var77;
         ArrayAccess.st(var3, (int)((int)var18 & 4294967295L) + 0, 11 * (0xFF & (int)(var40 & 0xFF & 0xFF)) + 37 & 0xFF & 0xFF);
         var4 = (int)var79 + 1 & 4294967295L;
      }
   }
}
