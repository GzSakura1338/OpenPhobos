package org.phobos.openapi;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.Exposed;

@Exposed
public interface OpenSetting<T> {
   T getValue();

   T getObject();

   void handleObject(T var1);

   void run();

   @Nullable
   String describe();

   JsonElement getJsonElement();
}
