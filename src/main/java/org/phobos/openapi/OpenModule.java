package org.phobos.openapi;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.Exposed;

@Exposed
public interface OpenModule {
   void run();

   void run2();

   boolean check();

   void run3();

   @Nullable
   String describe();

   @Nullable
   String describe2();

   <T> Optional<OpenSetting<T>> resolveOptional(String var1, Class<T> var2);
}
