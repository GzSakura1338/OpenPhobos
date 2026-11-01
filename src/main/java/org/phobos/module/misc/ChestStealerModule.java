package org.phobos.module.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.AbstractChestBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.api.TextProvider;
import org.phobos.api.AreaScanner;
import org.phobos.api.TimeGate;
import org.phobos.api.NumberRange;
import org.phobos.api.NumberRange;
import org.phobos.api.AreaScanner.Area;
import org.phobos.core.placement.ShapeDirectionResolver;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.MutableLongSetting;
import org.phobos.core.setting.ItemSetSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.core.inventory.ItemUtils;
import org.phobos.core.module.Categories;
import org.phobos.core.math.MathUtils;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.player.PlayerUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.inventory.InventoryUtils;
import org.phobos.core.player.InteractionManager.Interaction;
import org.phobos.event.InitEvent;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.module.Module;

public class ChestStealerModule extends Module implements AreaScanner {
   public final Setting<Boolean> close;
   public final TimeGate.Normal normal;
   public final Setting<Boolean> rotate;
   public final Setting<ChestStealerModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", ChestStealerModule.Mode.Rage));
   public final Setting<ChestStealerModule.Filter> filter;
   public final TimeGate.Normal normal2;
   public final Setting<Boolean> open = this.resolveSetting2(new BooleanSetting("Open"));
   public final Setting<Boolean> reset;
   public final Setting<Long> timeout = this.resolveSetting2(new LongNumberSetting("Timeout", 120000L, 0L, 300000L));
   public final Setting<NumberRange.Mutable<Long>> delay;
   public final Setting<ChestStealerModule.Rotate> rotateType;
   public final Setting<Set<Item>> whitelist;
   public long longValue;
   public final Setting<Double> range;
   public final Setting<Boolean> move = this.resolveSetting2(new BooleanSetting("Move"));
   public final Map<BlockPos, Long> blockPosMap;
   public final Setting<Boolean> normal3;

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   public ChestStealerModule(PhobosClient phobosClient) {
      super(phobosClient, "ChestStealer", Categories.misc, "Takes items out of containers");
      this.range = this.resolveSetting2(new NumberSetting<>("Range", 5.0, 0.1, 6.0));
      this.rotate = this.resolveSetting2(new BooleanSetting("Rotate"));
      this.rotateType = this.resolveSetting2(new EnumSetting<>("RotateType", ChestStealerModule.Rotate.Normal));
      this.delay = this.resolveSetting2(new MutableLongSetting("Delay", NumberRange.createMutable(50L, 50L), 0L, 500L, 0L, true));
      this.close = this.resolveSetting2(new BooleanSetting("Close", false));
      this.reset = this.resolveSetting2(new BooleanSetting("Reset").resolveSetting7("Resets delay on container open"));
      this.normal3 = this.resolveSetting2(new BooleanSetting("Normal", true));
      this.filter = this.resolveSetting2(new EnumSetting<>("Filter", ChestStealerModule.Filter.None));
      this.whitelist = this.resolveSetting2(new ItemSetSetting("Whitelist").resolveSetting4(this.filter, ChestStealerModule.Filter.Single));
      this.blockPosMap = new ConcurrentHashMap<>();
      this.normal = new TimeGate.Normal();
      this.normal2 = new TimeGate.Normal();
      this.createBuilder()
         .resolveBuilder6(this.open)
         .resolveBuilder5(this.resolveSettingGroup(this.rotate, this.rotateType))
         .resolveBuilder(this.timeout, this.range, this.move)
         .resolveBuilder3(this.resolveVisibility2(this.mode, ChestStealerModule.Mode.Rage))
         .getSettingSubsection();
      this.handleListener(
         new SafeListener<InitEvent<?>>() {
            public void handleObject(
               InitEvent<?> initEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (ChestStealerModule.this.reset.getValue()) {
                  if (initEvent.getScreen() instanceof GenericContainerScreen) {
                     ChestStealerModule.this.normal.run();
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
               phobosClient.getInventoryManager()
                  .handleConsumer2(
                     itemSwitcher -> {
                        if (clientPlayerEntity.currentScreenHandler instanceof GenericContainerScreenHandler genericContainerScreenHandler) {
                           if (ChestStealerModule.this.normal3.getValue() && genericContainerScreenHandler.getInventory().size() != 27) {
                              return;
                           }

                           if (ChestStealerModule.this.testGenericContainerScreenHandler(genericContainerScreenHandler)) {
                              return;
                           }

                           if (ChestStealerModule.this.close.getValue()
                              && ChestStealerModule.this.testItemSwitcher(itemSwitcher, genericContainerScreenHandler)) {
                              switch ((ChestStealerModule.Mode)ChestStealerModule.this.mode.getValue()) {
                                 case Rage:
                                 case Legit:
                                    clientPlayerEntity.closeHandledScreen();
                                    break;
                                 case Fast:
                                    if (ChestStealerModule.this.normal2.testLong(ChestStealerModule.this.longValue / 2L)) {
                                       clientPlayerEntity.closeHandledScreen();
                                       ChestStealerModule.this.longValue = MathUtils.computeInt5(545, 945);
                                    }
                              }

                              return;
                           }

                           List<Slot> list = ChestStealerModule.this.resolveList(genericContainerScreenHandler);
                           List<Slot> list2 = new ArrayList<>(list);

                           for (Slot slot : list) {
                              list2.remove(slot);
                              if (slot.hasStack() && !ChestStealerModule.this.testItemSwitcher2(itemSwitcher, slot)) {
                                 ItemStack itemStack = slot.getStack();
                                 Item item = itemStack.getItem();
                                 if (!ItemUtils.testItem(item)
                                    || !ItemUtils.testList(list2, item, itemStack, slot)
                                       && !ItemUtils.testItemSwitcher5(itemSwitcher, item, itemStack)
                                       && !ItemUtils.testItemSwitcher2(itemSwitcher, itemStack, slot)) {
                                    switch ((ChestStealerModule.Mode)ChestStealerModule.this.mode.getValue()) {
                                       case Rage:
                                          if (ChestStealerModule.this.normal.testSetting(ChestStealerModule.this.delay)) {
                                             clientPlayerInteractionManager.clickSlot(
                                                clientPlayerEntity.currentScreenHandler.syncId, slot.id, 0, SlotActionType.QUICK_MOVE, clientPlayerEntity
                                             );
                                          }
                                          break;
                                       case Fast:
                                          if (ChestStealerModule.this.normal.testLong2(ChestStealerModule.this.longValue)) {
                                             clientPlayerInteractionManager.clickSlot(
                                                clientPlayerEntity.currentScreenHandler.syncId, slot.id, 0, SlotActionType.QUICK_MOVE, clientPlayerEntity
                                             );
                                             ChestStealerModule.this.longValue = MathUtils.computeInt5(150, 300);
                                             ChestStealerModule.this.normal2.run();
                                          }
                                          break;
                                       case Legit:
                                          if (ChestStealerModule.this.normal.testLong2(ChestStealerModule.this.longValue)) {
                                             clientPlayerInteractionManager.clickSlot(
                                                clientPlayerEntity.currentScreenHandler.syncId, slot.id, 0, SlotActionType.QUICK_MOVE, clientPlayerEntity
                                             );
                                             ChestStealerModule.this.longValue = MathUtils.computeInt5(50, 160);
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
      );
      this.handleListener(
         new SafeListener<PlayerTickEvent>() {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               ChestStealerModule.this.blockPosMap
                  .entrySet()
                  .removeIf(entry -> entry.getValue() + ChestStealerModule.this.timeout.getValue() < System.currentTimeMillis());
               if (mc.currentScreen == null
                  && ChestStealerModule.this.mode.getValue().equals(ChestStealerModule.Mode.Rage)
                  && ChestStealerModule.this.open.getValue()) {
                  if (ChestStealerModule.this.move.getValue() || PlayerUtils.check5()) {
                     Area area = new Area();
                     area.handleVec3d(
                        clientPlayerEntity.getEyePos(),
                        ChestStealerModule.this.range.getValue(),
                        blockPosx -> clientWorld.getBlockState(blockPosx).getBlock() instanceof AbstractChestBlock
                           && !ChestStealerModule.this.blockPosMap.containsKey(blockPosx)
                     );
                     MutableObject<Boolean> mutableObject = new MutableObject(false);

                     for (BlockPos blockPos : area) {
                        phobosClient.getInteractionManager()
                           .handleBlockPos2(
                              blockPos,
                              clientPlayerEntity,
                              clientWorld,
                              ShapeDirectionResolver.shapeDirectionResolver,
                              blockHitResult -> {
                                 if (ChestStealerModule.this.rotate.getValue()) {
                                    float[] floats = RotationUtils.values2(clientPlayerEntity, blockHitResult.getPos());
                                    switch ((ChestStealerModule.Rotate)ChestStealerModule.this.rotateType.getValue()) {
                                       case Packet:
                                          if (!phobosClient.getRotationManager().testFloat(floats[0], floats[1])) {
                                             return;
                                          }
                                          break;
                                       case Normal:
                                       case Grim:
                                          phobosClient.getRotationManager().handleClientPlayerEntity3(clientPlayerEntity, floats[0], floats[1]);
                                    }
                                 }

                                 Interaction.INSTANCE.handleClientPlayerEntity(clientPlayerEntity, clientWorld, Hand.MAIN_HAND, blockHitResult);
                                 if (ChestStealerModule.this.rotate.getValue()
                                    && ChestStealerModule.this.rotateType.getValue().equals(ChestStealerModule.Rotate.Grim)) {
                                    phobosClient.getRotationManager().handleClientPlayerEntity5(clientPlayerEntity);
                                 }

                                 mutableObject.setValue(true);
                              }
                           );
                        if ((Boolean)mutableObject.getValue()) {
                           ChestStealerModule.this.blockPosMap.put(blockPos, System.currentTimeMillis());
                           break;
                        }
                     }
                  }
               }
            }
         }
      );
      ModuleListenerUtils.handleListenerHolder4(this, this.blockPosMap::clear);
   }

   public boolean testItemSwitcher(ItemSwitcher itemSwitcher, GenericContainerScreenHandler genericContainerScreenHandler) {
      List<Slot> list = this.resolveList(genericContainerScreenHandler);

      for (Slot slot : list) {
         if (slot.hasStack() && !this.testItemSwitcher2(itemSwitcher, slot)) {
            ItemStack itemStack = slot.getStack();
            Item item = itemStack.getItem();
            if (!ItemUtils.testItem(item) || !ItemUtils.testList(list, item, itemStack, slot) && !ItemUtils.testList2(list, itemStack, slot)) {
               return false;
            }
         }
      }

      return true;
   }

   public List<Slot> resolveList(GenericContainerScreenHandler genericContainerScreenHandler) {
      return InventoryUtils.resolveList2(genericContainerScreenHandler);
   }

   public boolean testItemSwitcher2(ItemSwitcher itemSwitcher, Slot slot) {
      return this.filter.testObject2(ChestStealerModule.Filter.None) ? true : ItemUtils.testItemSwitcher3(itemSwitcher, slot, this.whitelist);
   }

   public boolean testGenericContainerScreenHandler(GenericContainerScreenHandler genericContainerScreenHandler) {
      for (int i = 0; i < 9; i++) {
         if (genericContainerScreenHandler.getSlot(i).getStack().getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof PaneBlock) {
            return true;
         }
      }

      return false;
   }

   private enum Filter implements TextProvider {
      None("No filter"),
      Single("Imports default SkyWars selection");

      public final String text;

      Filter(final String description) {
         this.text = description;
      }

      @Override
      public String getText() {
         return this.text;
      }
   }

   private enum Mode {
      Rage,
      Legit,
      Fast;
   }

   private enum Rotate {
      Normal,
      Packet,
      Grim;
   }
}
