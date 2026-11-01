package org.phobos.core.hud.element;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;
import org.earthhack.ducks.IBufferBuilderStorage;
import org.joml.Matrix4f;
import org.phobos.event.AddEntityEvent;
import org.phobos.event.RemoveEntityEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.module.client.HudModule;
import org.phobos.module.render.NametagsModule;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.chat.ChatTextUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.hud.IconListHudElement;
import org.phobos.core.hud.ListHudElement;
import org.phobos.core.math.MathUtils;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.setting.MultiSelectSetting;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.setting.Setting;

public class TextRadarHudElement extends IconListHudElement<Entity> {
   public final Setting<Boolean> distance;
   public final Setting<Map<TextRadarHudElement.Entry, Boolean>> types = this.resolveSetting2(
      new MultiSelectSetting<>("Types", TextRadarHudElement.Entry.class)
         .resolveMultiSelectSetting6(TextRadarHudElement.Entry.values())
         .resolveMultiSelectSetting3(TextRadarHudElement.Entry.Players)
   );
   public final Setting<Boolean> health = this.resolveSetting2(new BooleanSetting("Health"));
   public final Setting<Boolean> distanceColor;
   public final Setting<Boolean> healthColor = this.resolveSetting2(new BooleanSetting("HealthColor"));

   @Override
   public void handleDataType3(ListHudElement.DataType<Entity> dataType) {
      dataType.resolveDataType2(
         () -> !this.icons.getValue()
            ? null
            : (dataType2, matrixStack, d, d2, d3) -> {
               Entity entity2 = dataType2.getObject();
               if (entity2 != null && mc.getNetworkHandler() != null) {
                  switch (entity2) {
                     case PlayerEntity playerEntity:
                        PlayerListEntry playerListEntry = playerEntity instanceof AbstractClientPlayerEntity abstractClientPlayerEntity
                           ? abstractClientPlayerEntity.getPlayerListEntry()
                           : mc.getNetworkHandler().getPlayerListEntry(playerEntity.getUuid());
                        if (playerListEntry == null) {
                           return;
                        }

                        RenderUtils.handleMatrixStack5(matrixStack, playerListEntry.getSkinTextures().texture(), d, d2, d3);
                        break;
                     case ItemEntity itemEntity:
                        Immediate immediate = ((IBufferBuilderStorage)(Object)mc.getBufferBuilders()).getBackingContext();
                        matrixStack.push();
                        matrixStack.translate((float)d, (float)d2, 0.0F);
                        matrixStack.translate(4.0F, 5.0F, 0.0F);
                        matrixStack.multiplyPositionMatrix(new Matrix4f().scaling(1.0F, -1.0F, 0.0F));
                        matrixStack.scale((float)d3, (float)d3, 0.0F);
                        NametagsModule.handleItemStack2((ItemStack)dataType.getObject2(), 0.0F, 0.0F, 0.0F, matrixStack, immediate, mc.world);
                        matrixStack.pop();
                        immediate.draw();
                        break;
                     default:
                  }
               }
            }
      );
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, PlayerEntity playerEntity) {
      if (!playerEntity.isMainPlayer() && this.types.getValue().get(TextRadarHudElement.Entry.Players)) {
         this.handleDataType(
            ListHudElement.DataType.<Entity>createDataType(
                  playerEntity,
                  entity -> {
                     if (mc.player == null) {
                        return ListHudElement.ReservedString.createReservedString("");
                     }

                     String text = "§R";
                     StringBuilder stringBuilder = new StringBuilder();
                     StringBuilder stringBuilder2 = new StringBuilder();
                     String text2 = HudModule.getHudModule().getBrackets().getValue() ? "[" : "";
                     String text3 = HudModule.getHudModule().getBrackets().getValue() ? "]" : "";
                     if (this.distance.getValue()) {
                        double d = clientPlayerEntity.distanceTo(entity);
                        double d2 = MathUtils.computeDouble(d, 1);
                        stringBuilder.append(text2)
                           .append(!this.distanceColor.getValue() ? "" : ChatTextUtils.describeDouble(d))
                           .append(d2)
                           .append(text)
                           .append(text3)
                           .append(" ");
                        stringBuilder2.append(text2).append(d2).append(text3).append(" ");
                     }

                     UUID uUID = entity.getUuid();
                     if (this.phobosClient.getFriendList().testUUID(uUID)) {
                        stringBuilder.append(Formatting.AQUA);
                     } else if (this.phobosClient.getEnemyList().testUUID(uUID)) {
                        stringBuilder.append(Formatting.RED);
                     }

                     stringBuilder.append(playerEntity.getNameForScoreboard()).append(text);
                     stringBuilder2.append(playerEntity.getNameForScoreboard());
                     if (this.health.getValue()) {
                        float f = this.phobosClient
                           .getFeatureRegistry()
                           .getDamageTracker()
                           .computeLivingEntity((LivingEntity)entity, EntityUtils.computeLivingEntity((LivingEntity)entity));
                        float f2 = MathUtils.computeFloat5(f, 1);
                        stringBuilder.append(" ").append(!this.healthColor.getValue() ? "" : ChatTextUtils.describeFloat(f)).append(f2).append(text);
                        stringBuilder2.append(" ").append(f2);
                     }

                     return ListHudElement.ReservedString.createReservedString2(stringBuilder.toString(), stringBuilder2.toString());
                  }
               )
               .resolveDataType2(
                  () -> !this.icons.getValue()
                     ? null
                     : (dataType, matrixStack, d, d2, d3) -> {
                        PlayerListEntry playerListEntry = playerEntity instanceof AbstractClientPlayerEntity abstractClientPlayerEntity
                           ? abstractClientPlayerEntity.getPlayerListEntry()
                           : clientPlayerEntity.networkHandler.getPlayerListEntry(playerEntity.getUuid());
                        if (playerListEntry != null) {
                           RenderUtils.handleMatrixStack5(matrixStack, playerListEntry.getSkinTextures().texture(), d, d2, d3);
                        }
                     }
               )
         );
      }
   }

   public void handleItemEntity(ItemEntity itemEntity) {
      if (!itemEntity.getStack().isEmpty() && this.types.getValue().get(TextRadarHudElement.Entry.Items)) {
         this.handleDataType(
            ListHudElement.DataType.<Entity>createDataType2(itemEntity, (dataType, entity) -> {
                  ItemStack itemStack = (ItemStack)dataType.getObject2();
                  String text = itemStack.getItem().getName().getString();
                  String text2 = HudModule.getHudModule().getBrackets().getValue() ? "[" : "";
                  String text3 = HudModule.getHudModule().getBrackets().getValue() ? "]" : "";
                  int i = itemEntity.getStack().getCount();
                  String text4 = "§7" + text2 + "§f" + i + "§7" + text3 + " §f" + text;
                  String text5 = text2 + i + text3 + " " + text;
                  return ListHudElement.ReservedString.createReservedString2(text4, text5);
               })
               .resolveDataType4(dataType -> dataType.setObject2(new ItemStack(itemEntity.getStack().getItem(), itemEntity.getStack().getCount())))
               .resolveDataType(dataType -> !this.icons.getValue() ? null : (dataType2, matrixStack, d, d2, d3) -> {
                  Immediate immediate = ((IBufferBuilderStorage)(Object)mc.getBufferBuilders()).getBackingContext();
                  matrixStack.push();
                  matrixStack.translate((float)d, (float)d2, 0.0F);
                  matrixStack.translate(4.0F, 5.0F, 0.0F);
                  matrixStack.multiplyPositionMatrix(new Matrix4f().scaling(1.0F, -1.0F, 0.0F));
                  matrixStack.scale((float)d3, (float)d3, 0.0F);
                  NametagsModule.handleItemStack2((ItemStack)dataType.getObject2(), 0.0F, 0.0F, 0.0F, matrixStack, immediate, mc.world);
                  matrixStack.pop();
                  immediate.draw();
               })
         );
      }
   }

   public static void handleTextRadarHudElement(TextRadarHudElement textRadarHudElement, Predicate<ListHudElement.DataType<Entity>> predicate) {
      textRadarHudElement.handlePredicate(predicate);
   }

   public static boolean testTextRadarHudElement(
      TextRadarHudElement textRadarHudElement, Predicate<ListHudElement.DataType<Entity>> predicate
   ) {
      return textRadarHudElement.testPredicate(predicate);
   }

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      this.handleDrawContext(drawContext);
   }

   public void handleClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity, Entity entity) {
      switch (entity) {
         case PlayerEntity playerEntity:
            this.handleClientPlayerEntity(clientPlayerEntity, playerEntity);
            break;
         case ItemEntity itemEntity:
            this.handleItemEntity(itemEntity);
            break;
         default:
      }
   }

   public static void handleTextRadarHudElement2(
      TextRadarHudElement textRadarHudElement, Predicate<ListHudElement.DataType<Entity>> predicate
   ) {
      textRadarHudElement.handlePredicate(predicate);
   }

   public TextRadarHudElement() {
      super("TextRadar");
      this.distance = this.resolveSetting2(new BooleanSetting("Distance"));
      this.distanceColor = this.resolveSetting2(new BooleanSetting("DistanceColor"));
      this.resolveSettingGroup(this.health, this.healthColor);
      this.resolveSettingGroup(this.distance, this.distanceColor);
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               List<Entity> list = Lists.newArrayList(clientWorld.getEntities());
               TextRadarHudElement.handleTextRadarHudElement2(
                  TextRadarHudElement.this, dataType -> !list.contains(dataType.getObject())
               );

               for (Entity entity : list) {
                  if (!(entity instanceof PlayerEntity playerEntity && playerEntity.isMainPlayer())
                     && !TextRadarHudElement.testTextRadarHudElement(
                        TextRadarHudElement.this, dataType -> Objects.equals(dataType.getObject(), entity)
                     )) {
                     TextRadarHudElement.this.handleClientPlayerEntity2(clientPlayerEntity, entity);
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<AddEntityEvent>() {
            public void handleObject(
               AddEntityEvent addEntityEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               TextRadarHudElement.this.handleClientPlayerEntity2(clientPlayerEntity, addEntityEvent.getEntity());
            }
         }
      );
      this.handleListener(
         new SafeListener<RemoveEntityEvent>() {
            public void handleObject(
               RemoveEntityEvent removeEntityEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (removeEntityEvent.getEntity() instanceof PlayerEntity || removeEntityEvent.getEntity() instanceof ItemEntity) {
                  TextRadarHudElement.handleTextRadarHudElement(
                     TextRadarHudElement.this, dataType -> Objects.equals(dataType.getObject(), removeEntityEvent.getEntity())
                  );
               }
            }
         }
      );
      ModuleListenerUtils.handleListenerHolder(this, () -> this.handleFlag(true));
   }

   private enum Entry {
      Players,
      Items;
   }
}
