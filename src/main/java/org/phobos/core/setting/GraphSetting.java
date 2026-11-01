package org.phobos.core.setting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.gui.element.GraphElement;
import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.gui.ModuleElement;

public class GraphSetting extends Setting<CurvePoints> {
   @Override
   public JsonElement createJsonPrimitive() {
      JsonArray jsonArray = new JsonArray();
      CurvePoints curvePoints = this.getValue();
      jsonArray.add(curvePoints.getDoubleValue4());
      jsonArray.add(curvePoints.getDoubleValue3());
      jsonArray.add(curvePoints.getDoubleValue2());
      jsonArray.add(curvePoints.getDoubleValue());
      return jsonArray;
   }

   public GraphSetting(String text, @NotNull CurvePoints curvePoints) {
      this(text, "", curvePoints);
   }

   public GraphSetting(String text, @Nullable String text2, CurvePoints curvePoints) {
      super(text, text2, curvePoints);
   }

   @Override
   public GuiElement createEnumElement(GuiManager guiManager, GuiPanel guiPanel, String text, ModuleElement moduleElement) {
      return new GraphElement(guiManager, guiPanel, 0.0F, 0.0F, 0.0F, 0.0F, moduleElement, text, this);
   }

   public GraphSetting(String text) {
      this(text, CurvePoints.createCurvePoints());
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      JsonArray jsonArray = jsonElement.getAsJsonArray();
      CurvePoints curvePoints = this.getValue();

      for (int i = 0; i < 4; i++) {
         curvePoints.handleInt(i, jsonArray.get(i).getAsDouble());
      }
   }
}
