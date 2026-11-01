package org.phobos.module.player;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.render.RenderContext;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class YawLockModule extends Module {
   public final FloatAnimator floatAnimator = new FloatAnimator();
   public double doubleValue;
   public double doubleValue2;

   public YawLockModule(PhobosClient phobosClient) {
      super(phobosClient, "YawLock", Categories.player, "Locks your rotation");
      this.handleListener(
         new SafeListener<RenderContext>() {
            public void handleObject(
               RenderContext renderContext,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               this.setRunnable(() -> {
                  YawLockModule.this.doubleValue = mc.mouse.getX();
                  YawLockModule.this.doubleValue2 = mc.mouse.getY();
               });
               if (mc.mouse.getX() == YawLockModule.this.doubleValue && mc.mouse.getY() == YawLockModule.this.doubleValue2) {
                  YawLockModule.this.floatAnimator.handleFloat3(10.0F, 0.06F);
                  YawLockModule.this.floatAnimator.setFloatValue(Math.round(clientPlayerEntity.getYaw() / 45.0F) * 45);
                  clientPlayerEntity.setYaw(YawLockModule.this.floatAnimator.getFloatValue2());
               } else {
                  YawLockModule.this.floatAnimator.handleFloat(clientPlayerEntity.getYaw());
               }
            }
         }
      );
   }
}
