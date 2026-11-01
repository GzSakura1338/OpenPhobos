package org.phobos.module.player;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.phobos.api.Visibility;
import org.phobos.api.BlockBreakContext;
import org.phobos.api.TimeGate.Normal;
import org.phobos.core.placement.DirectionResolver;
import org.phobos.core.placement.ExactDirectionResolver;
import org.phobos.core.placement.ShapeDirectionResolver;
import org.phobos.core.block.SimpleBlockBreaker;
import org.phobos.core.block.PacketBlockBreaker;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.event.events.CancellableEvent;
import org.phobos.core.event.events.UpdateBreakEvent;
import org.phobos.core.event.events.StartBreakEvent;
import org.phobos.core.event.events.FinishBreakEvent;
import org.phobos.core.render.RenderContext;
import org.phobos.core.setting.SettingGroup;
import org.phobos.core.PhobosClient;
import org.phobos.core.event.events.UnusedEvent;
import org.phobos.core.module.Categories;
import org.phobos.core.math.MathUtils;
import org.phobos.core.combat.CrystalUtils;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.tick.TickUtils;
import org.phobos.core.event.events.PacketEvent.PostReceive;
import org.phobos.core.event.events.PacketEvent.PostSend;
import org.phobos.core.player.InteractionManager.Silent;
import org.phobos.core.setting.SettingSubsection.Builder;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class SpeedmineModule extends Module {
   public final Setting<Boolean> mine;
   public final Setting<Boolean> air;
   public final Setting<Boolean> abort;
   public final Setting<Boolean> swap;
   public final Setting<Boolean> rotate = this.resolveSetting2(new BooleanSetting("Rotate", false));
   public final Setting<Boolean> booleanValueSetting;
   public final Setting<Boolean> fast;
   public final Setting<Silent> swapType;
   public final Setting<SpeedmineModule.Rotate> rotateType = this.resolveSetting2(new EnumSetting<>("RotateType", SpeedmineModule.Rotate.Normal));
   public static SpeedmineModule speedmineModule;
   public final Setting<SpeedmineModule._2B2T> n2b2t;
   public final Setting<Boolean> raytrace;
   public final Setting<Boolean> nCP;
   public final Setting<Boolean> swapMultiTask;
   public final Setting<Integer> ticks;
   public final Setting<Integer> swapDelay;
   public final Setting<Float> negate;
   public final Setting<Boolean> doubleValue;
   public final Setting<Float> damage;
   public final Setting<Double> range;
   public final Setting<Boolean> grim;
   public final Setting<Boolean> doubleAir;
   public final Setting<Boolean> cancel;
   public final Setting<SpeedmineModule.Mode> mode;
   public final Setting<Boolean> strictDirection;
   public final Setting<Long> mineDelay;
   public final Setting<Boolean> fade;
   public final SimpleBlockBreaker simpleBlockBreaker;
   public final Setting<Boolean> custom;
   public final Setting<Boolean> always;
   public final Setting<Long> fadeTime;
   public final Setting<Long> reset;
   public final Setting<Color> line;
   public final Normal normal;
   public final Setting<Boolean> end;
   public final Setting<Long> correctionTime;
   public final Normal normal2;
   public final Setting<Color> box;
   public final Setting<Boolean> correction;
   public final PacketBlockBreaker packetBlockBreaker;
   public final Setting<Boolean> animate;
   public final Setting<Boolean> render;
   public final Setting<Boolean> flow;
   public final Map<BlockPos, SpeedmineModule.AirUpdate> blockPosMap;
   public final Setting<Integer> doubleTicks;
   public boolean flag;
   public final Normal normal3;
   public final Setting<Boolean> renderReset;
   public final Setting<Boolean> swing;
   public final Setting<Boolean> start;
   public final Setting<Float> width;
   private static Object[] phobos$g1806f1350;

   public void handleBlockPos(BlockPos blockPos, ClientWorld clientWorld, ClientPlayerEntity clientPlayerEntity) {
      this.handleBlockPos4(blockPos, clientWorld, clientPlayerEntity, positionType -> {});
   }

   public void handleBlockPos2(BlockPos blockPos, Direction direction, ClientWorld clientWorld, ClientPlayerEntity clientPlayerEntity, boolean flag) {
      Setting var6 = this.doubleValue;
      Object var7 = var6.getValue();
      boolean var8 = var7 instanceof Boolean;
      if (var7 != null && !var8) {
         throw new ClassCastException("CHECKCAST failed");
      }

      label65: {
         boolean var9 = (Boolean)var7;
         if (var9) {
            boolean var10 = this.check4();
            if (var10) {
               label60: {
                  boolean var11 = this.check();
                  Action var17;
                  if (!var11) {
                     Action var12 = Action.STOP_DESTROY_BLOCK;
                     this.handleClientPlayerEntity2(clientPlayerEntity, clientWorld, blockPos, direction, var12);
                     Action var13 = Action.ABORT_DESTROY_BLOCK;
                     this.handleClientPlayerEntity2(clientPlayerEntity, clientWorld, blockPos, direction, var13);
                     Action var14 = Action.START_DESTROY_BLOCK;
                     this.handleClientPlayerEntity2(clientPlayerEntity, clientWorld, blockPos, direction, var14);
                     Action var15 = Action.STOP_DESTROY_BLOCK;
                     this.handleClientPlayerEntity2(clientPlayerEntity, clientWorld, blockPos, direction, var15);
                     Action var16 = Action.ABORT_DESTROY_BLOCK;
                     var17 = var16;
                  } else {
                     if (flag) {
                        PacketBlockBreaker var27 = this.packetBlockBreaker;
                        BlockPos var28 = var27.getBlockPos();
                        PacketBlockBreaker var29 = this.packetBlockBreaker;
                        Direction var30 = var29.getDirection();
                        Action var31 = Action.ABORT_DESTROY_BLOCK;
                        this.handleClientPlayerEntity2(clientPlayerEntity, clientWorld, var28, var30, var31);
                        ClientPlayNetworkHandler var32 = clientPlayerEntity.networkHandler;
                        Hand var34 = Hand.MAIN_HAND;
                        HandSwingC2SPacket var33 = new HandSwingC2SPacket(var34);
                        var32.sendPacket(var33);
                     }

                     Action var35 = Action.STOP_DESTROY_BLOCK;
                     this.handleClientPlayerEntity2(clientPlayerEntity, clientWorld, blockPos, direction, var35);
                     Action var36 = Action.START_DESTROY_BLOCK;
                     this.handleClientPlayerEntity2(clientPlayerEntity, clientWorld, blockPos, direction, var36);
                     Action var37 = Action.STOP_DESTROY_BLOCK;
                     this.handleClientPlayerEntity2(clientPlayerEntity, clientWorld, blockPos, direction, var37);
                     ClientPlayNetworkHandler var38 = clientPlayerEntity.networkHandler;
                     Hand var40 = Hand.MAIN_HAND;
                     HandSwingC2SPacket var39 = new HandSwingC2SPacket(var40);
                     var38.sendPacket(var39);
                     ClientPlayNetworkHandler var41 = clientPlayerEntity.networkHandler;
                     Hand var43 = Hand.MAIN_HAND;
                     HandSwingC2SPacket var42 = new HandSwingC2SPacket(var43);
                     var41.sendPacket(var42);
                     ClientPlayNetworkHandler var44 = clientPlayerEntity.networkHandler;
                     Hand var46 = Hand.MAIN_HAND;
                     HandSwingC2SPacket var45 = new HandSwingC2SPacket(var46);
                     var44.sendPacket(var45);
                     Action var47 = Action.ABORT_DESTROY_BLOCK;
                     this.handleClientPlayerEntity2(clientPlayerEntity, clientWorld, blockPos, direction, var47);
                     if (!flag) {
                        break label60;
                     }

                     Action var48 = Action.ABORT_DESTROY_BLOCK;
                     var17 = var48;
                  }

                  this.handleClientPlayerEntity2(clientPlayerEntity, clientWorld, blockPos, direction, var17);
               }

               Setting var18 = this.swing;
               Object var19 = var18.getValue();
               boolean var20 = var19 instanceof Boolean;
               if (var19 != null && !var20) {
                  throw new ClassCastException("CHECKCAST failed");
               }

               boolean var49 = (Boolean)var19;
               if (var49) {
                  Setting var50 = this.start;
                  Object var51 = var50.getValue();
                  boolean var52 = var51 instanceof Boolean;
                  if (var51 != null && !var52) {
                     throw new ClassCastException("CHECKCAST failed");
                  }

                  boolean var61 = (Boolean)var51;
                  if (var61) {
                     ClientPlayNetworkHandler var62 = clientPlayerEntity.networkHandler;
                     Hand var64 = Hand.MAIN_HAND;
                     HandSwingC2SPacket var63 = new HandSwingC2SPacket(var64);
                     var62.sendPacket(var63);
                     ClientPlayNetworkHandler var65 = clientPlayerEntity.networkHandler;
                     Hand var67 = Hand.MAIN_HAND;
                     HandSwingC2SPacket var66 = new HandSwingC2SPacket(var67);
                     var65.sendPacket(var66);
                     ClientPlayNetworkHandler var68 = clientPlayerEntity.networkHandler;
                     Hand var70 = Hand.MAIN_HAND;
                     HandSwingC2SPacket var69 = new HandSwingC2SPacket(var70);
                     var68.sendPacket(var69);
                  }
               }
               break label65;
            }
         }

         ClientPlayNetworkHandler var21 = clientPlayerEntity.networkHandler;
         Action var23 = Action.START_DESTROY_BLOCK;
         PlayerActionC2SPacket var22 = new PlayerActionC2SPacket(var23, blockPos, direction);
         var21.sendPacket(var22);
      }

      Setting var24 = this.cancel;
      Object var25 = var24.getValue();
      boolean var26 = var25 instanceof Boolean;
      if (var25 != null && !var26) {
         throw new ClassCastException("CHECKCAST failed");
      }

      boolean var53 = (Boolean)var25;
      if (var53) {
         ClientPlayNetworkHandler var54 = clientPlayerEntity.networkHandler;
         Action var56 = Action.ABORT_DESTROY_BLOCK;
         BlockPos var57 = blockPos.up(500);
         PlayerActionC2SPacket var55 = new PlayerActionC2SPacket(var56, var57, direction);
         var54.sendPacket(var55);
      }

      Setting var58 = this.swing;
      Object var59 = var58.getValue();
      boolean var60 = var59 instanceof Boolean;
      if (var59 != null && !var60) {
         throw new ClassCastException("CHECKCAST failed");
      }

      boolean var71 = (Boolean)var59;
      if (var71) {
         Setting var72 = this.start;
         Object var73 = var72.getValue();
         boolean var74 = var73 instanceof Boolean;
         if (var73 != null && !var74) {
            throw new ClassCastException("CHECKCAST failed");
         }

         boolean var80 = (Boolean)var73;
         if (var80) {
            ClientPlayNetworkHandler var81 = clientPlayerEntity.networkHandler;
            Hand var83 = Hand.MAIN_HAND;
            HandSwingC2SPacket var82 = new HandSwingC2SPacket(var83);
            var81.sendPacket(var82);
         }
      }

      PacketBlockBreaker var75 = this.packetBlockBreaker;
      var75.handleFloat2((float)0.0F);
      PacketBlockBreaker var76 = this.packetBlockBreaker;
      var76.setFlag2(true & true);
      PacketBlockBreaker var77 = this.packetBlockBreaker;
      var77.setFlag(true & true);
      PacketBlockBreaker var78 = this.packetBlockBreaker;
      Normal var79 = var78.getNormal();
      var79.run();
   }

   public void handleDoubleValue(org.phobos.core.animation.FloatAnimator.Double doubleValue, boolean flag) {
      doubleValue.setDoubleValue2(0.0);
      if (flag || this.renderReset.getValue()) {
         doubleValue.setDoubleValue(0.0);
      }
   }

   public void handleBlockPos3(BlockPos blockPos, Direction direction, ClientWorld clientWorld, ClientPlayerEntity clientPlayerEntity) {
      this.handleBlockPos5(blockPos, direction, clientWorld, clientPlayerEntity, positionType -> {});
   }

   public float computeDouble(double d) {
      float f = 0.0F;
      float f2 = this.damage.getValue();
      return (float)((d - f) / (f2 - f));
   }

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, BlockPos blockPos, boolean flag) {
      if (blockPos == null) {
         return false;
      } else {
         SpeedmineModule.BreakEvent breakEvent = new SpeedmineModule.BreakEvent(blockPos, flag);
         this.phobosClient.getEventBus().handleObject2(breakEvent);
         if (breakEvent.flag2 && !flag) {
            this.packetBlockBreaker.handleClientPlayerEntity(clientPlayerEntity);
            this.packetBlockBreaker.run2();
            return false;
         } else {
            return !breakEvent.isCancelled();
         }
      }
   }

   public Direction resolveDirection(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, BlockPos blockPos) {
      if (!this.raytrace.getValue() && this.strictDirection.getValue()) {
         return Direction.DOWN;
      }

      DirectionResolver directionResolver = this.grim.getValue() ? ExactDirectionResolver.exactDirectionResolver : ShapeDirectionResolver.shapeDirectionResolver;

      for (Direction direction : Direction.values()) {
         if (directionResolver.testBlockPos(blockPos, direction, clientWorld, clientPlayerEntity)
            && this.testClientPlayerEntity3(clientPlayerEntity, clientWorld, blockPos, direction)) {
            return direction;
         }
      }

      return null;
   }

   public void clear() {
      this.packetBlockBreaker.run2();
      this.simpleBlockBreaker.run2();
   }

   public boolean testClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, BlockPos blockPos) {
      return this.resolveDirection(clientPlayerEntity, clientWorld, blockPos) != null;
   }

   public void handleBlockPos4(
      BlockPos blockPos, ClientWorld clientWorld, ClientPlayerEntity clientPlayerEntity, Consumer<SpeedmineModule.PositionType> consumer
   ) {
      Direction direction = this.resolveDirection(clientPlayerEntity, clientWorld, blockPos);
      if (direction != null) {
         if (!this.doubleValue.getValue() || !this.packetBlockBreaker.testBlockPos(blockPos) && !this.simpleBlockBreaker.testBlockPos(blockPos)) {
            this.handleBlockPos5(blockPos, direction, clientWorld, clientPlayerEntity, consumer);
         }
      }
   }

   public void handleBlockBreakContext(BlockBreakContext blockBreakContext, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      if (this.mine.getValue()) {
         this.setFlag(true);
         this.normal.run();
         blockBreakContext.setCancelled(true);
         this.handleBlockPos3(blockBreakContext.getBlockPos(), blockBreakContext.getDirection(), clientWorld, clientPlayerEntity);
      }
   }

   public boolean check() {
      return this.n2b2t.testObject2(SpeedmineModule._2B2T.Current);
   }

   public double[] values(Box box) {
      return new double[]{(box.maxX - box.minX) / 2.0, (box.maxY - box.minY) / 2.0, (box.maxZ - box.minZ) / 2.0};
   }

   public boolean testBlockPos(BlockPos blockPos, BlockState blockState, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      if (!clientWorld.isInBuildLimit(blockPos)) {
         return false;
      } else {
         return !clientPlayerEntity.isCreative() && blockState.getHardness(clientWorld, blockPos) < 0.0F && !clientPlayerEntity.isSpectator()
            ? false
            : !blockState.getOutlineShape(clientWorld, blockPos).isEmpty();
      }
   }

   public boolean testBlockPos2(BlockPos blockPos, BlockState blockState, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      if (blockState.isAir()) {
         return true;
      } else if (!clientWorld.isInBuildLimit(blockPos)) {
         return false;
      } else {
         return !clientPlayerEntity.isCreative() && blockState.getHardness(clientWorld, blockPos) < 0.0F && !clientPlayerEntity.isSpectator()
            ? false
            : !blockState.getOutlineShape(clientWorld, blockPos).isEmpty();
      }
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      this.packetBlockBreaker.handleClientPlayerEntity(clientPlayerEntity);
      this.packetBlockBreaker.run2();
   }

   public boolean testBlockPos3(BlockPos blockPos) {
      return this.mode.getValue() != SpeedmineModule.Mode.Normal && this.packetBlockBreaker.testBlockPos(blockPos);
   }

   public void handleClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, BlockPos blockPos, Direction direction, Action action) {
      if (action == Action.ABORT_DESTROY_BLOCK) {
         clientPlayerEntity.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.ABORT_DESTROY_BLOCK, blockPos, direction));
      } else {
         TickUtils.handleClientWorld(
            clientWorld, integer -> clientPlayerEntity.networkHandler.sendPacket(new PlayerActionC2SPacket(action, blockPos, direction, integer))
         );
      }
   }

   public boolean testClientPlayerEntity3(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, BlockPos blockPos, Direction direction) {
      return !this.raytrace.getValue()
         ? true
         : CrystalUtils.computeClientPlayerEntity2(clientPlayerEntity, clientWorld, blockPos, RotationUtils.resolveVec3d(blockPos, direction, blockPos.toCenterPos()))
            <= 1;
   }

   public void run5() {
      Builder var1 = this.createBuilder();
      Setting var2 = this.mine;
      Builder var3 = var1.resolveBuilder6(var2);
      SettingGroup[] var4 = new SettingGroup[1];
      Setting var5 = this.strictDirection;
      Setting[] var6 = new Setting[1];
      Setting var7 = this.grim;
      var6[0] = var7;
      SettingGroup var8 = this.resolveSettingGroup(var5, var6);
      var4[0] = var8;
      Builder var9 = var3.resolveBuilder5(var4);
      Setting[] var10 = new Setting[12];
      Setting var11 = this.mode;
      var10[0] = var11;
      Setting var12 = this.abort;
      var10[1] = var12;
      Setting var13 = this.fast;
      var10[2] = var13;
      Setting var14 = this.mineDelay;
      var10[3] = var14;
      Setting var15 = this.raytrace;
      var10[4] = var15;
      Setting var16 = this.nCP;
      var10[5] = var16;
      Setting var17 = this.damage;
      var10[6] = var17;
      Setting var18 = this.ticks;
      var10[7] = var18;
      Setting var19 = this.air;
      var10[8] = var19;
      Setting var20 = this.cancel;
      var10[9] = var20;
      Setting var21 = this.range;
      var10[10] = var21;
      Setting var22 = this.n2b2t;
      var10[11] = var22;
      Builder var23 = var9.resolveBuilder(var10);
      var23.getSettingSubsection();
      Builder var24 = this.createBuilder();
      Setting var25 = this.render;
      Builder var26 = var24.resolveBuilder6(var25);
      SettingGroup[] var27 = new SettingGroup[2];
      Setting var28 = this.custom;
      Setting[] var29 = new Setting[2];
      Setting var30 = this.box;
      var29[0] = var30;
      Setting var31 = this.line;
      var29[1] = var31;
      SettingGroup var32 = this.resolveSettingGroup(var28, var29);
      var27[0] = var32;
      Setting var33 = this.fade;
      Setting[] var34 = new Setting[1];
      Setting var35 = this.fadeTime;
      var34[0] = var35;
      SettingGroup var36 = this.resolveSettingGroup(var33, var34);
      var27[1] = var36;
      Builder var37 = var26.resolveBuilder5(var27);
      Setting[] var38 = new Setting[5];
      Setting var39 = this.animate;
      var38[0] = var39;
      Setting var40 = this.renderReset;
      var38[1] = var40;
      Setting var41 = this.always;
      var38[2] = var41;
      Setting var42 = this.flow;
      var38[3] = var42;
      Setting var43 = this.width;
      var38[4] = var43;
      Builder var44 = var37.resolveBuilder(var38);
      var44.getSettingSubsection();
      Setting var45 = this.swap;
      Setting[] var46 = new Setting[4];
      Setting var47 = this.swapMultiTask;
      var46[0] = var47;
      Setting var48 = this.booleanValueSetting;
      var46[1] = var48;
      Setting var49 = this.swapDelay;
      var46[2] = var49;
      Setting var50 = this.swapType;
      var46[3] = var50;
      this.resolveSettingGroup(var45, var46);
      Setting var51 = this.rotate;
      Setting[] var52 = new Setting[1];
      Setting var53 = this.rotateType;
      var52[0] = var53;
      this.resolveSettingGroup(var51, var52);
      Setting var54 = this.doubleValue;
      Setting[] var55 = new Setting[4];
      Setting var56 = this.negate;
      var55[0] = var56;
      Setting var57 = this.doubleAir;
      var55[1] = var57;
      Setting var58 = this.doubleTicks;
      var55[2] = var58;
      Setting var59 = this.reset;
      var55[3] = var59;
      this.resolveSettingGroup(var54, var55);
      Setting var60 = this.swing;
      Setting[] var61 = new Setting[2];
      Setting var62 = this.start;
      var61[0] = var62;
      Setting var63 = this.end;
      var61[1] = var63;
      this.resolveSettingGroup(var60, var61);
      Setting var64 = this.correction;
      Setting[] var65 = new Setting[1];
      Setting var66 = this.correctionTime;
      var65[0] = var66;
      SettingGroup var67 = this.resolveSettingGroup(var64, var65);
      Visibility var84 = this::check6;
      var67.resolveSettingGroup5(var84);
   }

   public SpeedmineModule(PhobosClient phobosClient) {
      super(phobosClient, "Speedmine", Categories.player, "Mines blocks really fast");
      this.swap = this.resolveSetting2(new BooleanSetting("Swap", false));
      this.swapMultiTask = this.resolveSetting2(new BooleanSetting("SwapMultiTask"));
      this.booleanValueSetting = this.resolveSetting2(new BooleanSetting("1.21").resolveSetting7("1.21 delayed switch"));
      this.swapDelay = this.resolveSetting2(new NumberSetting<>("SwapDelay", 2, 1, 4).resolveSetting(this.booleanValueSetting));
      this.swapType = this.resolveSetting2(new EnumSetting<>("SwapType", Silent.Swap).resolveSetting3(() -> !this.booleanValueSetting.getValue()));
      this.strictDirection = this.resolveSetting2(new BooleanSetting("StrictDirection", false).resolveSetting7("Strict direction"));
      this.grim = this.resolveSetting2(new BooleanSetting("Grim", false).resolveSetting7("Grim"));
      this.raytrace = this.resolveSetting2(new BooleanSetting("Raytrace"));
      this.mine = this.resolveSetting2(new BooleanSetting("Mine", false));
      this.mode = this.resolveSetting2(new EnumSetting<>("Mode", SpeedmineModule.Mode.Fast));
      this.abort = this.resolveSetting2(new BooleanSetting("Abort", true).resolveSetting4(this.mode, SpeedmineModule.Mode.Fast));
      this.fast = this.resolveSetting2(
         new BooleanSetting("Fast").resolveSetting7("Tries to break even if it's air").resolveSetting4(this.mode, SpeedmineModule.Mode.Instant)
      );
      this.mineDelay = this.resolveSetting2(new LongNumberSetting("MineDelay", 0L, 0L, 500L).resolveSetting4(this.mode, SpeedmineModule.Mode.Instant));
      this.nCP = this.resolveSetting2(new BooleanSetting("NCP", false).resolveSetting7("NCP Mode"));
      this.damage = this.resolveSetting2(new NumberSetting<>("Damage", 1.0F, 0.7F, 1.0F).resolveSetting7("Block damage"));
      this.ticks = this.resolveSetting2(new NumberSetting<>("Ticks", 3, 0, 20).resolveSetting7("Additional mining ticks"));
      this.air = this.resolveSetting2(new BooleanSetting("Air", false).resolveSetting7("Simulates breaking"));
      this.cancel = this.resolveSetting2(new BooleanSetting("Cancel", false).resolveSetting7("Sends a cancel packet"));
      this.range = this.resolveSetting2(new NumberSetting<>("Range", 5.25, 1.0, 6.0).resolveSetting7("Maximum range"));
      this.n2b2t = this.resolveSetting2(
         new EnumSetting<>("2b2t", SpeedmineModule._2B2T.None).resolveSetting7("Extra packets = bypass (Requires Swing -> End)")
      );
      this.doubleValue = this.resolveSetting2(new BooleanSetting("Double", false));
      this.negate = this.resolveSetting2(new NumberSetting<>("Negate", 0.1F, 0.0F, 0.4F));
      this.doubleAir = this.resolveSetting2(new BooleanSetting("DoubleAir", false));
      this.doubleTicks = this.resolveSetting2(new NumberSetting<>("DoubleTicks", 2, 1, 6));
      this.reset = this.resolveSetting2(new LongNumberSetting("Reset", 0L, 0L, 500L, true));
      this.swing = this.resolveSetting2(new BooleanSetting("Swing", false));
      this.start = this.resolveSetting2(new BooleanSetting("Start", true));
      this.end = this.resolveSetting2(new BooleanSetting("End", true));
      this.render = this.resolveSetting2(new BooleanSetting("Render", false));
      this.renderReset = this.resolveSetting2(new BooleanSetting("RenderReset", false));
      this.animate = this.resolveSetting2(new BooleanSetting("Animate", false));
      this.always = this.resolveSetting2(new BooleanSetting("Always", false));
      this.flow = this.resolveSetting2(new BooleanSetting("Flow", false));
      this.custom = this.resolveSetting2(new BooleanSetting("Custom", false));
      this.box = this.resolveSetting2(new ColorSetting("Box", new Color(255, 255, 255, 100)));
      this.line = this.resolveSetting2(new ColorSetting("Line", new Color(255, 255, 255, 255)));
      this.width = this.resolveSetting2(new NumberSetting<>("Width", 1.0F, 0.0F, 2.0F));
      this.fade = this.resolveSetting2(new BooleanSetting("Fade"));
      this.fadeTime = this.resolveSetting2(new LongNumberSetting("FadeTime", 250L, 50L, 1000L));
      this.correction = this.resolveSetting2(new BooleanSetting("Correction").resolveSetting7("Corrects invalid blocks"));
      this.correctionTime = this.resolveSetting2(new LongNumberSetting("CorrectionTime", 250L, 50L, 1000L));
      this.packetBlockBreaker = new PacketBlockBreaker(this.phobosClient, this);
      this.simpleBlockBreaker = new SimpleBlockBreaker(this.phobosClient, this);
      this.normal2 = new Normal();
      this.normal3 = new Normal();
      this.flag = false;
      this.normal = new Normal();
      this.blockPosMap = new ConcurrentHashMap<>();
      speedmineModule = this;
      this.run5();
      this.handleListener(
         new SafeListener<RenderContext>() {
            public void handleObject(
               RenderContext renderContext,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (SpeedmineModule.this.render.getValue()) {
                  SpeedmineModule.this.packetBlockBreaker.handleClientWorld(clientWorld);
                  SpeedmineModule.this.simpleBlockBreaker.handleClientWorld(clientWorld);
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<FinishBreakEvent>() {
            public void handleObject(
               FinishBreakEvent finishBreakEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!clientPlayerEntity.isCreative() && !clientPlayerEntity.isSpectator()) {
                  SpeedmineModule.this.handleBlockBreakContext(finishBreakEvent, clientPlayerEntity, clientWorld);
               } else {
                  SpeedmineModule.this.clear();
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<PlayerTickEvent>(2) {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               SpeedmineModule.this.blockPosMap.entrySet().removeIf(entry -> {
                  BlockPos blockPos = entry.getKey();
                  SpeedmineModule.AirUpdate airUpdate = entry.getValue();
                  if (airUpdate.getLongValue() + SpeedmineModule.this.correctionTime.getValue() < System.currentTimeMillis()) {
                     clientWorld.setBlockState(blockPos, airUpdate.getBlockState(), 8);
                     SpeedmineModule.this.packetBlockBreaker.handleClientPlayerEntity9(clientPlayerEntity, blockPos);
                     return true;
                  } else {
                     return false;
                  }
               });
               if (!clientPlayerEntity.isCreative() && !clientPlayerEntity.isSpectator()) {
                  SpeedmineModule.this.packetBlockBreaker.handleClientPlayerEntity4(clientPlayerEntity, clientWorld, clientPlayerInteractionManager, false);
                  SpeedmineModule.this.simpleBlockBreaker.handleClientPlayerEntity4(clientPlayerEntity, clientWorld, clientPlayerInteractionManager, false);
               } else {
                  SpeedmineModule.this.clear();
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<UnusedEvent>() {
            public void handleObject(
               UnusedEvent unusedEvent, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!clientPlayerEntity.isCreative() && !clientPlayerEntity.isSpectator()) {
                  SpeedmineModule.this.packetBlockBreaker.handleClientPlayerEntity4(clientPlayerEntity, clientWorld, clientPlayerInteractionManager, true);
                  SpeedmineModule.this.simpleBlockBreaker.handleClientPlayerEntity4(clientPlayerEntity, clientWorld, clientPlayerInteractionManager, true);
               } else {
                  SpeedmineModule.this.clear();
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<StartBreakEvent>() {
            public void handleObject(
               StartBreakEvent startBreakEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!clientPlayerEntity.isCreative() && !clientPlayerEntity.isSpectator()) {
                  if (SpeedmineModule.this.packetBlockBreaker.check()
                     && !Objects.equals(SpeedmineModule.this.packetBlockBreaker.getBlockPos(), startBreakEvent.getBlockPos())
                     && !SpeedmineModule.this.testBlockPos(
                        SpeedmineModule.this.packetBlockBreaker.getBlockPos(),
                        clientWorld.getBlockState(SpeedmineModule.this.packetBlockBreaker.getBlockPos()),
                        clientPlayerEntity,
                        clientWorld
                     )) {
                     SpeedmineModule.this.handleBlockBreakContext(startBreakEvent, clientPlayerEntity, clientWorld);
                  }

                  startBreakEvent.setCancelled(true);
               } else {
                  SpeedmineModule.this.clear();
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<UpdateBreakEvent>() {
            public void handleObject(
               UpdateBreakEvent updateBreakEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!clientPlayerEntity.isCreative() && !clientPlayerEntity.isSpectator()) {
                  updateBreakEvent.setCancelled(true);
               } else {
                  SpeedmineModule.this.clear();
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<PostReceive<BlockUpdateS2CPacket>>() {
            public void handleObject(
               PostReceive<BlockUpdateS2CPacket> postReceive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               mc.submit(() -> {
                  if (!clientPlayerEntity.isCreative()) {
                     if (Objects.equals(postReceive.getPacket().getPos(), SpeedmineModule.this.packetBlockBreaker.getBlockPos())) {
                        SpeedmineModule.this.packetBlockBreaker.handleClientPlayerEntity4(clientPlayerEntity, clientWorld, clientPlayerInteractionManager, false);
                     }

                     if (Objects.equals(postReceive.getPacket().getPos(), SpeedmineModule.this.simpleBlockBreaker.getBlockPos())) {
                        SpeedmineModule.this.simpleBlockBreaker.handleClientPlayerEntity4(clientPlayerEntity, clientWorld, clientPlayerInteractionManager, false);
                     }

                     if (postReceive.getPacket().getState().isAir()) {
                        SpeedmineModule.this.blockPosMap.remove(postReceive.getPacket().getPos());
                     }
                  }
               });
            }
         }
      );
      this.handleListener(
         new SafeListener<PostSend<UpdateSelectedSlotC2SPacket>>() {
            public void handleObject(
               PostSend<UpdateSelectedSlotC2SPacket> postSend,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (SpeedmineModule.this.nCP.getValue()) {
                  mc.submit(() -> {
                     if (!clientPlayerEntity.isCreative()) {
                        SpeedmineModule.this.packetBlockBreaker.handleClientPlayerEntity5(clientPlayerEntity);
                     }
                  });
               }
            }
         }
      );
      ModuleListenerUtils.handleListenerHolder4(this, this::clear);
   }

   public void handleBlockPos5(
      BlockPos blockPos, Direction direction, ClientWorld clientWorld, ClientPlayerEntity clientPlayerEntity, Consumer<SpeedmineModule.PositionType> consumer
   ) {
      boolean var6 = clientPlayerEntity.isCreative();
      if (!var6) {
         boolean var7 = clientPlayerEntity.isSpectator();
         if (!var7) {
            BlockState var8 = clientWorld.getBlockState(blockPos);
            boolean var9 = this.testBlockPos(blockPos, var8, clientPlayerEntity, clientWorld);
            if (var9) {
               SimpleBlockBreaker var10 = this.simpleBlockBreaker;
               boolean var11 = var10.testBlockPos(blockPos);
               if (!var11) {
                  MutableBoolean var13;
                  PacketBlockBreaker var12 = this.packetBlockBreaker;
                  var12.setFlag3(false & true);
                  var13 = new MutableBoolean();
                  PacketBlockBreaker var14 = this.packetBlockBreaker;
                  boolean var15 = var14.check();
                  label41:
                  if (var15) {
                     PacketBlockBreaker var16 = this.packetBlockBreaker;
                     Direction var17 = var16.getDirection();
                     Setting var18 = this.doubleValue;
                     Object var19 = var18.getValue();
                     boolean var20 = var19 instanceof Boolean;
                     if (var19 != null && !var20) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     boolean var30 = (Boolean)var19;
                     if (var30) {
                        PacketBlockBreaker var31 = this.packetBlockBreaker;
                        boolean var32 = var31.testBlockPos(blockPos);
                        if (!var32 && var17 != null) {
                           SimpleBlockBreaker var33 = this.simpleBlockBreaker;
                           boolean var34 = var33.check();
                           if (!var34) {
                              boolean var35 = this.check4();
                              if (!var35) {
                                 ClientPlayNetworkHandler var36 = clientPlayerEntity.networkHandler;
                                 Action var38 = Action.STOP_DESTROY_BLOCK;
                                 PacketBlockBreaker var39 = this.packetBlockBreaker;
                                 BlockPos var40 = var39.getBlockPos();
                                 PlayerActionC2SPacket var37 = new PlayerActionC2SPacket(var38, var40, var17);
                                 var36.sendPacket(var37);
                                 ClientPlayNetworkHandler var41 = clientPlayerEntity.networkHandler;
                                 Action var43 = Action.START_DESTROY_BLOCK;
                                 PacketBlockBreaker var44 = this.packetBlockBreaker;
                                 BlockPos var45 = var44.getBlockPos();
                                 PlayerActionC2SPacket var42 = new PlayerActionC2SPacket(var43, var45, var17);
                                 var41.sendPacket(var42);
                                 ClientPlayNetworkHandler var46 = clientPlayerEntity.networkHandler;
                                 Action var48 = Action.STOP_DESTROY_BLOCK;
                                 PacketBlockBreaker var49 = this.packetBlockBreaker;
                                 BlockPos var50 = var49.getBlockPos();
                                 PlayerActionC2SPacket var47 = new PlayerActionC2SPacket(var48, var50, var17);
                                 var46.sendPacket(var47);
                              }

                              SimpleBlockBreaker var51 = this.simpleBlockBreaker;
                              boolean[] var52 = new boolean[0];
                              var51.handleFlag(true & true, var52);
                              SimpleBlockBreaker var53 = this.simpleBlockBreaker;
                              PacketBlockBreaker var54 = this.packetBlockBreaker;
                              BlockPos var55 = var54.getBlockPos();
                              var53.setBlockPos(var55);
                              SimpleBlockBreaker var56 = this.simpleBlockBreaker;
                              PacketBlockBreaker var57 = this.packetBlockBreaker;
                              float var58 = var57.getFloatValue3();
                              var56.handleFloat2((float)Float.intBitsToFloat(Float.floatToRawIntBits(var58)));
                              SimpleBlockBreaker var59 = this.simpleBlockBreaker;
                              PacketBlockBreaker var60 = this.packetBlockBreaker;
                              BlockState var61 = var60.getBlockState2();
                              var59.handleBlockState2(var61);
                              SimpleBlockBreaker var63 = this.simpleBlockBreaker;
                              BlockPos var64 = var63.getBlockPos();
                              SpeedmineModule.Type var65 = SpeedmineModule.Type.Double;
                              SpeedmineModule.PositionType var62 = new SpeedmineModule.PositionType(var64, var65);
                              consumer.accept(var62);
                              var13.setTrue();
                              break label41;
                           }
                        }
                     }

                     ClientPlayNetworkHandler var66 = clientPlayerEntity.networkHandler;
                     Action var68 = Action.ABORT_DESTROY_BLOCK;
                     PacketBlockBreaker var69 = this.packetBlockBreaker;
                     BlockPos var70 = var69.getBlockPos();
                     Direction var71 = Direction.DOWN;
                     Object var72 = Objects.requireNonNullElse(var17, var71);
                     boolean var73 = var72 instanceof Direction;
                     if (var72 != null && !var73) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     PlayerActionC2SPacket var67 = new PlayerActionC2SPacket(var68, var70, (Direction)var72);
                     var66.sendPacket(var67);
                     PacketBlockBreaker var74 = this.packetBlockBreaker;
                     boolean var75 = var74.testBlockPos(blockPos);
                     if (var75) {
                        PacketBlockBreaker var76 = this.packetBlockBreaker;
                        var76.run2();
                        return;
                     }
                  }

                  PacketBlockBreaker var21 = this.packetBlockBreaker;
                  boolean[] var22 = new boolean[0];
                  var21.handleFlag(false & true, var22);
                  boolean var23 = var13.booleanValue();
                  this.handleBlockPos2(blockPos, direction, clientWorld, clientPlayerEntity, var23);
                  PacketBlockBreaker var24 = this.packetBlockBreaker;
                  BlockPos var25 = blockPos.toImmutable();
                  var24.setBlockPos(var25);
                  PacketBlockBreaker var27 = this.packetBlockBreaker;
                  BlockPos var28 = var27.getBlockPos();
                  SpeedmineModule.Type var29 = SpeedmineModule.Type.Normal;
                  SpeedmineModule.PositionType var26 = new SpeedmineModule.PositionType(var28, var29);
                  consumer.accept(var26);
               }
            }
         }
      }
   }

   public void handleClientPlayerInteractionManager(ClientPlayerInteractionManager clientPlayerInteractionManager, BlockPos blockPos, BlockState blockState) {
      if (this.correction.getValue()) {
         this.blockPosMap.put(blockPos, new SpeedmineModule.AirUpdate(blockState, System.currentTimeMillis()));
      }

      clientPlayerInteractionManager.breakBlock(blockPos);
   }

   @Override
   public String nothing() {
      boolean flag = this.packetBlockBreaker.check() && this.simpleBlockBreaker.check();
      if (flag) {
         return String.format("%.1f %.1f", this.packetBlockBreaker.getFloatValue2(), this.simpleBlockBreaker.getFloatValue3());
      } else if (this.packetBlockBreaker.check()) {
         return String.valueOf(MathUtils.computeFloat5(this.packetBlockBreaker.getFloatValue2(), 1));
      } else {
         return this.simpleBlockBreaker.check() ? String.valueOf(MathUtils.computeFloat5(this.simpleBlockBreaker.getFloatValue3(), 1)) : null;
      }
   }

   public static boolean testBlockPos4(BlockPos blockPos) {
      return speedmineModule.packetBlockBreaker.testBlockPos(blockPos);
   }

   public static List<BlockPos> getList() {
      List<BlockPos> list = new ArrayList<>(2);
      if (speedmineModule.packetBlockBreaker.check()) {
         list.add(speedmineModule.packetBlockBreaker.getBlockPos());
      }

      if (speedmineModule.simpleBlockBreaker.check()) {
         list.add(speedmineModule.simpleBlockBreaker.getBlockPos());
      }

      return list;
   }

   public void run() {
      if (this.flag) {
         if (!this.packetBlockBreaker.check() || this.normal.testLong(5000L)) {
            this.flag = false;
         }
      }
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   @Override
   public void run4() {
      this.clear();
   }

   public static boolean testBlockPos5(BlockPos blockPos) {
      return speedmineModule.simpleBlockBreaker.testBlockPos(blockPos);
   }

   public static boolean check2() {
      return speedmineModule.check7() && speedmineModule.packetBlockBreaker.check() && speedmineModule.nCP.getValue();
   }

   public boolean isFlag() {
      return this.flag;
   }

   public PacketBlockBreaker getPacketBlockBreaker() {
      return this.packetBlockBreaker;
   }

   public static SpeedmineModule getSpeedmineModule() {
      return speedmineModule;
   }

   public boolean check3() {
      return this.n2b2t.testObject2(SpeedmineModule._2B2T.Old);
   }

   public Setting<Boolean> getDoubleValue() {
      return this.doubleValue;
   }

   public boolean check4() {
      return !this.n2b2t.testObject2(SpeedmineModule._2B2T.None);
   }

   public boolean check6() {
      return this.air.getValue() || this.doubleValue.getValue() && this.doubleAir.getValue();
   }

   public SimpleBlockBreaker getSimpleBlockBreaker() {
      return this.simpleBlockBreaker;
   }

   private static class AirUpdate {
      public final long longValue;
      public final BlockState blockState;

      public BlockState getBlockState() {
         return this.blockState;
      }

      public long getLongValue() {
         return this.longValue;
      }

      public AirUpdate(BlockState blockState, long l) {
         this.blockState = blockState;
         this.longValue = l;
      }
   }

   public static class BreakEvent extends CancellableEvent {
      public final BlockPos blockPos;
      public final boolean flag;
      public boolean flag2 = false;

      public BreakEvent(BlockPos blockPos, boolean flag) {
         this.blockPos = blockPos;
         this.flag = flag;
      }

      public boolean isFlag() {
         return this.flag;
      }

      public boolean isFlag2() {
         return this.flag2;
      }

      public BlockPos getBlockPos() {
         return this.blockPos;
      }
   }

   public enum Mode {
      Normal {
         @Override
         public void execute(ClientPlayerEntity player, ClientWorld world, BlockPos pos, Direction direction, PacketBlockBreaker position) {
            position.run2();
         }
      },
      Fast {
         @Override
         public void execute(ClientPlayerEntity player, ClientWorld world, BlockPos pos, Direction direction, PacketBlockBreaker position) {
            player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.ABORT_DESTROY_BLOCK, pos.offset(direction), direction.getOpposite()));
            player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.START_DESTROY_BLOCK, pos, direction));
            player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, pos, direction));
            position.setFlag2(false);
            position.getNormal().run();
            position.handleFloat2(0.0F);
         }
      },
      Instant {};

      public void execute(ClientPlayerEntity player, ClientWorld world, BlockPos pos, Direction direction, PacketBlockBreaker position) {
      }
   }

   public static class PositionType {
      public final BlockPos blockPos;
      public final SpeedmineModule.Type type;

      public SpeedmineModule.Type getType() {
         return this.type;
      }

      public PositionType(BlockPos blockPos, SpeedmineModule.Type type) {
         this.blockPos = blockPos;
         this.type = type;
      }

      public BlockPos getBlockPos() {
         return this.blockPos;
      }
   }

   public enum Rotate {
      Normal,
      Packet,
      Silent;
   }

   public enum Type {
      Normal,
      Double;
   }

   public enum _2B2T {
      None,
      Current,
      Old;
   }
}
