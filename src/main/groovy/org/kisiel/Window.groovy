package org.kisiel

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import static org.lwjgl.glfw.GLFW.*
import static org.lwjgl.opengl.GL11.glClearColor
import static org.lwjgl.system.MemoryUtil.NULL

import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWKeyCallbackI
import org.lwjgl.opengl.GL

class Window {
	private long window

	Window(Configuration config) {
		GLFWErrorCallback.createPrint(System.err).set()
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW")
		}
		glfwDefaultWindowHints()
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE)
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, config.opengl.version.major)
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, config.opengl.version.minor)
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)

		def monitor = config.window.fullscreen ? glfwGetPrimaryMonitor() : NULL
		window = glfwCreateWindow(config.window.width, config.window.height, config.window.title, monitor, NULL)
		if (window == NULL) {
			throw new RuntimeException("Unable to create GLFW window")
		}
		glfwMakeContextCurrent(window)
		glfwSwapInterval(1)
		GL.createCapabilities()
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
	}

	def show() {
		glfwShowWindow(window)
	}

	def registerKeyCallback(GLFWKeyCallbackI callback) {
		glfwSetKeyCallback(window, callback)
	}

	def untilClosed(Closure closure) {
		while (!glfwWindowShouldClose(window)) {
			closure(window)
		}
	}

	def destroy() {
		glfwFreeCallbacks(window)
		glfwDestroyWindow(window)
		glfwTerminate()
		glfwSetErrorCallback(null).free()
	}
}
