package org.phobos.api;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public interface Kernel32Library extends WinNT, StdCallLibrary {
   Kernel32Library INSTANCE = (Kernel32Library)Native.loadLibrary("kernel32", Kernel32Library.class, W32APIOptions.UNICODE_OPTIONS);

   boolean testHANDLE(HANDLE var1, Kernel32Structs.MODULEENTRY32W var2);

   boolean testHANDLE2(HANDLE var1, Kernel32Structs.PROCESSENTRY32.ByReference var2);

   int value();

   HANDLE resolveHANDLE(DWORD var1, DWORD var2);

   boolean testHANDLE3(HANDLE var1, Pointer var2, Pointer var3, int var4, IntByReference var5);

   boolean testHANDLE4(HANDLE var1, Kernel32Structs.PROCESSENTRY32.ByReference var2);

   HANDLE resolveHANDLE2(int var1, boolean var2, int var3);

   boolean testHANDLE5(HANDLE var1);
}
