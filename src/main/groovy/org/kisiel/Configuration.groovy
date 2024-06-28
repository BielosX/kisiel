package org.kisiel

import groovy.transform.Immutable
import groovy.yaml.YamlSlurper

@Immutable
class WindowConfig {
	int width
	int height
	boolean fullscreen
	String title
}

@Immutable
class Configuration {
	WindowConfig window
}

class ConfigLoader {
	def private parser = new YamlSlurper()

	Configuration loadDefault() {
		def resource = ConfigLoader.classLoader.getResourceAsStream('default.yaml')
		return parser.parseText(new String(resource.readAllBytes())) as Configuration
	}
}
