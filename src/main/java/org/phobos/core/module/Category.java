package org.phobos.core.module;

import org.phobos.api.Named;

public class Category implements Named {
   public final String name;

   @Override
   public String getName() {
      return this.name;
   }

   public Category(String text) {
      this.name = text;
   }
}
