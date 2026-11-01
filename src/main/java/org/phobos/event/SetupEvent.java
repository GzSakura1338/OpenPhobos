package org.phobos.event;

import lombok.SneakyThrows;
import com.google.common.base.Preconditions;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.chars.Char2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.awt.Font;
import java.io.Closeable;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import lombok.NonNull;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.phobos.core.render.FontState;
import org.phobos.core.render.FontPage;
import org.phobos.core.util.BufferUtils;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.render.texture.TextureIdUtils;
import org.phobos.core.render.Render3d;
import org.phobos.data.URecord;

public class SetupEvent implements Closeable {
   public boolean flag;
   public final String text;
   public static final String text2 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_";
   public final Int2ObjectMap<ObjectList<SetupEvent.DrawEntry>> objectListInt2ObjectMap = new Int2ObjectOpenHashMap();
   public int intValue;
   public final float floatValue;
   public final ObjectList<FontPage> fontPageObjectList = new ObjectArrayList();
   public Font[] fontArray;
   public final ReentrantReadWriteLock reentrantReadWriteLock;
   public int intValue2;
   public static final Char2IntArrayMap char2IntArrayMap = new Char2IntArrayMap() {
      {
         this.put('0', 0);
         this.put('1', 170);
         this.put('2', 43520);
         this.put('3', 43690);
         this.put('4', 11141120);
         this.put('5', 11141290);
         this.put('6', 16755200);
         this.put('7', 11184810);
         this.put('8', 5592405);
         this.put('9', 5592575);
         this.put('A', 5635925);
         this.put('B', 5636095);
         this.put('C', 16733525);
         this.put('D', 16733695);
         this.put('E', 16777045);
         this.put('F', 16777215);
      }
   };
   public final int intValue3;
   public final int intValue4;
   public Future<Void> voidValueFuture;
   public static final ExecutorService executorService = Executors.newCachedThreadPool();

   public float value() {
      return 0.57F;
   }

   public void handleMatrixStack(MatrixStack matrixStack, String text, float f, float f2, float f3, float f4, float f5, float f6) {
      this.handleMatrixStack2(matrixStack, text, f - this.computeText(text) / 2.0F, f2, f3, f4, f5, f6);
   }

   public FontPage resolveFontPage(char c, char c2) {
      FontPage fontPage = new FontPage(c, c2, this.fontArray, this.intValue3);
      this.reentrantReadWriteLock.writeLock().lock();

      try {
         fontPage.run3();
         this.fontPageObjectList.add(fontPage);
      } finally {
         this.reentrantReadWriteLock.writeLock().unlock();
      }

      return fontPage;
   }

   @Override
   @SneakyThrows
   public void close() {
      if (this.voidValueFuture != null && !this.voidValueFuture.isDone() && !this.voidValueFuture.isCancelled()) {
         this.voidValueFuture.cancel(true);
         this.voidValueFuture.get();
         this.voidValueFuture = null;
      }

      this.reentrantReadWriteLock.writeLock().lock();

      try {
         ObjectListIterator throwable = this.fontPageObjectList.iterator();

         while (throwable.hasNext()) {
            FontPage fontPage = (FontPage)throwable.next();
            fontPage.run();
         }

         this.fontPageObjectList.clear();
         this.flag = false;
      } finally {
         this.reentrantReadWriteLock.writeLock().unlock();
      }
   }

   public URecord resolveURecord(char c) {
      this.reentrantReadWriteLock.readLock().lock();

      try {
         ObjectListIterator i = this.fontPageObjectList.iterator();

         while (i.hasNext()) {
            FontPage fontPage = (FontPage)i.next();
            if (fontPage.testChar(c)) {
               return fontPage.resolveURecord(c);
            }
         }
      } finally {
         this.reentrantReadWriteLock.readLock().unlock();
      }

      int var8 = computeInt(c, this.intValue4);
      FontPage fontPage2 = this.resolveFontPage((char)var8, (char)(var8 + this.intValue4));
      return fontPage2.resolveURecord(c);
   }

   public SetupEvent(Font[] fonts, float f) {
      this(fonts, f, 256, 5, null);
   }

   public void handleMatrixStack2(MatrixStack matrixStack, String text, float f, float f2, float f3, float f4, float f5, float f6) {
      this.handleMatrixStack3(matrixStack, text, f, f2, f3, f4, f5, f6, false);
   }

   public void run() {
      int i = Render3d.value();
      if (i != this.intValue2) {
         this.close();
         this.handleFont(this.fontArray, this.floatValue);
      }
   }

   public SetupEvent(@NonNull Font[] fonts, float f, int i, int i2) {
      this.intValue = 0;
      this.intValue2 = -1;
      this.reentrantReadWriteLock = new ReentrantReadWriteLock();
      if (fonts == null) {
         throw new NullPointerException("fonts is marked non-null but is null");
      }

      Preconditions.checkArgument(f > 0.0F, "sizePx <= 0");
      Preconditions.checkArgument(fonts.length > 0, "fonts.length <= 0");
      Preconditions.checkArgument(i > 4, "Unreasonable charactersPerPage count");
      Preconditions.checkArgument(i2 > 0, "paddingBetweenCharacters <= 0");
      this.floatValue = f;
      this.intValue4 = i;
      this.intValue3 = i2;
      this.text = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_";
      this.handleFont(fonts, f);
   }

   public void handleMatrixStack3(MatrixStack matrixStack, String text, float f, float f2, float f3, float f4, float f5, float f6, boolean flag) {
      if (this.voidValueFuture != null && !this.voidValueFuture.isDone()) {
         try {
            this.voidValueFuture.get();
         } catch (InterruptedException | ExecutionException exception) {
            exception.printStackTrace();
         }
      }

      this.run();
      float f18 = f3;
      float f19 = f4;
      float f20 = f5;
      matrixStack.push();
      matrixStack.translate(f, f2, 0.0F);
      matrixStack.scale(1.0F / this.intValue, 1.0F / this.intValue, 1.0F);
      boolean flag2 = FontState.getFontState().isFlag();
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
      Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
      float f21 = flag ? 0.25F : 1.0F;
      char[] chars = text.toCharArray();
      float f22 = 0.0F;
      float f23 = 0.0F;
      boolean flag3 = false;
      int i4 = 0;
      synchronized (this.objectListInt2ObjectMap) {
         for (int i2 = 0; i2 < chars.length; i2++) {
            char c2 = chars[i2];
            if (flag3) {
               flag3 = false;
               char c = Character.toUpperCase(c2);
               if (char2IntArrayMap.containsKey(c)) {
                  int i = char2IntArrayMap.get(c);
                  int[] ints = ColorUtils.values5(i);
                  f18 = ints[0] / 255.0F;
                  f19 = ints[1] / 255.0F;
                  f20 = ints[2] / 255.0F;
               } else if (c == 'R') {
                  f18 = f3;
                  f19 = f4;
                  f20 = f5;
               }
            } else if (c2 == 167) {
               flag3 = true;
            } else if (c2 == '\n') {
               f23 += this.computeText2(text.substring(i4, i2)) * this.intValue;
               f22 = 0.0F;
               i4 = i2 + 1;
            } else {
               URecord uRecord = this.resolveURecord(c2);
               if (uRecord != null) {
                  if (uRecord.value() != ' ') {
                     FontPage fontPage = uRecord.owner();
                     if (fontPage == null) {
                        continue;
                     }

                     NativeImageBackedTexture nativeImageBackedTexture = fontPage.nativeImageBackedTexture;
                     if (nativeImageBackedTexture == null) {
                        continue;
                     }

                     SetupEvent.DrawEntry drawEntry = new SetupEvent.DrawEntry(f22, f23, f18 * f21, f19 * f21, f20 * f21, uRecord);
                     ((ObjectList)this.objectListInt2ObjectMap.computeIfAbsent(nativeImageBackedTexture.getGlId(), i -> new ObjectArrayList())).add(drawEntry);
                  }

                  f22 += uRecord.width();
               }
            }
         }

         IntIterator var44 = this.objectListInt2ObjectMap.keySet().iterator();

         while (var44.hasNext()) {
            int i3 = (Integer)var44.next();
            RenderSystem.setShaderTexture(0, i3);
            if (flag2) {
               TextureIdUtils.setIntValue(i3);
               TextureIdUtils.run();
            }

            List<SetupEvent.DrawEntry> list = (List<SetupEvent.DrawEntry>)this.objectListInt2ObjectMap.get(i3);
            BufferBuilder bufferBuilder = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

            for (SetupEvent.DrawEntry drawEntry2 : list) {
               float f7 = drawEntry2.atX;
               float f8 = drawEntry2.atY;
               float f9 = drawEntry2.r;
               float f10 = drawEntry2.g;
               float f11 = drawEntry2.b;
               URecord uRecord2 = drawEntry2.toDraw;
               FontPage fontPage2 = uRecord2.owner();
               float f12 = uRecord2.width();
               float f13 = uRecord2.height();
               float f14 = (float)uRecord2.u() / fontPage2.intValue;
               float f15 = (float)uRecord2.v() / fontPage2.intValue3;
               float f16 = (float)(uRecord2.u() + uRecord2.width()) / fontPage2.intValue;
               float f17 = (float)(uRecord2.v() + uRecord2.height()) / fontPage2.intValue3;
               bufferBuilder.vertex(matrix4f, f7 + 0.0F, f8 + f13, 0.0F).texture(f14, f17).color(f9, f10, f11, f6);
               bufferBuilder.vertex(matrix4f, f7 + f12, f8 + f13, 0.0F).texture(f16, f17).color(f9, f10, f11, f6);
               bufferBuilder.vertex(matrix4f, f7 + f12, f8 + 0.0F, 0.0F).texture(f16, f15).color(f9, f10, f11, f6);
               bufferBuilder.vertex(matrix4f, f7 + 0.0F, f8 + 0.0F, 0.0F).texture(f14, f15).color(f9, f10, f11, f6);
            }

            BufferUtils.handleBufferBuilder(bufferBuilder);
            if (flag2) {
               TextureIdUtils.close();
            }
         }

         this.objectListInt2ObjectMap.clear();
      }

      matrixStack.pop();
   }

   public SetupEvent(@NonNull Font[] fonts, float f, int i, int i2, @Nullable String text) {
      this.intValue = 0;
      this.intValue2 = -1;
      this.reentrantReadWriteLock = new ReentrantReadWriteLock();
      if (fonts == null) {
         throw new NullPointerException("fonts is marked non-null but is null");
      }

      Preconditions.checkArgument(f > 0.0F, "sizePx <= 0");
      Preconditions.checkArgument(fonts.length > 0, "fonts.length <= 0");
      Preconditions.checkArgument(i > 4, "Unreasonable charactersPerPage count");
      Preconditions.checkArgument(i2 > 0, "paddingBetweenCharacters <= 0");
      this.floatValue = f;
      this.intValue4 = i;
      this.intValue3 = i2;
      this.text = text;
      this.handleFont(fonts, f);
   }

   public float computeText(String text) {
      char[] chars = describeText(text).toCharArray();
      float f = 0.0F;
      float f2 = 0.0F;

      for (char c : chars) {
         if (c == '\n') {
            f2 = Math.max(f, f2);
            f = 0.0F;
         } else {
            URecord uRecord = this.resolveURecord(c);
            if (uRecord != null) {
               f += (float)uRecord.width() / this.intValue;
            }
         }
      }

      return Math.max(f, f2);
   }

   public Future<Void> getFuture() {
      return executorService.submit(() -> {
         this.reentrantReadWriteLock.writeLock().lock();

         try {
            for (char c : this.text.toCharArray()) {
               if (Thread.interrupted()) {
                  break;
               }

               this.resolveURecord(c);
            }
         } finally {
            this.reentrantReadWriteLock.writeLock().unlock();
         }

         return null;
      });
   }

   public void handleMatrixStack4(MatrixStack matrixStack, String text, float f, float f2, float f3, float f4, float f5, float f6) {
      this.handleMatrixStack3(matrixStack, text, f + 1.0F, f2 + 1.0F, f3, f4, f5, f6, true);
      this.handleMatrixStack3(matrixStack, text, f, f2, f3, f4, f5, f6, false);
   }

   public static String describeText(String text) {
      char[] chars = text.toCharArray();
      StringBuilder stringBuilder = new StringBuilder();

      for (int i = 0; i < chars.length; i++) {
         char c = chars[i];
         if (c == 167) {
            i++;
         } else {
            stringBuilder.append(c);
         }
      }

      return stringBuilder.toString();
   }

   public float computeText2(String text) {
      char[] chars = describeText(text).toCharArray();
      if (chars.length == 0) {
         chars = new char[]{' '};
      }

      float f = 0.0F;
      float f2 = 0.0F;

      for (char c : chars) {
         if (c == '\n') {
            if (f == 0.0F) {
               URecord uRecord = this.resolveURecord(' ');
               if (uRecord != null) {
                  f = (float)uRecord.height() / this.intValue;
               }
            }

            f2 += f;
            f = 0.0F;
         } else {
            URecord uRecord2 = this.resolveURecord(c);
            if (uRecord2 != null) {
               f = Math.max((float)uRecord2.height() / this.intValue, f);
            }
         }
      }

      return f + f2;
   }

   public void handleFont(Font[] fonts, float f) {
      if (this.flag) {
         throw new IllegalStateException("Double call to init()");
      }

      this.reentrantReadWriteLock.writeLock().lock();

      try {
         this.intValue2 = Render3d.value();
         this.intValue = this.intValue2;
         this.fontArray = new Font[fonts.length];

         for (int i = 0; i < fonts.length; i++) {
            this.fontArray[i] = fonts[i].deriveFont(f * this.intValue);
         }

         if (this.text != null && !this.text.isEmpty()) {
            this.voidValueFuture = this.getFuture();
         }

         this.flag = true;
      } finally {
         this.reentrantReadWriteLock.writeLock().unlock();
      }
   }

   public static int computeInt(int i, int i2) {
      return i2 * (int)Math.floor((double)i / i2);
   }

   public record DrawEntry(float atX, float atY, float r, float g, float b, URecord toDraw) {
   }
}
