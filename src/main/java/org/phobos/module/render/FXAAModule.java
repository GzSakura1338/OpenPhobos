package org.phobos.module.render;

import net.minecraft.util.Identifier;
import org.phobos.api.ShaderRegistry;
import org.phobos.api.PostEffectShader;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.FXAARenderListener;
import org.phobos.module.Module;

public class FXAAModule extends Module {
   public final PostEffectShader postEffectShader;
   public final Setting<Float> delta;
   public final Setting<Float> span = new NumberSetting<>("Span", 8.0F, 0.1F, 10.0F).resolveSetting6(this);

   public float value() {
      return 1.0F / this.delta.getValue();
   }

   public FXAAModule(PhobosClient phobosClient) {
      super(phobosClient, "FXAA", Categories.render, "FXAA filtering");
      this.delta = new NumberSetting<>("Delta", 128.0F, 64.0F, 256.0F).resolveSetting6(this);
      this.postEffectShader = ShaderRegistry.getINSTANCE().resolvePostEffectShader2(Identifier.of("phoboslite", "shaders/post/fxaa.json"), postEffectShader -> {
         postEffectShader.handleText5("resolution", mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
         postEffectShader.handleText2("span", this.value3());
         postEffectShader.handleText2("multiplication", this.value2());
         postEffectShader.handleText2("minimum", this.value());
      });
      this.handleListener(new FXAARenderListener(this));
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }

   public float value2() {
      return 1.0F / this.value3();
   }

   public float value3() {
      return this.span.getValue();
   }
}
