/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gfx

sealed interface Vector2<T: Number> {
	val x: T
	val y: T

	operator fun plus(other: Vector2<T>): Vector2<T>
}

data class Vector2I(val x: Int, val y: Int) {
	fun plus(other: Vector2I) = Vector2I(x + other.x, y + other.y)
}

data class Vector2D(val x: Double, val y: Double) {
	fun plus(other: Vector2D) = Vector2D(x + other.x, y + other.y)
}

data class Vector2F(val x: Float, val y: Float) {
	fun plus(other: Vector2F) = Vector2F(x + other.x, y + other.y)
}
