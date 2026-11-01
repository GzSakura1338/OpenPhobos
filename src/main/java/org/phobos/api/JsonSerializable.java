package org.phobos.api;

import com.google.gson.JsonElement;

public interface JsonSerializable {
   void handleJsonElement(JsonElement var1);

   JsonElement createJsonPrimitive();
}
