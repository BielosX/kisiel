package org.kisiel

import static org.kisiel.Blending.WeightingFactor.ONE_MINUS_SOURCE_ALPHA
import static org.kisiel.Blending.WeightingFactor.SOURCE_ALPHA
import static org.lwjgl.opengl.GL11.GL_BLEND
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA
import static org.lwjgl.opengl.GL11.glBlendFunc
import static org.lwjgl.opengl.GL11.glDisable
import static org.lwjgl.opengl.GL11.glEnable

class Blending {
	boolean enabled
	WeightingFactor sourceWeightingFactor
	WeightingFactor destinationWeightingFactor

	private Blending() {
		enabled = false
		sourceWeightingFactor = SOURCE_ALPHA
		destinationWeightingFactor = ONE_MINUS_SOURCE_ALPHA
	}

	enum WeightingFactor {
		SOURCE_ALPHA (GL_SRC_ALPHA),
		ONE_MINUS_SOURCE_ALPHA (GL_ONE_MINUS_SRC_ALPHA)

		final int value

		WeightingFactor(int value) {
			this.value = value
		}
	}

	static void blending(Closure closure) {
		def blending = new Blending()
		closure.resolveStrategy = Closure.DELEGATE_ONLY
		closure.delegate = blending
		closure()
		if (blending.enabled) {
			glEnable(GL_BLEND)
			glBlendFunc(blending.sourceWeightingFactor.value, blending.destinationWeightingFactor.value)
		} else {
			glDisable(GL_BLEND)
		}
	}
}
