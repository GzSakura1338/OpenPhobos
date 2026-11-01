package org.phobos.core.util;

import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.Constant;

public class NullableRef<S> implements Constant<S>, Supplier<S> {
   @Nullable
   public S object;

   public NullableRef(@Nullable S object) {
      this.object = object;
   }

   @Override
   public S get() {
      return this.object;
   }

   public void setObject(@Nullable S object) {
      this.object = object;
   }

   public NullableRef() {
   }
}
