package org.phobos.api;

public interface ContextResult<T, CTX> {
   CTX getObject();

   boolean[] getFlagArray();

   default CTX getObject2() {
      boolean[] flags = this.getFlagArray();

      for (int i = 0; i < flags.length; i++) {
         boolean flag = flags[i];
         if (!flag) {
            flags[i] = true;
            break;
         }
      }

      return this.getObject();
   }

   default T getObject3() throws Throwable {
      for (boolean flag : this.getFlagArray()) {
         if (!flag) {
            throw this.createIllegalStateException();
         }
      }

      return this.get();
   }

   Throwable createIllegalStateException();

   T get();
}
