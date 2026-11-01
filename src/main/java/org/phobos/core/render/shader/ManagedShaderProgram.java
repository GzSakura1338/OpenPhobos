package org.phobos.core.render.shader;

import com.google.common.base.Preconditions;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.fabricmc.fabric.impl.client.rendering.FabricShaderProgram;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import org.phobos.api.TextureUniform;
import org.phobos.api.ShaderProgramHandle;

public final class ManagedShaderProgram extends ManagedShader<ShaderProgram> implements ShaderProgramHandle {
   public final Map<String, ProgramSamplerVariable> textMap2 = new HashMap<>();
   public final Consumer<ShaderProgramHandle> shaderProgramHandleConsumer;
   public final VertexFormat vertexFormat;

   @Override
   public void handleInt(int i, int i2) {
      Preconditions.checkNotNull(this.autoCloseable);

      for (ShaderVariable shaderVariable : this.getShaderVariableList()) {
         this.testShaderVariable(shaderVariable, this.autoCloseable);
      }

      this.shaderProgramHandleConsumer.accept(this);
   }

   public ShaderProgram createFabricShaderProgram(ResourceFactory resourceFactory, MinecraftClient minecraftClient, Identifier identifier) throws IOException {
      return new FabricShaderProgram(resourceFactory, this.getIdentifier(), this.vertexFormat);
   }

   @Override
   public ShaderProgram getShaderProgram() {
      return this.autoCloseable;
   }

   public ManagedShaderProgram(Identifier identifier, VertexFormat vertexFormat, Consumer<ShaderProgramHandle> consumer) {
      super(identifier);
      this.vertexFormat = vertexFormat;
      this.shaderProgramHandleConsumer = consumer;
   }

   @Override
   public void handleIOException(IOException iOException) {
      LogUtils.getLogger().error("Could not create shader program {}", this.getIdentifier(), iOException);
   }

   @Override
   public TextureUniform resolveTextureUniform(String text) {
      return this.resolveShaderVariable(this.textMap2, ProgramSamplerVariable::new, text, "sampler");
   }

   public boolean testShaderVariable(ShaderVariable shaderVariable, ShaderProgram shaderProgram) {
      return shaderVariable.testShaderProgram(shaderProgram);
   }
}
