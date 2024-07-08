package org.kisiel

import static org.lwjgl.opengl.GL11.*
import static org.lwjgl.opengl.GL30.glGenerateMipmap
import static org.lwjgl.stb.STBImage.stbi_image_free
import static org.lwjgl.stb.STBImage.stbi_load_from_memory

import org.lwjgl.BufferUtils
import org.lwjgl.system.MemoryStack

class Texture {
	int textureId
	Sampler sampler
	int width
	int height

	Texture(Sampler sampler, byte[] image) {
		textureId = glGenTextures()
		this.sampler = sampler
		glBindTexture(GL_TEXTURE_2D, textureId)
		def buffer = BufferUtils.createByteBuffer(image.size())
		buffer.put(image)
		buffer.clear()
		MemoryStack.stackPush().withCloseable { stack ->
			def width = stack.callocInt(1)
			def height = stack.callocInt(1)
			def channels = stack.callocInt(1)
			def loadedImage = stbi_load_from_memory(buffer, width, height, channels, 0)
			this.width = width.get()
			this.height = height.get()
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, this.width, this.height, 0, GL_RGB, GL_UNSIGNED_BYTE, loadedImage)
			stbi_image_free(loadedImage)
		}
		glGenerateMipmap(GL_TEXTURE_2D)
	}

	void use() {
		sampler.bind(textureId)
		glBindTexture(GL_TEXTURE_2D, textureId)
	}
}
