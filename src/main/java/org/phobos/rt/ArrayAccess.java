package org.phobos.rt;

public final class ArrayAccess {
   public static int ld(Object array, int index) {
      if (array instanceof byte[] bytes) {
         return bytes[index];
      }
      return ((boolean[])array)[index] ? 1 : 0;
   }

   public static void st(Object array, int index, int value) {
      if (array instanceof byte[] bytes) {
         bytes[index] = (byte)value;
      } else {
         ((boolean[])array)[index] = (value & 1) != 0;
      }
   }
}
