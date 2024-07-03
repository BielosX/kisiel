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
class OpenglVersion {
	int major
	int minor
}

@Immutable
class OpenglConfig {
	OpenglVersion version
}

@Immutable
class Configuration {
	WindowConfig window
	OpenglConfig opengl
}

class ConfigLoader {
	private def parser = new YamlSlurper()
	private ResourcesLoader resourceLoader

	ConfigLoader(ResourcesLoader loader) {
		resourceLoader = loader
	}

	Configuration loadDefault() {
		return parser.parseText(resourceLoader.getResource('default.yaml')) as Configuration
	}
}
