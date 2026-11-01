package org.phobos.core.config;

public class BuildInfo {
   public static final BuildInfo INSTANCE = new BuildInfo();
   public final String text;
   public final String text2;
   public final String text3 = "1.6.6";
   public final BuildInfo.Type type;

   public String describe() {
      return "219bb7c7e02d56ec8af6c20758501bd0f7b66750";
   }

   public static BuildInfo getINSTANCE() {
      return INSTANCE;
   }

   public BuildInfo() {
      this.text = "2026-01-18T19:07:10.799417400Z";
      this.text2 = "219bb7c7e02d56ec8af6c20758501bd0f7b66750";
      this.type = BuildInfo.Type.Release;
   }

   public String describe2() {
      return "2026-01-18T19:07:10.799417400Z";
   }

   public BuildInfo.Type getType() {
      return this.type;
   }

   public String describe3() {
      return "1.6.6";
   }

   public enum Type {
      Release,
      Beta;
   }
}
