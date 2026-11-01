package org.phobos.mixin.game.render.screen;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.text.Text;
import org.phobos.api.TooltipDataProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(DrawContext.class)
public abstract class MixinDrawContext {
   @Inject(
      method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;Ljava/util/Optional;II)V",
      at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V", shift = Shift.BEFORE),
      locals = LocalCapture.CAPTURE_FAILHARD
   )
   private void drawTooltipHook1(
      TextRenderer textRenderer, List<Text> text, Optional<TooltipData> data, int x, int y, CallbackInfo ci, List<TooltipComponent> list
   ) {
      if (data.isPresent() && data.get() instanceof TooltipDataProvider meteorTooltipData) {
         list.add(meteorTooltipData.getTooltipComponent());
      }
   }

   @ModifyReceiver(
      method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;Ljava/util/Optional;II)V",
      at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V")
   )
   private Optional<TooltipData> drawTooltipHook2(Optional<TooltipData> data, Consumer<TooltipData> consumer) {
      return data.isPresent() && data.get() instanceof TooltipDataProvider ? Optional.empty() : data;
   }
}
