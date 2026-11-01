package org.phobos.api;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.BaseTSD.ULONG_PTR;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.WORD;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinUser.INPUT;
import com.sun.jna.ptr.IntByReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public interface Win32Process {
   int PROCESS_VM_OPERATION = 8;
   int PROCESS_VM_WRITE = 32;
   int intValue2 = 173;
   int intValue3 = 178;
   int intValue4 = 174;
   int intValue5 = 176;
   int intValue6 = 179;
   int intValue7 = 177;
   int PROCESS_VM_READ = 16;
   int intValue8 = 175;

   default int computeText2(String text) {
      Kernel32Library kernel32Library = Kernel32Library.INSTANCE;
      HANDLE hANDLE = kernel32Library.resolveHANDLE(Kernel32Structs.dWORD, new DWORD(0L));
      Kernel32Structs.PROCESSENTRY32.ByReference byReference = new Kernel32Structs.PROCESSENTRY32.ByReference();

      while (kernel32Library.testHANDLE4(hANDLE, byReference)) {
         if (Native.toString(byReference.szExeFile).equals(text)) {
            int i = byReference.th32ProcessID.intValue();
            kernel32Library.testHANDLE5(hANDLE);
            return i;
         }
      }

      kernel32Library.testHANDLE5(hANDLE);
      return -1;
   }

   default void handleInt(int i) {
      INPUT iNPUT = new INPUT();
      iNPUT.type = new DWORD(1L);
      iNPUT.input.setType("ki");
      iNPUT.input.ki.wVk = new WORD(i);
      iNPUT.input.ki.wScan = new WORD(0L);
      iNPUT.input.ki.time = new DWORD(0L);
      iNPUT.input.ki.dwExtraInfo = new ULONG_PTR(0L);
      iNPUT.input.ki.dwFlags = new DWORD(0L);
      User32.INSTANCE.SendInput(new DWORD(1L), new INPUT[]{iNPUT}, iNPUT.size());
      iNPUT.input.ki.dwFlags = new DWORD(2L);
      User32.INSTANCE.SendInput(new DWORD(1L), new INPUT[]{iNPUT}, iNPUT.size());
   }

   default HWND resolveHWND(int i) {
      return this.resolveHWND2(i, hWND -> true);
   }

   default Map<String, PsapiLibrary.ModuleInfo> resolveMap(HANDLE hANDLE) {
      Map<String, PsapiLibrary.ModuleInfo> map = new HashMap<>();
      Pointer[] pointers = this.values2(hANDLE);

      for (Pointer pointer : pointers) {
         char[] chars = new char[1024];
         int i = PsapiLibrary.INSTANCE.computeHANDLE(hANDLE, pointer, chars, chars.length);
         String text = new String(chars, 0, i);
         PsapiLibrary.ModuleInfo moduleInfo = new PsapiLibrary.ModuleInfo();
         PsapiLibrary.INSTANCE.testHANDLE2(hANDLE, pointer, moduleInfo, moduleInfo.size());
         map.put(text, moduleInfo);
      }

      return map;
   }

   default PsapiLibrary.ModuleInfo resolveModuleInfo2(HANDLE hANDLE, String text) {
      Pointer[] pointers = this.values2(hANDLE);

      for (Pointer pointer : pointers) {
         char[] chars = new char[1024];
         int i = PsapiLibrary.INSTANCE.computeHANDLE(hANDLE, pointer, chars, chars.length);
         String text2 = new String(chars, 0, i);
         if (text2.equals(text)) {
            PsapiLibrary.ModuleInfo moduleInfo = new PsapiLibrary.ModuleInfo();
            PsapiLibrary.INSTANCE.testHANDLE2(hANDLE, pointer, moduleInfo, moduleInfo.size());
            return moduleInfo;
         }
      }

      return null;
   }

   default List<Integer> resolveList(String text) {
      List<Integer> list = new ArrayList<>();
      Kernel32Library kernel32Library = Kernel32Library.INSTANCE;
      HANDLE hANDLE = kernel32Library.resolveHANDLE(Kernel32Structs.dWORD, new DWORD(0L));
      Kernel32Structs.PROCESSENTRY32.ByReference byReference = new Kernel32Structs.PROCESSENTRY32.ByReference();

      while (kernel32Library.testHANDLE4(hANDLE, byReference)) {
         if (Native.toString(byReference.szExeFile).equals(text)) {
            list.add(byReference.th32ProcessID.intValue());
         }
      }

      kernel32Library.testHANDLE5(hANDLE);
      return list;
   }

   default boolean testHWND(HWND hWND) {
      return User32.INSTANCE.IsWindowVisible(hWND);
   }

   default HANDLE resolveHANDLE(int i) {
      Kernel32Library kernel32Library = Kernel32Library.INSTANCE;
      return kernel32Library.resolveHANDLE2(56, false, i);
   }

   default String describeHWND(HWND hWND) {
      char[] chars = new char[512];
      int i = User32.INSTANCE.GetWindowText(hWND, chars, chars.length);
      return Native.toString(Arrays.copyOf(chars, i));
   }

   default HWND resolveHWND2(int i, Win32Process.WindowCondition windowCondition) {
      AtomicReference<HWND> atomicReference = new AtomicReference<>();
      User32.INSTANCE.EnumWindows((hWND, pointer) -> {
         IntByReference intByReference = new IntByReference();
         User32.INSTANCE.GetWindowThreadProcessId(hWND, intByReference);
         if (intByReference.getValue() == i && this.testHWND(hWND) && windowCondition.testHWND(hWND)) {
            atomicReference.set(hWND);
            return false;
         } else {
            return true;
         }
      }, null);
      return atomicReference.get();
   }

   default Pointer[] values2(HANDLE hANDLE) {
      IntByReference intByReference = new IntByReference();
      Pointer[] pointers = new Pointer[2048];
      if (!PsapiLibrary.INSTANCE.testHANDLE(hANDLE, pointers, pointers.length, intByReference, 3)) {
         throw new RuntimeException("Failed to get module list: ERROR " + Kernel32Library.INSTANCE.value());
      } else {
         int i = intByReference.getValue();
         if (i == 0) {
            throw new RuntimeException("No modules found");
         } else {
            return Arrays.copyOf(pointers, i);
         }
      }
   }

   interface WindowCondition {
      boolean testHWND(HWND var1);
   }
}
