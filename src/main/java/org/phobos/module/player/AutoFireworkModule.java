package org.phobos.module.player;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworksComponent;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.player.InteractionManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.event.AutoFireworkPostSendClientCommandListener;
import org.phobos.module.Module;

public class AutoFireworkModule extends Module {
   public final TimeGate.Normal normal;
   public final Setting<InteractionManager.Silent> switchValue = new EnumSetting<>("Switch", InteractionManager.Silent.Normal).resolveSetting6(this);
   public int intValue;
   public final Setting<Long> delay = this.resolveSetting2(new LongNumberSetting("Delay", 3000L, 0L, 10000L, true));

   public AutoFireworkModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoFirework", Categories.player, "Uses a firework after starting to glide");
      this.normal = new TimeGate.Normal();
      this.handleListener(new AutoFireworkPostSendClientCommandListener(this));
   }

   @Override
   public void run3() {
      this.normal.run();
   }

   @Override
   public String nothing() {
      int i;
      return mc.player != null
            && this.intValue > 0
            && (i = this.intValue * 2 - mc.player.getFallFlyingTicks()) > 0
            && PlayerActionUtils.testClientPlayerEntity3(mc.player)
         ? String.valueOf(i)
         : null;
   }

   public boolean testFireworkRocketItem(FireworkRocketItem fireworkRocketItem) {
      ItemStack itemStack = new ItemStack(fireworkRocketItem);
      FireworksComponent fireworksComponent = (FireworksComponent)itemStack.get(DataComponentTypes.FIREWORKS);
      if (fireworksComponent != null) {
         this.intValue = fireworksComponent.flightDuration();
      }

      return true;
   }
}
