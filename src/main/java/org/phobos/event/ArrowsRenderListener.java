package org.phobos.event;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.render.RenderStates;
import org.phobos.core.render.Render3d;
import org.phobos.module.combat.AntiBotModule;
import org.phobos.module.render.ArrowsModule;

public final class ArrowsRenderListener extends ModuleListener.Safe<ArrowsModule, RenderEvent2> {
   public ArrowsRenderListener(ArrowsModule arrowsModule) {
      super(arrowsModule, RenderEvent2.class);
   }

   public void handleObject(
      RenderEvent2 renderEvent2, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      MatrixStack matrixStack = renderEvent2.getDrawContext().getMatrices();
      Vec3d vec3d = mc.gameRenderer.getCamera().getPos();
      float f2 = renderEvent2.getFloatValue();
      this.module.timedFloatAnimator.setFloatValue(clientPlayerEntity.getYaw());
      this.module.timedFloatAnimator.handleFloat3(10.0F, 0.2F);
      float f3 = this.module.timedFloatAnimator.getFloatValue2();

      for (PlayerEntity playerEntity : clientWorld.getPlayers()) {
         if (!AntiBotModule.testEntity2(playerEntity) && playerEntity.isAlive()) {
            double d = (playerEntity.getX() + (playerEntity.getX() - playerEntity.lastRenderX) * f2 - vec3d.getX()) * 0.01;
            double d2 = (playerEntity.getZ() + (playerEntity.getZ() - playerEntity.lastRenderZ) * f2 - vec3d.getZ()) * 0.01;
            double d3 = Math.cos(f3 * (Math.PI / 180.0));
            double d4 = Math.sin(f3 * (Math.PI / 180.0));
            double d5 = -(d2 * d3 - d * d4);
            double d6 = -(d * d3 + d2 * d4);
            double d7 = -d5;
            double d8 = -d6;
            double d9 = 43.0 * this.module.radius.getValue();
            double d10 = d9 * 2.0 + d9;
            double d11 = mc.getWindow().getScaledWidth() / 2.0F - d9 - d9 / 2.0;
            double d12 = mc.getWindow().getScaledHeight() / 2.0F - d9 - d9 / 2.0;
            double d13 = Math.atan2(d5, d6) * 180.0 / Math.PI;
            double d14 = d10 / 2.0 * Math.cos(Math.toRadians(d13)) + d11 + d10 / 2.0;
            double d15 = d10 / 2.0 * Math.sin(Math.toRadians(d13)) + d12 + d10 / 2.0;
            if (Math.sqrt(d8 * d8 + d7 * d7) < d10 / 2.0) {
               int i = (int)clientPlayerEntity.getPos().distanceTo(playerEntity.getPos());
               if (i > 0) {
                  matrixStack.push();
                  matrixStack.translate(d14, d15, 0.0);
                  float f = this.module.size.getValue();
                  matrixStack.scale(f, f, f);
                  matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)d13));
                  this.handleMatrixStack(matrixStack, this.module.resolveColor(playerEntity.getUuid()));
                  matrixStack.pop();
               }
            }
         }
      }
   }

   public void handleMatrixStack(MatrixStack matrixStack, Color color) {
      float f = 18.0F;
      RenderSystem.setShaderTexture(0, ArrowsModule.identifier);
      Render3d.handleColor(color);
      RenderUtils.handleMatrixStack2(matrixStack, -f / 2.0F, -f / 2.0F, f);
      RenderStates.runnable2.run();
   }
}
