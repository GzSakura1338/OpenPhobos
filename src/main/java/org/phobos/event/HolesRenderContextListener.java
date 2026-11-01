package org.phobos.event;

import java.awt.Color;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Box;
import org.phobos.core.render.RenderContext;
import org.phobos.core.chunk.Hole;
import org.phobos.core.render.Render3d;
import org.phobos.core.render.Render3d;
import org.phobos.core.render.Render3d;
import org.phobos.module.render.HolesModule;
import org.phobos.module.render.HolesModule;

public final class HolesRenderContextListener extends ModuleListener.Safe<HolesModule, RenderContext> {
   public HolesRenderContextListener(HolesModule holesModule) {
      super(holesModule, RenderContext.class);
   }

   public void handleObject(
      RenderContext renderContext, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      for (Hole hole : this.module.holePriorityQueue) {
         HolesModule.PaintData paintData = this.module.createPaintData(hole);
         double d = hole.computeEntity(clientPlayerEntity);
         if (paintData.draw()) {
            float f = paintData.height();
            Box box2 = new Box(
               hole.getIntValue3(),
               hole.getIntValue4(),
               hole.getIntValue(),
               hole.getIntValue2(),
               hole.getIntValue4() + (paintData.glow() ? 0.0F : f),
               hole.getIntValue7()
            );
            Render3d.handleCuboid(
               Render3d.Cuboid.createBuilder()
                  .resolveBuilder11(box2)
                  .resolveBuilder18(this.module.resolveColor(d, paintData.box()), this.module.resolveColor(d, paintData.line()))
                  .resolveBuilder12(paintData.width())
                  .getCuboid()
            );
            if (paintData.glow()) {
               Box box = new Box(
                  hole.getIntValue3(),
                  hole.getIntValue4(),
                  hole.getIntValue(),
                  hole.getIntValue2(),
                  hole.getIntValue4() + f,
                  hole.getIntValue7()
               );
               Color color = this.module.resolveColor(d, paintData.glowColor());
               Render3d.handleFade(
                  Render3d.Fade.createBuilder()
                     .resolveBuilder2(box)
                     .resolveBuilder(f < 0.0F ? this.module.color : color, f < 0.0F ? color : this.module.color)
                     .getFade()
               );
            }
         }
      }
   }
}
