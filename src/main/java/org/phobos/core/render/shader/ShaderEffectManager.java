package org.phobos.core.render.shader;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.util.Identifier;
import org.earthhack.ducks.IPostEffectProcessor;
import org.lwjgl.opengl.GL11;
import org.phobos.api.ShaderRegistry;
import org.phobos.api.PostEffectShader;
import org.phobos.event.Listener;
import org.phobos.module.client.RenderModule;
import org.phobos.module.render.ShaderModule;
import org.phobos.module.render.ShaderModule;
import org.phobos.core.Feature;
import org.phobos.core.system.Profiler;
import org.phobos.core.render.texture.SkyboxTextureUtils;
import org.phobos.core.tick.TickCounter;

public class ShaderEffectManager extends Feature {
   public boolean flag;
   public static PostEffectShader postEffectShader;
   public static PostEffectShader postEffectShader2;
   public static PostEffectShader postEffectShader3;
   public static PostEffectShader postEffectShader4;
   public static PostEffectShader postEffectShader5;
   public static final List<ShaderEffectManager.ShaderAction> shaderActionList = new ArrayList<>();
   public static PostEffectShader postEffectShader6;
   public ShaderEffectManager.ShaderFramebuffer shaderFramebuffer;
   public static float floatValue = 0.0F;
   public static PostEffectShader postEffectShader7;
   public float floatValue2;
   public static PostEffectShader postEffectShader8;

   public void run2() {
      this.flag = true;
      postEffectShader8 = null;
      postEffectShader4 = null;
      postEffectShader2 = null;
      postEffectShader6 = null;
      postEffectShader7 = null;
      postEffectShader = null;
      postEffectShader5 = null;
      postEffectShader3 = null;
   }

   public ShaderEffectManager.ShaderFramebuffer getShaderFramebuffer() {
      return this.shaderFramebuffer;
   }

   public PostEffectShader resolvePostEffectShader(ShaderEffectManager.Shader shader) {
      return switch (shader) {
         case Color -> postEffectShader;
         case Gradient -> postEffectShader7;
         case Circle -> postEffectShader5;
         case Texture -> postEffectShader3;
      };
   }

   public boolean check() {
      if (postEffectShader8 != null && postEffectShader4 != null && postEffectShader2 != null && postEffectShader6 != null) {
         return false;
      }

      this.shaderFramebuffer = new ShaderEffectManager.ShaderFramebuffer(mc.getFramebuffer().textureWidth, mc.getFramebuffer().textureHeight);
      this.run();
      return true;
   }

   public boolean check2() {
      if (GL11.glGetError() == 1282) {
         this.run2();
         this.check();
         return true;
      } else {
         return false;
      }
   }

   public ShaderEffectManager() {
      this.handleListener(
         new Listener<TickCounter>() {
            public void handleObject2(TickCounter tickCounter) {
               ShaderEffectManager.floatValue = ShaderEffectManager.floatValue
                  + 0.01F * RenderModule.getRenderModule().getSpeed().resolveObject(floatValue -> floatValue / 100.0F);
            }
         }
      );
   }

   public void handleRunnable(Runnable runnable, ShaderEffectManager.Shader shader) {
      Framebuffer framebuffer = mc.getFramebuffer();
      RenderSystem.assertOnRenderThreadOrInit();
      if (this.shaderFramebuffer.textureWidth != framebuffer.textureWidth || this.shaderFramebuffer.textureHeight != framebuffer.textureHeight) {
         this.shaderFramebuffer.resize(framebuffer.textureWidth, framebuffer.textureHeight, false);
      }

      GlStateManager._glBindFramebuffer(36009, this.shaderFramebuffer.fbo);
      this.shaderFramebuffer.beginWrite(true);
      runnable.run();
      this.shaderFramebuffer.endWrite();
      GlStateManager._glBindFramebuffer(36009, framebuffer.fbo);
      framebuffer.beginWrite(false);
      Framebuffer framebuffer2 = mc.getFramebuffer();
      PostEffectShader postEffectShader = this.resolvePostEffectShader2(shader);
      PostEffectProcessor postEffectProcessor = postEffectShader.getPostEffectProcessor();
      if (postEffectProcessor != null) {
         ((IPostEffectProcessor)(Object)postEffectProcessor).createBuffer("bufIn", this.shaderFramebuffer);
      }

      Framebuffer framebuffer3 = postEffectShader.getPostEffectProcessor().getSecondaryTarget("bufOut");
      this.handleShader(shader, postEffectShader);
      this.shaderFramebuffer.clear(false);
      framebuffer2.beginWrite(false);
      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA, SrcFactor.ZERO, DstFactor.ONE);
      RenderSystem.backupProjectionMatrix();
      framebuffer3.draw(framebuffer3.textureWidth, framebuffer3.textureHeight, false);
      RenderSystem.restoreProjectionMatrix();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableBlend();
   }

   public void handleRunnable2(Runnable runnable, ShaderEffectManager.Shader shader) {
      if (!this.flag) {
         shaderActionList.add(new ShaderEffectManager.ShaderAction(runnable, shader));
      }
   }

   public void run() {
      Profiler.handleText2("Shaders");
      postEffectShader8 = ShaderRegistry.getINSTANCE().resolvePostEffectShader(Identifier.of("phoboslite", "shaders/post/gradient.json"));
      postEffectShader4 = ShaderRegistry.getINSTANCE().resolvePostEffectShader(Identifier.of("phoboslite", "shaders/post/color.json"));
      postEffectShader2 = ShaderRegistry.getINSTANCE().resolvePostEffectShader(Identifier.of("phoboslite", "shaders/post/circle.json"));
      postEffectShader6 = ShaderRegistry.getINSTANCE().resolvePostEffectShader(Identifier.of("phoboslite", "shaders/post/texture.json"));
      postEffectShader7 = ShaderRegistry.getINSTANCE().resolvePostEffectShader2(Identifier.of("phoboslite", "shaders/post/gradient.json"), postEffectShader -> {
         PostEffectProcessor postEffectProcessor = postEffectShader.getPostEffectProcessor();
         if (postEffectProcessor != null) {
            ((IPostEffectProcessor)(Object)postEffectProcessor).createBuffer("bufIn", mc.worldRenderer.getEntityOutlinesFramebuffer());
            ((IPostEffectProcessor)(Object)postEffectProcessor).createBuffer("bufOut", mc.worldRenderer.getEntityOutlinesFramebuffer());
         }
      });
      postEffectShader = ShaderRegistry.getINSTANCE().resolvePostEffectShader2(Identifier.of("phoboslite", "shaders/post/color.json"), postEffectShader -> {
         PostEffectProcessor postEffectProcessor = postEffectShader.getPostEffectProcessor();
         if (postEffectProcessor != null) {
            ((IPostEffectProcessor)(Object)postEffectProcessor).createBuffer("bufIn", mc.worldRenderer.getEntityOutlinesFramebuffer());
            ((IPostEffectProcessor)(Object)postEffectProcessor).createBuffer("bufOut", mc.worldRenderer.getEntityOutlinesFramebuffer());
         }
      });
      postEffectShader5 = ShaderRegistry.getINSTANCE().resolvePostEffectShader2(Identifier.of("phoboslite", "shaders/post/circle.json"), postEffectShader -> {
         PostEffectProcessor postEffectProcessor = postEffectShader.getPostEffectProcessor();
         if (postEffectProcessor != null) {
            ((IPostEffectProcessor)(Object)postEffectProcessor).createBuffer("bufIn", mc.worldRenderer.getEntityOutlinesFramebuffer());
            ((IPostEffectProcessor)(Object)postEffectProcessor).createBuffer("bufOut", mc.worldRenderer.getEntityOutlinesFramebuffer());
         }
      });
      postEffectShader3 = ShaderRegistry.getINSTANCE().resolvePostEffectShader2(Identifier.of("phoboslite", "shaders/post/texture.json"), postEffectShader -> {
         PostEffectProcessor postEffectProcessor = postEffectShader.getPostEffectProcessor();
         if (postEffectProcessor != null) {
            ((IPostEffectProcessor)(Object)postEffectProcessor).createBuffer("bufIn", mc.worldRenderer.getEntityOutlinesFramebuffer());
            ((IPostEffectProcessor)(Object)postEffectProcessor).createBuffer("bufOut", mc.worldRenderer.getEntityOutlinesFramebuffer());
         }
      });
      Profiler.handleText("Shaders");
      this.flag = false;
   }

   public int computeShaderModule(ShaderModule shaderModule) {
      return switch ((ShaderModule.Outline)shaderModule.outlineSetting.getValue()) {
         case Solid -> -1;
         case Glow -> 0;
         case Overlay -> 1;
         case Multilayer -> 2;
      };
   }

   public boolean testShaderModule(ShaderModule shaderModule) {
      return !shaderModule.outlineSetting.testObject2(ShaderModule.Outline.Solid);
   }

   public void run3() {
      if (!shaderActionList.isEmpty()) {
         if (postEffectShader == null) {
            this.shaderFramebuffer = new ShaderEffectManager.ShaderFramebuffer(mc.getFramebuffer().textureWidth, mc.getFramebuffer().textureHeight);
            this.run();
         }

         if (this.shaderFramebuffer != null) {
            shaderActionList.forEach(shaderAction -> {
               try {
                  this.handleRunnable(shaderAction.drawAction, shaderAction.shader);
               } catch (Throwable var3) {
               }
            });
            shaderActionList.clear();
         }
      }
   }

   public static float getFloatValue() {
      return floatValue;
   }

   public PostEffectShader resolvePostEffectShader2(ShaderEffectManager.Shader shader) {
      return switch (shader) {
         case Color -> postEffectShader4;
         case Gradient -> postEffectShader8;
         case Circle -> postEffectShader2;
         case Texture -> postEffectShader6;
      };
   }

   public void handleShader(ShaderEffectManager.Shader shader, PostEffectShader postEffectShader) {
      ShaderModule shaderModule = ShaderModule.getShaderModule();
      if (shaderModule != null) {
         Color color3 = shaderModule.getColor6();
         Color color4 = shaderModule.getColor5();
         Color color5 = shaderModule.getColor2();
         Color color6 = shaderModule.getColor3();
         switch (shader) {
            case Color:
               postEffectShader.handleText2("alpha0", this.testShaderModule(shaderModule) ? -1.0F : 1.0F);
               postEffectShader.handleText3("overlay", this.computeShaderModule(shaderModule));
               postEffectShader.handleText7("color", color3);
               postEffectShader.handleText7("colorF", color4);
               postEffectShader.handleText7("colorE", color5);
               postEffectShader.handleText7("outlinecolor", color6);
               postEffectShader.handleText3("sharp", shaderModule.value6());
               postEffectShader.handleText5("resolution", mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
               postEffectShader.handleText2("width", shaderModule.value5());
               postEffectShader.handleText2("scale", shaderModule.value());
               postEffectShader.handleText2("radius", shaderModule.value4());
               postEffectShader.handleText3("quality", shaderModule.integerSetting.getValue());
               postEffectShader.handleFloat(mc.getRenderTickCounter().getTickDelta(false));
               break;
            case Gradient:
               postEffectShader.handleText2("alpha0", this.testShaderModule(shaderModule) ? -1.0F : 1.0F);
               postEffectShader.handleText2("alpha1", shaderModule.value7());
               postEffectShader.handleText2("alpha2", shaderModule.value8());
               postEffectShader.handleText2("alpha2F", shaderModule.value2());
               postEffectShader.handleText2("alpha2E", shaderModule.value3());
               postEffectShader.handleText7("outlinecolor", color6);
               postEffectShader.handleText3("oct", 10);
               postEffectShader.handleText3("quality", shaderModule.integerSetting.getValue());
               postEffectShader.handleText3("overlay", this.computeShaderModule(shaderModule));
               postEffectShader.handleText3("sharp", shaderModule.value6());
               postEffectShader.handleText2("factor", shaderModule.floatValueSetting2.getValue());
               postEffectShader.handleText2("moreGradient", shaderModule.floatValueSetting5.getValue());
               postEffectShader.handleText5("resolution", mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
               postEffectShader.handleText2("width", shaderModule.value5());
               postEffectShader.handleText2("scale", shaderModule.value());
               postEffectShader.handleText2("radius", shaderModule.value4());
               postEffectShader.handleText2("time", this.floatValue2);
               postEffectShader.handleFloat(mc.getRenderTickCounter().getTickDelta(false));
               this.floatValue2 = this.floatValue2 + shaderModule.integerSetting2.getValue().intValue() * 0.001F;
               break;
            case Circle:
               Color color = shaderModule.getColor4();
               Color color2 = shaderModule.getColor();
               postEffectShader.handleText7("color1", color3);
               postEffectShader.handleText7("color2", color);
               postEffectShader.handleText7("outline1", color6);
               postEffectShader.handleText7("outline2", color2);
               postEffectShader.handleText2("width", shaderModule.value5());
               postEffectShader.handleText2("scale", shaderModule.value());
               postEffectShader.handleText2("radius", shaderModule.value4());
               postEffectShader.handleText3("quality", shaderModule.integerSetting.getValue());
               postEffectShader.handleText3("overlay", this.computeShaderModule(shaderModule));
               postEffectShader.handleText3("sharp", shaderModule.value6());
               postEffectShader.handleText2("alpha0", this.testShaderModule(shaderModule) ? -1.0F : 1.0F);
               postEffectShader.handleText2("steps", shaderModule.floatValueSetting3.getValue());
               postEffectShader.handleText2("time", this.floatValue2);
               postEffectShader.handleFloat(mc.getRenderTickCounter().getTickDelta(false));
               this.floatValue2 = this.floatValue2 + shaderModule.integerSetting2.getValue().intValue() * 0.001F;
               break;
            case Texture:
               RenderSystem.setShaderTexture(0, SkyboxTextureUtils.identifier);
               postEffectShader.handleText2("alpha0", this.testShaderModule(shaderModule) ? -1.0F : 1.0F);
               postEffectShader.handleText3("quality", shaderModule.integerSetting.getValue());
               postEffectShader.handleText3("overlay", this.computeShaderModule(shaderModule));
               postEffectShader.handleText3("sharp", shaderModule.value6());
               postEffectShader.handleText7("color", color3);
               postEffectShader.handleText7("outlinecolor", color6);
               postEffectShader.handleText2("width", shaderModule.value5());
               postEffectShader.handleText2("scale", shaderModule.value());
               postEffectShader.handleText2("radius", shaderModule.value4());
               postEffectShader.handleFloat(mc.getRenderTickCounter().getTickDelta(false));
         }
      }
   }

   public enum GlobalShader {
      None,
      Circle,
      Plasma;
   }

   public enum Shader {
      Color,
      Gradient,
      Circle,
      Texture;
   }

   public record ShaderAction(Runnable drawAction, ShaderEffectManager.Shader shader) {
   }

   public static class ShaderFramebuffer extends Framebuffer {
      public ShaderFramebuffer(int i, int i2) {
         super(false);
         RenderSystem.assertOnRenderThreadOrInit();
         this.resize(i, i2, true);
         this.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
      }
   }
}
