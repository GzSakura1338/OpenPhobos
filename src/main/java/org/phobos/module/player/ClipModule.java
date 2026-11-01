package org.phobos.module.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.phobos.api.TextProvider;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.core.placement.DirectionUtils;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.tick.TickUtils;
import org.phobos.module.Module;

public class ClipModule extends Module {
   public final Setting<Boolean> swing;
   public final Setting<Float> angle;
   public final Setting<Boolean> crawl;
   public final Setting<Boolean> elevator;
   public final Setting<Double> extension;
   public final Setting<ClipModule.Sides> sides;
   public final Setting<ClipModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", ClipModule.Mode.Mover));
   public final Setting<ClipModule.Rotate> rotate = this.resolveSetting2(
      new EnumSetting<>("Rotate", ClipModule.Rotate.Simple).resolveSetting3(this.resolveVisibility2(this.mode, ClipModule.Mode.Pearl))
   );
   public final Setting<Boolean> attack;

   public Setting<ClipModule.Rotate> getRotate() {
      return this.rotate;
   }

   @Override
   public void run3() {
      if (mc.player != null && mc.world != null) {
         switch ((ClipModule.Mode)this.mode.getValue()) {
            case Desync:
               double d = 1.0E-7;
               double d2 = Math.cos(Math.toRadians(mc.player.getYaw(1.0F) + 90.0F));
               double d3 = Math.sin(Math.toRadians(mc.player.getYaw(1.0F) + 90.0F));
               mc.player.setPosition(mc.player.getX() + d * d2, mc.player.getY(), mc.player.getZ() + d * d3);
               break;
            case Mover:
               this.phobosClient
                  .getInventoryManager()
                  .handleConsumer2(
                     itemSwitcher -> {
                        Item item = itemSwitcher.resolveItem2(Items.ENDER_PEARL);
                        if (item != null) {
                           Box box3 = mc.player.getBoundingBox().expand(this.extension.getValue());
                           Map<BlockPos, Integer> map = new HashMap<>();
                           BlockPos blockPos3 = mc.player.getBlockPos();

                           for (Direction direction : DirectionUtils.directionArray) {
                              if (!mc.world.getBlockState(blockPos3.offset(direction)).getCollisionShape(mc.world, blockPos3.offset(direction)).isEmpty()) {
                                 BlockPos blockPos = blockPos3.offset(direction);
                                 Box box = new Box(blockPos);
                                 if (box.intersects(box3)) {
                                    map.put(blockPos, this.computeBlockPos(blockPos, mc.world));
                                 }
                              }
                           }

                           Map<BlockPos, Integer> map2 = map.entrySet()
                              .stream()
                              .sorted(Map.Entry.<BlockPos, Integer>comparingByValue().reversed())
                              .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (integer, integer2) -> integer, LinkedHashMap::new));
                           MutableFloat mutableFloat = new MutableFloat(this.angle.getValue());
                           if (BlockUtils.testPlayerEntity(mc.player, mc.world)) {
                              if (this.crawl.getValue() && mc.player.isInSwimmingPose() && !mc.player.isTouchingWater()) {
                                 mutableFloat.setValue(0.0F);
                              } else {
                                 if (!this.elevator.getValue() || mc.player.isInSwimmingPose()) {
                                    return;
                                 }

                                 mutableFloat.setValue(90.0F);
                              }
                           }

                           List<BlockPos> list = new ArrayList<>(4);

                           for (Map.Entry<BlockPos, Integer> entry : map2.entrySet()) {
                              BlockPos blockPos2 = entry.getKey();
                              Box box2 = new Box(blockPos2);
                              if (box2.intersects(box3)) {
                                 list.add(entry.getKey());
                              }
                           }

                           if (!list.isEmpty()) {
                              ItemSwitcher.SwitchResult switchResult = itemSwitcher.resolveSwitchResult3(Hand.MAIN_HAND, item, 1);
                              if (switchResult != null) {
                                 TickUtils.handleClientWorld(
                                    mc.world,
                                    integer -> {
                                       float f = mutableFloat.getValue();
                                       if (list.size() == 1) {
                                          BlockPos blockPos = list.getFirst();
                                          float[] floats = new float[]{RotationUtils.values7(mc.player, blockPos.getX(), blockPos.getY(), blockPos.getZ())[0], f};
                                          this.phobosClient.getRotationManager().handleClientPlayerEntity3(mc.player, floats[0], floats[1]);
                                          this.run();
                                          this.phobosClient.getMovementSimulator().handleClientPlayerEntity(mc.player);
                                          mc.player
                                             .networkHandler
                                             .sendPacket(new PlayerInteractItemC2SPacket(switchResult.hand(), integer, floats[0], floats[1]));
                                          if (this.rotate.getValue().equals(ClipModule.Rotate.Silent)) {
                                             this.phobosClient.getRotationManager().handleClientPlayerEntity5(mc.player);
                                          }

                                          if (this.swing.getValue()) {
                                             mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
                                          }

                                          AutoBlinkModule.getAutoBlinkModule().run();
                                       } else {
                                          BlockPos blockPos2x = BlockUtils.resolveBlockPos(list.getFirst(), list.getLast(), mc.player);
                                          if (blockPos2x != null) {
                                             float[] floats2 = new float[]{
                                                RotationUtils.values7(mc.player, blockPos2x.getX(), blockPos2x.getY(), blockPos2x.getZ())[0], f
                                             };
                                             this.phobosClient.getRotationManager().handleClientPlayerEntity3(mc.player, floats2[0], floats2[1]);
                                             this.run();
                                             this.phobosClient.getMovementSimulator().handleClientPlayerEntity(mc.player);
                                             mc.player
                                                .networkHandler
                                                .sendPacket(new PlayerInteractItemC2SPacket(switchResult.hand(), integer, floats2[0], floats2[1]));
                                             if (this.rotate.getValue().equals(ClipModule.Rotate.Silent)) {
                                                this.phobosClient.getRotationManager().handleClientPlayerEntity5(mc.player);
                                             }

                                             if (this.swing.getValue()) {
                                                mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
                                             }

                                             AutoBlinkModule.getAutoBlinkModule().run();
                                          }
                                       }
                                    }
                                 );
                              }
                           }
                        }
                     }
                  );
               break;
            case Pearl:
               ClipModule.Sides sides = this.sides.getValue();
               Vec3d vec3d = mc.player.getBlockPos().toCenterPos();
               boolean flag = vec3d.x - mc.player.getX() > 0.0;
               boolean flag2 = vec3d.z - mc.player.getZ() > 0.0;
               double d4 = vec3d.x + 0.20000000009497754 * (flag ? -1 : 1);
               double d5 = vec3d.z + 0.2000000000949811 * (flag2 ? -1 : 1);
               mc.player.setPosition(sides.x ? d4 : mc.player.getX(), mc.player.getY(), sides.z ? d5 : mc.player.getZ());
         }

         this.run11();
      }
   }

   public Setting<Double> getExtension() {
      return this.extension;
   }

   public void run() {
      if (mc.player != null) {
         if (this.attack.getValue() && mc.interactionManager != null) {
            Box box = mc.player.getBoundingBox().expand(0.5, 0.5, 0.5);
            assert mc.world != null;

            for (Entity entity : mc.world.getEntities()) {
               if (box.intersects(entity.getBoundingBox()) && entity instanceof ItemFrameEntity) {
                  mc.interactionManager.attackEntity(mc.player, entity);
                  if (this.swing.getValue()) {
                     mc.player.swingHand(Hand.MAIN_HAND);
                  }
               }
            }

            BlockPosUtils.resolveSet3(mc.player, mc.player.getBoundingBox().minY + 0.25).forEach(blockPos -> {
               if (mc.world.getBlockState(blockPos).isOf(Blocks.SCAFFOLDING)) {
                  BlockUtils.handleClientPlayerEntity(mc.player, blockPos, Direction.UP);
               }
            });
         }
      }
   }

   public Setting<Boolean> getElevator() {
      return this.elevator;
   }

   public Setting<Boolean> getAttack() {
      return this.attack;
   }

   public Setting<Boolean> getBooleanValueSetting() {
      return this.crawl;
   }

   public int computeBlockPos(BlockPos blockPos, ClientWorld clientWorld) {
      int i = 0;
      BlockState blockState = clientWorld.getBlockState(blockPos);
      if (blockState.getBlock() == Blocks.BEDROCK) {
         i += 2;
      }

      if (blockState.getBlock() == Blocks.OBSIDIAN) {
         i++;
      }

      if (!clientWorld.getBlockState(blockPos.down()).isReplaceable()) {
         i += 2;
      }

      if (!clientWorld.getBlockState(blockPos.up()).isReplaceable()) {
         i--;
      }

      return i;
   }

   public Setting<ClipModule.Sides> getSides() {
      return this.sides;
   }

   public boolean check() {
      return this.mode.getValue().equals(ClipModule.Mode.Pearl);
   }

   public ClipModule(PhobosClient phobosClient) {
      super(phobosClient, "Clip", Categories.player, "Clips you into blocks nearby");
      this.extension = this.resolveSetting2(
         new NumberSetting<>("Extension", 0.2, 0.01, 0.3).resolveSetting7("Colliding hitbox extension").resolveSetting4(this.mode, ClipModule.Mode.Pearl)
      );
      this.angle = this.resolveSetting2(new NumberSetting<>("Angle", 82.8F, -90.0F, 90.0F).resolveSetting4(this.mode, ClipModule.Mode.Pearl));
      this.attack = this.resolveSetting2(
         new BooleanSetting("Attack").resolveSetting7("Breaks blocking entities").resolveSetting4(this.mode, ClipModule.Mode.Pearl)
      );
      this.swing = this.resolveSetting2(new BooleanSetting("Swing").resolveSetting7("Hand swing").resolveSetting4(this.mode, ClipModule.Mode.Pearl));
      this.crawl = this.resolveSetting2(new BooleanSetting("Crawl").resolveSetting7("Crawl phase").resolveSetting4(this.mode, ClipModule.Mode.Pearl));
      this.elevator = this.resolveSetting2(
         new BooleanSetting("Elevator").resolveSetting4(this.mode, ClipModule.Mode.Pearl).resolveSetting7("Allows you to clip upwards when phased")
      );
      this.sides = this.resolveSetting2(new EnumSetting<>("Sides", ClipModule.Sides.Both).resolveSetting4(this.mode, ClipModule.Mode.Desync));
      ModuleListenerUtils.handleModule(this);
   }

   public Setting<Float> getAngle() {
      return this.angle;
   }

   public Setting<ClipModule.Mode> getModeSetting() {
      return this.mode;
   }

   public Setting<Boolean> getSwing() {
      return this.swing;
   }

   private enum Mode implements TextProvider {
      Mover("Moves you forward"),
      Pearl("Throws an enderpearl"),
      Desync("Hitbox desync or something");

      public final String text;

      Mode(final String description) {
         this.text = description;
      }

      @Override
      public String getText() {
         return this.text;
      }
   }

   private enum Rotate {
      Simple,
      Silent;
   }

   private enum Sides {
      Both(true, true),
      X(true, false),
      Z(false, true);

      public final boolean x;
      public final boolean z;

      public boolean isX() {
         return this.x;
      }

      public boolean isZ() {
         return this.z;
      }

      Sides(final boolean x, final boolean z) {
         this.x = x;
         this.z = z;
      }
   }
}
