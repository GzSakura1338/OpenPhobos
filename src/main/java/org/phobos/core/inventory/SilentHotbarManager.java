package org.phobos.core.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.Fluent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.mixin.game.render.interfaces.IHeldItemRenderer;
import org.phobos.core.ClientHolder;
import org.phobos.core.Feature;
import org.phobos.core.math.MathUtils;
import org.phobos.core.PhobosClient;

public class SilentHotbarManager extends Feature implements Fluent<SilentHotbarManager> {
   public final PhobosClient phobosClient;
   public static SilentHotbarManager silentHotbarManager = new SilentHotbarManager(ClientHolder.getPhobosClient());
   public int intValue;
   public boolean flag;
   public boolean flag2;
   public SilentHotbarManager.SilentHotbarState silentHotbarState = null;

   public PhobosClient getPhobosClient() {
      return this.phobosClient;
   }

   public void handleObject(@Nullable Object object, int i) {
      if (this.silentHotbarState != null && Objects.equals(object, this.silentHotbarState.object)) {
         this.silentHotbarState.intValue = i;
         this.intValue = 0;
      }
   }

   public SilentHotbarManager get() {
      return this;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public SilentHotbarManager(PhobosClient phobosClient) {
      this.intValue = 0;
      this.flag2 = false;
      this.phobosClient = phobosClient;
      this.handleListener(
         new SafeListener<TickEvent2>(1001) {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (SilentHotbarManager.this.silentHotbarState != null) {
                  if (SilentHotbarManager.this.intValue >= SilentHotbarManager.this.silentHotbarState.intValue) {
                     if (SilentHotbarManager.this.silentHotbarState.getSelectionConfig() != null) {
                        SilentHotbarManager.this.silentHotbarState.getSelectionConfig().getRunnableList3().forEach(Runnable::run);
                        SilentHotbarManager.this.silentHotbarState.getMutableBoolean().setTrue();
                     }

                     SilentHotbarManager.this.silentHotbarState = null;
                     SilentHotbarManager.this.run();
                  } else {
                     if (SilentHotbarManager.this.silentHotbarState.flag3 && SilentHotbarManager.this.silentHotbarState.flag) {
                        phobosClient.getInventoryManager().handleConsumer2(itemSwitcher -> {
                           if (itemSwitcher.check()) {
                              itemSwitcher.handleInt5(SilentHotbarManager.this.silentHotbarState.slot.getIndex(), 0, SlotActionType.QUICK_MOVE);
                              SilentHotbarManager.this.silentHotbarState.flag = false;
                           } else {
                              int i = clientPlayerEntity.getInventory().selectedSlot % 8 + 1;
                              itemSwitcher.handleInt5(SilentHotbarManager.this.silentHotbarState.slot.getIndex(), i, SlotActionType.SWAP);
                              SilentHotbarManager.this.silentHotbarState.flag = false;
                           }
                        });
                     }

                     SilentHotbarManager.this.intValue++;
                  }
               }
            }
         }
      );
      ClientHolder.getEventBus().handleObject(this);
   }

   public void run() {
      this.flag = true;
   }

   public boolean check() {
      return this.silentHotbarState != null;
   }

   public int value() {
      if (this.flag2 || this.silentHotbarState == null || this.silentHotbarState.flag3 && this.silentHotbarState.flag) {
         return mc.player != null && mc.player.getInventory() != null ? mc.player.getInventory().selectedSlot : 0;
      }

      if (this.silentHotbarState.selectionConfig != null && !this.silentHotbarState.selectionConfig.getRunnableList().isEmpty()) {
         this.flag2 = true;
         this.silentHotbarState.selectionConfig.getRunnableList().forEach(Runnable::run);
         this.silentHotbarState.selectionConfig.getRunnableList().clear();
         this.flag2 = false;
      }

      int i = this.silentHotbarState.slot.getIndex();
      return MathUtils.computeInt3(i, 0, 8);
   }

   public int value2() {
      if (this.silentHotbarState == null || this.silentHotbarState.flag3 && this.silentHotbarState.flag) {
         return mc.player != null && mc.player.getInventory() != null ? mc.player.getInventory().selectedSlot : 0;
      } else {
         return this.silentHotbarState.intValue2;
      }
   }

   public boolean isFlag2() {
      return this.flag2;
   }

   public SilentHotbarManager.SilentHotbarState getSilentHotbarState() {
      return this.silentHotbarState;
   }

   public int computeInt(int i) {
      if (this.silentHotbarState != null && this.silentHotbarState.selectionConfig != null && this.silentHotbarState.selectionConfig.isFlag()) {
         int i2 = this.silentHotbarState.slot.getIndex();
         return MathUtils.computeInt3(i2, 0, 8);
      } else {
         return i;
      }
   }

   public boolean testObject(@Nullable Object object) {
      return this.silentHotbarState != null && Objects.equals(object, this.silentHotbarState.object);
   }

   public void handleObject2(@Nullable Object object) {
      if (this.silentHotbarState != null && Objects.equals(object, this.silentHotbarState.object)) {
         this.silentHotbarState = null;
         this.run();
      }
   }

   public int getIntValue() {
      return this.intValue;
   }

   public void handleObject3(@Nullable Object object, Slot slot, int i, SilentHotbarManager.SelectionConfig... selectionConfigs) {
      if (this.silentHotbarState != null
         && !Objects.equals(object, this.silentHotbarState.object)
         && this.silentHotbarState.getSelectionConfig() != null
         && !this.silentHotbarState.getMutableBoolean().getValue()) {
         this.silentHotbarState.getSelectionConfig().getRunnableList2().forEach(Runnable::run);
      }

      boolean flag = !ItemSwitcher.testSlot7(slot);
      this.silentHotbarState = new SilentHotbarManager.SilentHotbarState(
         slot, object, i, this.value2(), flag, selectionConfigs.length == 0 ? null : selectionConfigs[0]
      );
      this.silentHotbarState.setFlag(flag);
      this.intValue = 0;
   }

   public void run2() {
      if (this.flag) {
         IHeldItemRenderer iHeldItemRenderer = (IHeldItemRenderer)(Object)mc.gameRenderer.firstPersonRenderer;
         iHeldItemRenderer.setEquipProgressMainHand(1.0F);
         this.flag = false;
      }
   }

   public SilentHotbarManager.SelectionConfig.Builder getBuilder() {
      return SilentHotbarManager.SelectionConfig.createBuilder();
   }

   public static class SelectionConfig {
      public final List<Runnable> runnableList;
      public final List<Runnable> runnableList2;
      public boolean flag;
      public final List<Runnable> runnableList3 = new ArrayList<>();

      public List<Runnable> getRunnableList3() {
         return this.runnableList3;
      }

      public List<Runnable> getRunnableList() {
         return this.runnableList;
      }

      public boolean isFlag() {
         return this.flag;
      }

      public List<Runnable> getRunnableList2() {
         return this.runnableList2;
      }

      public void setFlag(boolean flag) {
         this.flag = flag;
      }

      public static SilentHotbarManager.SelectionConfig.Builder createBuilder() {
         return new SilentHotbarManager.SelectionConfig.Builder();
      }

      public SelectionConfig() {
         this.runnableList = new ArrayList<>();
         this.runnableList2 = new ArrayList<>();
         this.flag = false;
      }

      public static class Builder {
         public final SilentHotbarManager.SelectionConfig selectionConfig = new SilentHotbarManager.SelectionConfig();

         public SilentHotbarManager.SelectionConfig.Builder resolveBuilder(Runnable runnable) {
            this.selectionConfig.getRunnableList().add(runnable);
            return this;
         }

         public SilentHotbarManager.SelectionConfig.Builder resolveBuilder2(Runnable runnable) {
            this.selectionConfig.getRunnableList2().add(runnable);
            return this;
         }

         public SilentHotbarManager.SelectionConfig.Builder resolveBuilder3(boolean flag) {
            this.selectionConfig.setFlag(flag);
            return this;
         }

         public SilentHotbarManager.SelectionConfig getSelectionConfig() {
            return this.selectionConfig;
         }

         public SilentHotbarManager.SelectionConfig.Builder resolveBuilder4(Runnable runnable) {
            this.selectionConfig.getRunnableList3().add(runnable);
            return this;
         }
      }
   }

   private static class SilentHotbarState {
      @Nullable
      public final SilentHotbarManager.SelectionConfig selectionConfig;
      public int intValue;
      public final int intValue2;
      public final Slot slot;
      public final AtomicInteger atomicInteger;
      public boolean flag;
      public boolean flag2 = false;
      @Nullable
      public final Object object;
      public final MutableBoolean mutableBoolean = new MutableBoolean();
      public final boolean flag3;

      public boolean isFlag2() {
         return this.flag2;
      }

      public void setFlag2(boolean flag) {
         this.flag2 = flag;
      }

      public MutableBoolean getMutableBoolean() {
         return this.mutableBoolean;
      }

      public int getIntValue() {
         return this.intValue;
      }

      @Nullable
      public SilentHotbarManager.SelectionConfig getSelectionConfig() {
         return this.selectionConfig;
      }

      public Slot getSlot() {
         return this.slot;
      }

      public AtomicInteger getAtomicInteger() {
         return this.atomicInteger;
      }

      public void setIntValue(int i) {
         this.intValue = i;
      }

      public SilentHotbarState(Slot slot, @Nullable Object object, int i, int i2, boolean flag, SilentHotbarManager.SelectionConfig selectionConfig) {
         this.atomicInteger = new AtomicInteger();
         this.slot = slot;
         this.object = object;
         this.intValue = i;
         this.intValue2 = i2;
         this.flag3 = flag;
         this.selectionConfig = selectionConfig;
      }

      public int getIntValue2() {
         return this.intValue2;
      }

      public void setFlag(boolean flag) {
         this.flag = flag;
      }

      @Nullable
      public Object getObject() {
         return this.object;
      }

      public boolean isFlag() {
         return this.flag;
      }

      public boolean isFlag3() {
         return this.flag3;
      }
   }
}
