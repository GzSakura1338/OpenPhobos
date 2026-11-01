package org.phobos.core.render.shader;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.PostEffectPass;
import net.minecraft.client.gl.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.phobos.api.IntUniform4;
import org.phobos.api.IntUniform1;
import org.phobos.api.FloatUniform1;
import org.phobos.api.IntUniform3;
import org.phobos.api.IntUniform2;
import org.phobos.api.FloatUniform2;
import org.phobos.api.FloatUniform3;
import org.phobos.api.MatrixUniform;
import org.phobos.api.FloatUniform4;

public final class UniformVariable
   extends ShaderVariable
   implements IntUniform1,
   IntUniform2,
   IntUniform3,
   IntUniform4,
   FloatUniform1,
   FloatUniform2,
   FloatUniform3,
   FloatUniform4,
   MatrixUniform {
   public int intValue;
   public GlUniform[] glUniformArray;
   public float floatValue;
   public static final GlUniform[] glUniformArray2 = new GlUniform[0];
   public final int intValue2;
   public float floatValue2;
   public int intValue3;
   public int intValue4;
   public boolean flag;
   public float floatValue3;
   public int intValue5;
   public float floatValue4;

   @Override
   public void handleVector3f(Vector3f vector3f) {
      this.handleFloat2(vector3f.x(), vector3f.y(), vector3f.z());
   }

   @Override
   public void handleFloat3(float f, float f2, float f3, float f4) {
      GlUniform[] glUniforms = this.glUniformArray;
      int i = glUniforms.length;
      if (i > 0 && (this.flag || this.floatValue2 != f || this.floatValue != f2 || this.floatValue3 != f3 || this.floatValue4 != f4)) {
         for (GlUniform glUniform : glUniforms) {
            glUniform.set(f, f2, f3, f4);
         }

         this.floatValue2 = f;
         this.floatValue = f2;
         this.floatValue3 = f3;
         this.floatValue4 = f4;
         this.flag = false;
      }
   }

   @Override
   public void handleFloat(float f, float f2) {
      GlUniform[] glUniforms = this.glUniformArray;
      int i = glUniforms.length;
      if (i > 0 && (this.flag || this.floatValue2 != f || this.floatValue != f2)) {
         for (GlUniform glUniform : glUniforms) {
            glUniform.set(f, f2);
         }

         this.floatValue2 = f;
         this.floatValue = f2;
         this.flag = false;
      }
   }

   @Override
   public void handleVector2f(Vector2f vector2f) {
      this.handleFloat(vector2f.x(), vector2f.y());
   }

   @Override
   public void handleMatrix4f(Matrix4f matrix4f) {
      GlUniform[] glUniforms = this.glUniformArray;
      int i = glUniforms.length;
      if (i > 0) {
         for (GlUniform glUniform : glUniforms) {
            glUniform.set(matrix4f);
         }
      }
   }

   public void run() {
      if (!this.flag) {
         for (GlUniform glUniform : this.glUniformArray) {
            if (glUniform.getIntData() != null) {
               glUniform.setForDataType(this.intValue5, this.intValue3, this.intValue, this.intValue4);
            } else {
               assert glUniform.getFloatData() != null;
               glUniform.setForDataType(this.floatValue2, this.floatValue, this.floatValue3, this.floatValue4);
            }
         }
      }
   }

   @Override
   public boolean testList(List<PostEffectPass> list) {
      List<GlUniform> list2 = new ArrayList<>();

      for (PostEffectPass postEffectPass : list) {
         GlUniform glUniform = postEffectPass.getProgram().getUniformByName(this.name);
         if (glUniform != null) {
            if (glUniform.getCount() != this.intValue2) {
               throw new IllegalStateException(
                  "Mismatched number of values, expected " + this.intValue2 + " but JSON definition declares " + glUniform.getCount()
               );
            }

            list2.add(glUniform);
         }
      }

      if (!list2.isEmpty()) {
         this.glUniformArray = list2.toArray(new GlUniform[0]);
         this.run();
         return true;
      } else {
         this.glUniformArray = glUniformArray2;
         return false;
      }
   }

   @Override
   public void handleInt2(int i, int i2) {
      GlUniform[] glUniforms = this.glUniformArray;
      int i3 = glUniforms.length;
      if (i3 > 0 && (this.flag || this.intValue5 != i || this.intValue3 != i2)) {
         for (GlUniform glUniform : glUniforms) {
            glUniform.set(i, i2);
         }

         this.intValue5 = i;
         this.intValue3 = i2;
         this.flag = false;
      }
   }

   @Override
   public void handleFloat4(float[] floats) {
      if (this.intValue2 != floats.length) {
         throw new IllegalArgumentException("Mismatched values size, expected " + this.intValue2 + " but got " + floats.length);
      }

      GlUniform[] glUniforms = this.glUniformArray;
      int i = glUniforms.length;
      if (i > 0) {
         for (GlUniform glUniform : glUniforms) {
            glUniform.set(floats);
         }
      }
   }

   @Override
   public void handleInt(int i, int i2, int i3, int i4) {
      GlUniform[] glUniforms = this.glUniformArray;
      int i5 = glUniforms.length;
      if (i5 > 0 && (this.flag || this.intValue5 != i || this.intValue3 != i2 || this.intValue != i3 || this.intValue4 != i4)) {
         for (GlUniform glUniform : glUniforms) {
            glUniform.set(i, i2, i3, i4);
         }

         this.intValue5 = i;
         this.intValue3 = i2;
         this.intValue = i3;
         this.intValue4 = i4;
         this.flag = false;
      }
   }

   @Override
   public void handleInt3(int i) {
      GlUniform[] glUniforms = this.glUniformArray;
      int i2 = glUniforms.length;
      if (i2 > 0 && (this.flag || this.intValue5 != i)) {
         for (GlUniform glUniform : glUniforms) {
            glUniform.set(i);
         }

         this.intValue5 = i;
         this.flag = false;
      }
   }

   @Override
   public void handleFloat5(float f) {
      GlUniform[] glUniforms = this.glUniformArray;
      int i = glUniforms.length;
      if (i > 0 && (this.flag || this.floatValue2 != f)) {
         for (GlUniform glUniform : glUniforms) {
            glUniform.set(f);
         }

         this.floatValue2 = f;
         this.flag = false;
      }
   }

   @Override
   public void handleFloat2(float f, float f2, float f3) {
      GlUniform[] glUniforms = this.glUniformArray;
      int i = glUniforms.length;
      if (i > 0 && (this.flag || this.floatValue2 != f || this.floatValue != f2 || this.floatValue3 != f3)) {
         for (GlUniform glUniform : glUniforms) {
            glUniform.set(f, f2, f3);
         }

         this.floatValue2 = f;
         this.floatValue = f2;
         this.floatValue3 = f3;
         this.flag = false;
      }
   }

   @Override
   public boolean testShaderProgram(ShaderProgram shaderProgram) {
      GlUniform glUniform = shaderProgram.getUniform(this.name);
      if (glUniform != null) {
         this.glUniformArray = new GlUniform[]{glUniform};
         this.run();
         return true;
      } else {
         this.glUniformArray = glUniformArray2;
         return false;
      }
   }

   public UniformVariable(String text, int i) {
      super(text);
      this.glUniformArray = glUniformArray2;
      this.flag = true;
      this.intValue2 = i;
   }

   @Override
   public void handleVector4f(Vector4f vector4f) {
      this.handleFloat3(vector4f.x(), vector4f.y(), vector4f.z(), vector4f.w());
   }

   @Override
   public void handleInt4(int i, int i2, int i3) {
      GlUniform[] glUniforms = this.glUniformArray;
      int i4 = glUniforms.length;
      if (i4 > 0 && (this.flag || this.intValue5 != i || this.intValue3 != i2 || this.intValue != i3)) {
         for (GlUniform glUniform : glUniforms) {
            glUniform.set(i, i2, i3);
         }

         this.intValue5 = i;
         this.intValue3 = i2;
         this.intValue = i3;
         this.flag = false;
      }
   }
}
