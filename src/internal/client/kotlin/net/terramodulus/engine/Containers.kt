/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.engine

data class Rgba(val r: Int, val g: Int, val b: Int, val a: Int) {
	fun toArray() = intArrayOf(r, g, b, a)
}

data class Vec3F(val x: Float, val y: Float, val z: Float) {
	fun toArray() = floatArrayOf(x, y, z)
}
