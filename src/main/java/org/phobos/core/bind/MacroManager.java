package org.phobos.core.bind;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.Named;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.JsonSerializable;
import org.phobos.type.NORMALCOMBINEDEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.ClientHolder;
import org.phobos.core.util.NullSafe;
import org.phobos.core.PhobosClient;

public class MacroManager implements Named, JsonSerializable, MinecraftHolder, Iterable<Macro> {
   public static final Logger LOGGER = LoggerFactory.getLogger(MacroManager.class);
   public final List<Macro> macroList = new ArrayList<>();
   public final PhobosClient phobosClient;
   private static Object[] phobos$g1806cd230;
   private static Object[] phobos$g1806ccfd0;
   private static Object[] phobos$g1806cd0e8;
   private static Object[] phobos$g1806cce18;

   public boolean testText(String text) {
      Boolean var3 = false & true;
      MutableObject var2 = new MutableObject<>(var3);
      List var4 = this.macroList;
      String var10000 = text;
      MutableObject cap22 = var2;
      String cap21 = var10000;
      Predicate<Macro> var21 = var2x -> testText3(cap21, cap22, var2x);
      var4.removeIf(var21);
      Object var22 = var2.getValue();
      boolean var23 = var22 instanceof Boolean;
      if (var22 != null && !var23) {
         throw new ClassCastException("CHECKCAST failed");
      } else {
         return (Boolean)var22;
      }
   }

   @Override
   public JsonElement createJsonPrimitive() {
      JsonObject jsonObject = new JsonObject();

      for (Macro macro : this) {
         String text = macro.getName();
         if (text != null) {
            JsonElement jsonElement = macro.createJsonPrimitive();
            if (jsonElement != null) {
               jsonObject.add(text, jsonElement);
            }
         }
      }

      return jsonObject;
   }

   public static boolean testText2(String text, Macro macro) {
      return macro.getName().equals(text);
   }

   @Nullable
   public Macro resolveMacro(String text) {
      for (Macro macro : this.macroList) {
         if (macro.getName().toLowerCase().startsWith(text)) {
            return macro;
         }
      }

      return null;
   }

   public void handleText(String text, String text2, BindKey bindKey) {
      this.macroList.add(new Macro(text, new String[]{text2}, bindKey, NORMALCOMBINEDEnum.NORMAL));
   }

   public static boolean testMacro(Macro macro, Macro macro2) {
      return macro2.getName().equals(macro.getName());
   }

   public PhobosClient getPhobosClient() {
      return this.phobosClient;
   }

   @Override
   public String getName() {
      return "macros.json";
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();

      for (String text : jsonObject.keySet()) {
         JsonElement jsonElement2 = jsonObject.get(text);
         if (jsonElement2 != null) {
            try {
               Macro macro = new Macro();
               macro.handleJsonElement(jsonElement2);
               this.macroList.add(macro);
            } catch (Throwable throwable) {
               ClientHolder.getPhobosClient().handleText5("MacroManager", "Unable to load " + text);
               throwable.printStackTrace();
            }
         }
      }
   }

   public List<Macro> getMacroList() {
      return this.macroList;
   }

   public MacroManager(PhobosClient phobosClient) {
      this.phobosClient = phobosClient;
   }

   @NotNull
   @Override
   public Iterator<Macro> iterator() {
      return this.macroList.iterator();
   }

   public static boolean testText3(String text, MutableObject mutableObject, Macro macro) {
      String text2 = macro.getBindKey().getName();
      if (text.equals(macro.getName())) {
         mutableObject.setValue(true);
         return true;
      } else if (text.equals(text2)) {
         mutableObject.setValue(true);
         return true;
      } else {
         return false;
      }
   }

   public boolean testMacro2(Macro macro) {
      return this.macroList.remove(macro);
   }

   public boolean testText4(String text, String text2, String text3) {
      List var4 = this.macroList;
      Stream var5 = var4.stream();
      String cap23 = text;
      Predicate<Macro> var22 = var1 -> testText2(cap23, var1);
      boolean var23 = var5.anyMatch(var22);
      byte var44;
      if (!var23) {
         PhobosClient var24 = this.phobosClient;

         label59: {
            label60: {
               BindManager var25;
               try {
                  var25 = var24.getBindManager();
               } catch (Throwable var55) {
                  boolean var48 = true;
                  if (var48) {
                     break label60;
                  }

                  throw var55;
               }

               BindKey var26;
               try {
                  var26 = var25.resolveBindKey2(text3);
               } catch (Throwable var54) {
                  boolean var56 = true;
                  if (var56) {
                     break label60;
                  }

                  throw var54;
               }

               MacroManager var10000 = this;
               String var10001 = text;
               String cap26 = text2;
               String cap25 = var10001;
               MacroManager cap24 = var10000;
               Consumer<BindKey> var43 = var3 -> cap24.handleText(cap25, cap26, var3);

               try {
                  NullSafe.handleObject(var26, var43);
                  break label59;
               } catch (Throwable var53) {
                  boolean var57 = true;
                  if (!var57) {
                     throw var53;
                  }
               }
            }

            long var45 = 0L;
            return (((int)((int)var45 & 0xFF & 0xFF)) != 0);
         }

         var44 = 1;
      } else {
         var44 = 0;
      }

      long var45 = var44 & 4294967295L;
      return (((int)((int)var45 & 0xFF & 0xFF)) != 0);
   }

   public boolean testMacro3(Macro macro) {
      List var2 = this.macroList;
      Stream var3 = var2.stream();
      Macro cap27 = macro;
      Predicate<Macro> var20 = var1 -> testMacro(cap27, var1);
      boolean var21 = var3.anyMatch(var20);
      byte var23;
      if (!var21) {
         List var22 = this.macroList;
         var22.add(macro);
         var23 = 1;
      } else {
         var23 = 0;
      }

      return (((int)((int)(var23 & 4294967295L) & 0xFF & 0xFF)) != 0);
   }

   @Nullable
   public Macro resolveMacro2(String text) {
      return this.macroList.stream().filter(macro -> macro.getName().equals(text)).findFirst().orElse(null);
   }
}
