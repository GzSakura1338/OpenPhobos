package org.phobos.module.player;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.item.PotionItem;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.inventory.InventoryUtils;
import org.phobos.event.FastEatSendPlayerInteractItemListener;
import org.phobos.event.FastEatPlayerMoveListener;
import org.phobos.event.FastEatUnusedListener;
import org.phobos.event.FastEatSendPlayerActionListener;
import org.phobos.event.FastEatPlayerTickListener;
import org.phobos.module.Module;

public class FastEatModule extends Module {
   public final Setting<FastEatModule.Mode> mode = new EnumSetting<>("Mode", FastEatModule.Mode.Packet).resolveSetting6(this);
   public final Setting<Boolean> cancel;
   public final Setting<Float> speed = new NumberSetting<>("Speed", 15.0F, 1.0F, 25.0F).resolveSetting6(this);

   public FastEatModule(PhobosClient phobosClient) {
      super(phobosClient, "FastEat", Categories.player, "Exploits that make you fat");
      this.cancel = new BooleanSetting("Cancel").resolveSetting6(this);
      this.handleListener(new FastEatSendPlayerActionListener(this));
      this.handleListener(new FastEatSendPlayerInteractItemListener(this));
      this.handleListener(new FastEatPlayerTickListener(this));
      this.handleListener(new FastEatUnusedListener(this));
      this.handleListener(new FastEatPlayerMoveListener(this));
   }

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ItemStack itemStack) {
      return itemStack != null
         && clientPlayerEntity.isUsingItem()
         && (InventoryUtils.testItem3(itemStack.getItem()) || itemStack.getItem() instanceof PotionItem || itemStack.getItem() instanceof MilkBucketItem);
   }

   public enum Mode {
      Fast,
      Update,
      Packet,
      Grim;
   }
}
