package org.phobos.api;

import org.jetbrains.annotations.Nullable;

public interface PrefixNamedCollection<T extends Named> extends NamedCollection<T> {
   @Nullable
   default T resolveNamed(String text) {
      for (T named : this) {
         if (named.describe3().startsWith(text)) {
            return named;
         }
      }

      return null;
   }
}
