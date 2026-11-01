package org.phobos.mixin.game.render.interfaces;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemRenderer.class)
public interface IItemRenderer {
   @Invoker("renderBakedItemModel")
   void invokeRenderBakedItemModel(BakedModel var1, ItemStack var2, int var3, int var4, MatrixStack var5, VertexConsumer var6);

   @Accessor("builtinModelItemRenderer")
   BuiltinModelItemRenderer getBuiltinModelItemRenderer();
}
