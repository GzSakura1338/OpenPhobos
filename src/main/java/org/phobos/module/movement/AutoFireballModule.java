package org.phobos.module.movement;

import java.util.function.Consumer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.phobos.core.placement.ShapeDirectionResolver;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.tick.TickUtils;
import org.phobos.core.inventory.ItemSwitcher.SwitchResult;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class AutoFireballModule extends Module {
   public final Setting<AutoFireballModule.Rotate> rotate = this.resolveSetting2(new EnumSetting<>("Rotate", AutoFireballModule.Rotate.Normal));
   public final Setting<Float> pitch = this.resolveSetting2(new NumberSetting<>("Pitch", 90.0F, -90.0F, 90.0F));

   public void handleBlockPos(BlockPos blockPos, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Consumer<BlockHitResult> consumer) {
      Direction direction = ShapeDirectionResolver.shapeDirectionResolver.resolveDirection(blockPos, clientPlayerEntity, clientWorld);
      if (direction != null) {
         Vec3d vec3d = new Vec3d(
            blockPos.getX() + 0.5 - direction.getOffsetX() * 0.5 + direction.getOffsetX(),
            blockPos.getY() + 0.5 - direction.getOffsetY() * 0.5 + direction.getOffsetY(),
            blockPos.getZ() + 0.5 - direction.getOffsetZ() * 0.5 + direction.getOffsetZ()
         );
         BlockHitResult blockHitResult = new BlockHitResult(vec3d, direction, blockPos, false);
         consumer.accept(blockHitResult);
      }
   }

   public AutoFireballModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoFireball", Categories.movement, "Uses a firework");
      this.handleListener(
         new SafeListener<PlayerTickEvent>(3) {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               phobosClient.getInventoryManager()
                  .handleConsumer2(
                     itemSwitcher -> {
                        if (itemSwitcher.testItem2(Items.FIRE_CHARGE)
                           && (!RotationManager.isFlag() || AutoFireballModule.this.rotate.getValue().equals(AutoFireballModule.Rotate.Silent))) {
                           SwitchResult switchResult = itemSwitcher.resolveSwitchResult3(Hand.MAIN_HAND, Items.FIRE_CHARGE, 3);
                           if (switchResult != null) {
                              switch ((AutoFireballModule.Rotate)AutoFireballModule.this.rotate.getValue()) {
                                 case Silent:
                                    phobosClient.getRotationManager()
                                       .testFloat(clientPlayerEntity.getYaw() + 180.0F, AutoFireballModule.this.pitch.getValue());
                                    break;
                                 case Normal:
                                    phobosClient.getRotationManager()
                                       .handleClientPlayerEntity3(
                                          clientPlayerEntity, clientPlayerEntity.getYaw() + 180.0F, AutoFireballModule.this.pitch.getValue()
                                       );
                              }

                              TickUtils.handleClientWorld(
                                 clientWorld,
                                 integer -> clientPlayerEntity.networkHandler
                                    .sendPacket(
                                       new PlayerInteractItemC2SPacket(
                                          Hand.MAIN_HAND, integer, clientPlayerEntity.getYaw() + 180.0F, AutoFireballModule.this.pitch.getValue()
                                       )
                                    )
                              );
                              if (AutoFireballModule.this.rotate.getValue().equals(AutoFireballModule.Rotate.Silent)) {
                                 phobosClient.getRotationManager().handleClientPlayerEntity5(clientPlayerEntity);
                              }

                              AutoFireballModule.this.run11();
                           }
                        }
                     }
                  );
            }
         }
      );
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }

   private enum Rotate {
      Normal,
      Silent;
   }
}
