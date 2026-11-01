package org.phobos.mixin.game.render.process;

import java.awt.Color;
import net.minecraft.client.particle.WaterSuspendParticle;
import net.minecraft.client.particle.WaterSuspendParticle.CrimsonSporeFactory;
import org.phobos.module.misc.WeatherModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CrimsonSporeFactory.class)
public abstract class MixinCrimsonSporeFactory {
   @Redirect(
      method = "createParticle(Lnet/minecraft/particle/SimpleParticleType;Lnet/minecraft/client/world/ClientWorld;DDDDDD)Lnet/minecraft/client/particle/Particle;",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/WaterSuspendParticle;setColor(FFF)V")
   )
   private void createParticleHook(WaterSuspendParticle instance, float red, float green, float blue) {
      Color color = WeatherModule.getColor();
      if (color == null) {
         instance.setColor(red, green, blue);
      } else {
         instance.setColor(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F);
      }
   }
}
