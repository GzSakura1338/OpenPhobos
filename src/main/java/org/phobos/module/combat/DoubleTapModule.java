package org.phobos.module.combat;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.input.InputUtils;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.event.InteractEvent;
import org.phobos.event.OnMouseButtonEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.AttackEvent2.Pre;
import org.phobos.type.KeyboardMouseEnum;
import org.phobos.module.Module;
import org.phobos.module.player.SilentHotbarModule;

public class DoubleTapModule extends Module {
   public final Setting<Boolean> autoSword;
   public final Setting<Boolean> sword;
   public final Setting<Boolean> filter;
   public final TimeGate.Normal normal;
   public final Setting<Long> delay;
   public final Setting<Boolean> crystal = this.resolveSetting2(
      new BooleanSetting("Crystal", true).resolveSetting7("Places obsidian when right clicking with crystals")
   );
   public boolean flag;
   public final Setting<Boolean> totem;

   public DoubleTapModule(PhobosClient phobosClient) {
      super(phobosClient, "DoubleTap", "Places obsidian macro-like", Categories.combat);
      this.sword = this.resolveSetting2(new BooleanSetting("Sword", true).resolveSetting7("Places obsidian when right clicking with sword"));
      this.filter = this.resolveSetting2(new BooleanSetting("Filter", true));
      this.totem = this.resolveSetting2(new BooleanSetting("Totem").resolveSetting7("Switches to a crystal when right clicking obsidian with a totem"));
      this.delay = this.resolveSetting2(new LongNumberSetting("Delay", 50L, 0L, 500L, true));
      this.autoSword = this.resolveSetting2(new BooleanSetting("AutoSword").resolveSetting7("When attacking a player, swaps to a sword"));
      this.normal = new TimeGate.Normal();
      this.flag = false;
      this.handleListener(
         new SafeListener<Pre>() {
            public void handleObject(
               Pre pre, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (DoubleTapModule.this.autoSword.getValue()) {
                  if (!(clientPlayerEntity.getMainHandStack().getItem() instanceof AxeItem)) {
                     if (pre.getEntity() instanceof PlayerEntity playerEntity) {
                        if (EntityUtils.testPhobosClient2(phobosClient, clientPlayerEntity, playerEntity)) {
                           phobosClient.getInventoryManager()
                              .handleConsumer2(
                                 itemSwitcher -> itemSwitcher.handleRunnable3(
                                    () -> {
                                       Slot slot = itemSwitcher.resolveObject(slotx -> slotx.getStack().getItem() instanceof SwordItem ? slotx : null);
                                       if (slot != null) {
                                          SilentHotbarModule.getSilentHotbarManager()
                                             .handleObject3(
                                                DoubleTapModule.this,
                                                slot,
                                                2,
                                                SilentHotbarModule.getSilentHotbarManager().getBuilder().resolveBuilder3(true).getSelectionConfig()
                                             );
                                       }
                                    }
                                 )
                              );
                        }
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<OnMouseButtonEvent>() {
            public void handleObject(
               OnMouseButtonEvent onMouseButtonEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (onMouseButtonEvent.getKeyboardMouseEnum() == KeyboardMouseEnum.Mouse
                  && onMouseButtonEvent.getIntValue() == 1
                  && onMouseButtonEvent.getIntValue2() == 1) {
                  DoubleTapModule.this.flag = true;
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<InteractEvent>() {
            public void handleObject(
               InteractEvent interactEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (interactEvent.getBlockHitResult().getBlockPos() != null) {
                  if (mc.currentScreen == null) {
                     if (DoubleTapModule.this.normal.testNumber(DoubleTapModule.this.delay.getValue()) || DoubleTapModule.this.flag) {
                        DoubleTapModule.this.flag = false;
                        phobosClient.getInventoryManager()
                           .handleConsumer2(
                              itemSwitcher -> itemSwitcher.handleRunnable3(
                                 () -> {
                                    DoubleTapModule.this.normal.run();
                                    if ((!clientPlayerEntity.getMainHandStack().isOf(Items.END_CRYSTAL) || !DoubleTapModule.this.crystal.getValue())
                                       && (!(clientPlayerEntity.getMainHandStack().getItem() instanceof SwordItem) || !DoubleTapModule.this.sword.getValue())) {
                                       if (DoubleTapModule.this.totem.getValue()
                                          && clientPlayerEntity.getMainHandStack().isOf(Items.TOTEM_OF_UNDYING)
                                          && DoubleTapModule.this.testBlockPos(interactEvent.getBlockHitResult().getBlockPos(), clientWorld)
                                          && !DoubleTapModule.this.testBlockPos2(interactEvent.getBlockHitResult().getBlockPos(), clientWorld)) {
                                          Slot slot2 = itemSwitcher.resolveObject(slot -> slot.getStack().isOf(Items.END_CRYSTAL) ? slot : null);
                                          if (slot2 == null) {
                                             return;
                                          }

                                          InputUtils.handleSlot(slot2);
                                       }
                                    } else if (!DoubleTapModule.this.testBlockPos(interactEvent.getBlockHitResult().getBlockPos(), clientWorld)) {
                                       if (!DoubleTapModule.this.filter.getValue() || interactEvent.getBlockHitResult().getSide().equals(Direction.UP)) {
                                          Slot slot = itemSwitcher.resolveObject(slotx -> slotx.getStack().isOf(Blocks.OBSIDIAN.asItem()) ? slotx : null);
                                          if (slot != null) {
                                             SilentHotbarModule.getSilentHotbarManager()
                                                .handleObject3(
                                                   DoubleTapModule.this,
                                                   slot,
                                                   1,
                                                   SilentHotbarModule.getSilentHotbarManager().getBuilder().resolveBuilder3(true).getSelectionConfig()
                                                );
                                          }
                                       }
                                    }
                                 }
                              )
                           );
                     }
                  }
               }
            }
         }
      );
   }

   public boolean testBlockPos(BlockPos blockPos, ClientWorld clientWorld) {
      return BlockUtils.testBlockPos(blockPos, clientWorld, false);
   }

   public boolean testBlockPos2(BlockPos blockPos, ClientWorld clientWorld) {
      BlockState blockState = clientWorld.getBlockState(blockPos);
      return blockState.getBlock().equals(Blocks.RESPAWN_ANCHOR);
   }
}
