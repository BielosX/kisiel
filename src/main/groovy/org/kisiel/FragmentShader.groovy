package org.kisiel

import static org.lwjgl.opengl.GL20.*

class FragmentShader extends Shader {
	FragmentShader(ResourcesLoader loader, String resourceName) {
		loadShader(loader, resourceName, { return glCreateShader(GL_FRAGMENT_SHADER) })
	}
}
