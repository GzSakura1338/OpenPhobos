package org.phobos.core;

public class PhobosException extends Exception {
   public PhobosException(String text, Throwable throwable) {
      super(text, throwable);
   }

   public PhobosException(String text) {
      super(text);
   }
}
