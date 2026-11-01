package org.phobos.core.hud.element;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.function.TriConsumer;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.event.events.PacketEvent.PostSend;
import org.phobos.core.hud.ListHudElement.ReservedString;
import org.phobos.event.EntityStatusEvent;
import org.phobos.event.Listener;
import org.phobos.event.SafeListener;
import org.phobos.module.client.MediaModule;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.config.ConfigManager;
import org.phobos.core.hud.IconListHudElement;
import org.phobos.core.hud.ListHudElement;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.module.ModuleToggleNotice;
import org.phobos.core.PhobosClient;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.setting.Setting;

public class NotificationsHudElement extends IconListHudElement<NotificationsHudElement.Notification> {
   public final Setting<Boolean> combat;
   public static final Identifier identifier = Identifier.of("phobos", "icons/totem.png");
   public final Map<String, Long> textMap;
   public static final Identifier identifier2 = Identifier.of("phobos", "icons/cross.png");
   public final Setting<Long> fade;
   public final Setting<Boolean> modules;
   public final Setting<Boolean> self;
   public final Setting<Long> combatDelay;
   public final Setting<Boolean> replace = this.resolveSetting2(new BooleanSetting("Replace"));
   public static final Identifier identifier3 = Identifier.of("phobos", "icons/checkmark.png");
   public final Setting<Boolean> totems;
   public static final Identifier identifier4 = Identifier.of("phobos", "icons/notification_red.png");

   @Override
   public void handleDataType3(ListHudElement.DataType<NotificationsHudElement.Notification> dataType) {
      dataType.resolveDataType2(() -> !this.icons.getValue() ? null : (dataTypex, matrixStack, d, d2, d3) -> {
         NotificationsHudElement.Notification notification = (NotificationsHudElement.Notification)dataTypex.getObject();
         if (notification != null) {
            RenderSystem.setShaderTexture(0, notification.icon());
            RenderUtils.handleMatrixStack2(matrixStack, d, d2, d3);
         }
      });
   }

   public static Setting<Boolean> resolveSetting3(NotificationsHudElement notificationsHudElement) {
      return notificationsHudElement.icons;
   }

   public static void handleNotificationsHudElement(
      NotificationsHudElement notificationsHudElement, ListHudElement.DataType dataType
   ) {
      notificationsHudElement.handleDataType(dataType);
   }

   public static PhobosClient resolvePhobosClient(NotificationsHudElement notificationsHudElement) {
      return notificationsHudElement.phobosClient;
   }

   public static Setting<Boolean> resolveSetting4(NotificationsHudElement notificationsHudElement) {
      return notificationsHudElement.icons;
   }

   public void handleText(String text) {
      this.handlePredicate(dataType -> dataType.getObject() != null && dataType.getObject().id != null && dataType.getObject().id.equalsIgnoreCase(text));
   }

   public static void handleNotificationsHudElement2(
      NotificationsHudElement notificationsHudElement, ListHudElement.DataType dataType
   ) {
      notificationsHudElement.handleDataType(dataType);
   }

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      this.handlePredicate(dataType -> dataType.getLongValue() + this.fade.getValue() < System.currentTimeMillis());
      this.textMap.entrySet().removeIf(entry -> entry.getValue() < System.currentTimeMillis());
      this.handleDrawContext(drawContext);
   }

   public static PhobosClient resolvePhobosClient2(NotificationsHudElement notificationsHudElement) {
      return notificationsHudElement.phobosClient;
   }

   public static Setting<Boolean> resolveSetting5(NotificationsHudElement notificationsHudElement) {
      return notificationsHudElement.icons;
   }

   public static Setting<Boolean> resolveSetting6(NotificationsHudElement notificationsHudElement) {
      return notificationsHudElement.icons;
   }

   public static void handleNotificationsHudElement3(
      NotificationsHudElement notificationsHudElement, ListHudElement.DataType dataType
   ) {
      notificationsHudElement.handleDataType(dataType);
   }

   public NotificationsHudElement() {
      super("Notifications");
      this.modules = this.resolveSetting2(new BooleanSetting("Modules", true).resolveSetting7("Notify module status"));
      this.totems = this.resolveSetting2(new BooleanSetting("Totems", false).resolveSetting7("Totem pops"));
      this.self = this.resolveSetting2(new BooleanSetting("Self", false).resolveSetting7("Count self-pops"));
      this.combat = this.resolveSetting2(new BooleanSetting("Combat").resolveSetting7("Combat reminders"));
      this.combatDelay = this.resolveSetting2(new LongNumberSetting("CombatDelay", 10000L, 1000L, 60000L));
      this.fade = this.resolveSetting2(new LongNumberSetting("Fade", 500L, 10L, 5000L));
      this.textMap = new HashMap<>();
      this.resolveSettingGroup(this.totems, this.self);
      this.resolveSettingGroup(this.combat, this.combatDelay);
      this.handleListener(
         new SafeListener<ModuleToggleNotice>() {
            public void handleObject(
               ModuleToggleNotice moduleToggleNotice,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!ConfigManager.flag) {
                  if (NotificationsHudElement.this.modules.getValue()) {
                     if (NotificationsHudElement.this.replace.getValue()) {
                        NotificationsHudElement.this.handleText(moduleToggleNotice.getModule().describe4());
                     }

                     String text = (moduleToggleNotice.flag ? Formatting.GREEN : Formatting.RED)
                        + moduleToggleNotice.module.describe4()
                        + Formatting.RESET
                        + (moduleToggleNotice.flag ? " enabled." : " disabled.");
                     String text2 = moduleToggleNotice.module.describe4() + (moduleToggleNotice.flag ? " enabled." : " disabled.");
                     NotificationsHudElement.handleNotificationsHudElement(
                        NotificationsHudElement.this,
                        ListHudElement.DataType.createDataType3(
                              new NotificationsHudElement.Notification(
                                 moduleToggleNotice.getModule().describe4(),
                                 moduleToggleNotice.flag ? NotificationsHudElement.identifier3 : NotificationsHudElement.identifier2
                              ),
                              ReservedString.createReservedString2(text, text2)
                           )
                           .resolveDataType2(
                              () -> !NotificationsHudElement.resolveSetting3(NotificationsHudElement.this).getValue()
                                 ? null
                                 : (dataType, matrixStack, d, d2, d3) -> {
                                    NotificationsHudElement.Notification notification = dataType.getObject();
                                    if (notification != null) {
                                       RenderSystem.setShaderTexture(0, notification.icon());
                                       RenderUtils.handleMatrixStack2(matrixStack, d, d2, d3);
                                    }
                                 }
                           )
                     );
                  }
               }
            }
         }
      );
      this.handleListener(
         new Listener<EntityStatusEvent>() {
            public void handleObject2(EntityStatusEvent entityStatusEvent) {
               if (NotificationsHudElement.this.totems.getValue()
                  && entityStatusEvent.entity() instanceof PlayerEntity playerEntity
                  && (!Objects.equals(entityStatusEvent.entity(), MinecraftHolder.mc.player) || NotificationsHudElement.this.self.getValue())) {
                  int i = NotificationsHudElement.resolvePhobosClient3(NotificationsHudElement.this)
                     .getEntityIdManager()
                     .computeEntity(entityStatusEvent.entity());
                  boolean flag = entityStatusEvent.entity() instanceof ClientPlayerEntity;
                  if (NotificationsHudElement.this.replace.getValue()) {
                     NotificationsHudElement.this.handleText("TotemPops" + playerEntity.getId());
                  }

                  String text = MediaModule.describeText(playerEntity.getNameForScoreboard());
                  String text2 = Formatting.GRAY
                     + (flag ? "You" : text)
                     + Formatting.RESET
                     + " ha%s popped ".formatted(flag ? "ve" : "s")
                     + Formatting.GRAY
                     + i
                     + Formatting.RESET
                     + " totem%s.".formatted(i > 1 ? "s" : "");
                  String text3 = (flag ? "You" : text) + " ha%s popped ".formatted(flag ? "ve" : "s") + i + " totems.";
                  NotificationsHudElement.handleNotificationsHudElement3(
                     NotificationsHudElement.this,
                     ListHudElement.DataType.createDataType3(
                           new NotificationsHudElement.Notification(
                              "TotemPops" + playerEntity.getId(), NotificationsHudElement.identifier
                           ),
                           ReservedString.createReservedString2(text2, text3)
                        )
                        .resolveDataType2(
                           () -> !NotificationsHudElement.resolveSetting4(NotificationsHudElement.this).getValue()
                              ? null
                              : (dataType, matrixStack, d, d2, d3) -> {
                                 NotificationsHudElement.Notification notification = dataType.getObject();
                                 if (notification != null) {
                                    RenderSystem.setShaderTexture(0, notification.icon());
                                    RenderUtils.handleMatrixStack2(matrixStack, d, d2, d3);
                                 }
                              }
                        )
                  );
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<PostSend<?>>() {
            public void handleObject(
               PostSend<?> postSend,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (NotificationsHudElement.this.combat.getValue()) {
                  switch (postSend.getPacket()) {
                     case PlayerInteractBlockC2SPacket playerInteractBlockC2SPacket:
                        ItemStack itemStack = clientPlayerEntity.getStackInHand(playerInteractBlockC2SPacket.getHand());
                        if (!itemStack.isOf(Items.END_CRYSTAL) || NotificationsHudElement.this.textMap.containsKey("Crystals")) {
                           return;
                        }

                        NotificationsHudElement.resolvePhobosClient(NotificationsHudElement.this)
                           .getInventoryManager()
                           .handleConsumer2(
                              itemSwitcher -> {
                                 int i = itemSwitcher.computePredicate(itemStackx -> itemStackx.isOf(Items.END_CRYSTAL));
                                 if (i < 64) {
                                    if (NotificationsHudElement.this.replace.getValue()) {
                                       NotificationsHudElement.this.handleText("CombatCrystals");
                                    }

                                    String text = "You only have " + Formatting.GRAY + i + Formatting.RESET + " crystals left!";
                                    String text2 = "You only have " + i + " crystals left!";
                                    NotificationsHudElement.handleNotificationsHudElement2(
                                       NotificationsHudElement.this,
                                       ListHudElement.DataType.createDataType3(
                                             new NotificationsHudElement.Notification(
                                                "CombatCrystals", NotificationsHudElement.identifier4
                                             ),
                                             ReservedString.createReservedString2(text, text2)
                                          )
                                          .resolveDataType2(
                                             () -> !NotificationsHudElement.resolveSetting6(NotificationsHudElement.this).getValue()
                                                ? null
                                                : (dataType, matrixStack, d, d2, d3) -> {
                                                   NotificationsHudElement.Notification notification = dataType.getObject();
                                                   if (notification != null) {
                                                      RenderSystem.setShaderTexture(0, notification.icon());
                                                      RenderUtils.handleMatrixStack2(matrixStack, d, d2, d3);
                                                   }
                                                }
                                          )
                                    );
                                    NotificationsHudElement.this.textMap
                                       .put("Crystals", System.currentTimeMillis() + NotificationsHudElement.this.combatDelay.getValue());
                                 }
                              }
                           );
                        break;
                     case PlayerInteractItemC2SPacket playerInteractItemC2SPacket:
                        label19: {
                           ItemStack itemStack2 = clientPlayerEntity.getStackInHand(playerInteractItemC2SPacket.getHand());
                           TriConsumer<Item, String, String> triConsumer = (item, text, text2) -> {
                              if (!NotificationsHudElement.this.textMap.containsKey(text2)) {
                                 NotificationsHudElement.resolvePhobosClient2(NotificationsHudElement.this)
                                    .getInventoryManager()
                                    .handleConsumer2(
                                       itemSwitcher -> {
                                          int i = itemSwitcher.computePredicate(itemStackArg -> itemStackArg.isOf(item));
                                          if (i < item.getMaxCount()) {
                                             if (NotificationsHudElement.this.replace.getValue()) {
                                                NotificationsHudElement.this.handleText("Combat" + text2);
                                             }

                                             String text3 = "You only have " + Formatting.GRAY + i + Formatting.RESET + " %s left!".formatted(text);
                                             String text4 = "You only have " + i + " %s left!".formatted(text);
                                             NotificationsHudElement.handleNotificationsHudElement4(
                                                NotificationsHudElement.this,
                                                ListHudElement.DataType.createDataType3(
                                                      new NotificationsHudElement.Notification(
                                                         "Combat" + text2, NotificationsHudElement.identifier4
                                                      ),
                                                      ReservedString.createReservedString2(text3, text4)
                                                   )
                                                   .resolveDataType2(
                                                      () -> !NotificationsHudElement.resolveSetting5(NotificationsHudElement.this)
                                                            .getValue()
                                                         ? null
                                                         : (dataType, matrixStack, d, d2, d3) -> {
                                                            NotificationsHudElement.Notification notification = dataType.getObject();
                                                            if (notification != null) {
                                                               RenderSystem.setShaderTexture(0, notification.icon());
                                                               RenderUtils.handleMatrixStack2(matrixStack, d, d2, d3);
                                                            }
                                                         }
                                                   )
                                             );
                                             NotificationsHudElement.this.textMap
                                                .put(text2, System.currentTimeMillis() + NotificationsHudElement.this.combatDelay.getValue());
                                          }
                                       }
                                    );
                              }
                           };
                           if (itemStack2.isOf(Items.EXPERIENCE_BOTTLE)) {
                              triConsumer.accept(Items.EXPERIENCE_BOTTLE, "exp", "XP");
                           }

                           if (itemStack2.isOf(Items.ENDER_PEARL)) {
                              triConsumer.accept(Items.ENDER_PEARL, "pearls", "Pearls");
                           }
                           break label19;
                        }
                     default:
                  }
               }
            }
         }
      );
   }

   public static PhobosClient resolvePhobosClient3(NotificationsHudElement notificationsHudElement) {
      return notificationsHudElement.phobosClient;
   }

   public static void handleNotificationsHudElement4(
      NotificationsHudElement notificationsHudElement, ListHudElement.DataType dataType
   ) {
      notificationsHudElement.handleDataType(dataType);
   }

   public record Notification(String id, Identifier icon) {
   }
}
