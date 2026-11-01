package org.phobos.core;

import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Range;
import org.phobos.api.MinecraftHolder;
import org.phobos.module.combat.CriticalsModule;
import org.phobos.core.module.ModuleLookup;

public class PhobosSounds implements MinecraftHolder {
   public final Identifier identifier;
   public SoundEvent soundEvent;
   public final Identifier identifier2;
   public SoundEvent soundEvent2;
   public SoundEvent soundEvent3;
   public SoundEvent soundEvent4;
   public final Identifier identifier3;
   public SoundEvent soundEvent5;
   public SoundEvent soundEvent6;
   public SoundEvent soundEvent7;
   public final Identifier identifier4;
   public SoundEvent soundEvent8;
   public final Identifier identifier5;
   public final Identifier identifier6;
   public SoundEvent soundEvent9;
   public final Identifier identifier7;
   public SoundEvent soundEvent10;
   public SoundEvent soundEvent11;
   public final Identifier identifier8;
   public final Identifier identifier9;
   public final Identifier identifier10;
   public SoundEvent soundEvent12;
   public final Identifier identifier11;
   public final Identifier identifier12 = Identifier.of("phoboslite:hard");

   public PhobosSounds() {
      this.soundEvent3 = SoundEvent.of(this.identifier12);
      this.identifier9 = Identifier.of("phoboslite:metal");
      this.soundEvent10 = SoundEvent.of(this.identifier9);
      this.identifier11 = Identifier.of("phoboslite:normal");
      this.soundEvent5 = SoundEvent.of(this.identifier11);
      this.identifier8 = Identifier.of("phoboslite:overwatch");
      this.soundEvent11 = SoundEvent.of(this.identifier8);
      this.identifier3 = Identifier.of("phoboslite:shot");
      this.soundEvent9 = SoundEvent.of(this.identifier3);
      this.identifier10 = Identifier.of("phoboslite:soft");
      this.soundEvent4 = SoundEvent.of(this.identifier10);
      this.identifier4 = Identifier.of("phoboslite:sword");
      this.soundEvent7 = SoundEvent.of(this.identifier4);
      this.identifier2 = Identifier.of("phoboslite:wood");
      this.soundEvent6 = SoundEvent.of(this.identifier2);
      this.identifier7 = Identifier.of("phoboslite:vine");
      this.soundEvent = SoundEvent.of(this.identifier7);
      this.identifier5 = Identifier.of("phobos:click");
      this.soundEvent12 = SoundEvent.of(this.identifier5);
      this.identifier = Identifier.of("phobos:switch");
      this.soundEvent8 = SoundEvent.of(this.identifier);
      this.identifier6 = Identifier.of("phobos:lobotomy");
      this.soundEvent2 = SoundEvent.of(this.identifier6);
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, boolean flag) {
      if ((!flag || !ModuleLookup.createModuleHandle(CriticalsModule.class).check())
         && (
            clientPlayerEntity.isSprinting()
               || !(clientPlayerEntity.fallDistance > 0.0F)
               || clientPlayerEntity.isOnGround()
               || clientPlayerEntity.isClimbing()
               || clientPlayerEntity.isSubmergedInWater()
               || clientPlayerEntity.hasStatusEffect(StatusEffects.BLINDNESS)
               || clientPlayerEntity.getControllingVehicle() != null
         )) {
         if (clientPlayerEntity.getAttackCooldownProgress(0.5F) > 0.9) {
            clientWorld.playSound(
               clientPlayerEntity,
               clientPlayerEntity.getX(),
               clientPlayerEntity.getY(),
               clientPlayerEntity.getZ(),
               SoundEvents.ENTITY_PLAYER_ATTACK_STRONG,
               clientPlayerEntity.getSoundCategory(),
               1.0F,
               1.0F
            );
         } else {
            clientWorld.playSound(
               clientPlayerEntity,
               clientPlayerEntity.getX(),
               clientPlayerEntity.getY(),
               clientPlayerEntity.getZ(),
               SoundEvents.ENTITY_PLAYER_ATTACK_WEAK,
               clientPlayerEntity.getSoundCategory(),
               1.0F,
               1.0F
            );
         }
      } else {
         clientWorld.playSound(
            clientPlayerEntity,
            clientPlayerEntity.getX(),
            clientPlayerEntity.getY(),
            clientPlayerEntity.getZ(),
            SoundEvents.ENTITY_PLAYER_ATTACK_CRIT,
            clientPlayerEntity.getSoundCategory(),
            1.0F,
            1.0F
         );
      }
   }

   public void handleText(String text, String text2, @Range(from = 0L, to = 100L) int i) {
      try {
         InputStream inputStream = PhobosSounds.class.getResourceAsStream("/assets/minecraft/" + text + "/sounds/" + text2 + ".wav");
         if (inputStream == null) {
            throw new NullPointerException("Resource stream not found.");
         }

         InputStream inputStream2 = new BufferedInputStream(inputStream);
         AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream2);
         Clip clip = AudioSystem.getClip();
         clip.open(audioInputStream);
         FloatControl floatControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
         float f = (float)(Math.log(i / 100.0) / Math.log(10.0) * 20.0);
         floatControl.setValue(f);
         clip.start();
      } catch (Exception var10) {
      }
   }

   public void handleSoundEvent(SoundEvent soundEvent, float f) {
      if (mc.world != null && mc.player != null) {
         Vec3d vec3d = mc.player.getEyePos();
         mc.world.playSound(vec3d.getX(), vec3d.getY(), vec3d.getZ(), soundEvent, SoundCategory.MASTER, f, 1.0F, false);
      }
   }

   public PhobosSounds getPhobosSounds() {
      Registry.register(Registries.SOUND_EVENT, this.identifier12, this.soundEvent3);
      Registry.register(Registries.SOUND_EVENT, this.identifier9, this.soundEvent10);
      Registry.register(Registries.SOUND_EVENT, this.identifier11, this.soundEvent5);
      Registry.register(Registries.SOUND_EVENT, this.identifier8, this.soundEvent11);
      Registry.register(Registries.SOUND_EVENT, this.identifier3, this.soundEvent9);
      Registry.register(Registries.SOUND_EVENT, this.identifier10, this.soundEvent4);
      Registry.register(Registries.SOUND_EVENT, this.identifier4, this.soundEvent7);
      Registry.register(Registries.SOUND_EVENT, this.identifier2, this.soundEvent6);
      Registry.register(Registries.SOUND_EVENT, this.identifier7, this.soundEvent);
      Registry.register(Registries.SOUND_EVENT, this.identifier5, this.soundEvent12);
      Registry.register(Registries.SOUND_EVENT, this.identifier, this.soundEvent8);
      Registry.register(Registries.SOUND_EVENT, this.identifier6, this.soundEvent2);
      return this;
   }

   public interface AbstractSound {
      SoundEvent resolveSoundEvent(PhobosClient var1);
   }

   public enum Sound implements PhobosSounds.AbstractSound {
      Hard {
         @Override
         public SoundEvent resolveSoundEvent(PhobosClient phobos) {
            return phobos.getPhobosSounds().soundEvent3;
         }
      },
      Metal {
         @Override
         public SoundEvent resolveSoundEvent(PhobosClient phobos) {
            return phobos.getPhobosSounds().soundEvent10;
         }
      },
      Normal {
         @Override
         public SoundEvent resolveSoundEvent(PhobosClient phobos) {
            return phobos.getPhobosSounds().soundEvent5;
         }
      },
      Overwatch {
         @Override
         public SoundEvent resolveSoundEvent(PhobosClient phobos) {
            return phobos.getPhobosSounds().soundEvent11;
         }
      },
      Shot {
         @Override
         public SoundEvent resolveSoundEvent(PhobosClient phobos) {
            return phobos.getPhobosSounds().soundEvent9;
         }
      },
      Soft {
         @Override
         public SoundEvent resolveSoundEvent(PhobosClient phobos) {
            return phobos.getPhobosSounds().soundEvent4;
         }
      },
      Sword {
         @Override
         public SoundEvent resolveSoundEvent(PhobosClient phobos) {
            return phobos.getPhobosSounds().soundEvent7;
         }
      },
      Wood {
         @Override
         public SoundEvent resolveSoundEvent(PhobosClient phobos) {
            return phobos.getPhobosSounds().soundEvent6;
         }
      };

      public void play(PhobosClient phobos, float volume) {
         phobos.getPhobosSounds().handleSoundEvent(this.resolveSoundEvent(phobos), volume);
      }
   }
}
