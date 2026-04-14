/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.void

import net.terramodulus.engine.PhyBody
import net.terramodulus.engine.PhyEnv
import net.terramodulus.engine.PhyGeom
import net.terramodulus.engine.PhyGeomBox
import net.terramodulus.engine.Vec3D
import java.io.Closeable
import kotlin.math.abs
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class World(commander: Ymir) : Closeable {
	private val env = PhyEnv()
	private val world = env.createWorld()

	val objects = HashMap<ObjId, VoidGeom>()

	init {
		// Spawn point
		objects[ObjId.randomUnique(objects)] = commander.wrapCube(createCube(.0, .0, .0), .0, .0, .0)
		// Main Character

		objects[ObjId.randomUnique(objects)] = commander.wrapChar(
			world.newBody(PhyBody.Mass.SphereTotal(1.0, .5)).apply { addGeom(createGeomSphere(.5)) }
		)
		// Test Objects
		randomCubes(commander).forEach { objects[ObjId.randomUnique(objects)] = it }
		// Running in parallel
		Thread {
			while(true) {
				tick()
				Thread.sleep(1000 / 20)
			}
		}.start()
	}

	interface Ymir {
		fun wrapCube(phyGeom: PhyGeom, x: Double, y: Double, z: Double): VoidGeom

		/** Always at (0, 1, 0) */
		fun wrapChar(phyBody: PhyBody): VoidGeom
	}

	/** A wrapper containing rendering context, with a geom of dimensions of 1mx1mx1m */
	interface VoidGeom {
		fun render()

		val pos: Vec3D
	}

	interface EnvVoidGeom : VoidGeom {
		val phyGeom: PhyGeom
	}

	interface PlayerVoidGeom : VoidGeom {
		val phyBody: PhyBody
	}

	private fun randomCubes(commander: Ymir): ArrayList<VoidGeom> {
		val list = ArrayList<VoidGeom>()
		var i = 0
		for (y in -3..3) {
			println("Generating: ${++i}/7")
			for (x in 1..20) {
				for (z in 1..20) {
					for (xs in booleanArrayOf(false, true)) {
						for (zs in booleanArrayOf(false, true)) {
							val xx = (if (xs) x else -x).toDouble();
							val zz = (if (zs) z else -z).toDouble();
							if (y == 0 || Random.nextInt(abs(y) * 10 + 10) == 0) {
								list.add(commander.wrapCube(createCube(xx, y.toDouble(), zz), xx, y.toDouble(), zz))
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

	fun tick() = world.tick()

	override fun close() {
		env.close()
	}
}
