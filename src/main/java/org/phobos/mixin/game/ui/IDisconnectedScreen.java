package org.phobos.mixin.game.ui;

import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.network.DisconnectionInfo;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DisconnectedScreen.class)
public interface IDisconnectedScreen extends IScreen {
   @Accessor("parent")
   Screen getParent();

   @Accessor("info")
   DisconnectionInfo getInfo();

   @Accessor("buttonLabel")
   Text getButtonLabel();

   @Accessor("grid")
   DirectionalLayoutWidget getGrid();
}
