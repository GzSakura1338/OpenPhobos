package org.phobos.core.gui;

import java.util.ArrayList;
import java.util.List;
import org.phobos.api.ListenerHolder;
import org.phobos.api.AnimationValue;
import org.phobos.event.Listener;
import org.phobos.module.client.UIModule;

public abstract class InteractiveElement extends GuiElement implements ListenerHolder {
   public boolean flag10;
   public final List<Listener<?>> listenerList = new ArrayList<>();
   public final AnimationValue.Float floatValue13 = UIModule.getUIModule().animation.getValue().getFactory().getObject();

   public float getFloatValue33() {
      return this.getFloatValue4() + this.floatValue13.getFloatValue2();
   }

   public abstract void handleFlag(boolean var1);

   @Override
   public List<Listener<?>> getListenerList() {
      return this.listenerList;
   }

   public InteractiveElement(GuiManager guiManager, GuiPanel guiPanel, float f, float f2, float f3, float f4, ModuleElement moduleElement) {
      super(guiManager, guiPanel, f, f2, f3, f4, moduleElement);
   }

   public float value6() {
      return this.getFloatValue4() + this.floatValue13.getFloatValue();
   }

   public void setFlag10(boolean flag) {
      this.flag10 = flag;
   }

   public boolean isFlag10() {
      return this.flag10;
   }

   public AnimationValue.Float getFloatValue13() {
      return this.floatValue13;
   }
}
