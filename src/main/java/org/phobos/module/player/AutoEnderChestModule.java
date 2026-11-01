package org.phobos.module.player;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.api.TimeGate;
import org.phobos.api.NumberRange;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.MutableNumberSetting;
import org.phobos.core.setting.MutableLongSetting;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.tick.TimerManager;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.module.PlacementModule;
import org.phobos.core.placement.DirectionUtils;
import org.phobos.core.entity.EntityInterpolation;
import org.phobos.core.module.Categories;
import org.phobos.core.math.MathUtils;
import org.phobos.core.module.ModuleLookup;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.event.HandleInputEventsEvent;
import org.phobos.event.SafeListener;
import org.phobos.module.combat.AutoMineModule;

public class AutoEnderChestModule extends PlacementModule {
   public final MutableObject<BlockPos> blockPosMutableObject;
   public final Setting<NumberRange.Mutable<Long>> wait;
   public final Setting<NumberRange.Mutable<Integer>> tick;
   public final TimeGate.Normal normal3;
   public final Setting<AutoEnderChestModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", AutoEnderChestModule.Mode.Mine));
   public final Setting<AutoEnderChestModule.Blocking> block = new EnumSetting<>("Block", AutoEnderChestModule.Blocking.None)
      .resolveSetting4(this.mode, AutoEnderChestModule.Mode.Mine)
      .resolveSetting6(this);

   public BlockPos resolveBlockPos(Set<BlockPos> set, ClientWorld clientWorld) {
      if (this.resolveBlockPos2(set, clientWorld) != null) {
         return null;
      }

      Set<BlockPos> set2 = new HashSet<>();

      for (BlockPos blockPos2 : set) {
         BlockPos blockPos = blockPos2.up();
         if (clientWorld.getBlockState(blockPos).isAir()) {
            set2.add(blockPos);
         }
      }

      MutableInt mutableInt = new MutableInt(0);
      MutableObject<BlockPos> mutableObject = new MutableObject(null);

      for (BlockPos blockPos3 : set2) {
         int i = this.computeBlockPos(blockPos3, clientWorld);
         if (mutableObject.getValue() == null) {
            mutableObject.setValue(blockPos3);
         } else if (mutableInt.getValue() < i) {
            mutableObject.setValue(blockPos3);
            mutableInt.setValue(i);
         }
      }

      return (BlockPos)mutableObject.getValue();
   }

   public int computeBlockPos(BlockPos blockPos, ClientWorld clientWorld) {
      int i = 0;

      for (Direction direction : Direction.values()) {
         if (BlockUtils.testBlockPos7(blockPos.offset(direction), clientWorld)) {
            i++;
         }
      }

      return i;
   }

   public AutoEnderChestModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoEnderChest", "Farms obsidian from enderchests", Categories.player, 1);
      this.wait = new MutableLongSetting("Wait", 500L, 50L, 5000L).resolveSetting4(this.mode, AutoEnderChestModule.Mode.Combat).resolveSetting6(this);
      this.tick = new MutableNumberSetting<>("Tick", 1, 1, 3).resolveSetting4(this.mode, AutoEnderChestModule.Mode.Combat).resolveSetting6(this);
      this.blockPosMutableObject = new MutableObject(null);
      this.normal3 = new TimeGate.Normal();
      this.getSettingGroupList2().forEach(settingGroup -> settingGroup.resolveSettingGroup4(this.mode, AutoEnderChestModule.Mode.Mine));
      this.handleListener(
         new SafeListener<HandleInputEventsEvent>() {
            public void handleObject(
               HandleInputEventsEvent handleInputEventsEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (AutoEnderChestModule.this.mode.testObject2(AutoEnderChestModule.Mode.Combat)) {
                  if (!phobosClient.getRotationDelay().check() && mc.currentScreen == null) {
                     phobosClient.getInventoryManager()
                        .handleConsumer2(
                           itemSwitcher -> {
                              itemSwitcher.handleRunnable3(() -> {});
                              Slot slot = itemSwitcher.resolveObject(slotx -> {
                                 if (slotx.getStack().getItem() instanceof BlockItem blockItem) {
                                    return blockItem.getBlock() instanceof EnderChestBlock ? slotx : null;
                                 } else {
                                    return null;
                                 }
                              });
                              if (slot != null) {
                                 if (mc.crosshairTarget instanceof BlockHitResult blockHitResult) {
                                    BlockPos blockPos = blockHitResult.getBlockPos();
                                    BlockState blockState = clientWorld.getBlockState(blockPos);
                                    if (!blockState.isAir() && !blockState.isOf(Blocks.ENDER_CHEST)) {
                                       if (AutoEnderChestModule.this.normal3.testSetting2(AutoEnderChestModule.this.wait)) {
                                          for (PlayerEntity playerEntity : clientWorld.getPlayers()) {
                                             if (EntityUtils.testPhobosClient(phobosClient, clientPlayerEntity, playerEntity, 10.0)) {
                                                double d = 5.0;
                                                Vec3d vec3d = EntityInterpolation.createVec3d2(playerEntity, TimerManager.getFloatValue());
                                                Vec3d vec3d2 = MathUtils.createVec3d4(playerEntity.getYaw(), playerEntity.getPitch());
                                                Vec3d vec3d3 = vec3d.add(vec3d2.getX() * d, vec3d2.getY() * d, vec3d2.getZ() * d);
                                                BlockHitResult blockHitResult2 = clientWorld.raycast(
                                                   new RaycastContext(vec3d, vec3d3, ShapeType.OUTLINE, FluidHandling.NONE, playerEntity)
                                                );
                                                if (blockHitResult2 != null && blockPos.equals(blockHitResult2.getBlockPos())) {
                                                   SilentHotbarModule.getSilentHotbarManager()
                                                      .handleObject3(
                                                         AutoEnderChestModule.this,
                                                         slot,
                                                         AutoEnderChestModule.this.tick.getValue().getNumber4(),
                                                         SilentHotbarModule.getSilentHotbarManager().getBuilder().resolveBuilder3(true).getSelectionConfig()
                                                      );
                                                   handleInputEventsEvent.setCancelled(true);
                                                   mc.doItemUse();
                                                   AutoEnderChestModule.this.normal3.run();
                                                   return;
                                                }
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        );
                  }
               }
            }
         }
      );
   }

   @Override
   public String nothing() {
      return this.mode.testObject2(AutoEnderChestModule.Mode.Mine) ? super.nothing() : "Combat";
   }

   public void handleItemSwitcher3(ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      if (this.blockPosMutableObject.getValue() != null && this.block.getValue().equals(AutoEnderChestModule.Blocking.Required)) {
         Block block = itemSwitcher.resolveBlock(Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN);
         if (block != null) {
            for (Entity entity : clientWorld.getEntities()) {
               if (entity instanceof ItemEntity itemEntity) {
                  ItemStack itemStack = itemEntity.getStack();
                  if (itemStack.getItem() instanceof BlockItem blockItem
                     && blockItem.getBlock().equals(Blocks.OBSIDIAN)
                     && (itemStack.getCount() == 8 || itemEntity.age <= 15)) {
                     this.handleClientPlayerEntity(clientPlayerEntity, clientWorld, itemEntity, blockPos -> this.resolvePlacement8(block, blockPos));
                  }
               }
            }
         }
      }
   }

   @Override
   public void run3() {
      this.blockPosMutableObject.setValue(null);
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ItemEntity itemEntity, Consumer<BlockPos> consumer) {
      ItemEntity itemEntity2 = new ItemEntity(clientWorld, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), itemEntity.getStack());
      itemEntity2.setVelocity(itemEntity.getVelocity());
      Box box = new Box((BlockPos)this.blockPosMutableObject.getValue());

      for (int i = 0; i < 16; i++) {
         try {
            itemEntity2.tick();
            if (itemEntity2.isOnGround()) {
               break;
            }

            Vec3d vec3d = itemEntity2.getPos();
            if (!box.contains(vec3d)) {
               BlockPos blockPos = BlockPos.ofFloored(vec3d);
               if (!((BlockPos)this.blockPosMutableObject.getValue()).equals(blockPos)) {
                  consumer.accept(blockPos);
               }
            }
         } catch (Throwable throwable) {
            throwable.printStackTrace();
         }
      }
   }

   public BlockPos resolveBlockPos2(Set<BlockPos> set, ClientWorld clientWorld) {
      for (BlockPos blockPos2 : set) {
         BlockPos blockPos = blockPos2.up();
         BlockState blockState = clientWorld.getBlockState(blockPos);
         if (blockState.isOf(Blocks.ENDER_CHEST)) {
            return blockPos;
         }
      }

      return null;
   }

   @Override
   public PlacementModule.Placement resolvePlacement8(Block block, BlockPos blockPos) {
      PlacementModule.Placement placement = super.resolvePlacement8(block, blockPos);
      if (placement.isSuccessful() && block.equals(Blocks.ENDER_CHEST)) {
         switch ((AutoEnderChestModule.Blocking)this.block.getValue()) {
            case Required:
            default:
               break;
            case Full:
               this.phobosClient.getInventoryManager().handleConsumer2(itemSwitcher -> {
                  Block blockx = itemSwitcher.resolveBlock(Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN);
                  if (blockx != null) {
                     for (Direction direction : DirectionUtils.directionArray2) {
                        this.resolvePlacement8(blockx, blockPos.offset(direction));
                     }
                  }
               });
               break;
            case None:
               this.blockPosMutableObject.setValue(blockPos);
         }
      }

      return placement;
   }

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.mode.testObject2(AutoEnderChestModule.Mode.Mine)) {
         this.handleItemSwitcher3(itemSwitcher, clientPlayerEntity, clientWorld);
         Block block = itemSwitcher.resolveBlock(Blocks.ENDER_CHEST);
         if (block == null) {
            this.blockPosMutableObject.setValue(null);
         } else {
            AutoMineModule autoMineModule = AutoMineModule.getAutoMineModule();
            SpeedmineModule speedmineModule = ModuleLookup.resolveModule(SpeedmineModule.class);
            if (speedmineModule != null
               && (autoMineModule == null || !autoMineModule.check7() || !autoMineModule.isFlag())
               && this.phobosClient.getSurroundManager().testPlayerEntity2(clientPlayerEntity)) {
               Set<BlockPos> set = this.getPhobosClient().getSurroundManager().resolveSet(clientPlayerEntity);
               if (set == null) {
                  this.blockPosMutableObject.setValue(null);
               } else {
                  set.removeIf(blockPosx -> blockPosx.getY() < clientPlayerEntity.getY() - 0.5);
                  BlockPos blockPos2 = speedmineModule.getPacketBlockBreaker().getBlockPos();
                  if (blockPos2 == null || !set.contains(blockPos2.down())) {
                     BlockPos blockPos3 = this.resolveBlockPos2(set, clientWorld);
                     if (blockPos3 != null) {
                        speedmineModule.handleBlockPos(blockPos3, clientWorld, clientPlayerEntity);
                     } else {
                        BlockPos blockPos = this.resolveBlockPos(set, clientWorld);
                        if (blockPos != null) {
                           this.resolvePlacement8(block, blockPos);
                        }
                     }
                  } else if (clientWorld.getBlockState(blockPos2).isReplaceable()) {
                     this.resolvePlacement8(block, blockPos2);
                  }
               }
            } else {
               this.blockPosMutableObject.setValue(null);
            }
         }
      }
   }

   private enum Blocking {
      None,
      Full,
      Required;
   }

   private enum Mode {
      Mine,
      Combat;
   }
}
