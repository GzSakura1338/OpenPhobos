package org.phobos.mixin.game.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.awt.Desktop;
import java.net.URI;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LogoDrawer;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget.PressAction;
import net.minecraft.client.util.Window;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.earthhack.ducks.ITitleScreen;
import org.earthhack.mixin.EventSubscriber;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.nanovg.NanoVG;
import org.phobos.api.Easing;
import org.phobos.api.TimeGate;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.gui.screen.ClickGuiScreen;
import org.phobos.core.user.UserSession;
import org.phobos.core.gui.ClipStack;
import org.phobos.core.animation.EasedAnimation;
import org.phobos.core.gui.ClipBounds;
import org.phobos.core.config.ConfigManager;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.ClientHolder;
import org.phobos.core.ClientGuard;
import org.phobos.core.user.Permissions;
import org.phobos.event.InitEvent;
import org.phobos.event.InitEvent3;
import org.phobos.module.client.IRCModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class MixinTitleScreen extends MixinScreen implements EventSubscriber, MinecraftHolder, ITitleScreen {
   @Unique
   private String line = null;
   @Unique
   private final TimeGate.Normal normal = new TimeGate.Normal();
   @Unique
   private static final Identifier BUTTON = Identifier.of("phoboslite:icon/icon_button");
   @Shadow
   private boolean doBackgroundFade;
   @Shadow
   private long backgroundFadeStart;
   @Unique
   private static final Identifier BUTTON_FOCUSED = Identifier.of("phoboslite:icon/icon_button_focused");
   @Unique
   private final EasedAnimation chatAnimation = new EasedAnimation(150L);
   @Unique
   private final ClipStack clipStack = new ClipStack();
   @Unique
   private String text = null;
   @Unique
   private static final Identifier LOGO = Identifier.of("phoboslite", "textures/logo.png");

   @Unique
   @Override
   public void onMessage() {
      this.chatAnimation.setLongValue3(System.currentTimeMillis());
   }

   @Unique
   private boolean testInt(int key) {
      return GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), key) == 1;
   }

   @Inject(method = "render", at = @At("TAIL"))
   private void renderHook(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      if (IRCModule.getIRCModule() != null
         && IRCModule.getIRCModule().check7()
         && ClientHolder.getPhobosClient() != null
         && ClientHolder.getPhobosClient().getUserTagManager() != null) {
         float f = 1.0F;
         if (this.doBackgroundFade) {
            float g = (float)(Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 2000.0F;
            if (!(g > 1.0F)) {
               g = MathHelper.clamp(g, 0.0F, 1.0F);
               f = MathHelper.clampedMap(g, 0.5F, 1.0F, 0.0F, 1.0F);
            }
         }

         int i = MathHelper.ceil(f * 255.0F) << 24;
         if ((i & -67108864) != 0) {
            this.start();
            int height = mc.getWindow().getScaledHeight();
            int width = mc.getWindow().getScaledWidth();
            float y = height - height / 5.0F;
            float h = y + height / 3.0F;
            float x = 2.0F;
            float w = width / 2.0F;
            this.handleDouble(x, y - height / 3.0F, w, h);
            int offset = -10;

            for (String message : ClientHolder.getPhobosClient().getUserTagManager().getTextDeque()) {
               ClientHolder.getPhobosClient()
                  .getRenderManager()
                  .handleDrawContext(
                     context,
                     message,
                     x,
                     y + offset + this.chatAnimation.computeEasing(Easing.InOutEasing.INSTANCE3) * 10.0F,
                     new Color(16777215 | i),
                     1.0F,
                     true
                  );
               offset -= 12;
            }

            this.run3();
            this.run4();
         }
      }
   }

   @Unique
   public void run3() {
      long ctx = this.value();
      NanoVG.nvgResetScissor(ctx);
      NanoVG.nvgRestore(ctx);
      this.handleClipBounds(this.clipStack.getClipBounds());
   }

   @Unique
   private void run4() {
      ClientHolder.getPhobosClient().getRenderManager().run4();
   }

   private void handleClipBounds(ClipBounds section) {
      if (section == null) {
         RenderSystem.disableScissor();
      } else {
         Window w = mc.getWindow();
         int i = w.getHeight();
         double d = w.getScaleFactor();
         double e = section.getDoubleValue2() * d;
         double f = i - section.value2() * d;
         double g = section.getDoubleValue3() * d;
         double h = section.getDoubleValue() * d;
         RenderSystem.enableScissor((int)e, (int)f, Math.max(0, (int)g), Math.max(0, (int)h));
      }
   }

   private void update0() {
      ClientGuard.handleConsumer(text -> this.text = text);
   }

   @Inject(method = "<init>(ZLnet/minecraft/client/gui/LogoDrawer;)V", at = @At("TAIL"))
   private void initHook(boolean doBackgroundFade, LogoDrawer logoDrawer, CallbackInfo ci) {
      this.invoke(new InitEvent<>(Screen.class.cast(this)));
      UserSession.userSession.handleRunnable(() -> {
         if (UserSession.userSession.getUserProfile() != null) {
            this.line = this.code(UserSession.userSession.getUserProfile().getPermission()) + "Sydney";
            this.update0();
         }
      });
   }

   @Unique
   private long value() {
      return ClientHolder.getPhobosClient().getRenderManager().value();
   }

   @Unique
   private String code(Permissions.Permission permission) {
      return permission.getFormatting();
   }

   @Redirect(
      method = "render",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)I"
      )
   )
   private int renderHook2(DrawContext instance, TextRenderer textRenderer, String text, int x, int y, int color) {
      if (this.line != null && this.text != null) {
         if (this.normal.testLong2(30000L)) {
            this.update0();
         }

         instance.drawTextWithShadow(mc.textRenderer, this.text, 2, 2, color);
         instance.drawTextWithShadow(mc.textRenderer, "Logged in as " + this.line + "§r", 2, 14, color);
         return instance.drawTextWithShadow(textRenderer, text.replaceAll("/Fabric \\(Modded\\)", ""), x, y, color);
      } else {
         return instance.drawTextWithShadow(textRenderer, text, x, y, color);
      }
   }

   @Unique
   private void start() {
      ClientHolder.getPhobosClient().getRenderManager().start();
   }

   @Unique
   public void handleDouble(double x, double y, double w, double h) {
      long ctx = this.value();
      NanoVG.nvgSave(ctx);
      NanoVG.nvgIntersectScissor(ctx, (float)x, (float)y, (float)w, (float)h);
      this.handleClipBounds(this.clipStack.resolveClipBounds(new ClipBounds(x, y, w, h)));
   }

   @Unique
   @Override
   public void run2() {
      this.update0();
   }


   @Redirect(
      method = "init",
      at = @At(
         value = "NEW",
         target = "(IIIILnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;Lnet/minecraft/client/font/TextRenderer;)Lnet/minecraft/client/gui/widget/PressableTextWidget;"
      )
   )
   private PressableTextWidget initHook2(int x, int y, int width, int height, Text text, PressAction onPress, TextRenderer textRenderer) {
      MutableText copyright = Text.literal("Copyright Phobos Ltd.");
      int length = this.textRenderer.getWidth(copyright);
      int offset = this.width - length - 2;
      return new PressableTextWidget(offset, y, length, height, copyright, button -> {
         Desktop desktop = Desktop.getDesktop();

         try {
            desktop.browse(new URI("https://discord.gg/3arthh4ck"));
         } catch (Throwable var4) {
            onPress.onPress(button);
         }
      }, textRenderer);
   }

   @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TextIconButtonWidget;setPosition(II)V", ordinal = 1))
   private void initHook(CallbackInfo ci) {
      assert this.client != null;
      int l = this.height / 4 + 48;
      TexturedButtonWidget icon = this.addDrawableChild(this.createUIButton(button -> this.client.setScreen(ClickGuiScreen.clickGuiScreen.getScreen())));
      icon.setPosition(this.width / 2 + 104, l + 72 + 12 - 15 - 8);
      icon.setWidth(20);
      icon.setHeight(20);
   }

   @Inject(method = "init", at = @At("TAIL"))
   private void initHook1(CallbackInfo ci) {
      if (!InitEvent3.flag) {
         mc.setScreen(new InitEvent3());
      }
   }

   @Unique
   private TexturedButtonWidget createUIButton(PressAction onPress) {
      return new TexturedButtonWidget(0, 0, 0, 0, new ButtonTextures(BUTTON, BUTTON_FOCUSED), onPress) {
         public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            if (!ConfigManager.getCloudSettings().theme.getValue()) {
               float f = 1.0F;
               if (MixinTitleScreen.this.doBackgroundFade) {
                  float g = (float)(Util.getMeasuringTimeMs() - MixinTitleScreen.this.backgroundFadeStart) / 2000.0F;
                  if (g <= 1.0F) {
                     g = MathHelper.clamp(g, 0.0F, 1.0F);
                     f = MathHelper.clampedMap(g, 0.5F, 1.0F, 0.0F, 1.0F);
                  }

                  this.setAlpha(f);
               }

               super.renderWidget(context, mouseX, mouseY, delta);
            } else {
               RenderUtils.handleMatrixStack12(
                  context.getMatrices(), Color.WHITE, this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0.0F, () -> {
                     RenderUtils.menuButtonShader.handleColor(Color.WHITE);
                     RenderUtils.menuButtonShader.run3();
                  }
               );
               int offset = 2;
               RenderSystem.setShaderTexture(0, MixinTitleScreen.LOGO);
               RenderUtils.handleMatrixStack3(context.getMatrices(), this.getX() + offset, this.getY() + offset, this.width - offset * 2, this.height - offset * 2);
            }
         }
      };
   }
}
