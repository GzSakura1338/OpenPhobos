package org.phobos.mixin.game;

import net.minecraft.client.render.RenderTickCounter.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Dynamic.class)
public interface IDynamic {
   @Accessor("paused")
   boolean isPaused();

   @Accessor("tickFrozen")
   boolean isFrozen();
}
