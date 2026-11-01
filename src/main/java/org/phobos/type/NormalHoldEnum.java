package org.phobos.type;

import org.phobos.api.Named;

public enum NormalHoldEnum implements Named {
   Normal("Normal"),
   Hold("Hold");

   public final String name;

   @Override
   public String toString() {
      return this.name;
   }

   NormalHoldEnum(final String name) {
      this.name = name;
   }

   public static NormalHoldEnum fromString(String string) {
      return string.equals("Hold") ? Hold : Normal;
   }

   @Override
   public String getName() {
      return this.name;
   }

   public static NormalHoldEnum other(NormalHoldEnum other) {
      return switch (other) {
         case Normal -> Hold;
         case Hold -> Normal;
      };
   }
}
