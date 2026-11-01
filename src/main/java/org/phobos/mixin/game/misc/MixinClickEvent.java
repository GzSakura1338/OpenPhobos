package org.phobos.mixin.game.misc;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.text.ClickEvent;
import org.earthhack.ducks.IClickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ClickEvent.class)
public abstract class MixinClickEvent implements IClickEvent {
   @Unique
   private final List<Runnable> callbacks = new ArrayList<>();

   @Override
   public List<Runnable> getCallbacks() {
      return this.callbacks;
   }
}
