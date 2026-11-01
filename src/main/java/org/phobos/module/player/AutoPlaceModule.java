package org.phobos.module.player;

import java.util.Set;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.ItemSetSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.DrawBlockOutlineEvent;
import org.phobos.event.SafeListener;
import org.phobos.type.KeyboardMouseEnum;
import org.phobos.module.Module;

public class AutoPlaceModule extends Module {
   public final Setting<Long> delay;
   public final Setting<Double> frame = this.resolveSetting2(new NumberSetting<>("Frame", 8.0, 0.0, 30.0));
   public final Setting<Boolean> any;
   public HitResult hitResult;
   public final TimeGate.Normal normal;
   public BlockPos blockPos;
   public final Setting<Set<Item>> whitelist;
   public int intValue;

   public Hand resolveHand(ClientPlayerEntity clientPlayerEntity) {
      ItemStack itemStack = clientPlayerEntity.getMainHandStack();
      ItemStack itemStack2 = clientPlayerEntity.getOffHandStack();
      if (itemStack.isEmpty() && itemStack2.isEmpty()) {
         return Hand.MAIN_HAND;
      } else {
         return itemStack.getItem() instanceof BlockItem ? Hand.MAIN_HAND : (itemStack2.getItem() instanceof BlockItem ? Hand.OFF_HAND : Hand.MAIN_HAND);
      }
   }

   public AutoPlaceModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoPlace", Categories.player, "Simulates legit fast place");
      this.delay = this.resolveSetting2(new LongNumberSetting("Delay", 60L, 1L, 500L, true));
      this.whitelist = this.resolveSetting2(new ItemSetSetting("Whitelist"));
      this.any = this.resolveSetting2(new BooleanSetting("Any"));
      this.normal = new TimeGate.Normal();
      this.handleListener(
         new SafeListener<DrawBlockOutlineEvent>() {
            public void handleObject(
               DrawBlockOutlineEvent drawBlockOutlineEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (mc.currentScreen == null && !clientPlayerEntity.getAbilities().flying) {
                  ItemStack itemStack = clientPlayerEntity.getMainHandStack();
                  ItemStack itemStack2 = clientPlayerEntity.getOffHandStack();
                  if (!itemStack.isEmpty() || !itemStack2.isEmpty()) {
                     if (AutoPlaceModule.this.whitelist.getValue().contains(itemStack.getItem())
                        || AutoPlaceModule.this.whitelist.getValue().contains(itemStack2.getItem())
                        || AutoPlaceModule.this.any.getValue()) {
                        HitResult hitResult = mc.crosshairTarget;
                        if (hitResult instanceof BlockHitResult blockHitResult) {
                           if (!blockHitResult.getSide().equals(Direction.UP) && !blockHitResult.getSide().equals(Direction.DOWN)) {
                              if (AutoPlaceModule.this.hitResult != null && AutoPlaceModule.this.intValue < AutoPlaceModule.this.frame.getValue()) {
                                 AutoPlaceModule.this.intValue++;
                              } else {
                                 AutoPlaceModule.this.hitResult = hitResult;
                                 BlockPos blockPos = blockHitResult.getBlockPos();
                                 if (AutoPlaceModule.this.blockPos == null
                                    || blockPos.getX() != AutoPlaceModule.this.blockPos.getX()
                                    || blockPos.getY() != AutoPlaceModule.this.blockPos.getY()
                                    || blockPos.getZ() != AutoPlaceModule.this.blockPos.getZ()) {
                                    BlockState blockState = clientWorld.getBlockState(blockPos);
                                    if (blockState.isAir() || !blockState.getFluidState().isEmpty()) {
                                       return;
                                    }

                                    if (!phobosClient.getBindManager().testKeyboardMouseEnum(KeyboardMouseEnum.Mouse, 1)) {
                                       return;
                                    }

                                    if (AutoPlaceModule.this.normal.testNumber2(AutoPlaceModule.this.delay.getValue())) {
                                       Hand hand = AutoPlaceModule.this.resolveHand(clientPlayerEntity);
                                       if (clientPlayerInteractionManager.interactBlock(clientPlayerEntity, hand, blockHitResult).isAccepted()) {
                                          mc.options.rightKey.setPressed(true);
                                          clientPlayerEntity.swingHand(hand);
                                          mc.options.rightKey.setPressed(false);
                                          AutoPlaceModule.this.blockPos = blockPos;
                                          AutoPlaceModule.this.intValue = 0;
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      );
   }
}
