package org.phobos.core.command;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.Describable;

public class CommandCompletion implements Describable {
   public final MutableObject<String> textMutableObject = new MutableObject();
   public final MutableObject<String> textMutableObject2 = new MutableObject();

   public String describeText(String text, String text2) {
      return text.length() < text2.length() ? text.concat(text2.substring(text.length())) : text;
   }

   public static String describeText2(String text, String text2) {
      return text.length() < text2.length() ? text.concat(text2.substring(text.length())) : text;
   }

   @Nullable
   @Override
   public String describe() {
      return (String)this.textMutableObject2.getValue();
   }

   public MutableObject<String> getTextMutableObject() {
      return this.textMutableObject;
   }

   public void clear() {
      this.textMutableObject.setValue(null);
   }

   public MutableObject<String> getTextMutableObject2() {
      return this.textMutableObject2;
   }

   @Nullable
   public static <T> T resolveObject(String text, Collection<T> collection, Function<T, String> function, Predicate<T> predicate) {
      for (T object : collection) {
         if (function.apply(object).startsWith(text) && predicate.test(object)) {
            return object;
         }
      }

      return null;
   }

   @Nullable
   public static String describeText3(String text, Collection<String> collection) {
      for (String text2 : collection) {
         if (text2.toLowerCase().startsWith(text)) {
            return text2;
         }
      }

      return null;
   }

   public void handleText(String text, String text2) {
      this.textMutableObject.setValue(text);
      this.textMutableObject2.setValue(text2);
   }

   @Nullable
   @Override
   public String describe2() {
      return (String)this.textMutableObject.getValue();
   }
}
