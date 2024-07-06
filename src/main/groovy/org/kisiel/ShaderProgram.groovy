package org.kisiel

import static org.lwjgl.opengl.GL11.GL_TRUE
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS
import static org.lwjgl.opengl.GL20.glAttachShader
import static org.lwjgl.opengl.GL20.glCreateProgram
import static org.lwjgl.opengl.GL20.glDeleteProgram
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog
import static org.lwjgl.opengl.GL20.glGetProgrami
import static org.lwjgl.opengl.GL20.glGetUniformLocation
import static org.lwjgl.opengl.GL20.glLinkProgram
import static org.lwjgl.opengl.GL20.glUniform1f
import static org.lwjgl.opengl.GL20.glUniform4fv
import static org.lwjgl.opengl.GL20.glUseProgram

import org.kisiel.exceptions.InvalidUniformLocation
import org.kisiel.exceptions.ShaderLinkerError
import org.kisiel.math.Vector4

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

	void delete() {
		glDeleteProgram(programId)
	}

	private int getLocation(String name) {
		def location = glGetUniformLocation(programId, name)
		if (location == -1) {
			throw new InvalidUniformLocation("${name} not found")
		}
		return location
	}

	void setUniform(String name, Vector4<Float> vector) {
		use()
		glUniform4fv(getLocation(name), [
			vector.x,
			vector.y,
			vector.z,
			vector.w
		] as float[])
	}

	void setUniform(String name, float value) {
		use()
		glUniform1f(getLocation(name), value)
	}
}
