package org.phobos.mixin.game.ui;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.gui.hud.PlayerListHud.Heart;
import net.minecraft.client.gui.hud.PlayerListHud.ScoreDisplayEntry;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.scoreboard.ReadableScoreboardScore;
import net.minecraft.scoreboard.ScoreHolder;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardCriterion.RenderType;
import net.minecraft.scoreboard.number.NumberFormat;
import net.minecraft.scoreboard.number.StyledNumberFormat;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import org.earthhack.ducks.IPlayerListEntry;
import org.earthhack.mixin.EventSubscriber;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.user.UserTagManager;
import org.phobos.core.hud.element.PlayerListHudElement;
import org.phobos.core.render.RenderUtils;
import org.phobos.module.misc.ExtraTabModule;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerListHud.class, priority = Integer.MAX_VALUE)
public abstract class MixinPlayerListHud implements EventSubscriber {
   @Shadow
   @Final
   private MinecraftClient client;
   @Shadow
   @Nullable
   private Text header;
   @Unique
   private final FloatAnimator floatAnimator = new FloatAnimator();
   @Shadow
   private boolean visible;
   @Shadow
   @Nullable
   private Text footer;
   @Shadow
   @Final
   private Map<UUID, Heart> hearts;

   @Redirect(method = "collectPlayerEntries", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;limit(J)Ljava/util/stream/Stream;"))
   private Stream<PlayerListEntry> collectPlayerEntriesHook(Stream<PlayerListEntry> stream, long limit) {
      return ExtraTabModule.getExtraTabModule().check7()
         ? stream.limit(ExtraTabModule.getExtraTabModule().getSize().getValue().intValue())
         : stream.limit(limit);
   }

   @Inject(method = "render", at = @At("HEAD"), cancellable = true)
   public void render(DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, @Nullable ScoreboardObjective objective, CallbackInfo ci) {
      ci.cancel();
      boolean custom = !PlayerListHudElement.getPlayerListHudElement().isFlag3();
      if (custom) {
         this.floatAnimator.setFloatValue(this.visible ? 0.0F : 500.0F);
         this.floatAnimator.handleFloat3(10.0F, 0.1F);
         if (this.floatAnimator.getFloatValue2() >= 495.0F) {
            return;
         }

         context.getMatrices().push();
         context.getMatrices().translate(0.0F, -this.floatAnimator.getFloatValue2(), 0.0F);
      } else if (!this.visible) {
         return;
      }

      List<PlayerListEntry> list = this.collectPlayerEntries();
      List<ScoreDisplayEntry> list2 = new ArrayList<>(list.size());
      int i = this.client.textRenderer.getWidth(" ");
      int j = 0;
      int k = 0;

      for (PlayerListEntry playerListEntry : list) {
         boolean bl4 = ExtraTabModule.getExtraTabModule().check7()
            && ExtraTabModule.getExtraTabModule().getTelemetry().getValue()
            && playerListEntry instanceof IPlayerListEntry access
            && access.getUser() != null;
         Text text = this.getPlayerName(playerListEntry);
         j = Math.max(j, this.client.textRenderer.getWidth(text) + (bl4 ? 9 : 0));
         int l = 0;
         MutableText text2 = null;
         int m = 0;
         if (objective != null) {
            ScoreHolder scoreHolder = ScoreHolder.fromProfile(playerListEntry.getProfile());
            ReadableScoreboardScore readableScoreboardScore = scoreboard.getScore(scoreHolder, objective);
            if (readableScoreboardScore != null) {
               l = readableScoreboardScore.getScore();
            }

            if (objective.getRenderType() != RenderType.HEARTS) {
               NumberFormat numberFormat = objective.getNumberFormatOr(StyledNumberFormat.YELLOW);
               text2 = ReadableScoreboardScore.getFormattedScore(readableScoreboardScore, numberFormat);
               m = this.client.textRenderer.getWidth(text2);
               k = Math.max(k, m > 0 ? i + m : 0);
            }
         }

         list2.add(new ScoreDisplayEntry(text, l, text2, m));
      }

      if (!this.hearts.isEmpty()) {
         Set<UUID> set = list.stream().map(playerEntry -> playerEntry.getProfile().getId()).collect(Collectors.toSet());
         this.hearts.keySet().removeIf(uuid -> !set.contains(uuid));
      }

      int n = list.size();
      int o = n;

      int p;
      for (p = 1; o > 20; o = (n + p - 1) / p) {
         p++;
      }

      boolean bl = this.client.isInSingleplayer() || this.client.getNetworkHandler().getConnection().isEncrypted();
      int q;
      if (objective != null) {
         if (objective.getRenderType() == RenderType.HEARTS) {
            q = 90;
         } else {
            q = k;
         }
      } else {
         q = 0;
      }

      int m = Math.min(p * ((bl ? 9 : 0) + j + q + 13), scaledWindowWidth - 50) / p;
      int r = scaledWindowWidth / 2 - (m * p + (p - 1) * 5) / 2;
      int s = 10;
      int t = m * p + (p - 1) * 5;
      List<OrderedText> list3 = null;
      if (this.header != null) {
         list3 = this.client.textRenderer.wrapLines(this.header, scaledWindowWidth - 50);

         for (OrderedText orderedText : list3) {
            t = Math.max(t, this.client.textRenderer.getWidth(orderedText));
         }
      }

      List<OrderedText> list4 = null;
      if (this.footer != null) {
         list4 = this.client.textRenderer.wrapLines(this.footer, scaledWindowWidth - 50);

         for (OrderedText orderedText2 : list4) {
            t = Math.max(t, this.client.textRenderer.getWidth(orderedText2));
         }
      }

      Color color = new Color(0, 0, 0, 100);
      if (list3 != null) {
         int var10001 = scaledWindowWidth / 2 - t / 2 - 1;
         int var10002 = s - 1;
         int var10003 = scaledWindowWidth / 2 + t / 2 + 1;
         int var10005 = list3.size();
         Objects.requireNonNull(this.client.textRenderer);
         if (custom) {
            float x1 = scaledWindowWidth / 2 - t / 2 - 1;
            float y1 = s - 1;
            float x2 = scaledWindowWidth / 2 + t / 2 + 1;
            float y2 = s + list3.size() * 9;
            RenderUtils.handleMatrixStack38(context.getMatrices(), color, x1, y1, x2, y2, 6.0F, 6.0F, 0.0F, 0.0F, 30.0F);
         } else {
            context.fill(var10001, var10002, var10003, s + var10005 * 9, Integer.MIN_VALUE);
         }

         for (OrderedText orderedText2 : list3) {
            int u = this.client.textRenderer.getWidth(orderedText2);
            context.drawTextWithShadow(this.client.textRenderer, orderedText2, scaledWindowWidth / 2 - u / 2, s, -1);
            Objects.requireNonNull(this.client.textRenderer);
            s += 9;
         }

         s++;
      }

      if (custom) {
         float x1 = scaledWindowWidth / 2 - t / 2 - 1;
         float y1 = s - 1;
         float x2 = scaledWindowWidth / 2 + t / 2 + 1;
         float y2 = s + o * 9;
         RenderUtils.handleMatrixStack38(
            context.getMatrices(),
            color,
            x1,
            y1,
            x2,
            y2,
            list3 != null ? 0.0F : 6.0F,
            list3 != null ? 0.0F : 6.0F,
            list4 != null ? 0.0F : 6.0F,
            list4 != null ? 0.0F : 6.0F,
            30.0F
         );
      } else {
         context.fill(scaledWindowWidth / 2 - t / 2 - 1, s - 1, scaledWindowWidth / 2 + t / 2 + 1, s + o * 9, Integer.MIN_VALUE);
      }

      int v = this.client.options.getTextBackgroundColor(553648127);

      for (int w = 0; w < n; w++) {
         int u = w / o;
         int x = w % o;
         int y = r + u * m + u * 5;
         int z = s + x * 9;
         context.fill(y, z, y + m, z + 8, v);
         RenderSystem.enableBlend();
         if (w < list.size()) {
            PlayerListEntry playerListEntry2 = list.get(w);
            ScoreDisplayEntry scoreDisplayEntry = list2.get(w);
            GameProfile gameProfile = playerListEntry2.getProfile();
            boolean bl5 = ExtraTabModule.getExtraTabModule().getTelemetryReplace().getValue();
            boolean bl4 = ExtraTabModule.getExtraTabModule().check7()
               && ExtraTabModule.getExtraTabModule().getTelemetry().getValue()
               && playerListEntry2 instanceof IPlayerListEntry access
               && access.getUser() != null;
            if (bl) {
               PlayerEntity playerEntity = this.client.world.getPlayerByUuid(gameProfile.getId());
               boolean bl2 = playerEntity != null && LivingEntityRenderer.shouldFlipUpsideDown(playerEntity);
               boolean bl3 = playerEntity != null && playerEntity.isPartVisible(PlayerModelPart.HAT);
               PlayerSkinDrawer.draw(context, playerListEntry2.getSkinTextures().texture(), y, z, 8, bl3, bl2);
               y += 9;
            }

            IPlayerListEntry access = (IPlayerListEntry)(Object)playerListEntry2;
            if (bl4) {
               UserTagManager.TelemetryUser user = access.getUser();
               RenderSystem.setShaderTexture(0, user.getIdentifier());
               RenderUtils.handleMatrixStack2(context.getMatrices(), y, z, 8.0);
               y += 9;
            }

            context.drawTextWithShadow(
               this.client.textRenderer,
               ExtraTabModule.getExtraTabModule().describeText(scoreDisplayEntry.name, access.getUser()),
               y,
               z,
               playerListEntry2.getGameMode() == GameMode.SPECTATOR ? -1862270977 : -1
            );
            if (objective != null && playerListEntry2.getGameMode() != GameMode.SPECTATOR) {
               int aa = y + j + 1;
               int ab = aa + q - (bl4 ? 9 : 0);
               if (ab - aa > 5) {
                  this.renderScoreboardObjective(objective, z, scoreDisplayEntry, aa, ab, gameProfile.getId(), context);
               }
            }

            this.renderLatencyIcon(context, m, y - (bl && bl4 ? 18 : (!bl && !bl4 ? 0 : 9)), z, playerListEntry2);
         }
      }

      if (list4 != null) {
         s += o * 9 + 1;
         int var10001 = scaledWindowWidth / 2 - t / 2 - 1;
         int var10002 = s - 1;
         int var10003 = scaledWindowWidth / 2 + t / 2 + 1;
         int var10005 = list4.size();
         Objects.requireNonNull(this.client.textRenderer);
         if (custom) {
            float x1 = scaledWindowWidth / 2 - t / 2 - 1;
            float y1 = s - 1;
            float x2 = scaledWindowWidth / 2 + t / 2 + 1;
            float y2 = s + var10005 * 9;
            RenderUtils.handleMatrixStack38(context.getMatrices(), color, x1, y1, x2, y2, 0.0F, 0.0F, 6.0F, 6.0F, 30.0F);
         } else {
            context.fill(var10001, var10002, var10003, s + var10005 * 9, Integer.MIN_VALUE);
         }

         for (OrderedText orderedText3 : list4) {
            int x = this.client.textRenderer.getWidth(orderedText3);
            context.drawTextWithShadow(this.client.textRenderer, orderedText3, scaledWindowWidth / 2 - x / 2, s, -1);
            Objects.requireNonNull(this.client.textRenderer);
            s += 9;
         }
      }

      if (custom) {
         context.getMatrices().pop();
      }
   }

   @Shadow
   protected abstract List<PlayerListEntry> collectPlayerEntries();

   @Shadow
   public abstract Text getPlayerName(PlayerListEntry var1);

   @Shadow
   protected abstract void renderLatencyIcon(DrawContext var1, int var2, int var3, int var4, PlayerListEntry var5);

   @Shadow
   protected abstract void renderScoreboardObjective(
      ScoreboardObjective var1, int var2, ScoreDisplayEntry var3, int var4, int var5, UUID var6, DrawContext var7
   );
}
