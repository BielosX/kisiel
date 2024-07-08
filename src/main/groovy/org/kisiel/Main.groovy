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
		def triangle = new VertexArray()
		triangle.addCoordinates([
			// top right
			0.5f,
			0.5f,
			0.0f,
			// bottom right
			0.5f,
			-0.5f,
			0.0f,
			// bottom left
			-0.5f,
			-0.5f,
			0.0f,
			// top left
			-0.5f,
			0.5f,
			0.0f,
		] as float[])
		triangle.addIndices([0, 1, 3, 1, 2, 3] as int[])
		triangle.addTextureCoordinates([
			1.0f,
			1.0f,
			1.0f,
			0.0f,
			0.0f,
			0.0f,
			0.0f,
			1.0f
		] as float[])
		def sampler = new Sampler()
		def texture = new Texture(sampler, resourcesLoader.getResourceBytes("textures/wood.png"))
		texture.use()
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
		//shaderProgram.setUniform("color", colors[colorIndex])
		//shaderProgram.setUniform("alpha", alpha / 100)
		FaceCulling.enabled()
				.cullBack()
				.clockwise()
				.apply()
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
				//shaderProgram.setUniform("color", color)
				//shaderProgram.setUniform("alpha", alpha / 100)
			}
			def before = Instant.now()
			clearer().color().depth().clear()
			texture.use()
			shaderProgram.use()
			triangle.drawTriangles()
			glfwSwapBuffers(w)
			glfwPollEvents()
			def after = Instant.now()
			elapsed += Duration.between(before, after).toMillis()
		}
		[vertexShader, fragmentShader].each { it -> it.delete() }
		shaderProgram.delete()
		window.destroy()
	}
}
