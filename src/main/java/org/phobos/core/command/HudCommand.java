package org.phobos.core.command;

import java.util.Arrays;
import net.minecraft.text.Text;
import org.phobos.module.client.NotificationsModule;
import org.phobos.core.hud.HudElement;
import org.phobos.core.hud.HudManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.setting.Setting;

public class HudCommand extends SyntaxCommand {
   public HudCommand(PhobosClient phobosClient) {
      super(phobosClient, "hud", "element", "setting", "value");
   }

   @Override
   public void handleString(String[] texts, String text) {
      if (texts.length == 1) {
         HudElement hudElement = HudManager.resolveHudElement(texts[0]);
         if (hudElement == null) {
            this.handleText2("Hud element not found");
         } else {
            NotificationsModule.getNotificationsModule()
               .handleTriConsumer(
                  (color, color2, color3) -> {
                     StringBuilder stringBuilder = new StringBuilder();

                     for (Setting<?> setting : hudElement.getSettingList()) {
                        stringBuilder.append(setting.getName()).append(", ");
                     }

                     String textx = stringBuilder.toString();
                     String text2 = textx.substring(0, textx.length() - 2);
                     Text text3 = Text.literal(hudElement.getName() + ": ")
                        .withColor(color.hashCode())
                        .append(Text.literal(text2).withColor(color2.hashCode()));
                     this.handleText7(text3);
                  }
               );
         }
      } else {
         HudElement hudElement2 = HudManager.resolveHudElement(texts[0]);
         if (hudElement2 == null) {
            this.handleText2("Hud element not found");
         } else {
            hudElement2.resolveOptional(texts[1])
               .ifPresent(
                  setting -> {
                     if (texts.length == 2) {
                        NotificationsModule.getNotificationsModule()
                           .handleTriConsumer(
                              (color, color2, color3) -> {
                                 Text textx = Text.literal(hudElement2.getName() + ": ")
                                    .withColor(color.hashCode())
                                    .append(Text.literal(setting.getName()).withColor(color2.hashCode()))
                                    .append(Text.literal(": ").withColor(color3.hashCode()))
                                    .append(Text.literal(String.valueOf(setting.getValue())).withColor(color2.hashCode()));
                                 this.handleText7(textx);
                              }
                           );
                     } else {
                        try {
                           if (setting.testString(Arrays.copyOfRange(texts, 2, texts.length))) {
                              NotificationsModule.getNotificationsModule()
                                 .handleTriConsumer(
                                    (color, color2, color3) -> {
                                       Text textx = Text.literal(hudElement2.getName() + ": ")
                                          .withColor(color.hashCode())
                                          .append(Text.literal(setting.getName()).withColor(color2.hashCode()))
                                          .append(Text.literal(" -> ").withColor(color3.hashCode()))
                                          .append(Text.literal(String.valueOf(setting)).withColor(color2.hashCode()));
                                       this.handleText7(textx);
                                    }
                                 );
                              return;
                           }
                        } catch (Throwable throwable) {
                           this.handleText2("Failed to set value for %s".formatted(setting.getName()));
                        }

                        this.handleText2("Failed to set value for %s".formatted(setting.getName()));
                     }
                  }
               );
         }
      }
   }

   @Override
   public Suggestion resolveSuggestion(String text, String text2, String[] texts) {
      switch (text) {
         case "element":
            HudElement hudElement = this.phobosClient.getHudManager().resolveHudElement2(text2);
            if (hudElement != null) {
               return Suggestion.createSuggestion3(hudElement.getName());
            }
            break;
         case "setting":
            if (texts.length == 0) {
               return null;
            }

            HudElement hudElement2 = this.phobosClient.getHudManager().resolveHudElement2(texts[0]);
            if (hudElement2 != null) {
               Setting<?> setting = hudElement2.resolveSetting(text2);
               if (setting == null) {
                  return null;
               }

               return Suggestion.createSuggestion3(setting.getName());
            }
            break;
         case "value":
            if (texts.length < 1) {
               return null;
            }

            HudElement hudElement3 = this.phobosClient.getHudManager().resolveHudElement2(texts[0]);
            if (hudElement3 != null) {
               Setting<?> setting2 = hudElement3.resolveOptional(texts[1]).orElse(null);
               if (setting2 == null) {
                  return null;
               }

               String[] texts2 = Arrays.copyOfRange(texts, 2, texts.length);
               return setting2.resolveSuggestion(texts2);
            }
      }

      return null;
   }

   @Override
   public boolean testString(String[] texts) {
      return texts.length > 0;
   }
}
