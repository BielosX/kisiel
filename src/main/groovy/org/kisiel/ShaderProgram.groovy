package org.kisiel

import static org.lwjgl.opengl.GL11.GL_TRUE
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS
import static org.lwjgl.opengl.GL20.glAttachShader
import static org.lwjgl.opengl.GL20.glCreateProgram
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog
import static org.lwjgl.opengl.GL20.glGetProgrami
import static org.lwjgl.opengl.GL20.glLinkProgram
import static org.lwjgl.opengl.GL20.glUseProgram

import org.kisiel.exceptions.ShaderLinkerError

class ShaderProgram {
	int programId

	ShaderProgram(VertexShader vertexShader, FragmentShader fragmentShader) {
		programId = glCreateProgram()
		glAttachShader(programId, vertexShader.shaderId)
		glAttachShader(programId, fragmentShader.shaderId)
		glLinkProgram(programId)
		def status = glGetProgrami(programId, GL_LINK_STATUS)
		if (status != GL_TRUE) {
			def log = glGetProgramInfoLog(programId)
			throw new ShaderLinkerError(log)
		}
	}

	void use() {
		glUseProgram(programId)
	}
}
