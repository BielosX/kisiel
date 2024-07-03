package org.kisiel

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER
import static org.lwjgl.opengl.GL20.glCreateShader
import static org.lwjgl.opengl.GL20.glShaderSource

class FragmentShader {
	int shaderId

	FragmentShader(ResourcesLoader loader, String resourceName) {
		def shaderCode = loader.getResource(resourceName)
		shaderId = glCreateShader(GL_FRAGMENT_SHADER)
		glShaderSource(shaderId, shaderCode)
		ShaderCompiler.compile(shaderId)
	}
}
