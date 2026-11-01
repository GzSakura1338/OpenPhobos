package org.phobos.core.gui;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.phobos.api.MinecraftHolder;

public final class CursorUtils implements MinecraftHolder {
   public static ByteBuffer resolveByteBuffer(String text, IntBuffer intBuffer, IntBuffer intBuffer2, IntBuffer intBuffer3) {
      STBImage.stbi_set_flip_vertically_on_load(false);
      ByteBuffer byteBuffer = STBImage.stbi_load(text, intBuffer, intBuffer2, intBuffer3, 4);
      if (byteBuffer == null) {
         throw new RuntimeException("Failed to load %s".formatted(text));
      } else {
         return byteBuffer;
      }
   }

   public static void run2() {
      handleStyle(CursorUtils.Style.Default);
   }

   public static GLFWImage resolveGLFWImage(ByteBuffer byteBuffer, int i, int i2) {
      GLFWImage gLFWImage = GLFWImage.malloc();
      gLFWImage.set(i, i2, byteBuffer);
      return gLFWImage;
   }

   public static long value() {
      return mc.getWindow().getHandle();
   }

   public CursorUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static void handleStyle(CursorUtils.Style style) {
      if (style.isCustom() && !style.isCustomLoaded()) {
         style.load();
      }

      long l = GLFW.glfwCreateStandardCursor(style.getShape());
      GLFW.glfwSetCursor(value(), l);
   }

   public enum Style {
      Default(221185),
      Link(221188),
      Move(221193),
      Text(221186),
      Crosshair(221187),
      Resize_Sideways(221189),
      Resize_Vertical(221190),
      NIGGER("assets/cursors/cp_niggatronzoid_clankers.png");

      public long customPtr;
      public final boolean custom;
      public final int shape;
      public boolean customLoaded;
      public final String customPath;

      Style(String customPath) {
         this(-1, true, customPath);
      }

      public void load() {
         if (!this.custom) {
            throw new RuntimeException("Load called for a custom cursor");
         }

         MemoryStack stack = MemoryStack.stackPush();

         try {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);
            STBImage.stbi_set_flip_vertically_on_load(false);
            ByteBuffer image = STBImage.stbi_load(this.customPath, w, h, comp, 4);
            if (image == null) {
               throw new RuntimeException("Failed to load cursor image: " + STBImage.stbi_failure_reason());
            }

            GLFWImage cursorImg = GLFWImage.malloc(stack);
            cursorImg.set(w.get(0), h.get(0), image);
            long cursor = GLFW.glfwCreateCursor(cursorImg, 0, 0);
            STBImage.stbi_image_free(image);
            this.customPtr = cursor;
            this.customLoaded = true;
         } catch (Throwable var10) {
            if (stack != null) {
               try {
                  stack.close();
               } catch (Throwable var9) {
                  var10.addSuppressed(var9);
               }
            }

            throw var10;
         }

         if (stack != null) {
            stack.close();
         }
      }

      public boolean isCustomLoaded() {
         return this.customLoaded;
      }

      Style(int shape) {
         this(shape, false, null);
      }

      public long getCustomPtr() {
         return this.customPtr;
      }

      public String getCustomPath() {
         return this.customPath;
      }

      Style(final int shape, final boolean custom, final String customPath) {
         this.shape = shape;
         this.custom = custom;
         this.customPath = customPath;
      }

      public int getShape() {
         return this.shape;
      }

      public boolean isCustom() {
         return this.custom;
      }

      public void unload() {
         if (this.custom && this.customLoaded) {
            GLFW.glfwDestroyCursor(this.customPtr);
         }
      }
   }
}
