package org.kisiel

import java.util.stream.Stream

import static org.lwjgl.opengl.GL11.*
import static org.lwjgl.opengl.GL15.*
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

	void addSingleBufferFloatAttributes(Tuple2<Float[], Integer>... attributes) {
		glBindVertexArray(vertexArrayId)
		def bufferId = glGenBuffers()
		glBindBuffer(GL_ARRAY_BUFFER, bufferId)
		attributeBuffers.push(bufferId)
		def size = Arrays.stream(attributes)
		.map { x ->
			def first = x.getV1()
			first.size()
		}.reduce(0, { l, r ->  l + r })
		MemoryStack.stackPush().withCloseable { stack ->
			def buffer = stack.callocFloat(size)
			def first = attributes[0]
			def numberOfAttributes = first.getV1().size().intdiv(first.getV2())
			(0..<numberOfAttributes).each { i ->
				attributes.each { a ->
					def attributeSize = a.getV2()
					buffer.put(a.getV1() as float[], attributeSize * i, attributeSize)
				}
			}
			buffer = buffer.clear()
			glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW)
		}
		def offset = 0L
		def stride = Arrays.stream(attributes)
		.map { x -> x.getV2() }
		.reduce(0, { l, r -> l + r }) * Float.BYTES
		attributes.each { a ->
			def attributeSize = a.getV2()
			def bytes = attributeSize * Float.BYTES
			glVertexAttribPointer(index, attributeSize, GL_FLOAT, false, stride, offset)
			glEnableVertexAttribArray(index)
			index++
			offset += bytes
		}
	}

	void addFloatAttribute(float[] attribute, int size) {
		MemoryStack.stackPush().withCloseable { stack ->
			def buffer = stack.callocFloat(attribute.size())
					.put(attribute)
					.clear()
			addFloatAttribute(buffer, size)
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
			glDrawArrays(GL_TRIANGLES, 0, vertices)
		}
	}

	void destroy() {
		glDeleteBuffers(attributeBuffers + elementBufferId as int[])
		glDeleteVertexArrays(vertexArrayId)
	}
}
