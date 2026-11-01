package org.phobos.module.render;

import java.util.function.Function;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.Hand;
import org.earthhack.ducks.IEndCrystalEntity;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.player.SneakAnimationState;
import org.phobos.core.entity.EntityAnimationState;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.event.Listener;
import org.phobos.event.OnMouseButtonEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.SwingSpeedEvent;
import org.phobos.type.KeyboardMouseEnum;
import org.phobos.module.Module;

public class AnimationsModule extends Module {
   public final Setting<Boolean> players;
   public final Setting<Boolean> booleanValueSetting;
   public static AnimationsModule animationsModule;
   public final Setting<Boolean> speed;
   public final Setting<Float> crystalsSpeedFactor;
   public final Setting<AnimationsModule.Selection> selection;
   public final Setting<Float> speedFactor;
   public final Setting<Boolean> position;
   public final Setting<Boolean> swing = this.resolveSetting2(new BooleanSetting("Swing", false));
   public final Setting<Integer> swingSpeed = this.resolveSetting2(new NumberSetting<>("SwingSpeed", 6, 0, 25));
   public final Setting<Boolean> crystalsSpeed;
   public final Setting<Boolean> sneak;
   public final Setting<Boolean> noDeath;
   public final Setting<Boolean> limbs;
   public final Setting<Float> crystalsScaleValue;
   public final Setting<Boolean> playersStatic;
   public final Setting<AnimationsModule.SneakAnimation> sneakMode;
   public final Setting<Boolean> rotations;
   public final Setting<Boolean> crystalsScale;
   public final Setting<Boolean> crystalsSmooth;
   public final Setting<Boolean> crystals;
   public final Setting<Float> crystalsOffsetFactor;
   public final Setting<Boolean> crystalsOffset;

   public static boolean check() {
      return animationsModule.check7()
         && animationsModule.sneak.getValue()
         && animationsModule.sneakMode.getValue().equals(AnimationsModule.SneakAnimation.Custom);
   }

   public Setting<Integer> getSwingSpeed() {
      return this.swingSpeed;
   }

   public Setting<AnimationsModule.Selection> getSelection() {
      return this.selection;
   }

   public Setting<Boolean> getSpeed() {
      return this.speed;
   }

   public static float computeFloat(float f) {
      return animationsModule.check7() && animationsModule.crystals.getValue() && animationsModule.crystalsSpeed.getValue()
         ? f * animationsModule.crystalsSpeedFactor.getValue()
         : f;
   }

   public Setting<Boolean> getSwing() {
      return this.swing;
   }

   public AnimationsModule(PhobosClient phobosClient) {
      super(phobosClient, "Animations", Categories.render, "Manages animations");
      this.speed = this.resolveSetting2(new BooleanSetting("Speed", false));
      this.selection = this.resolveSetting2(new EnumSetting<>("Selection", AnimationsModule.Selection.All));
      this.limbs = this.resolveSetting2(new BooleanSetting("Limbs"));
      this.rotations = this.resolveSetting2(new BooleanSetting("Rotations"));
      this.position = this.resolveSetting2(new BooleanSetting("Position"));
      this.speedFactor = this.resolveSetting2(new NumberSetting<>("SpeedFactor", 1.0F, -3.0F, 3.0F));
      this.sneak = this.resolveSetting2(new BooleanSetting("Sneak", false));
      this.sneakMode = this.resolveSetting2(new EnumSetting<>("SneakMode", AnimationsModule.SneakAnimation.Lerp));
      this.crystals = this.resolveSetting2(new BooleanSetting("Crystals"));
      this.crystalsScale = this.resolveSetting2(new BooleanSetting("CrystalsScale"));
      this.crystalsScaleValue = this.resolveSetting2(new NumberSetting<>("CrystalsScaleValue", 2.0F, 0.0F, 3.0F));
      this.crystalsSpeed = this.resolveSetting2(new BooleanSetting("CrystalsSpeed"));
      this.crystalsSpeedFactor = this.resolveSetting2(new NumberSetting<>("CrystalsSpeedFactor", 1.0F, 0.0F, 100.0F));
      this.crystalsOffset = this.resolveSetting2(new BooleanSetting("CrystalsOffset"));
      this.crystalsOffsetFactor = this.resolveSetting2(new NumberSetting<>("CrystalsOffsetFactor", 1.0F, 0.0F, 1.0F));
      this.crystalsSmooth = this.resolveSetting2(new BooleanSetting("CrystalsSmooth"));
      this.players = this.resolveSetting2(new BooleanSetting("Players"));
      this.playersStatic = this.resolveSetting2(new BooleanSetting("PlayersStatic"));
      this.booleanValueSetting = this.resolveSetting2(new BooleanSetting("1.7").resolveSetting7("1.7 like animations"));
      this.noDeath = this.resolveSetting2(new BooleanSetting("NoDeath").resolveSetting7("Disables tilt when the entity is dead"));
      this.createBuilder()
         .resolveBuilder6(this.crystals)
         .resolveBuilder5(
            this.resolveSettingGroup(this.crystalsScale, this.crystalsScaleValue),
            this.resolveSettingGroup(this.crystalsSpeed, this.crystalsSpeedFactor),
            this.resolveSettingGroup(this.crystalsOffset, this.crystalsOffsetFactor)
         )
         .resolveBuilder(this.crystalsSmooth)
         .getSettingSubsection();
      this.resolveSettingGroup(this.players, this.playersStatic);
      this.resolveSettingGroup(this.swing, this.swingSpeed);
      this.resolveSettingGroup(this.speed, this.selection, this.limbs, this.rotations, this.position, this.speedFactor);
      this.resolveSettingGroup(this.sneak, this.sneakMode);
      this.handleListener(new Listener<SwingSpeedEvent>() {
         public void handleObject2(SwingSpeedEvent swingSpeedEvent) {
            if (AnimationsModule.this.swing.getValue()) {
               swingSpeedEvent.setIntValue(AnimationsModule.this.swingSpeed.getValue());
            }
         }
      });
      this.handleListener(new Listener<EntityAnimationState>() {
         public void handleObject2(EntityAnimationState entityAnimationState) {
            if (AnimationsModule.this.speed.getValue()) {
               if (AnimationsModule.this.selection.getValue().function.apply(entityAnimationState.getEntity())) {
                  entityAnimationState.setFloatValue(AnimationsModule.this.speedFactor.getValue());
                  entityAnimationState.setFlag2(AnimationsModule.this.limbs.getValue());
                  entityAnimationState.setFlag(AnimationsModule.this.rotations.getValue());
                  entityAnimationState.setFlag3(AnimationsModule.this.position.getValue());
               }
            }
         }
      });
      this.handleListener(new Listener<SneakAnimationState>() {
         public void handleObject2(SneakAnimationState sneakAnimationState) {
            if (AnimationsModule.this.sneak.getValue()) {
               sneakAnimationState.setSneakAnimation(AnimationsModule.this.sneakMode.getValue());
            }
         }
      });
      this.handleListener(
         new SafeListener<OnMouseButtonEvent>() {
            public void handleObject(
               OnMouseButtonEvent onMouseButtonEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (clientPlayerEntity.isUsingItem() && AnimationsModule.this.booleanValueSetting.getValue()) {
                  if (onMouseButtonEvent.getKeyboardMouseEnum().equals(KeyboardMouseEnum.Mouse)
                     && onMouseButtonEvent.getIntValue2() == 0
                     && onMouseButtonEvent.getIntValue() == 1) {
                     PlayerActionUtils.handleClientPlayerEntity7(clientPlayerEntity, Hand.MAIN_HAND);
                  }
               }
            }
         }
      );
      animationsModule = this;
   }

   public static float computeEndCrystalEntity(EndCrystalEntity endCrystalEntity) {
      if (animationsModule.check7() && animationsModule.crystals.getValue() && animationsModule.crystalsSmooth.getValue()) {
         IEndCrystalEntity iEndCrystalEntity = (IEndCrystalEntity)(Object)endCrystalEntity;
         FloatAnimator floatAnimator = iEndCrystalEntity.getAnimation();
         floatAnimator.handleFloat3(10.0F, 0.1F);
         return floatAnimator.getFloatValue2();
      } else {
         return 2.0F;
      }
   }

   public static boolean check2() {
      return animationsModule.check7() && animationsModule.players.getValue() && animationsModule.playersStatic.getValue();
   }

   public static boolean check3() {
      return animationsModule.check7() && animationsModule.booleanValueSetting.getValue();
   }

   public static float computeFloat2(float f) {
      return animationsModule.check7() && animationsModule.crystals.getValue() && animationsModule.crystalsOffset.getValue()
         ? f * animationsModule.crystalsOffsetFactor.getValue()
         : f;
   }

   public static float value() {
      return animationsModule.check7() && animationsModule.crystals.getValue() && animationsModule.crystalsScale.getValue()
         ? animationsModule.crystalsScaleValue.getValue()
         : 2.0F;
   }

   public static float computeFloat3(float f) {
      return f * animationsModule.speedFactor.getValue();
   }

   public static boolean check4() {
      return check9() && animationsModule.rotations.getValue();
   }

   public Setting<Boolean> getLimbs() {
      return this.limbs;
   }

   public static boolean check5() {
      return animationsModule.check7() && animationsModule.noDeath.getValue();
   }

   public static boolean check6() {
      return check9() && animationsModule.limbs.getValue();
   }

   public static boolean check8() {
      return check9() && animationsModule.position.getValue();
   }

   public static boolean check9() {
      return animationsModule.check7() && animationsModule.speed.getValue();
   }

   public Setting<Boolean> getBooleanValueSetting6() {
      return this.crystals;
   }

   public Setting<Boolean> getPosition() {
      return this.position;
   }

   public Setting<Boolean> getCrystalsSpeed() {
      return this.crystalsSpeed;
   }

   public Setting<Float> getFloatValueSetting2() {
      return this.speedFactor;
   }

   public Setting<AnimationsModule.SneakAnimation> getSneakMode() {
      return this.sneakMode;
   }

   public Setting<Float> getCrystalsScaleValue() {
      return this.crystalsScaleValue;
   }

   public static AnimationsModule getAnimationsModule() {
      return animationsModule;
   }

   public Setting<Boolean> getCrystalsScale() {
      return this.crystalsScale;
   }

   public Setting<Boolean> getNoDeath() {
      return this.noDeath;
   }

   public Setting<Float> getCrystalsOffsetFactor() {
      return this.crystalsOffsetFactor;
   }

   public Setting<Boolean> getRotations() {
      return this.rotations;
   }

   public Setting<Boolean> getPlayersStatic() {
      return this.playersStatic;
   }

   public Setting<Boolean> getSneak() {
      return this.sneak;
   }

   public Setting<Boolean> getCrystalsOffset() {
      return this.crystalsOffset;
   }

   public Setting<Boolean> getBooleanValueSetting() {
      return this.booleanValueSetting;
   }

   public Setting<Boolean> getBooleanValueSetting8() {
      return this.players;
   }

   public Setting<Boolean> getCrystalsSmooth() {
      return this.crystalsSmooth;
   }

   public Setting<Float> getCrystalsSpeedFactor() {
      return this.crystalsSpeedFactor;
   }

   private enum Selection {
      All(entity -> true),
      Self(entity -> entity instanceof ClientPlayerEntity),
      Others(entity -> !(entity instanceof ClientPlayerEntity));

      public final Function<Entity, Boolean> function;

      Selection(final Function<Entity, Boolean> function) {
         this.function = function;
      }
   }

   public enum SneakAnimation {
      Lerp,
      Custom;
   }
}
