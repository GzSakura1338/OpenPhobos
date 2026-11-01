package org.phobos.core.loader;

import lombok.SneakyThrows;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.UnaryOperator;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.fabricmc.loader.impl.launch.knot.Knot;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.ClassTransformerRegistry;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.transformer.Config;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;

public class MixinConfigRegistrar {
   public final ClassTransformerManager classTransformerManager = new ClassTransformerManager();
   public static final MixinConfigRegistrar INSTANCE = new MixinConfigRegistrar();
   public final AtomicBoolean atomicBoolean = new AtomicBoolean();

   public void handleUnaryOperator(UnaryOperator<IMixinTransformer> unaryOperator) {
      try {
         IMixinTransformer iMixinTransformer = getIMixinTransformer();
         iMixinTransformer = unaryOperator.apply(iMixinTransformer);
         handleIMixinTransformer(iMixinTransformer);
      } catch (Exception var3) {
      }
   }

   @SneakyThrows
   public static void handleConfig(@Nullable Config config) throws NoSuchFieldException, IllegalAccessException {
      Field field = Mixins.class.getDeclaredField("registeredConfigs");
      field.setAccessible(true);
      Set<String> set = (Set<String>)field.get(null);
      if (config != null && !set.contains(config.getName())) {
         MixinEnvironment mixinEnvironment = config.getEnvironment();
         if (mixinEnvironment != null) {
            Method method = MixinEnvironment.class.getDeclaredMethod("registerConfig", String.class);
            method.setAccessible(true);
            method.invoke(mixinEnvironment, config.getName());
         }

         handleConfig2(config);
         set.add(config.getName());
         Config config2 = config.getParent();
         if (config2 != null) {
            handleConfig(config2);
         }
      }
   }

   public static void handleConfig2(Config config) {
      try {
         Method method2 = Config.class.getDeclaredMethod("get");
         method2.setAccessible(true);
         Object object = method2.invoke(config);
         Class<?> classValue = Class.forName("org.spongepowered.asm.mixin.transformer.MixinConfig");
         Method method3 = classValue.getDeclaredMethod("select", MixinEnvironment.class);
         method3.setAccessible(true);
         if ((Boolean)method3.invoke(object, MixinEnvironment.getDefaultEnvironment())) {
            Method method = classValue.getDeclaredMethod("onSelect");
            method.setAccessible(true);
            method.invoke(object);
            getList().add(object);
         }
      } catch (Exception var6) {
      }
   }

   public static MixinConfigRegistrar getINSTANCE() {
      return INSTANCE;
   }

   @SneakyThrows
   public static IMixinTransformer getIMixinTransformer() {
      Object object = getObject();
      Field field = getField();
      field.setAccessible(true);
      return (IMixinTransformer)field.get(object);
   }

   @SneakyThrows
   public static void handleText(String text) throws NoSuchFieldException, IllegalAccessException {
      Config config = null;

      try {
         config = Config.create(text, MixinEnvironment.getDefaultEnvironment());
      } catch (Exception var3) {
      }

      handleConfig(config);
   }

   @SneakyThrows
   public static Object getObject() {
      Knot knot = (Knot)FabricLauncherBase.getLauncher();
      Field field = Knot.class.getDeclaredField("classLoader");
      field.setAccessible(true);
      return field.get(knot);
   }

   @SneakyThrows
   public static Field getField() {
      Class<?> classValue = Class.forName("net.fabricmc.loader.impl.launch.knot.KnotClassDelegate");
      return classValue.getDeclaredField("mixinTransformer");
   }

   @SneakyThrows
   public static IMixinTransformer getIMixinTransformer2() {
      IMixinTransformer iMixinTransformer = getIMixinTransformer();
      Class<?> classValue2 = Class.forName("org.spongepowered.asm.mixin.transformer.MixinTransformer");

      while (!classValue2.isInstance(iMixinTransformer)) {
         Class<?> classValue = iMixinTransformer.getClass();
         boolean flag = false;

         while (classValue != Object.class) {
            try {
               Field field = classValue.getDeclaredField("delegate");
               field.setAccessible(true);
               iMixinTransformer = (IMixinTransformer)field.get(iMixinTransformer);
               flag = true;
               break;
            } catch (NoSuchFieldException | ClassCastException exception) {
               classValue = classValue.getSuperclass();
            }
         }

         if (!flag) {
            throw new NoSuchFieldException("Failed to find delegate field on " + iMixinTransformer.getClass());
         }
      }

      return iMixinTransformer;
   }

   @SneakyThrows
   public static void handleIMixinTransformer(IMixinTransformer iMixinTransformer) throws IllegalAccessException {
      Object object = getObject();
      Field field = getField();
      field.setAccessible(true);
      field.set(object, iMixinTransformer);
   }

   public ClassTransformerRegistry getClassTransformerRegistry() {
      synchronized (this.atomicBoolean) {
         if (!this.atomicBoolean.getAndSet(true)) {
            this.handleUnaryOperator(iMixinTransformer -> new ClassTransformerManager.MixinTransformers(iMixinTransformer, this.classTransformerManager));
         }
      }

      return this.classTransformerManager;
   }

   @SneakyThrows
   public static List<Object> getList() {
      IMixinTransformer iMixinTransformer = getIMixinTransformer2();
      Class<?> classValue = Class.forName("org.spongepowered.asm.mixin.transformer.MixinTransformer");
      Field field = classValue.getDeclaredField("processor");
      field.setAccessible(true);
      Object object = field.get(iMixinTransformer);
      Class<?> classValue2 = Class.forName("org.spongepowered.asm.mixin.transformer.MixinProcessor");
      Field field2 = classValue2.getDeclaredField("pendingConfigs");
      field2.setAccessible(true);
      return (List<Object>)field2.get(object);
   }
}
