package org.phobos.event;

import java.awt.Color;
import java.util.function.Supplier;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import org.phobos.core.entity.PositionRotationTracker;
import org.phobos.core.render.TrailEffect;
import org.phobos.core.render.ColorUtils;
import org.phobos.module.Module;
import org.phobos.module.misc.ParticlesModule;
import org.phobos.module.misc.ParticlesModule;

public final class ParticlesAttackListener extends ModuleListener<ParticlesModule, AttackEvent2> {
   public static Module resolveModule(ParticlesAttackListener particlesAttackListener) {
      return particlesAttackListener.module;
   }

   public Supplier<PositionRotationTracker> getSupplier() {
      long l = System.currentTimeMillis();
      long l2 = this.module.time.getValue();
      return () -> new PositionRotationTracker() {
         @Override
         public boolean alwaysTrue() {
            return ((ParticlesModule)ParticlesAttackListener.resolveModule(ParticlesAttackListener.this)).square.getValue();
         }

         @Override
         public Color getColor() {
            float f = ((ParticlesModule)ParticlesAttackListener.resolveModule3(ParticlesAttackListener.this)).color.getValue().getAlpha();
            float f2 = f / (float)l2;
            float f3 = MathHelper.clamp((int)(f2 * (float)(l + l2 - System.currentTimeMillis())), 0, (int)f) / 255.0F;
            return ColorUtils.createColor6(((ParticlesModule)ParticlesAttackListener.resolveModule2(ParticlesAttackListener.this)).color.getValue(), f3);
         }

         @Override
         public void run() {
            this.setVec3d(this.getVec3d2());
            this.setVec3d2(this.getVec3d2().add(this.getVec3d3()));
            this.setVec3d3(this.getVec3d3().multiply(0.7F));
         }
      };
   }

   public static Module resolveModule2(ParticlesAttackListener particlesAttackListener) {
      return particlesAttackListener.module;
   }

   public static Module resolveModule3(ParticlesAttackListener particlesAttackListener) {
      return particlesAttackListener.module;
   }

   public ParticlesAttackListener(ParticlesModule particlesModule) {
      super(particlesModule, AttackEvent2.class);
   }

   public void handleObject2(AttackEvent2 attackEvent2) {
      switch ((ParticlesModule.Mode)this.module.mode.getValue()) {
         case Always:
            for (int i = 0; i < this.module.times.getValue(); i++) {
               mc.particleManager.addEmitter(attackEvent2.getEntity(), ParticleTypes.CRIT);
            }
            break;
         case Custom:
            TrailEffect trailEffect = new TrailEffect(
               this.phobosClient, this.getSupplier(), attackEvent2.getEntity(), 3, this.module.time.getValue()
            );
            this.phobosClient.getEventBus().handleObject(trailEffect);
      }
   }
}
