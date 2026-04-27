/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.engine

import net.terramodulus.engine.ferricia.Physics.dropPhyEnv
import net.terramodulus.engine.ferricia.Physics.newPhyEnv
import java.io.Closeable

class PhyEnv : Closeable {
	private val handle = newPhyEnv()

	fun createWorld(): PhyWorld = PhyWorld(handle)

	override fun close() {
		dropPhyEnv(handle)
	}
}
