package org.phobos.module.player;

import java.awt.Color;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import org.earthhack.ducks.IEntity;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.render.RenderContext;
import org.phobos.core.render.PlayerModelCopy;
import org.phobos.core.PhobosClient;
import org.phobos.core.fakeplayer.FakePlayer;
import org.phobos.module.PacketHoldModule;
import org.phobos.core.module.Categories;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.render.Render3d;
import org.phobos.event.Listener;
import org.phobos.mixin.game.entity.ILivingEntity;
import org.phobos.mixin.game.render.interfaces.ILimbAnimator;
import org.phobos.module.movement.StepModule;

public class BlinkModule extends PacketHoldModule {
   public final Setting<Boolean> all = this.resolveSetting2(new BooleanSetting("All").resolveSetting7("Shifts all packets"));
   public final Setting<Boolean> animations;
   public final Setting<Color> color;
   public boolean flag;
   public int intValue;
   public final Setting<Integer> packets;
   public final Setting<Boolean> render = this.resolveSetting2(new BooleanSetting("Render").resolveSetting7("Displays server position"));
   public final Setting<Float> width;
   public static BlinkModule blinkModule;
   public final Setting<Boolean> step;
   public static final int intValue2 = -92871825;
   public final Setting<Boolean> lines;
   public PlayerModelCopy playerModelCopy;
   public final Setting<BlinkModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", BlinkModule.Mode.Texture));
   public final Setting<Color> linesColor;
   public final Setting<Boolean> disable;

   @Override
   public void run3() {
      if (this.step.getValue() && !StepModule.getStepModule().check7()) {
         StepModule.getStepModule().run12();
      }

      if (!mc.isInSingleplayer()) {
         this.intValue = 0;
         this.flag = false;
         this.setFlag2(!this.all.getValue());
         this.run2();
         if (mc.player != null && mc.world != null && this.render.getValue()) {
            float[] floats = this.phobosClient.getRotationManager().values2();
            PlayerEntity playerEntity = new FakePlayer(mc.world, mc.player.getNameForScoreboard());
            ClientPlayerEntity clientPlayerEntity = mc.player;
            playerEntity.copyFrom(clientPlayerEntity);
            playerEntity.setId(-92871825);
            playerEntity.prevYaw = floats[0];
            playerEntity.setYaw(floats[0]);
            playerEntity.prevBodyYaw = floats[2];
            playerEntity.bodyYaw = floats[2];
            playerEntity.handSwingTicks = clientPlayerEntity.handSwingTicks;
            playerEntity.setSneaking(clientPlayerEntity.isSneaking());
            playerEntity.setPose(clientPlayerEntity.getPose());
            ILivingEntity iLivingEntity = (ILivingEntity)(Object)playerEntity;
            iLivingEntity.setLeaningPitch(((ILivingEntity)(Object)clientPlayerEntity).getLeaningPitch());
            iLivingEntity.setLastLeaningPitch(((ILivingEntity)(Object)clientPlayerEntity).getLeaningPitch());
            ((IEntity)(Object)playerEntity).setClientSide(true);
            if (this.mode.getValue().equals(BlinkModule.Mode.Texture)) {
               mc.world.addEntity(playerEntity);
               this.flag = true;
            } else {
               if (this.animations.getValue()) {
                  playerEntity.limbAnimator.setSpeed(clientPlayerEntity.limbAnimator.getSpeed());
                  ((ILimbAnimator)(Object)playerEntity.limbAnimator).setPos(clientPlayerEntity.limbAnimator.getPos());
               }

               this.playerModelCopy = new PlayerModelCopy(playerEntity, mc.getRenderTickCounter().getTickDelta(false));
            }
         }
      }
   }

   @Override
   public boolean testPacket(Packet<?> packet) {
      if (!this.disable.getValue()) {
         return true;
      } else if (this.intValue++ > this.packets.getValue()) {
         this.run11();
         return false;
      } else {
         return true;
      }
   }

   public static BlinkModule getBlinkModule() {
      return blinkModule;
   }

   @Override
   public void run4() {
      if (this.step.getValue() && StepModule.getStepModule().check7()) {
         StepModule.getStepModule().run11();
      }

      if (!mc.isInSingleplayer()) {
         this.playerModelCopy = null;
         if (mc.world != null && this.flag) {
            mc.world.removeEntity(-92871825, RemovalReason.DISCARDED);
         }

         this.run();
         this.run5();
      }
   }

   public BlinkModule(PhobosClient phobosClient) {
      super(phobosClient, "Blink", Categories.player, "Cancels packets to appear teleporting");
      this.lines = this.resolveSetting2(new BooleanSetting("Lines").resolveSetting3(this.resolveVisibility2(this.mode, BlinkModule.Mode.Model)));
      this.linesColor = this.resolveSetting2(
         new ColorSetting("LinesColor", Color.WHITE).getColorSetting().resolveSetting3(this.resolveVisibility2(this.mode, BlinkModule.Mode.Model))
      );
      this.color = this.resolveSetting2(
         new ColorSetting("Color", new Color(255, 255, 255, 50))
            .getColorSetting()
            .resolveSetting3(this.resolveVisibility2(this.mode, BlinkModule.Mode.Model))
      );
      this.width = this.resolveSetting2(
         new NumberSetting<>("Width", 1.0F, 0.0F, 3.0F).resolveSetting3(this.resolveVisibility2(this.mode, BlinkModule.Mode.Model))
      );
      this.animations = this.resolveSetting2(new BooleanSetting("Animations").resolveSetting3(this.resolveVisibility2(this.mode, BlinkModule.Mode.Model)));
      this.disable = this.resolveSetting2(new BooleanSetting("Disable").resolveSetting7("Turns off the module when enough packets were cancelled"));
      this.packets = this.resolveSetting2(new NumberSetting<>("Packets", 500, 10, 10000));
      this.step = this.resolveSetting2(new BooleanSetting("Step"));
      this.createBuilder()
         .resolveBuilder6(this.render)
         .resolveBuilder5(
            this.resolveSettingGroup(this.lines, this.linesColor).resolveSettingGroup5(this.resolveVisibility2(this.mode, BlinkModule.Mode.Model))
         )
         .resolveBuilder(this.mode, this.color, this.width, this.animations)
         .getSettingSubsection();
      this.resolveSettingGroup(this.disable, this.packets);
      this.handleListener(
         new Listener<RenderContext>() {
            public void handleObject2(RenderContext renderContext) {
               if (!MinecraftHolder.mc.isInSingleplayer()) {
                  if (BlinkModule.this.render.getValue()
                     && BlinkModule.this.playerModelCopy != null
                     && BlinkModule.this.mode.getValue().equals(BlinkModule.Mode.Model)) {
                     Render3d.run6();
                     Render3d.handlePlayerEntityModel4(BlinkModule.this.playerModelCopy.getPlayerEntityPlayerEntityModel());
                     Render3d.handlePlayerEntity2(
                        BlinkModule.this.playerModelCopy.getPlayerEntity(),
                        BlinkModule.this.playerModelCopy.getPlayerEntityPlayerEntityModel(),
                        BlinkModule.this.playerModelCopy.getLongValue2(),
                        BlinkModule.this.color.getValue(),
                        BlinkModule.this.lines.getValue(),
                        BlinkModule.this.linesColor.getValue(),
                        BlinkModule.this.width.getValue(),
                        BlinkModule.this.playerModelCopy.getFloatValue()
                     );
                     Render3d.run2();
                  }
               }
            }
         }
      );
      ModuleListenerUtils.handleListenerHolder4(this, this::run11);
      blinkModule = this;
   }

   private enum Mode {
      Texture,
      Model;
   }
}
