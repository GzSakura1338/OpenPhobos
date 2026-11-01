package org.phobos.mixin.game.input;

import java.awt.Color;
import net.minecraft.text.CharacterVisitor;
import net.minecraft.text.Style;
import net.minecraft.text.TextVisitFactory;
import net.minecraft.util.Formatting;
import org.phobos.api.MinecraftHolder;
import org.phobos.module.client.ColorsModule;
import org.phobos.module.client.MediaModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TextVisitFactory.class)
public abstract class MixinTextVisitFactory implements MinecraftHolder {
   @Shadow
   private static boolean visitRegularCharacter(Style style, CharacterVisitor visitor, int index, char c) {
      return false;
   }

   @ModifyArg(
      method = "visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/text/TextVisitFactory;visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z",
         ordinal = 0
      ),
      index = 0
   )
   private static String transform(String string, int i, Style style1, Style style2, CharacterVisitor characterVisitor) {
      return MediaModule.describeText(string);
   }

   @Inject(
      method = "visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z",
      at = @At("HEAD"),
      cancellable = true
   )
   private static void visitFormattedHook(
      String text, int startIndex, Style startingStyle, Style resetStyle, CharacterVisitor visitor, CallbackInfoReturnable<Boolean> cir
   ) {
      int length = text.length();
      Style style = startingStyle;
      boolean rainbow = false;

      for (int index = startIndex; index < length; index++) {
         char character = text.charAt(index);
         if (character == 167) {
            if (index + 1 >= length) {
               break;
            }

            char next = text.charAt(index + 1);
            Formatting formatting = Formatting.byCode(next);
            if (formatting != null) {
               style = formatting == Formatting.RESET ? resetStyle : style.withExclusiveFormatting(formatting);
               rainbow = false;
            } else if (next == '+') {
               rainbow = true;
            }

            index++;
         } else if (Character.isHighSurrogate(character)) {
            if (index + 1 >= length) {
               if (!visitor.accept(index, style, 65533)) {
                  cir.setReturnValue(false);
                  return;
               }
               break;
            }

            char next = text.charAt(index + 1);
            if (Character.isLowSurrogate(next)) {
               if (!visitor.accept(index, style, Character.toCodePoint(character, next))) {
                  cir.setReturnValue(false);
                  return;
               }

               index++;
            } else if (!visitor.accept(index, style, 65533)) {
               cir.setReturnValue(false);
               return;
            }
         } else {
            if (rainbow) {
               style = style.withColor(Color.HSBtoRGB(ColorsModule.computeDouble(index * 10), 1.0F, 1.0F));
            }

            if (!visitRegularCharacter(style, visitor, index, character)) {
               cir.setReturnValue(false);
               return;
            }
         }
      }

      cir.setReturnValue(true);
   }
}
