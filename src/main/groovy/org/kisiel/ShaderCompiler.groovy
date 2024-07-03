package org.kisiel

import static org.lwjgl.opengl.GL11.GL_TRUE
import static org.lwjgl.opengl.GL20.*

import org.kisiel.exceptions.ShaderCompilationError

class ShaderCompiler {
	static void compile(int shaderId) {
		glCompileShader(shaderId)
		def status = glGetShaderi(shaderId, GL_COMPILE_STATUS)
		if (status != GL_TRUE) {
			def log = glGetShaderInfoLog(shaderId)
			throw new ShaderCompilationError(log)
		}
	}
}
