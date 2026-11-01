package org.phobos.api;

import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;

public interface MixinClassNames {
   interface CCS {
      static <T> T resolveObject(Object object, Class<T> classValue) {
         try {
            return (T)object;
         } catch (ClassCastException classCastException) {
            throw classCastException;
         }
      }

      static <T> T resolveObject2(Object object, T object2) {
         try {
            return (T)object;
         } catch (ClassCastException classCastException) {
            throw classCastException;
         }
      }
   }

   interface CPL {
      static boolean testText(String text, String text2) {
         String text3 = System.getProperty(text);
         return text3 != null && text3.equals(text2);
      }
   }

   interface ECA {
      static boolean testFlag(boolean flag) {
         return true;
      }

      static boolean testFlag2(boolean flag) {
         return flag;
      }

      static boolean testFlag3(boolean flag, Runnable runnable) {
         if (flag) {
            runnable.run();
         }

         return flag;
      }
   }

   interface ECS {
      @Nullable
      static <T> T resolveObject(Constant<T> constant, Class<?> classValue) {
         try {
            return constant.get();
         } catch (Throwable throwable) {
            if (throwable.getClass() != classValue) {
               throw throwable;
            } else {
               return null;
            }
         }
      }

      static <T> boolean testConstant(Constant<T> constant) {
         try {
            return constant.get() != null;
         } catch (Throwable throwable) {
            return false;
         }
      }
   }

   interface LE {
      static <T> T resolveObject(Consumer<T> consumer, T object) {
         consumer.accept(object);
         return object;
      }
   }
}
