/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.void

import net.terramodulus.engine.PhyEnv
import net.terramodulus.engine.PhyGeom
import net.terramodulus.engine.PhyGeomBox
import java.io.Closeable
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.abs
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class World(commander: Ymir) : Closeable {
	private val env = PhyEnv()
	private val world = env.createWorld()

	val objects = HashMap<Uuid, VoidGeom>()

	init {
		randomCubes(commander).forEach { objects[Uuid.random()] = it }
	}

	interface Ymir {
		fun wrapCube(phyGeom: PhyGeom, x: Double, y: Double, z: Double): VoidGeom

		/** Always at (0, 1, 0) */
		fun wrapChar(phyGeom: PhyGeom): VoidGeom
	}

	/** A wrapper containing rendering context, with a geom of dimensions of 1mx1mx1m */
	interface VoidGeom {
		val phyGeom: PhyGeom

		fun render()
	}

	private fun randomCubes(commander: Ymir): ArrayList<VoidGeom> {
		val list = ArrayList<VoidGeom>()
		// Spawn point
		list.add(commander.wrapCube(createCube(.0, .0, .0), .0, .0, .0))
		// Main Character
		list.add(commander.wrapChar(createGeomSphere(.5)))
		// Test Objects
		for (z in -5..5) {
			println(z)
			for (x in 1..10) {
				for (y in 1..10) {
					for (xs in booleanArrayOf(false, true)) {
						for (ys in booleanArrayOf(false, true)) {
							val xx = (if (xs) x else -x).toDouble();
							val yy = (if (ys) y else -y).toDouble();
							if (z == 0 || Random.nextInt(abs(z)) == 0) {
								list.add(commander.wrapCube(createCube(xx, yy, z.toDouble()), xx, yy, z.toDouble()))
							}
						}
					}
				}
			}
		}
		return list
	}

	private fun createCube(x: Double, y: Double, z: Double): PhyGeomBox {
		val cube = createGeomBox(1.0, 1.0, 1.0)
		cube.setPosition(doubleArrayOf(x, y, z))
		return cube
	}

	internal fun createGeomBox(x: Double, y: Double, z: Double) = world.createGeomBox(doubleArrayOf(x, y, z))
	internal fun createGeomSphere(radius: Double) = world.createGeomSphere(radius)

	override fun close() {
		env.close()
	}
}
