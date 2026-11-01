package org.phobos.module.combat;

import net.minecraft.block.Block;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.player.InteractionManager;
import org.phobos.core.player.InteractionManager;
import org.phobos.core.combat.CrystalPlacement;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.world.RaycastUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.event.InteractEvent;
import org.phobos.event.SafeListener;

public class AutoCrystalModule extends PlacesCrystalsModule {
   public static AutoCrystalModule autoCrystalModule;
   public boolean flag;
   public final Setting<Boolean> runTests = this.resolveSetting2(new BooleanSetting("RunTests"));
   public BlockPos blockPos;

   public static void handleEndCrystalEntity(EndCrystalEntity endCrystalEntity, boolean flag) {
      if (mc.player != null && mc.world != null && mc.interactionManager != null) {
         autoCrystalModule.handleEndCrystalEntity2(endCrystalEntity, mc.player, mc.interactionManager, flag);
      }
   }

   public void handleBlockPos(BlockPos blockPos) {
      this.phobosClient.getInventoryManager().handleConsumer2(itemSwitcher -> {
         Block block = this.resolveBlock3(itemSwitcher);
         if (block != null) {
            this.resolvePlacement8(block, blockPos);
         }
      });
   }

   public void handleClientPlayerEntity(
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      CrystalPlacement crystalPlacement,
      InteractionManager.SwitchMode switchMode,
      InteractionManager.Silent silent,
      boolean... flags
   ) {
      this.getModuleRecord().handleClientPlayerEntity(clientPlayerEntity, clientWorld, crystalPlacement, switchMode, silent);
      if (flags.length > 0) {
         this.resolvePlacesCrystalsModule2(flags[0]);
      }
   }

   public static AutoCrystalModule getAutoCrystalModule() {
      return autoCrystalModule;
   }

   public void handleClientPlayerEntity2(
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      BlockPos blockPos,
      InteractionManager.SwitchMode switchMode,
      InteractionManager.Silent silent,
      boolean... flags
   ) {
      this.getModuleRecord().handleClientPlayerEntity(clientPlayerEntity, clientWorld, blockPos, switchMode, silent);
      if (flags.length > 0) {
         this.resolvePlacesCrystalsModule2(flags[0]);
      }
   }

   public AutoCrystalModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoCrystal", Categories.combat);
      this.runTests.handleConsumer2(booleanValue -> {
         if (this.runTests.getValue()) {
            this.flag = true;
            this.runTests.setValue(false);
         }
      });
      this.handleListener(
         new SafeListener<InteractEvent>() {
            public void handleObject(
               InteractEvent interactEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (AutoCrystalModule.this.flag) {
                  AutoCrystalModule.this.blockPos = interactEvent.getBlockHitResult().getBlockPos();
                  AutoCrystalModule.this.flag = false;
               }
            }
         }
      );
      autoCrystalModule = this;
   }

   public void handleEndCrystalEntity2(
      EndCrystalEntity endCrystalEntity, ClientPlayerEntity clientPlayerEntity, ClientPlayerInteractionManager clientPlayerInteractionManager, boolean flag
   ) {
      double d = this.getDoubleValueSetting5().getValue();
      if (!(endCrystalEntity.getBoundingBox().squaredMagnitude(clientPlayerEntity.getEyePos()) >= d * d)) {
         boolean flag2 = false;
         if (this.getBooleanValueSetting17().getValue()
            && this.getRotateSetting().getValue().equals(PlacesCrystalsModule.Rotate.Silent)
            && !RaycastUtils.testPhobosClient(this.phobosClient, endCrystalEntity)) {
            if (!this.getIntegerSetting6().testObject3(-1) && this.getAtomicInteger2().get() > this.getIntegerSetting6().getValue()) {
               return;
            }

            float[] floats = RotationUtils.values(clientPlayerEntity, endCrystalEntity);
            this.phobosClient.getRotationManager().handleClientPlayerEntity3(clientPlayerEntity, floats[0], floats[1]);
            this.getAtomicInteger2().incrementAndGet();
            flag2 = true;
         }

         clientPlayerInteractionManager.attackEntity(clientPlayerEntity, endCrystalEntity);
         Hand hand = clientPlayerEntity.getOffHandStack().isOf(Items.END_CRYSTAL) ? Hand.OFF_HAND : Hand.MAIN_HAND;
         switch ((PlacesCrystalsModule.SwingType)this.getSwingTypeSetting2().getValue()) {
            case Auto:
               clientPlayerEntity.swingHand(hand);
               break;
            case Offhand:
               clientPlayerEntity.swingHand(Hand.OFF_HAND);
               break;
            case Mainhand:
               clientPlayerEntity.swingHand(Hand.MAIN_HAND);
               break;
            case Packet:
               clientPlayerEntity.networkHandler.sendPacket(new HandSwingC2SPacket(hand));
         }

         if (flag2) {
            this.phobosClient.getRotationManager().handleClientPlayerEntity5(clientPlayerEntity);
         }

         this.handleEndCrystalEntity3(endCrystalEntity, flag);
      }
   }
}
