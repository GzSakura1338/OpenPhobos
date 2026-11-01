package org.phobos.core.script;

import org.phobos.type.UnknownTypeUnknownMethodEnum;

public class ScriptTypeRef {
   public final int intValue;
   public final UnknownTypeUnknownMethodEnum unknownTypeUnknownMethodEnum;

   public UnknownTypeUnknownMethodEnum getUnknownTypeUnknownMethodEnum() {
      return this.unknownTypeUnknownMethodEnum;
   }

   public ScriptTypeRef(UnknownTypeUnknownMethodEnum unknownTypeUnknownMethodEnum, int i) {
      this.unknownTypeUnknownMethodEnum = unknownTypeUnknownMethodEnum;
      this.intValue = i;
   }

   public int getIntValue() {
      return this.intValue;
   }
}
