package org.phobos.mixin.game.ui;

import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractInventoryScreen.class)
public abstract class MixinAbstractInventoryScreen<T extends ScreenHandler> extends MixinHandledScreen<T> {
}
