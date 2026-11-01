package org.phobos.type;

import java.util.function.Consumer;
import org.phobos.core.bind.Macro;

public enum NORMALCOMBINEDEnum {
   NORMAL("normal", macro -> {
      if (macro.getTextArray().length > 0) {
         macro.handleText(macro.getTextArray()[0]);
      }
   }),
   COMBINED("combined", macro -> {
      for (String command : macro.getTextArray()) {
         macro.handleText(command);
      }
   }),
   SWITCH("flow", macro -> {
      if (macro.getTextArray().length != 0) {
         macro.handleText(macro.getTextArray()[macro.getIntValue()]);
         macro.setIntValue(macro.getIntValue() + 1);
         if (macro.getIntValue() >= macro.getTextArray().length) {
            macro.setIntValue(0);
         }
      }
   });

   public final String name;
   public final Consumer<Macro> action;

   NORMALCOMBINEDEnum(final String name, final Consumer<Macro> action) {
      this.name = name;
      this.action = action;
   }

   public String getName() {
      return this.name;
   }

   @Override
   public String toString() {
      return this.name;
   }

   public Consumer<Macro> getAction() {
      return this.action;
   }

   public static NORMALCOMBINEDEnum fromString(String string) {
      return switch (string) {
         case "combined" -> COMBINED;
         case "switch" -> SWITCH;
         default -> NORMAL;
      };
   }
}
