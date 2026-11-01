package org.phobos.core.rotation;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.earthhack.ducks.IPlayerInteractEntityC2SPacket;
import org.earthhack.ducks.IPlayerMoveC2SPacket;
import org.earthhack.mixin.EventSubscriber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.TimeGate;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.event.events.PacketEvent.PostSend;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.core.event.events.PacketEvent.Send;
import org.phobos.core.event.events.TravelEvent.Post;
import org.phobos.core.event.events.TravelEvent.Pre;
import org.phobos.event.AttackEvent;
import org.phobos.event.FindCrosshairTargetEvent;
import org.phobos.event.JumpEvent2;
import org.phobos.event.Listener;
import org.phobos.event.MoveEvent2;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent;
import org.phobos.event.UpdateVelocityEvent;
import org.phobos.mixin.net.interfaces.IPlayerInteractItemC2SPacket;
import org.phobos.module.client.AntiCheatModule;
import org.phobos.module.player.BlockTweaksModule;
import org.phobos.module.render.FreecamModule;
import org.phobos.module.client.NotificationsModule;
import org.phobos.module.client.RotationsModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.ClientHolder;
import org.phobos.core.Feature;
import org.phobos.core.math.MathUtils;
import org.phobos.core.event.events.PlayerAnimationEvent;
import org.phobos.core.event.events.PlayerMoveEvent;
import org.phobos.core.world.RaycastUtils;
import org.phobos.core.render.RenderContext;
import org.phobos.core.event.events.RotationEvent;
import org.phobos.core.animation.TimedFloatAnimator;
import org.phobos.core.tick.TimerManager;
import org.phobos.core.event.events.UnusedEvent;

public class RotationManager extends Feature implements EventSubscriber {
   public float floatValue;
   public boolean flag;
   public boolean flag2;
   public float floatValue2;
   public float floatValue3;
   public float floatValue4;
   public boolean flag3;
   public float floatValue5;
   public boolean flag4;
   public static final Logger LOGGER = LoggerFactory.getLogger(RotationManager.class);
   public float floatValue6;
   public float floatValue7;
   public float floatValue8;
   public float floatValue9;
   public float floatValue10;
   public float floatValue11;
   public float floatValue12;
   public float floatValue13;
   public boolean flag5;
   public final TimedFloatAnimator timedFloatAnimator;
   public float floatValue14;
   public final Queue<Runnable> runnableQueue;
   public boolean flag6;
   public final RotationManager.Rotation rotation = RotationManager.Rotation.resolveRotation(RotationManager.Rotation.INSTANCE);
   public final RotationState rotationState;
   public final RotationManager.RotationAnimation rotationAnimation;
   public final TimeGate.Normal normal = new TimeGate.Normal();
   public final RotationSmoother rotationSmoother;
   public float floatValue15;
   public final TimeGate.Normal normal2;
   public float floatValue16;
   public float floatValue17;
   public float floatValue18;

   public double computeFloat(float f, double d, double d2) {
      if (d < 0.0) {
         f += 180.0F;
      }

      float f2 = 1.0F;
      if (d < 0.0) {
         f2 = -0.5F;
      } else if (d > 0.0) {
         f2 = 0.5F;
      }

      if (d2 > 0.0) {
         f -= 90.0F * f2;
      }

      if (d2 < 0.0) {
         f += 90.0F * f2;
      }

      return Math.toRadians(f);
   }

   public float computeClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, float f) {
      double d = clientPlayerEntity.getX() - clientPlayerEntity.prevX;
      double d2 = clientPlayerEntity.getZ() - clientPlayerEntity.prevZ;
      float f2 = this.timedFloatAnimator.getFloatValue();
      if (d * d + d2 * d2 > 0.0025000002F) {
         f2 = (float)(MathHelper.atan2(d2, d) * 180.0F / (float)Math.PI - 90.0);
      }

      if (clientPlayerEntity.handSwingProgress > 0.0F) {
         f2 = f;
      }

      float f3 = MathUtils.computeFloat3(
         MathHelper.wrapDegrees(f - (this.timedFloatAnimator.getFloatValue() + MathHelper.wrapDegrees(f2 - this.timedFloatAnimator.getFloatValue()) * 0.3F)),
         -45.0F,
         75.0F
      );
      return (f3 > 50.0F ? f3 * 0.2F : 0.0F) + f - f3;
   }

   public RotationManager() {
      this.rotationAnimation = new RotationManager.RotationAnimation();
      this.rotationSmoother = new RotationSmoother();
      this.timedFloatAnimator = new TimedFloatAnimator();
      this.normal2 = new TimeGate.Normal();
      this.runnableQueue = new ConcurrentLinkedQueue<>();
      this.rotationState = new RotationState();
      this.handleListener(new Listener<MoveEvent2>() {
         public void handleObject2(MoveEvent2 moveEvent2) {
            NotificationsModule.getNotificationsModule().run();
         }
      });
      this.handleListener(
         new Listener<RenderContext>() {
            public void handleObject2(RenderContext renderContext) {
               if (MinecraftHolder.mc.player != null) {
                  RotationManager.this.rotationSmoother
                     .handleFloat5(
                        MathUtils.computeFloat8(10.0F, 15.0F) * RotationManager.this.floatValue16,
                        MathUtils.computeFloat8(0.01F, 0.1F) * RotationManager.this.floatValue16
                     );
                  RotationManager.this.rotationAnimation.run();
                  RotationManager.this.floatValue18 = RotationManager.this.timedFloatAnimator.getFloatValue2();
                  RotationManager.this.timedFloatAnimator
                     .setFloatValue(
                        RotationManager.this.computeClientPlayerEntity(
                           MinecraftHolder.mc.player, RotationManager.this.rotationAnimation.getTimedFloatAnimator2().getFloatValue2()
                        )
                     );
                  if (RotationsModule.check5()) {
                     RotationManager.this.timedFloatAnimator
                        .setFloatValue2(
                           MathHelper.lerpAngleDegrees(
                              renderContext.getFloatValue(), RotationManager.this.floatValue18, RotationManager.this.timedFloatAnimator.getFloatValue()
                           )
                        );
                  } else {
                     RotationManager.this.timedFloatAnimator.handleFloat3(50.0F, 0.1F);
                  }
               }
            }
         }
      );
      this.handleListener(new Listener<PlayerAnimationEvent>() {
         public void handleObject2(PlayerAnimationEvent playerAnimationEvent) {
            if (MinecraftHolder.mc.player != null) {
               if (!RotationsModule.check()) {
                  if (RotationsModule.check5()) {
                     playerAnimationEvent.setFloatValue(RotationManager.this.rotationState.getFloatValue6());
                     playerAnimationEvent.setFloatValue32(RotationManager.this.rotationState.getFloatValue3());
                     playerAnimationEvent.setFloatValue2(RotationManager.this.rotationState.getFloatValue2());
                     playerAnimationEvent.setFloatValue6(RotationManager.this.rotationState.getFloatValue4());
                     playerAnimationEvent.setFloatValue3(RotationManager.this.rotationState.getFloatValue5());
                     playerAnimationEvent.setFloatValue4(RotationManager.this.rotationState.getFloatValue());
                     playerAnimationEvent.setCancelled(true);
                  } else {
                     float f = RotationManager.this.rotationAnimation.getTimedFloatAnimator2().getFloatValue2();
                     float f2 = RotationManager.this.rotationAnimation.getTimedFloatAnimator().getFloatValue2();
                     playerAnimationEvent.setFloatValue(f);
                     playerAnimationEvent.setFloatValue32(f);
                     playerAnimationEvent.setFloatValue2(f2);
                     playerAnimationEvent.setFloatValue6(f2);
                     playerAnimationEvent.setFloatValue3(RotationManager.this.timedFloatAnimator.getFloatValue2());
                     playerAnimationEvent.setFloatValue4(RotationManager.this.timedFloatAnimator.getFloatValue2());
                     playerAnimationEvent.setCancelled(true);
                  }
               }
            }
         }
      });
      this.handleListener(
         new SafeListener<PlayerTickEvent>(Integer.MAX_VALUE) {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               RotationManager.this.flag = true;
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
               if (RotationManager.this.flag5 && !RotationManager.this.flag2) {
                  if (RotationManager.this.rotationSmoother.testFloat(20.0F)) {
                     RotationManager.this.flag5 = false;
                  } else {
                     RotationManager.this.testRotation(RotationManager.this.rotationSmoother.createRotation());
                  }
               }

               RotationManager.this.flag = false;
               RotationManager.this.floatValue15 = RotationManager.this.flag2 ? RotationManager.this.floatValue8 : clientPlayerEntity.getYaw();
               RotationManager.this.floatValue17 = RotationManager.this.flag2 ? RotationManager.this.floatValue : clientPlayerEntity.getPitch();
            }
         }
      );
      this.handleListener(
         new SafeListener<PlayerMoveEvent>(Integer.MAX_VALUE) {
            public void handleObject(
               PlayerMoveEvent playerMoveEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               RotationManager.this.rotationAnimation.setFlag2(RotationManager.this.flag2);
               float f2 = RotationManager.this.flag2 ? RotationManager.this.floatValue8 : clientPlayerEntity.getYaw();
               float f3 = RotationManager.this.flag2 ? RotationManager.this.floatValue : clientPlayerEntity.getPitch();
               float f4 = RotationManager.this.floatValue4;
               float f5 = f2 - f4;
               if (f5 < -180.0F || f5 > 180.0F) {
                  float f = Math.round(Math.abs(f5 / 360.0F));
                  f5 = f5 < 0.0F ? f5 + 360.0F * f : f5 - 360.0F * f;
               }

               float f6 = f4 + f5;
               float f7 = MathHelper.clamp(f3, -90.0F, 90.0F);
               if (RotationManager.this.flag2 || Float.isNaN(RotationManager.this.floatValue3) && Float.isNaN(RotationManager.this.floatValue6)) {
                  RotationManager.this.floatValue3 = f6;
                  RotationManager.this.floatValue6 = f7;
               } else {
                  RotationManager.this.floatValue3 = Float.NaN;
                  RotationManager.this.floatValue6 = Float.NaN;
               }

               if (RotationManager.this.flag2 && RotationsModule.getRotationsModule().sensitivity.getValue()) {
                  float[] floats = new float[]{RotationManager.this.floatValue4, RotationManager.this.floatValue5};
                  float[] floats2 = new float[]{f6, f7};
                  float[] floats3 = RotationManager.this.values(floats, floats2);
                  f6 = floats3[0];
                  f7 = floats3[1];
               }

               playerMoveEvent.setFloatValue(f6);
               playerMoveEvent.setFloatValue2(f7);
               RotationManager.this.rotationState.handleClientPlayerEntity(clientPlayerEntity, f6, f7);
               if (RotationManager.this.flag2) {
                  RotationManager.this.rotationAnimation.handleFloat(RotationManager.this.floatValue8);
                  RotationManager.this.rotationAnimation.handleFloat2(RotationManager.this.floatValue);
               } else if (RotationManager.this.flag4) {
                  RotationManager.this.rotationAnimation.handleFloat(RotationManager.this.floatValue13);
                  RotationManager.this.rotationAnimation.handleFloat2(RotationManager.this.floatValue14);
                  RotationManager.this.flag4 = false;
               } else {
                  RotationManager.this.rotationAnimation.handleFloat(clientPlayerEntity.getYaw());
                  RotationManager.this.rotationAnimation.handleFloat2(clientPlayerEntity.getPitch());
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<UnusedEvent>(Integer.MIN_VALUE) {
            public void handleObject(
               UnusedEvent unusedEvent, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               RotationManager.this.runnableQueue.forEach(Runnable::run);
               RotationManager.this.runnableQueue.clear();
               RotationManager.this.rotation.run();
               RotationManager.this.flag3 = false;
               RotationManager.this.flag2 = false;
            }
         }
      );
      this.handleListener(
         new SafeListener<Pre>() {
            public void handleObject(
               Pre pre, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!RotationManager.this.rotation.check() && RotationManager.this.flag3) {
                  RotationManager.this.floatValue9 = clientPlayerEntity.getYaw();
                  RotationManager.this.floatValue11 = clientPlayerEntity.getPitch();
                  clientPlayerEntity.setYaw(RotationManager.this.rotation.getFloatValue2());
                  clientPlayerEntity.setPitch(RotationManager.this.rotation.getFloatValue());
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Post>() {
            public void handleObject(
               Post post, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!RotationManager.this.rotation.check() && RotationManager.this.flag3) {
                  clientPlayerEntity.setYaw(RotationManager.this.floatValue9);
                  clientPlayerEntity.setPitch(RotationManager.this.floatValue11);
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<TickEvent>(1) {
            public void handleObject(
               TickEvent tickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (RotationsModule.check2() && !RotationManager.this.rotation.check() && !RotationManager.this.flag3) {
                  float f = clientPlayerEntity.input.movementForward;
                  float f2 = clientPlayerEntity.input.movementSideways;
                  float f3 = (clientPlayerEntity.getYaw() - RotationManager.this.rotation.getFloatValue2()) * (float) (Math.PI / 180.0);
                  float f4 = MathHelper.cos(f3);
                  float f5 = MathHelper.sin(f3);
                  clientPlayerEntity.input.movementSideways = Math.round(f2 * f4 - f * f5);
                  clientPlayerEntity.input.movementForward = Math.round(f * f4 + f2 * f5);
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<UpdateVelocityEvent>() {
            public void handleObject(
               UpdateVelocityEvent updateVelocityEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (RotationsModule.check2() && !RotationManager.this.rotation.check() && !RotationManager.this.flag3) {
                  updateVelocityEvent.setVec3d(
                     RotationManager.this.createVec3d(
                        updateVelocityEvent.getVec3d2(), updateVelocityEvent.getFloatValue(), RotationManager.this.rotation.getFloatValue2()
                     )
                  );
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<JumpEvent2>() {
            public void handleObject(
               JumpEvent2 jumpEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!RotationManager.this.rotation.check()) {
                  if (RotationsModule.check2() || RotationManager.this.flag3) {
                     jumpEvent2.setFloatValue(RotationManager.this.rotation.getFloatValue2());
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<AttackEvent>() {
            public void handleObject(
               AttackEvent attackEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!RotationManager.this.rotation.check()) {
                  if (RotationsModule.check2() || RotationManager.this.flag3) {
                     attackEvent.setFloatValue(RotationManager.this.rotation.getFloatValue2());
                  }
               }
            }
         }
      );
      this.handleListener(new Listener<PostSend<Full>>(Integer.MAX_VALUE) {
         public void handleObject2(PostSend<Full> postSend) {
            if (((Full)postSend.getPacket()).changesLook()) {
               RotationManager.this.floatValue4 = ((Full)postSend.getPacket()).getYaw(0.0F);
               RotationManager.this.floatValue5 = ((Full)postSend.getPacket()).getPitch(0.0F);
            }
         }
      });
      this.handleListener(new Listener<PostSend<LookAndOnGround>>(Integer.MAX_VALUE) {
         public void handleObject2(PostSend<LookAndOnGround> postSend) {
            RotationManager.this.floatValue4 = ((LookAndOnGround)postSend.getPacket()).getYaw(0.0F);
            RotationManager.this.floatValue5 = ((LookAndOnGround)postSend.getPacket()).getPitch(0.0F);
         }
      });
      this.handleListener(
         new SafeListener<FindCrosshairTargetEvent>() {
            public void handleObject(
               FindCrosshairTargetEvent findCrosshairTargetEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (FreecamModule.getFreecamModule().check7()) {
                  FreecamModule freecamModule = FreecamModule.getFreecamModule();
                  Vec3d vec3d = freecamModule.createVec3d();
                  Camera camera = mc.gameRenderer.getCamera();
                  findCrosshairTargetEvent.setHitResult(
                     RotationManager.this.resolveHitResult(
                        entity -> vec3d,
                        findCrosshairTargetEvent.getEntity(),
                        clientWorld,
                        RotationManager.Rotation.createRotation(camera.getYaw(), camera.getPitch()),
                        findCrosshairTargetEvent.getDoubleValue2(),
                        findCrosshairTargetEvent.getDoubleValue(),
                        findCrosshairTargetEvent.getFloatValue()
                     )
                  );
               } else if (RotationsModule.check6()) {
                  if (!RotationManager.this.normal.testLong(100L)) {
                     findCrosshairTargetEvent.setHitResult(
                        RotationManager.this.resolveHitResult(
                           entity -> entity.getCameraPosVec(findCrosshairTargetEvent.getFloatValue()),
                           findCrosshairTargetEvent.getEntity(),
                           clientWorld,
                           RotationManager.Rotation.createRotation(RotationManager.this.floatValue15, RotationManager.this.floatValue17),
                           findCrosshairTargetEvent.getDoubleValue2(),
                           findCrosshairTargetEvent.getDoubleValue(),
                           findCrosshairTargetEvent.getFloatValue()
                        )
                     );
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Send<PlayerInteractItemC2SPacket>>(Integer.MIN_VALUE) {
            public void handleObject(
               Send<PlayerInteractItemC2SPacket> send,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!RotationManager.this.flag6) {
                  if (!send.isCancelled()) {
                     PlayerInteractItemC2SPacket playerInteractItemC2SPacket = (PlayerInteractItemC2SPacket)send.getPacket();
                     if (!MathUtils.testFloat(RotationManager.this.floatValue4, playerInteractItemC2SPacket.getYaw(), 0.03F)
                        || !MathUtils.testFloat(RotationManager.this.floatValue5, playerInteractItemC2SPacket.getPitch(), 0.03F)) {
                        if (RotationsModule.check6()) {
                           IPlayerInteractItemC2SPacket iPlayerInteractItemC2SPacket = (IPlayerInteractItemC2SPacket)(Object)playerInteractItemC2SPacket;
                           iPlayerInteractItemC2SPacket.setYaw(RotationManager.this.floatValue4);
                           iPlayerInteractItemC2SPacket.setPitch(RotationManager.this.floatValue5);
                        } else if (RotationsModule.check4()) {
                           float f = playerInteractItemC2SPacket.getYaw();
                           float f2 = playerInteractItemC2SPacket.getPitch();
                           send.setCancelled(true);
                           RotationManager.this.flag6 = true;
                           RotationManager.this.handleClientPlayerEntity3(clientPlayerEntity, f, f2);
                           clientPlayerEntity.networkHandler.sendPacket(send.getPacket());
                           RotationManager.this.flag6 = false;
                           RotationManager.this.handleClientPlayerEntity5(clientPlayerEntity);
                        }
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Send<PlayerInteractBlockC2SPacket>>(Integer.MIN_VALUE) {
            public void handleObject(
               Send<PlayerInteractBlockC2SPacket> send,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!RotationManager.this.flag6) {
                  if (RotationsModule.check4() && !send.isCancelled()) {
                     float[] floats = RotationUtils.values2(clientPlayerEntity, ((PlayerInteractBlockC2SPacket)send.getPacket()).getBlockHitResult().getPos());
                     if (!MathUtils.testFloat(RotationManager.this.floatValue4, floats[0], 0.03F)
                        || !MathUtils.testFloat(RotationManager.this.floatValue5, floats[1], 0.03F)) {
                        if (!RotationManager.this.rotation.testFloat(floats[0], floats[1])) {
                           send.setCancelled(true);
                           RotationManager.this.flag6 = true;
                           RotationManager.this.handleClientPlayerEntity3(clientPlayerEntity, floats[0], floats[1]);
                           clientPlayerEntity.networkHandler.sendPacket(send.getPacket());
                           RotationManager.this.flag6 = false;
                           RotationManager.this.handleClientPlayerEntity5(clientPlayerEntity);
                        }
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Send<PlayerInteractEntityC2SPacket>>(Integer.MIN_VALUE) {
            public void handleObject(
               Send<PlayerInteractEntityC2SPacket> send,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!RotationManager.this.flag6) {
                  if (RotationsModule.check4() && !send.isCancelled()) {
                     Entity entity = clientWorld.getEntityById(((IPlayerInteractEntityC2SPacket)(Object)send.getPacket()).getEntityId());
                     if (entity != null) {
                        if (!RaycastUtils.testPhobosClient(ClientHolder.getPhobosClient(), entity)) {
                           float[] floats = RotationUtils.values3(clientPlayerEntity, entity, RotationsModule.getRotationsModule().slices.getValue());
                           send.setCancelled(true);
                           RotationManager.this.flag6 = true;
                           RotationManager.this.handleClientPlayerEntity3(clientPlayerEntity, floats[0], floats[1]);
                           clientPlayerEntity.networkHandler.sendPacket(send.getPacket());
                           RotationManager.this.flag6 = false;
                           RotationManager.this.handleClientPlayerEntity5(clientPlayerEntity);
                        }
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Receive<PlayerPositionLookS2CPacket>>(Integer.MIN_VALUE) {
            public void handleObject(
               Receive<PlayerPositionLookS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!receive.isCancelled()) {
                  RotationManager.this.floatValue4 = ((PlayerPositionLookS2CPacket)receive.getPacket()).getYaw();
                  RotationManager.this.floatValue5 = ((PlayerPositionLookS2CPacket)receive.getPacket()).getPitch();
               }
            }
         }
      );
   }

   public boolean testFloat(float f, float f2) {
      return this.testFloat2(f, f2, false);
   }

   public Vec3d createVec3d(Vec3d vec3d, float f, float f2) {
      double d = vec3d.lengthSquared();
      if (d < AntiCheatModule.Client.computeDouble2(1.0E-7)) {
         return Vec3d.ZERO;
      }

      Vec3d vec3d2 = (d > 1.0 ? vec3d.normalize() : vec3d).multiply(f);
      float f3 = MathHelper.sin(f2 * (float) (Math.PI / 180.0));
      float f4 = MathHelper.cos(f2 * (float) (Math.PI / 180.0));
      return new Vec3d(vec3d2.x * f4 - vec3d2.z * f3, vec3d2.y, vec3d2.z * f4 + vec3d2.x * f3);
   }

   public HitResult resolveHitResult(
      Function<Entity, Vec3d> function, Entity entity, ClientWorld clientWorld, RotationManager.Rotation rotation, double d, double d2, float f
   ) {
      double d3 = Math.max(d, d2);
      double d4 = MathHelper.square(d3);
      Vec3d vec3d = function.apply(entity);
      HitResult hitResult = this.resolveHitResult3(clientWorld, entity, function, rotation, d3, f);
      double d5 = hitResult.getPos().squaredDistanceTo(vec3d);
      if (hitResult.getType() != Type.MISS) {
         d4 = d5;
         d3 = Math.sqrt(d4);
      }

      Vec3d vec3d2 = rotation.getVec3d();
      Vec3d vec3d3 = vec3d.add(vec3d2.x * d3, vec3d2.y * d3, vec3d2.z * d3);
      Box box = entity.getBoundingBox().stretch(vec3d2.multiply(d3)).expand(1.0, 1.0, 1.0);
      EntityHitResult entityHitResult = ProjectileUtil.raycast(
         entity,
         vec3d,
         vec3d3,
         box,
         entityx -> !BlockTweaksModule.check() && !BlockTweaksModule.testEntity(entityx) ? !entityx.isSpectator() && entityx.canHit() : false,
         d4
      );
      return entityHitResult != null && entityHitResult.getPos().squaredDistanceTo(vec3d) < d5
         ? this.resolveHitResult2(entityHitResult, vec3d, d2)
         : this.resolveHitResult2(hitResult, vec3d, d);
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, float f) {
      this.rotationSmoother.handleRotation(RotationManager.Rotation.createRotation2(clientPlayerEntity));
      this.normal2.run();
      this.flag5 = true;
      this.floatValue16 = f;
   }

   @Nullable
   public RotationManager.Rotation getRotation2() {
      return !Float.isNaN(this.floatValue3) && !Float.isNaN(this.floatValue6)
         ? RotationManager.Rotation.createRotation(this.floatValue3, this.floatValue6)
         : null;
   }

   public boolean testRotation(RotationManager.Rotation rotation) {
      return this.testFloat(rotation.floatValue2, rotation.floatValue);
   }

   public float value2() {
      float f = (float)((Double)mc.options.getMouseSensitivity().getValue() * 0.6F + 0.2F);
      return f * f * f * 8.0F;
   }

   public void handleFloat(float f, float f2) {
      this.flag4 = true;
      this.floatValue13 = f;
      this.floatValue14 = f2;
      this.normal.run();
   }

   public long value() {
      return this.normal.value();
   }

   public float[] values(float[] floats, float[] floats2) {
      double d = (float)(this.value2() * 0.15);
      float f = this.computeFloat3(floats2[0], floats[0], d);
      float f2 = this.computeFloat3(floats2[1], floats[1], d);
      return new float[]{f, MathUtils.computeFloat3(f2, -90.0F, 90.0F)};
   }

   public boolean testFloat2(float f, float f2, boolean flag) {
      if (this.flag && !this.flag2) {
         RotationEvent rotationEvent = this.invoke(new RotationEvent(new RotationManager.Rotation(f, f2)));
         if (rotationEvent.isCancelled()) {
            return false;
         }

         this.floatValue8 = f;
         this.floatValue = f2;
         this.flag2 = true;
         this.rotation.handleFloat2(f, f2);
         this.flag3 = flag;
         this.normal.run();
         return true;
      } else {
         return false;
      }
   }

   public void handleClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity, PlayerMoveEvent playerMoveEvent) {
   }

   public HitResult resolveHitResult2(HitResult hitResult, Vec3d vec3d, double d) {
      Vec3d vec3d3 = hitResult.getPos();
      if (!vec3d3.isInRange(vec3d, d)) {
         Vec3d vec3d2 = hitResult.getPos();
         Direction direction = Direction.getFacing(vec3d2.x - vec3d.x, vec3d2.y - vec3d.y, vec3d2.z - vec3d.z);
         return BlockHitResult.createMissed(vec3d2, direction, BlockPos.ofFloored(vec3d2));
      } else {
         return hitResult;
      }
   }

   public void handleClientPlayerEntity3(ClientPlayerEntity clientPlayerEntity, float f, float f2) {
      this.handleClientPlayerEntity4(clientPlayerEntity, f, f2, true);
   }

   public float computeFloat2(float f) {
      return this.rotation.check() ? f : this.rotation.getFloatValue();
   }

   public void handleClientPlayerEntity4(ClientPlayerEntity clientPlayerEntity, float f, float f2, boolean flag) {
      if (flag) {
         this.floatValue2 = this.floatValue4;
         this.floatValue7 = this.floatValue5;
      }

      PlayerEntity playerEntity = ClientHolder.getPhobosClient().getMovementSimulator().resolvePlayerEntity(clientPlayerEntity);
      PlayerMoveC2SPacket playerMoveC2SPacket = new Full(playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), f, f2, playerEntity.isOnGround());
      this.handlePlayerMoveC2SPacket(playerMoveC2SPacket);
      clientPlayerEntity.networkHandler.sendPacket(playerMoveC2SPacket);
   }

   public float computeFloat3(float f, float f2, double d) {
      float f3 = f - f2;
      return f2 + (float)Math.round(f3 / d) * (float)d;
   }

   public HitResult resolveHitResult3(
      ClientWorld clientWorld, Entity entity, Function<Entity, Vec3d> function, RotationManager.Rotation rotation, double d, float f
   ) {
      Vec3d vec3d = function.apply(entity);
      Vec3d vec3d2 = rotation.getVec3d();
      Vec3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
      return clientWorld.raycast(new RaycastContext(vec3d, vec3d3, ShapeType.OUTLINE, FluidHandling.NONE, entity));
   }

   public float computeFloat4(float f) {
      return this.rotation.check() ? f : this.rotation.getFloatValue2();
   }

   @NotNull
   public static RotationManager.Rotation getRotation() {
      return ClientHolder.getPhobosClient().getRotationManager().rotation;
   }

   public float getFloatValue() {
      return this.floatValue12;
   }

   public float getFloatValue7() {
      return this.floatValue7;
   }

   public float getFloatValue2() {
      return this.floatValue10;
   }

   public boolean isFlag2() {
      return this.flag;
   }

   public void setRunnable(Runnable runnable) {
      this.runnableQueue.add(runnable);
   }

   public float getFloatValue3() {
      return this.floatValue;
   }

   public float getFloatValue22() {
      return this.floatValue2;
   }

   public float getFloatValue8() {
      return this.floatValue8;
   }

   public float computeFloat5(float f) {
      return this.flag2 ? this.floatValue8 : (this.rotation.check() ? f : this.rotation.getFloatValue2());
   }

   public float[] values2() {
      return new float[]{
         this.rotationAnimation.getTimedFloatAnimator2().getFloatValue2(),
         this.rotationAnimation.getTimedFloatAnimator().getFloatValue2(),
         this.timedFloatAnimator.getFloatValue2()
      };
   }

   public void handleClientPlayerEntity5(ClientPlayerEntity clientPlayerEntity) {
      PlayerEntity playerEntity = ClientHolder.getPhobosClient().getMovementSimulator().resolvePlayerEntity(clientPlayerEntity);
      PlayerMoveC2SPacket playerMoveC2SPacket = new Full(
         playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), this.floatValue2, this.floatValue7, playerEntity.isOnGround()
      );
      this.handlePlayerMoveC2SPacket(playerMoveC2SPacket);
      clientPlayerEntity.networkHandler.sendPacket(playerMoveC2SPacket);
   }

   public float getFloatValue11() {
      return this.floatValue11;
   }

   public float getFloatValue5() {
      return this.floatValue5;
   }

   public void handlePlayerMoveC2SPacket(PlayerMoveC2SPacket playerMoveC2SPacket) {
      ((IPlayerMoveC2SPacket)(Object)playerMoveC2SPacket).setSkipping();
   }

   public boolean isFlag4() {
      return this.flag4;
   }

   public float getFloatValue9() {
      return this.floatValue9;
   }

   public float getFloatValue32() {
      return this.floatValue3;
   }

   public float getFloatValue4() {
      return this.floatValue4;
   }

   public static boolean isFlag() {
      return ClientHolder.getPhobosClient().getRotationManager().flag2;
   }

   public boolean isFlag3() {
      return this.flag3;
   }

   public float computeFloat6(float f) {
      return this.flag2 ? this.floatValue : (this.rotation.check() ? f : this.rotation.getFloatValue());
   }

   public RotationManager.Rotation getRotation3() {
      return this.rotation;
   }

   public float getFloatValue14() {
      return this.floatValue14;
   }

   public void setFlag2(boolean flag) {
      this.flag = flag;
   }

   public float getFloatValue13() {
      return this.floatValue13;
   }

   public float getFloatValue18() {
      return this.floatValue18;
   }

   public RotationState getRotationState() {
      return this.rotationState;
   }

   public TimeGate.Normal getNormal() {
      return this.normal;
   }

   public boolean isFlag5() {
      return this.flag5;
   }

   public float getFloatValue17() {
      return this.floatValue17;
   }

   public float getFloatValue15() {
      return this.floatValue15;
   }

   public boolean isFlag6() {
      return this.flag6;
   }

   public void setFloatValue10(float f) {
      this.floatValue10 = f;
   }

   public void setFlag4(boolean flag) {
      this.flag4 = flag;
   }

   public void setFlag3(boolean flag) {
      this.flag3 = flag;
   }

   public RotationSmoother getRotationSmoother() {
      return this.rotationSmoother;
   }

   public void setFlag(boolean flag) {
      this.flag2 = flag;
   }

   public TimedFloatAnimator getTimedFloatAnimator() {
      return this.timedFloatAnimator;
   }

   public Queue<Runnable> getRunnableQueue() {
      return this.runnableQueue;
   }

   public TimeGate.Normal getNormal2() {
      return this.normal2;
   }

   public RotationManager.RotationAnimation getRotationAnimation() {
      return this.rotationAnimation;
   }

   public float getFloatValue6() {
      return this.floatValue6;
   }

   public float getFloatValue16() {
      return this.floatValue16;
   }

   public void setFloatValue18(float f) {
      this.floatValue18 = f;
   }

   public void setFloatValue14(float f) {
      this.floatValue14 = f;
   }

   public void setFloatValue2(float f) {
      this.floatValue2 = f;
   }

   public void setFloatValue17(float f) {
      this.floatValue17 = f;
   }

   public void setFloatValue8(float f) {
      this.floatValue8 = f;
   }

   public void setFloatValue3(float f) {
      this.floatValue3 = f;
   }

   public void setFloatValue(float f) {
      this.floatValue = f;
   }

   public void setFloatValue15(float f) {
      this.floatValue15 = f;
   }

   public void setFloatValue16(float f) {
      this.floatValue16 = f;
   }

   public void setFlag6(boolean flag) {
      this.flag6 = flag;
   }

   public void setFloatValue11(float f) {
      this.floatValue11 = f;
   }

   public void setFloatValue4(float f) {
      this.floatValue4 = f;
   }

   public void setFlag5(boolean flag) {
      this.flag5 = flag;
   }

   public void setFloatValue9(float f) {
      this.floatValue9 = f;
   }

   public void setFloatValue13(float f) {
      this.floatValue13 = f;
   }

   public void setFloatValue12(float f) {
      this.floatValue12 = f;
   }

   public void setFloatValue5(float f) {
      this.floatValue5 = f;
   }

   public void setFloatValue7(float f) {
      this.floatValue7 = f;
   }

   public void setFloatValue6(float f) {
      this.floatValue6 = f;
   }

   public static final class Rotation {
      public float floatValue;
      public static final RotationManager.Rotation INSTANCE = new RotationManager.Rotation(Float.NaN, Float.NaN);
      public float floatValue2;

      public static RotationManager.Rotation createRotation(float f, float f2) {
         return new RotationManager.Rotation(f, f2);
      }

      public void handleEntity(Entity entity) {
         entity.setYaw(this.floatValue2);
         entity.setPitch(this.floatValue);
      }

      public void setFloatValue2(float f) {
         this.floatValue2 = f;
      }

      public void run() {
         this.floatValue2 = Float.NaN;
         this.floatValue = Float.NaN;
      }

      public Rotation(float f, float f2) {
         this.floatValue2 = f;
         this.floatValue = f2;
      }

      public boolean testFloat(float f, float f2) {
         return this.floatValue2 == f && this.floatValue == f2;
      }

      public void handleFloat(float[] floats) {
         floats[0] = this.floatValue2;
         floats[1] = this.floatValue;
      }

      public static RotationManager.Rotation createRotation2(PlayerEntity playerEntity) {
         return new RotationManager.Rotation(playerEntity.getYaw(), playerEntity.getPitch());
      }

      public float getFloatValue2() {
         return this.floatValue2;
      }

      public static RotationManager.Rotation createRotation3(float[] floats) {
         return new RotationManager.Rotation(floats[0], floats[1]);
      }

      public Vec3d getVec3d() {
         return MathUtils.createVec3d4(this.floatValue2, this.floatValue);
      }

      public boolean check() {
         return Float.isNaN(this.floatValue2) || Float.isNaN(this.floatValue);
      }

      public void setFloatValue(float f) {
         this.floatValue = f;
      }

      public void handleFloat2(float f, float f2) {
         this.floatValue2 = f;
         this.floatValue = f2;
      }

      public float[] values() {
         return new float[]{this.floatValue2, this.floatValue};
      }

      public static RotationManager.Rotation resolveRotation(RotationManager.Rotation rotation) {
         return createRotation(rotation.floatValue2, rotation.floatValue);
      }

      public float getFloatValue() {
         return this.floatValue;
      }
   }

   public static class RotationAnimation {
      public final TimedFloatAnimator timedFloatAnimator;
      public float floatValue;
      public float floatValue2;
      public float floatValue3;
      public final TimedFloatAnimator timedFloatAnimator2;
      public boolean flag = false;
      public final TimeGate.Normal normal = new TimeGate.Normal();
      public boolean flag2;
      public float floatValue4;

      public float getFloatValue3() {
         return this.floatValue3;
      }

      public void setFloatValue3(float f) {
         this.floatValue3 = f;
      }

      public void handleFloat(float f) {
         this.floatValue = this.floatValue2;
         this.floatValue2 = f;
      }

      public float getFloatValue() {
         return this.floatValue;
      }

      public TimeGate.Normal getNormal() {
         return this.normal;
      }

      public TimedFloatAnimator getTimedFloatAnimator() {
         return this.timedFloatAnimator;
      }

      public float getFloatValue4() {
         return this.floatValue4;
      }

      public float getFloatValue2() {
         return this.floatValue2;
      }

      public RotationManager.Rotation getRotation() {
         return RotationManager.Rotation.createRotation(this.timedFloatAnimator2.getFloatValue2(), this.timedFloatAnimator.getFloatValue2());
      }

      public void handleFloat2(float f) {
         this.floatValue3 = this.floatValue4;
         this.floatValue4 = f;
      }

      public void handleFloat3(float f, float f2) {
         float f3 = this.timedFloatAnimator2.getFloatValue2();
         float f4 = this.timedFloatAnimator.getFloatValue2();
         this.flag = RotationPlanner.testFloat2(f3, f4, f, f2);
      }

      public float value() {
         return !this.normal.testLong(1000L) ? (!this.flag2 && !this.flag ? 0.06F : 0.08F) : 0.2F;
      }

      public void setFlag2(boolean flag) {
         this.flag2 = flag;
      }

      public void run() {
         this.handleFloat(MathHelper.wrapDegrees(this.floatValue2));
         if (this.flag2) {
            this.flag = false;
            this.normal.run();
         } else {
            this.handleFloat3(this.floatValue2, this.floatValue4);
         }

         this.timedFloatAnimator2.setFloatValue(this.floatValue2);
         this.timedFloatAnimator.setFloatValue(this.floatValue4);
         if (RotationsModule.check5()) {
            this.timedFloatAnimator2.setFloatValue2(MathHelper.lerpAngleDegrees(TimerManager.getFloatValue(), this.floatValue, this.floatValue2));
            this.timedFloatAnimator.setFloatValue2(MathHelper.lerpAngleDegrees(TimerManager.getFloatValue(), this.floatValue3, this.floatValue4));
         } else {
            float f = this.value();
            this.timedFloatAnimator2.handleFloat3(50.0F, f);
            this.timedFloatAnimator.handleFloat3(50.0F, f);
         }
      }

      public RotationAnimation() {
         this.timedFloatAnimator2 = new TimedFloatAnimator();
         this.timedFloatAnimator = new TimedFloatAnimator();
      }

      public TimedFloatAnimator getTimedFloatAnimator2() {
         return this.timedFloatAnimator2;
      }

      public boolean isFlag() {
         return this.flag;
      }

      public void setFlag(boolean flag) {
         this.flag = flag;
      }

      public boolean isFlag2() {
         return this.flag2;
      }

      public void setFloatValue(float f) {
         this.floatValue = f;
      }
   }
}
