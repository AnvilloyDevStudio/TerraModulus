/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.engine

import net.terramodulus.engine.ferricia.Physics.newPhyWorldSpace

class PhySpace internal constructor(worldHandle: ULong) {
	internal val handle = newPhyWorldSpace(worldHandle)

	fun createGeomBox(lengths: DoubleArray) = PhyGeomBox.newSpace(handle, lengths)
}
