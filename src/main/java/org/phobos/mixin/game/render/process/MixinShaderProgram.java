package org.phobos.mixin.game.render.process;

import java.util.List;
import java.util.Map;
import net.minecraft.client.gl.ShaderProgram;
import org.phobos.api.ShaderInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ShaderProgram.class)
public abstract class MixinShaderProgram implements ShaderInfo {
   @Shadow
   @Final
   private Map<String, String> samplers;

   @Override
   public boolean testText(String name) {
      return this.samplers.containsKey(name);
   }

   @Accessor("samplerNames")
   @Override
   public abstract List<String> getList();

   @Accessor("loadedSamplerIds")
   @Override
   public abstract List<Integer> getList2();
}
