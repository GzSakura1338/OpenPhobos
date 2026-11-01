package org.phobos.mixin.game.render.interfaces;

import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DrawContext.class)
public interface IDrawContext {
   @Invoker("tryDraw")
   void invokeDraw();
}
