package org.phobos.core.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.chat.ChatTextUtils;
import org.phobos.core.config.ConfigManager;
import org.phobos.core.inventory.EnchantmentUtils;
import org.phobos.core.kit.Kit;
import org.phobos.core.kit.KitRegistry;
import org.phobos.core.PhobosClient;

public class KitCommand extends SyntaxCommand {
   public static final CommandSyntax commandSyntax = new CommandSyntax("dbp");
   public static final CommandSyntax commandSyntax2 = CommandSyntax.createCommandSyntax("normal/grim/old/save/load/del/remove/delete");
   private static Object[] phobos$g1806c1be8;
   private static Object[] phobos$g1806c1cd0;
   private static Object[] phobos$g1806c2258;
   private static Object[] phobos$g1806c2620;
   private static Object[] phobos$g1806c26f8;
   private static Object[] phobos$g1806c2820;
   private static Object[] phobos$g1806c28f8;

   public void handleString2(String[] texts) {
      this.handleText2("Kit %s not found".formatted(texts[1]));
   }

   @Override
   public void handleString(String[] texts, String text) {
      this.handleString3(texts, text);
   }

   public Supplier<ItemStack> resolveSupplier(Item item, int i) {
      return () -> new ItemStack(item, i);
   }

   public void handleKit(Kit kit) {
      KitRegistry.getINSTANCE().handleKit2(kit);
      ConfigManager.getCloudSettings().kit.setValue(kit.getName());
      this.handleText6("Loaded %s".formatted(kit.getName()));
   }

   public void handleString3(String[] texts, String text) {
      MinecraftClient var3 = MinecraftHolder.mc;
      ClientPlayerEntity var4 = var3.player;
      if (var4 != null) {
         MinecraftClient var5 = MinecraftHolder.mc;
         ClientWorld var6 = var5.world;
         if (var6 != null) {
            long var10;
            label216: {
               long var21;
               label215: {
                  Object var7 = texts[0];
                  String var8 = ((String)var7).toLowerCase();
                  int var9 = var8.hashCode();
                  byte var20;
                  if (var9 <= 110119) {
                     if (var9 != 110119) {
                        if (var9 != -1335458389) {
                           if (var9 != -1039745817) {
                              if (var9 != -934610812) {
                                 if (var9 != 99339) {
                                    var10 = 4294967295L;
                                    break label216;
                                 }

                                 boolean var16 = var8.equals("del");
                                 if (!var16) {
                                    var10 = 4294967295L;
                                    break label216;
                                 }

                                 var21 = 5L;
                                 break label215;
                              }

                              boolean var15 = var8.equals("remove");
                              if (!var15) {
                                 var10 = 4294967295L;
                                 break label216;
                              }

                              var20 = 7;
                           } else {
                              boolean var14 = var8.equals("normal");
                              if (!var14) {
                                 var10 = 4294967295L;
                                 break label216;
                              }

                              var20 = 1;
                           }
                        } else {
                           boolean var13 = var8.equals("delete");
                           if (!var13) {
                              var10 = 4294967295L;
                              break label216;
                           }

                           var20 = 6;
                        }
                     } else {
                        boolean var12 = var8.equals("old");
                        if (!var12) {
                           var10 = 4294967295L;
                           break label216;
                        }

                        var20 = 2;
                     }
                  } else {
                     if (var9 != 3181391) {
                        if (var9 != 3327206) {
                           if (var9 != 3522941) {
                              var10 = 4294967295L;
                              break label216;
                           }

                           boolean var19 = var8.equals("save");
                           if (!var19) {
                              var10 = 4294967295L;
                              break label216;
                           }

                           var21 = 3L;
                        } else {
                           boolean var18 = var8.equals("load");
                           if (!var18) {
                              var10 = 4294967295L;
                              break label216;
                           }

                           var21 = 4L;
                        }
                        break label215;
                     }

                     boolean var17 = var8.equals("grim");
                     if (!var17) {
                        var10 = 4294967295L;
                        break label216;
                     }

                     var20 = 0;
                  }

                  var21 = var20 & 4294967295L;
               }

               var10 = (int)var21 & 4294967295L;
            }

            if (Integer.compareUnsigned((int)var10, 7) <= 0) {
               int var67;
               Object var68;
               Object var69;
               switch ((int)var10) {
                  case 0:
                  case 1:
                  case 2:
                  default:
                     ArrayList var23 = new ArrayList();
                     int var24 = texts.length;
                     if (var24 > 1) {
                        Object var25 = texts[1];
                        boolean var26 = ((String)var25).contains("&");
                        int var29;
                        Object var30;
                        if (var26) {
                           String[] var27 = ((String)var25).split("&");
                           List var28 = Arrays.asList(var27);
                           var29 = 7082104;
                           var30 = var28;
                        } else {
                           var29 = 7082040;
                           var30 = var25;
                        }

                        switch (var29) {
                           case 7082040:
                           default:
                              var23.add(var30);
                              break;
                           case 7082104:
                              var23.addAll((Collection)var30);
                        }
                     }

                     ShulkerBoxBlockEntity var31;
                     Supplier var36;
                     Supplier var38;
                     Supplier var40;
                     Supplier var42;
                     Supplier var44;
                     Supplier var46;
                     Supplier var48;
                     long var52;
                     label178: {
                        BlockPos var32 = BlockPos.ORIGIN;
                        Block var33 = Blocks.SHULKER_BOX;
                        BlockState var34 = var33.getDefaultState();
                        var31 = new ShulkerBoxBlockEntity(var32, var34);
                        Item var35 = Items.END_CRYSTAL;
                        var36 = this.resolveSupplier(var35, 64);
                        Item var37 = Items.EXPERIENCE_BOTTLE;
                        var38 = this.resolveSupplier(var37, 64);
                        Item var39 = Items.TOTEM_OF_UNDYING;
                        var40 = this.resolveSupplier(var39, 1);
                        Item var41 = Items.ENCHANTED_GOLDEN_APPLE;
                        var42 = this.resolveSupplier(var41, 64);
                        Item var43 = Items.ENDER_PEARL;
                        var44 = this.resolveSupplier(var43, 16);
                        Item var45 = Items.ENDER_CHEST;
                        var46 = this.resolveSupplier(var45, 64);
                        Item var47 = Items.CHORUS_FRUIT;
                        var48 = this.resolveSupplier(var47, 64);
                        Object var49 = texts[0];
                        String var50 = ((String)var49).toLowerCase();
                        int var51 = var50.hashCode();
                        byte var161;
                        if (var51 != -1039745817) {
                           if (var51 != 110119) {
                              if (var51 != 3181391) {
                                 var52 = 4294967295L;
                                 break label178;
                              }

                              boolean var148 = var50.equals("grim");
                              if (!var148) {
                                 var52 = 4294967295L;
                                 break label178;
                              }

                              var161 = 1;
                           } else {
                              boolean var147 = var50.equals("old");
                              if (!var147) {
                                 var52 = 4294967295L;
                                 break label178;
                              }

                              var161 = 2;
                           }
                        } else {
                           boolean var146 = var50.equals("normal");
                           if (!var146) {
                              var52 = 4294967295L;
                              break label178;
                           }

                           var161 = 0;
                        }

                        var52 = var161 & 4294967295L;
                     }

                     label165: {
                        int[] var205;
                        Supplier var206;
                        if ((int)var52 != 0) {
                           if ((int)((int)var52 & 4294967295L) + -1 != 0) {
                              if ((int)((int)((int)var52 & 4294967295L) + -1 & 4294967295L) != 1) {
                                 Object var149 = texts[0];
                                 String var157 = "Kit " + (String)var149 + " doesn't exist! Available types: normal/grim/old";
                                 this.handleText2(var157);
                                 return;
                              }

                              Item var298 = Items.DIAMOND_HELMET;
                              ItemStack var297 = new ItemStack(var298);
                              ItemStack var299 = this.resolveItemStack(var297, "3arth's H3lmet");
                              RegistryKey var300 = Enchantments.PROTECTION;
                              EnchantmentUtils.handleItemStack3(var299, var300, 4);
                              RegistryKey var301 = Enchantments.AQUA_AFFINITY;
                              EnchantmentUtils.handleItemStack3(var299, var301, 1);
                              RegistryKey var302 = Enchantments.RESPIRATION;
                              EnchantmentUtils.handleItemStack3(var299, var302, 3);
                              Item var304 = Items.DIAMOND_CHESTPLATE;
                              ItemStack var303 = new ItemStack(var304);
                              ItemStack var305 = this.resolveItemStack(var303, "3arth's Ch3stpl4te");
                              RegistryKey var306 = Enchantments.PROTECTION;
                              EnchantmentUtils.handleItemStack3(var305, var306, 4);
                              Item var308 = Items.DIAMOND_LEGGINGS;
                              ItemStack var307 = new ItemStack(var308);
                              ItemStack var309 = this.resolveItemStack(var307, "3arth's P4nts");
                              RegistryKey var310 = Enchantments.BLAST_PROTECTION;
                              EnchantmentUtils.handleItemStack3(var309, var310, 4);
                              Item var312 = Items.DIAMOND_BOOTS;
                              ItemStack var311 = new ItemStack(var312);
                              ItemStack var313 = this.resolveItemStack(var311, "3arth's B0ots");
                              boolean var314 = var23.contains("dbp");
                              int var315;
                              if (!var314) {
                                 var315 = 7085904;
                              } else {
                                 var315 = 7085888;
                              }
                              EnchantmentUtils.handleItemStack3(var313, switch (var315) {
                                 default -> Enchantments.BLAST_PROTECTION;
                                 case 7085904 -> Enchantments.PROTECTION;
                              }, 4);
                              RegistryKey var317 = Enchantments.FEATHER_FALLING;
                              EnchantmentUtils.handleItemStack3(var313, var317, 4);
                              RegistryKey var318 = Enchantments.DEPTH_STRIDER;
                              EnchantmentUtils.handleItemStack3(var313, var318, 3);
                              Item var320 = Items.DIAMOND_SWORD;
                              ItemStack var319 = new ItemStack(var320);
                              ItemStack var321 = this.resolveItemStack(var319, "3arthbl4de");
                              RegistryKey var322 = Enchantments.SHARPNESS;
                              EnchantmentUtils.handleItemStack3(var321, var322, 5);
                              RegistryKey var323 = Enchantments.FIRE_ASPECT;
                              EnchantmentUtils.handleItemStack3(var321, var323, 2);
                              RegistryKey var324 = Enchantments.SWEEPING_EDGE;
                              EnchantmentUtils.handleItemStack3(var321, var324, 3);
                              RegistryKey var325 = Enchantments.KNOCKBACK;
                              EnchantmentUtils.handleItemStack3(var321, var325, 2);
                              Item var327 = Items.DIAMOND_PICKAXE;
                              ItemStack var326 = new ItemStack(var327);
                              ItemStack var328 = this.resolveItemStack(var326, "German Efficiency");
                              RegistryKey var329 = Enchantments.EFFICIENCY;
                              EnchantmentUtils.handleItemStack3(var328, var329, 5);
                              RegistryKey var330 = Enchantments.FORTUNE;
                              EnchantmentUtils.handleItemStack3(var328, var330, 3);
                              var31.setStack(0, var299);
                              var31.setStack(1, var305);
                              var31.setStack(2, var309);
                              var31.setStack(3, var313);
                              var31.setStack(10, var328);
                              var31.setStack(11, var321);
                              int[] var331 = new int[6];
                              var331[0] = 4;
                              var331[1] = 5;
                              var331[2] = 12;
                              var331[3] = 13;
                              var331[4] = 22;
                              var331[5] = 23;
                              this.handleShulkerBoxBlockEntity(var31, var36, var331);
                              int[] var332 = new int[6];
                              var332[0] = 7;
                              var332[1] = 8;
                              var332[2] = 16;
                              var332[3] = 17;
                              var332[4] = 25;
                              var332[5] = 26;
                              this.handleShulkerBoxBlockEntity(var31, var38, var332);
                              int[] var333 = new int[4];
                              var333[0] = 6;
                              var333[1] = 14;
                              var333[2] = 15;
                              var333[3] = 24;
                              this.handleShulkerBoxBlockEntity(var31, var40, var333);
                              int[] var334 = new int[2];
                              var334[0] = 9;
                              var334[1] = 18;
                              this.handleShulkerBoxBlockEntity(var31, var42, var334);
                              int[] var335 = new int[1];
                              var335[0] = 21;
                              this.handleShulkerBoxBlockEntity(var31, var44, var335);
                              int[] var336 = new int[1];
                              var336[0] = 19;
                              this.handleShulkerBoxBlockEntity(var31, var46, var336);
                              int[] var337 = new int[1];
                              var337[0] = 20;
                              this.handleShulkerBoxBlockEntity(var31, var48, var337);
                              break label165;
                           }

                           Item var219 = Items.NETHERITE_HELMET;
                           ItemStack var218 = new ItemStack(var219);
                           ItemStack var220 = this.resolveItemStack(var218, "3arth's H3lmet");
                           RegistryKey var221 = Enchantments.PROTECTION;
                           EnchantmentUtils.handleItemStack3(var220, var221, 4);
                           RegistryKey var222 = Enchantments.AQUA_AFFINITY;
                           EnchantmentUtils.handleItemStack3(var220, var222, 1);
                           RegistryKey var223 = Enchantments.RESPIRATION;
                           EnchantmentUtils.handleItemStack3(var220, var223, 3);
                           Item var225 = Items.NETHERITE_CHESTPLATE;
                           ItemStack var224 = new ItemStack(var225);
                           ItemStack var226 = this.resolveItemStack(var224, "3arth's Ch3stpl4te");
                           RegistryKey var227 = Enchantments.PROTECTION;
                           EnchantmentUtils.handleItemStack3(var226, var227, 4);
                           Item var229 = Items.NETHERITE_LEGGINGS;
                           ItemStack var228 = new ItemStack(var229);
                           ItemStack var230 = this.resolveItemStack(var228, "3arth's P4nts");
                           RegistryKey var231 = Enchantments.BLAST_PROTECTION;
                           EnchantmentUtils.handleItemStack3(var230, var231, 4);
                           RegistryKey var232 = Enchantments.SWIFT_SNEAK;
                           EnchantmentUtils.handleItemStack3(var230, var232, 3);
                           Item var234 = Items.NETHERITE_BOOTS;
                           ItemStack var233 = new ItemStack(var234);
                           ItemStack var235 = this.resolveItemStack(var233, "3arth's B0ots");
                           boolean var236 = var23.contains("dbp");
                           int var237;
                           if (!var236) {
                              var237 = 7084272;
                           } else {
                              var237 = 7084256;
                           }
                           EnchantmentUtils.handleItemStack3(var235, switch (var237) {
                              default -> Enchantments.BLAST_PROTECTION;
                              case 7084272 -> Enchantments.PROTECTION;
                           }, 4);
                           RegistryKey var239 = Enchantments.FEATHER_FALLING;
                           EnchantmentUtils.handleItemStack3(var235, var239, 4);
                           RegistryKey var240 = Enchantments.SOUL_SPEED;
                           EnchantmentUtils.handleItemStack3(var235, var240, 3);
                           RegistryKey var241 = Enchantments.DEPTH_STRIDER;
                           EnchantmentUtils.handleItemStack3(var235, var241, 3);
                           Item var243 = Items.NETHERITE_SWORD;
                           ItemStack var242 = new ItemStack(var243);
                           ItemStack var244 = this.resolveItemStack(var242, "3arthbl4de");
                           RegistryKey var245 = Enchantments.SHARPNESS;
                           EnchantmentUtils.handleItemStack3(var244, var245, 5);
                           RegistryKey var246 = Enchantments.FIRE_ASPECT;
                           EnchantmentUtils.handleItemStack3(var244, var246, 2);
                           RegistryKey var247 = Enchantments.SWEEPING_EDGE;
                           EnchantmentUtils.handleItemStack3(var244, var247, 3);
                           RegistryKey var248 = Enchantments.KNOCKBACK;
                           EnchantmentUtils.handleItemStack3(var244, var248, 2);
                           Item var250 = Items.NETHERITE_PICKAXE;
                           ItemStack var249 = new ItemStack(var250);
                           ItemStack var251 = this.resolveItemStack(var249, "German Efficiency");
                           RegistryKey var252 = Enchantments.EFFICIENCY;
                           EnchantmentUtils.handleItemStack3(var251, var252, 5);
                           RegistryKey var253 = Enchantments.FORTUNE;
                           EnchantmentUtils.handleItemStack3(var251, var253, 3);
                           var31.setStack(0, var220);
                           var31.setStack(1, var226);
                           var31.setStack(2, var230);
                           var31.setStack(3, var235);
                           var31.setStack(10, var251);
                           var31.setStack(11, var244);
                           Supplier<ItemStack> var270 = KitCommand::createItemStack2;
                           int[] var271 = new int[1];
                           var271[0] = 4;
                           this.handleShulkerBoxBlockEntity(var31, var270, var271);
                           Supplier<ItemStack> var288 = KitCommand::createItemStack;
                           int[] var289 = new int[1];
                           var289[0] = 13;
                           this.handleShulkerBoxBlockEntity(var31, var288, var289);
                           int[] var290 = new int[6];
                           var290[0] = 7;
                           var290[1] = 8;
                           var290[2] = 16;
                           var290[3] = 17;
                           var290[4] = 25;
                           var290[5] = 26;
                           this.handleShulkerBoxBlockEntity(var31, var40, var290);
                           int[] var291 = new int[4];
                           var291[0] = 14;
                           var291[1] = 15;
                           var291[2] = 23;
                           var291[3] = 24;
                           this.handleShulkerBoxBlockEntity(var31, var36, var291);
                           int[] var292 = new int[3];
                           var292[0] = 12;
                           var292[1] = 21;
                           var292[2] = 22;
                           this.handleShulkerBoxBlockEntity(var31, var44, var292);
                           int[] var293 = new int[2];
                           var293[0] = 9;
                           var293[1] = 18;
                           this.handleShulkerBoxBlockEntity(var31, var42, var293);
                           int[] var294 = new int[2];
                           var294[0] = 5;
                           var294[1] = 6;
                           this.handleShulkerBoxBlockEntity(var31, var38, var294);
                           int[] var295 = new int[1];
                           var295[0] = 20;
                           this.handleShulkerBoxBlockEntity(var31, var48, var295);
                           int[] var296 = new int[1];
                           var296[0] = 19;
                           var205 = var296;
                           var206 = var46;
                        } else {
                           Item var163 = Items.NETHERITE_HELMET;
                           ItemStack var162 = new ItemStack(var163);
                           ItemStack var164 = this.resolveItemStack(var162, "3arth's H3lmet");
                           RegistryKey var165 = Enchantments.PROTECTION;
                           EnchantmentUtils.handleItemStack3(var164, var165, 4);
                           RegistryKey var166 = Enchantments.AQUA_AFFINITY;
                           EnchantmentUtils.handleItemStack3(var164, var166, 1);
                           RegistryKey var167 = Enchantments.RESPIRATION;
                           EnchantmentUtils.handleItemStack3(var164, var167, 3);
                           Item var169 = Items.NETHERITE_CHESTPLATE;
                           ItemStack var168 = new ItemStack(var169);
                           ItemStack var170 = this.resolveItemStack(var168, "3arth's Ch3stpl4te");
                           RegistryKey var171 = Enchantments.PROTECTION;
                           EnchantmentUtils.handleItemStack3(var170, var171, 4);
                           Item var173 = Items.NETHERITE_LEGGINGS;
                           ItemStack var172 = new ItemStack(var173);
                           ItemStack var174 = this.resolveItemStack(var172, "3arth's P4nts");
                           RegistryKey var175 = Enchantments.BLAST_PROTECTION;
                           EnchantmentUtils.handleItemStack3(var174, var175, 4);
                           RegistryKey var176 = Enchantments.SWIFT_SNEAK;
                           EnchantmentUtils.handleItemStack3(var174, var176, 3);
                           Item var178 = Items.NETHERITE_BOOTS;
                           ItemStack var177 = new ItemStack(var178);
                           ItemStack var179 = this.resolveItemStack(var177, "3arth's B0ots");
                           boolean var180 = var23.contains("dbp");
                           int var181;
                           if (!var180) {
                              var181 = 7083072;
                           } else {
                              var181 = 7083056;
                           }
                           EnchantmentUtils.handleItemStack3(var179, switch (var181) {
                              default -> Enchantments.BLAST_PROTECTION;
                              case 7083072 -> Enchantments.PROTECTION;
                           }, 4);
                           RegistryKey var183 = Enchantments.FEATHER_FALLING;
                           EnchantmentUtils.handleItemStack3(var179, var183, 4);
                           RegistryKey var184 = Enchantments.SOUL_SPEED;
                           EnchantmentUtils.handleItemStack3(var179, var184, 3);
                           RegistryKey var185 = Enchantments.DEPTH_STRIDER;
                           EnchantmentUtils.handleItemStack3(var179, var185, 3);
                           Item var187 = Items.NETHERITE_SWORD;
                           ItemStack var186 = new ItemStack(var187);
                           ItemStack var188 = this.resolveItemStack(var186, "3arthbl4de");
                           RegistryKey var189 = Enchantments.SHARPNESS;
                           EnchantmentUtils.handleItemStack3(var188, var189, 5);
                           RegistryKey var190 = Enchantments.FIRE_ASPECT;
                           EnchantmentUtils.handleItemStack3(var188, var190, 2);
                           RegistryKey var191 = Enchantments.SWEEPING_EDGE;
                           EnchantmentUtils.handleItemStack3(var188, var191, 3);
                           RegistryKey var192 = Enchantments.KNOCKBACK;
                           EnchantmentUtils.handleItemStack3(var188, var192, 2);
                           Item var194 = Items.NETHERITE_PICKAXE;
                           ItemStack var193 = new ItemStack(var194);
                           ItemStack var195 = this.resolveItemStack(var193, "German Efficiency");
                           RegistryKey var196 = Enchantments.EFFICIENCY;
                           EnchantmentUtils.handleItemStack3(var195, var196, 5);
                           RegistryKey var197 = Enchantments.FORTUNE;
                           EnchantmentUtils.handleItemStack3(var195, var197, 3);
                           var31.setStack(0, var164);
                           var31.setStack(1, var170);
                           var31.setStack(2, var174);
                           var31.setStack(3, var179);
                           var31.setStack(10, var195);
                           var31.setStack(11, var188);
                           int[] var198 = new int[6];
                           var198[0] = 4;
                           var198[1] = 5;
                           var198[2] = 12;
                           var198[3] = 13;
                           var198[4] = 22;
                           var198[5] = 23;
                           this.handleShulkerBoxBlockEntity(var31, var36, var198);
                           int[] var199 = new int[6];
                           var199[0] = 7;
                           var199[1] = 8;
                           var199[2] = 16;
                           var199[3] = 17;
                           var199[4] = 25;
                           var199[5] = 26;
                           this.handleShulkerBoxBlockEntity(var31, var38, var199);
                           int[] var200 = new int[4];
                           var200[0] = 6;
                           var200[1] = 14;
                           var200[2] = 15;
                           var200[3] = 24;
                           this.handleShulkerBoxBlockEntity(var31, var40, var200);
                           int[] var201 = new int[2];
                           var201[0] = 9;
                           var201[1] = 18;
                           this.handleShulkerBoxBlockEntity(var31, var42, var201);
                           int[] var202 = new int[1];
                           var202[0] = 19;
                           this.handleShulkerBoxBlockEntity(var31, var46, var202);
                           int[] var203 = new int[1];
                           var203[0] = 20;
                           this.handleShulkerBoxBlockEntity(var31, var48, var203);
                           int[] var204 = new int[1];
                           var204[0] = 21;
                           var205 = var204;
                           var206 = var44;
                        }

                        this.handleShulkerBoxBlockEntity(var31, var206, var205);
                     }

                     Item var208 = Items.WHITE_SHULKER_BOX;
                     ItemStack var207 = new ItemStack(var208);
                     MinecraftClient var209 = MinecraftHolder.mc;
                     ClientWorld var210 = var209.world;
                     DynamicRegistryManager var211 = var210.getRegistryManager();
                     NbtCompound var212 = var31.createComponentlessNbtWithIdentifyingData(var211);
                     var31.removeFromCopiedStackNbt(var212);
                     BlockEntityType var213 = var31.getType();
                     BlockItem.setBlockEntityData(var207, var213, var212);
                     ComponentMap var214 = var31.createComponentMap();
                     var207.applyComponentsFrom(var214);
                     MinecraftClient var215 = MinecraftHolder.mc;
                     ClientPlayerEntity var216 = var215.player;
                     boolean var217 = var216.isCreative();
                     if (!var217) {
                        var67 = 7087048;
                        var68 = "You are not in creative mode";
                        var69 = this;
                     } else {
                        MinecraftClient var338 = MinecraftHolder.mc;
                        ClientPlayerEntity var339 = var338.player;
                        ClientPlayNetworkHandler var340 = var339.networkHandler;
                        CreativeInventoryActionC2SPacket var341 = new CreativeInventoryActionC2SPacket(36, var207);
                        var67 = 7087016;
                        var68 = var341;
                        var69 = var340;
                     }
                     break;
                  case 3:
                     int var54 = texts.length;
                     if (var54 >= 2) {
                        Object var55 = texts[1];
                        boolean var56 = ChatTextUtils.testText((String)var55);
                        if (var56) {
                           MinecraftClient var57 = MinecraftHolder.mc;
                           ClientPlayerEntity var58 = var57.player;
                           Kit var59 = Kit.resolveKit((String)var55, var58);
                           KitRegistry var60 = KitRegistry.getINSTANCE();
                           boolean var61 = var60.testNamed4(var59);
                           if (var61) {
                              KitRegistry var62 = KitRegistry.getINSTANCE();
                              String var63 = var59.getName();
                              var62.handleText(var63);
                              Object[] var64 = new Object[1];
                              String var65 = var59.getName();
                              var64[0] = var65;
                              String var66 = "Saved %s".formatted(var64);
                              var67 = 7087368;
                              var68 = var66;
                              var69 = this;
                           } else {
                              Object[] var158 = new Object[1];
                              Object var159 = texts[1];
                              var158[0] = var159;
                              String var160 = "Failed to add %s".formatted(var158);
                              var67 = 7087256;
                              var68 = var160;
                              var69 = this;
                           }
                        } else {
                           var67 = 7087128;
                           var68 = "Kit name contains unsupported characters";
                           var69 = this;
                        }
                     } else {
                        var67 = 7087080;
                        var68 = "Invalid arguments";
                        var69 = this;
                     }
                     break;
                  case 4:
                     int var70 = texts.length;
                     if (var70 >= 2) {
                        KitRegistry var71 = KitRegistry.getINSTANCE();
                        Object var72 = texts[1];
                        Optional var73 = var71.resolveOptional2((String)var72);
                        Consumer<Kit> var90 = this::handleKit;
                        KitCommand var346 = this;
                        String[] cap65 = texts;
                        KitCommand cap64 = var346;
                        Runnable var107 = () -> cap64.handleString2(cap65);
                        var73.ifPresentOrElse(var90, var107);
                        return;
                     }

                     var67 = 7087400;
                     var68 = "Invalid arguments";
                     var69 = this;
                     break;
                  case 5:
                  case 6:
                  case 7:
                     int var108 = texts.length;
                     if (var108 >= 2) {
                        KitRegistry var109 = KitRegistry.getINSTANCE();
                        Object var110 = texts[1];
                        Optional var111 = var109.resolveOptional2((String)var110);
                        Consumer<Kit> var128 = this::handleKit2;
                        KitCommand var10000 = this;
                        String[] cap67 = texts;
                        KitCommand cap66 = var10000;
                        Runnable var145 = () -> cap66.handleString4(cap67);
                        var111.ifPresentOrElse(var128, var145);
                        return;
                     }

                     var67 = 7087912;
                     var68 = "Invalid arguments";
                     var69 = this;
               }

               switch (var67) {
                  case 7087016:
                  default:
                     ((ClientPlayNetworkHandler)var69).sendPacket((Packet<?>)var68);
                     break;
                  case 7087048:
                     ((KitCommand)var69).handleText2((String)var68);
                     break;
                  case 7087080:
                     ((KitCommand)var69).handleText2((String)var68);
                     break;
                  case 7087128:
                     ((KitCommand)var69).handleText2((String)var68);
                     break;
                  case 7087256:
                     ((KitCommand)var69).handleText2((String)var68);
                     break;
                  case 7087368:
                     ((KitCommand)var69).handleText6((String)var68);
                     break;
                  case 7087400:
                     ((KitCommand)var69).handleText2((String)var68);
                     break;
                  case 7087912:
                     ((KitCommand)var69).handleText2((String)var68);
               }
            }
         }
      }
   }

   public ItemStack resolveItemStack(ItemStack itemStack, String text) {
      EnchantmentUtils.handleItemStack3(itemStack, Enchantments.UNBREAKING, 3);
      EnchantmentUtils.handleItemStack3(itemStack, Enchantments.MENDING, 1);
      this.handleItemStack(itemStack, text);
      return itemStack;
   }

   public void handleShulkerBoxBlockEntity(ShulkerBoxBlockEntity shulkerBoxBlockEntity, Supplier<ItemStack> supplier, int... ints) {
      for (int i = 0; i < 27; i++) {
         if (this.testInt(i, ints)) {
            shulkerBoxBlockEntity.setStack(i, supplier.get());
         }
      }
   }

   public void handleString4(String[] texts) {
      this.handleText2("Kit %s not found".formatted(texts[1]));
   }

   public static ItemStack createItemStack() {
      return new ItemStack(Items.ITEM_FRAME, 64);
   }

   @Override
   public Suggestion resolveSuggestion(String text, String text2, String[] texts) {
      switch (text) {
         case "type":
            return commandSyntax2.resolveSuggestion(text2);
         case "flags":
            if (texts.length == 0) {
               return null;
            } else {
               String text4 = texts[0];
               switch (text4.toLowerCase()) {
                  case "normal":
                  case "grim":
                  case "old":
                     String[] texts2 = text2.split("&");
                     if (texts2.length < 2) {
                        return commandSyntax.resolveSuggestion(text2);
                     } else {
                        String text3 = texts2[texts2.length - 1];
                        Suggestion suggestion = commandSyntax.resolveSuggestion(text3);
                        if (suggestion != null) {
                           StringBuilder stringBuilder = new StringBuilder();

                           for (int i = 0; i < texts2.length - 1; i++) {
                              stringBuilder.append(texts2[i]).append("&");
                           }

                           stringBuilder.append(suggestion.getText());
                           return Suggestion.createSuggestion3(stringBuilder.toString());
                        }
                        break;
                     }
                  case "load":
                  case "del":
                  case "delete":
                  case "remove":
                     Kit kit = KitRegistry.getINSTANCE().resolveNamed(text2);
                     if (kit == null) {
                        return null;
                     }

                     return Suggestion.createSuggestion3(kit.getName());
               }
            }
         default:
            return null;
      }
   }

   public KitCommand(PhobosClient phobosClient) {
      super(phobosClient, "kit", "type", "flags");
   }

   public void handleItemStack(ItemStack itemStack, String text) {
      itemStack.set(DataComponentTypes.CUSTOM_NAME, Text.literal(text));
   }

   public static ItemStack createItemStack2() {
      return new ItemStack(Items.SCAFFOLDING, 64);
   }

   public void handleKit2(Kit kit) {
      if (KitRegistry.getINSTANCE().testNamed3(kit)) {
         this.handleText6("Deleted %s".formatted(kit.getName()));
      } else {
         this.handleText6("Failed to delete %s".formatted(kit.getName()));
      }
   }

   public boolean testInt(int i, int[] ints) {
      for (int i2 : ints) {
         if (i2 == i) {
            return true;
         }
      }

      return false;
   }

   @Override
   public boolean testString(String[] texts) {
      return texts.length >= 1;
   }
}
