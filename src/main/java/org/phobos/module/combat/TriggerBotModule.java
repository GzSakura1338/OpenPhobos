package org.phobos.module.combat;

import java.util.Set;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.hit.EntityHitResult;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.ItemSetSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.render.RenderContext;
import org.phobos.core.PhobosClient;
import org.phobos.module.TargetModule;
import org.phobos.core.module.Categories;
import org.phobos.core.math.MathUtils;
import org.phobos.event.SafeListener;

public class TriggerBotModule extends TargetModule {
   public long longValue;
   public final Setting<Long> delay = this.resolveSetting2(new LongNumberSetting("Delay", 100L, 0L, 3000L, true));
   public final Setting<Long> spread = this.resolveSetting2(new LongNumberSetting("Spread", 0L, 0L, 150L, true).resolveSetting7("Randomness"));
   public final Setting<Set<Item>> items = this.resolveSetting2(
      new ItemSetSetting("Items", Items.WOODEN_SWORD, Items.GOLDEN_SWORD, Items.IRON_SWORD, Items.DIAMOND_SWORD, Items.NETHERITE_SWORD)
   );
   public final TimeGate.Normal normal = new TimeGate.Normal();

   public TriggerBotModule(PhobosClient phobosClient) {
      super(phobosClient, "TriggerBot", Categories.combat, "Clicks for you");
      this.handleListener(
         new SafeListener<RenderContext>() {
            public void handleObject(
               RenderContext renderContext,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (TriggerBotModule.this.items.getValue().contains(clientPlayerEntity.getMainHandStack().getItem())) {
                  if (mc.crosshairTarget instanceof EntityHitResult entityHitResult) {
                     Entity entity = entityHitResult.getEntity();
                     if (TriggerBotModule.this.testEntity2(entity)) {
                        if (TriggerBotModule.this.normal.testLong2(TriggerBotModule.this.longValue)) {
                           TriggerBotModule.this.longValue = TriggerBotModule.this.delay.getValue()
                              + MathUtils.computeLong5(0L, TriggerBotModule.this.spread.getValue());
                           mc.submit(mc::doAttack);
                        }
                     }
                  }
               }
            }
         }
      );
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }
}
