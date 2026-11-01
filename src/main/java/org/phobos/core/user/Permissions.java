package org.phobos.core.user;

import java.awt.Color;
import java.util.List;

public class Permissions {
   public enum Permission {
      User,
      Beta(List.of("beta"), "§b", Color.CYAN),
      Moderator(List.of("beta", "mod"), "§a", Color.GREEN),
      Admin(List.of("beta", "mod", "admin"), "§c", Color.RED);

      public final List<String> flags;
      public final String formatting;
      public final Color color;

      public boolean isModerator() {
         return this.flags.contains("mod");
      }

      public String getFormatting() {
         return this.formatting;
      }

      public Color getColor() {
         return this.color;
      }

      public boolean isBeta() {
         return this.flags.contains("beta");
      }

      Permission(List<String> flags, String formatting, Color color) {
         this.flags = flags;
         this.formatting = formatting;
         this.color = color;
      }

      public boolean isAdmin() {
         return this.flags.contains("admin");
      }

      public List<String> getFlags() {
         return this.flags;
      }

      Permission() {
         this.flags = List.of();
         this.formatting = "";
         this.color = Color.WHITE;
      }
   }
}
