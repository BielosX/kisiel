package org.kisiel

import static org.lwjgl.opengl.GL11.GL_FLOAT
import static org.lwjgl.opengl.GL15.*
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import static org.lwjgl.opengl.GL20.glVertexAttribPointer
import static org.lwjgl.opengl.GL30.glBindVertexArray
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays
import static org.lwjgl.opengl.GL30.glGenVertexArrays

import java.nio.FloatBuffer
import java.nio.IntBuffer
import org.lwjgl.system.MemoryStack

class VertexArray {
	int vertexBufferId
	int elementBufferId
	int vertexArrayId
	int triangles
	int indices

	VertexArray() {
		vertexBufferId = glGenBuffers()
		vertexArrayId = glGenVertexArrays()
	}

	void addCoordinates(FloatBuffer buffer) {
		triangles = buffer.remaining().intdiv(3)
		glBindVertexArray(vertexArrayId)
		glBindBuffer(GL_ARRAY_BUFFER, vertexBufferId)
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW)
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0L)
		glEnableVertexAttribArray(0)
	}

	void addCoordinates(float[] coordinates) {
		MemoryStack.stackPush().withCloseable { stack ->
			def buffer = stack.callocFloat(coordinates.size())
					.put(coordinates)
					.clear()
			addCoordinates(buffer)
		}
	}

	void addIndices(IntBuffer buffer) {
		indices = buffer.remaining()
		elementBufferId = glGenBuffers()
		glBindVertexArray(vertexArrayId)
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementBufferId)
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW)
	}

	void addIndices(int[] indices) {
		MemoryStack.stackPush().withCloseable { stack ->
			def buffer = stack.callocInt(indices.size())
					.put(indices)
					.clear()
			addIndices(buffer)
		}
	}

	void use() {
		glBindVertexArray(vertexArrayId)
	}

	void drawTriangles() {
		use()
		if (elementBufferId != 0 && indices != 0) {
			glDrawElements(GL_TRIANGLES, indices, GL_UNSIGNED_INT, 0)
		} else {
			glDrawArrays(GL_TRIANGLES, 0, triangles)
		}
	}

	void destroy() {
		glDeleteBuffers([
			vertexBufferId,
			elementBufferId
		] as int[])
		glDeleteVertexArrays(vertexArrayId)
	}
}
