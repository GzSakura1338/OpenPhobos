package org.phobos.api;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface PsapiLibrary extends WinNT, StdCallLibrary {
   PsapiLibrary INSTANCE = (PsapiLibrary)Native.loadLibrary("psapi", PsapiLibrary.class, W32APIOptions.UNICODE_OPTIONS);

   boolean testHANDLE(HANDLE var1, Pointer[] var2, int var3, IntByReference var4, int var5);

   int computeHANDLE(HANDLE var1, Pointer var2, char[] var3, int var4);

   boolean testHANDLE2(HANDLE var1, Pointer var2, PsapiLibrary.ModuleInfo var3, int var4);

   class ModuleFilter {
      public static final int intValue = 2;
      public static final int intValue2 = 3;
      public static final int intValue3 = 0;
      public static final int intValue4 = 1;
   }

   class ModuleInfo extends Structure {
      public Pointer pointer;
      public Pointer pointer2;
      public int intValue;

      public long value() {
         return Pointer.nativeValue(this.pointer2);
      }

      public long value2() {
         return Pointer.nativeValue(this.pointer);
      }

      public int getIntValue() {
         return this.intValue;
      }

      public List<String> getFieldOrder() {
         return new ArrayList<>(Arrays.asList("BaseOfDll", "SizeOfImage", "EntryPoint"));
      }
   }
}
