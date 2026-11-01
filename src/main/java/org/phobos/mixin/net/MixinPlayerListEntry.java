package org.phobos.mixin.net;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.SkinTextures.Model;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.earthhack.ducks.IPlayerListEntry;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.user.UserSession;
import org.phobos.core.player.SkinManager;
import org.phobos.core.player.SkinManager;
import org.phobos.core.user.UserTagManager;
import org.phobos.core.user.UserTagManager;
import org.phobos.core.config.ConfigManager;
import org.phobos.core.ClientHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListEntry.class)
public abstract class MixinPlayerListEntry implements EventSubscriber, MinecraftHolder, IPlayerListEntry {
   @Unique
   private Text name;
   @Unique
   private Identifier cape;
   @Unique
   private UserTagManager.TelemetryUser content;
   @Unique
   private Model skinModel;
   @Shadow
   @Final
   private GameProfile profile;
   @Unique
   private Text user;
   @Unique
   private Identifier skin;
   @Unique
   private String skinUrl;

   @Override
   public Text getFormattedPlayerName() {
      return this.user;
   }

   @Inject(method = "<init>", at = @At("TAIL"))
   private void onInit(GameProfile profile, boolean secureChatEnforced, CallbackInfo ci) {
      if (this.cape == null) {
         Identifier cape = SkinManager.resolveIdentifier(profile.getName());
         if (cape != null) {
            this.cape = cape;
         }
      }

      if (this.skin == null) {
         SkinManager.SkinBase skin = SkinManager.resolveSkinBase(profile.getName());
         if (skin != null) {
            this.skin = skin.getIdentifier();
            this.skinUrl = skin.getText();
            this.skinModel = skin.getModel();
         }
      }

      this.setSelf(profile);
      UserTagManager manager = ClientHolder.getPhobosClient().getUserTagManager();
      UserTagManager.TelemetryUser user = manager.resolveTelemetryUser(profile.getName());
      if (user != null) {
         this.setUser(user);
      }
   }

   @Unique
   private void setSelf(GameProfile profile) {
      if (mc.getNetworkHandler() != null && mc.player != null && mc.player.getNameForScoreboard().equals(profile.getName())) {
         this.setUser(
            new UserTagManager.TelemetryUser(UserSession.userSession.getUserProfile().getPermission().getFormatting(), "Sydney", profile.getName())
         );
      }
   }

   @Override
   public void setCape(Identifier identifier) {
      SkinManager.handleText3(this.profile.getName(), identifier);
      this.cape = identifier;
   }

   @Override
   public void setSkin(Identifier identifier, String url, Model model) {
      SkinManager.handleText(this.profile.getName(), identifier, url, model);
      this.skin = identifier;
      this.skinUrl = url;
      this.skinModel = model;
   }

   @Override
   public UserTagManager.TelemetryUser getUser() {
      return this.content;
   }

   @Override
   public void setUser(UserTagManager.TelemetryUser user) {
      this.content = user;
      this.user = Text.literal(user.getText3());
      this.name = Text.literal(user.getText());
   }

   @Inject(method = "getSkinTextures", at = @At("TAIL"), cancellable = true)
   private void getSkinTexturesHook(CallbackInfoReturnable<SkinTextures> cir) {
      SkinTextures original = (SkinTextures)cir.getReturnValue();
      Identifier cape = this.cape != null && ConfigManager.getCloudSettings().capes.getValue() ? this.cape : original.capeTexture();
      SkinTextures replacement = new SkinTextures(
         this.skin != null && ConfigManager.getCloudSettings().skins.getValue() ? this.skin : original.texture(),
         this.skinUrl != null && ConfigManager.getCloudSettings().skins.getValue() ? this.skinUrl : original.textureUrl(),
         cape,
         cape,
         this.skinModel != null ? this.skinModel : original.model(),
         original.secure()
      );
      cir.setReturnValue(replacement);
   }

   @Shadow
   public abstract GameProfile getProfile();

   @Override
   public Text getFormattedUsername() {
      return this.name;
   }
}
