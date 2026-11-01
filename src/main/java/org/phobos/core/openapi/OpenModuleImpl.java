package org.phobos.core.openapi;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import org.phobos.module.Module;
import org.phobos.openapi.OpenModule;
import org.phobos.openapi.OpenSetting;
import org.phobos.core.setting.Setting;

public class OpenModuleImpl implements OpenModule {
   public Module module;

   @Override
   public void run() {
      Module var1 = this.module;
      var1.run12();
   }

   @Override
   public boolean check() {
      Module var1 = this.module;
      return var1.check7();
   }

   @Nullable
   @Override
   public String describe2() {
      Module var1 = this.module;
      return var1.getText3();
   }

   @Override
   public void run3() {
      Module var1 = this.module;
      var1.run9();
   }

   @Override
   public <T> Optional<OpenSetting<T>> resolveOptional(String text, Class<T> classValue) {
      Module var3 = this.module;
      Optional var4 = var3.resolveOptional2(text, classValue);
      boolean var5 = var4.isPresent();
      Optional var7;
      if (!var5) {
         Optional var6 = Optional.empty();
         var7 = var6;
      } else {
         Object var8 = var4.get();
         boolean var9 = var8 instanceof Setting;
         if (var8 != null && !var9) {
            throw new ClassCastException("CHECKCAST failed");
         }

         SettingAdapter var10 = new SettingAdapter();
         Optional var11 = Optional.of(var10);
         var7 = var11;
      }

      return var7;
   }

   @Override
   public void run2() {
      Module var1 = this.module;
      var1.run11();
   }

   @Nullable
   @Override
   public String describe() {
      Module var1 = this.module;
      return var1.nothing();
   }
}
