/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.engine

import net.terramodulus.engine.ferricia.Physics.newPhyCollisionManager
import net.terramodulus.engine.ferricia.Physics.newPhyWorld
import net.terramodulus.engine.ferricia.Physics.omitPhyCollisionManagerSpace
import net.terramodulus.engine.ferricia.Physics.processPhyCollisionManager
import net.terramodulus.engine.ferricia.Physics.tickPhyWorld

class PhyWorld internal constructor(envHandle: ULong) {
	private val handle = newPhyWorld(envHandle)
	private val cmHandle = newPhyCollisionManager()

	fun createGeomBox(lengths: DoubleArray) = PhyGeomBox.newWorld(handle, lengths)
	fun createGeomSphere(radius: Double) = PhyGeomSphere(handle, radius)
	fun createGeomPlane(params: DoubleArray) = PhyGeomPlane(handle, params)

	fun newSpace() = PhySpace(handle)
	fun newBody(mass: PhyBody.Mass) = PhyBody(handle, mass)

	fun omitSpace(space: PhySpace) = omitPhyCollisionManagerSpace(cmHandle, space.handle)

	fun tick() {
		tickPhyWorld(handle, cmHandle)
		processPhyCollisionManager(cmHandle, handle)
	}
}
