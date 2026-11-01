package org.phobos.core.hud.element;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.hud.HudElement;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.task.TaskScheduler;

public class ToastHudElement extends HudElement {
   public static final Identifier identifier = Identifier.of("phoboslite", "textures/icons/future.png");
   public final TaskScheduler taskScheduler;
   public final FloatAnimator floatAnimator;
   public final FloatAnimator floatAnimator2;
   public final FloatAnimator floatAnimator3;
   public float floatValue9;
   public String text;
   public static final Identifier identifier2 = Identifier.of("phoboslite", "textures/icons/info.png");
   public static final Identifier identifier3 = Identifier.of("phoboslite", "textures/icons/plus.png");
   public int intValue;
   public final Color color;
   public Identifier identifier4;
   public static final Identifier identifier5 = Identifier.of("phoboslite", "textures/icons/back.png");
   public final FloatAnimator floatAnimator4;
   public static final Identifier identifier6 = Identifier.of("phoboslite", "textures/icons/cancel.png");
   public long longValue;
   public final List<ToastHudElement.Notification> notificationList = new ArrayList<>();
   public long longValue2;
   public static final Identifier identifier7 = Identifier.of("phoboslite", "textures/icons/check.png");

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
   }

   public void handleDrawContext(DrawContext drawContext) {
      this.floatValue9 = this.floatAnimator2.getFloatValue2();
      this.floatAnimator2.run6();
      this.floatAnimator.run6();
      this.floatAnimator3.setFloatValue(Math.round(this.floatAnimator2.getFloatValue2()) > 85 ? 180.0F : 0.0F);
      this.floatAnimator4.setFloatValue(Math.round(this.floatAnimator2.getFloatValue2()) > 85 ? 250.0F : -70.0F);
      this.floatAnimator3.run6();
      this.floatAnimator4.run6();
      this.handleDrawContext5(
         drawContext,
         -this.floatAnimator2.getFloatValue2() * 2.0F,
         30.0,
         this.floatAnimator2.getFloatValue2() * 2.0F,
         5.0F + this.floatAnimator.getFloatValue2(),
         ColorUtils.createColor6(this.color, this.intValue - 105.0F)
      );
      this.handleDouble(
         -this.floatAnimator.getFloatValue2() * 2.0F, 30.0, -this.floatAnimator2.getFloatValue2() * 2.0F, 5.0F + this.floatAnimator.getFloatValue2()
      );
      if (this.identifier4 != null) {
         RenderSystem.setShaderTexture(0, this.identifier4);
         RenderUtils.handleMatrixStack2(
            drawContext.getMatrices(),
            this.getFloatValue2() + 90.0F - ((this.floatValue9 > this.floatAnimator2.getFloatValue2() ? 250.0F : this.floatAnimator4.getFloatValue2()) + 5.0F),
            45.0,
            30.0
         );
      }

      this.run3();
      this.handleDouble(110.0F - (this.floatAnimator4.getFloatValue2() + 14.0F), 30.0, 360.0, 5.0F + this.floatAnimator.getFloatValue2());
      boolean flag = this.longValue2 != -1L && this.longValue != -1L;
      if (flag) {
         long l = this.longValue - System.currentTimeMillis() + this.longValue2;
         if (l >= 0L) {
         }
      }
   }

   public boolean testText(String text, ToastHudElement.Icon icon, long l, long l2) {
      if (Math.round(this.floatAnimator2.getFloatValue2()) > 5) {
         return false;
      }

      boolean flag = l != -1L && l2 != -1L;
      this.identifier4 = icon.identifier;
      this.longValue2 = l;
      this.longValue = l2;
      this.text = text != null ? text : "";
      this.floatAnimator2.setFloatValue(90.0F);
      this.taskScheduler.handleLong2(200L, this::run5);
      this.taskScheduler.handleLong2(700L, () -> this.floatAnimator.setFloatValue(25.0F));
      this.taskScheduler.handleLong4(flag ? l2 : 3200L, () -> this.floatAnimator.setFloatValue(0.0F), 3);
      this.taskScheduler.handleLong4(flag ? l2 + 700L : 3900L, () -> {
         this.run16();
         this.floatAnimator2.setFloatValue(0.0F);
      }, 5);
      return true;
   }

   public void run2() {
      this.notificationList.removeIf(notification -> this.testText(notification.text, notification.icon, notification.timeStamp, notification.time));
      if (!this.notificationList.isEmpty() && this.floatAnimator.getFloatValue() >= 23.0F) {
         this.run();
      }
   }

   public ToastHudElement() {
      super("Notification", 0.0F, 0.0F, 0.0F, 10.0F);
      this.floatAnimator2 = new FloatAnimator();
      this.floatAnimator = new FloatAnimator();
      this.floatAnimator3 = new FloatAnimator();
      this.floatAnimator4 = new FloatAnimator();
      this.taskScheduler = new TaskScheduler();
      this.color = new Color(0, 0, 0, 0);
      this.identifier4 = null;
      this.longValue2 = -1L;
      this.longValue = -1L;
      this.intValue = 0;
      this.phobosClient.getEventBus().handleObject(this.taskScheduler);
   }

   public void run5() {
      this.intValue = 0;
      this.taskScheduler.handleLong2(100L, () -> this.intValue = 255);
      this.taskScheduler.handleLong2(200L, () -> this.intValue = 0);
      this.taskScheduler.handleLong2(300L, () -> this.intValue = 255);
   }

   public void run() {
      this.taskScheduler.handleInt(3);
      this.taskScheduler.handleInt(5);
      this.run16();
      this.floatAnimator2.setFloatValue(0.0F);
   }

   public void run16() {
      this.run5();
      this.taskScheduler.handleLong2(500L, () -> this.intValue = 255);
   }

   public enum Icon {
      BACK(ToastHudElement.identifier5),
      CANCEL(ToastHudElement.identifier6),
      CHECK(ToastHudElement.identifier7),
      FUTURE(ToastHudElement.identifier),
      INFO(ToastHudElement.identifier2),
      PLUS(ToastHudElement.identifier3);

      public final Identifier identifier;

      Icon(final Identifier identifier) {
         this.identifier = identifier;
      }

      public Identifier getIdentifier() {
         return this.identifier;
      }
   }

   private record Notification(String text, ToastHudElement.Icon icon, long timeStamp, long time) {
   }
}
