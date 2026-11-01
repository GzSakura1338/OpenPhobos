package org.phobos.core.bootstrap;

import lombok.SneakyThrows;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import org.phobos.api.BootstrapRemote;
import org.phobos.api.ClientInitializer;
import org.phobos.core.config.BuildInfo.Type;
import org.phobos.core.config.BuildInfo;
import org.phobos.core.PhobosLog;

public class BootstrapService extends UnicastRemoteObject implements BootstrapRemote {
   public final Registry registry;

   @SneakyThrows public static void run() {
      label24: {
         try {
            Registry var0 = LocateRegistry.createRegistry(1099);
            BootstrapService var1 = new BootstrapService(var0);
            var0.rebind("Bootstrap", var1);
         } catch (Throwable var4) {
            boolean var3 = true;
            if (var3) {
               PhobosLog.handleText("Loader", "Failed to initialize the client");
               break label24;
            }

            throw var4;
         }

         outlined_1804d0780(BootstrapService.class, 0L, 0L);
      }
   }

   public BootstrapService(Registry registry) throws RemoteException {
      this.registry = registry;
   }

   @Override
   @SneakyThrows public void handleClientInitializer(ClientInitializer clientInitializer, long l) throws RemoteException {
      if ((l != 234043877857195293L ? ((int)l <= (int)234043877857195293L ? -1 : 1) : 0) == 0) {
         long[] var4 = new long[6];
         var4[0] = 2224961381767398078L;
         var4[1] = 5819110724022498049L;
         var4[2] = 4967941764311155865L;
         var4[3] = 1639064478920766944L;
         var4[4] = -6810938074878119118L;
         var4[5] = 3516185207075139484L;
         clientInitializer.handleLong(var4);
         BootstrapPing.setFlag(true & true);

         Registry var5;
         try {
            var5 = LocateRegistry.getRegistry(1099);
         } catch (Throwable var12) {
            boolean var8 = true;
            if (var8) {
               return;
            }

            throw var12;
         }

         try {
            var5.unbind("Bootstrap");
         } catch (Throwable var11) {
            boolean var13 = true;
            if (var13) {
               return;
            }

            throw var11;
         }

         try {
            UnicastRemoteObject.unexportObject(this, true & true);
         } catch (Throwable var10) {
            boolean var14 = true;
            if (var14) {
               return;
            }

            throw var10;
         }

         Registry var6 = this.registry;

         try {
            UnicastRemoteObject.unexportObject(var6, true & true);
         } catch (Throwable var9) {
            boolean var15 = true;
            if (!var15) {
               throw var9;
            }
         }
      }
   }

   public static Object outlined_1804cb890(Object var0, Object var1) {
      Instant var2 = Instant.now();
      long var3 = var2.toEpochMilli();
      long var5 = ((Instant)var1).toEpochMilli();
      TimeUnit var7 = TimeUnit.MILLISECONDS;
      long var8 = var7.toDays(var3 - var5);
      TimeUnit var10 = TimeUnit.MILLISECONDS;
      long var11 = var10.toHours(var3 - var5);
      TimeUnit var13 = TimeUnit.DAYS;
      long var14 = var13.toHours(var8);
      TimeUnit var16 = TimeUnit.MILLISECONDS;
      long var17 = var16.toMinutes(var3 - var5);
      TimeUnit var19 = TimeUnit.HOURS;
      TimeUnit var20 = TimeUnit.MILLISECONDS;
      long var21 = var20.toHours(var3 - var5);
      long var23 = var19.toMinutes(var21);
      long[] var25 = new long[3];
      var25[0] = var8;
      var25[1] = var11 - var14;
      var25[2] = var17 - var23;
      long var26 = var25[0];
      long var28;
      if (var26 > 0L) {
         var28 = 1L;
      } else {
         var28 = (var26 == 0L ? 0 : -1) & 4294967295L;
      }

      String var49;
      Object[] var50;
      if ((int)var28 <= 0) {
         long var30 = var25[1];
         long var32;
         if (var30 > 0L) {
            var32 = 1L;
         } else {
            var32 = (var30 == 0L ? 0 : -1) & 4294967295L;
         }

         if ((int)var32 <= 0) {
            long var34 = var25[2];
            Object[] var36 = new Object[2];
            Long var37 = var34;
            var36[0] = var37;
            long var38;
            if (var34 <= 1L) {
               var38 = (var34 + -1L == 0L ? 0 : -1) & 4294967295L;
            } else {
               var38 = 1L;
            }

            String var48;
            if ((int)((int)var38 & 4294967295L) != 0) {
               var48 = "s";
            } else {
               var48 = "";
            }

            var36[1] = var48;
            var49 = "%s minute%s ago";
            var50 = var36;
         } else {
            Object[] var44 = new Object[2];
            Long var45 = var30;
            var44[0] = var45;
            long var46;
            if (var30 <= 1L) {
               var46 = (var30 + -1L == 0L ? 0 : -1) & 4294967295L;
            } else {
               var46 = 1L;
            }

            String var53;
            if ((int)((int)var46 & 4294967295L) != 0) {
               var53 = "s";
            } else {
               var53 = "";
            }

            var44[1] = var53;
            var49 = "%s hour%s ago";
            var50 = var44;
         }
      } else {
         Object[] var40 = new Object[2];
         Long var41 = var26;
         var40[0] = var41;
         long var42;
         if (var26 <= 1L) {
            var42 = (var26 + -1L == 0L ? 0 : -1) & 4294967295L;
         } else {
            var42 = 1L;
         }

         String var52;
         if ((int)((int)var42 & 4294967295L) != 0) {
            var52 = "s";
         } else {
            var52 = "";
         }

         var40[1] = var52;
         var49 = "%s day%s ago";
         var50 = var40;
      }

      return var49.formatted(var50);
   }

   public static void outlined_1804d0780(Object var0, long var1, long var3) {
      PrintStream var5 = System.out;
      var5.println(" ");
      PrintStream var6 = System.out;
      var6.println(" ");
      PrintStream var7 = System.out;
      var7.println(" ");
      BuildInfo var8 = BuildInfo.getINSTANCE();
      String var9 = var8.describe2();
      Instant var10 = Instant.parse(var9);
      PrintStream var11 = System.out;
      Object[] var12 = new Object[1];
      String var13 = var8.describe3();
      var12[0] = var13;
      var11.printf("Running Phobos v%s%n", var12);
      PrintStream var14 = System.out;
      Object[] var15 = new Object[1];
      Object var16 = outlined_1804cb890(null, var10);
      var15[0] = var16;
      var14.printf("Built %s%n", var15);
      PrintStream var17 = System.out;
      Object[] var18 = new Object[1];
      String var19 = var8.describe();
      var18[0] = var19;
      var17.printf("Git: %s%n", var18);
      PrintStream var20 = System.out;
      Object[] var21 = new Object[1];
      Type var22 = var8.getType();
      var21[0] = var22;
      var20.printf("Build: %s", var21);
      PrintStream var23 = System.out;
      var23.println(" ");
      PrintStream var24 = System.out;
      var24.println(" ");
      PrintStream var25 = System.out;
      var25.println(" ");
   }
}
