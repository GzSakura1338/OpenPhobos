package org.phobos.api;

public interface UniformLookup {
   IntUniform4 resolveIntUniform4(String var1);

   FloatUniform4 resolveFloatUniform4(String var1);

   TextureUniform resolveTextureUniform(String var1);

   IntUniform1 resolveIntUniform1(String var1);

   FloatUniform3 resolveFloatUniform3(String var1);

   MatrixUniform resolveMatrixUniform(String var1);

   IntUniform3 resolveIntUniform3(String var1);

   IntUniform2 resolveIntUniform2(String var1);

   FloatUniform1 resolveFloatUniform1(String var1);

   FloatUniform2 resolveFloatUniform2(String var1);
}
