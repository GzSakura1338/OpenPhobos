package org.phobos.api;

import org.jetbrains.annotations.Nullable;

public interface TextProvider {
   String getText();

   @Nullable
   static <T> String describeObject2(T object) {
      return object instanceof TextProvider textProvider ? textProvider.getText() : null;
   }
}
