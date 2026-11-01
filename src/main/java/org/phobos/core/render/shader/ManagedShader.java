package org.phobos.core.render.shader;

import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import org.phobos.api.IntUniform4;
import org.phobos.api.IntUniform1;
import org.phobos.api.FloatUniform1;
import org.phobos.api.IntUniform3;
import org.phobos.api.IntUniform2;
import org.phobos.api.FloatUniform2;
import org.phobos.api.UniformLookup;
import org.phobos.api.FloatUniform3;
import org.phobos.api.MatrixUniform;
import org.phobos.api.FloatUniform4;

public abstract class ManagedShader<S extends AutoCloseable> implements UniformLookup {
   public final Map<String, UniformVariable> textMap = new HashMap<>();
   public S autoCloseable;
   public final Identifier identifier;
   public final List<ShaderVariable> shaderVariableList = new ArrayList<>();
   public boolean flag;

   @Override
   public FloatUniform3 resolveFloatUniform3(String text) {
      return this.resolveShaderVariable(this.textMap, textx -> new UniformVariable(textx, 3), text, "uniform");
   }

   public <U extends ShaderVariable> U resolveShaderVariable(Map<String, U> map, Function<String, U> function, String text, String text2) {
      U shaderVariable = (U)map.get(text);
      if (shaderVariable != null) {
         return shaderVariable;
      }

      U shaderVariable2 = (U)function.apply(text);
      if (this.autoCloseable != null) {
         boolean flag = this.testShaderVariable(shaderVariable2, this.autoCloseable);
         if (!flag) {
            LogUtils.getLogger().warn("No {} found with name {} in shader {}", new Object[]{text2, text, this.identifier});
         }
      }

      map.put(text, shaderVariable2);
      this.shaderVariableList.add(shaderVariable2);
      return shaderVariable2;
   }

   @Override
   public FloatUniform1 resolveFloatUniform1(String text) {
      return this.resolveShaderVariable(this.textMap, textx -> new UniformVariable(textx, 1), text, "uniform");
   }

   @Override
   public IntUniform2 resolveIntUniform2(String text) {
      return this.resolveShaderVariable(this.textMap, textx -> new UniformVariable(textx, 2), text, "uniform");
   }

   public void run() {
      if (this.check()) {
         try {
            assert this.autoCloseable != null;
            this.autoCloseable.close();
            this.autoCloseable = null;
         } catch (Exception exception) {
            throw new RuntimeException("Failed to release shader " + this.identifier, exception);
         }
      }

      this.flag = false;
   }

   public abstract S createFabricShaderProgram(ResourceFactory var1, MinecraftClient var2, Identifier var3) throws IOException;

   @Override
   public IntUniform1 resolveIntUniform1(String text) {
      return this.resolveShaderVariable(this.textMap, textx -> new UniformVariable(textx, 1), text, "uniform");
   }

   public ManagedShader(Identifier identifier) {
      this.identifier = identifier;
   }

   public boolean check() {
      return this.autoCloseable != null;
   }

   public Collection<ShaderVariable> getShaderVariableList() {
      return this.shaderVariableList;
   }

   @Override
   public IntUniform3 resolveIntUniform3(String text) {
      return this.resolveShaderVariable(this.textMap, textx -> new UniformVariable(textx, 3), text, "uniform");
   }

   public void handleResourceFactory(ResourceFactory resourceFactory) throws IOException {
      this.run();
      MinecraftClient minecraftClient = MinecraftClient.getInstance();
      this.autoCloseable = this.createFabricShaderProgram(resourceFactory, minecraftClient, this.identifier);
      this.handleInt(minecraftClient.getWindow().getFramebufferWidth(), minecraftClient.getWindow().getFramebufferHeight());
   }

   @Override
   public IntUniform4 resolveIntUniform4(String text) {
      return this.resolveShaderVariable(this.textMap, textx -> new UniformVariable(textx, 4), text, "uniform");
   }

   public abstract void handleIOException(IOException var1);

   @Override
   public FloatUniform2 resolveFloatUniform2(String text) {
      return this.resolveShaderVariable(this.textMap, textx -> new UniformVariable(textx, 2), text, "uniform");
   }

   public abstract boolean testShaderVariable(ShaderVariable var1, S var2);

   public void handleResourceFactory2(ResourceFactory resourceFactory) {
      try {
         this.handleResourceFactory(resourceFactory);
      } catch (IOException iOException) {
         this.flag = true;
         this.handleIOException(iOException);
      }
   }

   public boolean isFlag() {
      return this.flag;
   }

   @Override
   public String toString() {
      return "%s[%s]".formatted(this.getClass().getSimpleName(), this.identifier);
   }

   public abstract void handleInt(int var1, int var2);

   @Override
   public FloatUniform4 resolveFloatUniform4(String text) {
      return this.resolveShaderVariable(this.textMap, textx -> new UniformVariable(textx, 4), text, "uniform");
   }

   @Override
   public MatrixUniform resolveMatrixUniform(String text) {
      return this.resolveShaderVariable(this.textMap, textx -> new UniformVariable(textx, 16), text, "uniform");
   }

   public Identifier getIdentifier() {
      return this.identifier;
   }
}
