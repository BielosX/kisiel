package org.kisiel

import static org.lwjgl.opengl.GL20.*

class VertexShader extends Shader {
	VertexShader(ResourcesLoader loader, String resourceName) {
		loadShader(loader, resourceName, { glCreateShader(GL_VERTEX_SHADER) })
	}
}
