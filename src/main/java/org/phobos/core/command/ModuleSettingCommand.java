package org.phobos.core.command;

import java.util.Arrays;
import net.minecraft.text.Text;
import org.phobos.module.Module;
import org.phobos.module.client.NotificationsModule;
import org.phobos.core.setting.Setting;

public class ModuleSettingCommand extends SyntaxCommand {
   public final Module module;

   public ModuleSettingCommand(Module module) {
      super(module.getPhobosClient(), module.getName(), "setting", "value");
      this.module = module;
   }

   @Override
   public Suggestion resolveSuggestion(String text, String text2, String[] texts) {
      switch (text) {
         case "setting":
            Setting<?> setting = this.module.resolveSetting(text2);
            if (setting == null) {
               return null;
            }

            return Suggestion.createSuggestion3(setting.getName());
         case "value":
            if (texts.length < 1) {
               return null;
            } else {
               Setting<?> setting2 = this.module.resolveOptional(texts[0]).orElse(null);
               if (setting2 == null) {
                  return null;
               }

               String[] texts2 = Arrays.copyOfRange(texts, 1, texts.length);
               return setting2.resolveSuggestion(texts2);
            }
         default:
            return null;
      }
   }

   @Override
   public void handleString(String[] texts, String text) {
      if (texts.length == 0) {
         NotificationsModule.getNotificationsModule().handleTriConsumer((color, color2, color3) -> {
            StringBuilder stringBuilder = new StringBuilder();

            for (Setting<?> setting : this.module.getSettingList()) {
               stringBuilder.append(setting.getName()).append(", ");
            }

            String textx = stringBuilder.toString();
            String text2 = textx.substring(0, textx.length() - 2);
            Text text3 = Text.literal(this.module.getName() + ": ").withColor(color.hashCode()).append(Text.literal(text2).withColor(color2.hashCode()));
            this.handleText7(text3);
         });
      } else {
         this.module
            .resolveOptional(texts[0])
            .ifPresent(
               setting -> {
                  if (texts.length == 1) {
                     NotificationsModule.getNotificationsModule()
                        .handleTriConsumer(
                           (color, color2, color3) -> {
                              Text textx = Text.literal(this.module.getName() + ": ")
                                 .withColor(color.hashCode())
                                 .append(Text.literal(setting.getName()).withColor(color2.hashCode()))
                                 .append(Text.literal(": ").withColor(color3.hashCode()))
                                 .append(Text.literal(String.valueOf(setting.getValue())).withColor(color2.hashCode()));
                              this.handleText7(textx);
                           }
                        );
                  } else {
                     try {
                        if (setting.testString(Arrays.copyOfRange(texts, 1, texts.length))) {
                           NotificationsModule.getNotificationsModule()
                              .handleTriConsumer(
                                 (color, color2, color3) -> {
                                    Text textx = Text.literal(this.module.getName() + ": ")
                                       .withColor(color.hashCode())
                                       .append(Text.literal(setting.getName()).withColor(color2.hashCode()))
                                       .append(Text.literal(" -> ").withColor(color3.hashCode()))
                                       .append(Text.literal(String.valueOf(setting.getValue())).withColor(color2.hashCode()));
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

   @Override
   public boolean testString(String[] texts) {
      return true;
   }
}
