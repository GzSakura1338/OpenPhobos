package org.phobos.core.module;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.Named;
import org.phobos.api.ConfigSectionHandler;
import org.phobos.api.KeyValue;
import org.phobos.api.JsonSerializable;
import org.phobos.module.movement.AccelerationModule;
import org.phobos.module.client.AccountModule;
import org.phobos.module.combat.AimBotModule;
import org.phobos.module.player.AirPlaceModule;
import org.phobos.module.render.AmbienceModule;
import org.phobos.module.movement.AnchorModule;
import org.phobos.module.render.AnimationsModule;
import org.phobos.module.misc.AnnouncerModule;
import org.phobos.module.misc.AntiAimModule;
import org.phobos.module.combat.AntiBotModule;
import org.phobos.module.client.AntiCheatModule;
import org.phobos.module.player.AntiClimbModule;
import org.phobos.module.player.AntiCrawlModule;
import org.phobos.module.combat.AntiDropModule;
import org.phobos.module.player.AntiHungerModule;
import org.phobos.module.misc.AntiLagModule;
import org.phobos.module.player.AntiLevitationModule;
import org.phobos.module.combat.AntiMaceModule;
import org.phobos.module.combat.AntiPhaseModule;
import org.phobos.module.combat.AntiRegearModule;
import org.phobos.module.misc.AntiSoundModule;
import org.phobos.module.misc.AntiVanishModule;
import org.phobos.module.movement.AntiVoidModule;
import org.phobos.module.movement.AntiWebModule;
import org.phobos.module.combat.AuraModule;
import org.phobos.module.combat.Auto32KModule;
import org.phobos.module.movement.AutoAlignModule;
import org.phobos.module.combat.AutoAnchorModule;
import org.phobos.module.combat.AutoArmorModule;
import org.phobos.module.combat.AutoArrowBlockModule;
import org.phobos.module.combat.AutoBedModule;
import org.phobos.module.player.AutoBlinkModule;
import org.phobos.module.movement.AutoBoostModule;
import org.phobos.module.movement.AutoChaseModule;
import org.phobos.module.misc.AutoClickerModule;
import org.phobos.module.misc.AutoCraftModule;
import org.phobos.module.combat.AutoCrawlTrapModule;
import org.phobos.module.combat.AutoCreeperModule;
import org.phobos.module.combat.AutoCrystalBlockModule;
import org.phobos.module.combat.AutoCrystalModule;
import org.phobos.module.misc.AutoDisableModule;
import org.phobos.module.combat.AutoDropModule;
import org.phobos.module.misc.AutoDupeModule;
import org.phobos.module.player.AutoEnderChestModule;
import org.phobos.module.player.AutoFireworkModule;
import org.phobos.module.misc.AutoGGModule;
import org.phobos.module.combat.AutoIgniteModule;
import org.phobos.module.misc.AutoInteractModule;
import org.phobos.module.misc.AutoKitModule;
import org.phobos.module.combat.AutoLogModule;
import org.phobos.module.misc.AutoLoginModule;
import org.phobos.module.combat.AutoMaceModule;
import org.phobos.module.combat.AutoMineModule;
import org.phobos.module.combat.AutoPearlBlockModule;
import org.phobos.module.player.AutoPearlModule;
import org.phobos.module.player.AutoPlaceModule;
import org.phobos.module.combat.AutoPotModule;
import org.phobos.module.misc.AutoReconnectModule;
import org.phobos.module.misc.AutoRegearModule;
import org.phobos.module.player.AutoRespawnModule;
import org.phobos.module.combat.AutoRodModule;
import org.phobos.module.combat.AutoShieldModule;
import org.phobos.module.combat.AutoSoupModule;
import org.phobos.module.combat.AutoTNTModule;
import org.phobos.module.misc.AutoTPAModule;
import org.phobos.module.combat.AutoThrowModule;
import org.phobos.module.player.AutoToolModule;
import org.phobos.module.combat.AutoTotemModule;
import org.phobos.module.combat.AutoTrapModule;
import org.phobos.module.movement.AutoTridentModule;
import org.phobos.module.misc.AutoTrollModule;
import org.phobos.module.misc.AutoWalkModule;
import org.phobos.module.combat.AutoWebModule;
import org.phobos.module.player.AutoXPModule;
import org.phobos.module.combat.BacktrackModule;
import org.phobos.module.player.BlinkModule;
import org.phobos.module.render.BlockHighlightModule;
import org.phobos.module.movement.BlockLagModule;
import org.phobos.module.player.BlockTweaksModule;
import org.phobos.module.combat.BlockerModule;
import org.phobos.module.movement.BoatFlyModule;
import org.phobos.module.combat.BowKillerModule;
import org.phobos.module.combat.BowSpamModule;
import org.phobos.module.render.BreadcrumbsModule;
import org.phobos.module.render.BreakHighlightModule;
import org.phobos.module.combat.BreakerModule;
import org.phobos.module.player.BridgeModule;
import org.phobos.module.player.BuilderModule;
import org.phobos.module.player.CTweaksModule;
import org.phobos.module.client.CalculationsModule;
import org.phobos.module.render.CameraModule;
import org.phobos.module.client.CapesModule;
import org.phobos.module.render.ChamsModule;
import org.phobos.module.misc.ChatModule;
import org.phobos.module.misc.ChestStealerModule;
import org.phobos.module.render.ChorusHighlightModule;
import org.phobos.module.player.ClipModule;
import org.phobos.module.client.ColorsModule;
import org.phobos.module.combat.CombatPlaceModule;
import org.phobos.module.misc.CrasherModule;
import org.phobos.module.combat.CriticalsModule;
import org.phobos.module.render.CrosshairModule;
import org.phobos.module.combat.DStrolModule;
import org.phobos.module.movement.DisablerModule;
import org.phobos.module.combat.DoubleHandModule;
import org.phobos.module.combat.DoubleTapModule;
import org.phobos.module.movement.ElytraFlyModule;
import org.phobos.module.client.EngineModule;
import org.phobos.module.movement.EntityControlModule;
import org.phobos.module.movement.EntitySpeedModule;
import org.phobos.module.misc.ExtraTabModule;
import org.phobos.module.render.FXAAModule;
import org.phobos.module.movement.FakeFlyModule;
import org.phobos.module.movement.FakeLagModule;
import org.phobos.module.misc.FakeLatencyModule;
import org.phobos.module.client.FakePlayerModule2;
import org.phobos.module.player.FastEatModule;
import org.phobos.module.movement.FastFallModule;
import org.phobos.module.misc.FastLatencyModule;
import org.phobos.module.player.FastPlaceModule;
import org.phobos.module.movement.FastSwimModule;
import org.phobos.module.misc.FastTransactionsModule;
import org.phobos.module.movement.FireworksModule;
import org.phobos.module.misc.FlagDetectorModule;
import org.phobos.module.movement.FlightModule;
import org.phobos.module.client.FontModule;
import org.phobos.module.render.FreeLookModule;
import org.phobos.module.render.FreecamModule;
import org.phobos.module.render.FullbrightModule;
import org.phobos.module.client.GameTiersModule;
import org.phobos.module.render.GhostsModule;
import org.phobos.module.movement.HighJumpModule;
import org.phobos.module.misc.HitSoundModule;
import org.phobos.module.combat.HoleFillModule;
import org.phobos.module.movement.HoleShiftModule;
import org.phobos.module.movement.HoleSnapModule;
import org.phobos.module.render.HolesModule;
import org.phobos.module.client.HudModule;
import org.phobos.module.client.IRCModule;
import org.phobos.module.movement.IceSpeedModule;
import org.phobos.module.misc.IllegalDisconnectModule;
import org.phobos.module.client.IncognitoModule;
import org.phobos.module.player.InfDurabilityModule;
import org.phobos.module.player.InvCleanerModule;
import org.phobos.module.player.InvLockModule;
import org.phobos.module.player.InvManagerModule;
import org.phobos.module.movement.InvMoveModule;
import org.phobos.module.player.InvSyncModule;
import org.phobos.module.render.ItemPhysicsModule;
import org.phobos.module.render.ItemsModule;
import org.phobos.module.movement.JesusModule;
import org.phobos.module.movement.JumpXtendModule;
import org.phobos.module.combat.KeepSprintModule;
import org.phobos.module.render.KillEffectModule;
import org.phobos.module.misc.KillSoundModule;
import org.phobos.module.combat.LagSwitchModule;
import org.phobos.module.player.LegitHelperModule;
import org.phobos.module.render.LobotomyModule;
import org.phobos.module.render.LogoutSpotsModule;
import org.phobos.module.movement.LongJumpModule;
import org.phobos.module.client.MediaModule;
import org.phobos.module.misc.MiddleClickModule;
import org.phobos.module.Module;
import org.phobos.module.combat.MoreKBModule;
import org.phobos.module.render.MotionBlurModule;
import org.phobos.module.misc.MountBypassModule;
import org.phobos.module.player.MultiTaskModule;
import org.phobos.module.render.NametagsModule;
import org.phobos.module.render.NewChunksModule;
import org.phobos.module.render.NoBobModule;
import org.phobos.module.movement.NoFallModule;
import org.phobos.module.player.NoInteractModule;
import org.phobos.module.misc.NoInterpModule;
import org.phobos.module.movement.NoJumpDelayModule;
import org.phobos.module.misc.NoPacketKickModule;
import org.phobos.module.movement.NoPushModule;
import org.phobos.module.render.NoRenderModule;
import org.phobos.module.player.NoRotateModule;
import org.phobos.module.misc.NoServerPackModule;
import org.phobos.module.movement.NoSlowModule;
import org.phobos.module.client.NotificationsModule;
import org.phobos.module.player.NukerModule;
import org.phobos.module.movement.PacketFlyModule;
import org.phobos.module.misc.ParticlesModule;
import org.phobos.module.render.PhaseHighlightModule;
import org.phobos.module.movement.PhaseModule;
import org.phobos.module.render.PhaseStatusModule;
import org.phobos.module.movement.PhaseWalkModule;
import org.phobos.module.client.PhobosModule;
import org.phobos.module.render.PopChamsModule;
import org.phobos.module.misc.PortalsModule;
import org.phobos.module.render.PredictionsModule;
import org.phobos.module.combat.PusherModule;
import org.phobos.module.combat.QuiverModule;
import org.phobos.module.client.RPCModule;
import org.phobos.module.player.ReachModule;
import org.phobos.module.client.RenderModule;
import org.phobos.module.misc.ReplenishModule;
import org.phobos.module.render.RescaleModule;
import org.phobos.module.client.RotationsModule;
import org.phobos.module.movement.SafeWalkModule;
import org.phobos.module.player.ScaffoldModule;
import org.phobos.module.misc.ScreenshotModule;
import org.phobos.module.render.SearchModule;
import org.phobos.module.combat.SelfTrapModule;
import org.phobos.module.render.SetDeadModule;
import org.phobos.module.render.ShaderModule;
import org.phobos.module.player.SilentHotbarModule;
import org.phobos.module.render.SkeletonModule;
import org.phobos.module.movement.SlowWalkModule;
import org.phobos.module.player.SneakModule;
import org.phobos.module.misc.SoundFixerModule;
import org.phobos.module.misc.SpammerModule;
import org.phobos.module.movement.SpeedModule;
import org.phobos.module.player.SpeedmineModule;
import org.phobos.module.misc.SpooferModule;
import org.phobos.module.movement.SprintModule;
import org.phobos.module.misc.StaffDetectorModule;
import org.phobos.module.movement.StairsModule;
import org.phobos.module.movement.StepModule;
import org.phobos.module.player.SuicideModule;
import org.phobos.module.combat.SurroundModule;
import org.phobos.module.movement.TargetStrafeModule;
import org.phobos.module.client.TestModule2Module;
import org.phobos.module.client.TestModuleModule;
import org.phobos.module.combat.TickBaseModule;
import org.phobos.module.movement.TickShiftModule;
import org.phobos.module.player.TimerModule;
import org.phobos.module.misc.TooltipsModule;
import org.phobos.module.render.TracersModule;
import org.phobos.module.combat.TrackerModule;
import org.phobos.module.render.TrailsModule;
import org.phobos.module.render.TrajectoriesModule;
import org.phobos.module.combat.TriggerBotModule;
import org.phobos.module.client.UIModule;
import org.phobos.module.misc.UnfocusedCPUModule;
import org.phobos.module.movement.VelocityModule;
import org.phobos.module.render.ViewModelModule;
import org.phobos.module.player.VirtualHotbarModule;
import org.phobos.module.combat.WTapModule;
import org.phobos.module.movement.WallClimbModule;
import org.phobos.module.render.WaypointsModule;
import org.phobos.module.misc.WeatherModule;
import org.phobos.module.player.XCarryModule;
import org.phobos.module.render.XRayModule;
import org.phobos.module.player.YawLockModule;
import org.phobos.module.render.ZoomModule;
import org.phobos.type.BINDSMODULESEnum;
import org.phobos.core.command.CommandManager;
import org.phobos.core.Feature;
import org.phobos.module.player.LegacyModule;
import org.phobos.core.command.ModuleSettingCommand;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModuleSet;
import org.phobos.core.user.UserSession;

public class ModuleManager extends Feature implements JsonSerializable, Named, ConfigSectionHandler {
   public final PlacementModuleSet placementModuleSet;
   public final Map<Category, List<Module>> categoryMap;
   public PhobosClient phobosClient;
   private static Object[] phobos$g1806cd6e0;
   private static Object[] phobos$g1806cd468;
   private static Object[] phobos$g1806cd560;
   private List<Module> list;

   public Optional<KeyValue<Module, String>> resolveOptional(String text) {
      Module module = this.resolveOptional3(text).orElse(null);
      if (module == null) {
         return Optional.empty();
      }

      try {
         JsonElement jsonElement = module.createJsonPrimitive();
         return Optional.of(KeyValue.createValue(module, jsonElement.toString()));
      } catch (Throwable throwable) {
         return Optional.empty();
      }
   }

   public void handleText(String text) {
      try {
         String[] texts = text.split("##");
         String text2 = texts[1];
         this.resolveOptional3(text2).ifPresent(module -> {
            JsonElement jsonElement = JsonParser.parseString(texts[2]);
            module.handleJsonElement(jsonElement);
         });
      } catch (Throwable var4) {
      }
   }

   public Map<Category, List<Module>> getMap() {
      List<Module> list = this.resolveList(UIModule.getUIModule().order.getValue());
      Map<Category, List<Module>> map = new LinkedHashMap<>();

      for (Category category : Categories.getCategoryList()) {
         map.put(category, list.stream().filter(module -> Objects.equals(module.getCategory(), category)).toList());
      }

      return map;
   }

   public void handleModule(Module module) {
      this.getList2().remove(module);
   }

   @Override
   public JsonElement resolveJsonElement(BINDSMODULESEnum bINDSMODULESEnum) {
      JsonObject jsonObject = new JsonObject();

      for (Module module : this.getList2()) {
         jsonObject.add(module.getName(), module.createJsonPrimitive());
      }

      return jsonObject;
   }

   @Nullable
   public Module resolveModule(String text) {
      for (Module module : this.getList2()) {
         if (module.describe3().startsWith(text)) {
            return module;
         }
      }

      return null;
   }

   public void handleModule2(Module module) {
      List var2 = this.getList2();
      boolean var3 = var2.contains(module);
      if (!var3) {
         byte var4;
         if (module == null) {
            var4 = 0;
         } else {
            var4 = 1;
         }

         Preconditions.checkArgument((((int)(var4 & 0xFF & 0xFF) & 1) != 0), "Cannot register null!");
         String var5 = module.getName();
         byte var6;
         if (var5 == null) {
            var6 = 0;
         } else {
            var6 = 1;
         }

         Preconditions.checkArgument((((int)(var6 & 0xFF & 0xFF) & 1) != 0), "A module should be named!");
         Category var7 = module.getCategory();
         byte var8;
         if (var7 == null) {
            var8 = 0;
         } else {
            var8 = 1;
         }

         Preconditions.checkArgument((((int)(var8 & 0xFF & 0xFF) & 1) != 0), "Cannot register a module with no category!");

         Throwable var45;
         label78: {
            CommandManager.CommandLoader var9;
            try {
               var9 = CommandManager.CommandLoader.getINSTANCE();
            } catch (Throwable var53) {
               var45 = var53;
               boolean var46 = true;
               if (var46) {
                  break label78;
               }

               throw var53;
            }

            ModuleManager var10000 = this;
            Module cap17 = module;
            ModuleManager cap16 = var10000;
            Runnable var26 = () -> cap16.handleModule3(cap17);

            try {
               var9.handleRunnable(var26);
            } catch (Throwable var52) {
               var45 = var52;
               boolean var54 = true;
               if (var54) {
                  break label78;
               }

               throw var52;
            }

            List var27;
            try {
               var27 = this.getList2();
            } catch (Throwable var51) {
               var45 = var51;
               boolean var55 = true;
               if (var55) {
                  break label78;
               }

               throw var51;
            }

            Comparator<Module> var44 = Named::computeNamed;

            try {
               this.handleList(var27, module, var44);
               return;
            } catch (Throwable var50) {
               var45 = var50;
               boolean var56 = true;
               if (!var56) {
                  throw var50;
               }
            }
         }

         PrintStream var47 = System.err;
         var45.printStackTrace(var47);
      }
   }

   public void run3() {
      ArrayList var1 = new ArrayList(5);
      Categories.handleList(var1);
      Consumer<Category> var18 = this::handleCategory;
      var1.forEach(var18);
   }

   public void handlePhobosClient(PhobosClient phobosClient) {
      this.phobosClient = phobosClient;
      boolean var2 = UserSession.check2();
      boolean var3 = UserSession.check();
      AuraModule var4 = new AuraModule(phobosClient);
      this.handleModule2(var4);
      KeepSprintModule var5 = new KeepSprintModule(phobosClient);
      this.handleModule2(var5);
      BacktrackModule var6 = new BacktrackModule(phobosClient);
      this.handleModule2(var6);
      AutoCrystalModule var7 = new AutoCrystalModule(phobosClient);
      this.handleModule2(var7);
      SurroundModule var8 = new SurroundModule(phobosClient);
      this.handleModule2(var8);
      AutoTotemModule var9 = new AutoTotemModule(phobosClient);
      this.handleModule2(var9);
      AutoMineModule var10 = new AutoMineModule(phobosClient);
      this.handleModule2(var10);
      AutoAnchorModule var11 = new AutoAnchorModule(phobosClient);
      this.handleModule2(var11);
      AutoArmorModule var12 = new AutoArmorModule(phobosClient);
      this.handleModule2(var12);
      AutoTrapModule var13 = new AutoTrapModule(phobosClient);
      this.handleModule2(var13);
      LagSwitchModule var14 = new LagSwitchModule(phobosClient);
      this.handleModule2(var14);
      DoubleTapModule var15 = new DoubleTapModule(phobosClient);
      this.handleModule2(var15);
      CriticalsModule var16 = new CriticalsModule(phobosClient);
      this.handleModule2(var16);
      HoleFillModule var17 = new HoleFillModule(phobosClient);
      this.handleModule2(var17);
      SelfTrapModule var18 = new SelfTrapModule(phobosClient);
      this.handleModule2(var18);
      BlockerModule var19 = new BlockerModule(phobosClient);
      this.handleModule2(var19);
      AntiBotModule var20 = new AntiBotModule(phobosClient);
      this.handleModule2(var20);
      AutoMaceModule var21 = new AutoMaceModule(phobosClient);
      this.handleModule2(var21);
      AutoBedModule var22 = new AutoBedModule(phobosClient);
      this.handleModule2(var22);
      AutoSoupModule var23 = new AutoSoupModule(phobosClient);
      this.handleModule2(var23);
      AutoRodModule var24 = new AutoRodModule(phobosClient);
      this.handleModule2(var24);
      AutoThrowModule var25 = new AutoThrowModule(phobosClient);
      this.handleModule2(var25);
      MoreKBModule var26 = new MoreKBModule(phobosClient);
      this.handleModule2(var26);
      WTapModule var27 = new WTapModule(phobosClient);
      this.handleModule2(var27);
      AutoCrawlTrapModule var28 = new AutoCrawlTrapModule(phobosClient);
      this.handleModule2(var28);
      AutoWebModule var29 = new AutoWebModule(phobosClient);
      this.handleModule2(var29);
      AutoPearlBlockModule var30 = new AutoPearlBlockModule(phobosClient);
      this.handleModule2(var30);
      AutoCrystalBlockModule var31 = new AutoCrystalBlockModule(phobosClient);
      this.handleModule2(var31);
      BowKillerModule var32 = new BowKillerModule(phobosClient);
      this.handleModule2(var32);
      AutoArrowBlockModule var33 = new AutoArrowBlockModule(phobosClient);
      this.handleModule2(var33);
      AutoPotModule var34 = new AutoPotModule(phobosClient);
      this.handleModule2(var34);
      DStrolModule var35 = new DStrolModule(phobosClient);
      this.handleModule2(var35);
      DoubleHandModule var36 = new DoubleHandModule(phobosClient);
      this.handleModule2(var36);
      BreakerModule var37 = new BreakerModule(phobosClient);
      this.handleModule2(var37);
      AutoDropModule var38 = new AutoDropModule(phobosClient);
      this.handleModule2(var38);
      TickBaseModule var39 = new TickBaseModule(phobosClient);
      this.handleModule2(var39);
      TriggerBotModule var40 = new TriggerBotModule(phobosClient);
      this.handleModule2(var40);
      Auto32KModule var41 = new Auto32KModule(phobosClient);
      this.handleModule2(var41);
      CombatPlaceModule var42 = new CombatPlaceModule(phobosClient);
      this.handleModule2(var42);
      AntiRegearModule var43 = new AntiRegearModule(phobosClient);
      this.handleModule2(var43);
      AutoTNTModule var44 = new AutoTNTModule(phobosClient);
      this.handleModule2(var44);
      BowSpamModule var45 = new BowSpamModule(phobosClient);
      this.handleModule2(var45);
      AntiMaceModule var46 = new AntiMaceModule(phobosClient);
      this.handleModule2(var46);
      PusherModule var47 = new PusherModule(phobosClient);
      this.handleModule2(var47);
      AimBotModule var48 = new AimBotModule(phobosClient);
      this.handleModule2(var48);
      AntiPhaseModule var49 = new AntiPhaseModule(phobosClient);
      this.handleModule2(var49);
      AutoLogModule var50 = new AutoLogModule(phobosClient);
      this.handleModule2(var50);
      QuiverModule var51 = new QuiverModule(phobosClient);
      this.handleModule2(var51);
      AntiDropModule var52 = new AntiDropModule(phobosClient);
      this.handleModule2(var52);
      if (var2) {
         AutoIgniteModule var237 = new AutoIgniteModule(phobosClient);
         this.handleModule2(var237);
         AutoCreeperModule var238 = new AutoCreeperModule(phobosClient);
         this.handleModule2(var238);
         AutoShieldModule var239 = new AutoShieldModule(phobosClient);
         this.handleModule2(var239);
         TrackerModule var240 = new TrackerModule(phobosClient);
         this.handleModule2(var240);
      }

      ChestStealerModule var53 = new ChestStealerModule(phobosClient);
      this.handleModule2(var53);
      FastLatencyModule var54 = new FastLatencyModule(phobosClient);
      this.handleModule2(var54);
      AntiAimModule var55 = new AntiAimModule(phobosClient);
      this.handleModule2(var55);
      MiddleClickModule var56 = new MiddleClickModule(phobosClient);
      this.handleModule2(var56);
      AutoWalkModule var57 = new AutoWalkModule(phobosClient);
      this.handleModule2(var57);
      ExtraTabModule var58 = new ExtraTabModule(phobosClient);
      this.handleModule2(var58);
      AntiVanishModule var59 = new AntiVanishModule(phobosClient);
      this.handleModule2(var59);
      AutoReconnectModule var60 = new AutoReconnectModule(phobosClient);
      this.handleModule2(var60);
      NoPacketKickModule var61 = new NoPacketKickModule(phobosClient);
      this.handleModule2(var61);
      NoServerPackModule var62 = new NoServerPackModule(phobosClient);
      this.handleModule2(var62);
      ChatModule var63 = new ChatModule(phobosClient);
      this.handleModule2(var63);
      ReplenishModule var64 = new ReplenishModule(phobosClient);
      this.handleModule2(var64);
      MultiTaskModule var65 = new MultiTaskModule(phobosClient);
      this.handleModule2(var65);
      AutoTPAModule var66 = new AutoTPAModule(phobosClient);
      this.handleModule2(var66);
      AutoKitModule var67 = new AutoKitModule(phobosClient);
      this.handleModule2(var67);
      FakeLatencyModule var68 = new FakeLatencyModule(phobosClient);
      this.handleModule2(var68);
      WeatherModule var69 = new WeatherModule(phobosClient);
      this.handleModule2(var69);
      TooltipsModule var70 = new TooltipsModule(phobosClient);
      this.handleModule2(var70);
      SpooferModule var71 = new SpooferModule(phobosClient);
      this.handleModule2(var71);
      NoInterpModule var72 = new NoInterpModule(phobosClient);
      this.handleModule2(var72);
      UnfocusedCPUModule var73 = new UnfocusedCPUModule(phobosClient);
      this.handleModule2(var73);
      IllegalDisconnectModule var74 = new IllegalDisconnectModule(phobosClient);
      this.handleModule2(var74);
      ParticlesModule var75 = new ParticlesModule(phobosClient);
      this.handleModule2(var75);
      FlagDetectorModule var76 = new FlagDetectorModule(phobosClient);
      this.handleModule2(var76);
      AutoDupeModule var77 = new AutoDupeModule(phobosClient);
      this.handleModule2(var77);
      CrasherModule var78 = new CrasherModule(phobosClient);
      this.handleModule2(var78);
      PortalsModule var79 = new PortalsModule(phobosClient);
      this.handleModule2(var79);
      AutoGGModule var80 = new AutoGGModule(phobosClient);
      this.handleModule2(var80);
      AutoLoginModule var81 = new AutoLoginModule(phobosClient);
      this.handleModule2(var81);
      SpammerModule var82 = new SpammerModule(phobosClient);
      this.handleModule2(var82);
      KillSoundModule var83 = new KillSoundModule(phobosClient);
      this.handleModule2(var83);
      HitSoundModule var84 = new HitSoundModule(phobosClient);
      this.handleModule2(var84);
      SoundFixerModule var85 = new SoundFixerModule(phobosClient);
      this.handleModule2(var85);
      AntiLagModule var86 = new AntiLagModule(phobosClient);
      this.handleModule2(var86);
      AutoInteractModule var87 = new AutoInteractModule(phobosClient);
      this.handleModule2(var87);
      AutoDisableModule var88 = new AutoDisableModule(phobosClient);
      this.handleModule2(var88);
      AutoClickerModule var89 = new AutoClickerModule(phobosClient);
      this.handleModule2(var89);
      AntiSoundModule var90 = new AntiSoundModule(phobosClient);
      this.handleModule2(var90);
      MountBypassModule var91 = new MountBypassModule(phobosClient);
      this.handleModule2(var91);
      StaffDetectorModule var92 = new StaffDetectorModule(phobosClient);
      this.handleModule2(var92);
      AutoRegearModule var93 = new AutoRegearModule(phobosClient);
      this.handleModule2(var93);
      ScreenshotModule var94 = new ScreenshotModule(phobosClient);
      this.handleModule2(var94);
      AutoCraftModule var95 = new AutoCraftModule(phobosClient);
      this.handleModule2(var95);
      AnnouncerModule var96 = new AnnouncerModule(phobosClient);
      this.handleModule2(var96);
      AutoTrollModule var97 = new AutoTrollModule(phobosClient);
      this.handleModule2(var97);
      if (var2) {
         FastTransactionsModule var241 = new FastTransactionsModule(phobosClient);
         this.handleModule2(var241);
      }

      FullbrightModule var98 = new FullbrightModule(phobosClient);
      this.handleModule2(var98);
      ShaderModule var99 = new ShaderModule(phobosClient);
      this.handleModule2(var99);
      NoRenderModule var100 = new NoRenderModule(phobosClient);
      this.handleModule2(var100);
      NametagsModule var101 = new NametagsModule(phobosClient);
      this.handleModule2(var101);
      ZoomModule var102 = new ZoomModule(phobosClient);
      this.handleModule2(var102);
      FreeLookModule var103 = new FreeLookModule(phobosClient);
      this.handleModule2(var103);
      TrailsModule var104 = new TrailsModule(phobosClient);
      this.handleModule2(var104);
      BreadcrumbsModule var105 = new BreadcrumbsModule(phobosClient);
      this.handleModule2(var105);
      ItemsModule var106 = new ItemsModule(phobosClient);
      this.handleModule2(var106);
      BlockHighlightModule var107 = new BlockHighlightModule(phobosClient);
      this.handleModule2(var107);
      ViewModelModule var108 = new ViewModelModule(phobosClient);
      this.handleModule2(var108);
      XRayModule var109 = new XRayModule(phobosClient);
      this.handleModule2(var109);
      AnimationsModule var110 = new AnimationsModule(phobosClient);
      this.handleModule2(var110);
      KillEffectModule var111 = new KillEffectModule(phobosClient);
      this.handleModule2(var111);
      AmbienceModule var112 = new AmbienceModule(phobosClient);
      this.handleModule2(var112);
      MotionBlurModule var113 = new MotionBlurModule(phobosClient);
      this.handleModule2(var113);
      ChamsModule var114 = new ChamsModule(phobosClient);
      this.handleModule2(var114);
      PopChamsModule var115 = new PopChamsModule(phobosClient);
      this.handleModule2(var115);
      CameraModule var116 = new CameraModule(phobosClient);
      this.handleModule2(var116);
      SetDeadModule var117 = new SetDeadModule(phobosClient);
      this.handleModule2(var117);
      TracersModule var118 = new TracersModule(phobosClient);
      this.handleModule2(var118);
      TrajectoriesModule var119 = new TrajectoriesModule(phobosClient);
      this.handleModule2(var119);
      SearchModule var120 = new SearchModule(phobosClient);
      this.handleModule2(var120);
      RescaleModule var121 = new RescaleModule(phobosClient);
      this.handleModule2(var121);
      HolesModule var122 = new HolesModule(phobosClient);
      this.handleModule2(var122);
      NewChunksModule var123 = new NewChunksModule(phobosClient);
      this.handleModule2(var123);
      GhostsModule var124 = new GhostsModule(phobosClient);
      this.handleModule2(var124);
      PhaseStatusModule var125 = new PhaseStatusModule(phobosClient);
      this.handleModule2(var125);
      if (var2) {
         PredictionsModule var242 = new PredictionsModule(phobosClient);
         this.handleModule2(var242);
         LobotomyModule var243 = new LobotomyModule(phobosClient);
         this.handleModule2(var243);
      }

      BreakHighlightModule var126 = new BreakHighlightModule(phobosClient);
      this.handleModule2(var126);
      PhaseHighlightModule var127 = new PhaseHighlightModule(phobosClient);
      this.handleModule2(var127);
      WaypointsModule var128 = new WaypointsModule(phobosClient);
      this.handleModule2(var128);
      SkeletonModule var129 = new SkeletonModule(phobosClient);
      this.handleModule2(var129);
      CrosshairModule var130 = new CrosshairModule(phobosClient);
      this.handleModule2(var130);
      ChorusHighlightModule var131 = new ChorusHighlightModule(phobosClient);
      this.handleModule2(var131);
      FXAAModule var132 = new FXAAModule(phobosClient);
      this.handleModule2(var132);
      ItemPhysicsModule var133 = new ItemPhysicsModule(phobosClient);
      this.handleModule2(var133);
      FreecamModule var134 = new FreecamModule(phobosClient);
      this.handleModule2(var134);
      LogoutSpotsModule var135 = new LogoutSpotsModule(phobosClient);
      this.handleModule2(var135);
      NoBobModule var136 = new NoBobModule(phobosClient);
      this.handleModule2(var136);
      VelocityModule var137 = new VelocityModule(phobosClient);
      this.handleModule2(var137);
      NoSlowModule var138 = new NoSlowModule(phobosClient);
      this.handleModule2(var138);
      SprintModule var139 = new SprintModule(phobosClient);
      this.handleModule2(var139);
      NoPushModule var140 = new NoPushModule(phobosClient);
      this.handleModule2(var140);
      InvMoveModule var141 = new InvMoveModule(phobosClient);
      this.handleModule2(var141);
      SpeedModule var142 = new SpeedModule(phobosClient);
      this.handleModule2(var142);
      NoJumpDelayModule var143 = new NoJumpDelayModule(phobosClient);
      this.handleModule2(var143);
      FlightModule var144 = new FlightModule(phobosClient);
      this.handleModule2(var144);
      AutoBoostModule var145 = new AutoBoostModule(phobosClient);
      this.handleModule2(var145);
      AntiVoidModule var146 = new AntiVoidModule(phobosClient);
      this.handleModule2(var146);
      LongJumpModule var147 = new LongJumpModule(phobosClient);
      this.handleModule2(var147);
      TargetStrafeModule var148 = new TargetStrafeModule(phobosClient);
      this.handleModule2(var148);
      StepModule var149 = new StepModule(phobosClient);
      this.handleModule2(var149);
      DisablerModule var150 = new DisablerModule(phobosClient);
      this.handleModule2(var150);
      FastFallModule var151 = new FastFallModule(phobosClient);
      this.handleModule2(var151);
      AccelerationModule var152 = new AccelerationModule(phobosClient);
      this.handleModule2(var152);
      AutoChaseModule var153 = new AutoChaseModule(phobosClient);
      this.handleModule2(var153);
      ElytraFlyModule var154 = new ElytraFlyModule(phobosClient);
      this.handleModule2(var154);
      NoFallModule var155 = new NoFallModule(phobosClient);
      this.handleModule2(var155);
      HoleSnapModule var156 = new HoleSnapModule(phobosClient);
      this.handleModule2(var156);
      PhaseModule var157 = new PhaseModule(phobosClient);
      this.handleModule2(var157);
      AutoTridentModule var158 = new AutoTridentModule(phobosClient);
      this.handleModule2(var158);
      HoleShiftModule var159 = new HoleShiftModule(phobosClient);
      this.handleModule2(var159);
      TickShiftModule var160 = new TickShiftModule(phobosClient);
      this.handleModule2(var160);
      JesusModule var161 = new JesusModule(phobosClient);
      this.handleModule2(var161);
      StairsModule var162 = new StairsModule(phobosClient);
      this.handleModule2(var162);
      IceSpeedModule var163 = new IceSpeedModule(phobosClient);
      this.handleModule2(var163);
      AntiWebModule var164 = new AntiWebModule(phobosClient);
      this.handleModule2(var164);
      FastSwimModule var165 = new FastSwimModule(phobosClient);
      this.handleModule2(var165);
      PacketFlyModule var166 = new PacketFlyModule(phobosClient);
      this.handleModule2(var166);
      EntitySpeedModule var167 = new EntitySpeedModule(phobosClient);
      this.handleModule2(var167);
      EntityControlModule var168 = new EntityControlModule(phobosClient);
      this.handleModule2(var168);
      BlockLagModule var169 = new BlockLagModule(phobosClient);
      this.handleModule2(var169);
      FakeFlyModule var170 = new FakeFlyModule(phobosClient);
      this.handleModule2(var170);
      WallClimbModule var171 = new WallClimbModule(phobosClient);
      this.handleModule2(var171);
      SafeWalkModule var172 = new SafeWalkModule(phobosClient);
      this.handleModule2(var172);
      HighJumpModule var173 = new HighJumpModule(phobosClient);
      this.handleModule2(var173);
      PhaseWalkModule var174 = new PhaseWalkModule(phobosClient);
      this.handleModule2(var174);
      AutoAlignModule var175 = new AutoAlignModule(phobosClient);
      this.handleModule2(var175);
      AnchorModule var176 = new AnchorModule(phobosClient);
      this.handleModule2(var176);
      FireworksModule var177 = new FireworksModule(phobosClient);
      this.handleModule2(var177);
      if (var2) {
         BoatFlyModule var244 = new BoatFlyModule(phobosClient);
         this.handleModule2(var244);
         FakeLagModule var245 = new FakeLagModule(phobosClient);
         this.handleModule2(var245);
         SlowWalkModule var246 = new SlowWalkModule(phobosClient);
         this.handleModule2(var246);
         JumpXtendModule var247 = new JumpXtendModule(phobosClient);
         this.handleModule2(var247);
      }

      ScaffoldModule var178 = new ScaffoldModule(phobosClient);
      this.handleModule2(var178);
      NoRotateModule var179 = new NoRotateModule(phobosClient);
      this.handleModule2(var179);
      ClipModule var180 = new ClipModule(phobosClient);
      this.handleModule2(var180);
      SpeedmineModule var181 = new SpeedmineModule(phobosClient);
      this.handleModule2(var181);
      AutoXPModule var182 = new AutoXPModule(phobosClient);
      this.handleModule2(var182);
      SuicideModule var183 = new SuicideModule(phobosClient);
      this.handleModule2(var183);
      AutoRespawnModule var184 = new AutoRespawnModule(phobosClient);
      this.handleModule2(var184);
      InvCleanerModule var185 = new InvCleanerModule(phobosClient);
      this.handleModule2(var185);
      TimerModule var186 = new TimerModule(phobosClient);
      this.handleModule2(var186);
      InvManagerModule var187 = new InvManagerModule(phobosClient);
      this.handleModule2(var187);
      FastPlaceModule var188 = new FastPlaceModule(phobosClient);
      this.handleModule2(var188);
      AutoPlaceModule var189 = new AutoPlaceModule(phobosClient);
      this.handleModule2(var189);
      AirPlaceModule var190 = new AirPlaceModule(phobosClient);
      this.handleModule2(var190);
      BlinkModule var191 = new BlinkModule(phobosClient);
      this.handleModule2(var191);
      AutoBlinkModule var192 = new AutoBlinkModule(phobosClient);
      this.handleModule2(var192);
      NoInteractModule var193 = new NoInteractModule(phobosClient);
      this.handleModule2(var193);
      AutoEnderChestModule var194 = new AutoEnderChestModule(phobosClient);
      this.handleModule2(var194);
      ReachModule var195 = new ReachModule(phobosClient);
      this.handleModule2(var195);
      LegacyModule var196 = new LegacyModule(phobosClient);
      this.handleModule2(var196);
      InvLockModule var197 = new InvLockModule(phobosClient);
      this.handleModule2(var197);
      BridgeModule var198 = new BridgeModule(phobosClient);
      this.handleModule2(var198);
      BlockTweaksModule var199 = new BlockTweaksModule(phobosClient);
      this.handleModule2(var199);
      XCarryModule var200 = new XCarryModule(phobosClient);
      this.handleModule2(var200);
      AntiHungerModule var201 = new AntiHungerModule(phobosClient);
      this.handleModule2(var201);
      SneakModule var202 = new SneakModule(phobosClient);
      this.handleModule2(var202);
      FastEatModule var203 = new FastEatModule(phobosClient);
      this.handleModule2(var203);
      AntiLevitationModule var204 = new AntiLevitationModule(phobosClient);
      this.handleModule2(var204);
      AutoFireworkModule var205 = new AutoFireworkModule(phobosClient);
      this.handleModule2(var205);
      AutoToolModule var206 = new AutoToolModule(phobosClient);
      this.handleModule2(var206);
      AutoPearlModule var207 = new AutoPearlModule(phobosClient);
      this.handleModule2(var207);
      VirtualHotbarModule var208 = new VirtualHotbarModule(phobosClient);
      this.handleModule2(var208);
      AntiClimbModule var209 = new AntiClimbModule(phobosClient);
      this.handleModule2(var209);
      NukerModule var210 = new NukerModule(phobosClient);
      this.handleModule2(var210);
      BuilderModule var211 = new BuilderModule(phobosClient);
      this.handleModule2(var211);
      if (var2) {
         InfDurabilityModule var248 = new InfDurabilityModule(phobosClient);
         this.handleModule2(var248);
      }

      SilentHotbarModule var212 = new SilentHotbarModule(phobosClient);
      this.handleModule2(var212);
      AntiCrawlModule var213 = new AntiCrawlModule(phobosClient);
      this.handleModule2(var213);
      YawLockModule var214 = new YawLockModule(phobosClient);
      this.handleModule2(var214);
      LegitHelperModule var215 = new LegitHelperModule(phobosClient);
      this.handleModule2(var215);
      CTweaksModule var216 = new CTweaksModule(phobosClient);
      this.handleModule2(var216);
      InvSyncModule var217 = new InvSyncModule(phobosClient);
      this.handleModule2(var217);
      FontModule var218 = new FontModule(phobosClient);
      this.handleModule2(var218);
      UIModule var219 = new UIModule(phobosClient);
      this.handleModule2(var219);
      RotationsModule var220 = new RotationsModule(phobosClient);
      this.handleModule2(var220);
      AccountModule var221 = new AccountModule(phobosClient);
      this.handleModule2(var221);
      HudModule var222 = new HudModule(phobosClient);
      this.handleModule2(var222);
      ColorsModule var223 = new ColorsModule(phobosClient);
      this.handleModule2(var223);
      CalculationsModule var224 = new CalculationsModule(phobosClient);
      this.handleModule2(var224);
      RenderModule var225 = new RenderModule(phobosClient);
      this.handleModule2(var225);
      NotificationsModule var226 = new NotificationsModule(phobosClient);
      this.handleModule2(var226);
      AntiCheatModule var227 = new AntiCheatModule(phobosClient);
      this.handleModule2(var227);
      IRCModule var228 = new IRCModule(phobosClient);
      this.handleModule2(var228);
      IncognitoModule var229 = new IncognitoModule(phobosClient);
      this.handleModule2(var229);
      MediaModule var230 = new MediaModule(phobosClient);
      this.handleModule2(var230);
      EngineModule var231 = new EngineModule(phobosClient);
      this.handleModule2(var231);
      CapesModule var232 = new CapesModule(phobosClient);
      this.handleModule2(var232);
      FakePlayerModule2 var233 = new FakePlayerModule2(phobosClient);
      this.handleModule2(var233);
      GameTiersModule var234 = new GameTiersModule(phobosClient);
      this.handleModule2(var234);
      RPCModule var235 = new RPCModule(phobosClient);
      this.handleModule2(var235);
      PhobosModule var236 = new PhobosModule(phobosClient);
      this.handleModule2(var236);
      if (var3) {
         TestModuleModule var249 = new TestModuleModule(phobosClient);
         this.handleModule2(var249);
         TestModule2Module var250 = new TestModule2Module(phobosClient);
         this.handleModule2(var250);
      }

      this.run3();
   }

   @Override
   public String getName() {
      return "modules.json";
   }

   public <T extends Named> void handleList(List<T> list, T named, Comparator<T> comparator) {
      for (int i = 0; i < list.size(); i++) {
         T named2 = (T)list.get(i);
         if (comparator.compare(named2, named) >= 0) {
            list.add(i, named);
            return;
         }
      }

      list.add(named);
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();

      for (Module module : this.getList2()) {
         JsonElement jsonElement2 = jsonObject.get(module.getName());
         if (jsonElement2 == null) {
            module.run10();
         } else {
            module.handleJsonElement(jsonElement2);
         }
      }
   }

   public <C extends Module> Optional<C> resolveOptional2(Class<C> classValue) {
      return this.getList2().stream().filter(module -> module.getClass().equals(classValue)).map(module -> (C)module).findFirst();
   }

   public ModuleManager() {
      this.setList(Collections.synchronizedList(new LinkedList()));
      this.categoryMap = new LinkedHashMap<>();
      this.placementModuleSet = new PlacementModuleSet();
   }

   public void handleModule3(Module module) {
      this.phobosClient.getCommandManager().handleCommand(new ModuleSettingCommand(module));
   }

   public PlacementModuleSet getPlacementModuleSet() {
      return this.placementModuleSet;
   }

   @Override
   public JsonElement createJsonPrimitive() {
      JsonObject jsonObject = new JsonObject();

      for (Module module : this.getList2()) {
         jsonObject.add(module.getName(), module.createJsonPrimitive());
      }

      return jsonObject;
   }

   public List<Module> getList() {
      return this.getList2();
   }

   public void handleCategory(Category category) {
      this.categoryMap.put(category, this.getList2().stream().filter(module -> Objects.equals(module.getCategory(), category)).toList());
   }

   public List<Module> resolveList(UIModule.Order order) {
      LinkedList<Module> linkedList = new LinkedList<>(this.getList2());
      linkedList.sort((module, module2) -> order.equals(UIModule.Order.Default) ? module.computeNamed(module2) : -module.computeNamed(module2));
      return linkedList;
   }

   public PhobosClient getPhobosClient() {
      return this.phobosClient;
   }

   @Override
   public void handleBINDSMODULESEnum(BINDSMODULESEnum bINDSMODULESEnum) {
   }

   public Map<Category, List<Module>> getCategoryMap() {
      return this.categoryMap;
   }

   public Optional<Module> resolveOptional3(String text) {
      for (Module module : this.getList2()) {
         if (module.getName().equalsIgnoreCase(text)) {
            return Optional.of(module);
         }
      }

      return Optional.empty();
   }

   public List<Module> getList2() {
      return this.list;
   }

   public void setList(List<Module> var1) {
      this.list = var1;
   }
}
