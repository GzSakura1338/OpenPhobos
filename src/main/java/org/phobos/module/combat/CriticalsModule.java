package org.phobos.module.combat;

import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import org.earthhack.ducks.IPlayerInteractEntityC2SPacket;
import org.phobos.api.TextProvider;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.MultiSelectSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.player.PlayerUtils;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.event.events.PacketEvent.Send;
import org.phobos.event.SafeListener;
import org.phobos.mixin.game.entity.IClientPlayerEntity;
import org.phobos.module.Module;

public class CriticalsModule extends Module {
   public final Setting<CriticalsModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", CriticalsModule.Mode.Vanilla));
   public final Setting<Map<CriticalsModule.Pause, Boolean>> pause = this.resolveSetting2(
      new MultiSelectSetting<>("Pause", CriticalsModule.Pause.class).resolveMultiSelectSetting6(CriticalsModule.Pause.values())
   );
   public final TimeGate.Normal normal = new TimeGate.Normal();
   public static CriticalsModule criticalsModule;

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   public CriticalsModule(PhobosClient phobosClient) {
      super(phobosClient, "Criticals", Categories.combat, "Deals more damage to enemies.");
      this.handleListener(
         new SafeListener<Send<PlayerInteractEntityC2SPacket>>() {
            public void handleObject(
               Send<PlayerInteractEntityC2SPacket> send,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               for (Entry<CriticalsModule.Pause, Boolean> entry : CriticalsModule.this.pause.getValue().entrySet()) {
                  if (entry.getValue() && entry.getKey().pause(clientPlayerEntity, clientWorld)) {
                     return;
                  }
               }

               if (!AutoMaceModule.check()) {
                  if (((IPlayerInteractEntityC2SPacket)(Object)send.getPacket()).entity() instanceof LivingEntity
                     && ((IPlayerInteractEntityC2SPacket)(Object)send.getPacket()).attack()
                     && mc.isOnThread()
                     && clientPlayerEntity.isOnGround()
                     && !clientPlayerEntity.isSpectator()
                     && !clientPlayerEntity.hasStatusEffect(StatusEffects.BLINDNESS)
                     && !clientPlayerEntity.hasVehicle()
                     && !clientPlayerEntity.isSubmergedInWater()
                     && !clientPlayerEntity.isClimbing()
                     && CriticalsModule.this.normal.testLong(400L)) {
                     switch ((CriticalsModule.Mode)CriticalsModule.this.mode.getValue()) {
                        case _2b2t:
                           clientPlayerEntity.networkHandler
                              .sendPacket(
                                 new PositionAndOnGround(clientPlayerEntity.getX(), clientPlayerEntity.getY() + 0.05, clientPlayerEntity.getZ(), false)
                              );
                           clientPlayerEntity.networkHandler
                              .sendPacket(new PositionAndOnGround(clientPlayerEntity.getX(), clientPlayerEntity.getY(), clientPlayerEntity.getZ(), false));
                           clientPlayerEntity.networkHandler
                              .sendPacket(
                                 new PositionAndOnGround(clientPlayerEntity.getX(), clientPlayerEntity.getY() + 0.03, clientPlayerEntity.getZ(), false)
                              );
                           clientPlayerEntity.networkHandler
                              .sendPacket(new PositionAndOnGround(clientPlayerEntity.getX(), clientPlayerEntity.getY(), clientPlayerEntity.getZ(), false));
                           break;
                        case Strict:
                           clientPlayerEntity.networkHandler
                              .sendPacket(
                                 new PositionAndOnGround(
                                    clientPlayerEntity.getX(), clientPlayerEntity.getY() + 0.062600301692775, clientPlayerEntity.getZ(), false
                                 )
                              );
                           clientPlayerEntity.networkHandler
                              .sendPacket(
                                 new PositionAndOnGround(
                                    clientPlayerEntity.getX(), clientPlayerEntity.getY() + 0.07260029960661, clientPlayerEntity.getZ(), false
                                 )
                              );
                           clientPlayerEntity.networkHandler
                              .sendPacket(new PositionAndOnGround(clientPlayerEntity.getX(), clientPlayerEntity.getY(), clientPlayerEntity.getZ(), false));
                           clientPlayerEntity.networkHandler
                              .sendPacket(new PositionAndOnGround(clientPlayerEntity.getX(), clientPlayerEntity.getY(), clientPlayerEntity.getZ(), false));
                        case Grim:
                        case Vanilla:
                        default:
                           break;
                        case Jump:
                           if (clientPlayerEntity.isOnGround()) {
                              PlayerEntity playerEntity = phobosClient.getMovementSimulator().resolvePlayerEntity(clientPlayerEntity);
                              double d = playerEntity.getX();
                              double d2 = playerEntity.getY();
                              double d3 = playerEntity.getZ();
                              float f = playerEntity.getYaw();
                              float f2 = playerEntity.getPitch();
                              clientPlayerEntity.networkHandler.sendPacket(new Full(d, d2 + 0.0625, d3, f, f2, false));
                              clientPlayerEntity.networkHandler.sendPacket(new Full(d, d2 + 0.0625013579, d3, f, f2, false));
                              clientPlayerEntity.networkHandler.sendPacket(new Full(d, d2 + 1.3579E-6, d3, f, f2, false));
                           }
                           break;
                        case Legit:
                           if (clientPlayerEntity.isOnGround()) {
                              PlayerEntity playerEntity2 = phobosClient.getMovementSimulator().resolvePlayerEntity(clientPlayerEntity);
                              double d4 = playerEntity2.getX();
                              double d5 = playerEntity2.getY();
                              double d6 = playerEntity2.getZ();
                              float f3 = playerEntity2.getYaw();
                              float f4 = playerEntity2.getPitch();
                              clientPlayerEntity.networkHandler.sendPacket(new Full(d4, d5, d6, f3, f4, true));
                              clientPlayerEntity.networkHandler.sendPacket(new Full(d4, d5 + 0.0625, d6, f3, f4, false));
                              clientPlayerEntity.networkHandler.sendPacket(new Full(d4, d5 + 0.04535, d6, f3, f4, false));
                           }
                     }

                     if (!CriticalsModule.this.mode.testObject2(CriticalsModule.Mode.Legit)) {
                        ((IClientPlayerEntity)(Object)clientPlayerEntity).setLastOnGround(false);
                     }

                     CriticalsModule.this.normal.run();
                  }
               }
            }
         }
      );
      criticalsModule = this;
   }

   public static boolean check() {
      return criticalsModule.check7() && criticalsModule.mode.testObject2(CriticalsModule.Mode.Jump);
   }

   public static boolean check2() {
      return criticalsModule.check7()
         && (criticalsModule.mode.testObject2(CriticalsModule.Mode.Legit) || criticalsModule.mode.testObject2(CriticalsModule.Mode.Jump));
   }

   private enum Mode implements TextProvider {
      Vanilla("Vanilla packet criticals"),
      Strict("Strict NCP criticals"),
      Legit("Delays Aura attacks to always deal critical damage"),
      Jump("Performs a jump"),
      Grim("Grim 1-5E reverse jump"),
      _2b2t("2b2t criticals");

      public final String text;

      Mode(final String description) {
         this.text = description;
      }

      @Override
      public String getText() {
         return this.text;
      }
   }

   private enum Pause implements TextProvider {
      Move("Pause when moving") {
         @Override
         public boolean pause(ClientPlayerEntity player, ClientWorld world) {
            return PlayerUtils.check5();
         }
      },
      Phase("Pause when not phased") {
         @Override
         public boolean pause(ClientPlayerEntity player, ClientWorld world) {
            return !BlockUtils.testPlayerEntity(player, world);
         }
      };

      public final String text;

      @Override
      public String getText() {
         return this.text;
      }

      Pause(final String description) {
         this.text = description;
      }

      public boolean pause(ClientPlayerEntity player, ClientWorld world) {
         return false;
      }
   }
}
