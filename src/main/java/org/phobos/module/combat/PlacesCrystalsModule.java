package org.phobos.module.combat;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.ExperienceOrbSpawnS2CPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableObject;
import org.earthhack.ducks.IEntity;
import org.earthhack.ducks.IPlayerInteractEntityC2SPacket;
import org.earthhack.ducks.ITotemPoppingEntity;
import org.phobos.api.TextProvider;
import org.phobos.api.EntityTargetFilter;
import org.phobos.api.Visibility;
import org.phobos.api.EntityTargetFilter.Target;
import org.phobos.api.TimeGate.Normal;
import org.phobos.api.TimeGate.Threaded;
import org.phobos.core.placement.DirectionResolver;
import org.phobos.core.placement.ExactDirectionResolver;
import org.phobos.core.placement.ShapeDirectionResolver;
import org.phobos.core.placement.DefaultDirectionResolver;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.MultiSelectSetting;
import org.phobos.core.setting.BindSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.combat.CrystalCalculationTask;
import org.phobos.core.combat.CrystalDamageTracker;
import org.phobos.core.animation.AnimatedDouble;
import org.phobos.core.block.BlockPosGrid;
import org.phobos.core.animation.AnimatedBox;
import org.phobos.core.render.RenderContext;
import org.phobos.core.tick.TickCounter;
import org.phobos.core.render.CameraState;
import org.phobos.core.placement.PlacementResult;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.task.DelayedRunnable;
import org.phobos.core.combat.CrystalSafetyCalculator;
import org.phobos.core.combat.CrystalTargetSelector;
import org.phobos.core.module.Category;
import org.phobos.core.combat.CrystalQueue;
import org.phobos.core.setting.SettingGroup;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.net.PacketListenerBase;
import org.phobos.core.task.TaskScheduler;
import org.phobos.core.block.BlockBreakManager;
import org.phobos.core.bind.KeyCombination;
import org.phobos.core.combat.CrystalPlacement;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.math.BoxUtils;
import org.phobos.core.ClientHolder;
import org.phobos.core.tick.TimeUtils;
import org.phobos.core.math.MathUtils;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.combat.CrystalUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.input.InputUtils;
import org.phobos.core.world.RaycastUtils;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.inventory.InventoryUtils;
import org.phobos.core.event.events.PacketEvent.PostSend;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.core.event.events.PacketEvent.Send;
import org.phobos.core.entity.DeadEntityTracker.SetDeadType;
import org.phobos.core.player.InteractionManager.Silent;
import org.phobos.core.player.InteractionManager.SwitchMode;
import org.phobos.core.block.BlockBreakManager.Progress;
import org.phobos.core.setting.SettingSubsection.Builder;
import org.phobos.core.render.Render3d.Cuboid;
import org.phobos.core.player.PlayerActionUtils.PackedRangeCalculator;
import org.phobos.core.player.PlayerActionUtils.RangeCalculator;
import org.phobos.data.ModuleRecord;
import org.phobos.event.Listener;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.RemoveEntityEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.SetEvent;
import org.phobos.event.TickEvent2;
import org.phobos.mixin.net.interfaces.IPlayerMoveC2SPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.module.client.AntiCheatModule;
import org.phobos.module.player.SpeedmineModule;

public class PlacesCrystalsModule extends PlacementModule implements EntityTargetFilter {
   public Setting<Double> doubleValueSetting2;
   public Setting<Integer> integerSetting5;
   public Setting<Integer> integerSetting6;
   public Setting<Double> doubleValueSetting3;
   public Setting<Boolean> booleanValueSetting10;
   public static final Logger LOGGER2 = LoggerFactory.getLogger(PlacesCrystalsModule.class);
   public Setting<Boolean> booleanValueSetting11;
   public Setting<Boolean> booleanValueSetting12;
   public Setting<Boolean> booleanValueSetting13;
   public Setting<PlacesCrystalsModule.Rotate> rotateSetting;
   public Setting<Double> doubleValueSetting4;
   public static final Identifier identifier = Identifier.of("phobos", "textures/crystal.png");
   public Setting<Boolean> booleanValueSetting14;
   public Setting<PlacesCrystalsModule.CalculationType> calculationTypeSetting;
   public Setting<Boolean> booleanValueSetting15;
   public Setting<PlacesCrystalsModule.EvaluationType> evaluationTypeSetting;
   public Setting<Boolean> booleanValueSetting16;
   public static final double doubleValue = 144.0;
   public Setting<Boolean> booleanValueSetting17;
   public static final double doubleValue2 = 324.0;
   public Setting<SetDeadType> setDeadTypeSetting;
   public Setting<Map<RangeCalculator, Boolean>> mapSetting;
   public Setting<Double> doubleValueSetting5;
   public Setting<Boolean> booleanValueSetting18;
   public Setting<Long> longValueSetting2;
   public Setting<Boolean> booleanValueSetting19;
   public Setting<Map<RangeCalculator, Boolean>> mapSetting2;
   public Setting<Double> doubleValueSetting6;
   public Setting<PlacesCrystalsModule.SwingType> swingTypeSetting;
   public Setting<PlacesCrystalsModule.SwingType> swingTypeSetting2;
   public Setting<Boolean> booleanValueSetting20;
   public Setting<Float> floatValueSetting2;
   public Setting<Integer> integerSetting7;
   public Setting<Integer> integerSetting8;
   public Setting<Integer> integerSetting9;
   public Setting<Double> doubleValueSetting7;
   public Setting<Integer> integerSetting10;
   public Setting<PlacesCrystalsModule.Calc> calcSetting;
   public Setting<Double> doubleValueSetting8;
   public Setting<Float> floatValueSetting3;
   public Setting<Boolean> booleanValueSetting21;
   public Setting<Map<PlacesCrystalsModule.Addon, Boolean>> mapSetting3;
   public Setting<Boolean> booleanValueSetting22;
   public Setting<Integer> integerSetting11;
   public Setting<Float> floatValueSetting4;
   public Setting<Float> floatValueSetting5;
   public Setting<Boolean> booleanValueSetting23;
   public Setting<Long> longValueSetting3;
   public Setting<PlacesCrystalsModule.Sequential> sequentialSetting;
   public Setting<Boolean> booleanValueSetting24;
   public Setting<Silent> silentSetting2;
   public Setting<SwitchMode> switchModeSetting;
   public Setting<Long> longValueSetting4;
   public Setting<Long> longValueSetting5;
   public Setting<Boolean> booleanValueSetting25;
   public Setting<Float> floatValueSetting6;
   public Setting<Double> doubleValueSetting9;
   public Setting<PlacesCrystalsModule.TimingPreset> timingPresetSetting;
   public Setting<Boolean> booleanValueSetting26;
   public Setting<Integer> integerSetting12;
   public Setting<Boolean> booleanValueSetting27;
   public Setting<SwitchMode> switchModeSetting2;
   public Setting<Boolean> booleanValueSetting28;
   public Setting<Integer> integerSetting13;
   public Setting<Float> floatValueSetting7;
   public Setting<Double> doubleValueSetting10;
   public Setting<Boolean> booleanValueSetting29;
   public Setting<Double> doubleValueSetting11;
   public Setting<PlacesCrystalsModule.Deflector> deflectorSetting;
   public Setting<Long> longValueSetting6;
   public Setting<Boolean> booleanValueSetting30;
   public Setting<Float> floatValueSetting8;
   public Setting<Silent> silentSetting3;
   public Setting<Boolean> booleanValueSetting31;
   public Setting<Boolean> booleanValueSetting32;
   public Setting<Boolean> booleanValueSetting33;
   public Setting<Boolean> booleanValueSetting34;
   public Setting<Double> doubleValueSetting12;
   public Setting<Integer> integerSetting14;
   public Setting<Boolean> booleanValueSetting35;
   public Setting<Boolean> booleanValueSetting36;
   public Setting<Boolean> booleanValueSetting37;
   public Setting<Boolean> booleanValueSetting38;
   public Setting<Boolean> booleanValueSetting39;
   public Setting<Color> colorSetting3;
   public Setting<Boolean> booleanValueSetting40;
   public Setting<Boolean> booleanValueSetting41;
   public Setting<Long> longValueSetting7;
   public Setting<Boolean> booleanValueSetting42;
   public Setting<Float> floatValueSetting9;
   public Setting<Color> colorSetting4;
   public Setting<Boolean> booleanValueSetting43;
   public Setting<Long> longValueSetting8;
   public Setting<Color> colorSetting5;
   public Setting<Boolean> booleanValueSetting44;
   public Setting<Boolean> booleanValueSetting45;
   public Setting<Boolean> booleanValueSetting46;
   public Setting<Double> doubleValueSetting13;
   public Setting<KeyCombination> keyCombinationSetting;
   public Setting<Float> floatValueSetting10;
   public Setting<Boolean> booleanValueSetting47;
   public CrystalCalculationTask crystalCalculationTask;
   public Setting<Color> colorSetting6;
   public Setting<Integer> integerSetting15;
   public Setting<Boolean> booleanValueSetting48;
   public Setting<PlacesCrystalsModule.ExtendSwing> extendSwingSetting;
   public BlockPosGrid<CrystalPlacement> crystalPlacementBlockPosGrid;
   public Setting<Float> floatValueSetting11;
   public Setting<Boolean> booleanValueSetting49;
   public Setting<Boolean> booleanValueSetting50;
   public Setting<Integer> integerSetting16;
   public Setting<Map<Target, Boolean>> mapSetting4;
   public Setting<Long> longValueSetting9;
   public Setting<Boolean> booleanValueSetting51;
   public Setting<Integer> integerSetting17;
   public Setting<Boolean> booleanValueSetting52;
   public Setting<Integer> integerSetting18;
   public Setting<Boolean> booleanValueSetting53;
   public Setting<Boolean> booleanValueSetting54;
   public Setting<Boolean> booleanValueSetting55;
   public MutableObject<Float> floatValueMutableObject;
   public AnimatedDouble animatedDouble2;
   public volatile CrystalPlacement crystalPlacement;
   public volatile int intValue;
   public Threaded threaded;
   public Normal normal3;
   public MutableObject<String> textMutableObject;
   public Set<Integer> integerSet;
   public Map<BlockPos, Long> blockPosMap;
   public Threaded threaded2;
   public AnimatedDouble animatedDouble3;
   public MutableObject<EndCrystalEntity> endCrystalEntityMutableObject;
   public boolean flag2;
   public Entity entity;
   public MutableObject<Float> floatValueMutableObject2;
   public Threaded threaded3;
   public Normal normal4;
   public AtomicInteger atomicInteger2;
   public ModuleRecord moduleRecord;
   public volatile BlockPos blockPos2;
   public Normal normal5;
   public List<Entity> entityList;
   public Threaded threaded4;
   public Threaded threaded5;
   public Normal normal6;
   public volatile boolean flag3;
   public boolean flag4;
   public AnimatedBox animatedBox;
   public CameraState cameraState;
   public Set<EndCrystalEntity> endCrystalEntitySet;
   public Threaded threaded6;
   public boolean flag5;
   public BlockPos blockPos3;
   public CrystalDamageTracker crystalDamageTracker;
   public boolean flag6;
   public Entity entity2;
   public CrystalQueue crystalQueue;
   public BlockPos blockPos4;
   public long longValue;
   public FloatAnimator floatAnimator;
   private static Object[] phobos$g1806ddfd0;
   private static Object[] phobos$g1806de180;
   private static Object[] phobos$g1806df870;
   private static Object[] phobos$g1806e0140;
   private static Object[] phobos$g1806e1788;
   private static Object[] phobos$g1806e1908;
   private static Object[] phobos$g1806dd628;
   private static Object[] phobos$g1806dd720;
   private static Object[] phobos$g1806dd818;

   public PlacesCrystalsModule(PhobosClient phobosClient, String text, Category category) {
      super(phobosClient, text, "Places crystals", category, 10);
      this.nativeInit2(phobosClient, text, category);
   }

   public void nativeInit2(final PhobosClient var1, String var2, Category var3) {
      Boolean var5 = false & true;
      BooleanSetting var4 = new BooleanSetting("StrictDirection", var5);
      Setting var6 = var4.resolveSetting7("Strict direction");
      Setting var7 = this.resolveSetting2(var6);
      this.booleanValueSetting15 = var7;
      Boolean var9 = false & true;
      BooleanSetting var8 = new BooleanSetting("StrictDirectionGrim", var9);
      Setting var10 = this.resolveSetting2(var8);
      this.booleanValueSetting16 = var10;
      Boolean var12 = false & true;
      BooleanSetting var11 = new BooleanSetting("Rotate", var12);
      Setting var13 = var11.resolveSetting7("Rotate");
      Setting var14 = this.resolveSetting2(var13);
      this.booleanValueSetting17 = var14;
      PlacesCrystalsModule.Rotate var16 = PlacesCrystalsModule.Rotate.Normal;
      EnumSetting var15 = new EnumSetting<>("RotateType", var16);
      Setting var17 = var15.resolveSetting7("Rotation type");
      Setting var18 = this.resolveSetting2(var17);
      this.rotateSetting = var18;
      BooleanSetting var19 = new BooleanSetting("RotateConfirm");
      Setting var20 = this.rotateSetting;
      PlacesCrystalsModule.Rotate[] var21 = new PlacesCrystalsModule.Rotate[2];
      PlacesCrystalsModule.Rotate var22 = PlacesCrystalsModule.Rotate.Normal;
      var21[0] = var22;
      PlacesCrystalsModule.Rotate var23 = PlacesCrystalsModule.Rotate.Hybrid;
      var21[1] = var23;
      Visibility var24 = Visibility.resolveVisibility3(var20, var21);
      Setting var25 = var19.resolveSetting3(var24);
      Setting var26 = this.resolveSetting2(var25);
      this.booleanValueSetting11 = var26;
      BooleanSetting var27 = new BooleanSetting("RotateStrict");
      Visibility var44 = this::check5;
      Setting var45 = var27.resolveSetting3(var44);
      Setting var46 = var45.resolveSetting7("Additional raytrace check");
      Setting var47 = this.resolveSetting2(var46);
      this.booleanValueSetting14 = var47;
      Integer var49 = 1;
      Integer var50 = -1;
      Integer var51 = 50;
      NumberSetting var48 = new NumberSetting<>("RotateLimit", var49, var50, var51);
      Integer var52 = -1;
      NumberSetting var53 = var48.resolveNumberSetting("Off", var52);
      Visibility var70 = this::check;
      Setting var71 = var53.resolveSetting3(var70);
      Setting var72 = var71.resolveSetting7("Rotations/Tick");
      Setting var73 = this.resolveSetting2(var72);
      this.integerSetting6 = var73;
      BooleanSetting var74 = new BooleanSetting("InteractionRaytrace");
      Setting var75 = this.resolveSetting2(var74);
      this.booleanValueSetting12 = var75;
      Integer var77 = 2;
      Integer var78 = 0;
      Integer var79 = 4;
      NumberSetting var76 = new NumberSetting<>("InteractionRaytraceDepth", var77, var78, var79);
      Setting var80 = this.resolveSetting2(var76);
      this.integerSetting5 = var80;
      BooleanSetting var81 = new BooleanSetting("RaytraceBypass");
      Setting var82 = this.resolveSetting2(var81);
      this.booleanValueSetting13 = var82;
      BooleanSetting var83 = new BooleanSetting("IgnoreY");
      Setting var84 = var83.resolveSetting7("Ignores entities if they are above certain height");
      Setting var85 = this.resolveSetting2(var84);
      this.booleanValueSetting10 = var85;
      Double var87 = 61.0;
      Double var88 = -64.0;
      Double var89 = 330.0;
      NumberSetting var86 = new NumberSetting<>("IgnoreYY", var87, var88, var89);
      Setting var90 = var86.resolveSetting7("Ignore height");
      Setting var91 = this.resolveSetting2(var90);
      this.doubleValueSetting3 = var91;
      PlacesCrystalsModule.EvaluationType var93 = PlacesCrystalsModule.EvaluationType.Damage;
      EnumSetting var92 = new EnumSetting<>("Evaluation", var93);
      Setting var94 = this.resolveSetting2(var92);
      this.evaluationTypeSetting = var94;
      PlacesCrystalsModule.CalculationType var96 = PlacesCrystalsModule.CalculationType.Damage;
      EnumSetting var95 = new EnumSetting<>("Calculation", var96);
      Setting var97 = this.resolveSetting2(var95);
      this.calculationTypeSetting = var97;
      Double var99 = 6.0;
      Double var100 = 0.1;
      Double var101 = 36.0;
      NumberSetting var98 = new NumberSetting<>("MinDamage", var99, var100, var101);
      Setting var102 = var98.resolveSetting7("Minimum damage");
      Setting var103 = this.calculationTypeSetting;
      PlacesCrystalsModule.CalculationType var104 = PlacesCrystalsModule.CalculationType.Damage;
      Setting var105 = var102.resolveSetting4(var103, var104);
      Setting var106 = this.resolveSetting2(var105);
      this.doubleValueSetting2 = var106;
      Double var108 = 12.0;
      Double var109 = 0.1;
      Double var110 = 36.0;
      NumberSetting var107 = new NumberSetting<>("MaxSelfDamage", var108, var109, var110);
      Setting var111 = var107.resolveSetting7("Maximum damage to local player");
      Setting var112 = this.calculationTypeSetting;
      PlacesCrystalsModule.CalculationType var113 = PlacesCrystalsModule.CalculationType.Damage;
      Setting var114 = var111.resolveSetting4(var112, var113);
      Setting var115 = this.resolveSetting2(var114);
      this.doubleValueSetting4 = var115;
      Float var117 = (float)2.0F;
      Float var118 = (float)0.1F;
      Float var119 = (float)10.0F;
      NumberSetting var116 = new NumberSetting<>("Balance", var117, var118, var119);
      Setting var120 = this.calculationTypeSetting;
      PlacesCrystalsModule.CalculationType var121 = PlacesCrystalsModule.CalculationType.Balance;
      Setting var122 = var116.resolveSetting4(var120, var121);
      Setting var123 = this.resolveSetting2(var122);
      this.floatValueSetting3 = var123;
      Float var125 = (float)2.0F;
      Float var126 = (float)0.1F;
      Float var127 = (float)10.0F;
      NumberSetting var124 = new NumberSetting<>("Factor", var125, var126, var127);
      Setting var128 = this.calculationTypeSetting;
      PlacesCrystalsModule.CalculationType var129 = PlacesCrystalsModule.CalculationType.Balance;
      Setting var130 = var124.resolveSetting4(var128, var129);
      Setting var131 = this.resolveSetting2(var130);
      this.floatValueSetting2 = var131;
      Boolean var133 = false & true;
      BooleanSetting var132 = new BooleanSetting("Suicide", var133);
      Setting var134 = var132.resolveSetting7("Ignores damage dealt to local player");
      Setting var135 = this.resolveSetting2(var134);
      this.booleanValueSetting18 = var135;
      BooleanSetting var136 = new BooleanSetting("Lethal");
      Setting var137 = var136.resolveSetting7("Places ignoring self damage, if it kills the enemy and not you");
      Setting var138 = this.resolveSetting2(var137);
      this.booleanValueSetting20 = var138;
      Integer var140 = 1;
      Integer var141 = 1;
      Integer var142 = 20;
      NumberSetting var139 = new NumberSetting<>("PlaceAttempts", var140, var141, var142);
      Setting var143 = var139.resolveSetting7("Sends additional packets");
      Setting var144 = this.resolveSetting2(var143);
      this.integerSetting8 = var144;
      MultiSelectSetting var145 = new MultiSelectSetting<>("PlaceType", RangeCalculator.class);
      RangeCalculator[] var146 = RangeCalculator.values();
      Function[] var147 = new Function[0];
      MultiSelectSetting var148 = var145.resolveMultiSelectSetting6(var146, var147);
      RangeCalculator var149 = RangeCalculator.Standing;
      MultiSelectSetting var150 = var148.resolveMultiSelectSetting3(var149);
      Setting var151 = this.resolveSetting2(var150);
      this.mapSetting2 = var151;
      Double var153 = 4.2;
      Double var154 = 0.0;
      Double var155 = 6.0;
      NumberSetting var152 = new NumberSetting<>("PlaceRange", var153, var154, var155);
      Double var156 = 0.0;
      NumberSetting var157 = var152.resolveNumberSetting("None", var156);
      Setting var158 = var157.resolveSetting7("Place range");
      Setting var159 = this.resolveSetting2(var158);
      this.doubleValueSetting8 = var159;
      Double var161 = 4.2;
      Double var162 = 0.0;
      Double var163 = 6.0;
      NumberSetting var160 = new NumberSetting<>("PlaceWallRange", var161, var162, var163);
      Double var164 = 0.0;
      NumberSetting var165 = var160.resolveNumberSetting("None", var164);
      Setting var166 = var165.resolveSetting7("Place range through walls");
      Setting var167 = this.resolveSetting2(var166);
      this.doubleValueSetting6 = var167;
      Integer var169 = 0;
      Integer var170 = 0;
      Integer var171 = 16;
      NumberSetting var168 = new NumberSetting<>("PlaceMultipoint", var169, var170, var171);
      Integer var172 = 0;
      NumberSetting var173 = var168.resolveNumberSetting("Single", var172);
      Setting var174 = this.resolveSetting2(var173);
      this.integerSetting7 = var174;
      PlacesCrystalsModule.SwingType var176 = PlacesCrystalsModule.SwingType.Auto;
      EnumSetting var175 = new EnumSetting<>("PlaceSwing", var176);
      Setting var177 = var175.resolveSetting7("Place swing");
      Setting var178 = this.resolveSetting2(var177);
      this.swingTypeSetting = var178;
      Integer var180 = 1;
      Integer var181 = 1;
      Integer var182 = 20;
      NumberSetting var179 = new NumberSetting<>("BreakAttempts", var180, var181, var182);
      Setting var183 = var179.resolveSetting7("Sends additional packets");
      Setting var184 = this.resolveSetting2(var183);
      this.integerSetting10 = var184;
      MultiSelectSetting var185 = new MultiSelectSetting<>("BreakType", RangeCalculator.class);
      RangeCalculator[] var186 = RangeCalculator.values();
      Function[] var187 = new Function[0];
      MultiSelectSetting var188 = var185.resolveMultiSelectSetting6(var186, var187);
      RangeCalculator var189 = RangeCalculator.Standing;
      MultiSelectSetting var190 = var188.resolveMultiSelectSetting3(var189);
      Setting var191 = this.resolveSetting2(var190);
      this.mapSetting = var191;
      Double var193 = 3.0;
      Double var194 = 0.0;
      Double var195 = 6.0;
      NumberSetting var192 = new NumberSetting<>("BreakRange", var193, var194, var195);
      Double var196 = 3.1;
      NumberSetting var197 = var192.resolveNumberSetting("Grim", var196);
      Double var198 = 0.0;
      NumberSetting var199 = var197.resolveNumberSetting("None", var198);
      Setting var200 = var199.resolveSetting7("Break range");
      Setting var201 = this.resolveSetting2(var200);
      this.doubleValueSetting5 = var201;
      Double var203 = 3.0;
      Double var204 = 0.0;
      Double var205 = 6.0;
      NumberSetting var202 = new NumberSetting<>("BreakWallRange", var203, var204, var205);
      Double var206 = 3.1;
      NumberSetting var207 = var202.resolveNumberSetting("Grim", var206);
      Double var208 = 0.0;
      NumberSetting var209 = var207.resolveNumberSetting("None", var208);
      Setting var210 = var209.resolveSetting7("Break range through walls");
      Setting var211 = this.resolveSetting2(var210);
      this.doubleValueSetting7 = var211;
      Integer var213 = 0;
      Integer var214 = 0;
      Integer var215 = 16;
      NumberSetting var212 = new NumberSetting<>("BreakMultipoint", var213, var214, var215);
      Integer var216 = 0;
      NumberSetting var217 = var212.resolveNumberSetting("Single", var216);
      Setting var218 = this.resolveSetting2(var217);
      this.integerSetting9 = var218;
      PlacesCrystalsModule.SwingType var220 = PlacesCrystalsModule.SwingType.Auto;
      EnumSetting var219 = new EnumSetting<>("BreakSwing", var220);
      Setting var221 = var219.resolveSetting7("Break swing");
      Setting var222 = this.resolveSetting2(var221);
      this.swingTypeSetting2 = var222;
      BooleanSetting var223 = new BooleanSetting("SetDead");
      Setting var224 = var223.resolveSetting7("Removes crystals");
      Setting var225 = this.resolveSetting2(var224);
      this.booleanValueSetting19 = var225;
      LongNumberSetting var226 = new LongNumberSetting("SetDeadTime", 50L, 0L, 1000L, true & true);
      Setting var227 = this.resolveSetting2(var226);
      this.longValueSetting2 = var227;
      SetDeadType var229 = SetDeadType.Visibility;
      EnumSetting var228 = new EnumSetting<>("SetDeadType", var229);
      Setting var230 = this.resolveSetting2(var228);
      this.setDeadTypeSetting = var230;
      PlacesCrystalsModule.Calc var232 = PlacesCrystalsModule.Calc.Fast;
      EnumSetting var231 = new EnumSetting<>("Calculation", var232);
      Setting var233 = this.resolveSetting2(var231);
      this.calcSetting = var233;
      PlacesCrystalsModule.Sequential var235 = PlacesCrystalsModule.Sequential.Strong;
      EnumSetting var234 = new EnumSetting<>("Sequential", var235);
      Setting var236 = this.resolveSetting2(var234);
      this.sequentialSetting = var236;
      PlacesCrystalsModule.TimingPreset var238 = PlacesCrystalsModule.TimingPreset.NCP;
      EnumSetting var237 = new EnumSetting<>("TimingsPreset", var238);
      Setting var239 = this.resolveSetting2(var237);
      this.timingPresetSetting = var239;
      LongNumberSetting var240 = new LongNumberSetting("Attack", 100L, 0L, 500L, true & true);
      Setting var241 = var240.resolveSetting7("Synchronized attack delay");
      Setting var242 = this.timingPresetSetting;
      PlacesCrystalsModule.TimingPreset[] var243 = new PlacesCrystalsModule.TimingPreset[1];
      PlacesCrystalsModule.TimingPreset var244 = PlacesCrystalsModule.TimingPreset.Custom;
      var243[0] = var244;
      Visibility var245 = Visibility.resolveVisibility3(var242, var243);
      Setting var246 = var241.resolveSetting3(var245);
      Setting var247 = this.resolveSetting2(var246);
      this.longValueSetting5 = var247;
      LongNumberSetting var248 = new LongNumberSetting("Stuck", 1500L, 0L, 5000L, true & true);
      Setting var249 = var248.resolveSetting7("Timeout to consider crystal as stuck");
      Setting var250 = this.resolveSetting2(var249);
      this.longValueSetting3 = var250;
      LongNumberSetting var251 = new LongNumberSetting("Replace", 25L, -1L, 250L, true & true);
      Long var252 = -1L;
      NumberSetting var253 = var251.resolveNumberSetting("Instant", var252);
      Setting var254 = var253.resolveSetting7("After-break replace delay");
      Setting var255 = this.timingPresetSetting;
      PlacesCrystalsModule.TimingPreset[] var256 = new PlacesCrystalsModule.TimingPreset[1];
      PlacesCrystalsModule.TimingPreset var257 = PlacesCrystalsModule.TimingPreset.Custom;
      var256[0] = var257;
      Visibility var258 = Visibility.resolveVisibility3(var255, var256);
      Setting var259 = var254.resolveSetting3(var258);
      Setting var260 = this.resolveSetting2(var259);
      this.longValueSetting4 = var260;
      BooleanSetting var261 = new BooleanSetting("FriendProtect");
      Setting var262 = var261.resolveSetting7("Ensures your friends won't die");
      Setting var263 = this.resolveSetting2(var262);
      this.booleanValueSetting25 = var263;
      Float var265 = (float)10.0F;
      Float var266 = (float)0.1F;
      Float var267 = (float)36.0F;
      NumberSetting var264 = new NumberSetting<>("FriendProtectDamage", var265, var266, var267);
      Setting var268 = var264.resolveSetting7("Max damage for a position to be considered invalid (stacked)");
      Setting var269 = this.resolveSetting2(var268);
      this.floatValueSetting5 = var269;
      SwitchMode var271 = SwitchMode.Off;
      EnumSetting var270 = new EnumSetting<>("AntiWeaknessMode", var271);
      Setting var272 = var270.resolveSetting7("Weakness potion effect workaround");
      Setting var273 = this.resolveSetting2(var272);
      this.switchModeSetting = var273;
      Silent var275 = Silent.Normal;
      EnumSetting var274 = new EnumSetting<>("AntiWeaknessSilent", var275);
      Setting var276 = this.switchModeSetting;
      SwitchMode var277 = SwitchMode.Silent;
      Setting var278 = var274.resolveSetting4(var276, var277);
      Setting var279 = var278.resolveSetting7("Switch type");
      Setting var280 = this.resolveSetting2(var279);
      this.silentSetting2 = var280;
      Integer var282 = 0;
      Integer var283 = 0;
      Integer var284 = 10;
      NumberSetting var281 = new NumberSetting<>("Ticks", var282, var283, var284);
      Setting var285 = var281.resolveSetting7("Ticks existed");
      Setting var286 = this.resolveSetting2(var285);
      this.integerSetting12 = var286;
      Boolean var288 = false & true;
      BooleanSetting var287 = new BooleanSetting("Inhibit", var288);
      Setting var289 = var287.resolveSetting7("Makes sure you can't attack the same crystal twice");
      Setting var290 = this.resolveSetting2(var289);
      this.booleanValueSetting26 = var290;
      MultiSelectSetting var291 = new MultiSelectSetting<>("Addons", PlacesCrystalsModule.Addon.class);
      Consumer<MultiSelectSetting> var308 = PlacesCrystalsModule::handleMultiSelectSetting2;
      MultiSelectSetting var309 = var291.resolveMultiSelectSetting5(var308);
      Setting var310 = this.resolveSetting2(var309);
      this.mapSetting3 = var310;
      Boolean var312 = false & true;
      BooleanSetting var311 = new BooleanSetting("Outplace", var312);
      Setting var313 = var311.resolveSetting7("Faceplaces when unsafe");
      Setting var314 = this.resolveSetting2(var313);
      this.booleanValueSetting24 = var314;
      Float var316 = (float)6.0F;
      Float var317 = (float)1.0F;
      Float var318 = (float)16.0F;
      NumberSetting var315 = new NumberSetting<>("OutplaceDamage", var316, var317, var318);
      Setting var319 = this.resolveSetting2(var315);
      this.floatValueSetting4 = var319;
      Float var321 = (float)3.0F;
      Float var322 = (float)1.0F;
      Float var323 = (float)12.0F;
      NumberSetting var320 = new NumberSetting<>("OutplaceMax", var321, var322, var323);
      Setting var324 = this.resolveSetting2(var320);
      this.floatValueSetting6 = var324;
      BooleanSetting var325 = new BooleanSetting("OutplaceFriends");
      Setting var326 = this.resolveSetting2(var325);
      this.booleanValueSetting23 = var326;
      Boolean var328 = false & true;
      BooleanSetting var327 = new BooleanSetting("Items", var328);
      Setting var329 = var327.resolveSetting7("Attempts to explode items");
      Setting var330 = this.resolveSetting2(var329);
      this.booleanValueSetting21 = var330;
      Integer var332 = 8;
      Integer var333 = 1;
      Integer var334 = 64;
      NumberSetting var331 = new NumberSetting<>("ItemsQuantity", var332, var333, var334);
      Setting var335 = var331.resolveSetting7("Minimum stack size to explode");
      Setting var336 = this.resolveSetting2(var335);
      this.integerSetting11 = var336;
      Double var338 = 4.0;
      Double var339 = 0.1;
      Double var340 = 6.0;
      NumberSetting var337 = new NumberSetting<>("ItemsRange", var338, var339, var340);
      Setting var341 = var337.resolveSetting7("Range from an enemy");
      Setting var342 = this.resolveSetting2(var341);
      this.doubleValueSetting9 = var342;
      Boolean var344 = false & true;
      BooleanSetting var343 = new BooleanSetting("Deflector", "Attempts to deflect projectiles such as arrows or ender pearls", var344);
      Setting var345 = this.resolveSetting2(var343);
      this.booleanValueSetting22 = var345;
      BooleanSetting var346 = new BooleanSetting("DeflectorArrows");
      Setting var347 = this.resolveSetting2(var346);
      this.booleanValueSetting30 = var347;
      BooleanSetting var348 = new BooleanSetting("DeflectorPearls");
      Setting var349 = this.resolveSetting2(var348);
      this.booleanValueSetting35 = var349;
      PlacesCrystalsModule.Deflector var351 = PlacesCrystalsModule.Deflector.Boost;
      EnumSetting var350 = new EnumSetting<>("DeflectorMode", var351);
      Setting var352 = this.resolveSetting2(var350);
      this.deflectorSetting = var352;
      Float var354 = (float)8.0F;
      Float var355 = (float)0.1F;
      Float var356 = (float)16.0F;
      NumberSetting var353 = new NumberSetting<>("DeflectorDamage", var354, var355, var356);
      Setting var357 = var353.resolveSetting7("Minimum damage to the projectile");
      Setting var358 = this.resolveSetting2(var357);
      this.floatValueSetting8 = var358;
      Float var360 = (float)2.0F;
      Float var361 = (float)1.0F;
      Float var362 = (float)10.0F;
      NumberSetting var359 = new NumberSetting<>("DeflectorReduction", var360, var361, var362);
      Setting var363 = var359.resolveSetting7("Ensures you don't deflect over player damage");
      Setting var364 = this.resolveSetting2(var363);
      this.floatValueSetting7 = var364;
      Boolean var366 = false & true;
      BooleanSetting var365 = new BooleanSetting("MultiTask", var366);
      Setting var367 = var365.resolveSetting7("Places while using");
      Setting var368 = this.resolveSetting2(var367);
      this.booleanValueSetting27 = var368;
      SwitchMode var370 = SwitchMode.Normal;
      EnumSetting var369 = new EnumSetting<>("Switch", var370);
      Setting var371 = var369.resolveSetting7("Switch type");
      Setting var372 = this.resolveSetting2(var371);
      this.switchModeSetting2 = var372;
      Silent var374 = Silent.Normal;
      EnumSetting var373 = new EnumSetting<>("Silent", var374);
      Setting var375 = this.switchModeSetting2;
      SwitchMode[] var376 = new SwitchMode[1];
      SwitchMode var377 = SwitchMode.Silent;
      var376[0] = var377;
      Visibility var378 = this.resolveVisibility2(var375, var376);
      Setting var379 = var373.resolveSetting3(var378);
      Setting var380 = var379.resolveSetting7("Swap back");
      Setting var381 = this.resolveSetting2(var380);
      this.silentSetting3 = var381;
      BooleanSetting var382 = new BooleanSetting("Smart");
      Setting var383 = var382.resolveSetting7("Switches to a gap when holding right click with end crystal");
      Visibility var400 = this::check8;
      Setting var401 = var383.resolveSetting3(var400);
      Setting var402 = this.resolveSetting2(var401);
      this.booleanValueSetting33 = var402;
      BooleanSetting var403 = new BooleanSetting("Back");
      Setting var404 = this.switchModeSetting2;
      SwitchMode var405 = SwitchMode.Normal;
      Setting var406 = var403.resolveSetting4(var404, var405);
      Setting var407 = this.resolveSetting2(var406);
      this.booleanValueSetting32 = var407;
      BooleanSetting var408 = new BooleanSetting("Simulate");
      Setting var409 = var408.resolveSetting7("Purely visual");
      Setting var410 = this.resolveSetting2(var409);
      this.booleanValueSetting28 = var410;
      Integer var412 = 0;
      Integer var413 = 0;
      Integer var414 = 100;
      NumberSetting var411 = new NumberSetting<>("SimulateTicks", var412, var413, var414);
      Setting var415 = var411.resolveSetting7("Simulation age in ticks");
      Setting var416 = this.resolveSetting2(var415);
      this.integerSetting13 = var416;
      Integer var418 = -1;
      Integer var419 = 0;
      Integer var420 = 100;
      NumberSetting var417 = new NumberSetting<>("Steps", var418, var419, var420);
      Setting var421 = var417.resolveSetting7("Simulation steps");
      Setting var422 = this.resolveSetting2(var421);
      this.integerSetting14 = var422;
      BooleanSetting var423 = new BooleanSetting("Sound");
      Setting var424 = var423.resolveSetting7("Simulate sounds");
      Setting var425 = this.resolveSetting2(var424);
      this.booleanValueSetting29 = var425;
      Boolean var427 = false & true;
      BooleanSetting var426 = new BooleanSetting("Cooldown", var427);
      Setting var428 = var426.resolveSetting7("Swap cooldown");
      Setting var429 = this.resolveSetting2(var428);
      this.booleanValueSetting31 = var429;
      Double var431 = 2.0;
      Double var432 = 0.0;
      Double var433 = 10.0;
      NumberSetting var430 = new NumberSetting<>("CooldownDelay", var431, var432, var433);
      Setting var434 = this.resolveSetting2(var430);
      this.doubleValueSetting10 = var434;
      Boolean var436 = true & true;
      BooleanSetting var435 = new BooleanSetting("Faceplace", var436);
      Setting var437 = var435.resolveSetting7("Places at fallback positions");
      Setting var438 = this.resolveSetting2(var437);
      this.booleanValueSetting34 = var438;
      Double var440 = 10.0;
      Double var441 = 0.0;
      Double var442 = 36.0;
      NumberSetting var439 = new NumberSetting<>("FaceplaceHealth", var440, var441, var442);
      Setting var443 = this.resolveSetting2(var439);
      this.doubleValueSetting12 = var443;
      LongNumberSetting var444 = new LongNumberSetting("FaceplaceDelay", 450L, 0L, 2000L);
      Long var445 = 450L;
      NumberSetting var446 = var444.resolveNumberSetting("Sync", var445);
      Setting var447 = this.resolveSetting2(var446);
      this.longValueSetting6 = var447;
      Double var449 = 7.5;
      Double var450 = 0.0;
      Double var451 = 100.0;
      NumberSetting var448 = new NumberSetting<>("Armor", var449, var450, var451, "%");
      Setting var452 = this.resolveSetting2(var448);
      this.doubleValueSetting11 = var452;
      Boolean var454 = false & true;
      BooleanSetting var453 = new BooleanSetting("Idle", var454);
      Setting var455 = var453.resolveSetting7("Places all the time");
      Setting var456 = this.resolveSetting2(var455);
      this.booleanValueSetting44 = var456;
      BooleanSetting var457 = new BooleanSetting("Collide");
      Setting var458 = var457.resolveSetting7("Faceplaces if a player collides with a wall next tick");
      Setting var459 = this.resolveSetting2(var458);
      this.booleanValueSetting41 = var459;
      BindSetting var460 = new BindSetting("FaceplaceKey");
      Setting var461 = this.resolveSetting2(var460);
      this.keyCombinationSetting = var461;
      Boolean var463 = false & true;
      BooleanSetting var462 = new BooleanSetting("Base", var463);
      Setting var464 = var462.resolveSetting7("Automatically places obsidian base");
      Setting var465 = this.resolveSetting2(var464);
      this.booleanValueSetting38 = var465;
      LongNumberSetting var466 = new LongNumberSetting("BaseDelay", 500L, 0L, 3000L, true & true);
      Setting var467 = this.resolveSetting2(var466);
      this.longValueSetting7 = var467;
      Double var469 = 10.0;
      Double var470 = 0.1;
      Double var471 = 36.0;
      NumberSetting var468 = new NumberSetting<>("BaseDamage", var469, var470, var471);
      Setting var472 = this.resolveSetting2(var468);
      this.doubleValueSetting13 = var472;
      Boolean var474 = true & true;
      BooleanSetting var473 = new BooleanSetting("Render", var474);
      Setting var475 = this.resolveSetting2(var473);
      this.booleanValueSetting40 = var475;
      BooleanSetting var476 = new BooleanSetting("Interpolate");
      Setting var477 = this.resolveSetting2(var476);
      this.booleanValueSetting36 = var477;
      Float var479 = (float)0.1F;
      Float var480 = (float)0.05F;
      Float var481 = (float)0.5F;
      NumberSetting var478 = new NumberSetting<>("InterpolateSpeed", var479, var480, var481);
      Setting var482 = this.resolveSetting2(var478);
      this.floatValueSetting10 = var482;
      Boolean var484 = false & true;
      BooleanSetting var483 = new BooleanSetting("Fade", var484);
      Setting var485 = var483.resolveSetting7("Fade animation");
      Setting var486 = this.resolveSetting2(var485);
      this.booleanValueSetting45 = var486;
      Boolean var488 = false & true;
      BooleanSetting var487 = new BooleanSetting("FadeAll", var488);
      Setting var489 = var487.resolveSetting7("Applies animation on all positions");
      Setting var490 = this.resolveSetting2(var489);
      this.booleanValueSetting43 = var490;
      LongNumberSetting var491 = new LongNumberSetting("FadeTime", 250L, 0L, 1000L, true & true);
      Setting var492 = var491.resolveSetting7("Fade time");
      Setting var493 = this.resolveSetting2(var492);
      this.longValueSetting8 = var493;
      BooleanSetting var494 = new BooleanSetting("FadeIn");
      Setting var495 = this.resolveSetting2(var494);
      this.booleanValueSetting42 = var495;
      Color var497 = new Color(255, 255, 255, 100);
      ColorSetting var496 = new ColorSetting("RenderBox", var497);
      ColorSetting var498 = var496.getColorSetting();
      Setting var499 = this.resolveSetting2(var498);
      this.colorSetting3 = var499;
      Color var501 = new Color(255, 255, 255, 255);
      ColorSetting var500 = new ColorSetting("RenderLine", var501);
      ColorSetting var502 = var500.getColorSetting();
      Setting var503 = this.resolveSetting2(var502);
      this.colorSetting4 = var503;
      Float var505 = (float)1.0F;
      Float var506 = (float)0.0F;
      Float var507 = (float)2.0F;
      NumberSetting var504 = new NumberSetting<>("RenderWidth", var505, var506, var507);
      Setting var508 = var504.resolveSetting7("Line width");
      Setting var509 = this.resolveSetting2(var508);
      this.floatValueSetting9 = var509;
      BooleanSetting var510 = new BooleanSetting("Damage");
      Setting var511 = var510.resolveSetting7("Displays damage");
      Setting var512 = this.resolveSetting2(var511);
      this.booleanValueSetting37 = var512;
      Color var514 = Color.WHITE;
      ColorSetting var513 = new ColorSetting("DamageColor", var514);
      Setting var515 = this.resolveSetting2(var513);
      this.colorSetting5 = var515;
      BooleanSetting var516 = new BooleanSetting("Font");
      Setting var517 = var516.resolveSetting7("Uses custom font");
      Setting var518 = this.resolveSetting2(var517);
      this.booleanValueSetting39 = var518;
      BooleanSetting var519 = new BooleanSetting("Shadow");
      Setting var520 = this.resolveSetting2(var519);
      this.booleanValueSetting46 = var520;
      BooleanSetting var521 = new BooleanSetting("Indicator");
      Setting var522 = this.resolveSetting2(var521);
      this.booleanValueSetting50 = var522;
      Color var524 = Color.WHITE;
      ColorSetting var523 = new ColorSetting("IndicatorColor", var524);
      ColorSetting var525 = var523.getColorSetting();
      Setting var526 = this.resolveSetting2(var525);
      this.colorSetting6 = var526;
      Float var528 = (float)1.0F;
      Float var529 = (float)0.1F;
      Float var530 = (float)3.0F;
      NumberSetting var527 = new NumberSetting<>("Scale", var528, var529, var530);
      Setting var531 = var527.resolveSetting7("2D rendering scale");
      Setting var532 = this.booleanValueSetting37;
      Visibility var533 = Visibility.resolveVisibility2(var532);
      Setting var534 = this.booleanValueSetting50;
      Visibility var535 = Visibility.resolveVisibility2(var534);
      Visibility var536 = var533.resolveVisibility(var535);
      Setting var537 = var531.resolveSetting3(var536);
      Setting var538 = this.resolveSetting2(var537);
      this.floatValueSetting11 = var538;
      BooleanSetting var539 = new BooleanSetting("Prediction");
      Setting var540 = var539.resolveSetting7("Player prediction");
      Setting var541 = this.resolveSetting2(var540);
      this.booleanValueSetting54 = var541;
      Integer var543 = 2;
      Integer var544 = 0;
      Integer var545 = 7;
      NumberSetting var542 = new NumberSetting<>("PredictionPlace", var543, var544, var545);
      Integer var546 = 7;
      NumberSetting var547 = var542.resolveNumberSetting("Latency", var546);
      Setting var548 = var547.resolveSetting7("Prediction ticks for place calculation");
      Setting var549 = this.resolveSetting2(var548);
      this.integerSetting15 = var549;
      Integer var551 = 0;
      Integer var552 = 0;
      Integer var553 = 7;
      NumberSetting var550 = new NumberSetting<>("PredictionBreak", var551, var552, var553);
      Integer var554 = 7;
      NumberSetting var555 = var550.resolveNumberSetting("Latency", var554);
      Setting var556 = var555.resolveSetting7("Prediction ticks for break calculation");
      Setting var557 = this.resolveSetting2(var556);
      this.integerSetting16 = var557;
      BooleanSetting var558 = new BooleanSetting("PredictionSelf");
      Setting var559 = var558.resolveSetting7("Predict local players movement");
      Setting var560 = this.resolveSetting2(var559);
      this.booleanValueSetting49 = var560;
      BooleanSetting var561 = new BooleanSetting("PredictionValidity");
      Setting var562 = var561.resolveSetting7("Applies prediction when calculating entity hitboxes");
      Setting var563 = this.resolveSetting2(var562);
      this.booleanValueSetting48 = var563;
      BooleanSetting var564 = new BooleanSetting("Extend");
      Setting var565 = var564.resolveSetting7("Predicts entity id");
      Setting var566 = this.resolveSetting2(var565);
      this.booleanValueSetting51 = var566;
      BooleanSetting var567 = new BooleanSetting("ExtendSafe");
      Setting var568 = this.resolveSetting2(var567);
      this.booleanValueSetting53 = var568;
      BooleanSetting var569 = new BooleanSetting("ExtendDynamic");
      Setting var570 = var569.resolveSetting7("Fail-safe prediction based on totem pops");
      Setting var571 = this.resolveSetting2(var570);
      this.booleanValueSetting47 = var571;
      LongNumberSetting var572 = new LongNumberSetting("ExtendDynamicMax", 500L, 50L, 3000L, true & true);
      Setting var573 = var572.resolveSetting7("Time delta");
      Setting var574 = this.resolveSetting2(var573);
      this.longValueSetting9 = var574;
      Integer var576 = 1;
      Integer var577 = 1;
      Integer var578 = 40;
      NumberSetting var575 = new NumberSetting<>("ExtendPackets", var576, var577, var578);
      Setting var579 = var575.resolveSetting7("Prediction amount");
      Setting var580 = this.resolveSetting2(var579);
      this.integerSetting17 = var580;
      Integer var582 = 0;
      Integer var583 = 0;
      Integer var584 = 20;
      NumberSetting var581 = new NumberSetting<>("ExtendOffset", var582, var583, var584);
      Setting var585 = var581.resolveSetting7("ID offset");
      Setting var586 = this.resolveSetting2(var585);
      this.integerSetting18 = var586;
      BooleanSetting var587 = new BooleanSetting("ExtendUpdate");
      Setting var588 = this.resolveSetting2(var587);
      this.booleanValueSetting55 = var588;
      PlacesCrystalsModule.ExtendSwing var590 = PlacesCrystalsModule.ExtendSwing.Always;
      EnumSetting var589 = new EnumSetting<>("ExtendSwing", var590);
      Setting var591 = this.resolveSetting2(var589);
      this.extendSwingSetting = var591;
      MultiSelectSetting var592 = new MultiSelectSetting<>("Targets", Target.class);
      Consumer<MultiSelectSetting> var609 = PlacesCrystalsModule::handleMultiSelectSetting;
      MultiSelectSetting var610 = var592.resolveMultiSelectSetting5(var609);
      Setting var611 = this.resolveSetting2(var610);
      this.mapSetting4 = var611;
      BooleanSetting var612 = new BooleanSetting("Debug");
      Setting var613 = this.resolveSetting2(var612);
      this.booleanValueSetting52 = var613;
      CrystalCalculationTask var614 = new CrystalCalculationTask(this);
      this.crystalCalculationTask = var614;
      Function<BlockPos, CrystalPlacement> var632 = CrystalPlacement::new;
      CrystalPlacement[] var633 = new CrystalPlacement[0];
      BlockPosGrid var615 = new BlockPosGrid<>(7, var632, var633);
      this.crystalPlacementBlockPosGrid = var615;
      Normal var634 = new Normal();
      this.normal3 = var634;
      Threaded var635 = new Threaded();
      this.threaded2 = var635;
      Threaded var636 = new Threaded();
      this.threaded3 = var636;
      Threaded var637 = new Threaded();
      this.threaded = var637;
      Normal var638 = new Normal();
      this.normal4 = var638;
      MutableObject var639 = new MutableObject(null);
      this.textMutableObject = var639;
      Float var641 = (float)0.0F;
      MutableObject var640 = new MutableObject<>(var641);
      this.floatValueMutableObject2 = var640;
      Float var643 = (float)0.0F;
      MutableObject var642 = new MutableObject<>(var643);
      this.floatValueMutableObject = var642;
      HashSet var644 = new HashSet();
      this.integerSet = var644;
      AnimatedDouble var645 = new AnimatedDouble();
      this.animatedDouble2 = var645;
      AnimatedDouble var646 = new AnimatedDouble();
      this.animatedDouble3 = var646;
      PhobosClient var648 = this.getPhobosClient();
      ModuleRecord var647 = new ModuleRecord(this, var648);
      this.moduleRecord = var647;
      MutableObject var649 = new MutableObject();
      this.endCrystalEntityMutableObject = var649;
      HashMap var650 = new HashMap(8);
      this.blockPosMap = var650;
      AtomicInteger var651 = new AtomicInteger(0);
      this.atomicInteger2 = var651;
      BlockPos var652 = new BlockPos(0, 64536, 0);
      this.blockPos2 = var652;
      this.crystalPlacement = (CrystalPlacement)null;
      this.flag2 = false & true;
      Normal var653 = new Normal();
      this.normal6 = var653;
      Threaded var654 = new Threaded();
      this.threaded4 = var654;
      Threaded var655 = new Threaded();
      this.threaded6 = var655;
      Threaded var656 = new Threaded();
      this.threaded5 = var656;
      Normal var657 = new Normal();
      this.normal5 = var657;
      ArrayList var658 = new ArrayList();
      this.entityList = var658;
      KeySetView var659 = ConcurrentHashMap.newKeySet();
      this.endCrystalEntitySet = var659;
      PhobosClient var661 = this.phobosClient;
      Setting var662 = this.integerSetting13;
      Setting var663 = this.booleanValueSetting29;
      CrystalQueue var660 = new CrystalQueue(var661, var662, var663);
      this.crystalQueue = var660;
      CrystalDamageTracker var664 = new CrystalDamageTracker();
      this.crystalDamageTracker = var664;
      CameraState var665 = new CameraState();
      this.cameraState = var665;
      AnimatedBox var666 = new AnimatedBox();
      this.animatedBox = var666;
      FloatAnimator var667 = new FloatAnimator();
      this.floatAnimator = var667;
      this.run5();
      var var668 = new SafeListener<Send<UpdateSelectedSlotC2SPacket>>() {
         public void handleObject(
            Send<UpdateSelectedSlotC2SPacket> send,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            PlacesCrystalsModule.this.normal3.run();
         }
      };
      this.handleListener(var668);
      var var669 = new Listener<TickEvent2>() {
         public void handleObject2(TickEvent2 tickEvent2) {
            PlacesCrystalsModule.this.crystalQueue.run();
            PlacesCrystalsModule.this.atomicInteger2.set(0);
            if (PlacesCrystalsModule.this.threaded3.testLong(500L)) {
               PlacesCrystalsModule.this.textMutableObject.setValue(null);
               PlacesCrystalsModule.this.entity2 = null;
            }
         }
      };
      this.handleListener(var669);
      var var670 = new SafeListener<TickCounter>() {
         public void handleObject(
            TickCounter tickCounter,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (PlacesCrystalsModule.this.calcSetting.testObject2(PlacesCrystalsModule.Calc.Fast)) {
               PlacesCrystalsModule.this.crystalCalculationTask
                  .handleClientPlayerEntity2(
                     clientPlayerEntity,
                     PlacesCrystalsModule.this.threaded3.testLong(100L) && PlacesCrystalsModule.this.threaded2.testLong(100L) ? 50 : 100,
                     true
                  );
            }
         }
      };
      this.handleListener(var670);
      var var671 = new SafeListener<PlayerTickEvent>(16) {
         public void handleObject(
            PlayerTickEvent playerTickEvent,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (PlacesCrystalsModule.this.calcSetting.testObject2(PlacesCrystalsModule.Calc.Sync)) {
               PlacesCrystalsModule.this.crystalCalculationTask
                  .handleClientPlayerEntity2(
                     clientPlayerEntity,
                     PlacesCrystalsModule.this.threaded3.testLong(100L) && PlacesCrystalsModule.this.threaded2.testLong(100L) ? 50 : 100,
                     true
                  );
            }

            CrystalTargetSelector crystalTargetSelector = PlacesCrystalsModule.this.createValue(clientWorld, clientPlayerEntity);
            crystalTargetSelector.run3();
            if (PlacesCrystalsModule.this.booleanValueSetting33.getValue()
               && (
                  PlacesCrystalsModule.this.switchModeSetting2.testObject2(SwitchMode.Off)
                     || PlacesCrystalsModule.this.switchModeSetting2.testObject2(SwitchMode.Normal)
               )
               && clientPlayerEntity.getMainHandStack().isOf(Items.END_CRYSTAL)
               && mc.options.useKey.isPressed()) {
               var1.getInventoryManager().handleConsumer2(itemSwitcher -> itemSwitcher.handleRunnable3(() -> {
                  Slot slot = itemSwitcher.resolveObject(slotx -> slotx.getStack().isOf(Items.ENCHANTED_GOLDEN_APPLE) ? slotx : null);
                  if (slot != null) {
                     InputUtils.handleSlot(slot);
                  }
               }));
            }

            if (PlacesCrystalsModule.this.booleanValueSetting17.getValue()) {
               if (PlacesCrystalsModule.this.testClientPlayerEntity5(clientPlayerEntity, clientWorld, crystalTargetSelector)) {
                  return;
               }

               if (PlacesCrystalsModule.this.blockPos2.getY() != -1000
                  && !PlacesCrystalsModule.this.threaded3.testLong(150L)
                  && !PlacesCrystalsModule.this.rotateSetting.getValue().equals(PlacesCrystalsModule.Rotate.Silent)) {
                  float[] floats = RotationUtils.values7(
                     clientPlayerEntity,
                     PlacesCrystalsModule.this.blockPos2.getX() + 0.5,
                     PlacesCrystalsModule.this.blockPos2.getY(),
                     PlacesCrystalsModule.this.blockPos2.getZ() + 0.5
                  );
                  var1.getRotationManager().testFloat(floats[0], floats[1]);
                  return;
               }
            }
         }
      };
      this.handleListener(var671);
      var var672 = new SafeListener<RenderContext>() {
         public void handleObject(
            RenderContext renderContext,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            PlacesCrystalsModule.this.crystalQueue.run3();
            if (PlacesCrystalsModule.this.booleanValueSetting40.getValue()) {
               PlacesCrystalsModule.this.animatedDouble2
                  .handleDouble4(PlacesCrystalsModule.this.entity2 != null ? PlacesCrystalsModule.this.floatValueMutableObject2.getValue().floatValue() : 0.0);
               PlacesCrystalsModule.this.animatedDouble3
                  .handleDouble4(PlacesCrystalsModule.this.entity2 != null ? PlacesCrystalsModule.this.floatValueMutableObject.getValue().floatValue() : 0.0);
               PlacesCrystalsModule.this.animatedDouble2.handleFloat(10.0F, 0.2F);
               PlacesCrystalsModule.this.animatedDouble3.handleFloat(10.0F, 0.2F);
               PlacesCrystalsModule.this.handleRenderContext(renderContext, clientPlayerEntity);
            }
         }
      };
      this.handleListener(var672);
      List var673 = this.getListenerList();
      var var674 = new PacketListenerBase<EntitySpawnS2CPacket>(EntitySpawnS2CPacket.class) {
         @Override
         public void handleReceive(Receive<EntitySpawnS2CPacket> receive) {
            ClientWorld clientWorld = mc.world;
            ClientPlayerEntity clientPlayerEntity = mc.player;
            ClientPlayerInteractionManager clientPlayerInteractionManager = mc.interactionManager;
            if (PlacesCrystalsModule.this.flag2) {
               PlacesCrystalsModule.this.flag2 = false;
            } else if (clientWorld != null && clientPlayerEntity != null && clientPlayerInteractionManager != null) {
               PlacesCrystalsModule.Sequential sequential = PlacesCrystalsModule.this.sequentialSetting.getValue();
               if (!sequential.equals(PlacesCrystalsModule.Sequential.None)) {
                  if (receive.getPacket().getEntityType().equals(EntityType.END_CRYSTAL)) {
                     double d = receive.getPacket().getX();
                     double d2 = receive.getPacket().getY();
                     double d3 = receive.getPacket().getZ();
                     EndCrystalEntity endCrystalEntity = new EndCrystalEntity(clientWorld, d, d2, d3);
                     endCrystalEntity.setId(receive.getPacket().getEntityId());
                     if (PlacesCrystalsModule.this.booleanValueSetting28.getValue()) {
                        PlacesCrystalsModule.this.crystalQueue.handleEndCrystalEntity2(endCrystalEntity);
                     }

                     if (!PlacesCrystalsModule.this.testClientPlayerEntity4(clientPlayerEntity, clientWorld, endCrystalEntity)) {
                        return;
                     }

                     PlacesCrystalsModule.this.handlePacketListenerBase(
                        this, clientPlayerEntity, clientWorld, clientPlayerInteractionManager, endCrystalEntity, sequential, false
                     );
                  }
               }
            }
         }
      };
      List var675 = var674.getListenerList();
      var673.addAll(var675);
      var var676 = new Listener<Receive<?>>() {
         public void handleObject2(Receive<?> receive) {
            switch (receive.getPacket()) {
               case EntitySpawnS2CPacket entitySpawnS2CPacket:
                  PlacesCrystalsModule.this.handleInt(entitySpawnS2CPacket.getEntityId());
                  break;
               case ExperienceOrbSpawnS2CPacket experienceOrbSpawnS2CPacket:
                  PlacesCrystalsModule.this.handleInt(experienceOrbSpawnS2CPacket.getEntityId());
                  break;
               default:
            }
         }
      };
      this.handleListener(var676);
      var var677 = new SafeListener<Send<HandSwingC2SPacket>>(Integer.MIN_VALUE) {
         public void handleObject(
            Send<HandSwingC2SPacket> send,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (PlacesCrystalsModule.this.flag3) {
               send.setCancelled(true);
               PlacesCrystalsModule.this.flag3 = false;
            }
         }
      };
      this.handleListener(var677);
      var var678 = new SafeListener<Send<PlayerInteractEntityC2SPacket>>() {
         public void handleObject(
            Send<PlayerInteractEntityC2SPacket> send,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (((IPlayerInteractEntityC2SPacket)(Object)send.getPacket()).entity() instanceof EndCrystalEntity endCrystalEntity) {
               int i = endCrystalEntity.age;
               int i2 = endCrystalEntity.getId();
               if (PlacesCrystalsModule.this.booleanValueSetting31.getValue()
                  && !PlacesCrystalsModule.this.normal3.testLong((long)(PlacesCrystalsModule.this.doubleValueSetting10.getValue() * 25.0))) {
                  send.setCancelled(true);
                  PlacesCrystalsModule.this.flag3 = true;
               } else if (!PlacesCrystalsModule.this.integerSetting12.getValue().equals(0) && i < PlacesCrystalsModule.this.integerSetting12.getValue()) {
                  send.setCancelled(true);
                  PlacesCrystalsModule.this.flag3 = true;
               } else {
                  if (PlacesCrystalsModule.this.booleanValueSetting26.getValue()) {
                     if (PlacesCrystalsModule.this.integerSet.remove(i2)) {
                        send.setCancelled(true);
                        PlacesCrystalsModule.this.flag3 = true;
                        return;
                     }

                     PlacesCrystalsModule.this.integerSet.add(i2);
                  }
               }
            }
         }
      };
      this.handleListener(var678);
      var var679 = new SafeListener<RemoveEntityEvent>() {
         public void handleObject(
            RemoveEntityEvent removeEntityEvent,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            PlacesCrystalsModule.this.crystalDamageTracker.handleRemoveEntityEvent(removeEntityEvent);
            if (PlacesCrystalsModule.this.booleanValueSetting26.getValue() && removeEntityEvent.getEntity() instanceof EndCrystalEntity endCrystalEntity) {
               PlacesCrystalsModule.this.integerSet.remove(endCrystalEntity.getId());
            }
         }
      };
      this.handleListener(var679);
      var var680 = new SafeListener<SetEvent>() {
         public void handleObject(
            SetEvent setEvent, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            PlacesCrystalsModule.this.crystalDamageTracker.handleSetEvent(setEvent);
         }
      };
      this.handleListener(var680);
      var var681 = new SafeListener<SpeedmineModule.BreakEvent>() {
         public void handleObject(
            SpeedmineModule.BreakEvent breakEvent,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            PlacesCrystalsModule.this.normal4.run();
         }
      };
      this.handleListener(var681);
      var var682 = new SafeListener<Send<?>>() {
         public void handleObject(
            Send<?> send, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (PlacesCrystalsModule.this.booleanValueSetting12.getValue() && PlacesCrystalsModule.this.booleanValueSetting13.getValue()) {
               if (send.getPacket() instanceof PlayerMoveC2SPacket playerMoveC2SPacket) {
                  PlayerEntity playerEntity = var1.getMovementSimulator().resolvePlayerEntity(clientPlayerEntity);
                  if (playerEntity.getPitch() != -91.0F) {
                     ((IPlayerMoveC2SPacket)(Object)playerMoveC2SPacket).setPitch(-91.0F);
                  }
               }
            }
         }
      };
      this.handleListener(var682);
      var var683 = new SafeListener<PostSend<PlayerInteractBlockC2SPacket>>() {
         public void handleObject(
            PostSend<PlayerInteractBlockC2SPacket> postSend,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (PlacesCrystalsModule.this.booleanValueSetting51.getValue()) {
               Hand hand = postSend.getPacket().getHand();
               if (clientPlayerEntity.getStackInHand(hand).isOf(Items.END_CRYSTAL)) {
                  if (!PlacesCrystalsModule.this.booleanValueSetting53.getValue() || PlacesCrystalsModule.this.testList(clientWorld.getPlayers(), clientWorld)) {
                     if (PlacesCrystalsModule.this.booleanValueSetting47.getValue()) {
                        if (PlacesCrystalsModule.this.entity2 == null
                           || !(PlacesCrystalsModule.this.entity2 instanceof ITotemPoppingEntity iTotemPoppingEntity)) {
                           return;
                        }

                        long l = iTotemPoppingEntity.lastTotemPop().value();
                        if (l <= 50L || l > 50L + PlacesCrystalsModule.this.longValueSetting9.getValue()) {
                           return;
                        }
                     }

                     if (PlacesCrystalsModule.this.booleanValueSetting55.getValue()) {
                        for (Entity entity : clientWorld.getEntities()) {
                           PlacesCrystalsModule.this.handleInt(entity.getId());
                        }
                     }

                     Runnable runnableArg = () -> {
                        switch ((PlacesCrystalsModule.SwingType)PlacesCrystalsModule.this.swingTypeSetting2.getValue()) {
                           case Auto:
                              clientPlayerEntity.swingHand(hand);
                              break;
                           case Mainhand:
                              clientPlayerEntity.swingHand(Hand.MAIN_HAND);
                              break;
                           case None:
                              clientPlayerEntity.swingHand(Hand.OFF_HAND);
                              break;
                           case Offhand:
                              clientPlayerEntity.networkHandler.sendPacket(new HandSwingC2SPacket(hand));
                        }
                     };
                     boolean flag = PlacesCrystalsModule.this.extendSwingSetting.testObject2(PlacesCrystalsModule.ExtendSwing.Always);

                     for (int i2 = 1 - PlacesCrystalsModule.this.integerSetting18.getValue(); i2 < PlacesCrystalsModule.this.integerSetting17.getValue(); i2++) {
                        Entity entity2 = clientWorld.getEntityById(PlacesCrystalsModule.this.intValue);
                        if (entity2 == null || entity2 instanceof EndCrystalEntity) {
                           int i = PlacesCrystalsModule.this.intValue + i2;
                           clientPlayerEntity.networkHandler
                              .sendPacket(new PlayerInteractEntityC2SPacket(i, clientPlayerEntity.isSneaking(), PlayerInteractEntityC2SPacket.ATTACK));
                           if (flag) {
                              runnableArg.run();
                           }
                        }
                     }

                     runnableArg.run();
                  }
               }
            }
         }
      };
      this.handleListener(var683);
   }

   public void handlePacketListenerBase(
      PacketListenerBase<?> packetListenerBase,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager,
      EndCrystalEntity endCrystalEntity,
      PlacesCrystalsModule.Sequential sequential,
      boolean flag
   ) {
      try {
         CrystalSafetyCalculator crystalSafetyCalculator = this.createCrystalSafetyCalculator(clientWorld, clientPlayerEntity);
         MutableObject<Float> mutableObject = new MutableObject<>(0.0F);
         if (this.testCrystalSafetyCalculator(crystalSafetyCalculator, endCrystalEntity, mutableObject) && this.alwaysTrue2()) {
            float[] floats = RotationUtils.values3(clientPlayerEntity, endCrystalEntity, this.integerSetting9.getValue());
            MutableBoolean mutableBoolean = new MutableBoolean(false);
            if (this.booleanValueSetting17.getValue() && !RaycastUtils.testPhobosClient(this.phobosClient, endCrystalEntity)) {
               if (!this.rotateSetting.testObject3(PlacesCrystalsModule.Rotate.Silent)) {
                  return;
               }

               if (!this.integerSetting6.getValue().equals(-1) && this.atomicInteger2.get() > this.integerSetting6.getValue()) {
                  return;
               }

               this.phobosClient.getRotationManager().handleClientPlayerEntity3(clientPlayerEntity, floats[0], floats[1]);
               this.atomicInteger2.incrementAndGet();
               mutableBoolean.setTrue();
            }

            switch ((PlacesCrystalsModule.Rotate)this.rotateSetting.getValue()) {
               case Hybrid:
               case Normal:
                  this.phobosClient.getRotationManager().testFloat(floats[0], floats[1]);
               default:
                  this.handleClientPlayerEntity3(clientPlayerEntity, endCrystalEntity, false);
                  this.crystalDamageTracker.handleEntity(this.entity2, endCrystalEntity, mutableObject.getValue(), this.getBiConsumer());
                  if (this.booleanValueSetting17.getValue() && this.rotateSetting.getValue().reset && mutableBoolean.getValue()) {
                     this.phobosClient.getRotationManager().handleClientPlayerEntity5(clientPlayerEntity);
                  }

                  this.handleEndCrystalEntity3(endCrystalEntity, false);
                  this.threaded2.run();
                  long l = TimeUtils.value();
                  packetListenerBase.handleThreadExecutor(
                     mc,
                     () -> {
                        Entity entity = clientWorld.getEntityById(endCrystalEntity.getId());
                        this.entity = entity;
                        if (entity instanceof IEntity iEntity) {
                           iEntity.setAttackTime(l);
                        }

                        if (!sequential.equals(PlacesCrystalsModule.Sequential.Strict)) {
                           long l2 = this.longValueSetting4.getValue();
                           if (l2 < 0L) {
                              this.crystalCalculationTask.handleClientPlayerEntity(clientPlayerEntity, 0, true, crystalTargetSelector -> crystalTargetSelector.setFlag2(false));
                           } else {
                              this.phobosClient
                                 .getTaskScheduler()
                                 .handleLong3(
                                    l2,
                                    this,
                                    () -> this.crystalCalculationTask
                                       .handleClientPlayerEntity(clientPlayerEntity, 0, true, crystalTargetSelector -> crystalTargetSelector.setFlag2(false))
                                 );
                           }
                        }
                     }
                  );
            }
         }
      } catch (Exception exception) {
         if (!flag && !mc.isOnThread()) {
            mc.submit(
               () -> this.handlePacketListenerBase(
                  packetListenerBase, clientPlayerEntity, clientWorld, clientPlayerInteractionManager, endCrystalEntity, sequential, true
               )
            );
         }
      }
   }

   public void handleClientPlayerEntity3(ClientPlayerEntity clientPlayerEntity, Entity entity, boolean flag) {
      if (this.booleanValueSetting27.getValue() || !clientPlayerEntity.isUsingItem()) {
         PhobosClient phobosClient = ClientHolder.getPhobosClient();
         if (flag && this.booleanValueSetting17.getValue() && !RaycastUtils.testPhobosClient(phobosClient, entity)) {
            float[] floats = RotationUtils.values3(clientPlayerEntity, entity, this.integerSetting9.getValue());
            switch ((PlacesCrystalsModule.Rotate)this.rotateSetting.getValue()) {
               case Hybrid:
               case Normal:
                  if (!phobosClient.getRotationManager().isFlag2()) {
                     return;
                  }

                  phobosClient.getRotationManager().testFloat(floats[0], floats[1]);
                  break;
               case Silent:
                  if (!this.integerSetting6.testObject3(-1) && this.atomicInteger2.get() > this.integerSetting6.getValue()) {
                     return;
                  }

                  phobosClient.getRotationManager().handleClientPlayerEntity3(clientPlayerEntity, floats[0], floats[1]);
                  this.atomicInteger2.incrementAndGet();
            }
         }

         int i = this.integerSetting10.getValue();
         if (i == 1) {
            phobosClient.getInteractionManager().handleEntity(entity, clientPlayerEntity, this.switchModeSetting.getValue(), this.silentSetting2.getValue());
            Hand hand = clientPlayerEntity.getOffHandStack().isOf(Items.END_CRYSTAL) ? Hand.OFF_HAND : Hand.MAIN_HAND;
            switch ((PlacesCrystalsModule.SwingType)this.swingTypeSetting2.getValue()) {
               case Auto:
                  clientPlayerEntity.swingHand(hand);
                  break;
               case Mainhand:
                  clientPlayerEntity.swingHand(Hand.MAIN_HAND);
                  break;
               case None:
                  clientPlayerEntity.swingHand(Hand.OFF_HAND);
                  break;
               case Offhand:
                  clientPlayerEntity.networkHandler.sendPacket(new HandSwingC2SPacket(hand));
            }
         } else {
            DelayedRunnable.handleRunnable(() -> {
               phobosClient.getInteractionManager().handleEntity(entity, clientPlayerEntity, this.switchModeSetting.getValue(), this.silentSetting2.getValue());
               Hand handx = clientPlayerEntity.getOffHandStack().isOf(Items.END_CRYSTAL) ? Hand.OFF_HAND : Hand.MAIN_HAND;
               switch ((PlacesCrystalsModule.SwingType)this.swingTypeSetting2.getValue()) {
                  case Auto:
                     clientPlayerEntity.swingHand(handx);
                     break;
                  case Mainhand:
                     clientPlayerEntity.swingHand(Hand.MAIN_HAND);
                     break;
                  case None:
                     clientPlayerEntity.swingHand(Hand.OFF_HAND);
                     break;
                  case Offhand:
                     clientPlayerEntity.networkHandler.sendPacket(new HandSwingC2SPacket(handx));
               }
            }, i);
            if (flag && this.booleanValueSetting17.getValue() && this.rotateSetting.getValue().reset()) {
               phobosClient.getRotationManager().handleClientPlayerEntity5(clientPlayerEntity);
            }
         }
      }
   }

   public RangeCalculator[] values() {
      Set<RangeCalculator> set = new HashSet<>();

      for (Entry<RangeCalculator, Boolean> entry : this.mapSetting.getValue().entrySet()) {
         if (entry.getValue()) {
            set.add(entry.getKey());
         }
      }

      return set.isEmpty() ? new RangeCalculator[]{RangeCalculator.Standing} : set.toArray(RangeCalculator[]::new);
   }

   @Override
   public DirectionResolver getDirectionResolver() {
      return this.booleanValueSetting15.getValue()
         ? (this.booleanValueSetting16.getValue() ? ExactDirectionResolver.exactDirectionResolver : ShapeDirectionResolver.shapeDirectionResolver)
         : DefaultDirectionResolver.defaultDirectionResolver;
   }

   public CrystalSafetyCalculator createCrystalSafetyCalculator(ClientWorld clientWorld, ClientPlayerEntity clientPlayerEntity) {
      return new CrystalSafetyCalculator(this, this.getPhobosClient(), clientPlayerEntity, clientWorld, this.phobosClient.getDamageCalculator());
   }

   public void handleCrystalPlacement(
      CrystalPlacement crystalPlacement, MutableObject<CrystalPlacement> mutableObject, MutableObject<CrystalPlacement> mutableObject2, boolean flag
   ) {
      switch ((PlacesCrystalsModule.EvaluationType)this.evaluationTypeSetting.getValue()) {
         case Ratio:
            if (crystalPlacement.isFlag42() && !flag) {
               if (crystalPlacement.testCrystalPlacement(mutableObject2.getValue())) {
                  mutableObject2.setValue(crystalPlacement);
               }
            } else if (crystalPlacement.testCrystalPlacement(mutableObject.getValue())) {
               mutableObject.setValue(crystalPlacement);
            }
            break;
         case Damage:
            if (crystalPlacement.isFlag42() && !flag) {
               if (crystalPlacement.testCrystalPlacement3(mutableObject2.getValue(), this.floatValueSetting3.getValue())) {
                  mutableObject2.setValue(crystalPlacement);
               }
            } else if (crystalPlacement.testCrystalPlacement3(mutableObject.getValue(), this.floatValueSetting3.getValue())) {
               mutableObject.setValue(crystalPlacement);
            }
      }
   }

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, CrystalPlacement crystalPlacement) {
      if (this.booleanValueSetting18.getValue()) {
         return true;
      }

      float f = crystalPlacement.getFloatValue();
      if (f >= EntityUtils.computeLivingEntity(clientPlayerEntity)) {
         return false;
      }

      if (crystalPlacement.isFlag6()) {
         return true;
      }

      return switch ((PlacesCrystalsModule.EvaluationType)this.evaluationTypeSetting.getValue()) {
         case Ratio -> f < this.doubleValueSetting4.getValue();
         case Damage -> true;
      };
   }

   public void handleEndCrystalEntity3(EndCrystalEntity endCrystalEntity, boolean flag) {
      if (this.booleanValueSetting19.getValue()) {
         if (flag) {
            this.phobosClient.getDeadEntityTracker().handleSetDeadType(SetDeadType.Kill, endCrystalEntity);
         } else if (this.longValueSetting2.testObject2(0L)) {
            this.phobosClient.getDeadEntityTracker().handleSetDeadType(this.setDeadTypeSetting.getValue(), endCrystalEntity);
         } else {
            TaskScheduler taskScheduler = new TaskScheduler();
            taskScheduler.handleLong(
               this.longValueSetting2.getValue(),
               () -> this.phobosClient.getDeadEntityTracker().handleSetDeadType(this.setDeadTypeSetting.getValue(), endCrystalEntity)
            );
         }
      }
   }

   public int value() {
      return this.booleanValueSetting12.getValue() ? this.integerSetting5.getValue() : -1;
   }

   public RangeCalculator[] values2() {
      Set<RangeCalculator> set = new HashSet<>();

      for (Entry<RangeCalculator, Boolean> entry : this.mapSetting2.getValue().entrySet()) {
         if (entry.getValue()) {
            set.add(entry.getKey());
         }
      }

      return set.isEmpty() ? new RangeCalculator[]{RangeCalculator.Standing} : set.toArray(RangeCalculator[]::new);
   }

   public boolean testList(List<AbstractClientPlayerEntity> list, ClientWorld clientWorld) {
      for (AbstractClientPlayerEntity abstractClientPlayerEntity : list) {
         if (this.testAbstractClientPlayerEntity(abstractClientPlayerEntity)) {
            return false;
         }
      }

      for (Entry<BlockPos, Progress> entry : this.phobosClient.getBlockBreakManager().getBlockPosMap3().entrySet()) {
         BlockPos blockPos = entry.getKey();
         BlockState blockState = clientWorld.getBlockState(blockPos);
         float f;
         if ((f = BlockBreakManager.computeBlockPos(blockPos, blockState, clientWorld))
               * ((float)TimeUtils.computeLong(entry.getValue().getLongValue()) / 50.0F)
               * f
            >= 0.6F) {
            return false;
         }
      }

      return !this.normal4.testLong(500L) ? false : SpeedmineModule.getList().isEmpty();
   }

   public CrystalTargetSelector createValue(ClientWorld clientWorld, ClientPlayerEntity clientPlayerEntity) {
      return new CrystalTargetSelector(this, this.getPhobosClient(), clientPlayerEntity, clientWorld, this.phobosClient.getDamageCalculator());
   }

   public void run5() {
      Setting var1 = this.mapSetting4;
      Object var2 = var1.getValue();
      boolean var3 = var2 instanceof Map;
      if (var2 != null && !var3) {
         throw new ClassCastException("CHECKCAST failed");
      }

      Target var4 = Target.Players;
      Boolean var5 = true & true;
      ((Map)var2).put(var4, var5);
      Setting var6 = this.mapSetting4;
      Object var7 = var6.getValue();
      boolean var8 = var7 instanceof Map;
      if (var7 != null && !var8) {
         throw new ClassCastException("CHECKCAST failed");
      }

      Target var9 = Target.Invisibles;
      Boolean var10 = true & true;
      ((Map)var7).put(var9, var10);
      Builder var11 = this.createBuilder();
      Builder var12 = var11.resolveBuilder4("Main");
      SettingGroup[] var13 = new SettingGroup[10];
      Setting[] var14 = new Setting[6];
      Setting var15 = this.integerSetting8;
      var14[0] = var15;
      Setting var16 = this.mapSetting2;
      var14[1] = var16;
      Setting var17 = this.doubleValueSetting8;
      var14[2] = var17;
      Setting var18 = this.doubleValueSetting6;
      var14[3] = var18;
      Setting var19 = this.integerSetting7;
      var14[4] = var19;
      Setting var20 = this.swingTypeSetting;
      var14[5] = var20;
      SettingGroup var21 = this.resolveSettingGroup2("Place", var14);
      var13[0] = var21;
      Setting[] var22 = new Setting[6];
      Setting var23 = this.integerSetting10;
      var22[0] = var23;
      Setting var24 = this.mapSetting;
      var22[1] = var24;
      Setting var25 = this.doubleValueSetting5;
      var22[2] = var25;
      Setting var26 = this.doubleValueSetting7;
      var22[3] = var26;
      Setting var27 = this.integerSetting9;
      var22[4] = var27;
      Setting var28 = this.swingTypeSetting2;
      var22[5] = var28;
      SettingGroup var29 = this.resolveSettingGroup2("Break", var22);
      var13[1] = var29;
      Setting var30 = this.booleanValueSetting31;
      Setting[] var31 = new Setting[1];
      Setting var32 = this.doubleValueSetting10;
      var31[0] = var32;
      SettingGroup var33 = this.resolveSettingGroup(var30, var31);
      var13[2] = var33;
      Setting var34 = this.booleanValueSetting19;
      Setting[] var35 = new Setting[2];
      Setting var36 = this.longValueSetting2;
      var35[0] = var36;
      Setting var37 = this.setDeadTypeSetting;
      var35[1] = var37;
      SettingGroup var38 = this.resolveSettingGroup(var34, var35);
      var13[3] = var38;
      Setting var39 = this.booleanValueSetting25;
      Setting[] var40 = new Setting[1];
      Setting var41 = this.floatValueSetting5;
      var40[0] = var41;
      SettingGroup var42 = this.resolveSettingGroup(var39, var40);
      var13[4] = var42;
      Setting var43 = this.booleanValueSetting28;
      Setting[] var44 = new Setting[3];
      Setting var45 = this.integerSetting13;
      var44[0] = var45;
      Setting var46 = this.integerSetting14;
      var44[1] = var46;
      Setting var47 = this.booleanValueSetting29;
      var44[2] = var47;
      SettingGroup var48 = this.resolveSettingGroup(var43, var44);
      var13[5] = var48;
      Setting[] var49 = new Setting[2];
      Setting var50 = this.switchModeSetting;
      var49[0] = var50;
      Setting var51 = this.silentSetting2;
      var49[1] = var51;
      SettingGroup var52 = this.resolveSettingGroup2("AntiWeakness", var49);
      var13[6] = var52;
      Setting var53 = this.booleanValueSetting21;
      Setting[] var54 = new Setting[2];
      Setting var55 = this.integerSetting11;
      var54[0] = var55;
      Setting var56 = this.doubleValueSetting9;
      var54[1] = var56;
      SettingGroup var57 = this.resolveSettingGroup(var53, var54);
      var13[7] = var57;
      Setting var58 = this.booleanValueSetting22;
      Setting[] var59 = new Setting[5];
      Setting var60 = this.booleanValueSetting30;
      var59[0] = var60;
      Setting var61 = this.booleanValueSetting35;
      var59[1] = var61;
      Setting var62 = this.deflectorSetting;
      var59[2] = var62;
      Setting var63 = this.floatValueSetting8;
      var59[3] = var63;
      Setting var64 = this.floatValueSetting7;
      var59[4] = var64;
      SettingGroup var65 = this.resolveSettingGroup(var58, var59);
      var13[8] = var65;
      Setting var66 = this.booleanValueSetting24;
      Setting[] var67 = new Setting[3];
      Setting var68 = this.floatValueSetting4;
      var67[0] = var68;
      Setting var69 = this.floatValueSetting6;
      var67[1] = var69;
      Setting var70 = this.booleanValueSetting23;
      var67[2] = var70;
      SettingGroup var71 = this.resolveSettingGroup(var66, var67);
      var13[9] = var71;
      Builder var72 = var12.resolveBuilder5(var13);
      Setting[] var73 = new Setting[8];
      Setting var74 = this.booleanValueSetting26;
      var73[0] = var74;
      Setting var75 = this.integerSetting12;
      var73[1] = var75;
      Setting var76 = this.booleanValueSetting27;
      var73[2] = var76;
      Setting var77 = this.switchModeSetting2;
      var73[3] = var77;
      Setting var78 = this.silentSetting3;
      var73[4] = var78;
      Setting var79 = this.booleanValueSetting33;
      var73[5] = var79;
      Setting var80 = this.booleanValueSetting32;
      var73[6] = var80;
      Setting var81 = this.mapSetting3;
      var73[7] = var81;
      Builder var82 = var72.resolveBuilder(var73);
      var82.getSettingSubsection();
      Builder var83 = this.createBuilder();
      Builder var84 = var83.resolveBuilder4("Interaction");
      SettingGroup[] var85 = new SettingGroup[3];
      Setting var86 = this.booleanValueSetting15;
      Setting[] var87 = new Setting[1];
      Setting var88 = this.booleanValueSetting16;
      var87[0] = var88;
      SettingGroup var89 = this.resolveSettingGroup(var86, var87);
      var85[0] = var89;
      Setting var90 = this.booleanValueSetting17;
      Setting[] var91 = new Setting[4];
      Setting var92 = this.booleanValueSetting11;
      var91[0] = var92;
      Setting var93 = this.booleanValueSetting14;
      var91[1] = var93;
      Setting var94 = this.rotateSetting;
      var91[2] = var94;
      Setting var95 = this.integerSetting6;
      var91[3] = var95;
      SettingGroup var96 = this.resolveSettingGroup(var90, var91);
      var85[1] = var96;
      Setting var97 = this.booleanValueSetting12;
      Setting[] var98 = new Setting[2];
      Setting var99 = this.integerSetting5;
      var98[0] = var99;
      Setting var100 = this.booleanValueSetting13;
      var98[1] = var100;
      SettingGroup var101 = this.resolveSettingGroup(var97, var98);
      var85[2] = var101;
      Builder var102 = var84.resolveBuilder5(var85);
      var102.getSettingSubsection();
      Builder var103 = this.createBuilder();
      Builder var104 = var103.resolveBuilder4("Calculations");
      SettingGroup[] var105 = new SettingGroup[1];
      Setting var106 = this.booleanValueSetting10;
      Setting[] var107 = new Setting[1];
      Setting var108 = this.doubleValueSetting3;
      var107[0] = var108;
      SettingGroup var109 = this.resolveSettingGroup(var106, var107);
      var105[0] = var109;
      Builder var110 = var104.resolveBuilder5(var105);
      Setting[] var111 = new Setting[8];
      Setting var112 = this.evaluationTypeSetting;
      var111[0] = var112;
      Setting var113 = this.calculationTypeSetting;
      var111[1] = var113;
      Setting var114 = this.doubleValueSetting2;
      var111[2] = var114;
      Setting var115 = this.doubleValueSetting4;
      var111[3] = var115;
      Setting var116 = this.floatValueSetting3;
      var111[4] = var116;
      Setting var117 = this.floatValueSetting2;
      var111[5] = var117;
      Setting var118 = this.booleanValueSetting18;
      var111[6] = var118;
      Setting var119 = this.booleanValueSetting20;
      var111[7] = var119;
      Builder var120 = var110.resolveBuilder(var111);
      var120.getSettingSubsection();
      Builder var121 = this.createBuilder();
      Setting var122 = this.booleanValueSetting38;
      Builder var123 = var121.resolveBuilder6(var122);
      List var124 = this.getSettingGroupList2();
      Builder var125 = var123.resolveBuilder2(var124);
      Setting[] var126 = new Setting[2];
      Setting var127 = this.longValueSetting7;
      var126[0] = var127;
      Setting var128 = this.doubleValueSetting13;
      var126[1] = var128;
      Builder var129 = var125.resolveBuilder(var126);
      var129.getSettingSubsection();
      Builder var130 = this.createBuilder();
      Setting var131 = this.booleanValueSetting40;
      Builder var132 = var130.resolveBuilder6(var131);
      SettingGroup[] var133 = new SettingGroup[4];
      Setting var134 = this.booleanValueSetting36;
      Setting[] var135 = new Setting[1];
      Setting var136 = this.floatValueSetting10;
      var135[0] = var136;
      SettingGroup var137 = this.resolveSettingGroup(var134, var135);
      var133[0] = var137;
      Setting var138 = this.booleanValueSetting45;
      Setting[] var139 = new Setting[3];
      Setting var140 = this.booleanValueSetting43;
      var139[0] = var140;
      Setting var141 = this.longValueSetting8;
      var139[1] = var141;
      Setting var142 = this.booleanValueSetting42;
      var139[2] = var142;
      SettingGroup var143 = this.resolveSettingGroup(var138, var139);
      var133[1] = var143;
      Setting var144 = this.booleanValueSetting37;
      Setting[] var145 = new Setting[3];
      Setting var146 = this.colorSetting5;
      var145[0] = var146;
      Setting var147 = this.booleanValueSetting39;
      var145[1] = var147;
      Setting var148 = this.booleanValueSetting46;
      var145[2] = var148;
      SettingGroup var149 = this.resolveSettingGroup(var144, var145);
      var133[2] = var149;
      Setting var150 = this.booleanValueSetting50;
      Setting[] var151 = new Setting[1];
      Setting var152 = this.colorSetting6;
      var151[0] = var152;
      SettingGroup var153 = this.resolveSettingGroup(var150, var151);
      var133[3] = var153;
      Builder var154 = var132.resolveBuilder5(var133);
      Setting[] var155 = new Setting[4];
      Setting var156 = this.colorSetting3;
      var155[0] = var156;
      Setting var157 = this.colorSetting4;
      var155[1] = var157;
      Setting var158 = this.floatValueSetting9;
      var155[2] = var158;
      Setting var159 = this.floatValueSetting11;
      var155[3] = var159;
      Builder var160 = var154.resolveBuilder(var155);
      var160.getSettingSubsection();
      Setting var161 = this.booleanValueSetting34;
      Setting[] var162 = new Setting[6];
      Setting var163 = this.doubleValueSetting12;
      var162[0] = var163;
      Setting var164 = this.longValueSetting6;
      var162[1] = var164;
      Setting var165 = this.doubleValueSetting11;
      var162[2] = var165;
      Setting var166 = this.booleanValueSetting44;
      var162[3] = var166;
      Setting var167 = this.booleanValueSetting41;
      var162[4] = var167;
      Setting var168 = this.keyCombinationSetting;
      var162[5] = var168;
      this.resolveSettingGroup(var161, var162);
      Setting[] var169 = new Setting[6];
      Setting var170 = this.calcSetting;
      var169[0] = var170;
      Setting var171 = this.sequentialSetting;
      var169[1] = var171;
      Setting var172 = this.timingPresetSetting;
      var169[2] = var172;
      Setting var173 = this.longValueSetting5;
      var169[3] = var173;
      Setting var174 = this.longValueSetting3;
      var169[4] = var174;
      Setting var175 = this.longValueSetting4;
      var169[5] = var175;
      this.resolveSettingGroup2("Timings", var169);
      Setting var176 = this.booleanValueSetting54;
      Setting[] var177 = new Setting[4];
      Setting var178 = this.integerSetting15;
      var177[0] = var178;
      Setting var179 = this.integerSetting16;
      var177[1] = var179;
      Setting var180 = this.booleanValueSetting49;
      var177[2] = var180;
      Setting var181 = this.booleanValueSetting48;
      var177[3] = var181;
      this.resolveSettingGroup(var176, var177);
      Builder var182 = this.createBuilder();
      Setting var183 = this.booleanValueSetting51;
      Builder var184 = var182.resolveBuilder6(var183);
      SettingGroup[] var185 = new SettingGroup[1];
      Setting var186 = this.booleanValueSetting47;
      Setting[] var187 = new Setting[1];
      Setting var188 = this.longValueSetting9;
      var187[0] = var188;
      SettingGroup var189 = this.resolveSettingGroup(var186, var187);
      var185[0] = var189;
      Builder var190 = var184.resolveBuilder5(var185);
      Setting[] var191 = new Setting[5];
      Setting var192 = this.booleanValueSetting55;
      var191[0] = var192;
      Setting var193 = this.extendSwingSetting;
      var191[1] = var193;
      Setting var194 = this.booleanValueSetting53;
      var191[2] = var194;
      Setting var195 = this.integerSetting17;
      var191[3] = var195;
      Setting var196 = this.integerSetting18;
      var191[4] = var196;
      Builder var197 = var190.resolveBuilder(var191);
      var197.getSettingSubsection();
      Setting var198 = this.timingPresetSetting;
      Consumer<PlacesCrystalsModule.TimingPreset> var215 = this::handleTimingPreset;
      var198.handleConsumer2(var215);
      Setting var216 = this.longValueSetting5;
      Consumer<Long> var233 = this::handleLongValue;
      var216.handleConsumer2(var233);
      Setting var234 = this.longValueSetting4;
      Consumer<Long> var251 = this::handleLongValue2;
      var234.handleConsumer2(var251);
   }

   public BiConsumer<Float, Float> getBiConsumer() {
      return !this.booleanValueSetting52.getValue() ? (floatValue, floatValue2) -> {} : (floatValue, floatValue2) -> {
         if (floatValue.floatValue() != 0.0) {
            double d = floatValue2 / floatValue * 100.0;
            if (d < 50.0) {
               this.handleText6("Miss-match ratio: " + d);
            }
         }
      };
   }

   public void handleInt(int i) {
      if (i > this.intValue) {
         this.intValue = i;
      }
   }

   public void handleClientPlayerEntity6(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, BlockHitResult blockHitResult, Hand hand) {
      if (mc.interactionManager != null) {
         int i = this.integerSetting8.getValue();
         if (i == 1) {
            this.phobosClient
               .getServerAntiCheatDetector()
               .handleConsumer(integer -> clientPlayerEntity.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(hand, blockHitResult, integer)));
            switch ((PlacesCrystalsModule.SwingType)this.swingTypeSetting.getValue()) {
               case Auto:
                  clientPlayerEntity.swingHand(hand);
                  break;
               case Mainhand:
                  clientPlayerEntity.swingHand(Hand.MAIN_HAND);
                  break;
               case None:
                  clientPlayerEntity.swingHand(Hand.OFF_HAND);
                  break;
               case Offhand:
                  clientPlayerEntity.networkHandler.sendPacket(new HandSwingC2SPacket(hand));
            }
         } else {
            DelayedRunnable.handleRunnable(
               () -> {
                  this.phobosClient
                     .getServerAntiCheatDetector()
                     .handleConsumer(integer -> clientPlayerEntity.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(hand, blockHitResult, integer)));
                  switch ((PlacesCrystalsModule.SwingType)this.swingTypeSetting.getValue()) {
                     case Auto:
                        clientPlayerEntity.swingHand(hand);
                        break;
                     case Mainhand:
                        clientPlayerEntity.swingHand(Hand.MAIN_HAND);
                        break;
                     case None:
                        clientPlayerEntity.swingHand(Hand.OFF_HAND);
                        break;
                     case Offhand:
                        clientPlayerEntity.networkHandler.sendPacket(new HandSwingC2SPacket(hand));
                  }
               },
               i
            );
         }
      }
   }

   public boolean testClientPlayerEntity4(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, EndCrystalEntity endCrystalEntity) {
      double d = CrystalUtils.testPlayerEntity2(clientPlayerEntity, clientWorld, endCrystalEntity, AntiCheatModule.getAntiCheatModule().getSplitter().getValue())
         ? this.doubleValueSetting5.getValue()
         : this.value2();
      PackedRangeCalculator packedRangeCalculator = this.createPackedRangeCalculator();
      if (!packedRangeCalculator.testFunction2(rangeCalculator -> rangeCalculator.calculate(clientPlayerEntity, endCrystalEntity.getBoundingBox(), d))) {
         return false;
      } else if (this.booleanValueSetting17.getValue()
         && this.rotateSetting.testObject3(PlacesCrystalsModule.Rotate.Normal)
         && this.booleanValueSetting14.getValue()
         && !RaycastUtils.testPhobosClient(this.phobosClient, endCrystalEntity)) {
         return false;
      } else {
         return this.booleanValueSetting31.getValue() && !this.normal3.testLong((long)(this.doubleValueSetting10.getValue() * 25.0))
            ? false
            : this.booleanValueSetting27.getValue() || !clientPlayerEntity.isUsingItem();
      }
   }

   public PlacementResult createPlacementResult(int i) {
      return new PlacementResult(this.booleanValueSetting54.getValue() && this.booleanValueSetting48.getValue(), i);
   }

   public boolean testClientPlayerEntity5(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, CrystalTargetSelector crystalTargetSelector) {
      if (this.booleanValueSetting11.getValue() && this.endCrystalEntityMutableObject.getValue() != null) {
         EndCrystalEntity endCrystalEntity = this.endCrystalEntityMutableObject.getValue();
         if (clientWorld.getEntityById(endCrystalEntity.getId()) != null
            && this.alwaysTrue2()
            && (this.booleanValueSetting27.getValue() || !clientPlayerEntity.isUsingItem())) {
            double d = CrystalUtils.testPlayerEntity2(
                  clientPlayerEntity, clientWorld, endCrystalEntity, AntiCheatModule.getAntiCheatModule().getSplitter().getValue()
               )
               ? this.doubleValueSetting5.getValue()
               : this.value2();
            PackedRangeCalculator packedRangeCalculator = this.createPackedRangeCalculator();
            if (!packedRangeCalculator.testFunction2(rangeCalculator -> rangeCalculator.calculate(clientPlayerEntity, endCrystalEntity.getBoundingBox(), d))) {
               this.endCrystalEntityMutableObject.setValue(null);
               return false;
            } else {
               float[] floats = RotationUtils.values(clientPlayerEntity, endCrystalEntity);
               this.phobosClient.getRotationManager().testFloat(floats[0], floats[1]);
               this.handleClientPlayerEntity3(clientPlayerEntity, endCrystalEntity, false);
               this.handleEndCrystalEntity3(endCrystalEntity, false);
               this.threaded2.run();
               crystalTargetSelector.setFlag3(true);
               this.endCrystalEntityMutableObject.setValue(null);
               return true;
            }
         } else {
            this.endCrystalEntityMutableObject.setValue(null);
            return false;
         }
      } else {
         if (this.endCrystalEntityMutableObject.getValue() != null) {
            this.endCrystalEntityMutableObject.setValue(null);
         }

         return false;
      }
   }

   public boolean testAbstractClientPlayerEntity(AbstractClientPlayerEntity abstractClientPlayerEntity) {
      return InventoryUtils.testLivingEntity(abstractClientPlayerEntity, Items.BOW)
         || InventoryUtils.testLivingEntity(abstractClientPlayerEntity, Items.EXPERIENCE_BOTTLE)
         || abstractClientPlayerEntity.getMainHandStack().getItem() instanceof PickaxeItem
         || abstractClientPlayerEntity.getMainHandStack().getItem() instanceof ShovelItem;
   }

   public void handleBlockPos2(BlockPos blockPos) {
      Box box = BoxUtils.createBox9(blockPos);
      boolean flag = this.booleanValueSetting45.getValue() && this.booleanValueSetting42.getValue();
      this.floatAnimator.handleFloat3(10.0F, 0.05F);
      Color color = flag ? ColorUtils.createColor4(this.colorSetting3.getValue(), this.floatAnimator.getFloatValue2()) : this.colorSetting3.getValue();
      Color color2 = flag ? ColorUtils.createColor4(this.colorSetting4.getValue(), this.floatAnimator.getFloatValue2()) : this.colorSetting4.getValue();
      long l = BlockUtils.computeObject(this, blockPos);
      if (this.booleanValueSetting45.getValue()) {
         Render3d.getCuboidQueue().removeIf(cuboid -> cuboid.testObject(l));
         if (!this.booleanValueSetting43.getValue()) {
            Render3d.getCuboidQueue().removeIf(cuboid -> cuboid.getIntValue() == 2);
         }
      }

      if (this.booleanValueSetting36.getValue()) {
         double d = this.animatedBox.createBox().getCenter().distanceTo(blockPos.toCenterPos());
         this.animatedBox.handleBox(box, d > 10.0);
         this.animatedBox.handleFloat(10.0F, this.floatValueSetting10.getValue());
         Render3d.handleCuboid(
            Cuboid.createBuilder()
               .resolveBuilder11(this.animatedBox.createBox())
               .resolveBuilder18(color, color2)
               .resolveBuilder12(this.floatValueSetting9.getValue())
               .resolveBuilder6(this.booleanValueSetting45, this.longValueSetting8)
               .resolveBuilder14(l)
               .resolveBuilder15(2)
               .getCuboid()
         );
      } else {
         Render3d.handleCuboid(
            Cuboid.createBuilder()
               .resolveBuilder11(box)
               .resolveBuilder18(color, color2)
               .resolveBuilder12(this.floatValueSetting9.getValue())
               .resolveBuilder6(this.booleanValueSetting45, this.longValueSetting8)
               .resolveBuilder14(l)
               .resolveBuilder15(2)
               .getCuboid()
         );
      }
   }

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      CrystalPlacement crystalPlacement = this.getCrystalPlacement();
      if (crystalPlacement != null) {
         this.resolvePlacesCrystalsModule6(null);
         CrystalTargetSelector crystalTargetSelector = this.createValue(clientWorld, clientPlayerEntity);
         crystalTargetSelector.run3();
         crystalTargetSelector.handleValue(this.crystalPlacement);
         this.resolvePlacesCrystalsModule6(null);
      }
   }

   public void run2() {
      this.entity = null;
      this.blockPos2 = new BlockPos(0, -1000, 0);
      this.blockPosMap.clear();
      this.endCrystalEntitySet.clear();
      this.intValue = 0;
      this.entity2 = null;
      this.textMutableObject.setValue(null);
      this.integerSet.clear();
      this.crystalQueue.clear();

      for (CrystalPlacement crystalPlacement : this.crystalPlacementBlockPosGrid.getObjectArray()) {
         crystalPlacement.run();
      }
   }

   public PackedRangeCalculator createPackedRangeCalculator() {
      return new PackedRangeCalculator(this.values2(), this.values());
   }

   public void handleRenderContext(RenderContext renderContext, ClientPlayerEntity clientPlayerEntity) {
      BlockPos blockPos = this.getBlockPos();
      if (blockPos != null) {
         this.longValue = System.currentTimeMillis();
         this.blockPos4 = null;
      }

      this.handleRenderContext2(renderContext, clientPlayerEntity, blockPos);
      if (blockPos != null) {
         this.handleBlockPos2(blockPos);
      }
   }

   public boolean testCrystalSafetyCalculator(CrystalSafetyCalculator crystalSafetyCalculator, EndCrystalEntity endCrystalEntity, MutableObject<Float> mutableObject) {
      return crystalSafetyCalculator.testEndCrystalEntity2(endCrystalEntity, mutableObject, new MutableBoolean(false));
   }

   public boolean testClientPlayerEntity6(ClientPlayerEntity clientPlayerEntity) {
      if (!this.booleanValueSetting27.getValue()
         && PlayerActionUtils.testClientPlayerEntity7(clientPlayerEntity)
         && !this.switchModeSetting2.testObject2(SwitchMode.Off)) {
         return false;
      }

      if (!ItemSwitcher.testItem3(Items.END_CRYSTAL)) {
         return false;
      }

      return switch ((SwitchMode)this.switchModeSetting2.getValue()) {
         case Off -> clientPlayerEntity.getMainHandStack().isOf(Items.END_CRYSTAL) || clientPlayerEntity.getOffHandStack().isOf(Items.END_CRYSTAL);
         case Normal, Silent -> true;
      };
   }

   public void handleRenderContext2(RenderContext renderContext, ClientPlayerEntity clientPlayerEntity, BlockPos blockPos) {
      if (blockPos != null || this.booleanValueSetting45.getValue() && this.blockPos4 != null) {
         BlockPos blockPos2 = Objects.requireNonNullElse(blockPos, this.blockPos4);
         Vec3d vec3d = this.booleanValueSetting36.getValue() ? this.animatedBox.createBox().getCenter() : blockPos2.toCenterPos();
         this.cameraState.setClientPlayerEntity(clientPlayerEntity);
         this.cameraState.setVec3d(vec3d);
         this.cameraState.setCamera(mc.gameRenderer.getCamera());
         this.cameraState.setEntity(mc.getCameraEntity());
         Function<Float, Float> function = floatValue -> {
            if (!this.booleanValueSetting45.getValue()) {
               return floatValue;
            }

            long l = this.longValueSetting8.getValue();
            float fx = floatValue * 255.0F;
            float f2 = fx / (float)l;
            return MathHelper.clamp((int)(f2 * (float)(this.longValue + l - System.currentTimeMillis())), 0, (int)fx) / 255.0F;
         };
         float f = function.apply(1.0F);
         if (f <= 0.05F) {
            this.blockPos4 = null;
         } else {
            this.cameraState
               .handleFloat(
                  renderContext.getFloatValue(),
                  this.floatValueSetting11.getValue(),
                  (matrixStack, vec3dx) -> {
                     this.cameraState.run();
                     String text = String.valueOf(MathUtils.computeFloat5(this.floatValueMutableObject2.getValue(), 1));
                     float f3 = (
                           this.booleanValueSetting39.getValue()
                              ? this.phobosClient.getRenderManager().computeText3(text)
                              : mc.textRenderer.getWidth(text)
                        )
                        / 2.0F;
                     float f4 = -(this.booleanValueSetting50.getValue() ? (this.booleanValueSetting39.getValue() ? -11.0F : -9.5F) : 10.0F);
                     if (this.booleanValueSetting50.getValue()) {
                        float fx = 11.0F;
                        float f2 = 0.5F;
                        Color color = this.colorSetting6.getValue();
                        RenderUtils.handleMatrixStack29(
                           matrixStack, ColorUtils.createColor6(Color.BLACK, function.apply(color.getAlpha() / 255.0F)), 0.0, 0.0, fx + f2, fx + f2, 360
                        );
                        RenderUtils.handleMatrixStack29(
                           matrixStack, ColorUtils.createColor6(color, function.apply(color.getAlpha() / 255.0F)), 0.0, 0.0, fx, fx, 360
                        );
                        RenderSystem.setShaderTexture(0, identifier);
                        RenderUtils.handleMatrixStack4(matrixStack, Color.WHITE, -5.5, -(this.booleanValueSetting37.getValue() ? 8.5F : 6.0F), 12.0);
                     }

                     if (this.booleanValueSetting37.getValue()) {
                        Color color2 = this.colorSetting5.getValue();
                        matrixStack.push();
                        if (this.booleanValueSetting50.getValue()) {
                           matrixStack.scale(0.5F, 0.5F, 1.0F);
                        }

                        if (this.booleanValueSetting39.getValue()) {
                           this.phobosClient
                              .getRenderManager()
                              .handleMatrixStack2(
                                 matrixStack,
                                 text,
                                 -f3,
                                 f4,
                                 color2.getRed() / 255.0F,
                                 color2.getGreen() / 255.0F,
                                 color2.getBlue() / 255.0F,
                                 function.apply(color2.getAlpha() / 255.0F),
                                 this.booleanValueSetting46.getValue()
                              );
                           matrixStack.pop();
                           return;
                        }

                        this.phobosClient
                           .getRenderManager()
                           .getText3dRenderer()
                           .handleMatrixStack3(
                              matrixStack,
                              text,
                              -f3,
                              f4,
                              ColorUtils.createColor6(color2, function.apply(color2.getAlpha() / 255.0F)).hashCode(),
                              this.booleanValueSetting46.getValue()
                           );
                        matrixStack.pop();
                     }
                  }
               );
         }
      }
   }

   public Setting<PlacesCrystalsModule.Rotate> getRotateSetting() {
      return this.rotateSetting;
   }

   public Setting<Double> getDoubleValueSetting4() {
      return this.doubleValueSetting4;
   }

   public Setting<Boolean> getBooleanValueSetting16() {
      return this.booleanValueSetting16;
   }

   @Override
   public String nothing() {
      return (this.textMutableObject.getValue() == null ? "None" : this.textMutableObject.getValue())
         + ", "
         + this.describeDouble(MathUtils.computeDouble(this.animatedDouble2.value(), 2))
         + ", "
         + this.describeDouble(MathUtils.computeDouble(this.animatedDouble3.value(), 2));
   }

   public Setting<Boolean> getBooleanValueSetting17() {
      return this.booleanValueSetting17;
   }

   public Setting<Boolean> getBooleanValueSetting14() {
      return this.booleanValueSetting14;
   }

   public Setting<Boolean> getBooleanValueSetting10() {
      return this.booleanValueSetting10;
   }

   public String describeDouble(double d) {
      String text = String.valueOf(d);
      String[] texts = text.split("\\.");
      return texts[1].length() != 2 ? texts[0] + "." + texts[1] + "0" : text;
   }

   public Setting<Boolean> getBooleanValueSetting18() {
      return this.booleanValueSetting18;
   }

   @Override
   public void run4() {
      this.run2();
   }

   @Override
   public Setting<Boolean> getBooleanValueSetting7() {
      return this.booleanValueSetting15;
   }

   public Setting<Boolean> getBooleanValueSetting12() {
      return this.booleanValueSetting12;
   }

   public Setting<Boolean> getBooleanValueSetting13() {
      return this.booleanValueSetting13;
   }

   public Setting<Float> getFloatValueSetting2() {
      return this.floatValueSetting2;
   }

   public Setting<PlacesCrystalsModule.EvaluationType> getEvaluationTypeSetting() {
      return this.evaluationTypeSetting;
   }

   public Setting<Integer> getIntegerSetting6() {
      return this.integerSetting6;
   }

   public Setting<Double> getDoubleValueSetting3() {
      return this.doubleValueSetting3;
   }

   public boolean alwaysTrue2() {
      return true;
   }

   public Setting<PlacesCrystalsModule.CalculationType> getCalculationTypeSetting() {
      return this.calculationTypeSetting;
   }

   public boolean alwaysTrue3() {
      return true;
   }

   public void handleBlockPos3(BlockPos blockPos) {
      this.floatAnimator.setFloatValue(1.0F);
      this.normal6.run();
      this.blockPos3 = blockPos;
   }

   public double value2() {
      return this.doubleValueSetting7.getValue();
   }

   public Setting<Boolean> getBooleanValueSetting11() {
      return this.booleanValueSetting11;
   }

   public double value3() {
      return this.booleanValueSetting12.getValue() ? 0.0 : this.doubleValueSetting6.getValue();
   }

   public Setting<Integer> getIntegerSetting5() {
      return this.integerSetting5;
   }

   public BlockPos getBlockPos() {
      if (this.normal6.testLong(250L)) {
         this.floatAnimator.handleFloat(0.0F);
         this.blockPos3 = null;
      }

      return this.blockPos3;
   }

   public Setting<Float> getFloatValueSetting3() {
      return this.floatValueSetting3;
   }

   public Setting<Double> getDoubleValueSetting2() {
      return this.doubleValueSetting2;
   }

   @Override
   public Setting<Map<Target, Boolean>> getTargets2() {
      return this.mapSetting4;
   }

   public Setting<Float> getFloatValueSetting5() {
      return this.floatValueSetting5;
   }

   public Setting<Boolean> getBooleanValueSetting25() {
      return this.booleanValueSetting25;
   }

   public Setting<PlacesCrystalsModule.SwingType> getSwingTypeSetting() {
      return this.swingTypeSetting;
   }

   public Setting<Long> getLongValueSetting4() {
      return this.longValueSetting4;
   }

   public Setting<Double> getDoubleValueSetting6() {
      return this.doubleValueSetting6;
   }

   public Setting<Map<RangeCalculator, Boolean>> getMapSetting2() {
      return this.mapSetting2;
   }

   public Setting<SetDeadType> getSetDeadTypeSetting() {
      return this.setDeadTypeSetting;
   }

   public Setting<Map<PlacesCrystalsModule.Addon, Boolean>> getMapSetting3() {
      return this.mapSetting3;
   }

   public Setting<PlacesCrystalsModule.SwingType> getSwingTypeSetting2() {
      return this.swingTypeSetting2;
   }

   public Setting<Integer> getIntegerSetting7() {
      return this.integerSetting7;
   }

   public Setting<PlacesCrystalsModule.Calc> getCalcSetting() {
      return this.calcSetting;
   }

   public Setting<SwitchMode> getSwitchModeSetting() {
      return this.switchModeSetting;
   }

   public Setting<Long> getLongValueSetting5() {
      return this.longValueSetting5;
   }

   public Setting<Double> getDoubleValueSetting5() {
      return this.doubleValueSetting5;
   }

   public Setting<Double> getDoubleValueSetting8() {
      return this.doubleValueSetting8;
   }

   public Setting<Double> getDoubleValueSetting7() {
      return this.doubleValueSetting7;
   }

   public Setting<Silent> getSilentSetting2() {
      return this.silentSetting2;
   }

   public Setting<Boolean> getBooleanValueSetting20() {
      return this.booleanValueSetting20;
   }

   public Setting<Integer> getIntegerSetting10() {
      return this.integerSetting10;
   }

   public Setting<PlacesCrystalsModule.TimingPreset> getTimingPresetSetting() {
      return this.timingPresetSetting;
   }

   public Setting<Integer> getIntegerSetting12() {
      return this.integerSetting12;
   }

   public Setting<Boolean> getBooleanValueSetting19() {
      return this.booleanValueSetting19;
   }

   public Setting<Map<RangeCalculator, Boolean>> getMapSetting() {
      return this.mapSetting;
   }

   public Setting<PlacesCrystalsModule.Sequential> getSequentialSetting() {
      return this.sequentialSetting;
   }

   public Setting<Integer> getIntegerSetting9() {
      return this.integerSetting9;
   }

   public Setting<Boolean> getBooleanValueSetting26() {
      return this.booleanValueSetting26;
   }

   public Setting<Long> getLongValueSetting2() {
      return this.longValueSetting2;
   }

   public Setting<Integer> getIntegerSetting8() {
      return this.integerSetting8;
   }

   public Setting<Long> getLongValueSetting3() {
      return this.longValueSetting3;
   }

   public Setting<Double> getDoubleValueSetting12() {
      return this.doubleValueSetting12;
   }

   public Setting<Boolean> getBooleanValueSetting33() {
      return this.booleanValueSetting33;
   }

   public Setting<Float> getFloatValueSetting8() {
      return this.floatValueSetting8;
   }

   public Setting<Boolean> getBooleanValueSetting35() {
      return this.booleanValueSetting35;
   }

   public Setting<Boolean> getBooleanValueSetting21() {
      return this.booleanValueSetting21;
   }

   public Setting<Boolean> getBooleanValueSetting34() {
      return this.booleanValueSetting34;
   }

   public Setting<Boolean> getBooleanValueSetting23() {
      return this.booleanValueSetting23;
   }

   public Setting<Double> getDoubleValueSetting9() {
      return this.doubleValueSetting9;
   }

   public Setting<Integer> getIntegerSetting11() {
      return this.integerSetting11;
   }

   public Setting<Float> getFloatValueSetting7() {
      return this.floatValueSetting7;
   }

   public Setting<Long> getLongValueSetting6() {
      return this.longValueSetting6;
   }

   public Setting<Double> getDoubleValueSetting11() {
      return this.doubleValueSetting11;
   }

   public Setting<Boolean> getBooleanValueSetting24() {
      return this.booleanValueSetting24;
   }

   public Setting<PlacesCrystalsModule.Deflector> getDeflectorSetting() {
      return this.deflectorSetting;
   }

   public Setting<Float> getFloatValueSetting6() {
      return this.floatValueSetting6;
   }

   public Setting<Boolean> getBooleanValueSetting29() {
      return this.booleanValueSetting29;
   }

   public Setting<SwitchMode> getSwitchModeSetting2() {
      return this.switchModeSetting2;
   }

   public Setting<Boolean> getBooleanValueSetting44() {
      return this.booleanValueSetting44;
   }

   public Setting<Boolean> getBooleanValueSetting28() {
      return this.booleanValueSetting28;
   }

   public Setting<Integer> getIntegerSetting14() {
      return this.integerSetting14;
   }

   public Setting<Float> getFloatValueSetting42() {
      return this.floatValueSetting4;
   }

   public Setting<Boolean> getCooldown() {
      return this.booleanValueSetting31;
   }

   public Setting<Boolean> getBooleanValueSetting32() {
      return this.booleanValueSetting32;
   }

   public Setting<Silent> getSilentSetting3() {
      return this.silentSetting3;
   }

   public Setting<Integer> getIntegerSetting13() {
      return this.integerSetting13;
   }

   public Setting<Boolean> getBooleanValueSetting27() {
      return this.booleanValueSetting27;
   }

   public Setting<Boolean> getBooleanValueSetting22() {
      return this.booleanValueSetting22;
   }

   public Setting<Boolean> getBooleanValueSetting30() {
      return this.booleanValueSetting30;
   }

   public Setting<Double> getDoubleValueSetting10() {
      return this.doubleValueSetting10;
   }

   public Setting<Boolean> getBooleanValueSetting50() {
      return this.booleanValueSetting50;
   }

   @Override
   public Setting<Boolean> getBooleanValueSetting8() {
      return this.booleanValueSetting40;
   }

   public Setting<Color> getColorSetting6() {
      return this.colorSetting6;
   }

   public Setting<Boolean> getBooleanValueSetting41() {
      return this.booleanValueSetting41;
   }

   public Setting<Boolean> getShadow() {
      return this.booleanValueSetting46;
   }

   public Setting<Float> getFloatValueSetting10() {
      return this.floatValueSetting10;
   }

   public Setting<Long> getLongValueSetting8() {
      return this.longValueSetting8;
   }

   public Setting<Float> getFloatValueSetting11() {
      return this.floatValueSetting11;
   }

   public Setting<Boolean> getBooleanValueSetting54() {
      return this.booleanValueSetting54;
   }

   public Setting<Double> getDoubleValueSetting13() {
      return this.doubleValueSetting13;
   }

   public Setting<Boolean> getBooleanValueSetting36() {
      return this.booleanValueSetting36;
   }

   public Setting<Boolean> getBooleanValueSetting53() {
      return this.booleanValueSetting53;
   }

   public Setting<Float> getFloatValueSetting4() {
      return this.floatValueSetting9;
   }

   public Setting<Long> getLongValueSetting7() {
      return this.longValueSetting7;
   }

   public Setting<Color> getColorSetting4() {
      return this.colorSetting4;
   }

   public Setting<Boolean> getBooleanValueSetting39() {
      return this.booleanValueSetting39;
   }

   public Setting<Boolean> getBooleanValueSetting48() {
      return this.booleanValueSetting48;
   }

   public Setting<Color> getColorSetting5() {
      return this.colorSetting5;
   }

   public Setting<Boolean> getBooleanValueSetting45() {
      return this.booleanValueSetting45;
   }

   public Setting<Boolean> getBooleanValueSetting51() {
      return this.booleanValueSetting51;
   }

   public Setting<Boolean> getBooleanValueSetting43() {
      return this.booleanValueSetting43;
   }

   public Setting<Boolean> getBooleanValueSetting42() {
      return this.booleanValueSetting42;
   }

   public Setting<Boolean> getSelf() {
      return this.booleanValueSetting49;
   }

   public Setting<KeyCombination> getKeyCombinationSetting() {
      return this.keyCombinationSetting;
   }

   public Setting<Color> getColorSetting3() {
      return this.colorSetting3;
   }

   public Setting<Integer> getIntegerSetting15() {
      return this.integerSetting15;
   }

   public Setting<Integer> getIntegerSetting16() {
      return this.integerSetting16;
   }

   public Setting<Boolean> getBooleanValueSetting38() {
      return this.booleanValueSetting38;
   }

   public Setting<Boolean> getDamage() {
      return this.booleanValueSetting37;
   }

   public Set<Integer> getIntegerSet() {
      return this.integerSet;
   }

   public AnimatedDouble getAnimatedDouble3() {
      return this.animatedDouble3;
   }

   @Override
   public Setting<Boolean> getBooleanValueSetting9() {
      return this.booleanValueSetting52;
   }

   public AtomicInteger getAtomicInteger2() {
      return this.atomicInteger2;
   }

   public Setting<Integer> getIntegerSetting18() {
      return this.integerSetting18;
   }

   public Threaded getThreaded2() {
      return this.threaded2;
   }

   public BlockPos getBlockPos2() {
      return this.blockPos2;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public Normal getNormal3() {
      return this.normal3;
   }

   public Setting<PlacesCrystalsModule.ExtendSwing> getExtendSwingSetting() {
      return this.extendSwingSetting;
   }

   public CrystalCalculationTask getCrystalCalculationTask() {
      return this.crystalCalculationTask;
   }

   public Entity getEntity() {
      return this.entity;
   }

   public Setting<Integer> getIntegerSetting17() {
      return this.integerSetting17;
   }

   public Setting<Map<Target, Boolean>> getMapSetting4() {
      return this.mapSetting4;
   }

   public Setting<Boolean> getBooleanValueSetting47() {
      return this.booleanValueSetting47;
   }

   public MutableObject<EndCrystalEntity> getEndCrystalEntityMutableObject() {
      return this.endCrystalEntityMutableObject;
   }

   public MutableObject<String> getTextMutableObject() {
      return this.textMutableObject;
   }

   public MutableObject<Float> getFloatValueMutableObject() {
      return this.floatValueMutableObject;
   }

   public Setting<Boolean> getBooleanValueSetting55() {
      return this.booleanValueSetting55;
   }

   public Threaded getThreaded() {
      return this.threaded;
   }

   public Setting<Long> getLongValueSetting9() {
      return this.longValueSetting9;
   }

   public Normal getNormal4() {
      return this.normal4;
   }

   public ModuleRecord getModuleRecord() {
      return this.moduleRecord;
   }

   public BlockPosGrid<CrystalPlacement> getCrystalPlacementBlockPosGrid() {
      return this.crystalPlacementBlockPosGrid;
   }

   public CrystalPlacement getCrystalPlacement() {
      return this.crystalPlacement;
   }

   public AnimatedDouble getAnimatedDouble2() {
      return this.animatedDouble2;
   }

   public Threaded getThreaded3() {
      return this.threaded3;
   }

   public Map<BlockPos, Long> getBlockPosMap() {
      return this.blockPosMap;
   }

   public MutableObject<Float> getFloatValueMutableObject2() {
      return this.floatValueMutableObject2;
   }

   public Normal getNormal6() {
      return this.normal6;
   }

   public CameraState getCameraState() {
      return this.cameraState;
   }

   public PlacesCrystalsModule resolvePlacesCrystalsModule(BlockPos blockPos) {
      this.blockPos2 = blockPos;
      return this;
   }

   public Set<EndCrystalEntity> getEndCrystalEntitySet() {
      return this.endCrystalEntitySet;
   }

   public BlockPos getBlockPos3() {
      return this.blockPos3;
   }

   public AnimatedBox getAnimatedBox() {
      return this.animatedBox;
   }

   public boolean isFlag42() {
      return this.flag4;
   }

   public boolean isFlag5() {
      return this.flag5;
   }

   public boolean isFlag6() {
      return this.flag6;
   }

   public CrystalQueue getCrystalQueue() {
      return this.crystalQueue;
   }

   public List<Entity> getEntityList() {
      return this.entityList;
   }

   public PlacesCrystalsModule resolvePlacesCrystalsModule2(boolean flag) {
      this.flag2 = flag;
      return this;
   }

   public Threaded getThreaded4() {
      return this.threaded4;
   }

   public Threaded getThreaded5() {
      return this.threaded5;
   }

   public FloatAnimator getFloatAnimator() {
      return this.floatAnimator;
   }

   public boolean isFlag2() {
      return this.flag2;
   }

   public Threaded getThreaded6() {
      return this.threaded6;
   }

   public PlacesCrystalsModule resolvePlacesCrystalsModule3(Entity entity) {
      this.entity2 = entity;
      return this;
   }

   public PlacesCrystalsModule resolvePlacesCrystalsModule4(boolean flag) {
      this.flag4 = flag;
      return this;
   }

   public BlockPos getBlockPos4() {
      return this.blockPos4;
   }

   public Entity getEntity2() {
      return this.entity2;
   }

   public boolean isFlag3() {
      return this.flag3;
   }

   public PlacesCrystalsModule resolvePlacesCrystalsModule5(boolean flag) {
      this.flag5 = flag;
      return this;
   }

   public long getLongValue() {
      return this.longValue;
   }

   public PlacesCrystalsModule resolvePlacesCrystalsModule6(CrystalPlacement crystalPlacement) {
      this.crystalPlacement = crystalPlacement;
      return this;
   }

   public Normal getNormal5() {
      return this.normal5;
   }

   public CrystalDamageTracker getCrystalDamageTracker() {
      return this.crystalDamageTracker;
   }

   public void handleLongValue(Long longValue) {
      this.timingPresetSetting.setValue(PlacesCrystalsModule.TimingPreset.Custom);
   }

   public boolean check5() {
      return !this.rotateSetting.getValue().equals(PlacesCrystalsModule.Rotate.Silent);
   }

   public boolean check() {
      return !this.rotateSetting.getValue().equals(PlacesCrystalsModule.Rotate.Normal);
   }

   public void handleTimingPreset(PlacesCrystalsModule.TimingPreset timingPreset) {
      if (timingPreset != PlacesCrystalsModule.TimingPreset.Custom) {
         this.longValueSetting5.setValue2(timingPreset.attack);
         this.longValueSetting4.setValue2(timingPreset.replace);
      }
   }

   public boolean check8() {
      return this.switchModeSetting2.testObject2(SwitchMode.Off) || this.switchModeSetting2.testObject2(SwitchMode.Normal);
   }

   public static void handleMultiSelectSetting(MultiSelectSetting multiSelectSetting) {
      for (Target target : Target.values()) {
         multiSelectSetting.resolveMultiSelectSetting(target, target.name());
      }
   }

   public static void handleMultiSelectSetting2(MultiSelectSetting multiSelectSetting) {
      for (PlacesCrystalsModule.Addon addon : PlacesCrystalsModule.Addon.values()) {
         multiSelectSetting.resolveMultiSelectSetting(addon, addon.name());
      }
   }

   public void handleLongValue2(Long longValue) {
      this.timingPresetSetting.setValue(PlacesCrystalsModule.TimingPreset.Custom);
   }

   public enum Addon implements TextProvider {
      Mining("Ignores recently mined positions");

      public final String text;

      @Override
      public String getText() {
         return this.text;
      }

      Addon(final String description) {
         this.text = description;
      }
   }

   public enum Calc {
      Fast,
      Sync;
   }

   public enum CalculationType {
      Damage,
      Balance;
   }

   public enum Deflector {
      Boost,
      Block;
   }

   public enum EvaluationType {
      Damage,
      Ratio;
   }

   public enum ExtendSwing {
      Always,
      Single;
   }

   public enum Rotate {
      Normal(true, false),
      Hybrid(true, false),
      Silent(false, true);

      public final boolean reset;
      public final boolean strict;

      public boolean reset() {
         return this.reset;
      }

      public boolean strict() {
         return this.strict;
      }

      Rotate(final boolean strict, final boolean reset) {
         this.strict = strict;
         this.reset = reset;
      }
   }

   public enum Sequential {
      None,
      Strict,
      Strong;
   }

   public enum SwingType {
      Auto,
      Mainhand,
      Offhand,
      Packet,
      None;
   }

   public enum TimingPreset {
      Custom(-1L, -1L),
      Vanilla(0L, -1L),
      NCP(100L, 25L),
      Grim(100L, -1L),
      Strict(100L, 50L);

      public final long attack;
      public final long replace;

      public long attack() {
         return this.attack;
      }

      TimingPreset(final long attack, final long replace) {
         this.attack = attack;
         this.replace = replace;
      }

      public long replace() {
         return this.replace;
      }
   }
}
