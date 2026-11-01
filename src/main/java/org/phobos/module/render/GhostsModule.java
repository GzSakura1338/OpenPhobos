package org.phobos.module.render;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.render.PlayerRenderState;
import org.phobos.core.render.RenderContext;
import org.phobos.core.tick.TimerManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.fakeplayer.FakePlayer;
import org.phobos.core.module.Categories;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.event.RemoveEntityEvent;
import org.phobos.event.SafeListener;
import org.phobos.mixin.game.entity.ILivingEntity;
import org.phobos.mixin.game.render.interfaces.ILimbAnimator;
import org.phobos.module.Module;

public class GhostsModule extends Module {
   public final Setting<Double> range = this.resolveSetting2(new NumberSetting<>("Range", 16.0, 3.0, 300.0));
   public final Map<PlayerRenderState, Long> playerRenderStateMap;
   public final Setting<Long> fade = this.resolveSetting2(new LongNumberSetting("Fade", 1000L, 50L, 5000L));

   public GhostsModule(PhobosClient phobosClient) {
      super(phobosClient, "Ghosts", Categories.render, "Fades out entities when they disappear");
      this.playerRenderStateMap = new ConcurrentHashMap<>();
      this.handleListener(
         new SafeListener<RemoveEntityEvent>() {
            public void handleObject(
               RemoveEntityEvent removeEntityEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (removeEntityEvent.getEntity() instanceof AbstractClientPlayerEntity abstractClientPlayerEntity) {
                  SkinTextures var14 = abstractClientPlayerEntity.getSkinTextures();
                  if (!(clientPlayerEntity.distanceTo(abstractClientPlayerEntity) > GhostsModule.this.range.getValue())) {
                     float f = ((ILivingEntity)(Object)abstractClientPlayerEntity).getLeaningPitch();
                     float f2 = ((ILivingEntity)(Object)abstractClientPlayerEntity).getLeaningPitch();
                     float f3 = abstractClientPlayerEntity.limbAnimator.getSpeed();
                     float f4 = abstractClientPlayerEntity.limbAnimator.getPos();
                     FakePlayer fakePlayer = new FakePlayer(clientWorld, abstractClientPlayerEntity.getNameForScoreboard());
                     fakePlayer.copyFrom(abstractClientPlayerEntity);
                     fakePlayer.lastRenderX = abstractClientPlayerEntity.lastRenderX;
                     fakePlayer.lastRenderY = abstractClientPlayerEntity.lastRenderY;
                     fakePlayer.lastRenderZ = abstractClientPlayerEntity.lastRenderZ;
                     fakePlayer.prevX = abstractClientPlayerEntity.getX();
                     fakePlayer.prevY = abstractClientPlayerEntity.getY();
                     fakePlayer.prevZ = abstractClientPlayerEntity.getZ();
                     fakePlayer.prevYaw = abstractClientPlayerEntity.getYaw();
                     fakePlayer.setYaw(abstractClientPlayerEntity.getYaw());
                     fakePlayer.prevBodyYaw = abstractClientPlayerEntity.bodyYaw;
                     fakePlayer.bodyYaw = abstractClientPlayerEntity.bodyYaw;
                     fakePlayer.prevHeadYaw = abstractClientPlayerEntity.headYaw;
                     fakePlayer.headYaw = abstractClientPlayerEntity.headYaw;
                     fakePlayer.handSwingTicks = abstractClientPlayerEntity.handSwingTicks;
                     fakePlayer.setSneaking(abstractClientPlayerEntity.isSneaking());
                     fakePlayer.setPose(abstractClientPlayerEntity.getPose());
                     fakePlayer.setSkinTextures(var14);
                     ILivingEntity iLivingEntity = (ILivingEntity)(Object)fakePlayer;
                     iLivingEntity.setLeaningPitch(f);
                     iLivingEntity.setLastLeaningPitch(f2);
                     fakePlayer.limbAnimator.setSpeed(f3);
                     ((ILimbAnimator)(Object)fakePlayer.limbAnimator).setPos(f4);
                     PlayerRenderState playerRenderState = new PlayerRenderState(fakePlayer);
                     playerRenderState.setLongValue(GhostsModule.this.fade.getValue());
                     playerRenderState.setFloatValue(TimerManager.getFloatValue());
                     org.earthhack.ducks.ILivingEntity.cast(fakePlayer).setGhostModel(playerRenderState);
                     GhostsModule.this.playerRenderStateMap.put(playerRenderState, System.currentTimeMillis());
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<RenderContext>() {
            public void handleObject(
               RenderContext renderContext,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               for (Entry<PlayerRenderState, Long> entry : GhostsModule.this.playerRenderStateMap.entrySet()) {
                  PlayerRenderState playerRenderState = entry.getKey();
                  long l = entry.getValue();
                  long l2 = playerRenderState.getLongValue();
                  if (l + l2 < System.currentTimeMillis()) {
                     GhostsModule.this.playerRenderStateMap.remove(playerRenderState);
                  } else {
                     playerRenderState.setFloatValue2(255.0F / (float)l2 * (float)(l + l2 - System.currentTimeMillis()) / 255.0F);
                     phobosClient.getEntityFadeManager().handleEntity(playerRenderState.getPlayerEntity(), playerRenderState.getFloatValue());
                  }
               }
            }
         }
      );
      ModuleListenerUtils.handleListenerHolder4(this, this.playerRenderStateMap::clear);
   }
}
