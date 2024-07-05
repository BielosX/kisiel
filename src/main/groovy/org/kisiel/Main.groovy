package org.kisiel

import static org.kisiel.BufferClearer.clearer
import static org.lwjgl.glfw.GLFW.*

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
			-1.0f,
			1.0f,
			0.0f,
			-0.7f,
			1.0f,
			0.0f,
			-0.8f,
			0.7f,
			0.0f
		] as float[])
		secondTriangle.addIndices([0, 1 , 2] as int[])
		def plane = new VertexArray()
		plane.addCoordinates([
			// top right
			1.0f,
			1.0f,
			0.0f,
			// bottom right
			1.0f,
			0.8f,
			0.0f,
			// bottom left
			0.8f,
			0.8f,
			0.0f,
			// top left
			0.8f,
			1.0f,
			0.0f
		] as float[])
		plane.addIndices([0, 1, 3, 1, 2 , 3] as int[])
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
			clearer().color().depth().clear()
			shaderProgram.use()
			vertexArray.drawTriangles()
			secondTriangle.drawTriangles()
			plane.drawTriangles()
			glfwSwapBuffers(w)
			glfwPollEvents()
		}

		[
			vertexArray,
			secondTriangle,
			plane
		].each { it -> it.destroy() }
		[vertexShader, fragmentShader].each { it -> it.delete() }
		shaderProgram.delete()
		window.destroy()
	}
}
