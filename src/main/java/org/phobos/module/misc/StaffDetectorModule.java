package org.phobos.module.misc;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.StringSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.social.StaffLists;
import org.phobos.core.PhobosClient;
import org.phobos.core.task.AsyncUtils;
import org.phobos.core.module.Categories;
import org.phobos.event.StaffDetectorReceivePlayerListListener;
import org.phobos.module.Module;
import org.phobos.module.client.NotificationsModule;

public class StaffDetectorModule extends Module {
   public final StaffLists staffLists;
   public final Setting<Boolean> leave;
   public final Setting<String> leaveCommand;
   public final Setting<StaffDetectorModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", StaffDetectorModule.Mode.Hypixel));
   public final Setting<String> chatElement;
   public final Setting<Boolean> chat = this.resolveSetting2(new BooleanSetting("Chat").resolveSetting7("Sends something to in-game chat"));

   public StaffDetectorModule(PhobosClient phobosClient) {
      super(phobosClient, "StaffDetector", Categories.misc);
      this.chatElement = this.resolveSetting2(new StringSetting("ChatElement", "[!] %s is a staff."));
      this.leave = this.resolveSetting2(new BooleanSetting("Leave").resolveSetting7("Runs leave command when staff is detected"));
      this.leaveCommand = this.resolveSetting2(new StringSetting("LeaveCommand", "leave"));
      this.staffLists = new StaffLists();
      this.resolveSettingGroup(this.chat, this.chatElement);
      this.resolveSettingGroup(this.leave, this.leaveCommand);
      this.handleListener(new StaffDetectorReceivePlayerListListener(this));
      this.staffLists.handleText(this.mode.getValue().getKey());
      this.mode.handleConsumer2(mode -> this.staffLists.handleText(mode.getKey()));
      AsyncUtils.handleRunnable(this.staffLists::run3);
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, String text) {
      if (this.contains(text)) {
         this.handleClientPlayerEntity2(clientPlayerEntity, text);
      }
   }

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   public boolean contains(String text) {
      return this.staffLists.contains(text);
   }

   public void handleClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity, String text) {
      NotificationsModule.getNotificationsModule()
         .handleTriConsumer(
            (color, color2, color3) -> this.handleText7(
               Text.literal("[!] ")
                  .formatted(new Formatting[]{Formatting.RED, Formatting.BOLD})
                  .append(Text.literal("%s ".formatted(text)).withColor(color2.hashCode()))
                  .append(Text.literal("is a staff member").withColor(color3.hashCode()))
            )
         );
      if (this.chat.getValue()) {
         String text2 = this.chatElement.getValue().formatted(text);
         clientPlayerEntity.networkHandler.sendChatMessage(text2);
      }

      if (this.leave.getValue()) {
         clientPlayerEntity.networkHandler.sendChatCommand(this.leaveCommand.getValue());
      }
   }

   public enum Mode {
      BlocksMC("blocksmc.com"),
      Cubecraft("cubecraft.net"),
      Hypixel("hypixel.net");

      public final String key;

      Mode(final String key) {
         this.key = key;
      }

      public String getKey() {
         return this.key;
      }
   }
}
