package org.phobos.module.movement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.phobos.api.ListenerHolder;
import org.phobos.api.TextProvider;
import org.phobos.api.Visibility;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.TimeGate.Normal;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.event.events.PlayerMoveEvent;
import org.phobos.core.render.RenderContext;
import org.phobos.core.animation.TimedFloatAnimator;
import org.phobos.core.player.InteractionManager;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.ClientHolder;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.math.MathUtils;
import org.phobos.core.player.PlayerUtils;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.event.events.PacketEvent.PostSend;
import org.phobos.core.event.events.PacketEvent.Send;
import org.phobos.core.inventory.ItemSwitcher.SwitchResult;
import org.phobos.core.movement.ElytraManager.FlyConfiguration;
import org.phobos.event.JumpEvent;
import org.phobos.event.Listener;
import org.phobos.event.MoveEvent2;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.RemoveEntityEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent;
import org.phobos.event.TickEvent2;
import org.phobos.mixin.game.entity.IFireworkRocketEntity;
import org.phobos.mixin.net.interfaces.IPlayerInputC2SPacket;
import org.phobos.module.Module;
import org.phobos.module.player.SilentHotbarModule;
import org.phobos.module.render.FreeLookModule;

public class ElytraFlyModule extends Module {
   public final Setting<Boolean> use;
   public final Setting<Boolean> stand;
   public final Setting<Integer> top;
   public final Setting<Boolean> infinite;
   public final Setting<InteractionManager.Silent> useSilent;
   public final Setting<Float> verticalSpeed;
   public final Setting<Float> speed;
   public final Setting<Double> multiplier;
   public final Setting<Float> exploitPitch;
   public final Setting<Boolean> look;
   public final Setting<Boolean> n0Tick;
   public final Setting<Boolean> staticValue;
   public final Setting<Boolean> limit;
   public final Setting<Boolean> vertical;
   public final Setting<Float> max;
   public final Setting<Float> accelerationFactor;
   public final Setting<Boolean> rotate;
   public static ElytraFlyModule elytraFlyModule;
   public final Setting<Boolean> control;
   public final Setting<InteractionManager.Silent> switchValue;
   public final Setting<ElytraFlyModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", ElytraFlyModule.Mode.Normal));
   public final Setting<Double> n0TickStart;
   public final Setting<ElytraFlyModule.ZeroTick> n0TickMode;
   public final Setting<Boolean> n0TickAccelerate;
   public final Setting<Boolean> exploitCancel;
   public final Setting<Boolean> exploit;
   public final Setting<Boolean> acceleration;
   public final Setting<ElytraFlyModule.Strict> strict;

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      ItemStack itemStack = clientPlayerEntity.getInventory().getArmorStack(2);
      return itemStack.isOf(Items.ELYTRA) && ElytraItem.isUsable(itemStack);
   }

   public static boolean check() {
      return elytraFlyModule.check7() && elytraFlyModule.mode.getValue().equals(ElytraFlyModule.Mode.Bounce);
   }

   public boolean testClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity) {
      if (this.testClientPlayerEntity4(clientPlayerEntity) && this.testClientPlayerEntity3(clientPlayerEntity)) {
         clientPlayerEntity.networkHandler
            .sendPacket(new ClientCommandC2SPacket(clientPlayerEntity, net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode.START_FALL_FLYING));
         return true;
      } else {
         return false;
      }
   }

   public static boolean check2() {
      return mc.player == null ? false : elytraFlyModule.testClientPlayerEntity2(mc.player);
   }

   @Override
   public void run4() {
      ClientHolder.getEventBus().handleObject4(this.mode.getValue());
      this.mode.getValue().run4();
   }

   public boolean testClientPlayerEntity3(ClientPlayerEntity clientPlayerEntity) {
      if (!clientPlayerEntity.isTouchingWater()
         && !clientPlayerEntity.hasStatusEffect(StatusEffects.LEVITATION)
         && this.testClientPlayerEntity(clientPlayerEntity)) {
         clientPlayerEntity.startFallFlying();
         return true;
      } else {
         return false;
      }
   }

   public static boolean check3() {
      return elytraFlyModule != null
         && elytraFlyModule.check7()
         && elytraFlyModule.mode.testObject2(ElytraFlyModule.Mode.Shift)
         && elytraFlyModule.control.getValue();
   }

   public ElytraFlyModule(PhobosClient phobosClient) {
      super(phobosClient, "ElytraFly", Categories.movement, "Modifies the way you travel on an elytra");
      this.limit = this.resolveSetting2(new BooleanSetting("Limit", false));
      this.max = this.resolveSetting2(new NumberSetting<>("Max", 2.5F, 0.1F, 20.0F));
      this.speed = this.resolveSetting2(
         new NumberSetting<>("Speed", 1.5F, 0.1F, 20.0F)
            .getNumberSetting()
            .resolveSetting3(this.resolveVisibility2(this.mode, ElytraFlyModule.Mode.Boost, ElytraFlyModule.Mode.Normal))
      );
      this.top = this.resolveSetting2(new NumberSetting<>("Top", 200, 50, 360).resolveSetting4(this.mode, ElytraFlyModule.Mode.Infinite));
      this.infinite = this.resolveSetting2(new BooleanSetting("Infinite").resolveSetting4(this.mode, ElytraFlyModule.Mode.Packet));
      this.strict = this.resolveSetting2(new EnumSetting<>("Strict", ElytraFlyModule.Strict.None).resolveSetting4(this.mode, ElytraFlyModule.Mode.Packet));
      this.rotate = this.resolveSetting2(new BooleanSetting("Rotate").resolveSetting4(this.mode, ElytraFlyModule.Mode.Bounce));
      this.stand = this.resolveSetting2(new BooleanSetting("Stand").resolveSetting4(this.mode, ElytraFlyModule.Mode.Bounce));
      this.multiplier = this.resolveSetting2(
         new NumberSetting<>("Multiplier", 104.0, 10.0, 500.0, "%")
            .resolveNumberSetting("Grim", 104.0)
            .resolveSetting4(this.mode, ElytraFlyModule.Mode.Firework)
      );
      this.use = this.resolveSetting2(new BooleanSetting("Use").resolveSetting7("Uses fireworks"));
      this.useSilent = this.resolveSetting2(new EnumSetting<>("UseSilent", InteractionManager.Silent.Normal));
      this.exploit = this.resolveSetting2(new BooleanSetting("Exploit"));
      this.exploitPitch = this.resolveSetting2(new NumberSetting<>("ExploitPitch", 35.0F, 0.0F, 90.0F));
      this.exploitCancel = this.resolveSetting2(new BooleanSetting("ExploitCancel"));
      this.look = this.resolveSetting2(
         new BooleanSetting("Look")
            .resolveSetting4(this.mode, ElytraFlyModule.Mode.Firework)
            .resolveSetting2(Visibility.resolveVisibility2(this.exploit).getVisibility2(), true)
            .resolveSetting7("Look in the boost direction")
      );
      this.staticValue = this.resolveSetting2(
         new BooleanSetting("Static")
            .resolveSetting4(this.mode, ElytraFlyModule.Mode.Firework)
            .resolveSetting2(Visibility.resolveVisibility2(this.exploit).getVisibility2(), true)
            .resolveSetting7("Makes you not move if you aren't pressing movement keys")
      );
      this.n0Tick = this.resolveSetting2(new BooleanSetting("0-Tick").resolveSetting7("Modifies jump height"));
      this.n0TickAccelerate = this.resolveSetting2(new BooleanSetting("0-TickAccelerate"));
      this.n0TickMode = this.resolveSetting2(new EnumSetting<>("0-TickMode", ElytraFlyModule.ZeroTick.Jump));
      this.n0TickStart = this.resolveSetting2(new NumberSetting<>("0-TickStart", 40.0, 10.0, 100.0));
      this.switchValue = this.resolveSetting2(
         new EnumSetting<>("Switch", InteractionManager.Silent.Normal).resolveSetting4(this.mode, ElytraFlyModule.Mode.Shift)
      );
      this.control = this.resolveSetting2(
         new BooleanSetting("Control").resolveSetting7("Disables firework boost").resolveSetting4(this.mode, ElytraFlyModule.Mode.Shift)
      );
      this.acceleration = this.resolveSetting2(new BooleanSetting("Acceleration"));
      this.accelerationFactor = this.resolveSetting2(new NumberSetting<>("AccelerationFactor", 9.0F, 0.0F, 100.0F));
      this.vertical = this.resolveSetting2(new BooleanSetting("Vertical", true));
      this.verticalSpeed = this.resolveSetting2(new NumberSetting<>("VerticalSpeed", 0.47F, 0.0F, 2.0F));
      this.resolveSettingGroup(this.limit, this.max).resolveSettingGroup4(this.mode, ElytraFlyModule.Mode.Boost);
      this.resolveSettingGroup(this.acceleration, this.accelerationFactor).resolveSettingGroup4(this.mode, ElytraFlyModule.Mode.Packet);
      this.resolveSettingGroup(this.vertical, this.verticalSpeed)
         .resolveSettingGroup5(this.resolveVisibility2(this.mode, ElytraFlyModule.Mode.Packet, ElytraFlyModule.Mode.Normal));
      this.resolveSettingGroup(this.n0Tick, this.n0TickAccelerate, this.n0TickMode, this.n0TickStart)
         .resolveSettingGroup4(this.mode, ElytraFlyModule.Mode.Bounce);
      this.resolveSettingGroup(this.use, this.useSilent).resolveSettingGroup4(this.mode, ElytraFlyModule.Mode.Firework);
      this.resolveSettingGroup(this.exploit, this.exploitPitch, this.exploitCancel).resolveSettingGroup4(this.mode, ElytraFlyModule.Mode.Firework);

      for (ElytraFlyModule.Mode mode : ElytraFlyModule.Mode.values()) {
         mode.init(phobosClient, this);
      }

      this.mode.handleBiConsumer((modex, mode2) -> {
         if (this.check7()) {
            ClientHolder.getEventBus().handleObject4(modex);
            modex.run4();
            ClientHolder.getEventBus().handleObject(mode2);
            mode2.run3();
         }
      });
      elytraFlyModule = this;
   }

   @Override
   public void run3() {
      ClientHolder.getEventBus().handleObject(this.mode.getValue());
      this.mode.getValue().run3();
   }

   public boolean testClientPlayerEntity4(ClientPlayerEntity clientPlayerEntity) {
      return !clientPlayerEntity.getAbilities().flying && !clientPlayerEntity.hasVehicle() && !clientPlayerEntity.isClimbing()
         ? this.testClientPlayerEntity(clientPlayerEntity)
         : false;
   }

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   private enum Mode implements ListenerHolder, TextProvider {
      Normal {
         @Override
         public String getText() {
            return "Vanilla ElytraFly";
         }

         @Override
         public void init(PhobosClient phobos, ElytraFlyModule module) {
            this.handleListener(
               new SafeListener<MoveEvent2>() {
                  public void handleObject(
                     MoveEvent2 moveEvent2,
                     ClientPlayerEntity clientPlayerEntity,
                     ClientWorld clientWorld,
                     ClientPlayerInteractionManager clientPlayerInteractionManager
                  ) {
                     if (PlayerActionUtils.testClientPlayerEntity3(clientPlayerEntity)) {
                        ItemStack itemStack = clientPlayerEntity.getEquippedStack(EquipmentSlot.CHEST);
                        if (itemStack.isOf(Items.ELYTRA) && ElytraItem.isUsable(itemStack)) {
                           double[] doubles = PlayerUtils.values2(clientPlayerEntity, module.speed.getValue().floatValue());
                           moveEvent2.setDoubleValue3(doubles[0]);
                           moveEvent2.setDoubleValue2(doubles[1]);
                           moveEvent2.setDoubleValue(
                              module.vertical.getValue()
                                 ? (
                                    mc.options.jumpKey.isPressed()
                                       ? module.verticalSpeed.getValue().floatValue()
                                       : (mc.options.sneakKey.isPressed() ? -module.verticalSpeed.getValue() : 0.0)
                                 )
                                 : 0.0
                           );
                           clientPlayerEntity.setVelocity(moveEvent2.getVec3d());
                        }
                     }
                  }
               }
            );
         }
      },
      Boost {
         @Override
         public void init(PhobosClient phobos, ElytraFlyModule module) {
            this.handleListener(
               new SafeListener<MoveEvent2>() {
                  public void handleObject(
                     MoveEvent2 moveEvent2,
                     ClientPlayerEntity clientPlayerEntity,
                     ClientWorld clientWorld,
                     ClientPlayerInteractionManager clientPlayerInteractionManager
                  ) {
                     if (PlayerActionUtils.testClientPlayerEntity3(clientPlayerEntity)) {
                        double d = module.speed.getValue() / 10.0F;
                        double[] doubles = PlayerUtils.values6(d, phobos.getRotationManager().getFloatValue15());
                        moveEvent2.setDoubleValue3(moveEvent2.getDoubleValue2() + doubles[0]);
                        moveEvent2.setDoubleValue2(moveEvent2.getDoubleValue() + doubles[1]);
                        double d2 = Math.hypot(moveEvent2.getDoubleValue2(), moveEvent2.getDoubleValue());
                        if (d2 > module.max.getValue().floatValue() && module.limit.getValue()) {
                           moveEvent2.setDoubleValue3(moveEvent2.getDoubleValue2() * module.max.getValue().floatValue() / d2);
                           moveEvent2.setDoubleValue2(moveEvent2.getDoubleValue() * module.max.getValue().floatValue() / d2);
                        }

                        clientPlayerEntity.setVelocity(moveEvent2.getVec3d());
                        moveEvent2.setCancelled(true);
                     }
                  }
               }
            );
         }

         @Override
         public String getText() {
            return "Boosts you forward";
         }
      },
      Shift {
         public final Normal normal = new Normal();
         public float yAcceleration;
         public float acceleration;

         @Override
         public void init(PhobosClient phobos, ElytraFlyModule module) {
            this.handleListener(
               new SafeListener<MoveEvent2>() {
                  public void handleObject(
                     MoveEvent2 moveEvent2,
                     ClientPlayerEntity clientPlayerEntity,
                     ClientWorld clientWorld,
                     ClientPlayerInteractionManager clientPlayerInteractionManager
                  ) {
                     if (PlayerActionUtils.testClientPlayerEntity3(clientPlayerEntity)) {
                        MutableBoolean mutableBoolean = new MutableBoolean();
                        Iterator var6 = clientWorld.getOtherEntities(null, clientPlayerEntity.getBoundingBox().expand(3.0)).iterator();

                        while (true) {
                           if (var6.hasNext()) {
                              Entity entity = (Entity)var6.next();
                              if (!(entity instanceof IFireworkRocketEntity iFireworkRocketEntity)
                                 || !(iFireworkRocketEntity.getShooter() instanceof ClientPlayerEntity)) {
                                 continue;
                              }

                              mutableBoolean.setTrue();
                              break;
                           }

                           if (!normal.testLong(500L)) {
                              mutableBoolean.setValue(!normal.testLong(75L));
                           } else {
                              phobos.getInventoryManager()
                                 .handleConsumer2(
                                    itemSwitcher -> {
                                       Slot slot = itemSwitcher.resolveObject(slotx -> slotx.getStack().getItem() instanceof FireworkRocketItem ? slotx : null);
                                       if (slot != null) {
                                          normal.run();
                                          itemSwitcher.resolveSwitchResult(Hand.MAIN_HAND, slot, module.switchValue.getValue().getFlag() | 1);
                                          clientPlayerEntity.networkHandler
                                             .sendPacket(
                                                new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0, clientPlayerEntity.getYaw(), clientPlayerEntity.getPitch())
                                             );
                                          mutableBoolean.setTrue();
                                       }
                                    }
                                 );
                           }
                           break;
                        }

                        if (!mutableBoolean.isFalse()) {
                           if (clientPlayerEntity.horizontalCollision || clientPlayerEntity.verticalCollision) {
                              acceleration = 0.0F;
                              yAcceleration = 0.0F;
                           }

                           if (clientPlayerEntity.getFallFlyingTicks() < 4) {
                              moveEvent2.setDoubleValue(0.2F);
                           } else {
                              if (mc.options.jumpKey.isPressed()) {
                                 moveEvent2.setDoubleValue(module.verticalSpeed.getValue() * Math.min((yAcceleration += 9.0F) / 100.0F, 1.0F));
                              } else if (mc.options.sneakKey.isPressed()) {
                                 moveEvent2.setDoubleValue(-module.verticalSpeed.getValue() * Math.min((yAcceleration += 9.0F) / 100.0F, 1.0F));
                              } else {
                                 moveEvent2.setDoubleValue(0.0);
                              }

                              if (!PlayerUtils.check5()) {
                                 acceleration = 0.0F;
                              }

                              if (clientPlayerEntity.input.movementSideways > 0.0F) {
                                 clientPlayerEntity.input.movementSideways = 1.0F;
                              } else if (clientPlayerEntity.input.movementSideways < 0.0F) {
                                 clientPlayerEntity.input.movementSideways = -1.0F;
                              }

                              PlayerUtils.handleMoveEvent2(
                                 moveEvent2, clientPlayerEntity, module.speed.getValue() * Math.min((acceleration += 9.0F) / 100.0F, 1.0F)
                              );
                           }
                        }
                     }
                  }
               }
            );
         }

         @Override
         public String getText() {
            return "Uses fireworks and controls movements";
         }
      },
      Infinite {
         public ElytraFlyModule module;
         public boolean flag;
         public float floatValue;
         public float floatValue2;

         @Override
         public String getText() {
            return "Rotates perfectly to fly without fireworks";
         }

         @Override
         public void init(PhobosClient phobos, ElytraFlyModule module) {
            this.module = module;
            this.handleListener(
               new SafeListener<PlayerTickEvent>() {
                  public void handleObject(
                     PlayerTickEvent playerTickEvent,
                     ClientPlayerEntity clientPlayerEntity,
                     ClientWorld clientWorld,
                     ClientPlayerInteractionManager clientPlayerInteractionManager
                  ) {
                     if (PlayerActionUtils.testClientPlayerEntity3(clientPlayerEntity)) {
                        ItemStack itemStack = clientPlayerEntity.getEquippedStack(EquipmentSlot.CHEST);
                        if (itemStack.isOf(Items.ELYTRA) && ElytraItem.isUsable(itemStack)) {
                           phobos.getRotationManager()
                              .testFloat(RotationUtils.computeClientPlayerEntity2(clientPlayerEntity), getInfinitePitch(clientPlayerEntity));
                        }
                     }
                  }
               }
            );
            this.handleListener(
               new SafeListener<TickEvent2>() {
                  public void handleObject(
                     TickEvent2 tickEvent2,
                     ClientPlayerEntity clientPlayerEntity,
                     ClientWorld clientWorld,
                     ClientPlayerInteractionManager clientPlayerInteractionManager
                  ) {
                     floatValue2 = (float)Math.hypot(clientPlayerEntity.getX() - clientPlayerEntity.prevX, clientPlayerEntity.getZ() - clientPlayerEntity.prevZ);
                  }
               }
            );
         }

         @Override
         public void run3() {
            this.flag = false;
         }

         public float getInfinitePitch(ClientPlayerEntity player) {
            if (player.getY() < this.module.top.getValue().intValue()) {
               if (this.floatValue2 * 72.0F < 25.0F && !this.flag) {
                  this.flag = true;
               }

               if (this.floatValue2 * 72.0F > 150.0F && this.flag) {
                  this.flag = false;
               }
            } else {
               this.flag = true;
            }

            if (this.flag) {
               this.floatValue += 3.0F;
            } else {
               this.floatValue -= 3.0F;
            }

            this.floatValue = MathUtils.computeFloat3(this.floatValue, -40.0F, 40.0F);
            return this.floatValue;
         }
      },
      Packet {
         public float acceleration;

         @Override
         public String getText() {
            return "ElytraFly with packets";
         }

         @Override
         public void run3() {
            this.acceleration = 0.0F;
         }

         @Override
         public void init(PhobosClient phobos, ElytraFlyModule module) {
            this.handleListener(
               new SafeListener<PlayerMoveEvent>() {
                  public void handleObject(
                     PlayerMoveEvent playerMoveEvent,
                     ClientPlayerEntity clientPlayerEntity,
                     ClientWorld clientWorld,
                     ClientPlayerInteractionManager clientPlayerInteractionManager
                  ) {
                     ItemStack itemStack = clientPlayerEntity.getEquippedStack(EquipmentSlot.CHEST);
                     if (itemStack.isOf(Items.ELYTRA) && ElytraItem.isUsable(itemStack)) {
                        if (module.infinite.getValue() && !PlayerActionUtils.testClientPlayerEntity3(clientPlayerEntity)) {
                           clientPlayerEntity.networkHandler
                              .sendPacket(
                                 new ClientCommandC2SPacket(
                                    clientPlayerEntity, net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode.START_FALL_FLYING
                                 )
                              );
                        }

                        if (clientPlayerEntity.age % 3 != 0 && module.strict.getValue().equals(ElytraFlyModule.Strict.Motion)) {
                           playerMoveEvent.setCancelled(true);
                        }
                     }
                  }
               }
            );
            this.handleListener(
               new SafeListener<MoveEvent2>() {
                  public void handleObject(
                     MoveEvent2 moveEvent2,
                     ClientPlayerEntity clientPlayerEntity,
                     ClientWorld clientWorld,
                     ClientPlayerInteractionManager clientPlayerInteractionManager
                  ) {
                     clientPlayerEntity.getAbilities().flying = false;
                     clientPlayerEntity.getAbilities().setFlySpeed(0.05F);
                     ItemStack itemStack = clientPlayerEntity.getEquippedStack(EquipmentSlot.CHEST);
                     if (itemStack.isOf(Items.ELYTRA) && ElytraItem.isUsable(itemStack)) {
                        clientPlayerEntity.getAbilities().flying = true;
                        clientPlayerEntity.getAbilities()
                           .setFlySpeed(
                              module.speed.getValue()
                                 / 15.0F
                                 * (
                                    module.acceleration.getValue()
                                       ? Math.min((acceleration = acceleration + module.accelerationFactor.getValue()) / 100.0F, 1.0F)
                                       : 1.0F
                                 )
                           );
                        if (clientPlayerEntity.age % 3 == 0 && module.strict.getValue().equals(ElytraFlyModule.Strict.Motion)) {
                           moveEvent2.setVec3d(Vec3d.ZERO);
                        } else {
                           if (Math.abs(moveEvent2.getDoubleValue2()) < 0.05) {
                              moveEvent2.setDoubleValue3(0.0);
                           }

                           if (Math.abs(moveEvent2.getDoubleValue()) < 0.05) {
                              moveEvent2.setDoubleValue2(0.0);
                           }

                           moveEvent2.setDoubleValue(
                              module.vertical.getValue()
                                 ? (
                                    mc.options.jumpKey.isPressed()
                                       ? module.verticalSpeed.getValue().floatValue()
                                       : (mc.options.sneakKey.isPressed() ? -module.verticalSpeed.getValue() : 0.0)
                                 )
                                 : 0.0
                           );
                           switch ((ElytraFlyModule.Strict)module.strict.getValue()) {
                              case UNCP:
                                 moveEvent2.setDoubleValue(2.0E-4 - (clientPlayerEntity.age % 2 == 0 ? 0.0 : 1.0E-6) + MathUtils.computeDouble9(0.0, 9.0E-7));
                                 break;
                              case Motion:
                                 moveEvent2.setDoubleValue(-1.0000889E-12F);
                                 break;
                              case None:
                                 moveEvent2.setDoubleValue(-4.0003556E-12F);
                           }

                           if (clientPlayerEntity.horizontalCollision
                              && (
                                 module.strict.getValue().equals(ElytraFlyModule.Strict.UNCP) || module.strict.getValue().equals(ElytraFlyModule.Strict.Motion)
                              )
                              && clientPlayerEntity.age % 2 == 0) {
                              moveEvent2.setDoubleValue(-0.07840000152587923);
                           }

                           if ((module.infinite.getValue() || module.strict.getValue().equals(ElytraFlyModule.Strict.Motion))
                              && !PlayerUtils.check5()
                              && Math.abs(moveEvent2.getDoubleValue2()) < 0.121) {
                              float f = (float)Math.toRadians(4.5 * (clientPlayerEntity.age % 80));
                              moveEvent2.setDoubleValue3(Math.sin(f) * 0.12);
                              moveEvent2.setDoubleValue2(Math.cos(f) * 0.12);
                           }
                        }
                     }
                  }
               }
            );
         }

         @Override
         public void run4() {
            if (MinecraftHolder.mc.player != null) {
               MinecraftHolder.mc.player.getAbilities().flying = false;
               MinecraftHolder.mc.player.getAbilities().setFlySpeed(0.05F);
            }
         }
      },
      Bounce {
         public double doubleValue;
         public final FlyConfiguration flyConfiguration = FlyConfiguration.createFactory().getFactory2().getFlyConfiguration2();

         public boolean boost(PhobosClient phobos, ElytraFlyModule module, ElytraFlyModule.ZeroTick mode) {
            double speed = phobos.getMovementSimulator().getDoubleValue();
            return speed > module.n0TickStart.getValue() ? module.n0TickMode.testObject2(mode) : false;
         }

         @Override
         public String getText() {
            return "Automatically bounces off the ground";
         }

         @Override
         public void init(PhobosClient phobos, ElytraFlyModule module) {
            this.handleListener(
               new SafeListener<PlayerTickEvent>() {
                  public void handleObject(
                     PlayerTickEvent playerTickEvent,
                     ClientPlayerEntity clientPlayerEntity,
                     ClientWorld clientWorld,
                     ClientPlayerInteractionManager clientPlayerInteractionManager
                  ) {
                     if (!PlayerActionUtils.testClientPlayerEntity3(clientPlayerEntity)
                        && clientPlayerEntity.fallDistance > 0.0F
                        && module.testClientPlayerEntity4(clientPlayerEntity)) {
                        module.testClientPlayerEntity2(clientPlayerEntity);
                     }
                  }
               }
            );
            this.handleListener(
               new SafeListener<PlayerTickEvent>() {
                  public void handleObject(
                     PlayerTickEvent playerTickEvent,
                     ClientPlayerEntity clientPlayerEntity,
                     ClientWorld clientWorld,
                     ClientPlayerInteractionManager clientPlayerInteractionManager
                  ) {
                     if (PlayerActionUtils.testClientPlayerEntity3(clientPlayerEntity)) {
                        if (module.n0TickAccelerate.getValue()) {
                           PlayerActionUtils.handleClientPlayerEntity29(clientPlayerEntity, 0.0);
                        }

                        if (module.rotate.getValue() && !RotationManager.isFlag()) {
                           phobos.getRotationManager()
                              .testFloat(
                                 FreeLookModule.getFreeLookModule().check7()
                                    ? FreeLookModule.getFreeLookModule().getRotation().getFloatValue2()
                                    : clientPlayerEntity.getYaw(),
                                 75.0F
                              );
                        }
                     }

                     if (module.stand.getValue()) {
                        if (phobos.getElytraManager().isFlag()) {
                           phobos.getElytraManager().getFlyConfiguration().setFlag5(true);
                        } else {
                           phobos.getElytraManager().testFlyConfiguration2(flyConfiguration);
                        }
                     }
                  }
               }
            );
            this.handleListener(
               new SafeListener<TickEvent>() {
                  public void handleObject(
                     TickEvent tickEvent,
                     ClientPlayerEntity clientPlayerEntity,
                     ClientWorld clientWorld,
                     ClientPlayerInteractionManager clientPlayerInteractionManager
                  ) {
                     if (PlayerActionUtils.testClientPlayerEntity3(clientPlayerEntity)) {
                        tickEvent.getKeyboardInput().jumping = true;
                        tickEvent.getKeyboardInput().pressingForward = true;
                        tickEvent.getKeyboardInput().pressingLeft = false;
                        tickEvent.getKeyboardInput().pressingRight = false;
                        tickEvent.getKeyboardInput().pressingBack = false;
                        tickEvent.getKeyboardInput().movementForward = 1.0F;
                        tickEvent.getKeyboardInput().movementSideways = 0.0F;
                     }
                  }
               }
            );
            this.handleListener(
               new SafeListener<JumpEvent>(10000) {
                  public void handleObject(
                     JumpEvent jumpEvent,
                     ClientPlayerEntity clientPlayerEntity,
                     ClientWorld clientWorld,
                     ClientPlayerInteractionManager clientPlayerInteractionManager
                  ) {
                     if (module.n0Tick.getValue()) {
                        if (PlayerActionUtils.testClientPlayerEntity3(clientPlayerEntity)) {
                           if (boost(phobos, module, ElytraFlyModule.ZeroTick.Jump)) {
                              jumpEvent.setFloatValue(0.06F);
                           }
                        }
                     }
                  }
               }
            );
            this.handleListener(
               new SafeListener<MoveEvent2>(1000) {
                  public void handleObject(
                     MoveEvent2 moveEvent2,
                     ClientPlayerEntity clientPlayerEntity,
                     ClientWorld clientWorld,
                     ClientPlayerInteractionManager clientPlayerInteractionManager
                  ) {
                     if (clientPlayerEntity.isOnGround()) {
                        if (clientPlayerEntity.isSprinting() && boost(phobos, module, ElytraFlyModule.ZeroTick.Modifier)) {
                           Vec3d vec3d = clientPlayerEntity.getVelocity();
                           moveEvent2.setDoubleValue3(vec3d.getX());
                           moveEvent2.setDoubleValue(0.0);
                           moveEvent2.setDoubleValue2(vec3d.getZ());
                        }
                     }
                  }
               }
            );
         }
      },
      Macro {
         @Override
         public void init(PhobosClient phobos, ElytraFlyModule module) {
         }

         @Override
         public void run3() {
            if (MinecraftHolder.mc.player != null && MinecraftHolder.mc.interactionManager != null) {
               ClientHolder.getPhobosClient()
                  .getInventoryManager()
                  .handleConsumer2(
                     context -> context.handleRunnable3(
                        () -> {
                           Slot elytra = context.resolveObject(
                              slot -> slot.getStack().getItem() instanceof ElytraItem && ElytraItem.isUsable(slot.getStack()) ? slot : null
                           );
                           Slot fireworks = context.resolveObject(slot -> slot.getStack().getItem() instanceof FireworkRocketItem ? slot : null);
                           if (elytra != null && fireworks != null) {
                              if (this.checkFallFlying(MinecraftHolder.mc.player)) {
                                 SilentHotbarModule.getSilentHotbarManager().handleObject3(ElytraFlyModule.elytraFlyModule, elytra, 1);
                                 MinecraftHolder.mc.interactionManager.interactItem(MinecraftHolder.mc.player, Hand.MAIN_HAND);
                                 MinecraftHolder.mc
                                    .player
                                    .networkHandler
                                    .sendPacket(
                                       new ClientCommandC2SPacket(
                                          MinecraftHolder.mc.player, net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode.START_FALL_FLYING
                                       )
                                    );
                                 MinecraftHolder.mc.player.startFallFlying();
                                 ClientHolder.getPhobosClient().getScheduler().handleRunnable2(() -> {
                                    if (MinecraftHolder.mc.player != null && MinecraftHolder.mc.interactionManager != null) {
                                       SilentHotbarModule.getSilentHotbarManager().handleObject3(ElytraFlyModule.elytraFlyModule, fireworks, 1);
                                       MinecraftHolder.mc.interactionManager.interactItem(MinecraftHolder.mc.player, Hand.MAIN_HAND);
                                    }
                                 }, 1);
                              }
                           }
                        }
                     )
                  );
            }
         }

         public boolean checkFallFlying(ClientPlayerEntity player) {
            return !player.isOnGround() && !player.isFallFlying() && !player.isTouchingWater() && !player.hasStatusEffect(StatusEffects.LEVITATION);
         }

         @Override
         public String getText() {
            return "Equips elytra, uses a firework and takes the elytra off";
         }

         @Override
         public void run4() {
            if (MinecraftHolder.mc.player != null && MinecraftHolder.mc.interactionManager != null) {
               if (MinecraftHolder.mc.player.getInventory().getArmorStack(2).getItem() instanceof ElytraItem) {
                  ClientHolder.getPhobosClient()
                     .getInventoryManager()
                     .handleConsumer2(
                        context -> context.handleRunnable3(
                           () -> {
                              Slot chestplate = context.resolveObject(
                                 slot -> slot.getStack().getItem() instanceof ArmorItem armor && armor.getSlotType().equals(EquipmentSlot.CHEST) ? slot : null
                              );
                              if (chestplate != null) {
                                 SilentHotbarModule.getSilentHotbarManager().handleObject3(ElytraFlyModule.elytraFlyModule, chestplate, 1);
                                 MinecraftHolder.mc.interactionManager.interactItem(MinecraftHolder.mc.player, Hand.MAIN_HAND);
                              }
                           }
                        )
                     );
               }
            }
         }
      },
      Firework {
         public static final double FACTOR = 0.1;
         public static final double BOOST = 1.5;
         public final TimedFloatAnimator smoothRotation;
         public final Normal cooldown = new Normal();
         public boolean disableGravity;

         {
            this.smoothRotation = new TimedFloatAnimator();
            this.disableGravity = false;
         }

         @Override
         public void init(PhobosClient phobos, ElytraFlyModule module) {
            this.handleListener(
               new SafeListener<RemoveEntityEvent>() {
                  public void handleObject(
                     RemoveEntityEvent removeEntityEvent,
                     ClientPlayerEntity clientPlayerEntity,
                     ClientWorld clientWorld,
                     ClientPlayerInteractionManager clientPlayerInteractionManager
                  ) {
                     if (module.use.getValue() && !removeEntityEvent.isCancelled()) {
                        if (removeEntityEvent.getEntity() instanceof IFireworkRocketEntity iFireworkRocketEntity) {
                           if (clientPlayerEntity.equals(iFireworkRocketEntity.getShooter())) {
                              useFirework(phobos, module, clientPlayerEntity, clientPlayerInteractionManager);
                           }
                        }
                     }
                  }
               }
            );
            this.handleListener(
               new SafeListener<PostSend<ClientCommandC2SPacket>>(Integer.MIN_VALUE) {
                  public void handleObject(
                     PostSend<ClientCommandC2SPacket> postSend,
                     ClientPlayerEntity clientPlayerEntity,
                     ClientWorld clientWorld,
                     ClientPlayerInteractionManager clientPlayerInteractionManager
                  ) {
                     if (((ClientCommandC2SPacket)postSend.getPacket()).getMode()
                           == net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode.START_FALL_FLYING
                        && module.use.getValue()
                        && cooldown.testLong2(500L)) {
                        useFirework(phobos, module, clientPlayerEntity, clientPlayerInteractionManager);
                     }
                  }
               }
            );
            this.handleListener(
               new SafeListener<PlayerTickEvent>(Integer.MIN_VALUE) {
                  public void handleObject(
                     PlayerTickEvent playerTickEvent,
                     ClientPlayerEntity clientPlayerEntity,
                     ClientWorld clientWorld,
                     ClientPlayerInteractionManager clientPlayerInteractionManager
                  ) {
                     if (PlayerActionUtils.testClientPlayerEntity3(clientPlayerEntity)) {
                        if (canRun(module, clientPlayerEntity, clientWorld)) {
                           if (!module.staticValue.getValue() || module.exploit.getValue() || (module.look.getValue() ? PlayerUtils.check4() : PlayerUtils.check())) {
                              Vec3d vec3d;
                              if (module.exploit.getValue()) {
                                 if (RotationManager.isFlag()) {
                                    return;
                                 }

                                 float f = FreeLookModule.computeClientPlayerEntity2(clientPlayerEntity);
                                 if (PlayerActionUtils.testClientPlayerEntity6(clientPlayerEntity, clientWorld)) {
                                    smoothRotation.handleFloat(f);
                                 }

                                 smoothRotation.setFloatValue(f);
                                 float f2 = smoothRotation.getFloatValue2();
                                 float f3 = -module.exploitPitch.getValue();
                                 phobos.getRotationManager().testFloat(f2, f3);
                                 vec3d = MathUtils.createVec3d4(f2, 0.0F);
                              } else if (module.look.getValue() && !RotationManager.isFlag()) {
                                 float f4 = PlayerUtils.computeClientPlayerEntity6(clientPlayerEntity);
                                 float f5 = PlayerUtils.computeFlag2(true);
                                 vec3d = MathUtils.createVec3d4(f4, f5);
                                 phobos.getRotationManager().testFloat(f4, f5);
                              } else {
                                 vec3d = RotationManager.isFlag()
                                    ? phobos.getRotationManager().getRotation3().getVec3d()
                                    : clientPlayerEntity.getRotationVector();
                              }

                              double d = module.multiplier.getValue() / 100.0;
                              double d2 = 1.5 * d;
                              Vec3d vec3d4 = clientPlayerEntity.getVelocity();
                              clientPlayerEntity.setVelocity(
                                 vec3d4.add(
                                    vec3d.x * 0.1 + (vec3d.x * d2 - vec3d4.x) * 0.5,
                                    vec3d.y * 0.1 + (vec3d.y * d2 - vec3d4.y) * 0.5,
                                    vec3d.z * 0.1 + (vec3d.z * d2 - vec3d4.z) * 0.5
                                 )
                              );
                              disableGravity = !mc.options.jumpKey.isPressed() && !mc.options.sneakKey.isPressed();
                           } else {
                              clientPlayerEntity.setVelocity(Vec3d.ZERO);
                           }
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
                     smoothRotation.handleFloat3(10.0F, 0.02F);
                  }
               }
            );
            this.handleListener(
               new SafeListener<MoveEvent2>() {
                  public void handleObject(
                     MoveEvent2 moveEvent2,
                     ClientPlayerEntity clientPlayerEntity,
                     ClientWorld clientWorld,
                     ClientPlayerInteractionManager clientPlayerInteractionManager
                  ) {
                     if (!moveEvent2.isCancelled() && !moveEvent2.isFlag()) {
                        if (disableGravity) {
                           moveEvent2.setDoubleValue(0.0);
                        }

                        disableGravity = false;
                        if (PlayerActionUtils.testClientPlayerEntity3(clientPlayerEntity) && module.staticValue.getValue()) {
                           if (module.look.getValue() ? !PlayerUtils.check4() : !PlayerUtils.check()) {
                              moveEvent2.setVec3d(Vec3d.ZERO);
                              moveEvent2.setFlag(true);
                           }
                        }
                     } else {
                        disableGravity = false;
                     }
                  }
               }
            );
            this.handleListener(
               new SafeListener<Send<PlayerInputC2SPacket>>() {
                  public void handleObject(
                     Send<PlayerInputC2SPacket> send,
                     ClientPlayerEntity clientPlayerEntity,
                     ClientWorld clientWorld,
                     ClientPlayerInteractionManager clientPlayerInteractionManager
                  ) {
                     if (PlayerActionUtils.testClientPlayerEntity3(clientPlayerEntity) && module.exploit.getValue()) {
                        if (module.exploitCancel.getValue()) {
                           send.setCancelled(true);
                        } else {
                           IPlayerInputC2SPacket iPlayerInputC2SPacket = (IPlayerInputC2SPacket)(Object)send.getPacket();
                           iPlayerInputC2SPacket.setJumping(false);
                           iPlayerInputC2SPacket.setSneaking(false);
                           iPlayerInputC2SPacket.setForward(0.0F);
                           iPlayerInputC2SPacket.setSideways(0.0F);
                        }
                     }
                  }
               }
            );
         }

         public boolean fireworkCollision(ClientPlayerEntity player, ClientWorld world) {
            for (Entity entity : world.getOtherEntities(null, BlockPosUtils.createBox(player, 10.0))) {
               if (entity instanceof IFireworkRocketEntity rocket) {
                  LivingEntity shooter = rocket.getShooter();
                  if (player.equals(shooter)) {
                     return true;
                  }
               }
            }

            return false;
         }

         public boolean canRun(ElytraFlyModule module, ClientPlayerEntity player, ClientWorld world) {
            return !module.use.getValue() ? true : this.fireworkCollision(player, world);
         }

         @Override
         public String getText() {
            return "Propels you forward as if you had a firework attached to you";
         }

         public void useFirework(PhobosClient phobos, ElytraFlyModule module, ClientPlayerEntity player, ClientPlayerInteractionManager interactionManager) {
            phobos.getInventoryManager().handleConsumer2(context -> {
               Slot slot = context.resolveObject(s -> s.getStack().isOf(Items.FIREWORK_ROCKET) ? s : null);
               if (slot != null) {
                  SwitchResult result = context.resolveSwitchResult(Hand.MAIN_HAND, slot, module.useSilent.getValue().getFlag() | 1);
                  if (result != null) {
                     interactionManager.interactItem(player, Hand.MAIN_HAND);
                  }
               }
            });
         }
      };

      public final List<Listener<?>> listenerList = new ArrayList<>();

      public void run4() {
      }

      public void run3() {
      }

      @Override
      public List<Listener<?>> getListenerList() {
         return this.listenerList;
      }

      public abstract void init(PhobosClient var1, ElytraFlyModule var2);
   }

   private enum Strict {
      None,
      NCP,
      UNCP,
      Motion;
   }

   private enum ZeroTick {
      Jump,
      Modifier;
   }
}
