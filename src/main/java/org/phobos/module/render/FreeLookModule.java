package org.phobos.module.render;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.Listener;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent;
import org.phobos.module.Module;

public class FreeLookModule extends Module {
   public RotationManager.Rotation rotation;
   public static FreeLookModule freeLookModule;
   public final Setting<Boolean> yaw;
   public final Setting<Boolean> nametags = this.resolveSetting2(new BooleanSetting("Nametags", false).resolveSetting7("Tweaks nametags transformation"));
   public final Setting<Boolean> pitch;

   public Setting<Boolean> getPitch() {
      return this.pitch;
   }

   public RotationManager.Rotation getRotation() {
      return this.rotation;
   }

   @Override
   public void run3() {
      if (mc.player != null) {
         this.rotation = new RotationManager.Rotation(mc.player.getYaw(), mc.player.getPitch());
      }
   }

   public static float computeClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity) {
      return freeLookModule.check7() ? freeLookModule.rotation.getFloatValue2() : clientPlayerEntity.getYaw();
   }

   public Setting<Boolean> getNametags() {
      return this.nametags;
   }

   public FreeLookModule(PhobosClient phobosClient) {
      super(phobosClient, "FreeLook", Categories.render, "Better F5");
      this.yaw = this.resolveSetting2(new BooleanSetting("Yaw", false).resolveSetting7("Uses starting yaw for nametags rotation"));
      this.pitch = this.resolveSetting2(new BooleanSetting("Pitch", false).resolveSetting7("Uses starting pitch for nametags rotation"));
      this.rotation = new RotationManager.Rotation(0.0F, 0.0F);
      this.resolveSettingGroup(this.nametags, this.yaw, this.pitch);
      this.handleListener(
         new SafeListener<PlayerTickEvent>(-1) {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               mc.options.setPerspective(Perspective.THIRD_PERSON_BACK);
               if (FreeLookModule.this.rotation != null && !RotationManager.isFlag()) {
                  phobosClient.getRotationManager()
                     .testFloat2(FreeLookModule.this.rotation.getFloatValue2(), FreeLookModule.this.rotation.getFloatValue(), true);
               }
            }
         }
      );
      this.handleListener(new Listener<TickEvent>(2) {
         public void handleObject2(TickEvent tickEvent) {
            tickEvent.setCancelled(true);
         }
      });
      freeLookModule = this;
   }

   public static boolean alwaysTrue() {
      return freeLookModule.check7() && freeLookModule.nametags.getValue();
   }

   public static float computeClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      return freeLookModule.check7() ? freeLookModule.rotation.getFloatValue() : clientPlayerEntity.getPitch();
   }

   @Override
   public void run4() {
      if (mc.player != null && this.rotation != null) {
         this.rotation.handleEntity(mc.player);
      }

      mc.options.setPerspective(Perspective.FIRST_PERSON);
      this.rotation = null;
   }

   public static float computeFloat2(float f) {
      return freeLookModule.check7() && freeLookModule.nametags.getValue() && freeLookModule.pitch.getValue() ? 0.0F : f;
   }

   public Setting<Boolean> getYaw() {
      return this.yaw;
   }

   public static FreeLookModule getFreeLookModule() {
      return freeLookModule;
   }

   public static float computeFloat(float f) {
      return freeLookModule.check7() && freeLookModule.nametags.getValue() && freeLookModule.yaw.getValue() ? freeLookModule.rotation.getFloatValue2() : f;
   }
}
