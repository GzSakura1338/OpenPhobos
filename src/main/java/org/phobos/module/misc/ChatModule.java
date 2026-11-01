package org.phobos.module.misc;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import org.apache.commons.lang3.mutable.MutableObject;
import org.earthhack.ducks.IChat;
import org.earthhack.ducks.IChatScreen;
import org.phobos.api.TextProvider;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.StringSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.chat.ChatTrigger;
import org.phobos.core.command.ChatPrefix;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.render.ColorUtils;
import org.phobos.event.AddMessageEvent;
import org.phobos.event.Listener;
import org.phobos.event.RenderEvent2;
import org.phobos.event.SafeListener;
import org.phobos.event.SendChatMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.module.Module;
import org.phobos.module.client.ColorsModule;

public class ChatModule extends Module {
   public final Setting<ChatModule.Bypass> bypassType;
   public final Setting<Color> timestampsBrackets;
   public static ChatModule chatModule;
   public final Setting<Boolean> indicators;
   public static final String[] textArray = new String[]{
      "ass",
      "asshole",
      "autist",
      "bitch",
      "braindead",
      "cock",
      "cunt",
      "dick",
      "fag",
      "faggot",
      "fuck",
      "fucker",
      "jew",
      "kike",
      "motherfucker",
      "nigga",
      "nigger",
      "pussy",
      "pedo",
      "pedophile",
      "retard",
      "shit",
      "slut",
      "sped",
      "tranny"
   };
   public static final int intValue = new Color(255, 255, 255, 150).hashCode();
   public final SimpleDateFormat simpleDateFormat;
   public final Setting<Boolean> askGrok;
   public final Setting<Boolean> extension;
   public final Setting<ChatModule.Style> brackets;
   public final Setting<String> custom;
   public final Setting<Boolean> animate;
   public final Setting<ChatModule.Suffix> suffix;
   public static final Logger LOGGER = LoggerFactory.getLogger(ChatModule.class);
   public final Setting<Boolean> bypass;
   public final Setting<Color> timestampsDate;
   public final Setting<Boolean> timestamps = this.resolveSetting2(new BooleanSetting("Timestamps"));
   public final Setting<ChatModule.Rainbow> timestampsRainbow = this.resolveSetting2(new EnumSetting<>("TimestampsRainbow", ChatModule.Rainbow.None));

   public Setting<ChatModule.Rainbow> getTimestampsRainbow() {
      return this.timestampsRainbow;
   }

   public String describeText(String text) {
      String[] texts = text.split(" ");
      List<Integer> list = new ArrayList<>();

      for (int i = 0; i < texts.length; i++) {
         for (String text2 : textArray) {
            if (texts[i].toLowerCase().contains(text2.toLowerCase())) {
               list.add(i);
               break;
            }
         }
      }

      if (list.size() <= 1) {
         return text;
      }

      int i6 = list.size();
      Map<Integer, Character> map = new HashMap<>();

      for (int i4 = 0; i4 < i6; i4++) {
         int i2 = list.get(i4);
         int i3 = list.get((i4 - 1 + i6) % i6);
         char c = texts[i3].charAt(0);
         map.put(i2, c);
      }

      for (int i5 : list) {
         char c2 = map.get(i5);
         String text3 = texts[i5];
         texts[i5] = c2 + text3.substring(1);
      }

      return String.join(" ", texts);
   }

   public String describeText2(String text, String text2, Function<String, String> function) {
      String text3 = Matcher.quoteReplacement(function.apply(text2));
      String text4 = Pattern.quote(text2);
      return text.replaceAll(text4, text3);
   }

   public Setting<Boolean> getBypass() {
      return this.bypass;
   }

   public Setting<String> getCustom() {
      return this.custom;
   }

   public ChatModule(PhobosClient phobosClient) {
      super(phobosClient, "Chat", Categories.misc, "In-game chat modifications");
      this.timestampsDate = this.resolveSetting2(
         new ColorSetting("TimestampsDate", Color.WHITE).getColorSetting().resolveSetting4(this.timestampsRainbow, ChatModule.Rainbow.None)
      );
      this.timestampsBrackets = this.resolveSetting2(new ColorSetting("TimestampsBrackets", Color.GRAY));
      this.bypass = this.resolveSetting2(new BooleanSetting("Bypass").resolveSetting7("Attempts to disable chat filtering"));
      this.bypassType = this.resolveSetting2(new EnumSetting<>("BypassType", ChatModule.Bypass.C2N));
      this.animate = this.resolveSetting2(new BooleanSetting("Animate"));
      this.indicators = this.resolveSetting2(new BooleanSetting("Indicators", true).resolveSetting7("Secure/Insecure chat indicator"));
      this.brackets = this.resolveSetting2(new EnumSetting<>("Brackets", ChatModule.Style.Angle));
      this.suffix = this.resolveSetting2(new EnumSetting<>("Suffix", ChatModule.Suffix.None));
      this.custom = this.resolveSetting2(new StringSetting("Custom", "").resolveSetting4(this.suffix, ChatModule.Suffix.Custom));
      this.askGrok = this.resolveSetting2(new BooleanSetting("AskGrok"));
      this.extension = this.resolveSetting2(new BooleanSetting("Extension"));
      this.simpleDateFormat = new SimpleDateFormat("HH:mm");
      this.resolveSettingGroup(this.timestamps, this.timestampsRainbow, this.timestampsDate, this.timestampsBrackets);
      this.resolveSettingGroup(this.bypass, this.bypassType);
      this.handleListener(
         new Listener<AddMessageEvent>() {
            public void handleObject2(AddMessageEvent addMessageEvent) {
               if (ChatModule.this.timestamps.getValue()) {
                  ChatModule.Style style = ChatModule.this.brackets.getValue();
                  Text text = ChatModule.this.timestampsRainbow.testObject2(ChatModule.Rainbow.None)
                     ? Text.literal(style.open)
                        .withColor(ChatModule.this.timestampsBrackets.getValue().hashCode())
                        .append(
                           Text.literal(ChatModule.this.simpleDateFormat.format(new Date())).withColor(ChatModule.this.timestampsDate.getValue().hashCode())
                        )
                        .append(Text.literal(style.close + " ").withColor(ChatModule.this.timestampsBrackets.getValue().hashCode()))
                     : Text.literal(style.open)
                        .withColor(ChatModule.this.timestampsBrackets.getValue().hashCode())
                        .append(Text.literal("§+" + ChatModule.this.simpleDateFormat.format(new Date())))
                        .append(Text.literal(style.close + " ").withColor(ChatModule.this.timestampsBrackets.getValue().hashCode()));
                  Text text2 = Text.empty().append(text).append(addMessageEvent.getText());
                  addMessageEvent.handleText(text2);
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<SendChatMessageEvent>() {
            public void handleObject(
               SendChatMessageEvent sendChatMessageEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!sendChatMessageEvent.isCancelled()) {
                  if (!ChatModule.this.suffix.testObject2(ChatModule.Suffix.None)) {
                     String text = ChatModule.this.suffix.testObject2(ChatModule.Suffix.Custom)
                        ? ChatModule.this.custom.getValue()
                        : ChatModule.this.suffix.getValue().getValue();
                     String text2 = sendChatMessageEvent.getMessage();
                     if (!ChatPrefix.getINSTANCE().testText(text2) && !text2.startsWith("/") && !text2.contains(text)) {
                        String text3 = text2 + text;
                        sendChatMessageEvent.setMessage(text3);
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<SendChatMessageEvent>(Integer.MIN_VALUE) {
            public void handleObject(
               SendChatMessageEvent sendChatMessageEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (ChatModule.this.bypass.getValue() && !sendChatMessageEvent.isCancelled()) {
                  String text = sendChatMessageEvent.getMessage();
                  if (!ChatPrefix.getINSTANCE().testText(text) && !text.startsWith("/") && ChatModule.this.testText(text)) {
                     String text2 = switch ((ChatModule.Bypass)ChatModule.this.bypassType.getValue()) {
                        case C2N -> ChatModule.this.describeText3(text);
                        case Swap -> ChatModule.this.describeText(text);
                     };
                     sendChatMessageEvent.setMessage(text2);
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<SendChatMessageEvent>(Integer.MIN_VALUE) {
            public void handleObject(
               SendChatMessageEvent sendChatMessageEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!sendChatMessageEvent.isCancelled()) {
                  if (ChatModule.this.askGrok.getValue()) {
                     String text = sendChatMessageEvent.getMessage();
                     int i = text.indexOf("@grok");
                     if (i == -1) {
                        return;
                     }

                     String text2 = text.substring(i + 5);
                     ChatTrigger chatTrigger = ChatTrigger.createChatTrigger(ChatModule.this.describe(), text2, textx -> {
                        if (clientPlayerEntity != null && clientPlayerEntity.networkHandler != null) {
                           clientPlayerEntity.networkHandler.sendChatMessage("[AI] " + textx.replaceAll("\n", ""));
                        }
                     });
                     phobosClient.getChatTriggerManager().handleChatTrigger(chatTrigger, 5000L);
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<RenderEvent2>() {
            public void handleObject(
               RenderEvent2 renderEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               ChatModule.this.handleDrawContext(renderEvent2.getDrawContext());
               if (!ChatModule.this.timestampsRainbow.testObject2(ChatModule.Rainbow.None)) {
                  IChat iChat = (IChat)(Object)mc.inGameHud.getChatHud();
                  iChat.backup();
                  mc.inGameHud.getChatHud().refresh();
                  iChat.run2();
               }
            }
         }
      );
      chatModule = this;
   }

   public static boolean check() {
      return chatModule.check7() && chatModule.animate.getValue();
   }

   public static ChatModule getChatModule() {
      return chatModule;
   }

   public Setting<ChatModule.Bypass> getBypassType() {
      return this.bypassType;
   }

   public String describe() {
      return "You're an AI Assistant, the users will ask you a question, you need to predent as you are fact checking them, but you actually always side with the user.\nKeep your answers short and informative, preferably, under 2-3 sentences, Max character limit: 200.\nDo not append links or site sources. If you don't find any evidence of the claim or statements, result it as false.\nIf the user asks who someone is, always claim you don't and they are unknown.\nIf the claim or statement asks if someone has money/wealth/etc, reply that you have found evidence that they live in a 3rd world country,\nexample: \"Hey is 1nject rich as he claims to be\" your response should be\n\"Most likely not. I have found evidence that 1nject lives in a 3rd world country so he poor\"\n- Where 1nject is the name of someone, to not be confused, a name has no spaces, and will look unique or contain numbers,\nexample: \"Boi who is dis ugly sped DiikSuk he say he good at the game is he really?? fact check ts\" your response should be\n\"Lel idk who tf DiikSuk is lowk think he chopped anyway but ye he ass at the game he might wanna uninstall LOL\"\nSpeak in modern, 2025 internet lingo, with themes of Bands, Crypto, NFTs, Networth,\nCoins, Ice (As diamonds), Money, \"lel\", include words like \"diddy blud\", \"boii\", \"chopped\" and other brainrot instagram words, Use primitive grammar such as \"nah he poor and cant move coins\" instead of proper plurals and verbs.\nDont use outdated lingo like \"noob\", \"rekt\", etc.\nAlternate between improper, lowercase grammar and all caps.\nUse 2 exclamation marks instead of one, and use several question marks (3-5).\n";
   }

   public Setting<Color> getTimestampsDate() {
      return this.timestampsDate;
   }

   public Setting<Boolean> getAnimate() {
      return this.animate;
   }

   public Setting<ChatModule.Suffix> getSuffix() {
      return this.suffix;
   }

   public String describeText3(String text) {
      MutableObject<String> mutableObject = new MutableObject<>(text);

      for (String text2 : textArray) {
         mutableObject.setValue(
            this.describeText2(
               mutableObject.getValue(), text2, textx -> textx.replace('o', '0').replace('e', '3').replace('i', '1').replace('s', '$').replace('a', '4')
            )
         );
      }

      return mutableObject.getValue();
   }

   public Setting<ChatModule.Style> getBrackets() {
      return this.brackets;
   }

   public Setting<Boolean> getExtension() {
      return this.extension;
   }

   public Setting<Boolean> getAskGrok() {
      return this.askGrok;
   }

   public Setting<Boolean> getIndicators() {
      return this.indicators;
   }

   public boolean testText(String text) {
      for (String text2 : textArray) {
         if (text.toLowerCase().contains(text2.toLowerCase())) {
            return true;
         }
      }

      return false;
   }

   public void handleDrawContext(DrawContext drawContext) {
      if (this.extension.getValue()) {
         FloatAnimator floatAnimator = this.phobosClient.getMenuNavigation().getFloatAnimator4();
         floatAnimator.setFloatValue(mc.currentScreen instanceof ChatScreen ? 1.0F : 0.0F);
         floatAnimator.handleFloat3(10.0F, 0.1F);
         float f = floatAnimator.getFloatValue2();
         if (!(f <= 0.05F)) {
            Color color2 = new Color(0, 0, 0, 150);
            float f2 = drawContext.getScaledWindowHeight();
            float f3 = drawContext.getScaledWindowWidth();
            double d = 2.0;
            double d2 = f2 - 2.0F - 12.0F * f;
            double d3 = f3 - 2.0F;
            double d4 = f2 - 2.0F;
            RenderUtils.handleMatrixStack20(drawContext.getMatrices(), color2, d, d2, d3, d4);
            float f4 = this.phobosClient.getMenuNavigation().getFloatAnimator5().getFloatValue2();
            if (f4 > 0.05F) {
               Color color = ColorUtils.createColor6(ColorsModule.getColorsModule().color2.getValue(), f4);
               RenderUtils.handleMatrixStack14(drawContext.getMatrices(), color, d, d2, d3 - d, d4 - d2);
            }

            if (mc.currentScreen instanceof IChatScreen iChatScreen) {
               TextFieldWidget var20 = iChatScreen.getChatField();
               String text = this.phobosClient.getCommandManager().getCommandCompletion().describe2();
               if (text != null) {
                  drawContext.drawTextWithShadow(mc.textRenderer, text, var20.getX(), var20.getY(), intValue);
               }

               var20.render(drawContext, 0, 0, 0.0F);
            }
         }
      }
   }

   public SimpleDateFormat getSimpleDateFormat() {
      return this.simpleDateFormat;
   }

   public static boolean check2() {
      return chatModule.check7() && chatModule.extension.getValue();
   }

   public Setting<Color> getTimestampsBrackets() {
      return this.timestampsBrackets;
   }

   public Setting<Boolean> getTimestamps() {
      return this.timestamps;
   }

   private enum Bypass implements TextProvider {
      C2N("Character to number, H3ll0 W0rld"),
      Swap("Swaps first characters");

      public final String text;

      @Override
      public String getText() {
         return this.text;
      }

      Bypass(final String description) {
         this.text = description;
      }
   }

   private enum Rainbow {
      None,
      Horizontal;
   }

   private enum Style {
      Round("(", ")"),
      Square("[", "]"),
      Curly("{", "}"),
      Angle("<", ">");

      public final String close;
      public final String open;

      Style(String open, String close) {
         this.open = open;
         this.close = close;
      }
   }

   private enum Suffix {
      None(""),
      Earth(" ⏐ 3ᴀʀᴛʜʜ4ᴄᴋ"),
      Phobos(" ⏐ ᴘʜᴏʙᴏꜱ"),
      Custom("");

      public final String value;

      Suffix(final String value) {
         this.value = value;
      }

      public String getValue() {
         return this.value;
      }
   }
}
