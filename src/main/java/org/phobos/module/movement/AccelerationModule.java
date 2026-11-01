package org.phobos.module.movement;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.util.math.Vec3d;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.movement.SpeedState;
import org.phobos.core.movement.MovementModel;
import org.phobos.core.movement.GroundMovementModel;
import org.phobos.core.movement.ParameterizedMovementModel;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.player.PlayerUtils;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.core.movement.MovementModel.State;
import org.phobos.event.MoveEvent2;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class AccelerationModule extends Module {
   public final Setting<Boolean> boost;
   public final SpeedState speedState;
   public final Setting<Boolean> stop;
   public final Setting<Boolean> move = this.resolveSetting2(new BooleanSetting("Move"));
   public final Setting<Boolean> air = this.resolveSetting2(new BooleanSetting("Air", true));
   public final Setting<Double> boostReduction;
   public final Setting<Double> moveFactor;
   public final Setting<Integer> boostTicks;
   public final Setting<AccelerationModule.Control> moveControl = this.resolveSetting2(
      new EnumSetting<>("MoveControl", AccelerationModule.Control.Strict).resolveSetting7("Uses alternative speed for UNCP only")
   );

   public AccelerationModule(PhobosClient phobosClient) {
      super(phobosClient, "Acceleration", Categories.movement, "Makes you strafe");
      this.moveFactor = this.resolveSetting2(new NumberSetting<>("MoveFactor", 1.0, 0.5, 2.0));
      this.boost = this.resolveSetting2(new BooleanSetting("Boost"));
      this.boostTicks = this.resolveSetting2(new NumberSetting<>("BoostTicks", 0, 0, 16).resolveNumberSetting("None", 0));
      this.boostReduction = this.resolveSetting2(new NumberSetting<>("BoostReduction", 1.0, 0.1, 5.0));
      this.stop = this.resolveSetting2(new BooleanSetting("Stop", true));
      this.speedState = new SpeedState();
      this.resolveSettingGroup(this.move, this.air, this.moveControl, this.moveFactor);
      this.resolveSettingGroup(this.boost, this.boostTicks, this.boostReduction);
      this.handleListener(
         new SafeListener<MoveEvent2>(1000) {
            public void handleObject(
               MoveEvent2 moveEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!FlightModule.getFlightModule().check7()) {
                  if (!PlayerUtils.check3() && !clientPlayerEntity.getAbilities().flying && !moveEvent2.isFlag()) {
                     if (clientPlayerEntity.isOnGround() || AccelerationModule.this.air.getValue()) {
                        if (AccelerationModule.this.move.getValue()
                           && !SpeedModule.getSpeedModule().check7()
                           && !LongJumpModule.getLongJumpModule().check7()
                           && (clientPlayerEntity.input.movementForward != 0.0F || clientPlayerEntity.input.movementSideways != 0.0F)) {
                           MovementModel movementModel = switch ((AccelerationModule.Control)AccelerationModule.this.moveControl.getValue()) {
                              case Strict -> new GroundMovementModel();
                              case Custom -> new ParameterizedMovementModel(0.3888882626268, 0.0, false);
                              case Fast -> new ParameterizedMovementModel(0.287 * AccelerationModule.this.moveFactor.getValue(), 0.0, false);
                           };
                           if (AccelerationModule.this.boost.getValue()) {
                              AccelerationModule.this.speedState.handleInt(AccelerationModule.this.boostTicks.getValue());
                              if (movementModel instanceof GroundMovementModel groundMovementModel) {
                                 groundMovementModel.setDoubleValue2(AccelerationModule.this.speedState.getDoubleValue());
                              }
                           }

                           State state = movementModel.resolveState2(clientPlayerEntity, clientWorld, new State(), PlayerUtils.createVec3d2(clientPlayerEntity));
                           if (!state.isFlag2()) {
                              clientPlayerEntity.setVelocity(state.getVec3d());
                              moveEvent2.setVec3d(state.getVec3d());
                           }
                        }

                        if (!PlayerUtils.check() && AccelerationModule.this.stop.getValue()) {
                           clientPlayerEntity.setVelocity(new Vec3d(0.0, clientPlayerEntity.getVelocity().y, 0.0));
                        }
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Receive<ExplosionS2CPacket>>() {
            public void handleObject(
               Receive<ExplosionS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (AccelerationModule.this.boost.getValue()) {
                  AccelerationModule.this.speedState
                     .handleDouble2(
                        ((ExplosionS2CPacket)receive.getPacket()).getPlayerVelocityX(),
                        ((ExplosionS2CPacket)receive.getPacket()).getPlayerVelocityZ(),
                        AccelerationModule.this.boostReduction.getValue()
                     );
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Receive<EntityVelocityUpdateS2CPacket>>() {
            public void handleObject(
               Receive<EntityVelocityUpdateS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (((EntityVelocityUpdateS2CPacket)receive.getPacket()).getEntityId() == clientPlayerEntity.getId()) {
                  if (AccelerationModule.this.boost.getValue()) {
                     AccelerationModule.this.speedState
                        .handleDouble2(
                           ((EntityVelocityUpdateS2CPacket)receive.getPacket()).getVelocityX(),
                           ((EntityVelocityUpdateS2CPacket)receive.getPacket()).getVelocityZ(),
                           AccelerationModule.this.boostReduction.getValue()
                        );
                  }
               }
            }
         }
      );
   }

   @Override
   public void run3() {
      this.speedState.run();
   }

   @Override
   public void run4() {
      this.speedState.run();
   }

   private enum Control {
      Strict,
      Fast,
      Custom;
   }
}
