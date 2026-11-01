package org.phobos.core.user;

import java.util.Deque;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import java.util.function.Consumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.util.Identifier;
import org.earthhack.ducks.IEntity;
import org.earthhack.ducks.IPlayerListEntry;
import org.earthhack.ducks.ITitleScreen;
import org.phobos.api.NetworkPacket;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.TimeGate.Normal;
import org.phobos.core.event.events.PacketEvent.PostSend;
import org.phobos.core.user.Permissions.Permission;
import org.phobos.event.AddEntityEvent;
import org.phobos.event.DisconnectEvent2;
import org.phobos.event.Listener;
import org.phobos.event.MoveEvent2;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.module.client.CalculationsModule;
import org.phobos.module.client.IncognitoModule;
import org.phobos.core.ClientHolder;
import org.phobos.core.config.ConfigManager;
import org.phobos.core.server.ConnectionInfo;
import org.phobos.core.net.packet.EmptyPacket261;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.fakeplayer.FakePlayer;
import org.phobos.core.Feature;
import org.phobos.core.net.packet.FlagMessagePacket251;
import org.phobos.core.net.packet.FlagTextPairPacket236;
import org.phobos.core.net.packet.IntFloatPacket320;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.net.PacketBus;
import org.phobos.core.PhobosClient;
import org.phobos.core.net.packet.PlayerDataPacket;
import org.phobos.core.net.packet.TextMapPacket309;
import org.phobos.core.net.packet.TextPairPacket265;
import org.phobos.core.net.packet.TextPairPacket294;

public class UserTagManager extends Feature {
   public Normal normal;
   public String name;
   public static final Identifier identifier = Identifier.of("phoboslite", "textures/tags/user.png");
   public Map<String, UserTagManager.TelemetryUser> textMap;
   public Normal normal2;
   public boolean flag;
   public boolean flag2;
   public Map<UserTagManager.SharedESP, Long> sharedESPMap;
   public Deque<String> textDeque;
   public static final Identifier identifier2 = Identifier.of("phoboslite", "textures/tags/admin.png");
   public Set<UserTagManager.TelemetryUser> telemetryUserSet;
   public Map<String, UserTagManager.TelemetryUser> textMap2;
   public static final Identifier identifier3 = Identifier.of("phoboslite", "textures/tags/beta.png");
   private static Object[] phobos$g1806d2210;
   private static Object[] phobos$g1806d2308;
   private static Object[] phobos$g1806d2400;
   private static Object[] phobos$g1806d24f8;
   private static Object[] phobos$g1806d25f0;
   private static Object[] phobos$g1806d26e8;
   private static Object[] phobos$g1806d2918;
   private static Object[] phobos$g1806d2a00;

   public boolean testInt(int i) {
      return this.sharedESPMap.entrySet().stream().anyMatch(entry -> entry.getKey().id == i);
   }

   public UserTagManager.TelemetryUser resolveTelemetryUser(String text) {
      return this.textMap.get(text);
   }

   public Set<UserTagManager.TelemetryUser> getTelemetryUserSet() {
      return this.telemetryUserSet;
   }

   public void run() {
      this.flag2 = true;
   }

   public void handlePhobosClient(PhobosClient phobosClient) {
      boolean var2 = this.flag;
      if (var2) {
         PacketBus var3 = phobosClient.getPacketBus();
         String var5 = this.name;
         IncognitoModule var6 = IncognitoModule.getIncognitoModule();
         boolean var7 = var6.check7();
         FlagTextPairPacket236 var4 = new FlagTextPairPacket236((String)null, var5, var7);
         var3.handleNetworkPacket2(var4);
         this.flag = false & true;
      }
   }

   public UserTagManager(PhobosClient phobosClient) {
      this.nativeInit(phobosClient);
   }

   public void nativeInit(final PhobosClient var1) {
      ConcurrentLinkedDeque var2 = new ConcurrentLinkedDeque();
      this.textDeque = var2;
      ConcurrentHashMap var3 = new ConcurrentHashMap();
      this.sharedESPMap = var3;
      KeySetView var4 = ConcurrentHashMap.newKeySet();
      this.telemetryUserSet = var4;
      ConcurrentHashMap var5 = new ConcurrentHashMap();
      this.textMap = var5;
      ConcurrentHashMap var6 = new ConcurrentHashMap();
      this.textMap2 = var6;
      Normal var7 = new Normal();
      this.normal2 = var7;
      Normal var8 = new Normal();
      this.normal = var8;
      PacketBus var9 = var1.getPacketBus();
      Consumer<TextPairPacket265> var26 = this::handleTextPairPacket265;
      var9.handleClassValue(TextPairPacket265.class, var26);
      PacketBus var27 = var1.getPacketBus();
      Consumer<TextPairPacket294> var44 = this::handleTextPairPacket294;
      var27.handleClassValue(TextPairPacket294.class, var44);
      PacketBus var45 = var1.getPacketBus();
      Consumer<TextMapPacket309> var62 = this::handleTextMapPacket309;
      var45.handleClassValue(TextMapPacket309.class, var62);
      PacketBus var63 = var1.getPacketBus();
      Consumer<FlagMessagePacket251> var80 = this::handleFlagMessagePacket251;
      var63.handleClassValue(FlagMessagePacket251.class, var80);
      PacketBus var81 = var1.getPacketBus();
      Consumer<PlayerDataPacket> var98 = this::handlePlayerDataPacket;
      var81.handleClassValue(PlayerDataPacket.class, var98);
      PacketBus var99 = var1.getPacketBus();
      PhobosClient cap4 = var1;
      Consumer<IntFloatPacket320> var116 = var1x -> handlePhobosClient3(cap4, var1x);
      var99.handleClassValue(IntFloatPacket320.class, var116);
      var var117 = new Listener<PostSend<LoginHelloC2SPacket>>() {
         public void handleObject2(PostSend<LoginHelloC2SPacket> postSend) {
            UserTagManager.this.flag2 = true;
            UserTagManager.this.name = postSend.getPacket().name();
         }
      };
      this.handleListener(var117);
      var var118 = new Listener<MoveEvent2>() {
         public void handleObject2(MoveEvent2 moveEvent2) {
            if (MinecraftHolder.mc.player != null) {
               if (UserTagManager.this.flag2) {
                  UserTagManager.this.flag2 = false;
                  UserTagManager.this.run2();
                  if (MinecraftHolder.mc.getServer() == null) {
                     UserTagManager.this.handlePhobosClient2(var1);
                  }
               }
            }
         }
      };
      this.handleListener(var118);
      var var119 = new SafeListener<DisconnectEvent2>() {
         public void handleObject(
            DisconnectEvent2 disconnectEvent2,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            UserTagManager.this.handlePhobosClient(var1);
         }
      };
      this.handleListener(var119);
      var var120 = new SafeListener<TickEvent2>() {
         public void handleObject(
            TickEvent2 tickEvent2,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            UserTagManager.this.sharedESPMap
               .entrySet()
               .removeIf(entry -> clientWorld.getEntityById(entry.getKey().id) != null && entry.getValue() + 3000L < System.currentTimeMillis());
            if (UserTagManager.this.normal.testLong2(5000L)) {
               for (PlayerEntity playerEntity : clientWorld.getPlayers()) {
                  if (!(playerEntity instanceof FakePlayer) && !(playerEntity instanceof IEntity iEntity && iEntity.isClientSide())) {
                     UserTagManager.this.handlePlayerEntity(playerEntity);
                  }
               }
            }

            if (CalculationsModule.check4() && UserTagManager.this.normal2.testLong2(2000L)) {
               var1.getPacketBus().handleNetworkPacket2(new IntFloatPacket320(EntityUtils.computeLivingEntity(clientPlayerEntity), clientPlayerEntity.getId()));
            }
         }
      };
      this.handleListener(var120);
      var var121 = new Listener<AddEntityEvent>() {
         public void handleObject2(AddEntityEvent addEntityEvent) {
            if (addEntityEvent.getEntity() instanceof PlayerEntity playerEntity && !playerEntity.isMainPlayer()) {
               if (!(playerEntity instanceof FakePlayer)) {
                  if (!UserTagManager.this.contains(playerEntity.getNameForScoreboard())) {
                     UserTagManager.this.handlePlayerEntity(playerEntity);
                  }
               }
            }
         }
      };
      this.handleListener(var121);
      var var122 = new Listener<ConnectionInfo>() {
         public void handleObject2(ConnectionInfo connectionInfo) {
         }
      };
      this.handleListener(var122);
      PacketBus var123 = var1.getPacketBus();
      EmptyPacket261 var124 = new EmptyPacket261();
      var123.handleNetworkPacket2(var124);
      Map var125 = this.sharedESPMap;
      Runnable var142 = var125::clear;
      ModuleListenerUtils.handleListenerHolder4(this, var142);
      Runnable var159 = this::run3;
      ModuleListenerUtils.handleListenerHolder(this, var159);
   }

   public static Identifier resolveIdentifier(Permission permission) {
      return switch (permission) {
         case Moderator, Admin -> identifier3;
         case Beta -> identifier2;
         default -> identifier;
      };
   }

   public Map<UserTagManager.SharedESP, Long> getSharedESPMap() {
      return this.sharedESPMap;
   }

   public Deque<String> getTextDeque() {
      return this.textDeque;
   }

   public void handleText(String text) {
      String text2 = this.describeText3(text);
      UserTagManager.TelemetryUser telemetryUser = this.textMap2.get(text2);
      if (telemetryUser != null) {
         this.telemetryUserSet.remove(telemetryUser);
         this.textMap2.remove(text2);
         this.textMap.remove(telemetryUser.text);
      }
   }

   public boolean contains(String text) {
      return this.textMap.containsKey(text);
   }

   public void handlePlayerEntity(PlayerEntity playerEntity) {
      String text;
      if (playerEntity.isCreative()) {
         text = "[C]";
      } else if (playerEntity.isSpectator()) {
         text = "[SP]";
      } else {
         text = "[S]";
      }

      PlayerListEntry playerListEntry;
      ClientHolder.getPhobosClient()
         .getPacketBus()
         .handleNetworkPacket2(
            new PlayerDataPacket(
               playerEntity.getId(),
               playerEntity.getX(),
               playerEntity.getY(),
               playerEntity.getZ(),
               playerEntity.getNameForScoreboard(),
               playerEntity.getUuid(),
               text,
               EntityUtils.computeLivingEntity(playerEntity),
               mc.getNetworkHandler() != null && (playerListEntry = mc.getNetworkHandler().getPlayerListEntry(playerEntity.getUuid())) != null
                  ? playerListEntry.getLatency()
                  : 0,
               playerEntity.getHeight()
            )
         );
   }

   public long computeInt2(int i) {
      Entry<UserTagManager.SharedESP, Long> entry = this.sharedESPMap
         .entrySet()
         .stream()
         .filter(entryx -> ((UserTagManager.SharedESP)entryx.getKey()).id == i)
         .findFirst()
         .orElse(null);
      return entry == null ? 0L : entry.getValue();
   }

   public String describeText(String text) {
      UserTagManager.TelemetryUser telemetryUser = this.textMap.get(text);
      if (telemetryUser != null) {
         return telemetryUser.text2 + telemetryUser.text3;
      } else {
         return mc.player != null && mc.player.getNameForScoreboard().equalsIgnoreCase(text) ? UserSession.userSession.describe() + "§r" : text;
      }
   }

   public void run2() {
      MinecraftClient var1 = MinecraftHolder.mc;
      ClientPlayNetworkHandler var2 = var1.getNetworkHandler();
      if (var2 != null) {
         MinecraftClient var3 = MinecraftHolder.mc;
         ClientPlayerEntity var4 = var3.player;
         if (var4 != null) {
            MinecraftClient var5 = MinecraftHolder.mc;
            ClientPlayNetworkHandler var6 = var5.getNetworkHandler();
            MinecraftClient var7 = MinecraftHolder.mc;
            ClientPlayerEntity var8 = var7.player;
            UUID var9 = var8.getUuid();
            PlayerListEntry var10 = var6.getPlayerListEntry(var9);
            if (var10 != null) {
               boolean var11 = var10 instanceof IPlayerListEntry;
               if (var10 != null && !var11) {
                  throw new ClassCastException("CHECKCAST failed");
               }

               UserSession var13 = UserSession.userSession;
               UserProfile var14 = var13.getUserProfile();
               Permission var15 = var14.getPermission();
               String var16 = var15.getFormatting();
               UserSession var17 = UserSession.userSession;
               String var18 = var17.getText();
               MinecraftClient var19 = MinecraftHolder.mc;
               ClientPlayerEntity var20 = var19.player;
               String var21 = var20.getNameForScoreboard();
               UserTagManager.TelemetryUser var12 = new UserTagManager.TelemetryUser(var16, var18, var21);
               ((IPlayerListEntry)(Object)var10).setUser(var12);
               Set var22 = this.telemetryUserSet;
               var22.add(var12);
               Map var23 = this.textMap;
               MinecraftClient var24 = MinecraftHolder.mc;
               ClientPlayerEntity var25 = var24.player;
               String var26 = var25.getNameForScoreboard();
               var23.put(var26, var12);
               Map var27 = this.textMap2;
               String var28 = var12.text3;
               var27.put(var28, var12);
            }
         }
      }
   }

   public String describeText2(String text) {
      return !text.startsWith("§") ? "" : text.substring(0, 2);
   }

   public String describeText3(String text) {
      return text.startsWith("§") ? text.substring(2) : text;
   }

   public void handlePhobosClient2(PhobosClient phobosClient) {
      MinecraftClient var2 = MinecraftHolder.mc;
      ServerInfo var3 = var2.getCurrentServerEntry();
      if (var3 != null) {
         MinecraftClient var4 = MinecraftHolder.mc;
         ServerInfo var5 = var4.getCurrentServerEntry();
         String var6 = var5.address;
         if (var6 != null) {
            this.flag = true & true;
            PacketBus var7 = phobosClient.getPacketBus();
            String var10 = this.name;
            IncognitoModule var11 = IncognitoModule.getIncognitoModule();
            boolean var12 = var11.check7();
            FlagTextPairPacket236 var9 = new FlagTextPairPacket236(var6, var10, var12);
            var7.handleNetworkPacket2(var9);
         }
      }
   }

   public void run3() {
      this.sharedESPMap.clear();
      this.telemetryUserSet.clear();
      this.textMap.clear();
      this.textMap2.clear();
   }

   public void handlePlayerDataPacket(PlayerDataPacket playerDataPacket) {
      if (mc.player != null && mc.world != null) {
         if (ConfigManager.getCloudSettings().shared.getValue()) {
            if (mc.world.getEntityById(playerDataPacket.getIntValue2()) == null) {
               this.sharedESPMap.entrySet().removeIf(entry -> entry.getKey().id == playerDataPacket.getIntValue2());
               this.sharedESPMap
                  .put(
                     new UserTagManager.SharedESP(
                        playerDataPacket.getIntValue2(),
                        playerDataPacket.getDoubleValue3(),
                        playerDataPacket.getDoubleValue2(),
                        playerDataPacket.getDoubleValue(),
                        playerDataPacket.getName(),
                        playerDataPacket.getUUID(),
                        playerDataPacket.getText(),
                        playerDataPacket.getFloatValue2(),
                        playerDataPacket.getIntValue(),
                        playerDataPacket.getFloatValue()
                     ),
                     System.currentTimeMillis()
                  );
            }
         }
      }
   }

   public Map<String, UserTagManager.TelemetryUser> getTextMap() {
      return this.textMap;
   }

   public static void handlePhobosClient3(PhobosClient phobosClient, IntFloatPacket320 intFloatPacket320) {
      if (ConfigManager.getCloudSettings().shared.getValue() && mc.world != null && CalculationsModule.check4()) {
         if (mc.world.getEntityById(intFloatPacket320.getIntValue2()) instanceof LivingEntity livingEntity && !livingEntity.isDead()) {
            phobosClient.getFeatureRegistry().getDamageTracker().handleLivingEntity3(livingEntity, intFloatPacket320.getFloatValue2());
         }
      }
   }

   public boolean isFlag() {
      return this.flag;
   }

   public void handleTextPairPacket294(TextPairPacket294 textPairPacket294) {
      this.handleText(textPairPacket294.getText());
   }

   public Normal getNormal() {
      return this.normal;
   }

   public void handleTextMapPacket309(TextMapPacket309 textMapPacket309) {
      this.textDeque.clear();

      for (Entry<String, String> entry : textMapPacket309.getTextMap().entrySet()) {
         this.textDeque.addFirst(entry.getKey() + ": " + entry.getValue());
         if (this.textDeque.size() > 100) {
            this.textDeque.poll();
         }
      }
   }

   public Normal getNormal2() {
      return this.normal2;
   }

   public boolean isFlag2() {
      return this.flag2;
   }

   public void handleTextPairPacket265(TextPairPacket265 textPairPacket265) {
      if (mc.getNetworkHandler() != null) {
         String text = textPairPacket265.getText();
         String text2 = textPairPacket265.getText2();
         String text3 = this.describeText2(text);
         String text4 = this.describeText3(text);
         UserTagManager.TelemetryUser telemetryUser = new UserTagManager.TelemetryUser(text3, text4, text2);
         this.telemetryUserSet.add(telemetryUser);
         this.textMap.put(text2, telemetryUser);
         this.textMap2.put(text4, telemetryUser);
         PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(text2);
         if (playerListEntry != null) {
            IPlayerListEntry iPlayerListEntry = (IPlayerListEntry)(Object)playerListEntry;
            iPlayerListEntry.setUser(telemetryUser);
         }
      }
   }

   public String getName() {
      return this.name;
   }

   public Map<String, UserTagManager.TelemetryUser> getTextMap2() {
      return this.textMap2;
   }

   public void handleFlagMessagePacket251(FlagMessagePacket251 flagMessagePacket251) {
      this.textDeque.addFirst(flagMessagePacket251.getText2() + ": " + flagMessagePacket251.getMessage());
      if (this.textDeque.size() > 100) {
         this.textDeque.poll();
      }

      if (mc.currentScreen instanceof ITitleScreen iTitleScreen) {
         iTitleScreen.onMessage();
      }
   }

   public record SharedESP(int id, double x, double y, double z, String name, UUID uuid, String gameMode, float health, int latency, float height) {
   }

   public static class TelemetryUser {
      public final String text;
      public final Identifier identifier;
      public final String text2;
      public final Permission permission;
      public final String text3;

      public String getText() {
         return this.text;
      }

      public String getText2() {
         return this.text2;
      }

      public TelemetryUser(String text, String text2, String text3) {
         this.text2 = text;
         this.text3 = text2;
         this.text = text3;
         this.permission = this.getPermission2();
         this.identifier = UserTagManager.resolveIdentifier(this.permission);
      }

      public Permission getPermission() {
         return this.permission;
      }

      public Identifier getIdentifier() {
         return this.identifier;
      }

      public Permission getPermission2() {
         if (this.text2 == null) {
            return Permission.User;
         }

         for (Permission permission : Permission.values()) {
            if (permission.getFormatting().equals(this.text2)) {
               return permission;
            }
         }

         return Permission.User;
      }

      public String getText3() {
         return this.text3;
      }

      public TelemetryUser(String text, String text2, String text3, Permission permission, Identifier identifier) {
         this.text2 = text;
         this.text3 = text2;
         this.text = text3;
         this.permission = permission;
         this.identifier = identifier;
      }
   }
}
