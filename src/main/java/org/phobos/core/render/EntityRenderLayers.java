package org.phobos.core.render;

import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.RenderLayer.MultiPhase;
import net.minecraft.client.render.RenderLayer.MultiPhaseParameters;
import net.minecraft.client.render.RenderPhase.Texture;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.earthhack.ducks.IRenderLayer;
import org.lwjgl.opengl.GL11C;

public class EntityRenderLayers {
   public static final BiFunction<Identifier, Boolean, RenderLayer> identifierBiFunction2 = Util.memoize(
      (identifier, booleanValue) -> {
         MultiPhaseParameters multiPhaseParameters = MultiPhaseParameters.builder()
            .program(RenderLayer.ENTITY_CUTOUT_NONULL_PROGRAM)
            .depthTest(RenderPhase.LEQUAL_DEPTH_TEST)
            .texture(new Texture(identifier, false, false))
            .transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY)
            .lightmap(RenderLayer.ENABLE_LIGHTMAP)
            .overlay(RenderLayer.ENABLE_OVERLAY_COLOR)
            .build(booleanValue);
         MultiPhase multiPhase = RenderLayer.of(
            "entity_cutout_see_through", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, DrawMode.QUADS, 1536, true, true, multiPhaseParameters
         );
         handleMultiPhase(multiPhase);
         return multiPhase;
      }
   );
   public static final BiFunction<Identifier, Boolean, RenderLayer> identifierBiFunction = Util.memoize(
      (identifier, booleanValue) -> {
         MultiPhaseParameters multiPhaseParameters = MultiPhaseParameters.builder()
            .program(RenderLayer.ENTITY_CUTOUT_NONULL_PROGRAM)
            .depthTest(RenderPhase.LEQUAL_DEPTH_TEST)
            .texture(new Texture(identifier, false, false))
            .transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY)
            .lightmap(RenderLayer.ENABLE_LIGHTMAP)
            .overlay(RenderLayer.ENABLE_OVERLAY_COLOR)
            .build(booleanValue);
         return RenderLayer.of(
            "entity_cutout_transparency", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, DrawMode.QUADS, 1536, true, true, multiPhaseParameters
         );
      }
   );
   public static final Function<Identifier, RenderLayer> identifierFunction = Util.memoize(
      identifier -> {
         MultiPhaseParameters multiPhaseParameters = MultiPhaseParameters.builder()
            .program(RenderLayer.ENTITY_SOLID_PROGRAM)
            .texture(new Texture(identifier, false, false))
            .transparency(RenderLayer.NO_TRANSPARENCY)
            .lightmap(RenderLayer.ENABLE_LIGHTMAP)
            .overlay(RenderLayer.ENABLE_OVERLAY_COLOR)
            .build(true);
         MultiPhase multiPhase = RenderLayer.of(
            "entity_solid_see_through", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, DrawMode.QUADS, 1536, true, true, multiPhaseParameters
         );
         handleMultiPhase(multiPhase);
         return multiPhase;
      }
   );
   public static final RenderLayer renderLayer = resolveRenderLayer(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);

   public static void handleMultiPhase(MultiPhase multiPhase) {
      IRenderLayer iRenderLayer = (IRenderLayer)(Object)multiPhase;
      iRenderLayer.setStarter(() -> {
         GL11C.glEnable(32823);
         GL11C.glPolygonOffset(1.0F, -1100000.0F);
      });
      iRenderLayer.setFinisher(() -> {
         GL11C.glPolygonOffset(1.0F, 1100000.0F);
         GL11C.glDisable(32823);
      });
   }

   public static RenderLayer resolveRenderLayer(Identifier identifier) {
      return identifierBiFunction2.apply(identifier, true);
   }

   public static RenderLayer getRenderLayer() {
      return renderLayer;
   }

   public static RenderLayer resolveRenderLayer2(Identifier identifier) {
      return identifierBiFunction.apply(identifier, true);
   }

   public static RenderLayer resolveRenderLayer3(Identifier identifier) {
      return identifierFunction.apply(identifier);
   }
}
