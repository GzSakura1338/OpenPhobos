package org.phobos.api;

import com.google.gson.JsonElement;
import org.phobos.type.BINDSMODULESEnum;

public interface ConfigSectionHandler {
   void handleBINDSMODULESEnum(BINDSMODULESEnum var1);

   JsonElement resolveJsonElement(BINDSMODULESEnum var1);
}
