package org.phobos.core.loader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.phobos.api.ExceptionPolicy;

public class ClassScanner {
   public static <T> void handleExceptionPolicy(ExceptionPolicy exceptionPolicy, Class<T> classValue, T object, String text, Object... objects) {
      try {
         classValue.getDeclaredMethod(text, objects.getClass()).invoke(object, objects);
      } catch (Exception exception) {
         exceptionPolicy.handleException(exception);
      }
   }

   public static List<Class<?>> resolveList(File file, String text) throws ClassNotFoundException {
      List<Class<?>> list = new ArrayList<>();
      if (!file.exists()) {
         return list;
      }

      File[] files = file.listFiles();
      assert files != null;

      for (File file2 : files) {
         if (file2.isDirectory()) {
            assert !file2.getName().contains(".");
            list.addAll(resolveList(file2, text + "." + file2.getName()));
         } else if (file2.getName().endsWith(".class")) {
            list.add(Class.forName(text + "." + file2.getName().substring(0, file2.getName().length() - 6)));
         }
      }

      return list;
   }

   public static Class<?>[] values(String text) throws ClassNotFoundException, IOException {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      assert classLoader != null;
      String text2 = text.replace('.', '/');
      Enumeration<URL> enumeration = classLoader.getResources(text2);
      List<File> list = new ArrayList<>();

      while (enumeration.hasMoreElements()) {
         URL uRL = enumeration.nextElement();
         list.add(new File(uRL.getFile()));
      }

      ArrayList<Class<?>> arrayList = new ArrayList<>();

      for (File file : list) {
         arrayList.addAll(resolveList(file, text));
      }

      return arrayList.toArray(new Class[0]);
   }
}
