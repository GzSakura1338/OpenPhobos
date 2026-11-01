package org.phobos.module.player;

import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.BlockItem;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.BridgeTickListener;
import org.phobos.module.Module;

public class BridgeModule extends Module {
   public final Setting<Boolean> left;
   public final Setting<Boolean> backward;
   public final Setting<Boolean> forward;
   public final Setting<Boolean> ground;
   public final Setting<Float> pitch;
   public final Setting<Boolean> blocks;
   public final Setting<Boolean> right;
   public final Setting<Boolean> sneak;
   public final Setting<Float> distance = new NumberSetting<>("Distance", 0.4F, 0.01F, 1.3F).resolveSetting6(this);

   public BridgeModule(PhobosClient phobosClient) {
      super(phobosClient, "Bridge", Categories.player, "Helps you legit bridge");
      this.blocks = new BooleanSetting("Blocks").resolveSetting6(this);
      this.ground = new BooleanSetting("Ground", true).resolveSetting6(this);
      this.pitch = new NumberSetting<>("Pitch", -90.0F, -90.0F, 90.0F).resolveSetting6(this);
      this.sneak = new BooleanSetting("Sneak").resolveSetting6(this);
      this.left = new BooleanSetting("Left").resolveSetting6(this);
      this.right = new BooleanSetting("Right").resolveSetting6(this);
      this.forward = new BooleanSetting("Forward").resolveSetting6(this);
      this.backward = new BooleanSetting("Backward").resolveSetting6(this);
      this.resolveSettingGroup2("Filter", this.blocks, this.ground, this.pitch, this.sneak, this.left, this.right, this.forward, this.backward);
      this.handleListener(new BridgeTickListener(this));
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }

   public boolean testKeyboardInput(KeyboardInput keyboardInput, ClientPlayerEntity clientPlayerEntity) {
      if (clientPlayerEntity == null) {
         return false;
      } else if (keyboardInput.sneaking) {
         return true;
      } else if (this.blocks.getValue()
         && !(clientPlayerEntity.getMainHandStack().getItem() instanceof BlockItem)
         && !(clientPlayerEntity.getOffHandStack().getItem() instanceof BlockItem)) {
         return false;
      } else if (this.ground.getValue() && !clientPlayerEntity.isOnGround()) {
         return false;
      } else if (clientPlayerEntity.getPitch() > this.pitch.getValue() && clientPlayerEntity.getPitch() < this.pitch.getValue()) {
         return false;
      } else if (this.sneak.getValue() && !keyboardInput.sneaking) {
         return false;
      } else if (this.left.getValue() && !keyboardInput.pressingLeft) {
         return false;
      } else if (this.right.getValue() && !keyboardInput.pressingRight) {
         return false;
      } else if (this.forward.getValue() && !keyboardInput.pressingForward) {
         return false;
      } else {
         return this.backward.getValue() && !keyboardInput.pressingBack ? false : false;
      }
   }
}
