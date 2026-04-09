/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.engine

import net.terramodulus.engine.ferricia.Physics.newWorldPhyGeomBox
import net.terramodulus.engine.ferricia.Physics.setPhyRawGeomPlaceablePosition

class PhyGeomBox(worldHandle: ULong, lengths: Array<Double>) {
	private val handle: ULong = newWorldPhyGeomBox(worldHandle, lengths)

	fun setPosition(pos: Array<Double>) = setPhyRawGeomPlaceablePosition(handle, pos)
}
