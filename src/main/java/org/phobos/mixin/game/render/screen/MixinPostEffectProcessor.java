package org.phobos.mixin.game.render.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectPass;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.util.Identifier;
import org.earthhack.ducks.IPostEffectProcessor;
import org.phobos.mixin.game.render.interfaces.IPostEffectPass;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PostEffectProcessor.class)
public abstract class MixinPostEffectProcessor implements IPostEffectProcessor {
   @Shadow
   @Final
   private List<PostEffectPass> passes;
   @Unique
   private final List<String> buffers = new ArrayList<>();
   @Shadow
   @Final
   private Map<String, Framebuffer> targetsByName;

   @Override
   public void createBuffer(String name, Framebuffer buffer) {
      Framebuffer old = this.targetsByName.get(name);
      if (old != buffer) {
         if (old != null) {
            for (PostEffectPass pass : this.passes) {
               if (pass.input == old) {
                  ((IPostEffectPass)(Object)pass).setInput(buffer);
               }

               if (pass.output == old) {
                  ((IPostEffectPass)(Object)pass).setOutput(buffer);
               }
            }

            this.targetsByName.remove(name);
            this.buffers.remove(name);
         }

         this.targetsByName.put(name, buffer);
         this.buffers.add(name);
      }
   }

   @Redirect(
      method = "parsePass",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;")
   )
   private Identifier parsePassHook(String path) {
      return path.endsWith("shader.png") ? Identifier.of("phobos", "textures/shader.png") : Identifier.ofVanilla(path);
   }
}
