package org.phobos.core.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.phobos.type.NORMALCOMBINEDEnum;
import org.phobos.core.bind.BindKey;
import org.phobos.core.bind.Macro;
import org.phobos.core.PhobosClient;

public class MacroCommand extends SyntaxCommand {
   public static final CommandSyntax commandSyntax = CommandSyntax.createCommandSyntax("add/del/list/combine/switch");

   @Override
   public void handleString(String[] texts, String text) {
      switch (texts[0].toLowerCase()) {
         case "add":
            if (texts.length < 4) {
               this.handleText2("Usage: macro add name key command");
               return;
            }

            String[] texts2 = Arrays.copyOfRange(texts, 0, 3);
            String text2 = texts[1];
            String text3 = text.replaceAll(this.getName() + " " + String.join(" ", texts2) + " ", "");
            String text4 = texts[2].toUpperCase();
            String text5 = this.phobosClient.getMacroManager().testText4(text2, text3, text4)
               ? "Bound '%s' to %s".formatted(text3, text4.toUpperCase())
               : "Unable to register '%s'".formatted(text3);
            this.handleText6(text5);
            break;
         case "del":
            if (texts.length != 2) {
               this.handleText2("Usage: macro del name");
               return;
            }

            String text6 = texts[1];
            String text7 = this.phobosClient.getMacroManager().testText(text6) ? "Deleted %s".formatted(text6) : "Failed to delete %s".formatted(text6);
            this.handleText6(text7);
            break;
         case "list":
            if (texts.length != 1) {
               this.handleText2("Usage: macro list");
               return;
            }

            for (Macro macro : this.phobosClient.getMacroManager()) {
               String text8 = macro.getName();
               String text9 = macro.getBindKey().getName();
               this.handleText6("%s: %s".formatted(text8, text9));
            }
            break;
         case "combine":
            if (texts.length < 5) {
               this.handleText2("Usage: macro combine name [names]");
               return;
            }

            String[] texts3 = (String[])ArrayUtils.subarray(texts, 3, texts.length);
            List<Macro> list = new ArrayList<>();

            for (String text10 : texts3) {
               Macro macro2 = this.phobosClient.getMacroManager().resolveMacro2(text10);
               if (macro2 == null) {
                  this.handleText2("Unable to find macro %s".formatted(text10));
                  return;
               }

               if (!macro2.getNORMALCOMBINEDEnum().equals(NORMALCOMBINEDEnum.NORMAL)) {
                  this.handleText2("Unable to merge %s with type %s".formatted(text10, macro2.getNORMALCOMBINEDEnum().toString()));
                  return;
               }

               list.add(macro2);
            }

            BindKey bindKey = this.phobosClient.getBindManager().resolveBindKey2(texts[2].toUpperCase());
            if (bindKey == null) {
               this.handleText2("Couldn't find key %s".formatted(texts[2].toUpperCase()));
               return;
            }

            Macro[] macros = list.toArray(new Macro[0]);
            Macro macro4 = Macro.createMacro(texts[1], bindKey, macros);

            for (Macro macro3 : macros) {
               this.phobosClient.getMacroManager().testMacro2(macro3);
            }

            String text11 = "%s into %s".formatted(Arrays.toString(list.stream().map(Macro::getName).toList().toArray(new String[0])), texts[1]);
            if (this.phobosClient.getMacroManager().testMacro3(macro4)) {
               this.handleText6("Switch merged " + text11);
               return;
            }

            this.handleText2("Failed to merge " + text11);
            break;
         case "switch":
            if (texts.length < 5) {
               this.handleText6("Usage: macro switch name [names]");
               return;
            }

            String[] texts4 = (String[])ArrayUtils.subarray(texts, 3, texts.length);
            List<Macro> list2 = new ArrayList<>();

            for (String text12 : texts4) {
               Macro macro5 = this.phobosClient.getMacroManager().resolveMacro2(text12);
               if (macro5 == null) {
                  this.handleText2("Unable to find macro %s".formatted(text12));
                  return;
               }

               if (!macro5.getNORMALCOMBINEDEnum().equals(NORMALCOMBINEDEnum.NORMAL)) {
                  this.handleText2("Unable to merge %s with type %s".formatted(text12, macro5.getNORMALCOMBINEDEnum().toString()));
                  return;
               }

               list2.add(macro5);
            }

            BindKey bindKey2 = this.phobosClient.getBindManager().resolveBindKey2(texts[2].toUpperCase());
            if (bindKey2 == null) {
               this.handleText2("Couldn't find key %s".formatted(texts[2].toUpperCase()));
               return;
            }

            Macro[] macros2 = list2.toArray(new Macro[0]);
            Macro macro7 = Macro.createMacro2(texts[1], bindKey2, macros2);
            if (macro7 == null) {
               this.handleText2("Couldn't create flow macro.");
               return;
            }

            for (Macro macro6 : macros2) {
               this.phobosClient.getMacroManager().testMacro2(macro6);
            }

            String text13 = "%s into %s".formatted(Arrays.toString(list2.stream().map(Macro::getName).toList().toArray(new String[0])), texts[1]);
            if (this.phobosClient.getMacroManager().testMacro3(macro7)) {
               this.handleText6("Flow merged " + text13);
               return;
            }

            this.handleText2("Failed to switch " + text13);
      }
   }

   @Override
   public boolean testString(String[] texts) {
      return texts.length > 0;
   }

   public MacroCommand(PhobosClient phobosClient) {
      super(phobosClient, "macro", "action", "name", "key", "command");
   }

   @Override
   public Suggestion resolveSuggestion(String text, String text2, String[] texts) {
      switch (text) {
         case "action":
            return commandSyntax.resolveSuggestion(text2);
         case "name":
            if (texts.length != 0 && texts[0].equalsIgnoreCase("del")) {
               Macro macro = this.phobosClient.getMacroManager().resolveMacro(text2);
               if (macro != null) {
                  return Suggestion.createSuggestion(macro.getName());
               }
            }
            break;
         case "key":
            BindKey bindKey = this.phobosClient.getBindManager().resolveBindKey3(text2);
            if (bindKey != null) {
               return Suggestion.createSuggestion3(bindKey.getName());
            }
      }

      return null;
   }
}
