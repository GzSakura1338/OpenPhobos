package org.phobos.core.script;

import java.awt.Color;
import java.util.List;
import org.phobos.event.RenderEvent2;
import org.phobos.core.PhobosClient;
import org.phobos.core.render.RenderUtils;

public class UiScriptLibrary extends ScriptLibrary {
   public void handleRenderEvent2(RenderEvent2 renderEvent2, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8) {
      RenderUtils.handleMatrixStack20(renderEvent2.getDrawContext().getMatrices(), new Color((float)d5, (float)d6, (float)d7, (float)d8), d, d2, d + d3, d2 + d4);
   }

   public UiScriptLibrary(PhobosClient phobosClient, Script script) {
      super("ui", phobosClient, script);
      List<ScriptMember<?>> list = ScriptMember.resolveList(this, "ui", this.getClass());
      list.forEach(this::handleScriptMember);
   }
}
