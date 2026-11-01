package org.phobos.event;

import org.phobos.module.render.FXAAModule;

public final class FXAARenderListener extends ModuleListener<FXAAModule, RenderEvent> {
   public void handleObject2(RenderEvent renderEvent) {
      this.module.postEffectShader.handleText5("resolution", mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
      this.module.postEffectShader.handleText2("span", this.module.value3());
      this.module.postEffectShader.handleText2("multiplication", this.module.value2());
      this.module.postEffectShader.handleText2("minimum", this.module.value());
      this.module.postEffectShader.handleFloat(renderEvent.getFloatValue());
   }

   public FXAARenderListener(FXAAModule fXAAModule) {
      super(fXAAModule, RenderEvent.class);
   }
}
