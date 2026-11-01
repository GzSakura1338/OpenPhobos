package org.phobos.core.hud.element;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.event.events.PacketEvent.PostSend;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.core.hud.CpsCounter;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.Setting;
import org.phobos.core.hud.TextHudElement;

public class PacketsHudElement extends TextHudElement {
   public final CpsCounter cpsCounter;
   public final Setting<PacketsHudElement.Mode> mode = this.resolveSetting2(
      new EnumSetting<>("Mode", PacketsHudElement.Mode.Constant)
   );

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      int i3 = this.cpsCounter.value();
      String text = this.describeText2("Packets: ", String.valueOf(i3));
      this.handleDrawContext9(drawContext, text);
   }

   public PacketsHudElement() {
      super("Packets");
      this.cpsCounter = new CpsCounter();
      this.handleListener(
         new SafeListener<PostSend<?>>() {
            public void handleObject(
               PostSend<?> postSend,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               PacketsHudElement.this.cpsCounter.run2();
            }
         }
      );
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (PacketsHudElement.this.mode.testObject2(PacketsHudElement.Mode.Tick)) {
                  PacketsHudElement.this.cpsCounter.run();
               }
            }
         }
      );
   }

   public enum Mode {
      Constant,
      Tick;
   }
}
