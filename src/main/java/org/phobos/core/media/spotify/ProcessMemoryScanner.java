package org.phobos.core.media.spotify;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.ptr.IntByReference;
import java.util.Map;
import java.util.Map;
import org.phobos.api.Win32Process;
import org.phobos.api.PsapiLibrary;
import org.phobos.api.Kernel32Library;

public class ProcessMemoryScanner implements Win32Process {
   public final HANDLE hANDLE;
   public final HWND hWND;
   public static final Gson gson = new Gson();
   public final int intValue;
   public long longValue = 10000L;

   public int computeLong(long l) {
      byte[] bytes = this.values(l, 4);
      return bytes[0] & 0xFF | (bytes[1] & 0xFF) << 8 | (bytes[2] & 0xFF) << 16 | (bytes[3] & 0xFF) << 24;
   }

   public long computeLong2(long l, long l2, byte[] bytes) {
      long l5 = System.currentTimeMillis();
      int i3 = 65536;

      for (long l4 = l; l4 < l2; l4 += i3) {
         byte[] bytes2 = this.values(l4, i3 + bytes.length);

         for (int i2 = 0; i2 < bytes2.length - bytes.length; i2++) {
            boolean flag = true;

            for (int i = 0; i < bytes.length; i++) {
               if (bytes2[i2 + i] != bytes[i]) {
                  flag = false;
                  break;
               }
            }

            if (flag) {
               return l4 + i2;
            }
         }

         long l3 = System.currentTimeMillis() - l5;
         if (l3 > this.longValue) {
            throw new IllegalStateException("Scan timeout of " + this.longValue + "ms reached at address " + l4);
         }
      }

      return -1L;
   }

   public byte computeLong3(long l) {
      return this.values(l, 1)[0];
   }

   public ProcessMemoryScanner(String text) {
      this.intValue = this.computeText2(text);
      if (this.intValue == -1) {
         throw new IllegalStateException("Process of executable " + text + " not found");
      }

      this.hANDLE = this.resolveHANDLE(this.intValue);
      if (this.hANDLE == null) {
         throw new IllegalStateException("Process handle of " + this.intValue + " not found");
      }

      this.hWND = this.resolveHWND2(this.intValue, hWND -> {
         String textx = this.describeHWND(hWND);
         return !textx.equals("Spotify Debug Window") && !textx.equals("DevTools");
      });
      if (this.describe().isEmpty()) {
         throw new IllegalStateException("Window for process " + this.intValue + " not found");
      }
   }

   public boolean testLong(long l, int... ints) {
      byte[] bytes = this.values(l, ints.length);

      for (int i = 0; i < bytes.length; i++) {
         if (bytes[i] != (byte)ints[i]) {
            return false;
         }
      }

      return true;
   }

   public long computeLong4(long l, long l2, String text, ProcessMemoryScanner.SearchCondition searchCondition) {
      return this.computeLong6(l, l2, text.getBytes(), searchCondition);
   }

   public String createText(long l, int i) {
      return new String(this.values(l, i));
   }

   public boolean testLong2(long l, String... texts) {
      for (String text : texts) {
         if (this.testLong5(l, text)) {
            return true;
         }
      }

      return false;
   }

   public byte[] values(long l, int i) {
      Kernel32Library kernel32Library = Kernel32Library.INSTANCE;
      Memory memory = new Memory(i);
      kernel32Library.testHANDLE3(this.hANDLE, new Pointer(l), memory, i, new IntByReference(i));
      return memory.getByteArray(0L, i);
   }

   public long computeLong5(long l, String text, int i) {
      return this.computeLong7(l, text, (lx, i2) -> i2 == i);
   }

   public long computeLong6(long l, long l2, byte[] bytes, ProcessMemoryScanner.SearchCondition searchCondition) {
      long l4 = l;

      for (int i = 0; l4 < l2; i++) {
         long l3 = this.computeLong2(l4, l2, bytes);
         if (l3 == -1L || searchCondition.testLong(l3, i)) {
            return l3;
         }

         l4 = l3 + 1L;
      }

      return -1L;
   }

   public long computeLong7(long l, String text, ProcessMemoryScanner.SearchCondition searchCondition) {
      return this.computeLong4(l, Long.MAX_VALUE, text, searchCondition);
   }

   public long computeText(String text, String text2) {
      PsapiLibrary.ModuleInfo moduleInfo = this.resolveModuleInfo(text);
      return moduleInfo == null ? -1L : this.computeLong5(moduleInfo.value2(), text2, 0);
   }

   public boolean testLong3(long l, byte[] bytes) {
      byte[] bytes2 = this.values(l, bytes.length);

      for (int i = 0; i < bytes2.length; i++) {
         if (bytes2[i] != bytes[i]) {
            return false;
         }
      }

      return true;
   }

   public boolean testLong4(long l, byte[]... bytes) {
      for (byte[] bytes2 : bytes) {
         if (this.testLong3(l, bytes2)) {
            return true;
         }
      }

      return false;
   }

   public boolean testLong5(long l, String text) {
      return this.testLong3(l, text.getBytes());
   }

   public boolean testLong6(long l) {
      return this.computeLong3(l) == 1;
   }

   public boolean check() {
      return this.hANDLE != null;
   }

   public Map<String, PsapiLibrary.ModuleInfo> getMap() {
      return this.resolveMap(this.hANDLE);
   }

   public HANDLE getHANDLE() {
      return this.hANDLE;
   }

   public long computeInVq2GPDL88H5yMlZYDtPjs0YlIcewX1(ProcessMemoryScanner.SearchRule... searchRules) {
      long l = -1L;

      for (ProcessMemoryScanner.SearchRule searchRule : searchRules) {
         l = this.computeLong7(l + 1L, searchRule.getText(), searchRule.getSearchCondition());
         if (l == -1L) {
            return -1L;
         }
      }

      return l;
   }

   public JsonObject resolveJsonObject(long l) {
      int i2 = 0;
      boolean flag = false;
      int i3 = 1024;
      long l2 = 0L;
      StringBuilder stringBuilder = new StringBuilder();

      do {
         String text = this.createText(l + l2, i3);

         for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '"') {
               flag = !flag;
            }

            if (!flag) {
               if (c == '{' || c == '[') {
                  i2++;
               } else if (c == '}' || c == ']') {
                  i2--;
               }
            }

            stringBuilder.append(c);
            if (i2 == 0) {
               break;
            }
         }

         l2 += i3;
      } while (i2 > 0);

      return (JsonObject)gson.fromJson(stringBuilder.toString(), JsonObject.class);
   }

   public void close() {
      Kernel32Library.INSTANCE.testHANDLE5(this.hANDLE);
   }

   public long value() {
      long l = 0L;

      for (Map.Entry<String, PsapiLibrary.ModuleInfo> entry : this.getMap().entrySet()) {
         l = Math.max(l, entry.getValue().value2() + entry.getValue().getIntValue());
      }

      return l;
   }

   public void setLongValue(long l) {
      this.longValue = l;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public long computeLong8(long l, long l2, ProcessMemoryScanner.SearchCondition searchCondition, String... texts) {
      for (String text : texts) {
         long l3 = this.computeLong4(l, l2, text, searchCondition);
         if (l3 != -1L) {
            return l3;
         }
      }

      return -1L;
   }

   public PsapiLibrary.ModuleInfo resolveModuleInfo(String text) {
      return this.resolveModuleInfo2(this.hANDLE, text);
   }

   public long computeString(String... texts) {
      long l = -1L;

      for (String text : texts) {
         l = this.computeLong5(l + 1L, text, 0);
         if (l == -1L) {
            return -1L;
         }
      }

      return l;
   }

   public long value2() {
      long l2 = Long.MAX_VALUE;

      for (Map.Entry<String, PsapiLibrary.ModuleInfo> entry : this.getMap().entrySet()) {
         long l = entry.getValue().value2();
         if (l > 0L) {
            l2 = Math.min(l2, l);
         }
      }

      return l2;
   }

   public String describe() {
      return this.describeHWND(this.hWND);
   }

   public interface SearchCondition {
      boolean testLong(long var1, int var3);
   }

   public static class SearchRule {
      public final ProcessMemoryScanner.SearchCondition searchCondition;
      public final String text;

      public ProcessMemoryScanner.SearchCondition getSearchCondition() {
         return this.searchCondition;
      }

      public SearchRule(String text, ProcessMemoryScanner.SearchCondition searchCondition) {
         this.text = text;
         this.searchCondition = searchCondition;
      }

      public String getText() {
         return this.text;
      }
   }
}
