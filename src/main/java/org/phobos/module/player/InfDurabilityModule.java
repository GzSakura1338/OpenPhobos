package org.phobos.module.player;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import org.phobos.api.TextProvider;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.MultiSelectSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.event.InfDurabilityBreakBlockListener;
import org.phobos.event.InfDurabilityAttackListener;
import org.phobos.module.Module;

public class InfDurabilityModule extends Module {
   public final Setting<Map<InfDurabilityModule.Type, Boolean>> selection = this.resolveSetting2(
      new MultiSelectSetting<>("Selection", InfDurabilityModule.Type.class).resolveMultiSelectSetting5(multiSelectSetting -> {
         for (InfDurabilityModule.Type type : InfDurabilityModule.Type.values()) {
            multiSelectSetting.resolveMultiSelectSetting2(type, type.name(), true);
         }
      })
   );

   public void handleClientPlayerEntity(
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager,
      InfDurabilityModule.Type type,
      BlockPos blockPos
   ) {
      ItemStack itemStack = clientPlayerEntity.getMainHandStack();
      if (!itemStack.isEmpty() && itemStack.isDamageable()) {
         if (this.selection.getValue().containsKey(type)) {
            Slot slot = clientPlayerEntity.playerScreenHandler.getSlot(this.computeSupplier(() -> PlayerActionUtils.value() % 8 + 1));
            SilentHotbarModule.getSilentHotbarManager().handleObject3(this, slot, 2);
         }
      }
   }

   public int computeSupplier(Supplier<Integer> supplier) {
      AtomicInteger atomicInteger = new AtomicInteger(-1);
      this.phobosClient.getInventoryManager().handleConsumer2(itemSwitcher -> itemSwitcher.resolveObject(slot -> {
         if (atomicInteger.get() == -1) {
            return null;
         }

         if (!itemSwitcher.testSlot4(slot)) {
            return null;
         }

         if (slot.getStack().isEmpty() || !slot.getStack().isDamageable()) {
            atomicInteger.set(slot.id);
         }

         return null;
      }));
      int i = atomicInteger.get();
      return i == -1 ? supplier.get() : i;
   }

   public InfDurabilityModule(PhobosClient phobosClient) {
      super(phobosClient, "InfDurability", Categories.player, "Abuses minecraft attributes logic");
      this.handleListener(new InfDurabilityAttackListener(this));
      this.handleListener(new InfDurabilityBreakBlockListener(this));
      ModuleGroups.getINSTANCE().handleText("beta", this);
   }

   public enum Type implements TextProvider {
      Attack("Triggers when attacking an entity"),
      Break("Triggers when a block is broken");

      public final String text;

      @Override
      public String getText() {
         return this.text;
      }

      Type(final String description) {
         this.text = description;
      }
   }
}
