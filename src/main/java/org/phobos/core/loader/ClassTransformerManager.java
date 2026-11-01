package org.phobos.core.loader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.phobos.api.ClassTransformerRegistry;
import org.phobos.api.ClassTransformerSpec;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import org.spongepowered.asm.mixin.transformer.ext.IExtensionRegistry;
import org.spongepowered.asm.transformers.MixinClassWriter;

public class ClassTransformerManager implements ClassTransformerRegistry {
   public final Map<String, List<ClassTransformerSpec>> textMap = new HashMap<>();

   @Override
   public void handleClassTransformerSpec(ClassTransformerSpec classTransformerSpec) {
      for (String text : classTransformerSpec.getTextArray()) {
         this.textMap.computeIfAbsent(text.replace(".", "/"), textx -> new CopyOnWriteArrayList<>()).add(classTransformerSpec);
      }
   }

   public byte[] values(String text, byte[] bytes) {
      List<ClassTransformerSpec> list = this.textMap.get(text.replace(".", "/"));
      if (list == null) {
         return bytes;
      }

      ClassNode classNode = new ClassNode();
      ClassReader classReader = new ClassReader(bytes);
      classReader.accept(classNode, 8);

      for (ClassTransformerSpec classTransformerSpec : list) {
         classTransformerSpec.handleClassNode(classNode);
      }

      ClassWriter classWriter = new MixinClassWriter(classReader, 2);
      classNode.accept(classWriter);
      return classWriter.toByteArray();
   }

   private static class DelegatingMixinTransformer implements IMixinTransformer {
      public final IMixinTransformer iMixinTransformer;

      public boolean generateClass(MixinEnvironment mixinEnvironment, String text, ClassNode classNode) {
         return this.iMixinTransformer.generateClass(mixinEnvironment, text, classNode);
      }

      public byte[] transformClass(MixinEnvironment mixinEnvironment, String text, byte[] bytes) {
         return this.iMixinTransformer.transformClass(mixinEnvironment, text, bytes);
      }

      public DelegatingMixinTransformer(IMixinTransformer iMixinTransformer) {
         this.iMixinTransformer = iMixinTransformer;
      }

      public boolean computeFramesForClass(MixinEnvironment mixinEnvironment, String text, ClassNode classNode) {
         return this.iMixinTransformer.computeFramesForClass(mixinEnvironment, text, classNode);
      }

      public List<String> reload(String text, ClassNode classNode) {
         return this.iMixinTransformer.reload(text, classNode);
      }

      public void audit(MixinEnvironment mixinEnvironment) {
         this.iMixinTransformer.audit(mixinEnvironment);
      }

      public IExtensionRegistry getExtensions() {
         return this.iMixinTransformer.getExtensions();
      }

      public boolean transformClass(MixinEnvironment mixinEnvironment, String text, ClassNode classNode) {
         return this.iMixinTransformer.transformClass(mixinEnvironment, text, classNode);
      }

      public byte[] transformClassBytes(String text, String text2, byte[] bytes) {
         return this.iMixinTransformer.transformClassBytes(text, text2, bytes);
      }

      public byte[] generateClass(MixinEnvironment mixinEnvironment, String text) {
         return this.iMixinTransformer.generateClass(mixinEnvironment, text);
      }
   }

   public static class MixinTransformers extends ClassTransformerManager.DelegatingMixinTransformer {
      public final ClassTransformerManager classTransformerManager;

      @Override
      public boolean transformClass(final MixinEnvironment mixinEnvironment, final String text, final ClassNode classNode) {
         return super.transformClass(mixinEnvironment, text, classNode);
      }

      @Override
      public IExtensionRegistry getExtensions() {
         return super.getExtensions();
      }

      @Override
      public boolean computeFramesForClass(final MixinEnvironment mixinEnvironment, final String text, final ClassNode classNode) {
         return super.computeFramesForClass(mixinEnvironment, text, classNode);
      }

      @Override
      public byte[] transformClassBytes(String text, String text2, byte[] bytes) {
         bytes = this.values(text, text2, bytes);
         return this.classTransformerManager.values(text, bytes);
      }

      public byte[] values(String text, String text2, byte[] bytes) {
         return super.transformClassBytes(text, text2, bytes);
      }

      @Override
      public boolean generateClass(final MixinEnvironment mixinEnvironment, final String text, final ClassNode classNode) {
         return super.generateClass(mixinEnvironment, text, classNode);
      }

      public MixinTransformers(IMixinTransformer iMixinTransformer, ClassTransformerManager classTransformerManager) {
         super(iMixinTransformer);
         this.classTransformerManager = classTransformerManager;
      }

      @Override
      public byte[] transformClass(final MixinEnvironment mixinEnvironment, final String text, final byte[] bytes) {
         return super.transformClass(mixinEnvironment, text, bytes);
      }

      @Override
      public void audit(final MixinEnvironment mixinEnvironment) {
         super.audit(mixinEnvironment);
      }

      @Override
      public List reload(final String text, final ClassNode classNode) {
         return super.reload(text, classNode);
      }

      @Override
      public byte[] generateClass(final MixinEnvironment mixinEnvironment, final String text) {
         return super.generateClass(mixinEnvironment, text);
      }
   }
}
