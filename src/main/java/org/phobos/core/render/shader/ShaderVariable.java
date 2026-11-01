package org.phobos.core.render.shader;

import java.util.List;
import net.minecraft.client.gl.PostEffectPass;
import net.minecraft.client.gl.ShaderProgram;

public abstract class ShaderVariable {
   public final String name;

   public abstract boolean testList(List<PostEffectPass> var1);

   public String getName() {
      return this.name;
   }

   public abstract boolean testShaderProgram(ShaderProgram var1);

   public ShaderVariable(String text) {
      this.name = text;
   }
}
