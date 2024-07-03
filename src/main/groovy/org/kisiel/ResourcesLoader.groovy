package org.kisiel

import static java.nio.charset.StandardCharsets.UTF_8

import groovy.yaml.YamlSlurper

class ResourcesLoader {
	private def parser = new YamlSlurper()
	private def classLoader = ResourcesLoader.classLoader
	private Map<String, Object> resourcesStructure

	ResourcesLoader() {
		def metadata = classLoader.getResourceAsStream('metadata.yaml')
		def metadataStr = new String(metadata.readAllBytes(), UTF_8)
		resourcesStructure = parser.parseText(metadataStr) as Map<String, Object>
		println("metadata.yaml successfully loaded")
	}

	String getResource(String name) {
		return new String(classLoader.getResourceAsStream(name).readAllBytes(), UTF_8)
	}
}
