package org.phobos.core.user;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.phobos.core.user.Permissions.Permission;
import org.phobos.core.ClientGuard;

public class UserSession {
   public static UserSession userSession = new UserSession();
   public int intValue;
   public boolean flag;
   public String text;
   public static boolean flag2 = false;
   public String text2;
   public static boolean flag3 = false;
   public long longValue;
   public long longValue2 = 0L;
   public String text3;
   public final List<Runnable> runnableList;
   public UserProfile userProfile;
   private static Object[] phobos$g1806bb9a8;

   public long getLongValue2() {
      return this.longValue2;
   }

   public String describe() {
      return this.userProfile.getPermission().getFormatting() + this.text;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public UserProfile getUserProfile() {
      return this.userProfile;
   }

   public static long[] values(Date v0) {
      Instant var1 = Instant.now();
      Date var2 = Date.from(var1);
      long var3 = v0.getTime();
      long var5 = var2.getTime();
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
      return var25;
   }

   public UserSession() {
      this.longValue = 0L;
      this.flag = true;
      this.runnableList = new ArrayList<>();
   }

   public int getIntValue() {
      return this.intValue;
   }

   public String getText3() {
      return this.text3;
   }

   public List<Runnable> getRunnableList() {
      return this.runnableList;
   }

   public void run() {
      long var1 = this.longValue2;
      if ((var1 != 915458012576718010L ? ((int)var1 <= (int)915458012576718010L ? -1 : 1) : 0) != 0) {
         char[] var4 = new char[16];
         var4[0] = 'L';
         var4[1] = 'o';
         var4[2] = 'c';
         var4[3] = 'k';
         var4[4] = ' ';
         var4[5] = 'o';
         var4[6] = 'u';
         var4[7] = 't';
         var4[8] = ' ';
         var4[9] = 'o';
         var4[10] = 'f';
         var4[11] = ' ';
         var4[12] = 's';
         var4[13] = 'y';
         var4[14] = 'n';
         var4[15] = 'c';
         String var3 = new String(var4);
         ClientGuard.handleText(var3);
         ClientGuard.stop();
      }

      this.longValue2 = 0L;
      this.flag = true & true;
      List var5 = this.runnableList;
      Consumer<Runnable> var22 = Runnable::run;
      var5.forEach(var22);
   }

   public String getText() {
      return this.text;
   }

   public void run2() {
      this.flag = false & true;
      this.longValue2 = 915458012576718010L;
   }

   public static boolean check() {
      UserSession var0 = userSession;
      long var1 = var0.longValue;
      long var7;
      if ((var1 != -2097208918008357666L ? ((int)var1 <= (int)-2097208918008357666L ? -1 : 1) : 0) == 0) {
         UserSession var3 = userSession;
         UserProfile var4 = var3.userProfile;
         Permission var5 = var4.getPermission();
         int var6 = (var5.isAdmin()) ? 1 : 0;
         var7 = var6 & 0xFF & 0xFF;
      } else {
         char[] var10 = new char[18];
         var10[0] = 'I';
         var10[1] = 'n';
         var10[2] = 'v';
         var10[3] = 'a';
         var10[4] = 'l';
         var10[5] = 'i';
         var10[6] = 'd';
         var10[7] = ' ';
         var10[8] = 'a';
         var10[9] = 'c';
         var10[10] = 'c';
         var10[11] = 'e';
         var10[12] = 's';
         var10[13] = 's';
         var10[14] = ' ';
         var10[15] = 'k';
         var10[16] = 'e';
         var10[17] = 'y';
         String var9 = new String(var10);
         ClientGuard.handleText(var9);
         var7 = 0L;
      }

      return (((int)((int)((int)var7 & 4294967295L) & 0xFF & 0xFF)) != 0);
   }

   public void setText3(String text) {
      this.text3 = text;
   }

   public static void handleText(String v0) {
      char[] var2 = new char[32];
      var2[0] = '5';
      var2[1] = 'D';
      var2[2] = 'w';
      var2[3] = 'k';
      var2[4] = 'h';
      var2[5] = 'j';
      var2[6] = '1';
      var2[7] = 'y';
      var2[8] = 'K';
      var2[9] = 'N';
      var2[10] = 'C';
      var2[11] = 'v';
      var2[12] = 'E';
      var2[13] = 'd';
      var2[14] = 'x';
      var2[15] = 'Q';
      var2[16] = 'J';
      var2[17] = 'T';
      var2[18] = '7';
      var2[19] = 'g';
      var2[20] = '2';
      var2[21] = 'P';
      var2[22] = 'W';
      var2[23] = 'B';
      var2[24] = 'N';
      var2[25] = 'F';
      var2[26] = 'n';
      var2[27] = 'R';
      var2[28] = 'k';
      var2[29] = 't';
      var2[30] = 'w';
      var2[31] = 'j';
      String var1 = new String(var2);
      boolean var3 = v0.equals(var1);
      if (!var3) {
         char[] var5 = new char[36];
         var5[0] = 'I';
         var5[1] = 'n';
         var5[2] = 'v';
         var5[3] = 'a';
         var5[4] = 'l';
         var5[5] = 'i';
         var5[6] = 'd';
         var5[7] = ' ';
         var5[8] = 'i';
         var5[9] = 'n';
         var5[10] = 'i';
         var5[11] = 't';
         var5[12] = ' ';
         var5[13] = 'k';
         var5[14] = 'e';
         var5[15] = 'y';
         var5[16] = ' ';
         var5[17] = 'f';
         var5[18] = 'o';
         var5[19] = 'r';
         var5[20] = ' ';
         var5[21] = 's';
         var5[22] = 'e';
         var5[23] = 's';
         var5[24] = 's';
         var5[25] = 'i';
         var5[26] = 'o';
         var5[27] = 'n';
         var5[28] = ' ';
         var5[29] = 'h';
         var5[30] = 'a';
         var5[31] = 'n';
         var5[32] = 'd';
         var5[33] = 'l';
         var5[34] = 'e';
         var5[35] = 'r';
         String var4 = new String(var5);
         ClientGuard.handleText(var4);
         ClientGuard.stop();
      }

      UserSession var6 = userSession;
      var6.text2 = v0;
      UserSession var7 = userSession;
      var7.intValue = 2147483607;
      UserSession var8 = userSession;
      var8.longValue = -2097208918008357666L;
   }

   public static boolean check2() {
      UserSession var0 = userSession;
      long var1 = var0.longValue;
      long var7;
      if ((var1 != -2097208918008357666L ? ((int)var1 <= (int)-2097208918008357666L ? -1 : 1) : 0) == 0) {
         UserSession var3 = userSession;
         UserProfile var4 = var3.userProfile;
         Permission var5 = var4.getPermission();
         int var6 = (var5.isBeta()) ? 1 : 0;
         var7 = var6 & 0xFF & 0xFF;
      } else {
         char[] var10 = new char[18];
         var10[0] = 'I';
         var10[1] = 'n';
         var10[2] = 'v';
         var10[3] = 'a';
         var10[4] = 'l';
         var10[5] = 'i';
         var10[6] = 'd';
         var10[7] = ' ';
         var10[8] = 'a';
         var10[9] = 'c';
         var10[10] = 'c';
         var10[11] = 'e';
         var10[12] = 's';
         var10[13] = 's';
         var10[14] = ' ';
         var10[15] = 'k';
         var10[16] = 'e';
         var10[17] = 'y';
         String var9 = new String(var10);
         ClientGuard.handleText(var9);
         var7 = 0L;
      }

      return (((int)((int)((int)var7 & 4294967295L) & 0xFF & 0xFF)) != 0);
   }

   public static String describe2() {
      UserSession var0 = userSession;
      int var1 = var0.intValue;
      if (var1 != 2147483607) {
         ClientGuard.stop();
      }

      UserSession var2 = userSession;
      return var2.text2;
   }

   public String getText2() {
      return this.text2;
   }

   public void handleRunnable(Runnable var1) {
      boolean var2 = this.flag;
      if (!var2) {
         List var3 = this.runnableList;
         var3.add(var1);
      } else {
         var1.run();
      }
   }

   public static String describe3() {
      return userSession.text3;
   }

   public void handleUserProfile(UserProfile var1, long var2) {
      boolean var4 = this.flag;
      if (var4) {
         ClientGuard.handleText("Setting session after handle is locked");
         ClientGuard.stop();
      }

      if ((var2 != 9025803917011585253L ? ((int)var2 <= (int)9025803917011585253L ? -1 : 1) : 0) != 0) {
         char[] var6 = new char[32];
         var6[0] = 'S';
         var6[1] = 'e';
         var6[2] = 't';
         var6[3] = 't';
         var6[4] = 'i';
         var6[5] = 'n';
         var6[6] = 'g';
         var6[7] = ' ';
         var6[8] = 's';
         var6[9] = 'e';
         var6[10] = 's';
         var6[11] = 's';
         var6[12] = 'i';
         var6[13] = 'o';
         var6[14] = 'n';
         var6[15] = ' ';
         var6[16] = 'w';
         var6[17] = 'i';
         var6[18] = 't';
         var6[19] = 'h';
         var6[20] = ' ';
         var6[21] = 'i';
         var6[22] = 'n';
         var6[23] = 'v';
         var6[24] = 'a';
         var6[25] = 'l';
         var6[26] = 'i';
         var6[27] = 'd';
         var6[28] = ' ';
         var6[29] = 'k';
         var6[30] = 'e';
         var6[31] = 'y';
         String var5 = new String(var6);
         ClientGuard.handleText(var5);
         ClientGuard.stop();
      }

      this.userProfile = var1;
      Permission var7 = var1.getPermission();
      boolean var8 = var7.isBeta();
      flag3 = var8;
      Permission var9 = var1.getPermission();
      boolean var10 = var9.isAdmin();
      flag2 = var10;
   }

   public long getLongValue() {
      return this.longValue;
   }

   public void handleText2(String var1, long var2) {
      boolean var4 = this.flag;
      if (var4) {
         ClientGuard.handleText("Setting username after handle is locked");
         ClientGuard.stop();
      }

      if ((var2 != -777384089782239851L ? ((int)var2 <= (int)-777384089782239851L ? -1 : 1) : 0) != 0) {
         char[] var6 = new char[33];
         var6[0] = 'S';
         var6[1] = 'e';
         var6[2] = 't';
         var6[3] = 't';
         var6[4] = 'i';
         var6[5] = 'n';
         var6[6] = 'g';
         var6[7] = ' ';
         var6[8] = 'u';
         var6[9] = 's';
         var6[10] = 'e';
         var6[11] = 'r';
         var6[12] = 'n';
         var6[13] = 'a';
         var6[14] = 'm';
         var6[15] = 'e';
         var6[16] = ' ';
         var6[17] = 'w';
         var6[18] = 'i';
         var6[19] = 't';
         var6[20] = 'h';
         var6[21] = ' ';
         var6[22] = 'i';
         var6[23] = 'n';
         var6[24] = 'v';
         var6[25] = 'a';
         var6[26] = 'l';
         var6[27] = 'i';
         var6[28] = 'd';
         var6[29] = ' ';
         var6[30] = 'k';
         var6[31] = 'e';
         var6[32] = 'y';
         String var5 = new String(var6);
         ClientGuard.handleText(var5);
         ClientGuard.stop();
      }

      this.text = var1;
   }
}
