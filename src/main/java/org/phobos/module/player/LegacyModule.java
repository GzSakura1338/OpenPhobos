package org.phobos.module.player;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.phobos.api.Named;
import org.phobos.event.Listener;
import org.phobos.module.Module;
import org.phobos.core.module.Categories;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.event.events.HeldItemRenderEvent;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.setting.Setting;

public class LegacyModule extends Module {
   public static LegacyModule legacyModule;
   public final Setting<Float> offset;
   public boolean flag;
   public final Setting<LegacyModule.Animation> animation = this.resolveSetting2(new EnumSetting<>("Animation", LegacyModule.Animation.Default));
   public final Setting<Float> scale;

   public LegacyModule(PhobosClient phobosClient) {
      super(phobosClient, "1.8", Categories.player, "Tweaks some game mechanics to match 1.8");
      this.offset = this.resolveSetting2(new NumberSetting<>("Offset", 0.1F, 0.05F, 0.3F).resolveSetting4(this.animation, LegacyModule.Animation.OneSeven));
      this.scale = this.resolveSetting2(new NumberSetting<>("Scale", 0.9F, 0.1F, 1.0F).resolveSetting4(this.animation, LegacyModule.Animation.OneSeven));
      this.flag = false;
      legacyModule = this;
      this.handleListener(new Listener<HeldItemRenderEvent>() {
         public void handleObject2(HeldItemRenderEvent heldItemRenderEvent) {
            if (heldItemRenderEvent.getHand().equals(Hand.OFF_HAND)) {
               heldItemRenderEvent.setCancelled(true);
            }
         }
      });
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }

   public static boolean check() {
      return legacyModule.flag;
   }

   public static boolean testMatrixStack(MatrixStack matrixStack, Arm arm, float f, float f2) {
      if (!check2()) {
         return false;
      }

      legacyModule.animation.getValue().transform(legacyModule, matrixStack, arm, f2, f);
      return true;
   }

   public static void handleMatrixStack(MatrixStack matrixStack, Arm arm) {
      int i = arm == Arm.RIGHT ? 1 : -1;
      matrixStack.translate(i * 0.56F, -0.52F, -0.72F);
   }

   public static boolean check2() {
      return mc.player != null && mc.player.getMainHandStack().getItem() instanceof SwordItem && legacyModule != null && legacyModule.check7()
         ? mc.player != null && testClientPlayerEntity(mc.player) || legacyModule.flag
         : false;
   }

   public static LegacyModule getLegacyModule() {
      return legacyModule;
   }

   public static boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      return clientPlayerEntity.isUsingItem() && clientPlayerEntity.getActiveItem().getUseAction().equals(UseAction.BLOCK);
   }

   public static void handleFlag(boolean flag) {
      legacyModule.flag = flag;
   }

   public enum Animation implements Named {
      Default("Default") {},
      OneSeven("1.7") {
         @Override
         public void transform(LegacyModule module, MatrixStack matrices, Arm arm, float equipProgress, float swingProgress) {
            matrices.translate(Arm.RIGHT.equals(arm) ? -0.1F : 0.1F, module.offset.getValue(), 0.0F);
            LegacyModule.handleMatrixStack(matrices, arm);
            LegacyModule.Animation.applyOffset(matrices, arm, swingProgress * module.scale.getValue());
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-102.25F));
            matrices.multiply((Arm.RIGHT.equals(arm) ? RotationAxis.POSITIVE_Y : RotationAxis.NEGATIVE_Y).rotationDegrees(13.365F));
            matrices.multiply((Arm.RIGHT.equals(arm) ? RotationAxis.POSITIVE_Z : RotationAxis.NEGATIVE_Z).rotationDegrees(78.05F));
         }
      },
      Push("Push") {
         @Override
         public void transform(LegacyModule module, MatrixStack matrices, Arm arm, float equipProgress, float swingProgress) {
            matrices.translate(Arm.RIGHT.equals(arm) ? -0.1F : 0.1F, 0.1F, 0.0F);
            LegacyModule.handleMatrixStack(matrices, arm);
            float g = MathHelper.sin(MathHelper.sqrt(swingProgress) * __pi());
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((Arm.RIGHT.equals(arm) ? 1 : -1) * g * 10.0F));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(g * -35.0F));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-102.25F));
            matrices.multiply((Arm.RIGHT.equals(arm) ? RotationAxis.POSITIVE_Y : RotationAxis.NEGATIVE_Y).rotationDegrees(13.365F));
            matrices.multiply((Arm.RIGHT.equals(arm) ? RotationAxis.POSITIVE_Z : RotationAxis.NEGATIVE_Z).rotationDegrees(78.05F));
         }
      };

      public final String name;

      @Override
      public String getName() {
         return this.name;
      }

      public static void applyOffset(MatrixStack matrices, Arm arm, float swingProgress) {
         float armSide = arm == Arm.RIGHT ? 1.0F : -1.0F;
         float f = MathHelper.sin(swingProgress * swingProgress * __pi());
         matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(armSide * (45.0F + f * -20.0F)));
         float g = MathHelper.sin(MathHelper.sqrt(swingProgress) * __pi());
         matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(armSide * g * -20.0F));
         matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(g * -80.0F));
         matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(armSide * -45.0F));
      }

      Animation(final String name) {
         this.name = name;
      }

      public static float __pi() {
         return (float) Math.PI;
      }

      public void transform(LegacyModule module, MatrixStack matrices, Arm arm, float equipProgress, float swingProgress) {
         float g = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI) * 2.2F;
         LegacyModule.handleMatrixStack(matrices, arm);
         matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
         matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-30.0F * (1.0F - g) - 20.0F));
         matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(100.0F));
      }
   }
}
