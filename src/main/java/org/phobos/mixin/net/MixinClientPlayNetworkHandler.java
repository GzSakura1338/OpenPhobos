package org.phobos.mixin.net;

import com.llamalad7.mixinextras.sugar.Local;
import java.time.Instant;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.encryption.NetworkEncryptionUtils.SecureRandomUtil;
import net.minecraft.network.message.LastSeenMessagesCollector;
import net.minecraft.network.message.MessageBody;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.network.message.LastSeenMessagesCollector.LastSeenMessages;
import net.minecraft.network.message.MessageChain.Packer;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import org.earthhack.ducks.IClientPlayerNetworkHandler;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.api.MinecraftHolder;
import org.phobos.event.EntityStatusEvent;
import org.phobos.event.OnEntityEquipmentUpdateEvent;
import org.phobos.event.SendChatMessageEvent;
import org.phobos.mixin.net.interfaces.IClientConnection;
import org.phobos.module.misc.SoundFixerModule;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinClientPlayNetworkHandler
   extends MixinClientCommonNetworkHandler
   implements EventSubscriber,
   MinecraftHolder,
   IClientPlayerNetworkHandler {
   @Shadow
   public Packer messagePacker;
   @Shadow
   @Final
   private Scoreboard scoreboard;
   @Shadow
   public LastSeenMessagesCollector lastSeenMessagesCollector;
   @Shadow
   private ClientWorld world;

   @Redirect(method = "onScoreboardScoreUpdate", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V", remap = false))
   private void onScoreboardScoreUpdateHook(Logger instance, String string, Object object) {
   }

   @Inject(method = "onEntityTrackerUpdate", at = @At("HEAD"), cancellable = true)
   public void onEntityPosition(EntityTrackerUpdateS2CPacket packet, CallbackInfo ci) {
      ci.cancel();

      try {
         NetworkThreadUtils.forceMainThread(packet, (ClientPlayNetworkHandler)(Object)this, mc);
         Entity entity = this.world.getEntityById(packet.id());
         if (entity != null) {
            entity.getDataTracker().writeUpdatedEntries(packet.trackedValues());
         }
      } catch (Throwable var4) {
      }
   }

   @Shadow
   public abstract ClientConnection getConnection();

   @Redirect(
      method = "onEntityStatus",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/world/ClientWorld;playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZ)V"
      )
   )
   private void onEntityStatusHook(
      ClientWorld instance, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, boolean useDistance
   ) {
      instance.playSound(x, y, z, sound, category, volume, pitch, SoundFixerModule.check3() || useDistance);
   }

   @Inject(
      method = "onEntityStatus",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/particle/ParticleManager;addEmitter(Lnet/minecraft/entity/Entity;Lnet/minecraft/particle/ParticleEffect;I)V"
      )
   )
   private void entityStatusHook(EntityStatusS2CPacket packet, CallbackInfo ci, @Local Entity entity) {
      this.invoke(new EntityStatusEvent(entity));
   }

   @Override
   public void handlePacket(Packet<?> packet) {
      ((IClientConnection)(Object)this.getConnection()).invokeSendInternal(packet, null, true);
   }

   @Inject(method = "onEntityEquipmentUpdate", at = @At("HEAD"))
   private void onEntityEquipmentUpdateHook(EntityEquipmentUpdateS2CPacket packet, CallbackInfo ci) {
      if (mc.world != null) {
         this.invoke(new OnEntityEquipmentUpdateEvent(packet, mc.world.getEntityById(packet.getEntityId())));
      }
   }

   @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
   private void sendChatMessageHook(String message, CallbackInfo ci) {
      ci.cancel();
      SendChatMessageEvent event = this.invoke(new SendChatMessageEvent(message));
      if (!event.isCancelled()) {
         String content = event.getMessage();
         Instant instant = Instant.now();
         long l = SecureRandomUtil.nextLong();
         LastSeenMessages lastSeenMessages = this.lastSeenMessagesCollector.collect();
         MessageSignatureData messageSignatureData = this.messagePacker.pack(new MessageBody(content, instant, l, lastSeenMessages.lastSeen()));
         this.sendPacket(new ChatMessageC2SPacket(content, instant, l, messageSignatureData, lastSeenMessages.update()));
      }
   }
}
