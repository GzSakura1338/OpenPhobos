package org.phobos.mixin.game.render.internal;

import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldRenderer.class)
public interface IWorldRenderer {
   @Accessor("MOON_PHASES")
   static Identifier getMoonPhases() {
      return Identifier.ofVanilla("textures/environment/moon_phases.png");
   }

   @Accessor("starsBuffer")
   VertexBuffer getStarsBuffer();

   @Accessor("lightSkyBuffer")
   VertexBuffer getLightSkyBuffer();

   @Accessor("SUN")
   static Identifier getSun() {
      return Identifier.ofVanilla("textures/environment/sun.png");
   }

   @Accessor("darkSkyBuffer")
   VertexBuffer getDarkSkyBuffer();

   @Accessor("END_SKY")
   static Identifier getEndSky() {
      return Identifier.ofVanilla("textures/environment/end_sky.png");
   }
}
