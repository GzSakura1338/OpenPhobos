package org.phobos.module.client;

import org.phobos.core.setting.Setting;
import org.phobos.core.setting.StringSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.ClientHolder;
import org.phobos.module.Module;

public class MediaModule extends Module {
   public final Setting<String> replacement = new StringSetting("Replacement", "Player").resolveSetting6(this);
   public static MediaModule mediaModule;
   public final Setting<Boolean> friends = new BooleanSetting("Friends").resolveSetting6(this);
   public final Setting<String> friendsReplacement = new StringSetting("FriendsReplacement", "Friend");

   public MediaModule(PhobosClient phobosClient) {
      super(phobosClient, "Media", Categories.client, "Hides your name");
      this.resolveSettingGroup(this.friends, this.friendsReplacement);
      mediaModule = this;
   }

   public static MediaModule getMediaModule() {
      return mediaModule;
   }

   public static boolean check3() {
      return mediaModule != null && mediaModule.check7();
   }

   public static String describe() {
      return mediaModule.friendsReplacement.getValue();
   }

   public Setting<String> getFriendsReplacement() {
      return this.friendsReplacement;
   }

   public static String describeText(String text) {
      if (check3()) {
         if (mc.player != null && text.contains(mc.player.getNameForScoreboard())) {
            return text.replace(mc.player.getNameForScoreboard(), describe2());
         }

         if (!mediaModule.friends.getValue()) {
            return text;
         }

         String text2 = ClientHolder.getPhobosClient().getFriendList().describeText(text);
         if (text2 != null) {
            return text.replace(text2, describe());
         }
      }

      return text;
   }

   public Setting<Boolean> getFriends() {
      return this.friends;
   }

   public static String describe2() {
      return mediaModule.replacement.getValue();
   }

   public Setting<String> getReplacement() {
      return this.replacement;
   }
}
