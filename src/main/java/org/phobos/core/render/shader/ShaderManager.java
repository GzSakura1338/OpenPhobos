package org.phobos.core.render.shader;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.Window;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import org.phobos.api.WindowCallback;
import org.phobos.api.WorldRendererCallback;
import org.phobos.api.ShaderRegistry;
import org.phobos.api.ShaderProgramHandle;
import org.phobos.api.PostEffectShader;

public final class ShaderManager implements ShaderRegistry {
   public static final ShaderManager INSTANCE = new ShaderManager();
   public final Set<ManagedShader<?>> managedShaderSet = new ReferenceOpenHashSet();

   @Override
   public ShaderProgramHandle resolveShaderProgramHandle(Identifier identifier, VertexFormat vertexFormat, Consumer<ShaderProgramHandle> consumer) {
      ManagedShaderProgram managedShaderProgram = new ManagedShaderProgram(identifier, vertexFormat, consumer);
      this.managedShaderSet.add(managedShaderProgram);
      return managedShaderProgram;
   }

   public void handleInt(int i, int i2) {
      if (!this.managedShaderSet.isEmpty()) {
         for (ManagedShader<?> managedShader : this.managedShaderSet) {
            if (managedShader.check()) {
               managedShader.handleInt(i, i2);
            }
         }
      }
   }

   public ShaderManager() {
      WindowCallback.windowCallbackEvent
         .register((WindowCallback)(minecraftClient, window) -> this.handleInt2(window.getFramebufferWidth(), window.getFramebufferHeight()));
      WorldRendererCallback.worldRendererCallbackEvent.register((WorldRendererCallback)worldRenderer -> {
         Window window = MinecraftClient.getInstance().getWindow();
         this.handleInt(window.getFramebufferWidth(), window.getFramebufferHeight());
      });
   }

   public void handleResourceFactory(ResourceFactory resourceFactory) {
      for (ManagedShader<?> managedShader : this.managedShaderSet) {
         managedShader.handleResourceFactory2(resourceFactory);
      }
   }

   public void handleInt2(int i, int i2) {
      this.handleInt(i, i2);
   }

   @Override
   public PostEffectShader resolvePostEffectShader(Identifier identifier) {
      return this.resolvePostEffectShader2(identifier, postEffectShader -> {});
   }

   @Override
   public ShaderProgramHandle resolveShaderProgramHandle2(Identifier identifier, VertexFormat vertexFormat) {
      return this.resolveShaderProgramHandle(identifier, vertexFormat, shaderProgramHandle -> {});
   }

   @Override
   public ShaderProgramHandle resolveShaderProgramHandle3(Identifier identifier) {
      return this.resolveShaderProgramHandle2(identifier, VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL);
   }

   @Override
   public PostEffectShader resolvePostEffectShader2(Identifier identifier, Consumer<PostEffectShader> consumer) {
      ManagedPostEffect managedPostEffect = new ManagedPostEffect(identifier, consumer);
      this.managedShaderSet.add(managedPostEffect);
      return managedPostEffect;
   }
}
