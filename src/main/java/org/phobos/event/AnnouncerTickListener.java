package org.phobos.event;

import java.text.DecimalFormat;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import org.phobos.module.misc.AnnouncerModule;
import org.phobos.module.client.NotificationsModule;

public final class AnnouncerTickListener extends ModuleListener.Safe<AnnouncerModule, TickEvent2> {
   public final DecimalFormat decimalFormat = new DecimalFormat("0.00");

   public AnnouncerTickListener(AnnouncerModule announcerModule) {
      super(announcerModule, TickEvent2.class);
   }

   public void handleObject(
      TickEvent2 tickEvent2, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.movement.getValue()) {
         if (this.module.vec3d == null) {
            this.module.vec3d = clientPlayerEntity.getPos();
            this.module.normal.run();
         } else if (this.module.normal.testNumber(this.module.delay.getValue())) {
            double d = clientPlayerEntity.getPos().distanceTo(this.module.vec3d);
            if (!(d <= 1.0) && !(d > 1000.0)) {
               this.module.vec3d = clientPlayerEntity.getPos();
               NotificationsModule.getNotificationsModule()
                  .handleTriConsumer(
                     (color, color2, color3) -> this.module
                        .handleText(
                           "I just walked %s blocks".formatted(this.decimalFormat.format(d)),
                           Text.literal("I just walked ")
                              .withColor(color3.hashCode())
                              .append(
                                 Text.literal(this.decimalFormat.format(d))
                                    .withColor(color2.hashCode())
                                    .append(Text.literal(" blocks").withColor(color3.hashCode()))
                              )
                        )
                  );
            }
         }
      }
   }
}
