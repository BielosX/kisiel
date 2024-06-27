package org.kisiel

import groovy.transform.Immutable

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
