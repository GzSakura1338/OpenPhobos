package org.phobos.api;

import java.util.Arrays;
import java.util.function.Supplier;
import org.jetbrains.annotations.Contract;
import org.phobos.core.user.UserSession;

@FunctionalInterface
public interface Visibility {
   Visibility INSTANCE = () -> true;
   Visibility INSTANCE2 = () -> UserSession.flag3;
   Visibility INSTANCE3 = () -> UserSession.flag2;

   default Visibility resolveVisibility(Visibility visibility) {
      return () -> this.isFlag8() || visibility.isFlag8();
   }

   static Visibility resolveVisibility2(Supplier<Boolean> supplier) {
      return supplier::get;
   }

   boolean isFlag8();

   @SafeVarargs
   @Contract("_, _ -> new")
   static <T> Visibility resolveVisibility3(Supplier<T> supplier, T... objects) {
      return () -> Arrays.asList(objects).contains(supplier.get());
   }

   static Visibility resolveVisibility4(Visibility visibility, Visibility visibility2, Visibility.OpAnd opAnd) {
      return switch (opAnd) {
         case AND -> visibility.resolveVisibility5(visibility2);
         case OR -> visibility.resolveVisibility(visibility2);
         case NONE -> visibility.getVisibility2().resolveVisibility5(visibility2.getVisibility2());
      };
   }

   default Visibility getVisibility2() {
      return () -> !this.isFlag8();
   }

   default Visibility resolveVisibility5(Visibility visibility) {
      return () -> this.isFlag8() && visibility.isFlag8();
   }

   enum OpAnd {
      AND,
      OR,
      NONE;
   }
}
