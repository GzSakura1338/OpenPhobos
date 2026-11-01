package org.phobos.core.render;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.RenderLayer.MultiPhaseParameters;
import net.minecraft.client.render.RenderPhase.Texture;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.util.Identifier;

public class GlintRenderLayers extends RenderLayer {
   public static RenderLayer renderLayer5 = resolveRenderLayer7("glint_color");
   public static RenderLayer renderLayer6 = resolveRenderLayer2("entity_glint_color");
   public static RenderLayer renderLayer7 = resolveRenderLayer6("glint_direct_color");
   public static RenderLayer renderLayer2 = resolveRenderLayer5("entity_glint_direct_color");
   public static RenderLayer renderLayer = resolveRenderLayer4("armor_glint_color");
   public static RenderLayer renderLayer4 = resolveRenderLayer("armor_entity_glint_color");
   public static RenderLayer renderLayer3 = resolveRenderLayer3("translucent_glint_color");

   public static RenderLayer resolveRenderLayer(String text) {
      Identifier identifier = ItemRenderer.ITEM_ENCHANTMENT_GLINT;
      MultiPhaseParameters multiPhaseParameters = MultiPhaseParameters.builder()
         .program(RenderPhase.ARMOR_ENTITY_GLINT_PROGRAM)
         .texture(new Texture(identifier, true, false))
         .writeMaskState(COLOR_MASK)
         .cull(DISABLE_CULLING)
         .depthTest(EQUAL_DEPTH_TEST)
         .transparency(GLINT_TRANSPARENCY)
         .texturing(ENTITY_GLINT_TEXTURING)
         .colorLogic(RenderPhase.NO_COLOR_LOGIC)
         .layering(VIEW_OFFSET_Z_LAYERING)
         .build(false);
      return RenderLayer.of(
         "armor_entity_glint_" + text,
         VertexFormats.POSITION_TEXTURE_COLOR,
         DrawMode.QUADS,
         256,
         MultiPhaseParameters.builder()
            .program(RenderPhase.ARMOR_ENTITY_GLINT_PROGRAM)
            .texture(new Texture(identifier, true, false))
            .writeMaskState(COLOR_MASK)
            .cull(DISABLE_CULLING)
            .depthTest(EQUAL_DEPTH_TEST)
            .transparency(GLINT_TRANSPARENCY)
            .texturing(ENTITY_GLINT_TEXTURING)
            .layering(VIEW_OFFSET_Z_LAYERING)
            .build(false)
      );
   }

   public static RenderLayer resolveRenderLayer2(String text) {
      Identifier identifier = ItemRenderer.ITEM_ENCHANTMENT_GLINT;
      return RenderLayer.of(
         "entity_glint_" + text,
         VertexFormats.POSITION_TEXTURE_COLOR,
         DrawMode.QUADS,
         256,
         MultiPhaseParameters.builder()
            .program(RenderPhase.ENTITY_GLINT_PROGRAM)
            .texture(new Texture(identifier, true, false))
            .writeMaskState(COLOR_MASK)
            .cull(DISABLE_CULLING)
            .depthTest(EQUAL_DEPTH_TEST)
            .transparency(GLINT_TRANSPARENCY)
            .target(ITEM_ENTITY_TARGET)
            .texturing(ENTITY_GLINT_TEXTURING)
            .build(false)
      );
   }

   public static RenderLayer resolveRenderLayer3(String text) {
      Identifier identifier = ItemRenderer.ITEM_ENCHANTMENT_GLINT;
      return RenderLayer.of(
         "glint_translucent_" + text,
         VertexFormats.POSITION_TEXTURE_COLOR,
         DrawMode.QUADS,
         256,
         MultiPhaseParameters.builder()
            .program(TRANSLUCENT_GLINT_PROGRAM)
            .texture(new Texture(identifier, true, false))
            .writeMaskState(COLOR_MASK)
            .cull(DISABLE_CULLING)
            .depthTest(EQUAL_DEPTH_TEST)
            .transparency(GLINT_TRANSPARENCY)
            .texturing(GLINT_TEXTURING)
            .target(ITEM_ENTITY_TARGET)
            .build(false)
      );
   }

   public static void handleObject2ObjectLinkedOpenHashMap(Object2ObjectLinkedOpenHashMap<RenderLayer, BufferAllocator> object2ObjectLinkedOpenHashMap) {
      handleObject2ObjectLinkedOpenHashMap2(object2ObjectLinkedOpenHashMap, renderLayer5);
      handleObject2ObjectLinkedOpenHashMap2(object2ObjectLinkedOpenHashMap, renderLayer6);
      handleObject2ObjectLinkedOpenHashMap2(object2ObjectLinkedOpenHashMap, renderLayer7);
      handleObject2ObjectLinkedOpenHashMap2(object2ObjectLinkedOpenHashMap, renderLayer2);
      handleObject2ObjectLinkedOpenHashMap2(object2ObjectLinkedOpenHashMap, renderLayer);
      handleObject2ObjectLinkedOpenHashMap2(object2ObjectLinkedOpenHashMap, renderLayer4);
      handleObject2ObjectLinkedOpenHashMap2(object2ObjectLinkedOpenHashMap, renderLayer3);
   }

   public static RenderLayer resolveRenderLayer4(String text) {
      Identifier identifier = ItemRenderer.ITEM_ENCHANTMENT_GLINT;
      return RenderLayer.of(
         "armor_glint_" + text,
         VertexFormats.POSITION_TEXTURE_COLOR,
         DrawMode.QUADS,
         256,
         MultiPhaseParameters.builder()
            .program(RenderPhase.ARMOR_ENTITY_GLINT_PROGRAM)
            .texture(new Texture(identifier, true, false))
            .writeMaskState(COLOR_MASK)
            .cull(DISABLE_CULLING)
            .depthTest(EQUAL_DEPTH_TEST)
            .transparency(GLINT_TRANSPARENCY)
            .texturing(GLINT_TEXTURING)
            .layering(VIEW_OFFSET_Z_LAYERING)
            .build(false)
      );
   }

   public static RenderLayer resolveRenderLayer5(String text) {
      Identifier identifier = ItemRenderer.ITEM_ENCHANTMENT_GLINT;
      return RenderLayer.of(
         "entity_glint_direct_" + text,
         VertexFormats.POSITION_TEXTURE_COLOR,
         DrawMode.QUADS,
         256,
         MultiPhaseParameters.builder()
            .program(RenderPhase.DIRECT_ENTITY_GLINT_PROGRAM)
            .texture(new Texture(identifier, true, false))
            .writeMaskState(COLOR_MASK)
            .cull(DISABLE_CULLING)
            .depthTest(EQUAL_DEPTH_TEST)
            .transparency(GLINT_TRANSPARENCY)
            .texturing(ENTITY_GLINT_TEXTURING)
            .build(false)
      );
   }

   public static RenderLayer resolveRenderLayer6(String text) {
      Identifier identifier = ItemRenderer.ITEM_ENCHANTMENT_GLINT;
      return RenderLayer.of(
         "glint_direct_" + text,
         VertexFormats.POSITION_TEXTURE_COLOR,
         DrawMode.QUADS,
         256,
         MultiPhaseParameters.builder()
            .program(RenderPhase.DIRECT_ENTITY_GLINT_PROGRAM)
            .texture(new Texture(identifier, true, false))
            .writeMaskState(COLOR_MASK)
            .cull(DISABLE_CULLING)
            .depthTest(EQUAL_DEPTH_TEST)
            .transparency(GLINT_TRANSPARENCY)
            .texturing(GLINT_TEXTURING)
            .build(false)
      );
   }

   public static RenderLayer resolveRenderLayer7(String text) {
      Identifier identifier = ItemRenderer.ITEM_ENCHANTMENT_GLINT;
      return RenderLayer.of(
         "glint_" + text,
         VertexFormats.POSITION_TEXTURE_COLOR,
         DrawMode.QUADS,
         256,
         MultiPhaseParameters.builder()
            .program(RenderPhase.GLINT_PROGRAM)
            .texture(new Texture(identifier, true, false))
            .writeMaskState(COLOR_MASK)
            .cull(DISABLE_CULLING)
            .depthTest(EQUAL_DEPTH_TEST)
            .transparency(GLINT_TRANSPARENCY)
            .texturing(GLINT_TEXTURING)
            .build(false)
      );
   }

   public static void handleObject2ObjectLinkedOpenHashMap2(
      Object2ObjectLinkedOpenHashMap<RenderLayer, BufferAllocator> object2ObjectLinkedOpenHashMap, RenderLayer renderLayer
   ) {
      if (!object2ObjectLinkedOpenHashMap.containsKey(renderLayer)) {
         object2ObjectLinkedOpenHashMap.put(renderLayer, new BufferAllocator(renderLayer.getExpectedBufferSize()));
      }
   }

   public GlintRenderLayers(String text, VertexFormat vertexFormat, DrawMode drawMode, int i, boolean flag, boolean flag2, Runnable runnable, Runnable runnable2) {
      super(text, vertexFormat, drawMode, i, flag, flag2, runnable, runnable2);
   }
}
