package org.phobos.core.hud.element;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.phobos.event.AddMessageEvent;
import org.phobos.event.SafeListener;
import org.phobos.module.client.FontModule;
import org.phobos.module.client.HudModule;
import org.phobos.type.CenterRightEnum;
import org.phobos.type.TopCenterEnum;
import org.phobos.core.hud.AnimatedListHudElement;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.chat.ChatTextUtils;
import org.phobos.core.hud.ListHudElement;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.Setting;
import org.phobos.core.chat.SignedMessage;

public class ChatHudElement extends AnimatedListHudElement<MessageSignatureData> {
   public final Setting<Integer> limit;
   public static ChatHudElement chatHudElement;
   public final Setting<Boolean> color = this.resolveSetting2(new BooleanSetting("Color"));
   public final Setting<Float> scale;

   public void handleText(String text, MessageSignatureData messageSignatureData) {
      this.handleDataType(
         ListHudElement.DataType.createDataType(
            messageSignatureData, messageSignatureDatax -> ListHudElement.ReservedString.createReservedString2(text, ChatTextUtils.describeText4(text))
         )
      );
      int i2 = this.size() - this.limit.getValue();
      if (i2 > 0) {
         for (int i = 0; i < i2 && i < 100; i++) {
            this.run();
         }
      }
   }

   @Override
   public float getFloatValue22() {
      return 0.08F;
   }

   public static ChatHudElement getChatHudElement() {
      return chatHudElement;
   }

   public static void handleChatHudElement(ChatHudElement chatHudElement, Predicate<ListHudElement.DataType<MessageSignatureData>> predicate) {
      chatHudElement.handlePredicate(predicate);
   }

   public static boolean alwaysFalse() {
      return false;
   }

   @Override
   public float value4() {
      return this.isFlag9() ? this.scale.getValue() : Math.min(this.size() * 10.0F, this.scale.getValue());
   }

   @Override
   public float computeFloat2(float f, float f2) {
      return -f2;
   }

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      this.setFlag9(mc.currentScreen instanceof ChatScreen);
      if (!this.isFlag9()) {
         this.getFloatAnimator9().setFloatValue(0.0F);
      }

      this.handleDrawContext(drawContext);
   }

   public void handleText2(Text text, MessageSignatureData messageSignatureData, boolean flag) {
      String text3 = ChatTextUtils.describeText3(text, this.color.getValue() ? "" : "§r", flag);

      for (String text2 : this.resolveList(text3)) {
         this.handleText(text2, messageSignatureData);
      }
   }

   @Override
   public void handleDouble2(double d, double d2, double d3) {
      this.getFloatAnimator9().setFloatValue((float)(this.getFloatAnimator9().getFloatValue() + d3 * 10.0));
      if (this.getFloatAnimator9().getFloatValue() < 0.0F) {
         this.getFloatAnimator9().setFloatValue(0.0F);
      }
   }

   public ChatHudElement() {
      super("Chat");
      this.limit = this.resolveSetting2(new NumberSetting<>("Limit", 100, 5, 150));
      this.scale = this.resolveSetting2(new NumberSetting<>("Scale", 100.0F, 15.0F, 200.0F));
      this.handleListener(
         new SafeListener<AddMessageEvent>(Integer.MIN_VALUE) {
            public void handleObject(
               AddMessageEvent addMessageEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               ChatHudElement.this.handleText2(
                  addMessageEvent.getText(), addMessageEvent.getMessageSignatureData(), HudModule.getHudModule().check4()
               );
            }
         }
      );
      this.handleListener(
         new SafeListener<SignedMessage>() {
            public void handleObject(
               SignedMessage signedMessage,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               ChatHudElement.handleChatHudElement(
                  ChatHudElement.this, dataType -> signedMessage.getMessageSignatureData().equals(dataType.getObject())
               );
            }
         }
      );
      FontModule.Dispatcher.handleConsumer(booleanValue -> {
         this.handleFlag(true);

         for (ChatHudLine chatHudLine : this.getPhobosClient().getChatManager().getMessages()) {
            this.handleText2(chatHudLine.content(), chatHudLine.signature(), booleanValue);
         }
      });
      this.setFlag2(false);
      this.topCenterEnum = TopCenterEnum.Bottom;
      this.centerRightEnum = CenterRightEnum.Left;
      chatHudElement = this;
   }

   public List<String> resolveList(String text) {
      List<String> list = new ArrayList<>();
      int i3 = text.length();
      if (i3 <= 150) {
         list.add(text);
         return list;
      }

      for (int i2 = 0; i2 < i3; i2 += 150) {
         int i = Math.min(i2 + 150, i3);
         list.add("§r" + text.substring(i2, i));
      }

      return list;
   }
}
