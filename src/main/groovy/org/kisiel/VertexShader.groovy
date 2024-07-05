package org.kisiel

import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER
import static org.lwjgl.opengl.GL20.glCreateShader
import static org.lwjgl.opengl.GL20.glDeleteShader
import static org.lwjgl.opengl.GL20.glShaderSource

class VertexShader implements Shader {
	int shaderId

	VertexShader(ResourcesLoader loader, String resourceName) {
		def shaderCode = loader.getResource(resourceName)
		shaderId = glCreateShader(GL_VERTEX_SHADER)
		glShaderSource(shaderId, shaderCode)
		ShaderCompiler.compile(shaderId)
	}

	@Override
	void delete() {
		glDeleteShader(shaderId)
	}
}
