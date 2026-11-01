package org.phobos.mixin.game.ui;

import net.minecraft.client.gui.widget.ClickableWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClickableWidget.class)
public abstract class MixinClickableWidget {
   @Shadow
   protected float alpha;
   @Shadow
   public boolean active;

   @Shadow
   public abstract int getHeight();

   @Shadow
   public abstract int getWidth();

   @Shadow
   public abstract int getY();

   @Shadow
   public abstract int getX();
}
