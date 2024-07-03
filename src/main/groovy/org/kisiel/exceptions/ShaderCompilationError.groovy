package org.kisiel.exceptions

class ShaderCompilationError extends RuntimeException {
	ShaderCompilationError(String message) {
		super(message)
	}
}
