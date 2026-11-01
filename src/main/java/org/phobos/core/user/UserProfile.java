package org.phobos.core.user;

import lombok.SneakyThrows;
import java.time.Instant;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.phobos.core.user.Permissions.Permission;
import org.phobos.core.ClientGuard;

public class UserProfile {
   public int intValue;
   public Date date;
   public Permission permission;
   public boolean flag;
   public String text;

   public Date getDate() {
      return this.date;
   }

   public Permission getPermission() {
      return this.permission;
   }

   public String getText() {
      return this.text;
   }

   @NotNull
   @SneakyThrows public static UserProfile resolveUserProfile(String text, long l) {
      Throwable var18;
      label107: {
         if (((l ^ -7255842416701556750L) != 3229078921922912107L ? ((int)(l ^ -7255842416701556750L) <= (int)3229078921922912107L ? -1 : 1) : 0) != 0
            && ((l ^ -1135586879083862883L) != -6196441182681930632L ? ((int)(l ^ -1135586879083862883L) <= (int)-6196441182681930632L ? -1 : 1) : 0) != 0) {
            char[] var4 = new char[28];
            var4[0] = 'C';
            var4[1] = 'r';
            var4[2] = 'e';
            var4[3] = 'a';
            var4[4] = 't';
            var4[5] = 'i';
            var4[6] = 'n';
            var4[7] = 'g';
            var4[8] = ' ';
            var4[9] = 's';
            var4[10] = 'e';
            var4[11] = 's';
            var4[12] = 's';
            var4[13] = 'i';
            var4[14] = 'o';
            var4[15] = 'n';
            var4[16] = ' ';
            var4[17] = 'o';
            var4[18] = 'u';
            var4[19] = 't';
            var4[20] = ' ';
            var4[21] = 'o';
            var4[22] = 'f';
            var4[23] = ' ';
            var4[24] = 's';
            var4[25] = 'y';
            var4[26] = 'n';
            var4[27] = 'c';

            String var3;
            try {
               var3 = new String(var4);
            } catch (Throwable var29) {
               var18 = var29;
               break label107;
            }

            try {
               ClientGuard.handleText(var3);
            } catch (Throwable var28) {
               var18 = var28;
               break label107;
            }

            ClientGuard.stop();
         }

         char[] var6 = new char[2];
         var6[0] = '\\';
         var6[1] = '+';

         String var5;
         try {
            var5 = new String(var6);
         } catch (Throwable var27) {
            var18 = var27;
            break label107;
         }

         String[] var7;
         try {
            var7 = text.split(var5);
         } catch (Throwable var26) {
            var18 = var26;
            break label107;
         }

         Object var8 = var7[0];
         Object var9 = var7[1];
         Object var10 = var7[2];

         int var11;
         try {
            var11 = Integer.parseInt((String)var10);
         } catch (Throwable var25) {
            var18 = var25;
            break label107;
         }

         String var13;
         try {
            var13 = StringUtils.capitalize((String)var8);
         } catch (Throwable var24) {
            var18 = var24;
            break label107;
         }

         Permission var14;
         try {
            var14 = Permission.valueOf(var13);
         } catch (Throwable var23) {
            var18 = var23;
            break label107;
         }

         Instant var15;
         try {
            var15 = Instant.parse((CharSequence)var9);
         } catch (Throwable var22) {
            var18 = var22;
            break label107;
         }

         Date var16;
         try {
            var16 = Date.from(var15);
         } catch (Throwable var21) {
            var18 = var21;
            break label107;
         }

         try {
            return new UserProfile(var14, var16, var11, -7414912244269932525L);
         } catch (Throwable var20) {
            var18 = var20;
         }
      }

      boolean var19 = true;
      if (!var19) {
         throw var18;
      }

      ClientGuard.stop();
      return null;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public UserProfile(Permission permission, Date date, int i, long l) {
      this.nativeInit(permission, date, i, l);
   }

   public void nativeInit(Permission var1, Date var2, int var3, long var4) {
      this.text = "https://i.imgur.com/aAD1vDb.jpeg";
      this.permission = var1;
      this.date = var2;
      this.intValue = var3;
      Instant var6 = Instant.now();
      Date var7 = Date.from(var6);
      boolean var8 = var2.before(var7);
      this.flag = var8;
   }

   public void setText(String text) {
      this.text = text;
   }

   public boolean isFlag() {
      return this.flag;
   }
}
