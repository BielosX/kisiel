package org.kisiel

import static org.lwjgl.opengl.GL11.GL_LINEAR
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR
import static org.lwjgl.opengl.GL11.GL_REPEAT
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T
import static org.lwjgl.opengl.GL33.glBindSampler
import static org.lwjgl.opengl.GL33.glGenSamplers
import static org.lwjgl.opengl.GL33.glSamplerParameteri

class Sampler {
	int samplerId

	Sampler() {
		samplerId = glGenSamplers()
		glSamplerParameteri(samplerId, GL_TEXTURE_WRAP_S, GL_REPEAT)
		glSamplerParameteri(samplerId, GL_TEXTURE_WRAP_T, GL_REPEAT)
		glSamplerParameteri(samplerId, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
		glSamplerParameteri(samplerId, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)
	}

	void bind(int textureId) {
		glBindSampler(textureId, samplerId)
	}
}
