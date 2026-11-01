package org.phobos.api;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.PointerByReference;

public interface Shell32 extends Library {
   Shell32 INSTANCE = (Shell32)Native.load("Shell32", Shell32.class);

   int computePointerByReference(PointerByReference var1, PointerByReference var2, PointerByReference var3);
}
