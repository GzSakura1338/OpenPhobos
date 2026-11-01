package org.phobos.core.loader;

import java.util.List;
import org.phobos.api.LoadableModule;

public class ModuleClassData implements LoadableModule {
   public final byte[] byteArray;
   public final List<String> textList;
   public final String name;

   @Override
   public byte[] getByteArray() {
      return this.byteArray;
   }

   public ModuleClassData(String text, byte[] bytes, List<String> list) {
      this.name = text;
      this.byteArray = bytes;
      this.textList = list;
   }

   @Override
   public boolean testList(List<String> list) {
      if (this.textList.isEmpty()) {
         return true;
      }

      for (String text : this.textList) {
         if (!list.contains(text)) {
            return false;
         }
      }

      return true;
   }

   @Override
   public List<String> getTextList() {
      return this.textList;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public Class<?> getClassValue() throws ClassCastException, IllegalAccessException {
      DefiningClassLoader definingClassLoader = new DefiningClassLoader(Thread.currentThread().getContextClassLoader());
      return definingClassLoader.resolveClassValue2(this.name.replaceAll("/", "."), this.byteArray);
   }
}
