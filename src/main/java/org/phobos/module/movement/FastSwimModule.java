package org.phobos.module.movement;

import net.minecraft.client.network.ClientPlayerEntity;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.player.PlayerUtils;
import org.phobos.event.FastSwimMoveListener;
import org.phobos.module.Module;

public class FastSwimModule extends Module {
   public final Setting<Boolean> water;
   public float floatValue;
   public final Setting<Boolean> horizontal;
   public final Setting<FastSwimModule.Mode> mode = new EnumSetting<>("Mode", FastSwimModule.Mode.Intave).resolveSetting6(this);
   public final Setting<Boolean> lava;
   public final Setting<Float> verticalSpeed;
   public final Setting<Boolean> vertical;
   public final Setting<Float> horizontalSpeed;

   public FastSwimModule(PhobosClient phobosClient) {
      super(phobosClient, "FastSwim", Categories.movement, "Makes you faster in liquids");
      this.horizontal = new BooleanSetting("Horizontal").resolveSetting6(this);
      this.horizontalSpeed = new NumberSetting<>("HorizontalSpeed", 1.0F, 0.1F, 10.0F).resolveSetting6(this);
      this.vertical = new BooleanSetting("Vertical").resolveSetting6(this);
      this.verticalSpeed = new NumberSetting<>("VerticalSpeed", 1.0F, 0.1F, 10.0F).resolveSetting6(this);
      this.water = this.resolveSetting2(new BooleanSetting("Water"));
      this.lava = this.resolveSetting2(new BooleanSetting("Lava"));
      this.floatValue = 0.0F;
      this.resolveSettingGroup(this.horizontal, this.horizontalSpeed).resolveSettingGroup4(this.mode, FastSwimModule.Mode.Vanilla);
      this.resolveSettingGroup(this.vertical, this.verticalSpeed).resolveSettingGroup4(this.mode, FastSwimModule.Mode.Vanilla);
      this.handleListener(new FastSwimMoveListener(this));
   }

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      return clientPlayerEntity.isSwimming() && this.water.getValue() || clientPlayerEntity.isInLava() && this.lava.getValue() && PlayerUtils.check5();
   }

   public enum Mode {
      Vanilla,
      Intave;
   }
}
