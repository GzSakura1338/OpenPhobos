package org.phobos.core.movement;

import java.util.ArrayDeque;
import java.util.Deque;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.OnGroundOnly;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.Vec3d;
import org.phobos.core.event.events.PacketEvent.PostReceive;
import org.phobos.core.event.events.PacketEvent.PostSend;
import org.phobos.event.DisconnectEvent;
import org.phobos.event.Listener;
import org.phobos.event.SafeListener;
import org.phobos.core.fakeplayer.ControlledFakePlayer;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.rotation.LookDirection;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.event.events.PlayerMoveEvent;
import org.phobos.core.render.RenderContext;
import org.phobos.core.event.events.UnusedEvent;

public class MovementSimulator extends SilentMovementManager {
   public volatile boolean flag;
   public final Deque<LookDirection> lookDirectionDeque = new ArrayDeque<>(400);
   public ControlledFakePlayer controlledFakePlayer = null;
   public static final int intValue = 400;
   public volatile int intValue2;
   public volatile boolean flag2;
   public double doubleValue;
   public SilentMovementManager.SilentMovement silentMovement;

   public static LookDirection resolveLookDirection(MovementSimulator movementSimulator, LookDirection lookDirection) {
      return movementSimulator.lookDirection = lookDirection;
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      clientPlayerEntity.networkHandler.sendPacket(new ClientCommandC2SPacket(clientPlayerEntity, Mode.RELEASE_SHIFT_KEY));
   }

   public static LookDirection resolveLookDirection2(MovementSimulator movementSimulator) {
      return movementSimulator.lookDirection;
   }

   public static void handleMovementSimulator(MovementSimulator movementSimulator, LookDirection lookDirection, Deque deque) {
      movementSimulator.handleLookDirection(lookDirection, deque);
   }

   public Deque<LookDirection> getLookDirectionDeque() {
      return this.lookDirectionDeque;
   }

   public void setDoubleValue(double d) {
      this.doubleValue = d;
   }

   public static LookDirection resolveLookDirection3(MovementSimulator movementSimulator) {
      return movementSimulator.lookDirection;
   }

   public double getDoubleValue() {
      return this.doubleValue;
   }

   public static LookDirection resolveLookDirection4(MovementSimulator movementSimulator) {
      return movementSimulator.lookDirection;
   }

   public void handleClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity) {
      clientPlayerEntity.networkHandler.sendPacket(new ClientCommandC2SPacket(clientPlayerEntity, Mode.PRESS_SHIFT_KEY));
   }

   public boolean isFlag2() {
      return this.flag2;
   }

   public PlayerEntity resolvePlayerEntity(PlayerEntity playerEntity) {
      ControlledFakePlayer controlledFakePlayer = this.controlledFakePlayer;
      if (controlledFakePlayer != null) {
         Vec3d vec3d = controlledFakePlayer.getPos();
         if (playerEntity != null) {
            controlledFakePlayer.setPose(playerEntity.getPose());
         }

         controlledFakePlayer.setPosition(vec3d);
         return controlledFakePlayer;
      } else {
         return playerEntity;
      }
   }

   public int getIntValue2() {
      return this.intValue2;
   }

   public MovementSimulator() {
      super(400);
      this.handleListener(
         new SafeListener<PlayerMoveEvent>(Integer.MAX_VALUE) {
            public void handleObject(
               PlayerMoveEvent playerMoveEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (MovementSimulator.this.silentMovement != null) {
                  MovementSimulator.this.silentMovement.setDoubleValue4(clientPlayerEntity.getX());
                  MovementSimulator.this.silentMovement.setDoubleValue(clientPlayerEntity.getY());
                  MovementSimulator.this.silentMovement.setDoubleValue6(clientPlayerEntity.getZ());
                  playerMoveEvent.setDoubleValue3(MovementSimulator.this.silentMovement.getDoubleValue3());
                  playerMoveEvent.setDoubleValue(MovementSimulator.this.silentMovement.getDoubleValue5());
                  playerMoveEvent.setDoubleValue2(MovementSimulator.this.silentMovement.getDoubleValue2());
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<UnusedEvent>(Integer.MIN_VALUE) {
            public void handleObject(
               UnusedEvent unusedEvent, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (MovementSimulator.this.silentMovement != null) {
                  clientPlayerEntity.setPosition(
                     MovementSimulator.this.silentMovement.getDoubleValue4(),
                     MovementSimulator.this.silentMovement.getDoubleValue(),
                     MovementSimulator.this.silentMovement.getDoubleValue6()
                  );
                  MovementSimulator.this.silentMovement = null;
               }
            }
         }
      );
      this.handleListener(new Listener<PostReceive<PlayerPositionLookS2CPacket>>() {
         public void handleObject2(PostReceive<PlayerPositionLookS2CPacket> postReceive) {
            MovementSimulator.this.intValue2 = ((PlayerPositionLookS2CPacket)postReceive.getPacket()).getTeleportId();
         }
      });
      this.handleListener(new Listener<PostSend<TeleportConfirmC2SPacket>>() {
         public void handleObject2(PostSend<TeleportConfirmC2SPacket> postSend) {
            MovementSimulator.this.intValue2 = ((TeleportConfirmC2SPacket)postSend.getPacket()).getTeleportId();
         }
      });
      this.handleListener(
         new Listener<UnusedEvent>(Integer.MAX_VALUE) {
            public void handleObject2(UnusedEvent unusedEvent) {
               MovementSimulator.handleMovementSimulator(
                  MovementSimulator.this,
                  MovementSimulator.resolveLookDirection2(MovementSimulator.this),
                  MovementSimulator.this.lookDirectionDeque
               );
            }
         }
      );
      this.handleListener(new Listener<DisconnectEvent>() {
         public void handleObject2(DisconnectEvent disconnectEvent) {
            MovementSimulator.resolveLookDirection(MovementSimulator.this, new LookDirection());
            MovementSimulator.this.controlledFakePlayer = null;
            MovementSimulator.this.lookDirectionDeque.clear();
         }
      });
      this.handleListener(
         new Listener<PostSend<OnGroundOnly>>(Integer.MAX_VALUE) {
            public void handleObject2(PostSend<OnGroundOnly> postSend) {
               MovementSimulator.resolveLookDirection5(
                  MovementSimulator.this,
                  new LookDirection(
                     MovementSimulator.resolveLookDirection3(MovementSimulator.this), ((OnGroundOnly)postSend.getPacket()).isOnGround()
                  )
               );
               MovementSimulator.this.handleLookDirection(
                  MovementSimulator.resolveLookDirection4(MovementSimulator.this),
                  MovementSimulator.resolveDeque(MovementSimulator.this)
               );
            }
         }
      );
      this.handleListener(
         new Listener<PostSend<LookAndOnGround>>(Integer.MAX_VALUE) {
            public void handleObject2(PostSend<LookAndOnGround> postSend) {
               MovementSimulator.resolveLookDirection20(
                  MovementSimulator.this,
                  new LookDirection(
                     MovementSimulator.resolveLookDirection19(MovementSimulator.this),
                     ((LookAndOnGround)postSend.getPacket())
                        .getPitch(MovementSimulator.resolveLookDirection18(MovementSimulator.this).getFloatValue2()),
                     ((LookAndOnGround)postSend.getPacket())
                        .getYaw(MovementSimulator.resolveLookDirection21(MovementSimulator.this).getFloatValue()),
                     ((LookAndOnGround)postSend.getPacket()).isOnGround()
                  )
               );
               MovementSimulator.this.handleLookDirection(
                  MovementSimulator.resolveLookDirection13(MovementSimulator.this),
                  MovementSimulator.resolveDeque2(MovementSimulator.this)
               );
            }
         }
      );
      this.handleListener(
         new Listener<PostSend<PositionAndOnGround>>(Integer.MAX_VALUE) {
            public void handleObject2(PostSend<PositionAndOnGround> postSend) {
               MovementSimulator.resolveLookDirection22(
                  MovementSimulator.this,
                  new LookDirection(
                     MovementSimulator.resolveLookDirection7(MovementSimulator.this),
                     ((PositionAndOnGround)postSend.getPacket()).getX(MovementSimulator.resolveLookDirection12(MovementSimulator.this).getX()),
                     ((PositionAndOnGround)postSend.getPacket()).getY(MovementSimulator.resolveLookDirection16(MovementSimulator.this).getY()),
                     ((PositionAndOnGround)postSend.getPacket()).getZ(MovementSimulator.resolveLookDirection14(MovementSimulator.this).getZ()),
                     ((PositionAndOnGround)postSend.getPacket()).isOnGround()
                  )
               );
               MovementSimulator.this.handleLookDirection(
                  MovementSimulator.resolveLookDirection15(MovementSimulator.this),
                  MovementSimulator.resolveDeque3(MovementSimulator.this)
               );
            }
         }
      );
      this.handleListener(
         new Listener<PostSend<Full>>(Integer.MAX_VALUE) {
            public void handleObject2(PostSend<Full> postSend) {
               MovementSimulator.resolveLookDirection10(
                  MovementSimulator.this,
                  new LookDirection(
                     ((Full)postSend.getPacket()).getX(MovementSimulator.resolveLookDirection17(MovementSimulator.this).getX()),
                     ((Full)postSend.getPacket()).getY(MovementSimulator.resolveLookDirection9(MovementSimulator.this).getY()),
                     ((Full)postSend.getPacket()).getZ(MovementSimulator.resolveLookDirection11(MovementSimulator.this).getZ()),
                     ((Full)postSend.getPacket()).getPitch(MovementSimulator.resolveLookDirection23(MovementSimulator.this).getFloatValue2()),
                     ((Full)postSend.getPacket()).getYaw(MovementSimulator.resolveLookDirection6(MovementSimulator.this).getFloatValue()),
                     ((Full)postSend.getPacket()).isOnGround()
                  )
               );
               MovementSimulator.this.handleLookDirection(
                  MovementSimulator.resolveLookDirection8(MovementSimulator.this),
                  MovementSimulator.resolveDeque4(MovementSimulator.this)
               );
            }
         }
      );
      this.handleListener(
         new SafeListener<PostSend<ClientCommandC2SPacket>>(Integer.MAX_VALUE) {
            public void handleObject(
               PostSend<ClientCommandC2SPacket> postSend,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               switch (((ClientCommandC2SPacket)postSend.getPacket()).getMode()) {
                  case PRESS_SHIFT_KEY:
                     MovementSimulator.this.flag = true;
                     break;
                  case RELEASE_SHIFT_KEY:
                     MovementSimulator.this.flag = false;
                     break;
                  case START_SPRINTING:
                     MovementSimulator.this.flag2 = true;
                     break;
                  case STOP_SPRINTING:
                     MovementSimulator.this.flag2 = false;
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
               MovementSimulator.this.setDoubleValue(EntityUtils.computeEntity(clientPlayerEntity));
            }
         }
      );
      ModuleListenerUtils.handleListenerHolder4(this, () -> this.controlledFakePlayer = null);
   }

   public static LookDirection resolveLookDirection5(MovementSimulator movementSimulator, LookDirection lookDirection) {
      return movementSimulator.lookDirection = lookDirection;
   }

   public boolean isFlag() {
      return this.flag;
   }

   @Override
   public synchronized void handleLookDirection(LookDirection lookDirection, Deque<LookDirection> deque) {
      if (deque == this.lookDirectionDeque2) {
         ClientPlayerEntity clientPlayerEntity = mc.player;
         ControlledFakePlayer controlledFakePlayer = this.controlledFakePlayer;
         if (clientPlayerEntity != null && (controlledFakePlayer == null || controlledFakePlayer.getAbstractClientPlayerEntity() != clientPlayerEntity)
            )
          {
            controlledFakePlayer = new ControlledFakePlayer(clientPlayerEntity);
            this.controlledFakePlayer = controlledFakePlayer;
         }

         if (controlledFakePlayer != null) {
            controlledFakePlayer.prevYaw = controlledFakePlayer.getYaw();
            controlledFakePlayer.prevPitch = controlledFakePlayer.getPitch();
            lookDirection.handleEntity(controlledFakePlayer);
         }
      }

      super.handleLookDirection(lookDirection, deque);
   }

   public void setSilentMovement(SilentMovementManager.SilentMovement silentMovement) {
      this.silentMovement = silentMovement;
   }

   public static Deque<LookDirection> resolveDeque(MovementSimulator movementSimulator) {
      return movementSimulator.lookDirectionDeque2;
   }

   public PlayerEntity resolvePlayerEntity2(PlayerEntity playerEntity) {
      ControlledFakePlayer controlledFakePlayer = this.controlledFakePlayer;
      if (controlledFakePlayer != null) {
         Vec3d vec3d = controlledFakePlayer.getPos();
         controlledFakePlayer.setPosition(vec3d);
         return controlledFakePlayer;
      } else {
         return playerEntity;
      }
   }

   public SilentMovementManager.SilentMovement getSilentMovement() {
      return this.silentMovement;
   }

   public static LookDirection resolveLookDirection6(MovementSimulator movementSimulator) {
      return movementSimulator.lookDirection;
   }

   public static LookDirection resolveLookDirection7(MovementSimulator movementSimulator) {
      return movementSimulator.lookDirection;
   }

   public static LookDirection resolveLookDirection8(MovementSimulator movementSimulator) {
      return movementSimulator.lookDirection;
   }

   public static LookDirection resolveLookDirection9(MovementSimulator movementSimulator) {
      return movementSimulator.lookDirection;
   }

   public static Deque<LookDirection> resolveDeque2(MovementSimulator movementSimulator) {
      return movementSimulator.lookDirectionDeque2;
   }

   public static Deque<LookDirection> resolveDeque3(MovementSimulator movementSimulator) {
      return movementSimulator.lookDirectionDeque2;
   }

   public static LookDirection resolveLookDirection10(MovementSimulator movementSimulator, LookDirection lookDirection) {
      return movementSimulator.lookDirection = lookDirection;
   }

   public static LookDirection resolveLookDirection11(MovementSimulator movementSimulator) {
      return movementSimulator.lookDirection;
   }

   public static LookDirection resolveLookDirection12(MovementSimulator movementSimulator) {
      return movementSimulator.lookDirection;
   }

   public static LookDirection resolveLookDirection13(MovementSimulator movementSimulator) {
      return movementSimulator.lookDirection;
   }

   public static LookDirection resolveLookDirection14(MovementSimulator movementSimulator) {
      return movementSimulator.lookDirection;
   }

   public static LookDirection resolveLookDirection15(MovementSimulator movementSimulator) {
      return movementSimulator.lookDirection;
   }

   public static LookDirection resolveLookDirection16(MovementSimulator movementSimulator) {
      return movementSimulator.lookDirection;
   }

   public static LookDirection resolveLookDirection17(MovementSimulator movementSimulator) {
      return movementSimulator.lookDirection;
   }

   public static LookDirection resolveLookDirection18(MovementSimulator movementSimulator) {
      return movementSimulator.lookDirection;
   }

   public static LookDirection resolveLookDirection19(MovementSimulator movementSimulator) {
      return movementSimulator.lookDirection;
   }

   public static Deque<LookDirection> resolveDeque4(MovementSimulator movementSimulator) {
      return movementSimulator.lookDirectionDeque2;
   }

   public static LookDirection resolveLookDirection20(MovementSimulator movementSimulator, LookDirection lookDirection) {
      return movementSimulator.lookDirection = lookDirection;
   }

   public static LookDirection resolveLookDirection21(MovementSimulator movementSimulator) {
      return movementSimulator.lookDirection;
   }

   public static LookDirection resolveLookDirection22(MovementSimulator movementSimulator, LookDirection lookDirection) {
      return movementSimulator.lookDirection = lookDirection;
   }

   public static LookDirection resolveLookDirection23(MovementSimulator movementSimulator) {
      return movementSimulator.lookDirection;
   }
}
