package org.phobos.core.setting;

import java.util.function.Supplier;

public class BooleanCondition {
   public final Supplier<Boolean> booleanValueSupplier;

   public Supplier<Boolean> getBooleanValueSupplier() {
      return this.booleanValueSupplier;
   }

   public BooleanCondition(Supplier<Boolean> supplier) {
      this.booleanValueSupplier = supplier;
   }
}
