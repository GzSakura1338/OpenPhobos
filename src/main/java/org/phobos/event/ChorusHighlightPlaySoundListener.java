package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import org.phobos.core.render.Render3d;
import org.phobos.core.render.Render3d;
import org.phobos.module.render.ChorusHighlightModule;

public final class ChorusHighlightPlaySoundListener extends ModuleListener.Safe<ChorusHighlightModule, PlaySoundEvent> {
   public ChorusHighlightPlaySoundListener(ChorusHighlightModule chorusHighlightModule) {
      super(chorusHighlightModule, PlaySoundEvent.class);
   }

   public void handleObject(
      PlaySoundEvent playSoundEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (playSoundEvent.getSoundEvent().equals(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT)) {
         double d = clientPlayerEntity.getBoundingBox().getLengthY();
         double d2 = playSoundEvent.getDoubleValue2();
         double d3 = playSoundEvent.getDoubleValue3();
         double d4 = playSoundEvent.getDoubleValue();
         Render3d.handleCuboid(
            Render3d.Cuboid.createBuilder()
               .resolveBuilder11(new Box(d2 - 0.3, d3, d4 - 0.3, d2 + 0.3, d3 + d, d4 + 0.3))
               .resolveBuilder18(this.module.box.getValue(), this.module.line.getValue())
               .resolveBuilder12(this.module.width.getValue())
               .resolveBuilder5(this.module.fade.getValue())
               .getCuboid()
         );
      }
   }
}
