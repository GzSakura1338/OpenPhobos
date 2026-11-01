package org.phobos.core.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.texture.NativeImageBackedTexture;
import org.phobos.data.URecord;

public class FontPage {
   public final char charValue;
   public boolean flag = false;
   public NativeImageBackedTexture nativeImageBackedTexture;
   public final char charValue2;
   public final Font[] fontArray;
   public final URecord[] uRecordArray;
   public int intValue;
   public final int intValue2;
   public int intValue3;

   public Font resolveFont(char c) {
      for (Font font : this.fontArray) {
         if (font.canDisplay(c)) {
            return font;
         }
      }

      return this.fontArray[0];
   }

   public void run() {
      synchronized (this) {
         this.flag = false;
         if (this.nativeImageBackedTexture != null) {
            this.nativeImageBackedTexture.close();
         }

         Arrays.fill(this.uRecordArray, null);
         this.intValue = -1;
         this.intValue3 = -1;
      }
   }

   public void run2() {
      if (!this.flag) {
         boolean flag = FontState.getFontState().isFlag2();
         int i3 = this.charValue - this.charValue2 - 1;
         int i4 = (int)(Math.ceil(Math.sqrt(i3)) * 1.5);
         int i5 = 0;
         int i6 = 0;
         int i7 = 0;
         int i8 = 0;
         int i9 = 0;
         int i10 = 0;
         int i11 = 0;
         List<URecord> list = new ArrayList<>();
         AffineTransform affineTransform = new AffineTransform();
         FontRenderContext fontRenderContext = new FontRenderContext(affineTransform, true, flag);

         while (i5 <= i3) {
            char c = (char)(this.charValue2 + i5);
            Font font = this.resolveFont(c);
            Rectangle2D rectangle2D = font.getStringBounds(String.valueOf(c), fontRenderContext);
            int i = (int)Math.ceil(rectangle2D.getWidth());
            int i2 = (int)Math.ceil(rectangle2D.getHeight());
            i5++;
            i7 = Math.max(i7, i9 + i);
            i8 = Math.max(i8, i10 + i2);
            if (i6 >= i4) {
               i9 = 0;
               i10 += i11 + this.intValue2;
               i6 = 0;
               i11 = 0;
            }

            i11 = Math.max(i11, i2);
            list.add(new URecord(i9, i10, i, i2, c, this));
            i9 += i + this.intValue2;
            i6++;
         }

         BufferedImage bufferedImage = new BufferedImage(Math.max(i7 + this.intValue2, 1), Math.max(i8 + this.intValue2, 1), 2);
         this.intValue = bufferedImage.getWidth();
         this.intValue3 = bufferedImage.getHeight();
         Graphics2D graphics2D = bufferedImage.createGraphics();
         graphics2D.setColor(new Color(255, 255, 255, 0));
         graphics2D.fillRect(0, 0, this.intValue, this.intValue3);
         graphics2D.setColor(Color.WHITE);
         graphics2D.setRenderingHint(
            RenderingHints.KEY_FRACTIONALMETRICS, flag ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF
         );
         graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
         graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

         for (URecord uRecord : list) {
            graphics2D.setFont(this.resolveFont(uRecord.value()));
            FontMetrics fontMetrics = graphics2D.getFontMetrics();
            graphics2D.drawString(String.valueOf(uRecord.value()), uRecord.u(), uRecord.v() + fontMetrics.getAscent());
            this.uRecordArray[uRecord.value() - this.charValue2] = uRecord;
         }

         this.nativeImageBackedTexture = Render3d.resolveNativeImageBackedTexture(bufferedImage);
         this.flag = true;
      }
   }

   public FontPage(char c, char c2, Font[] fonts, int i) {
      this.charValue2 = c;
      this.charValue = c2;
      this.fontArray = fonts;
      this.intValue2 = i;
      this.uRecordArray = new URecord[c2 - c];
   }

   public boolean testChar(char c) {
      return c >= this.charValue2 && c < this.charValue;
   }

   public void run3() {
      synchronized (this) {
         this.run2();
      }
   }

   public URecord resolveURecord(char c) {
      synchronized (this) {
         if (!this.flag) {
            this.run2();
         }

         return this.uRecordArray[c - this.charValue2];
      }
   }
}
