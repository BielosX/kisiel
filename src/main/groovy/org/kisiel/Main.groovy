package org.kisiel

import static org.kisiel.Blending.WeightingFactor.ONE_MINUS_SOURCE_ALPHA
import static org.kisiel.Blending.WeightingFactor.SOURCE_ALPHA
import static org.kisiel.Blending.blending
import static org.kisiel.BufferClearer.clear
import static org.kisiel.FaceCulling.CullFaceMode.BACK
import static org.kisiel.FaceCulling.FrontFaceDirection.CLOCKWISE
import static org.kisiel.FaceCulling.faceCulling
import static org.kisiel.math.Vector4.vec4
import static org.lwjgl.glfw.GLFW.*

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
		def wood = new Texture(sampler, resourcesLoader.getResourceBytes("textures/wood.png"))
		def woodIndex = 0
		def moonIndex = 1
		wood.use(woodIndex)
		def moon = new Texture(sampler, resourcesLoader.getResourceBytes("textures/moon_overlay.png"))
		moon.use(moonIndex)
		def vertexShader = new VertexShader(resourcesLoader, "shaders/demo.vs")
		def fragmentShader = new FragmentShader(resourcesLoader, "shaders/demo.fs")
		def colors = [
			vec4(1.0f, 0.0f, 0.0f, 1.0f),
			vec4(0.0f, 1.0f, 0.0f, 1.0f),
			vec4(0.0f, 0.0f, 1.0f, 1.0f),
			vec4(1.0f, 1.0f, 1.0f, 1.0f),
		]
		def shaderProgram = new ShaderProgram(vertexShader, fragmentShader)
		shaderProgram.setUniform("firstTexture", woodIndex)
		shaderProgram.setUniform("secondTexture", moonIndex)
		def colorIndex = 0
		def random = new Random()
		def alpha = 50
		blending {
			enabled = true
			sourceWeightingFactor = SOURCE_ALPHA
			destinationWeightingFactor = ONE_MINUS_SOURCE_ALPHA
		}
		//shaderProgram.setUniform("color", colors[colorIndex])
		//shaderProgram.setUniform("alpha", alpha / 100)
		faceCulling {
			enabled = true
			mode = BACK
			direction = CLOCKWISE
		}
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
			clear {
				color = true
				depth = true
			}
			wood.use(woodIndex)
			moon.use(moonIndex)
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
