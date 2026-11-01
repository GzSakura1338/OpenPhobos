package org.phobos.core.render;

import java.awt.Color;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.phobos.core.math.MutableBox;
import org.phobos.core.math.MutableVec3d;

public class RenderContext {
   public Matrix4f matrix4f;
   public final MutableBox mutableBox;
   public final Rect4 rect4;
   public final MutableVec3d mutableVec3d;
   public final MutableVec3d mutableVec3d2;
   public float floatValue;
   public final Rect4 rect42 = new Rect4();
   public Matrix4f matrix4f2;
   public MatrixStack matrixStack;
   public static final RenderContext INSTANCE = new RenderContext();
   public Camera camera;

   public Rect4 getRect4() {
      return this.rect4;
   }

   public void setMatrixStack(MatrixStack matrixStack) {
      this.matrixStack = matrixStack;
   }

   public static RenderContext getINSTANCE() {
      return INSTANCE;
   }

   public MutableVec3d getMutableVec3d2() {
      return this.mutableVec3d2;
   }

   public RenderContext() {
      this.rect4 = new Rect4();
      this.mutableVec3d2 = new MutableVec3d();
      this.mutableVec3d = new MutableVec3d();
      this.mutableBox = new MutableBox();
      this.matrixStack = new MatrixStack();
   }

   public void setFloatValue(float f) {
      this.floatValue = f;
   }

   public Camera getCamera() {
      return this.camera;
   }

   public void setMatrix4f(Matrix4f matrix4f) {
      this.matrix4f = matrix4f;
   }

   public MatrixStack getMatrixStack() {
      return this.matrixStack;
   }

   public void handleColor(Color color, Color color2) {
      this.rect42.handleColor(color2);
      this.rect4.handleColor(color);
   }

   public void setCamera(Camera camera) {
      this.camera = camera;
   }

   public Rect4 getRect42() {
      return this.rect42;
   }

   public void setMatrix4f2(Matrix4f matrix4f) {
      this.matrix4f2 = matrix4f;
   }

   public Matrix4f getMatrix4f2() {
      return this.matrix4f2;
   }

   public Matrix4f getMatrix4f() {
      return this.matrix4f;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public MutableVec3d getMutableVec3d() {
      return this.mutableVec3d;
   }

   public void handleFloat(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
      this.rect42.handleFloat(f5, f6, f7, f8);
      this.rect4.handleFloat(f, f2, f3, f4);
   }

   public MutableBox getMutableBox() {
      return this.mutableBox;
   }
}
