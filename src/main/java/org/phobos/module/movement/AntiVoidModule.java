package org.phobos.module.movement;

import net.minecraft.block.Block;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.tick.TimerManager;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.block.BlockUtils;
import org.phobos.mixin.game.input.IKeyBinding;
import org.phobos.type.KeyboardMouseEnum;

public class AntiVoidModule extends PlacementModule {
   public final Setting<AntiVoidModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", AntiVoidModule.Mode.Timer));
   public final Setting<Double> doubleValueSetting2 = this.resolveSetting2(new NumberSetting<>("Y", -20.0, -100.0, 345.0));

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      if (this.mode.getValue().equals(AntiVoidModule.Mode.Timer) && !this.phobosClient.getRotationDelay().check()) {
         ItemStack itemStack = clientPlayerEntity.getInventory().getArmorStack(2);
         return itemStack.isOf(Items.ELYTRA) && ElytraItem.isUsable(itemStack)
            ? clientPlayerEntity.getItemCooldownManager().isCoolingDown(Items.FIREWORK_ROCKET)
            : false;
      } else {
         return false;
      }
   }

   public AntiVoidModule(PhobosClient phobosClient) {
      super(phobosClient, "AntiVoid", "Prevents you from falling into the void", Categories.movement, -15);
      this.getSettingGroupList2().forEach(settingGroup -> settingGroup.resolveSettingGroup5(() -> this.mode.getValue().equals(AntiVoidModule.Mode.Clutch)));
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }

   public boolean testEntity(Entity entity, ClientWorld clientWorld) {
      if (entity.isOnGround()) {
         return false;
      }

      for (BlockPos blockPos : BlockPosUtils.resolveSet3(entity, entity.getBoundingBox().minY - 0.25)) {
         for (int i = blockPos.getY() - 1; i > -64; i--) {
            if (BlockUtils.testBlockPos7(new BlockPos(blockPos.getX(), i, blockPos.getZ()), clientWorld)) {
               return false;
            }
         }
      }

      return true;
   }

   public boolean testInt(int i) {
      return this.phobosClient.getBindManager().testKeyboardMouseEnum(KeyboardMouseEnum.Keyboard, i);
   }

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.testEntity(clientPlayerEntity, clientWorld)
         && !(clientPlayerEntity.getY() > this.doubleValueSetting2.getValue())
         && !this.check()
         && !this.testClientPlayerEntity(clientPlayerEntity)) {
         switch ((AntiVoidModule.Mode)this.mode.getValue()) {
            case Clutch:
               for (int i = 0; i < 10; i++) {
                  PlayerActionUtils.handleClientPlayerEntity11(clientPlayerEntity);
               }
               break;
            case Lag:
               this.phobosClient.getTimerManager().handleFloat(0.01F, TimerManager.TimerPriority.High);
               break;
            case Timer:
               BlockPos blockPos2 = this.resolveBlockPos(clientPlayerEntity, clientWorld);
               if (blockPos2 == null) {
                  return;
               }

               Block block = itemSwitcher.resolveObject(
                  slot -> slot.getStack().getItem() instanceof BlockItem blockItem && BlockUtils.testClientWorld2(clientWorld, blockItem.getBlock())
                     ? blockItem.getBlock()
                     : null
               );
               if (block == null) {
                  return;
               }

               for (BlockPos blockPos : this.resolveList2(blockPos2, clientWorld, clientPlayerEntity, block)) {
                  this.resolvePlacement8(block, blockPos);
               }

               this.resolvePlacement8(block, blockPos2);
         }
      } else if (this.phobosClient.getTimerManager().getFloatValue22() == 0.01F) {
         this.phobosClient.getTimerManager().run2();
      }
   }

   public BlockPos resolveBlockPos(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      return BlockPosUtils.resolveSet7(clientPlayerEntity, 0.0).stream().<BlockPos>map(BlockPos::down).findAny().orElse(null);
   }

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   @Override
   public void run4() {
      this.phobosClient.getTimerManager().run2();
   }

   public boolean check() {
      if (!this.mode.getValue().equals(AntiVoidModule.Mode.Timer)) {
         return false;
      }

      for (KeyBinding keyBinding : mc.options.hotbarKeys) {
         if (!this.testInt(((IKeyBinding)(Object)keyBinding).getBoundKey().getCode())) {
            return false;
         }
      }

      return true;
   }

   private enum Mode {
      Lag,
      Timer,
      Clutch;
   }
}
