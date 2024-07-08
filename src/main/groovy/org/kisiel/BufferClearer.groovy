package org.kisiel

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT
import static org.lwjgl.opengl.GL11.glClear

class BufferClearer {
	boolean color
	boolean depth
	private BufferClearer() {
		color = false
		depth = false
	}

	static void clear(Closure closure) {
		def clearer = new BufferClearer()
		closure.resolveStrategy = Closure.DELEGATE_ONLY
		closure.delegate = clearer
		closure()
		int mask = 0
		if (clearer.color) {
			mask |= GL_COLOR_BUFFER_BIT
		}
		if (clearer.depth) {
			mask |= GL_DEPTH_BUFFER_BIT
		}
		glClear(mask)
	}
}
