package org.phobos.core.chat;

import java.util.Optional;
import java.util.function.Function;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public final class TextVisitorUtils {
   public static Text describeText(Text text, TextVisitorUtils.Visitor visitor) {
      MutableText mutableText = Text.empty();
      text.visit((style, textx) -> {
         TextVisitorUtils.Visit visit = TextVisitorUtils.Visit.createVisit2(textx, style);
         TextVisitorUtils.Visit visit2 = visitor.resolveVisit(visit);
         mutableText.append(Text.literal(visit2.string()).setStyle(visit2.style()));
         return Optional.empty();
      }, Style.EMPTY);
      return mutableText;
   }

   public TextVisitorUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public record Visit(String string, Style style) {

      public TextVisitorUtils.Visit createVisit(Function<Style, Style> function) {
         return new TextVisitorUtils.Visit(this.string, function.apply(this.style));
      }

      public static TextVisitorUtils.Visit createVisit2(String text, Style style) {
         return new TextVisitorUtils.Visit(text, style);
      }
   }

   public interface Visitor {
      TextVisitorUtils.Visit resolveVisit(TextVisitorUtils.Visit var1);
   }
}
