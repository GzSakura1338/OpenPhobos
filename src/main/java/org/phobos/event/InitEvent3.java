package org.phobos.event;

import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.user.UserSession;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.PhobosClient;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.math.MathUtils;

public class InitEvent3 extends Screen implements MinecraftHolder {
   public final long longValue;
   public final FloatAnimator floatAnimator = new FloatAnimator();
   public static boolean flag = false;

   public InitEvent3() {
      super(Text.literal(""));
      this.longValue = System.currentTimeMillis();
      flag = true;
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      this.floatAnimator
         .setFloatValue(
            UserSession.userSession.getUserProfile() != null && this.longValue + 6000L < System.currentTimeMillis() && PhobosClient.flag2 ? 0.0F : 1.0F
         );
      this.floatAnimator.handleFloat3(10.0F, 0.02F);
      Color color = new Color(0.5F, 0.5F, 0.5F, MathUtils.computeFloat5(this.floatAnimator.getFloatValue2(), 2));
      RenderUtils.handleMatrixStack12(context.getMatrices(), color, 0.0, 0.0, this.width, this.height, 0.0F, () -> {
         RenderUtils.loadingShader.handleColor(color);
         RenderUtils.loadingShader.run3();
      });
      if (this.floatAnimator.getFloatValue2() <= 0.05F
         && this.longValue + 8000L < System.currentTimeMillis()
         && PhobosClient.flag2
         && UserSession.userSession.getUserProfile() != null) {
         mc.setScreen(new TitleScreen(true));
      }
   }
}
