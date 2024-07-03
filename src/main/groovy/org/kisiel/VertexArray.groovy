package org.kisiel


import static org.lwjgl.opengl.GL11.GL_FLOAT
import static org.lwjgl.opengl.GL15.*
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import static org.lwjgl.opengl.GL20.glVertexAttribPointer
import static org.lwjgl.opengl.GL30.glBindVertexArray
import static org.lwjgl.opengl.GL30.glGenVertexArrays

import java.nio.FloatBuffer
import org.lwjgl.system.MemoryStack

class VertexArray {
	int vertexBufferId
	int vertexArrayId

	VertexArray() {
		vertexBufferId = glGenBuffers()
		vertexArrayId = glGenVertexArrays()
	}

	void addCoordinates(FloatBuffer buffer) {
		glBindVertexArray(vertexArrayId)
		glBindBuffer(GL_ARRAY_BUFFER, vertexBufferId)
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW)
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0L)
		glEnableVertexAttribArray(0)
	}

	void addCoordinates(float[] coordinates) {
		MemoryStack.stackPush().withCloseable { stack ->
			def buffer = stack.callocFloat(coordinates.size())
			buffer.put(coordinates)
			buffer.clear()
			addCoordinates(buffer)
		}
	}

	void use() {
		glBindVertexArray(vertexArrayId)
	}
}
