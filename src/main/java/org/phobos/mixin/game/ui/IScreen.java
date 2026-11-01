package org.phobos.mixin.game.ui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Screen.class)
public interface IScreen {
   @Accessor("title")
   Text getTitle();
}
