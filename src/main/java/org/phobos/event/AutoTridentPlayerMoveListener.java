package org.phobos.event;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.TridentItem;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.core.event.events.PlayerMoveEvent;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.tick.TickUtils;
import org.phobos.module.movement.AutoTridentModule;
import org.phobos.module.movement.AutoTridentModule;

public final class AutoTridentPlayerMoveListener extends ModuleListener.Safe<AutoTridentModule, PlayerMoveEvent> {
   public final List<Runnable> runnableList = new ArrayList<>();

   public AutoTridentPlayerMoveListener(AutoTridentModule autoTridentModule) {
      super(autoTridentModule, PlayerMoveEvent.class);
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, float f) {
      float f2 = this.phobosClient.getRotationManager().computeFloat4(clientPlayerEntity.getYaw());
      float f3 = this.phobosClient.getRotationManager().computeFloat2(clientPlayerEntity.getPitch());
      float f4 = -MathHelper.sin(f2 * (float) (Math.PI / 180.0)) * MathHelper.cos(f3 * (float) (Math.PI / 180.0));
      float f5 = -MathHelper.sin(f3 * (float) (Math.PI / 180.0));
      float f6 = MathHelper.cos(f2 * (float) (Math.PI / 180.0)) * MathHelper.cos(f3 * (float) (Math.PI / 180.0));
      float f7 = MathHelper.sqrt(f4 * f4 + f5 * f5 + f6 * f6);
      f4 *= f / f7;
      f5 *= f / f7;
      f6 *= f / f7;
      clientPlayerEntity.addVelocity(f4, f5, f6);
   }

   public void handleClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      TickUtils.handleClientWorld(
         clientWorld,
         integer -> clientPlayerEntity.networkHandler
            .sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, integer, clientPlayerEntity.getYaw(), clientPlayerEntity.getPitch()))
      );
   }

   public void handleObject(
      PlayerMoveEvent playerMoveEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      this.runnableList.forEach(Runnable::run);
      this.runnableList.clear();
      this.phobosClient.getInventoryManager().handleConsumer2(itemSwitcher -> {
         MutableObject<Float> mutableObject = new MutableObject(0.0F);
         Slot slot = itemSwitcher.resolveObject(slotx -> {
            if (slotx.getStack().getItem() instanceof TridentItem) {
               float f = EnchantmentHelper.getTridentSpinAttackStrength(slotx.getStack(), clientPlayerEntity);
               if (f > 0.0F) {
                  mutableObject.setValue(f);
                  return slotx;
               }
            }

            return null;
         });
         if (slot != null) {
            switch ((AutoTridentModule.Mode)this.module.mode.getValue()) {
               case Vanilla:
                  ItemSwitcher.SwitchResult switchResult = itemSwitcher.resolveSwitchResult(Hand.MAIN_HAND, slot, 3);
                  if (switchResult == null) {
                     return;
                  }

                  this.handleClientPlayerEntity2(clientPlayerEntity, clientWorld);
                  this.handleClientPlayerEntity4(clientPlayerEntity);
                  break;
               case Grim:
                  if (!this.module.normal.testLong(500L)) {
                     return;
                  }

                  ItemSwitcher.SwitchResult switchResult2 = itemSwitcher.resolveSwitchResult(Hand.MAIN_HAND, slot, 1);
                  if (switchResult2 == null) {
                     return;
                  }

                  this.handleClientPlayerEntity2(clientPlayerEntity, clientWorld);
                  this.handleClientPlayerEntity4(clientPlayerEntity);
                  this.module.normal.run();
            }
         }
      });
   }

   public void handleClientPlayerEntity4(ClientPlayerEntity clientPlayerEntity) {
      clientPlayerEntity.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN));
   }
}
