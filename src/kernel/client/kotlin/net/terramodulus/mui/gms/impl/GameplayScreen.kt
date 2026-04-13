/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gms.impl

import net.terramodulus.core.TerraModulus
import net.terramodulus.engine.Camera3D
import net.terramodulus.engine.PhyGeom
import net.terramodulus.engine.SimpleMesh3dGeomCube
import net.terramodulus.engine.SimpleMesh3dGeomSphere
import net.terramodulus.engine.WorldObjDrawable
import net.terramodulus.mui.gfx.GuiGeometry
import net.terramodulus.mui.gfx.GuiLine
import net.terramodulus.mui.gfx.RenderSystem
import net.terramodulus.mui.gms.Component
import net.terramodulus.mui.gms.Screen
import net.terramodulus.mui.gms.ScreenManager
import net.terramodulus.void.World
import java.io.File
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi

private fun getPathOfResource(path: String): String {
	return File(object {}.javaClass.getResource(path)!!.toURI()).absolutePath
}

private val RED = intArrayOf(255, 0, 0, 255)
private val GREEN = intArrayOf(0, 255, 0, 255)
private val BLUE = intArrayOf(0, 0, 255, 255)

@OptIn(ExperimentalUuidApi::class)
internal class GameplayScreen(private val core: TerraModulus, private val camera: Camera3D, renderSystemHandle: RenderSystem.Handle) : Screen() {
	private val geoShaders = camera.loadGeoShaders(
		getPathOfResource("/gwr_geo.vsh"),
		getPathOfResource("/gwr_geo.fsh"),
	)

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
		override fun wrapCube(phyGeom: PhyGeom, x: Double, y: Double, z: Double): VoidGeom {
			val color = randomColor()
			val drawable = SimpleMesh3dGeomCube(1F, color[0], color[1], color[2], color[3])
			drawable.updateModel(x.toFloat(), y.toFloat(), z.toFloat(), .5F, .5F, .5F, 1.0, .0, .0, .0)
			return VoidGeom(phyGeom, drawable)
		}

		private fun randomColor() = when (Random.nextInt(3)) {
			0 -> RED
			1 -> GREEN
			2 -> BLUE
			else -> throw AssertionError("Invalid color")
		}

		override fun wrapChar(phyGeom: PhyGeom): VoidGeom {
			val drawable = SimpleMesh3dGeomSphere(.5F, 255, 255, 255, 255)
			drawable.updateModel(0F, 1F, 0F, .5F, .5F, .5F, 1.0, .0, .0, .0)
			return VoidGeom(phyGeom, drawable)
		}
	}

	private inner class VoidGeom(override val phyGeom: PhyGeom, val drawable: WorldObjDrawable) : World.VoidGeom {
		override fun render() {
			renderGwrGeo(drawable)
		}
	}

	override fun update(renderSystem: RenderSystem, screenManager: ScreenManager) {

	}

	private inner class GameplayRenderer : Component() {
		@OptIn(ExperimentalUuidApi::class)
		override fun render(renderSystem: RenderSystem) {
			core.world?.objects?.values?.forEach { it.render() }
		}
	}

	internal fun renderGwrGeo(drawable: WorldObjDrawable) = camera.renderGwrGeo(drawable, geoShaders)

	override fun exit() {}
}
