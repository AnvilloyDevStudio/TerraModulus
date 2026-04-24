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
import kotlin.random.Random
import kotlin.random.nextInt

class World(commander: Ymir) : Closeable {
	private val env = PhyEnv()
	private val world = env.createWorld()

	val objects = HashMap<ObjId, VoidGeom>()
	val mainSpace = world.newSpace()
	// Floor at y=-100
	val floor = world.createGeomPlane(doubleArrayOf(0.0, 1.0, 0.0, -100.0))

	init {
		floor.setBits(1u, 1u.inv())
		world.omitSpace(mainSpace)
		// Spawn point
		objects[ObjId.randomUnique(objects)] = commander.wrapCube(createCube(.0, .0, .0), .0, .0, .0)
		// Main Character
		objects[ObjId.randomUnique(objects)] = commander.wrapChar(
			world.newBody(PhyBody.Mass.SphereTotal(1.0, .5)).apply {
				addGeom(createGeomSphere(.5))
				setPos(Vec3D(0.0, 1.0, 0.0))
			}
		)
		// Test Objects
		randomCubes(commander).forEach { objects[ObjId.randomUnique(objects)] = it }
		// Running in parallel
		Thread {
			while(true) {
				tick()
				Thread.sleep(1000 / 20) // in 20 Hz
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

	// Source: https://en.wikipedia.org/wiki/Maze_generation_algorithm
	private fun randomCubes(commander: Ymir): ArrayList<VoidGeom> {
		val list = ArrayList<VoidGeom>()
		var i = 0
		val total = 10 * 10 * 2 * 2
		val interval = 5.0
		val max = 5 * 5 * 5 // 125 for each set
		val directions = arrayOf(
			Vec3D(1.0, 0.0, 0.0),
			Vec3D(-1.0, 0.0, 0.0),
			Vec3D(0.0, 1.0, 0.0),
			Vec3D(0.0, -1.0, 0.0),
			Vec3D(0.0, 0.0, 1.0),
			Vec3D(0.0, 0.0, -1.0),
		)
		for (x in 1..10) {
			for (z in 1..10) {
				for (xs in booleanArrayOf(false, true)) {
					for (zs in booleanArrayOf(false, true)) {
						println("Generating: ${++i}/$total")
						val xx = (if (xs) x else -x).toDouble() * interval
						val zz = (if (zs) z else -z).toDouble() * interval
						val origin = Vec3D(xx, Random.nextInt(-3..3).toDouble(), zz)
						val visited = mutableSetOf(Vec3D(0.0, 0.0, 0.0))
						val heads = ArrayDeque<Vec3D>()
						heads.addLast(Vec3D(0.0, 0.0, 0.0))
						while (!heads.isEmpty()) {
							val head = heads.removeFirst()
							for (d in directions) {
								val cur = head + d
								if (Random.nextInt(max) > visited.size && cur !in visited) {
									visited.add(cur)
									if (Random.nextInt(max) > visited.size) {
										heads.addLast(cur)
									}
								}
							}
						}
						for (p in visited) {
							val pt = origin + p
							list.add(commander.wrapCube(createCube(pt.x, pt.y, pt.z), pt.x, pt.y, pt.z))
						}
					}
				}
			}
		}
		return list
	}

	private operator fun Vec3D.plus(other: Vec3D): Vec3D = Vec3D(x + other.x, y + other.y, z + other.z)

	private fun createCube(x: Double, y: Double, z: Double): PhyGeomBox {
		val cube = createGeomBox(1.0, 1.0, 1.0)
		cube.setPosition(doubleArrayOf(x, y, z))
		cube.setBits(1u, 1u.inv())
		return cube
	}

	internal fun createGeomBox(x: Double, y: Double, z: Double) = mainSpace.createGeomBox(doubleArrayOf(x, y, z))
	internal fun createGeomSphere(radius: Double) = world.createGeomSphere(radius)

	fun tick() = world.tick()

	override fun close() {
		env.close()
	}
}
