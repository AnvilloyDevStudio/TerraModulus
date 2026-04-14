/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.engine

import net.terramodulus.engine.ferricia.Physics.addPhyBodyGeom
import net.terramodulus.engine.ferricia.Physics.getPhyBodyPos
import net.terramodulus.engine.ferricia.Physics.newMassSphereTotal
import net.terramodulus.engine.ferricia.Physics.newPhyBody
import net.terramodulus.engine.ferricia.Physics.setPhyBodyLinearVel
import net.terramodulus.engine.ferricia.Physics.setPhyBodyPos

class PhyBody internal constructor(worldHandle: ULong, mass: Mass) {
	private val handle: ULong = newPhyBody(worldHandle, mass.handle)
	sealed class Mass(internal val handle: ULong) {
		class SphereTotal(mass: Double, radius: Double) : Mass(newMassSphereTotal(mass, radius))
	}

	fun addGeom(geom: PhyGeom) = addPhyBodyGeom(handle, geom.handle)

	fun setPos(pos: Vec3D) = setPhyBodyPos(handle, pos.toArray())

	fun setLinearVel(vel: Vec3D) = setPhyBodyLinearVel(handle, vel.toArray())

	fun getPos() = getPhyBodyPos(handle)
}
