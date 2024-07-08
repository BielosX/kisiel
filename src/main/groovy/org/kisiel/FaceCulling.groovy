package org.kisiel

import static org.lwjgl.opengl.GL11.*

class FaceCulling {
	boolean cullingEnabled
	int face
	int order

	private FaceCulling() {}

	static FaceCulling enabled() {
		FaceCulling culling = new FaceCulling()
		culling.cullingEnabled = true
		return culling
	}

	FaceCulling cullBack() {
		face = GL_BACK
		return this
	}

	FaceCulling clockwise() {
		order = GL_CW
		return this
	}

	void apply() {
		if (cullingEnabled) {
			glEnable(GL_CULL_FACE)
		} else {
			glDisable(GL_CULL_FACE)
		}
		glCullFace(face)
		glFrontFace(order)
	}
}
