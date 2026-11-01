package org.phobos.api;

import org.phobos.core.util.StackFrameInfo;
import org.phobos.core.util.StackTraceInfo;

public interface PacketPredicate {
   boolean testStackFrameInfo(StackFrameInfo var1);

   boolean testStackTraceInfo(StackTraceInfo var1);
}
