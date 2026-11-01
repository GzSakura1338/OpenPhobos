package org.phobos.core.gui;

import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;

public class DisconnectedScreenState {
   public DirectionalLayoutWidget directionalLayoutWidget;
   public DisconnectedScreen disconnectedScreen;

   public void setDirectionalLayoutWidget(DirectionalLayoutWidget directionalLayoutWidget) {
      this.directionalLayoutWidget = directionalLayoutWidget;
   }

   public DisconnectedScreenState(DisconnectedScreen disconnectedScreen, DirectionalLayoutWidget directionalLayoutWidget) {
      this.disconnectedScreen = disconnectedScreen;
      this.directionalLayoutWidget = directionalLayoutWidget;
   }

   public DisconnectedScreen getDisconnectedScreen() {
      return this.disconnectedScreen;
   }

   public void setDisconnectedScreen(DisconnectedScreen disconnectedScreen) {
      this.disconnectedScreen = disconnectedScreen;
   }

   public DirectionalLayoutWidget getDirectionalLayoutWidget() {
      return this.directionalLayoutWidget;
   }
}
