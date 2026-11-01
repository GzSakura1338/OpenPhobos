package org.phobos.core.render.texture;

import com.github.weisj.jsvg.SVGDocument;
import com.github.weisj.jsvg.attributes.ViewBox;
import com.github.weisj.jsvg.parser.SVGLoader;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.nio.charset.StandardCharsets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.intellij.lang.annotations.Language;
import org.phobos.core.render.Render3d;
import org.phobos.core.render.RenderUtils;

public class SvgTexture implements Closeable {
   public final int intValue;
   public final String text;
   public final int intValue2;
   public int intValue3 = -1;
   public Identifier identifier;

   public SvgTexture(@Language("SVG") String text, int i, int i2) {
      this.text = text;
      this.intValue = i;
      this.intValue2 = i2;
   }

   public void handleFloat(float f, float f2) {
      if (this.identifier != null) {
         this.close();
      }

      this.identifier = Render3d.getIdentifier();

      try {
         SVGLoader sVGLoader = new SVGLoader();
         SVGDocument sVGDocument = sVGLoader.load(new ByteArrayInputStream(this.text.getBytes(StandardCharsets.UTF_8)));
         assert sVGDocument != null;
         BufferedImage bufferedImage = new BufferedImage((int)Math.ceil(f), (int)Math.ceil(f2), 2);
         Graphics2D graphics2D = bufferedImage.createGraphics();
         graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
         sVGDocument.render(null, graphics2D, new ViewBox(f, f2));
         graphics2D.dispose();
         Render3d.handleIdentifier(this.identifier, bufferedImage);
      } catch (Throwable throwable) {
         this.identifier = Identifier.ofVanilla("missingno");
      }
   }

   public void handleMatrixStack(MatrixStack matrixStack, double d, double d2, float f, float f2) {
      int i = Render3d.value();
      if (this.intValue3 != i || this.identifier == null) {
         this.intValue3 = i;
         this.handleFloat(this.intValue * this.intValue3, this.intValue2 * this.intValue3);
      }

      if (!Render3d.isFlag()) {
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
      }

      RenderSystem.setShaderTexture(0, this.identifier);
      RenderUtils.handleMatrixStack3(matrixStack, d, d2, Math.ceil(f), Math.ceil(f2));
   }

   @Override
   public void close() {
      if (this.identifier == null) {
         throw new IllegalStateException("Already closed");
      }

      if (this.identifier.getNamespace().equals("renderer")) {
         MinecraftClient.getInstance().getTextureManager().destroyTexture(this.identifier);
      }

      this.identifier = null;
   }
}
