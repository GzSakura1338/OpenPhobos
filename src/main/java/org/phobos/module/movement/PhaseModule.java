package org.phobos.module.movement;

import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.phobos.api.TextProvider;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.block.VoxelShapeUtils;
import org.phobos.core.module.Categories;
import org.phobos.event.PhasePlayerTickListener;
import org.phobos.event.PhaseJumpListener;
import org.phobos.event.PhaseTickListener;
import org.phobos.event.PhaseComputeNextListener;
import org.phobos.module.Module;

public class PhaseModule extends Module {
   public boolean flag;
   public boolean flag2;
   public final Setting<Double> blocks;
   public static final VoxelShape voxelShape = VoxelShapeUtils.resolveVoxelShape(VoxelShapes.fullCube(), 0.001, 0.0, 0.001);
   public final Setting<Double> distance;
   public final Setting<PhaseModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", PhaseModule.Mode.Normal));
   public static PhaseModule phaseModule;
   public final Setting<Boolean> auto;
   public final Setting<Boolean> offset = this.resolveSetting2(new BooleanSetting("Offset").resolveSetting4(this.mode, PhaseModule.Mode.Desync));

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue()).replaceAll("_", " ");
   }

   public PhaseModule(PhobosClient phobosClient) {
      super(phobosClient, "Phase", Categories.movement, "Allows you to phase through blocks");
      this.distance = this.resolveSetting2(
         new NumberSetting<>("Distance", 0.0, 0.1, 10.0).resolveSetting3(this.resolveVisibility2(this.mode, PhaseModule.Mode.Normal))
      );
      this.blocks = this.resolveSetting2(
         new NumberSetting<>("Blocks", 0.003, 0.001, 10.0).resolveSetting3(this.resolveVisibility2(this.mode, PhaseModule.Mode.Normal))
      );
      this.auto = this.resolveSetting2(
         new BooleanSetting("Auto").resolveSetting3(this.resolveVisibility2(this.mode, PhaseModule.Mode.Normal, PhaseModule.Mode.Intave))
      );
      this.flag = false;
      this.flag2 = false;
      this.handleListener(new PhaseComputeNextListener(this));
      this.handleListener(new PhaseJumpListener(this));
      this.handleListener(new PhasePlayerTickListener(this));
      this.handleListener(new PhaseTickListener(this));
      this.handleTriConsumer3(
         (clientPlayerEntity, clientWorld, clientPlayerInteractionManager) -> {
            this.flag = false;
            if (clientPlayerEntity != null && clientWorld != null) {
               switch ((PhaseModule.Mode)this.mode.getValue()) {
                  case Flow:
                     if (this.auto.getValue()) {
                        double d = Math.cos(Math.toRadians(clientPlayerEntity.getYaw() + 90.0F));
                        double d2 = Math.sin(Math.toRadians(clientPlayerEntity.getYaw() + 90.0F));
                        clientPlayerEntity.setPosition(
                           clientPlayerEntity.getX() + (1.0 * this.blocks.getValue() * d + 0.0 * this.blocks.getValue() * d2),
                           clientPlayerEntity.getY(),
                           clientPlayerEntity.getZ() + (1.0 * this.blocks.getValue() * d2 - 0.0 * this.blocks.getValue() * d)
                        );
                     }
                     break;
                  case Radon_Auto:
                     clientPlayerEntity.setVelocity(0.0, 0.0, 0.0);
               }
            }
         }
      );
      phaseModule = this;
   }

   public static boolean check() {
      return phaseModule.check7() && phaseModule.mode.testObject2(PhaseModule.Mode.Flow);
   }

   public enum Mode implements TextProvider {
      Normal("Normal blocks phase"),
      Sand("Sand phase"),
      Desync("Small offset to create a hitbox desync"),
      Radon_Auto("Phase for RadonAC"),
      Intave("Intave 1.8.x phase"),
      Polar("Polar 1.21 phase"),
      Correction("UNCP Anti-stuck correction for block shapes"),
      Flow("Fast NCP Phase"),
      Anti("Allows you to align yourself to prevent enemies from pearl phasing");

      public final String text;

      Mode(final String description) {
         this.text = description;
      }

      @Override
      public String getText() {
         return this.text;
      }
   }

   public enum Shift {
      None,
      Packet,
      Zoom,
      Infinite,
      Skip;
   }
}
