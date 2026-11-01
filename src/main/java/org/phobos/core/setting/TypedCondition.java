package org.phobos.core.setting;

import java.util.function.Supplier;
import org.phobos.api.ValueSource;
import org.phobos.api.TypeBound;

public class TypedCondition {
   public Supplier<Boolean> booleanValueSupplier;

   public Supplier<Boolean> getBooleanValueSupplier() {
      return this.booleanValueSupplier;
   }

   public <T, K extends ValueSource<?> & TypeBound<T>, V extends ValueSource<?> & TypeBound<T>> TypedCondition(K valueSource, V valueSource2) {
      this.booleanValueSupplier = () -> valueSource.getValue() == valueSource2.getValue();
   }

   public void setBooleanValueSupplier(Supplier<Boolean> supplier) {
      this.booleanValueSupplier = supplier;
   }
}
