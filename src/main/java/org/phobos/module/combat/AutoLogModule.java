package org.phobos.module.combat;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Items;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.tick.TickCounter;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.event.EntityStatusEvent;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class AutoLogModule extends Module {
   public final Setting<AutoLogModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", AutoLogModule.Mode.Pop));
   public final Setting<Float> health = this.resolveSetting2(
      new NumberSetting<>("Health", 10.0F, 0.1F, 36.0F).resolveSetting4(this.mode, AutoLogModule.Mode.Health)
   );
   public final Setting<Boolean> noTotems = this.resolveSetting2(
      new BooleanSetting("NoTotems").resolveSetting7("Only when you have no totems").resolveSetting4(this.mode, AutoLogModule.Mode.Health)
   );
   public final Setting<Integer> totems = this.resolveSetting2(new NumberSetting<>("Totems", 1, 0, 5).resolveSetting4(this.mode, AutoLogModule.Mode.Totems));

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   public AutoLogModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoLog", Categories.combat);
      this.handleListener(
         new SafeListener<EntityStatusEvent>() {
            public void handleObject(
               EntityStatusEvent entityStatusEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (AutoLogModule.this.mode.testObject2(AutoLogModule.Mode.Pop)) {
                  if (entityStatusEvent.entity().equals(clientPlayerEntity)) {
                     PlayerActionUtils.run();
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<TickCounter>() {
            public void handleObject(
               TickCounter tickCounter,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               switch ((AutoLogModule.Mode)AutoLogModule.this.mode.getValue()) {
                  case Pop:
                     if (EntityUtils.computeLivingEntity(clientPlayerEntity) <= AutoLogModule.this.health.getValue()) {
                        return;
                     }

                     if (AutoLogModule.this.noTotems.getValue() && ItemSwitcher.testItem3(Items.TOTEM_OF_UNDYING)) {
                        return;
                     }

                     PlayerActionUtils.run();
                     break;
                  case Totems:
                     phobosClient.getInventoryManager().handleConsumer2(itemSwitcher -> {
                        if (itemSwitcher.computePredicate(itemStack -> itemStack.isOf(Items.TOTEM_OF_UNDYING)) <= AutoLogModule.this.totems.getValue()) {
                           PlayerActionUtils.run();
                        }
                     });
               }
            }
         }
      );
   }

   public enum Mode {
      Pop,
      Health,
      Totems;
   }
}
