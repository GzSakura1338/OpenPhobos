package org.phobos.core.render;

import com.mojang.authlib.GameProfile;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.SkinTextures.Model;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.phobos.api.MinecraftHolder;
import org.phobos.data.PlayerRecord;
import org.phobos.mixin.game.entity.ILivingEntity;
import org.phobos.mixin.game.render.interfaces.ILimbAnimator;
import org.phobos.core.tick.TimeUtils;

public final class PlayerModelCopy implements MinecraftHolder {
   public long longValue;
   public PlayerEntity playerEntity;
   public final float floatValue;
   public final long longValue2;
   public final PlayerEntityModel<PlayerEntity> playerEntityPlayerEntityModel;

   public void setLongValue(long l) {
      this.longValue = l;
   }

   public void handleCopyOnWriteArrayList(CopyOnWriteArrayList<PlayerModelCopy> copyOnWriteArrayList, long l) {
      if (this.longValue2 + l < System.currentTimeMillis()) {
         copyOnWriteArrayList.remove(this);
         this.remove();
      }
   }

   public PlayerEntity getPlayerEntity() {
      return this.playerEntity;
   }

   public static void handlePlayerEntity(PlayerEntity playerEntity, PlayerEntity playerEntity2) {
      playerEntity2.bodyYaw = playerEntity.bodyYaw;
      playerEntity2.headYaw = playerEntity.headYaw;
      playerEntity2.handSwingTicks = playerEntity.handSwingTicks;
      playerEntity2.handSwingProgress = playerEntity.handSwingProgress;
      playerEntity2.setSneaking(playerEntity.isSneaking());
      playerEntity2.setPose(playerEntity.getPose());
      ((ILivingEntity)(Object)playerEntity2).setLeaningPitch(((ILivingEntity)(Object)playerEntity).getLeaningPitch());
      ((ILivingEntity)(Object)playerEntity2).setLastLeaningPitch(((ILivingEntity)(Object)playerEntity).getLastLeaningPitch());
   }

   public void remove() {
      this.playerEntity.kill();
      this.playerEntity.setRemoved(RemovalReason.KILLED);
      this.playerEntity.onRemoved();
   }

   public void handlePlayerEntity2(PlayerEntity playerEntity, ClientWorld clientWorld, boolean flag) {
      this.playerEntity = resolvePlayerEntity(playerEntity, clientWorld);
      handlePlayerEntity(playerEntity, this.playerEntity);
      if (flag) {
         this.playerEntity.limbAnimator.setSpeed(playerEntity.limbAnimator.getSpeed());
         ((ILimbAnimator)(Object)this.playerEntity.limbAnimator).setPos(playerEntity.limbAnimator.getPos());
      }
   }

   public void handlePlayerRecord(PlayerRecord playerRecord, ClientWorld clientWorld, boolean flag) {
      this.playerEntity = resolvePlayerEntity(playerRecord.player(), clientWorld);
      this.playerEntity.setPosition(playerRecord.position());
      this.playerEntity.bodyYaw = playerRecord.bodyYaw();
      this.playerEntity.headYaw = playerRecord.headYaw();
      this.playerEntity.handSwingTicks = playerRecord.handSwingingTicks();
      this.playerEntity.handSwingProgress = playerRecord.handSwingingProgress();
      this.playerEntity.setSneaking(playerRecord.shift());
      this.playerEntity.setPose(playerRecord.player().getPose());
      ((ILivingEntity)(Object)this.playerEntity).setLeaningPitch(((ILivingEntity)(Object)playerRecord.player()).getLeaningPitch());
      ((ILivingEntity)(Object)this.playerEntity).setLastLeaningPitch(((ILivingEntity)(Object)playerRecord.player()).getLastLeaningPitch());
      if (flag) {
         this.playerEntity.limbAnimator.setSpeed(playerRecord.speed());
         ((ILimbAnimator)(Object)this.playerEntity.limbAnimator).setPos(playerRecord.pos());
      }
   }

   public long value() {
      return TimeUtils.computeLong(this.longValue2);
   }

   public static PlayerEntity resolvePlayerEntity(PlayerEntity playerEntity, ClientWorld clientWorld) {
      PlayerEntity playerEntity2 = new PlayerEntity(
         clientWorld, BlockPos.ORIGIN, playerEntity.bodyYaw, new GameProfile(playerEntity.getUuid(), playerEntity.getName().getString())
      ) {
         public boolean isSpectator() {
            return false;
         }

         public boolean isCreative() {
            return false;
         }
      };
      playerEntity2.copyFrom(playerEntity);
      handlePlayerEntity(playerEntity, playerEntity2);
      playerEntity2.limbAnimator.setSpeed(playerEntity.limbAnimator.getSpeed());
      ((ILimbAnimator)(Object)playerEntity2.limbAnimator).setPos(playerEntity.limbAnimator.getPos());
      return playerEntity2;
   }

   public PlayerModelCopy(PlayerEntity playerEntity, float f) {
      this.playerEntity = playerEntity;
      this.playerEntityPlayerEntityModel = playerEntity instanceof AbstractClientPlayerEntity abstractClientPlayerEntity
         ? new PlayerEntityModel(
            new Context(
                  mc.getEntityRenderDispatcher(),
                  mc.getItemRenderer(),
                  mc.getBlockRenderManager(),
                  mc.getEntityRenderDispatcher().getHeldItemRenderer(),
                  mc.getResourceManager(),
                  mc.getEntityModelLoader(),
                  mc.textRenderer
               )
               .getPart(abstractClientPlayerEntity.getSkinTextures().model().equals(Model.SLIM) ? EntityModelLayers.PLAYER_SLIM : EntityModelLayers.PLAYER),
            false
         )
         : new PlayerEntityModel(
            new Context(
                  mc.getEntityRenderDispatcher(),
                  mc.getItemRenderer(),
                  mc.getBlockRenderManager(),
                  mc.getEntityRenderDispatcher().getHeldItemRenderer(),
                  mc.getResourceManager(),
                  mc.getEntityModelLoader(),
                  mc.textRenderer
               )
               .getPart(EntityModelLayers.PLAYER),
            false
         );
      this.longValue2 = System.currentTimeMillis();
      this.floatValue = f;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public long getLongValue() {
      return this.longValue;
   }

   public void setPlayerEntity(PlayerEntity playerEntity) {
      this.playerEntity = playerEntity;
   }

   public long getLongValue2() {
      return this.longValue2;
   }

   public PlayerEntityModel<PlayerEntity> getPlayerEntityPlayerEntityModel() {
      return this.playerEntityPlayerEntityModel;
   }
}
