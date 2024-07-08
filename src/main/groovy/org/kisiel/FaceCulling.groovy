package org.kisiel

import static org.kisiel.FaceCulling.CullFaceMode.BACK
import static org.kisiel.FaceCulling.FrontFaceDirection.COUNTER_CLOCKWISE
import static org.lwjgl.opengl.GL11.*

class FaceCulling {
	boolean cullingEnabled
	CullFaceMode mode
	FrontFaceDirection direction

	private FaceCulling() {
		cullingEnabled = false
		mode = BACK
		direction = COUNTER_CLOCKWISE
	}

	enum CullFaceMode {
		FRONT (GL_FRONT),
		BACK (GL_BACK),
		FRONT_AND_BACK (GL_FRONT_AND_BACK)

		final int value

		private CullFaceMode(int value) {
			this.value = value
		}
	}

	enum FrontFaceDirection {
		CLOCKWISE (GL_CW),
		COUNTER_CLOCKWISE (GL_CCW)

		final int value
		private FrontFaceDirection(int value) {
			this.value = value
		}
	}

	static void faceCulling(Closure closure) {
		FaceCulling culling = new FaceCulling()
		closure.resolveStrategy = Closure.DELEGATE_ONLY
		closure.delegate = culling
		closure()
		if (culling.cullingEnabled) {
			glEnable(GL_CULL_FACE)
		} else {
			glDisable(GL_CULL_FACE)
		}
		glCullFace(culling.mode.value)
		glFrontFace(culling.direction.value)
	}
}
