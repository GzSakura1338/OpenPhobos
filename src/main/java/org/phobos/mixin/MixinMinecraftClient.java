package org.phobos.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.MinecraftClient.LoadingContext;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SleepingChatScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.tutorial.TutorialManager;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.ClientConnection;
import net.minecraft.util.Hand;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.profiler.Profiler;
import org.earthhack.mixin.EventSubscriber;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWImage.Buffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.phobos.api.WindowCallback;
import org.phobos.core.kit.KitRegistry;
import org.phobos.core.tick.TickCounter;
import org.phobos.core.render.shader.ShaderEffectManager;
import org.phobos.core.config.ConfigManager;
import org.phobos.core.world.WaypointManager;
import org.phobos.core.ClientHolder;
import org.phobos.core.inventory.SwitchOverride;
import org.phobos.core.inventory.InventoryUtils;
import org.phobos.event.DeathScreenEvent;
import org.phobos.event.DisconnectEvent;
import org.phobos.event.DisconnectEvent2;
import org.phobos.event.DoAttackEvent;
import org.phobos.event.EndTickEvent;
import org.phobos.event.HandleInputEventsEvent;
import org.phobos.event.InitEvent;
import org.phobos.event.OnDeathScreenEvent;
import org.phobos.event.RunEvent;
import org.phobos.event.SetupEvent;
import org.phobos.event.TickEvent2;
import org.phobos.module.player.FastPlaceModule;
import org.phobos.module.client.IRCModule;
import org.phobos.module.misc.MiddleClickModule;
import org.phobos.module.player.MultiTaskModule;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient implements EventSubscriber {
   @Shadow
   @Final
   public WorldRenderer worldRenderer;
   @Shadow
   @Final
   private MusicTracker musicTracker;
   @Shadow
   @Final
   public Keyboard keyboard;
   @Shadow
   @Nullable
   public HitResult crosshairTarget;
   @Shadow
   @Final
   private TextureManager textureManager;
   @Shadow
   private volatile boolean paused;
   @Shadow
   @Nullable
   public ClientWorld world;
   @Shadow
   @Final
   public GameRenderer gameRenderer;
   @Shadow
   @Final
   private SoundManager soundManager;
   @Shadow
   @Final
   private MessageHandler messageHandler;
   @Shadow
   @Final
   public InGameHud inGameHud;
   @Shadow
   private long uptimeInTicks;
   @Shadow
   @Nullable
   private ClientConnection integratedServerConnection;
   @Shadow
   @Nullable
   public ClientPlayerInteractionManager interactionManager;
   @Shadow
   @Final
   public GameOptions options;
   @Shadow
   @Nullable
   public ClientPlayerEntity player;
   @Shadow
   @Final
   private TutorialManager tutorialManager;
   @Shadow
   private int itemUseCooldown;
   @Shadow
   @Final
   public ParticleManager particleManager;
   @Shadow
   @Nullable
   private Supplier<CrashReport> crashReportSupplier;
   @Shadow
   @Final
   private Window window;
   @Shadow
   private Profiler profiler;
   @Shadow
   public int attackCooldown;
   @Shadow
   @Nullable
   public Screen currentScreen;
   @Shadow
   @Nullable
   private Overlay overlay;

   @Inject(method = "reloadResources(ZLnet/minecraft/client/MinecraftClient$LoadingContext;)Ljava/util/concurrent/CompletableFuture;", at = @At("TAIL"))
   private void setOverlayHook(boolean force, @Nullable LoadingContext loadingContext, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
      CompletableFuture<Void> result = (CompletableFuture<Void>)cir.getReturnValue();
      result.thenRun(() -> {
         ShaderEffectManager manager = ClientHolder.getPhobosClient().getShaderEffectManager();
         if (manager != null) {
            manager.run2();
            manager.check();
         }
      });
   }

   @Shadow
   public abstract Window getWindow();

   @Inject(method = "run", at = @At("RETURN"))
   private void runHook(CallbackInfo ci) {
      this.invoke(new RunEvent());
   }

   @Inject(
      method = "<init>",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setOverlay(Lnet/minecraft/client/gui/screen/Overlay;)V", shift = Shift.AFTER)
   )
   private void runGameHook(CallbackInfo ci) {
      ClientHolder.getPhobosClient().run3();
   }

   @Shadow
   protected abstract boolean shouldTick();

   @Shadow
   protected abstract void handleInputEvents();

   @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("RETURN"))
   private void disconnectHook(Screen disconnectionScreen, CallbackInfo ci) {
      this.invoke(new DisconnectEvent());
   }

   @Unique
   private void insertImage(InputStream _16, InputStream _32) {
      try {
         MemoryStack stack = MemoryStack.stackPush();

         try {
            Buffer buffer = GLFWImage.malloc(2, stack);
            List<InputStream> list = List.of(_16, _32);
            List<ByteBuffer> buffers = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
               NativeImage image = NativeImage.read(list.get(i));
               ByteBuffer bb = MemoryUtil.memAlloc(image.getWidth() * image.getHeight() * 4);
               bb.asIntBuffer().put(image.copyPixelsRgba());
               buffer.position(i);
               buffer.width(image.getWidth());
               buffer.height(image.getHeight());
               buffer.pixels(bb);
               buffers.add(bb);
            }

            try {
               if (GLFW.glfwGetPlatform() != 393219) {
                  GLFW.glfwSetWindowIcon(this.getWindow().getHandle(), buffer);
               }
            } catch (Throwable var11) {
            }

            for (ByteBuffer bb : buffers) {
               MemoryUtil.memFree(bb);
            }
         } catch (Throwable var12) {
            if (stack != null) {
               try {
                  stack.close();
               } catch (Throwable var10) {
                  var12.addSuppressed(var10);
               }
            }

            throw var12;
         }

         if (stack != null) {
            stack.close();
         }
      } catch (IOException var13) {
      }
   }

   @ModifyVariable(
      method = "render",
      ordinal = 0,
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;runTasks()V", shift = Shift.AFTER)
   )
   private int runTickHook(int i) {
      TickCounter.INSTANCE.setIntValue(i);
      this.invoke(TickCounter.INSTANCE);
      return TickCounter.INSTANCE.getIntValue();
   }

   @Shadow
   protected abstract String getWindowTitle();

   @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
   private void tickHook(CallbackInfo ci) {
      TickEvent2 event = this.invoke(new TickEvent2(), ci);
      if (event.isCancelled()) {
         this.uptimeInTicks++;
         if (this.world != null && !this.paused) {
            this.world.getTickManager().step();
         }

         if (this.itemUseCooldown > 0) {
            this.itemUseCooldown--;
         }

         this.profiler.push("gui");
         this.messageHandler.processDelayedMessages();
         this.inGameHud.tick(this.paused);
         this.profiler.pop();
         this.gameRenderer.updateCrosshairTarget(1.0F);
         this.tutorialManager.tick(this.world, this.crosshairTarget);
         this.profiler.push("gameMode");
         if (!this.paused && this.world != null) {
            this.interactionManager.tick();
         }

         this.profiler.swap("textures");
         if (this.shouldTick()) {
            this.textureManager.tick();
         }

         if (this.currentScreen != null || this.player == null) {
            if (this.currentScreen instanceof SleepingChatScreen sleepingChatScreen && !this.player.isSleeping()) {
               sleepingChatScreen.closeChatIfEmpty();
            }
         } else if (this.player.isDead() && !(this.currentScreen instanceof DeathScreen)) {
            this.setScreen((Screen)null);
         } else if (this.player.isSleeping() && this.world != null) {
            this.setScreen(new SleepingChatScreen());
         }

         if (this.currentScreen != null) {
            this.attackCooldown = 10000;
         }

         if (this.currentScreen != null) {
            Screen.wrapScreenError(() -> this.currentScreen.tick(), "Ticking screen", this.currentScreen.getClass().getCanonicalName());
         }

         if (!this.getDebugHud().shouldShowDebugHud()) {
            this.inGameHud.resetDebugHudChunk();
         }

         if (this.overlay == null && this.currentScreen == null) {
            this.profiler.swap("Keybindings");
            this.handleInputEvents();
            if (this.attackCooldown > 0) {
               this.attackCooldown--;
            }
         }

         if (this.world != null) {
            this.profiler.swap("gameRenderer");
            if (!this.paused) {
               this.gameRenderer.tick();
            }

            this.profiler.swap("levelRenderer");
            if (!this.paused) {
               this.worldRenderer.tick();
            }
         } else if (this.gameRenderer.getPostProcessor() != null) {
            this.gameRenderer.disablePostProcessor();
         }

         if (!this.paused) {
            this.musicTracker.tick();
         }

         this.soundManager.tick(this.paused);
         if (this.world != null) {
            this.profiler.swap("animateTick");
            if (!this.paused && this.shouldTick()) {
               this.world.doRandomBlockDisplayTicks(this.player.getBlockX(), this.player.getBlockY(), this.player.getBlockZ());
            }

            this.profiler.swap("particles");
            if (!this.paused && this.shouldTick()) {
               this.particleManager.tick();
            }
         } else if (this.integratedServerConnection != null) {
            this.profiler.swap("pendingConnection");
            this.integratedServerConnection.tick();
         }

         this.profiler.swap("keyboard");
         this.keyboard.pollDebugCrash();
         this.profiler.pop();
      }
   }

   @Shadow
   public abstract void setScreen(@Nullable Screen var1);

   @Shadow
   public abstract DebugHud getDebugHud();

   @Inject(method = "printCrashReport(Lnet/minecraft/util/crash/CrashReport;)V", at = @At("HEAD"))
   private void printCrashReportHook1(CrashReport report, CallbackInfo ci) {
      IRCModule.handleCrashReport(report);
      this.invoke(new RunEvent());
   }

   @Shadow
   protected abstract void handleBlockBreaking(boolean var1);

   @Inject(method = "joinWorld", at = @At("RETURN"))
   private void joinHook(CallbackInfo ci) {
      this.invoke(new DisconnectEvent());
   }

   @Inject(method = "onResolutionChanged", at = @At("TAIL"))
   private void captureResize(CallbackInfo ci) {
      ((WindowCallback)WindowCallback.windowCallbackEvent.invoker()).handleMinecraftClient((MinecraftClient)(Object)this, this.window);
   }

   @Inject(method = "<init>", at = @At("TAIL"))
   private void setupHook(CallbackInfo ci) {
      try {
         ClientHolder.getPhobosClient().getRenderManager().setSetupEvent(new SetupEvent(new Font[]{Font.decode("FreeSerif")}, 16.0F, 36, 2));
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   @Inject(
      method = "printCrashReport()V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/MinecraftClient;printCrashReport(Lnet/minecraft/client/MinecraftClient;Ljava/io/File;Lnet/minecraft/util/crash/CrashReport;)V"
      )
   )
   private void printCrashReportHook2(CallbackInfo ci) {
      assert this.crashReportSupplier != null;
      IRCModule.handleCrashReport(this.crashReportSupplier.get());
      this.invoke(new RunEvent());
   }

   @Inject(method = "stop", at = @At("HEAD"))
   private void stopHook(CallbackInfo ci) {
      Thread thread = new Thread(() -> {
         try {
            ConfigManager.stop();
            KitRegistry.stop();
            WaypointManager.handlePhobosClient2(ClientHolder.getPhobosClient());
         } catch (Throwable var1) {
         }
      });
      thread.setName("Phobos/Stop");
      thread.start();
   }

   @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;Z)V", at = @At("HEAD"))
   private void disconnectHook(Screen disconnectionScreen, boolean transferring, CallbackInfo ci) {
      this.invoke(new DisconnectEvent2());
   }

   @Inject(method = "tick", at = @At("TAIL"))
   public void endTickHook(CallbackInfo ci) {
      this.invoke(new EndTickEvent());
   }

   @Inject(
      method = "handleInputEvents",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z", ordinal = 0, shift = Shift.BEFORE),
      cancellable = true
   )
   private void handleInputEventsHook3(CallbackInfo ci) {
      this.invoke(new HandleInputEventsEvent(), ci);
   }

   @Redirect(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;isPressed()Z"))
   private boolean handleInputEventsHook4(KeyBinding instance) {
      boolean pressed = instance.isPressed();
      if (instance == this.options.attackKey) {
         return SwitchOverride.testFlag3(pressed);
      } else {
         return instance == this.options.useKey ? SwitchOverride.testFlag2(pressed) : pressed;
      }
   }

   @ModifyExpressionValue(
      method = "doItemUse",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;isBreakingBlock()Z")
   )
   private boolean doItemUseHook(boolean original) {
      return !MultiTaskModule.check4() && original;
   }

   @ModifyExpressionValue(
      method = "handleInputEvents",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z", ordinal = 0)
   )
   private boolean handleInputEventsHook1(boolean original) {
      return !MultiTaskModule.check3() && original;
   }

   @Inject(method = "setScreen", at = @At("HEAD"))
   private <T extends Screen> void screenHook(T screen, CallbackInfo ci) {
      this.invoke(new InitEvent(screen));
   }

   @Inject(
      method = "handleInputEvents",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z", ordinal = 0, shift = Shift.BEFORE)
   )
   private void handleInputEventsHook2(CallbackInfo info) {
      if (this.player != null && this.interactionManager != null) {
         if (MultiTaskModule.check2() && this.player.isUsingItem()) {
            if (!SwitchOverride.testFlag2(this.options.useKey.isPressed())) {
               this.interactionManager.stopUsingItem(this.player);
            }

            while (this.options.useKey.wasPressed()) {
            }
         }
      }
   }

   @Inject(
      method = "setScreen",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;showsDeathScreen()Z", shift = Shift.BEFORE)
   )
   private void onDeathScreenHook(Screen screen, CallbackInfo ci) {
      this.invoke(new OnDeathScreenEvent(this.player));
   }

   @Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
   private void handleBlockBreakingHook(boolean breaking, CallbackInfo ci) {
      if (ClientHolder.getPhobosClient().getBlockBreakManager().testClientPlayerEntity(this.player, this.world, this.interactionManager)) {
         ci.cancel();
      }
   }

   @Inject(method = "doItemPick", at = @At("HEAD"), cancellable = true)
   private void doItemPick(CallbackInfo ci) {
      if (MiddleClickModule.check()) {
         ci.cancel();
      }
   }

   @Inject(
      method = "doItemUse",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isItemEnabled(Lnet/minecraft/resource/featuretoggle/FeatureSet;)Z")
   )
   private void doItemUseHook(CallbackInfo ci, @Local ItemStack stack, @Local Hand hand) {
      this.itemUseCooldown = FastPlaceModule.computeItemStack(stack, hand);
   }

   @Inject(
      method = "doItemUse",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;interactEntityAtLocation(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/hit/EntityHitResult;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;"
      ),
      cancellable = true
   )
   private void doItemUseHook(CallbackInfo ci, @Local ItemStack itemStack, @Local Entity entity) {
      if (MultiTaskModule.check() && (entity instanceof EndCrystalEntity || entity instanceof PlayerEntity)) {
         if (InventoryUtils.testItem2(itemStack.getItem())) {
            return;
         }

         this.itemUseCooldown = 0;
         ci.cancel();
      }
   }

   @Unique
   private boolean acceptBlockBreaking(boolean original) {
      if (this.player == null) {
         return original;
      }

      ItemStack itemStack = this.player.getMainHandStack();
      if (!MultiTaskModule.check() || !itemStack.isOf(Items.END_CRYSTAL) && !itemStack.isOf(Items.OBSIDIAN)) {
         return original;
      }

      this.attackCooldown = 0;
      return false;
   }

   @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
   private void doAttackHook(CallbackInfoReturnable<Boolean> cir) {
      DoAttackEvent event = this.invoke(new DoAttackEvent());
      if (event.isCancelled()) {
         cir.setReturnValue(false);
      }
   }

   @ModifyExpressionValue(
      method = "handleBlockBreaking",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z")
   )
   private boolean handleBlockBreakingHook2(boolean original) {
      return !MultiTaskModule.check3() && original;
   }

   @Inject(
      method = "doAttack",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;attackBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z"
      ),
      cancellable = true
   )
   private void doAttackHook(CallbackInfoReturnable<Boolean> cir, @Local ItemStack itemStack) {
      if (MultiTaskModule.check() && (itemStack.isOf(Items.END_CRYSTAL) || itemStack.isOf(Items.OBSIDIAN))) {
         this.attackCooldown = 0;
         cir.setReturnValue(false);
      }
   }

   @Redirect(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;handleBlockBreaking(Z)V"))
   private void handleInputEventsHook$isPressed(MinecraftClient instance, boolean breaking) {
      this.handleBlockBreaking(this.acceptBlockBreaking(breaking));
   }

   @Inject(
      method = "setScreen",
      at = @At(value = "NEW", target = "(Lnet/minecraft/text/Text;Z)Lnet/minecraft/client/gui/screen/DeathScreen;"),
      cancellable = true
   )
   public void deathScreenHook(Screen screen, CallbackInfo ci) {
      this.invoke(new DeathScreenEvent(), ci);
   }
}
