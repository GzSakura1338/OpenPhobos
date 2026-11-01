package org.phobos.core.chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.util.Util;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.math.MathUtils;

public class TrollMessageBuilder {
   public final Set<String> textSet = new HashSet<>();
   public static final Map<Character, Character> characterMap = (Map<Character, Character>)Util.make(new HashMap(), hashMap -> {
      hashMap.put('r', 't');
      hashMap.put('g', 'h');
      hashMap.put('n', 'm');
      hashMap.put('a', 's');
      hashMap.put('t', 'g');
      hashMap.put('i', 'o');
   });
   public final List<ChatTemplate> chatTemplateList = new ArrayList<>();
   public final MutableObject<ChatTemplate> chatTemplateMutableObject = new MutableObject();
   public final MutableObject<TextPair> textPairMutableObject = new MutableObject();

   public TrollMessageBuilder resolveTrollMessageBuilder(String... texts) {
      return this.resolveTrollMessageBuilder2(new ChatTemplate(Arrays.stream(texts).<Function<TextPair, String>>map(text -> textPair -> text).toArray(Function[]::new)));
   }

   public TrollMessageBuilder resolveTrollMessageBuilder2(ChatTemplate chatTemplate) {
      this.chatTemplateList.add(chatTemplate);
      return this;
   }

   public TrollMessageBuilder resolveTrollMessageBuilder3(Function<TextPair, String> function) {
      return this.resolveTrollMessageBuilder2(new ChatTemplate(function));
   }

   public static String describeText(String text) {
      StringBuilder stringBuilder = new StringBuilder();
      char c2 = '0';
      if (MathUtils.testSupplier2(() -> 1.0)) {
         return text.toUpperCase();
      }

      for (char c : text.toCharArray()) {
         boolean flag = Character.isUpperCase(c);
         if (Character.isLetter(c) && c2 == ' ' && !flag && MathUtils.testSupplier2(() -> 30.0)) {
            c = Character.toUpperCase(c);
         }

         stringBuilder.append(c);
         if (MathUtils.testSupplier2(() -> MathUtils.computeDouble9(2.3, 7.5)) && c != c2) {
            Character character = characterMap.get(Character.toLowerCase(c));
            if (character != null) {
               stringBuilder.append(flag ? Character.toUpperCase(character) : character);
            }
         }

         c2 = c;
      }

      return stringBuilder.toString();
   }

   @SafeVarargs
   public final TrollMessageBuilder resolveTrollMessageBuilder4(Function<TextPair, String>... functions) {
      return this.resolveTrollMessageBuilder2(new ChatTemplate(functions));
   }

   public TrollMessageBuilder resolveTrollMessageBuilder5(String text) {
      return this.resolveTrollMessageBuilder(text);
   }

   public MutableObject<TextPair> getTextPairMutableObject() {
      return this.textPairMutableObject;
   }

   public Set<String> getTextSet() {
      return this.textSet;
   }

   public MutableObject<ChatTemplate> getChatTemplateMutableObject() {
      return this.chatTemplateMutableObject;
   }

   public TrollMessageBuilder resolveTrollMessageBuilder6(String text) {
      this.textSet.add(text);
      return this;
   }

   public List<ChatTemplate> getChatTemplateList() {
      return this.chatTemplateList;
   }

   public boolean testText(String text) {
      for (String text2 : this.textSet) {
         if (text2.contains(" ") && text.contains(text2)) {
            return true;
         }
      }

      String[] texts = text.split(" ");
      if (texts.length == 1) {
         for (String text3 : this.textSet) {
            if (text3.equalsIgnoreCase(text)) {
               return true;
            }
         }

         return false;
      } else {
         for (String text5 : texts) {
            for (String text4 : this.textSet) {
               if (text4.equalsIgnoreCase(text5)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   @Nullable
   public ChatTemplate getChatTemplate() {
      if (this.chatTemplateList.isEmpty()) {
         return null;
      } else {
         return this.chatTemplateList.size() == 1
            ? this.chatTemplateList.getFirst()
            : this.chatTemplateList.get(MathUtils.computeInt5(0, this.chatTemplateList.size() - 1));
      }
   }

   public boolean testText2(String text, Set<String> set) {
      for (String text3 : set) {
         String text2 = "<%s>".formatted(text3);
         if (text.contains(text2) && this.testText(text)) {
            this.textPairMutableObject.setValue(new TextPair(text, text3));
            return true;
         }
      }

      return false;
   }
}
