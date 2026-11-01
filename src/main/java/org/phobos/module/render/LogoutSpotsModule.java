package org.phobos.module.render;

import java.awt.Color;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;
import org.earthhack.ducks.IAbstractClientPlayerEntity;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.render.RenderContext;
import org.phobos.core.prediction.SampleBuffer;
import org.phobos.core.player.PlayerList;
import org.phobos.core.tick.TimerManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.fakeplayer.FakePlayer;
import org.phobos.core.module.Categories;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.prediction.SampleBuffer.Sample;
import org.phobos.event.RemoveEntityEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.mixin.game.entity.ILivingEntity;
import org.phobos.mixin.game.render.interfaces.ILimbAnimator;
import org.phobos.module.Module;

public class LogoutSpotsModule extends Module {
   public final Setting<Boolean> visual;
   public final Setting<Long> visualTime;
   public final Map<LogoutSpotsModule.VisualSpot, Long> visualSpotMap;
   public final Setting<Long> timeout;
   public final Setting<Color> color;
   public final Setting<Boolean> telemetry;
   public final Map<LogoutSpotsModule.LogoutSpot, Long> logoutSpotMap;
   public final Setting<Boolean> totems;
   public final Setting<Boolean> replace;
   public final Setting<Double> range;
   public final Setting<Boolean> clear;
   public final Setting<Boolean> shadow;
   public final Setting<Boolean> font;
   public final Setting<Float> scale = this.resolveSetting2(new NumberSetting<>("Scale", 1.0F, 0.5F, 2.0F));

   public LogoutSpotsModule(PhobosClient phobosClient) {
      super(phobosClient, "LogoutSpots", Categories.render);
      this.font = this.resolveSetting2(new BooleanSetting("Font"));
      this.shadow = this.resolveSetting2(new BooleanSetting("Shadow"));
      this.color = this.resolveSetting2(new ColorSetting("Color", Color.WHITE));
      this.range = this.resolveSetting2(new NumberSetting<>("Range", 300.0, 10.0, 300.0));
      this.timeout = this.resolveSetting2(new LongNumberSetting("Timeout", 10000L, 10000L, 600000L).resolveNumberSetting("None", 10000L));
      this.telemetry = this.resolveSetting2(new BooleanSetting("Telemetry", true).resolveSetting7("Displays an icon if a player is using phobos"));
      this.replace = this.resolveSetting2(new BooleanSetting("Replace").resolveSetting7("Replaces players name with their phobos name"));
      this.totems = this.resolveSetting2(new BooleanSetting("Totems"));
      this.clear = this.resolveSetting2(new BooleanSetting("Clear"));
      this.visual = this.resolveSetting2(new BooleanSetting("Visual").resolveSetting7("Shows where you've last seen a player"));
      this.visualTime = this.resolveSetting2(new LongNumberSetting("VisualTime", 60000L, 1000L, 600000L));
      this.logoutSpotMap = new ConcurrentHashMap<>();
      this.visualSpotMap = new ConcurrentHashMap<>();
      this.resolveSettingGroup2("Text", this.font, this.shadow, this.color, this.scale);
      this.resolveSettingGroup(this.visual, this.visualTime);
      this.createBuilder()
         .resolveBuilder4("Data")
         .resolveBuilder5(this.resolveSettingGroup(this.telemetry, this.replace))
         .resolveBuilder(this.totems)
         .getSettingSubsection();
      this.handleListener(
         new SafeListener<RemoveEntityEvent>() {
            public void handleObject(
               RemoveEntityEvent removeEntityEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (removeEntityEvent.getEntity() instanceof AbstractClientPlayerEntity abstractClientPlayerEntity
                  && !(abstractClientPlayerEntity instanceof FakePlayer)) {
                  SkinTextures skinTextures = abstractClientPlayerEntity.getSkinTextures();
                  if (!(clientPlayerEntity.distanceTo(abstractClientPlayerEntity) > LogoutSpotsModule.this.range.getValue())) {
                     float f = ((ILivingEntity)(Object)abstractClientPlayerEntity).getLeaningPitch();
                     float f2 = ((ILivingEntity)(Object)abstractClientPlayerEntity).getLeaningPitch();
                     float f3 = abstractClientPlayerEntity.limbAnimator.getSpeed();
                     float f4 = abstractClientPlayerEntity.limbAnimator.getPos();
                     float f5 = TimerManager.getFloatValue();
                     long l = System.currentTimeMillis();
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
                     fakePlayer.setSkinTextures(skinTextures);
                     ILivingEntity iLivingEntity = (ILivingEntity)(Object)fakePlayer;
                     iLivingEntity.setLeaningPitch(f);
                     iLivingEntity.setLastLeaningPitch(f2);
                     fakePlayer.limbAnimator.setSpeed(f3);
                     ((ILimbAnimator)(Object)fakePlayer.limbAnimator).setPos(f4);
                     int i = phobosClient.getEntityIdManager().computeEntity(abstractClientPlayerEntity);
                     String text = abstractClientPlayerEntity.getNameForScoreboard();
                     phobosClient.getScheduler()
                        .handleRunnable2(
                           () -> {
                              if (mc.getNetworkHandler() != null) {
                                 PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(abstractClientPlayerEntity.getUuid());
                                 if (playerListEntry != null) {
                                    if (!LogoutSpotsModule.this.visual.getValue()) {
                                       return;
                                    }

                                    SampleBuffer sampleBuffer = ((IAbstractClientPlayerEntity)(Object)abstractClientPlayerEntity).getHistory();
                                    sampleBuffer.handleInt2(3);
                                    PlayerList playerList = new PlayerList();

                                    for (Sample sample : sampleBuffer) {
                                       PlayerEntity playerEntity = sample.resolvePlayerEntity(fakePlayer, clientWorld);
                                       playerList.handlePlayerEntity(playerEntity);
                                    }

                                    LogoutSpotsModule.this.visualSpotMap.put(new LogoutSpotsModule.VisualSpot(fakePlayer, playerList, f5), l);
                                    return;
                                 }
                              }

                              LogoutSpotsModule.this.logoutSpotMap
                                 .put(new LogoutSpotsModule.LogoutSpot(fakePlayer, abstractClientPlayerEntity.getUuid(), text, i, f5), l);
                           },
                           2
                        );
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (mc.getNetworkHandler() != null) {
                  for (LogoutSpotsModule.LogoutSpot logoutSpot : LogoutSpotsModule.this.logoutSpotMap.keySet()) {
                     UUID uUID = logoutSpot.uuid;
                     if (mc.getNetworkHandler().getPlayerListEntry(uUID) != null) {
                        LogoutSpotsModule.this.logoutSpotMap.remove(logoutSpot);
                     }
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
               for (Entry<LogoutSpotsModule.LogoutSpot, Long> entry : LogoutSpotsModule.this.logoutSpotMap.entrySet()) {
                  LogoutSpotsModule.LogoutSpot logoutSpot = entry.getKey();
                  PlayerEntity playerEntity = logoutSpot.entity;
                  long l = entry.getValue();
                  if (!LogoutSpotsModule.this.timeout.getValue().equals(10000L) && l + LogoutSpotsModule.this.timeout.getValue() < System.currentTimeMillis()) {
                     LogoutSpotsModule.this.logoutSpotMap.remove(logoutSpot);
                  } else {
                     Render3d.handleText2(
                        LogoutSpotsModule.this.describeText(logoutSpot.name, l, logoutSpot.pops),
                        clientPlayerEntity,
                        playerEntity,
                        logoutSpot.tickDelta,
                        LogoutSpotsModule.this.scale.getValue(),
                        LogoutSpotsModule.this.color.getValue(),
                        LogoutSpotsModule.this.font.getValue(),
                        LogoutSpotsModule.this.shadow.getValue()
                     );
                     phobosClient.getEntityFadeManager().handleEntity(playerEntity, logoutSpot.tickDelta);
                  }
               }

               for (Entry<LogoutSpotsModule.VisualSpot, Long> entry2 : LogoutSpotsModule.this.visualSpotMap.entrySet()) {
                  LogoutSpotsModule.VisualSpot visualSpot = entry2.getKey();
                  PlayerEntity playerEntity3 = visualSpot.entity;
                  long l2 = entry2.getValue();
                  if (l2 + LogoutSpotsModule.this.timeout.getValue() < System.currentTimeMillis()) {
                     LogoutSpotsModule.this.visualSpotMap.remove(visualSpot);
                  } else {
                     phobosClient.getEntityFadeManager().handleEntity(playerEntity3, visualSpot.tick);

                     for (PlayerEntity playerEntity2 : visualSpot.slice) {
                        phobosClient.getEntityFadeManager().handleEntity(playerEntity2, visualSpot.tick);
                     }
                  }
               }
            }
         }
      );
      this.clear.handleConsumer2(booleanValue -> {
         if (booleanValue) {
            this.logoutSpotMap.clear();
            this.clear.setValue(false);
         }
      });
      ModuleListenerUtils.handleListenerHolder3(this, this.logoutSpotMap::clear);
   }

   public String describeLong(long l) {
      long l3 = TimeUnit.MILLISECONDS.toSeconds(l);
      if (l3 > 60L) {
         long l2 = TimeUnit.SECONDS.toMinutes(l3);
         return "%s minute%s ago".formatted(l2, l2 == 1L ? "" : "s");
      } else {
         return "%s second%s ago".formatted(l3, l3 == 1L ? "" : "s");
      }
   }

   public String describeText(String text, long l, int i) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(text).append(Formatting.RESET).append(", ");
      if (this.totems.getValue() && i != -1) {
         stringBuilder.append("-").append(i).append(", ");
      }

      stringBuilder.append(this.describeLong(System.currentTimeMillis() - l));
      return stringBuilder.toString();
   }

   private record LogoutSpot(PlayerEntity entity, UUID uuid, String name, int pops, float tickDelta) {
   }

   private record VisualSpot(PlayerEntity entity, PlayerList slice, float tick) {
   }
}
