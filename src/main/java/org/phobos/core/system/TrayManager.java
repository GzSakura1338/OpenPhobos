package org.phobos.core.system;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon;
import java.io.File;
import java.util.Optional;
import java.util.function.Consumer;
import javax.swing.ImageIcon;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.core.config.ConfigManager;
import org.phobos.core.Feature;
import org.phobos.core.PhobosClient;
import org.phobos.core.user.UserSession;

public class TrayManager extends Feature {
   public final MutableObject<Optional<TrayIcon>> optionalMutableObject = new MutableObject(Optional.empty());

   public void handlePopupMenu(PopupMenu popupMenu, String text, boolean flag, Consumer<Boolean> consumer) {
      CheckboxMenuItem checkboxMenuItem = new CheckboxMenuItem(text);
      checkboxMenuItem.setState(flag);
      checkboxMenuItem.addItemListener(itemEvent -> consumer.accept(checkboxMenuItem.getState()));
      popupMenu.add(checkboxMenuItem);
   }

   public void handleText(String text, String text2, TrayIcon.MessageType messageType) {
      if (((Optional)this.optionalMutableObject.getValue()).isPresent() && ConfigManager.getCloudSettings().notifications.getValue()) {
         ((TrayIcon)((Optional)this.optionalMutableObject.getValue()).get()).displayMessage(text, text2, messageType);
      }
   }

   public TrayManager(PhobosClient phobosClient) {
      try {
         if (SystemTray.isSupported()) {
            SystemTray systemTray = SystemTray.getSystemTray();
            PopupMenu popupMenu = new PopupMenu();
            MenuItem menuItem = new MenuItem("Exit");
            menuItem.addActionListener(actionEvent -> System.exit(0));
            popupMenu.add(new MenuItem(UserSession.userSession.getText()));
            popupMenu.addSeparator();
            this.handlePopupMenu(
               popupMenu,
               "Skins",
               ConfigManager.getCloudSettings().skins.getValue(),
               booleanValue -> ConfigManager.getCloudSettings().skins.setValue(booleanValue)
            );
            this.handlePopupMenu(
               popupMenu,
               "Capes",
               ConfigManager.getCloudSettings().capes.getValue(),
               booleanValue -> ConfigManager.getCloudSettings().capes.setValue(booleanValue)
            );
            this.handlePopupMenu(
               popupMenu,
               "Notifications",
               ConfigManager.getCloudSettings().notifications.getValue(),
               booleanValue -> ConfigManager.getCloudSettings().notifications.setValue(booleanValue)
            );
            this.handlePopupMenu(
               popupMenu,
               "Shared",
               ConfigManager.getCloudSettings().shared.getValue(),
               booleanValue -> ConfigManager.getCloudSettings().shared.setValue(booleanValue)
            );
            this.handlePopupMenu(
               popupMenu,
               "Theme",
               ConfigManager.getCloudSettings().theme.getValue(),
               booleanValue -> ConfigManager.getCloudSettings().theme.setValue(booleanValue)
            );
            popupMenu.addSeparator();
            popupMenu.add(menuItem);
            String text = System.getProperty("user.home");
            File file = new File(text, "earthhack/icon.png");
            if (!file.exists()) {
               phobosClient.handleText5("Tray", "Unable to load image.");
               return;
            }

            Image image = new ImageIcon(file.getAbsolutePath()).getImage();
            TrayIcon trayIcon = new TrayIcon(image, "3arthh4ck", popupMenu);
            trayIcon.setImageAutoSize(true);

            try {
               systemTray.add(trayIcon);
            } catch (AWTException aWTException) {
               phobosClient.handleText("Tray", "Failed to load into tray.");
            }

            this.optionalMutableObject.setValue(Optional.of(trayIcon));
         } else {
            phobosClient.handleText5("Tray", "Tray is not supported!");
         }
      } catch (Exception var11) {
      }
   }
}
