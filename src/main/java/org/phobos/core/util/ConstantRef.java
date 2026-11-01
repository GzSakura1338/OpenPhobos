package org.phobos.core.util;

import java.util.function.Supplier;
import org.phobos.api.Constant;

public class ConstantRef<S> implements Constant<S>, Supplier<S> {
   public final S object;

   public ConstantRef(S object) {
      this.object = object;
   }

   @Override
   public S get() {
      return this.object;
   }
}
