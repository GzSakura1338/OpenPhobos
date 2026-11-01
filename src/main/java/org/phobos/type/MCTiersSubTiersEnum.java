package org.phobos.type;

import java.util.Arrays;
import java.util.Optional;

public enum MCTiersSubTiersEnum {
   MCTiers("MCTiers", "https://api.uku3lig.net/tiers", '\ue901'),
   SubTiers("SubTiers", "https://subtiers.net/api", '\ue902');

   public final char icon;
   public final String name;
   public final String url;

   public static Optional<MCTiersSubTiersEnum> findByUrl(String url) {
      if (url.endsWith("/")) {
         url = url.substring(0, url.length() - 1);
      }

      String finalUrl = url;
      return Arrays.stream(values()).filter(list -> list.url.equals(finalUrl)).findFirst();
   }

   public char getIcon() {
      return this.icon;
   }

   public String getName() {
      return this.name;
   }

   public String styledName(boolean current) {
      String s = this.icon + " " + this.name;
      if (current) {
         s = s + " (selected)";
      }

      return s;
   }

   MCTiersSubTiersEnum(final String name, final String url, final char icon) {
      this.name = name;
      this.url = url;
      this.icon = icon;
   }

   public String getUrl() {
      return this.url;
   }
}
