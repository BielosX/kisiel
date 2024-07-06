package org.kisiel

import static org.kisiel.BufferClearer.clearer
import static org.kisiel.math.Vector4.vec4
import static org.lwjgl.glfw.GLFW.*
import static org.lwjgl.opengl.GL11.*

import java.time.Duration
import java.time.Instant

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
		def colors = [
			vec4(1.0f, 0.0f, 0.0f, 1.0f),
			vec4(0.0f, 1.0f, 0.0f, 1.0f),
			vec4(0.0f, 0.0f, 1.0f, 1.0f),
			vec4(1.0f, 1.0f, 1.0f, 1.0f),
		]
		def shaderProgram = new ShaderProgram(vertexShader, fragmentShader)
		def colorIndex = 0
		def random = new Random()
		def alpha = 50
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		shaderProgram.setUniform("color", colors[colorIndex])
		shaderProgram.setUniform("alpha", alpha / 100)
		shaderProgram.use()
		window.registerKeyCallback {w, key, scancode, action, mods ->
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
				glfwSetWindowShouldClose(w, true)
			}
		}

		window.show()
		def elapsed = 0
		window.untilClosed { long w ->
			if (elapsed > 1000) {
				elapsed -= 1000
				colorIndex = colorIndex == 3 ? 0 : colorIndex+1
				def color = colors[colorIndex]
				alpha = random.nextInt(51) + 50
				shaderProgram.setUniform("color", color)
				shaderProgram.setUniform("alpha", alpha / 100)
			}
			def before = Instant.now()
			clearer().color().depth().clear()
			shaderProgram.use()
			vertexArray.drawTriangles()
			secondTriangle.drawTriangles()
			plane.drawTriangles()
			glfwSwapBuffers(w)
			glfwPollEvents()
			def after = Instant.now()
			elapsed += Duration.between(before, after).toMillis()
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
