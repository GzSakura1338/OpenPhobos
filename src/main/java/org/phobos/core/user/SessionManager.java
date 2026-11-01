package org.phobos.core.user;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.phobos.api.TimeGate.Normal;
import org.phobos.core.user.Permissions.Permission;
import org.phobos.event.Listener;
import org.phobos.core.ClientGuard;
import org.phobos.core.ClientHolder;
import org.phobos.core.Feature;
import org.phobos.core.PhobosClient;
import org.phobos.core.tick.TickCounter;

public class SessionManager extends Feature {
   public List<String> textList;
   public Map<String, String> textMap;
   public Normal normal;
   public Normal normal2;
   public ExecutorService executorService;
   public Normal normal3;
   public Map<String, String> textMap2;

   public void startOffline(PhobosClient phobosClient) {
      UserSession session = UserSession.userSession;
      session.run2();
      UserSession.handleText("5Dwkhj1yKNCvEdxQJT7g2PWBNFnRktwj");
      Date expiry = Date.from(Instant.now().plus(36500L, ChronoUnit.DAYS));
      session.handleUserProfile(new UserProfile(Permission.Beta, expiry, 67, -7414912244269932525L), 9025803917011585253L);
      session.handleText2("Offline", -777384089782239851L);
      session.run();
      this.normal3.run();
      this.normal.run();
      phobosClient.run4();
   }

   public Map<String, String> getTextMap() {
      return this.textMap;
   }

   public Normal getNormal3() {
      return this.normal3;
   }

   public Map<String, String> getTextMap2() {
      return this.textMap2;
   }

   public Normal getNormal() {
      return this.normal2;
   }

   public ExecutorService getExecutorService() {
      return this.executorService;
   }

   public Normal getNormal2() {
      return this.normal;
   }

   public List<String> getTextList() {
      return this.textList;
   }

   public void run2() {
      UserSession var1 = UserSession.userSession;
      UserProfile var2 = var1.getUserProfile();
      if (var2 != null) {
         UserSession var3 = UserSession.userSession;
         UserProfile var4 = var3.getUserProfile();
         Date var5 = var4.getDate();
         long[] var6 = UserSession.values(var5);
         long var7 = var6[0];
         long var9 = var6[1];
         long var11 = var6[2];
         long var13;
         if (var7 > 0L) {
            var13 = 1L;
         } else {
            var13 = (var7 == 0L ? 0 : -1) & 4294967295L;
         }

         if ((int)var13 <= 0) {
            long var15;
            if (var9 > 0L) {
               var15 = 1L;
            } else {
               var15 = (var9 == 0L ? 0 : -1) & 4294967295L;
            }

            if ((int)var15 <= 0) {
               long var17;
               if (var11 <= 0L) {
                  var17 = (var11 == 0L ? 0 : -1) & 4294967295L;
               } else {
                  var17 = 1L;
               }

               if ((int)((int)var17 & 4294967295L) <= 0) {
                  Runtime var19 = Runtime.getRuntime();
                  var19.exit(0);
               }
            }
         }
      } else {
         ClientGuard.handleText("Time left checking failed");
         ClientGuard.stop();
      }
   }

   public void run3() {
      PhobosClient var1 = ClientHolder.getPhobosClient();
      UserTagManager var2 = var1.getUserTagManager();
      var2.run();
      Normal var3 = this.normal;
      var3.run();
   }

   public SessionManager(PhobosClient phobosClient) {
      this.nativeInit(phobosClient);
   }

   public void nativeInit(final PhobosClient var1) {
      ExecutorService var2 = Executors.newFixedThreadPool(4);
      this.executorService = var2;
      LinkedHashMap var3 = new LinkedHashMap(0);
      this.textMap = var3;
      LinkedHashMap var4 = new LinkedHashMap(0);
      this.textMap2 = var4;
      ArrayList var5 = new ArrayList(0);
      this.textList = var5;
      Normal var6 = new Normal();
      this.normal2 = var6;
      Normal var7 = new Normal();
      this.normal = var7;
      Normal var8 = new Normal();
      this.normal3 = var8;
      var var9 = new Listener<TickCounter>() {
         public void handleObject2(TickCounter tickCounter) {
            if (SessionManager.this.normal3.testLong2(30000L)) {
               SessionManager.this.run2();
            }
         }
      };
      this.handleListener(var9);
   }

   public void run5() {
      Normal var1 = this.normal3;
      var1.run();
   }
}
