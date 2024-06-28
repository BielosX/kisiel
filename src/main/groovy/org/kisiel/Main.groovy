package org.kisiel


import static org.lwjgl.glfw.GLFW.*
import static org.lwjgl.opengl.GL11.*

class Main {
	static void main(String[] args) {
		def loader = new ConfigLoader()
		def config = loader.loadDefault()
		def window = new Window(config)
		window.registerKeyCallback {w, key, scancode, action, mods ->
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
				glfwSetWindowShouldClose(w, true)
			}
		}

		window.show()
		window.untilClosed { w ->
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
			glfwSwapBuffers(w)
			glfwPollEvents()
		}

		window.destroy()
	}
}
