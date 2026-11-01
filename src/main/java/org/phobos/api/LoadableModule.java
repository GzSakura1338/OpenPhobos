package org.phobos.api;

import java.util.List;

public interface LoadableModule extends Named {
   List<String> getTextList();

   boolean testList(List<String> var1);

   byte[] getByteArray();

   Class<?> getClassValue() throws Exception;
}
