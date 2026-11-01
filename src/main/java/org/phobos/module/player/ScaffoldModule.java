package org.phobos.module.player;

import java.awt.Color;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import net.minecraft.block.Block;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.CollisionView;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.TextProvider;
import org.phobos.api.Visibility;
import org.phobos.api.TickGate;
import org.phobos.api.AreaScanner;
import org.phobos.api.TimeGate;
import org.phobos.api.NumberRange;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.MutableNumberSetting;
import org.phobos.core.setting.BindSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.placement.PositionPoolBuilder;
import org.phobos.core.animation.AnimatedVec3d;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.core.render.RenderContext;
import org.phobos.core.rotation.RotationSmoother;
import org.phobos.core.player.ScaffoldFlags;
import org.phobos.core.movement.MovementModel;
import org.phobos.core.movement.GroundMovementModel;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.tick.TimerManager;
import org.phobos.core.inventory.SilentHotbarManager;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.bind.KeyCombination;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.core.render.Highlights;
import org.phobos.core.module.Categories;
import org.phobos.core.math.BoxUtils;
import org.phobos.core.util.NullSafe;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.math.MathUtils;
import org.phobos.core.player.PlayerUtils;
import org.phobos.core.world.RaycastUtils;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.event.FindCrosshairTargetEvent;
import org.phobos.event.JumpEvent3;
import org.phobos.event.Listener;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent;
import org.phobos.event.TickEvent2;
import org.phobos.event.TickMovementEvent;
import org.phobos.mixin.net.interfaces.IPlayerMoveC2SPacket;
import org.phobos.module.render.FreeLookModule;
import org.phobos.module.player.SilentHotbarModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScaffoldModule
extends PlacementModule {
    public final Setting<NumberRange.Mutable<Float>> mutableSetting;
    public final Setting<NumberRange.Mutable<Integer>> jiggleDelay;
    public final Visibility visibility;
    public final Setting<Double> spread;
    public final Setting<Boolean> constant;
    public final Setting<Float> floatValueSetting2;
    public final Setting<Double> speed;
    public final Setting<Boolean> constantResync;
    public final Setting<Float> constantThreshold;
    public final Setting<Boolean> cursor;
    public static final Logger LOGGER2 = LoggerFactory.getLogger(ScaffoldModule.class);
    public static ScaffoldModule scaffoldModule;
    public final Setting<Mode> mode = this.resolveSetting2(new EnumSetting<Mode>("Mode", Mode.Plain));
    public final Setting<Float> cursorSpeed;
    public final Setting<Boolean> simulate;
    public final Setting<HitVectorV> hitVectorVSetting;
    public final Setting<Boolean> booleanValueSetting10;
    public final Setting<Boolean> instant;
    public final Setting<HitVectorH> hitVectorHSetting;
    public final Setting<Boolean> booleanValueSetting11;
    public final Setting<Double> cursorStep;
    public final Setting<Boolean> perfect;
    public final Setting<Boolean> jiggle;
    public final Setting<Boolean> constantHeavy;
    public final Setting<Integer> cycleTicks;
    public final Setting<Boolean> towerAlign;
    public final Setting<Boolean> autoJump;
    public final Setting<Float> towerShift;
    public final Setting<Boolean> tower;
    public final Setting<Integer> autoJumpThreshold;
    public final Setting<Long> towerTimeout;
    public final Setting<Boolean> cycle;
    public final Setting<Boolean> soft;
    public final Setting<Float> towerHeight;
    public final Setting<Long> delay;
    public final Setting<Float> towerSpeed;
    public final Setting<Tower> towerMode;
    public final Setting<Boolean> hold;
    public final Setting<Boolean> booleanValueSetting12;
    public final Setting<Integer> extend;
    public final Setting<Boolean> towerFill;
    public final Setting<Long> clickDelay;
    public final Setting<StrafeMode> strafe;
    public final Setting<Float> towerOffset;
    public final Setting<Float> towerPower;
    public final Setting<Boolean> click;
    public final Setting<AutoSelectMode> autoSelect;
    public final Setting<Integer> clickIterations;
    public float[] floatArray;
    public boolean flag;
    public final RotationSmoother rotationSmoother;
    public final Setting<Boolean> jumpY;
    public final TimeGate.Normal normal3;
    public final Setting<Boolean> sneak;
    public boolean flag2;
    public boolean flag3;
    public final TimeGate.Normal normal4;
    public final Setting<Boolean> jumpAuto;
    public final Setting<Boolean> impossible;
    public final Setting<Long> jumpDelay;
    public final TimeGate.Normal normal5;
    public final Setting<Sprint> sprint;
    public final Setting<Integer> radius;
    public final TimeGate.Normal normal6;
    public final Setting<Integer> sneakTick;
    public final TimeGate.Normal normal7;
    public final Setting<KeyCombination> holdY;
    public final Setting<JumpMode> jumpMode;
    public final Setting<Boolean> jump;
    public float[] floatArray2;
    public final ScaffoldingCursor scaffoldingCursor;
    public final Setting<Boolean> look;
    public float floatValue;
    public float[] floatArray3;
    public float floatValue2;
    public boolean flag4;
    public int intValue;
    public boolean flag5;
    public float floatValue3;
    public int intValue2;
    public float floatValue4;
    public int intValue3;
    public int intValue4;
    public double doubleValue;
    public final TimeGate.Normal normal8;
    public boolean flag6;
    public final BiFunction<Block, ClientWorld, Boolean> blockBiFunction;
    public int intValue5 = 0;
    public final BiFunction<Slot, ClientWorld, Block> slotBiFunction;
    public final float[] floatArray4;
    public float floatValue5;
    public int intValue6 = 4096;
    public final TickGate.Normal normal9;

    @Override
    public String nothing() {
        if (ScaffoldModule.mc.world == null) {
            return null;
        }
        MutableObject mutableObject = new MutableObject((Object)0);
        this.phobosClient.getInventoryManager().handleConsumer2(itemSwitcher -> mutableObject.setValue((Object)this.computeItemSwitcher((ItemSwitcher)itemSwitcher, ScaffoldModule.mc.world)));
        int i = (Integer)mutableObject.getValue();
        return String.valueOf(i);
    }

    public double value() {
        return 8.0E-4 + Math.random() * 0.008;
    }

    public float[] values(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, float f) {
        this.flag = this.flag2 = Math.floor(clientPlayerEntity.getX() + Math.cos(Math.toRadians(f)) * 0.5) != Math.floor(clientPlayerEntity.getX()) || Math.floor(clientPlayerEntity.getZ() + Math.sin(Math.toRadians(f)) * 0.5) != Math.floor(clientPlayerEntity.getZ());
        if (clientPlayerEntity.isOnGround()) {
            BlockPos blockPos = clientPlayerEntity.getBlockPos().offset(Direction.fromRotation((double)f));
            BlockPos blockPos2 = blockPos.offset(Direction.fromRotation((double)f));
            boolean flag = clientWorld.getBlockState(clientPlayerEntity.getBlockPos().down()).isAir();
            boolean flag2 = clientWorld.getBlockState(blockPos2.down()).isAir();
            if (flag && flag2) {
                boolean bl = this.flag2 = !this.flag2;
            }
        }
        if (Float.isNaN(this.floatValue)) {
            this.floatValue = ((Float)this.mutableSetting.getValue().getNumber4()).floatValue();
        }
        float f2 = this.floatValue;
        return new float[]{f + (this.booleanValueSetting11.getValue() == false || this.flag2 ? f2 : -f2), this.floatValueSetting2.getValue().floatValue()};
    }

    public boolean testAbstractClientPlayerEntity2(AbstractClientPlayerEntity abstractClientPlayerEntity, BlockPos blockPos) {
        return this.tower.getValue() != false && this.towerFill.getValue() != false && ScaffoldModule.mc.options.jumpKey.isPressed() && BlockPosUtils.resolveSet2(abstractClientPlayerEntity.getBoundingBox(), abstractClientPlayerEntity.getY() - 0.5).contains(blockPos);
    }

    public float[] values2(float f) {
        return new float[]{f, this.floatValueSetting2.getValue().floatValue() - 0.1f};
    }

    @Override
    public void run4() {
        if (ScaffoldModule.mc.player != null && ScaffoldModule.mc.player.isOnGround()) {
            this.intValue6 = (int)Math.floor(ScaffoldModule.mc.player.getY() - 1.0);
        }
        if (this.simulate.getValue().booleanValue()) {
            this.phobosClient.getRotationManager().handleClientPlayerEntity(ScaffoldModule.mc.player, (float)(this.speed.getValue() / 10.0));
        }
    }

    public boolean check3() {
        return this.tower.getValue() != false && ScaffoldModule.mc.options.jumpKey.isPressed();
    }

    public boolean testAbstractClientPlayerEntity(AbstractClientPlayerEntity abstractClientPlayerEntity) {
        for (VoxelShape voxelShape : abstractClientPlayerEntity.clientWorld.getBlockCollisions((Entity)abstractClientPlayerEntity, abstractClientPlayerEntity.getBoundingBox().expand(0.5, 0.0, 0.5).offset(0.0, -1.05, 0.0))) {
            if (voxelShape.isEmpty()) continue;
            return true;
        }
        return false;
    }

    public ScaffoldModule(final PhobosClient phobosClient) {
        super(phobosClient, "Scaffold", "Places blocks below you", Categories.player, 7);
        this.visibility = () -> this.mode.getValue().equals((Object)Mode.God) || this.mode.getValue().equals((Object)Mode.Telly) || this.mode.getValue().equals((Object)Mode.Multi);
        this.simulate = this.resolveSetting2(new BooleanSetting("Simulate", false));
        this.speed = this.resolveSetting2(new NumberSetting<Double>("Speed", 10.0, 0.0, 10.0));
        this.spread = this.resolveSetting2(new NumberSetting<Double>("Spread", 0.0, 0.0, 5.0).resolveSetting3(() -> !this.speed.getValue().equals(10.0)));
        this.instant = this.resolveSetting2(new BooleanSetting("Instant"));
        this.perfect = this.resolveSetting2(new BooleanSetting("Perfect"));
        this.jiggle = this.resolveSetting2(new BooleanSetting("Jiggle"));
        this.jiggleDelay = this.resolveSetting2(new MutableNumberSetting<Integer>("JiggleDelay", 3, Integer.valueOf(1), Integer.valueOf(10), new String[0]));
        this.constant = this.resolveSetting2(new BooleanSetting("Constant"));
        this.constantThreshold = this.resolveSetting2(new NumberSetting<Float>("ConstantThreshold", Float.valueOf(80.0f), Float.valueOf(0.1f), Float.valueOf(360.0f)));
        this.constantHeavy = this.resolveSetting2(new BooleanSetting("ConstantHeavy").resolveSetting7("Uses expensive angle calculation bruteforce to find the best angle"));
        this.constantResync = this.resolveSetting2(new BooleanSetting("ConstantResync").resolveSetting7("Places instantly if the placement didn't require an additional rotation").resolveSetting4(this.getRotationSetting(), PlacementModule.Rotation.Normal));
        this.cursor = this.resolveSetting2(new BooleanSetting("Cursor").resolveSetting7("Moves hit-vector in some way to make it seem legit"));
        this.cursorSpeed = this.resolveSetting2(new NumberSetting<Float>("CursorSpeed", Float.valueOf(10.0f), Float.valueOf(0.1f), Float.valueOf(10.0f)).resolveNumberSetting("Max", Float.valueOf(10.0f)));
        this.cursorStep = this.resolveSetting2(new NumberSetting<Double>("CursorStep", 0.1, 0.01, 0.2));
        this.hitVectorHSetting = this.resolveSetting2(new EnumSetting<HitVectorH>("Vector", HitVectorH.Normal).resolveSetting3(this.mode.getValue()::isAuto));
        this.hitVectorVSetting = this.resolveSetting2(new EnumSetting<HitVectorV>("Vertical", HitVectorV.Default).resolveSetting3(this.mode.getValue()::isAuto));
        this.booleanValueSetting10 = this.resolveSetting2(new BooleanSetting("DBG-HV*").resolveSetting7("Debugs hit vector").getSetting2());
        this.floatValueSetting2 = this.resolveSetting2(new NumberSetting<Float>("Pitch", Float.valueOf(80.0f), Float.valueOf(0.0f), Float.valueOf(90.0f)).resolveSetting3(this.resolveVisibility2(this.mode, new Mode[]{Mode.God, Mode.Telly, Mode.Fast, Mode.Multi}).resolveVisibility(this.simulate::getValue)));
        this.mutableSetting = this.resolveSetting2(new MutableNumberSetting<Float>("Tilt", Float.valueOf(45.0f), Float.valueOf(0.0f), Float.valueOf(90.0f), new String[0]).resolveSetting3(this.resolveVisibility2(this.mode, new Mode[]{Mode.God, Mode.Telly, Mode.Fast, Mode.Multi}).resolveVisibility(this.simulate::getValue)));
        this.booleanValueSetting11 = this.resolveSetting2(new BooleanSetting("Sideways").resolveSetting3(this.resolveVisibility2(this.mode, new Mode[]{Mode.God, Mode.Telly, Mode.Fast, Mode.Multi}).resolveVisibility(this.simulate::getValue)));
        this.delay = this.resolveSetting2(new LongNumberSetting("Delay", 0L, 0L, 500L, true).resolveSetting3(this.visibility));
        this.click = this.resolveSetting2(new BooleanSetting("Click", "Makes it seem like you're clicking at the block", false));
        this.clickDelay = this.resolveSetting2(new LongNumberSetting("ClickDelay", 750L, 0L, 2000L, true));
        this.clickIterations = this.resolveSetting2(new NumberSetting<Integer>("ClickIterations", 2, 1, 5));
        this.cycle = this.resolveSetting2(new BooleanSetting("Cycle").resolveSetting7("Pauses for a tick for jump"));
        this.cycleTicks = this.resolveSetting2(new NumberSetting<Integer>("CycleTicks", 0, 0, 6));
        this.strafe = this.resolveSetting2(new EnumSetting<StrafeMode>("Strafe", StrafeMode.None).resolveSetting4(this.mode, Mode.Plain));
        this.autoSelect = this.resolveSetting2(new EnumSetting<AutoSelectMode>("AutoSelect", AutoSelectMode.None).resolveSetting4(this.mode, Mode.Plain));
        this.tower = this.resolveSetting2(new BooleanSetting("Tower").resolveSetting4(this.mode, Mode.Plain));
        this.towerFill = this.resolveSetting2(new BooleanSetting("TowerFill"));
        this.towerMode = this.resolveSetting2(new EnumSetting<Tower>("TowerMode", Tower.Motion));
        this.towerPower = this.resolveSetting2(new NumberSetting<Float>("TowerPower", Float.valueOf(0.42f), Float.valueOf(0.0f), Float.valueOf(1.0f)).resolveSetting4(this.towerMode, Tower.Motion));
        this.towerHeight = this.resolveSetting2(new NumberSetting<Float>("TowerHeight", Float.valueOf(0.78f), Float.valueOf(0.76f), Float.valueOf(1.0f)).resolveSetting4(this.towerMode, Tower.Motion));
        this.towerShift = this.resolveSetting2(new NumberSetting<Float>("TowerShift", Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(3.0f)).resolveSetting4(this.towerMode, Tower.Motion));
        this.towerOffset = this.resolveSetting2(new NumberSetting<Float>("TowerOffset", Float.valueOf(0.1f), Float.valueOf(0.0f), Float.valueOf(0.2f)).resolveSetting3(this.resolveVisibility2(this.towerMode, new Tower[]{Tower.Down, Tower.Fast})));
        this.towerTimeout = this.resolveSetting2(new LongNumberSetting("TowerTimeout", 450L, 10L, 1000L, true).resolveSetting4(this.towerMode, Tower.Fast));
        this.towerSpeed = this.resolveSetting2(new NumberSetting<Float>("TowerSpeed", Float.valueOf(1.4f), Float.valueOf(1.0f), Float.valueOf(1.6f)).resolveSetting3(this.resolveVisibility2(this.towerMode, new Tower[]{Tower.Fast, Tower.Static})));
        this.towerAlign = this.resolveSetting2(new BooleanSetting("TowerAlign").resolveSetting4(this.towerMode, Tower.Watchdog));
        this.autoJump = this.resolveSetting2(new BooleanSetting("AutoJump"));
        this.autoJumpThreshold = this.resolveSetting2(new NumberSetting<Integer>("AutoJumpThreshold", 7, 3, 15));
        this.hold = this.resolveSetting2(new BooleanSetting("Hold"));
        this.soft = this.resolveSetting2(new BooleanSetting("Soft"));
        this.booleanValueSetting12 = this.resolveSetting2(new BooleanSetting("Bruteforce", "Tries to place without changing yaw", false).resolveSetting3(this.mode.getValue()::isAuto));
        this.extend = this.resolveSetting2(new NumberSetting<Integer>("Extend", 0, 0, 6).resolveSetting3(this.resolveVisibility2(this.mode, new Mode[]{Mode.Plain})));
        this.radius = this.resolveSetting2(new NumberSetting<Integer>("Radius", 0, 0, 5).resolveNumberSetting("None", 0).resolveSetting3(this.resolveVisibility2(this.mode, new Mode[]{Mode.Plain})));
        this.look = this.resolveSetting2(new BooleanSetting("Look", false).resolveSetting3(this.resolveVisibility2(this.mode, new Mode[]{Mode.Plain})));
        this.jump = this.resolveSetting2(new BooleanSetting("Jump", false));
        this.jumpMode = this.resolveSetting2(new EnumSetting<JumpMode>("JumpMode", JumpMode.Legit));
        this.jumpY = this.resolveSetting2(new BooleanSetting("JumpY", true).resolveSetting3(() -> this.jumpMode.getValue().equals((Object)JumpMode.Legit)));
        this.jumpAuto = this.resolveSetting2(new BooleanSetting("JumpAuto", true).resolveSetting3(() -> this.jumpMode.getValue().equals((Object)JumpMode.Legit)));
        this.jumpDelay = this.resolveSetting2(new LongNumberSetting("JumpDelay", 250L, 0L, 1500L, true).resolveSetting3(() -> this.jumpMode.getValue().equals((Object)JumpMode.Legit)));
        this.holdY = this.resolveSetting2(new BindSetting("HoldY").resolveSetting3(() -> this.jumpMode.getValue().equals((Object)JumpMode.Rage)));
        this.sneak = this.resolveSetting2(new BooleanSetting("Sneak", false));
        this.sneakTick = this.resolveSetting2(new NumberSetting<Integer>("SneakTick", 0, 0, 16));
        this.impossible = this.resolveSetting2(new BooleanSetting("Impossible", false).resolveSetting3(this.resolveVisibility2(this.mode, new Mode[]{Mode.Plain})));
        this.sprint = this.resolveSetting2(new EnumSetting<Sprint>("Sprint", Sprint.None));
        this.rotationSmoother = new RotationSmoother();
        this.scaffoldingCursor = new ScaffoldingCursor();
        this.floatArray = new float[2];
        this.floatArray2 = new float[2];
        this.normal3 = new TimeGate.Normal();
        this.normal7 = new TimeGate.Normal();
        this.normal6 = new TimeGate.Normal();
        this.normal5 = new TimeGate.Normal();
        this.normal4 = new TimeGate.Normal();
        this.normal8 = new TimeGate.Normal();
        this.normal9 = new TickGate.Normal();
        this.floatArray4 = new float[2];
        this.doubleValue = Double.NaN;
        this.blockBiFunction = (block, clientWorld) -> BlockUtils.testItem(block.asItem()) && !this.getSetSetting().getValue().contains(block);
        this.slotBiFunction = (slot, clientWorld) -> {
            BlockItem blockItem;
            Item item = slot.getStack().getItem();
            return item instanceof BlockItem && this.blockBiFunction.apply((blockItem = (BlockItem)item).getBlock(), (ClientWorld)clientWorld) != false ? blockItem.getBlock() : null;
        };
        this.floatArray3 = new float[0];
        this.run5();
        this.autoJump.handleConsumer2(booleanValue -> {
            this.intValue5 = 0;
        });
        this.handleListener(new SafeListener<RenderContext>(){

            @Override
            public void handleObject(RenderContext renderContext, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager) {
                if (ScaffoldModule.this.cursor.getValue().booleanValue() && ScaffoldModule.this.scaffoldingCursor.getBlockPos() != null) {
                    ScaffoldModule.this.scaffoldingCursor.handleFloat(ScaffoldModule.this.cursorSpeed.getValue().floatValue());
                    Highlights.handleBox(ScaffoldModule.this.scaffoldingCursor.getBox(), Color.RED, 50L);
                }
                if (!ScaffoldModule.this.simulate.getValue().booleanValue()) {
                    return;
                }
                if (ScaffoldModule.this.speed.getValue().equals(10.0) || ScaffoldModule.this.perfect.getValue().booleanValue()) {
                    ScaffoldModule.this.rotationSmoother.run2();
                    return;
                }
                float f = (float)(ScaffoldModule.this.speed.getValue() / 10.0);
                ScaffoldModule.this.rotationSmoother.handleFloat5(MathUtils.computeFloat8(10.0f, 15.0f) * f, MathUtils.computeFloat8(0.01f, 0.1f) * f);
            }
        });
        this.handleListener(new SafeListener<FindCrosshairTargetEvent>(Integer.MIN_VALUE){

            /*
             * WARNING - void declaration
             */
            @Override
            public void handleObject(FindCrosshairTargetEvent findCrosshairTargetEvent, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager) {
                if (ScaffoldModule.this.mode.testObject2(Mode.Fast)) {
                    if (!(clientPlayerEntity.getMainHandStack().getItem() instanceof BlockItem) && !(clientPlayerEntity.getOffHandStack().getItem() instanceof BlockItem)) {
                        return;
                    }
                    Hand hand = clientPlayerEntity.getMainHandStack().getItem() instanceof BlockItem ? Hand.MAIN_HAND : Hand.OFF_HAND;
                    HitResult hitResult = findCrosshairTargetEvent.getHitResult();
                    if (!(hitResult instanceof BlockHitResult)) {
                        return;
                    }
                    BlockHitResult blockHitResult2 = (BlockHitResult)hitResult;
                    BlockPos blockPos = blockHitResult2.getBlockPos().offset(blockHitResult2.getSide());
                    Block block = ((BlockItem)clientPlayerEntity.getStackInHand(hand).getItem()).getBlock();
                    if (BlockUtils.testBlockPos11(blockPos, block.getDefaultState().getOutlineShape((BlockView)clientWorld, blockPos), (World)clientWorld, true) && ScaffoldModule.this.click.getValue().booleanValue() && ScaffoldModule.this.normal8.testNumber2(ScaffoldModule.this.clickDelay.getValue())) {
                        clientPlayerInteractionManager.interactBlock(clientPlayerEntity, hand, (BlockHitResult)blockHitResult2);
                    }
                    return;
                }
                switch (ScaffoldModule.this.mode.getValue().ordinal()) {
                    case 1: 
                    case 2: 
                    case 4: {
                        if (ScaffoldModule.this.mode.getValue().equals((Object)Mode.Multi) && clientPlayerEntity.hurtTime != 0) {
                            return;
                        }
                        if (!(clientPlayerEntity.getMainHandStack().getItem() instanceof BlockItem) && !(clientPlayerEntity.getOffHandStack().getItem() instanceof BlockItem)) {
                            return;
                        }
                        Hand hand2 = clientPlayerEntity.getMainHandStack().getItem() instanceof BlockItem ? Hand.MAIN_HAND : Hand.OFF_HAND;
                        HitResult hitResult2 = findCrosshairTargetEvent.getHitResult();
                        if (!(hitResult2 instanceof BlockHitResult)) {
                            return;
                        }
                        BlockHitResult blockHitResult4 = (BlockHitResult)hitResult2;
                        if (ScaffoldModule.this.click.getValue().booleanValue() && blockHitResult4.getSide().equals((Object)Direction.UP) && ScaffoldModule.this.normal8.testNumber(ScaffoldModule.this.clickDelay.getValue())) {
                            double d = blockHitResult4.getPos().getX();
                            double d2 = blockHitResult4.getPos().getZ();
                            double d3 = d - (double)blockHitResult4.getBlockPos().getX();
                            double d4 = d2 - (double)blockHitResult4.getBlockPos().getZ();
                            if (d3 <= 0.1 || d3 >= 0.9 || d4 <= 0.1 || d4 >= 0.9) {
                                ItemStack itemStack = clientPlayerEntity.getStackInHand(hand2);
                                PlayerEntity playerEntity = phobosClient.getMovementSimulator().resolvePlayerEntity((PlayerEntity)clientPlayerEntity);
                                ItemPlacementContext itemPlacementContext = new ItemPlacementContext(playerEntity, hand2, itemStack, (BlockHitResult)blockHitResult4);
                                BlockItem blockItem = (BlockItem)itemStack.getItem();
                                ItemPlacementContext itemPlacementContext2 = blockItem.getPlacementContext(itemPlacementContext);
                                if (itemPlacementContext2 != null && !itemPlacementContext2.canPlace()) {
                                    for (int i = 0; i < ScaffoldModule.this.clickIterations.getValue(); ++i) {
                                        clientPlayerInteractionManager.interactBlock(clientPlayerEntity, hand2, (BlockHitResult)blockHitResult4);
                                    }
                                    ScaffoldModule.this.normal8.run();
                                }
                            }
                        }
                        BlockPos blockPos2 = blockHitResult4.getBlockPos();
                        BlockPos blockPos3 = blockPos2.offset(blockHitResult4.getSide());
                        BlockPos blockPos4 = BlockPos.ofFloored((Position)clientPlayerEntity.getPos());
                        if (blockPos3.getY() >= blockPos4.getY()) {
                            return;
                        }
                        Box box2 = new Box(blockPos3);
                        Box box = new Box(clientPlayerEntity.getPos(), clientPlayerEntity.getPos()).expand(2.0, 0.0, 2.0).stretch(0.0, -2.0, 0.0);
                        if (!box2.intersects(box)) {
                            return;
                        }
                        if (clientWorld.getBlockState(blockPos2).isAir()) {
                            return;
                        }
                        if (!clientWorld.getBlockState(blockPos3).getFluidState().isEmpty()) {
                            return;
                        }
                        if (!ScaffoldModule.this.normal5.testNumber2(ScaffoldModule.this.delay.getValue()) || !clientPlayerInteractionManager.interactBlock(clientPlayerEntity, hand2, (BlockHitResult)blockHitResult4).isAccepted()) break;
                        mc.options.rightKey.setPressed(true);
                        clientPlayerEntity.swingHand(hand2);
                        mc.options.rightKey.setPressed(false);
                        ++ScaffoldModule.this.intValue5;
                        if (ScaffoldModule.this.intValue4++ < ScaffoldModule.this.sneakTick.getValue()) break;
                        ScaffoldModule.this.normal6.run();
                        ScaffoldModule.this.intValue4 = 0;
                    }
                }
            }
        });
        this.handleListener(new SafeListener<PlayerTickEvent>(6){

            @Override
            public void handleObject(PlayerTickEvent playerTickEvent, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager) {
                int n = ScaffoldModule.this.intValue2 = clientPlayerEntity.isOnGround() ? 0 : ScaffoldModule.this.intValue2 + 1;
                if (RotationManager.isFlag()) {
                    ScaffoldModule.this.floatValue = Float.NaN;
                    return;
                }
                if (ScaffoldModule.this.cycle.getValue().booleanValue()) {
                    if (clientPlayerEntity.isOnGround() && clientPlayerEntity.input.jumping && (!ScaffoldModule.this.tower.getValue().booleanValue() || !mc.options.jumpKey.isPressed())) {
                        ScaffoldModule.this.intValue3 = ScaffoldModule.this.cycleTicks.getValue();
                        ScaffoldModule.this.floatValue = Float.NaN;
                        return;
                    }
                    if (ScaffoldModule.this.intValue3 > 0) {
                        ScaffoldModule.this.floatValue = Float.NaN;
                        return;
                    }
                }
                switch (ScaffoldModule.this.mode.getValue().ordinal()) {
                    case 0: {
                        if (!ScaffoldModule.this.simulate.getValue().booleanValue()) break;
                        if (ScaffoldModule.this.normal7.testLong(100L)) {
                            RotationManager.Rotation rotation = RotationManager.Rotation.createRotation2((PlayerEntity)clientPlayerEntity);
                            rotation.handleFloat(ScaffoldModule.this.floatArray4);
                        }
                        if (!ScaffoldModule.this.constant.getValue().booleanValue()) break;
                        ScaffoldModule.this.floatArray = ScaffoldModule.this.values5(ScaffoldModule.this.values4(clientPlayerEntity, clientWorld));
                        float f = ScaffoldModule.this.spread.getValue().floatValue();
                        ScaffoldModule.this.floatValue4 = ScaffoldModule.this.floatArray[0] + MathUtils.computeFloat8(-f, f);
                        ScaffoldModule.this.floatValue2 = ScaffoldModule.this.floatArray[1] + MathUtils.computeFloat8(-f, f);
                        phobosClient.getRotationManager().testFloat(ScaffoldModule.this.floatValue4, ScaffoldModule.this.floatValue2);
                        break;
                    }
                    case 1: 
                    case 2: 
                    case 4: {
                        if (ScaffoldModule.this.mode.getValue().equals((Object)Mode.Multi) && clientPlayerEntity.hurtTime != 0) {
                            if (ScaffoldModule.this.hold.getValue().booleanValue() && ScaffoldModule.this.getRotationSetting().getValue().equals(PlacementModule.Rotation.Normal)) {
                                if (!ScaffoldModule.this.normal7.testLong(1000L)) {
                                    phobosClient.getRotationManager().testFloat(ScaffoldModule.this.floatValue5, ScaffoldModule.this.floatValue3);
                                } else if (ScaffoldModule.this.soft.getValue().booleanValue()) {
                                    ScaffoldModule.this.floatValue5 = clientPlayerEntity.getYaw() + 180.7302f;
                                    ScaffoldModule.this.floatValue3 = 85.06623f;
                                    phobosClient.getRotationManager().testFloat(ScaffoldModule.this.floatValue5, ScaffoldModule.this.floatValue3);
                                }
                            }
                            return;
                        }
                        boolean flag = ScaffoldModule.this.mode.getValue().equals((Object)Mode.Telly);
                        if (flag && clientPlayerEntity.isOnGround()) {
                            return;
                        }
                        if (ScaffoldModule.this.flag4) {
                            ScaffoldModule.this.floatArray = new float[]{FreeLookModule.computeClientPlayerEntity2(clientPlayerEntity), FreeLookModule.computeClientPlayerEntity(clientPlayerEntity)};
                            ScaffoldModule.this.flag4 = false;
                        }
                        ScaffoldModule.this.floatArray2 = ScaffoldModule.this.values4(clientPlayerEntity, clientWorld);
                        ScaffoldModule.this.floatArray = ScaffoldModule.this.values5(ScaffoldModule.this.floatArray2);
                        phobosClient.getRotationManager().testFloat(ScaffoldModule.this.floatArray[0], ScaffoldModule.this.floatArray[1]);
                        return;
                    }
                    case 3: {
                        if (ScaffoldModule.this.flag4) {
                            ScaffoldModule.this.floatArray = new float[]{FreeLookModule.computeClientPlayerEntity2(clientPlayerEntity), FreeLookModule.computeClientPlayerEntity(clientPlayerEntity)};
                            ScaffoldModule.this.flag4 = false;
                        }
                        ScaffoldModule.this.floatArray2 = ScaffoldModule.this.values4(clientPlayerEntity, clientWorld);
                        ScaffoldModule.this.floatArray = ScaffoldModule.this.values5(ScaffoldModule.this.floatArray2);
                        phobosClient.getRotationManager().testFloat(ScaffoldModule.this.floatArray[0], ScaffoldModule.this.floatArray[1]);
                    }
                }
                if (ScaffoldModule.this.hold.getValue().booleanValue() && ScaffoldModule.this.getRotationSetting().getValue().equals(PlacementModule.Rotation.Normal)) {
                    if (!ScaffoldModule.this.normal7.testLong(1000L)) {
                        phobosClient.getRotationManager().testFloat(ScaffoldModule.this.floatValue5, ScaffoldModule.this.floatValue3);
                    } else if (ScaffoldModule.this.soft.getValue().booleanValue()) {
                        ScaffoldModule.this.floatValue5 = clientPlayerEntity.getYaw() + 180.7302f;
                        ScaffoldModule.this.floatValue3 = 85.06623f;
                        phobosClient.getRotationManager().testFloat(ScaffoldModule.this.floatValue5, ScaffoldModule.this.floatValue3);
                    }
                }
                boolean flag2 = ScaffoldModule.this.normal7.testLong(1000L);
                boolean[] flags = ScaffoldModule.this.values3();
                float f2 = ScaffoldModule.this.computeFloat3(clientPlayerEntity.getYaw(), flags);
                float f3 = ScaffoldModule.this.computeFlag2(flags);
                if (ScaffoldModule.this.look.getValue().booleanValue() && !flag2) {
                    if (ScaffoldModule.this.jump.getValue().booleanValue() && !ScaffoldModule.this.normal3.testNumber(ScaffoldModule.this.jumpDelay.getValue())) {
                        return;
                    }
                    phobosClient.getRotationManager().handleFloat(f2, f3);
                }
            }
        });
        this.handleListener(new SafeListener<JumpEvent3>(Integer.MIN_VALUE){

            @Override
            public void handleObject(JumpEvent3 jumpEvent3, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager) {
                ScaffoldModule.this.normal4.run();
                if (!ScaffoldModule.this.mode.getValue().isAuto()) {
                    return;
                }
                if (ScaffoldModule.this.jump.getValue().booleanValue() && ScaffoldModule.this.jumpMode.testObject2(JumpMode.Awaiting) && PlayerUtils.check()) {
                    ScaffoldModule.this.flag6 = true;
                }
                if (ScaffoldModule.this.jump.getValue().booleanValue() && ScaffoldModule.this.jumpMode.getValue().equals((Object)JumpMode.Legit)) {
                    if (!PlayerUtils.check()) {
                        ScaffoldModule.this.intValue6 = (int)Math.floor(clientPlayerEntity.getY()) + 1;
                    } else {
                        ScaffoldModule.this.normal3.run();
                        ScaffoldModule.this.intValue6 = (int)Math.floor(clientPlayerEntity.getY());
                    }
                }
                if (ScaffoldModule.this.tower.getValue().booleanValue()) {
                    switch (ScaffoldModule.this.towerMode.getValue().ordinal()) {
                        case 0: {
                            ScaffoldModule.this.doubleValue = clientPlayerEntity.getY();
                            break;
                        }
                        case 2: 
                        case 3: {
                            if (jumpEvent3.isCancelled()) break;
                            if (ScaffoldModule.this.towerMode.getValue().equals(Tower.Fast)) {
                                phobosClient.getTimerManager().handleFloat(ScaffoldModule.this.towerSpeed.getValue().floatValue(), TimerManager.TimerPriority.Normal);
                                phobosClient.getDelayedTaskManager().handleRunnable(() -> phobosClient.getTimerManager().run2(), ScaffoldModule.this.towerTimeout.getValue());
                            }
                            phobosClient.getDelayedTaskManager().handleRunnable2(() -> {
                                if (ScaffoldModule.this.normal7.testLong(300L)) {
                                    return;
                                }
                                PlayerActionUtils.handleClientPlayerEntity29(clientPlayerEntity, -1.0);
                            }, () -> clientPlayerEntity.getVelocity().getY() < (double)ScaffoldModule.this.towerOffset.getValue().floatValue() && !clientPlayerEntity.isOnGround());
                            break;
                        }
                        case 6: {
                            if (jumpEvent3.isCancelled()) break;
                            jumpEvent3.setCancelled(true);
                            clientPlayerEntity.setVelocity(0.0, 0.369, 0.0);
                        }
                    }
                }
            }
        });
        this.handleListener(new SafeListener<TickEvent2>(){

            @Override
            public void handleObject(TickEvent2 tickEvent2, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager) {
                if (!ScaffoldModule.this.mode.getValue().isAuto()) {
                    return;
                }
                ScaffoldModule.this.intValue = PlayerUtils.testClientPlayerEntity(clientPlayerEntity) ? ScaffoldModule.this.intValue + 1 : 0;
                switch (ScaffoldModule.this.strafe.getValue().ordinal()) {
                    case 1: {
                        MovementModel.State state = new GroundMovementModel().resolveState2((PlayerEntity)clientPlayerEntity, (CollisionView)clientWorld, new MovementModel.State(), PlayerUtils.createVec3d2(clientPlayerEntity));
                        if (state.isFlag2()) break;
                        clientPlayerEntity.setVelocity(state.getVec3d());
                        break;
                    }
                    case 2: {
                        double d = 0.207;
                        StatusEffectInstance statusEffectInstance = clientPlayerEntity.getStatusEffect(StatusEffects.SPEED);
                        if (statusEffectInstance != null && statusEffectInstance.getAmplifier() >= 0) {
                            d = 0.295;
                        }
                        if (clientPlayerEntity.age % 20 == 0 || ScaffoldModule.this.intValue <= 7) {
                            d = 0.09800000190734863;
                        }
                        double[] doubles = PlayerUtils.values2(clientPlayerEntity, d);
                        PlayerActionUtils.handleClientPlayerEntity13(clientPlayerEntity, doubles[0], doubles[1]);
                    }
                }
                if (ScaffoldModule.this.tower.getValue().booleanValue()) {
                    if (!mc.options.jumpKey.isPressed() || !ScaffoldModule.this.testAbstractClientPlayerEntity((AbstractClientPlayerEntity)clientPlayerEntity)) {
                        phobosClient.getTimerManager().run2();
                        ScaffoldModule.this.doubleValue = Double.NaN;
                        return;
                    }
                    switch (ScaffoldModule.this.towerMode.getValue().ordinal()) {
                        case 0: {
                            if (Double.isNaN(ScaffoldModule.this.doubleValue)) {
                                return;
                            }
                            if (!(clientPlayerEntity.getY() > ScaffoldModule.this.doubleValue + (double)ScaffoldModule.this.towerHeight.getValue().floatValue())) break;
                            clientPlayerEntity.setPosition(clientPlayerEntity.getX(), MathUtils.computeDouble11(clientPlayerEntity.getY()), clientPlayerEntity.getZ());
                            PlayerActionUtils.handleClientPlayerEntity29(clientPlayerEntity, ScaffoldModule.this.towerPower.getValue().floatValue());
                            clientPlayerEntity.setVelocity(clientPlayerEntity.getVelocity().multiply((double)ScaffoldModule.this.towerShift.getValue().floatValue(), 1.0, (double)ScaffoldModule.this.towerShift.getValue().floatValue()));
                            clientPlayerEntity.incrementStat(Stats.JUMP);
                            ScaffoldModule.this.doubleValue = clientPlayerEntity.getY();
                            break;
                        }
                        case 1: {
                            if (clientPlayerEntity.age % 2 == 0) {
                                PlayerActionUtils.handleClientPlayerEntity29(clientPlayerEntity, 0.7);
                                break;
                            }
                            PlayerActionUtils.handleClientPlayerEntity29(clientPlayerEntity, PlayerUtils.testClientPlayerEntity(clientPlayerEntity) ? (double)0.42f : (double)0.6f);
                            clientPlayerEntity.incrementStat(Stats.JUMP);
                            break;
                        }
                        case 4: {
                            if (clientPlayerEntity.getX() % 1.0 != 0.0 && !PlayerUtils.testClientPlayerEntity(clientPlayerEntity) && ScaffoldModule.this.towerAlign.getValue().booleanValue()) {
                                PlayerActionUtils.handleClientPlayerEntity24(clientPlayerEntity, Math.max((double)Math.round(clientPlayerEntity.getX()) - clientPlayerEntity.getX(), 0.281));
                            }
                            if (PlayerUtils.computeClientPlayerEntity3(clientPlayerEntity) > 14) {
                                PlayerActionUtils.handleClientPlayerEntity27(clientPlayerEntity, -0.09);
                                PlayerActionUtils.handleClientPlayerEntity25(clientPlayerEntity, 0.6, 1.0, 0.6);
                                return;
                            }
                            switch (PlayerUtils.computeClientPlayerEntity3(clientPlayerEntity) % 3) {
                                case 0: {
                                    PlayerActionUtils.handleClientPlayerEntity29(clientPlayerEntity, 0.42);
                                    break;
                                }
                                case 2: {
                                    PlayerActionUtils.handleClientPlayerEntity29(clientPlayerEntity, 1.0 - clientPlayerEntity.getY() % 1.0);
                                }
                            }
                            break;
                        }
                        case 5: {
                            if (ScaffoldModule.this.intValue2 == 4) {
                                PlayerActionUtils.handleClientPlayerEntity29(clientPlayerEntity, -0.4);
                            }
                            if (ScaffoldModule.this.intValue2 > 2) {
                                phobosClient.getTimerManager().handleFloat(ScaffoldModule.this.towerSpeed.getValue().floatValue(), TimerManager.TimerPriority.Normal);
                                break;
                            }
                            phobosClient.getTimerManager().run2();
                        }
                    }
                }
            }
        });
        this.handleListener(new SafeListener<PacketEvent.Send<?>>(){

            @Override
            public void handleObject(PacketEvent.Send<?> send, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager) {
                if (!ScaffoldModule.this.mode.getValue().isAuto()) {
                    return;
                }
                if (ScaffoldModule.this.tower.getValue().booleanValue()) {
                    switch (ScaffoldModule.this.towerMode.getValue().ordinal()) {
                        case 1: {
                            Object p = send.getPacket();
                            if (!(p instanceof PlayerMoveC2SPacket)) break;
                            PlayerMoveC2SPacket playerMoveC2SPacket = (PlayerMoveC2SPacket)p;
                            if (PlayerUtils.testClientPlayerEntity(clientPlayerEntity)) break;
                            IPlayerMoveC2SPacket iPlayerMoveC2SPacket = (IPlayerMoveC2SPacket)(Object)playerMoveC2SPacket;
                            if (clientPlayerEntity.age % 2 != 0) break;
                            iPlayerMoveC2SPacket.setX(iPlayerMoveC2SPacket.getX() + 0.1);
                            iPlayerMoveC2SPacket.setZ(iPlayerMoveC2SPacket.getZ() + 0.1);
                        }
                    }
                }
            }
        });
        this.handleListener(new SafeListener<TickEvent>(2){

            @Override
            public void handleObject(TickEvent tickEvent, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager) {
                if (ScaffoldModule.this.jump.getValue().booleanValue()) {
                    switch (ScaffoldModule.this.jumpMode.getValue().ordinal()) {
                        case 0: {
                            if (!PlayerUtils.check() || !ScaffoldModule.this.jumpAuto.getValue().booleanValue() || mc.options.jumpKey.isPressed()) break;
                            tickEvent.getKeyboardInput().jumping = true;
                            break;
                        }
                        case 3: {
                            if (!PlayerUtils.check()) break;
                            tickEvent.getKeyboardInput().jumping = true;
                        }
                    }
                }
                if (ScaffoldModule.this.sneak.getValue().booleanValue()) {
                    tickEvent.getKeyboardInput().sneaking = tickEvent.getKeyboardInput().sneaking | (ScaffoldModule.this.normal6.testLong(100L) && !ScaffoldModule.this.normal6.testLong(300L));
                }
                if (!tickEvent.getKeyboardInput().jumping) {
                    if (ScaffoldModule.this.autoJump.getValue().booleanValue() && ScaffoldModule.this.intValue5 >= ScaffoldModule.this.autoJumpThreshold.getValue()) {
                        tickEvent.getKeyboardInput().jumping = true;
                        ScaffoldModule.this.intValue5 = 0;
                    }
                    if (ScaffoldModule.this.simulate.getValue().booleanValue() && ScaffoldModule.this.jiggle.getValue().booleanValue() && PlayerUtils.check() && !ScaffoldModule.this.flag3 && ScaffoldModule.this.normal9.testInt3(ScaffoldModule.this.jiggleDelay.getValue().getNumber())) {
                        float f;
                        ScaffoldModule.this.jiggleDelay.getValue().run6();
                        float f2 = f = ScaffoldModule.this.flag && ScaffoldModule.this.booleanValueSetting11.getValue() != false ? -1.0f : 1.0f;
                        if ((double)tickEvent.getKeyboardInput().movementForward == 0.0) {
                            tickEvent.getKeyboardInput().movementForward = f;
                            return;
                        }
                        tickEvent.getKeyboardInput().movementSideways = f;
                    }
                }
            }
        });
        this.handleListener(new Listener<TickMovementEvent>(){

            @Override
            public void handleObject2(TickMovementEvent tickMovementEvent) {
                if (ScaffoldModule.this.sprint.testObject2(Sprint.Disable)) {
                    tickMovementEvent.setFlag(false);
                }
            }
        });
        this.handleListener(new Listener<ScaffoldFlags>(){

            @Override
            public void handleObject2(ScaffoldFlags scaffoldFlags) {
                if (ScaffoldModule.this.sprint.testObject2(Sprint.Client)) {
                    scaffoldFlags.setFlag(false);
                }
            }
        });
        scaffoldModule = this;
    }

    @Override
    public PlacementModule.RotationSequence getRotationSequence() {
        return (placementModule, action, phobosClient, set, rotation, timing, playerEntity, clientPlayerEntity, clientWorld, blockPos, blockPos2, direction, direction2, flag, mutableObject) -> {
            block14: {
                double d;
                Vec3d vec3d2;
                float[] floats;
                block17: {
                    block16: {
                        block15: {
                            floats = RotationUtils.values2((Entity)clientPlayerEntity, ((BlockHitResult)mutableObject.getValue()).getPos());
                            if (!this.simulate.getValue().booleanValue() || !this.constant.getValue().booleanValue() || !this.constantHeavy.getValue().booleanValue()) break block15;
                            float[] fArray = new float[]{this.floatValue4, this.floatValue2};
                            if (!(RotationUtils.computeFloat2(floats, fArray) >= this.constantThreshold.getValue().floatValue())) break block15;
                            break block16;
                        }
                        if (!this.booleanValueSetting12.getValue().booleanValue()) break block14;
                        vec3d2 = ((BlockHitResult)mutableObject.getValue()).getPos();
                        d = 0.25;
                        break block17;
                    }
                    for (float f = Math.max(this.floatValue2 - 60.0f, -90.0f); f <= Math.min(this.floatValue2 + 60.0f, 90.0f); f += 0.5f) {
                        BlockHitResult blockHitResult;
                        if (f <= -90.0f || f >= 90.0f || (blockHitResult = RaycastUtils.resolveBlockHitResult2((Entity)clientPlayerEntity, (World)clientWorld, blockPos2, direction, new float[]{this.floatValue4, f})) == null) continue;
                        mutableObject.setValue(blockHitResult);
                        break block14;
                    }
                    break block14;
                }
                for (float f2 = floats[1] - 40.0f; f2 <= floats[1] + 40.0f; f2 += 0.5f) {
                    Vec3d vec3d;
                    BlockHitResult blockHitResult2;
                    if (f2 <= -90.0f || f2 >= 90.0f || (blockHitResult2 = RaycastUtils.resolveBlockHitResult2((Entity)clientPlayerEntity, (World)clientWorld, blockPos2, direction, new float[]{this.simulate.getValue() != false && this.constant.getValue() != false ? this.floatValue4 : floats[0], f2})) == null || !((vec3d = blockHitResult2.getPos()).distanceTo(vec3d2) < d)) continue;
                    mutableObject.setValue(blockHitResult2);
                    break;
                }
            }
            float[] floats2 = RotationUtils.values2((Entity)clientPlayerEntity, ((BlockHitResult)mutableObject.getValue()).getPos());
            switch (this.getRotationSetting().getValue()) {
                case Instant: {
                    if (!set.isEmpty()) {
                        if (!set.stream().allMatch(PlacementModule.Action::isCompleted)) return PlacementModule.RotationSequence.Result.FAIL_BUSY;
                    }
                    if (RotationManager.isFlag()) return PlacementModule.RotationSequence.Result.FAIL_BUSY;
                    if (!phobosClient.getRotationManager().isFlag2()) return PlacementModule.RotationSequence.Result.FAIL_BUSY;
                    if (this.simulate.getValue().booleanValue()) {
                        if (this.instant.getValue().booleanValue()) {
                            phobosClient.getRotationManager().testFloat(floats2[0], floats2[1]);
                            return PlacementModule.RotationSequence.Result.SUCCESS_ROTATED;
                        }
                        if (this.perfect.getValue().booleanValue()) {
                            RotationManager.Rotation rotation2 = RotationManager.Rotation.createRotation3(this.values4(clientPlayerEntity, clientWorld));
                            rotation2.handleFloat(this.floatArray4);
                            return PlacementModule.RotationSequence.Result.SUCCESS_ROTATED;
                        }
                        if (this.constant.getValue().booleanValue()) {
                            float[] fArray = new float[]{this.floatValue4, this.floatValue2};
                            double d2 = RotationUtils.computeFloat2(floats2, fArray);
                            if (d2 < (double)this.constantThreshold.getValue().floatValue()) {
                                return PlacementModule.RotationSequence.Result.SUCCESS_ROTATED;
                            }
                            this.rotationSmoother.handleRotation2(RotationManager.Rotation.createRotation3(floats2));
                        }
                        phobosClient.getRotationManager().testFloat(floats2[0], floats2[1]);
                        return PlacementModule.RotationSequence.Result.SUCCESS_ROTATED;
                    }
                    if (RotationManager.isFlag()) {
                        return PlacementModule.RotationSequence.Result.SUCCESS_ROTATED;
                    }
                    phobosClient.getRotationManager().testFloat(floats2[0], floats2[1]);
                    this.floatValue5 = floats2[0];
                    this.floatValue3 = floats2[1];
                    return PlacementModule.RotationSequence.Result.SUCCESS_ROTATED;
                }
                case Normal: {
                    clientPlayerEntity.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.LookAndOnGround(floats2[0], floats2[1], clientPlayerEntity.isOnGround()));
                    return PlacementModule.RotationSequence.Result.SUCCESS_ROTATED;
                }
                case Hybrid: {
                    phobosClient.getRotationManager().handleClientPlayerEntity3(clientPlayerEntity, floats2[0], floats2[1]);
                    return PlacementModule.RotationSequence.Result.SUCCESS_ROTATED;
                }
            }
            return PlacementModule.RotationSequence.Result.SUCCESS_NOT_SUPPORTED;
        };
    }

    public void run5() {
        this.floatArray3 = new float[]{0.0f, 45.0f, 90.0f, 135.0f, 180.0f, -180.0f, -135.0f, -90.0f, -45.0f};
        this.getSettingGroupList2().forEach(settingGroup -> settingGroup.resolveSettingGroup5(() -> this.mode.getValue().isAuto()));
        this.createBuilder().resolveBuilder6(this.simulate).resolveBuilder5(this.resolveSettingGroup(this.constant, this.constantThreshold, this.constantHeavy, this.constantResync), this.resolveSettingGroup(this.jiggle, this.jiggleDelay)).resolveBuilder(this.speed, this.spread, this.instant, this.perfect).getSettingSubsection();
        this.resolveSettingGroup(this.cursor, this.cursorSpeed, this.cursorStep).resolveSettingGroup5(this.mode.getValue()::isAuto);
        this.resolveSettingGroup(this.autoJump, this.autoJumpThreshold);
        this.resolveSettingGroup(this.sneak, this.sneakTick);
        this.resolveSettingGroup(this.click, this.clickDelay, this.clickIterations).resolveSettingGroup5(() -> !this.mode.getValue().equals((Object)Mode.Plain));
        this.resolveSettingGroup(this.jump, this.jumpMode, this.jumpY, this.jumpAuto, this.jumpDelay, this.holdY).resolveSettingGroup5(() -> this.mode.getValue().isAuto());
        this.resolveSettingGroup(this.tower, this.towerFill, this.towerMode, this.towerPower, this.towerHeight, this.towerShift, this.towerOffset, this.towerSpeed, this.towerTimeout, this.towerAlign).resolveSettingGroup5(() -> this.mode.getValue().isAuto());
        this.resolveSettingGroup(this.hold, this.soft).resolveSettingGroup5(this.mode.getValue()::isAuto);
        this.resolveSettingGroup(this.cycle, this.cycleTicks);
    }

    public float computeFloat2(float f) {
        f = MathHelper.wrapDegrees((float)f);
        MutableObject mutableObject = new MutableObject((Object)Float.valueOf(0.0f));
        MutableObject mutableObject2 = new MutableObject((Object)Float.valueOf(Float.MAX_VALUE));
        for (float f3 : this.floatArray3) {
            float f2 = Math.abs(f3 - f);
            if (!(f2 < ((Float)mutableObject2.getValue()).floatValue())) continue;
            mutableObject.setValue((Object)Float.valueOf(f3));
            mutableObject2.setValue((Object)Float.valueOf(f2));
        }
        return ((Float)mutableObject.getValue()).floatValue();
    }

    public Vec3d resolveVec3d(BlockPos blockPos, Direction direction, double d, double d2, double d3) {
        return RotationUtils.createVec3d4(blockPos, direction, d, d2, d3);
    }

    public List<PlayerEntity> resolveList3(ClientPlayerEntity clientPlayerEntity) {
        ArrayList<PlayerEntity> list = new ArrayList<PlayerEntity>();
        if (this.extend.getValue().equals(0) || clientPlayerEntity.input.jumping) {
            list.add((PlayerEntity)clientPlayerEntity);
            return list;
        }
        for (int i = 0; i < this.extend.getValue(); ++i) {
            PlayerEntity playerEntity = this.phobosClient.getFeatureRegistry().getPredictionManager().resolvePlayerEntity2(i, clientPlayerEntity);
            list.add(playerEntity);
        }
        return list;
    }

    public boolean[] values3() {
        return new boolean[]{ScaffoldModule.mc.options.forwardKey.isPressed(), ScaffoldModule.mc.options.backKey.isPressed(), ScaffoldModule.mc.options.rightKey.isPressed(), ScaffoldModule.mc.options.leftKey.isPressed()};
    }

    @Override
    public PlacementModule.Placement resolvePlacement13(Item item, BlockPos blockPos, @Nullable Direction direction, boolean flag, boolean flag2, @Nullable BlockPos blockPos2) {
        this.setActionConsumer(action -> action.setRunnable(() -> {
            this.normal7.run();
            ++this.intValue5;
        }));
        return super.resolvePlacement13(item, blockPos, direction, flag, flag2, blockPos2);
    }

    @Override
    public PlacementModule.BlockHitSelector getBlockHitSelector() {
        return (playerEntity, clientPlayerEntity, clientWorld, blockPos2, blockPos22, direction2) -> {
            Vec3d vec3d2;
            BlockHitResult blockHitResult = RaycastUtils.resolveBlockHitResult6((Entity)playerEntity, (World)clientWorld, blockPos2, (blockPos, direction) -> this.testBlockPos3((BlockPos)blockPos, (Direction)direction, clientWorld, playerEntity), true);
            Vec3d vec3d = RotationUtils.createVec3d2(blockPos22, (World)clientWorld, direction2);
            Vec3d vec3d3 = vec3d2 = this.cursor.getValue() != false && this.scaffoldingCursor.testDirection(direction2) ? this.scaffoldingCursor.createVec3d(blockPos22) : this.hitVectorVSetting.getValue().mutate(this.phobosClient, clientPlayerEntity, clientWorld, blockPos22, direction2, blockHitResult, this.hitVectorHSetting.getValue().auto(this.phobosClient, clientPlayerEntity, clientWorld, blockPos22, direction2, blockHitResult, vec3d));
            if (this.isFlag13() && this.booleanValueSetting10.getValue().booleanValue()) {
                Highlights.handleBox(new Box(vec3d2, vec3d2).expand(0.015), Color.RED, 5000L);
            }
            return blockHitResult != null && blockHitResult.getBlockPos().offset(blockHitResult.getSide()).equals((Object)blockPos2) ? blockHitResult : new BlockHitResult(vec3d2, direction2, blockPos22, false);
        };
    }

    public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, double d) {
        return Math.abs(clientPlayerEntity.getVelocity().x) > d && Math.abs(clientPlayerEntity.getVelocity().z) > d;
    }

    @Override
    public void handleItemSwitcher(ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager) {
        Block block;
        if (!this.mode.getValue().isAuto()) {
            return;
        }
        if (this.mode.getValue().equals((Object)Mode.Multi) && clientPlayerEntity.hurtTime == 0) {
            return;
        }
        if (this.cycle.getValue().booleanValue()) {
            if (this.intValue3-- > 0) {
                return;
            }
            if (clientPlayerEntity.isOnGround() && clientPlayerEntity.input.jumping) {
                return;
            }
        }
        if (clientPlayerEntity.isOnGround()) {
            this.flag5 = true;
        }
        if ((block = (Block)itemSwitcher.resolveObject(slot -> this.slotBiFunction.apply((Slot)slot, clientWorld))) == null) {
            return;
        }
        if (this.impossible.getValue().booleanValue() && this.mode.getValue().equals((Object)Mode.Plain) && !clientWorld.getBlockCollisions((Entity)clientPlayerEntity, clientPlayerEntity.getBoundingBox().expand((double)-0.3f, 0.0, (double)-0.3f).offset(0.0, 0.5, 0.0)).iterator().hasNext()) {
            BlockPos blockPos2 = new BlockPos((int)Math.floor(clientPlayerEntity.getX()), (int)Math.floor(clientPlayerEntity.getY()) - 1, (int)Math.floor(clientPlayerEntity.getZ()));
            BlockPos blockPos3 = blockPos2.up(3);
            if (this.alwaysTrue()) {
                this.handleBlock(block, blockPos3);
            } else {
                List<BlockPos> list = this.resolveList2(blockPos3, clientWorld, clientPlayerEntity, block, new PositionPoolBuilder[0]);
                for (BlockPos blockPos : list) {
                    this.resolvePlacement8(block, blockPos);
                }
                this.handleBlock(block, blockPos3);
            }
        }
        if (this.radius.testObject2(0) && this.extend.testObject2(0)) {
            Box box = clientPlayerEntity.getBoundingBox().expand((double)-0.3f, 0.0, (double)-0.3f);
            Box box2 = new Box(box.minX, box.minY - 0.5, box.minZ, box.maxX, box.minY - 0.25, box.maxZ);
            if (clientWorld.getBlockCollisions((Entity)clientPlayerEntity, box2).iterator().hasNext()) {
                return;
            }
        }
        if (this.jump.getValue().booleanValue()) {
            switch (this.jumpMode.getValue().ordinal()) {
                case 0: {
                    if (this.jumpAuto.getValue().booleanValue() && !ScaffoldModule.mc.options.jumpKey.isPressed() && clientPlayerEntity.isOnGround()) {
                        clientPlayerEntity.jump();
                    }
                    if (!this.normal3.testNumber(this.jumpDelay.getValue())) {
                        return;
                    }
                    if (this.jumpY.getValue().booleanValue()) break;
                    this.intValue6 = 4096;
                    break;
                }
                case 4: {
                    if (!PlayerUtils.check() || ScaffoldModule.mc.options.jumpKey.isPressed() && this.tower.getValue().booleanValue()) {
                        this.intValue6 = 4096;
                        break;
                    }
                    if (clientPlayerEntity.fallDistance <= 0.0f) {
                        if (!clientPlayerEntity.isOnGround()) break;
                        this.flag6 = false;
                        this.intValue6 = (int)Math.floor(clientPlayerEntity.getY() - 1.0);
                        break;
                    }
                    if (clientPlayerEntity.fallDistance <= 1.5f && this.flag6) break;
                    this.intValue6 = 4096;
                    break;
                }
                case 1: {
                    if (this.phobosClient.getBindManager().testKeyCombination(this.holdY.getValue())) {
                        if (!clientPlayerEntity.isOnGround()) break;
                        this.intValue6 = (int)Math.floor(clientPlayerEntity.getY() - 1.0);
                        break;
                    }
                    this.intValue6 = 4096;
                    break;
                }
                case 2: 
                case 3: {
                    if (!ScaffoldModule.mc.options.jumpKey.isPressed() && clientPlayerEntity.fallDistance <= 1.0f) {
                        if (!clientPlayerEntity.isOnGround() && this.flag5) break;
                        this.intValue6 = (int)Math.floor(clientPlayerEntity.getY() - 1.0);
                        break;
                    }
                    this.intValue6 = 4096;
                }
            }
        } else {
            this.intValue6 = 4096;
        }
        for (PlayerEntity playerEntity : this.resolveList3(clientPlayerEntity)) {
            BlockPos blockPos5 = new BlockPos((int)Math.floor(playerEntity.getX()), this.intValue6 == 4096 ? (int)Math.floor(playerEntity.getY() - 1.0) : this.intValue6, (int)Math.floor(playerEntity.getZ()));
            if (this.alwaysTrue()) {
                this.handleBlock(block, blockPos5);
                continue;
            }
            List<BlockPos> list2 = this.resolveList2(blockPos5, clientWorld, clientPlayerEntity, block, new PositionPoolBuilder[0]);
            for (BlockPos blockPos4 : list2) {
                this.resolvePlacement8(block, blockPos4);
            }
            this.handleBlock(block, blockPos5);
        }
        if (this.intValue4++ >= this.sneakTick.getValue()) {
            this.normal6.run();
            this.intValue4 = 0;
        }
    }

    @Nullable
    public Vec3d resolveVec3d2(ClientPlayerEntity clientPlayerEntity, BlockPos blockPos, Direction direction, BiPredicate<Double, Double> biPredicate) {
        Box box = BoxUtils.createBox8(blockPos, direction);
        Vec3d[] vec3ds = BoxUtils.values3(box);
        double d2 = Double.MAX_VALUE;
        Vec3d vec3d2 = null;
        Vec3d vec3d3 = clientPlayerEntity.getEyePos();
        for (Vec3d vec3d : vec3ds) {
            double d = vec3d3.distanceTo(vec3d);
            if (!biPredicate.test(d2, d)) continue;
            vec3d2 = vec3d;
            d2 = d;
        }
        return vec3d2;
    }

    public void handleBlock(Block block, BlockPos blockPos) {
        this.resolvePlacement8(block, blockPos);
        if (!this.radius.testObject2(0)) {
            AreaScanner.Area area = new AreaScanner.Area();
            area.handleBlockPos3(blockPos, this.radius.getValue());
            area.handleInt4(blockPos.getY());
            for (BlockPos blockPos2 : area) {
                this.resolvePlacement8(block, blockPos2);
            }
        }
    }

    public int computeItemSwitcher(ItemSwitcher itemSwitcher, ClientWorld clientWorld) {
        return itemSwitcher.computePredicate(itemStack -> {
            BlockItem blockItem;
            Item item = itemStack.getItem();
            return item instanceof BlockItem && this.blockBiFunction.apply((blockItem = (BlockItem)item).getBlock(), clientWorld) != false;
        });
    }

    @Override
    public PlacementModule.ItemSelector getItemSelector() {
        return (placementModule, itemSwitcher, item) -> {
            MutableObject mutableObject = new MutableObject(null);
            MutableObject mutableObject2 = new MutableObject((Object)0);
            itemSwitcher.resolveObject(slot -> {
                if (!slot.getStack().isOf(item)) {
                    return null;
                }
                switch (this.autoSelect.getValue().ordinal()) {
                    case 0: {
                        if (mutableObject.getValue() != null) break;
                        mutableObject.setValue(slot);
                        break;
                    }
                    case 1: {
                        int i = slot.getStack().getCount();
                        if (mutableObject.getValue() == null) {
                            mutableObject.setValue(slot);
                            mutableObject2.setValue((Object)i);
                            return null;
                        }
                        if ((Integer)mutableObject2.getValue() >= i) break;
                        mutableObject.setValue(slot);
                        mutableObject2.setValue((Object)i);
                    }
                }
                return null;
            });
            if (mutableObject.getValue() == null) {
                return null;
            }
            return switch (this.getSwitchBackSetting().getValue()) {
                default -> throw new MatchException(null, null);
                case PlacementModule.SwitchBack.Silent -> itemSwitcher.resolveSwitchResult(Hand.MAIN_HAND, (Slot)mutableObject.getValue(), this.getSilentSetting().getValue().getFlag() | 1);
                case PlacementModule.SwitchBack.None -> itemSwitcher.resolveSwitchResult(Hand.MAIN_HAND, (Slot)mutableObject.getValue(), this.getSilentSetting().getValue().getFlag());
                case PlacementModule.SwitchBack.Spoof -> {
                    Slot slot = (Slot)mutableObject.getValue();
                    SilentHotbarModule.getSilentHotbarManager().handleObject3(this, slot, this.getIntegerSetting4().getValue(), new SilentHotbarManager.SelectionConfig[0]);
                    yield ItemSwitcher.SwitchResult.resolveSwitchResult(slot);
                }
            };
        };
    }

    public float computeFloat3(float f, boolean[] flags) {
        return this.computeFloat2(this.computeFloat(f, flags[0], flags[1], flags[2], flags[3]));
    }

    public float computeFloat(float f, boolean flag, boolean flag2, boolean flag3, boolean flag4) {
        float f2 = 0.0f;
        if (flag) {
            if (flag3) {
                f2 = 45.0f;
            } else if (flag4) {
                f2 = -45.0f;
            }
        } else if (flag2) {
            f2 = flag3 ? 135.0f : (flag4 ? -135.0f : 180.0f);
        } else if (flag3) {
            f2 = 90.0f;
        } else if (flag4) {
            f2 = -90.0f;
        }
        return f + 180.0f + f2;
    }

    public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, double d) {
        double[] doubles = PlayerUtils.values2(clientPlayerEntity, d);
        PlayerActionUtils.handleClientPlayerEntity24(clientPlayerEntity, doubles[0]);
        PlayerActionUtils.handleClientPlayerEntity26(clientPlayerEntity, doubles[1]);
    }

    public float[] values4(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
        double d = PlayerUtils.computeClientPlayerEntity6(clientPlayerEntity) + 180.0f;
        float f = Math.round(d / 45.0) * 45L;
        this.flag3 = f % 90.0f != 0.0f;
        return this.flag3 ? this.values2(f) : this.values(clientPlayerEntity, clientWorld, f);
    }

    public Vec3d resolveVec3d3(BlockPos blockPos, Direction direction, double d) {
        double d2 = MathUtils.computeDouble5(d / 6.0, 0.25, 0.85);
        double d3 = MathUtils.computeDouble9(0.0, 0.33);
        return this.resolveVec3d(blockPos, direction, 0.33 + d3, d2, 0.33 + d3);
    }

    @Override
    public void run3() {
        this.flag4 = true;
        this.flag5 = false;
        this.floatValue = Float.NaN;
        if (ScaffoldModule.mc.player != null && ScaffoldModule.mc.player.isOnGround()) {
            this.intValue6 = (int)Math.floor(ScaffoldModule.mc.player.getY() - 1.0);
        }
        if (this.simulate.getValue().booleanValue() && ScaffoldModule.mc.player != null) {
            RotationManager.Rotation rotation = RotationManager.Rotation.createRotation(ScaffoldModule.mc.player.getYaw() + 180.0f, 82.5f);
            rotation.handleFloat(this.floatArray4);
            this.rotationSmoother.handleRotation(rotation);
        }
    }

    public float[] values5(float[] floats) {
        if (this.simulate.getValue().booleanValue()) {
            this.rotationSmoother.handleFloat2(floats);
            RotationManager.Rotation rotation = this.rotationSmoother.createRotation();
            float f = rotation.getFloatValue2();
            float f2 = rotation.getFloatValue();
            return new float[]{f, MathUtils.computeFloat3(f2, -90.0f, 90.0f)};
        }
        return floats;
    }

    public float computeFlag(boolean flag, boolean flag2, boolean flag3, boolean flag4) {
        return flag && (flag3 || flag4) || flag2 && (flag3 || flag4) ? 77.4f : 82.8f;
    }

    public float computeFlag2(boolean[] flags) {
        return this.computeFlag(flags[0], flags[1], flags[2], flags[3]);
    }

    public static ScaffoldModule getScaffoldModule() {
        return scaffoldModule;
    }

    private static enum Mode {
        Plain(true),
        God(false),
        Telly(false),
        Fast(true),
        Multi(true);

        public final boolean auto;

        private Mode(boolean auto) {
            this.auto = auto;
        }

        public boolean isAuto() {
            return this.auto;
        }
    }

    private static enum HitVectorH implements TextProvider,
    HitVector
    {
        Normal("Default center hit vector"),
        Random("Random point"){

            @Override
            public Vec3d auto(PhobosClient phobos, ClientPlayerEntity player, ClientWorld world, BlockPos placeOn, Direction direction, BlockHitResult raytrace, Vec3d origin) {
                return RotationUtils.createVec3d3(placeOn, (World)world, direction, MathUtils::computeDouble9);
            }
        }
        ,
        Far("Farthest edge from your eyes"){

            @Override
            public Vec3d auto(PhobosClient phobos, ClientPlayerEntity player, ClientWorld world, BlockPos placeOn, Direction direction, BlockHitResult raytrace, Vec3d origin) {
                return NullSafe.resolveObject3(this.resolveVec3d2(player, placeOn, direction, (bestDistance, distance) -> distance > bestDistance), origin);
            }
        }
        ,
        Close("Closest edge to your eyes"){

            @Override
            public Vec3d auto(PhobosClient phobos, ClientPlayerEntity player, ClientWorld world, BlockPos placeOn, Direction direction, BlockHitResult raytrace, Vec3d origin) {
                return NullSafe.resolveObject3(this.resolveVec3d2(player, placeOn, direction, (bestDistance, distance) -> distance < bestDistance), origin);
            }
        }
        ,
        Top("Horizontally centered top part of the block"){

            @Override
            public Vec3d auto(PhobosClient phobos, ClientPlayerEntity player, ClientWorld world, BlockPos placeOn, Direction direction, BlockHitResult raytrace, Vec3d origin) {
                return Objects.equals(direction, Direction.UP) || Objects.equals(direction, Direction.DOWN) ? origin : RotationUtils.createVec3d3(placeOn, (World)world, direction, (min, max) -> min + (max - min) * (double)MathUtils.computeFloat8(0.8f, 1.0f));
            }
        }
        ,
        Adjacent("Closest (angle based)"){

            @Override
            public Vec3d auto(PhobosClient phobos, ClientPlayerEntity player, ClientWorld world, BlockPos placeOn, Direction direction, BlockHitResult raytrace, Vec3d origin) {
                float pitch;
                float yaw = phobos.getRotationManager().getFloatValue32();
                BlockHitResult result = this.resolveBlockHitResult(player, world, placeOn, direction, yaw, pitch = phobos.getRotationManager().getFloatValue6());
                if (result != null) {
                    return result.getPos();
                }
                return origin;
            }
        }
        ,
        Centered("Near the center, with 0.2 offset (0.4 total)"){

            @Override
            public Vec3d auto(PhobosClient phobos, ClientPlayerEntity player, ClientWorld world, BlockPos placeOn, Direction direction, BlockHitResult raytrace, Vec3d origin) {
                return RotationUtils.createVec3d3(placeOn, (World)world, direction, (min, max) -> {
                    double absCenter = (min + max) / 2.0;
                    double rMin = absCenter - 0.2;
                    double rMax = absCenter + 0.2;
                    return MathUtils.computeDouble9(rMin, rMax);
                });
            }
        }
        ,
        Free("Uses local facing to check for block intersection, may not be legit"){

            @Override
            public Vec3d auto(PhobosClient phobos, ClientPlayerEntity player, ClientWorld world, BlockPos placeOn, Direction direction, BlockHitResult raytrace, Vec3d origin) {
                return raytrace != null ? raytrace.getPos() : origin;
            }
        };

        public final String text;

        public Vec3d auto(PhobosClient phobos, ClientPlayerEntity player, ClientWorld world, BlockPos placeOn, Direction direction, BlockHitResult raytrace, Vec3d origin) {
            return RotationUtils.createVec3d2(placeOn, (World)world, direction);
        }

        private HitVectorH(String description) {
            this.text = description;
        }

        @Override
        public String getText() {
            return this.text;
        }
    }

    private static enum HitVectorV implements TextProvider,
    HitVector
    {
        Default("Does not modify calculated vector"),
        Center("Default center hit vector"){

            @Override
            public Vec3d mutate(PhobosClient phobos, ClientPlayerEntity player, ClientWorld world, BlockPos placeOn, Direction direction, BlockHitResult raytrace, Vec3d result) {
                return RotationUtils.createVec3d(placeOn, (World)world, direction, result, (min, max) -> (min + max) / 2.0);
            }
        }
        ,
        Random("Random point"){

            @Override
            public Vec3d mutate(PhobosClient phobos, ClientPlayerEntity player, ClientWorld world, BlockPos placeOn, Direction direction, BlockHitResult raytrace, Vec3d result) {
                return RotationUtils.createVec3d(placeOn, (World)world, direction, result, MathUtils::computeDouble9);
            }
        }
        ,
        Grouped("Starts at 0.3, ends at 0.7, near the center"){

            @Override
            public Vec3d mutate(PhobosClient phobos, ClientPlayerEntity player, ClientWorld world, BlockPos placeOn, Direction direction, BlockHitResult raytrace, Vec3d result) {
                return RotationUtils.createVec3d(placeOn, (World)world, direction, result, (min, max) -> {
                    double rMin = min + 0.3;
                    double rMax = max - 0.3;
                    return MathUtils.computeDouble9(rMin, rMax);
                });
            }
        }
        ,
        Outer("0.1 -> 0.3, 0.8 -> 1.0"){

            @Override
            public Vec3d mutate(PhobosClient phobos, ClientPlayerEntity player, ClientWorld world, BlockPos placeOn, Direction direction, BlockHitResult raytrace, Vec3d result) {
                return RotationUtils.createVec3d(placeOn, (World)world, direction, result, (min, max) -> {
                    double rMin = min + 0.3;
                    double rMax = max - 0.3;
                    return MathUtils.computeDouble9(rMin, rMax);
                });
            }
        };

        public final String text;

        public Vec3d mutate(PhobosClient phobos, ClientPlayerEntity player, ClientWorld world, BlockPos placeOn, Direction direction, BlockHitResult raytrace, Vec3d result) {
            return result;
        }

        @Override
        public String getText() {
            return this.text;
        }

        private HitVectorV(String description) {
            this.text = description;
        }
    }

    private static enum StrafeMode {
        None,
        Normal,
        Watchdog;

    }

    private static enum AutoSelectMode {
        None,
        Count;

    }

    private static enum Tower implements TextProvider
    {
        Motion("NCP tower"),
        Vulcan("Vulcan tower"),
        Down("Pulls you down after jumping"),
        Fast("Pulls you down and modifies game time"),
        Watchdog("Watchdog tower"),
        Static("Freezes you after you jump"),
        Modifier("Modifies jump height");

        public final String text;

        @Override
        public String getText() {
            return this.text;
        }

        private Tower(String description) {
            this.text = description;
        }
    }

    private static enum JumpMode {
        Legit,
        Rage,
        KeepY,
        Always,
        Awaiting;

    }

    private static enum Sprint implements TextProvider
    {
        None("No sprint modifications"),
        Client("Only sprints on the client, does not send sprint packets"),
        Disable("Disables sprinting");

        public final String text;

        @Override
        public String getText() {
            return this.text;
        }

        private Sprint(String description) {
            this.text = description;
        }
    }

    private static class ScaffoldingCursor {
        public BlockPos blockPos;
        public final SecureRandom secureRandom;
        public final AnimatedVec3d animatedVec3d = new AnimatedVec3d();

        public Box getBox() {
            Vec3d vec3d = this.createVec3d(this.blockPos);
            return new Box(vec3d, vec3d).expand(0.015);
        }

        public void handleDirection(Direction direction, double d) {
            boolean flag = this.secureRandom.nextBoolean();
            Vec3d vec3d = this.animatedVec3d.createVec3d();
            switch (direction) {
                case UP: 
                case DOWN: {
                    if (flag) {
                        double d2 = vec3d.getX() + d;
                        this.animatedVec3d.handleVec3d(MathUtils.createVec3d2(vec3d, d2));
                        break;
                    }
                    double d3 = vec3d.getZ() + d;
                    this.animatedVec3d.handleVec3d(MathUtils.createVec3d(vec3d, d3));
                    break;
                }
                case SOUTH: 
                case NORTH: {
                    if (flag) {
                        double d4 = vec3d.getY() + d;
                        this.animatedVec3d.handleVec3d(MathUtils.createVec3d3(vec3d, d4));
                        break;
                    }
                    double d5 = vec3d.getX() + d;
                    this.animatedVec3d.handleVec3d(MathUtils.createVec3d2(vec3d, d5));
                    break;
                }
                case EAST: 
                case WEST: {
                    if (flag) {
                        double d6 = vec3d.getY() + d;
                        this.animatedVec3d.handleVec3d(MathUtils.createVec3d3(vec3d, d6));
                        break;
                    }
                    double d7 = vec3d.getZ() + d;
                    this.animatedVec3d.handleVec3d(MathUtils.createVec3d(vec3d, d7));
                    break;
                }
            }
        }

        public BlockPos getBlockPos() {
            return this.blockPos;
        }

        public void handleBlockHitResult(BlockHitResult blockHitResult) {
            this.animatedVec3d.handleVec3d2(RotationUtils.resolveVec3d2(blockHitResult.getBlockPos(), blockHitResult.getPos()));
            this.blockPos = blockHitResult.getBlockPos();
        }

        public ScaffoldingCursor() {
            this.secureRandom = new SecureRandom();
        }

        public void handleFloat(float f) {
            this.animatedVec3d.handleFloat(10.0f, f);
        }

        public SecureRandom getSecureRandom() {
            return this.secureRandom;
        }

        public AnimatedVec3d getAnimatedVec3d() {
            return this.animatedVec3d;
        }

        public Vec3d createVec3d(BlockPos blockPos) {
            Vec3d vec3d = this.animatedVec3d.createVec3d();
            double d = (double)blockPos.getX() + vec3d.getX();
            double d2 = (double)blockPos.getY() + vec3d.getY();
            double d3 = (double)blockPos.getZ() + vec3d.getZ();
            return new Vec3d(d, d2, d3);
        }

        public boolean testDirection(Direction direction) {
            Vec3d vec3d = this.animatedVec3d.createVec3d();
            return switch (direction) {
                default -> throw new MatchException(null, null);
                case Direction.EAST -> {
                    if (vec3d.getX() == 1.0) {
                        yield true;
                    }
                    yield false;
                }
                case Direction.WEST -> {
                    if (vec3d.getX() == 0.0) {
                        yield true;
                    }
                    yield false;
                }
                case Direction.UP -> {
                    if (vec3d.getY() == 1.0) {
                        yield true;
                    }
                    yield false;
                }
                case Direction.DOWN -> {
                    if (vec3d.getY() == 0.0) {
                        yield true;
                    }
                    yield false;
                }
                case Direction.SOUTH -> {
                    if (vec3d.getZ() == 1.0) {
                        yield true;
                    }
                    yield false;
                }
                case Direction.NORTH -> vec3d.getZ() == 0.0;
            };
        }
    }

    private static interface HitVector {
        default public Vec3d resolveVec3d(Vec3d vec3d, Vec3d vec3d2, Function<Vec3d, Float> function) {
            float f2;
            float f = function.apply(vec3d).floatValue();
            return f > (f2 = function.apply(vec3d2).floatValue()) ? vec3d : vec3d2;
        }

        default public double computeDouble(double d) {
            if (d != 1.0) {
                d = d == 0.0 ? 0.25 : 0.75;
            }
            return d;
        }

        default public BlockHitResult resolveBlockHitResult(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, BlockPos blockPos, Direction direction, float f, float f2) {
            Vec3d[] vec3dArray;
            if (direction != Direction.UP && direction != Direction.DOWN) {
                Vec3d[] vec3dArray2 = new Vec3d[4];
                vec3dArray2[0] = RotationUtils.createVec3d4(blockPos, direction, 1.0, 0.0, 1.0);
                vec3dArray2[1] = RotationUtils.createVec3d4(blockPos, direction, 0.0, 0.0, 0.0);
                vec3dArray2[2] = RotationUtils.createVec3d4(blockPos, direction, 1.0, 1.0, 1.0);
                vec3dArray = vec3dArray2;
                vec3dArray2[3] = RotationUtils.createVec3d4(blockPos, direction, 0.0, 1.0, 0.0);
            } else {
                Vec3d[] vec3dArray3 = new Vec3d[4];
                vec3dArray3[0] = RotationUtils.createVec3d4(blockPos, direction, 1.0, 0.0, 1.0);
                vec3dArray3[1] = RotationUtils.createVec3d4(blockPos, direction, 0.0, 0.0, 1.0);
                vec3dArray3[2] = RotationUtils.createVec3d4(blockPos, direction, 1.0, 0.0, 0.0);
                vec3dArray = vec3dArray3;
                vec3dArray3[3] = RotationUtils.createVec3d4(blockPos, direction, 0.0, 0.0, 0.0);
            }
            Vec3d[] vec3ds = vec3dArray;
            MutableObject mutableObject = new MutableObject();
            MutableDouble mutableDouble = new MutableDouble(Double.MAX_VALUE);
            for (Vec3d vec3d2 : vec3ds) {
                float[] floats = RotationUtils.values2((Entity)clientPlayerEntity, vec3d2);
                float f3 = RotationUtils.computeFloat4(f, f2, floats[0], floats[1]);
                if (!((double)f3 < mutableDouble.getValue())) continue;
                mutableDouble.setValue((double)f3);
                mutableObject.setValue((Object)vec3d2);
            }
            if (mutableObject.getValue() == null) {
                return null;
            }
            Vec3d vec3d2 = (Vec3d)mutableObject.getValue();
            Vec3d vec3d3 = this.createVec3d(vec3d2);
            Vec3d vec3d4 = this.resolveVec3d(vec3d2, vec3d3, vec3d -> {
                float[] floats = RotationUtils.values2((Entity)clientPlayerEntity, vec3d);
                return Float.valueOf(RotationUtils.computeFloat4(floats[0], floats[1], f, f2));
            });
            return RaycastUtils.resolveBlockHitResult4((Entity)clientPlayerEntity, (World)clientWorld, blockPos, direction, vec3d4);
        }

        default public Vec3d createVec3d(Vec3d vec3d) {
            return new Vec3d(this.computeDouble(vec3d.getX()), this.computeDouble(vec3d.getY()), this.computeDouble(vec3d.getZ()));
        }

        @Nullable
        default public Vec3d resolveVec3d2(ClientPlayerEntity clientPlayerEntity, BlockPos blockPos, Direction direction, BiPredicate<Double, Double> biPredicate) {
            Box box = BoxUtils.createBox8(blockPos, direction);
            Vec3d[] vec3ds = BoxUtils.values3(box);
            double d2 = Double.MAX_VALUE;
            Vec3d vec3d2 = null;
            Vec3d vec3d3 = clientPlayerEntity.getEyePos();
            for (Vec3d vec3d : vec3ds) {
                double d = vec3d3.distanceTo(vec3d);
                if (!biPredicate.test(d2, d)) continue;
                vec3d2 = vec3d;
                d2 = d;
            }
            return vec3d2;
        }
    }
}
