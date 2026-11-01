package org.phobos.mixin.game.render.process;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.SequencedMap;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.chunk.BlockBufferAllocatorStorage;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.util.Util;
import org.earthhack.ducks.IBufferBuilderStorage;
import org.phobos.core.render.GlintRenderLayers;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BufferBuilderStorage.class)
public abstract class MixinBufferBuilderStorage implements IBufferBuilderStorage {
   @Unique
   private Immediate backing;
   @Shadow
   @Final
   private BlockBufferAllocatorStorage blockBufferBuilders;

   @Shadow
   private static void assignBufferBuilder(Object2ObjectLinkedOpenHashMap<RenderLayer, BufferAllocator> builderStorage, RenderLayer layer) {
   }

   @Inject(method = "<init>", at = @At("TAIL"))
   private void initHook(int maxBlockBuildersPoolSize, CallbackInfo ci) {
      SequencedMap<RenderLayer, BufferAllocator> sequencedMap = (SequencedMap<RenderLayer, BufferAllocator>)Util.make(
         new Object2ObjectLinkedOpenHashMap(), map -> {
            assignBufferBuilder(map, RenderLayer.getArmorEntityGlint());
            assignBufferBuilder(map, RenderLayer.getGlint());
            assignBufferBuilder(map, RenderLayer.getGlintTranslucent());
            assignBufferBuilder(map, RenderLayer.getEntityGlint());
            assignBufferBuilder(map, RenderLayer.getDirectEntityGlint());
         }
      );
      this.backing = VertexConsumerProvider.immediate(sequencedMap, new BufferAllocator(10240));
   }

   @Inject(method = "assignBufferBuilder", at = @At("HEAD"))
   private static void assignBufferBuilderHook(Object2ObjectLinkedOpenHashMap<RenderLayer, BufferAllocator> builderStorage, RenderLayer layer, CallbackInfo ci) {
      GlintRenderLayers.handleObject2ObjectLinkedOpenHashMap(builderStorage);
   }

   @Override
   public Immediate getBackingContext() {
      return this.backing;
   }
}
