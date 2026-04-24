/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gms.impl

import net.terramodulus.core.TerraModulus
import net.terramodulus.core.getPathOfResource
import net.terramodulus.engine.Camera3D
import net.terramodulus.engine.PhyBody
import net.terramodulus.engine.PhyGeom
import net.terramodulus.engine.Quat
import net.terramodulus.engine.Rgba
import net.terramodulus.engine.SimpleMesh3dGeomCube
import net.terramodulus.engine.SimpleMesh3dGeomSphere
import net.terramodulus.engine.Vec3D
import net.terramodulus.engine.Vec3F
import net.terramodulus.engine.WorldObjDrawable
import net.terramodulus.mui.gfx.Direction6C
import net.terramodulus.mui.gfx.RenderSystem
import net.terramodulus.mui.gfx.Vector3D
import net.terramodulus.mui.gms.Component
import net.terramodulus.mui.gms.Screen
import net.terramodulus.mui.gms.ScreenManager
import net.terramodulus.mui.input.InputSystem
import net.terramodulus.void.World
import kotlin.math.PI
import kotlin.math.sqrt
import kotlin.random.Random

private val WHITE = Rgba(255, 255, 255, 255)
private val RED = Rgba(255, 0, 0, 255)
private val GREEN = Rgba(0, 255, 0, 255)
private val BLUE = Rgba(0, 0, 255, 255)
private val STD_SCALE = Vec3F(.5F, .5F, .5F)
private val IDENT_ROT = Quat(1.0, .0, .0, .0)
private const val MASS = 1.0
private const val MAX_SPEED = PI * PI // reachable by autonomous movement
private const val MAX_ACC = PI * PI // without other forces, reaching MAX_SPEED in one second
private const val MOVE_EPSILON = .1 // smallest acc to apply

internal class GameplayScreen(private val core: TerraModulus, private val camera: Camera3D, renderSystemHandle: RenderSystem.Handle) : Screen() {
	private val geoShaders = camera.loadGeoShaders(
		getPathOfResource("/gwr_geo.vsh"),
		getPathOfResource("/gwr_geo.fsh"),
	)

	private lateinit var player: PlayerVoidGeom

	init {
		renderSystemHandle.setBackgroundColor(0F, 0F, 0F, 0F)
		core.world = World(Ymir())
		addComponent(GameplayRenderer())
// 		val progressBarEdge = GeomComponent(GuiLine(0, 100, 100, 100, 255, 255, 255, 255))
// 		addComponent(progressBarEdge)
// 		val progressBarCtnVal = GuiLine(0, 101, 0, 101, 255, 255, 0, 255)
// 		val progressBarCtn = GeomComponent(progressBarCtnVal)
// 		addComponent(progressBarCtn)
// 		class Tracker : World.ProgressTracker {
// 			override val progress: AtomicInteger = AtomicInteger(0)
// 			override val max: AtomicInteger = AtomicInteger(0)
// 			override fun update() {
// 				progressBarCtnVal.setPos(0, 101, 100 * progress.get() / max.get(), 101)
// 			}
// 		}
// 		Thread {
// 			core.world = World(Tracker(), Ymir())
// 			removeComponent(progressBarEdge)
// 			removeComponent(progressBarCtn)
// 			addComponent(GameplayRenderer())
// 		}.start()
	}

	private inner class Ymir : World.Ymir {
		override fun wrapCube(phyGeom: PhyGeom, x: Double, y: Double, z: Double): VoidGeom = EnvVoidGeom(phyGeom,
			SimpleMesh3dGeomCube(
				2F,
				randomColor(),
				Vec3F(x.toFloat(), y.toFloat(), z.toFloat()),
				STD_SCALE,
				IDENT_ROT,
			),
			Vec3D(x, y, z)
		)

		private fun randomColor() = when (Random.nextInt(3)) {
			0 -> RED
			1 -> GREEN
			2 -> BLUE
			else -> throw AssertionError("Invalid color")
		}

		override fun wrapChar(phyBody: PhyBody): VoidGeom {
			player = PlayerVoidGeom(phyBody,
				SimpleMesh3dGeomSphere(1F, WHITE, Vec3F(0F, 1F, 0F), STD_SCALE, IDENT_ROT)
			)
			return player
		}
	}

	private abstract inner class VoidGeom(val drawable: WorldObjDrawable) : World.VoidGeom {
		override fun render() {
			renderGwrGeo(drawable)
		}
	}

	private inner class EnvVoidGeom(override val phyGeom: PhyGeom, drawable: WorldObjDrawable, override val pos: Vec3D) :
		VoidGeom(drawable), World.EnvVoidGeom

	private inner class PlayerVoidGeom(override val phyBody: PhyBody, drawable: WorldObjDrawable) :
		VoidGeom(drawable), World.PlayerVoidGeom {
		fun move(dir: Vector3D) {
			if (dir == Vector3D.ZERO) return // avoid math errors and computations
			val dir = Vec3D(dir.x, dir.y, dir.z).normalize()
			val curVel = doubleArrayToVec3D(phyBody.getLinearVel())
			// Let d be the unit vector of autonomous movement target direction,
			//     v_c be the current velocity of body,
			//     v_p be the scalar projection of v_c on d.
			// v_p = v_c * d, may be negative
			// Autonomous acceleration is made only if v_p < MAX_SPEED.
			val projVel = curVel * dir
			if (projVel < MAX_SPEED) {
				// Let v_d be the delta velocity in direction of d,
				//     a_d be the delta acceleration to be made.
				// v_t = MAX_SPEED - v_p, must be positive
				// a_d = dir * clamp(v_t / 1 s, EPSILON, MAX)
				val deltaVel = MAX_SPEED - projVel
				val deltaAcc = dir * deltaVel.coerceIn(MOVE_EPSILON, MAX_ACC)
				phyBody.addForce(deltaAcc * MASS)
			}
		}

		override fun render() {
			val pos = doubleArrayToVec3F(phyBody.getPos())
			drawable.setPos(pos)
			camera.refreshPos(pos.toArray())
			super.render()
		}

		override val pos: Vec3D
			get() = doubleArrayToVec3D(phyBody.getPos())
	}

	private fun Vec3D.normalize(): Vec3D {
		val mag = mag()
		return Vec3D(x / mag, y / mag, z / mag)
	}

	private operator fun Vec3D.times(d: Double) = Vec3D(x * d, y * d, z * d)
	private operator fun Vec3D.minus(other: Vec3D) = Vec3D(x - other.x, y - other.y, z - other.z)
	// dot product
	private operator fun Vec3D.times(other: Vec3D) = x * other.x + y * other.y + z * other.z

	// dot product with itself
	private fun Vec3D.squared() = x * x + y * y + z * z
	// magnitude or length
	private fun Vec3D.mag() = sqrt(squared())

	private fun PhyBody.setLinearVel(vel: Vector3D) {
		setLinearVel(Vec3D(vel.x, vel.y, vel.z))
	}

	private fun doubleArrayToVec3F(pos: DoubleArray) = Vec3F(pos[0].toFloat(), pos[1].toFloat(), pos[2].toFloat())
	private fun doubleArrayToVec3D(pos: DoubleArray) = Vec3D(pos[0], pos[1], pos[2])

	private fun Direction6C.toKey() = when (this) {
		Direction6C.North -> InputSystem.Keys.W
		Direction6C.South -> InputSystem.Keys.S
		Direction6C.West -> InputSystem.Keys.A
		Direction6C.East -> InputSystem.Keys.D
		Direction6C.Up -> InputSystem.Keys.Space
		Direction6C.Down -> InputSystem.Keys.LShift
	}

	private fun Direction6C.toVector() = when (this) {
		Direction6C.North -> Vector3D(.0, .0, -1.0)
		Direction6C.South -> Vector3D(.0, .0, 1.0)
		Direction6C.West -> Vector3D(-1.0, .0, .0)
		Direction6C.East -> Vector3D(1.0, .0, .0)
		Direction6C.Up -> Vector3D(.0, 1.0, .0)
		Direction6C.Down -> Vector3D(.0, -1.0, .0)
	}

	override fun update(renderSystem: RenderSystem, screenManager: ScreenManager, inputSystem: InputSystem) {
		val dirs = ArrayList<Vector3D>()
		Direction6C.entries.forEach { if (inputSystem.condition { it.toKey().down() }) dirs.add(it.toVector()) }
		player.move(dirs.fold(Vector3D.ZERO, Vector3D::plus))
	}

	private inner class GameplayRenderer : Component() {
		override fun render(renderSystem: RenderSystem) {
			if (core.world != null) core.world!!.objects.values.sortedWith(
				compareBy<World.VoidGeom> { it.pos.y }.thenBy { it.pos.z }
			).forEach { it.render() }
		}
	}

	internal fun renderGwrGeo(drawable: WorldObjDrawable) = camera.renderGwrGeo(drawable, geoShaders)

	override fun exit() {}
}
