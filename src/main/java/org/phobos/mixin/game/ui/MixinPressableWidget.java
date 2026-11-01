package org.phobos.mixin.game.ui;

import java.awt.Color;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.util.math.MathHelper;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.config.ConfigManager;
import org.phobos.core.render.RenderUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PressableWidget.class)
public abstract class MixinPressableWidget extends MixinClickableWidget implements MinecraftHolder {
   @Shadow
   public abstract void drawMessage(DrawContext var1, TextRenderer var2, int var3);

   @Inject(method = "renderWidget", at = @At("HEAD"), cancellable = true)
   private void renderWidgetHook(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      if (ConfigManager.getCloudSettings().theme.getValue()) {
         ci.cancel();
         RenderUtils.handleMatrixStack12(
            context.getMatrices(), Color.WHITE, this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0.0F, () -> {
               RenderUtils.menuButtonShader.handleColor(Color.WHITE);
               RenderUtils.menuButtonShader.run3();
            }
         );
         int i = this.active ? 16777215 : 10526880;
         this.drawMessage(context, mc.textRenderer, i | MathHelper.ceil(this.alpha * 255.0F) << 24);
      }
   }
}
