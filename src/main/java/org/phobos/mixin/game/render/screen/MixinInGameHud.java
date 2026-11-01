package org.phobos.mixin.game.render.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import org.earthhack.mixin.EventSubscriber;
import org.jetbrains.annotations.Nullable;
import org.phobos.module.player.LegacyModule;
import org.phobos.event.RenderEvent2;
import org.phobos.module.render.CrosshairModule;
import org.phobos.module.render.NoRenderModule;
import org.phobos.module.render.NoRenderModule;
import org.phobos.module.render.NoRenderModule;
import org.phobos.module.player.SilentHotbarModule;
import org.phobos.module.player.VirtualHotbarModule;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class MixinInGameHud implements EventSubscriber {
   @Shadow
   @Final
   private static Identifier HOTBAR_ATTACK_INDICATOR_PROGRESS_TEXTURE;
   @Shadow
   @Final
   private static Identifier HOTBAR_ATTACK_INDICATOR_BACKGROUND_TEXTURE;
   @Shadow
   @Final
   private static Identifier HOTBAR_OFFHAND_LEFT_TEXTURE;
   @Shadow
   @Final
   private static Identifier HOTBAR_TEXTURE;
   @Shadow
   @Final
   private PlayerListHud playerListHud;
   @Shadow
   @Final
   private MinecraftClient client;
   @Shadow
   @Final
   private static Identifier HOTBAR_SELECTION_TEXTURE;
   @Shadow
   @Final
   private static Identifier HOTBAR_OFFHAND_RIGHT_TEXTURE;

   @Inject(method = "render", at = @At("TAIL"))
   private void renderHook(DrawContext ctx, RenderTickCounter tickCounter, CallbackInfo ci) {
      this.invoke(new RenderEvent2(ctx, tickCounter.getTickDelta(true)));
   }

   @Overwrite
   @SuppressWarnings("overwrite")
   private void renderHotbar(DrawContext context, RenderTickCounter tickCounter) {
      VirtualHotbarModule.getVirtualHotbarModule().run();
      PlayerEntity playerEntity = this.getCameraPlayer();
      if (playerEntity != null) {
         ItemStack itemStack = playerEntity.getOffHandStack();
         Arm arm = playerEntity.getMainArm().getOpposite();
         int i = context.getScaledWindowWidth() / 2;
         RenderSystem.enableBlend();
         context.getMatrices().push();
         context.getMatrices().translate(0.0F, 0.0F, -90.0F);
         context.drawGuiTexture(HOTBAR_TEXTURE, i - 91, context.getScaledWindowHeight() - 22, 182, 22);
         context.drawGuiTexture(
            HOTBAR_SELECTION_TEXTURE,
            i - 91 - 1 + VirtualHotbarModule.getVirtualHotbarModule().computeInt2(SilentHotbarModule.computeInt(playerEntity.getInventory().selectedSlot)) * 20,
            context.getScaledWindowHeight() - 22 - 1,
            24,
            23
         );
         if (!itemStack.isEmpty() && !LegacyModule.getLegacyModule().check7()) {
            if (arm == Arm.LEFT) {
               context.drawGuiTexture(HOTBAR_OFFHAND_LEFT_TEXTURE, i - 91 - 29, context.getScaledWindowHeight() - 23, 29, 24);
            } else {
               context.drawGuiTexture(HOTBAR_OFFHAND_RIGHT_TEXTURE, i + 91, context.getScaledWindowHeight() - 23, 29, 24);
            }
         }

         context.getMatrices().pop();
         RenderSystem.disableBlend();
         int l = 1;

         for (int m = 0; m < 9; m++) {
            int n = i - 90 + m * 20 + 2;
            int o = context.getScaledWindowHeight() - 16 - 3;
            this.renderHotbarItem(
               context,
               n,
               o,
               tickCounter,
               playerEntity,
               (ItemStack)playerEntity.getInventory().main.get(VirtualHotbarModule.getVirtualHotbarModule().computeInt4(m)),
               l++
            );
         }

         if (!itemStack.isEmpty() && !LegacyModule.getLegacyModule().check7()) {
            int var17 = context.getScaledWindowHeight() - 16 - 3;
            if (arm == Arm.LEFT) {
               this.renderHotbarItem(context, i - 91 - 26, var17, tickCounter, playerEntity, itemStack, l++);
            } else {
               this.renderHotbarItem(context, i + 91 + 10, var17, tickCounter, playerEntity, itemStack, l++);
            }
         }

         if (this.client.options.getAttackIndicator().getValue() == AttackIndicator.HOTBAR) {
            RenderSystem.enableBlend();
            float f = this.client.player.getAttackCooldownProgress(0.0F);
            if (f < 1.0F) {
               int n = context.getScaledWindowHeight() - 20;
               int o = i + 91 + 6;
               if (arm == Arm.RIGHT) {
                  o = i - 91 - 22;
               }

               int p = (int)(f * 19.0F);
               context.drawGuiTexture(HOTBAR_ATTACK_INDICATOR_BACKGROUND_TEXTURE, o, n, 18, 18);
               context.drawGuiTexture(HOTBAR_ATTACK_INDICATOR_PROGRESS_TEXTURE, 18, 18, 0, 18 - p, o, n + 18 - p, 18, p);
            }

            RenderSystem.disableBlend();
         }
      }
   }

   @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
   private void renderCrosshairHook(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
      if (CrosshairModule.check()) {
         ci.cancel();
      }
   }

   @Inject(method = "renderScoreboardSidebar*", at = @At("HEAD"), cancellable = true)
   private void renderScoreboardSidebarHook(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
      this.invoke(new NoRenderModule.Scoreboard(), ci);
   }

   @Inject(method = "renderHeldItemTooltip", at = @At("HEAD"), cancellable = true)
   private void renderHeldItemTooltipHook(DrawContext context, CallbackInfo ci) {
      this.invoke(new NoRenderModule.ActionBar(), ci);
   }

   @Shadow
   @Nullable
   protected abstract PlayerEntity getCameraPlayer();

   @Inject(method = "renderStatusEffectOverlay", at = @At("HEAD"), cancellable = true)
   private void renderStatusEffectHook(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
      this.invoke(new NoRenderModule.Potions(), ci);
   }

   @Shadow
   protected abstract void renderHotbarItem(DrawContext var1, int var2, int var3, RenderTickCounter var4, PlayerEntity var5, ItemStack var6, int var7);

   @Inject(method = "renderPlayerList", at = @At("HEAD"), cancellable = true)
   private void renderPlayerList(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
      ci.cancel();
      assert this.client.world != null && this.client.player != null;
      Scoreboard scoreboard = this.client.world.getScoreboard();
      ScoreboardObjective scoreboardObjective = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.LIST);
      this.playerListHud
         .setVisible(
            this.client.options.playerListKey.isPressed()
               && (!this.client.isInSingleplayer() || this.client.player.networkHandler.getListedPlayerListEntries().size() > 1 || scoreboardObjective != null)
         );
      this.playerListHud.render(context, context.getScaledWindowWidth(), scoreboard, scoreboardObjective);
   }
}
