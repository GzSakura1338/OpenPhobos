package org.phobos.core.render.shader;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.phobos.api.SamplerUniform;
import org.phobos.api.FramebufferHolder;
import org.phobos.api.PostEffectShader;
import org.phobos.mixin.game.render.other.AccessiblePassesShaderEffect;

public final class ManagedPostEffect extends ManagedShader<PostEffectProcessor> implements PostEffectShader {
   public final Consumer<PostEffectShader> postEffectShaderConsumer;
   public final Map<String, NamedFramebuffer> textMap2;
   public final Map<String, PostEffectSamplerVariable> textMap3 = new HashMap<>();

   @Override
   public PostEffectProcessor getPostEffectProcessor() {
      return this.getPostEffectProcessor2();
   }

   @Override
   public FramebufferHolder resolveFramebufferHolder(String text) {
      return this.textMap2.computeIfAbsent(text, textx -> {
         NamedFramebuffer namedFramebuffer = new NamedFramebuffer(textx);
         if (this.autoCloseable != null) {
            namedFramebuffer.handlePostEffectProcessor(this.autoCloseable);
         }

         return namedFramebuffer;
      });
   }

   @Override
   public void handleText(String text, float f, float f2, float f3, float f4) {
      this.resolveFloatUniform4(text).handleFloat3(f, f2, f3, f4);
   }

   @Override
   public void handleText2(String text, float f) {
      this.resolveFloatUniform1(text).handleFloat5(f);
   }

   public ManagedPostEffect(Identifier identifier, Consumer<PostEffectShader> consumer) {
      super(identifier);
      this.postEffectShaderConsumer = consumer;
      this.textMap2 = new HashMap<>();
   }

   @Override
   public void handleText3(String text, int i) {
      this.resolveIntUniform1(text).handleInt3(i);
   }

   @Override
   public void handleInt(int i, int i2) {
      Preconditions.checkNotNull(this.autoCloseable);
      this.autoCloseable.setupDimensions(i, i2);

      for (ShaderVariable shaderVariable : this.getShaderVariableList()) {
         this.testShaderVariable(shaderVariable, this.autoCloseable);
      }

      for (NamedFramebuffer namedFramebuffer : this.textMap2.values()) {
         namedFramebuffer.handlePostEffectProcessor(this.autoCloseable);
      }

      this.postEffectShaderConsumer.accept(this);
   }

   @Override
   public void handleText4(String text, Matrix4f matrix4f) {
      this.resolveMatrixUniform(text).handleMatrix4f(matrix4f);
   }

   public PostEffectProcessor createFabricShaderProgram(ResourceFactory resourceFactory, MinecraftClient minecraftClient, Identifier identifier) throws IOException {
      return new PostEffectProcessor(minecraftClient.getTextureManager(), minecraftClient.getResourceManager(), minecraftClient.getFramebuffer(), identifier);
   }

   @Override
   public void handleText5(String text, float f, float f2) {
      this.resolveFloatUniform2(text).handleFloat(f, f2);
   }

   @Override
   public void handleText6(String text, float f, float f2, float f3) {
      this.resolveFloatUniform3(text).handleFloat2(f, f2, f3);
   }

   @Override
   public SamplerUniform resolveTextureUniform(String text) {
      return this.resolveShaderVariable(this.textMap3, PostEffectSamplerVariable::new, text, "sampler");
   }

   @Override
   public void handleIOException(IOException iOException) {
      LogUtils.getLogger().error("Could not create screen shader {}", this.getIdentifier(), iOException);
   }

   public boolean testShaderVariable(ShaderVariable shaderVariable, PostEffectProcessor postEffectProcessor) {
      return shaderVariable.testList(((AccessiblePassesShaderEffect)postEffectProcessor).getPasses());
   }

   @Override
   public void handleFloat(float f) {
      PostEffectProcessor postEffectProcessor = this.getPostEffectProcessor();
      if (postEffectProcessor != null) {
         RenderSystem.disableBlend();
         RenderSystem.disableDepthTest();
         RenderSystem.resetTextureMatrix();
         postEffectProcessor.render(f);
         MinecraftClient.getInstance().getFramebuffer().beginWrite(true);
         RenderSystem.disableBlend();
         RenderSystem.blendFunc(770, 771);
         RenderSystem.enableDepthTest();
      }
   }

   public PostEffectProcessor getPostEffectProcessor2() {
      if (!this.check() && !this.isFlag()) {
         this.handleResourceFactory2(MinecraftClient.getInstance().getResourceManager());
      }

      return this.autoCloseable;
   }
}
