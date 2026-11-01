package org.phobos.core.script;

public class ScriptKeywords {
   public static String[] textArray = new String[]{"null", "nil", "nullptr"};
   public static String[] textArray2 = new String[]{"int", "integer", "int32"};
   public static String[] textArray3 = new String[]{"boolean", "bl", "bool"};
   public static String[] textArray4 = new String[]{"for", "foreach", "do"};
   public static String[] textArray5 = new String[]{"var", "val", "let", "typedef"};
   public static String[] textArray6 = new String[]{"void", "nothing", "none"};
   public static String[] textArray7 = new String[]{"struct", "class", "data", "classdef"};
   public static String[] textArray8 = new String[]{"string", "str"};
   public static String[] textArray9 = new String[]{"double", "float", "floating", "precise"};
   public static String[] textArray10 = new String[]{"break", "stop"};
   public static String[] textArray11 = new String[]{"func", "def", "method", "function", "fun", "fn"};
   public static String[] textArray12 = new String[]{"eventlistener", "eventhandler"};
   public static String text = "^(int|integer|double|float|floating|precise|boolean|bool|bl|string|let|var|variable|typedef)\\s+(const|static|final)?\\s*([a-zA-Z_]\\w*)\\s*(=|#|=>|:)?\\s*([^;]*)\\s*;";
   public static String[] textArray13 = new String[]{"const", "final", "immutable"};

   public static boolean testText(String text) {
      String text2 = text.trim();
      return text2.startsWith("//") || text2.startsWith("#");
   }
}
