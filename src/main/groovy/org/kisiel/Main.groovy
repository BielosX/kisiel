package org.kisiel

import static java.nio.charset.StandardCharsets.UTF_8
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import static org.lwjgl.glfw.GLFW.*
import static org.lwjgl.opengl.GL11.*
import static org.lwjgl.system.MemoryUtil.NULL

import groovy.yaml.YamlSlurper
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL

class Main {
	static void main(String[] args) {
		def parser = new YamlSlurper()
		def defaultConfig = Main.getClassLoader().getResourceAsStream("default.yaml")
		def config = parser.parseText(new String(defaultConfig.readAllBytes(), UTF_8)) as Configuration
		GLFWErrorCallback.createPrint(System.err).set()
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW")
		}
		glfwDefaultWindowHints()
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

		def monitor = config.window.fullscreen ? glfwGetPrimaryMonitor() : NULL
		def window = glfwCreateWindow(config.window.width, config.window.height, config.window.title, monitor, NULL)
		if (window == NULL) {
			throw new RuntimeException("Unable to create GLFW window")
		}

		glfwSetKeyCallback(window, {w, key, scancode, action, mods ->
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
				glfwSetWindowShouldClose(window, true)
			}
		})

		glfwMakeContextCurrent(window)
		glfwSwapInterval(1)
		glfwShowWindow(window)
		GL.createCapabilities()
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
		while (!glfwWindowShouldClose(window)) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
			glfwSwapBuffers(window)
			glfwPollEvents()
		}
		glfwFreeCallbacks(window)
		glfwDestroyWindow(window)
		glfwTerminate()
		glfwSetErrorCallback(null).free()
	}
}