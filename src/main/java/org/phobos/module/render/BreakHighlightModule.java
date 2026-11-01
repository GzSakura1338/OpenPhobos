package org.phobos.module.render;

import java.awt.Color;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import org.phobos.api.Easing;
import org.phobos.api.Easing.OutEasing;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.render.RenderContext;
import org.phobos.core.math.MutableBox;
import org.phobos.core.block.BlockBreakManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.tick.TimeUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.block.BlockBreakManager.Progress;
import org.phobos.core.render.Render3d.Cuboid;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class BreakHighlightModule extends Module {
   public final Setting<Color> box;
   public final Setting<BreakHighlightModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", BreakHighlightModule.Mode.Continuous));
   public final Setting<Color> line;
   public final Setting<Boolean> fade;
   public final Setting<Long> fadeTime;
   public final Setting<Float> width;

   public BreakHighlightModule(PhobosClient phobosClient) {
      super(phobosClient, "BreakHighlight", Categories.render, "Highlights blocks that are being broken");
      this.box = this.resolveSetting2(new ColorSetting("Box", new Color(255, 255, 255, 70)).getColorSetting());
      this.line = this.resolveSetting2(new ColorSetting("Line", Color.WHITE).getColorSetting());
      this.width = this.resolveSetting2(new NumberSetting<>("Width", 1.0F, 0.0F, 3.0F));
      this.fade = this.resolveSetting2(new BooleanSetting("Fade"));
      this.fadeTime = this.resolveSetting2(new LongNumberSetting("FadeTime", 500L, 0L, 2000L));
      this.resolveSettingGroup(this.fade, this.fadeTime).resolveSettingGroup5(this.resolveVisibility2(this.mode, BreakHighlightModule.Mode.Continuous));
      this.handleListener(
         new SafeListener<RenderContext>() {
            public void handleObject(
               RenderContext renderContext,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               MutableBox mutableBox = new MutableBox();

               for (Progress progress : BreakHighlightModule.this.getPhobosClient().getBlockBreakManager().getBlockPosMap3().values()) {
                  long l = TimeUtils.computeLong(progress.getLongValue());
                  BlockPos blockPos = progress.getBlockPos();
                  BlockState blockState = clientWorld.getBlockState(blockPos).getBlock().getDefaultState();
                  VoxelShape voxelShape = blockState.getOutlineShape(clientWorld, blockPos);
                  if (!blockState.isAir() && !voxelShape.isEmpty()) {
                     mutableBox.handleBox(voxelShape.getBoundingBox().offset(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
                     switch ((BreakHighlightModule.Mode)BreakHighlightModule.this.mode.getValue()) {
                        case Staged:
                           float f = MathHelper.clamp(BlockBreakManager.computeBlockPos(blockPos, blockState, clientWorld), 0.0F, 1.0F);
                           float f2 = f * (float)l / 50.0F;
                           float f3 = f2 - f;
                           double d = MathHelper.clamp(MathHelper.lerp(renderContext.getFloatValue(), f3 / 2.0, f2 / 2.0), 0.0, 0.5) * 2.0;
                           double d2 = Easing.computeEasing(OutEasing.INSTANCE, d);
                           mutableBox.handleDouble5(-0.5 + d2 / 2.0);
                           Render3d.getCuboidQueue().removeIf(cuboid -> cuboid.testObject(blockPos));
                           Render3d.handleCuboid(
                              Cuboid.createBuilder()
                                 .resolveBuilder10(mutableBox)
                                 .resolveBuilder18(BreakHighlightModule.this.box.getValue(), BreakHighlightModule.this.line.getValue())
                                 .resolveBuilder12(BreakHighlightModule.this.width.getValue())
                                 .resolveBuilder6(BreakHighlightModule.this.fade, BreakHighlightModule.this.fadeTime)
                                 .resolveBuilder14(blockPos)
                                 .getCuboid()
                           );
                           break;
                        case Continuous:
                           int i = progress.getIntValue();
                           progress.getFloatAnimator().setFloatValue(i != 0 && i <= 10 ? i / 10.0F : 0.0F);
                           progress.getFloatAnimator().handleFloat3(10.0F, 0.1F);
                           mutableBox.handleDouble5(-0.5 + progress.getFloatAnimator().getFloatValue2() / 2.0);
                           Render3d.handleCuboid(
                              Cuboid.createBuilder()
                                 .resolveBuilder10(mutableBox)
                                 .resolveBuilder18(BreakHighlightModule.this.box.getValue(), BreakHighlightModule.this.line.getValue())
                                 .resolveBuilder12(BreakHighlightModule.this.width.getValue())
                                 .getCuboid()
                           );
                     }
                  }
               }
            }
         }
      );
   }

   private enum Mode {
      Continuous,
      Staged;
   }
}
