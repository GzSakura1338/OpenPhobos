package org.phobos.module.client;

import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.StringSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.module.Module;

public class FakePlayerModule2 extends Module {
   public final Setting<Boolean> damage;
   public static FakePlayerModule2 fakePlayerModule2;
   public final Setting<Boolean> knockback = this.resolveSetting2(new BooleanSetting("Knockback"));
   public final Setting<FakePlayerModule2.Movement> movement;
   public final Setting<Boolean> heal;
   public final Setting<String> name2;
   public final Setting<Integer> limit;

   public FakePlayerModule2(PhobosClient phobosClient) {
      super(phobosClient, "FakePlayer", Categories.client, "Manages fake players");
      this.damage = this.resolveSetting2(new BooleanSetting("Damage", true));
      this.heal = this.resolveSetting2(new BooleanSetting("Heal", true));
      this.limit = this.resolveSetting2(new NumberSetting<>("Limit", 10, 1, 100).resolveSetting7("Maximum amount you can spawn"));
      this.name2 = this.resolveSetting2(new StringSetting("Name", "FakePlayer").resolveSetting7("FakePlayer name"));
      this.movement = this.resolveSetting2(new EnumSetting<>("Movement", FakePlayerModule2.Movement.None));
      fakePlayerModule2 = this;
   }

   public static FakePlayerModule2 getFakePlayerModule2() {
      return fakePlayerModule2;
   }

   public Setting<Boolean> getKnockback() {
      return this.knockback;
   }

   public Setting<Integer> getLimit() {
      return this.limit;
   }

   public Setting<Boolean> getHeal() {
      return this.heal;
   }

   public Setting<String> getName2() {
      return this.name2;
   }

   public Setting<FakePlayerModule2.Movement> getMovement() {
      return this.movement;
   }

   public Setting<Boolean> getDamage() {
      return this.damage;
   }

   public enum Movement {
      None,
      Forward,
      Procedural;
   }
}
