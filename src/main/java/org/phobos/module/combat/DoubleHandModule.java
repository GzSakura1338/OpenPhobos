package org.phobos.module.combat;

import net.minecraft.screen.slot.Slot;
import org.phobos.api.TimeGate;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.input.InputUtils;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.event.DoubleHandInteractListener;
import org.phobos.event.DoubleHandPostReceiveEntitySpawnListener;
import org.phobos.event.DoubleHandSetBlockStateListener;
import org.phobos.event.DoubleHandTickListener;
import org.phobos.module.Module;

public class DoubleHandModule extends Module {
   public final Setting<Float> health;
   public float floatValue;
   public final Setting<Float> damage;
   public final Setting<Integer> slot;
   public final Setting<Boolean> auto;
   public final Setting<Long> timeout;
   public final TimeGate.Normal normal;
   public final Setting<Long> totem;
   public final Setting<Boolean> offhand;
   public final Setting<Boolean> ground;
   public final Setting<Boolean> anchorWorld;
   public static boolean flag = false;
   public final TimeGate.Threaded threaded;
   public final Setting<Boolean> anchor;
   public final Setting<DoubleHandModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", DoubleHandModule.Mode.Rage));
   public final Setting<Float> threshold;
   public final Setting<Boolean> shield;

   public void run() {
      this.phobosClient.getInventoryManager().handleConsumer2(itemSwitcher -> {
         Slot slot = itemSwitcher.getSlot();
         if (slot != null) {
            InputUtils.handleSlot(slot);
         }
      });
   }

   public boolean testFloat(float f) {
      return PlayerActionUtils.testDouble(f, this.threshold.getValue().floatValue());
   }

   public DoubleHandModule(PhobosClient phobosClient) {
      super(phobosClient, "DoubleHand", Categories.combat, "Holds a totem but works as a gap");
      this.anchor = this.resolveSetting2(new BooleanSetting("Anchor").resolveSetting7("Selects totem on when clicking a lethal anchor"));
      this.anchorWorld = this.resolveSetting2(new BooleanSetting("AnchorWorld").resolveSetting7("Assumes newly placed anchors are dangerous"));
      this.offhand = this.resolveSetting2(new BooleanSetting("Offhand").resolveSetting7("Offhand totem").resolveSetting4(this.mode, DoubleHandModule.Mode.Legit));
      this.ground = this.resolveSetting2(
         new BooleanSetting("Ground")
            .resolveSetting7("Requires you to be off ground to work with crystals")
            .resolveSetting4(this.mode, DoubleHandModule.Mode.Legit)
      );
      this.threshold = this.resolveSetting2(
         new NumberSetting<>("Threshold", 2.0F, 0.0F, 4.0F)
            .resolveSetting7("If damage is higher than (health - threshold)")
            .resolveSetting4(this.mode, DoubleHandModule.Mode.Legit)
      );
      this.timeout = this.resolveSetting2(new LongNumberSetting("Timeout", 750L, 50L, 3000L, true).resolveSetting4(this.mode, DoubleHandModule.Mode.Legit));
      this.slot = this.resolveSetting2(
         new NumberSetting<>("Slot", 1, 1, 9).resolveSetting7("Slot for double hand").resolveSetting4(this.mode, DoubleHandModule.Mode.Rage)
      );
      this.auto = this.resolveSetting2(new BooleanSetting("Auto").resolveSetting7("Switches to totem on low health"));
      this.health = this.resolveSetting2(new NumberSetting<>("Health", 10.0F, 1.0F, 36.0F).resolveSetting7("Gives you a totem under this health (Not locked)"));
      this.damage = this.resolveSetting2(
         new NumberSetting<>("Damage", 10.0F, 4.0F, 16.0F).resolveSetting7("Gives you a totem if you take this amount of damage")
      );
      this.totem = this.resolveSetting2(
         new LongNumberSetting("Totem", 100L, 50L, 500L, true).resolveSetting7("Gives you a totem if you popped within the timeout")
      );
      this.shield = this.resolveSetting2(new BooleanSetting("Shield").resolveSetting7("Does nothing if you're holding a shield"));
      this.threaded = new TimeGate.Threaded();
      this.normal = new TimeGate.Normal();
      this.resolveSettingGroup(this.anchor, this.anchorWorld).resolveSettingGroup4(this.mode, DoubleHandModule.Mode.Legit);
      this.resolveSettingGroup(this.auto, this.health, this.damage, this.totem).resolveSettingGroup4(this.mode, DoubleHandModule.Mode.Rage);
      this.handleListener(new DoubleHandInteractListener(this));
      this.handleListener(new DoubleHandSetBlockStateListener(this));
      this.handleListener(new DoubleHandPostReceiveEntitySpawnListener(this));
      this.handleListener(new DoubleHandTickListener(this));
   }

   public enum Mode {
      Legit,
      Rage;
   }
}
