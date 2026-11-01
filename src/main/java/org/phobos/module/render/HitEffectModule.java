package org.phobos.module.render;

import com.mojang.authlib.GameProfile;
import java.awt.Color;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.render.RenderContext;
import org.phobos.core.render.PlayerModelCopy;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.rotation.RotationManager.Rotation;
import org.phobos.event.AttackEvent2;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.mixin.game.entity.ILivingEntity;
import org.phobos.mixin.game.render.interfaces.ILimbAnimator;
import org.phobos.module.Module;

public class HitEffectModule extends Module {
   public final Setting<Color> trail;
   public final Setting<Boolean> dissolve;
   public final Setting<Long> dissolveTime;
   public final Setting<Float> width;
   public final Setting<Color> color = this.resolveSetting2(new ColorSetting("Color", ColorUtils.createColor6(Color.WHITE, 0.2F)).getColorSetting());
   public final Setting<Color> linesColor;
   public final Setting<Long> fade;
   public final Setting<Boolean> gradient;
   public final CopyOnWriteArrayList<PlayerModelCopy> playerModelCopyCopyOnWriteArrayList;
   public final Setting<Color> lead;
   public final Setting<Float> factor;
   public final Setting<Boolean> lines = this.resolveSetting2(new BooleanSetting("Lines"));

   public HitEffectModule(PhobosClient phobosClient) {
      super(phobosClient, "HitEffect", Categories.render);
      this.linesColor = this.resolveSetting2(new ColorSetting("LinesColor", Color.WHITE).getColorSetting());
      this.width = this.resolveSetting2(new NumberSetting<>("Width", 1.0F, 0.0F, 3.0F));
      this.fade = this.resolveSetting2(new LongNumberSetting("Fade", 500L, 0L, 5000L));
      this.factor = this.resolveSetting2(new NumberSetting<>("Factor", 1.0F, 0.1F, 3.0F));
      this.dissolve = this.resolveSetting2(new BooleanSetting("Dissolve", false));
      this.lead = this.resolveSetting2(new ColorSetting("Lead", new Color(0.95F, 0.35F, 0.01F, 1.0F)));
      this.trail = this.resolveSetting2(new ColorSetting("Trail", new Color(0.95F, 0.75F, 0.01F, 1.0F)));
      this.dissolveTime = this.resolveSetting2(new LongNumberSetting("DissolveTime", 1700L, 500L, 4000L, true));
      this.gradient = this.resolveSetting2(new BooleanSetting("Gradient"));
      this.playerModelCopyCopyOnWriteArrayList = new CopyOnWriteArrayList<>();
      this.resolveSettingGroup(this.lines, this.linesColor, this.width);
      this.resolveSettingGroup(this.dissolve, this.dissolveTime, this.lead, this.trail, this.gradient);
      this.handleListener(
         new SafeListener<AttackEvent2>() {
            public void handleObject(
               AttackEvent2 attackEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (attackEvent2.getEntity() instanceof PlayerEntity playerEntity) {
                  PlayerEntity playerEntity3 = new PlayerEntity(
                     clientWorld, BlockPos.ORIGIN, playerEntity.bodyYaw, new GameProfile(playerEntity.getUuid(), playerEntity.getName().getString())
                  ) {
                     public boolean isSpectator() {
                        return false;
                     }

                     public boolean isCreative() {
                        return false;
                     }
                  };
                  playerEntity3.copyFrom(playerEntity);
                  playerEntity3.prevYaw = playerEntity.getYaw();
                  playerEntity3.setYaw(playerEntity.getYaw());
                  playerEntity3.prevBodyYaw = playerEntity.bodyYaw;
                  playerEntity3.bodyYaw = playerEntity.bodyYaw;
                  playerEntity3.prevHeadYaw = playerEntity.headYaw;
                  playerEntity3.headYaw = playerEntity.headYaw;
                  playerEntity3.handSwingTicks = playerEntity.handSwingTicks;
                  playerEntity3.setSneaking(playerEntity.isSneaking());
                  playerEntity3.setPose(playerEntity.getPose());
                  float[] floats = RotationUtils.values2(clientPlayerEntity, playerEntity3.getEyePos());
                  Rotation rotation = new Rotation(floats[0], floats[1]);
                  playerEntity3.setVelocity(
                     rotation.getVec3d()
                        .multiply((float)HitEffectModule.this.fade.getValue().longValue() / 1000.0F)
                        .multiply(HitEffectModule.this.factor.getValue().floatValue())
                  );
                  ILivingEntity iLivingEntity = (ILivingEntity)(Object)playerEntity3;
                  iLivingEntity.setLeaningPitch(((ILivingEntity)(Object)playerEntity).getLeaningPitch());
                  iLivingEntity.setLastLeaningPitch(((ILivingEntity)(Object)playerEntity).getLeaningPitch());
                  playerEntity3.limbAnimator.setSpeed(playerEntity.limbAnimator.getSpeed());
                  ((ILimbAnimator)(Object)playerEntity3.limbAnimator).setPos(playerEntity.limbAnimator.getPos());
                  PlayerModelCopy playerModelCopy = new PlayerModelCopy(playerEntity3, mc.getRenderTickCounter().getTickDelta(false));
                  playerModelCopy.setLongValue(System.currentTimeMillis());
                  HitEffectModule.this.playerModelCopyCopyOnWriteArrayList.add(playerModelCopy);
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
               Render3d.run6();

               for (PlayerModelCopy playerModelCopy : HitEffectModule.this.playerModelCopyCopyOnWriteArrayList) {
                  Render3d.handlePlayerEntityModel4(playerModelCopy.getPlayerEntityPlayerEntityModel());
                  Render3d.handlePlayerEntity3(
                     playerModelCopy.getPlayerEntity(),
                     playerModelCopy.getPlayerEntityPlayerEntityModel(),
                     playerModelCopy.getLongValue2(),
                     HitEffectModule.this.color,
                     HitEffectModule.this.fade,
                     HitEffectModule.this.lines,
                     HitEffectModule.this.linesColor,
                     HitEffectModule.this.width,
                     playerModelCopy.getFloatValue(),
                     model -> {
                        if (HitEffectModule.this.dissolve.getValue()) {
                           model.setRunnable(
                              () -> {
                                 Render3d.positionDissolveShader
                                    .handleFloat(
                                       (float)(System.currentTimeMillis() - playerModelCopy.getLongValue())
                                          / HitEffectModule.this.dissolveTime.getValue().floatValue(),
                                       HitEffectModule.this.lead.getValue(),
                                       HitEffectModule.this.trail.getValue(),
                                       HitEffectModule.this.gradient.getValue()
                                    );
                                 Render3d.positionDissolveShader.run3();
                              }
                           );
                           model.setRunnable2(
                              () -> {
                                 Render3d.linesDissolveShader
                                    .handleFloat(
                                       (float)(System.currentTimeMillis() - playerModelCopy.getLongValue())
                                          / HitEffectModule.this.dissolveTime.getValue().floatValue(),
                                       HitEffectModule.this.lead.getValue(),
                                       HitEffectModule.this.trail.getValue(),
                                       HitEffectModule.this.gradient.getValue()
                                    );
                                 Render3d.linesDissolveShader.run3();
                              }
                           );
                        }
                     }
                  );
               }

               Render3d.run2();
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
               HitEffectModule.this.playerModelCopyCopyOnWriteArrayList
                  .forEach(
                     playerModelCopy -> {
                        playerModelCopy.handleCopyOnWriteArrayList(HitEffectModule.this.playerModelCopyCopyOnWriteArrayList, HitEffectModule.this.fade.getValue());
                        Vec3d vec3d = playerModelCopy.getPlayerEntity().getPos();
                        Vec3d vec3d2 = vec3d.add(playerModelCopy.getPlayerEntity().getVelocity());
                        playerModelCopy.getPlayerEntity()
                           .setVelocity(playerModelCopy.getPlayerEntity().getVelocity().multiply(0.77F * HitEffectModule.this.factor.getValue()));
                        playerModelCopy.getPlayerEntity().lastRenderX = vec3d.getX();
                        playerModelCopy.getPlayerEntity().lastRenderY = vec3d.getY();
                        playerModelCopy.getPlayerEntity().lastRenderZ = vec3d.getZ();
                        playerModelCopy.getPlayerEntity().setPosition(vec3d2);
                     }
                  );
            }
         }
      );
   }
}
