package org.phobos.core.system;

public class OsDetector {
   public static final OsDetector INSTANCE = new OsDetector();
   public OsDetector.OS oS;

   public OsDetector.OS getOS() {
      return this.oS;
   }

   public boolean check() {
      return this.oS.equals(OsDetector.OS.Windows);
   }

   public void setOS(OsDetector.OS oS) {
      this.oS = oS;
   }

   public void run() {
      String text = System.getProperty("os.name");
      if (text.contains("Windows")) {
         this.setOS(OsDetector.OS.Windows);
      } else if (text.contains("Linux")) {
         this.setOS(OsDetector.OS.Linux);
      }
   }

   public static OsDetector getINSTANCE() {
      return INSTANCE;
   }

   public enum OS {
      Windows,
      Linux;
   }
}
