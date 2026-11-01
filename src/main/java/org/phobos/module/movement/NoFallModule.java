package org.phobos.module.movement;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.OnGroundOnly;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.phobos.api.TimeGate;
import org.phobos.api.AreaScanner.Area;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.event.events.PlayerMoveEvent;
import org.phobos.core.render.RenderContext;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.entity.EntityInterpolation;
import org.phobos.core.module.Categories;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.tick.TickUtils;
import org.phobos.core.event.events.PacketEvent.Send;
import org.phobos.core.inventory.ItemSwitcher.SwitchResult;
import org.phobos.core.tick.TimerManager.TimerPriority;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.mixin.net.interfaces.IPlayerMoveC2SPacket;
import org.phobos.module.Module;
import org.phobos.module.player.SilentHotbarModule;
import org.phobos.module.render.FreeLookModule;

public class NoFallModule extends Module {
   public BlockPos blockPos;
   public boolean flag;
   public final Setting<NoFallModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", NoFallModule.Mode.Vanilla));
   public Vec3d vec3d;
   public final Setting<NoFallModule.Ground> ground = this.resolveSetting2(new EnumSetting<>("Ground", NoFallModule.Ground.Enabled));
   public final Setting<Boolean> grim = this.resolveSetting2(new BooleanSetting("Grim").resolveSetting4(this.mode, NoFallModule.Mode.Pearl));
   public BlockPos blockPos2;
   public int intValue;
   public int intValue2;
   public final TimeGate.Normal normal = new TimeGate.Normal();
   public float[] floatArray;
   public NoFallModule.Stage stage = NoFallModule.Stage.None;

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   public NoFallModule(PhobosClient phobosClient) {
      super(phobosClient, "NoFall", Categories.movement, "Prevents fall damage");
      this.handleListener(
         new SafeListener<PlayerMoveEvent>() {
            public void handleObject(
               PlayerMoveEvent playerMoveEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               switch ((NoFallModule.Mode)NoFallModule.this.mode.getValue()) {
                  case Pearl:
                     if (PlayerActionUtils.testClientPlayerEntity3(clientPlayerEntity)) {
                        return;
                     }

                     if (clientPlayerEntity.getVelocity().getY() > -0.5) {
                        return;
                     }

                     playerMoveEvent.setFlag(true);
                     break;
                  case MLG:
                     double d = clientPlayerEntity.fallDistance - clientPlayerEntity.getVelocity().getY();
                     if (d >= 3.3F) {
                        phobosClient.getTimerManager().handleFloat(0.5F, TimerPriority.Max);
                        clientPlayerEntity.networkHandler.sendPacket(new OnGroundOnly(true));
                        clientPlayerEntity.fallDistance = 0.0F;
                        phobosClient.getScheduler().handleRunnable2(() -> phobosClient.getTimerManager().run2(), 1);
                     }
                     break;
                  case Grim:
                     if (NoFallModule.this.testClientPlayerEntity(clientPlayerEntity)) {
                        playerMoveEvent.setDoubleValue(clientPlayerEntity.getY() + 0.1F);
                     }
                     break;
                  case Anti:
                     if (NoFallModule.this.testClientPlayerEntity(clientPlayerEntity)) {
                        clientPlayerEntity.setOnGround(true);
                        clientPlayerEntity.getAbilities().flying = true;
                        clientPlayerEntity.getAbilities().allowFlying = true;
                        playerMoveEvent.setFlag(true);
                        clientPlayerEntity.velocityModified = true;
                        clientPlayerEntity.getAbilities().flying = false;
                        clientPlayerEntity.jump();
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
               if (NoFallModule.this.mode.getValue().equals(NoFallModule.Mode.Grim) && clientPlayerEntity.fallDistance > 3.0F) {
                  clientPlayerEntity.networkHandler
                     .sendPacket(
                        new Full(
                           clientPlayerEntity.getX(),
                           clientPlayerEntity.getY() + 1.0E-9,
                           clientPlayerEntity.getZ(),
                           clientPlayerEntity.getYaw(),
                           clientPlayerEntity.getPitch(),
                           false
                        )
                     );
                  clientPlayerEntity.onLanding();
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
               if (NoFallModule.this.mode.getValue().equals(NoFallModule.Mode.Pearl)) {
                  if (!(clientPlayerEntity.fallDistance < 3.0F)) {
                     Vec3d vec3d = EntityInterpolation.createVec3d2(clientPlayerEntity, renderContext.getFloatValue());
                     BlockHitResult blockHitResult = clientWorld.raycast(
                        new RaycastContext(vec3d, vec3d.add(0.0, -6.0, 0.0), ShapeType.COLLIDER, FluidHandling.NONE, clientPlayerEntity)
                     );
                     if (blockHitResult != null) {
                        Vec3d vec3d2 = blockHitResult.getPos();
                        if (!(vec3d.distanceTo(vec3d2) >= 1.5)) {
                           phobosClient.getInventoryManager()
                              .handleConsumer2(
                                 itemSwitcher -> {
                                    SwitchResult switchResult = itemSwitcher.resolveSwitchResult3(Hand.MAIN_HAND, Items.ENDER_PEARL, 1);
                                    if (switchResult != null) {
                                       TickUtils.handleClientWorld(
                                          clientWorld,
                                          integer -> {
                                             float f = FreeLookModule.getFreeLookModule().check7()
                                                ? FreeLookModule.getFreeLookModule().getRotation().getFloatValue2()
                                                : clientPlayerEntity.getYaw();
                                             float f2 = 90.0F;
                                             phobosClient.getRotationManager().handleClientPlayerEntity3(clientPlayerEntity, f, f2);
                                             clientPlayerEntity.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(switchResult.hand(), integer, f, f2));
                                             if (NoFallModule.this.grim.getValue()) {
                                                phobosClient.getRotationManager().handleClientPlayerEntity5(clientPlayerEntity);
                                             }
                                          }
                                       );
                                    }
                                 }
                              );
                        }
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<PlayerTickEvent>(26) {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (NoFallModule.this.mode.testObject2(NoFallModule.Mode.MLG)) {
                  if (!RotationManager.isFlag()) {
                     if (clientPlayerEntity.isOnGround() && NoFallModule.this.stage != NoFallModule.Stage.Collect) {
                        NoFallModule.this.stage = NoFallModule.Stage.None;
                     }

                     phobosClient.getInventoryManager()
                        .handleConsumer2(
                           itemSwitcher -> {
                              switch (NoFallModule.this.stage) {
                                 case Collect:
                                    if (itemSwitcher.testItem2(Items.WATER_BUCKET)) {
                                       NoFallModule.this.handleClientPlayerEntity(
                                          clientPlayerEntity,
                                          clientWorld,
                                          () -> {
                                             if (!(clientPlayerEntity.getPos().distanceTo(NoFallModule.this.vec3d) < 3.0)
                                                && !(clientPlayerEntity.fallDistance <= 1.0F)) {
                                                phobosClient.getRotationManager()
                                                   .testFloat(NoFallModule.this.floatArray[0], NoFallModule.this.floatArray[1]);
                                                NoFallModule.this.stage = NoFallModule.Stage.Aim;
                                             }
                                          }
                                       );
                                    }
                                    break;
                                 case None:
                                    NoFallModule.this.handleClientPlayerEntity(clientPlayerEntity, clientWorld, () -> {
                                       phobosClient.getRotationManager().testFloat(NoFallModule.this.floatArray[0], NoFallModule.this.floatArray[1]);
                                       Slot slotx = itemSwitcher.resolveObject(slotxx -> slotxx.getStack().isOf(Items.WATER_BUCKET) ? slotxx : null);
                                       if (slotx == null) {
                                          NoFallModule.this.stage = NoFallModule.Stage.None;
                                       } else {
                                          SilentHotbarModule.getSilentHotbarManager().handleObject3(NoFallModule.this, slotx, 10);
                                          NoFallModule.this.stage = NoFallModule.Stage.Place;
                                          NoFallModule.this.intValue2 = 0;
                                       }
                                    });
                                    break;
                                 case Final:
                                    NoFallModule.this.handleClientPlayerEntity(clientPlayerEntity, clientWorld, () -> {
                                       SilentHotbarModule.getSilentHotbarManager().handleObject(NoFallModule.this, 4);
                                       phobosClient.getRotationManager().testFloat(NoFallModule.this.floatArray[0], NoFallModule.this.floatArray[1]);
                                       if (clientPlayerEntity.isOnGround()) {
                                          NoFallModule.this.stage = NoFallModule.Stage.Collect;
                                       } else if (NoFallModule.this.intValue2++ >= 1) {
                                          if (!(clientPlayerEntity.getPos().distanceTo(NoFallModule.this.vec3d) > 3.0)) {
                                             clientPlayerInteractionManager.interactItem(clientPlayerEntity, Hand.MAIN_HAND);
                                             NoFallModule.this.stage = NoFallModule.Stage.Collect;
                                             NoFallModule.this.intValue2 = 0;
                                          }
                                       }
                                    });
                                    break;
                                 case Place:
                                    SilentHotbarModule.getSilentHotbarManager().handleObject(NoFallModule.this, 4);
                                    if (!clientPlayerEntity.isOnGround()) {
                                       NoFallModule.this.intValue2 = 0;
                                    } else {
                                       Area area = new Area();
                                       area.handleVec3d(
                                          clientPlayerEntity.getEyePos(),
                                          3.0,
                                          blockPos -> clientWorld.getBlockState(blockPos).getFluidState().isOf(Fluids.WATER)
                                       );
                                       NoFallModule.this.blockPos = area.resolveBlockPos(true);
                                       if (NoFallModule.this.blockPos == null) {
                                          NoFallModule.this.stage = NoFallModule.Stage.None;
                                       } else {
                                          NoFallModule.this.floatArray = RotationUtils.values2(clientPlayerEntity, NoFallModule.this.blockPos.toCenterPos());
                                          phobosClient.getRotationManager().testFloat(NoFallModule.this.floatArray[0], NoFallModule.this.floatArray[1]);
                                          if (NoFallModule.this.intValue2++ >= 3) {
                                             clientPlayerInteractionManager.interactItem(clientPlayerEntity, Hand.MAIN_HAND);
                                             SilentHotbarModule.getSilentHotbarManager().handleObject2(NoFallModule.this);
                                             NoFallModule.this.stage = NoFallModule.Stage.None;
                                             NoFallModule.this.normal.run();
                                             NoFallModule.this.intValue = 0;
                                          }
                                       }
                                    }
                                    break;
                                 case Aim:
                                    if (NoFallModule.this.blockPos == null) {
                                       NoFallModule.this.stage = NoFallModule.Stage.None;
                                    } else if (!clientPlayerEntity.getMainHandStack().isOf(Items.WATER_BUCKET)) {
                                       if (NoFallModule.this.normal.testLong(500L)) {
                                          if (NoFallModule.this.intValue > 3) {
                                             NoFallModule.this.stage = NoFallModule.Stage.None;
                                          } else {
                                             NoFallModule.this.floatArray = RotationUtils.values2(clientPlayerEntity, NoFallModule.this.blockPos.toCenterPos());
                                             phobosClient.getRotationManager().testFloat(NoFallModule.this.floatArray[0], NoFallModule.this.floatArray[1]);
                                             Slot slot = itemSwitcher.resolveObject(slotx -> slotx.getStack().isOf(Items.BUCKET) ? slotx : null);
                                             if (slot == null) {
                                                NoFallModule.this.stage = NoFallModule.Stage.None;
                                             } else {
                                                SilentHotbarModule.getSilentHotbarManager().handleObject3(NoFallModule.this, slot, 4);
                                                clientPlayerInteractionManager.interactItem(clientPlayerEntity, Hand.MAIN_HAND);
                                                NoFallModule.this.normal.run();
                                                NoFallModule.this.intValue++;
                                             }
                                          }
                                       }
                                    } else {
                                       SilentHotbarModule.getSilentHotbarManager().handleObject2(NoFallModule.this);
                                       NoFallModule.this.stage = NoFallModule.Stage.None;
                                       NoFallModule.this.blockPos = null;
                                    }
                              }
                           }
                        );
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Send<?>>() {
            public void handleObject(
               Send<?> send, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (send.getPacket() instanceof PlayerMoveC2SPacket playerMoveC2SPacket) {
                  if (!PlayerActionUtils.testClientPlayerEntity3(clientPlayerEntity)) {
                     if (!(clientPlayerEntity.getVelocity().getY() > -0.5)) {
                        boolean flag = NoFallModule.this.ground.testObject2(NoFallModule.Ground.Enabled);
                        if (playerMoveC2SPacket.isOnGround() != flag) {
                           ((IPlayerMoveC2SPacket)(Object)playerMoveC2SPacket).setOnGround(flag);
                        }
                     }
                  }
               }
            }
         }
      );
   }

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      return PlayerActionUtils.testClientPlayerEntity3(clientPlayerEntity) ? false : clientPlayerEntity.fallDistance >= 3.0F;
   }

   @Override
   public void run4() {
      this.stage = NoFallModule.Stage.None;
      if (this.flag) {
         this.phobosClient.getTimerManager().run2();
      }

      this.flag = false;
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Runnable runnable) {
      BlockHitResult blockHitResult = clientWorld.raycast(
         new RaycastContext(
            clientPlayerEntity.getPos(), clientPlayerEntity.getPos().add(0.0, -10.0, 0.0), ShapeType.COLLIDER, FluidHandling.NONE, clientPlayerEntity
         )
      );
      if (blockHitResult != null) {
         this.vec3d = blockHitResult.getPos();
         if (!(clientPlayerEntity.getPos().distanceTo(this.vec3d) >= 6.0)) {
            this.blockPos2 = blockHitResult.getBlockPos();
            this.floatArray = RotationUtils.values4(clientPlayerEntity, clientWorld, this.blockPos2, Direction.UP);
            runnable.run();
         }
      }
   }

   private enum Ground {
      Enabled,
      Disabled;
   }

   private enum Mode {
      Vanilla,
      Watchdog,
      Anti,
      AAC,
      Grim,
      Pearl,
      MLG;
   }

   private enum Stage {
      None,
      Aim,
      Place,
      Collect,
      Final;
   }
}
