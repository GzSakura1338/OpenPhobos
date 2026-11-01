package org.phobos.core.render.shader;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.client.gl.PostEffectPass;
import net.minecraft.client.gl.ShaderProgram;
import org.phobos.api.ShaderInfo;
import org.phobos.api.TextureUniform;

public abstract class SamplerVariable extends ShaderVariable implements TextureUniform {
   public ShaderInfo[] shaderInfoArray = new ShaderInfo[0];
   public Object object;
   public int[] intArray = new int[0];

   @Override
   public boolean testList(List<PostEffectPass> list) {
      List<ShaderInfo> list2 = new ArrayList<>(list.size());
      IntList intList = new IntArrayList(list.size());

      for (PostEffectPass postEffectPass : list) {
         JsonEffectShaderProgram jsonEffectShaderProgram = postEffectPass.getProgram();
         ShaderInfo shaderInfo = (ShaderInfo)jsonEffectShaderProgram;
         if (shaderInfo.testText(this.name)) {
            list2.add(shaderInfo);
            intList.add(this.computeShaderInfo(shaderInfo));
         }
      }

      this.shaderInfoArray = list2.toArray(new ShaderInfo[0]);
      this.intArray = intList.toArray(new int[0]);
      this.run();
      return this.shaderInfoArray.length > 0;
   }

   public void run() {
      Object object = this.object;
      if (object != null) {
         this.object = null;
         this.set(object);
      }
   }

   public int computeShaderInfo(ShaderInfo shaderInfo) {
      return shaderInfo.getList2().get(shaderInfo.getList().indexOf(this.name));
   }

   public abstract void set(Object var1);

   public boolean testShaderProgram(ShaderInfo shaderInfo) {
      if (shaderInfo.testText(this.name)) {
         this.shaderInfoArray = new ShaderInfo[]{shaderInfo};
         this.intArray = new int[]{this.computeShaderInfo(shaderInfo)};
         this.run();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean testShaderProgram(ShaderProgram shaderProgram) {
      return this.testShaderProgram((ShaderInfo)shaderProgram);
   }

   public SamplerVariable(String text) {
      super(text);
   }
}
