package org.phobos.module.misc;

import java.util.Map;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.MultiSelectSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent;
import org.phobos.event.TickMovementEvent;
import org.phobos.module.Module;

public class AutoWalkModule extends Module {
   public final Setting<Map<AutoWalkModule.Input, Boolean>> inputs = this.resolveSetting2(
      new MultiSelectSetting<>("Inputs", AutoWalkModule.Input.class).resolveMultiSelectSetting6(AutoWalkModule.Input.values()).resolveSetting7("Additional inputs")
   );

   public AutoWalkModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoWalk", Categories.misc, "Makes you walk forward");
      this.handleListener(
         new SafeListener<TickEvent>(Integer.MAX_VALUE) {
            public void handleObject(
               TickEvent tickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               KeyboardInput keyboardInput = tickEvent.getKeyboardInput();
               keyboardInput.movementForward = 1.0F;
               keyboardInput.movementSideways = 0.0F;
               keyboardInput.jumping = AutoWalkModule.this.inputs.getValue().get(AutoWalkModule.Input.Jump);
               keyboardInput.sneaking = AutoWalkModule.this.inputs.getValue().get(AutoWalkModule.Input.Sneak);
            }
         }
      );
      this.handleListener(
         new SafeListener<TickMovementEvent>() {
            public void handleObject(
               TickMovementEvent tickMovementEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               tickMovementEvent.setFlag(AutoWalkModule.this.inputs.getValue().get(AutoWalkModule.Input.Sprint));
            }
         }
      );
   }

   private enum Input {
      Jump,
      Sneak,
      Sprint;
   }
}
