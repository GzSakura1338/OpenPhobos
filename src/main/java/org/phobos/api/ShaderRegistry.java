package org.phobos.api;

import java.util.function.Consumer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.util.Identifier;
import org.phobos.core.render.shader.ShaderManager;

public interface ShaderRegistry {
   ShaderProgramHandle resolveShaderProgramHandle3(Identifier var1);

   PostEffectShader resolvePostEffectShader(Identifier var1);

   PostEffectShader resolvePostEffectShader2(Identifier var1, Consumer<PostEffectShader> var2);

   ShaderProgramHandle resolveShaderProgramHandle2(Identifier var1, VertexFormat var2);

   static ShaderRegistry getINSTANCE() {
      return ShaderManager.INSTANCE;
   }

   ShaderProgramHandle resolveShaderProgramHandle(Identifier var1, VertexFormat var2, Consumer<ShaderProgramHandle> var3);
}
