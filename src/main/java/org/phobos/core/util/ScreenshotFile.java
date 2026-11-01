package org.phobos.core.util;

import java.io.File;

public class ScreenshotFile {
   public final File file;

   public ScreenshotFile(File file) {
      this.file = file;
   }

   public File getFile() {
      return this.file;
   }
}
