package org.kisiel

import static org.lwjgl.opengl.GL11.*
import static org.lwjgl.opengl.GL15.*
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import static org.lwjgl.opengl.GL20.glVertexAttribPointer
import static org.lwjgl.opengl.GL30.*

import java.nio.FloatBuffer
import java.nio.IntBuffer
import org.lwjgl.system.MemoryStack

class VertexArray {
	List<Integer> attributeBuffers = new ArrayList<>()
	int elementBufferId
	int vertexArrayId
	int vertices
	int indices
	int index = 0

	VertexArray() {
		vertexArrayId = glGenVertexArrays()
	}

	void addCoordinates(FloatBuffer buffer) {
		vertices = buffer.remaining().intdiv(3)
		addFloatAttribute(buffer, 3)
	}

	void addCoordinates(float[] coordinates) {
		MemoryStack.stackPush().withCloseable { stack ->
			def buffer = stack.callocFloat(coordinates.size())
					.put(coordinates)
					.clear()
			addCoordinates(buffer)
		}
	}

	void addFloatAttribute(FloatBuffer buffer, int size) {
		glBindVertexArray(vertexArrayId)
		def bufferId = glGenBuffers()
		glBindBuffer(GL_ARRAY_BUFFER, bufferId)
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW)
		glVertexAttribPointer(index, size, GL_FLOAT, false, size * Float.BYTES, 0L)
		glEnableVertexAttribArray(index)
		attributeBuffers.push(bufferId)
		index++
	}

	void addFloatAttribute(float[] attribute, int size) {
		MemoryStack.stackPush().withCloseable { stack ->
			def buffer = stack.callocFloat(attribute.size())
					.put(attribute)
					.clear()
			addFloatAttribute(buffer, size)
		}
	}

	void addTextureCoordinates(float[] coordinates) {
		addFloatAttribute(coordinates, 2)
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
			glDrawArrays(GL_TRIANGLES, 0, vertices)
		}
	}

	void destroy() {
		glDeleteBuffers(attributeBuffers + elementBufferId as int[])
		glDeleteVertexArrays(vertexArrayId)
	}
}
