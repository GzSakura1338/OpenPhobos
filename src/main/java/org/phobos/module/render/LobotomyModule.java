package org.phobos.module.render;

import java.awt.Color;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.math.MathUtils;
import org.phobos.core.render.ColorUtils;
import org.phobos.event.OnDeathScreenEvent;
import org.phobos.event.RenderEvent2;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class LobotomyModule extends Module {
   public final FloatAnimator floatAnimator = new FloatAnimator();

   public int computeFloat(float f) {
      return ColorUtils.createColor6(new Color(MathUtils.computeInt5(0, 255), MathUtils.computeInt5(0, 255), MathUtils.computeInt5(0, 255)), f).hashCode();
   }

   public LobotomyModule(PhobosClient phobosClient) {
      super(phobosClient, "Lobotomy", Categories.render, "Gives you a lobotomy when you die");
      this.handleListener(
         new SafeListener<OnDeathScreenEvent>() {
            public void handleObject(
               OnDeathScreenEvent onDeathScreenEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               phobosClient.getPhobosSounds().handleSoundEvent(phobosClient.getPhobosSounds().soundEvent2, 1.0F);
               LobotomyModule.this.floatAnimator.setFloatValue2(1.0F);
            }
         }
      );
      this.handleListener(
         new SafeListener<RenderEvent2>(-2147483548) {
            public void handleObject(
               RenderEvent2 renderEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               LobotomyModule.this.floatAnimator.handleFloat3(5.0F, 0.005F);
               float f = LobotomyModule.this.floatAnimator.getFloatValue2();
               if (!(f <= 0.05F)) {
                  int i = mc.getWindow().getScaledWidth();
                  int i2 = mc.getWindow().getScaledHeight();
                  int i3 = i / 2;
                  int i4 = i2 / 2;
                  if (f > 0.75F) {
                     renderEvent2.getDrawContext().fill(0, 0, i3, i2, LobotomyModule.this.computeFloat(f));
                     renderEvent2.getDrawContext().fill(i3, 0, i, i2, LobotomyModule.this.computeFloat(f));
                  } else if (f > 0.5F) {
                     renderEvent2.getDrawContext().fill(0, 0, i3, i4, LobotomyModule.this.computeFloat(f));
                     renderEvent2.getDrawContext().fill(i3, 0, i, i4, LobotomyModule.this.computeFloat(f));
                     renderEvent2.getDrawContext().fill(0, i4, i3, i2, LobotomyModule.this.computeFloat(f));
                     renderEvent2.getDrawContext().fill(i3, i4, i, i2, LobotomyModule.this.computeFloat(f));
                  } else if (f > 0.25F) {
                     renderEvent2.getDrawContext().fill(0, 0, i, i4, LobotomyModule.this.computeFloat(f));
                     renderEvent2.getDrawContext().fill(0, i4, i, i2, LobotomyModule.this.computeFloat(f));
                  } else {
                     renderEvent2.getDrawContext().fill(0, 0, i, i2, LobotomyModule.this.computeFloat(f));
                  }
               }
            }
         }
      );
      ModuleGroups.getINSTANCE().handleText("beta", this);
   }
}
