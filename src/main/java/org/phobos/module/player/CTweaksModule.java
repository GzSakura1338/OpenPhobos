package org.phobos.module.player;

import com.google.gson.JsonObject;
import java.security.SecureRandom;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.StringSetting;
import org.phobos.core.setting.BindSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.tick.TimerManager;
import org.phobos.core.bind.KeyCombination;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.bind.BindTriggerUtils;
import org.phobos.core.bind.BindTriggerUtils.Type;
import org.phobos.module.Module;

public class CTweaksModule extends Module {
   public final Setting<Boolean> spawnInvincible;
   public final Setting<Boolean> spawnBaby;
   public final Setting<CTweaksModule.EntityType> spawnType;
   public final Setting<Double> arrowSpeed;
   public final Setting<Integer> fireballPower;
   public final Setting<KeyCombination> fireballKey;
   public final Setting<KeyCombination> spawnKey;
   public final Setting<Boolean> spawnGlow;
   public final Setting<KeyCombination> arrowKey = this.resolveSetting2(new BindSetting("ArrowKey"));
   public final Setting<String> spawnName;
   public final Setting<Double> fireballSpeed;
   public final Setting<Integer> spawnArea;
   public final Setting<Integer> spawnMultiplier;

   public CTweaksModule(PhobosClient phobosClient) {
      super(phobosClient, "CTweaks", Categories.player);
      this.arrowSpeed = this.resolveSetting2(new NumberSetting<>("ArrowSpeed", 10.0, 0.1, 10.0));
      this.fireballKey = this.resolveSetting2(new BindSetting("FireballKey"));
      this.fireballSpeed = this.resolveSetting2(new NumberSetting<>("FireballSpeed", 6.0, 0.1, 10.0));
      this.fireballPower = this.resolveSetting2(new NumberSetting<>("FireballPower", 15, 2, 50));
      this.spawnKey = this.resolveSetting2(new BindSetting("SpawnKey"));
      this.spawnType = this.resolveSetting2(new EnumSetting<>("SpawnType", CTweaksModule.EntityType.Dragon));
      this.spawnName = this.resolveSetting2(new StringSetting("SpawnName", "Nigger"));
      this.spawnInvincible = this.resolveSetting2(new BooleanSetting("SpawnInvincible"));
      this.spawnGlow = this.resolveSetting2(new BooleanSetting("SpawnGlow"));
      this.spawnBaby = this.resolveSetting2(new BooleanSetting("SpawnBaby"));
      this.spawnArea = this.resolveSetting2(new NumberSetting<>("SpawnArea", 60, 10, 200));
      this.spawnMultiplier = this.resolveSetting2(new NumberSetting<>("SpawnMultiplier", 1, 1, 10));
      this.resolveSettingGroup2("Arrow", this.arrowKey, this.arrowSpeed).resolveSettingGroup6("Summons an arrow");
      this.resolveSettingGroup2("Fireball", this.fireballKey, this.fireballSpeed, this.fireballPower).resolveSettingGroup6("Summons a custom fireball");
      this.resolveSettingGroup2(
            "Spawn", this.spawnKey, this.spawnType, this.spawnName, this.spawnInvincible, this.spawnGlow, this.spawnBaby, this.spawnArea, this.spawnMultiplier
         )
         .resolveSettingGroup6("Summons entities");
      BindTriggerUtils.handleSupplier(this.arrowKey, () -> {
         MinecraftClient var1 = MinecraftHolder.mc;
         ClientPlayerEntity var2 = var1.player;
         if (var2 != null) {
            MinecraftClient var3 = MinecraftHolder.mc;
            ClientWorld var4 = var3.world;
            if (var4 != null) {
               MinecraftClient var5 = MinecraftHolder.mc;
               ClientPlayNetworkHandler var6 = var5.getNetworkHandler();
               if (var6 != null) {
                  float var7 = TimerManager.getFloatValue();
                  MinecraftClient var8 = MinecraftHolder.mc;
                  ClientPlayerEntity var9 = var8.player;
                  float var10 = var9.getYaw((float)Float.intBitsToFloat(Float.floatToRawIntBits(var7)));
                  MinecraftClient var11 = MinecraftHolder.mc;
                  ClientPlayerEntity var12 = var11.player;
                  float var13 = var12.getPitch((float)Float.intBitsToFloat(Float.floatToRawIntBits(var7)));
                  MinecraftClient var14 = MinecraftHolder.mc;
                  ClientPlayerEntity var15 = var14.player;
                  Vec3d var16 = var15.getCameraPosVec((float)Float.intBitsToFloat(Float.floatToRawIntBits(var7)));
                  Setting var17 = this.arrowSpeed;
                  Object var18 = var17.getValue();
                  boolean var19 = var18 instanceof Double;
                  if (var18 != null && !var19) {
                     throw new ClassCastException("CHECKCAST failed");
                  }

                  double var20 = (Double)var18;
                  double var22 = Math.toRadians(Float.intBitsToFloat(Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var10)) + 90.0F)));
                  double var24 = Math.cos(var22);
                  double var26 = Math.toRadians(Float.intBitsToFloat(Float.floatToRawIntBits(var13)));
                  double var28 = Math.sin(var26);
                  double var30 = Math.toRadians(Float.intBitsToFloat(Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var10)) + 90.0F)));
                  double var32 = Math.sin(var30);
                  double var34 = var16.getX();
                  double var36 = var16.getY();
                  double var38 = var16.getZ();
                  MinecraftClient var40 = MinecraftHolder.mc;
                  ClientPlayNetworkHandler var41 = var40.getNetworkHandler();
                  Object[] var42 = new Object[6];
                  Double var43 = var34;
                  var42[0] = var43;
                  Double var44 = var36;
                  var42[1] = var44;
                  Double var45 = var38;
                  var42[2] = var45;
                  Double var46 = var20 * var24;
                  var42[3] = var46;
                  Double var47 = Double.longBitsToDouble(Double.doubleToRawLongBits(var28) ^ Long.MIN_VALUE) * var20;
                  var42[4] = var47;
                  Double var48 = var20 * var32;
                  var42[5] = var48;
                  String var49 = "summon minecraft:arrow %s %s %s {Motion:[%sd, %sd, %sd]}".formatted(var42);
                  var41.sendChatCommand(var49);
               }
            }
         }
      }, this, Type.Active);
      BindTriggerUtils.handleSupplier(this.fireballKey, () -> {
         MinecraftClient var1 = MinecraftHolder.mc;
         ClientPlayerEntity var2 = var1.player;
         if (var2 != null) {
            MinecraftClient var3 = MinecraftHolder.mc;
            ClientWorld var4 = var3.world;
            if (var4 != null) {
               MinecraftClient var5 = MinecraftHolder.mc;
               ClientPlayNetworkHandler var6 = var5.getNetworkHandler();
               if (var6 != null) {
                  float var7 = TimerManager.getFloatValue();
                  MinecraftClient var8 = MinecraftHolder.mc;
                  ClientPlayerEntity var9 = var8.player;
                  float var10 = var9.getYaw((float)Float.intBitsToFloat(Float.floatToRawIntBits(var7)));
                  MinecraftClient var11 = MinecraftHolder.mc;
                  ClientPlayerEntity var12 = var11.player;
                  float var13 = var12.getPitch((float)Float.intBitsToFloat(Float.floatToRawIntBits(var7)));
                  MinecraftClient var14 = MinecraftHolder.mc;
                  ClientPlayerEntity var15 = var14.player;
                  Vec3d var16 = var15.getCameraPosVec((float)Float.intBitsToFloat(Float.floatToRawIntBits(var7)));
                  Setting var17 = this.fireballPower;
                  Object var18 = var17.getValue();
                  boolean var19 = var18 instanceof Integer;
                  if (var18 != null && !var19) {
                     throw new ClassCastException("CHECKCAST failed");
                  }

                  int var20 = (Integer)var18;
                  Setting var21 = this.fireballSpeed;
                  Object var22 = var21.getValue();
                  boolean var23 = var22 instanceof Double;
                  if (var22 != null && !var23) {
                     throw new ClassCastException("CHECKCAST failed");
                  }

                  double var24 = (Double)var22;
                  double var26 = Math.toRadians(Float.intBitsToFloat(Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var10)) + 90.0F)));
                  double var28 = Math.cos(var26);
                  double var30 = Math.toRadians(Float.intBitsToFloat(Float.floatToRawIntBits(var13)));
                  double var32 = Math.sin(var30);
                  double var34 = Math.toRadians(Float.intBitsToFloat(Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var10)) + 90.0F)));
                  double var36 = Math.sin(var34);
                  double var38 = var16.getX();
                  double var40 = var16.getY();
                  double var42 = var16.getZ();
                  MinecraftClient var44 = MinecraftHolder.mc;
                  ClientPlayNetworkHandler var45 = var44.getNetworkHandler();
                  Object[] var46 = new Object[7];
                  Double var47 = var38;
                  var46[0] = var47;
                  Double var48 = var40;
                  var46[1] = var48;
                  Double var49 = var42;
                  var46[2] = var49;
                  Double var50 = var24 * var28;
                  var46[3] = var50;
                  Double var51 = Double.longBitsToDouble(Double.doubleToRawLongBits(var32) ^ Long.MIN_VALUE) * var24;
                  var46[4] = var51;
                  Double var52 = var24 * var36;
                  var46[5] = var52;
                  Integer var53 = var20;
                  var46[6] = var53;
                  String var54 = "summon minecraft:fireball %s %s %s {Motion:[%sd, %sd, %sd], ExplosionPower:%s}".formatted(var46);
                  var45.sendChatCommand(var54);
               }
            }
         }
      }, this, Type.Active);
      BindTriggerUtils.handleSupplier(this.spawnKey, () -> {
         long var11 = 0L;
         MinecraftClient var1 = MinecraftHolder.mc;
         ClientPlayerEntity var2 = var1.player;
         if (var2 != null) {
            MinecraftClient var3 = MinecraftHolder.mc;
            ClientWorld var4 = var3.world;
            if (var4 != null) {
               MinecraftClient var5 = MinecraftHolder.mc;
               ClientPlayNetworkHandler var6 = var5.getNetworkHandler();
               if (var6 != null) {
                  SecureRandom var7 = new SecureRandom();
                  StringBuilder var8 = new StringBuilder();
                  long var9 = 0L;

                  while (true) {
                     long var13 = (int)var9 & 4294967295L | var11 & -4294967296L;
                     long var15 = var13;
                     long var17 = var15;
                     long var19 = var17;
                     long var21 = var19;
                     Setting var23 = this.spawnMultiplier;
                     long var24 = var21;
                     long var26 = var24;
                     long var28 = var26;
                     long var30 = var28;
                     long var32 = var30;
                     long var34 = var32;
                     Object var36 = var23.getValue();
                     long var37 = var34;
                     long var39 = var37;
                     long var41 = var39;
                     boolean var43 = var36 instanceof Integer;
                     if (var36 != null && !var43) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     long var46 = var41;
                     long var50 = var46;
                     long var52 = var50;
                     long var54 = var52;
                     long var56 = var54;
                     long var58 = var56;
                     long var60 = var58;
                     int var62 = (Integer)var36;
                     long var63 = var60;
                     long var65 = var63;
                     long var67 = var65;
                     if ((int)((int)var9 & 4294967295L) >= var62) {
                        break;
                     }

                     long var69 = var67;
                     long var71 = var69;
                     long var73 = var71;
                     long var75 = var73;
                     long var77 = var75;
                     long var79 = var77;
                     long var81 = var79;
                     long var83 = var81;
                     long var85 = var83;
                     long var87 = var85;
                     var8.append("execute as @a at @s run summon");
                     long var89 = var87;
                     long var91 = var89;
                     long var93 = var91;
                     long var95 = var93;
                     long var97 = var95;
                     long var99 = var97;
                     long var101 = var99;
                     long var103 = var101;
                     long var105 = var103;
                     long var107 = var105;
                     long var109 = var107;
                     long var111 = var109;
                     StringBuilder var113 = var8.append(" ");
                     long var114 = var111;
                     long var116 = var114;
                     long var118 = var116;
                     long var120 = var118;
                     long var122 = var120;
                     long var124 = var122;
                     Setting var126 = this.spawnType;
                     long var127 = var124;
                     long var129 = var127;
                     long var131 = var129;
                     long var133 = var131;
                     long var135 = var133;
                     long var137 = var135;
                     Object var139 = var126.getValue();
                     long var140 = var137;
                     long var142 = var140;
                     long var144 = var142;
                     boolean var146 = var139 instanceof CTweaksModule.EntityType;
                     if (var139 != null && !var146) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     long var149 = var144;
                     long var153 = var149;
                     long var155 = var153;
                     long var157 = var155;
                     long var159 = var157;
                     long var161 = var159;
                     long var163 = var161;
                     String var165 = ((CTweaksModule.EntityType)var139).getEntityTag();
                     long var166 = var163;
                     long var168 = var166;
                     long var170 = var168;
                     long var172 = var170;
                     long var174 = var172;
                     long var176 = var174;
                     long var178 = var176;
                     StringBuilder var180 = var113.append(var165);
                     long var181 = var178;
                     long var183 = var181;
                     long var185 = var183;
                     long var187 = var185;
                     long var189 = var187;
                     long var191 = var189;
                     long var193 = var191;
                     long var195 = var193;
                     long var197 = var195;
                     long var199 = var197;
                     var180.append(" ");
                     long var201 = var199;
                     long var203 = var201;
                     long var205 = var203;
                     long var207 = var205;
                     long var209 = var207;
                     long var211 = var209;
                     Setting var213 = this.spawnArea;
                     long var214 = var211;
                     long var216 = var214;
                     long var218 = var216;
                     long var220 = var218;
                     long var222 = var220;
                     long var224 = var222;
                     Object var226 = var213.getValue();
                     long var227 = var224;
                     long var229 = var227;
                     long var231 = var229;
                     boolean var233 = var226 instanceof Integer;
                     if (var226 != null && !var233) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     long var236 = var231;
                     long var240 = var236;
                     long var242 = var240;
                     long var244 = var242;
                     long var246 = var244;
                     long var248 = var246;
                     long var250 = var248;
                     int var252 = (Integer)var226;
                     long var253 = var250;
                     long var255 = var253;
                     long var257 = var255;
                     long var259 = var257;
                     long var261 = var259;
                     long var263 = var261;
                     long var265 = var263;
                     long var267 = var265;
                     long var269 = var267;
                     long var271 = var269;
                     Object[] var273 = new Object[3];
                     long var274 = var271;
                     long var276 = var274;
                     long var278 = var276;
                     long var280 = var278;
                     long var282 = var280;
                     long var284 = var282;
                     long var286 = var284;
                     long var288 = var286;
                     long var290 = var288;
                     long var292 = var290;
                     long var294 = var292;
                     long var296 = var294;
                     long var298 = var296;
                     long var300 = var298;
                     long var302 = var300;
                     long var304 = var302;
                     int var306 = this.computeSecureRandom(var7, var252);
                     long var307 = var304;
                     long var309 = var307;
                     long var311 = var309;
                     long var313 = var311;
                     long var315 = var313;
                     long var317 = var315;
                     Integer var319 = var306;
                     long var320 = var317;
                     long var322 = var320;
                     long var324 = var322;
                     var273[0] = var319;
                     long var326 = var324;
                     long var328 = var326;
                     long var330 = var328;
                     long var332 = var330;
                     long var334 = var332;
                     long var336 = var334;
                     long var338 = var336;
                     long var340 = var338;
                     long var342 = var340;
                     long var344 = var342;
                     long var346 = var344;
                     long var348 = var346;
                     long var350 = var348;
                     long var352 = var350;
                     int var354 = this.computeSecureRandom(var7, (int)((int)(var252 & 4294967295L) & 4294967295L));
                     long var355 = var352;
                     long var357 = var355;
                     long var359 = var357;
                     long var361 = var359;
                     long var363 = var361;
                     long var365 = var363;
                     Integer var367 = var354;
                     long var368 = var365;
                     long var370 = var368;
                     long var372 = var370;
                     var273[1] = var367;
                     long var374 = var372;
                     long var376 = var374;
                     long var378 = var376;
                     long var380 = var378;
                     long var382 = var380;
                     long var384 = var382;
                     long var386 = var384;
                     long var388 = var386;
                     long var390 = var388;
                     long var392 = var390;
                     long var394 = var392;
                     long var396 = var394;
                     long var398 = var396;
                     long var400 = var398;
                     int var402 = this.computeSecureRandom(var7, (int)((int)(var252 & 4294967295L) & 4294967295L));
                     long var403 = var400;
                     long var405 = var403;
                     long var407 = var405;
                     long var409 = var407;
                     long var411 = var409;
                     long var413 = var411;
                     Integer var415 = var402;
                     long var416 = var413;
                     long var418 = var416;
                     long var420 = var418;
                     var273[2] = var415;
                     long var422 = var420;
                     long var424 = var422;
                     long var426 = var424;
                     long var428 = var426;
                     String var430 = "~%d ~%d ~%d".formatted(var273);
                     long var431 = var428;
                     long var433 = var431;
                     long var435 = var433;
                     long var437 = var435;
                     long var439 = var437;
                     long var441 = var439;
                     long var443 = var441;
                     var8.append(var430);
                     long var445 = var443;
                     long var447 = var445;
                     long var449 = var447;
                     long var451 = var449;
                     long var453 = var451;
                     long var455 = var453;
                     long var457 = var455;
                     long var459 = var457;
                     long var461 = var459;
                     long var463 = var461;
                     long var465 = var463;
                     long var467 = var465;
                     StringBuilder var469 = var8.append(" ");
                     long var470 = var467;
                     long var472 = var470;
                     long var474 = var472;
                     long var476 = var474;
                     long var478 = var476;
                     long var480 = var478;
                     long var482 = var480;
                     long var484 = var482;
                     long var486 = var484;
                     JsonObject var488 = this.getJsonObject();
                     long var489 = var486;
                     long var491 = var489;
                     long var493 = var491;
                     long var495 = var493;
                     long var497 = var495;
                     long var499 = var497;
                     long var501 = var499;
                     var469.append(var488);
                     long var503 = var501;
                     long var505 = var503;
                     long var507 = var505;
                     long var509 = var507;
                     MinecraftClient var511 = MinecraftHolder.mc;
                     long var512 = var509;
                     long var514 = var512;
                     long var516 = var514;
                     long var518 = var516;
                     long var520 = var518;
                     long var522 = var520;
                     ClientPlayNetworkHandler var524 = var511.getNetworkHandler();
                     long var525 = var522;
                     long var527 = var525;
                     long var529 = var527;
                     long var531 = var529;
                     long var533 = var531;
                     long var535 = var533;
                     long var537 = var535;
                     long var539 = var537;
                     long var541 = var539;
                     String var543 = var8.toString();
                     long var544 = var541;
                     long var546 = var544;
                     long var548 = var546;
                     long var550 = var548;
                     long var552 = var550;
                     long var554 = var552;
                     long var556 = var554;
                     var524.sendChatCommand(var543);
                     long var558 = var556;
                     long var560 = var558;
                     long var562 = var560;
                     long var564 = var562;
                     long var566 = var564;
                     long var568 = var566;
                     long var570 = var568;
                     long var572 = var570;
                     long var574 = var572;
                     var8.setLength(0);
                     long var576 = var574;
                     long var10000 = (int)var576 + 1 & 4294967295L;
                     var11 = var576;
                     var9 = var10000;
                  }
               }
            }
         }
      }, this, Type.Active);
   }

   public int computeSecureRandom(SecureRandom secureRandom, int i) {
      return secureRandom.nextInt(i * 2) - i;
   }

   @NotNull
   public JsonObject getJsonObject() {
      JsonObject var1 = new JsonObject();
      JsonObject var2 = new JsonObject();
      Setting var3 = this.spawnName;
      Object var4 = var3.getValue();
      boolean var5 = var4 instanceof String;
      if (var4 != null && !var5) {
         throw new ClassCastException("CHECKCAST failed");
      }

      var2.addProperty("text", (String)var4);
      var2.addProperty("color", "white");
      String var6 = var2.toString();
      var1.addProperty("CustomName", var6);
      Integer var7 = 10000;
      var1.addProperty("Health", var7);
      Setting var8 = this.spawnInvincible;
      Object var9 = var8.getValue();
      boolean var10 = var9 instanceof Boolean;
      if (var9 != null && !var10) {
         throw new ClassCastException("CHECKCAST failed");
      }

      boolean var11 = (Boolean)var9;
      if (var11) {
         Boolean var12 = true & true;
         var1.addProperty("Invulnerable", var12);
      }

      Setting var13 = this.spawnGlow;
      Object var14 = var13.getValue();
      boolean var15 = var14 instanceof Boolean;
      if (var14 != null && !var15) {
         throw new ClassCastException("CHECKCAST failed");
      }

      boolean var16 = (Boolean)var14;
      if (var16) {
         Boolean var17 = true & true;
         var1.addProperty("Glowing", var17);
      }

      Setting var18 = this.spawnBaby;
      Object var19 = var18.getValue();
      boolean var20 = var19 instanceof Boolean;
      if (var19 != null && !var20) {
         throw new ClassCastException("CHECKCAST failed");
      }

      boolean var21 = (Boolean)var19;
      if (var21) {
         Integer var22 = 0;
         var1.addProperty("Age", var22);
      }

      Boolean var23 = true & true;
      var1.addProperty("PersistenceRequired", var23);
      Boolean var24 = true & true;
      var1.addProperty("NoAI", var24);
      return var1;
   }

   private enum EntityType {
      Villager("villager"),
      Dragon("ender_dragon"),
      Giant("giant"),
      Creeper("creeper"),
      Wither("wither");

      public final String entityTag;

      public String getEntityTag() {
         return this.entityTag;
      }

      EntityType(final String entityTag) {
         this.entityTag = entityTag;
      }
   }
}
