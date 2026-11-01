package org.phobos.core.hud.element;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.phobos.data.AttackersRecord;
import org.phobos.event.SafeListener;
import org.phobos.module.client.MediaModule;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.hud.IconListHudElement;
import org.phobos.core.hud.ListHudElement;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.setting.Setting;

public class KillfeedHudElement extends IconListHudElement<Object> {
   public final Setting<Long> fade = this.resolveSetting2(new LongNumberSetting("Fade", 1000L, 250L, 5000L, true));
   public static final Identifier identifier = Identifier.of("phobos", "icons/skull.png");
   public final Setting<Boolean> test = this.resolveSetting2(
      new BooleanSetting("Test").resolveSetting7("Adds a fake entry, use this to see the position of the element")
   );

   public static void handleKillfeedHudElement(
      KillfeedHudElement killfeedHudElement, ListHudElement.DataType dataType
   ) {
      killfeedHudElement.handleDataType(dataType);
   }

   public static Setting<Boolean> resolveSetting3(KillfeedHudElement killfeedHudElement) {
      return killfeedHudElement.icons;
   }

   public KillfeedHudElement() {
      super("Killfeed");
      this.handleListener(
         new SafeListener<AttackersRecord>() {
            public void handleObject(
               AttackersRecord attackersRecord,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (mc.getNetworkHandler() != null) {
                  if (attackersRecord.testPlayerEntity(clientPlayerEntity) && attackersRecord.victim() != null) {
                     String text = attackersRecord.victim().getNameForScoreboard();
                     String text2 = MediaModule.describeText(clientPlayerEntity.getNameForScoreboard());
                     StringBuilder stringBuilder = new StringBuilder();
                     StringBuilder stringBuilder2 = new StringBuilder();
                     stringBuilder.append(text2);
                     stringBuilder2.append(text2);

                     for (PlayerEntity playerEntity : attackersRecord.attackers()) {
                        if (playerEntity != null && !playerEntity.equals(clientPlayerEntity)) {
                           stringBuilder.append(" §f+§r ").append(playerEntity.getNameForScoreboard());
                           stringBuilder2.append(" + ").append(playerEntity.getNameForScoreboard());
                        }
                     }

                     stringBuilder.append(" §f->§r ").append(text);
                     stringBuilder2.append(" -> ").append(text);
                     KillfeedHudElement.handleKillfeedHudElement(
                        KillfeedHudElement.this,
                        ListHudElement.DataType.resolveDataType3(
                              ListHudElement.ReservedString.createReservedString2(stringBuilder.toString(), stringBuilder2.toString())
                           )
                           .resolveDataType(
                              dataType -> !KillfeedHudElement.resolveSetting3(KillfeedHudElement.this).getValue()
                                 ? null
                                 : (dataType2, matrixStack, d, d2, d3) -> {
                                    PlayerEntity playerEntityx = (PlayerEntity)dataType.getObject2();
                                    PlayerListEntry playerListEntry = playerEntityx instanceof AbstractClientPlayerEntity abstractClientPlayerEntity
                                       ? abstractClientPlayerEntity.getPlayerListEntry()
                                       : mc.getNetworkHandler().getPlayerListEntry(playerEntityx.getUuid());
                                    if (playerListEntry != null) {
                                       RenderUtils.handleMatrixStack5(matrixStack, playerListEntry.getSkinTextures().texture(), d, d2, d3);
                                    }
                                 }
                           )
                           .resolveDataType4(dataType -> dataType.setObject2(attackersRecord.victim()))
                     );
                  }
               }
            }
         }
      );
      this.test
         .handleConsumer2(
            booleanValue -> {
               if (booleanValue && mc.player != null) {
                  this.handleDataType(
                     ListHudElement.DataType.<Object>resolveDataType3(
                           ListHudElement.ReservedString.createReservedString2("Attacker §f->§r Victim", "Attacker -> Victim")
                        )
                        .resolveDataType2(() -> !this.icons.getValue() ? null : (dataType, matrixStack, d, d2, d3) -> {
                           RenderSystem.setShaderTexture(0, identifier);
                           RenderUtils.handleMatrixStack2(matrixStack, d, d2, d3);
                        })
                  );
                  this.test.setValue(false);
               }
            }
         );
   }

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      this.handlePredicate(dataType -> dataType.getLongValue() + this.fade.getValue() < System.currentTimeMillis());
      this.handleDrawContext(drawContext);
   }

   @Override
   public void handleDataType3(ListHudElement.DataType<Object> dataType) {
      dataType.resolveDataType(
         dataTypex -> this.icons.getValue() && mc.getNetworkHandler() != null
            ? (dataType2, matrixStack, d, d2, d3) -> {
               PlayerEntity playerEntity = (PlayerEntity)dataTypex.getObject2();
               PlayerListEntry playerListEntry = playerEntity instanceof AbstractClientPlayerEntity abstractClientPlayerEntity
                  ? abstractClientPlayerEntity.getPlayerListEntry()
                  : mc.getNetworkHandler().getPlayerListEntry(playerEntity.getUuid());
               if (playerListEntry != null) {
                  RenderUtils.handleMatrixStack5(matrixStack, playerListEntry.getSkinTextures().texture(), d, d2, d3);
               }
            }
            : null
      );
   }
}
