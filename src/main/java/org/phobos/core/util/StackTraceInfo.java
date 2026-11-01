package org.phobos.core.util;

import org.phobos.api.PacketPredicate;
import org.phobos.core.util.collection.SimpleList;

public class StackTraceInfo {
   public final SimpleList<StackFrameInfo> stackFrameInfoSimpleList = new SimpleList<>();
   public final StackFrameInfo stackFrameInfo;

   public StackTraceInfo(StackTraceElement[] stackTraceElements) {
      if (stackTraceElements.length == 0) {
         this.stackFrameInfo = null;
      } else {
         StackTraceElement stackTraceElement2 = stackTraceElements[2];
         this.stackFrameInfo = new StackFrameInfo(
            stackTraceElement2.isNativeMethod(),
            stackTraceElement2.getFileName(),
            stackTraceElement2.getClassName(),
            stackTraceElement2.getMethodName(),
            stackTraceElement2.getLineNumber()
         );

         for (int i = 2; i < stackTraceElements.length; i++) {
            StackTraceElement stackTraceElement = stackTraceElements[i];
            this.stackFrameInfoSimpleList
               .handleObject(
                  new StackFrameInfo(
                     stackTraceElement.isNativeMethod(),
                     stackTraceElement.getFileName(),
                     stackTraceElement.getClassName(),
                     stackTraceElement.getMethodName(),
                     stackTraceElement.getLineNumber()
                  )
               );
         }
      }
   }

   public StackFrameInfo getStackFrameInfo() {
      return this.stackFrameInfo;
   }

   public SimpleList<StackFrameInfo> getStackFrameInfoSimpleList() {
      return this.stackFrameInfoSimpleList;
   }

   public boolean testPacketPredicate(PacketPredicate packetPredicate) {
      return packetPredicate.testStackFrameInfo(this.stackFrameInfo) && packetPredicate.testStackTraceInfo(this);
   }
}
