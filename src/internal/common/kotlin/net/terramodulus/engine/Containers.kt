/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.engine

data class Quat(val w: Double, val i: Double, val j: Double, val k: Double) {
	fun toArray() = doubleArrayOf(w, i, j, k)
}

data class Vec3D(val x: Double, val y: Double, val z: Double) {
	companion object {
		/**
		 * @param array array containing 3 double values
		 */
		fun fromArray(array: DoubleArray) = Vec3D(array[0], array[1], array[2])
	}

	fun toArray() = doubleArrayOf(x, y, z)
}
