package org.phobos.core.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import net.minecraft.client.font.TextRenderer.TextLayerType;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.phobos.core.render.Render3d.CameraLine;
import org.phobos.core.render.Render3d.Cuboid;
import org.phobos.core.render.Render3d.Fade;
import org.phobos.core.render.Render3d.Line;
import org.phobos.core.render.Render3d.Lines;
import org.phobos.core.render.Render3d.Model;
import org.phobos.event.Listener;
import org.phobos.event.RenderEvent2;
import org.phobos.event.RenderTransformedWorldEvent;
import org.phobos.event.SetupEvent;
import org.phobos.mixin.game.render.interfaces.IDrawContext;
import org.phobos.module.client.FontModule;
import org.phobos.module.client.RenderModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.inventory.ClickScheduler;
import org.phobos.core.Feature;
import org.phobos.core.tick.TimeUtils;

public class RenderManager extends Feature {
   public static final Logger LOGGER = LoggerFactory.getLogger(RenderManager.class);
   public Queue<Consumer<Matrix4f>> consumerQueue;
   public NanoVgRenderer nanoVgRenderer;
   public Text3dRenderer text3dRenderer;
   public ContextConsumer<DrawContext> drawContextContextConsumer;
   public SetupEvent setupEvent;

   public void handleRunnable(Runnable runnable) {
      this.run4();
      runnable.run();
      this.start();
   }

   public void handleRenderContext(RenderContext renderContext) {
      Render3d.handleQueue(Render3d.getTransformedLineQueue());

      for (Cuboid cuboid : Render3d.getCuboidQueue()) {
         this.handleRenderContext2(renderContext, cuboid);
      }

      for (Lines lines : Render3d.getLinesQueue()) {
         Render3d.handleRenderContext(renderContext, lines.getLineList(), lines.getFloatValue(), lines.getLongValue(), lines.getLongValue2());
      }

      Vec3d vec3d2 = Render3d.vec3d;

      for (CameraLine cameraLine : Render3d.getCameraLineQueue()) {
         List<Line> list = new ArrayList<>();

         for (int i = 0; i < cameraLine.getOptionalList().size(); i++) {
            Optional<Vec3d> optional = cameraLine.getOptionalList().get(i);
            Vec3d vec3d = cameraLine.getVec3dList().get(i);
            Color color = cameraLine.getColorList().get(i);
            list.add(new Line(optional.isPresent() ? vec3d2.add(optional.get()) : vec3d2, vec3d, color));
         }

         Render3d.handleRenderContext(renderContext, list, cameraLine.getFloatValue(), 0L, 0L);
      }

      for (Consumer<Matrix4f> consumer : this.consumerQueue) {
         consumer.accept(Render3d.matrix4f2);
      }

      for (Model model : Render3d.getModelQueue()) {
         Render3d.handleMatrixStack3(
            renderContext.getMatrixStack(),
            model.getPlayerEntity(),
            model.getPlayerEntityEntityModel(),
            model,
            model.getFloatValue(),
            model.getColor(),
            model.isFlag(),
            model.getColor2(),
            model.getFloatValue2()
         );
      }
   }

   public void handleRenderContext2(RenderContext renderContext, Cuboid cuboid) {
      renderContext.getMutableBox().handleBox(cuboid.getBox());
      cuboid.getVec3dCompletableSupplier().handleConsumer(vec3d -> renderContext.getMutableBox().handleVec3d(vec3d));
      cuboid.getBlockPosCompletableSupplier().handleConsumer(blockPos -> renderContext.getMutableBox().handleBlockPos2(blockPos));
      float f5 = cuboid.getFloatValue4();
      float f6 = cuboid.getFloatValue6();
      long l2 = cuboid.getLongValue2();
      if (l2 != -1L) {
         long l = cuboid.getLongValue();
         float f = f5 * 255.0F;
         float f2 = f6 * 255.0F;
         float f3 = f / (float)l2;
         float f4 = f2 / (float)l2;
         f5 = MathHelper.clamp((int)(f3 * (float)(l + l2 - System.currentTimeMillis())), 0, (int)f) / 255.0F;
         f6 = MathHelper.clamp((int)(f4 * (float)(l + l2 - System.currentTimeMillis())), 0, (int)f2) / 255.0F;
      }

      cuboid.setColor(new Color(cuboid.getFloatValue5(), cuboid.getFloatValue8(), cuboid.getFloatValue3(), f5));
      cuboid.setColor2(new Color(cuboid.getFloatValue(), cuboid.getFloatValue7(), cuboid.getFloatValue2(), f6));
      renderContext.handleColor(cuboid.getColor(), cuboid.getColor2());
      Render3d.handleRenderContext2(
         renderContext,
         cuboid.getFloatValue9(),
         !cuboid.isFlag(),
         () -> cuboid.getCuboidConsumer2().accept(cuboid),
         () -> cuboid.getCuboidConsumer().accept(cuboid)
      );
   }

   public void handleMatrixStack(MatrixStack matrixStack, String text, float f, float f2, float f3, float f4, float f5, float f6) {
      float f7 = this.setupEvent.value();
      matrixStack.scale(f7, f7, f7);
      this.setupEvent.handleMatrixStack2(matrixStack, text, f, f2, f3, f4, f5, f6);
      matrixStack.scale(1.0F / f7, 1.0F / f7, 1.0F / f7);
   }

   public double computeText(String text) {
      return this.computeText6(text, false, this.value2());
   }

   public void handleText(String text, float f, float f2, Color color, float f3, boolean flag) {
      this.handleText3(text, f, f2, color, f3, flag, 5.0F);
   }

   public void handleMatrixStack2(MatrixStack matrixStack, String text, float f, float f2, float f3, float f4, float f5, float f6, boolean flag) {
      float f7 = this.setupEvent.value();
      matrixStack.scale(f7, f7, f7);
      if (flag) {
         this.setupEvent.handleMatrixStack4(matrixStack, text, f, f2, f3, f4, f5, f6);
      } else {
         this.setupEvent.handleMatrixStack2(matrixStack, text, f, f2, f3, f4, f5, f6);
      }

      matrixStack.scale(1.0F / f7, 1.0F / f7, 1.0F / f7);
   }

   public void handleDrawContext(DrawContext drawContext, String text, float f, float f2, Color color, float f3, boolean flag) {
      FontModule fontModule = FontModule.getFontModule();
      if (fontModule != null) {
         if (fontModule.check7()) {
            if (!this.nanoVgRenderer.isFlag4()) {
               this.nanoVgRenderer.run5();
            }

            this.nanoVgRenderer.handleText2(text, f, f2, fontModule.getSize().getValue() * f3, color, flag);
         } else {
            drawContext.getMatrices().scale(f3, f3, f3);
            mc.textRenderer
               .draw(
                  text,
                  f / f3,
                  (int)(f2 / f3),
                  color.hashCode(),
                  flag,
                  drawContext.getMatrices().peek().getPositionMatrix(),
                  drawContext.getVertexConsumers(),
                  TextLayerType.NORMAL,
                  0,
                  15728880,
                  mc.textRenderer.isRightToLeft()
               );
            ((IDrawContext)(Object)drawContext).invokeDraw();
            drawContext.getMatrices().scale(1.0F / f3, 1.0F / f3, 1.0F / f3);
         }
      }
   }

   public double computeText2(String text, float f) {
      return this.computeText6(text, false, this.value2() * f);
   }

   public float computeText3(String text) {
      return this.setupEvent.computeText(text);
   }

   public double computeText4(String text, float f) {
      return this.computeText7(text, false, this.value2() * f);
   }

   public void handleConsumer(Consumer<DrawContext> consumer) {
      this.drawContextContextConsumer.add(consumer);
   }

   public double computeText5(String text) {
      return this.computeText7(text, false, this.value2());
   }

   public void handleDrawContext2(DrawContext drawContext, String text, float f, float f2, Color color, float f3, boolean flag, boolean flag2) {
      FontModule fontModule = FontModule.getFontModule();
      if (fontModule != null) {
         if (fontModule.check7() && flag2) {
            if (!this.nanoVgRenderer.isFlag4()) {
               this.nanoVgRenderer.run5();
            }

            this.nanoVgRenderer.handleText2(text, f, f2, fontModule.getSize().getValue() * f3, color, flag);
         } else {
            drawContext.getMatrices().scale(f3, f3, f3);
            mc.textRenderer
               .draw(
                  text,
                  f / f3,
                  (int)(f2 / f3),
                  color.hashCode(),
                  flag,
                  drawContext.getMatrices().peek().getPositionMatrix(),
                  drawContext.getVertexConsumers(),
                  TextLayerType.NORMAL,
                  0,
                  15728880,
                  mc.textRenderer.isRightToLeft()
               );
            ((IDrawContext)(Object)drawContext).invokeDraw();
            drawContext.getMatrices().scale(1.0F / f3, 1.0F / f3, 1.0F / f3);
         }
      }
   }

   public RenderManager() {
      this.nativeInit();
   }

   public void nativeInit() {
      NanoVgRenderer var1 = new NanoVgRenderer();
      this.nanoVgRenderer = var1;
      ContextConsumer var2 = new ContextConsumer();
      this.drawContextContextConsumer = var2;
      Text3dRenderer var3 = new Text3dRenderer();
      this.text3dRenderer = var3;
      ConcurrentLinkedQueue var4 = new ConcurrentLinkedQueue();
      this.consumerQueue = var4;
      var var5 = new Listener<RenderEvent2>(Integer.MAX_VALUE) {
         public void handleObject2(RenderEvent2 renderEvent2) {
            RenderManager.this.drawContextContextConsumer.handleObject2(renderEvent2.getDrawContext(), true);
         }
      };
      this.handleListener(var5);
      var var6 = new Listener<RenderContext>(Integer.MAX_VALUE) {
         public void handleObject2(RenderContext renderContext) {
            ClickScheduler.getINSTANCE().run();
            Render3d.run3();
         }
      };
      this.handleListener(var6);
      var var7 = new Listener<RenderContext>(Integer.MIN_VALUE) {
         public void handleObject2(RenderContext renderContext) {
            Render3d.longValue = TimeUtils.value();
            if (RenderModule.value() == -1) {
               RenderManager.this.handleRenderContext(renderContext);
            } else {
               MultisampleFramebuffer.handleRunnable(() -> RenderManager.this.handleRenderContext(renderContext));
            }

            Render3d.getCuboidQueue()
               .removeIf(cuboid -> cuboid.getLongValue2() == -1L || cuboid.getLongValue() + cuboid.getLongValue2() < System.currentTimeMillis());
            Render3d.getLinesQueue().removeIf(lines -> lines.getLongValue() == 0L || lines.getLongValue2() + lines.getLongValue() < System.currentTimeMillis());
            Render3d.getCameraLineQueue().clear();
            Render3d.getModelQueue().clear();
            RenderManager.this.consumerQueue.clear();
         }
      };
      this.handleListener(var7);
      var var8 = new Listener<RenderTransformedWorldEvent>(Integer.MIN_VALUE) {
         public void handleObject2(RenderTransformedWorldEvent renderTransformedWorldEvent) {
            RenderManager.this.handleMatrixStack3(renderTransformedWorldEvent.matrixStack());
            if (!Render3d.getConsumerQueue().isEmpty()) {
               if (RenderModule.value() == -1) {
                  for (Consumer<Matrix4f> consumer : Render3d.getConsumerQueue()) {
                     consumer.accept(Render3d.matrix4f);
                  }
               } else {
                  MultisampleFramebuffer.handleRunnable(() -> {
                     for (Consumer<Matrix4f> consumerx : Render3d.getConsumerQueue()) {
                        consumerx.accept(Render3d.matrix4f);
                     }
                  });
               }

               Render3d.getConsumerQueue().clear();
            }
         }
      };
      this.handleListener(var8);
   }

   public void handleText2(String text, float f, float f2, Color color, Color color2, boolean flag, float f3) {
      FontModule fontModule = FontModule.getFontModule();
      if (fontModule != null) {
         if (!this.nanoVgRenderer.isFlag4()) {
            this.nanoVgRenderer.run5();
         }

         this.nanoVgRenderer.handleText(text, f, f2, 1.0F, color, color2, flag, f3);
      }
   }

   public void handleText3(String text, float f, float f2, Color color, float f3, boolean flag, float f4) {
      FontModule fontModule = FontModule.getFontModule();
      if (fontModule != null) {
         if (!this.nanoVgRenderer.isFlag4()) {
            this.nanoVgRenderer.run5();
         }

         this.nanoVgRenderer.handleText7(text, f, f2, fontModule.getSize().getValue() * f3, color, flag, f4);
      }
   }

   public void handleMatrixStack3(MatrixStack matrixStack) {
      for (Fade fade : Render3d.getFadeQueue()) {
         Render3d.handleBox(fade.getBox(), matrixStack.peek().getPositionMatrix(), fade.getColor2(), fade.getColor());
      }

      Render3d.getFadeQueue().clear();
   }

   public float value2() {
      return FontModule.getFontModule().getSize().getValue();
   }

   public double computeText6(String text, boolean flag, float f) {
      FontModule fontModule = FontModule.getFontModule();
      if (fontModule == null) {
         return 0.0;
      }

      if (!fontModule.check7() && !flag) {
         return 9.0;
      }

      if (!this.nanoVgRenderer.isFlag4()) {
         this.nanoVgRenderer.run5();
      }

      return this.nanoVgRenderer.computeText(text);
   }

   public boolean isFlag4() {
      return this.nanoVgRenderer.isFlag4();
   }

   public Queue<Consumer<Matrix4f>> getConsumerQueue() {
      return this.consumerQueue;
   }

   public SetupEvent getSetupEvent() {
      return this.setupEvent;
   }

   public double computeText7(String text, boolean flag, float f) {
      FontModule fontModule = FontModule.getFontModule();
      if (fontModule == null) {
         return 0.0;
      }

      if (!fontModule.check7() && !flag) {
         return mc.textRenderer.getWidth(text);
      }

      if (!this.nanoVgRenderer.isFlag4()) {
         this.nanoVgRenderer.run5();
      }

      return this.nanoVgRenderer.computeText2(text, f);
   }

   public void start() {
      if (!this.nanoVgRenderer.isFlag4()) {
         this.nanoVgRenderer.run5();
      }

      this.nanoVgRenderer.start();
   }

   public long value3() {
      if (!this.nanoVgRenderer.isFlag4()) {
         this.nanoVgRenderer.run5();
      }

      return this.nanoVgRenderer.getLongValue();
   }

   public void run6() {
      this.nanoVgRenderer.run6();
   }

   public void run5() {
      if (!this.nanoVgRenderer.isFlag4()) {
         this.nanoVgRenderer.run5();
      }
   }

   public void run4() {
      if (!this.nanoVgRenderer.isFlag4()) {
         this.nanoVgRenderer.run5();
      }

      this.nanoVgRenderer.run4();
   }

   public long value() {
      return this.nanoVgRenderer.getLongValue();
   }

   public Text3dRenderer getText3dRenderer() {
      return this.text3dRenderer;
   }

   public NanoVgRenderer getNanoVgRenderer() {
      return this.nanoVgRenderer;
   }

   public void setSetupEvent(SetupEvent setupEvent) {
      this.setupEvent = setupEvent;
   }
}
