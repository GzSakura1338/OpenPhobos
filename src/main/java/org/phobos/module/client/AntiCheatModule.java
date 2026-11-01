package org.phobos.module.client;

import com.google.common.base.Predicates;
import java.util.function.Consumer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityAttachmentType;
import net.minecraft.entity.EntityAttachments;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.util.math.Direction;
import org.phobos.api.Named;
import org.phobos.api.TextProvider;
import org.phobos.api.TimeGate;
import org.phobos.core.anticheat.AntiCheatCheck;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.setting.SettingPages;
import org.phobos.core.anticheat.AntiCheatPlayerRegistry;
import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.setting.SettingGroup;
import org.phobos.core.server.LatencyManager;
import org.phobos.core.player.InteractionManager;
import org.phobos.core.setting.SettingSubsection;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.module.PlacementModule;
import org.phobos.module.PlacementModule;
import org.phobos.module.PlacementModule;
import org.phobos.core.module.Categories;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.render.texture.SkyboxTextureUtils;
import org.phobos.core.tick.TickUtils;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.core.event.events.PacketEvent.Send;
import org.phobos.event.SafeListener;
import org.phobos.event.SetBlockStateEvent;
import org.phobos.event.TickEvent2;
import org.phobos.module.Module;

public class AntiCheatModule extends Module {
   public final Setting<Boolean> aim;
   public final Setting<Boolean> clear;
   public final Setting<Boolean> friends;
   public final Setting<Boolean> autoClicker;
   public static AntiCheatModule antiCheatModule;
   public final Setting<Boolean> invalid;
   public final Setting<Long> vL;
   public final Setting<Double> threshold;
   public final Setting<Boolean> experimental;
   public final Setting<Boolean> self;
   public final Setting<Boolean> fly;
   public final Setting<Long> latency = this.resolveSetting2(new LongNumberSetting("Latency", 600L, 0L, 1000L, true));
   public final Setting<Boolean> combat;
   public final Setting<Boolean> reach;
   public final Setting<Boolean> aura;
   public final Setting<Boolean> movement;
   public final Setting<PlacementModule.Timing> timing;
   public final Setting<Boolean> simulation;
   public final Setting<Boolean> booleanValueSetting;
   public final Setting<Boolean> scaffold;
   public final Setting<Boolean> placing;
   public final Setting<Boolean> placingSwing;
   public final Setting<Integer> placingExtension;
   public final Setting<Boolean> place;
   public final Setting<Integer> placingTicks;
   public final Setting<Boolean> legacyPoses;
   public final Setting<AntiCheatModule.InventorySync> inventory;
   public final Setting<Boolean> ground;
   public final Setting<Boolean> debug;
   public final Setting<Boolean> reset;
   public final Setting<Integer> placingDelay;
   public final Setting<Boolean> misc;
   public final Setting<Boolean> raytrace;
   public final Setting<Boolean> placingStrictDirection;
   public final Setting<Boolean> ignoreDrops;
   public final Setting<Boolean> placingNoGlitchBlocks;
   public final Setting<Boolean> sneak;
   public final Setting<Boolean> placingGrim;
   public final Setting<Boolean> placingMultiTask;
   public final Setting<Boolean> hitboxSync;
   public final Setting<PlacementModule.Airplace> placingAirPlace;
   public final Setting<Boolean> accurate;
   public final Setting<Boolean> splitter;
   public final Setting<Boolean> placingAttack;
   public final Setting<Double> placingRange;
   public final Setting<PlacementModule.Rotation> placingRotation;
   public final Setting<Boolean> mineFix;
   public final Setting<InteractionManager.Silent> placingSwitch;
   public final Setting<Integer> packetLimiterAttacks;
   public final TimeGate.Normal normal;
   public final Setting<Boolean> switchValue;
   public final Setting<Integer> packetLimiterInteractions;
   public final Setting<Boolean> packetLimiter;
   public final Setting<Integer> packetLimiterGlobal;
   public final LatencyManager latencyManager;
   public final LatencyManager latencyManager2;
   public final Setting<Integer> packetLimiterActions;
   public final LatencyManager latencyManager3;
   public final LatencyManager latencyManager4;
   public final Setting<Boolean> blockRotation;
   public AntiCheatPlayerRegistry antiCheatPlayerRegistry;
   public final Setting<Integer> packetLimiterTicks;
   public final Setting<Long> cooldown;
   public final Setting<AntiCheatModule.SwitchBypass> silent;

   public Setting<Boolean> getClear() {
      return this.clear;
   }

   public Setting<Boolean> getCombat() {
      return this.combat;
   }

   public AntiCheatModule(PhobosClient phobosClient) {
      super(phobosClient, "AntiCheat", Categories.client, "Manages client interactions and local anticheat");
      this.threshold = this.resolveSetting2(new NumberSetting<>("Threshold", 1.0, 0.0, 3.0, "b"));
      this.self = this.resolveSetting2(new BooleanSetting("Self", false));
      this.friends = this.resolveSetting2(new BooleanSetting("Friends", false));
      this.clear = this.resolveSetting2(new BooleanSetting("Clear", true));
      this.vL = this.resolveSetting2(new LongNumberSetting("VL", 6000L, 0L, 12000L, true));
      this.experimental = this.resolveSetting2(new BooleanSetting("Experimental", false));
      this.aim = this.resolveSetting2(new BooleanSetting("Aim", false));
      this.invalid = this.resolveSetting2(new BooleanSetting("Invalid", false));
      this.combat = this.resolveSetting2(new BooleanSetting("Combat", false));
      this.aura = this.resolveSetting2(new BooleanSetting("Aura", false));
      this.autoClicker = this.resolveSetting2(new BooleanSetting("AutoClicker", false));
      this.reach = this.resolveSetting2(new BooleanSetting("Reach", false));
      this.movement = this.resolveSetting2(new BooleanSetting("Movement", false));
      this.fly = this.resolveSetting2(new BooleanSetting("Fly", false));
      this.ground = this.resolveSetting2(new BooleanSetting("Ground", false));
      this.simulation = this.resolveSetting2(new BooleanSetting("Simulation", false));
      this.scaffold = this.resolveSetting2(new BooleanSetting("Scaffold", false));
      this.misc = this.resolveSetting2(new BooleanSetting("Misc", false));
      this.place = this.resolveSetting2(new BooleanSetting("Place", false));
      this.reset = this.resolveSetting2(new BooleanSetting("Reset", false));
      this.debug = this.resolveSetting2(new BooleanSetting("Debug", false));
      this.inventory = this.resolveSetting2(new EnumSetting<>("Inventory", AntiCheatModule.InventorySync.Vanilla).resolveSetting7("Inventory simulation"));
      this.booleanValueSetting = this.resolveSetting2(new BooleanSetting("1.12").resolveSetting7("Pre 1.13 server parameter"));
      this.legacyPoses = this.resolveSetting2(new BooleanSetting("LegacyPoses"));
      this.placing = this.resolveSetting2(new BooleanSetting("Placing").resolveSetting7("Synchronizes all block placing modules to local parameters"));
      this.timing = this.resolveSetting2(new EnumSetting<>("Timing", PlacementModule.Timing.Vanilla).resolveSetting7("Vanilla - Vanilla, Sequential - NCP"));
      this.placingTicks = this.resolveSetting2(new NumberSetting<>("PlacingTicks", 6, 1, 36).resolveSetting7("Blocks pre tick"));
      this.placingDelay = this.resolveSetting2(new NumberSetting<>("PlacingDelay", 0, 0, 10).resolveSetting7("Block placing delay in ticks"));
      this.placingExtension = this.resolveSetting2(new NumberSetting<>("PlacingExtension", 6, 1, 10).resolveSetting7("Extension limit"));
      this.placingSwing = this.resolveSetting2(new BooleanSetting("PlacingSwing", true).resolveSetting7("Hand swing"));
      this.placingAttack = this.resolveSetting2(new BooleanSetting("PlacingAttack", false).resolveSetting7("Attack blocking crystals"));
      this.placingMultiTask = this.resolveSetting2(new BooleanSetting("PlacingMultiTask", false).resolveSetting7("Place while using"));
      this.placingRotation = this.resolveSetting2(new EnumSetting<>("PlacingRotation", PlacementModule.Rotation.Normal).resolveSetting7("Rotation type"));
      this.raytrace = this.resolveSetting2(new BooleanSetting("Raytrace").resolveSetting7("Raytrace checks the direction placing on"));
      this.placingStrictDirection = this.resolveSetting2(new BooleanSetting("PlacingStrictDirection", false).resolveSetting7("Strict direction"));
      this.placingGrim = this.resolveSetting2(new BooleanSetting("PlacingGrim", false).resolveSetting7("Grim"));
      this.placingAirPlace = this.resolveSetting2(new EnumSetting<>("PlacingAirPlace", PlacementModule.Airplace.None).resolveSetting7("Allows placing on air"));
      this.placingNoGlitchBlocks = this.resolveSetting2(new BooleanSetting("PlacingNoGlitchBlocks", false).resolveSetting7("No glitch blocks"));
      this.placingRange = this.resolveSetting2(new NumberSetting<>("PlacingRange", 6.0, 0.0, 6.0).resolveSetting7("Interaction range"));
      this.placingSwitch = this.resolveSetting2(new EnumSetting<>("PlacingSwitch", InteractionManager.Silent.Normal).resolveSetting7("Switch type"));
      this.sneak = this.resolveSetting2(new BooleanSetting("Sneak").resolveSetting7("Sneaks before placing"));
      this.splitter = this.resolveSetting2(
         new BooleanSetting("Splitter", false).resolveSetting7("For better validity calculations in AutoCrystal, might be resource heavy")
      );
      this.accurate = this.resolveSetting2(
         new BooleanSetting("Accurate").resolveSetting7("Ignores all timers for dropped entities, all dropped items are computed immediately")
      );
      this.mineFix = this.resolveSetting2(new BooleanSetting("MineFix").resolveSetting7("2b2t mining fix"));
      this.hitboxSync = this.resolveSetting2(new BooleanSetting("HitboxSync", true).resolveSetting7("Attempts to fix hitbox issues"));
      this.ignoreDrops = this.resolveSetting2(new BooleanSetting("IgnoreDrops").resolveSetting7("Ignores dropped obsidian"));
      this.blockRotation = this.resolveSetting2(
         new BooleanSetting("BlockRotation", true).resolveSetting7("Ensures you're looking at a block (not speedmine related)")
      );
      this.switchValue = this.resolveSetting2(new BooleanSetting("Switch").resolveSetting7("Global switch override"));
      this.silent = this.resolveSetting2(new EnumSetting<>("Silent", AntiCheatModule.SwitchBypass.Inventory).resolveSetting7("Switch type"));
      this.cooldown = this.resolveSetting2(
         new LongNumberSetting("Cooldown", 0L, 0L, 1500L, true).resolveSetting7("Cooldown between attempts to attack blocking entities")
      );
      this.packetLimiter = this.resolveSetting2(new BooleanSetting("PacketLimiter"));
      this.packetLimiterGlobal = this.resolveSetting2(new NumberSetting<>("PacketLimiterGlobal", 1249, 50, 5000));
      this.packetLimiterActions = this.resolveSetting2(new NumberSetting<>("PacketLimiterActions", 120, 5, 500));
      this.packetLimiterInteractions = this.resolveSetting2(new NumberSetting<>("PacketLimiterInteractions", 420, 5, 3000));
      this.packetLimiterAttacks = this.resolveSetting2(new NumberSetting<>("PacketLimiterAttacks", 79, 4, 1000));
      this.packetLimiterTicks = this.resolveSetting2(new NumberSetting<>("PacketLimiterTicks", 20, 1, 40));
      this.latencyManager2 = new LatencyManager(this.packetLimiterGlobal, this.packetLimiterTicks, Predicates.alwaysTrue()).getLatencyManager();
      this.latencyManager3 = new LatencyManager(this.packetLimiterActions, this.packetLimiterTicks, packet -> packet instanceof PlayerActionC2SPacket)
         .getLatencyManager();
      this.latencyManager4 = new LatencyManager(
            this.packetLimiterActions, this.packetLimiterTicks, packet -> packet instanceof PlayerInteractBlockC2SPacket
         )
         .getLatencyManager();
      this.latencyManager = new LatencyManager(
            this.packetLimiterActions, this.packetLimiterTicks, packet -> packet instanceof PlayerInteractEntityC2SPacket
         )
         .getLatencyManager();
      this.normal = new TimeGate.Normal();
      this.handleG77G8vOcsOCkBD4vxs1B9Tu4ZA82vvYs(
         AntiCheatModule.Client.collisions, AntiCheatModule.Client.dimensions, AntiCheatModule.Client.epsilon, AntiCheatModule.Client.nSI
      );
      SettingGroup settingGroup = this.resolveSettingGroup(this.clear, this.vL);
      SettingGroup settingGroup2 = this.resolveSettingGroup(
         this.placing,
         this.timing,
         this.placingTicks,
         this.placingDelay,
         this.placingExtension,
         this.placingSwing,
         this.placingAttack,
         this.placingMultiTask,
         this.placingRotation,
         this.raytrace,
         this.placingStrictDirection,
         this.placingGrim,
         this.placingAirPlace,
         this.placingNoGlitchBlocks,
         this.placingRange,
         this.placingSwitch
      );
      SettingGroup settingGroup3 = this.resolveSettingGroup(
         this.packetLimiter,
         this.packetLimiterGlobal,
         this.packetLimiterActions,
         this.packetLimiterInteractions,
         this.packetLimiterAttacks,
         this.packetLimiterTicks
      );
      SettingSubsection settingSubsection = this.createBuilder()
         .resolveBuilder4("Config")
         .resolveBuilder5(
            this.resolveSettingGroup(this.aim, this.invalid),
            this.resolveSettingGroup(this.combat, this.aura, this.autoClicker, this.reach),
            this.resolveSettingGroup(this.movement, this.fly, this.ground, this.simulation)
         )
         .resolveBuilder(this.scaffold, this.misc, this.place)
         .getSettingSubsection();
      SettingGroup settingGroup4 = this.resolveSettingGroup(this.switchValue, this.silent);
      SettingPages<AntiCheatModule.Page> settingPages = new SettingPages<>(this, AntiCheatModule.Page.Server);
      settingPages.handleEnumValue2(AntiCheatModule.Page.Analysis, settingGroup);
      settingPages.handleEnumValue3(AntiCheatModule.Page.Analysis, settingSubsection);
      settingPages.handleEnumValue(
         AntiCheatModule.Page.Analysis, this.experimental, this.latency, this.threshold, this.friends, this.reset, this.debug, this.self
      );
      settingPages.handleEnumValue(
         AntiCheatModule.Page.Server,
         this.inventory,
         this.booleanValueSetting,
         this.legacyPoses,
         this.sneak,
         this.splitter,
         this.silent,
         this.cooldown,
         this.accurate,
         this.mineFix,
         this.hitboxSync,
         this.ignoreDrops,
         this.blockRotation
      );
      settingPages.handleEnumValue2(AntiCheatModule.Page.Server, settingGroup3, settingGroup2, settingGroup4);
      settingPages.handleEnumValue(
         AntiCheatModule.Page.Client,
         AntiCheatModule.Client.collisions,
         AntiCheatModule.Client.dimensions,
         AntiCheatModule.Client.epsilon,
         AntiCheatModule.Client.nSI
      );
      this.reset.handleConsumer2(booleanValue -> {
         if (booleanValue) {
            if (this.antiCheatPlayerRegistry != null) {
               this.antiCheatPlayerRegistry.getUUIDMap().clear();
            }

            this.reset.setValue(false);
         }
      });
      this.timing.handleConsumer2(timing -> {
         if (this.placing.getValue()) {
            for (PlacementModule placementModule : phobosClient.getModuleManager().getPlacementModuleSet()) {
               placementModule.getTimingSetting().setValue(timing);
            }
         }
      });
      this.placingTicks.handleConsumer2(integer -> {
         if (this.placing.getValue()) {
            for (PlacementModule placementModule : phobosClient.getModuleManager().getPlacementModuleSet()) {
               placementModule.getIntegerSetting2().setValue(integer);
            }
         }
      });
      this.placingDelay.handleConsumer2(integer -> {
         if (this.placing.getValue()) {
            for (PlacementModule placementModule : phobosClient.getModuleManager().getPlacementModuleSet()) {
               placementModule.getIntegerSetting().setValue(integer);
            }
         }
      });
      this.placingExtension.handleConsumer2(integer -> {
         if (this.placing.getValue()) {
            for (PlacementModule placementModule : phobosClient.getModuleManager().getPlacementModuleSet()) {
               placementModule.getIntegerSetting3().setValue(integer);
            }
         }
      });
      this.placingSwing.handleConsumer2(booleanValue -> {
         if (this.placing.getValue()) {
            for (PlacementModule placementModule : phobosClient.getModuleManager().getPlacementModuleSet()) {
               placementModule.getBooleanValueSetting4().setValue(booleanValue);
            }
         }
      });
      this.placingAttack.handleConsumer2(booleanValue -> {
         if (this.placing.getValue()) {
            for (PlacementModule placementModule : phobosClient.getModuleManager().getPlacementModuleSet()) {
               placementModule.getBooleanValueSetting2().setValue(booleanValue);
            }
         }
      });
      this.placingMultiTask.handleConsumer2(booleanValue -> {
         if (this.placing.getValue()) {
            for (PlacementModule placementModule : phobosClient.getModuleManager().getPlacementModuleSet()) {
               placementModule.getBooleanValueSetting5().setValue(booleanValue);
            }
         }
      });
      this.placingRotation.handleConsumer2(rotation -> {
         if (this.placing.getValue()) {
            for (PlacementModule placementModule : phobosClient.getModuleManager().getPlacementModuleSet()) {
               placementModule.getRotationSetting().setValue(rotation);
            }
         }
      });
      this.raytrace.handleConsumer2(booleanValue -> {
         if (this.placing.getValue()) {
            for (PlacementModule placementModule : phobosClient.getModuleManager().getPlacementModuleSet()) {
               placementModule.getBooleanValueSetting6().setValue(booleanValue);
            }
         }
      });
      this.placingStrictDirection.handleConsumer2(booleanValue -> {
         if (this.placing.getValue()) {
            for (PlacementModule placementModule : phobosClient.getModuleManager().getPlacementModuleSet()) {
               placementModule.getBooleanValueSetting7().setValue(booleanValue);
            }
         }
      });
      this.placingGrim.handleConsumer2(booleanValue -> {
         if (this.placing.getValue()) {
            for (PlacementModule placementModule : phobosClient.getModuleManager().getPlacementModuleSet()) {
               placementModule.getBooleanValueSetting().setValue(booleanValue);
            }
         }
      });
      this.placingAirPlace.handleConsumer2(airplace -> {
         if (this.placing.getValue()) {
            for (PlacementModule placementModule : phobosClient.getModuleManager().getPlacementModuleSet()) {
               placementModule.getAirplaceSetting().setValue(airplace);
            }
         }
      });
      this.placingNoGlitchBlocks.handleConsumer2(booleanValue -> {
         if (this.placing.getValue()) {
            for (PlacementModule placementModule : phobosClient.getModuleManager().getPlacementModuleSet()) {
               placementModule.getBooleanValueSetting3().setValue(booleanValue);
            }
         }
      });
      this.placingRange.handleConsumer2(doubleValue -> {
         if (this.placing.getValue()) {
            for (PlacementModule placementModule : phobosClient.getModuleManager().getPlacementModuleSet()) {
               placementModule.getDoubleValueSetting().setValue(doubleValue);
            }
         }
      });
      this.placingSwitch.handleConsumer2(silent -> {
         if (this.placing.getValue()) {
            for (PlacementModule placementModule : phobosClient.getModuleManager().getPlacementModuleSet()) {
               placementModule.getSilentSetting().setValue(silent);
            }
         }
      });
      this.packetLimiter.handleConsumer2(booleanValue -> {
         this.latencyManager2.handleFlag(booleanValue);
         this.latencyManager3.handleFlag(booleanValue);
         this.latencyManager4.handleFlag(booleanValue);
         this.latencyManager.handleFlag(booleanValue);
      });
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (AntiCheatModule.this.antiCheatPlayerRegistry == null) {
                  AntiCheatModule.this.antiCheatPlayerRegistry = new AntiCheatPlayerRegistry(AntiCheatModule.this);
               }

               AntiCheatModule.this.antiCheatPlayerRegistry.run6();
            }
         }
      );
      this.handleListener(
         new SafeListener<Receive<?>>() {
            public void handleObject(
               Receive<?> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               for (AntiCheatPlayer antiCheatPlayer : AntiCheatModule.this.antiCheatPlayerRegistry.getList2()) {
                  antiCheatPlayer.antiCheatCheckRegistry.handleConsumer(antiCheatCheck -> antiCheatCheck.handlePacket(receive.getPacket()));
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<SetBlockStateEvent>() {
            public void handleObject(
               SetBlockStateEvent setBlockStateEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!setBlockStateEvent.state().isAir()) {
                  for (AntiCheatPlayer antiCheatPlayer : AntiCheatModule.this.antiCheatPlayerRegistry.getList2()) {
                     antiCheatPlayer.antiCheatCheckRegistry.handleConsumer(antiCheatCheck -> antiCheatCheck.handleBlockPos(setBlockStateEvent.pos(), setBlockStateEvent.state()));
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Send<HandSwingC2SPacket>>() {
            public void handleObject(
               Send<HandSwingC2SPacket> send,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               for (AntiCheatPlayer antiCheatPlayer : AntiCheatModule.this.antiCheatPlayerRegistry.getList2()) {
                  if (antiCheatPlayer.alwaysTrue()) {
                     antiCheatPlayer.antiCheatCheckRegistry.handleConsumer(AntiCheatCheck::run10);
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Receive<EntityPositionS2CPacket>>() {
            public void handleObject(
               Receive<EntityPositionS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               for (AntiCheatPlayer antiCheatPlayer : AntiCheatModule.this.antiCheatPlayerRegistry.getList2()) {
                  antiCheatPlayer.antiCheatCheckRegistry.handleConsumer(AntiCheatCheck::run8);
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Send<PlayerActionC2SPacket>>() {
            public void handleObject(
               Send<PlayerActionC2SPacket> send,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (AntiCheatModule.this.mineFix.getValue()) {
                  PlayerActionC2SPacket playerActionC2SPacket = (PlayerActionC2SPacket)send.getPacket();
                  if (playerActionC2SPacket.getAction() == Action.START_DESTROY_BLOCK
                     || playerActionC2SPacket.getAction() == Action.STOP_DESTROY_BLOCK
                     || playerActionC2SPacket.getAction() == Action.ABORT_DESTROY_BLOCK) {
                     if (BlockUtils.testClientWorld3(clientWorld, playerActionC2SPacket.getPos())) {
                        send.setCancelled(true);
                     } else if (playerActionC2SPacket.getAction().equals(Action.START_DESTROY_BLOCK)) {
                        TickUtils.handleConsumer2(
                           integer -> SkyboxTextureUtils.handlePacket(
                              new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, playerActionC2SPacket.getPos(), Direction.UP, integer)
                           )
                        );
                        TickUtils.handleConsumer2(
                           integer -> SkyboxTextureUtils.handlePacket(
                              new PlayerActionC2SPacket(Action.START_DESTROY_BLOCK, playerActionC2SPacket.getPos(), Direction.UP, integer)
                           )
                        );
                        TickUtils.handleConsumer2(
                           integer -> SkyboxTextureUtils.handlePacket(
                              new PlayerActionC2SPacket(Action.ABORT_DESTROY_BLOCK, playerActionC2SPacket.getPos(), Direction.UP, integer)
                           )
                        );
                        send.setCancelled(true);
                     }
                  }
               }
            }
         }
      );
      ModuleListenerUtils.handleListenerHolder3(this, () -> {
         this.antiCheatPlayerRegistry = null;
         this.antiCheatPlayerRegistry = new AntiCheatPlayerRegistry(this);
      });
      antiCheatModule = this;
   }

   public Setting<Boolean> getExperimental() {
      return this.experimental;
   }

   public Setting<Boolean> getGround() {
      return this.ground;
   }

   public static boolean check() {
      return antiCheatModule.switchValue.getValue();
   }

   public static boolean check2() {
      return antiCheatModule.silent.getValue().equals(AntiCheatModule.SwitchBypass.Inventory);
   }

   public Setting<Boolean> getAura() {
      return this.aura;
   }

   public Setting<Boolean> getMisc() {
      return this.misc;
   }

   public Setting<Boolean> getInvalid() {
      return this.invalid;
   }

   public Setting<Boolean> getScaffold() {
      return this.scaffold;
   }

   public Setting<Boolean> getMovement() {
      return this.movement;
   }

   public Setting<Boolean> getFly() {
      return this.fly;
   }

   public Setting<Boolean> getReach() {
      return this.reach;
   }

   public Setting<Boolean> getSelf() {
      return this.self;
   }

   public Setting<Long> getVL() {
      return this.vL;
   }

   public Setting<Double> getThreshold() {
      return this.threshold;
   }

   public Setting<Boolean> getFriends() {
      return this.friends;
   }

   public Setting<Boolean> getAim() {
      return this.aim;
   }

   public Setting<Boolean> getSimulation() {
      return this.simulation;
   }

   public Setting<Long> getLatency() {
      return this.latency;
   }

   public Setting<Boolean> getAutoClicker() {
      return this.autoClicker;
   }

   public static boolean check3() {
      return antiCheatModule.normal.testNumber2(antiCheatModule.cooldown.getValue());
   }

   public Setting<Boolean> getBooleanValueSetting9() {
      return this.debug;
   }

   public Setting<Boolean> getBooleanValueSetting3() {
      return this.placingNoGlitchBlocks;
   }

   public Setting<Integer> getIntegerSetting3() {
      return this.placingExtension;
   }

   public Setting<PlacementModule.Airplace> getAirplaceSetting() {
      return this.placingAirPlace;
   }

   public Setting<Boolean> getReset() {
      return this.reset;
   }

   public Setting<Boolean> getBooleanValueSetting6() {
      return this.raytrace;
   }

   public Setting<Double> getDoubleValueSetting() {
      return this.placingRange;
   }

   public Setting<Boolean> getBooleanValueSetting() {
      return this.placingGrim;
   }

   public Setting<Boolean> getLegacyPoses() {
      return this.legacyPoses;
   }

   public Setting<Boolean> getBooleanValueSetting4() {
      return this.placingSwing;
   }

   public Setting<AntiCheatModule.InventorySync> getInventory() {
      return this.inventory;
   }

   public Setting<Boolean> getSneak() {
      return this.sneak;
   }

   public Setting<InteractionManager.Silent> getSilentSetting() {
      return this.placingSwitch;
   }

   public Setting<Boolean> getBooleanValueSetting2() {
      return this.placingAttack;
   }

   public Setting<PlacementModule.Timing> getTimingSetting() {
      return this.timing;
   }

   public Setting<Boolean> getBooleanValueSetting8() {
      return this.booleanValueSetting;
   }

   public Setting<Boolean> getPlace() {
      return this.place;
   }

   public Setting<Boolean> getPlacing() {
      return this.placing;
   }

   public Setting<Integer> getIntegerSetting2() {
      return this.placingTicks;
   }

   public Setting<Boolean> getBooleanValueSetting5() {
      return this.placingMultiTask;
   }

   public Setting<Boolean> getBooleanValueSetting7() {
      return this.placingStrictDirection;
   }

   public Setting<PlacementModule.Rotation> getRotationSetting() {
      return this.placingRotation;
   }

   public Setting<Integer> getIntegerSetting() {
      return this.placingDelay;
   }

   public LatencyManager getLatencyManager() {
      return this.latencyManager;
   }

   public Setting<AntiCheatModule.SwitchBypass> getSilent() {
      return this.silent;
   }

   public Setting<Integer> getPacketLimiterAttacks() {
      return this.packetLimiterAttacks;
   }

   public Setting<Boolean> getMineFix() {
      return this.mineFix;
   }

   public AntiCheatPlayerRegistry getAntiCheatPlayerRegistry() {
      return this.antiCheatPlayerRegistry;
   }

   public Setting<Integer> getPacketLimiterActions() {
      return this.packetLimiterActions;
   }

   public Setting<Boolean> getPacketLimiter() {
      return this.packetLimiter;
   }

   public Setting<Boolean> getAccurate() {
      return this.accurate;
   }

   public Setting<Boolean> getSwitchValue() {
      return this.switchValue;
   }

   public Setting<Boolean> getHitboxSync() {
      return this.hitboxSync;
   }

   public Setting<Boolean> getIgnoreDrops() {
      return this.ignoreDrops;
   }

   public Setting<Boolean> getBlockRotation() {
      return this.blockRotation;
   }

   public TimeGate.Normal getNormal() {
      return this.normal;
   }

   public Setting<Long> getCooldown() {
      return this.cooldown;
   }

   public LatencyManager getLatencyManager4() {
      return this.latencyManager4;
   }

   public LatencyManager getLatencyManager2() {
      return this.latencyManager2;
   }

   public LatencyManager getLatencyManager3() {
      return this.latencyManager3;
   }

   public Setting<Integer> getPacketLimiterInteractions() {
      return this.packetLimiterInteractions;
   }

   public static AntiCheatModule getAntiCheatModule() {
      return antiCheatModule;
   }

   public Setting<Integer> getPacketLimiterTicks() {
      return this.packetLimiterTicks;
   }

   public Setting<Integer> getPacketLimiterGlobal() {
      return this.packetLimiterGlobal;
   }

   public Setting<Boolean> getSplitter() {
      return this.splitter;
   }

   public static class Client {
      public static final Setting<Boolean> nSI = new BooleanSetting("NSI").resolveSetting7("No sneaking interpolation");
      public static final EntityDimensions entityDimensions = EntityDimensions.changing(0.6F, 1.65F)
         .withEyeHeight(1.54F)
         .withAttachments(EntityAttachments.builder().add(EntityAttachmentType.VEHICLE, PlayerEntity.VEHICLE_ATTACHMENT_POS));
      public static final Setting<AntiCheatModule.Client.Dimensions> dimensions = new EnumSetting<>("Dimensions", AntiCheatModule.Client.Dimensions.Native);
      public static final Setting<AntiCheatModule.Client.CollisionVersion> collisions = new EnumSetting<>(
         "Collisions", AntiCheatModule.Client.CollisionVersion.Native
      );
      public static final EntityDimensions entityDimensions2 = EntityDimensions.changing(0.6F, 1.8F)
         .withEyeHeight(1.54F)
         .withAttachments(EntityAttachments.builder().add(EntityAttachmentType.VEHICLE, PlayerEntity.VEHICLE_ATTACHMENT_POS));
      public static final Setting<Boolean> epsilon = new BooleanSetting("Epsilon").resolveSetting7("1.13.2 and older movement velocity fix");

      public static boolean check() {
         return nSI.getValue();
      }

      public static double computeDouble(double d) {
         return switch ((AntiCheatModule.Client.CollisionVersion)collisions.getValue()) {
            case Old -> d;
            case _1_20 -> -0.001;
            case Native -> -1.0E-7;
         };
      }

      public static double computeDouble2(double d) {
         return epsilon.getValue() ? 1.0E-4 : d;
      }

      public static void handleConsumer(Consumer<EntityDimensions> consumer) {
         switch ((AntiCheatModule.Client.Dimensions)dimensions.getValue()) {
            case Old:
               consumer.accept(entityDimensions);
               break;
            case _1_8:
               consumer.accept(entityDimensions2);
         }
      }

      public enum CollisionVersion implements Named, TextProvider {
         Native("Native", "Native to local version"),
         Old("Old", "1.19.1 and older"),
         _1_20("1.20", "1.21 and older");

         public final String text;
         public final String name;

         @Override
         public String getText() {
            return this.text;
         }

         @Override
         public String getName() {
            return this.name;
         }

         CollisionVersion(final String name, final String description) {
            this.name = name;
            this.text = description;
         }
      }

      public enum Dimensions implements Named, TextProvider {
         Native("Native", "Native to local version"),
         Old("Old", "1.13.2 and older"),
         _1_8("1.8", "1.8 and older");

         public final String text;
         public final String name;

         @Override
         public String getName() {
            return this.name;
         }

         Dimensions(final String name, final String description) {
            this.name = name;
            this.text = description;
         }

         @Override
         public String getText() {
            return this.text;
         }
      }
   }

   public enum InventorySync {
      Vanilla(false, false),
      Strict(true, false),
      Grim(true, true);

      public final boolean strict;
      public final boolean grim;

      public boolean isStrict() {
         return this.strict;
      }

      InventorySync(final boolean strict, final boolean grim) {
         this.strict = strict;
         this.grim = grim;
      }

      public boolean isGrim() {
         return this.grim;
      }
   }

   private enum Page {
      Server,
      Analysis,
      Client;
   }

   private enum SwitchBypass implements TextProvider {
      Inventory("Uses additional clicks to bypass multi-task"),
      Pick("Uses pick packet to bypass cooldown bypass");

      public final String text;

      SwitchBypass(final String description) {
         this.text = description;
      }

      @Override
      public String getText() {
         return this.text;
      }
   }

   public enum Timing implements TextProvider {
      Normal("Normal execution"),
      Async("Async/Out-of-Tick execution"),
      Sync("Synchronized execution (NCP)");

      public final String text;

      @Override
      public String getText() {
         return this.text;
      }

      Timing(final String description) {
         this.text = description;
      }
   }
}
