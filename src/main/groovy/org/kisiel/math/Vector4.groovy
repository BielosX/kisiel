package org.kisiel.math

class Vector4<T> {
	T x
	T y
	T z
	T w

    static <K> Vector4<K> vec4(K x, K y, K z, K w) {
        def vector = new Vector4<K>()
        vector.x = x
        vector.y = y
        vector.z = z
        vector.w = w
        return vector
    }
}
