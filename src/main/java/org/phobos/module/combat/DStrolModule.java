package org.phobos.module.combat;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.tick.TimerManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.DStrolAttackListener;
import org.phobos.event.DStrolPlayerTickListener;
import org.phobos.module.Module;

public class DStrolModule extends Module {
   public final Setting<Float> releaseMultiplier;
   public final Setting<DStrolModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", DStrolModule.Mode.Evaluate));
   public final Setting<Double> stretch = this.resolveSetting2(
      new NumberSetting<>("Stretch", 2.8, 0.1, 4.0).resolveSetting4(this.mode, DStrolModule.Mode.Distance)
   );
   public final Setting<Integer> ticks;
   public final Setting<Double> control = this.resolveSetting2(
      new NumberSetting<>("Control", 3.3, 0.1, 4.0).resolveSetting4(this.mode, DStrolModule.Mode.Evaluate)
   );
   public final Setting<Integer> combatTicks;
   public final Setting<Float> factor;
   public final Setting<Double> distance = this.resolveSetting2(
      new NumberSetting<>("Distance", 3.6, 3.1, 6.0).resolveSetting4(this.mode, DStrolModule.Mode.Evaluate)
   );
   public final Setting<Integer> releaseTicks;
   public final Setting<Boolean> release;
   public int intValue;
   public boolean flag;
   public final Setting<Boolean> combat;

   public DStrolModule(PhobosClient phobosClient) {
      super(phobosClient, "DStrol", Categories.combat, "Controls time and distance to combo targets");
      this.ticks = this.resolveSetting2(new NumberSetting<>("Ticks", 4, 1, 10).resolveSetting4(this.mode, DStrolModule.Mode.Single));
      this.factor = this.resolveSetting2(new NumberSetting<>("Factor", 0.2F, 0.1F, 0.7F));
      this.release = this.resolveSetting2(
         new BooleanSetting("Release").resolveSetting3(this.resolveVisibility2(this.mode, DStrolModule.Mode.Evaluate, DStrolModule.Mode.Distance))
      );
      this.releaseTicks = this.resolveSetting2(new NumberSetting<>("ReleaseTicks", 2, 1, 5));
      this.releaseMultiplier = this.resolveSetting2(new NumberSetting<>("ReleaseMultiplier", 1.1F, 1.01F, 1.3F, "%"));
      this.combat = this.resolveSetting2(new BooleanSetting("Combat"));
      this.combatTicks = this.resolveSetting2(new NumberSetting<>("CombatTicks", 2, 1, 5));
      this.flag = false;
      this.intValue = 0;
      this.resolveSettingGroup(this.release, this.releaseTicks, this.releaseMultiplier);
      this.resolveSettingGroup(this.combat, this.combatTicks);
      this.handleListener(new DStrolPlayerTickListener(this));
      this.handleListener(new DStrolAttackListener(this));
   }

   public double computeClientWorld(ClientWorld clientWorld, Entity entity) {
      Vec3d vec3d = entity.getPos();
      Vec3d vec3d2 = vec3d.add(0.0, -6.0, 0.0);
      BlockHitResult blockHitResult = clientWorld.raycast(new RaycastContext(vec3d, vec3d2, ShapeType.OUTLINE, FluidHandling.NONE, entity));
      return blockHitResult == null ? Double.longBitsToDouble(9221120237041090560L) : vec3d.distanceTo(blockHitResult.getPos());
   }

   @Override
   public void run4() {
      this.run2();
   }

   public boolean testDouble(double d) {
      return d >= this.distance.getValue() && this.intValue > 0;
   }

   public void run() {
      if (this.intValue > this.releaseTicks.getValue()) {
         this.intValue = this.releaseTicks.getValue();
      }

      if (this.intValue-- >= 0) {
         this.phobosClient.getTimerManager().handleFloat(this.releaseMultiplier.getValue(), TimerManager.TimerPriority.High);
      }
   }

   public void run2() {
      this.intValue = 0;
      this.phobosClient.getTimerManager().run2();
   }

   public void run5() {
      this.phobosClient.getTimerManager().handleFloat(this.factor.getValue(), TimerManager.TimerPriority.High);
      this.intValue++;
   }

   public boolean testDouble2(double d) {
      return d >= this.control.getValue() && d <= this.distance.getValue();
   }

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   public enum Mode {
      Evaluate,
      Single,
      Distance;
   }
}
