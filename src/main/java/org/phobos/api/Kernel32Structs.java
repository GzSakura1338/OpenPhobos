package org.phobos.api;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.BaseTSD.ULONG_PTR;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.LONG;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface Kernel32Structs {
   DWORD dWORD = new DWORD(2L);
   DWORD dWORD2 = new DWORD(8L);

   class MODULEENTRY32W extends Structure {
      public Pointer pointer;
      public char[] charArray;
      public DWORD th32ModuleID;
      public DWORD dWORD;
      public DWORD dWORD2;
      public DWORD th32ProcessID;
      public char[] charArray2 = new char[256];
      public DWORD dwSize;
      public HMODULE hMODULE;
      public DWORD dWORD3;

      public List<String> getFieldOrder() {
         return new ArrayList<>(
            Arrays.asList(
               "dwSize", "th32ModuleID", "th32ProcessID", "GlblcntUsage", "ProccntUsage", "modBaseAddr", "modBaseSize", "hModule", "szModule", "szExePath"
            )
         );
      }

      public MODULEENTRY32W() {
         this.charArray = new char[260];
         this.dwSize = new DWORD(this.size());
      }

      public MODULEENTRY32W(Pointer pointer) {
         super(pointer);
         this.charArray = new char[260];
         this.read();
      }

      public static class ByReference extends Kernel32Structs.MODULEENTRY32W implements com.sun.jna.Structure.ByReference {
      }
   }

   class PROCESSENTRY32 extends Structure {
      public DWORD dwSize;
      public ULONG_PTR th32DefaultHeapID;
      public DWORD th32ParentProcessID;
      public LONG pcPriClassBase;
      public char[] szExeFile = new char[260];
      public DWORD th32ProcessID;
      public DWORD th32ModuleID;
      public DWORD cntThreads;
      public DWORD dwFlags;
      public DWORD cntUsage;

      public List<String> getFieldOrder() {
         return new ArrayList<>(
            Arrays.asList(
               "dwSize",
               "cntUsage",
               "th32ProcessID",
               "th32DefaultHeapID",
               "th32ModuleID",
               "cntThreads",
               "th32ParentProcessID",
               "pcPriClassBase",
               "dwFlags",
               "szExeFile"
            )
         );
      }

      public PROCESSENTRY32() {
         this.dwSize = new DWORD(this.size());
      }

      public PROCESSENTRY32(Pointer pointer) {
         super(pointer);
         this.read();
      }

      public static class ByReference extends com.sun.jna.platform.win32.Tlhelp32.PROCESSENTRY32 implements com.sun.jna.Structure.ByReference {
         public ByReference(Pointer pointer) {
            super(pointer);
         }

         public ByReference() {
         }

         public List<String> getFieldOrder() {
            return new ArrayList<>(
               Arrays.asList(
                  "dwSize",
                  "cntUsage",
                  "th32ProcessID",
                  "th32DefaultHeapID",
                  "th32ModuleID",
                  "cntThreads",
                  "th32ParentProcessID",
                  "pcPriClassBase",
                  "dwFlags",
                  "szExeFile"
               )
            );
         }
      }
   }
}
