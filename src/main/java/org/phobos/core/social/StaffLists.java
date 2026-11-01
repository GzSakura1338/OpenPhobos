package org.phobos.core.social;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.PhobosLog;

public class StaffLists implements MinecraftHolder {
   public final Map<String, Set<String>> textMap;
   public final MutableObject<Set<String>> setMutableObject = new MutableObject();

   public MutableObject<Set<String>> getSetMutableObject() {
      return this.setMutableObject;
   }

   public void handleText(String text) {
      this.setMutableObject.setValue(this.textMap.get(text));
   }

   public boolean contains(String text) {
      Set<String> set = (Set<String>)this.setMutableObject.getValue();
      if (set == null) {
         return false;
      }

      for (String text2 : set) {
         if (text2.equalsIgnoreCase(text)) {
            return true;
         }
      }

      return false;
   }

   public StaffLists() {
      this.textMap = new HashMap<>();
   }

   public Map<String, Set<String>> getTextMap() {
      return this.textMap;
   }

   public void handlePath(Path path, String text) {
      Path path2 = path.resolve(text + ".txt");
      File file = path2.toFile();
      if (!file.exists()) {
         PhobosLog.handleText("Staff", "Failed to load stafflist for %s".formatted(text));
      } else {
         try (
            InputStream inputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
         ) {
            Set<String> set = new HashSet<>();

            String text4;
            while ((text4 = bufferedReader.readLine()) != null) {
               String[] texts = text4.split(",");

               for (String text3 : texts) {
                  String text2 = text3.trim();
                  if (!text2.isEmpty()) {
                     set.add(text2);
                  }
               }
            }

            this.textMap.put(text, set);
         } catch (Throwable var19) {
         }
      }
   }

   public void run3() {
      String text = System.getProperty("user.home");
      Path path = Path.of(text, "earthhack", "staff");
      if (!path.toFile().exists()) {
         PhobosLog.handleText("Staff", "Unable to find staff folder");
      } else {
         this.handlePath(path, "blocksmc");
         this.handlePath(path, "cubecraft");
         this.handlePath(path, "hypixel");
      }
   }
}
