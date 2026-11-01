package org.phobos.api;

import net.minecraft.client.gl.ShaderProgram;

public interface ShaderProgramHandle extends UniformLookup {
   void run();

   ShaderProgram getShaderProgram();
}
