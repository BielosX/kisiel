package org.kisiel

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT
import static org.lwjgl.opengl.GL11.glClear

class BufferClearer {
	private int mask
	private BufferClearer() {
		mask = 0
	}

	static BufferClearer clearer() {
		return new BufferClearer()
	}

	BufferClearer color() {
		mask |= GL_COLOR_BUFFER_BIT
		return this
	}

	BufferClearer depth() {
		mask |= GL_DEPTH_BUFFER_BIT
		return this
	}

	void clear() {
		glClear(mask)
	}
}
