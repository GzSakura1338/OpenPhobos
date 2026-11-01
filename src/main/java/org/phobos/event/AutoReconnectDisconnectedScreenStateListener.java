package org.phobos.event;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.phobos.core.gui.DisconnectedScreenState;
import org.phobos.core.math.MathUtils;
import org.phobos.module.misc.AutoReconnectModule;

public final class AutoReconnectDisconnectedScreenStateListener extends ModuleListener<AutoReconnectModule, DisconnectedScreenState> {
   public void handleObject2(DisconnectedScreenState disconnectedScreenState) {
      this.module.flag = true;
      this.module.longValue = System.currentTimeMillis();
      this.module.normal.run();
      disconnectedScreenState.getDirectionalLayoutWidget()
         .add(
            ButtonWidget.builder(Text.literal("Reconnect"), buttonWidget -> this.module.handleScreen(disconnectedScreenState.getDisconnectedScreen())).width(200).build()
         );
      disconnectedScreenState.getDirectionalLayoutWidget().add(ButtonWidget.builder(this.describe2(), buttonWidget -> {
         this.module.flag = !this.module.flag;
         this.module.longValue = this.module.normal.value();
         buttonWidget.setMessage(this.describe2());
         this.module.handleDisconnectedScreen(disconnectedScreenState.getDisconnectedScreen());
      }).width(200).build());
      TextWidget textWidget = this.getTextWidget();
      disconnectedScreenState.getDirectionalLayoutWidget().add(textWidget);
   }

   public AutoReconnectDisconnectedScreenStateListener(AutoReconnectModule autoReconnectModule) {
      super(autoReconnectModule, DisconnectedScreenState.class);
   }

   public String describe() {
      float f = MathUtils.computeFloat5(
         (float)(this.module.delay.getValue() - (this.module.flag ? this.module.normal.value() : this.module.longValue)) / 1000.0F, 1
      );
      return this.module.flag2 ? Formatting.RED + "No ServerData found!" : "Reconnecting in " + (f <= 0.0F ? "0.0" : f) + "s.";
   }

   @NotNull
   public TextWidget getTextWidget() {
      var value = new TextWidget(Text.literal(this.describe()), mc.textRenderer) {
         public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            super.renderWidget(context, mouseX, mouseY, delta);
            this.setMessage(Text.literal(AutoReconnectDisconnectedScreenStateListener.this.describe()));
         }
      };
      value.setWidth(500);
      value.alignCenter();
      return value;
   }

   public Text describe2() {
      return Text.literal("AutoReconnect: ")
         .formatted(Formatting.RESET)
         .append(Text.literal(this.module.flag ? "On" : "Off").formatted(this.module.flag ? Formatting.GREEN : Formatting.RED));
   }
}
