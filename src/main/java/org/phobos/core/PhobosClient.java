package org.phobos.core;

import lombok.SneakyThrows;
import java.lang.invoke.StringConcatFactory;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.phobos.api.Raytracer;
import org.phobos.api.BootstrapRemote;
import org.phobos.api.ClientInitializer;
import org.phobos.api.MediaPlayerApi;
import org.phobos.api.ExecutorFactory;
import org.phobos.core.command.CommandManager.CommandLoader;
import org.phobos.core.PhobosLog.Counter;
import org.phobos.core.gui.CursorUtils.Style;
import org.phobos.event.Listener;
import org.phobos.event.RunEvent;
import org.phobos.module.client.IRCModule;
import org.phobos.module.client.NotificationsModule;
import org.phobos.type.API10API11Enum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.animation.AnimationManager;
import org.phobos.core.bind.BindManager;
import org.phobos.core.block.BlockBreakManager;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.bootstrap.BootstrapChallenge;
import org.phobos.core.bootstrap.BootstrapPing;
import org.phobos.core.config.BuildInfo;
import org.phobos.core.chat.ChatManager;
import org.phobos.core.chat.ChatTriggerManager;
import org.phobos.core.loader.ClassLoaderHolder;
import org.phobos.core.config.CloudApiClient;
import org.phobos.core.config.CloudConfigApi;
import org.phobos.core.command.CommandManager;
import org.phobos.core.config.ConfigManager;
import org.phobos.core.combat.DamageCache;
import org.phobos.core.combat.DamageCalculator;
import org.phobos.core.entity.DeadEntityTracker;
import org.phobos.core.task.DelayedTaskManager;
import org.phobos.core.movement.ElytraManager;
import org.phobos.core.social.EnemyList;
import org.phobos.core.entity.EntityFadeManager;
import org.phobos.core.entity.EntityIdManager;
import org.phobos.core.entity.EntityPoseManager;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.event.EventBus;
import org.phobos.core.script.EventClassRegistry;
import org.phobos.core.combat.ExplosionTracker;
import org.phobos.core.social.FriendList;
import org.phobos.core.chunk.HoleChunkScanner;
import org.phobos.core.hud.HudManager;
import org.phobos.core.config.ImportHandler;
import org.phobos.core.player.InteractionManager;
import org.phobos.core.inventory.InventoryManager;
import org.phobos.core.inventory.InventoryUtils;
import org.phobos.core.kit.KitRegistry;
import org.phobos.core.task.LockedExecutor;
import org.phobos.core.user.LoginLog;
import org.phobos.core.bind.MacroManager;
import org.phobos.core.media.MediaPlayerFactory;
import org.phobos.core.system.MemoryManager;
import org.phobos.core.gui.MenuNavigation;
import org.phobos.core.loader.ModsFolderScanner;
import org.phobos.core.module.ModuleManager;
import org.phobos.core.movement.MotionManager;
import org.phobos.core.movement.MovementSimulator;
import org.phobos.core.loader.OptionalModuleLoader;
import org.phobos.core.system.OsDetector;
import org.phobos.core.net.PacketBus;
import org.phobos.core.net.PacketRegistry;
import org.phobos.core.net.PacketSanitizer;
import org.phobos.core.fakeplayer.PlayerStateTracker;
import org.phobos.core.task.PostInitTasks;
import org.phobos.core.proxy.ProxyManager;
import org.phobos.core.render.Render3d;
import org.phobos.core.render.RenderManager;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.rotation.RotationDelay;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.task.Scheduler;
import org.phobos.core.gui.ScreenManager;
import org.phobos.core.script.ScriptManager;
import org.phobos.core.server.ServerAntiCheatDetector;
import org.phobos.core.server.ServerDetector;
import org.phobos.core.user.SessionManager;
import org.phobos.core.render.shader.ShaderEffectManager;
import org.phobos.core.player.SkinManager;
import org.phobos.core.combat.SurroundManager;
import org.phobos.core.task.TaskScheduler;
import org.phobos.core.tick.TickManager;
import org.phobos.core.tick.TimeUtils;
import org.phobos.core.tick.TimerManager;
import org.phobos.core.server.TpsTracker;
import org.phobos.core.entity.TrackerManager;
import org.phobos.core.system.TrayManager;
import org.phobos.core.user.UserProfile;
import org.phobos.core.user.UserSession;
import org.phobos.core.user.UserTagManager;
import org.phobos.core.world.WaypointManager;
import org.phobos.core.world.WorldHolder;

public class PhobosClient extends UnicastRemoteObject implements ExecutorFactory, ClientInitializer {
   public DelayedTaskManager delayedTaskManager;
   public static byte[] byteArray = new byte[0];
   public static final Path path = FabricLoader.getInstance().getGameDir().resolve("phoboslite");
   public SessionManager sessionManager;
   public long longValue;
   public static final API10API11Enum aPI10API11Enum = API10API11Enum.API_1_1;
   public List<Consumer<IRCModule>> consumerList;
   public CloudApiClient cloudApiClient;
   public static volatile boolean flag = true;
   public static boolean flag2 = false;
   public long longValue2;
   public List<Consumer<NotificationsModule>> consumerList2;
   public Logger LOGGER;
   public ExecutorService executorService;
   public BuildInfo buildInfo;
   public Thread thread;
   public LockedExecutor lockedExecutor;
   public static final byte[][] byteArray2 = new byte[1024][32];
   public PacketBus packetBus;
   public ImportHandler importHandler;
   public ConfigManager configManager;
   public FeatureRegistry featureRegistry;
   public BindManager bindManager;
   public FriendList friendList;
   public RotationManager rotationManager;
   public EnemyList enemyList;
   public PacketSanitizer packetSanitizer;
   public DamageCalculator damageCalculator;
   public OptionalModuleLoader optionalModuleLoader;
   public ModuleManager moduleManager;
   public ChatManager chatManager;
   public RenderManager renderManager;
   public ChatTriggerManager chatTriggerManager;
   public ShaderEffectManager shaderEffectManager;
   public DamageCalculator damageCalculator2;
   public MediaPlayerApi mediaPlayerApi;
   public MinecraftClient minecraftClient;
   public CommandManager commandManager;
   public MovementSimulator movementSimulator;
   public EventBus eventBus;
   public MenuNavigation menuNavigation;
   public MotionManager motionManager;
   public TrackerManager trackerManager;
   public AnimationManager animationManager;
   public TimerManager timerManager;
   public SkinManager skinManager;
   public TpsTracker tpsTracker;
   public EntityIdManager entityIdManager;
   public MemoryManager memoryManager;
   public TrayManager trayManager;
   public PhobosSounds phobosSounds;
   public SurroundManager surroundManager;
   public UserTagManager userTagManager;
   public TickManager tickManager;
   public WorldHolder worldHolder;
   public InventoryManager inventoryManager;
   public HudManager hudManager;
   public RotationDelay rotationDelay;
   public ServerDetector serverDetector;
   public TaskScheduler taskScheduler;
   public ServerAntiCheatDetector serverAntiCheatDetector;
   public EntityPoseManager entityPoseManager;
   public ScriptManager scriptManager;
   public Scheduler scheduler;
   public ExplosionTracker explosionTracker;
   public DamageCache damageCache;
   public CloudConfigApi cloudConfigApi;
   public ProxyManager proxyManager;
   public HoleChunkScanner holeChunkScanner;
   public ScreenManager screenManager;
   public BlockBreakManager blockBreakManager;
   public InteractionManager interactionManager;
   public MacroManager macroManager;
   public ElytraManager elytraManager;
   public EntityFadeManager entityFadeManager;
   public DeadEntityTracker deadEntityTracker;
   public PlayerStateTracker playerStateTracker;
   public WaypointManager waypointManager;
   private static Object[] phobos$g1806bcb18;
   private static Object[] phobos$g1806bc620;
   private static Object[] phobos$g1806bc758;
   private static Object[] phobos$g1806bdea0;
   private static Object[] phobos$g1806bf638;
   private static Object[] phobos$g1806bf740;
   private static Object[] phobos$g1806bf920;
   private static Object[] phobos$g1806bc8d0;
   private static Object[] phobos$g1806bc9e8;
   private static Object[] phobos$g1806bd198;

   public void handleText(String text, String text2) {
      Object[] var3 = new Object[1];
      var3[0] = text;
      String var4 = "[Phobos] #ERROR [%s] ".formatted(var3);
      String var12 = var4 + text2;
      PrintStream var13 = System.err;
      var13.println(var12);
      IRCModule var14 = IRCModule.getIRCModule();
      if (var14 == null) {
         List var15 = this.consumerList;
         String cap0 = var12;
         Consumer<IRCModule> var32 = var1 -> handleText8(cap0, var1);
         var15.add(var32);
      } else {
         IRCModule var33 = IRCModule.getIRCModule();
         var33.handleText2(var12);
      }
   }

   public static void handleRunnable(Runnable runnable) {
      ClientHolder.getPhobosClient().getLockedExecutor().handleRunnable(runnable);
   }

   public void run() {
      this.run5();
   }

   public DamageCalculator getDamageCalculator() {
      return this.serverDetector.isFlag2() ? this.damageCalculator2 : this.damageCalculator;
   }

   public void handleText2(Text text) {
      if (NotificationsModule.getNotificationsModule() != null) {
         NotificationsModule.getNotificationsModule().textList.add(text);
      } else {
         this.consumerList2.add(notificationsModule -> notificationsModule.textList.add(text));
      }
   }

   public void handleText3(String text, String text2) {
      Object[] var3 = new Object[1];
      var3[0] = text;
      String var4 = "[Phobos] #ERROR [%s] ".formatted(var3);
      String var12 = var4 + text2;
      IRCModule var13 = IRCModule.getIRCModule();
      if (var13 == null) {
         List var14 = this.consumerList;
         String cap1 = var12;
         Consumer<IRCModule> var31 = var1 -> handleText7(cap1, var1);
         var14.add(var31);
      } else {
         IRCModule var32 = IRCModule.getIRCModule();
         var32.handleText2(var12);
      }
   }

   public void run2() {
      Runnable var18 = this::run7;
      Thread var1 = new Thread(var18);
      this.thread = var1;
      Thread var19 = this.thread;
      var19.setName("NET");
      Thread var20 = this.thread;
      var20.start();
      Thread var21 = this.thread;
      PhobosClient.ExceptionReporter var22 = new PhobosClient.ExceptionReporter();
      var21.setUncaughtExceptionHandler(var22);
   }

   public void run3() {
      SessionManager var1 = this.sessionManager;
      if (var1 != null) {
         PacketBus var2 = this.packetBus;
         if (var2 != null) {
            Render3d.run5();
            ConfigManager.handlePhobosClient(this);
            KitRegistry.run7();
            WaypointManager.handlePhobosClient(this);
            InventoryUtils.run5();
            BlockUtils.run5();
            EntityUtils.run5();
            RenderManager var3 = new RenderManager();
            this.renderManager = var3;
            TimerManager var4 = new TimerManager();
            this.timerManager = var4;
            EventBus var5 = this.eventBus;
            RenderManager var6 = this.renderManager;
            var5.handleObject(var6);
            EventBus var7 = this.eventBus;
            TimerManager var8 = this.timerManager;
            var7.handleObject(var8);
            PacketBus var9 = this.packetBus;
            BootstrapChallenge.handlePacketBus(var9);
            BootstrapPing.run();
            return;
         }
      }

      char[] var11 = new char[35];
      var11[0] = 'S';
      var11[1] = 'e';
      var11[2] = 'c';
      var11[3] = 'u';
      var11[4] = 'r';
      var11[5] = 'i';
      var11[6] = 't';
      var11[7] = 'y';
      var11[8] = ' ';
      var11[9] = 'o';
      var11[10] = 'r';
      var11[11] = ' ';
      var11[12] = 'n';
      var11[13] = 'e';
      var11[14] = 't';
      var11[15] = 'w';
      var11[16] = 'o';
      var11[17] = 'r';
      var11[18] = 'k';
      var11[19] = ' ';
      var11[20] = 'n';
      var11[21] = 'o';
      var11[22] = 't';
      var11[23] = ' ';
      var11[24] = 'i';
      var11[25] = 'n';
      var11[26] = 'i';
      var11[27] = 't';
      var11[28] = 'i';
      var11[29] = 'a';
      var11[30] = 'l';
      var11[31] = 'i';
      var11[32] = 'z';
      var11[33] = 'e';
      var11[34] = 'd';
      String var10 = new String(var11);
      ClientGuard.handleText(var10);
      ClientGuard.stop();
   }

   public static <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier) {
      return CompletableFuture.supplyAsync(supplier, ClientHolder.getPhobosClient().getExecutorService());
   }

   @SneakyThrows public void run4() {
      label50: {
         Registry var1;
         try {
            var1 = LocateRegistry.getRegistry(1099);
         } catch (Throwable var15) {
            boolean var7 = true;
            if (!var7) {
               throw var15;
            }
            break label50;
         }

         char[] var3 = new char[9];
         var3[0] = 'B';
         var3[1] = 'o';
         var3[2] = 'o';
         var3[3] = 't';
         var3[4] = 's';
         var3[5] = 't';
         var3[6] = 'r';
         var3[7] = 'a';
         var3[8] = 'p';

         String var2;
         try {
            var2 = new String(var3);
         } catch (Throwable var14) {
            boolean var16 = true;
            if (!var16) {
               throw var14;
            }
            break label50;
         }

         Remote var4;
         try {
            var4 = var1.lookup(var2);
         } catch (Throwable var13) {
            boolean var17 = true;
            if (!var17) {
               throw var13;
            }
            break label50;
         }

         boolean var5 = var4 instanceof BootstrapRemote;
         if (var4 != null && !var5) {
            throw new ClassCastException("CHECKCAST failed");
         }

         try {
            ((BootstrapRemote)var4).handleClientInitializer(this, 234043877857195293L);
            return;
         } catch (Throwable var12) {
            var12.printStackTrace();
         }
      }

      char[] var9 = new char[9];
      var9[0] = 'B';
      var9[1] = 'o';
      var9[2] = 'o';
      var9[3] = 't';
      var9[4] = 's';
      var9[5] = 't';
      var9[6] = 'r';
      var9[7] = 'a';
      var9[8] = 'p';
      String var8 = new String(var9);
      char[] var11 = new char[14];
      var11[0] = 'F';
      var11[1] = 'a';
      var11[2] = 'i';
      var11[3] = 'l';
      var11[4] = 'e';
      var11[5] = 'd';
      var11[6] = ' ';
      var11[7] = 't';
      var11[8] = 'o';
      var11[9] = ' ';
      var11[10] = 'l';
      var11[11] = 'o';
      var11[12] = 'a';
      var11[13] = 'd';
      String var10 = new String(var11);
      PhobosLog.handleText(var8, var10);
   }

   public void handleText4(String text) {
      MutableText var2 = Text.literal("[Warning] ");
      Color var3 = Color.YELLOW;
      int var4 = var3.hashCode();
      MutableText var5 = var2.withColor(var4);
      MutableText var6 = Text.literal(text);
      Color var7 = Color.WHITE;
      int var8 = var7.hashCode();
      MutableText var9 = var6.withColor(var8);
      MutableText var10 = var5.append(var9);
      NotificationsModule var11 = NotificationsModule.getNotificationsModule();
      List var30;
      Object var31;
      if (var11 == null) {
         List var12 = this.consumerList2;
         Text cap2 = var10;
         Consumer<NotificationsModule> var29 = var1 -> handleText6(cap2, var1);
         var30 = var12;
         var31 = var29;
      } else {
         NotificationsModule var32 = NotificationsModule.getNotificationsModule();
         List var33 = var32.textList;
         var30 = var33;
         var31 = var10;
      }

      var30.add(var31);
   }

   public static void handleRunnable2(Runnable runnable) {
      CompletableFuture.runAsync(runnable, MinecraftClient.getInstance());
   }

   @Override
   @SneakyThrows public void handleLong(long... longs) throws RemoteException {
      CallSite var451 = null;
      boolean var2 = flag;
      if (var2) {
         flag = false & true;
         SessionManager var3 = this.sessionManager;
         if (var3 != null) {
            PacketBus var6 = this.packetBus;
            if (var6 != null) {
               UserSession var7 = UserSession.userSession;
               if (var7 != null) {
                  UserSession var8 = UserSession.userSession;
                  UserProfile var9 = var8.getUserProfile();
                  if (var9 != null) {
                     UserSession var10 = UserSession.userSession;
                     String var11 = var10.getText();
                     if (var11 != null) {
                        UserSession var12 = UserSession.userSession;
                        int var13 = var12.intValue;
                        if (var13 == 2147483607) {
                           int var14 = longs.length;
                           if (var14 == 6) {
                              long var15 = longs[0];
                              if ((var15 != 2224961381767398078L ? ((int)var15 <= (int)2224961381767398078L ? -1 : 1) : 0) == 0) {
                                 long var17 = longs[1];
                                 if ((var17 != 5819110724022498049L ? ((int)var17 <= (int)5819110724022498049L ? -1 : 1) : 0) == 0) {
                                    long var19 = longs[2];
                                    if ((var19 != 4967941764311155865L ? ((int)var19 <= (int)4967941764311155865L ? -1 : 1) : 0) == 0) {
                                       long var21 = longs[3];
                                       if ((var21 != 1639064478920766944L ? ((int)var21 <= (int)1639064478920766944L ? -1 : 1) : 0) == 0) {
                                          long var23 = longs[4];
                                          if ((var23 != -6810938074878119118L ? ((int)var23 <= (int)-6810938074878119118L ? -1 : 1) : 0) == 0) {
                                             long var25 = longs[5];
                                             if ((var25 != 3516185207075139484L ? ((int)var25 <= (int)3516185207075139484L ? -1 : 1) : 0) == 0) {
                                                char[] var28 = new char[13];
                                                var28[0] = 'p';
                                                var28[1] = 'h';
                                                var28[2] = 'o';
                                                var28[3] = 'b';
                                                var28[4] = 'o';
                                                var28[5] = 's';
                                                var28[6] = '.';
                                                var28[7] = 'a';
                                                var28[8] = 'p';
                                                var28[9] = 'i';
                                                var28[10] = '.';
                                                var28[11] = 'v';
                                                var28[12] = '2';
                                                String var27 = new String(var28);
                                                UserSession var29 = UserSession.userSession;
                                                String var30 = var29.getText();
                                                LoginLog.handleText(var27, var30);
                                                flag2 = true & true;
                                                long var31 = TimeUtils.value();
                                                this.longValue2 = var31;
                                                HashMap var33 = new HashMap();
                                                Long var34 = -6275075006882042360L;
                                                Long var35 = 4680592236358506239L;
                                                var33.put(var34, var35);
                                                Long var36 = 6017169145804762652L;
                                                Long var37 = 1050283723746364606L;
                                                var33.put(var36, var37);
                                                Long var38 = -6191281583848588539L;
                                                Long var39 = 6711360178256120310L;
                                                var33.put(var38, var39);
                                                Long var40 = -5266796131258165678L;
                                                Long var41 = -404601722741190750L;
                                                var33.put(var40, var41);
                                                Long var42 = -2962739926462865831L;
                                                Long var43 = 4894894675549189363L;
                                                var33.put(var42, var43);
                                                Long var44 = 8394262447341947759L;
                                                Long var45 = 935578773992851880L;
                                                var33.put(var44, var45);
                                                Long var46 = 7370043181199051858L;
                                                Long var47 = -2802640888925288030L;
                                                var33.put(var46, var47);
                                                Long var48 = -952958456371481110L;
                                                Long var49 = -992904157435677257L;
                                                var33.put(var48, var49);
                                                Long var50 = 2936372300021692977L;
                                                Long var51 = -5016909542103767610L;
                                                var33.put(var50, var51);
                                                Long var52 = 9216043062942571118L;
                                                Long var53 = 3036919563976960010L;
                                                var33.put(var52, var53);
                                                Long var54 = 1741127619999434920L;
                                                Long var55 = 8170339631389721011L;
                                                var33.put(var54, var55);
                                                Long var56 = -791021154762822567L;
                                                Long var57 = -8394101738692664142L;
                                                var33.put(var56, var57);
                                                Long var58 = 748465616347233890L;
                                                Long var59 = -8016712704382783788L;
                                                var33.put(var58, var59);
                                                Long var60 = 3073783040151416895L;
                                                Long var61 = 965244961636943378L;
                                                var33.put(var60, var61);
                                                Long var62 = -783935605298457989L;
                                                Long var63 = 8997672293153515046L;
                                                var33.put(var62, var63);
                                                Long var64 = -2195582990171017626L;
                                                Long var65 = -6709302648956882079L;
                                                var33.put(var64, var65);
                                                Map var66 = var33;
                                                Set var67 = var66.entrySet();
                                                Iterator var68 = var67.iterator();
                                                long var69 = 0L;

                                                while (true) {
                                                   long var71 = var69;
                                                   long var73 = var71;
                                                   long var75 = var73;
                                                   int var77 = (var68.hasNext()) ? 1 : 0;
                                                   long var78 = (long)(var77 & 0xFF & 0xFF) << 40 | var75 & -280375465082881L;
                                                   if ((int)((int)(var78 >>> 40) & 0xFF & 0xFF) == 0) {
                                                      ModsFolderScanner var96 = new ModsFolderScanner();
                                                      long var97 = TimeUtils.value();
                                                      FabricLoader var99 = FabricLoader.getInstance();
                                                      Path var100 = var99.getGameDir();
                                                      char[] var102 = new char[4];
                                                      var102[0] = 'm';
                                                      var102[1] = 'o';
                                                      var102[2] = 'd';
                                                      var102[3] = 's';
                                                      String var101 = new String(var102);
                                                      Path var103 = var100.resolve(var101);
                                                      var96.handlePath(var103);
                                                      char[] var105 = new char[8];
                                                      var105[0] = 'A';
                                                      var105[1] = 'n';
                                                      var105[2] = 'a';
                                                      var105[3] = 'l';
                                                      var105[4] = 'y';
                                                      var105[5] = 's';
                                                      var105[6] = 'i';
                                                      var105[7] = 's';
                                                      String var104 = new String(var105);
                                                      char[] var107 = new char[17];
                                                      var107[0] = 'C';
                                                      var107[1] = 'o';
                                                      var107[2] = 'm';
                                                      var107[3] = 'p';
                                                      var107[4] = 'l';
                                                      var107[5] = 'e';
                                                      var107[6] = 't';
                                                      var107[7] = 'e';
                                                      var107[8] = 'd';
                                                      var107[9] = ' ';
                                                      var107[10] = 'i';
                                                      var107[11] = 'n';
                                                      var107[12] = ' ';
                                                      var107[13] = '%';
                                                      var107[14] = 's';
                                                      var107[15] = 'm';
                                                      var107[16] = 's';
                                                      String var106 = new String(var107);
                                                      Object[] var108 = new Object[1];
                                                      long var109 = TimeUtils.value();
                                                      Long var111 = var109 - var97;
                                                      var108[0] = var111;
                                                      String var112 = var106.formatted(var108);
                                                      PhobosLog.handleText(var104, var112);
                                                      char[] var114 = new char[12];
                                                      var114[0] = 'I';
                                                      var114[1] = 'n';
                                                      var114[2] = 'i';
                                                      var114[3] = 't';
                                                      var114[4] = 'i';
                                                      var114[5] = 'a';
                                                      var114[6] = 'l';
                                                      var114[7] = 'i';
                                                      var114[8] = 'z';
                                                      var114[9] = 'i';
                                                      var114[10] = 'n';
                                                      var114[11] = 'g';
                                                      String var113 = new String(var114);
                                                      Counter var115 = PhobosLog.createCounter(var113, 50);
                                                      DelayedTaskManager var116 = new DelayedTaskManager();
                                                      this.delayedTaskManager = var116;
                                                      var115.run();
                                                      MinecraftClient var118 = this.minecraftClient;
                                                      Window var119 = var118.getWindow();
                                                      long var120 = var119.getHandle();
                                                      BindManager var117 = new BindManager(this, var120);
                                                      this.bindManager = var117;
                                                      var115.run();
                                                      ModuleManager var122 = new ModuleManager();
                                                      this.moduleManager = var122;
                                                      ModuleManager var123 = this.moduleManager;
                                                      var123.handlePhobosClient(this);
                                                      var115.run();
                                                      OptionalModuleLoader var124 = this.optionalModuleLoader;
                                                      ModuleManager var125 = this.moduleManager;
                                                      var124.handlePhobosClient(this, var125);
                                                      var115.run();
                                                      PacketSanitizer var126 = new PacketSanitizer();
                                                      this.packetSanitizer = var126;
                                                      var115.run();
                                                      CommandManager var127 = new CommandManager(this);
                                                      this.commandManager = var127;
                                                      var115.run();
                                                      ChatManager var128 = new ChatManager(this);
                                                      this.chatManager = var128;
                                                      var115.run();
                                                      MovementSimulator var129 = new MovementSimulator();
                                                      this.movementSimulator = var129;
                                                      var115.run();
                                                      RotationManager var130 = new RotationManager();
                                                      this.rotationManager = var130;
                                                      var115.run();
                                                      ShaderEffectManager var131 = new ShaderEffectManager();
                                                      this.shaderEffectManager = var131;
                                                      var115.run();
                                                      FriendList var132 = new FriendList();
                                                      this.friendList = var132;
                                                      var115.run();
                                                      EnemyList var133 = new EnemyList();
                                                      this.enemyList = var133;
                                                      var115.run();
                                                      InventoryManager var134 = new InventoryManager();
                                                      this.inventoryManager = var134;
                                                      var115.run();
                                                      AnimationManager var135 = new AnimationManager();
                                                      this.animationManager = var135;
                                                      var115.run();
                                                      TpsTracker var136 = new TpsTracker();
                                                      this.tpsTracker = var136;
                                                      var115.run();
                                                      MemoryManager var137 = new MemoryManager(this);
                                                      this.memoryManager = var137;
                                                      var115.run();
                                                      TaskScheduler var138 = new TaskScheduler();
                                                      this.taskScheduler = var138;
                                                      var115.run();
                                                      WorldHolder var139 = new WorldHolder();
                                                      this.worldHolder = var139;
                                                      var115.run();
                                                      ServerDetector var140 = new ServerDetector();
                                                      this.serverDetector = var140;
                                                      var115.run();
                                                      EntityIdManager var141 = new EntityIdManager();
                                                      this.entityIdManager = var141;
                                                      var115.run();
                                                      RotationDelay var142 = new RotationDelay();
                                                      this.rotationDelay = var142;
                                                      var115.run();
                                                      SurroundManager var143 = new SurroundManager(this);
                                                      this.surroundManager = var143;
                                                      var115.run();
                                                      TickManager var144 = new TickManager(this);
                                                      this.tickManager = var144;
                                                      var115.run();
                                                      TrackerManager var145 = new TrackerManager();
                                                      this.trackerManager = var145;
                                                      var115.run();
                                                      MotionManager var146 = new MotionManager();
                                                      this.motionManager = var146;
                                                      var115.run();
                                                      HudManager var147 = new HudManager(this);
                                                      this.hudManager = var147;
                                                      var115.run();
                                                      MenuNavigation var148 = new MenuNavigation(this);
                                                      this.menuNavigation = var148;
                                                      var115.run();
                                                      UserTagManager var149 = new UserTagManager(this);
                                                      this.userTagManager = var149;
                                                      var115.run();
                                                      SkinManager var150 = new SkinManager(this);
                                                      this.skinManager = var150;
                                                      var115.run();
                                                      TrayManager var151 = new TrayManager(this);
                                                      this.trayManager = var151;
                                                      var115.run();
                                                      DeadEntityTracker var152 = new DeadEntityTracker();
                                                      this.deadEntityTracker = var152;
                                                      var115.run();
                                                      ScriptManager var153 = new ScriptManager(this);
                                                      this.scriptManager = var153;
                                                      var115.run();
                                                      ConcurrentHashMap var155 = new ConcurrentHashMap();
                                                      ExecutorService var156 = this.executorService;
                                                      HoleChunkScanner var154 = new HoleChunkScanner(var155, var156);
                                                      this.holeChunkScanner = var154;
                                                      var115.run();
                                                      CloudConfigApi var157 = new CloudConfigApi(this);
                                                      this.cloudConfigApi = var157;
                                                      var115.run();
                                                      ServerAntiCheatDetector var158 = new ServerAntiCheatDetector();
                                                      this.serverAntiCheatDetector = var158;
                                                      var115.run();
                                                      EntityFadeManager var159 = new EntityFadeManager();
                                                      this.entityFadeManager = var159;
                                                      var115.run();
                                                      BlockBreakManager var160 = new BlockBreakManager(this);
                                                      this.blockBreakManager = var160;
                                                      var115.run();
                                                      WaypointManager var161 = new WaypointManager();
                                                      this.waypointManager = var161;
                                                      var115.run();
                                                      MacroManager var162 = new MacroManager(this);
                                                      this.macroManager = var162;
                                                      var115.run();
                                                      ProxyManager var163 = new ProxyManager(this);
                                                      this.proxyManager = var163;
                                                      var115.run();
                                                      InteractionManager var164 = new InteractionManager(this);
                                                      this.interactionManager = var164;
                                                      var115.run();
                                                      PhobosSounds var165 = new PhobosSounds();
                                                      this.phobosSounds = var165;
                                                      var115.run();
                                                      Scheduler var166 = new Scheduler();
                                                      this.scheduler = var166;
                                                      var115.run();
                                                      PlayerStateTracker var167 = new PlayerStateTracker(this);
                                                      this.playerStateTracker = var167;
                                                      var115.run();
                                                      ElytraManager var168 = new ElytraManager(this);
                                                      this.elytraManager = var168;
                                                      var115.run();
                                                      EntityPoseManager var169 = new EntityPoseManager();
                                                      this.entityPoseManager = var169;
                                                      var115.run();
                                                      ScreenManager var170 = new ScreenManager();
                                                      this.screenManager = var170;
                                                      var115.run();
                                                      DamageCache var171 = new DamageCache(this);
                                                      this.damageCache = var171;
                                                      var115.run();
                                                      ExplosionTracker var172 = new ExplosionTracker(this);
                                                      this.explosionTracker = var172;
                                                      var115.run();
                                                      FeatureRegistry var173 = new FeatureRegistry(this);
                                                      this.featureRegistry = var173;
                                                      var115.run2();
                                                      ConfigManager var174 = new ConfigManager(this);
                                                      this.configManager = var174;
                                                      ImportHandler var175 = this.importHandler;
                                                      var175.run();
                                                      EventBus var176 = this.eventBus;
                                                      ChatTriggerManager var177 = this.chatTriggerManager;
                                                      var176.handleObject(var177);
                                                      EventBus var178 = this.eventBus;
                                                      DelayedTaskManager var179 = this.delayedTaskManager;
                                                      var178.handleObject(var179);
                                                      EventBus var180 = this.eventBus;
                                                      ModuleManager var181 = this.moduleManager;
                                                      var180.handleObject(var181);
                                                      EventBus var182 = this.eventBus;
                                                      PacketSanitizer var183 = this.packetSanitizer;
                                                      var182.handleObject(var183);
                                                      EventBus var184 = this.eventBus;
                                                      BindManager var185 = this.bindManager;
                                                      var184.handleObject(var185);
                                                      EventBus var186 = this.eventBus;
                                                      ConfigManager var187 = this.configManager;
                                                      var186.handleObject(var187);
                                                      EventBus var188 = this.eventBus;
                                                      CommandManager var189 = this.commandManager;
                                                      var188.handleObject(var189);
                                                      EventBus var190 = this.eventBus;
                                                      ChatManager var191 = this.chatManager;
                                                      var190.handleObject(var191);
                                                      EventBus var192 = this.eventBus;
                                                      MovementSimulator var193 = this.movementSimulator;
                                                      var192.handleObject(var193);
                                                      EventBus var194 = this.eventBus;
                                                      RotationManager var195 = this.rotationManager;
                                                      var194.handleObject(var195);
                                                      EventBus var196 = this.eventBus;
                                                      ShaderEffectManager var197 = this.shaderEffectManager;
                                                      var196.handleObject(var197);
                                                      EventBus var198 = this.eventBus;
                                                      InventoryManager var199 = this.inventoryManager;
                                                      var198.handleObject(var199);
                                                      EventBus var200 = this.eventBus;
                                                      AnimationManager var201 = this.animationManager;
                                                      var200.handleObject(var201);
                                                      EventBus var202 = this.eventBus;
                                                      TpsTracker var203 = this.tpsTracker;
                                                      var202.handleObject(var203);
                                                      EventBus var204 = this.eventBus;
                                                      MemoryManager var205 = this.memoryManager;
                                                      var204.handleObject(var205);
                                                      EventBus var206 = this.eventBus;
                                                      TaskScheduler var207 = this.taskScheduler;
                                                      var206.handleObject(var207);
                                                      EventBus var208 = this.eventBus;
                                                      WorldHolder var209 = this.worldHolder;
                                                      var208.handleObject(var209);
                                                      EventBus var210 = this.eventBus;
                                                      ServerDetector var211 = this.serverDetector;
                                                      var210.handleObject(var211);
                                                      EventBus var212 = this.eventBus;
                                                      EntityIdManager var213 = this.entityIdManager;
                                                      var212.handleObject(var213);
                                                      EventBus var214 = this.eventBus;
                                                      RotationDelay var215 = this.rotationDelay;
                                                      var214.handleObject(var215);
                                                      EventBus var216 = this.eventBus;
                                                      SurroundManager var217 = this.surroundManager;
                                                      var216.handleObject(var217);
                                                      EventBus var218 = this.eventBus;
                                                      TickManager var219 = this.tickManager;
                                                      var218.handleObject(var219);
                                                      EventBus var220 = this.eventBus;
                                                      TrackerManager var221 = this.trackerManager;
                                                      var220.handleObject(var221);
                                                      EventBus var222 = this.eventBus;
                                                      MotionManager var223 = this.motionManager;
                                                      var222.handleObject(var223);
                                                      EventBus var224 = this.eventBus;
                                                      HudManager var225 = this.hudManager;
                                                      var224.handleObject(var225);
                                                      EventBus var226 = this.eventBus;
                                                      MenuNavigation var227 = this.menuNavigation;
                                                      var226.handleObject(var227);
                                                      EventBus var228 = this.eventBus;
                                                      UserTagManager var229 = this.userTagManager;
                                                      var228.handleObject(var229);
                                                      EventBus var230 = this.eventBus;
                                                      SkinManager var231 = this.skinManager;
                                                      var230.handleObject(var231);
                                                      EventBus var232 = this.eventBus;
                                                      TrayManager var233 = this.trayManager;
                                                      var232.handleObject(var233);
                                                      EventBus var234 = this.eventBus;
                                                      DeadEntityTracker var235 = this.deadEntityTracker;
                                                      var234.handleObject(var235);
                                                      EventBus var236 = this.eventBus;
                                                      ScriptManager var237 = this.scriptManager;
                                                      var236.handleObject(var237);
                                                      EventBus var238 = this.eventBus;
                                                      HoleChunkScanner var239 = this.holeChunkScanner;
                                                      var238.handleObject(var239);
                                                      EventBus var240 = this.eventBus;
                                                      CloudConfigApi var241 = this.cloudConfigApi;
                                                      var240.handleObject(var241);
                                                      EventBus var242 = this.eventBus;
                                                      ServerAntiCheatDetector var243 = this.serverAntiCheatDetector;
                                                      var242.handleObject(var243);
                                                      EventBus var244 = this.eventBus;
                                                      EntityFadeManager var245 = this.entityFadeManager;
                                                      var244.handleObject(var245);
                                                      EventBus var246 = this.eventBus;
                                                      BlockBreakManager var247 = this.blockBreakManager;
                                                      var246.handleObject(var247);
                                                      EventBus var248 = this.eventBus;
                                                      WaypointManager var249 = this.waypointManager;
                                                      var248.handleObject(var249);
                                                      EventBus var250 = this.eventBus;
                                                      ProxyManager var251 = this.proxyManager;
                                                      var250.handleObject(var251);
                                                      EventBus var252 = this.eventBus;
                                                      InteractionManager var253 = this.interactionManager;
                                                      var252.handleObject(var253);
                                                      EventBus var254 = this.eventBus;
                                                      Scheduler var255 = this.scheduler;
                                                      var254.handleObject(var255);
                                                      EventBus var256 = this.eventBus;
                                                      PlayerStateTracker var257 = this.playerStateTracker;
                                                      var256.handleObject(var257);
                                                      EventBus var258 = this.eventBus;
                                                      ElytraManager var259 = this.elytraManager;
                                                      var258.handleObject(var259);
                                                      EventBus var260 = this.eventBus;
                                                      EntityPoseManager var261 = this.entityPoseManager;
                                                      var260.handleObject(var261);
                                                      EventBus var262 = this.eventBus;
                                                      ScreenManager var263 = this.screenManager;
                                                      var262.handleObject(var263);
                                                      EventBus var264 = this.eventBus;
                                                      DamageCache var265 = this.damageCache;
                                                      var264.handleObject(var265);
                                                      EventBus var266 = this.eventBus;
                                                      ExplosionTracker var267 = this.explosionTracker;
                                                      var266.handleObject(var267);
                                                      EventBus var268 = this.eventBus;
                                                      CommandLoader var269 = CommandLoader.getINSTANCE();
                                                      var268.handleObject(var269);
                                                      FeatureRegistry var270 = this.featureRegistry;
                                                      EventBus var271 = this.eventBus;
                                                      var270.handleEventBus(var271);
                                                      ConfigManager var272 = this.configManager;
                                                      var272.run3();
                                                      ProxyManager var273 = this.proxyManager;
                                                      var273.run3();
                                                      OsDetector var274 = OsDetector.getINSTANCE();
                                                      int var275 = (var274.check()) ? 1 : 0;
                                                      if ((int)((int)((long)(var275 & 0xFF & 0xFF) << 8 >>> 8) & 0xFF & 0xFF) != 0) {
                                                         MediaPlayerApi var364 = MediaPlayerFactory.createSpotifyMediaPlayer();
                                                         this.mediaPlayerApi = var364;
                                                      }

                                                      EventBus var276 = this.eventBus;
                                                      PhobosClient.InitEvent var277 = new PhobosClient.InitEvent(this);
                                                      var276.handleObject2(var277);
                                                      Runtime var278 = Runtime.getRuntime();
                                                      Runnable var296 = this::run8;
                                                      Thread var279 = new Thread(var296);
                                                      var278.addShutdownHook(var279);
                                                      UserSession var297 = UserSession.userSession;
                                                      Runnable var314 = this::run10;
                                                      var297.handleRunnable(var314);
                                                      OsDetector var315 = OsDetector.getINSTANCE();
                                                      boolean var316 = var315.check();
                                                      if (var316) {
                                                         MediaPlayerApi var365 = this.mediaPlayerApi;
                                                         var365.getMediaPlayerApi();
                                                      }

                                                      long var317 = TimeUtils.value();
                                                      long var319 = this.longValue2;
                                                      this.longValue = var317 - var319;
                                                      char[] var322 = new char[4];
                                                      var322[0] = 'I';
                                                      var322[1] = 'n';
                                                      var322[2] = 'i';
                                                      var322[3] = 't';
                                                      String var321 = new String(var322);
                                                      char[] var324 = new char[25];
                                                      var324[0] = 'L';
                                                      var324[1] = 'o';
                                                      var324[2] = 'a';
                                                      var324[3] = 'd';
                                                      var324[4] = 'e';
                                                      var324[5] = 'd';
                                                      var324[6] = ' ';
                                                      var324[7] = 't';
                                                      var324[8] = 'h';
                                                      var324[9] = 'e';
                                                      var324[10] = ' ';
                                                      var324[11] = 'c';
                                                      var324[12] = 'l';
                                                      var324[13] = 'i';
                                                      var324[14] = 'e';
                                                      var324[15] = 'n';
                                                      var324[16] = 't';
                                                      var324[17] = ' ';
                                                      var324[18] = 'i';
                                                      var324[19] = 'n';
                                                      var324[20] = ' ';
                                                      var324[21] = '%';
                                                      var324[22] = 's';
                                                      var324[23] = 'm';
                                                      var324[24] = 's';
                                                      String var323 = new String(var324);
                                                      Object[] var325 = new Object[1];
                                                      long var326 = this.longValue;
                                                      Long var328 = var326;
                                                      var325[0] = var328;
                                                      String var329 = var323.formatted(var325);
                                                      PhobosLog.handleText(var321, var329);
                                                      EventBus var330 = this.eventBus;
                                                      Runnable var348 = PhobosClient::run9;
                                                      PhobosClient.Shutdown var331 = new PhobosClient.Shutdown(var348);
                                                      var330.handleObject(var331);
                                                      PostInitTasks var349 = PostInitTasks.getINSTANCE();
                                                      var349.run();
                                                      return;
                                                   }

                                                   long var80 = var78;
                                                   long var350 = var80;
                                                   long var352 = var350;
                                                   long var354 = var352;
                                                   Object var356 = var68.next();
                                                   long var357 = var354;
                                                   boolean var359 = var356 instanceof Entry;
                                                   if (var356 != null && !var359) {
                                                      throw new ClassCastException("CHECKCAST failed");
                                                   }

                                                   long var362 = var357;
                                                   long var368 = var362;
                                                   long var370 = var368;
                                                   long var372 = var370;
                                                   Object var374 = ((Entry)var356).getKey();
                                                   long var375 = var372;
                                                   long var377 = var375;
                                                   long var379 = var377;
                                                   long var381 = var379;
                                                   Object var383 = var33.get(var374);
                                                   long var384 = var381;
                                                   boolean var386 = var383 instanceof Long;
                                                   if (var383 != null && !var386) {
                                                      throw new ClassCastException("CHECKCAST failed");
                                                   }

                                                   long var389 = var384;
                                                   long var393 = var389;
                                                   long var395 = var393;
                                                   long var397 = var395;
                                                   long var399 = (Long)var383;
                                                   long var401 = var397;
                                                   long var403 = var401;
                                                   long var405 = var403;
                                                   long var407 = var405;
                                                   Object var409 = ((Entry)var356).getValue();
                                                   long var410 = var407;
                                                   boolean var412 = var409 instanceof Long;
                                                   if (var409 != null && !var412) {
                                                      throw new ClassCastException("CHECKCAST failed");
                                                   }

                                                   long var415 = var410;
                                                   long var419 = var415;
                                                   long var421 = var419;
                                                   long var423 = var421;
                                                   long var425 = (Long)var409;
                                                   long var427 = var423;
                                                   long var433;
                                                   if ((var399 != var425 ? ((int)var399 <= (int)var425 ? -1 : 1) : 0) == 0) {
                                                      long var429 = var427;
                                                      var433 = var429;
                                                   } else {
                                                      long var431 = var427;
                                                      long var435 = var431;
                                                      long var437 = var435;
                                                      long var439 = var437;
                                                      Object var441 = ((Entry)var356).getValue();
                                                      long var442 = var439;
                                                      long var444 = var442;
                                                      long var446 = var444;
                                                      String var448 = String.valueOf(var441);
                                                      long var449 = var446;
                                                      long var454;
                                                      if (var451 == null) {
                                                         long var452 = var449;
                                                         long var456 = var452;
                                                         long var458 = var456;
                                                         long var460 = var458;
                                                         long var462 = var460;
                                                         long var464 = var462;
                                                         long var466 = var464;
                                                         long var468 = var466;
                                                         long var470 = var468;
                                                         Lookup var472 = MethodHandles.lookup();
                                                         long var473 = var470;
                                                         long var475 = var473;
                                                         long var477 = var475;
                                                         long var479 = var477;
                                                         long var481 = var479;
                                                         long var483 = var481;
                                                         long var485 = var483;
                                                         Class<?> var487 = this.getClass();
                                                         ClassLoader var488 = var487.getClassLoader();
                                                         MethodType var489 = MethodType.fromMethodDescriptorString(
                                                            "(Ljava/lang/String;)Ljava/lang/String;", var488
                                                         );
                                                         long var490 = var485;
                                                         long var492 = var490;
                                                         long var494 = var492;
                                                         long var496 = var494;
                                                         long var498 = var496;
                                                         Object[] var500 = new Object[0];
                                                         long var501 = var498;
                                                         long var503 = var501;
                                                         var451 = StringConcatFactory.makeConcatWithConstants(var472, "makeConcatWithConstants", var489, "Invalid access key: \u0001", var500);
                                                         long var505 = var503;
                                                         long var507 = var505;
                                                         long var509 = var507;
                                                         var454 = var509;
                                                      } else {
                                                         var454 = var449;
                                                      }

                                                      long var511 = var454;
                                                      long var513 = var511;
                                                      long var515 = var513;
                                                      long var517 = var515;
                                                      long var519 = var517;
                                                      MethodHandle var521 = var451.getTarget();
                                                      long var522 = var519;
                                                      long var524 = var522;
                                                      long var526 = var524;
                                                      long var528 = var526;
                                                      long var530 = var528;
                                                      long var532 = var530;
                                                      long var534 = var532;
                                                      long var536 = var534;
                                                      long var538 = var536;
                                                      long var540 = var538;
                                                      long var544;
                                                      if (var448 != null) {
                                                         long var542 = var540;
                                                         Object[] var10000 = phobos$g1806bdea0;
                                                         if (phobos$g1806bdea0 == null) {
                                                            var10000 = phobos$g1806bdea0 = new Object[1];
                                                         }

                                                         var10000[0] = var448;
                                                         var544 = var542;
                                                      } else {
                                                         var544 = var540;
                                                      }

                                                      Object[] var10001 = phobos$g1806bdea0;
                                                      if (phobos$g1806bdea0 == null) {
                                                         var10001 = phobos$g1806bdea0 = new Object[1];
                                                      }

                                                      Object var546 = var521.invokeWithArguments(var10001);
                                                      long var547 = var544;
                                                      long var549 = var547;
                                                      long var551 = var549;
                                                      long var553 = var551;
                                                      ClientGuard.handleText((String)var546);
                                                      long var555 = var553;
                                                      long var557 = var555;
                                                      long var559 = var557;
                                                      ClientGuard.stop();
                                                      var433 = var559;
                                                   }

                                                   var69 = var433;
                                                }
                                             } else {
                                                char[] var95 = new char[30];
                                                var95[0] = 'I';
                                                var95[1] = 'n';
                                                var95[2] = 'v';
                                                var95[3] = 'a';
                                                var95[4] = 'l';
                                                var95[5] = 'i';
                                                var95[6] = 'd';
                                                var95[7] = ' ';
                                                var95[8] = 'i';
                                                var95[9] = 'n';
                                                var95[10] = 'i';
                                                var95[11] = 't';
                                                var95[12] = 'i';
                                                var95[13] = 'a';
                                                var95[14] = 'l';
                                                var95[15] = 'i';
                                                var95[16] = 'z';
                                                var95[17] = 'a';
                                                var95[18] = 't';
                                                var95[19] = 'i';
                                                var95[20] = 'o';
                                                var95[21] = 'n';
                                                var95[22] = ' ';
                                                var95[23] = 'k';
                                                var95[24] = 'e';
                                                var95[25] = 'y';
                                                var95[26] = 's';
                                                var95[27] = ':';
                                                var95[28] = ' ';
                                                var95[29] = '6';
                                                String var94 = new String(var95);
                                                ClientGuard.handleText(var94);
                                                ClientGuard.stop();
                                             }
                                          } else {
                                             char[] var93 = new char[30];
                                             var93[0] = 'I';
                                             var93[1] = 'n';
                                             var93[2] = 'v';
                                             var93[3] = 'a';
                                             var93[4] = 'l';
                                             var93[5] = 'i';
                                             var93[6] = 'd';
                                             var93[7] = ' ';
                                             var93[8] = 'i';
                                             var93[9] = 'n';
                                             var93[10] = 'i';
                                             var93[11] = 't';
                                             var93[12] = 'i';
                                             var93[13] = 'a';
                                             var93[14] = 'l';
                                             var93[15] = 'i';
                                             var93[16] = 'z';
                                             var93[17] = 'a';
                                             var93[18] = 't';
                                             var93[19] = 'i';
                                             var93[20] = 'o';
                                             var93[21] = 'n';
                                             var93[22] = ' ';
                                             var93[23] = 'k';
                                             var93[24] = 'e';
                                             var93[25] = 'y';
                                             var93[26] = 's';
                                             var93[27] = ':';
                                             var93[28] = ' ';
                                             var93[29] = '5';
                                             String var92 = new String(var93);
                                             ClientGuard.handleText(var92);
                                             ClientGuard.stop();
                                          }

                                          return;
                                       } else {
                                          char[] var91 = new char[30];
                                          var91[0] = 'I';
                                          var91[1] = 'n';
                                          var91[2] = 'v';
                                          var91[3] = 'a';
                                          var91[4] = 'l';
                                          var91[5] = 'i';
                                          var91[6] = 'd';
                                          var91[7] = ' ';
                                          var91[8] = 'i';
                                          var91[9] = 'n';
                                          var91[10] = 'i';
                                          var91[11] = 't';
                                          var91[12] = 'i';
                                          var91[13] = 'a';
                                          var91[14] = 'l';
                                          var91[15] = 'i';
                                          var91[16] = 'z';
                                          var91[17] = 'a';
                                          var91[18] = 't';
                                          var91[19] = 'i';
                                          var91[20] = 'o';
                                          var91[21] = 'n';
                                          var91[22] = ' ';
                                          var91[23] = 'k';
                                          var91[24] = 'e';
                                          var91[25] = 'y';
                                          var91[26] = 's';
                                          var91[27] = ':';
                                          var91[28] = ' ';
                                          var91[29] = '4';
                                          String var90 = new String(var91);
                                          ClientGuard.handleText(var90);
                                          ClientGuard.stop();
                                          return;
                                       }
                                    } else {
                                       char[] var89 = new char[30];
                                       var89[0] = 'I';
                                       var89[1] = 'n';
                                       var89[2] = 'v';
                                       var89[3] = 'a';
                                       var89[4] = 'l';
                                       var89[5] = 'i';
                                       var89[6] = 'd';
                                       var89[7] = ' ';
                                       var89[8] = 'i';
                                       var89[9] = 'n';
                                       var89[10] = 'i';
                                       var89[11] = 't';
                                       var89[12] = 'i';
                                       var89[13] = 'a';
                                       var89[14] = 'l';
                                       var89[15] = 'i';
                                       var89[16] = 'z';
                                       var89[17] = 'a';
                                       var89[18] = 't';
                                       var89[19] = 'i';
                                       var89[20] = 'o';
                                       var89[21] = 'n';
                                       var89[22] = ' ';
                                       var89[23] = 'k';
                                       var89[24] = 'e';
                                       var89[25] = 'y';
                                       var89[26] = 's';
                                       var89[27] = ':';
                                       var89[28] = ' ';
                                       var89[29] = '3';
                                       String var88 = new String(var89);
                                       ClientGuard.handleText(var88);
                                       ClientGuard.stop();
                                       return;
                                    }
                                 } else {
                                    char[] var87 = new char[30];
                                    var87[0] = 'I';
                                    var87[1] = 'n';
                                    var87[2] = 'v';
                                    var87[3] = 'a';
                                    var87[4] = 'l';
                                    var87[5] = 'i';
                                    var87[6] = 'd';
                                    var87[7] = ' ';
                                    var87[8] = 'i';
                                    var87[9] = 'n';
                                    var87[10] = 'i';
                                    var87[11] = 't';
                                    var87[12] = 'i';
                                    var87[13] = 'a';
                                    var87[14] = 'l';
                                    var87[15] = 'i';
                                    var87[16] = 'z';
                                    var87[17] = 'a';
                                    var87[18] = 't';
                                    var87[19] = 'i';
                                    var87[20] = 'o';
                                    var87[21] = 'n';
                                    var87[22] = ' ';
                                    var87[23] = 'k';
                                    var87[24] = 'e';
                                    var87[25] = 'y';
                                    var87[26] = 's';
                                    var87[27] = ':';
                                    var87[28] = ' ';
                                    var87[29] = '2';
                                    String var86 = new String(var87);
                                    ClientGuard.handleText(var86);
                                    ClientGuard.stop();
                                    return;
                                 }
                              } else {
                                 char[] var85 = new char[30];
                                 var85[0] = 'I';
                                 var85[1] = 'n';
                                 var85[2] = 'v';
                                 var85[3] = 'a';
                                 var85[4] = 'l';
                                 var85[5] = 'i';
                                 var85[6] = 'd';
                                 var85[7] = ' ';
                                 var85[8] = 'i';
                                 var85[9] = 'n';
                                 var85[10] = 'i';
                                 var85[11] = 't';
                                 var85[12] = 'i';
                                 var85[13] = 'a';
                                 var85[14] = 'l';
                                 var85[15] = 'i';
                                 var85[16] = 'z';
                                 var85[17] = 'a';
                                 var85[18] = 't';
                                 var85[19] = 'i';
                                 var85[20] = 'o';
                                 var85[21] = 'n';
                                 var85[22] = ' ';
                                 var85[23] = 'k';
                                 var85[24] = 'e';
                                 var85[25] = 'y';
                                 var85[26] = 's';
                                 var85[27] = ':';
                                 var85[28] = ' ';
                                 var85[29] = '1';
                                 String var84 = new String(var85);
                                 ClientGuard.handleText(var84);
                                 ClientGuard.stop();
                                 return;
                              }
                           } else {
                              char[] var83 = new char[34];
                              var83[0] = 'I';
                              var83[1] = 'n';
                              var83[2] = 'v';
                              var83[3] = 'a';
                              var83[4] = 'l';
                              var83[5] = 'i';
                              var83[6] = 'd';
                              var83[7] = ' ';
                              var83[8] = 'i';
                              var83[9] = 'n';
                              var83[10] = 'i';
                              var83[11] = 't';
                              var83[12] = 'i';
                              var83[13] = 'a';
                              var83[14] = 'l';
                              var83[15] = 'i';
                              var83[16] = 'z';
                              var83[17] = 'a';
                              var83[18] = 't';
                              var83[19] = 'i';
                              var83[20] = 'o';
                              var83[21] = 'n';
                              var83[22] = ' ';
                              var83[23] = 'k';
                              var83[24] = 'e';
                              var83[25] = 'y';
                              var83[26] = 's';
                              var83[27] = ' ';
                              var83[28] = 'l';
                              var83[29] = 'e';
                              var83[30] = 'n';
                              var83[31] = 'g';
                              var83[32] = 't';
                              var83[33] = 'h';
                              String var82 = new String(var83);
                              ClientGuard.handleText(var82);
                              ClientGuard.stop();
                              return;
                           }
                        }
                     }
                  }
               }
            }
         }

         char[] var5 = new char[18];
         var5[0] = 'W';
         var5[1] = 'r';
         var5[2] = 'o';
         var5[3] = 'n';
         var5[4] = 'g';
         var5[5] = ' ';
         var5[6] = 'i';
         var5[7] = 'n';
         var5[8] = 'i';
         var5[9] = 't';
         var5[10] = 'i';
         var5[11] = 'a';
         var5[12] = 'l';
         var5[13] = 'i';
         var5[14] = 'z';
         var5[15] = 'i';
         var5[16] = 'n';
         var5[17] = 'g';
         String var4 = new String(var5);
         ClientGuard.handleText(var4);
         ClientGuard.stop();
      }
   }

   public void handleText5(String text, String text2) {
      System.out.println("[Phobos] #INFO [%s] ".formatted(text) + text2);
   }

   @SneakyThrows public void run5() {
      SessionManager var1 = this.sessionManager;
      if (var1 != null) {
         PhobosSounds var2 = new PhobosSounds();
         PhobosSounds var3 = var2.getPhobosSounds();
         this.phobosSounds = var3;
         ExecutorService var4 = this.getExecutorService2();
         this.executorService = var4;
         Path var5 = path;
         File var6 = var5.toFile();
         var6.mkdirs();
         PacketRegistry.run();
         EventClassRegistry var7 = EventClassRegistry.INSTANCE;
         var7.run();
         ClassLoaderHolder.run();
         OptionalModuleLoader var8 = this.optionalModuleLoader;
         var8.run();
         char[] var10 = new char[9];
         var10[0] = 'u';
         var10[1] = 's';
         var10[2] = 'e';
         var10[3] = 'r';
         var10[4] = '.';
         var10[5] = 'h';
         var10[6] = 'o';
         var10[7] = 'm';
         var10[8] = 'e';
         String var9 = new String(var10);
         String var11 = System.getProperty(var9);
         char[] var14 = new char[9];
         var14[0] = 'e';
         var14[1] = 'a';
         var14[2] = 'r';
         var14[3] = 't';
         var14[4] = 'h';
         var14[5] = 'h';
         var14[6] = 'a';
         var14[7] = 'c';
         var14[8] = 'k';
         String var13 = new String(var14);
         File var12 = new File(var11, var13);
         boolean var15 = var12.exists();
         if (!var15) {
            var12.mkdir();
         }

         char[] var18 = new char[18];
         var18[0] = 'e';
         var18[1] = 'a';
         var18[2] = 'r';
         var18[3] = 't';
         var18[4] = 'h';
         var18[5] = 'h';
         var18[6] = 'a';
         var18[7] = 'c';
         var18[8] = 'k';
         var18[9] = '/';
         var18[10] = 'i';
         var18[11] = 'c';
         var18[12] = 'o';
         var18[13] = 'n';
         var18[14] = '.';
         var18[15] = 'p';
         var18[16] = 'n';
         var18[17] = 'g';
         String var17 = new String(var18);
         File var16 = new File(var11, var17);
         boolean var19 = var16.exists();
         label107:
         if (!var19) {
            char[] var22 = new char[31];
            var22[0] = 'h';
            var22[1] = 't';
            var22[2] = 't';
            var22[3] = 'p';
            var22[4] = 's';
            var22[5] = ':';
            var22[6] = '/';
            var22[7] = '/';
            var22[8] = 'i';
            var22[9] = '.';
            var22[10] = 'i';
            var22[11] = 'm';
            var22[12] = 'g';
            var22[13] = 'u';
            var22[14] = 'r';
            var22[15] = '.';
            var22[16] = 'c';
            var22[17] = 'o';
            var22[18] = 'm';
            var22[19] = '/';
            var22[20] = 'U';
            var22[21] = 'k';
            var22[22] = '3';
            var22[23] = 'T';
            var22[24] = 'o';
            var22[25] = 'd';
            var22[26] = 'i';
            var22[27] = '.';
            var22[28] = 'p';
            var22[29] = 'n';
            var22[30] = 'g';

            label122: {
               Throwable var36;
               label114: {
                  String var21;
                  try {
                     var21 = new String(var22);
                  } catch (Throwable var48) {
                     var36 = var48;
                     boolean var37 = var48 instanceof IOException;
                     if (var37) {
                        break label107;
                     }
                     break label114;
                  }

                  URL var20;
                  try {
                     var20 = new URL(var21);
                  } catch (Throwable var47) {
                     var36 = var47;
                     boolean var52 = var47 instanceof IOException;
                     if (var52) {
                        break label107;
                     }
                     break label114;
                  }

                  InputStream var23;
                  try {
                     var23 = var20.openStream();
                  } catch (Throwable var46) {
                     var36 = var46;
                     boolean var53 = var46 instanceof IOException;
                     if (var53) {
                        break label107;
                     }
                     break label114;
                  }

                  label123: {
                     Throwable var39;
                     label117: {
                        label116: {
                           Path var24;
                           try {
                              var24 = var16.toPath();
                           } catch (Throwable var51) {
                              var39 = var51;
                              boolean var40 = true;
                              if (!var40) {
                                 break label117;
                              }
                              break label116;
                           }

                           CopyOption[] var25 = new CopyOption[0];

                           try {
                              Files.copy(var23, var24, var25);
                              break label123;
                           } catch (Throwable var50) {
                              var39 = var50;
                              boolean var57 = true;
                              if (!var57) {
                                 break label117;
                              }
                           }
                        }

                        if (var23 != null) {
                           try {
                              var23.close();
                           } catch (Throwable var49) {
                              Throwable var41 = var49;
                              boolean var42 = true;
                              if (!var42) {
                                 throw var49;
                              }

                              try {
                                 var39.addSuppressed(var41);
                              } catch (Throwable var45) {
                                 var36 = var45;
                                 boolean var54 = var45 instanceof IOException;
                                 if (var54) {
                                    break label107;
                                 }
                                 break label114;
                              }
                           }
                        }

                        try {
                           throw var39;
                        } catch (Throwable var43) {
                           var36 = var43;
                           boolean var55 = var43 instanceof IOException;
                           if (var55) {
                              break label107;
                           }
                           break label114;
                        }
                     }

                     Throwable var38 = var39;
                     throw var38;
                  }

                  if (var23 == null) {
                     break label122;
                  }

                  try {
                     var23.close();
                     break label122;
                  } catch (Throwable var44) {
                     var36 = var44;
                     boolean var56 = var44 instanceof IOException;
                     if (var56) {
                        break label107;
                     }
                  }
               }

               Throwable var38 = var36;
               throw var38;
            }

            FabricLoader var26 = FabricLoader.getInstance();
            char[] var28 = new char[6];
            var28[0] = 's';
            var28[1] = 'o';
            var28[2] = 'd';
            var28[3] = 'i';
            var28[4] = 'u';
            var28[5] = 'm';
            String var27 = new String(var28);
            boolean var29 = var26.isModLoaded(var27);
            ClientHolder.flag2 = var29;
            RenderUtils.run2();
            this.run2();
            return;
         }

         FabricLoader var32 = FabricLoader.getInstance();
         char[] var34 = new char[6];
         var34[0] = 's';
         var34[1] = 'o';
         var34[2] = 'd';
         var34[3] = 'i';
         var34[4] = 'u';
         var34[5] = 'm';
         String var33 = new String(var34);
         boolean var35 = var32.isModLoaded(var33);
         ClientHolder.flag2 = var35;
         RenderUtils.run2();
         this.run2();
      } else {
         char[] var31 = new char[24];
         var31[0] = 'S';
         var31[1] = 'e';
         var31[2] = 'c';
         var31[3] = 'u';
         var31[4] = 'r';
         var31[5] = 'i';
         var31[6] = 't';
         var31[7] = 'y';
         var31[8] = ' ';
         var31[9] = 'n';
         var31[10] = 'o';
         var31[11] = 't';
         var31[12] = ' ';
         var31[13] = 'i';
         var31[14] = 'n';
         var31[15] = 'i';
         var31[16] = 't';
         var31[17] = 'i';
         var31[18] = 'a';
         var31[19] = 'l';
         var31[20] = 'i';
         var31[21] = 'z';
         var31[22] = 'e';
         var31[23] = 'd';
         String var30 = new String(var31);
         ClientGuard.handleText(var30);
         ClientGuard.stop();
      }
   }

   public static <T> CompletableFuture<T> resolveCompletableFuture(Supplier<T> supplier) {
      return CompletableFuture.supplyAsync(supplier);
   }

   public PhobosClient() throws RemoteException {
      this.nativeInit();
   }

   public void nativeInit() throws RemoteException {
      Logger var1 = LoggerFactory.getLogger("Phobos");
      this.LOGGER = var1;
      ArrayList var2 = new ArrayList();
      this.consumerList2 = var2;
      ArrayList var3 = new ArrayList();
      this.consumerList = var3;
      BuildInfo var4 = BuildInfo.getINSTANCE();
      this.buildInfo = var4;
      CloudApiClient var5 = new CloudApiClient();
      this.cloudApiClient = var5;
      ImportHandler var6 = new ImportHandler(this);
      this.importHandler = var6;
      EventBus var7 = new EventBus();
      this.eventBus = var7;
      MinecraftClient var8 = MinecraftClient.getInstance();
      this.minecraftClient = var8;
      OptionalModuleLoader var9 = new OptionalModuleLoader();
      this.optionalModuleLoader = var9;
      PacketBus var10 = new PacketBus();
      this.packetBus = var10;
      SessionManager var12 = new SessionManager(this);
      this.sessionManager = var12;
      LockedExecutor var13 = new LockedExecutor();
      this.lockedExecutor = var13;
      Raytracer var15 = Raytracer.getRaytracer();
      DamageCalculator var14 = new DamageCalculator(var15);
      this.damageCalculator = var14;
      Raytracer var17 = Raytracer.createRaytracerForCC();
      DamageCalculator var16 = new DamageCalculator(var17);
      this.damageCalculator2 = var16;
      ChatTriggerManager var18 = new ChatTriggerManager(this);
      this.chatTriggerManager = var18;
   }

   public static CompletableFuture<Void> resolveCompletableFuture2(Runnable runnable) {
      return CompletableFuture.runAsync(runnable, ClientHolder.getPhobosClient().getExecutorService());
   }

   public DamageCalculator getDamageCalculator2() {
      return this.damageCalculator;
   }

   public DamageCalculator getDamageCalculator22() {
      return this.damageCalculator2;
   }

   public Logger getLOGGER() {
      return this.LOGGER;
   }

   public CloudApiClient getCloudApiClient() {
      return this.cloudApiClient;
   }

   public BuildInfo getBuildInfo() {
      return this.buildInfo;
   }

   public PacketBus getPacketBus() {
      return this.packetBus;
   }

   public MediaPlayerApi getMediaPlayerApi() {
      return this.mediaPlayerApi;
   }

   public Thread getThread() {
      return this.thread;
   }

   public List<Consumer<NotificationsModule>> getConsumerList2() {
      return this.consumerList2;
   }

   public SessionManager getSessionManager() {
      return this.sessionManager;
   }

   public OptionalModuleLoader getOptionalModuleLoader() {
      return this.optionalModuleLoader;
   }

   public long getLongValue2() {
      return this.longValue2;
   }

   public ImportHandler getImportHandler() {
      return this.importHandler;
   }

   public long getLongValue() {
      return this.longValue;
   }

   public DelayedTaskManager getDelayedTaskManager() {
      return this.delayedTaskManager;
   }

   public List<Consumer<IRCModule>> getConsumerList() {
      return this.consumerList;
   }

   public EventBus getEventBus() {
      return this.eventBus;
   }

   public ExecutorService getExecutorService() {
      return this.executorService;
   }

   public LockedExecutor getLockedExecutor() {
      return this.lockedExecutor;
   }

   public MinecraftClient getMinecraftClient() {
      return this.minecraftClient;
   }

   public RotationManager getRotationManager() {
      return this.rotationManager;
   }

   public FriendList getFriendList() {
      return this.friendList;
   }

   public EnemyList getEnemyList() {
      return this.enemyList;
   }

   public BindManager getBindManager() {
      return this.bindManager;
   }

   public PacketSanitizer getPacketSanitizer() {
      return this.packetSanitizer;
   }

   public TpsTracker getTpsTracker() {
      return this.tpsTracker;
   }

   public InventoryManager getInventoryManager() {
      return this.inventoryManager;
   }

   public MemoryManager getMemoryManager() {
      return this.memoryManager;
   }

   public ConfigManager getConfigManager() {
      return this.configManager;
   }

   public ShaderEffectManager getShaderEffectManager() {
      return this.shaderEffectManager;
   }

   public RenderManager getRenderManager() {
      return this.renderManager;
   }

   public WorldHolder getWorldHolder() {
      return this.worldHolder;
   }

   public AnimationManager getAnimationManager() {
      return this.animationManager;
   }

   public CommandManager getCommandManager() {
      return this.commandManager;
   }

   public ModuleManager getModuleManager() {
      return this.moduleManager;
   }

   public FeatureRegistry getFeatureRegistry() {
      return this.featureRegistry;
   }

   public ChatManager getChatManager() {
      return this.chatManager;
   }

   public ChatTriggerManager getChatTriggerManager() {
      return this.chatTriggerManager;
   }

   public TaskScheduler getTaskScheduler() {
      return this.taskScheduler;
   }

   public MovementSimulator getMovementSimulator() {
      return this.movementSimulator;
   }

   public SkinManager getSkinManager() {
      return this.skinManager;
   }

   public TrackerManager getTrackerManager() {
      return this.trackerManager;
   }

   public DeadEntityTracker getDeadEntityTracker() {
      return this.deadEntityTracker;
   }

   public UserTagManager getUserTagManager() {
      return this.userTagManager;
   }

   public SurroundManager getSurroundManager() {
      return this.surroundManager;
   }

   public ScriptManager getScriptManager() {
      return this.scriptManager;
   }

   public ServerDetector getServerDetector() {
      return this.serverDetector;
   }

   public PhobosSounds getPhobosSounds() {
      return this.phobosSounds;
   }

   public CloudConfigApi getCloudConfigApi() {
      return this.cloudConfigApi;
   }

   public TrayManager getTrayManager() {
      return this.trayManager;
   }

   public EntityIdManager getEntityIdManager() {
      return this.entityIdManager;
   }

   public TimerManager getTimerManager() {
      return this.timerManager;
   }

   public EntityFadeManager getEntityFadeManager() {
      return this.entityFadeManager;
   }

   public HoleChunkScanner getHoleChunkScanner() {
      return this.holeChunkScanner;
   }

   public MotionManager getMotionManager() {
      return this.motionManager;
   }

   public MenuNavigation getMenuNavigation() {
      return this.menuNavigation;
   }

   public RotationDelay getRotationDelay() {
      return this.rotationDelay;
   }

   public HudManager getHudManager() {
      return this.hudManager;
   }

   public TickManager getTickManager() {
      return this.tickManager;
   }

   public ServerAntiCheatDetector getServerAntiCheatDetector() {
      return this.serverAntiCheatDetector;
   }

   public MacroManager getMacroManager() {
      return this.macroManager;
   }

   public ProxyManager getProxyManager() {
      return this.proxyManager;
   }

   public ExplosionTracker getExplosionTracker() {
      return this.explosionTracker;
   }

   public void setModuleManager(ModuleManager moduleManager) {
      this.moduleManager = moduleManager;
   }

   public void setLongValue2(long l) {
      this.longValue2 = l;
   }

   public void setLongValue(long l) {
      this.longValue = l;
   }

   public void setMediaPlayerApi(MediaPlayerApi mediaPlayerApi) {
      this.mediaPlayerApi = mediaPlayerApi;
   }

   public WaypointManager getWaypointManager() {
      return this.waypointManager;
   }

   public ElytraManager getElytraManager() {
      return this.elytraManager;
   }

   public ScreenManager getScreenManager() {
      return this.screenManager;
   }

   public void setThread(Thread thread) {
      this.thread = thread;
   }

   public BlockBreakManager getBlockBreakManager() {
      return this.blockBreakManager;
   }

   public Scheduler getScheduler() {
      return this.scheduler;
   }

   public InteractionManager getInteractionManager() {
      return this.interactionManager;
   }

   public void setExecutorService(ExecutorService executorService) {
      this.executorService = executorService;
   }

   public void setFeatureRegistry(FeatureRegistry featureRegistry) {
      this.featureRegistry = featureRegistry;
   }

   public DamageCache getDamageCache() {
      return this.damageCache;
   }

   public void setDelayedTaskManager(DelayedTaskManager delayedTaskManager) {
      this.delayedTaskManager = delayedTaskManager;
   }

   public PlayerStateTracker getPlayerStateTracker() {
      return this.playerStateTracker;
   }

   public EntityPoseManager getEntityPoseManager() {
      return this.entityPoseManager;
   }

   public void setBindManager(BindManager bindManager) {
      this.bindManager = bindManager;
   }

   public void setFriendList(FriendList friendList) {
      this.friendList = friendList;
   }

   public void setRotationManager(RotationManager rotationManager) {
      this.rotationManager = rotationManager;
   }

   public void setInventoryManager(InventoryManager inventoryManager) {
      this.inventoryManager = inventoryManager;
   }

   public void setCommandManager(CommandManager commandManager) {
      this.commandManager = commandManager;
   }

   public void setShaderEffectManager(ShaderEffectManager shaderEffectManager) {
      this.shaderEffectManager = shaderEffectManager;
   }

   public void setEnemyList(EnemyList enemyList) {
      this.enemyList = enemyList;
   }

   public void setMemoryManager(MemoryManager memoryManager) {
      this.memoryManager = memoryManager;
   }

   public void setTpsTracker(TpsTracker tpsTracker) {
      this.tpsTracker = tpsTracker;
   }

   public void setChatManager(ChatManager chatManager) {
      this.chatManager = chatManager;
   }

   public void setEntityIdManager(EntityIdManager entityIdManager) {
      this.entityIdManager = entityIdManager;
   }

   public void setConfigManager(ConfigManager configManager) {
      this.configManager = configManager;
   }

   public void setWorldHolder(WorldHolder worldHolder) {
      this.worldHolder = worldHolder;
   }

   public void setMovementSimulator(MovementSimulator movementSimulator) {
      this.movementSimulator = movementSimulator;
   }

   public void setPacketSanitizer(PacketSanitizer packetSanitizer) {
      this.packetSanitizer = packetSanitizer;
   }

   public void setTaskScheduler(TaskScheduler taskScheduler) {
      this.taskScheduler = taskScheduler;
   }

   public void setRenderManager(RenderManager renderManager) {
      this.renderManager = renderManager;
   }

   public void setAnimationManager(AnimationManager animationManager) {
      this.animationManager = animationManager;
   }

   public void setRotationDelay(RotationDelay rotationDelay) {
      this.rotationDelay = rotationDelay;
   }

   public void setServerDetector(ServerDetector serverDetector) {
      this.serverDetector = serverDetector;
   }

   public void setHoleChunkScanner(HoleChunkScanner holeChunkScanner) {
      this.holeChunkScanner = holeChunkScanner;
   }

   public void setTrayManager(TrayManager trayManager) {
      this.trayManager = trayManager;
   }

   public void setMenuNavigation(MenuNavigation menuNavigation) {
      this.menuNavigation = menuNavigation;
   }

   public void setServerAntiCheatDetector(ServerAntiCheatDetector serverAntiCheatDetector) {
      this.serverAntiCheatDetector = serverAntiCheatDetector;
   }

   public void setTimerManager(TimerManager timerManager) {
      this.timerManager = timerManager;
   }

   public void setBlockBreakManager(BlockBreakManager blockBreakManager) {
      this.blockBreakManager = blockBreakManager;
   }

   public void setMotionManager(MotionManager motionManager) {
      this.motionManager = motionManager;
   }

   public void setSkinManager(SkinManager skinManager) {
      this.skinManager = skinManager;
   }

   public void setCloudConfigApi(CloudConfigApi cloudConfigApi) {
      this.cloudConfigApi = cloudConfigApi;
   }

   public void setHudManager(HudManager hudManager) {
      this.hudManager = hudManager;
   }

   public void setPhobosSounds(PhobosSounds phobosSounds) {
      this.phobosSounds = phobosSounds;
   }

   public void setTickManager(TickManager tickManager) {
      this.tickManager = tickManager;
   }

   public void setScriptManager(ScriptManager scriptManager) {
      this.scriptManager = scriptManager;
   }

   public void setWaypointManager(WaypointManager waypointManager) {
      this.waypointManager = waypointManager;
   }

   public void setSurroundManager(SurroundManager surroundManager) {
      this.surroundManager = surroundManager;
   }

   public void setEntityFadeManager(EntityFadeManager entityFadeManager) {
      this.entityFadeManager = entityFadeManager;
   }

   public void setTrackerManager(TrackerManager trackerManager) {
      this.trackerManager = trackerManager;
   }

   public void setUserTagManager(UserTagManager userTagManager) {
      this.userTagManager = userTagManager;
   }

   public void setMacroManager(MacroManager macroManager) {
      this.macroManager = macroManager;
   }

   public void setDeadEntityTracker(DeadEntityTracker deadEntityTracker) {
      this.deadEntityTracker = deadEntityTracker;
   }

   public static void handleText6(Text text, NotificationsModule notificationsModule) {
      notificationsModule.textList.add(text);
   }

   public void setPlayerStateTracker(PlayerStateTracker playerStateTracker) {
      this.playerStateTracker = playerStateTracker;
   }

   public void run7() {
      long deadline = System.currentTimeMillis() + 120000L;
      while ((this.minecraftClient.getWindow() == null || this.minecraftClient.getOverlay() == null) && System.currentTimeMillis() < deadline) {
         try {
            Thread.sleep(50L);
         } catch (InterruptedException var1) {
            return;
         }
      }

      this.sessionManager.startOffline(this);
   }

   public void run8() {
      this.configManager.run8();
      this.proxyManager.run8();
      Arrays.stream(Style.values()).forEach(Style::unload);
   }

   public void setScreenManager(ScreenManager screenManager) {
      this.screenManager = screenManager;
   }

   public void setInteractionManager(InteractionManager interactionManager) {
      this.interactionManager = interactionManager;
   }

   public static void handleText7(String text, IRCModule iRCModule) {
      iRCModule.handleText2(text);
   }

   public static void run9() {
   }

   public void setElytraManager(ElytraManager elytraManager) {
      this.elytraManager = elytraManager;
   }

   public void setScheduler(Scheduler scheduler) {
      this.scheduler = scheduler;
   }

   public static void handleText8(String text, IRCModule iRCModule) {
      iRCModule.handleText2(text);
   }

   static {
      Arrays.fill(byteArray2, new byte[]{15, 15, 15, 15, 15, 15, 15, 15, 15});
   }

   public void setDamageCache(DamageCache damageCache) {
      this.damageCache = damageCache;
   }

   public void setProxyManager(ProxyManager proxyManager) {
      this.proxyManager = proxyManager;
   }

   public void setEntityPoseManager(EntityPoseManager entityPoseManager) {
      this.entityPoseManager = entityPoseManager;
   }

   public void run10() {
      if (IRCModule.getIRCModule() != null) {
         this.consumerList.forEach(consumer -> consumer.accept(IRCModule.getIRCModule()));
      }
   }

   public void setExplosionTracker(ExplosionTracker explosionTracker) {
      this.explosionTracker = explosionTracker;
   }

   public class ExceptionReporter implements UncaughtExceptionHandler {
      @Override
      public void uncaughtException(Thread thread, Throwable throwable) {
         PhobosClient var3 = PhobosClient.this;
         Logger var4 = var3.LOGGER;
         var4.info("Unable to establish server connection, shutting down...");
         ClientGuard.stop();
      }
   }

   public static final class InitEvent {
      public final PhobosClient phobosClient;

      public InitEvent(PhobosClient phobosClient) {
         this.phobosClient = phobosClient;
      }

      public PhobosClient getPhobosClient() {
         return this.phobosClient;
      }
   }

   private static class Shutdown extends Feature {
      public Shutdown(Runnable runnableArg) {
         this.handleListener(new Listener<RunEvent>() {
            public void handleObject2(RunEvent runEvent) {
               runnableArg.run();
            }
         });
      }
   }
}
