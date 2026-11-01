package org.phobos.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.event.events.HeldItemRenderEvent;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.ApplyEatOrDrinkTransformationEvent;
import org.phobos.event.Listener;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.RenderArmEvent2;
import org.phobos.event.SafeListener;
import org.phobos.mixin.game.render.interfaces.IHeldItemRenderer;
import org.phobos.module.Module;
import org.phobos.module.player.SilentHotbarModule;

public class ViewModelModule extends Module {
   public final Setting<ViewModelModule.Swing> mainhandSwing;
   public final Setting<Boolean> mainhand;
   public final Setting<Float> mainhandTranslateZ;
   public final Setting<Boolean> mainhandScale;
   public final Setting<Float> mainhandTranslateY;
   public final Setting<Boolean> mainhandTranslate;
   public final Setting<Float> mainhandScaleY;
   public final Setting<Boolean> offhand;
   public final Setting<Float> mainhandRotateZ;
   public final Setting<Float> mainhandScaleX;
   public final Setting<Float> mainhandScaleZ;
   public final Setting<Float> mainhandRotateX;
   public final Setting<Boolean> items = this.resolveSetting2(new BooleanSetting("Items", true).resolveSetting7("Items view model"));
   public final Setting<Boolean> mainhandInstant;
   public final Setting<Float> mainhandRotateY;
   public final Setting<Boolean> noSway = this.resolveSetting2(new BooleanSetting("NoSway"));
   public final Setting<Float> mainhandTranslateX;
   public static ViewModelModule viewModelModule;
   public final Setting<Boolean> mainhandRotate;
   public final Setting<Float> offhandTranslateZ;
   public final Setting<Float> offhandScaleZ;
   public final Setting<Float> offhandTranslateY;
   public final Setting<Float> offhandTranslateX;
   public final Setting<Float> offhandRotateY;
   public final Setting<ViewModelModule.Swing> offhandSwing;
   public final Setting<Float> eatingTranslateX;
   public final Setting<Float> offhandScaleX;
   public final Setting<Float> offhandScaleY;
   public final Setting<Boolean> offhandRotate;
   public final Setting<Float> offhandRotateX;
   public final Setting<Boolean> eatingTranslate;
   public final Setting<Float> eatingTranslateY;
   public final Setting<Float> eatingTranslateZ;
   public final Setting<Float> offhandRotateZ;
   public final Setting<Boolean> eating;
   public final Setting<Boolean> offhandInstant;
   public final Setting<Boolean> offhandTranslate;
   public final Setting<Boolean> offhandScale;
   public final Setting<Float> eatingScaleY;
   public final Setting<Boolean> eatingScale;
   public final Setting<Float> shieldRotateY;
   public final Setting<Float> shieldRotateZ;
   public final Setting<Boolean> eatingRotate;
   public final Setting<Float> eatingRotateX;
   public final Setting<Float> shieldTranslateZ;
   public final Setting<Boolean> shieldTranslate;
   public final Setting<Float> eatingScaleZ;
   public final Setting<Float> shieldTranslateX;
   public final Setting<Boolean> shieldScale;
   public final Setting<Float> eatingRotateY;
   public final Setting<Boolean> shield;
   public final Setting<Float> shieldRotateX;
   public final Setting<Boolean> shieldRotate;
   public final Setting<Float> eatingFactor;
   public final Setting<Float> eatingRotateZ;
   public final Setting<Float> eatingScaleX;
   public final Setting<Float> shieldTranslateY;
   public final Setting<Color> itemsColorValue;
   public final Setting<Float> handScaleY;
   public final Setting<Boolean> handTranslate;
   public final Setting<Boolean> hand;
   public final Setting<Float> handTranslateX;
   public final Setting<Float> handScaleZ;
   public final Setting<Float> shieldScaleZ;
   public final Setting<Float> handRotateX;
   public final Setting<Float> handRotateZ;
   public final Setting<Boolean> handRotate;
   public final Setting<Float> handScaleX;
   public final Setting<Float> handRotateY;
   public final Setting<Float> shieldScaleX;
   public final Setting<Float> shieldScaleY;
   public final Setting<Float> handTranslateY;
   public final Setting<Boolean> itemsColor;
   public final Setting<Boolean> handScale;
   public final Setting<Float> handTranslateZ;

   public void handleMatrixStack(MatrixStack matrixStack, Hand hand, boolean flag, boolean flag2) {
      switch (hand) {
         case MAIN_HAND:
            if (this.testFlag2(flag, flag2, this.mainhandTranslate.getValue())) {
               float[] floats = this.values3(
                  flag, flag2, this.mainhandTranslateX.getValue(), this.mainhandTranslateY.getValue(), this.mainhandTranslateZ.getValue()
               );
               matrixStack.translate(floats[0], floats[1], floats[2]);
            }
            break;
         case OFF_HAND:
            if (this.testFlag2(flag, flag2, this.offhandTranslate.getValue())) {
               float[] floats2 = this.values3(
                  flag, flag2, this.offhandTranslateX.getValue(), this.offhandTranslateY.getValue(), this.offhandTranslateZ.getValue()
               );
               matrixStack.translate(-floats2[0], floats2[1], floats2[2]);
            }
      }
   }

   public boolean testFlag(boolean flag, boolean flag2, boolean flag3) {
      return flag ? this.shieldScale.getValue() : (flag2 ? this.eatingScale.getValue() : flag3);
   }

   public float[] values(boolean flag, boolean flag2, float f, float f2, float f3) {
      return new float[]{
         flag ? this.shieldRotateX.getValue() : (flag2 ? this.eatingRotateX.getValue() : f),
         flag ? this.shieldRotateY.getValue() : (flag2 ? this.eatingRotateY.getValue() : f2),
         flag ? this.shieldRotateZ.getValue() : (flag2 ? this.eatingRotateZ.getValue() : f3)
      };
   }

   public void handleMatrixStack2(MatrixStack matrixStack, Hand hand, boolean flag, boolean flag2) {
      switch (hand) {
         case MAIN_HAND:
            if (this.testFlag(flag, flag2, this.mainhandScale.getValue())) {
               float[] floats = this.values2(flag, flag2, this.mainhandScaleX.getValue(), this.mainhandScaleY.getValue(), this.mainhandScaleZ.getValue());
               matrixStack.scale(floats[0], floats[1], floats[2]);
            }
            break;
         case OFF_HAND:
            if (this.testFlag(flag, flag2, this.offhandScale.getValue())) {
               float[] floats2 = this.values2(flag, flag2, this.offhandScaleX.getValue(), this.offhandScaleY.getValue(), this.offhandScaleZ.getValue());
               matrixStack.scale(floats2[0], floats2[1], floats2[2]);
            }
      }
   }

   public boolean testFlag2(boolean flag, boolean flag2, boolean flag3) {
      return flag ? this.shieldTranslate.getValue() : (flag2 ? this.eatingTranslate.getValue() : flag3);
   }

   public float[] values2(boolean flag, boolean flag2, float f, float f2, float f3) {
      return new float[]{
         flag ? this.shieldScaleX.getValue() : (flag2 ? this.eatingScaleX.getValue() : f),
         flag ? this.shieldScaleY.getValue() : (flag2 ? this.eatingScaleY.getValue() : f2),
         flag ? this.shieldScaleZ.getValue() : (flag2 ? this.eatingScaleZ.getValue() : f3)
      };
   }

   public void handleMatrixStack3(MatrixStack matrixStack, Hand hand) {
      ClientPlayerEntity clientPlayerEntity = mc.player;
      if (clientPlayerEntity != null) {
         boolean flag = clientPlayerEntity.isUsingItem()
            && hand.equals(clientPlayerEntity.getActiveHand())
            && clientPlayerEntity.getStackInHand(hand).getItem() instanceof ShieldItem
            && this.shield.getValue();
         boolean flag2 = clientPlayerEntity.isUsingItem() && hand.equals(clientPlayerEntity.getActiveHand()) && this.eating.getValue();
         if (flag || flag2 || this.testHand(hand)) {
            this.handleMatrixStack(matrixStack, hand, flag, flag2);
            this.handleMatrixStack2(matrixStack, hand, flag, flag2);
            this.handleMatrixStack4(matrixStack, hand, flag, flag2);
         }
      }
   }

   public boolean testHand(Hand hand) {
      return switch (hand) {
         case MAIN_HAND -> this.mainhand.getValue();
         case OFF_HAND -> this.offhand.getValue();
         default -> throw new MatchException(null, null);
      };
   }

   public float[] values3(boolean flag, boolean flag2, float f, float f2, float f3) {
      return new float[]{
         flag ? this.shieldTranslateX.getValue() : (flag2 ? this.eatingTranslateX.getValue() : f),
         flag ? this.shieldTranslateY.getValue() : (flag2 ? this.eatingTranslateY.getValue() : f2),
         flag ? this.shieldTranslateZ.getValue() : (flag2 ? this.eatingTranslateZ.getValue() : f3)
      };
   }

   public static ViewModelModule getViewModelModule() {
      return viewModelModule;
   }

   public ViewModelModule(PhobosClient phobosClient) {
      super(phobosClient, "ViewModel", Categories.render, "Modifies item view model");
      this.mainhand = this.resolveSetting2(new BooleanSetting("Mainhand", false).resolveSetting7("Mainhand settings"));
      this.mainhandTranslate = this.resolveSetting2(new BooleanSetting("MainhandTranslate", false));
      this.mainhandTranslateX = this.resolveSetting2(new NumberSetting<>("MainhandTranslateX", 0.0F, -5.0F, 5.0F));
      this.mainhandTranslateY = this.resolveSetting2(new NumberSetting<>("MainhandTranslateY", 0.0F, -5.0F, 5.0F));
      this.mainhandTranslateZ = this.resolveSetting2(new NumberSetting<>("MainhandTranslateZ", 0.0F, -5.0F, 5.0F));
      this.mainhandRotate = this.resolveSetting2(new BooleanSetting("MainhandRotate", false));
      this.mainhandRotateX = this.resolveSetting2(new NumberSetting<>("MainhandRotateX", 0.0F, -360.0F, 360.0F));
      this.mainhandRotateY = this.resolveSetting2(new NumberSetting<>("MainhandRotateY", 0.0F, -360.0F, 360.0F));
      this.mainhandRotateZ = this.resolveSetting2(new NumberSetting<>("MainhandRotateZ", 0.0F, -360.0F, 360.0F));
      this.mainhandScale = this.resolveSetting2(new BooleanSetting("MainhandScale", false));
      this.mainhandScaleX = this.resolveSetting2(new NumberSetting<>("MainhandScaleX", 1.0F, 0.0F, 2.0F));
      this.mainhandScaleY = this.resolveSetting2(new NumberSetting<>("MainhandScaleY", 1.0F, 0.0F, 2.0F));
      this.mainhandScaleZ = this.resolveSetting2(new NumberSetting<>("MainhandScaleZ", 1.0F, 0.0F, 2.0F));
      this.mainhandInstant = this.resolveSetting2(new BooleanSetting("MainhandInstant", false).resolveSetting7("No cooldown animation"));
      this.mainhandSwing = this.resolveSetting2(new EnumSetting<>("MainhandSwing", ViewModelModule.Swing.Normal));
      this.offhand = this.resolveSetting2(new BooleanSetting("Offhand", false).resolveSetting7("Offhand settings"));
      this.offhandTranslate = this.resolveSetting2(new BooleanSetting("OffhandTranslate", false));
      this.offhandTranslateX = this.resolveSetting2(new NumberSetting<>("OffhandTranslateX", 0.0F, -5.0F, 5.0F));
      this.offhandTranslateY = this.resolveSetting2(new NumberSetting<>("OffhandTranslateY", 0.0F, -5.0F, 5.0F));
      this.offhandTranslateZ = this.resolveSetting2(new NumberSetting<>("OffhandTranslateZ", 0.0F, -5.0F, 5.0F));
      this.offhandRotate = this.resolveSetting2(new BooleanSetting("OffhandRotate", false));
      this.offhandRotateX = this.resolveSetting2(new NumberSetting<>("OffhandRotateX", 0.0F, -360.0F, 360.0F));
      this.offhandRotateY = this.resolveSetting2(new NumberSetting<>("OffhandRotateY", 0.0F, -360.0F, 360.0F));
      this.offhandRotateZ = this.resolveSetting2(new NumberSetting<>("OffhandRotateZ", 0.0F, -360.0F, 360.0F));
      this.offhandScale = this.resolveSetting2(new BooleanSetting("OffhandScale", false));
      this.offhandScaleX = this.resolveSetting2(new NumberSetting<>("OffhandScaleX", 1.0F, 0.0F, 2.0F));
      this.offhandScaleY = this.resolveSetting2(new NumberSetting<>("OffhandScaleY", 1.0F, 0.0F, 2.0F));
      this.offhandScaleZ = this.resolveSetting2(new NumberSetting<>("OffhandScaleZ", 1.0F, 0.0F, 2.0F));
      this.offhandInstant = this.resolveSetting2(new BooleanSetting("OffhandInstant", false).resolveSetting7("No cooldown animation"));
      this.offhandSwing = this.resolveSetting2(new EnumSetting<>("OffhandSwing", ViewModelModule.Swing.Normal));
      this.eating = this.resolveSetting2(new BooleanSetting("Eating", false).resolveSetting7("Consuming settings"));
      this.eatingTranslate = this.resolveSetting2(new BooleanSetting("EatingTranslate", false));
      this.eatingTranslateX = this.resolveSetting2(new NumberSetting<>("EatingTranslateX", 0.0F, -5.0F, 5.0F));
      this.eatingTranslateY = this.resolveSetting2(new NumberSetting<>("EatingTranslateY", 0.0F, -5.0F, 5.0F));
      this.eatingTranslateZ = this.resolveSetting2(new NumberSetting<>("EatingTranslateZ", 0.0F, -5.0F, 5.0F));
      this.eatingRotate = this.resolveSetting2(new BooleanSetting("EatingRotate", false));
      this.eatingRotateX = this.resolveSetting2(new NumberSetting<>("EatingRotateX", 0.0F, -180.0F, 180.0F));
      this.eatingRotateY = this.resolveSetting2(new NumberSetting<>("EatingRotateY", 0.0F, -180.0F, 180.0F));
      this.eatingRotateZ = this.resolveSetting2(new NumberSetting<>("EatingRotateZ", 0.0F, -180.0F, 180.0F));
      this.eatingScale = this.resolveSetting2(new BooleanSetting("EatingScale", false));
      this.eatingScaleX = this.resolveSetting2(new NumberSetting<>("EatingScaleX", 1.0F, 0.0F, 2.0F));
      this.eatingScaleY = this.resolveSetting2(new NumberSetting<>("EatingScaleY", 1.0F, 0.0F, 2.0F));
      this.eatingScaleZ = this.resolveSetting2(new NumberSetting<>("EatingScaleZ", 1.0F, 0.0F, 2.0F));
      this.eatingFactor = this.resolveSetting2(new NumberSetting<>("EatingFactor", 5.0F, 0.1F, 500.0F));
      this.shield = this.resolveSetting2(new BooleanSetting("Shield", false).resolveSetting7("Shield settings"));
      this.shieldTranslate = this.resolveSetting2(new BooleanSetting("ShieldTranslate", false));
      this.shieldTranslateX = this.resolveSetting2(new NumberSetting<>("ShieldTranslateX", 0.0F, -5.0F, 5.0F));
      this.shieldTranslateY = this.resolveSetting2(new NumberSetting<>("ShieldTranslateY", 0.0F, -5.0F, 5.0F));
      this.shieldTranslateZ = this.resolveSetting2(new NumberSetting<>("ShieldTranslateZ", 0.0F, -5.0F, 5.0F));
      this.shieldRotate = this.resolveSetting2(new BooleanSetting("ShieldRotate", false));
      this.shieldRotateX = this.resolveSetting2(new NumberSetting<>("ShieldRotateX", 0.0F, -180.0F, 180.0F));
      this.shieldRotateY = this.resolveSetting2(new NumberSetting<>("ShieldRotateY", 0.0F, -180.0F, 180.0F));
      this.shieldRotateZ = this.resolveSetting2(new NumberSetting<>("ShieldRotateZ", 0.0F, -180.0F, 180.0F));
      this.shieldScale = this.resolveSetting2(new BooleanSetting("ShieldScale", false));
      this.shieldScaleX = this.resolveSetting2(new NumberSetting<>("ShieldScaleX", 1.0F, 0.0F, 2.0F));
      this.shieldScaleY = this.resolveSetting2(new NumberSetting<>("ShieldScaleY", 1.0F, 0.0F, 2.0F));
      this.shieldScaleZ = this.resolveSetting2(new NumberSetting<>("ShieldScaleZ", 1.0F, 0.0F, 2.0F));
      this.hand = this.resolveSetting2(new BooleanSetting("Hand", false).resolveSetting7("Hand view model"));
      this.handTranslate = this.resolveSetting2(new BooleanSetting("HandTranslate", false));
      this.handTranslateX = this.resolveSetting2(new NumberSetting<>("HandTranslateX", 0.0F, -5.0F, 5.0F));
      this.handTranslateY = this.resolveSetting2(new NumberSetting<>("HandTranslateY", 0.0F, -5.0F, 5.0F));
      this.handTranslateZ = this.resolveSetting2(new NumberSetting<>("HandTranslateZ", 0.0F, -5.0F, 5.0F));
      this.handRotate = this.resolveSetting2(new BooleanSetting("HandRotate", false));
      this.handRotateX = this.resolveSetting2(new NumberSetting<>("HandRotateX", 0.0F, -180.0F, 180.0F));
      this.handRotateY = this.resolveSetting2(new NumberSetting<>("HandRotateY", 0.0F, -180.0F, 180.0F));
      this.handRotateZ = this.resolveSetting2(new NumberSetting<>("HandRotateZ", 0.0F, -180.0F, 180.0F));
      this.handScale = this.resolveSetting2(new BooleanSetting("HandScale", false));
      this.handScaleX = this.resolveSetting2(new NumberSetting<>("HandScaleX", 1.0F, 0.0F, 2.0F));
      this.handScaleY = this.resolveSetting2(new NumberSetting<>("HandScaleY", 1.0F, 0.0F, 2.0F));
      this.handScaleZ = this.resolveSetting2(new NumberSetting<>("HandScaleZ", 1.0F, 0.0F, 2.0F));
      this.itemsColor = this.resolveSetting2(new BooleanSetting("ItemsColor", false));
      this.itemsColorValue = this.resolveSetting2(new ColorSetting("ItemsColorValue", Color.WHITE));
      this.createBuilder2()
         .resolveBuilder6(this.items)
         .resolveBuilder4(
            this.createBuilder()
               .resolveBuilder6(this.mainhand)
               .resolveBuilder5(
                  this.resolveSettingGroup(this.mainhandTranslate, this.mainhandTranslateX, this.mainhandTranslateY, this.mainhandTranslateZ),
                  this.resolveSettingGroup(this.mainhandRotate, this.mainhandRotateX, this.mainhandRotateY, this.mainhandRotateZ),
                  this.resolveSettingGroup(this.mainhandScale, this.mainhandScaleX, this.mainhandScaleY, this.mainhandScaleZ)
               )
               .resolveBuilder(this.mainhandInstant, this.mainhandSwing)
               .getSettingSubsection(),
            this.createBuilder()
               .resolveBuilder6(this.offhand)
               .resolveBuilder5(
                  this.resolveSettingGroup(this.offhandTranslate, this.offhandTranslateX, this.offhandTranslateY, this.offhandTranslateZ),
                  this.resolveSettingGroup(this.offhandRotate, this.offhandRotateX, this.offhandRotateY, this.offhandRotateZ),
                  this.resolveSettingGroup(this.offhandScale, this.offhandScaleX, this.offhandScaleY, this.offhandScaleZ)
               )
               .resolveBuilder(this.offhandInstant, this.offhandSwing)
               .getSettingSubsection(),
            this.createBuilder()
               .resolveBuilder6(this.eating)
               .resolveBuilder5(
                  this.resolveSettingGroup(this.eatingTranslate, this.eatingTranslateX, this.eatingTranslateY, this.eatingTranslateZ),
                  this.resolveSettingGroup(this.eatingRotate, this.eatingRotateX, this.eatingRotateY, this.eatingRotateZ),
                  this.resolveSettingGroup(this.eatingScale, this.eatingScaleX, this.eatingScaleY, this.eatingScaleZ)
               )
               .resolveBuilder(this.eatingFactor)
               .getSettingSubsection(),
            this.createBuilder()
               .resolveBuilder6(this.shield)
               .resolveBuilder5(
                  this.resolveSettingGroup(this.shieldTranslate, this.shieldTranslateX, this.shieldTranslateY, this.shieldTranslateZ),
                  this.resolveSettingGroup(this.shieldRotate, this.shieldRotateX, this.shieldRotateY, this.shieldRotateZ),
                  this.resolveSettingGroup(this.shieldScale, this.shieldScaleX, this.shieldScaleY, this.shieldScaleZ)
               )
               .getSettingSubsection()
         )
         .resolveBuilder7(this.resolveSettingGroup(this.itemsColor, this.itemsColorValue))
         .resolveBuilder3(this.noSway)
         .getSettingSection();
      this.createBuilder()
         .resolveBuilder6(this.hand)
         .resolveBuilder5(
            this.resolveSettingGroup(this.handTranslate, this.handTranslateX, this.handTranslateY, this.handTranslateZ),
            this.resolveSettingGroup(this.handRotate, this.handRotateX, this.handRotateY, this.handRotateZ),
            this.resolveSettingGroup(this.handScale, this.handScaleX, this.handScaleY, this.handScaleZ)
         )
         .getSettingSubsection();
      this.handleListener(new Listener<HeldItemRenderEvent>() {
         public void handleObject2(HeldItemRenderEvent heldItemRenderEvent) {
            if (ViewModelModule.this.items.getValue()) {
               if (ViewModelModule.this.itemsColor.getValue()) {
                  Color color = ViewModelModule.this.itemsColorValue.getValue();
                  RenderSystem.setShaderColor(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
               }

               ViewModelModule.this.handleMatrixStack3(heldItemRenderEvent.getMatrixStack(), heldItemRenderEvent.getHand());
            }
         }
      });
      this.handleListener(
         new SafeListener<ApplyEatOrDrinkTransformationEvent>() {
            public void handleObject(
               ApplyEatOrDrinkTransformationEvent applyEatOrDrinkTransformationEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (ViewModelModule.this.items.getValue()) {
                  if (ViewModelModule.this.eating.getValue()) {
                     applyEatOrDrinkTransformationEvent.setCancelled(true);
                     float f2 = clientPlayerEntity.getItemUseTimeLeft() - applyEatOrDrinkTransformationEvent.getFloatValue2() + 1.0F;
                     float f3 = f2 / applyEatOrDrinkTransformationEvent.getItemStack().getMaxUseTime(clientPlayerEntity);
                     if (f3 < 0.8F) {
                        float f = MathHelper.abs(MathHelper.cos(f2 / 4.0F * (float) Math.PI) * (ViewModelModule.this.eatingFactor.getValue() * 0.001F));
                        applyEatOrDrinkTransformationEvent.getMatrixStack().translate(0.0F, f, 0.0F);
                     }

                     float f4 = 1.0F - (float)Math.pow(f3, 27.0);
                     int i = applyEatOrDrinkTransformationEvent.getArm().equals(Arm.RIGHT) ? 1 : -1;
                     applyEatOrDrinkTransformationEvent.getMatrixStack().translate(f4 * 0.6F * i, f4 * -0.5F, f4 * 0.0F);
                     applyEatOrDrinkTransformationEvent.getMatrixStack().multiply(RotationAxis.POSITIVE_Y.rotationDegrees(i * f4 * 90.0F));
                     applyEatOrDrinkTransformationEvent.getMatrixStack().multiply(RotationAxis.POSITIVE_X.rotationDegrees(f4 * 10.0F));
                     applyEatOrDrinkTransformationEvent.getMatrixStack().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(i * f4 * 30.0F));
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<PlayerTickEvent>() {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (ViewModelModule.this.items.getValue()) {
                  IHeldItemRenderer iHeldItemRenderer = (IHeldItemRenderer)(Object)mc.gameRenderer.firstPersonRenderer;
                  if (ViewModelModule.this.mainhand.getValue()
                     && ViewModelModule.this.mainhandInstant.getValue()
                     && iHeldItemRenderer.getEquipProgressMainHand() <= 1.0F) {
                     iHeldItemRenderer.setEquipProgressMainHand(1.0F);
                     iHeldItemRenderer.setMainHand(SilentHotbarModule.resolveItemStack(clientPlayerEntity));
                  }

                  if (ViewModelModule.this.offhand.getValue()
                     && ViewModelModule.this.offhandInstant.getValue()
                     && iHeldItemRenderer.getEquipProgressOffHand() <= 1.0F) {
                     iHeldItemRenderer.setEquipProgressOffHand(1.0F);
                     iHeldItemRenderer.setOffHand(clientPlayerEntity.getOffHandStack());
                  }
               }
            }
         }
      );
      this.handleListener(
         new Listener<RenderArmEvent2>() {
            public void handleObject2(RenderArmEvent2 renderArmEvent2) {
               if (ViewModelModule.this.hand.getValue()) {
                  MatrixStack matrixStack = renderArmEvent2.getMatrixStack();
                  int i = renderArmEvent2.getArm().equals(Arm.RIGHT) ? 1 : -1;
                  if (ViewModelModule.this.handTranslate.getValue()) {
                     matrixStack.translate(
                        ViewModelModule.this.handTranslateX.getValue() * i,
                        ViewModelModule.this.handTranslateY.getValue(),
                        ViewModelModule.this.handTranslateZ.getValue() * i
                     );
                  }

                  if (ViewModelModule.this.handScale.getValue()) {
                     matrixStack.scale(
                        ViewModelModule.this.handScaleX.getValue(), ViewModelModule.this.handScaleY.getValue(), ViewModelModule.this.handScaleZ.getValue()
                     );
                  }

                  if (ViewModelModule.this.handRotate.getValue()) {
                     matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(ViewModelModule.this.handRotateX.getValue()));
                     matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(ViewModelModule.this.handRotateY.getValue()));
                     matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(ViewModelModule.this.handRotateZ.getValue()));
                  }
               }
            }
         }
      );
      viewModelModule = this;
   }

   public static float computeHand(Hand hand) {
      if (viewModelModule.check7() && viewModelModule.testHand(hand)) {
         return switch (hand) {
            case MAIN_HAND -> viewModelModule.mainhandSwing.getValue().getOffset();
            case OFF_HAND -> viewModelModule.offhandSwing.getValue().getOffset();
            default -> throw new MatchException(null, null);
         };
      } else {
         return 0.0F;
      }
   }

   public void handleMatrixStack4(MatrixStack matrixStack, Hand hand, boolean flag, boolean flag2) {
      switch (hand) {
         case MAIN_HAND:
            if (this.testFlag3(flag, flag2, this.mainhandRotate.getValue())) {
               float[] floats = this.values(flag, flag2, this.mainhandRotateX.getValue(), this.mainhandRotateY.getValue(), this.mainhandRotateZ.getValue());
               matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(floats[0]));
               matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(floats[1]));
               matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(floats[2]));
            }
            break;
         case OFF_HAND:
            if (this.testFlag3(flag, flag2, this.offhandRotate.getValue())) {
               float[] floats2 = this.values(flag, flag2, this.offhandRotateX.getValue(), this.offhandRotateY.getValue(), this.offhandRotateZ.getValue());
               matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(floats2[0]));
               matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(floats2[1]));
               matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(floats2[2]));
            }
      }
   }

   public boolean testFlag3(boolean flag, boolean flag2, boolean flag3) {
      return flag ? this.shieldRotate.getValue() : (flag2 ? this.eatingRotate.getValue() : flag3);
   }

   private enum Swing {
      Normal(0.0F),
      Reverse(-1.0F),
      Shake(-2.0F),
      Jitter(-3.0F);

      public final float offset;

      Swing(final float offset) {
         this.offset = offset;
      }

      public float getOffset() {
         return this.offset;
      }
   }
}
