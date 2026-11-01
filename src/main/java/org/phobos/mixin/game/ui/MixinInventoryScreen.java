package org.phobos.mixin.game.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.earthhack.ducks.IInventoryScreen;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.phobos.api.TimeGate;
import org.phobos.api.KeyValue;
import org.phobos.core.inventory.ClickScheduler;
import org.phobos.core.inventory.ScheduledClick;
import org.phobos.core.PhobosClient;
import org.phobos.core.ClientHolder;
import org.phobos.module.combat.AutoTotemModule;
import org.phobos.module.render.ChamsModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class MixinInventoryScreen extends MixinAbstractInventoryScreen<PlayerScreenHandler> implements IInventoryScreen {
   @Unique
   private final TimeGate.Threaded openTime = new TimeGate.Threaded();

   @Overwrite
   @SuppressWarnings("overwrite")
   public static void drawEntity(
      DrawContext context, float x, float y, float size, Vector3f vector3f, Quaternionf quaternionf, @Nullable Quaternionf quaternionf2, LivingEntity entity
   ) {
      context.getMatrices().push();
      context.getMatrices().translate(x, y, 50.0);
      context.getMatrices().scale(size, size, -size);
      context.getMatrices().translate(vector3f.x, vector3f.y, vector3f.z);
      context.getMatrices().multiply(quaternionf);
      DiffuseLighting.method_34742();
      EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
      if (quaternionf2 != null) {
         entityRenderDispatcher.setRotation(quaternionf2.conjugate(new Quaternionf()).rotateY((float) Math.PI));
      }

      entityRenderDispatcher.setRenderShadows(false);
      RenderSystem.runAsFancy(() -> {
         ChamsModule.flag = true;
         entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, context.getMatrices(), context.getVertexConsumers(), 15728880);
         ChamsModule.flag = false;
      });
      context.draw();
      entityRenderDispatcher.setRenderShadows(true);
      context.getMatrices().pop();
      DiffuseLighting.enableGuiDepthLighting();
   }

   @Inject(method = "render", at = @At("TAIL"))
   private void renderHook(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      Slot focused = this.focusedSlot;
      if (focused != null) {
         ScheduledClick action = ClickScheduler.getINSTANCE().resolveScheduledClick(focused);
         if (action != null) {
            ClientHolder.getPhobosClient().getInventoryManager().handleConsumer2(ctx -> ctx.handleSlot(focused, action));
         } else if (AutoTotemModule.check4()) {
            if (focused.getStack().isOf(Items.TOTEM_OF_UNDYING)) {
               if (AutoTotemModule.check6()) {
                  this.doTick(focused);
               }
            }
         }
      }
   }

   @Override
   public TimeGate.Threaded getOpenTime() {
      return this.openTime;
   }

   @Unique
   private void doTick(Slot slot) {
      PhobosClient phobos = ClientHolder.getPhobosClient();
      phobos.getInventoryManager().handleConsumer2(ctx -> {
         if (!ctx.getSlot2().equals(slot)) {
            if (ctx.getSlot2().getStack().isOf(Items.TOTEM_OF_UNDYING) || AutoTotemModule.check8() && !AutoTotemModule.testFlag(true)) {
               KeyValue<Boolean, Integer> doubleHand = AutoTotemModule.getKeyValue();
               if (doubleHand.getObject()) {
                  ctx.handleInt(doubleHand.getObject2() - 1, mainhand -> {
                     if (!mainhand.getStack().isOf(Items.TOTEM_OF_UNDYING)) {
                        if (AutoTotemModule.testFlag2(true)) {
                           ctx.handleInt5(slot.id, mainhand.getIndex(), SlotActionType.SWAP);
                           AutoTotemModule.run2();
                        }
                     }
                  });
               }
            } else {
               ctx.handleInt5(slot.id, 40, SlotActionType.SWAP);
               AutoTotemModule.run2();
            }
         }
      });
   }

   @Inject(method = "init", at = @At("HEAD"))
   private void initHook(CallbackInfo ci) {
      this.openTime.run();
   }
}
