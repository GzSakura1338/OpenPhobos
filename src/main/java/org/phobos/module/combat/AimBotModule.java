package org.phobos.module.combat;

import java.util.Objects;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.jetbrains.annotations.NotNull;
import org.phobos.api.RotationCalculator;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.rotation.AimCalculatorA;
import org.phobos.core.rotation.AimCalculatorB;
import org.phobos.core.PhobosClient;
import org.phobos.module.TargetModule;
import org.phobos.core.module.Categories;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;

public class AimBotModule extends TargetModule {
   public final Setting<Boolean> bow;
   public final RotationCalculator rotationCalculator;
   public final Setting<Integer> ticks;
   public final RotationCalculator rotationCalculator2;
   public final Setting<Double> range = this.resolveSetting2(new NumberSetting<>("Range", 64.0, 6.0, 200.0));
   public final Setting<Boolean> prediction;
   public final Setting<Boolean> thorwables;

   public AimBotModule(PhobosClient phobosClient) {
      super(phobosClient, "AimBot", Categories.combat, "Adjusts your aim");
      this.bow = this.resolveSetting2(new BooleanSetting("Bow"));
      this.thorwables = this.resolveSetting2(new BooleanSetting("Thorwables").resolveSetting7("Snowballs & Eggs"));
      this.prediction = this.resolveSetting2(new BooleanSetting("Prediction"));
      this.ticks = this.resolveSetting2(new NumberSetting<>("Ticks", 1, 1, 5));
      this.rotationCalculator2 = new AimCalculatorB();
      this.rotationCalculator = new AimCalculatorA();
      this.resolveSettingGroup(this.prediction, this.ticks);
      this.handleListener(
         new SafeListener<PlayerTickEvent>(8) {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               AimBotModule.this.handleClientPlayerEntity2(
                  clientPlayerEntity,
                  clientWorld,
                  AimBotModule.this.range.getValue(),
                  livingEntity -> {
                     LivingEntity livingEntity2 = AimBotModule.this.resolveLivingEntity(livingEntity);
                     if (AimBotModule.this.bow.getValue()) {
                        ItemStack itemStack = clientPlayerEntity.getActiveItem();
                        if (itemStack != null && itemStack.isOf(Items.BOW)) {
                           float[] floats = AimBotModule.this.rotationCalculator2.values(clientPlayerEntity, livingEntity2);
                           phobosClient.getRotationManager().testFloat(floats[0], floats[1]);
                           return;
                        }
                     }

                     if (AimBotModule.this.thorwables.getValue()
                        && (
                           AimBotModule.this.testClientPlayerEntity(clientPlayerEntity, Items.EGG)
                              || AimBotModule.this.testClientPlayerEntity(clientPlayerEntity, Items.SNOWBALL)
                        )) {
                        float[] floats2 = AimBotModule.this.rotationCalculator.values(clientPlayerEntity, livingEntity2);
                        phobosClient.getRotationManager().testFloat(floats2[0], floats2[1]);
                     }
                  }
               );
            }
         }
      );
   }

   @NotNull
   public LivingEntity resolveLivingEntity(@NotNull LivingEntity livingEntity) {
      if (this.prediction.getValue() && livingEntity instanceof PlayerEntity playerEntity2) {
         PlayerEntity playerEntity = this.phobosClient.getFeatureRegistry().getPredictionManager().resolvePlayerEntity3(this.ticks.getValue(), playerEntity2);
         return Objects.requireNonNullElse(playerEntity, livingEntity);
      } else {
         return livingEntity;
      }
   }

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, Item item) {
      return clientPlayerEntity.getMainHandStack().isOf(item) || clientPlayerEntity.getOffHandStack().isOf(item);
   }
}
