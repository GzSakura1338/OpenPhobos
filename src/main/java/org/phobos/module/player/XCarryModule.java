package org.phobos.module.player;

import java.util.List;
import java.util.Set;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Range;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.ItemSetSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.XCarrySendCloseHandledScreenListener;
import org.phobos.event.XCarryUnusedListener;
import org.phobos.module.Module;
import org.phobos.module.client.AntiCheatModule;

public class XCarryModule extends Module {
   public static XCarryModule xCarryModule;
   public final Setting<Set<Item>> items;
   public final Setting<Boolean> armor = this.resolveSetting2(new BooleanSetting("Armor").resolveSetting7("Unlocks armor slots"));
   public final Setting<XCarryModule.Mode> mode = new EnumSetting<>("Mode", XCarryModule.Mode.Default).resolveSetting6(this);
   public final TimeGate.Normal normal;
   public final Setting<Long> delay = new LongNumberSetting("Delay", 500L, 0L, 1000L, true)
      .resolveSetting3(this.resolveVisibility2(this.mode, XCarryModule.Mode.Auto))
      .resolveSetting6(this);

   public XCarryModule(PhobosClient phobosClient) {
      super(phobosClient, "XCarry", Categories.player, "Allows you to carry more items");
      this.items = new ItemSetSetting("Items", Items.END_CRYSTAL)
         .resolveSetSetting(4)
         .resolveSetting3(this.resolveVisibility2(this.mode, XCarryModule.Mode.Auto))
         .resolveSetting7("Items to put in crafting slots")
         .resolveSetting6(this);
      this.normal = new TimeGate.Normal();
      this.handleListener(new XCarrySendCloseHandledScreenListener(this));
      this.handleListener(new XCarryUnusedListener(this));
      xCarryModule = this;
   }

   public static boolean check() {
      return xCarryModule.check7() && xCarryModule.armor.getValue();
   }

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   @Override
   public void run3() {
      if (AntiCheatModule.getAntiCheatModule().getInventory().getValue().isStrict()) {
         this.handleText7(Text.literal("XCarry is not available, uncheck InventoryFix in AntiCheat.").formatted(Formatting.RED));
         this.run11();
      }
   }

   public Item resolveItem(@Range(from = 1L, to = 4L) int i) {
      List<Item> list = this.items.getValue().stream().toList();

      return switch (this.items.getValue().size()) {
         case 1 -> (Item)list.getFirst();
         case 2 -> {
            Item item = list.get(0);
            Item item2 = list.get(1);
            yield i % 2 == 0 ? item : item2;
         }
         case 3 -> {
            Item item3 = list.get(0);
            Item item4 = list.get(1);
            Item item5 = list.get(2);
            switch (i) {
               case 2:
                  yield item4;
               case 3:
                  yield item5;
               default:
                  yield item3;
            }
         }
         case 4 -> {
            Item item6 = list.get(0);
            Item item7 = list.get(1);
            Item item8 = list.get(2);
            Item item9 = list.get(3);
            switch (i) {
               case 2:
                  yield item7;
               case 3:
                  yield item8;
               case 4:
                  yield item9;
               default:
                  yield item6;
            }
         }
         default -> null;
      };
   }

   public enum Mode {
      Default,
      Auto;
   }
}
