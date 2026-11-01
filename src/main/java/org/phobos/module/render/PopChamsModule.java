package org.phobos.module.render;

import com.mojang.authlib.GameProfile;
import java.awt.Color;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LimbAnimator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.TimeGate.Normal;
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
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.render.Render3d;
import org.phobos.event.EntityStatusEvent;
import org.phobos.event.Listener;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.mixin.game.entity.ILivingEntity;
import org.phobos.mixin.game.render.interfaces.ILimbAnimator;
import org.phobos.module.Module;

public class PopChamsModule extends Module {
   public Setting<Long> longValueSetting;
   public Setting<Boolean> booleanValueSetting;
   public Setting<Color> colorSetting;
   public Setting<Float> floatValueSetting;
   public Setting<Color> colorSetting2;
   public Normal normal;
   public Setting<Boolean> booleanValueSetting2;
   public Setting<Color> colorSetting3;
   public Setting<Long> longValueSetting2;
   public Setting<Color> colorSetting4;
   public Setting<Boolean> booleanValueSetting3;
   public Setting<Boolean> booleanValueSetting4;
   public Setting<Float> floatValueSetting2;
   public CopyOnWriteArrayList<PlayerModelCopy> playerModelCopyCopyOnWriteArrayList;
   public Setting<Boolean> booleanValueSetting5;
   public Setting<Boolean> booleanValueSetting6;
   private static Object[] phobos$g1806f69b8;

   public void run5() {
      Setting var1 = this.booleanValueSetting;
      Setting[] var2 = new Setting[2];
      Setting var3 = this.colorSetting2;
      var2[0] = var3;
      Setting var4 = this.floatValueSetting;
      var2[1] = var4;
      this.resolveSettingGroup(var1, var2);
      Setting var5 = this.booleanValueSetting5;
      Setting[] var6 = new Setting[1];
      Setting var7 = this.floatValueSetting2;
      var6[0] = var7;
      this.resolveSettingGroup(var5, var6);
      Setting var8 = this.booleanValueSetting6;
      Setting[] var9 = new Setting[4];
      Setting var10 = this.longValueSetting2;
      var9[0] = var10;
      Setting var11 = this.colorSetting3;
      var9[1] = var11;
      Setting var12 = this.colorSetting4;
      var9[2] = var12;
      Setting var13 = this.booleanValueSetting3;
      var9[3] = var13;
      this.resolveSettingGroup(var8, var9);
   }

   public void handleClientWorld(ClientWorld clientWorld, PlayerEntity playerEntity) {
      Normal var3 = this.normal;
      var3.run();
      BlockPos var5 = BlockPos.ORIGIN;
      float var6 = playerEntity.bodyYaw;
      UUID var8 = playerEntity.getUuid();
      Text var9 = playerEntity.getName();
      String var10 = var9.getString();
      GameProfile var7 = new GameProfile(var8, var10);
      var var4 = new PlayerEntity(clientWorld, var5, (float)Float.intBitsToFloat(Float.floatToRawIntBits(var6)), var7) {
         @Override
         public boolean isCreative() {
            return false;
         }

         @Override
         public boolean isSpectator() {
            return false;
         }
      };
      var4.copyFrom(playerEntity);
      float var11 = playerEntity.getYaw();
      var4.prevYaw = var11;
      float var12 = playerEntity.getYaw();
      var4.setYaw((float)Float.intBitsToFloat(Float.floatToRawIntBits(var12)));
      float var13 = playerEntity.bodyYaw;
      var4.prevBodyYaw = var13;
      float var14 = playerEntity.bodyYaw;
      var4.bodyYaw = var14;
      float var15 = playerEntity.headYaw;
      var4.prevHeadYaw = var15;
      float var16 = playerEntity.headYaw;
      var4.headYaw = var16;
      int var17 = playerEntity.handSwingTicks;
      var4.handSwingTicks = var17;
      boolean var18 = playerEntity.isSneaking();
      var4.setSneaking(var18);
      EntityPose var19 = playerEntity.getPose();
      var4.setPose(var19);
      boolean var20 = var4 instanceof ILivingEntity;
      if (var4 != null && !var20) {
         throw new ClassCastException("CHECKCAST failed");
      }

      boolean var21 = playerEntity instanceof ILivingEntity;
      if (playerEntity != null && !var21) {
         throw new ClassCastException("CHECKCAST failed");
      }

      float var22 = ((ILivingEntity)(Object)playerEntity).getLeaningPitch();
      ((ILivingEntity)(Object)var4).setLeaningPitch((float)Float.intBitsToFloat(Float.floatToRawIntBits(var22)));
      boolean var23 = playerEntity instanceof ILivingEntity;
      if (playerEntity != null && !var23) {
         throw new ClassCastException("CHECKCAST failed");
      }

      float var24 = ((ILivingEntity)(Object)playerEntity).getLeaningPitch();
      ((ILivingEntity)(Object)var4).setLastLeaningPitch((float)Float.intBitsToFloat(Float.floatToRawIntBits(var24)));
      Setting var25 = this.booleanValueSetting2;
      Object var26 = var25.getValue();
      boolean var27 = var26 instanceof Boolean;
      if (var26 != null && !var27) {
         throw new ClassCastException("CHECKCAST failed");
      }

      boolean var28 = (Boolean)var26;
      if (var28) {
         LimbAnimator var29 = var4.limbAnimator;
         LimbAnimator var30 = playerEntity.limbAnimator;
         float var31 = var30.getSpeed();
         var29.setSpeed((float)Float.intBitsToFloat(Float.floatToRawIntBits(var31)));
         LimbAnimator var32 = var4.limbAnimator;
         boolean var33 = var32 instanceof ILimbAnimator;
         if (var32 != null && !var33) {
            throw new ClassCastException("CHECKCAST failed");
         }

         LimbAnimator var41 = playerEntity.limbAnimator;
         float var42 = var41.getPos();
         ((ILimbAnimator)(Object)var32).setPos((float)Float.intBitsToFloat(Float.floatToRawIntBits(var42)));
      }

      MinecraftClient var35 = MinecraftHolder.mc;
      RenderTickCounter var36 = var35.getRenderTickCounter();
      float var37 = var36.getTickDelta(false & true);
      PlayerModelCopy var34 = new PlayerModelCopy(var4, (float)Float.intBitsToFloat(Float.floatToRawIntBits(var37)));
      long var38 = System.currentTimeMillis();
      var34.setLongValue(var38);
      CopyOnWriteArrayList var40 = this.playerModelCopyCopyOnWriteArrayList;
      var40.add(var34);
   }

   public PopChamsModule(PhobosClient phobosClient) {
      super(phobosClient, "PopChams", Categories.render, "Renders a model when a player pops");
      this.nativeInit(phobosClient);
   }

   public void nativeInit(final PhobosClient var1) {
      Color var3 = Color.WHITE;
      Color var4 = ColorUtils.createColor6(var3, (float)0.2F);
      ColorSetting var2 = new ColorSetting("Color", var4);
      ColorSetting var5 = var2.getColorSetting();
      Setting var6 = this.resolveSetting2(var5);
      this.colorSetting = var6;
      BooleanSetting var7 = new BooleanSetting("Lines");
      Setting var8 = var7.resolveSetting7("Model lines");
      Setting var9 = this.resolveSetting2(var8);
      this.booleanValueSetting = var9;
      Color var11 = Color.WHITE;
      ColorSetting var10 = new ColorSetting("LinesColor", var11);
      ColorSetting var12 = var10.getColorSetting();
      Setting var13 = this.resolveSetting2(var12);
      this.colorSetting2 = var13;
      Float var15 = (float)1.0F;
      Float var16 = (float)0.0F;
      Float var17 = (float)3.0F;
      NumberSetting var14 = new NumberSetting<>("Width", var15, var16, var17);
      Setting var18 = var14.resolveSetting7("Line width");
      Setting var19 = this.resolveSetting2(var18);
      this.floatValueSetting = var19;
      LongNumberSetting var20 = new LongNumberSetting("Fade", 500L, 0L, 5000L);
      Setting var21 = var20.resolveSetting7("Fade time");
      Setting var22 = this.resolveSetting2(var21);
      this.longValueSetting = var22;
      BooleanSetting var23 = new BooleanSetting("Animations");
      Setting var24 = var23.resolveSetting7("Copy limbs animations");
      Setting var25 = this.resolveSetting2(var24);
      this.booleanValueSetting2 = var25;
      BooleanSetting var26 = new BooleanSetting("Await");
      Setting var27 = var26.resolveSetting7("Creates a snapshot next tick, to copy damage animation");
      Setting var28 = this.resolveSetting2(var27);
      this.booleanValueSetting4 = var28;
      BooleanSetting var29 = new BooleanSetting("Move");
      Setting var30 = var29.resolveSetting7("Moves the model upwards");
      Setting var31 = this.resolveSetting2(var30);
      this.booleanValueSetting5 = var31;
      Float var33 = (float)1.0F;
      Float var34 = (float)0.0F;
      Float var35 = (float)10.0F;
      NumberSetting var32 = new NumberSetting<>("Factor", var33, var34, var35);
      Setting var36 = this.resolveSetting2(var32);
      this.floatValueSetting2 = var36;
      Boolean var38 = false & true;
      BooleanSetting var37 = new BooleanSetting("Dissolve", var38);
      Setting var39 = var37.resolveSetting7("Dissolve effect");
      Setting var40 = this.resolveSetting2(var39);
      this.booleanValueSetting6 = var40;
      Color var42 = new Color((float)0.95F, (float)0.35F, (float)0.01F, (float)1.0F);
      ColorSetting var41 = new ColorSetting("Lead", var42);
      Setting var43 = this.resolveSetting2(var41);
      this.colorSetting3 = var43;
      Color var45 = new Color((float)0.95F, (float)0.75F, (float)0.01F, (float)1.0F);
      ColorSetting var44 = new ColorSetting("Trail", var45);
      Setting var46 = this.resolveSetting2(var44);
      this.colorSetting4 = var46;
      LongNumberSetting var47 = new LongNumberSetting("DissolveTime", 1700L, 500L, 4000L, true & true);
      Setting var48 = this.resolveSetting2(var47);
      this.longValueSetting2 = var48;
      BooleanSetting var49 = new BooleanSetting("Gradient");
      Setting var50 = this.resolveSetting2(var49);
      this.booleanValueSetting3 = var50;
      CopyOnWriteArrayList var51 = new CopyOnWriteArrayList();
      this.playerModelCopyCopyOnWriteArrayList = var51;
      Normal var52 = new Normal();
      this.normal = var52;
      this.run5();
      var var53 = new SafeListener<EntityStatusEvent>() {
         public void handleObject(
            EntityStatusEvent entityStatusEvent,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (!(entityStatusEvent.entity() instanceof ClientPlayerEntity)
               && !var1.getFriendList().testUUID(entityStatusEvent.entity().getUuid())
               && entityStatusEvent.entity() instanceof PlayerEntity playerEntity) {
               if (PopChamsModule.this.booleanValueSetting4.getValue()) {
                  var1.getScheduler().handleRunnable2(() -> PopChamsModule.this.handleClientWorld(clientWorld, playerEntity), 1);
               } else {
                  PopChamsModule.this.handleClientWorld(clientWorld, playerEntity);
               }
            }
         }
      };
      this.handleListener(var53);
      var var54 = new SafeListener<RenderContext>() {
         public void handleObject(
            RenderContext renderContext,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            Render3d.run6();

            for (PlayerModelCopy playerModelCopy : PopChamsModule.this.playerModelCopyCopyOnWriteArrayList) {
               Render3d.handlePlayerEntityModel4(playerModelCopy.getPlayerEntityPlayerEntityModel());
               Render3d.handlePlayerEntity3(
                  playerModelCopy.getPlayerEntity(),
                  playerModelCopy.getPlayerEntityPlayerEntityModel(),
                  playerModelCopy.getLongValue2(),
                  PopChamsModule.this.colorSetting,
                  PopChamsModule.this.longValueSetting,
                  PopChamsModule.this.booleanValueSetting,
                  PopChamsModule.this.colorSetting2,
                  PopChamsModule.this.floatValueSetting,
                  playerModelCopy.getFloatValue(),
                  model -> {
                     if (PopChamsModule.this.booleanValueSetting6.getValue()) {
                        model.setRunnable(
                           () -> {
                              Render3d.positionDissolveShader
                                 .handleFloat(
                                    (float)(System.currentTimeMillis() - playerModelCopy.getLongValue())
                                       / PopChamsModule.this.longValueSetting2.getValue().floatValue(),
                                    PopChamsModule.this.colorSetting3.getValue(),
                                    PopChamsModule.this.colorSetting4.getValue(),
                                    PopChamsModule.this.booleanValueSetting3.getValue()
                                 );
                              Render3d.positionDissolveShader.run3();
                           }
                        );
                        model.setRunnable2(
                           () -> {
                              Render3d.linesDissolveShader
                                 .handleFloat(
                                    (float)(System.currentTimeMillis() - playerModelCopy.getLongValue())
                                       / PopChamsModule.this.longValueSetting2.getValue().floatValue(),
                                    PopChamsModule.this.colorSetting3.getValue(),
                                    PopChamsModule.this.colorSetting4.getValue(),
                                    PopChamsModule.this.booleanValueSetting3.getValue()
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
      };
      this.handleListener(var54);
      var var55 = new Listener<TickEvent2>() {
         public void handleObject2(TickEvent2 tickEvent2) {
            if (PopChamsModule.this.normal.testLong(PopChamsModule.this.longValueSetting.getValue() + 1000L)
               && !PopChamsModule.this.playerModelCopyCopyOnWriteArrayList.isEmpty()) {
               PopChamsModule.this.playerModelCopyCopyOnWriteArrayList.clear();
            } else {
               PopChamsModule.this.playerModelCopyCopyOnWriteArrayList
                  .forEach(
                     playerModelCopy -> {
                        playerModelCopy.handleCopyOnWriteArrayList(
                           PopChamsModule.this.playerModelCopyCopyOnWriteArrayList, PopChamsModule.this.longValueSetting.getValue()
                        );
                        if (PopChamsModule.this.booleanValueSetting5.getValue()) {
                           Vec3d vec3d = playerModelCopy.getPlayerEntity().getPos();
                           playerModelCopy.getPlayerEntity().lastRenderX = vec3d.getX();
                           playerModelCopy.getPlayerEntity().lastRenderY = vec3d.getY();
                           playerModelCopy.getPlayerEntity().lastRenderZ = vec3d.getZ();
                           playerModelCopy.getPlayerEntity()
                              .setPosition(
                                 vec3d.getX(),
                                 vec3d.getY() - playerModelCopy.value() * 1.0E-14 * PopChamsModule.this.floatValueSetting2.getValue().floatValue(),
                                 vec3d.getZ()
                              );
                        }
                     }
                  );
            }
         }
      };
      this.handleListener(var55);
      CopyOnWriteArrayList var56 = this.playerModelCopyCopyOnWriteArrayList;
      Runnable var73 = var56::clear;
      ModuleListenerUtils.handleListenerHolder4(this, var73);
   }

   @Override
   public void run3() {
      this.playerModelCopyCopyOnWriteArrayList.clear();
   }

   @Override
   public void run4() {
      this.playerModelCopyCopyOnWriteArrayList.clear();
   }
}
