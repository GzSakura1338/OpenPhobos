package org.phobos.core.hud.element;

import java.awt.Color;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.earthhack.ducks.IAbstractClientPlayerEntity;
import org.phobos.module.combat.AntiBotModule;
import org.phobos.module.client.IRCModule;
import org.phobos.module.client.IRCModule;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.config.ConfigManager;
import org.phobos.core.hud.HudElement;
import org.phobos.core.render.texture.ImageTexture;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.render.Render3d;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.setting.Setting;
import org.phobos.core.animation.TimedFloatAnimator;
import org.phobos.core.tick.TimerManager;
import org.phobos.core.user.UserTagManager;

public class ArrowsHudElement extends HudElement {
   public final Setting<Double> playersRadius;
   public static final ImageTexture imageTexture = ImageTexture.resolveImageTexture2("phoboslite", "textures/ui/arrow.png", true);
   public final Setting<Float> pingsSize;
   public final Setting<Boolean> players;
   public final Setting<Color> sharedColor;
   public final TimedFloatAnimator timedFloatAnimator;
   public final Setting<Boolean> visible = this.resolveSetting2(
      new BooleanSetting("Visible", true).resolveSetting7("If off, only renders arrows for players off screen")
   );
   public final Setting<Color> playersColor;
   public final Setting<Color> playersFriends;
   public final Setting<Color> playersEnemies;
   public final Setting<Float> sharedSize;
   public final Setting<Double> sharedRadius;
   public final Setting<Boolean> shared;
   public final Setting<Boolean> pings;
   public final Setting<Float> playersSize;
   public final Setting<Double> pingsRadius;
   public final Setting<Color> pingsColor;

   public float computePlayerEntity(PlayerEntity playerEntity) {
      if (playerEntity instanceof IAbstractClientPlayerEntity iAbstractClientPlayerEntity) {
         iAbstractClientPlayerEntity.getEntityData().getFloatAnimator().handleFloat3(10.0F, 0.1F);
         return iAbstractClientPlayerEntity.getEntityData().getFloatAnimator().getFloatValue2();
      } else {
         return 0.0F;
      }
   }

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      ClientPlayerEntity clientPlayerEntity = mc.player;
      ClientWorld clientWorld = mc.world;
      if (clientPlayerEntity != null && clientWorld != null) {
         MatrixStack matrixStack = drawContext.getMatrices();
         Vec3d vec3d2 = mc.gameRenderer.getCamera().getPos();
         float f9 = TimerManager.getFloatValue();
         this.timedFloatAnimator.setFloatValue(clientPlayerEntity.getYaw());
         this.timedFloatAnimator.handleFloat3(10.0F, 0.2F);
         float f10 = this.timedFloatAnimator.getFloatValue2();
         double d38 = Math.cos(f10 * (Math.PI / 180.0));
         double d39 = Math.sin(f10 * (Math.PI / 180.0));
         if (this.players.getValue()) {
            for (PlayerEntity playerEntity : clientWorld.getPlayers()) {
               if (!playerEntity.isMainPlayer()) {
                  boolean flag2;
                  if (!AntiBotModule.testEntity2(playerEntity) && playerEntity.isAlive() && (this.visible.getValue() || !Render3d.testEntity(playerEntity))) {
                     flag2 = false;
                  } else {
                     this.handlePlayerEntity(playerEntity, 0.0F);
                     flag2 = true;
                  }

                  double d = (playerEntity.getX() + (playerEntity.getX() - playerEntity.lastRenderX) * f9 - vec3d2.getX()) * 0.01;
                  double d2 = (playerEntity.getZ() + (playerEntity.getZ() - playerEntity.lastRenderZ) * f9 - vec3d2.getZ()) * 0.01;
                  double d3 = -(d2 * d38 - d * d39);
                  double d4 = -(d * d38 + d2 * d39);
                  double d5 = -d3;
                  double d6 = -d4;
                  double d7 = 43.0 * this.playersRadius.getValue();
                  double d8 = d7 * 2.0 + d7;
                  double d9 = mc.getWindow().getScaledWidth() / 2.0F - d7 - d7 / 2.0;
                  double d10 = mc.getWindow().getScaledHeight() / 2.0F - d7 - d7 / 2.0;
                  double d11 = Math.atan2(d3, d4) * 180.0 / Math.PI;
                  double d12 = d8 / 2.0 * Math.cos(Math.toRadians(d11)) + d9 + d8 / 2.0;
                  double d13 = d8 / 2.0 * Math.sin(Math.toRadians(d11)) + d10 + d8 / 2.0;
                  int i3 = (int)clientPlayerEntity.getPos().distanceTo(playerEntity.getPos());
                  if (!flag2 && Math.sqrt(d6 * d6 + d5 * d5) < d8 / 2.0 && i3 > 0) {
                     this.handlePlayerEntity(playerEntity, 1.0F);
                  }

                  float f = this.computePlayerEntity(playerEntity);
                  if (!(f <= 0.05F)) {
                     matrixStack.push();
                     matrixStack.translate(d12, d13, 0.0);
                     float f2 = this.playersSize.getValue();
                     matrixStack.scale(f2, f2, f2);
                     matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)d11));
                     this.handleMatrixStack(matrixStack, this.resolveColor(playerEntity.getUuid(), f));
                     matrixStack.pop();
                  }
               }
            }
         }

         if (this.pings.getValue() && IRCModule.check()) {
            for (IRCModule.Beam beam : IRCModule.getIRCModule().getBeamList()) {
               Vec3d vec3d = beam.getVec3d();
               boolean flag4;
               if (!beam.isFlag2() && (this.visible.getValue() || !Render3d.testBox(beam.createBox()))) {
                  flag4 = false;
               } else {
                  this.handleBeam(beam, 0.0F);
                  flag4 = true;
               }

               double d14 = (vec3d.x - vec3d2.getX()) * 0.01;
               double d15 = (vec3d.z - vec3d2.getZ()) * 0.01;
               double d16 = -(d15 * d38 - d14 * d39);
               double d17 = -(d14 * d38 + d15 * d39);
               double d18 = -d16;
               double d19 = -d17;
               double d20 = 43.0 * this.pingsRadius.getValue();
               double d21 = d20 * 2.0 + d20;
               double d22 = mc.getWindow().getScaledWidth() / 2.0F - d20 - d20 / 2.0;
               double d23 = mc.getWindow().getScaledHeight() / 2.0F - d20 - d20 / 2.0;
               double d24 = Math.atan2(d16, d17) * 180.0 / Math.PI;
               double d25 = d21 / 2.0 * Math.cos(Math.toRadians(d24)) + d22 + d21 / 2.0;
               double d26 = d21 / 2.0 * Math.sin(Math.toRadians(d24)) + d23 + d21 / 2.0;
               int i4 = (int)clientPlayerEntity.getPos().distanceTo(vec3d);
               if (!flag4 && Math.sqrt(d19 * d19 + d18 * d18) < d21 / 2.0 && i4 > 0) {
                  this.handleBeam(beam, 1.0F);
               }

               float f3 = this.computeBeam(beam);
               if (!(f3 <= 0.05F)) {
                  matrixStack.push();
                  matrixStack.translate(d25, d26, 0.0);
                  float f4 = this.pingsSize.getValue();
                  matrixStack.scale(f4, f4, f4);
                  matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)d24));
                  this.handleMatrixStack(matrixStack, this.pingsColor.getValue());
                  matrixStack.pop();
               }
            }
         }

         if (this.shared.getValue() && ConfigManager.getCloudSettings().shared.getValue()) {
            for (Map.Entry<UserTagManager.SharedESP, Long> entry : this.phobosClient.getUserTagManager().getSharedESPMap().entrySet()) {
               UserTagManager.SharedESP sharedESP = entry.getKey();
               double d27 = (sharedESP.x() - vec3d2.getX()) * 0.01;
               double d28 = (sharedESP.z() - vec3d2.getZ()) * 0.01;
               double d29 = -(d28 * d38 - d27 * d39);
               double d30 = -(d27 * d38 + d28 * d39);
               double d31 = 43.0 * this.sharedRadius.getValue();
               double d32 = d31 * 2.0 + d31;
               double d33 = mc.getWindow().getScaledWidth() / 2.0F - d31 - d31 / 2.0;
               double d34 = mc.getWindow().getScaledHeight() / 2.0F - d31 - d31 / 2.0;
               double d35 = Math.atan2(d29, d30) * 180.0 / Math.PI;
               double d36 = d32 / 2.0 * Math.cos(Math.toRadians(d35)) + d33 + d32 / 2.0;
               double d37 = d32 / 2.0 * Math.sin(Math.toRadians(d35)) + d34 + d32 / 2.0;
               float f5 = this.sharedColor.getValue().getAlpha();
               float f6 = f5 / 3000.0F;
               float f7 = MathHelper.clamp((int)(f6 * (float)(entry.getValue() + 3000L - System.currentTimeMillis())), 0, (int)f5) / 255.0F;
               if (!(f7 <= 0.05F)) {
                  matrixStack.push();
                  matrixStack.translate(d36, d37, 0.0);
                  float f8 = this.sharedSize.getValue();
                  matrixStack.scale(f8, f8, f8);
                  matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)d35));
                  this.handleMatrixStack(matrixStack, ColorUtils.createColor6(this.sharedColor.getValue(), f7));
                  matrixStack.pop();
               }
            }
         }
      }
   }

   public Color resolveColor(UUID uUID, float f) {
      return ColorUtils.createColor6(
         ColorUtils.resolveColor2(this.phobosClient, uUID, this.playersColor.getValue(), this.playersFriends.getValue(), this.playersEnemies.getValue()), f
      );
   }

   public ArrowsHudElement() {
      super("Arrows", 0.0F, 0.0F, 0.0F, 0.0F);
      this.players = this.resolveSetting2(new BooleanSetting("Players"));
      this.playersRadius = this.resolveSetting2(new NumberSetting<>("PlayersRadius", 1.0, 0.3, 2.0));
      this.playersSize = this.resolveSetting2(new NumberSetting<>("PlayersSize", 1.0F, 0.5F, 1.5F));
      this.playersColor = this.resolveSetting2(new ColorSetting("PlayersColor", Color.WHITE).getColorSetting());
      this.playersFriends = this.resolveSetting2(new ColorSetting("PlayersFriends", Color.CYAN));
      this.playersEnemies = this.resolveSetting2(new ColorSetting("PlayersEnemies", Color.RED));
      this.pings = this.resolveSetting2(new BooleanSetting("Pings"));
      this.pingsRadius = this.resolveSetting2(new NumberSetting<>("PingsRadius", 1.0, 0.3, 2.0));
      this.pingsSize = this.resolveSetting2(new NumberSetting<>("PingsSize", 1.0F, 0.5F, 1.5F));
      this.pingsColor = this.resolveSetting2(new ColorSetting("PingsColor", Color.WHITE).resolveColorSetting(colorSetting -> colorSetting.setFlag(true)));
      this.shared = this.resolveSetting2(new BooleanSetting("Shared"));
      this.sharedRadius = this.resolveSetting2(new NumberSetting<>("SharedRadius", 1.0, 0.3, 2.0));
      this.sharedSize = this.resolveSetting2(new NumberSetting<>("SharedSize", 1.0F, 0.5F, 1.5F));
      this.sharedColor = this.resolveSetting2(new ColorSetting("SharedColor", Color.GRAY));
      this.timedFloatAnimator = new TimedFloatAnimator();
      this.resolveSettingGroup(this.players, this.playersRadius, this.playersSize, this.playersColor, this.playersFriends, this.playersEnemies);
      this.resolveSettingGroup(this.pings, this.pingsRadius, this.pingsSize, this.pingsColor).resolveSettingGroup5(IRCModule::check);
      this.resolveSettingGroup(this.shared, this.sharedRadius, this.sharedSize, this.sharedColor);
   }

   public void handleBeam(IRCModule.Beam beam, float f) {
      beam.getFloatAnimator().setFloatValue(f);
   }

   public void handlePlayerEntity(PlayerEntity playerEntity, float f) {
      if (playerEntity instanceof IAbstractClientPlayerEntity iAbstractClientPlayerEntity) {
         iAbstractClientPlayerEntity.getEntityData().getFloatAnimator().setFloatValue(f);
      }
   }

   public float computeBeam(IRCModule.Beam beam) {
      beam.getFloatAnimator().handleFloat3(10.0F, 0.1F);
      return beam.getFloatAnimator().getFloatValue2();
   }

   public void handleMatrixStack(MatrixStack matrixStack, Color color) {
      float f = 18.0F;
      RenderUtils.run3();
      imageTexture.run();
      RenderUtils.handleMatrixStack4(matrixStack, color, -f / 2.0F, -f / 2.0F, f);
      RenderUtils.run();
   }
}
