/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.engine

import net.terramodulus.engine.ferricia.Physics.newWorldPhyGeomBox
import net.terramodulus.engine.ferricia.Physics.newWorldPhyGeomSphere
import net.terramodulus.engine.ferricia.Physics.setPhyRawGeomPlaceablePosition

sealed class PhyGeom(internal val handle: ULong) {
	fun setPosition(pos: DoubleArray) = setPhyRawGeomPlaceablePosition(handle, pos)
}

class PhyGeomBox(worldHandle: ULong, lengths: DoubleArray) : PhyGeom(newWorldPhyGeomBox(worldHandle, lengths))

class PhyGeomSphere(worldHandle: ULong, radius: Double) : PhyGeom(newWorldPhyGeomSphere(worldHandle, radius))
