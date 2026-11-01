package org.phobos.mixin.game.ui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.config.ConfigManager;
import org.phobos.core.util.BufferUtils;
import org.phobos.core.render.RenderUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RotatingCubeMapRenderer.class)
public abstract class MixinRotatingCubeMapRenderer implements MinecraftHolder {
   @Inject(method = "render", at = @At("HEAD"), cancellable = true)
   private void renderHook1(DrawContext context, int width, int height, float alpha, float tickDelta, CallbackInfo ci) {
      if (ConfigManager.getCloudSettings().theme.getValue()) {
         ci.cancel();
         RenderUtils.run3();
         BufferBuilder buffer = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION);
         RenderUtils.resolveBufferBuilder(
            context.getMatrices().peek().getPositionMatrix(), buffer, 0.0F, 0.0F, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), 0.0F
         );
         RenderUtils.postProcessShader.run2();
         RenderUtils.postProcessShader.run3();
         BufferUtils.handleBufferBuilder(buffer);
         RenderUtils.run();
      }
   }
}
