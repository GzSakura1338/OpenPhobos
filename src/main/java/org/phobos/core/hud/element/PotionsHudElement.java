package org.phobos.core.hud.element;

import java.awt.Color;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.hud.ListHudElement.ReservedString;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.module.client.HudModule;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.hud.IconListHudElement;
import org.phobos.core.hud.ListHudElement;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.setting.Setting;
import org.phobos.core.hud.StatusEffectColors;

public class PotionsHudElement extends IconListHudElement<StatusEffectInstance> {
   public final Setting<PotionsHudElement.PotionColor> color = this.resolveSetting2(
      new EnumSetting<>("Color", PotionsHudElement.PotionColor.Sync)
   );
   public final Setting<Boolean> pulse = this.resolveSetting2(new BooleanSetting("Pulse"));
   public final Setting<Integer> pulseSeconds = this.resolveSetting2(new NumberSetting<>("PulseSeconds", 10, 5, 30));

   public static boolean testPotionsHudElement(
      PotionsHudElement potionsHudElement, Predicate<ListHudElement.DataType<StatusEffectInstance>> predicate
   ) {
      return potionsHudElement.testPredicate(predicate);
   }

   public static void handlePotionsHudElement(PotionsHudElement potionsHudElement, Predicate<ListHudElement.DataType<StatusEffectInstance>> predicate) {
      potionsHudElement.handlePredicate(predicate);
   }

   public static Setting<Boolean> resolveSetting3(PotionsHudElement potionsHudElement) {
      return potionsHudElement.icons;
   }

   public PotionsHudElement() {
      super("Potions");
      this.resolveSettingGroup(this.pulse, this.pulseSeconds);
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               PotionsHudElement.handlePotionsHudElement(
                  PotionsHudElement.this,
                  dataType -> dataType.getObject() != null && !clientPlayerEntity.hasStatusEffect(((StatusEffectInstance)dataType.getObject()).getEffectType())
               );

               for (StatusEffectInstance statusEffectInstance : clientPlayerEntity.getStatusEffects()) {
                  if (!PotionsHudElement.testPotionsHudElement(
                     PotionsHudElement.this,
                     dataType -> dataType.getObject() != null
                        && ((StatusEffect)((StatusEffectInstance)dataType.getObject()).getEffectType().value())
                           .equals(statusEffectInstance.getEffectType().value())
                        && PotionsHudElement.this.testDataType3(dataType, statusEffectInstance)
                  )) {
                     PotionsHudElement.handlePotionsHudElement2(
                        PotionsHudElement.this,
                        ListHudElement.DataType.<StatusEffectInstance>createDataType4(statusEffectInstance, statusEffectInstancex -> {
                              long l = statusEffectInstancex.getDuration();
                              float f = (float)l / 20.0F;
                              long l2 = TimeUnit.SECONDS.toMinutes((long)f);
                              long l3 = TimeUnit.SECONDS.toSeconds((long)f) % 60L;
                              int i = statusEffectInstancex.getAmplifier() + 1;
                              Text text = ((StatusEffect)statusEffectInstancex.getEffectType().value()).getName();
                              String text2 = String.format("%02d:%02d", l2, l3);
                              String text3 = text.getString() + (i > 1 ? " " + i + " " : " ") + text2;
                              String text4 = HudModule.getHudModule().describeText2(text.getString() + (i > 1 ? " " + i + " " : " "), text2);
                              return ReservedString.createReservedString2(text4, text3);
                           }, statusEffectInstancex -> {
                              switch ((PotionsHudElement.PotionColor)PotionsHudElement.this.color.getValue()) {
                                 case Default:
                                    return new Color(((StatusEffect)statusEffectInstancex.getEffectType().value()).getColor());
                                 case Old:
                                    RegistryEntry<StatusEffect> registryEntry = statusEffectInstancex.getEffectType();
                                    Color color = StatusEffectColors.resolveColor(registryEntry);
                                    return color == null ? new Color(((StatusEffect)registryEntry.value()).getColor()) : color;
                                 default:
                                    return null;
                              }
                           })
                           .resolveDataType2(
                              () -> !PotionsHudElement.resolveSetting3(PotionsHudElement.this).getValue()
                                 ? null
                                 : (dataType, matrixStack, d, d2, d3) -> PotionsHudElement.this.handleMatrixStack(
                                    matrixStack, d, d2, d3, dataType.getObject()
                                 )
                           )
                     );
                  }
               }
            }
         }
      );
   }

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      this.handleDrawContext(drawContext);
   }

   @Override
   public float computeDataType3(ListHudElement.DataType<StatusEffectInstance> dataType) {
      if (this.testDataType2(dataType)) {
         float f = dataType.getFloatAnimator().getFloatValue2();
         if (f > 0.8F) {
            return 0.0F;
         } else {
            return f < 0.2F ? 1.0F : dataType.getFloatAnimator().getFloatValue();
         }
      } else {
         return super.computeDataType3(dataType);
      }
   }

   public void handleMatrixStack(MatrixStack matrixStack, double d, double d2, double d3, @Nullable StatusEffectInstance statusEffectInstance) {
      if (statusEffectInstance != null) {
         StatusEffectSpriteManager statusEffectSpriteManager = mc.getStatusEffectSpriteManager();
         RegistryEntry<StatusEffect> registryEntry = statusEffectInstance.getEffectType();
         Sprite sprite = statusEffectSpriteManager.getSprite(registryEntry);
         RenderUtils.handleMatrixStack(matrixStack, d, d2, d3, d3, sprite);
      }
   }

   public boolean testDataType2(ListHudElement.DataType<StatusEffectInstance> dataType) {
      StatusEffectInstance statusEffectInstance = dataType.getObject();
      if (statusEffectInstance != null && this.pulse.getValue() && !dataType.isFlag()) {
         long l = statusEffectInstance.getDuration();
         int i = Math.round((float)l / 20.0F);
         return i <= this.pulseSeconds.getValue();
      } else {
         return false;
      }
   }

   public static void handlePotionsHudElement2(
      PotionsHudElement potionsHudElement, ListHudElement.DataType dataType
   ) {
      potionsHudElement.handleDataType(dataType);
   }

   public boolean testDataType3(ListHudElement.DataType<StatusEffectInstance> dataType, StatusEffectInstance statusEffectInstance) {
      dataType.setObject(statusEffectInstance);
      return true;
   }

   @Override
   public void handleDataType3(ListHudElement.DataType<StatusEffectInstance> dataType) {
      dataType.resolveDataType2(
         () -> !this.icons.getValue()
            ? null
            : (dataTypex, matrixStack, d, d2, d3) -> this.handleMatrixStack(matrixStack, d, d2, d3, (StatusEffectInstance)dataTypex.getObject())
      );
   }

   @Override
   public float computeDataType(ListHudElement.DataType<StatusEffectInstance> dataType) {
      return this.testDataType2(dataType) ? 0.05F : super.computeDataType(dataType);
   }

   private enum PotionColor {
      Sync,
      Default,
      Old;
   }
}
