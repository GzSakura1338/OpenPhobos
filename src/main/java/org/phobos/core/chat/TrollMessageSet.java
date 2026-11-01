package org.phobos.core.chat;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class TrollMessageSet {
   public final Set<TrollMessageBuilder> trollMessageBuilderSet = new HashSet<>();

   public Optional<TrollMessageBuilder> resolveOptional(String text, Set<String> set) {
      for (TrollMessageBuilder trollMessageBuilder : this.trollMessageBuilderSet) {
         if (trollMessageBuilder.testText2(text, set)) {
            return Optional.of(trollMessageBuilder);
         }
      }

      return Optional.empty();
   }

   public Set<TrollMessageBuilder> getTrollMessageBuilderSet() {
      return this.trollMessageBuilderSet;
   }

   public boolean testText(String text, Set<String> set, Consumer<String> consumer) {
      for (String text3 : set) {
         String text2 = "<%s>".formatted(text3);
         if (text.contains(text2)) {
            consumer.accept(text3);
            return true;
         }
      }

      return false;
   }

   public TrollMessageSet resolveTrollMessageSet(TrollMessageBuilder trollMessageBuilder) {
      this.trollMessageBuilderSet.add(trollMessageBuilder);
      return this;
   }
}
