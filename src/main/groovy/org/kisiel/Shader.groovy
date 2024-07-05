package org.kisiel


import static org.lwjgl.opengl.GL20.glDeleteShader
import static org.lwjgl.opengl.GL20.glShaderSource

abstract class Shader {
	int shaderId

	void loadShader(ResourcesLoader loader, String resourceName, Closure<Integer> factory) {
		def shaderCode = loader.getResource(resourceName)
		shaderId = factory()
		glShaderSource(shaderId, shaderCode)
		ShaderCompiler.compile(shaderId)
	}

	void delete() {
		glDeleteShader(shaderId)
	}
}
