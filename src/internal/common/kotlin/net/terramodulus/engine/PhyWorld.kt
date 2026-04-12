/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.engine

import net.terramodulus.engine.ferricia.Physics.newPhyWorld

class PhyWorld internal constructor(envHandle: ULong) {
	private val handle = newPhyWorld(envHandle)

	fun createGeomBox(lengths: DoubleArray) = PhyGeomBox(handle, lengths)
	fun createGeomSphere(radius: Double) = PhyGeomSphere(handle, radius)
}
