package org.phobos.module.client;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.util.TriConsumer;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.StringSetting;
import org.phobos.core.setting.MultiSelectSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.module.ModuleToggleNotice;
import org.phobos.core.net.packet.NotificationPacket;
import org.phobos.core.config.ConfigManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.PhobosClient.InitEvent;
import org.phobos.data.AttackersRecord;
import org.phobos.event.AddEntityEvent;
import org.phobos.event.DeathScreenEvent;
import org.phobos.event.EntityStatusEvent;
import org.phobos.event.Listener;
import org.phobos.event.RemoveEntityEvent;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class NotificationsModule extends Module {
   public final Setting<Boolean> streak;
   public final Setting<Boolean> totems;
   public final Setting<Map<Module, Boolean>> modulesSelection;
   public final Setting<Color> secondary;
   public final Setting<Boolean> death;
   public final Setting<Boolean> modules;
   public final Setting<Boolean> friends;
   public final Setting<Boolean> visualRange;
   public int intValue;
   public final Setting<Color> logo;
   public final Setting<Float> soundVolume;
   public final Setting<String> text;
   public static NotificationsModule notificationsModule;
   public final List<Text> textList = new ArrayList<>();
   public final Setting<Boolean> sound;
   public final Setting<Boolean> self;
   public final Setting<Boolean> kills;
   public final Setting<Color> main;

   public void handleText5(Text text) {
      this.phobosClient
         .getChatManager()
         .handleText5(Text.literal("[%s] ".formatted(this.text.getValue())).withColor(this.logo.getValue().getRGB()).append(Text.literal("").append(text)));
   }

   public List<Text> getTextList() {
      return this.textList;
   }

   public Setting<Boolean> getFriends() {
      return this.friends;
   }

   public Setting<Boolean> getDeath() {
      return this.death;
   }

   public Setting<Boolean> getModules() {
      return this.modules;
   }

   public Setting<Map<Module, Boolean>> getModulesSelection() {
      return this.modulesSelection;
   }

   public NotificationsModule(PhobosClient phobosClient) {
      super(phobosClient, "Notifications", Categories.client, "Chat notifications");
      this.logo = this.resolveSetting2(new ColorSetting("Logo", new Color(255, 255, 255, 255)).getColorSetting());
      this.main = this.resolveSetting2(new ColorSetting("Main", new Color(200, 200, 200, 255)).getColorSetting());
      this.secondary = this.resolveSetting2(new ColorSetting("Secondary", new Color(150, 150, 150, 255)).getColorSetting());
      this.modules = this.resolveSetting2(new BooleanSetting("Modules", true).resolveSetting7("Notify module status"));
      this.modulesSelection = this.resolveSetting2(new MultiSelectSetting<>("ModulesSelection", Module.class).resolveMultiSelectSetting5(multiSelectSetting -> {
         for (Module module : this.phobosClient.getModuleManager().getList()) {
            multiSelectSetting.resolveMultiSelectSetting2(module, module.getName(), true);
         }
      }));
      this.totems = this.resolveSetting2(new BooleanSetting("Totems", false).resolveSetting7("Totem pops"));
      this.self = this.resolveSetting2(new BooleanSetting("Self", false).resolveSetting7("Count self-pops"));
      this.kills = this.resolveSetting2(new BooleanSetting("Kills").resolveSetting7("Kill notification"));
      this.streak = this.resolveSetting2(new BooleanSetting("Streak").resolveSetting7("Kill-streaks"));
      this.death = this.resolveSetting2(new BooleanSetting("Death").resolveSetting7("Death coordinates"));
      this.visualRange = this.resolveSetting2(new BooleanSetting("VisualRange").resolveSetting7("Notifies when a player enters your visual range"));
      this.friends = this.resolveSetting2(new BooleanSetting("Friends"));
      this.sound = this.resolveSetting2(new BooleanSetting("Sound"));
      this.soundVolume = this.resolveSetting2(new NumberSetting<>("SoundVolume", 0.5F, 0.0F, 1.0F));
      this.text = this.resolveSetting2(new StringSetting("Text", "Phobos"));
      phobosClient.getPacketBus().handleClassValue(NotificationPacket.class, notificationPacket -> this.handleText5(Text.literal(notificationPacket.getMessage())));
      this.createBuilder()
         .resolveBuilder6(this.visualRange)
         .resolveBuilder5(this.resolveSettingGroup(this.sound, this.soundVolume))
         .resolveBuilder(this.friends)
         .getSettingSubsection();
      this.resolveSettingGroup(this.modules, this.modulesSelection);
      this.resolveSettingGroup(this.totems, this.self);
      this.resolveSettingGroup(this.kills, this.streak);
      this.handleListener(
         new Listener<ModuleToggleNotice>() {
            public void handleObject2(ModuleToggleNotice moduleToggleNotice) {
               if (!ConfigManager.flag) {
                  if (NotificationsModule.this.modules.getValue()) {
                     if (NotificationsModule.this.modulesSelection.getValue().getOrDefault(moduleToggleNotice.module, false)) {
                        Formatting formatting = moduleToggleNotice.flag ? Formatting.GREEN : Formatting.RED;
                        Text text = Text.literal("")
                           .append(Text.literal(moduleToggleNotice.module.describe4()).formatted(formatting))
                           .append(Text.literal(moduleToggleNotice.flag ? " enabled." : " disabled.").withColor(NotificationsModule.this.main.getValue().getRGB()));
                        NotificationsModule.this.handleText9(text, moduleToggleNotice.module.getName());
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new Listener<EntityStatusEvent>() {
            public void handleObject2(EntityStatusEvent entityStatusEvent) {
               if (NotificationsModule.this.totems.getValue()
                  && entityStatusEvent.entity() instanceof PlayerEntity
                  && (!Objects.equals(entityStatusEvent.entity(), MinecraftHolder.mc.player) || NotificationsModule.this.self.getValue())) {
                  int i = phobosClient.getEntityIdManager().computeEntity(entityStatusEvent.entity());
                  int i2 = NotificationsModule.this.main.getValue().getRGB();
                  int i3 = NotificationsModule.this.secondary.getValue().getRGB();
                  boolean flag = entityStatusEvent.entity() instanceof ClientPlayerEntity;
                  Text text = Text.literal("")
                     .append((flag ? Text.literal("You") : entityStatusEvent.entity().getName().copy()).withColor(i2))
                     .append(Text.literal(" ha%s popped ".formatted(flag ? "ve" : "s")).withColor(i3))
                     .append(Text.literal(String.valueOf(i)).withColor(i2))
                     .append(Text.literal(" totems.").withColor(i3));
                  NotificationsModule.this.handleText9(text, "TotemPops" + entityStatusEvent.entity().getId());
               }
            }
         }
      );
      this.handleListener(new Listener<InitEvent>() {
         public void handleObject2(InitEvent initEvent) {
            initEvent.getPhobosClient().getConsumerList2().forEach(consumer -> consumer.accept(NotificationsModule.this));
         }
      });
      this.handleListener(
         new SafeListener<AttackersRecord>() {
            public void handleObject(
               AttackersRecord attackersRecord,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (attackersRecord.testPlayerEntity(clientPlayerEntity) && NotificationsModule.this.kills.getValue()) {
                  int i = NotificationsModule.this.main.getValue().hashCode();
                  int i2 = NotificationsModule.this.secondary.getValue().hashCode();
                  NotificationsModule.this.intValue++;
                  MutableText mutableText = Text.literal("")
                     .append(Text.literal("You").withColor(i))
                     .append(Text.literal(" have killed ").withColor(i2))
                     .append(attackersRecord.victim().getName().getString())
                     .withColor(i);
                  if (attackersRecord.attackers().size() > 1) {
                     mutableText.append(Text.literal(" (").withColor(i2));
                  }

                  int i3 = attackersRecord.attackers().size();
                  int i4 = 0;

                  for (PlayerEntity playerEntity : attackersRecord.attackers()) {
                     if (!playerEntity.equals(clientPlayerEntity)) {
                        i4++;
                        mutableText.append(Text.literal(playerEntity.getName().getString()).withColor(i));
                        if (i3 - 1 > i4) {
                           mutableText.append(Text.literal(", ").withColor(i2));
                        }
                     }
                  }

                  if (attackersRecord.attackers().size() > 1) {
                     mutableText.append(Text.literal(")").withColor(i2));
                  }

                  NotificationsModule.this.handleText9(mutableText, "Kills");
                  if (NotificationsModule.this.intValue > 3 && NotificationsModule.this.streak.getValue()) {
                     NotificationsModule.this.handleText9(
                        Text.literal("Killstreak: ").withColor(i2).append(Text.literal(String.valueOf(NotificationsModule.this.intValue)).withColor(i)),
                        "Killstreak"
                     );
                  }
               }
            }
         }
      );
      this.handleListener(
         new Listener<DeathScreenEvent>(Integer.MAX_VALUE) {
            public void handleObject2(DeathScreenEvent deathScreenEvent) {
               if (NotificationsModule.this.death.getValue() && MinecraftHolder.mc.player != null) {
                  int i = NotificationsModule.this.main.getValue().getRGB();
                  int i2 = NotificationsModule.this.secondary.getValue().getRGB();
                  MutableText mutableText = Text.literal("")
                     .append(Text.literal("You").withColor(i))
                     .append(Text.literal(" died at ").withColor(i2))
                     .append(NotificationsModule.this.describeVec3d(MinecraftHolder.mc.player.getPos()))
                     .withColor(i);
                  NotificationsModule.this.handleText9(mutableText, "DeathCoords");
               }
            }
         }
      );
      this.handleListener(new Listener<AddEntityEvent>() {
         public void handleObject2(AddEntityEvent addEntityEvent) {
            NotificationsModule.this.handleEntity(addEntityEvent.getEntity(), true);
         }
      });
      this.handleListener(new Listener<RemoveEntityEvent>() {
         public void handleObject2(RemoveEntityEvent removeEntityEvent) {
            NotificationsModule.this.handleEntity(removeEntityEvent.getEntity(), false);
         }
      });
      ModuleListenerUtils.handleListenerHolder4(this, () -> this.intValue = 0);
      notificationsModule = this;
   }

   public Setting<Boolean> getTotems() {
      return this.totems;
   }

   public Setting<Boolean> getBooleanValueSetting3() {
      return this.self;
   }

   public Setting<Boolean> getStreak() {
      return this.streak;
   }

   public Setting<Color> getLogo() {
      return this.logo;
   }

   public Setting<Boolean> getSound() {
      return this.sound;
   }

   public Setting<Color> getBooleanValueSetting() {
      return this.main;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public void handleEntity(Entity entity, boolean flag) {
      if (this.visualRange.getValue()) {
         if (entity instanceof PlayerEntity playerEntity && !playerEntity.isMainPlayer()) {
            if (!this.phobosClient.getFriendList().testUUID(playerEntity.getUuid()) || this.friends.getValue()) {
               Color color = this.main.getValue();
               Color color2 = this.secondary.getValue();
               Text text = Text.literal(playerEntity.getNameForScoreboard())
                  .withColor(color.hashCode())
                  .append(Text.literal(" has %s your visual range.".formatted(flag ? "entered" : "left")).withColor(color2.hashCode()));
               this.handleText9(text, "VisualRange" + entity.getId());
               if (this.sound.getValue()) {
                  this.phobosClient.getPhobosSounds().handleSoundEvent(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, this.soundVolume.getValue());
               }
            }
         }
      }
   }

   public static NotificationsModule getNotificationsModule() {
      return notificationsModule;
   }

   public Setting<Boolean> getVisualRange() {
      return this.visualRange;
   }

   public void run() {
      if (!this.textList.isEmpty()) {
         this.textList.removeIf(text -> {
            this.handleText5(text);
            return true;
         });
      }
   }

   public Setting<Color> getSecondary() {
      return this.secondary;
   }

   public void handleConsumer(Consumer<Color> consumer) {
      consumer.accept(this.secondary.getValue());
   }

   public Setting<Float> getSoundVolume() {
      return this.soundVolume;
   }

   public void handleTriConsumer(TriConsumer<Color, Color, Color> triConsumer) {
      triConsumer.accept(this.logo.getValue(), this.main.getValue(), this.secondary.getValue());
   }

   public Setting<String> getText2() {
      return this.text;
   }

   public String describeVec3d(Vec3d vec3d) {
      return Math.round(vec3d.x) + ", " + Math.round(vec3d.y) + ", " + Math.round(vec3d.z);
   }

   public Setting<Boolean> getKills() {
      return this.kills;
   }

   public void handleText9(Text text, String text2) {
      this.phobosClient
         .getChatManager()
         .handleText9(Text.literal("[%s] ".formatted(this.text.getValue())).withColor(this.logo.getValue().getRGB()).append(text), text2);
   }
}
