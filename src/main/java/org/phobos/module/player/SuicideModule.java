package org.phobos.module.player;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.combat.CrystalSafetyCalculator;
import org.phobos.core.combat.CrystalTargetSelector;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.module.combat.AutoCrystalModule;
import org.phobos.module.combat.PlacesCrystalsModule;

public class SuicideModule extends PlacesCrystalsModule {
   public static SuicideModule suicideModule;
   public final Setting<Boolean> copy = this.resolveSetting2(new BooleanSetting("Copy", false));

   @Override
   public boolean testCrystalSafetyCalculator(CrystalSafetyCalculator crystalSafetyCalculator, EndCrystalEntity endCrystalEntity, MutableObject<Float> mutableObject) {
      return true;
   }

   @Override
   public CrystalTargetSelector createValue(ClientWorld clientWorld, ClientPlayerEntity clientPlayerEntity) {
      return new CrystalTargetSelector(this, this.getPhobosClient(), clientPlayerEntity, clientWorld, this.getPhobosClient().getDamageCalculator()) {
         @Override
         public boolean testLivingEntity(LivingEntity livingEntity, double d, double d2, double d3) {
            return livingEntity == null ? false : livingEntity.equals(this.clientPlayerEntity);
         }

         @Override
         public float computeEntity4(Entity entity) {
            return 0.0F;
         }

         @Override
         public double value() {
            return this.clientPlayerEntity.getY() + 2.0;
         }

         @Override
         public float computeBlockPos2(BlockPos blockPos, World world) {
            return 0.0F;
         }
      };
   }

   public static SuicideModule getSuicideModule() {
      return suicideModule;
   }

   public SuicideModule(PhobosClient phobosClient) {
      super(phobosClient, "Suicide", Categories.player);
      this.copy
         .handleConsumer2(
            booleanValue -> {
               if (this.copy.getValue()) {
                  this.copy.setValue(false);

                  for (Setting<?> setting : this.getSettingList()) {
                     if (AutoCrystalModule.getAutoCrystalModule().resolveOptional2(setting.getName(), setting.getValueClassValue2()).isPresent()
                        && !setting.getName().equalsIgnoreCase("Bind")
                        && !setting.getName().equalsIgnoreCase("Enabled")
                        && !setting.getName().equalsIgnoreCase("Shown")
                        && !setting.getName().equalsIgnoreCase("Toggle")) {
                        setting.testObject(
                           AutoCrystalModule.getAutoCrystalModule().resolveOptional2(setting.getName(), setting.getValueClassValue2()).get().getValue()
                        );
                     }
                  }
               }
            }
         );
      ModuleListenerUtils.handleModule(this);
      suicideModule = this;
   }

   @Override
   public void run3() {
      ClientPlayerEntity clientPlayerEntity = mc.player;
      if (mc.player != null) {
         clientPlayerEntity.networkHandler.sendChatCommand("kill");
      }
   }
}
