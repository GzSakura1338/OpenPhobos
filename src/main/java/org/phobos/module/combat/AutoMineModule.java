package org.phobos.module.combat;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.AreaScanner.Area;
import org.phobos.api.BlockStateAccess.Delegating;
import org.phobos.core.placement.ExactDirectionResolver;
import org.phobos.core.placement.ShapeDirectionResolver;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.combat.DamageCalculator;
import org.phobos.core.setting.DebugSettings;
import org.phobos.core.server.ServerDetector;
import org.phobos.core.block.BlockBreakManager;
import org.phobos.core.combat.SurroundManager;
import org.phobos.core.combat.CrystalPlacement;
import org.phobos.core.PhobosClient;
import org.phobos.core.render.Highlights;
import org.phobos.core.placement.DirectionUtils;
import org.phobos.core.module.Categories;
import org.phobos.core.math.BoxUtils;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.core.player.InteractionManager.Silent;
import org.phobos.core.player.InteractionManager.SwitchMode;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;
import org.phobos.module.player.SpeedmineModule;

public class AutoMineModule extends Module {
   public final Setting<Boolean> inside;
   public final Setting<Boolean> onGround;
   public final Setting<Silent> crystalsSilent;
   public final Setting<Boolean> strict;
   public final Setting<Boolean> crystalsBase;
   public final Setting<Boolean> crystalsInstant;
   public final Setting<Boolean> crystalsGeneric;
   public final Map<BlockPos, AutoMineModule.ExpectedCrystal> blockPosMap;
   public final Setting<Boolean> bombing;
   public final Setting<Boolean> await;
   public static AutoMineModule autoMineModule;
   public final Setting<AutoMineModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", AutoMineModule.Mode.Combat));
   public final Setting<Boolean> crystals;
   public final Setting<SwitchMode> crystalsSwitch;
   public final Setting<Boolean> uncrawl;
   public boolean flag;
   public final Setting<Boolean> unbox;
   public final Setting<Double> range = this.resolveSetting2(new NumberSetting<>("Range", 6.0, 0.0, 6.0));
   public final DebugSettings debugSettings;
   public final Setting<Boolean> down;
   public PlayerEntity playerEntity;
   public final Setting<AutoMineModule.Selection> selection;
   private static Object[] phobos$g1806e5750;
   private static Object[] phobos$g1806e4a38;
   private static Object[] phobos$g1806e4b30;
   private static Object[] phobos$g1806e4c48;
   private static Object[] phobos$g1806e4d20;
   private static Object[] phobos$g1806e4e28;
   private static Object[] phobos$g1806e4f80;
   private static Object[] phobos$g1806e5058;
   private static Object[] phobos$g1806e5150;
   private static Object[] phobos$g1806e5268;
   private static Object[] phobos$g1806e5340;
   private static Object[] phobos$g1806e5478;

   @Nullable
   public BlockPos resolveBlockPos(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, BlockPos blockPos) {
      Pair<BlockPos, BlockPos> pair = this.createPair(clientWorld, blockPos);
      if (this.testClientPlayerEntity(clientPlayerEntity, clientWorld, pair.getLeft())) {
         return pair.getLeft();
      } else {
         return this.testClientPlayerEntity(clientPlayerEntity, clientWorld, pair.getRight()) ? pair.getRight() : null;
      }
   }

   public Setting<AutoMineModule.Mode> getModeSetting() {
      return this.mode;
   }

   public boolean testWorld(World world, Set<BlockPos> set, Predicate<BlockState> predicate, boolean flag) {
      for (BlockPos blockPos : set) {
         if (flag) {
            if (!predicate.test(world.getBlockState(blockPos))) {
               return false;
            }
         } else if (predicate.test(world.getBlockState(blockPos))) {
            return true;
         }
      }

      return flag;
   }

   public void handlePosition(
      AutoMineModule.Position position, MutableObject<AutoMineModule.Position> mutableObject, MutableObject<AutoMineModule.Position> mutableObject2
   ) {
      if (mutableObject.getValue() == null) {
         mutableObject.setValue(position);
      } else {
         double d = position.getDoubleValue();
         if (d > mutableObject.getValue().getDoubleValue()) {
            mutableObject2.setValue(mutableObject.getValue());
            mutableObject.setValue(position);
         } else if (mutableObject2.getValue() == null) {
            mutableObject2.setValue(position);
         } else {
            if (d > mutableObject2.getValue().getDoubleValue()) {
               mutableObject2.setValue(position);
            }
         }
      }
   }

   @Nullable
   public PlayerEntity resolvePlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      MutableObject<PlayerEntity> mutableObject = new MutableObject<>();
      MutableDouble mutableDouble = new MutableDouble(Double.MAX_VALUE);

      for (PlayerEntity playerEntity : clientWorld.getPlayers()) {
         if (EntityUtils.testPhobosClient(this.phobosClient, clientPlayerEntity, playerEntity, 12.0)
            && !EntityUtils.testPlayerEntity(playerEntity)
            && (!this.onGround.getValue() || playerEntity.isOnGround())) {
            double d = RotationUtils.computeClientPlayerEntity3(clientPlayerEntity, playerEntity, 0.0);
            if (mutableObject.getValue() == null) {
               mutableObject.setValue(playerEntity);
               mutableDouble.setValue(d);
            } else if (d < mutableDouble.getValue()) {
               mutableObject.setValue(playerEntity);
               mutableDouble.setValue(d);
            }
         }
      }

      return mutableObject.getValue();
   }

   public AutoMineModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoMine", Categories.combat);
      this.unbox = this.resolveSetting2(
         new BooleanSetting("Unbox").resolveSetting7("Breaks outline before the bed itself").resolveSetting4(this.mode, AutoMineModule.Mode.Beds)
      );
      this.strict = this.resolveSetting2(new BooleanSetting("Strict").resolveSetting4(this.mode, AutoMineModule.Mode.Beds));
      this.selection = this.resolveSetting2(
         new EnumSetting<>("Selection", AutoMineModule.Selection.Dynamic).resolveSetting4(this.mode, AutoMineModule.Mode.Combat)
      );
      this.uncrawl = this.resolveSetting2(new BooleanSetting("Uncrawl").resolveSetting4(this.mode, AutoMineModule.Mode.Combat));
      this.crystals = this.resolveSetting2(new BooleanSetting("Crystals", true));
      this.crystalsSwitch = this.resolveSetting2(new EnumSetting<>("CrystalsSwitch", SwitchMode.Silent));
      this.crystalsSilent = this.resolveSetting2(new EnumSetting<>("CrystalsSilent", Silent.Swap).resolveSetting4(this.crystalsSwitch, SwitchMode.Silent));
      this.crystalsBase = this.resolveSetting2(new BooleanSetting("CrystalsBase"));
      this.crystalsInstant = this.resolveSetting2(
         new BooleanSetting("CrystalsInstant").resolveSetting7("Attacks the crystal when it spawns and pre-places another one")
      );
      this.crystalsGeneric = this.resolveSetting2(new BooleanSetting("CrystalsGeneric").resolveSetting7("Crystals anywhere where it can blow up the drop"));
      this.inside = this.resolveSetting2(new BooleanSetting("Inside", true).resolveSetting7("Mines blocks the enemy is inside"));
      this.bombing = this.resolveSetting2(new BooleanSetting("Bombing", true).resolveSetting7("Mines blocks to bomb crystals"));
      this.down = this.resolveSetting2(new BooleanSetting("Down", true).resolveSetting7("Mines below target"));
      this.onGround = this.resolveSetting2(
         new BooleanSetting("OnGround").resolveSetting7("Excludes air-bone targets").resolveSetting4(this.mode, AutoMineModule.Mode.Combat)
      );
      this.await = this.resolveSetting2(new BooleanSetting("Await").resolveSetting7("Does not mine until second position is broken"));
      this.debugSettings = DebugSettings.resolveDebugSettings(this);
      this.blockPosMap = new HashMap<>();
      this.resolveSettingGroup(this.crystals, this.crystalsSwitch, this.crystalsSilent, this.crystalsBase, this.crystalsInstant, this.crystalsGeneric)
         .resolveSettingGroup4(this.mode, AutoMineModule.Mode.Combat);
      this.handleListener(
         new SafeListener<SpeedmineModule.BreakEvent>(Integer.MIN_VALUE) {
            public void handleObject(
               SpeedmineModule.BreakEvent breakEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (AutoMineModule.this.mode.testObject2(AutoMineModule.Mode.Combat)
                  && AutoMineModule.this.crystals.getValue()
                  && !breakEvent.isCancelled()
                  && !breakEvent.isFlag2()
                  && !clientWorld.getBlockState(breakEvent.getBlockPos()).isAir()) {
                  AutoCrystalModule autoCrystalModule = AutoCrystalModule.getAutoCrystalModule();
                  Delegating delegating = Delegating.createDelegating(clientWorld);
                  BlockPos blockPos = breakEvent.getBlockPos();
                  BiConsumer<BlockPos, Boolean> biConsumer = (blockPos2, booleanValue) -> {
                     CrystalPlacement crystalPlacement = new CrystalPlacement(blockPos2);
                     crystalPlacement.handleServerDetector2(phobosClient.getServerDetector(), delegating, clientPlayerEntity, autoCrystalModule);
                     crystalPlacement.setFloatValue(phobosClient.getDamageCalculator().computeEntity(clientPlayerEntity, delegating, crystalPlacement));
                     if (crystalPlacement.isFlag42() && AutoMineModule.this.crystalsInstant.getValue()) {
                        EndCrystalEntity endCrystalEntity = crystalPlacement.getEndCrystalEntity();
                        if (endCrystalEntity != null) {
                           autoCrystalModule.handleClientPlayerEntity3(clientPlayerEntity, endCrystalEntity, true);
                        }
                     }

                     if (crystalPlacement.isFlag3()
                        && (AutoMineModule.this.crystalsBase.getValue() || !crystalPlacement.isFlag32())
                        && autoCrystalModule.testClientPlayerEntity(clientPlayerEntity, crystalPlacement)) {
                        if (!crystalPlacement.testConsumer(
                           endCrystalEntity -> autoCrystalModule.handleClientPlayerEntity3(clientPlayerEntity, endCrystalEntity, true)
                        )) {
                           autoCrystalModule.handleClientPlayerEntity(
                              clientPlayerEntity,
                              clientWorld,
                              crystalPlacement,
                              AutoMineModule.this.crystalsSwitch.getValue(),
                              AutoMineModule.this.crystalsSilent.getValue()
                           );
                        }

                        if (AutoMineModule.this.crystalsInstant.getValue()) {
                           delegating.getBlockPosMap().put(blockPos, Blocks.AIR.getDefaultState());
                           BlockPos blockPos3 = blockPos.down();
                           boolean flag = booleanValue && BlockUtils.testBlockPos(blockPos3, delegating, false);
                           AutoMineModule.this.blockPosMap
                              .put(
                                 crystalPlacement,
                                 flag ? AutoMineModule.ExpectedCrystal.createExpectedCrystal(blockPos3) : AutoMineModule.ExpectedCrystal.getExpectedCrystal()
                              );
                           delegating.getBlockPosMap().clear();
                        }
                     }
                  };

                  for (PlayerEntity playerEntity : clientWorld.getPlayers()) {
                     if (EntityUtils.testPhobosClient(phobosClient, clientPlayerEntity, playerEntity, 14.0)
                        && AutoMineModule.this.testBlockPos(blockPos, playerEntity, clientWorld, delegating, biConsumer)) {
                        return;
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Receive<EntitySpawnS2CPacket>>() {
            public void handleObject(
               Receive<EntitySpawnS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               EntitySpawnS2CPacket entitySpawnS2CPacket = receive.getPacket();
               if (AutoMineModule.this.mode.testObject2(AutoMineModule.Mode.Combat)
                  && AutoMineModule.this.crystals.getValue()
                  && AutoMineModule.this.crystalsInstant.getValue()
                  && entitySpawnS2CPacket.getEntityType() == EntityType.END_CRYSTAL) {
                  EndCrystalEntity endCrystalEntity = new EndCrystalEntity(
                     clientWorld, entitySpawnS2CPacket.getX(), entitySpawnS2CPacket.getY(), entitySpawnS2CPacket.getZ()
                  );
                  endCrystalEntity.setId(entitySpawnS2CPacket.getEntityId());
                  BlockPos blockPos = endCrystalEntity.getBlockPos().down();
                  AutoMineModule.ExpectedCrystal expectedCrystal = AutoMineModule.this.blockPosMap.remove(blockPos);
                  if (expectedCrystal != null) {
                     if (!expectedCrystal.check()) {
                        AutoCrystalModule.getAutoCrystalModule().handleClientPlayerEntity3(clientPlayerEntity, endCrystalEntity, true);
                        expectedCrystal.setFlag(true);
                        if (!expectedCrystal.isEmpty()) {
                           AutoCrystalModule.getAutoCrystalModule()
                              .handleClientPlayerEntity2(
                                 clientPlayerEntity,
                                 clientWorld,
                                 expectedCrystal.getBlockPos(),
                                 AutoMineModule.this.crystalsSwitch.getValue(),
                                 AutoMineModule.this.crystalsSilent.getValue()
                              );
                        }
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<PlayerTickEvent>(-20) {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               switch ((AutoMineModule.Mode)AutoMineModule.this.mode.getValue()) {
                  case Combat:
                     AutoMineModule.this.blockPosMap.entrySet().removeIf(entry -> entry.getValue().check());
                     AutoMineModule.this.flag = false;
                     AutoMineModule.this.playerEntity = null;
                     SpeedmineModule speedmineModule2 = SpeedmineModule.getSpeedmineModule();
                     if (speedmineModule2.check7() && speedmineModule2.isFlag()) {
                        speedmineModule2.run();
                        return;
                     }

                     boolean flag = speedmineModule2.getDoubleValue().getValue();
                     if (AutoMineModule.this.uncrawl.getValue() && clientPlayerEntity.isInSwimmingPose()) {
                        for (BlockPos blockPos9 : BlockPosUtils.resolveSet3(clientPlayerEntity, clientPlayerEntity.getBoundingBox().minY + 1.5)) {
                           BlockState blockState = clientWorld.getBlockState(blockPos9);
                           if (!blockState.isAir() && AutoMineModule.this.testSpeedmineModule(speedmineModule2, blockPos9, clientPlayerEntity, clientWorld)) {
                              speedmineModule2.handleBlockPos(blockPos9, clientWorld, clientPlayerEntity);
                              return;
                           }
                        }
                     }

                     MutableObject<AutoMineModule.Position> mutableObject = new MutableObject<>();
                     MutableObject<AutoMineModule.Position> mutableObject2 = new MutableObject<>();
                     Set<BlockPos> set = phobosClient.getSurroundManager().resolveSet2(clientPlayerEntity, true);
                     switch ((AutoMineModule.Selection)AutoMineModule.this.selection.getValue()) {
                        case Angle:
                           for (PlayerEntity playerEntity : clientWorld.getPlayers()) {
                              if (EntityUtils.testPhobosClient(phobosClient, clientPlayerEntity, playerEntity, 12.0)
                                 && !EntityUtils.testPlayerEntity(playerEntity)
                                 && (!AutoMineModule.this.onGround.getValue() || playerEntity.isOnGround())) {
                                 AutoMineModule.this.handleClientPlayerEntity(clientPlayerEntity, clientWorld, playerEntity, set, mutableObject, mutableObject2);
                              }
                           }
                           break;
                        case Range:
                           PlayerEntity playerEntity2 = AutoMineModule.this.resolvePlayerEntity(clientPlayerEntity, clientWorld);
                           if (playerEntity2 == null) {
                              return;
                           }

                           AutoMineModule.this.handleClientPlayerEntity(clientPlayerEntity, clientWorld, playerEntity2, set, mutableObject, mutableObject2);
                           break;
                        case Dynamic:
                           PlayerEntity playerEntity3 = EntityUtils.resolvePlayerEntity(
                              clientWorld,
                              clientPlayerEntity,
                              phobosClient,
                              12.0,
                              playerEntity -> (!AutoMineModule.this.onGround.getValue() || playerEntity.isOnGround())
                                 && !EntityUtils.testPlayerEntity(playerEntity)
                           );
                           if (playerEntity3 == null) {
                              return;
                           }

                           AutoMineModule.this.handleClientPlayerEntity(clientPlayerEntity, clientWorld, playerEntity3, set, mutableObject, mutableObject2);
                     }

                     AutoMineModule.Position position = mutableObject.getValue();
                     AutoMineModule.Position position2 = mutableObject2.getValue();
                     if (position == null) {
                        return;
                     }

                     if (AutoMineModule.this.debugSettings.check()) {
                        Highlights.handleBox(BoxUtils.createBox9(position.getBlockPos()), Color.RED, 50L);
                        if (position2 != null) {
                           Highlights.handleBox(BoxUtils.createBox9(position2.getBlockPos()), Color.BLUE, 50L);
                        }
                     }

                     AutoMineModule.this.playerEntity = position.resolveObject("target", PlayerEntity.class);
                     AutoMineModule.this.flag = true;
                     boolean flag2 = position.resolveObject("inside", Boolean.class) != null
                        || position2 != null && position2.resolveObject("inside", Boolean.class) != null;
                     if (!flag2
                        && (
                           speedmineModule2.getPacketBlockBreaker().testBlockPos(position.getBlockPos())
                              || position2 != null && speedmineModule2.getPacketBlockBreaker().testBlockPos(position2.getBlockPos())
                        )) {
                        return;
                     }

                     if (AutoMineModule.this.await.getValue() && speedmineModule2.getSimpleBlockBreaker().check()) {
                        return;
                     }

                     BlockPos blockPos10 = position.getBlockPos();
                     if (!speedmineModule2.getSimpleBlockBreaker().testBlockPos(blockPos10) && !speedmineModule2.getPacketBlockBreaker().testBlockPos(blockPos10)) {
                        speedmineModule2.handleClientPlayerEntity(clientPlayerEntity);
                        speedmineModule2.handleBlockPos(blockPos10, clientWorld, clientPlayerEntity);
                     }

                     if (!flag || position2 == null || !flag2 || speedmineModule2.getSimpleBlockBreaker().check()) {
                        return;
                     }

                     blockPos10 = position2.getBlockPos();
                     if (!speedmineModule2.getPacketBlockBreaker().testBlockPos(blockPos10) && !SpeedmineModule.testBlockPos5(blockPos10)) {
                        speedmineModule2.handleBlockPos4(blockPos10, clientWorld, clientPlayerEntity, positionType -> {});
                     }
                     break;
                  case Beds:
                     SpeedmineModule speedmineModule = SpeedmineModule.getSpeedmineModule();
                     if (speedmineModule == null) {
                        return;
                     }

                     if (speedmineModule.check7() && speedmineModule.isFlag()) {
                        return;
                     }

                     Area area = new Area();
                     area.handleVec3d(
                        clientPlayerEntity.getEyePos(),
                        AutoMineModule.this.range.getValue(),
                        blockPos -> clientWorld.getBlockState(blockPos).getBlock() instanceof BedBlock
                     );
                     BlockPos blockPos8 = speedmineModule.check7()
                        ? speedmineModule.getPacketBlockBreaker().getBlockPos()
                        : phobosClient.getBlockBreakManager().getBlockPos();
                     if (blockPos8 != null
                        && AutoMineModule.this.testSpeedmineModule(speedmineModule, blockPos8, clientPlayerEntity, clientWorld)
                        && (speedmineModule.check7() || phobosClient.getBlockBreakManager().testToggleable(AutoMineModule.this))) {
                        for (BlockPos blockPos : area) {
                           if (AutoMineModule.this.testClientWorld(clientWorld, blockPos, blockPos8)
                              && AutoMineModule.this.testClientPlayerEntity(clientPlayerEntity, clientWorld, blockPos8)) {
                              if (speedmineModule.check7()) {
                                 return;
                              }

                              phobosClient.getBlockBreakManager().handleToggleable2(AutoMineModule.this);
                              return;
                           }
                        }

                        if (AutoMineModule.this.unbox.getValue()) {
                           List<BlockPos> list = new ArrayList<>();

                           for (BlockPos blockPos2 : area) {
                              list.addAll(AutoMineModule.this.resolveList(clientPlayerEntity, clientWorld, blockPos2));
                           }

                           BlockPos blockPos3 = AutoMineModule.this.resolveBlockPos2(clientWorld, list);
                           if (blockPos3 != null && blockPos3.equals(blockPos8)) {
                              if (speedmineModule.check7()) {
                                 return;
                              }

                              phobosClient.getBlockBreakManager().handleToggleable2(AutoMineModule.this);
                              return;
                           }
                        }
                     }

                     for (BlockPos blockPos5 : area) {
                        BlockPos blockPos4 = AutoMineModule.this.resolveBlockPos(clientPlayerEntity, clientWorld, blockPos5);
                        if (blockPos4 != null && AutoMineModule.this.testSpeedmineModule(speedmineModule, blockPos4, clientPlayerEntity, clientWorld)) {
                           if (speedmineModule.check7()) {
                              speedmineModule.handleBlockPos(blockPos4, clientWorld, clientPlayerEntity);
                              return;
                           }

                           phobosClient.getBlockBreakManager().handleToggleable3(AutoMineModule.this, blockPos4);
                           return;
                        }
                     }

                     if (AutoMineModule.this.unbox.getValue()) {
                        List<BlockPos> list2 = new ArrayList<>();

                        for (BlockPos blockPos6 : area) {
                           list2.addAll(AutoMineModule.this.resolveList(clientPlayerEntity, clientWorld, blockPos6));
                        }

                        BlockPos blockPos7 = AutoMineModule.this.resolveBlockPos2(clientWorld, list2);
                        if (blockPos7 != null && AutoMineModule.this.testSpeedmineModule(speedmineModule, blockPos7, clientPlayerEntity, clientWorld)) {
                           if (speedmineModule.check7()) {
                              speedmineModule.handleBlockPos(blockPos7, clientWorld, clientPlayerEntity);
                              return;
                           }

                           phobosClient.getBlockBreakManager().handleToggleable3(AutoMineModule.this, blockPos7);
                           return;
                        }
                     }

                     phobosClient.getBlockBreakManager().handleToggleable(AutoMineModule.this);
               }
            }
         }
      );
      ModuleListenerUtils.handleListenerHolder4(this, this::run2);
      autoMineModule = this;
   }

   @Override
   public void run3() {
      this.run2();
   }

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, BlockPos blockPos) {
      if (!this.unbox.getValue()) {
         return true;
      }

      for (Direction direction : DirectionUtils.directionArray2) {
         BlockPos blockPos2 = blockPos.offset(direction);
         BlockState blockState = clientWorld.getBlockState(blockPos2);
         if (!(blockState.getBlock() instanceof BedBlock)
            && !this.testClientPlayerEntity2(clientPlayerEntity, clientWorld, blockPos, blockPos2, blockState, direction)
            && this.testSpeedmineModule(SpeedmineModule.getSpeedmineModule(), blockPos2, clientPlayerEntity, clientWorld)
            && blockState.isAir()) {
            return true;
         }
      }

      return false;
   }

   public List<BlockPos> resolveList(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, BlockPos blockPos) {
      Pair<BlockPos, BlockPos> pair = this.createPair(clientWorld, blockPos);
      List<BlockPos> list = new ArrayList<>();
      this.handleClientPlayerEntity2(clientPlayerEntity, clientWorld, pair.getLeft(), list);
      this.handleClientPlayerEntity2(clientPlayerEntity, clientWorld, pair.getRight(), list);
      return list;
   }

   public Setting<AutoMineModule.Selection> getSelection() {
      return this.selection;
   }

   public Setting<Boolean> getUnbox() {
      return this.unbox;
   }

   public Setting<Boolean> getStrict() {
      return this.strict;
   }

   public Setting<Double> getDoubleValueSetting() {
      return this.range;
   }

   public void handleClientPlayerEntity(
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      PlayerEntity playerEntity,
      Set<BlockPos> set,
      MutableObject<AutoMineModule.Position> mutableObject,
      MutableObject<AutoMineModule.Position> mutableObject2
   ) {
      Delegating var7 = Delegating.createDelegating(clientWorld);
      PhobosClient var8 = this.phobosClient;
      SurroundManager var9 = var8.getSurroundManager();
      Set var10 = var9.resolveSet2(playerEntity, true & true);
      double var11 = playerEntity.getY();
      Set var13 = BlockPosUtils.resolveSet3(playerEntity, var11 + 2.5);
      Box var14 = playerEntity.getBoundingBox();
      double var15 = var14.minY;
      Set var17 = BlockPosUtils.resolveSet3(playerEntity, var15 - 0.5);
      Box var18 = playerEntity.getBoundingBox();
      double var19 = var18.minY;
      Set var21 = BlockPosUtils.resolveSet3(playerEntity, var19 + 0.25);
      PlayerEntity cap90 = playerEntity;
      Predicate<BlockPos> var38 = var1 -> testPlayerEntity(cap90, var1);
      var10.removeIf(var38);
      var10.removeAll(var21);
      ClientWorld var10000 = clientWorld;
      ClientPlayerEntity cap92 = clientPlayerEntity;
      ClientWorld cap91 = var10000;
      Predicate<BlockPos> var55 = var2 -> testClientWorld5(cap91, cap92, var2);
      Setting var56 = this.inside;
      Object var57 = var56.getValue();
      boolean var58 = var57 instanceof Boolean;
      if (var57 != null && !var58) {
         throw new ClassCastException("CHECKCAST failed");
      }

      boolean var59 = (Boolean)var57;
      if (var59) {
         AutoMineModule var286 = this;
         ClientPlayerEntity var10001 = clientPlayerEntity;
         ClientWorld var10002 = clientWorld;
         PlayerEntity var10003 = playerEntity;
         Set var10004 = set;
         MutableObject var10006 = mutableObject;
         MutableObject cap100 = mutableObject2;
         MutableObject cap99 = var10006;
         Delegating cap98 = var7;
         Set cap97 = var10004;
         PlayerEntity cap96 = var10003;
         ClientWorld cap95 = var10002;
         ClientPlayerEntity cap94 = var10001;
         AutoMineModule cap93 = var286;
         Consumer<BlockPos> var76 = var8x -> cap93.handleClientPlayerEntity4(cap94, cap95, cap96, cap97, cap98, cap99, cap100, var8x);
         Predicate[] var77 = new Predicate[1];
         ClientWorld cap101 = clientWorld;
         Predicate<BlockPos> var94 = var1 -> testClientWorld2(cap101, var1);
         Predicate var95 = var55.and(var94);
         var77[0] = var95;
         this.handleClientPlayerEntity3(clientPlayerEntity, clientWorld, var21, var76, var77);
      }

      boolean var96 = this.testWorld2(clientWorld, var21, true & true);
      Predicate[] var141;
      Consumer var142;
      Object var143;
      if (var96) {
         Setting var97 = this.down;
         Object var98 = var97.getValue();
         boolean var99 = var98 instanceof Boolean;
         if (var98 != null && !var99) {
            throw new ClassCastException("CHECKCAST failed");
         }

         boolean var121 = (Boolean)var98;
         if (!var121) {
            return;
         }

         boolean var122 = this.testWorld2(clientWorld, var17, false & true);
         if (var122) {
            return;
         }

         AutoMineModule var287 = this;
         ClientPlayerEntity var291 = clientPlayerEntity;
         ClientWorld var295 = clientWorld;
         PlayerEntity var299 = playerEntity;
         Set var303 = set;
         MutableObject var307 = mutableObject;
         MutableObject cap117 = mutableObject2;
         MutableObject cap116 = var307;
         Delegating cap115 = var7;
         Set cap114 = var303;
         PlayerEntity cap113 = var299;
         ClientWorld cap112 = var295;
         ClientPlayerEntity cap111 = var291;
         AutoMineModule cap110 = var287;
         Consumer<BlockPos> var139 = var8x -> cap110.handleClientPlayerEntity8(cap111, cap112, cap113, cap114, cap115, cap116, cap117, var8x);
         Predicate[] var140 = new Predicate[1];
         var140[0] = var55;
         var141 = var140;
         var142 = var139;
         var143 = var17;
      } else {
         AutoMineModule var288 = this;
         ClientPlayerEntity var292 = clientPlayerEntity;
         ClientWorld var296 = clientWorld;
         PlayerEntity var300 = playerEntity;
         Set var304 = set;
         MutableObject var308 = mutableObject;
         MutableObject cap109 = mutableObject2;
         MutableObject cap108 = var308;
         Delegating cap107 = var7;
         Set cap106 = var304;
         PlayerEntity cap105 = var300;
         ClientWorld cap104 = var296;
         ClientPlayerEntity cap103 = var292;
         AutoMineModule cap102 = var288;
         Consumer<BlockPos> var116 = var8x -> cap102.handleClientPlayerEntity6(cap103, cap104, cap105, cap106, cap107, cap108, cap109, var8x);
         Predicate[] var117 = new Predicate[1];
         var117[0] = var55;
         this.handleClientPlayerEntity3(clientPlayerEntity, clientWorld, var10, var116, var117);
         Setting var118 = this.bombing;
         Object var119 = var118.getValue();
         boolean var120 = var119 instanceof Boolean;
         if (var119 != null && !var120) {
            throw new ClassCastException("CHECKCAST failed");
         }

         boolean var144 = (Boolean)var119;
         if (!var144) {
            return;
         }

         boolean var145 = this.testWorld2(clientWorld, var10, true & true);
         if (!var145) {
            return;
         }

         boolean var146 = playerEntity.isCrawling();
         if (var146) {
            return;
         }

         boolean var147 = playerEntity.isSwimming();
         if (var147) {
            return;
         }

         AutoMineModule var289 = this;
         var292 = clientPlayerEntity;
         var296 = clientWorld;
         var300 = playerEntity;
         var304 = set;
         var308 = mutableObject;
         MutableObject cap125 = mutableObject2;
         MutableObject cap124 = var308;
         Delegating cap123 = var7;
         Set cap122 = var304;
         PlayerEntity cap121 = var300;
         ClientWorld cap120 = var296;
         ClientPlayerEntity cap119 = var292;
         AutoMineModule cap118 = var289;
         Consumer<BlockPos> var164 = var8x -> cap118.handleClientPlayerEntity7(cap119, cap120, cap121, cap122, cap123, cap124, cap125, var8x);
         Predicate[] var165 = new Predicate[2];
         var165[0] = var55;
         ClientWorld cap126 = clientWorld;
         Predicate<BlockPos> var182 = var1 -> testClientWorld4(cap126, var1);
         var165[1] = var182;
         this.handleClientPlayerEntity3(clientPlayerEntity, clientWorld, var13, var164, var165);
         Stream var183 = var10.stream();
         Function<BlockPos, BlockPos> var200 = BlockPos::up;
         Stream var201 = var183.map(var200);
         Collector var202 = Collectors.toSet();
         Object var203 = var201.collect(var202);
         boolean var204 = var203 instanceof Set;
         if (var203 != null && !var204) {
            throw new ClassCastException("CHECKCAST failed");
         }

         AutoMineModule var290 = this;
         var292 = clientPlayerEntity;
         var296 = clientWorld;
         var300 = playerEntity;
         var304 = set;
         var308 = mutableObject;
         MutableObject cap134 = mutableObject2;
         MutableObject cap133 = var308;
         Delegating cap132 = var7;
         Set cap131 = var304;
         PlayerEntity cap130 = var300;
         ClientWorld cap129 = var296;
         ClientPlayerEntity cap128 = var292;
         AutoMineModule cap127 = var290;
         Consumer<BlockPos> var221 = var8x -> cap127.handleClientPlayerEntity5(cap128, cap129, cap130, cap131, cap132, cap133, cap134, var8x);
         Predicate[] var222 = new Predicate[2];
         var222[0] = var55;
         ClientWorld cap135 = clientWorld;
         Predicate<BlockPos> var239 = var1 -> testClientWorld3(cap135, var1);
         var222[1] = var239;
         var141 = var222;
         var142 = var221;
         var143 = var203;
      }

      this.handleClientPlayerEntity3(clientPlayerEntity, clientWorld, (Set<BlockPos>)var143, var142, var141);
   }

   public BlockPos resolveBlockPos2(ClientWorld clientWorld, List<BlockPos> list) {
      MutableObject<BlockPos> mutableObject = new MutableObject<>();
      MutableFloat mutableFloat = new MutableFloat(-1.0F);

      for (BlockPos blockPos : list) {
         BlockState blockState = clientWorld.getBlockState(blockPos);
         if (blockState.isAir()) {
            return blockPos;
         }

         float f = BlockBreakManager.computeBlockPos(blockPos, blockState, clientWorld);
         if (mutableObject.getValue() == null || mutableFloat.getValue() < f) {
            mutableObject.setValue(blockPos);
            mutableFloat.setValue(f);
         }
      }

      return mutableObject.getValue();
   }

   public double computeClientPlayerEntity(
      ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, PlayerEntity playerEntity, BlockPos blockPos, AutoMineModule.Position.Base base
   ) {
      Delegating delegating = new Delegating(clientWorld);
      AutoMineModule.Position position = new AutoMineModule.Position(blockPos, base);
      position.resolvePosition(this.phobosClient, clientPlayerEntity, clientWorld, playerEntity, ImmutableSet.of(), delegating).handleDelegating2(delegating);
      return position.getDoubleValue();
   }

   public Pair<BlockPos, BlockPos> createPair(ClientWorld clientWorld, BlockPos blockPos) {
      Direction direction = BedBlock.getOppositePartDirection(clientWorld.getBlockState(blockPos));
      return new Pair<>(blockPos, blockPos.offset(direction));
   }

   public boolean testBlockPos(
      BlockPos blockPos, PlayerEntity playerEntity, ClientWorld clientWorld, Delegating delegating, BiConsumer<BlockPos, Boolean> biConsumer
   ) {
      long var65 = 0L;
      long var63 = 0L;
      double var6 = playerEntity.getY();
      Set var8 = BlockPosUtils.resolveSet3(playerEntity, var6 + 2.5);
      PhobosClient var9 = this.phobosClient;
      SurroundManager var10 = var9.getSurroundManager();
      Set var11 = var10.resolveSet2(playerEntity, true & true);
      Stream var12 = var11.stream();
      Function<BlockPos, BlockPos> var29 = BlockPos::up;
      Stream var30 = var12.map(var29);
      Collector var31 = Collectors.toSet();
      Object var32 = var30.collect(var31);
      boolean var33 = var32 instanceof Set;
      if (var32 != null && !var33) {
         throw new ClassCastException("CHECKCAST failed");
      }

      byte var48;
      label119: {
         label118: {
            Boolean var52;
            Object var53;
            label124: {
               label116: {
                  boolean var34 = var8.contains(blockPos);
                  if (!var34) {
                     boolean var35 = ((Set)var32).contains(blockPos);
                     if (!var35) {
                        break label116;
                     }
                  }

                  boolean var36 = BlockUtils.testBlockPos(blockPos, clientWorld, true & true);
                  if (var36) {
                     Boolean var51 = false & true;
                     var52 = var51;
                     var53 = blockPos;
                     break label124;
                  }
               }

               Box var37 = playerEntity.getBoundingBox();
               double var38 = var37.minY;
               Set var40 = BlockPosUtils.resolveSet3(playerEntity, var38 - 0.5);
               Box var41 = playerEntity.getBoundingBox();
               double var42 = var41.minY;
               Set var44 = BlockPosUtils.resolveSet3(playerEntity, var42 + 0.25);
               boolean var45 = var11.contains(blockPos);
               if (!var45) {
                  boolean var46 = var44.contains(blockPos);
                  if (!var46) {
                     boolean var47 = var40.contains(blockPos);
                     if (!var47) {
                        var48 = 0;
                        break label119;
                     }
                  }
               }

               MutableObject var54 = new MutableObject();
               MutableFloat var55 = new MutableFloat((float)0.0F);
               Map var56 = delegating.getBlockPosMap();
               Block var57 = Blocks.AIR;
               BlockState var58 = var57.getDefaultState();
               var56.put(blockPos, var58);
               Direction[] var59 = DirectionUtils.directionArray;
               int var60 = var59.length;
               long var61 = 0L;

               while (true) {
                  long var10000 = (int)var61 & 4294967295L | var63 & -4294967296L;
                  long var67 = var65;
                  long var69 = var10000;
                  long var71 = var67;
                  long var73 = var69;
                  long var75 = var71;
                  long var77 = var73;
                  long var79 = var75;
                  long var81 = var77;
                  if ((int)((int)var61 & 4294967295L) >= var60) {
                     Map var269 = delegating.getBlockPosMap();
                     var269.clear();
                     Object var270 = var54.getValue();
                     boolean var271 = var270 instanceof BlockPos;
                     if (var270 != null && !var271) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     if (var270 == null) {
                        boolean var342 = BlockUtils.testBlockPos5(blockPos, clientWorld);
                        if (!var342) {
                           int var343 = (this.testBlockPos3(blockPos, clientWorld, delegating, biConsumer)) ? 1 : 0;
                           long var49 = var343 & 0xFF & 0xFF;
                           return (((int)((int)var49 & 0xFF & 0xFF)) != 0);
                        }

                        Boolean var497 = true & true;
                        biConsumer.accept(blockPos, var497);
                        break label118;
                     }

                     Boolean var496 = true & true;
                     var52 = var496;
                     var53 = var270;
                     break;
                  }

                  long var83 = var79;
                  long var85 = var81;
                  long var87 = var83;
                  long var89 = var85;
                  long var91 = var87;
                  long var93 = var89;
                  long var95 = var91;
                  long var97 = var93;
                  long var99 = var95;
                  long var101 = var97;
                  Object var103 = var59[(int)((int)var61 & 4294967295L)];
                  long var104 = var99;
                  long var106 = var101;
                  long var108 = var104;
                  long var110 = var106;
                  long var112 = var108;
                  long var114 = var110;
                  long var116 = var112;
                  long var118 = var114;
                  long var120 = var116;
                  long var122 = var118;
                  long var124 = var120;
                  long var126 = var122;
                  long var128 = var124;
                  long var130 = var126;
                  long var132 = var128;
                  long var134 = var130;
                  long var136 = var132;
                  long var138 = var134;
                  long var140 = var136;
                  long var142 = var138;
                  BlockPos var144 = blockPos.offset((Direction)var103);
                  long var145 = var140;
                  long var147 = var142;
                  long var149 = var145;
                  long var151 = var147;
                  long var153 = var149;
                  long var155 = var151;
                  long var157 = var153;
                  long var159 = var155;
                  long var161 = var157;
                  long var163 = var159;
                  long var165 = var161;
                  long var167 = var163;
                  long var169 = var165;
                  long var171 = var167;
                  BlockPos var173 = var144.down();
                  long var174 = var169;
                  long var176 = var171;
                  long var178 = var174;
                  long var180 = var176;
                  long var182 = var178;
                  long var184 = var180;
                  long var186 = var182;
                  long var188 = var184;
                  long var190 = var186;
                  long var192 = var188;
                  long var194 = var190;
                  long var196 = var192;
                  long var198 = var194;
                  long var200 = var196;
                  long var202 = var198;
                  long var204 = var200;
                  long var206 = var202;
                  long var208 = var204;
                  long var210 = var206;
                  long var212 = var208;
                  long var214 = var210;
                  long var216 = var212;
                  long var218 = var214;
                  long var220 = var216;
                  Setting var222 = this.crystalsBase;
                  long var223 = var218;
                  long var225 = var220;
                  long var227 = var223;
                  long var229 = var225;
                  long var231 = var227;
                  long var233 = var229;
                  long var235 = var231;
                  long var237 = var233;
                  long var239 = var235;
                  long var241 = var237;
                  long var243 = var239;
                  long var245 = var241;
                  Object var247 = var222.getValue();
                  long var248 = var243;
                  long var250 = var245;
                  long var252 = var248;
                  long var254 = var250;
                  long var256 = var252;
                  long var258 = var254;
                  boolean var260 = var247 instanceof Boolean;
                  if (var247 != null && !var260) {
                     throw new ClassCastException("CHECKCAST failed");
                  }

                  long var265 = var256;
                  long var267 = var258;
                  long var276 = var265;
                  long var278 = var267;
                  long var280 = var276;
                  long var282 = var278;
                  long var284 = var280;
                  long var286 = var282;
                  long var288 = var284;
                  long var290 = var286;
                  long var292 = var288;
                  long var294 = var290;
                  long var296 = var292;
                  long var298 = var294;
                  boolean var300 = (Boolean)var247;
                  long var301 = var296;
                  long var303 = var298;
                  long var305 = var301;
                  long var307 = var303;
                  long var309 = var305;
                  long var311 = var307;
                  long var313 = var309;
                  long var315 = var311;
                  long var317 = var313;
                  long var319 = var315;
                  boolean var321 = BlockUtils.testBlockPos2(var173, clientWorld, true & true, var300);
                  long var322 = var317;
                  long var324 = var319;
                  long var326 = var322;
                  long var328 = var324;
                  long var330 = var326;
                  long var332 = var328;
                  long var492;
                  long var494;
                  if (!var321) {
                     long var338 = var330;
                     long var340 = var332;
                     var492 = var338;
                     var494 = var340;
                  } else {
                     label130: {
                        long var334 = var330;
                        long var336 = var332;
                        long var344 = var334;
                        long var346 = var336;
                        long var348 = var344;
                        long var350 = var346;
                        long var352 = var348;
                        long var354 = var350;
                        PhobosClient var356 = this.phobosClient;
                        long var357 = var352;
                        long var359 = var354;
                        long var361 = var357;
                        long var363 = var359;
                        long var365 = var361;
                        long var367 = var363;
                        long var369 = var365;
                        long var371 = var367;
                        long var373 = var369;
                        long var375 = var371;
                        long var377 = var373;
                        long var379 = var375;
                        DamageCalculator var381 = var356.getDamageCalculator();
                        long var382 = var377;
                        long var384 = var379;
                        long var386 = var382;
                        long var388 = var384;
                        long var390 = var386;
                        long var392 = var388;
                        long var394 = var390;
                        long var396 = var392;
                        long var398 = var394;
                        long var400 = var396;
                        long var402 = var398;
                        long var404 = var400;
                        long var406 = var402;
                        long var408 = var404;
                        long var410 = var406;
                        long var412 = var408;
                        long var414 = var410;
                        long var416 = var412;
                        long var418 = var414;
                        long var420 = var416;
                        long var422 = var418;
                        long var424 = var420;
                        long var426 = var422;
                        long var428 = var424;
                        long var430 = var426;
                        long var432 = var428;
                        float var434 = var381.computeEntity(playerEntity, delegating, var173);
                        long var435 = var430;
                        long var437 = var432;
                        long var439 = var435;
                        long var441 = var437;
                        long var443 = var439;
                        long var445 = var441;
                        long var447 = Float.floatToRawIntBits(var434) & 4294967295L | var443 & -4294967296L;
                        long var449 = var445;
                        long var451 = var447;
                        long var453 = var449;
                        long var455 = var451;
                        long var457 = var453;
                        long var459 = var455;
                        long var461 = var457;
                        long var463 = var459;
                        long var465 = var461;
                        long var467 = var463;
                        long var469 = var465;
                        Object var471 = var54.getValue();
                        long var472 = var467;
                        long var474 = var469;
                        long var476 = var472;
                        long var478 = var474;
                        long var480 = var476;
                        long var482 = var478;
                        long var488;
                        long var490;
                        if (var471 != null) {
                           long var656;
                           long var658;
                           byte var660;
                           label132: {
                              long var484 = var480;
                              long var486 = var482;
                              long var498 = var484;
                              long var500 = var486;
                              long var502 = var498;
                              long var504 = var500;
                              long var506 = var502;
                              long var508 = var504;
                              long var510 = var506;
                              long var512 = var508;
                              long var514 = var510;
                              long var516 = var512;
                              long var518 = var514;
                              long var520 = var516;
                              long var522 = var518;
                              long var524 = var520;
                              long var526 = var522;
                              long var528 = var524;
                              Float var530 = var55.getValue();
                              long var531 = var526;
                              long var533 = var528;
                              long var535 = var531;
                              long var537 = var533;
                              long var539 = var535;
                              long var541 = var537;
                              long var543 = var539;
                              long var545 = var541;
                              long var547 = var543;
                              long var549 = var545;
                              long var551 = var547;
                              long var553 = var549;
                              long var555 = var551;
                              long var557 = var553;
                              float var559 = var530;
                              long var560 = var555;
                              long var562 = var557;
                              long var564 = var560;
                              long var566 = var562;
                              long var568 = var564;
                              long var570 = var566;
                              long var576;
                              long var578;
                              if (!Float.isNaN(Float.intBitsToFloat((int)((int)((int)var480 & 4294967295L) & 4294967295L)))
                                 && !Float.isNaN(Float.intBitsToFloat((int)((int)((int)var480 & 4294967295L) & 4294967295L)))) {
                                 long var572 = var568;
                                 long var574 = var570;
                                 if (!Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(var559)))) {
                                    long var652 = var572;
                                    long var654 = var574;
                                    if (Float.intBitsToFloat((int)((int)((int)((int)var480 & 4294967295L) & 4294967295L) & 4294967295L))
                                       > Float.intBitsToFloat(Float.floatToRawIntBits(var559))) {
                                       long var669 = var652;
                                       long var671 = var654;
                                       var656 = var669;
                                       var658 = var671;
                                       var660 = 1;
                                       break label132;
                                    }

                                    long var673 = var652;
                                    long var675 = var654;
                                    if (!Float.isNaN(Float.intBitsToFloat((int)((int)((int)((int)var480 & 4294967295L) & 4294967295L) & 4294967295L)))
                                       && !Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(var559)))) {
                                       long var677 = var673;
                                       long var679 = var675;
                                       if (Float.intBitsToFloat(Float.floatToRawIntBits(var434)) == Float.intBitsToFloat(Float.floatToRawIntBits(var559))) {
                                          long var681 = var677;
                                          long var683 = var679;
                                          var656 = var681;
                                          var658 = var683;
                                          var660 = 0;
                                          break label132;
                                       }

                                       var576 = var677;
                                       var578 = var679;
                                    } else {
                                       var576 = var673;
                                       var578 = var675;
                                    }
                                 } else {
                                    var576 = var572;
                                    var578 = var574;
                                 }
                              } else {
                                 var576 = var568;
                                 var578 = var570;
                              }

                              var656 = var576;
                              var658 = var578;
                              var660 = -1;
                           }

                           long var661 = var656;
                           long var663 = var658;
                           long var665 = var661;
                           long var667 = var663;
                           if ((int)(var660 & 4294967295L) <= 0) {
                              var492 = var665;
                              var494 = var667;
                              break label130;
                           }

                           var488 = var665;
                           var490 = var667;
                        } else {
                           var488 = var480;
                           var490 = var482;
                        }

                        long var580 = var488;
                        long var582 = var490;
                        long var584 = var580;
                        long var586 = var582;
                        long var588 = var584;
                        long var590 = var586;
                        long var592 = var588;
                        long var594 = var590;
                        long var596 = var592;
                        long var598 = var594;
                        long var600 = var596;
                        long var602 = var598;
                        long var604 = var600;
                        long var606 = var602;
                        long var608 = var604;
                        long var610 = var606;
                        var54.setValue(var173);
                        long var612 = var608;
                        long var614 = var610;
                        long var616 = var612;
                        long var618 = var614;
                        long var620 = var616;
                        long var622 = var618;
                        long var624 = var620;
                        long var626 = var622;
                        long var628 = var624;
                        long var630 = var626;
                        long var632 = var628;
                        long var634 = var630;
                        long var636 = var632;
                        long var638 = var634;
                        long var640 = var636;
                        long var642 = var638;
                        long var644 = var640;
                        long var646 = var642;
                        var55.setValue((float)Float.intBitsToFloat((int)((int)((int)((int)var480 & 4294967295L) & 4294967295L) & 4294967295L)));
                        long var648 = var644;
                        long var650 = var646;
                        var10000 = (int)var650 + 1 & 4294967295L;
                        var65 = var648;
                        var63 = var650;
                        var61 = var10000;
                        continue;
                     }
                  }

                  var10000 = (int)var494 + 1 & 4294967295L;
                  var65 = var492;
                  var63 = var494;
                  var61 = var10000;
               }
            }

            biConsumer.accept((BlockPos)var53, var52);
         }

         var48 = 1;
      }

      long var49 = var48 & 4294967295L;
      return (((int)((int)var49 & 0xFF & 0xFF)) != 0);
   }

   public void run2() {
      this.blockPosMap.clear();
      this.playerEntity = null;
   }

   public void handleClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, BlockPos blockPos, List<BlockPos> list) {
      for (Direction direction : DirectionUtils.directionArray2) {
         BlockPos blockPos2 = blockPos.offset(direction);
         BlockState blockState = clientWorld.getBlockState(blockPos2);
         if (!(blockState.getBlock() instanceof BedBlock)
            && !this.testClientPlayerEntity2(clientPlayerEntity, clientWorld, blockPos, blockPos2, blockState, direction)
            && this.testSpeedmineModule(SpeedmineModule.getSpeedmineModule(), blockPos2, clientPlayerEntity, clientWorld)
            && !blockState.isAir()) {
            list.add(blockPos.offset(direction));
         }
      }
   }

   public boolean testClientWorld(ClientWorld clientWorld, BlockPos blockPos, BlockPos blockPos2) {
      Pair<BlockPos, BlockPos> pair = this.createPair(clientWorld, blockPos);
      return pair.getLeft().equals(blockPos2) ? true : pair.getRight().equals(blockPos2);
   }

   public void handleClientPlayerEntity3(
      ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Set<BlockPos> set, Consumer<BlockPos> consumer, Predicate<BlockPos>... predicates
   ) {
      for (BlockPos blockPos : set) {
         if (this.testSpeedmineModule(SpeedmineModule.getSpeedmineModule(), blockPos, clientPlayerEntity, clientWorld)
            && this.testBlockPos2(blockPos, predicates)) {
            consumer.accept(blockPos);
         }
      }
   }

   public boolean testWorld2(World world, Set<BlockPos> set, boolean flag) {
      return this.testWorld(world, set, blockState -> blockState.isOf(Blocks.BEDROCK), flag);
   }

   public boolean testClientPlayerEntity2(
      ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, BlockPos blockPos, BlockPos blockPos2, BlockState blockState, Direction direction
   ) {
      if (this.strict.getValue() && !blockState.isAir()) {
         Direction direction2 = ShapeDirectionResolver.shapeDirectionResolver.resolveDirection(blockPos2, clientPlayerEntity, clientWorld);
         return direction2 == null ? true : !ExactDirectionResolver.exactDirectionResolver.testBlockPos(blockPos, direction, clientWorld, clientPlayerEntity);
      } else {
         return false;
      }
   }

   public boolean testBlockPos2(BlockPos blockPos, Predicate<BlockPos>... predicates) {
      for (Predicate<BlockPos> predicate : predicates) {
         if (!predicate.test(blockPos)) {
            return false;
         }
      }

      return true;
   }

   @Override
   public String nothing() {
      return switch ((AutoMineModule.Mode)this.mode.getValue()) {
         case Combat -> this.selection.getValue() + (this.playerEntity != null ? ", " + this.playerEntity.getNameForScoreboard() : "");
         case Beds -> "Beds";
      };
   }

   public boolean testSpeedmineModule(SpeedmineModule speedmineModule, BlockPos blockPos, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      return clientPlayerEntity.getEyePos().squaredDistanceTo(blockPos.toCenterPos()) > this.range.getValue() * this.range.getValue()
         ? false
         : speedmineModule.testBlockPos2(blockPos, clientWorld.getBlockState(blockPos), clientPlayerEntity, clientWorld);
   }

   public boolean testBlockPos3(BlockPos blockPos, ClientWorld clientWorld, Delegating delegating, BiConsumer<BlockPos, Boolean> biConsumer) {
      if (!this.crystalsGeneric.getValue()) {
         return false;
      }

      delegating.getBlockPosMap().put(blockPos, Blocks.AIR.getDefaultState());
      Area area = new Area();
      area.handleVec3d(blockPos.toCenterPos(), 4.0, Predicates.alwaysTrue());
      ItemEntity itemEntity = new ItemEntity(clientWorld, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, new ItemStack(Items.OBSIDIAN));

      for (BlockPos blockPos2 : area) {
         if (BlockUtils.testBlockPos5(blockPos2, clientWorld)) {
            float f = this.phobosClient.getDamageCalculator().computeEntity(itemEntity, delegating, blockPos2);
            if (!(f < 5.0F)) {
               biConsumer.accept(blockPos2, true);
               delegating.getBlockPosMap().clear();
               return true;
            }
         }
      }

      delegating.getBlockPosMap().clear();
      return false;
   }

   public void handleClientPlayerEntity4(
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      PlayerEntity playerEntity,
      Set set,
      Delegating delegating,
      MutableObject mutableObject,
      MutableObject mutableObject2,
      BlockPos blockPos
   ) {
      AutoMineModule.Position position = new AutoMineModule.Position(blockPos, AutoMineModule.Position.Base.Inside);
      position.resolvePosition(this.phobosClient, clientPlayerEntity, clientWorld, playerEntity, set, delegating).handleDelegating2(delegating);
      position.handleText("inside", true);
      position.handleText("target", playerEntity);
      this.handlePosition(position, mutableObject, mutableObject2);
   }

   public boolean isFlag() {
      return this.flag;
   }

   public Setting<Boolean> getAwait() {
      return this.await;
   }

   public static boolean testClientWorld2(ClientWorld clientWorld, BlockPos blockPos) {
      BlockState blockState = clientWorld.getBlockState(blockPos);
      if (BlockUtils.testBlockPos9(blockPos, clientWorld)) {
         return false;
      } else {
         return blockState.isOf(Blocks.COBWEB) ? false : !(blockState.getBlock() instanceof AnvilBlock);
      }
   }

   public Setting<Boolean> getDown() {
      return this.down;
   }

   public Setting<SwitchMode> getCrystalsSwitch() {
      return this.crystalsSwitch;
   }

   public void handleClientPlayerEntity5(
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      PlayerEntity playerEntity,
      Set set,
      Delegating delegating,
      MutableObject mutableObject,
      MutableObject mutableObject2,
      BlockPos blockPos
   ) {
      AutoMineModule.Position position = new AutoMineModule.Position(blockPos, AutoMineModule.Position.Base.Top);
      position.resolvePosition(this.phobosClient, clientPlayerEntity, clientWorld, playerEntity, set, delegating).handleDelegating2(delegating);
      position.handleText("target", playerEntity);
      this.handlePosition(position, mutableObject, mutableObject2);
   }

   public static boolean testClientWorld3(ClientWorld clientWorld, BlockPos blockPos) {
      return !BlockUtils.testBlockPos7(blockPos.up(), clientWorld);
   }

   public Setting<Boolean> getCrystalsGeneric() {
      return this.crystalsGeneric;
   }

   public Setting<Boolean> getCrystalsBase() {
      return this.crystalsBase;
   }

   public DebugSettings getDebugSettings() {
      return this.debugSettings;
   }

   public PlayerEntity getPlayerEntity() {
      return this.playerEntity;
   }

   public Setting<Boolean> getUncrawl() {
      return this.uncrawl;
   }

   public static boolean testPlayerEntity(PlayerEntity playerEntity, BlockPos blockPos) {
      return blockPos.getY() < playerEntity.getY() - 0.45;
   }

   public static boolean testClientWorld4(ClientWorld clientWorld, BlockPos blockPos) {
      return !BlockUtils.testBlockPos7(blockPos.up(), clientWorld);
   }

   public Setting<Boolean> getInside() {
      return this.inside;
   }

   public Setting<Boolean> getOnGround() {
      return this.onGround;
   }

   public void handleClientPlayerEntity6(
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      PlayerEntity playerEntity,
      Set set,
      Delegating delegating,
      MutableObject mutableObject,
      MutableObject mutableObject2,
      BlockPos blockPos
   ) {
      AutoMineModule.Position position = new AutoMineModule.Position(blockPos, AutoMineModule.Position.Base.Surrounding);
      position.resolvePosition(this.phobosClient, clientPlayerEntity, clientWorld, playerEntity, set, delegating).handleDelegating2(delegating);
      position.handleText("target", playerEntity);
      this.handlePosition(position, mutableObject, mutableObject2);
   }

   public void handleClientPlayerEntity7(
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      PlayerEntity playerEntity,
      Set set,
      Delegating delegating,
      MutableObject mutableObject,
      MutableObject mutableObject2,
      BlockPos blockPos
   ) {
      AutoMineModule.Position position = new AutoMineModule.Position(blockPos, AutoMineModule.Position.Base.Top);
      position.resolvePosition(this.phobosClient, clientPlayerEntity, clientWorld, playerEntity, set, delegating).handleDelegating2(delegating);
      position.handleText("target", playerEntity);
      this.handlePosition(position, mutableObject, mutableObject2);
   }

   public Setting<Silent> getCrystalsSilent() {
      return this.crystalsSilent;
   }

   public static boolean testClientWorld5(ClientWorld clientWorld, ClientPlayerEntity clientPlayerEntity, BlockPos blockPos) {
      BlockState blockState = clientWorld.getBlockState(blockPos);
      SpeedmineModule speedmineModule = SpeedmineModule.getSpeedmineModule();
      return speedmineModule.testBlockPos2(blockPos, blockState, clientPlayerEntity, clientWorld)
         && speedmineModule.testClientPlayerEntity2(clientPlayerEntity, clientWorld, blockPos)
         && !blockState.isOf(Blocks.COBWEB)
         && !(blockState.getBlock() instanceof AnvilBlock);
   }

   public Setting<Boolean> getBombing() {
      return this.bombing;
   }

   public Setting<Boolean> getCrystalsInstant() {
      return this.crystalsInstant;
   }

   public Setting<Boolean> getBooleanValueSetting6() {
      return this.crystals;
   }

   public Map<BlockPos, AutoMineModule.ExpectedCrystal> getBlockPosMap() {
      return this.blockPosMap;
   }

   public void handleClientPlayerEntity8(
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      PlayerEntity playerEntity,
      Set set,
      Delegating delegating,
      MutableObject mutableObject,
      MutableObject mutableObject2,
      BlockPos blockPos
   ) {
      AutoMineModule.Position position = new AutoMineModule.Position(blockPos, AutoMineModule.Position.Base.Bottom);
      position.resolvePosition(this.phobosClient, clientPlayerEntity, clientWorld, playerEntity, set, delegating).handleDelegating2(delegating);
      position.handleText("target", playerEntity);
      this.handlePosition(position, mutableObject, mutableObject2);
   }

   public static AutoMineModule getAutoMineModule() {
      return autoMineModule;
   }

   private enum Crystals {
      Target,
      Scan;
   }

   private static class ExpectedCrystal {
      @Nullable
      public final BlockPos blockPos;
      public final long longValue;
      public boolean flag;

      public void setFlag(boolean flag) {
         this.flag = flag;
      }

      public boolean isEmpty() {
         return this.blockPos == null;
      }

      public ExpectedCrystal(@Nullable BlockPos blockPos, long l) {
         this.blockPos = blockPos;
         this.longValue = l;
      }

      @Nullable
      public BlockPos getBlockPos() {
         return this.blockPos;
      }

      public boolean isFlag() {
         return this.flag;
      }

      public boolean check() {
         return System.currentTimeMillis() - this.longValue > 250L;
      }

      public long getLongValue() {
         return this.longValue;
      }

      public static AutoMineModule.ExpectedCrystal createExpectedCrystal(@Nullable BlockPos blockPos) {
         return new AutoMineModule.ExpectedCrystal(blockPos, System.currentTimeMillis());
      }

      public static AutoMineModule.ExpectedCrystal getExpectedCrystal() {
         return createExpectedCrystal(null);
      }
   }

   private enum Mode {
      Combat,
      Beds;
   }

   public static class Position {
      public final Map<String, Object> textMap = new HashMap<>();
      public final Vec3i[] vec3iArray = new Vec3i[]{new Vec3i(1, 0, 1), new Vec3i(-1, 0, 1), new Vec3i(-1, 0, -1), new Vec3i(1, 0, -1)};
      public final AutoMineModule.Position.Base base;
      public final BlockPos blockPos;
      public double doubleValue = 0.0;

      public AutoMineModule.Position.Base getBase() {
         return this.base;
      }

      public Map<String, Object> getTextMap() {
         return this.textMap;
      }

      public double getDoubleValue() {
         return this.doubleValue;
      }

      public void run() {
         this.doubleValue = this.base.getDefaultGScore();
      }

      public Position(BlockPos blockPos, AutoMineModule.Position.Base base) {
         this.blockPos = blockPos;
         this.base = base;
      }

      public AutoMineModule.Position resolvePosition(
         PhobosClient phobosClient,
         ClientPlayerEntity clientPlayerEntity,
         ClientWorld clientWorld,
         PlayerEntity playerEntity,
         Set<BlockPos> set,
         Delegating delegating
      ) {
         long var33 = 0L;
         Map var7 = delegating.getBlockPosMap();
         BlockPos var8 = this.blockPos;
         Block var9 = Blocks.AIR;
         BlockState var10 = var9.getDefaultState();
         var7.put(var8, var10);
         this.run();
         BlockPos var11 = this.blockPos;
         boolean var12 = set.contains(var11);
         if (var12) {
            this.handleDouble2(10.0);
         }

         List var13 = SpeedmineModule.getList();
         BlockPos var14 = this.blockPos;
         boolean var15 = var13.contains(var14);
         if (var15) {
            this.handleDouble(0.5);
         }

         BlockPos var16 = this.blockPos;
         BlockState var17 = clientWorld.getBlockState(var16);
         int var18 = (var17.isAir()) ? 1 : 0;
         MutableBoolean var19 = new MutableBoolean();
         AutoMineModule.Position.Base var20 = this.base;
         int var21 = var20.ordinal();
         int var51;
         double var52;
         if (var21 != 2) {
            BlockPos var22 = this.blockPos;
            boolean var23 = BlockUtils.testBlockPos(var22, clientWorld, false & true);
            if (var23) {
               BlockPos var24 = this.blockPos;
               boolean var25 = this.testPhobosClient(phobosClient, clientPlayerEntity, clientWorld, playerEntity, var24);
               if (var25) {
                  this.handleDouble(5.0);
               }
            }

            AutoMineModule.Position.Base var26 = this.base;
            AutoMineModule.Position.Base var27 = AutoMineModule.Position.Base.Surrounding;
            boolean var28 = var26.equals(var27);
            if (var28) {
               this.handleDelegating(delegating, (((int)((int)(var18 & 0xFF & 0xFF) & 0xFF & 0xFF) & 1) != 0), 4.0, 8.0);
            }

            Direction[] var29 = DirectionUtils.directionArray;
            int var30 = var29.length;
            long var31 = 0L;

            while (true) {
               long var35 = (int)var31 & 4294967295L | var33 & -4294967296L;
               long var37 = var35;
               long var39 = var37;
               long var41 = var39;
               if ((int)((int)var31 & 4294967295L) >= (int)(var30 & 4294967295L)) {
                  boolean var139 = var19.isTrue();
                  if (!var139 || (int)(var18 & 0xFF & 0xFF) == 0) {
                     return this;
                  }

                  var51 = 7227048;
                  var52 = 10.0;
                  break;
               }

               long var43 = var41;
               long var54 = var43;
               long var56 = var54;
               long var58 = var56;
               long var60 = var58;
               Object var62 = var29[(int)((int)var31 & 4294967295L)];
               long var63 = var60;
               long var65 = var63;
               long var67 = var65;
               long var69 = var67;
               long var71 = var69;
               BlockPos var73 = this.blockPos;
               long var74 = var71;
               long var76 = var74;
               long var78 = var76;
               long var80 = var78;
               long var82 = var80;
               long var84 = var82;
               long var86 = var84;
               long var88 = var86;
               BlockPos var90 = var73.offset((Direction)var62);
               long var91 = var88;
               long var93 = var91;
               long var95 = var93;
               long var97 = var95;
               long var99 = var97;
               long var101 = var99;
               long var103 = var101;
               BlockPos var105 = var90.down();
               long var106 = var103;
               long var108 = var106;
               long var110 = var108;
               long var112 = var110;
               long var114 = var112;
               long var116 = var114;
               long var118 = var116;
               long var120 = var118;
               long var122 = var120;
               long var124 = var122;
               long var126 = var124;
               boolean var128 = BlockUtils.testBlockPos(var105, clientWorld, false & true);
               long var129 = var126;
               long var131 = var129;
               long var133 = var131;
               long var137;
               if (var128) {
                  long var135 = var133;
                  long var140 = var135;
                  long var142 = var140;
                  long var144 = var142;
                  long var146 = var144;
                  long var148 = var146;
                  long var150 = var148;
                  long var152 = var150;
                  long var154 = var152;
                  long var156 = var154;
                  long var158 = var156;
                  long var160 = var158;
                  long var162 = var160;
                  long var164 = var162;
                  long var166 = var164;
                  long var168 = var166;
                  long var170 = var168;
                  boolean var172 = this.testPhobosClient(phobosClient, clientPlayerEntity, clientWorld, playerEntity, var105);
                  long var173 = var170;
                  long var175 = var173;
                  long var177 = var175;
                  if (var172) {
                     long var179 = var177;
                     long var181 = var179;
                     long var183 = var181;
                     long var185 = var183;
                     long var187 = var185;
                     long var189 = var187;
                     long var191 = var189;
                     var19.setTrue();
                     long var193 = var191;
                     long var195 = var193;
                     long var197 = var195;
                     long var199 = var197;
                     long var201 = var199;
                     long var203 = var201;
                     long var205 = var203;
                     long var207 = var205;
                     long var209 = var207;
                     this.handleDouble(10.0);
                     var137 = var209;
                  } else {
                     var137 = var177;
                  }
               } else {
                  var137 = var133;
               }

               long var10000 = (int)var137 + 1 & 4294967295L;
               var33 = var137;
               var31 = var10000;
            }
         } else {
            List var45 = SpeedmineModule.getList();
            BlockPos var46 = this.blockPos;
            boolean var47 = var45.contains(var46);
            if (var47) {
               this.handleDouble(4.0);
               return this;
            }

            BlockPos var48 = this.blockPos;
            BlockPos var49 = var48.up();
            boolean var50 = BlockUtils.testBlockPos7(var49, clientWorld);
            if (!var50) {
               return this;
            }

            var51 = 7226744;
            var52 = 20.0;
         }

         switch (var51) {
            case 7226744:
            default:
               this.handleDouble2(var52);
               break;
            case 7227048:
               this.handleDouble(var52);
         }

         return this;
      }

      public <T> T resolveObject(String text, Class<T> classValue) {
         return (T)this.textMap.get(text);
      }

      public boolean testPhobosClient(
         PhobosClient phobosClient, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, PlayerEntity playerEntity, BlockPos blockPos
      ) {
         AutoCrystalModule var6 = AutoCrystalModule.getAutoCrystalModule();
         Setting var7 = var6.getDoubleValueSetting4();
         Object var8 = var7.getValue();
         boolean var9 = var8 instanceof Double;
         if (var8 != null && !var9) {
            throw new ClassCastException("CHECKCAST failed");
         }

         double var10 = (Double)var8;
         CrystalPlacement var12 = new CrystalPlacement(blockPos);
         ServerDetector var13 = phobosClient.getServerDetector();
         var12.handleServerDetector2(var13, clientWorld, clientPlayerEntity, var6);
         this.handlePhobosClient(phobosClient, clientPlayerEntity, clientWorld, playerEntity, var12);
         return var12.testDouble(var10);
      }

      public void handleDelegating(Delegating delegating, boolean flag, double d, double d2) {
         if (BlockUtils.testBlockPos(this.blockPos.down(), delegating, false)) {
            if (flag) {
               this.handleDouble(d2);
            }
         } else {
            this.handleDouble2(d);
         }
      }

      public void handleText(String text, Object object) {
         this.textMap.put(text, object);
      }

      public void handleDouble(double d) {
         this.doubleValue += d;
      }

      public BlockPos getBlockPos() {
         return this.blockPos;
      }

      public void handleDouble2(double d) {
         this.doubleValue -= d;
      }

      public void handlePhobosClient(
         PhobosClient phobosClient,
         ClientPlayerEntity clientPlayerEntity,
         ClientWorld clientWorld,
         PlayerEntity playerEntity,
         CrystalPlacement crystalPlacement
      ) {
         crystalPlacement.setFloatValue2(phobosClient.getDamageCalculator().computeEntity(playerEntity, clientWorld, crystalPlacement));
         crystalPlacement.setFloatValue(phobosClient.getDamageCalculator().computeEntity(clientPlayerEntity, clientWorld, crystalPlacement));
      }

      public Vec3i[] getVec3iArray() {
         return this.vec3iArray;
      }

      public void handleDelegating2(Delegating delegating) {
         delegating.getBlockPosMap().clear();
      }

      public enum Base {
         Inside(100.0),
         Surrounding(4.0),
         Top(1.0),
         Bottom(2.0);

         public final double defaultGScore;

         Base(final double defaultGScore) {
            this.defaultGScore = defaultGScore;
         }

         public double getDefaultGScore() {
            return this.defaultGScore;
         }
      }
   }

   private enum Selection {
      Dynamic,
      Angle,
      Range;
   }
}
