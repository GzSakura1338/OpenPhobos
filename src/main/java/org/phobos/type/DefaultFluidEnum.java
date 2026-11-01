package org.phobos.type;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.GameRenderer;
import org.phobos.api.RenderBatch;
import org.phobos.core.render.Render3d;

public enum DefaultFluidEnum {
   Default(new RenderBatch() {
      @Override
      public void run() {
      }

      @Override
      public void run2() {
         RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);
      }

      @Override
      public void run3() {
         RenderSystem.setShader(GameRenderer::getPositionProgram);
      }

      @Override
      public void run4() {
      }
   }),
   Fluid(new RenderBatch() {
      @Override
      public void run() {
      }

      @Override
      public void run3() {
         Render3d.positionFluidShader.run2();
         Render3d.positionFluidShader.run3();
      }

      @Override
      public void run4() {
      }

      @Override
      public void run2() {
         Render3d.linesFluidShader.run2();
         Render3d.linesFluidShader.run3();
      }
   }),
   Silk(new RenderBatch() {
      @Override
      public void run2() {
         Render3d.linesSilkShader.run2();
         Render3d.linesSilkShader.run3();
      }

      @Override
      public void run3() {
         Render3d.positionSilkShader.run2();
         Render3d.positionSilkShader.run3();
      }

      @Override
      public void run() {
      }

      @Override
      public void run4() {
      }
   }),
   Noise(new RenderBatch() {
      @Override
      public void run3() {
         Render3d.positionNoiseShader.run2();
         Render3d.positionNoiseShader.run3();
      }

      @Override
      public void run4() {
      }

      @Override
      public void run2() {
         Render3d.linesNoiseShader.run2();
         Render3d.linesNoiseShader.run3();
      }

      @Override
      public void run() {
      }
   }),
   Flux(new RenderBatch() {
      @Override
      public void run3() {
         Render3d.positionFluxShader.run2();
         Render3d.positionFluxShader.run3();
      }

      @Override
      public void run() {
      }

      @Override
      public void run2() {
         Render3d.linesFluxShader.run2();
         Render3d.linesFluxShader.run3();
      }

      @Override
      public void run4() {
      }
   });

   public final RenderBatch renderer;

   public RenderBatch getRenderer() {
      return this.renderer;
   }

   DefaultFluidEnum(final RenderBatch renderer) {
      this.renderer = renderer;
   }
}
