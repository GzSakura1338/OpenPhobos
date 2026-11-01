package org.phobos.core.net;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.phobos.data.TypeRecord;

public final class TypeRecordUtils {
   public static <T> TypeRecord<T> resolveTypeRecord(Class<?> classValue) {
      if (classValue.getGenericSuperclass() instanceof ParameterizedType parameterizedType3) {
         Type[] var3 = parameterizedType3.getActualTypeArguments();
         int var4 = var3.length;
         byte var5 = 0;
         if (var5 < var4) {
            Type type2 = var3[var5];
            if (type2 instanceof ParameterizedType parameterizedType2) {
               if (!(parameterizedType2.getRawType() instanceof Class)) {
                  throw new RuntimeException();
               }

               Type[] var8 = parameterizedType2.getActualTypeArguments();
               int var9 = var8.length;
               byte var10 = 0;
               if (var10 < var9) {
                  Type type = var8[var10];
                  if (type instanceof ParameterizedType parameterizedType && parameterizedType.getRawType() instanceof Class) {
                     return new TypeRecord<>((Class<T>)parameterizedType2.getRawType(), (Class<?>)parameterizedType.getRawType());
                  }

                  if (type instanceof Class) {
                     return new TypeRecord<>((Class<T>)parameterizedType2.getRawType(), (Class<?>)type);
                  }
               }

               return new TypeRecord<>((Class<T>)parameterizedType2.getRawType(), null);
            }

            if (type2 instanceof Class) {
               return new TypeRecord<>((Class<T>)type2, null);
            }

            throw new RuntimeException();
         }
      }

      throw new RuntimeException();
   }

   public TypeRecordUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
