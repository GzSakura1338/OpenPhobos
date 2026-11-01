package org.phobos.module.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.Visibility;
import org.phobos.api.Visibility;
import org.phobos.api.TimeGate;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.NumberRange;
import org.phobos.api.NumberRange;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.MutableLongSetting;
import org.phobos.core.setting.BindSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.block.BlockPosGrid;
import org.phobos.core.task.LockedExecutor;
import org.phobos.core.render.RenderContext;
import org.phobos.core.tick.TickCounter;
import org.phobos.core.bind.BindKey;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.tick.TimerManager;
import org.phobos.core.bind.KeyCombination;
import org.phobos.core.block.MutableBlockPos;
import org.phobos.core.PhobosClient;
import org.phobos.module.ExplosivePlacementModule;
import org.phobos.core.module.Categories;
import org.phobos.core.math.MathUtils;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.tick.TickUtils;
import org.phobos.event.HandleInputEventsEvent;
import org.phobos.event.InteractEvent;
import org.phobos.event.Listener;
import org.phobos.event.OnMouseButtonEvent;
import org.phobos.event.SafeListener;
import org.phobos.module.player.SpeedmineModule.BreakEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.module.player.SilentHotbarModule;

public class AutoAnchorModule extends ExplosivePlacementModule {
   public final Setting<Boolean> explode;
   public final Setting<Boolean> prediction;
   public final Setting<Long> timeout;
   public final Setting<Boolean> own;
   public final Setting<AutoAnchorModule.Exploding> explodeItem;
   public final Setting<NumberRange.Mutable<Long>> dynamicRange;
   public final Setting<Boolean> dynamic;
   public final Setting<Boolean> auto;
   public final Setting<Integer> ticks;
   public final Setting<Integer> poll;
   public final Setting<Boolean> scan;
   public final Setting<Double> fail;
   public final Setting<Double> minDamage;
   public final Setting<Double> maxSelfDamage;
   public final Setting<AutoAnchorModule.Blocking> item;
   public final Setting<AutoAnchorModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", AutoAnchorModule.Mode.Rage));
   public final Setting<AutoAnchorModule.AnchorBlock> block;
   public final Setting<AutoAnchorModule.Lanience> lanience;
   public final Setting<Boolean> suicide;
   public final Setting<Boolean> cleanup;
   public boolean flag;
   public boolean flag2;
   public boolean flag3;
   public final MutableObject<AutoAnchorModule.AnchorPosition> anchorPositionMutableObject;
   public final Setting<KeyCombination> place;
   public final LockedExecutor lockedExecutor;
   public float floatValue;
   public final TimeGate.Normal normal3;
   public boolean flag4;
   public final Map<BlockPos, Long> blockPosMap;
   public final Setting<Double> doubleChance;
   public BlockPos blockPos;
   public final Setting<Boolean> onlyOwn;
   public boolean flag5;
   public final Setting<Boolean> extra;
   public final Setting<KeyCombination> doubleValue;
   public final TimeGate.Normal normal4;
   public final MutableObject<Float> floatValueMutableObject;
   public final BlockPosGrid<AutoAnchorModule.AnchorPosition> anchorPositionBlockPosGrid;
   public int intValue;

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      switch ((AutoAnchorModule.Mode)this.mode.getValue()) {
         case Rage:
            if (!clientWorld.getDimension().bedWorks()) {
               return;
            }

            Block block = itemSwitcher.resolveBlock(Blocks.RESPAWN_ANCHOR);
            Block block2 = itemSwitcher.resolveBlock(Blocks.GLOWSTONE);
            if (block == null || block2 == null) {
               return;
            }

            this.floatValueMutableObject.setValue(Float.MIN_VALUE);
            this.anchorPositionMutableObject.setValue(null);
            List<AutoAnchorModule.AnchorPosition> list = new ArrayList<>();

            for (AutoAnchorModule.AnchorPosition anchorPosition : this.anchorPositionBlockPosGrid.getObjectArray()) {
               if ((anchorPosition.isFlag3() || anchorPosition.isFlag())
                  && !(anchorPosition.getFloatValue2() < this.minDamage.getValue())
                  && (
                     this.suicide.getValue()
                        || !(anchorPosition.getFloatValue() > this.maxSelfDamage.getValue())
                           && !(anchorPosition.getFloatValue() >= EntityUtils.computeLivingEntity(clientPlayerEntity))
                  )) {
                  if (anchorPosition.getFloatValue2() > (Float)this.floatValueMutableObject.getValue()) {
                     this.floatValueMutableObject.setValue(anchorPosition.getFloatValue2());
                     this.anchorPositionMutableObject.setValue(anchorPosition);
                  } else {
                     list.add(anchorPosition);
                  }
               }
            }

            AutoAnchorModule.AnchorPosition anchorPosition2 = (AutoAnchorModule.AnchorPosition)this.anchorPositionMutableObject.getValue();
            if (anchorPosition2 != null) {
               this.floatValue = anchorPosition2.getFloatValue2();
               if (anchorPosition2.isFlag32()) {
                  this.handleClientPlayerEntity2(clientPlayerEntity, clientWorld, clientPlayerInteractionManager, anchorPosition2);
               } else if (anchorPosition2.isFlag()) {
                  this.handleClientPlayerEntity(clientPlayerEntity, clientWorld, clientPlayerInteractionManager, anchorPosition2);
                  this.handleClientPlayerEntity2(clientPlayerEntity, clientWorld, clientPlayerInteractionManager, anchorPosition2);
               } else {
                  this.resolvePlacement8(block, anchorPosition2);
               }
            }

            if (this.cleanup.getValue()) {
               for (AutoAnchorModule.AnchorPosition anchorPosition3 : list) {
                  if (anchorPosition3.isFlag32()) {
                     this.handleClientPlayerEntity2(clientPlayerEntity, clientWorld, clientPlayerInteractionManager, anchorPosition3);
                  }
               }
            }
            break;
         case Legit:
            if (this.lanience.test(AutoAnchorModule.Lanience.Update) && this.normal4.testLong(this.value())) {
               if (clientPlayerEntity.isUsingItem() || this.phobosClient.getRotationDelay().check()) {
                  this.flag5 = false;
                  return;
               }

               itemSwitcher.handleRunnable3(
                  () -> {
                     Block blockx = itemSwitcher.resolveBlock(Blocks.RESPAWN_ANCHOR);
                     Block block2x = itemSwitcher.resolveBlock(Blocks.GLOWSTONE);
                     Block block3 = itemSwitcher.resolveBlock(this.item.getValue().getBlocks());
                     if (blockx != null && block2x != null && block3 != null) {
                        Slot slot2 = itemSwitcher.resolveObject(slotx -> slotx.getStack().isOf(blockx.asItem()) ? slotx : null);
                        Slot slot3 = itemSwitcher.resolveObject(slotx -> slotx.getStack().isOf(block2x.asItem()) ? slotx : null);
                        Slot slot4 = itemSwitcher.resolveObject(slotx -> slotx.getStack().isOf(block3.asItem()) ? slotx : null);
                        if (slot2 == null || slot3 == null || slot4 == null) {
                           this.run();
                        } else if (this.scan.getValue() && EntityUtils.resolvePlayerEntity2(clientWorld, clientPlayerEntity, this.phobosClient) == null) {
                           this.run();
                        } else if (mc.crosshairTarget instanceof BlockHitResult blockHitResult) {
                           BlockPos blockPos5 = BlockUtils.resolveBlockPos3(clientWorld, blockHitResult.getBlockPos());
                           BlockPos blockPos6 = blockPos5.offset(blockHitResult.getSide());
                           BlockState blockState2 = clientWorld.getBlockState(blockPos5);
                           if (!this.onlyOwn.getValue() || this.blockPosMap.containsKey(blockPos5)) {
                              if (blockState2.isOf(Blocks.RESPAWN_ANCHOR)) {
                                 int i = (Integer)clientWorld.getBlockState(blockPos5).get(RespawnAnchorBlock.CHARGES);
                                 int i2 = this.extra.getValue() && i > 0 ? (MathUtils.testSupplier2(() -> 5.0) ? 1 : 0) : 0;
                                 if (i <= i2) {
                                    if (this.normal3.testLong(this.value())) {
                                       SilentHotbarModule.getSilentHotbarManager()
                                          .handleObject3(
                                             this, slot3, 1, SilentHotbarModule.getSilentHotbarManager().getBuilder().resolveBuilder3(true).getSelectionConfig()
                                          );
                                       this.phobosClient.getInteractionManager().run();
                                       this.normal4.run();
                                       this.normal3.run();
                                    }
                                 } else {
                                    if (this.block.testObject2(AutoAnchorModule.AnchorBlock.Fast)) {
                                       BlockPos blockPos4 = this.resolveBlockPos(clientPlayerEntity, clientWorld, blockPos5);
                                       if (blockPos4 != null) {
                                          this.resolvePlacement8(block3, blockPos4);
                                          this.normal3.run();
                                       }
                                    }

                                    if (this.explode.getValue() || this.flag2) {
                                       this.flag2 = false;
                                       if (clientPlayerEntity.getMainHandStack().isOf(Blocks.GLOWSTONE.asItem())) {
                                          Slot slot = itemSwitcher.resolveObject(slotx -> slotx.getStack().isOf(Items.TOTEM_OF_UNDYING) ? slotx : null);
                                          SilentHotbarModule.getSilentHotbarManager()
                                             .handleObject3(
                                                this,
                                                !this.flag3 && slot != null ? slot : slot2,
                                                2,
                                                SilentHotbarModule.getSilentHotbarManager().getBuilder().resolveBuilder3(true).getSelectionConfig()
                                             );
                                       }

                                       this.phobosClient.getInteractionManager().run();
                                       if (this.flag3) {
                                          this.phobosClient.getInteractionManager().run();
                                          this.flag2 = true;
                                       }

                                       this.flag3 = false;
                                       this.blockPos = null;
                                       this.normal4.run();
                                       this.normal3.run();
                                    }
                                 }
                              } else if (this.flag5) {
                                 SilentHotbarModule.getSilentHotbarManager()
                                    .handleObject3(
                                       this, slot2, 1, SilentHotbarModule.getSilentHotbarManager().getBuilder().resolveBuilder3(true).getSelectionConfig()
                                    );
                                 this.phobosClient.getInteractionManager().run();
                                 this.flag5 = false;
                                 this.flag2 = true;
                              } else if (!blockState2.isOf(Blocks.GLOWSTONE)) {
                                 if (this.block.testObject2(AutoAnchorModule.AnchorBlock.Macro)) {
                                    float f2 = TimerManager.getFloatValue();
                                    float f3 = clientPlayerEntity.getYaw(f2);
                                    float f4 = clientPlayerEntity.getPitch(f2);
                                    Vec3d vec3d3 = clientPlayerEntity.getCameraPosVec(f2);
                                    double d = clientPlayerEntity.getBlockInteractionRange();
                                    float f = f4;

                                    while (true) {
                                       if (!(f > f4 - 50.0F)) {
                                          return;
                                       }

                                       Vec3d vec3d = MathUtils.createVec3d4(f3, f);
                                       Vec3d vec3d2 = vec3d3.add(vec3d.x * d, vec3d.y * d, vec3d.z * d);
                                       BlockHitResult blockHitResult2 = clientWorld.raycast(
                                          new RaycastContext(vec3d3, vec3d2, ShapeType.COLLIDER, FluidHandling.NONE, clientPlayerEntity)
                                       );
                                       if (blockHitResult2 != null) {
                                          BlockPos blockPos = blockHitResult2.getBlockPos();
                                          BlockPos blockPos2 = blockPos.offset(blockHitResult2.getSide());
                                          BlockState blockState = clientWorld.getBlockState(blockPos);
                                          if (blockPos6.getY() == blockPos2.getY()
                                             && clientPlayerEntity.getBlockY() >= blockPos2.getY()
                                             && blockState.isOf(Blocks.RESPAWN_ANCHOR)) {
                                             BlockPos blockPos3 = this.resolveBlockPos2(clientPlayerEntity, clientWorld, blockPos);
                                             if (blockPos3 == null) {
                                                this.blockPos = blockPos;
                                                SilentHotbarModule.getSilentHotbarManager()
                                                   .handleObject3(
                                                      this,
                                                      slot4,
                                                      1,
                                                      SilentHotbarModule.getSilentHotbarManager().getBuilder().resolveBuilder3(true).getSelectionConfig()
                                                   );
                                                this.phobosClient.getInteractionManager().run();
                                                this.normal4.run();
                                                break;
                                             }
                                          }
                                       }

                                       f -= 5.0F;
                                    }
                                 }
                              }
                           }
                        }
                     } else {
                        this.run();
                     }
                  }
               );
            }
      }
   }

   public void run2() {
      for (AutoAnchorModule.AnchorPosition anchorPosition : this.anchorPositionBlockPosGrid.getObjectArray()) {
         anchorPosition.run();
      }
   }

   public long value() {
      return !this.dynamic.getValue() ? 100L : this.dynamicRange.getValue().getNumber4();
   }

   public AutoAnchorModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoAnchor", "Places and explodes anchors", Categories.combat, 5);
      this.minDamage = this.resolveSetting2(new NumberSetting<>("MinDamage", 6.0, 0.1, 36.0));
      this.maxSelfDamage = this.resolveSetting2(new NumberSetting<>("MaxSelfDamage", 12.0, 0.1, 36.0));
      this.suicide = this.resolveSetting2(new BooleanSetting("Suicide", false).resolveSetting7("Ignore self damage"));
      this.prediction = this.resolveSetting2(new BooleanSetting("Prediction").resolveSetting7("Prediction (Simulation)"));
      this.ticks = this.resolveSetting2(new NumberSetting<>("Ticks", 1, 1, 4));
      this.cleanup = this.resolveSetting2(
         new BooleanSetting("Cleanup", false)
            .resolveSetting4(this.mode, AutoAnchorModule.Mode.Rage)
            .resolveSetting7("Removes placed anchors regardless if they deal damage or not")
      );
      this.own = this.resolveSetting2(new BooleanSetting("Own", true).resolveSetting7("Only explodes anchors placed by you"));
      this.timeout = this.resolveSetting2(new LongNumberSetting("Timeout", 450L, 0L, 5000L, true).resolveSetting7("Place timeout"));
      this.fail = this.resolveSetting2(
         new NumberSetting<>("Fail", 25.5, 0.0, 100.0, "%").resolveSetting4(this.mode, AutoAnchorModule.Mode.Legit).resolveSetting7("Fail chance")
      );
      this.explode = this.resolveSetting2(new BooleanSetting("Explode", false).resolveSetting7("Explode anchors"));
      this.explodeItem = this.resolveSetting2(
         new EnumSetting<>("ExplodeItem", AutoAnchorModule.Exploding.Anchor).resolveSetting7("Item to use when exploding")
      );
      this.scan = this.resolveSetting2(
         new BooleanSetting("Scan", true)
            .resolveSetting3(
               Visibility.resolveVisibility4(
                  Visibility.resolveVisibility3(this.mode, AutoAnchorModule.Mode.Legit),
                  Visibility.resolveVisibility3(this.mode, AutoAnchorModule.Mode.Blatant),
                  Visibility.OpAnd.OR
               )
            )
            .resolveSetting7("Only explodes if an enemy is nearby")
      );
      this.block = this.resolveSetting2(
         new EnumSetting<>("Block", AutoAnchorModule.AnchorBlock.None).resolveSetting7("Places glowstone between you and the anchor")
      );
      this.item = this.resolveSetting2(new EnumSetting<>("Item", AutoAnchorModule.Blocking.Glowstone));
      this.auto = this.resolveSetting2(
         new BooleanSetting("Auto")
            .resolveSetting4(this.mode, AutoAnchorModule.Mode.Blatant)
            .resolveSetting7("Switches to an anchor when interacting a block with a totem")
      );
      this.lanience = this.resolveSetting2(
         new EnumSetting<>("Lanience", AutoAnchorModule.Lanience.Update).resolveSetting4(this.mode, AutoAnchorModule.Mode.Blatant)
      );
      this.poll = this.resolveSetting2(
         new NumberSetting<>("Poll", 0, 0, 5).resolveSetting4(this.mode, AutoAnchorModule.Mode.Blatant).resolveSetting7("Blocks keyboard inputs")
      );
      this.dynamic = this.resolveSetting2(new BooleanSetting("Dynamic").resolveSetting7("Makes the delay slightly less obvious"));
      this.dynamicRange = this.resolveSetting2(new MutableLongSetting("DynamicRange", NumberRange.createMutable(25L, 100L), 0L, 100L));
      this.extra = this.resolveSetting2(
         new BooleanSetting("Extra").resolveSetting4(this.mode, AutoAnchorModule.Mode.Blatant).resolveSetting7("Sometimes charges twice to seem more legit")
      );
      this.place = this.resolveSetting2(
         new BindSetting("Place").resolveSetting4(this.mode, AutoAnchorModule.Mode.Blatant).resolveSetting7("Place + Break key")
      );
      this.doubleValue = this.resolveSetting2(
         new BindSetting("Double").resolveSetting4(this.mode, AutoAnchorModule.Mode.Blatant).resolveSetting7("Place + Break + Place + Break key")
      );
      this.doubleChance = this.resolveSetting2(
         new NumberSetting<>("DoubleChance", 70.0, 10.0, 100.0)
            .resolveSetting4(this.mode, AutoAnchorModule.Mode.Blatant)
            .resolveSetting3(() -> !this.doubleValue.getValue().isEmpty())
      );
      this.onlyOwn = this.resolveSetting2(
         new BooleanSetting("OnlyOwn").resolveSetting4(this.mode, AutoAnchorModule.Mode.Blatant).resolveSetting7("Only accept anchors placed by you")
      );
      this.anchorPositionBlockPosGrid = new BlockPosGrid<>(7, AutoAnchorModule.AnchorPosition::new, new AutoAnchorModule.AnchorPosition[0]);
      this.lockedExecutor = new LockedExecutor();
      this.floatValueMutableObject = new MutableObject(0.0F);
      this.anchorPositionMutableObject = new MutableObject(null);
      this.floatValue = 0.0F;
      this.flag = false;
      this.normal3 = new TimeGate.Normal();
      this.normal4 = new TimeGate.Normal();
      this.blockPosMap = new ConcurrentHashMap<>();
      this.getSettingGroupList2()
         .forEach(
            settingGroup -> settingGroup.resolveSettingGroup5(
               Visibility.resolveVisibility4(
                  Visibility.resolveVisibility3(this.mode, AutoAnchorModule.Mode.Rage),
                  () -> this.mode.testObject2(AutoAnchorModule.Mode.Blatant) && this.block.testObject2(AutoAnchorModule.AnchorBlock.Fast),
                  Visibility.OpAnd.OR
               )
            )
         );
      this.update.resolveSetting4(this.mode, AutoAnchorModule.Mode.Rage);
      Visibility visibility = Visibility.resolveVisibility4(
         Visibility.resolveVisibility3(this.mode, AutoAnchorModule.Mode.Legit),
         Visibility.resolveVisibility3(this.mode, AutoAnchorModule.Mode.Blatant),
         Visibility.OpAnd.OR
      );
      this.resolveSettingGroup2("Calculation", this.minDamage, this.maxSelfDamage, this.suicide).resolveSettingGroup4(this.mode, AutoAnchorModule.Mode.Rage);
      this.resolveSettingGroup(this.prediction, this.ticks).resolveSettingGroup4(this.mode, AutoAnchorModule.Mode.Rage);
      this.resolveSettingGroup(this.own, this.timeout).resolveSettingGroup4(this.mode, AutoAnchorModule.Mode.Legit);
      this.resolveSettingGroup2("Blocking", this.block, this.item)
         .resolveSettingGroup5(Visibility.resolveVisibility3(this.mode, AutoAnchorModule.Mode.Blatant));
      this.resolveSettingGroup(this.dynamic, this.dynamicRange).resolveSettingGroup4(this.mode, AutoAnchorModule.Mode.Blatant);
      this.resolveSettingGroup(this.explode, this.explodeItem).resolveSettingGroup5(visibility);
      this.handleListener(
         new SafeListener<OnMouseButtonEvent>() {
            public void handleObject(
               OnMouseButtonEvent onMouseButtonEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (onMouseButtonEvent.getIntValue() == 1 && mc.currentScreen == null) {
                  BindKey bindKey = phobosClient.getBindManager()
                     .resolveBindKey(onMouseButtonEvent.getKeyboardMouseEnum(), onMouseButtonEvent.getIntValue2());
                  if (bindKey == null) {
                     return;
                  }

                  if (AutoAnchorModule.this.place.getValue().getBindKeySet().contains(bindKey)) {
                     AutoAnchorModule.this.flag5 = true;
                  }

                  if (AutoAnchorModule.this.doubleValue.getValue().getBindKeySet().contains(bindKey)) {
                     AutoAnchorModule.this.flag3 = true;
                     AutoAnchorModule.this.flag5 = true;
                  }
               }
            }
         }
      );
      this.handleListener(
         new Listener<BreakEvent>(80) {
            public void handleObject2(BreakEvent breakEvent) {
               if (MinecraftHolder.mc.world != null
                  && AutoAnchorModule.this.mode.testObject2(AutoAnchorModule.Mode.Rage)
                  && MinecraftHolder.mc.world.getBlockState(breakEvent.getBlockPos()).isOf(Blocks.RESPAWN_ANCHOR)) {
                  breakEvent.setCancelled(true);
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
               if (AutoAnchorModule.this.mode.testObject2(AutoAnchorModule.Mode.Rage)) {
                  PlayerEntity playerEntity = EntityUtils.resolvePlayerEntity2(clientWorld, clientPlayerEntity, phobosClient);
                  if (playerEntity == null) {
                     AutoAnchorModule.this.run2();
                  } else {
                     AutoAnchorModule.this.lockedExecutor.handleRunnable(() -> {
                        for (AutoAnchorModule.AnchorPosition anchorPosition : AutoAnchorModule.this.anchorPositionBlockPosGrid.getObjectArray()) {
                           try {
                              ClientWorld clientWorldx = phobosClient.getWorldHolder().getClientWorld();
                              if (clientWorldx == null) {
                                 return;
                              }

                              anchorPosition.handleClientPlayerEntity(clientPlayerEntity, clientWorldx);
                              if (anchorPosition.isFlag3()) {
                                 AutoAnchorModule.this.handleAnchorPosition(anchorPosition, clientPlayerEntity, clientWorldx);
                              }
                           } catch (Throwable throwable) {
                              throwable.printStackTrace();
                           }
                        }
                     });
                  }
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
               BlockHitResult blockHitResult = interactEvent.getBlockHitResult();
               BlockPos blockPos = BlockUtils.resolveBlockPos3(clientWorld, blockHitResult.getBlockPos());
               Direction direction = blockHitResult.getSide();
               BlockPos blockPos2 = blockPos.offset(direction);
               boolean flag = clientPlayerEntity.getMainHandStack().isOf(Blocks.RESPAWN_ANCHOR.asItem())
                  || clientPlayerEntity.getOffHandStack().isOf(Blocks.RESPAWN_ANCHOR.asItem());
               if (flag) {
                  AutoAnchorModule.this.blockPosMap.put(blockPos2, System.currentTimeMillis());
               }

               switch ((AutoAnchorModule.Mode)AutoAnchorModule.this.mode.getValue()) {
                  case Legit:
                     if (flag && AutoAnchorModule.this.check3()) {
                        AutoAnchorModule.this.normal4.run();
                        AutoAnchorModule.this.normal3.run();
                     }

                     if (AutoAnchorModule.this.auto.getValue()) {
                        ItemStack itemStack = clientPlayerEntity.getMainHandStack();
                        if (itemStack.isOf(Items.TOTEM_OF_UNDYING)) {
                           BlockState blockState = clientWorld.getBlockState(blockPos);
                           if (!blockState.isOf(Blocks.RESPAWN_ANCHOR)
                              && !blockState.isOf(Blocks.GLOWSTONE)
                              && (
                                 !AutoAnchorModule.this.scan.getValue() || EntityUtils.resolvePlayerEntity2(clientWorld, clientPlayerEntity, phobosClient) != null
                              )) {
                              phobosClient.getInventoryManager()
                                 .handleConsumer2(
                                    itemSwitcher -> itemSwitcher.handleRunnable3(
                                       () -> {
                                          Slot slot = itemSwitcher.resolveObject(slotx -> slotx.getStack().isOf(Items.RESPAWN_ANCHOR) ? slotx : null);
                                          if (slot != null) {
                                             SilentHotbarModule.getSilentHotbarManager()
                                                .handleObject3(
                                                   AutoAnchorModule.this,
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
         new SafeListener<RenderContext>() {
            public void handleObject(
               RenderContext renderContext,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (AutoAnchorModule.this.mode.testObject2(AutoAnchorModule.Mode.Legit)) {
                  if (mc.currentScreen == null) {
                     if (!clientPlayerEntity.isUsingItem()) {
                        if (!AutoAnchorModule.this.scan.getValue() || EntityUtils.resolvePlayerEntity2(clientWorld, clientPlayerEntity, phobosClient) != null) {
                           if (!AutoAnchorModule.this.flag) {
                              AutoAnchorModule.this.blockPosMap
                                 .entrySet()
                                 .removeIf(entry -> entry.getValue() + AutoAnchorModule.this.timeout.getValue() < System.currentTimeMillis());
                              if (mc.crosshairTarget instanceof BlockHitResult blockHitResult) {
                                 BlockPos blockPos = blockHitResult.getBlockPos();
                                 if (!AutoAnchorModule.this.own.getValue() || AutoAnchorModule.this.blockPosMap.containsKey(blockPos)) {
                                    if (clientWorld.getBlockState(blockPos).getBlock().equals(Blocks.RESPAWN_ANCHOR)) {
                                       boolean flag = (Integer)clientWorld.getBlockState(blockPos).get(RespawnAnchorBlock.CHARGES) > 0;
                                       phobosClient.getInventoryManager()
                                          .handleConsumer2(
                                             itemSwitcher -> {
                                                if (!flag) {
                                                   if (!itemSwitcher.testItem(Blocks.GLOWSTONE.asItem())) {
                                                      return;
                                                   }

                                                   Slot slot = itemSwitcher.resolveObject(
                                                      slotx -> slotx.getStack()
                                                            .isOf(AutoAnchorModule.this.check2() ? Blocks.RESPAWN_ANCHOR.asItem() : Blocks.GLOWSTONE.asItem())
                                                         ? slotx
                                                         : null
                                                   );
                                                   if (slot != null) {
                                                      AutoAnchorModule.this.flag = true;
                                                      SilentHotbarModule.getSilentHotbarManager()
                                                         .handleObject3(
                                                            AutoAnchorModule.this,
                                                            slot,
                                                            2,
                                                            SilentHotbarModule.getSilentHotbarManager()
                                                               .getBuilder()
                                                               .resolveBuilder3(true)
                                                               .resolveBuilder4(
                                                                  () -> {
                                                                     if (AutoAnchorModule.this.explode.getValue()) {
                                                                        phobosClient.getScheduler()
                                                                           .handleRunnable2(
                                                                              () -> phobosClient.getEventBus().handleRunnable(mc::doItemUse), 1
                                                                           );
                                                                     }

                                                                     AutoAnchorModule.this.flag = false;
                                                                  }
                                                               )
                                                               .resolveBuilder2(() -> AutoAnchorModule.this.flag = false)
                                                               .getSelectionConfig()
                                                         );
                                                      phobosClient.getScheduler()
                                                         .handleRunnable2(() -> phobosClient.getEventBus().handleRunnable(mc::doItemUse), 1);
                                                   }
                                                }
                                             }
                                          );
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<HandleInputEventsEvent>() {
            public void handleObject(
               HandleInputEventsEvent handleInputEventsEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!AutoAnchorModule.this.poll.testObject2(0) && AutoAnchorModule.this.intValue-- > 0) {
                  handleInputEventsEvent.setCancelled(true);
               }

               switch ((AutoAnchorModule.Mode)AutoAnchorModule.this.mode.getValue()) {
                  case Legit:
                     if (AutoAnchorModule.this.lanience.test(AutoAnchorModule.Lanience.Poll)
                        && AutoAnchorModule.this.normal4.testLong(AutoAnchorModule.this.value())) {
                        phobosClient.getInventoryManager()
                           .handleConsumer2(
                              itemSwitcher -> {
                                 if (!clientPlayerEntity.isUsingItem() && !phobosClient.getRotationDelay().check()) {
                                    itemSwitcher.handleRunnable3(
                                       () -> {
                                          Block block = itemSwitcher.resolveBlock(Blocks.RESPAWN_ANCHOR);
                                          Block block2 = itemSwitcher.resolveBlock(Blocks.GLOWSTONE);
                                          Block block3 = itemSwitcher.resolveBlock(AutoAnchorModule.this.item.getValue().getBlocks());
                                          if (block != null && block2 != null && block3 != null) {
                                             Slot slot2 = itemSwitcher.resolveObject(slotx -> slotx.getStack().isOf(block.asItem()) ? slotx : null);
                                             Slot slot3 = itemSwitcher.resolveObject(slotx -> slotx.getStack().isOf(block2.asItem()) ? slotx : null);
                                             Slot slot4 = itemSwitcher.resolveObject(slotx -> slotx.getStack().isOf(block3.asItem()) ? slotx : null);
                                             if (slot2 == null || slot3 == null || slot4 == null) {
                                                AutoAnchorModule.this.run();
                                             } else if (AutoAnchorModule.this.scan.getValue()
                                                && EntityUtils.resolvePlayerEntity2(clientWorld, clientPlayerEntity, phobosClient) == null) {
                                                AutoAnchorModule.this.run();
                                             } else if (mc.crosshairTarget instanceof BlockHitResult blockHitResult) {
                                                BlockPos blockPos5 = BlockUtils.resolveBlockPos3(clientWorld, blockHitResult.getBlockPos());
                                                BlockPos blockPos6 = blockPos5.offset(blockHitResult.getSide());
                                                BlockState blockState2 = clientWorld.getBlockState(blockPos5);
                                                if (!AutoAnchorModule.this.onlyOwn.getValue() || AutoAnchorModule.this.blockPosMap.containsKey(blockPos5)) {
                                                   if (blockState2.isOf(Blocks.RESPAWN_ANCHOR)) {
                                                      int i = (Integer)clientWorld.getBlockState(blockPos5).get(RespawnAnchorBlock.CHARGES);
                                                      int i2 = AutoAnchorModule.this.extra.getValue() && i > 0
                                                         ? (MathUtils.testSupplier2(() -> 5.0) ? 1 : 0)
                                                         : 0;
                                                      if (i <= i2) {
                                                         if (AutoAnchorModule.this.normal3.testLong(AutoAnchorModule.this.value())) {
                                                            SilentHotbarModule.getSilentHotbarManager()
                                                               .handleObject3(
                                                                  this,
                                                                  slot3,
                                                                  1,
                                                                  SilentHotbarModule.getSilentHotbarManager()
                                                                     .getBuilder()
                                                                     .resolveBuilder3(true)
                                                                     .getSelectionConfig()
                                                               );
                                                            handleInputEventsEvent.setCancelled(true);
                                                            phobosClient.getInteractionManager().run();
                                                            AutoAnchorModule.this.normal3.run();
                                                            AutoAnchorModule.this.normal4.run();
                                                         }
                                                      } else {
                                                         if (AutoAnchorModule.this.block.testObject2(AutoAnchorModule.AnchorBlock.Fast)) {
                                                            BlockPos blockPos4 = AutoAnchorModule.this.resolveBlockPos(
                                                               clientPlayerEntity, clientWorld, blockPos5
                                                            );
                                                            if (blockPos4 != null) {
                                                               AutoAnchorModule.this.resolvePlacement8(block3, blockPos4);
                                                               AutoAnchorModule.this.normal4.run();
                                                               AutoAnchorModule.this.normal3.run();
                                                            }
                                                         }

                                                         if (AutoAnchorModule.this.explode.getValue() || AutoAnchorModule.this.flag2) {
                                                            AutoAnchorModule.this.flag2 = false;
                                                            boolean flag = AutoAnchorModule.this.flag3
                                                               && MathUtils.testSupplier2(AutoAnchorModule.this.doubleChance);
                                                            if (clientPlayerEntity.getMainHandStack().isOf(Blocks.GLOWSTONE.asItem()) || flag) {
                                                               Slot slot = itemSwitcher.resolveObject(slotx -> {
                                                                  if (!itemSwitcher.testSlot4(slotx)) {
                                                                     return null;
                                                                  } else {
                                                                     return slotx.getStack().isOf(Items.TOTEM_OF_UNDYING) ? slotx : null;
                                                                  }
                                                               });
                                                               SilentHotbarModule.getSilentHotbarManager()
                                                                  .handleObject3(
                                                                     this,
                                                                     !flag && slot != null ? slot : slot2,
                                                                     flag ? 4 : 1,
                                                                     SilentHotbarModule.getSilentHotbarManager()
                                                                        .getBuilder()
                                                                        .resolveBuilder3(true)
                                                                        .getSelectionConfig()
                                                                  );
                                                            }

                                                            phobosClient.getInteractionManager().run();
                                                            if (flag) {
                                                               phobosClient.getInteractionManager().run();
                                                               AutoAnchorModule.this.flag2 = true;
                                                            }

                                                            handleInputEventsEvent.setCancelled(true);
                                                            AutoAnchorModule.this.flag3 = false;
                                                            AutoAnchorModule.this.blockPos = null;
                                                            AutoAnchorModule.this.normal4.run();
                                                            AutoAnchorModule.this.normal3.run();
                                                         }
                                                      }
                                                   } else if (AutoAnchorModule.this.flag5) {
                                                      SilentHotbarModule.getSilentHotbarManager()
                                                         .handleObject3(
                                                            this,
                                                            slot2,
                                                            1,
                                                            SilentHotbarModule.getSilentHotbarManager().getBuilder().resolveBuilder3(true).getSelectionConfig()
                                                         );
                                                      handleInputEventsEvent.setCancelled(true);
                                                      phobosClient.getInteractionManager().run();
                                                      AutoAnchorModule.this.flag5 = false;
                                                      AutoAnchorModule.this.flag2 = true;
                                                   } else if (!blockState2.isOf(Blocks.GLOWSTONE)) {
                                                      if (AutoAnchorModule.this.block.testObject2(AutoAnchorModule.AnchorBlock.Macro)) {
                                                         float f2 = TimerManager.getFloatValue();
                                                         float f3 = clientPlayerEntity.getYaw(f2);
                                                         float f4 = clientPlayerEntity.getPitch(f2);
                                                         Vec3d vec3d3 = clientPlayerEntity.getCameraPosVec(f2);
                                                         double d = clientPlayerEntity.getBlockInteractionRange();
                                                         float f = f4;

                                                         while (true) {
                                                            if (!(f > f4 - 50.0F)) {
                                                               return;
                                                            }

                                                            Vec3d vec3d = MathUtils.createVec3d4(f3, f);
                                                            Vec3d vec3d2 = vec3d3.add(vec3d.x * d, vec3d.y * d, vec3d.z * d);
                                                            BlockHitResult blockHitResult2 = clientWorld.raycast(
                                                               new RaycastContext(vec3d3, vec3d2, ShapeType.COLLIDER, FluidHandling.NONE, clientPlayerEntity)
                                                            );
                                                            if (blockHitResult2 != null) {
                                                               BlockPos blockPos = blockHitResult2.getBlockPos();
                                                               BlockPos blockPos2 = blockPos.offset(blockHitResult2.getSide());
                                                               BlockState blockState = clientWorld.getBlockState(blockPos);
                                                               if (blockPos6.getY() == blockPos2.getY()
                                                                  && clientPlayerEntity.getBlockY() >= blockPos2.getY()
                                                                  && blockState.isOf(Blocks.RESPAWN_ANCHOR)) {
                                                                  BlockPos blockPos3 = AutoAnchorModule.this.resolveBlockPos2(
                                                                     clientPlayerEntity, clientWorld, blockPos
                                                                  );
                                                                  if (blockPos3 == null) {
                                                                     AutoAnchorModule.this.blockPos = blockPos;
                                                                     SilentHotbarModule.getSilentHotbarManager()
                                                                        .handleObject3(
                                                                           this,
                                                                           slot4,
                                                                           1,
                                                                           SilentHotbarModule.getSilentHotbarManager()
                                                                              .getBuilder()
                                                                              .resolveBuilder3(true)
                                                                              .getSelectionConfig()
                                                                        );
                                                                     handleInputEventsEvent.setCancelled(true);
                                                                     phobosClient.getInteractionManager().run();
                                                                     AutoAnchorModule.this.normal4.run();
                                                                     break;
                                                                  }
                                                               }
                                                            }

                                                            f -= 5.0F;
                                                         }
                                                      }
                                                   }
                                                }
                                             }
                                          } else {
                                             AutoAnchorModule.this.run();
                                          }
                                       }
                                    );
                                 } else {
                                    AutoAnchorModule.this.run();
                                 }
                              }
                           );
                     }
               }
            }
         }
      );
      ModuleListenerUtils.handleListenerHolder4(this, () -> this.flag = false);
   }

   public void handleItemSwitcher3(
      ItemSwitcher itemSwitcher,
      BlockHitResult blockHitResult,
      ClientPlayerEntity clientPlayerEntity,
      ClientPlayerInteractionManager clientPlayerInteractionManager,
      ClientWorld clientWorld,
      Item... items
   ) {
      Item item = itemSwitcher.resolveItem2(items);
      if (item != null) {
         ItemSwitcher.SwitchResult switchResult = itemSwitcher.resolveSwitchResult3(Hand.MAIN_HAND, item, 3);
         if (switchResult != null) {
            this.handleSwitchResult2(switchResult, clientPlayerEntity, clientPlayerInteractionManager, clientWorld, blockHitResult);
         }
      }
   }

   public BlockPos resolveBlockPos(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, BlockPos blockPos) {
      Vec3d vec3d = blockPos.toCenterPos().add(0.0, -0.5, 0.0);
      Vec3d vec3d2 = clientPlayerEntity.getPos();
      BlockPos blockPos2 = BlockUtils.resolveBlockPos2(vec3d, vec3d2);
      BlockState blockState = clientWorld.getBlockState(blockPos2);
      return blockState.getFluidState().isEmpty() && blockState.getBlock().getBlastResistance() > 0.0F ? null : blockPos2;
   }

   public void handleSwitchResult(
      ItemSwitcher.SwitchResult switchResult,
      ClientPlayerEntity clientPlayerEntity,
      ClientPlayerInteractionManager clientPlayerInteractionManager,
      ClientWorld clientWorld,
      BlockPos blockPos
   ) {
      boolean flag = clientPlayerEntity.isSneaking();
      if (flag) {
         clientPlayerEntity.networkHandler
            .sendPacket(new ClientCommandC2SPacket(clientPlayerEntity, net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
      }

      this.phobosClient.getInteractionManager().handleBlockPos2(blockPos, clientPlayerEntity, clientWorld, this.getDirectionResolver(), blockHitResult -> {
         Hand hand = switchResult.hand();
         clientPlayerInteractionManager.interactBlock(clientPlayerEntity, hand, blockHitResult);
         if (this.getBooleanValueSetting4().getValue()) {
            clientPlayerEntity.swingHand(hand);
         }
      });
      if (flag) {
         clientPlayerEntity.networkHandler
            .sendPacket(new ClientCommandC2SPacket(clientPlayerEntity, net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));
      }
   }

   public void run() {
      this.flag3 = false;
      this.flag5 = false;
      this.flag2 = false;
   }

   public void handleItemSwitcher4(
      ItemSwitcher itemSwitcher,
      BlockPos blockPos,
      ClientPlayerEntity clientPlayerEntity,
      ClientPlayerInteractionManager clientPlayerInteractionManager,
      ClientWorld clientWorld
   ) {
      ItemSwitcher.SwitchResult switchResult = itemSwitcher.resolveSwitchResult3(Hand.MAIN_HAND, Items.GLOWSTONE, 3);
      if (switchResult != null) {
         this.handleSwitchResult(switchResult, clientPlayerEntity, clientPlayerInteractionManager, clientWorld, blockPos);
      }
   }

   @Nullable
   public BlockPos resolveBlockPos2(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, BlockPos blockPos) {
      Vec3d vec3d = clientPlayerEntity.getBlockPos().toCenterPos();
      Vec3d vec3d2 = blockPos.toCenterPos();
      BlockHitResult blockHitResult = clientWorld.raycast(new RaycastContext(vec3d, vec3d2, ShapeType.COLLIDER, FluidHandling.NONE, clientPlayerEntity));
      BlockPos blockPos2 = blockHitResult.getBlockPos();
      return blockPos.equals(blockPos2) ? null : blockPos2;
   }

   public void handleAnchorPosition(AutoAnchorModule.AnchorPosition anchorPosition, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      List<BlockPos> list = new ArrayList<>(List.of(anchorPosition));
      anchorPosition.setFloatValue(
         this.phobosClient.getDamageCalculator().computeEntity3(clientPlayerEntity, clientWorld, 5.0F, anchorPosition.toCenterPos(), list)
      );

      for (PlayerEntity playerEntity3 : clientWorld.getPlayers()) {
         if (playerEntity3 != null
            && !playerEntity3.isDead()
            && !playerEntity3.equals(clientPlayerEntity)
            && playerEntity3.isAlive()
            && !(playerEntity3.squaredDistanceTo(clientPlayerEntity) > MathHelper.square(324.0))
            && !this.phobosClient.getFriendList().testUUID(playerEntity3.getUuid())
            && !(playerEntity3.squaredDistanceTo(anchorPosition.toCenterPos()) > 144.0)
            && !playerEntity3.isCreative()
            && !playerEntity3.isSpectator()) {
            PlayerEntity playerEntity;
            PlayerEntity playerEntity2 = this.prediction.getValue()
                  && (playerEntity = this.phobosClient.getFeatureRegistry().getPredictionManager().resolvePlayerEntity3(this.ticks.getValue(), playerEntity3))
                     != null
               ? playerEntity
               : playerEntity3;
            float f = this.phobosClient.getDamageCalculator().computeEntity3(playerEntity2, clientWorld, 5.0F, anchorPosition.toCenterPos(), list);
            if (f > anchorPosition.getFloatValue2()) {
               anchorPosition.setFloatValue2(f);
               anchorPosition.setPlayerEntity(playerEntity2);
            }
         }
      }
   }

   public boolean check2() {
      Random random = new Random();
      return random.nextInt(1000) < this.fail.getValue() * 10.0;
   }

   public void handleItemSwitcher5(
      ItemSwitcher itemSwitcher,
      BlockHitResult blockHitResult,
      ClientPlayerEntity clientPlayerEntity,
      ClientPlayerInteractionManager clientPlayerInteractionManager,
      ClientWorld clientWorld
   ) {
      ItemSwitcher.SwitchResult switchResult = itemSwitcher.resolveSwitchResult3(Hand.MAIN_HAND, Items.GLOWSTONE, 3);
      if (switchResult != null) {
         this.handleSwitchResult2(switchResult, clientPlayerEntity, clientPlayerInteractionManager, clientWorld, blockHitResult);
      }
   }

   public void handleClientPlayerEntity(
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager,
      AutoAnchorModule.AnchorPosition anchorPosition
   ) {
      this.phobosClient
         .getInventoryManager()
         .handleConsumer2(
            itemSwitcher -> this.handleItemSwitcher4(itemSwitcher, anchorPosition, clientPlayerEntity, clientPlayerInteractionManager, clientWorld)
         );
   }

   public void handleClientPlayerEntity2(
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager,
      AutoAnchorModule.AnchorPosition anchorPosition
   ) {
      this.phobosClient
         .getInventoryManager()
         .handleConsumer2(
            itemSwitcher -> this.handleItemSwitcher6(itemSwitcher, anchorPosition, clientPlayerEntity, clientPlayerInteractionManager, clientWorld)
         );
   }

   public boolean check3() {
      return MathUtils.testSupplier2(() -> 20.0);
   }

   public void handleItemSwitcher6(
      ItemSwitcher itemSwitcher,
      BlockPos blockPos,
      ClientPlayerEntity clientPlayerEntity,
      ClientPlayerInteractionManager clientPlayerInteractionManager,
      ClientWorld clientWorld
   ) {
      Slot slot = itemSwitcher.resolveObject(slotx -> !slotx.getStack().isOf(Blocks.GLOWSTONE.asItem()) ? slotx : null);
      if (slot != null) {
         ItemSwitcher.SwitchResult switchResult = itemSwitcher.resolveSwitchResult(Hand.MAIN_HAND, slot, 3);
         if (switchResult != null) {
            this.handleSwitchResult(switchResult, clientPlayerEntity, clientPlayerInteractionManager, clientWorld, blockPos);
         }
      }
   }

   public void handleItemSwitcher7(
      ItemSwitcher itemSwitcher,
      BlockHitResult blockHitResult,
      ClientPlayerEntity clientPlayerEntity,
      ClientPlayerInteractionManager clientPlayerInteractionManager,
      ClientWorld clientWorld,
      Block block
   ) {
      ItemSwitcher.SwitchResult switchResult = itemSwitcher.resolveSwitchResult3(Hand.MAIN_HAND, block.asItem(), 3);
      if (switchResult != null) {
         this.handleSwitchResult2(switchResult, clientPlayerEntity, clientPlayerInteractionManager, clientWorld, blockHitResult);
      }
   }

   public void handleSwitchResult2(
      ItemSwitcher.SwitchResult switchResult,
      ClientPlayerEntity clientPlayerEntity,
      ClientPlayerInteractionManager clientPlayerInteractionManager,
      ClientWorld clientWorld,
      BlockHitResult blockHitResult
   ) {
      TickUtils.handleClientWorld(clientWorld, integer -> {
         Hand hand = switchResult.hand();
         clientPlayerInteractionManager.interactBlock(clientPlayerEntity, hand, blockHitResult);
         switch ((AutoAnchorModule.Mode)this.mode.getValue()) {
            case Rage:
               if (this.getBooleanValueSetting4().getValue()) {
                  clientPlayerEntity.swingHand(hand);
               }
               break;
            case Legit:
            case Blatant:
               clientPlayerEntity.swingHand(switchResult.hand());
         }
      });
   }

   @Override
   public String nothing() {
      return this.mode.testObject2(AutoAnchorModule.Mode.Legit) ? String.valueOf(MathUtils.computeFloat5(this.floatValue, 2)) : "Legit";
   }

   @Override
   public void run4() {
      this.floatValue = 0.0F;
      this.run();
      this.flag = false;
      this.run2();
   }

   private enum AnchorBlock {
      None,
      Fast,
      Macro;
   }

   private static class AnchorPosition extends MutableBlockPos {
      public float floatValue;
      public boolean flag;
      public boolean flag2;
      public final Vec3i vec3i;
      public PlayerEntity playerEntity;
      public boolean flag3;
      public float floatValue2 = 0.0F;
      public static final Logger LOGGER = LoggerFactory.getLogger(AutoAnchorModule.AnchorPosition.class);
      public boolean flag4;
      public boolean flag5;

      public Vec3i getVec3i() {
         return this.vec3i;
      }

      public void setFloatValue2(float f) {
         this.floatValue2 = f;
      }

      public boolean isFlag4() {
         return this.flag;
      }

      public float getFloatValue() {
         return this.floatValue;
      }

      public AnchorPosition(Vec3i vec3i) {
         this.floatValue = Float.MAX_VALUE;
         this.vec3i = vec3i;
      }

      public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
         this.run();
         this.set(
            Math.floor(clientPlayerEntity.getX()) + this.vec3i.getX(),
            Math.floor(clientPlayerEntity.getEyeY()) + this.vec3i.getY(),
            Math.floor(clientPlayerEntity.getZ()) + this.vec3i.getZ()
         );
         BlockState blockState = clientWorld.getBlockState(this);
         if (blockState.getBlock().getBlastResistance() < 35.0F) {
            this.flag4 = true;
         }

         if (blockState.isOf(Blocks.RESPAWN_ANCHOR)) {
            this.flag2 = true;
         } else if (!blockState.isReplaceable()) {
            return;
         }

         this.handleClientWorld(clientWorld);
         this.flag3 = this.flag2 && (Integer)blockState.get(RespawnAnchorBlock.CHARGES) > 0;
         this.flag5 = this.flag;
      }

      public PlayerEntity getPlayerEntity() {
         return this.playerEntity;
      }

      public void setFlag(boolean flag) {
         this.flag2 = flag;
      }

      public boolean isFlag() {
         return this.flag2;
      }

      public void setFlag4(boolean flag) {
         this.flag = flag;
      }

      public float getFloatValue2() {
         return this.floatValue2;
      }

      public void setFlag3(boolean flag) {
         this.flag5 = flag;
      }

      public void handleClientWorld(ClientWorld clientWorld) {
         this.flag = !BlockUtils.testBlockPos6(this, VoxelShapes.fullCube(), clientWorld, false);
      }

      public boolean isFlag32() {
         return this.flag3;
      }

      public void setFlag32(boolean flag) {
         this.flag3 = flag;
      }

      public void setPlayerEntity(PlayerEntity playerEntity) {
         this.playerEntity = playerEntity;
      }

      public void setFlag2(boolean flag) {
         this.flag4 = flag;
      }

      public boolean isFlag3() {
         return this.flag5;
      }

      public boolean isFlag2() {
         return this.flag4;
      }

      public void setFloatValue(float f) {
         this.floatValue = f;
      }

      public void run() {
         this.floatValue2 = 0.0F;
         this.floatValue = Float.MAX_VALUE;
         this.flag5 = false;
         this.flag2 = false;
         this.flag4 = false;
         this.flag = false;
         this.playerEntity = null;
      }
   }

   private enum Blocking {
      Glowstone(Blocks.GLOWSTONE),
      Obsidian(Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN);

      public final Block[] blocks;

      public Block[] getBlocks() {
         return this.blocks;
      }

      Blocking(Block... blocks) {
         this.blocks = blocks;
      }
   }

   private enum Exploding {
      Anchor(Items.RESPAWN_ANCHOR),
      Totem(Items.TOTEM_OF_UNDYING);

      public final Item[] items;

      public Item[] getItems() {
         return this.items;
      }

      Exploding(Item... items) {
         this.items = items;
      }
   }

   private enum Lanience {
      Update,
      Poll;
   }

   private enum Mode {
      Rage,
      Blatant,
      Legit;
   }
}
