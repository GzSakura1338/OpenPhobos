package org.phobos.core.animation;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import org.phobos.api.MinecraftHolder;
import org.phobos.event.GetFovEvent;
import org.phobos.event.Listener;
import org.phobos.core.Feature;
import org.phobos.core.render.RenderContext;

public class AnimationManager extends Feature {
   public float floatValue;
   public boolean flag;
   public boolean flag2;
   public FloatAnimator floatAnimator;
   public double doubleValue;

   public AnimationManager() {
      this.nativeInit();
   }

   public void nativeInit() {
      MinecraftClient var2 = MinecraftHolder.mc;
      GameOptions var3 = var2.options;
      SimpleOption var4 = var3.getFov();
      Object var5 = var4.getValue();
      boolean var6 = var5 instanceof Integer;
      if (var5 != null && !var6) {
         throw new ClassCastException("CHECKCAST failed");
      }

      int var7 = (Integer)var5;
      MinecraftClient var8 = MinecraftHolder.mc;
      GameOptions var9 = var8.options;
      SimpleOption var10 = var9.getFov();
      Object var11 = var10.getValue();
      boolean var12 = var11 instanceof Integer;
      if (var11 != null && !var12) {
         throw new ClassCastException("CHECKCAST failed");
      }

      int var13 = (Integer)var11;
      FloatAnimator var1 = new FloatAnimator(
         (float)Float.intBitsToFloat(Float.floatToRawIntBits(var7)), (float)Float.intBitsToFloat(Float.floatToRawIntBits(var13))
      );
      this.floatAnimator = var1;
      var var14 = new Listener<GetFovEvent>() {
         public void handleObject2(GetFovEvent getFovEvent) {
            AnimationManager.this.doubleValue = getFovEvent.getDoubleValue();
            if (AnimationManager.this.flag || !AnimationManager.this.floatAnimator.testFloat(0.05F)) {
               getFovEvent.setDoubleValue(AnimationManager.this.floatAnimator.getFloatValue2());
            }
         }
      };
      this.handleListener(var14);
      var var15 = new Listener<RenderContext>() {
         public void handleObject2(RenderContext renderContext) {
            MinecraftHolder.mc.options.smoothCameraEnabled = AnimationManager.this.flag && AnimationManager.this.flag2;
            AnimationManager.this.floatAnimator
               .setFloatValue(AnimationManager.this.flag ? AnimationManager.this.floatValue : (float)AnimationManager.this.doubleValue);
            AnimationManager.this.floatAnimator.handleFloat3(50.0F, 0.06F);
         }
      };
      this.handleListener(var15);
   }

   public void handleDouble(double d, boolean flag) {
      this.flag = true & true;
      this.flag2 = flag;
      this.floatValue = (float)d;
   }

   public void run2() {
      this.flag = false;
   }
}
