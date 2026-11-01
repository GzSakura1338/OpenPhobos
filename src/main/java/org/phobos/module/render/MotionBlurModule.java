package org.phobos.module.render;

import net.minecraft.util.Identifier;
import org.phobos.api.ShaderRegistry;
import org.phobos.api.PostEffectShader;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.Listener;
import org.phobos.event.RenderEvent;
import org.phobos.module.Module;

public class MotionBlurModule extends Module {
   public final Setting<Boolean> vignette;
   public final Setting<Float> scale;
   public final PostEffectShader postEffectShader;
   public final Setting<Integer> amount = this.resolveSetting2(new NumberSetting<>("Amount", 50, 0, 100));

   public float value() {
      return this.amount.getValue().intValue() / 100.0F;
   }

   @Override
   public String nothing() {
      return this.amount.getValue() + "%";
   }

   public MotionBlurModule(PhobosClient phobosClient) {
      super(phobosClient, "MotionBlur", Categories.render, "Adds motion blur");
      this.vignette = this.resolveSetting2(new BooleanSetting("Vignette"));
      this.scale = this.resolveSetting2(new NumberSetting<>("Scale", 0.25F, 0.0F, 5.0F));
      this.postEffectShader = ShaderRegistry.getINSTANCE().resolvePostEffectShader2(Identifier.of("phoboslite", "shaders/post/motion_blur.json"), postEffectShader -> {
         postEffectShader.handleText2("BlendFactor", this.value());
         postEffectShader.handleText2("Falloff", this.scale.getValue());
         postEffectShader.handleText2("Sample0", this.vignette.getValue() ? 1.0F : 0.0F);
      });
      this.resolveSettingGroup(this.vignette, this.scale);
      this.handleListener(new Listener<RenderEvent>() {
         public void handleObject2(RenderEvent renderEvent) {
            if (!MotionBlurModule.this.amount.getValue().equals(0)) {
               MotionBlurModule.this.postEffectShader.handleText2("BlendFactor", MotionBlurModule.this.value());
               MotionBlurModule.this.postEffectShader.handleText2("Falloff", MotionBlurModule.this.scale.getValue());
               MotionBlurModule.this.postEffectShader.handleText2("Sample0", MotionBlurModule.this.vignette.getValue() ? 1.0F : 0.0F);
               MotionBlurModule.this.postEffectShader.handleFloat(renderEvent.getFloatValue());
            }
         }
      });
   }
}
