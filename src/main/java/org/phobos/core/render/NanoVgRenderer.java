package org.phobos.core.render;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL3;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;
import org.phobos.api.MinecraftHolder;
import org.phobos.module.client.FontModule;
import org.phobos.core.PhobosLog;
import org.phobos.core.chat.TextFormattingUtils;

public class NanoVgRenderer implements MinecraftHolder {
   public int intValue;
   public int intValue2;
   public int intValue3;
   public boolean flag;
   public int intValue4;
   public int intValue5;
   public boolean flag2;
   public int intValue6;
   public static final float floatValue = 0.0F;
   public int intValue7;
   public int intValue8;
   public boolean flag3;
   public final boolean[] flagArray;
   public int intValue9;
   public int intValue10;
   public int intValue11;
   public boolean flag4;
   public boolean flag5;
   public int intValue12;
   public int intValue13;
   public int intValue14 = -1;
   public ByteBuffer byteBuffer;
   public int intValue15;
   public boolean flag6;
   public int intValue16;
   public int intValue17;
   public long longValue;

   public void run6() {
      if (this.longValue != 0L && this.flag4) {
         this.flag4 = false;
         NanoVGGL3.nvgDelete(this.longValue);
         this.longValue = 0L;
         this.intValue14 = -1;
         this.run();
         this.run5();
      }
   }

   public boolean isFlag5() {
      return this.flag5;
   }

   public void handleFloat(float f, float f2, float f3, Color color) {
      NanoVG.nvgBeginPath(this.longValue);
      NanoVG.nvgCircle(this.longValue, f, f2, f3);
      NanoVG.nvgFillColor(this.longValue, this.resolveNVGColor(color));
      NanoVG.nvgFill(this.longValue);
      NanoVG.nvgClosePath(this.longValue);
   }

   public boolean isFlag6() {
      return this.flag6;
   }

   public float computeText(String text) {
      float[] floats = new float[4];
      NanoVG.nvgTextBounds(this.longValue, 0.0F, 0.0F, TextFormattingUtils.describeText(text), floats);
      return floats[3] - floats[1];
   }

   public boolean isFlag4() {
      return this.flag4;
   }

   public void run4() {
      NanoVG.nvgEndFrame(this.longValue);
      GL20.glUseProgram(this.intValue);
      GL20.glBlendFunc(this.intValue12, this.intValue8);
      if (this.flag3) {
         GL20.glEnable(2929);
      } else {
         GL20.glDisable(2929);
      }

      if (this.flag2) {
         GL20.glEnable(3089);
      } else {
         GL20.glDisable(3089);
      }

      if (this.flag5) {
         GL20.glEnable(2884);
      } else {
         GL20.glDisable(2884);
      }

      if (this.flag) {
         GL20.glEnable(3042);
      } else {
         GL20.glDisable(3042);
      }

      GL20.glCullFace(this.intValue7);
      GL20.glColorMask(this.flagArray[0], this.flagArray[1], this.flagArray[2], this.flagArray[3]);
      GL20.glStencilMask(this.intValue16);
      GL20.glStencilOp(7680, 7680, 7680);
      GL20.glStencilFunc(this.intValue2, this.intValue13, -1);
      GL13.glActiveTexture(this.intValue15);
      GL30.glBindVertexArray(this.intValue3);
      GL15.glBindBuffer(34962, this.intValue17);
      GL20.glBindTexture(3553, this.intValue10);
   }

   public void handleText(String text, float f, float f2, float f3, Color color, Color color2, boolean flag, float f4) {
      Color color3 = color;
      text = text.trim();
      String[] texts = text.split("§");
      boolean flag2 = false;
      if (texts.length > 0) {
         String text2 = texts[0];
         if (flag) {
            this.handleText3(text2, f, f2, f3, color, color2, f4);
         } else {
            this.handleText4(text2, f, f2, f3, color);
         }

         f += this.computeText2(text2, f3) + (text2.endsWith(" ") ? this.computeText2("a", f3) / 1.5F : 0.0F);
      }

      for (int i = 1; i < texts.length; i++) {
         String text4 = texts[i];
         if (!text4.isEmpty()) {
            switch (text4.charAt(0)) {
               case '#':
                  if (text4.length() < 7) {
                     continue;
                  }

                  String text3 = text4.substring(0, 7);
                  color3 = Color.decode(text3);
                  text4 = text4.substring(6);
                  break;
               case '$':
               case '%':
               case '&':
               case '\'':
               case '(':
               case ')':
               case '*':
               case '+':
               case ',':
               case '-':
               case '.':
               case '/':
               case ':':
               case ';':
               case '<':
               case '=':
               case '>':
               case '?':
               case '@':
               case 'A':
               case 'B':
               case 'C':
               case 'D':
               case 'E':
               case 'F':
               case 'G':
               case 'H':
               case 'I':
               case 'J':
               case 'K':
               case 'L':
               case 'M':
               case 'N':
               case 'O':
               case 'P':
               case 'Q':
               case 'R':
               case 'S':
               case 'T':
               case 'U':
               case 'V':
               case 'W':
               case 'X':
               case 'Y':
               case 'Z':
               case '[':
               case '\\':
               case ']':
               case '^':
               case '_':
               case '`':
               case 'g':
               case 'h':
               case 'i':
               case 'j':
               case 'k':
               case 'l':
               case 'm':
               case 'p':
               case 'q':
               default:
                  color3 = color;
                  break;
               case '0':
                  color3 = Color.BLACK;
                  break;
               case '1':
                  color3 = new Color(170);
                  break;
               case '2':
                  color3 = new Color(43520);
                  break;
               case '3':
                  color3 = new Color(43690);
                  break;
               case '4':
                  color3 = new Color(11141120);
                  break;
               case '5':
                  color3 = new Color(11141290);
                  break;
               case '6':
                  color3 = new Color(16755200);
                  break;
               case '7':
                  color3 = Color.GRAY;
                  break;
               case '8':
                  color3 = Color.DARK_GRAY;
                  break;
               case '9':
                  color3 = Color.BLUE;
                  break;
               case 'a':
                  color3 = Color.GREEN;
                  break;
               case 'b':
                  color3 = new Color(5636095);
                  break;
               case 'c':
                  color3 = Color.RED;
                  break;
               case 'd':
                  color3 = new Color(16733695);
                  break;
               case 'e':
                  color3 = Color.YELLOW;
                  break;
               case 'f':
                  color3 = Color.WHITE;
                  break;
               case 'n':
                  flag = true;
                  break;
               case 'o':
                  flag = false;
                  break;
               case 'r':
                  color3 = Color.WHITE;
                  flag2 = true;
            }

            if (text4.length() > 1) {
               if (color3 != color || flag2) {
                  text4 = text4.substring(1);
               }

               flag2 = false;
               if (flag) {
                  this.handleText3(text4, f, f2, f3, color3, color2, f4);
               } else {
                  this.handleText4(text4, f, f2, f3, color3);
               }

               f += this.computeText2(text4, f3) + (text4.endsWith(" ") ? this.computeText2("a", f3) / 1.5F : 0.0F);
            }
         }
      }
   }

   public void handleText2(String text, float f, float f2, float f3, Color color, boolean flag) {
      float f4 = color.getAlpha() / 255.0F;
      Color color2 = color;
      Color color3 = new Color((color2.getRGB() & 16579836) >> 2 | color2.getRGB() & 0xFF000000);
      text = text.trim();
      String[] texts = text.split("§");
      boolean flag2 = false;
      if (texts.length > 0) {
         String text2 = texts[0];
         if (flag) {
            this.handleText5(text2, f, f2, f3, color, color3);
         } else {
            this.handleText4(text2, f, f2, f3, color);
         }

         f += this.computeText2(text2, f3) + (text2.endsWith(" ") ? this.computeText2("a", f3) / 1.5F : 0.0F);
      }

      for (int i = 1; i < texts.length; i++) {
         String text4 = texts[i];
         if (!text4.isEmpty()) {
            switch (text4.charAt(0)) {
               case '#':
                  if (text4.length() < 7) {
                     continue;
                  }

                  String text3 = text4.substring(0, 7);
                  color2 = Color.decode(text3);
                  text4 = text4.substring(6);
                  break;
               case '$':
               case '%':
               case '&':
               case '\'':
               case '(':
               case ')':
               case '*':
               case '+':
               case ',':
               case '-':
               case '.':
               case '/':
               case ':':
               case ';':
               case '<':
               case '=':
               case '>':
               case '?':
               case '@':
               case 'A':
               case 'B':
               case 'C':
               case 'D':
               case 'E':
               case 'F':
               case 'G':
               case 'H':
               case 'I':
               case 'J':
               case 'K':
               case 'L':
               case 'M':
               case 'N':
               case 'O':
               case 'P':
               case 'Q':
               case 'R':
               case 'S':
               case 'T':
               case 'U':
               case 'V':
               case 'W':
               case 'X':
               case 'Y':
               case 'Z':
               case '[':
               case '\\':
               case ']':
               case '^':
               case '_':
               case '`':
               case 'g':
               case 'h':
               case 'i':
               case 'j':
               case 'k':
               case 'l':
               case 'm':
               case 'p':
               case 'q':
               default:
                  color2 = color;
                  break;
               case '0':
                  color2 = Color.BLACK;
                  break;
               case '1':
                  color2 = new Color(170);
                  break;
               case '2':
                  color2 = new Color(43520);
                  break;
               case '3':
                  color2 = new Color(43690);
                  break;
               case '4':
                  color2 = new Color(11141120);
                  break;
               case '5':
                  color2 = new Color(11141290);
                  break;
               case '6':
                  color2 = new Color(16755200);
                  break;
               case '7':
                  color2 = Color.GRAY;
                  break;
               case '8':
                  color2 = Color.DARK_GRAY;
                  break;
               case '9':
                  color2 = Color.BLUE;
                  break;
               case 'a':
                  color2 = Color.GREEN;
                  break;
               case 'b':
                  color2 = new Color(5636095);
                  break;
               case 'c':
                  color2 = Color.RED;
                  break;
               case 'd':
                  color2 = new Color(16733695);
                  break;
               case 'e':
                  color2 = Color.YELLOW;
                  break;
               case 'f':
                  color2 = Color.WHITE;
                  break;
               case 'n':
                  flag = true;
                  break;
               case 'o':
                  flag = false;
                  break;
               case 'r':
                  color2 = Color.WHITE;
                  flag2 = true;
            }

            color2 = ColorUtils.createColor6(color2, f4);
            if (color2 != color || flag2) {
               color3 = new Color((color2.getRGB() & 16579836) >> 2 | color2.getRGB() & 0xFF000000);
            }

            if (text4.length() > 1) {
               if (color2 != color || flag2) {
                  text4 = text4.substring(1);
               }

               flag2 = false;
               if (flag) {
                  this.handleText5(text4, f, f2, f3, color2, color3);
               } else {
                  this.handleText4(text4, f, f2, f3, color2);
               }

               f += this.computeText2(text4, f3) + (text4.endsWith(" ") ? this.computeText2("a", f3) / 1.5F : 0.0F);
            }
         }
      }
   }

   public NVGColor resolveNVGColor(Color color) {
      NVGColor nVGColor = NVGColor.create();
      nVGColor.r(color.getRed() / 255.0F);
      nVGColor.g(color.getGreen() / 255.0F);
      nVGColor.b(color.getBlue() / 255.0F);
      nVGColor.a(color.getAlpha() / 255.0F);
      return nVGColor;
   }

   public void run() {
      if (this.byteBuffer != null) {
         MemoryUtil.memFree(this.byteBuffer);
         this.byteBuffer = null;
      }
   }

   public boolean isFlag2() {
      return this.flag2;
   }

   public void handleText3(String text, float f, float f2, float f3, Color color, Color color2, float f4) {
      FontModule fontModule = FontModule.getFontModule();
      if (fontModule != null) {
         NanoVG.nvgBeginPath(this.longValue);
         NanoVG.nvgFontFaceId(this.longValue, this.intValue14);
         NanoVG.nvgFontSize(this.longValue, f3);
         NanoVG.nvgTextAlign(this.longValue, 9);
         NanoVG.nvgFontBlur(this.longValue, f4);
         NanoVG.nvgFillColor(this.longValue, this.resolveNVGColor(color2));
         NanoVG.nvgText(this.longValue, f, f2, text);
         NanoVG.nvgFontBlur(this.longValue, 0.0F);
         NanoVG.nvgFillColor(this.longValue, this.resolveNVGColor(color));
         NanoVG.nvgText(this.longValue, f, f2, text);
         NanoVG.nvgClosePath(this.longValue);
      }
   }

   public void handleText4(String text, float f, float f2, float f3, Color color) {
      NanoVG.nvgBeginPath(this.longValue);
      NanoVG.nvgFontFaceId(this.longValue, this.intValue14);
      NanoVG.nvgFillColor(this.longValue, this.resolveNVGColor(color));
      NanoVG.nvgFontSize(this.longValue, f3);
      NanoVG.nvgFontBlur(this.longValue, 0.0F);
      NanoVG.nvgTextAlign(this.longValue, 9);
      NanoVG.nvgText(this.longValue, f, f2, text);
      NanoVG.nvgClosePath(this.longValue);
   }

   public void run5() {
      FontModule fontModule = FontModule.getFontModule();
      if (fontModule != null) {
         this.longValue = NanoVGGL3.nvgCreate(1);

         try {
            String text = fontModule.getFont2d().getValue();
            byte[] bytes = fontModule.values();
            this.run();
            this.byteBuffer = MemoryUtil.memAlloc(bytes.length);
            this.byteBuffer.put(bytes);
            this.byteBuffer.flip();
            if (NanoVG.nvgCreateFontMem(this.longValue, text, this.byteBuffer, false) == -1) {
               throw new RuntimeException("Failed to create NanoVG font.");
            }

            this.intValue14 = NanoVG.nvgFindFont(this.longValue, text);
            if (this.intValue14 == -1) {
               PhobosLog.handleText("Font", "Unable to find %s".formatted(text));
               fontModule.run11();
            }

            PhobosLog.handleText("Font", "Loaded %s".formatted(text));
            this.flag4 = true;
         } catch (Exception exception) {
            exception.printStackTrace();
            fontModule.run11();
         }
      }
   }

   public void handleFloat2(float f, float f2, float f3, float f4, float f5, Color color) {
      NanoVG.nvgBeginPath(this.longValue);
      NanoVG.nvgRoundedRect(this.longValue, f, f2, f3, f4, f5);
      NanoVG.nvgFillColor(this.longValue, this.resolveNVGColor(color));
      NanoVG.nvgFill(this.longValue);
      NanoVG.nvgClosePath(this.longValue);
   }

   public void start() {
      IntBuffer intBuffer = BufferUtils.createIntBuffer(1);
      GL20.glGetIntegerv(35725, intBuffer);
      this.intValue = intBuffer.get(0);
      intBuffer.clear();
      GL20.glGetIntegerv(3041, intBuffer);
      this.intValue12 = intBuffer.get(0);
      intBuffer.clear();
      GL20.glGetIntegerv(3040, intBuffer);
      this.intValue8 = intBuffer.get(0);
      intBuffer.clear();
      GL20.glGetIntegerv(35097, intBuffer);
      this.intValue9 = intBuffer.get(0);
      intBuffer.clear();
      this.flag3 = GL20.glIsEnabled(2929);
      this.flag2 = GL20.glIsEnabled(3089);
      this.flag5 = GL20.glIsEnabled(2884);
      this.flag = GL20.glIsEnabled(3042);
      ByteBuffer byteBuffer = BufferUtils.createByteBuffer(4);
      GL20.glGetBooleanv(3107, byteBuffer);

      for (int i = 0; i < 4; i++) {
         this.flagArray[i] = byteBuffer.get(i) != 0;
      }

      GL20.glGetIntegerv(2968, intBuffer);
      this.intValue16 = intBuffer.get(0);
      intBuffer.clear();
      GL20.glGetIntegerv(2962, intBuffer);
      this.intValue2 = intBuffer.get(0);
      intBuffer.clear();
      GL20.glGetIntegerv(2963, intBuffer);
      this.intValue13 = intBuffer.get(0);
      intBuffer.clear();
      GL20.glGetIntegerv(34016, intBuffer);
      this.intValue15 = intBuffer.get(0);
      intBuffer.clear();
      GL20.glGetIntegerv(34229, intBuffer);
      this.intValue3 = intBuffer.get(0);
      intBuffer.clear();
      GL20.glGetIntegerv(34964, intBuffer);
      this.intValue17 = intBuffer.get(0);
      intBuffer.clear();
      GL20.glGetIntegerv(32873, intBuffer);
      this.intValue10 = intBuffer.get(0);
      intBuffer.clear();
      GL20.glGetIntegerv(2885, intBuffer);
      this.intValue7 = intBuffer.get(0);
      intBuffer.clear();

      try {
         NanoVG.nvgBeginFrame(this.longValue, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), 3.0F);
      } catch (NullPointerException nullPointerException) {
         nullPointerException.printStackTrace();
      }
   }

   public void handleText5(String text, float f, float f2, float f3, Color color, Color color2) {
      FontModule fontModule = FontModule.getFontModule();
      if (fontModule != null) {
         NanoVG.nvgBeginPath(this.longValue);
         NanoVG.nvgFontFaceId(this.longValue, this.intValue14);
         NanoVG.nvgFontSize(this.longValue, f3);
         NanoVG.nvgTextAlign(this.longValue, 9);
         NanoVG.nvgFontBlur(this.longValue, 0.0F);
         NanoVG.nvgFillColor(this.longValue, this.resolveNVGColor(color2));
         NanoVG.nvgText(this.longValue, f + 1.0F, f2 + 1.0F, text);
         NanoVG.nvgFontBlur(this.longValue, 0.0F);
         NanoVG.nvgFillColor(this.longValue, this.resolveNVGColor(color));
         NanoVG.nvgText(this.longValue, f, f2, text);
         NanoVG.nvgClosePath(this.longValue);
      }
   }

   public float computeText2(String text, float f) {
      float[] floats = new float[4];
      NanoVG.nvgFontSize(this.longValue, f);
      NanoVG.nvgTextBounds(this.longValue, 0.0F, 0.0F, TextFormattingUtils.describeText(text), floats);
      return floats[2] - floats[0];
   }

   public boolean isFlag3() {
      return this.flag3;
   }

   public NanoVgRenderer() {
      this.flag4 = false;
      this.flagArray = new boolean[4];
      this.byteBuffer = null;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public long getLongValue() {
      return this.longValue;
   }

   public void handleText6(String text, float f, float f2, float f3, Color color, boolean flag) {
      this.handleText7(text, f, f2, f3, color, flag, 5.0F);
   }

   public void handleText7(String text, float f, float f2, float f3, Color color, boolean flag, float f4) {
      float f5 = color.getAlpha() / 255.0F;
      Color color2 = color;
      Color color3 = new Color((color2.getRGB() & 16579836) >> 2 | color2.getRGB() & -202116109);
      text = text.trim();
      String[] texts = text.split("§");
      boolean flag2 = false;
      if (texts.length > 0) {
         String text2 = texts[0];
         if (flag) {
            this.handleText3(text2, f, f2, f3, color, color3, f4);
         } else {
            this.handleText4(text2, f, f2, f3, color);
         }

         f += this.computeText2(text2, f3) + (text2.endsWith(" ") ? this.computeText2("a", f3) / 1.5F : 0.0F);
      }

      for (int i = 1; i < texts.length; i++) {
         String text4 = texts[i];
         if (!text4.isEmpty()) {
            switch (text4.charAt(0)) {
               case '#':
                  if (text4.length() < 7) {
                     continue;
                  }

                  String text3 = text4.substring(0, 7);
                  color2 = Color.decode(text3);
                  text4 = text4.substring(6);
                  break;
               case '$':
               case '%':
               case '&':
               case '\'':
               case '(':
               case ')':
               case '*':
               case '+':
               case ',':
               case '-':
               case '.':
               case '/':
               case ':':
               case ';':
               case '<':
               case '=':
               case '>':
               case '?':
               case '@':
               case 'A':
               case 'B':
               case 'C':
               case 'D':
               case 'E':
               case 'F':
               case 'G':
               case 'H':
               case 'I':
               case 'J':
               case 'K':
               case 'L':
               case 'M':
               case 'N':
               case 'O':
               case 'P':
               case 'Q':
               case 'R':
               case 'S':
               case 'T':
               case 'U':
               case 'V':
               case 'W':
               case 'X':
               case 'Y':
               case 'Z':
               case '[':
               case '\\':
               case ']':
               case '^':
               case '_':
               case '`':
               case 'g':
               case 'h':
               case 'i':
               case 'j':
               case 'k':
               case 'l':
               case 'm':
               case 'p':
               case 'q':
               default:
                  color2 = color;
                  break;
               case '0':
                  color2 = Color.BLACK;
                  break;
               case '1':
                  color2 = new Color(170);
                  break;
               case '2':
                  color2 = new Color(43520);
                  break;
               case '3':
                  color2 = new Color(43690);
                  break;
               case '4':
                  color2 = new Color(11141120);
                  break;
               case '5':
                  color2 = new Color(11141290);
                  break;
               case '6':
                  color2 = new Color(16755200);
                  break;
               case '7':
                  color2 = Color.GRAY;
                  break;
               case '8':
                  color2 = Color.DARK_GRAY;
                  break;
               case '9':
                  color2 = Color.BLUE;
                  break;
               case 'a':
                  color2 = Color.GREEN;
                  break;
               case 'b':
                  color2 = new Color(5636095);
                  break;
               case 'c':
                  color2 = Color.RED;
                  break;
               case 'd':
                  color2 = new Color(16733695);
                  break;
               case 'e':
                  color2 = Color.YELLOW;
                  break;
               case 'f':
                  color2 = Color.WHITE;
                  break;
               case 'n':
                  flag = true;
                  break;
               case 'o':
                  flag = false;
                  break;
               case 'r':
                  color2 = Color.WHITE;
                  flag2 = true;
            }

            color2 = ColorUtils.createColor6(color2, f5);
            if (color2 != color) {
               color3 = new Color((color2.getRGB() & 16579836) >> 2 | color2.getRGB() & -202116109);
            }

            if (text4.length() > 1) {
               if (color2 != color || flag2) {
                  text4 = text4.substring(1);
               }

               flag2 = false;
               if (flag) {
                  this.handleText3(text4, f, f2, f3, color2, color3, f4);
               } else {
                  this.handleText4(text4, f, f2, f3, color2);
               }

               f += this.computeText2(text4, f3) + (text4.endsWith(" ") ? this.computeText2("a", f3) / 1.5F : 0.0F);
            }
         }
      }
   }

   public void handleFloat3(float f, float f2, float f3, float f4, Color color) {
      NVGPaint nVGPaint = NanoVG.nvgRadialGradient(
         this.longValue, f, f2, 1.0F - f3 / f4, f3, this.resolveNVGColor(color), this.resolveNVGColor(TextFormattingUtils.color), NVGPaint.create()
      );
      NanoVG.nvgBeginPath(this.longValue);
      NanoVG.nvgCircle(this.longValue, f, f2, f3);
      NanoVG.nvgFillPaint(this.longValue, nVGPaint);
      NanoVG.nvgFill(this.longValue);
      NanoVG.nvgClosePath(this.longValue);
   }
}
