package org.phobos.core.hud;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.HudAnchors;
import org.phobos.type.TopBottomEnum;

public class HudLayoutManager implements MinecraftHolder {
   public final List<HudAnchors> hudAnchorsList;
   public final HudAnchors hudAnchors;
   public final HudAnchors hudAnchors2;
   public final HudAnchors hudAnchors3;
   public final HudAnchors hudAnchors4 = HudAnchors.createImpl();

   public int computeClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, int i) {
      int i3 = clientPlayerEntity.getMaxAir();
      int i4 = Math.min(clientPlayerEntity.getAir(), i3);
      if (!clientPlayerEntity.isSubmergedIn(FluidTags.WATER) && i4 >= i3) {
         return 0;
      }

      int i2 = this.computeInt(i) - 1;
      return i2 * 10;
   }

   public List<HudAnchors> getHudAnchorsList() {
      return this.hudAnchorsList;
   }

   @Nullable
   public PlayerEntity getPlayerEntity() {
      return mc.getCameraEntity() instanceof PlayerEntity playerEntity ? playerEntity : null;
   }

   public void handleDrawContext(DrawContext drawContext, PlayerEntity playerEntity, int i, int i2, int i3, int i4) {
      int i9 = playerEntity.getArmor();
      if (i9 > 0) {
         int i7 = i - (i2 - 1) * i3 - 10;
         int i8 = 0;

         for (int i6 = 0; i6 < 10; i6++) {
            int i5 = i4 + i6 * 8;
            if (i6 * 2 + 1 < i9) {
               i8 += 9;
            }

            if (i6 * 2 + 1 == i9) {
               i8 += 9;
            }

            if (i6 * 2 + 1 > i9) {
               i8 += 9;
            }
         }
      }
   }

   @Nullable
   public LivingEntity getLivingEntity() {
      PlayerEntity playerEntity = this.getPlayerEntity();
      if (playerEntity != null) {
         Entity entity = playerEntity.getVehicle();
         if (entity == null) {
            return null;
         }

         if (entity instanceof LivingEntity) {
            return (LivingEntity)entity;
         }
      }

      return null;
   }

   public void handleDrawContext2(DrawContext drawContext, ClientPlayerEntity clientPlayerEntity, int i) {
      int i2 = drawContext.getScaledWindowHeight() - 23;
      Arm arm = clientPlayerEntity.getMainArm().getOpposite();
      switch (arm) {
         case LEFT:
            this.hudAnchors.handleFloat6(i - 91 - 29, i2, 24.0F, 24.0F);
            break;
         case RIGHT:
            this.hudAnchors.handleFloat6(i + 91, i2, 24.0F, 24.0F);
      }
   }

   public HudAnchors getHudAnchors4() {
      return this.hudAnchors4;
   }

   public void handleDrawContext3(DrawContext drawContext, ClientPlayerEntity clientPlayerEntity, int i) {
      float f = (float)clientPlayerEntity.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH);
      int i2 = MathHelper.ceil(clientPlayerEntity.getAbsorptionAmount());
      int i3 = MathHelper.ceil((f + i2) / 2.0F / 10.0F);
      int i4 = i3 % 10;
      int i5 = i - 91;
      int i6 = drawContext.getScaledWindowHeight() - 30;
      this.hudAnchors2.handleFloat6(i5, i6, 81.0F, -(i4 * 10) - this.computeClientPlayerEntity2(clientPlayerEntity));
   }

   public HudAnchors getHudAnchors2() {
      return this.hudAnchors2;
   }

   public HudAnchors getHudAnchors() {
      return this.hudAnchors;
   }

   public HudAnchors getHudAnchors3() {
      return this.hudAnchors3;
   }

   public void handleDrawContext4(DrawContext drawContext) {
      ClientPlayerEntity clientPlayerEntity = mc.player;
      if (clientPlayerEntity != null) {
         int i = drawContext.getScaledWindowWidth() / 2;
         this.handleDrawContext5(drawContext, i);
         this.handleDrawContext2(drawContext, clientPlayerEntity, i);
         this.handleDrawContext6(drawContext, clientPlayerEntity, i);
         this.handleDrawContext3(drawContext, clientPlayerEntity, i);
      }
   }

   public void run() {
      for (HudAnchors hudAnchors : this.hudAnchorsList) {
         for (Map.Entry<HudElement, Set<TopBottomEnum>> entry : hudAnchors.getHudElementMap().entrySet()) {
            HudElement hudElement = entry.getKey();
            if (!hudElement.isFlag72() && !hudElement.isFlag4() && !hudElement.isFlag3()) {
               for (TopBottomEnum topBottomEnum : entry.getValue()) {
                  topBottomEnum.retain(hudAnchors, hudElement);
                  if (!hudElement.getHudElementMap().isEmpty()) {
                     hudElement.run11();
                     hudElement.run12();
                  }
               }
            }
         }
      }
   }

   public void handleDrawContext5(DrawContext drawContext, int i) {
      int i2 = i - 91;
      int i3 = drawContext.getScaledWindowHeight() - 22;
      this.hudAnchors4.handleFloat6(i2, i3, 182.0F, 22.0F);
   }

   public int computeInt(int i) {
      return (int)Math.ceil(i / 10.0);
   }

   public int computeLivingEntity(@Nullable LivingEntity livingEntity) {
      if (livingEntity != null && livingEntity.isLiving()) {
         float f = livingEntity.getMaxHealth();
         int i = (int)(f + 0.5F) / 2;
         if (i > 30) {
            i = 30;
         }

         return i;
      } else {
         return 0;
      }
   }

   public HudLayoutManager() {
      this.hudAnchors2 = HudAnchors.createImpl();
      this.hudAnchors3 = HudAnchors.createImpl();
      this.hudAnchors = HudAnchors.createImpl();
      this.hudAnchorsList = ImmutableList.of(this.hudAnchors4, this.hudAnchors2, this.hudAnchors3, this.hudAnchors);
   }

   public int computeClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity) {
      int i = clientPlayerEntity.getArmor();
      return i > 0 ? 9 : 0;
   }

   public void handleDrawContext6(DrawContext drawContext, ClientPlayerEntity clientPlayerEntity, int i) {
      LivingEntity livingEntity = this.getLivingEntity();
      int i2 = this.computeLivingEntity(livingEntity);
      int i3 = i + 91;
      int i4 = drawContext.getScaledWindowHeight() - 30;
      if (i2 != 0) {
         assert livingEntity != null;
         this.hudAnchors3
            .handleFloat6(
               i3 - 81,
               i4,
               81.0F,
               (int)(-Math.ceil((livingEntity.getMaxHealth() - 1.0F) / 20.0F) * 10.0) + this.computeClientPlayerEntity(clientPlayerEntity, i2)
            );
      } else {
         this.hudAnchors3.handleFloat6(i3 - 81, i4, 81.0F, -9 + this.computeClientPlayerEntity(clientPlayerEntity, i2));
      }
   }
}
