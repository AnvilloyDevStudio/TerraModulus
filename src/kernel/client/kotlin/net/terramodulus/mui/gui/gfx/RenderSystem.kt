/*
 * SPDX-FileCopyrightText: 2025-2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.gfx

import com.cout970.math.vec3.Vec3f
import net.terramodulus.core.TerraModulus
import net.terramodulus.core.getResourceAsBytes
import net.terramodulus.core.getResourceAsString
import net.terramodulus.engine.Canvas
import net.terramodulus.engine.GeomDrawable
import net.terramodulus.engine.MeshDrawable
import net.terramodulus.mui.gui.agim.ScreenManager
import net.terramodulus.mui.gui.agim.impl.GameplayScreen
import net.terramodulus.mui.gui.asd.AsdHandle

class RenderSystem internal constructor(private val core: TerraModulus, private val canvas: Canvas) {
	val handle: Handle = HandleImpl()
	private val texShaders = canvas.loadTexShaders(
		getResourceAsString("/gms_tex.vsh"),
		getResourceAsString("/gms_tex.fsh")
	)
	private val geoShaders = canvas.loadGeoShaders(
		getResourceAsString("/gms_geo.vsh"),
		getResourceAsString("/gms_geo.fsh")
	)
	val targetFps = 1000;

	sealed interface Handle {
		val canvasHandle: CanvasHandle

		fun loadTexture(path: String): UInt

		fun setBackgroundColor(red: Float, green: Float, blue: Float, alpha: Float)
	}

	inner class CanvasHandle internal constructor() {
		internal val canvas = this@RenderSystem.canvas
	}

	private inner class HandleImpl : Handle {
		override val canvasHandle = CanvasHandle()

		override fun loadTexture(path: String) = canvas.loadImage(getResourceAsBytes(path))

		override fun setBackgroundColor(red: Float, green: Float, blue: Float, alpha: Float) {
			canvas.setClearColor(red, green, blue, alpha)
		}
	}

	internal fun newGameplayScreen(pos: Vec3f) =
		{ managerHandle: ScreenManager.Handle, asdHandle: AsdHandle.Container, it: Handle ->
			GameplayScreen(core, canvas.createCamera(floatArrayOf(pos.x, pos.y, pos.z)), it, managerHandle, asdHandle)
		}

	internal fun renderGuiTex(drawable: MeshDrawable, texture: UInt) = canvas.renderGuiTex(drawable, texShaders, texture)

	internal fun renderGuiGeo(drawable: GeomDrawable) = canvas.renderGuiGeo(drawable, geoShaders)

	internal fun render() {

	}
}
