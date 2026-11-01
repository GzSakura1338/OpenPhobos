package org.phobos.module.combat;

import java.awt.Color;
import java.util.LinkedHashSet;
import java.util.Set;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TrackedPosition;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import org.phobos.api.NumberRange;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.MutableLongSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.animation.AnimatedBox;
import org.phobos.core.render.RenderContext;
import org.phobos.core.PhobosClient;
import org.phobos.core.fakeplayer.FakePlayer;
import org.phobos.core.entity.EntityInterpolation;
import org.phobos.core.module.Categories;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.core.render.Render3d.Cuboid;
import org.phobos.event.AttackEvent2;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.module.Module;

public class BacktrackModule extends Module {
   public final Setting<Float> width;
   public Entity entity;
   public final Setting<Color> line;
   public final Setting<Color> box;
   public final AnimatedBox animatedBox;
   public final Set<BacktrackModule.PacketWrapper> packetWrapperSet;
   public TrackedPosition trackedPosition;
   public final Setting<Double> min = this.resolveSetting2(new NumberSetting<>("Min", 3.0, 0.1, 6.0));
   public final Setting<Double> max = this.resolveSetting2(new NumberSetting<>("Max", 6.0, 3.0, 10.0));
   public final Setting<Boolean> render;
   public final Setting<NumberRange.Mutable<Long>> delay = this.resolveSetting2(new MutableLongSetting("Delay", 100L, 10L, 2000L));
   public final Setting<BacktrackModule.Render> mode;

   public boolean check() {
      return this.entity != null && this.entity.isAlive() && this.testEntity(this.entity);
   }

   public BacktrackModule(PhobosClient phobosClient) {
      super(phobosClient, "Backtrack", "Forces lag to compensate reach", Categories.combat);
      this.render = this.resolveSetting2(new BooleanSetting("Render", false));
      this.mode = this.resolveSetting2(new EnumSetting<>("Mode", BacktrackModule.Render.Box));
      this.box = this.resolveSetting2(
         new ColorSetting("Box", new Color(255, 255, 255, 50)).getColorSetting().resolveSetting4(this.mode, BacktrackModule.Render.Box)
      );
      this.line = this.resolveSetting2(new ColorSetting("Line", Color.WHITE).getColorSetting().resolveSetting4(this.mode, BacktrackModule.Render.Box));
      this.width = this.resolveSetting2(new NumberSetting<>("Width", 1.0F, 0.0F, 2.0F).resolveSetting4(this.mode, BacktrackModule.Render.Box));
      this.packetWrapperSet = new LinkedHashSet<>();
      this.animatedBox = new AnimatedBox();
      this.resolveSettingGroup(this.render, this.mode, this.box, this.line, this.width);
      this.handleListener(
         new SafeListener<Receive<?>>() {
            public void handleObject(
               Receive<?> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               synchronized (BacktrackModule.this.packetWrapperSet) {
                  Packet<?> packet = receive.getPacket();
                  if (!receive.isCancelled()) {
                     if (!BacktrackModule.this.packetWrapperSet.isEmpty() || BacktrackModule.this.check()) {
                        if (!(packet instanceof ChatMessageS2CPacket)
                           && !(packet instanceof GameMessageS2CPacket)
                           && !(packet instanceof CommandExecutionC2SPacket)) {
                           if (!(packet instanceof DisconnectS2CPacket) && !(packet instanceof PlayerPositionLookS2CPacket)) {
                              if (!(
                                 packet instanceof PlaySoundS2CPacket playSoundS2CPacket
                                    && ((SoundEvent)playSoundS2CPacket.getSound().value()).equals(SoundEvents.ENTITY_PLAYER_HURT)
                              )) {
                                 if (packet instanceof HealthUpdateS2CPacket && ((HealthUpdateS2CPacket)packet).getHealth() <= 0.0F) {
                                    BacktrackModule.this.handleFlag(true, false);
                                 } else {
                                    boolean flag = packet instanceof EntityS2CPacket
                                       && ((EntityS2CPacket)packet).getEntity(clientWorld) == BacktrackModule.this.entity;
                                    boolean flag2 = packet instanceof EntityPositionS2CPacket
                                       && BacktrackModule.this.entity != null
                                       && ((EntityPositionS2CPacket)packet).getEntityId() == BacktrackModule.this.entity.getId();
                                    if (flag || flag2) {
                                       Vec3d vec3d;
                                       if (packet instanceof EntityS2CPacket entityS2CPacket && BacktrackModule.this.trackedPosition != null) {
                                          vec3d = BacktrackModule.this.trackedPosition
                                             .withDelta(entityS2CPacket.getDeltaX(), entityS2CPacket.getDeltaY(), entityS2CPacket.getDeltaZ());
                                       } else {
                                          assert packet instanceof EntityPositionS2CPacket;
                                          EntityPositionS2CPacket entityPositionS2CPacket = (EntityPositionS2CPacket)packet;
                                          vec3d = new Vec3d(entityPositionS2CPacket.getX(), entityPositionS2CPacket.getY(), entityPositionS2CPacket.getZ());
                                       }

                                       if (BacktrackModule.this.trackedPosition != null) {
                                          BacktrackModule.this.trackedPosition.setPos(vec3d);
                                       }

                                       if (BacktrackModule.this.entity != null) {
                                          double d = BacktrackModule.this.entity.squaredDistanceTo(clientPlayerEntity);
                                          double d2 = clientPlayerEntity.squaredDistanceTo(vec3d);
                                          if (d2 < d) {
                                             BacktrackModule.this.handleFlag2(false);
                                             return;
                                          }
                                       }
                                    }

                                    receive.setCancelled(true);
                                    BacktrackModule.this.packetWrapperSet.add(new BacktrackModule.PacketWrapper(packet, System.currentTimeMillis()));
                                 }
                              }
                           } else {
                              BacktrackModule.this.handleFlag(true, false);
                           }
                        }
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<TickEvent2>(1002) {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (BacktrackModule.this.check()) {
                  BacktrackModule.this.handleFlag2(false);
               } else {
                  BacktrackModule.this.handleFlag(true, false);
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<AttackEvent2>() {
            public void handleObject(
               AttackEvent2 attackEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!mc.isInSingleplayer()) {
                  Entity entity = attackEvent2.getEntity();
                  if (BacktrackModule.this.testEntity(entity)) {
                     if (!entity.equals(BacktrackModule.this.entity)) {
                        BacktrackModule.this.handleFlag(true, false);
                        BacktrackModule.this.trackedPosition = new TrackedPosition();
                        BacktrackModule.this.trackedPosition.setPos(entity.getTrackedPosition().getPos());
                        BacktrackModule.this.animatedBox.handleBox(EntityInterpolation.resolveBox2(entity, entity.getPos()), true);
                     }

                     BacktrackModule.this.entity = entity;
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<RenderContext>() {
            public void handleObject(
               RenderContext renderContext,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (BacktrackModule.this.render.getValue() && BacktrackModule.this.entity != null && BacktrackModule.this.trackedPosition != null) {
                  switch ((BacktrackModule.Render)BacktrackModule.this.mode.getValue()) {
                     case Box:
                        BacktrackModule.this.animatedBox.run();
                        BacktrackModule.this.animatedBox
                           .handleBox(EntityInterpolation.resolveBox2(BacktrackModule.this.entity, BacktrackModule.this.trackedPosition.getPos()), false);
                        Render3d.handleCuboid(
                           Cuboid.createBuilder()
                              .resolveBuilder11(BacktrackModule.this.animatedBox.createBox())
                              .resolveBuilder18(BacktrackModule.this.box.getValue(), BacktrackModule.this.line.getValue())
                              .resolveBuilder12(BacktrackModule.this.width.getValue())
                              .getCuboid()
                        );
                        break;
                     case Model:
                        if (!(BacktrackModule.this.entity instanceof AbstractClientPlayerEntity abstractClientPlayerEntity)) {
                           return;
                        }

                        FakePlayer fakePlayer = new FakePlayer(clientWorld, BacktrackModule.this.entity.getNameForScoreboard());
                        fakePlayer.setSkinTextures(abstractClientPlayerEntity.getSkinTextures());
                        fakePlayer.copyFrom(BacktrackModule.this.entity);
                        phobosClient.getEntityFadeManager().handleEntity3(fakePlayer);
                  }
               }
            }
         }
      );
      ModuleListenerUtils.handleListenerHolder3(this, () -> this.handleFlag(false, true));
   }

   @Override
   public void run4() {
      this.handleFlag(false, false);
   }

   public boolean testEntity(Entity entity) {
      return mc.player != null
         && entity.isAttackable()
         && this.testDouble(entity.getBoundingBox().squaredMagnitude(mc.player.getEyePos()))
         && mc.player.age > 10
         && !(entity instanceof FakePlayer);
   }

   public boolean testDouble(double d) {
      return d >= this.min.getValue() && d <= this.max.getValue();
   }

   @Override
   public void run3() {
      this.handleFlag(true, false);
   }

   public void handleFlag(boolean flag, boolean flag2) {
      if (flag && !flag2) {
         this.handleFlag2(true);
      } else if (flag2) {
         this.packetWrapperSet.clear();
      }

      this.entity = null;
      this.trackedPosition = null;
   }

   public void handlePacket2(Packet<?> packet) {
      try {
         ((Packet)packet).apply(mc.getNetworkHandler());
      } catch (Exception var3) {
      }
   }

   @Override
   public String nothing() {
      return String.valueOf(this.delay.getValue());
   }

   public void handleFlag2(boolean flag) {
      synchronized (this.packetWrapperSet) {
         this.packetWrapperSet.removeIf(packetWrapper -> {
            if (!flag && packetWrapper.time + this.delay.getValue().getNumber() >= System.currentTimeMillis()) {
               return false;
            }

            this.delay.getValue().run6();
            mc.renderTaskQueue.add(() -> this.handlePacket2(packetWrapper.packet));
            return true;
         });
      }
   }

   private record PacketWrapper(Packet<?> packet, long time) {
   }

   private enum Render {
      Box,
      Model;
   }
}
