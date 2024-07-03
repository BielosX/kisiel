package org.kisiel


import static org.lwjgl.glfw.GLFW.*
import static org.lwjgl.opengl.GL11.*

class Main {
	static void main(String[] args) {
		def resourcesLoader = new ResourcesLoader()
		def loader = new ConfigLoader(resourcesLoader)
		def config = loader.loadDefault()
		def window = new Window(config)
		def vertexArray = new VertexArray()
		vertexArray.addCoordinates([
			-0.5f,
			-0.5f,
			0.0f,
			0.5f,
			-0.5f,
			0.0f,
			0.0f,
			0.5f,
			0.0f
		] as float[])
		def secondTriangle = new VertexArray()
		secondTriangle.addCoordinates([
				-1.0f, 1.0f, 0.0f,
				-0.7f, 1.0f, 0.0f,
				-0.8f, 0.7f, 0.0f
		] as float[])
		def vertexShader = new VertexShader(resourcesLoader, "shaders/demo.vs")
		def fragmentShader = new FragmentShader(resourcesLoader, "shaders/demo.fs")
		def shaderProgram = new ShaderProgram(vertexShader, fragmentShader)
		shaderProgram.use()
		window.registerKeyCallback {w, key, scancode, action, mods ->
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
				glfwSetWindowShouldClose(w, true)
			}
		}

		window.show()
		window.untilClosed { long w ->
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
			shaderProgram.use()
			vertexArray.drawTriangles()
			secondTriangle.drawTriangles()
			glfwSwapBuffers(w)
			glfwPollEvents()
		}

		window.destroy()
	}
}
