package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import org.phobos.core.math.MathUtils;
import org.phobos.module.misc.AnnouncerModule;
import org.phobos.module.client.NotificationsModule;

public final class AnnouncerClearActiveItemListener extends ModuleListener.Safe<AnnouncerModule, ClearActiveItemEvent> {
   public AnnouncerClearActiveItemListener(AnnouncerModule announcerModule) {
      super(announcerModule, ClearActiveItemEvent.class);
   }

   public void handleObject(
      ClearActiveItemEvent clearActiveItemEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.consume.getValue()) {
         if (clearActiveItemEvent.getItemStack().isOf(Items.GOLDEN_APPLE) || clearActiveItemEvent.getItemStack().isOf(Items.ENCHANTED_GOLDEN_APPLE)) {
            this.module.intValue++;
            int i = MathUtils.computeInt5(1, 6);
            if (this.module.normal.testNumber(this.module.delay.getValue()) && this.module.intValue > i) {
               String text = "%s %s".formatted(this.module.intValue, clearActiveItemEvent.getItemStack().getName().getString());
               NotificationsModule.getNotificationsModule()
                  .handleTriConsumer(
                     (color, color2, color3) -> this.module
                        .handleText(
                           "I just ate %s".formatted(text),
                           Text.literal("I just ate ").withColor(color3.hashCode()).append(Text.literal(text).withColor(color2.hashCode()))
                        )
                  );
               this.module.intValue = 0;
            }
         }
      }
   }
}
