package org.phobos.mixin.net;

import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket.InteractTypeHandler;
import org.earthhack.ducks.IPlayerInteractEntityC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerInteractEntityC2SPacket.class)
public abstract class MixinPlayerInteractEntityC2SPacket implements IPlayerInteractEntityC2SPacket {
   @Unique
   private Entity entity;
   @Shadow
   @Final
   private int entityId;
   @Unique
   private boolean attack;
   @Shadow
   @Final
   private InteractTypeHandler type;

   @Override
   public boolean attack() {
      return this.attack;
   }

   @Override
   public void setEntity(Entity entity) {
      this.entity = entity;
   }

   @Override
   public void setAttack(boolean attack) {
      this.attack = attack;
   }

   @Override
   public int getEntityId() {
      return this.entityId;
   }

   @Override
   public Entity entity() {
      return this.entity;
   }

   @Inject(method = "attack", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
   private static void attackPacket(Entity entity, boolean playerSneaking, CallbackInfoReturnable<PlayerInteractEntityC2SPacket> cir) {
      PlayerInteractEntityC2SPacket packet = (PlayerInteractEntityC2SPacket)cir.getReturnValue();
      if (packet != null) {
         ((IPlayerInteractEntityC2SPacket)(Object)packet).setAttack(true);
         ((IPlayerInteractEntityC2SPacket)(Object)packet).setEntity(entity);
      }
   }

   @Override
   public InteractTypeHandler getType() {
      return this.type;
   }
}
