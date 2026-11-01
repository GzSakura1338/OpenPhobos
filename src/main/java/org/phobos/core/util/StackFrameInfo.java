package org.phobos.core.util;

public class StackFrameInfo {
   public final int intValue;
   public final String text;
   public final String name;
   public final String text2;
   public final boolean flag;

   public boolean isFlag() {
      return this.flag;
   }

   public int value() throws IllegalCallerException {
      if (this.intValue == -2) {
         throw new IllegalCallerException("Cannot get line index for native method");
      } else {
         return this.intValue;
      }
   }

   public Class<?> getClassValue() throws ClassNotFoundException {
      return Class.forName(this.getText2());
   }

   public String getName() {
      return this.name;
   }

   public String getText() {
      return this.text;
   }

   public String getText2() {
      return this.text2;
   }

   public StackFrameInfo(boolean flag, String text, String text2, String text3, int i) {
      this.flag = flag;
      this.text = text;
      this.text2 = text2;
      this.name = text3;
      this.intValue = i;
   }
}
