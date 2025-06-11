/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gfx

data class Vector2I(val x: Int, val y: Int) {
	companion object {
		val ZERO = Vector2I(0, 0)
	}

	operator fun plus(other: Vector2I) = Vector2I(x + other.x, y + other.y)
}

data class Vector2D(val x: Double, val y: Double) {
	companion object {
		val ZERO = Vector2D(.0, .0)
	}

	operator fun plus(other: Vector2D) = Vector2D(x + other.x, y + other.y)
}

data class Vector2F(val x: Float, val y: Float) {
	companion object {
		val ZERO = Vector2F(0F, 0F)
	}

	operator fun plus(other: Vector2F) = Vector2F(x + other.x, y + other.y)
}

data class Vector3I(val x: Int, val y: Int, val z: Int) {
	companion object {
		val ZERO = Vector3I(0, 0, 0)
	}

	operator fun plus(other: Vector3I) = Vector3I(x + other.x, y + other.y, z + other.z)
}

data class Vector3D(val x: Double, val y: Double, val z: Double) {
	companion object {
		val ZERO = Vector3D(.0, .0, .0)
	}

	operator fun plus(other: Vector3D) = Vector3D(x + other.x, y + other.y, z + other.z)
}

data class Vector3F(val x: Float, val y: Float, val z: Float) {
	companion object {
		val ZERO = Vector3F(0F, 0F, 0F)
	}

	operator fun plus(other: Vector3F) = Vector3F(x + other.x, y + other.y, z + other.z)
}
